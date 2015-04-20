package layers.datalink;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim on 19.04.2015.
 */

// TODO нет исправления ошибок, нужно деление многочленов
public class CRCCoder {
    private byte[] map;                 // Отображение половины числа в кодовую последовательность
    private Map<Byte, Byte> reverseMap; // Отображение кодовой последовательности в половину числа

    public CRCCoder() {
        map = new byte[] {0, 11, 22, 29, 39, 44, 49, 58, 69, 78, 83, 88, 98, 105, 116, 127};
        reverseMap = new HashMap<>();
        for (byte i = 0; i < map.length; ++i)
            reverseMap.put(map[i], i);
    }

    public byte[] encode(byte[] data) {
        byte[] result = new byte[data.length * 2];

        byte b, halfByte1, halfByte2;
        for (int i = 0; i < data.length; ++i) {
            b = data[i];

            halfByte1 = (byte)(b & (byte) 0b00001111);
            result[2 * i] = map[halfByte1];

            halfByte2 = (byte)((b & 0xFF) >> (byte) 4);
            result[2 * i + 1] = map[halfByte2];
        }

        return result;
    }

    public byte[] decode(byte[] data) {
        byte[] result = new byte[data.length / 2];

        byte current;
        for (int i = 0; i < data.length - 1; i += 2) {
            current = 0;

            // TODO current может быть null
            current |= reverseMap.get(data[i]);
            current |= reverseMap.get(data[i + 1]) << 4;

            result[i / 2] = current;
        }

        return result;
    }

    public static void main(String[] args) {
        String a = "Hello, Kursach!";
        //byte[] a = new byte[] {13, 14, 0, 1};
        CRCCoder crcCoder = new CRCCoder();
        byte[] encoded = crcCoder.encode(a.getBytes());
        System.out.println("Encoded");
        System.out.println(new String(encoded));

        byte[] decoded = crcCoder.decode(encoded);
        System.out.println("Decoded");
        System.out.println(new String(decoded));
    }
}
