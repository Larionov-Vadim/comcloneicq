package layers.datalink;

import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Created by Vadim on 19.04.2015.
 */
public class DatalinkLayer {
    // TODO ApplicationLayer
    private PhysicalLayer physicalLayer;


    public void send(Serializable object) {
        byte[] data = SerializationUtils.serialize(object);
        Frame frame = new Frame(Frame.Type.I);

        // TODO magic!
    }

    public void connect(ComPortSettings settings) {
        // TODO
    }

    public void disconnect() {
        // TODO
    }


    public PhysicalLayer getLowerLayer() {
        return physicalLayer;
    }


}
