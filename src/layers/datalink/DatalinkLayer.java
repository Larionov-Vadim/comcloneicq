package layers.datalink;

import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import messages.Messages;
import messages.TestSerialize;
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

    private Queue<Frame> framesToSend = new ConcurrentLinkedQueue<>();
    private List<Frame> receivedFrames = new LinkedList<>();

    private AtomicBoolean sendAck = new AtomicBoolean(false);
    private AtomicBoolean sendRet = new AtomicBoolean(false);
    private AtomicBoolean permissionToTransmit = new AtomicBoolean(true);

    private boolean connected = false;
    private boolean threadRun = false;

    private int sendingDelay = 100;

    // TODO Нужен ApplicationLayer
    public DatalinkLayer() {
        this.physicalLayer = new PhysicalLayer(this);
    }

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
            // System.out.println("permissionToTransmit: " + permissionToTransmit.get());
            if (permissionToTransmit.get()) {
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
                }
            }

            try {
                Thread.sleep(sendingDelay);
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
                    LOGGER.warning("Кадр неизвестного типа");
            }
        }
        else {
            LOGGER.log(Level.INFO, "Кадр испорчен");
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
                LOGGER.log(Level.SEVERE, "Ошибка десериализации объекта", e);
            }

            // TODO отправляем object прикладному уровню
            if (object instanceof String)
                System.out.println((String) object);
            else if (object instanceof TestSerialize)
                ((TestSerialize) object).print();
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
