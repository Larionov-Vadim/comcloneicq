package layers.physical;

import java.util.ArrayList;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Test {

    public static void main(String[] args) {
        PhysicalLayerImpl physicalLayer = new PhysicalLayerImpl();
        ArrayList<String> listNames = physicalLayer.getPortsName();

        System.out.println("PortNames:");
        for (String portName : listNames)
            System.out.println("\t" + portName);

        System.out.println("\nПараметры com-порта:");
        int rate = physicalLayer.getRate();
        System.out.println("\tRate: " + rate);

        String receiverName = listNames.get(0);     // COM1
        String senderName = listNames.get(1);       // COM2

        System.out.println("\nreceiverName: " + receiverName);
        System.out.println("senderName: " + senderName);

    }
}
