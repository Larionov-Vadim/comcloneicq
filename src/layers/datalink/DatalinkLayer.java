package layers.datalink;

import layers.application.ApplicationLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import messages.Messages;
import messages.TestSerialize;
import sun.rmi.runtime.Log;
import utils.Utils;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vadim on 19.04.2015.
 */
public class DatalinkLayer implements Runnable {
    private static Logger LOGGER = Logger.getLogger(DatalinkLayer.class.getName());
    // TODO ApplicationLayer
    private PhysicalLayer physicalLayer;
    private ApplicationLayer applicationLayer;

    private Queue<Frame> framesToSend = new ConcurrentLinkedQueue<>();
    private List<Frame> receivedFrames = new LinkedList<>();

    private AtomicBoolean sendAck = new AtomicBoolean(false);
    private AtomicBoolean sendRet = new AtomicBoolean(false);
    private AtomicBoolean permissionToTransmit = new AtomicBoolean(true);

    private boolean connected = false;
    private boolean threadRun = false;

    /** Задержка в мс между отправкой каждого кадра*/
    private int sendingDelay = 100;


    // TODO РќСѓР¶РµРЅ ApplicationLayer
    public DatalinkLayer(ApplicationLayer applicationLayer) {
        this.physicalLayer = new PhysicalLayer(this);
        this.applicationLayer = applicationLayer;
    }

    // TODO удалить

    /** Если не придёт подтверждение ACK на кадр INFO, то кадр INFO
     * будет отправлен повторно через время = sendingDelay*waitingCycles */
    private static final int initWaitingCycles = 2000;
    private int waitingCycles = initWaitingCycles;

    private static final int initMaxFails = 5;
    private int fails = initMaxFails;
    private AtomicBoolean repeatSend = new AtomicBoolean(false);

    // TODO Нужен ApplicationLayer
/*>>>>>>> 322118ecec71bc0899fa2aa2e218032cad7480c9
    public DatalinkLayer() {
        this.physicalLayer = new PhysicalLayer(this);
        this.applicationLayer = null;
    }*/

    private byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    /**
     * Преобразует пакеты прикладного уровня в кадры канального уровня и кладёт их в очередь.
     *  В другом потоке происходит отправка кадров на физический уровень.
     * @param object Сериализуемый объект, пакет прикладного уровня.
     */
    public synchronized void send(Object object) {
        byte[] data = new byte[0];
        try {
            data = serialize(object);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка сериализации объекта", e);
        }

        // Дробим данные на кадры
        int chunkSize = Frame.MAX_DATA_SIZE;
        int countFrames = data.length / chunkSize + (data.length % chunkSize == 0 ? 0 : 1);
        boolean flagFinal = false;
        for (int i = 0; i < countFrames; ++i) {
            int fromIndex = i * chunkSize;
            int toIndex = (data.length < (i + 1) * chunkSize ? data.length : (i + 1 ) * chunkSize);
            if (toIndex == data.length)
                flagFinal = true;

            byte[] currentData = Arrays.copyOfRange(data, fromIndex, toIndex);
            Frame frame = new Frame(Frame.Type.I, currentData, flagFinal);
            framesToSend.add(frame);
        }
    }


    public boolean connect(ComPortSettings settings) {
        if (!getLowerLayer().connect(settings))
            return false;
        sendAck.set(false);
        sendRet.set(false);
        permissionToTransmit.set(true);
        connected = getLowerLayer().isConnected();
        threadRun = true;
        Thread sendingThread = new Thread(this);
        sendingThread.start();
        return true;
    }

    public void disconnect() {
        setDisconnectParams();
        getLowerLayer().disconnect();
    }

    private void setDisconnectParams() {
        connected = false;
        threadRun = false;
        permissionToTransmit.set(false);
        framesToSend.clear();
        sendAck.set(false);
        sendRet.set(false);
    }


    public PhysicalLayer getLowerLayer() {
        return physicalLayer;
    }

    @Override
    public void run() {
        /**
         * Пока есть соединение, проверяем очередь framesToSend
         * Если очередь не пуста, то отправляем пакет и ждём ACK
         *      Если пришёл RET, либо TIMEOUT истёк, то повторяем отправку кадра
         */
        while(threadRun) {
            if (permissionToTransmit.get() && getLowerLayer().readyToSend()) {
                if (sendRet.get()) {
                    getLowerLayer().send(Frame.newRETFrame().serialize());
                    sendRet.set(false);
                    permissionToTransmit.set(false);
                }

                else if (sendAck.get()) {
                    getLowerLayer().send(Frame.newACKFrame().serialize());
                    sendAck.set(false);
                    permissionToTransmit.set(true);
                }

                else if (!framesToSend.isEmpty()) {
                    byte[] bytes = framesToSend.peek().serialize();
                    getLowerLayer().send(bytes);
                    permissionToTransmit.set(false);
                    repeatSend.set(true);
                }
            }

            try {
                Thread.sleep(sendingDelay);

                if (repeatSend.get()) {
                    --waitingCycles;
                    if (waitingCycles <= 0) {
                        --fails;
                        if (fails <= 0) {
                            LOGGER.warning("Разрыв соединения в связи с периодическим неполучением ACK-кадров");
                            notifyOnMessage(Messages.DISCONNECTED);
                            disconnect();
                            return;
                        }
                        LOGGER.info("Разрешение на переотправку кадра");
                        permissionToTransmit.set(true);
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Обработанное исключение", e);
            }
        }
    }

    /**
     * Получает кадры с физического уровня
     * @param data Массив байт (кадр) с физического уровня
     */
    public void receive(byte[] data) {
        Frame frame = Frame.deserialize(data);

        if (frame.isCorrect()) {
            // System.out.println("Type frame: " + frame.getType().name());
            switch (frame.getType()){
                case ACK:
                    repeatSend.set(false);
                    fails = initMaxFails;
                    waitingCycles = initWaitingCycles;

                    framesToSend.poll();
                    permissionToTransmit.set(true);
                    break;
                case RET:
                    permissionToTransmit.set(true);
                    break;
                case I:
                    receivedInfoFrame(frame);
                    sendAck.set(true);
                    permissionToTransmit.set(true);
                    break;
                default:
                    LOGGER.warning("РљР°РґСЂ РЅРµРёР·РІРµСЃС‚РЅРѕРіРѕ С‚РёРїР°");
            }
        }
        else {
            LOGGER.log(Level.INFO, "РљР°РґСЂ РёСЃРїРѕСЂС‡РµРЅ");
            sendRet.set(true);
            permissionToTransmit.set(true);
        }
    }

    private void receivedInfoFrame(Frame frame) {
        if (frame.isFinalFrame()) {
            byte[] data = new byte[0];
            for(Frame f : receivedFrames)
                data = Utils.concatenate(data,f.getData());
            data = Utils.concatenate(data, frame.getData());

            receivedFrames.clear();

            Object object = null;
            try {
                object = deserialize(data);
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "РћС€РёР±РєР° РґРµСЃРµСЂРёР°Р»РёР·Р°С†РёРё РѕР±СЉРµРєС‚Р°", e);
            }

            // TODO отправляем object прикладному уровню


            System.out.println("Output");

            //System.out.println((String) object);
            applicationLayer.getLinkToAppl().takeSomething(object);
/*=======
            if (object instanceof String)
                System.out.println((String) object);
            else if (object instanceof TestSerialize)
                ((TestSerialize) object).print();
>>>>>>> 322118ecec71bc0899fa2aa2e218032cad7480c9*/
        }
        else {
            receivedFrames.add(frame);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setSendingDelay(int sendingDelay) {
        this.sendingDelay = sendingDelay;
    }

    public int getSendingDelay() {
        return sendingDelay;
    }

    public void notifyOnMessage(Messages message) {
        System.out.println("notifyOnMessage: " + message.name());
        switch (message) {
            case CONNECTED:
                LOGGER.info("Connected");
                setConnected(true);
                break;
            case DISCONNECTED:
                LOGGER.info("Disconnected");
                setDisconnectParams();
                break;
        }
        // TODO
        // getUpperLayer.notifyOnMessage(message);
    }
}
