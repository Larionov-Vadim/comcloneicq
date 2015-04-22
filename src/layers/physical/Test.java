package layers.physical;

import layers.physical.Settings.Settings;

import java.util.List;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Test {

    public static void main(String[] args) {
        List<String> listNames = PhysicalLayerImpl.getAvailablePorts();
        PhysicalLayerImpl physicalLayer = new PhysicalLayerImpl();
        Settings settings = new Settings(listNames.get(5));
        physicalLayer.connect(settings);
        String msg = "Hello";
        System.out.println(msg);
        physicalLayer.send(msg.getBytes());
    }
}
