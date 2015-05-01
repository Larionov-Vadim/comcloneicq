package layers.physical;

import gnu.io.*;
import layers.physical.Settings.ComPortSettings;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.*;

/**
 * Created by Vadim on 18.04.2015.
 */
public class PhysicalLayer implements SerialPortEventListener {
    private static Logger LOGGER = Logger.getLogger(PhysicalLayer.class.getName());
    private static final String PORT_NAME = "COM_CLONE_ICQ";
    private static final int TIMEOUT = 2000;
    private static List<String> availablePorts;

    //private CommPortIdentifier portId;
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

    public synchronized void send(byte[] data) {
//        serialPort.setRTS(false);
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        try {
            outputStream.write("hello".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(ComPortSettings settings) {
        if (serialPort != null)
            disconnect();

        String port = settings.getPort();
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
            serialPort = (SerialPort) portId.open(PORT_NAME, TIMEOUT);
            LOGGER.info("Port " + port + " opened");

            serialPort.setSerialPortParams(settings.getBaudRate(), settings.getDataBits(),
                    settings.getStopBits(), settings.getParity());
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | NoSuchPortException e) {
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

        serialPort.notifyOnBreakInterrupt(true);
        serialPort.notifyOnCarrierDetect(true);
        serialPort.notifyOnFramingError(true);
        serialPort.notifyOnOutputEmpty(true);
        serialPort.notifyOnRingIndicator(true);
        serialPort.notifyOnOverrunError(true);
        serialPort.notifyOnParityError(true);

        setConnected(true);
    }

    public synchronized void disconnect() {
        if (serialPort != null) {
            System.out.println("Not null");
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
        connected = false;
    }


    private void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:        // Break Interrupt
                System.out.println("BI");
                break;
            case SerialPortEvent.CD:        // Carried Detect (наличие несущей)
                System.out.println("CD=[" + serialPort.isCD() + "]");
                break;
            case SerialPortEvent.CTS:       // Clear to Send (готовность передачи)
                System.out.println("CTS=[" + serialPort.isCTS() + "]");
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                dataAvailable();
                break;

            case SerialPortEvent.DSR:       // Data set Ready (готовность источника данных)
                System.out.println("DSR=[" + serialPort.isDSR() + "]");
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
                System.out.println("RI=[" + serialPort.isRI() + "]");
                break;
        }
    }

    private void dataAvailable() {
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
    }

    public static void main(String[] args) {
        List<String> list = PhysicalLayer.getAvailablePorts();
        ComPortSettings settings = new ComPortSettings(PhysicalLayer.getAvailablePorts().get(4));

        PhysicalLayer physicalLayer = new PhysicalLayer();
        physicalLayer.connect(settings);
        physicalLayer.connect(settings);
        physicalLayer.connect(settings);
        //physicalLayer.disconnect();
    }

}