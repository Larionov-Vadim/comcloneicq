package utils;

import java.lang.reflect.Array;

/**
 * Created by Vadim on 20.04.2015.
 */
public class Utils {
    public static byte[] concatenate(byte[] A, byte[] B) {
        int aLength = A.length;
        int bLength = B.length;

        byte[] C = (byte[]) Array.newInstance(A.getClass().getComponentType(), aLength + bLength);
        System.arraycopy(A, 0, C, 0, aLength);
        System.arraycopy(B, 0, C, aLength, bLength);

        return C;
    }

    public static byte[] concatenate(byte a, byte[] B) {
        return concatenate(new byte[]{a}, B);
    }

    public static byte[] concatenate(byte[] A, byte b) {
        return concatenate(A, new byte[]{b});
    }

    public static byte[] concatenate(byte a, byte b) {
        byte[] C = new byte[2];
        C[0] = a;
        C[1] = b;
        return C;
    }
}
