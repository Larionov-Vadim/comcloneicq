package layers.datalink;

import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import org.apache.commons.lang3.SerializationUtils;
import utils.Utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Vadim on 19.04.2015.
 */
public class DatalinkLayer implements Runnable {
    // TODO ApplicationLayer
    private PhysicalLayer physicalLayer = new PhysicalLayer();

    private Queue<Frame> framesToSend = new ConcurrentLinkedQueue<>();

    private List<Frame> receivedFrames = new LinkedList<>();

    private AtomicBoolean sendAck = new AtomicBoolean(false);
    private AtomicBoolean sendRet = new AtomicBoolean(false);
    private AtomicBoolean permissionToTransmit = new AtomicBoolean(true);

    private int sendingDelay = 1000;
    private boolean connected = false;

    /**
     * Преобразует пакеты прикладного уровня в кадры канального уровня и кладёт их в очередь
     *  В другом потоке происходит отправка кадров на физический уровень
     * @param object Сериализуемый объект, пакет прикладного уровня
     */
    public void send(Serializable object) {
        byte[] data = SerializationUtils.serialize(object);

        // Дробим данные на кадры
        int chunkSize = Frame.MAX_DATA_SIZE;
        int countFrames = data.length / chunkSize + (data.length % chunkSize != 0 ? 1 : 0);
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


    public void connect(ComPortSettings settings) {
        // TODO Можно сделать предварительный disconnect
        getLowerLayer().connect(settings);
        connected = true;
        permissionToTransmit.set(true);
        sendAck.set(false);
        sendRet.set(false);
        Thread sendingThread = new Thread(this);
        sendingThread.start();
    }

    public void disconnect() {
        getLowerLayer().disconnect();
        connected = false;
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
        // TODO ЧИСТИТЬ ОЧЕРЕДИ!
        // TODO Peek достаёт элемент, но не удаляет его
        while(connected) {
            if (permissionToTransmit.get()) {
                if (sendRet.get()) {
                    getLowerLayer().send(Frame.newRETFrame().serialize());
                    sendRet.set(false);
                }

                else if (sendAck.get()) {
                    getLowerLayer().send(Frame.newACKFrame().serialize());
                    sendAck.set(false);
                }

                else if (!framesToSend.isEmpty()) {
                    getLowerLayer().send(framesToSend.peek().serialize());
                }

                permissionToTransmit.set(false);
            }

            try {
                Thread.sleep(sendingDelay);
            } catch (InterruptedException e) {
                // TODO magic?
                e.printStackTrace();
            }
        }
    }

    /**
     * Получает кадры с физического уровня
     * @param data Массив байт (кадр) с физического уровня
     */
    // TODO может synchorized?
    public void receive(byte[] data) {
        // TODO
        Frame frame = Frame.deserialize(data);

        if (frame.isCorrect()) {
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
                    // TODO Unreacheble statement, пишем в лог
            }
        }
        else {
            sendRet.set(true);
            permissionToTransmit.set(true);
        }
    }

    private void receivedInfoFrame(Frame frame) {
        // Из списка receivedFrames достаём все элементы и собираем их в один объект
        if (frame.isFinalFrame()) {
            byte[] data = new byte[0];
            for(Frame f : receivedFrames)
                data = Utils.concatenate(data,f.getData());
            data = Utils.concatenate(data, frame.getData());
            receivedFrames.clear();
            Serializable object = SerializationUtils.deserialize(data);
            // TODO создаём объект и отправляем его прикладному уровню
        }
        else {
            receivedFrames.add(frame);
        }
    }


    public boolean isConnected() {
        return getLowerLayer().isConnected();
    }

    public void setSendingDelay(int sendingDelay) {
        this.sendingDelay = sendingDelay;
    }

    public int getSendingDelay() {
        return sendingDelay;
    }
//    public static void main(String[] args) {
//        String str = "Hello World! Yo! Hellooo!";
//        System.out.println(str.length());
//        DatalinkLayer datalinkLayer = new DatalinkLayer();
//        datalinkLayer.send(str.getBytes());
//
//        System.out.println(datalinkLayer.framesToSend.size());
//
//        List<Frame> frames = new ArrayList<>();
//        while (!datalinkLayer.framesToSend.isEmpty()) {
//            frames.add(datalinkLayer.framesToSend.poll());
//        }
//
//        System.out.println();
//        // frames.stream().forEach(s -> System.out.println("isFinal: " + s.isFinalFrame()));
//        ArrayList<byte[]> b = new ArrayList<>();
//
//        byte[] result = new byte[0];
//        for (Frame frame : frames) {
//            System.out.println("isFinal: " + frame.isFinalFrame());
//            result = Utils.concatenate(result, frame.getData());
//        }
//        System.out.println(new String(result));
//    }
}
