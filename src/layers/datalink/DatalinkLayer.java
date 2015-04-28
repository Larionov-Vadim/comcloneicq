package layers.datalink;

import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import org.apache.commons.lang3.SerializationUtils;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Vadim on 19.04.2015.
 */
public class DatalinkLayer {
    // TODO ApplicationLayer
    private PhysicalLayer physicalLayer;

    private Queue<Frame> framesToSend = new ConcurrentLinkedQueue<>();
    private Queue<Frame> receivedFrames = new ConcurrentLinkedQueue<>();

    public void send(Serializable object) {
        byte[] data = SerializationUtils.serialize(object);

        // Дробим данные на кадры
        int chunkSize = Frame.MAX_DATA_SIZE;
        int countFrames = data.length / chunkSize + (data.length % chunkSize != 0 ? 1 : 0);
        boolean flagFinal = false;
        for (int i = 0; i < countFrames; ++i) {
            int fromIndex = i * chunkSize;
            int toIndex = (data.length < (i + 1) * chunkSize ? data.length : (i + 1 ) * chunkSize);
            if (toIndex == data.length)
                flagFinal = true;

            byte[] currentData = Arrays.copyOfRange(data, fromIndex, toIndex);
            Frame frame = new Frame(Frame.Type.I, currentData, flagFinal);
            framesToSend.add(frame);
        }
    }

    public void receive(byte[] data) {
        // TODO
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

//    public static void main(String[] args) {
//        String str = "Hello World! Yo! Hellooo!";
//        System.out.println(str.length());
//        DatalinkLayer datalinkLayer = new DatalinkLayer();
//        datalinkLayer.send(str.getBytes());
//
//        System.out.println(datalinkLayer.framesToSend.size());
//
//        List<Frame> frames = new ArrayList<>();
//        while (!datalinkLayer.framesToSend.isEmpty()) {
//            frames.add(datalinkLayer.framesToSend.poll());
//        }
//
//        System.out.println();
//        // frames.stream().forEach(s -> System.out.println("isFinal: " + s.isFinalFrame()));
//        ArrayList<byte[]> b = new ArrayList<>();
//
//        byte[] result = new byte[0];
//        for (Frame frame : frames) {
//            System.out.println("isFinal: " + frame.isFinalFrame());
//            result = Utils.concatenate(result, frame.getData());
//        }
//        System.out.println(new String(result));
//    }
}
