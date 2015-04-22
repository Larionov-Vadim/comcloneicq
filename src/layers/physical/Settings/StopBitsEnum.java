package layers.physical.Settings;

import gnu.io.SerialPort;

/**
 * Created by Vadim on 22.04.2015.
 */
public enum StopBitsEnum {
    STOPBITS_1("1", SerialPort.STOPBITS_1),
    STOPBITS_1_5("1,5", SerialPort.STOPBITS_1_5),
    STOPBITS_2("2", SerialPort.STOPBITS_2);

    private String name;
    private int value;

    StopBitsEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static boolean contains(String name) {
        for (StopBitsEnum stopbits : StopBitsEnum.values())
            if (stopbits.name.equals(name))
                return true;
        return false;
    }

    public static boolean contains(int value) {
        for (StopBitsEnum stopbits : StopBitsEnum.values())
            if (stopbits.value == value)
                return true;
        return false;
    }
}
