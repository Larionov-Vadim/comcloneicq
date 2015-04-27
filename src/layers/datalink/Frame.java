package layers.datalink;

import layers.PDU;
import Utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Vadim on 20.04.2015.
 */
public class Frame implements PDU {
    /** Структура кадра
     *      |START_BYTE|TypeFrame|NumberFrame|Data.Length*|Data*|STOP_BYTE|
     * Type: I - информационный кадр для передачи сообщения типа String
     *       ACK - положительная квитанция о получении кадра
     *       RET - требование повторить передачу последнего кадра
     *
     */
    public enum Type {                   // Тип кадра
        MSG, INIT, ACK, RET;
    }

    private static final byte START_BYTE = (byte) 0xFF;
    private static final byte STOP_BYTE = (byte) 0xFF;

    private static final int MAX_SIZE = 128;    // Или 127?

    private Type type;
    private byte[] data;
    private boolean correct = true;


    public Frame(Type type) {
        this.type = type;
    }

    public Frame(Type type, byte[] data) {
        this.type = type;
        this.data = data;
    }



    public static Frame newACKFrame() {
        return new Frame(Type.ACK);
    }

    public static Frame newRETFrame() {
        return new Frame(Type.RET);
    }

    public boolean isCorrect() {
        return correct;
    }

    private void setCorrect(boolean correct) {
        this.correct = correct;
    }


    @Override
    public byte[] serialize() {
        // TODO всё может быть null;

        // Формируем тип кадра
        byte typeFrame = (byte) type.ordinal();

        // Если есть данные, то добавляем их в кадр
        if (data != null) {
            // Add length
            //Utils.concatenate();
            // Add data
        }
        // Обёртки

        return new byte[0];
    }


    public static void main(String[] args) {
        Type type = Type.ACK;
        System.out.println(type.ordinal());
        String[] arr = new String[] {"hsad"};
        System.out.println();
    }
}
