package layers.physical;

import gnu.io.*;
import layers.physical.Settings.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.*;

/**
 * Created by Vadim on 18.04.2015.
 */
class PhysicalLayerImpl implements PhysicalLayer, SerialPortEventListener {
    private static Logger LOGGER = Logger.getLogger(PhysicalLayerImpl.class.getName());
    private static final String PORT_NAME = "COM_CLONE_ICQ";
    private static final int TIMEOUT = 2000;
    private static List<String> availablePorts;

    private CommPortIdentifier portId;
    private SerialPort serialPort;

    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread readThread;

    private boolean connected;

    public static List<String> getAvailablePorts(){
        if (availablePorts != null)
            return availablePorts;

        availablePorts = new ArrayList<>();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier portIdentifier;
        while (portList.hasMoreElements()) {
            portIdentifier = (CommPortIdentifier) portList.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
                availablePorts.add(portIdentifier.getName());
        }

        return availablePorts;
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

    public void connect(Settings settings) {
        String port = settings.getPort();

        try {
            serialPort = (SerialPort) portId.open(PORT_NAME, TIMEOUT);
            serialPort.setSerialPortParams(settings.getBaudRate(), settings.getDataBits(),
                    settings.getStopBits(), settings.getParity());
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (PortInUseException | UnsupportedCommOperationException | IOException e) {
            e.printStackTrace();
        }

        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            serialPort.notifyOnCTS(true);
            serialPort.notifyOnDSR(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }

        serialPort.setRTS(true);
        serialPort.setDTR(true);

        setConnected(true);
    }

    public synchronized void disconnect() {
        if (serialPort != null) {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            serialPort.setRTS(false);
            serialPort.setDTR(false);
            serialPort.close();

            LOGGER.info("Port " + serialPort.getName() + " closed");

            inputStream = null;
            outputStream = null;
            serialPort = null;
        }
        else {
            LOGGER.info("Port is not opened");
        }
    }



    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:        // Break Interrupt
                System.out.println("BI");
                break;
            case SerialPortEvent.CD:        // Carried Detect (наличие несущей)
                System.out.println("CD");
                break;
            case SerialPortEvent.CTS:       // Clear to Send (готовность передачи)
                System.out.println("CTS");
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                System.out.println("DATA_AVAILABLE");
                byte[] readBuffer = new byte[200];

                try {
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    System.out.print(new String(readBuffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case SerialPortEvent.DSR:       // Data set Ready (готовность источника данных)
                System.out.println("DSR");
                break;
            case SerialPortEvent.FE:        // Framing Error
                System.out.println("FE");
                break;
            case SerialPortEvent.OE:        // Overrun Error
                System.out.println("OE");
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("OUTPUT_BUFFER_EMPTY");
                break;
            case SerialPortEvent.PE:        // Parity Error
                System.out.println("PE");
                break;
            case SerialPortEvent.RI:        // Ring Indicator (сигнал вызова)
                System.out.println("RI");
                break;
        }
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public static void main(String[] args) {
        List<String> list = PhysicalLayerImpl.getAvailablePorts();
        for (String str : list){
            System.out.println(str);
        }
    }
}