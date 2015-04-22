package layers.physical.Settings;

import gnu.io.SerialPort;

/**
 * Created by Vadim on 22.04.2015.
 */
public enum FlowControlEnum {
    FLOWCONTROL_NONE("None", SerialPort.FLOWCONTROL_NONE),
    FLOWCONTROL_RTSCTS_IN("RTS/CTS In", SerialPort.FLOWCONTROL_RTSCTS_IN),
    FLOWCONTROL_RTSCTS_OUT("RTS/CTS Out", SerialPort.FLOWCONTROL_RTSCTS_OUT),
    FLOWCONTROL_XONXOFF_IN("XON/XOFF In", SerialPort.FLOWCONTROL_XONXOFF_IN),
    FLOWCONTROL_XONXOFF_OUT("XON/XOFF Out", SerialPort.FLOWCONTROL_XONXOFF_OUT);

    private String name;
    private int value;

    FlowControlEnum(String name, int value) {
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
        for (FlowControlEnum flowControl : FlowControlEnum.values())
            if (flowControl.name.equals(name))
                return true;
        return false;
    }

    public static boolean contains(int value) {
        for (FlowControlEnum flowControl : FlowControlEnum.values())
            if (flowControl.value == value)
                return true;
        return false;
    }
}
