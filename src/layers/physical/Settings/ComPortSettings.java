package layers.physical.Settings;

import gnu.io.SerialPort;

import java.util.ArrayList;
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

    public ComPortSettings(String port) {
        this.port = port;
    }

    public static List<Integer> getAvailableBaudRates() {
        return Arrays.asList(300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 28800, 38400, 57600, 115200);
    }

    // TODO enum-ы или строки? Пока есть и то, и другое
    public static List<DataBitsEnum> getAvailableDataBits() {
        return Arrays.asList(DataBitsEnum.values());
    }

    public static List<String> getAvailableDataBitsNames() {
        List<String> availableDataBits = new ArrayList<>();
        for (DataBitsEnum databits: DataBitsEnum.values())
            availableDataBits.add(databits.getName());
        return availableDataBits;
    }


    public static List<FlowControlEnum> getAvailableFlowControls() {
        return Arrays.asList(FlowControlEnum.values());
    }

    public static List<String> getAvailableFlowControlsNames() {
        List<String> availableFlowControls = new ArrayList<>();
        for (FlowControlEnum flowControl: FlowControlEnum.values())
            availableFlowControls.add(flowControl.getName());
        return availableFlowControls;
    }


    public static List<StopBitsEnum> getAvailableStopBits() {
        return Arrays.asList(StopBitsEnum.values());
    }

    public static List<String> getAvailableStopBitsNames() {
        List<String> availableStopBits = new ArrayList<>();
        for (StopBitsEnum stopbits: StopBitsEnum.values())
            availableStopBits.add(stopbits.getName());
        return availableStopBits;
    }


    public static List<ParityEnum> getAvailableParity() {
        return Arrays.asList(ParityEnum.values());
    }

    public static List<String> getAvailableParityNames() {
        List<String> availableParity = new ArrayList<>();
        for (ParityEnum parity: ParityEnum.values())
            availableParity.add(parity.getName());
        return availableParity;
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

    public static void main(String[] args) {
        List<String> availableDataBits = ComPortSettings.getAvailableDataBitsNames();
        for(String str : availableDataBits) {
            System.out.println(str);
        }
        System.out.println(DataBitsEnum.contains(8));

        List<String> availableFlowControls = ComPortSettings.getAvailableFlowControlsNames();
        for(String str : availableFlowControls) {
            System.out.println(str);
        }

        System.out.println(FlowControlEnum.contains(8));

        List<String> availableParity = ComPortSettings.getAvailableParityNames();
        for(String str : availableParity) {
            System.out.println(str);
        }

        List<DataBitsEnum> databitsEnum = ComPortSettings.getAvailableDataBits();
        for (DataBitsEnum databits : databitsEnum) {
            System.out.println(databits);
        }
    }
}
