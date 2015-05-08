package layers.physical;

import layers.datalink.DatalinkLayer;
import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;

/**
 * Created by HP on 06.05.2015.
 */
public class TestSettings {

    public static void main(String[] args) {
        String portName = "Com1";
        int baudRate = ComPortSettings.getAvailableBaudRates().get(3);

        System.out.println("Baud Rates");
        for (int i : ComPortSettings.getAvailableBaudRates()) {
            System.out.println(i);
        }

        DataBitsEnum dataBitsEnum = ComPortSettings.getAvailableDataBits().get(0);

        int dataBitsSize = ComPortSettings.getAvailableDataBits().size();
        String[] dataBits = new String[dataBitsSize];
        for (int i = 0; i < dataBitsSize; ++i) {
            dataBits[i] = ComPortSettings.getAvailableDataBits().get(i).getName();
        }

        System.out.println("\nDatabits");
        for (String data : dataBits) {
            System.out.println(data);
        }

        Object str = "Это String!";
        if (str instanceof String) {
            str = (String) str;
            System.out.println("Type: String; " + str);
        }
        if (str instanceof Integer) {
            str = (Integer) str;
            System.out.println("Type: Integer; " + str);
        }
        // ComPortSettings settings = new ComPortSettings(portName, baudRate, dataBitsEnum, );
        DatalinkLayer datalinkLayer1 = new DatalinkLayer();
        DatalinkLayer datalinkLayer2 = new DatalinkLayer();
        //datalinkLayer1.send();
    }
}
