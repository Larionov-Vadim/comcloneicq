package layers.datalink;

import exceptions.DecodeException;
import utils.Utils;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Vadim on 20.04.2015.
 */

/**
 * Экземпляр класса Frame представляет собой кадр канального уровня.
 * Структура кадра:
 *      |START_BYTE|TypeFrame|flagFinal|Data*|STOP_BYTE|
 * Типы кадров:
 *      ACK - положительная квитанция о получении кадра
 *      RET - требованеи повторить передачу последнего кадра
 *      I - информационный кадр (кадр с данными)
 * Размеры:
 *      START_BYTE == STOP_BYTE -- 1 byte
 *      TypeFrame -- 1 byte
 *      flagFinal -- 1 byte
 *      Data -- max size 128 * 2 == 256 byte (128 - исходные данные)
 *      MinFrameSize -- 6 byte (с учётом START_BYTE и STOP_BYTE)
 *      MaxFrameSize -- 262 byte (с учётом START_BYTE и STOP_BYTE)
 */
public class Frame {
    private static Logger LOGGER = Logger.getLogger(Frame.class.getName());
    public enum Type {                   // Тип кадра
        ACK, RET, I, ERROR
    }

    public static final byte START_BYTE = (byte) 0xFF;
    public static final byte STOP_BYTE = (byte) 0xFF;

    /**
     * Максимальный размер данный в data в байтах (после сериализации)
     */
    public static final int MAX_DATA_SIZE = 128;

    /**
     * Минимальный размер (в байтах) кадра с учётом стартовых и стоповых бит
     */
    public static final int MIN_FRAME_SIZE = 6;

    /**
     * Максимальный размер (в байтах) кадра с учётом стартовых и стоповых бит
    */
    public static final int MAX_FRAME_SIZE = 262;

    private static CRCCoder crcCoder = new CRCCoder();

    private Type type;
    private byte[] data = null;
    private boolean flagFinal = true;
    private boolean correct = true;


    private Frame(Type type) {
        this.type = type;
    }

    public Frame(Type type, byte[] data, boolean flagFinal) {
        this.type = type;
        this.data = data;
        this.flagFinal = flagFinal;
    }


    public static Frame newACKFrame() {
        return new Frame(Type.ACK);
    }

    public static Frame newRETFrame() {
        return new Frame(Type.RET);
    }

    public static Frame newERRORFrame() {
        Frame errorFrame = new Frame(Type.ERROR);
        errorFrame.correct = false;
        return errorFrame;
    }


    private void setCorrect(boolean correct) {
        this.correct = correct;
    }

    private void setFlagFinal(boolean flagFinal) {
        this.flagFinal = flagFinal;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isFinalFrame() {
        return flagFinal;
    }

    public byte[] getData() {
        return data;
    }

    public Type getType() {
        return type;
    }


    public byte[] serialize() {
        byte typeFrame = (byte) type.ordinal();
        byte byteFlagFinal = (byte)(flagFinal ? 1 : 0);
        byte[] info = Utils.concatenate(typeFrame, byteFlagFinal);
        if (data != null) {
            info = Utils.concatenate(info, data);
        }

        // Циклическое кодирование [7,4]
        byte[] encodedInfo = crcCoder.encode(info);

        byte[] frame = Utils.concatenate(START_BYTE, encodedInfo);
        return Utils.concatenate(frame, STOP_BYTE);
    }

    public static Frame deserialize(byte[] data) {
        try {
            data = crcCoder.decode(data);
        } catch (DecodeException e) {
            return Frame.newERRORFrame();
        }

        // Структура data: |typeFrame|byteFlagFinal|data|
        Type typeFrame = Type.values()[data[0]];
        boolean flagFinal = (data[1] != 0);
        byte[] info = Arrays.copyOfRange(data, 2, data.length);

        return new Frame(typeFrame, info, flagFinal);
    }

    // TODO рудименты
    public static void main(String[] args) {
        byte[] dataIn = "Hello!".getBytes();
        Frame frameIn = new Frame(Type.I, dataIn, true);

        byte[] toPhyLayer = frameIn.serialize();
        //toPhyLayer[7] = 1;
        Frame fromPhyLayer = Frame.deserialize(Arrays.copyOfRange(toPhyLayer, 1, toPhyLayer.length - 1));

        System.out.println("Message");
        if (fromPhyLayer.isCorrect()) {
            System.out.println("Type: " + fromPhyLayer.getType().name());
            System.out.println("Data: " + new String(fromPhyLayer.getData()));
            System.out.println("isFinalFrame: " + fromPhyLayer.isFinalFrame());
        }
        else {
            System.out.println("Type: " + fromPhyLayer.getType().name());
        }

        System.out.println();
        System.out.println("ACK");
        Frame ackFrame = Frame.newACKFrame();
        if (ackFrame.isCorrect()) {
            System.out.println("Type: " + ackFrame.getType().name());
            System.out.println("data is null: " + ackFrame.getData() == null);
            System.out.println("isFinalFrame: " + ackFrame.isFinalFrame());
        }
    }
}
