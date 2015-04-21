package layers.physical.Settings;

import gnu.io.SerialPort;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vadim on 21.04.2015.
 */
public class Settings {
    private int baudRate = 9600;
    private int dataBits = SerialPort.DATABITS_8;
    private int stopBits = SerialPort.STOPBITS_1;
    private int parity = SerialPort.PARITY_NONE;

    private String port;

    public Settings(String port) {
        this.port = port;
    }

    public static List<Integer> getAvailableBaudRates() {
        return Arrays.asList(300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200);
    }

    public static List<Integer> getAvailableDataBits() {
        return Arrays.asList(SerialPort.DATABITS_5, SerialPort.DATABITS_6,
                             SerialPort.DATABITS_7, SerialPort.DATABITS_8);
    }

    // TODO нужны enum-ы, Parity, stopbits
    public static List<Integer> getAvailableFlowControl() {
        return Arrays.asList(SerialPort.FLOWCONTROL_NONE);
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public String getPort() {
        return port;
    }
}
