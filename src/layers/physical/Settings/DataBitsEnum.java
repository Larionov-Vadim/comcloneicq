package layers.physical.Settings;

import gnu.io.SerialPort;

/**
 * Created by Vadim on 22.04.2015.
 */
public enum DataBitsEnum {
    DATABITS_5("5 bit", SerialPort.DATABITS_5),
    DATABITS_6("6 bit", SerialPort.DATABITS_6),
    DATABITS_7("7 bit", SerialPort.DATABITS_7),
    DATABITS_8("8 bit", SerialPort.DATABITS_8);

    private String name;
    private int value;

    DataBitsEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int size() {
        return 4;
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
        for (DataBitsEnum databits : DataBitsEnum.values())
            if (databits.name.equals(name))
                return true;
        return false;
    }

    public static boolean contains(int value) {
        for (DataBitsEnum databits : DataBitsEnum.values())
            if (databits.value == value)
                return true;
        return false;
    }
}
