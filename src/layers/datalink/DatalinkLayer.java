package layers.datalink;

import layers.application.ApplicationLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
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

    private int sendingDelay = 1000;
    private boolean connected = false;


    // TODO РќСѓР¶РµРЅ ApplicationLayer
    public DatalinkLayer(ApplicationLayer applicationLayer) {
        this.physicalLayer = new PhysicalLayer(this);
        this.applicationLayer = applicationLayer;
    }

    // TODO удалить
    public DatalinkLayer() {
        this.physicalLayer = new PhysicalLayer(this);
        this.applicationLayer = null;
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
     * РџСЂРµРѕР±СЂР°Р·СѓРµС‚ РїР°РєРµС‚С‹ РїСЂРёРєР»Р°РґРЅРѕРіРѕ СѓСЂРѕРІРЅСЏ РІ РєР°РґСЂС‹ РєР°РЅР°Р»СЊРЅРѕРіРѕ СѓСЂРѕРІРЅСЏ Рё РєР»Р°РґС‘С‚ РёС… РІ РѕС‡РµСЂРµРґСЊ.
     *  Р’ РґСЂСѓРіРѕРј РїРѕС‚РѕРєРµ РїСЂРѕРёСЃС…РѕРґРёС‚ РѕС‚РїСЂР°РІРєР° РєР°РґСЂРѕРІ РЅР° С„РёР·РёС‡РµСЃРєРёР№ СѓСЂРѕРІРµРЅСЊ.
     * @param object РЎРµСЂРёР°Р»РёР·СѓРµРјС‹Р№ РѕР±СЉРµРєС‚, РїР°РєРµС‚ РїСЂРёРєР»Р°РґРЅРѕРіРѕ СѓСЂРѕРІРЅСЏ.
     */
    public synchronized void send(Object object) {
        byte[] data = new byte[0];
        try {
            data = serialize(object);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "РћС€РёР±РєР° СЃРµСЂРёР°Р»РёР·Р°С†РёРё РѕР±СЉРµРєС‚Р°", e);
        }

        // Р”СЂРѕР±РёРј РґР°РЅРЅС‹Рµ РЅР° РєР°РґСЂС‹
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


    public void connect(ComPortSettings settings) {
        getLowerLayer().connect(settings);
        sendAck.set(false);
        sendRet.set(false);
        permissionToTransmit.set(true);
        connected = true;
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
         * РџРѕРєР° РµСЃС‚СЊ СЃРѕРµРґРёРЅРµРЅРёРµ, РїСЂРѕРІРµСЂСЏРµРј РѕС‡РµСЂРµРґСЊ framesToSend
         * Р•СЃР»Рё РѕС‡РµСЂРµРґСЊ РЅРµ РїСѓСЃС‚Р°, С‚Рѕ РѕС‚РїСЂР°РІР»СЏРµРј РїР°РєРµС‚ Рё Р¶РґС‘Рј ACK
         *      Р•СЃР»Рё РїСЂРёС€С‘Р» RET, Р»РёР±Рѕ TIMEOUT РёСЃС‚С‘Рє, С‚Рѕ РїРѕРІС‚РѕСЂСЏРµРј РѕС‚РїСЂР°РІРєСѓ РєР°РґСЂР°
         */
        while(connected) {
            if (permissionToTransmit.get()) {
                if (sendRet.get()) {
                    getLowerLayer().send(Frame.newRETFrame().serialize());
                    sendRet.set(false);
                    permissionToTransmit.set(false);
                }

                else if (sendAck.get()) {
                    getLowerLayer().send(Frame.newACKFrame().serialize());
                    sendAck.set(false);
                    permissionToTransmit.set(false);
                }

                else if (!framesToSend.isEmpty()) {
                    getLowerLayer().send(framesToSend.peek().serialize());
                    permissionToTransmit.set(false);
                }
            }

            try {
                Thread.sleep(sendingDelay);
            } catch (InterruptedException e) {
                // TODO
                LOGGER.log(Level.SEVERE, "Interrupt Exception in Thread.sleep(sendingDelay)", e);
            }
        }
    }

    /**
     * РџРѕР»СѓС‡Р°РµС‚ РєР°РґСЂС‹ СЃ С„РёР·РёС‡РµСЃРєРѕРіРѕ СѓСЂРѕРІРЅСЏ
     * @param data РњР°СЃСЃРёРІ Р±Р°Р№С‚ (РєР°РґСЂ) СЃ С„РёР·РёС‡РµСЃРєРѕРіРѕ СѓСЂРѕРІРЅСЏ
     */
    public void receive(byte[] data) {
        Frame frame = Frame.deserialize(data);


        if (frame.isCorrect()) {
            System.out.println("Type frame: " + frame.getType().name());
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
        // Р�Р· СЃРїРёСЃРєР° receivedFrames РґРѕСЃС‚Р°С‘Рј РІСЃРµ СЌР»РµРјРµРЅС‚С‹ Рё СЃРѕР±РёСЂР°РµРј РёС… РІ РѕРґРёРЅ РѕР±СЉРµРєС‚

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

            // TODO РѕС‚РїСЂР°РІР»СЏРµРј object РїСЂРёРєР»Р°РґРЅРѕРјСѓ СѓСЂРѕРІРЅСЋ
            System.out.println((String) object);
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

}
