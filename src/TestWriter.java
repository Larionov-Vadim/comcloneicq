import layers.datalink.DatalinkLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;
import layers.physical.Settings.ParityEnum;
import layers.physical.Settings.StopBitsEnum;

/**
 * Created by Vadim on 01.05.2015.
 */
public class TestWriter {
    private static DatalinkLayer datalinkLayer = new DatalinkLayer();

    public static void main(String[] args)  {
        String portName = PhysicalLayer.getAvailablePorts().get(4);
        int baudRate = ComPortSettings.getAvailableBaudRates().get(5);
        DataBitsEnum databits = ComPortSettings.getAvailableDataBits().get(3);
        StopBitsEnum stopbits = ComPortSettings.getAvailableStopBits().get(0);
        ParityEnum parity = ComPortSettings.getAvailableParity().get(0);

        ComPortSettings settings = new ComPortSettings(portName, baudRate, databits, stopbits, parity);
        datalinkLayer.connect(settings);

        datalinkLayer.send("Hello");
        datalinkLayer.send("World");
        datalinkLayer.send("Bingo!");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        datalinkLayer.disconnect();
    }
}
