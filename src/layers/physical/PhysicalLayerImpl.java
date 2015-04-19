package layers.physical;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.*;

/**
 * Created by Vadim on 18.04.2015.
 */
class PhysicalLayerImpl implements PhysicalLayer {
    private static Logger LOGGER = Logger.getLogger(PhysicalLayerImpl.class.getName());

    private Enumeration portList;
    private CommPortIdentifier portId;
    private SerialPort serialPort;

    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread readThread;

    // Параметры com-порта
    private int rate = 9600;        // Скорость передачи [бод/с], default value = 9600


    public PhysicalLayerImpl() {
        // TODO а если null?
        portList = CommPortIdentifier.getPortIdentifiers();
    }

    public void setPortParams(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public ArrayList<String> getPortsName() {
        ArrayList<String> listNames = new ArrayList<>();
        if (portList == null)
            return listNames;

        while (portList.hasMoreElements())
            listNames.add(((CommPortIdentifier) portList.nextElement()).getName());
        return listNames;
    }

    // TODO synchronized? Иии не готово
    @Override
    public synchronized void send(byte[] data) {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setPortByName(String namePort) {
        if (portListIsNull())
            return false;

        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getName().equals(namePort) && (portId.getPortType() == CommPortIdentifier.PORT_SERIAL))
                this.portId = portId;
        }

        return portId == null;
    }

    private boolean portListIsNull() {
        return portList == null;
    }
}