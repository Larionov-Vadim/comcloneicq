package layers.datalink;

import exceptions.DecodeException;

import java.util.logging.Logger;

/**
 * Created by Vadim on 19.04.2015.
 */

public class CRCCoder {
    private static Logger LOGGER = Logger.getLogger(CRCCoder.class.getName());
    // Кодированные числа [0; 15]: 0, 11, 22, 29, 39, 44, 49, 58, 69, 78, 83, 88, 98, 105, 116, 127

    /**
     * Кодирование циклическим кодом [7,4] с порождающим полиномом X^3 + X + 1
     * @param data Данные, которые необходимо закодировать
     * @return Закодированный массив байт
     */
    public byte[] encode(byte[] data) {
        byte[] result = new byte[data.length * 2];
        byte currentByte, halfByte1, halfByte2, residue;

        for (int i = 0; i < data.length; ++i) {
            currentByte = data[i];

            halfByte1 = (byte)((currentByte & 0b00001111) << 3);
            residue = divide(halfByte1);
            result[2 * i] = (byte)(halfByte1 | residue);

            halfByte2 = (byte)((currentByte & 0b11110000) >> (4 - 3));
            residue = divide(halfByte2);
            result[2 * i + 1] = (byte)(halfByte2 | residue);
        }

        return result;
    }

    /**
     * Декодирование циклического кода [7,4] с порождающим полиномом X^3 + X + 1
     * @param data Закодированные данные
     * @return Декодированный массив байт
     * @throws DecodeException Выбрасывается при ошибке декодирования
     */
    public byte[] decode(byte[] data) throws DecodeException {
        byte[] result = new byte[data.length / 2];
        byte currentByte, halfByte1, halfByte2, residue;

        for (int i = 0; i < data.length - 1; i += 2) {
            currentByte = 0;
            halfByte1 = data[i];
            halfByte2 = data[i + 1];

            residue = divide(halfByte1);
            if (residue != 0)
                throw new DecodeException("residue " + residue + "!= 0");
            currentByte = (byte)(currentByte | (halfByte1 & 0b1111000) >> 3);

            residue = divide(halfByte2);
            if (residue != 0)
                throw new DecodeException("residue " + residue + "!= 0");
            currentByte = (byte)(currentByte | ((halfByte2 & 0b1111000) << 1));

            result[i / 2] = currentByte;
        }

        return result;
    }

    private byte getShiftNumbers(byte number, byte maxShift) {
        byte count = 0;
        for (byte b = 0b1000000; b != 0; b = (byte)(b >> 1)) {
            if ((number & b) > 0)
                return count > maxShift ? maxShift : count;
            ++count;
        }
        return 0;
    }

    /**
     * Функция деления делимого devidend на порождающий полином X^3 + X + 1
     * @param dividend делимое
     * @return остаток от деления
     */
    private byte divide(byte dividend) {
        final byte divider = 0b1011000;     // Порождающий полином X^3 + X + 1 со сдвигом << 3
        byte maxShift = 3;                  // Максимальное количество сдвигов, чтобы не испортить порождающий полином
        byte residue = dividend;            // Остаток от деления dividend на порождающий полином

        while((residue) > 0b111) {
            residue = (byte)(residue ^ (divider >> getShiftNumbers(residue, maxShift)));
        }
        return residue;
    }

    public static void main(String[] args) {
        CRCCoder crcCoder = new CRCCoder();
        byte data[] = "Hello World!\n\tTabTab".getBytes();
        byte[] encoded = crcCoder.encode(data);
        System.out.println("Encoded");
        System.out.println(new String(encoded));

        byte[] decoded = new byte[0];
        try {
            decoded = crcCoder.decode(encoded);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        System.out.println("Decoded");
        System.out.println(new String(decoded));
    }
}
