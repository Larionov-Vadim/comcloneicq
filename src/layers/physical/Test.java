package layers.physical;

import layers.physical.Settings.ComPortSettings;

import java.util.List;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Test {

    public static void main(String[] args) {
        List<String> listNames = PhysicalLayer.getAvailablePorts();
        PhysicalLayer physicalLayer = new PhysicalLayer();
        ComPortSettings settings = new ComPortSettings(listNames.get(5));
        physicalLayer.connect(settings);
        String msg = "Hello";
        System.out.println(msg);
        physicalLayer.send(msg.getBytes());
    }
}
