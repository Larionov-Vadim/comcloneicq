package layers.physical.Settings;

import gnu.io.SerialPort;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vadim on 21.04.2015.
 */
public class ComPortSettings {
    private String port;
    private int baudRate = 9600;                        // Default values
    private int dataBits = SerialPort.DATABITS_8;
    private int stopBits = SerialPort.STOPBITS_1;
    private int parity = SerialPort.PARITY_NONE;

    public ComPortSettings(String portName, int baudRate, DataBitsEnum dataBits,
                           StopBitsEnum stopBits, ParityEnum parity) {
        this.port = portName;
        this.baudRate = baudRate;
        this.dataBits = dataBits.getValue();
        this.stopBits = stopBits.getValue();
        this.parity = parity.getValue();
    }

    public static List<Integer> getAvailableBaudRates() {
        return Arrays.asList(300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200);
    }

    public static List<DataBitsEnum> getAvailableDataBits() {
        return Arrays.asList(DataBitsEnum.values());
    }

    public static List<FlowControlEnum> getAvailableFlowControls() {
        return Arrays.asList(FlowControlEnum.values());
    }

    public static List<StopBitsEnum> getAvailableStopBits() {
        return Arrays.asList(StopBitsEnum.values());
    }

    public static List<ParityEnum> getAvailableParity() {
        return Arrays.asList(ParityEnum.values());
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
