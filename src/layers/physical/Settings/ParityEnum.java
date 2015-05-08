package layers.physical.Settings;

import gnu.io.SerialPort;

/**
 * Created by Vadim on 22.04.2015.
 */
public enum  ParityEnum {
    PARITY_NONE("None", SerialPort.PARITY_NONE),
    PARITY_ODD("Odd", SerialPort.PARITY_ODD),
    PARITY_EVEN("Even", SerialPort.PARITY_EVEN),
    PARITY_MARK("Mark", SerialPort.PARITY_MARK),
    PARITY_SPACE("Space", SerialPort.PARITY_SPACE);

    private String name;
    private int value;

    ParityEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int size() {
        return 5;
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
        for (ParityEnum parity : ParityEnum.values())
            if (parity.name.equals(name))
                return true;
        return false;
    }

    public static boolean contains(int value) {
        for (ParityEnum parity : ParityEnum.values())
            if (parity.value == value)
                return true;
        return false;
    }
}
