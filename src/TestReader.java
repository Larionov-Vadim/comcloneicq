import layers.datalink.DatalinkLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;
import layers.physical.Settings.ParityEnum;
import layers.physical.Settings.StopBitsEnum;

/**
 * Created by Vadim on 02.05.2015.
 */
public class TestReader {
    private static DatalinkLayer datalinkLayer = new DatalinkLayer();

    public static void main(String[] args) {
        System.out.println("BLA-BLA-BLA");

        for (String str : PhysicalLayer.getAvailablePorts()) {
            System.out.println(str);
        }

        String portName = PhysicalLayer.getAvailablePorts().get(2);
        int baudRate = ComPortSettings.getAvailableBaudRates().get(5);
        DataBitsEnum databits = ComPortSettings.getAvailableDataBits().get(3);
        StopBitsEnum stopbits = ComPortSettings.getAvailableStopBits().get(0);
        ParityEnum parity = ComPortSettings.getAvailableParity().get(0);

        ComPortSettings settings = new ComPortSettings(portName, baudRate, databits, stopbits, parity);
        datalinkLayer.connect(settings);
    }
}
