package layers.physical;

import java.util.List;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Test {

    public static void main(String[] args) {
        List<String> listNames = PhysicalLayerImpl.getAvailablePorts();

        System.out.println("PortNames:");
        for (String portName : listNames)
            System.out.println("\t" + portName);


        String receiverName = listNames.get(0);     // COM1
        String senderName = listNames.get(1);       // COM2

        System.out.println("\nreceiverName: " + receiverName);
        System.out.println("senderName: " + senderName);

    }
}
