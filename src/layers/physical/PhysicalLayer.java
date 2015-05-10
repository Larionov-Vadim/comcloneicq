package layers.physical;

import gnu.io.*;
import layers.datalink.DatalinkLayer;
import layers.datalink.Frame;
import layers.physical.Settings.ComPortSettings;
import messages.Messages;

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

    private DatalinkLayer datalinkLayer;

    private static final String PORT_NAME = "COM_CLONE_ICQ";
    private static final int TIMEOUT = 2000;
    private static List<String> availablePorts;

    private static byte START_BYTE = Frame.START_BYTE;
    private static byte STOP_BYTE = Frame.STOP_BYTE;

    private SerialPort serialPort;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean connected;

    public PhysicalLayer(DatalinkLayer datalinkLayer) {
        this.datalinkLayer = datalinkLayer;
    }

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
            outputStream.write(data);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Обработанное исключение", e);
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
        } catch (PortInUseException e) {
            notifyOnMessage(Messages.PORT_IN_USE);
            LOGGER.log(Level.WARNING, "Обработанное исключение", e);
        } catch (UnsupportedCommOperationException | NoSuchPortException | IOException e) {
            LOGGER.log(Level.WARNING, "Обработанное исключение", e);
        }

        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            serialPort.notifyOnCTS(true);
            serialPort.notifyOnDSR(true);
            serialPort.notifyOnCarrierDetect(true);
        } catch (TooManyListenersException e) {
            LOGGER.log(Level.WARNING, "Обработанное исключение", e);
        }

        serialPort.setRTS(true);
        serialPort.setDTR(true);

        if (checkConnection()) {
            setConnected(true);
            notifyOnMessage(Messages.CONNECTED);
        }
        else
            setConnected(false);
    }

    public synchronized void disconnect() {
        if (serialPort != null) {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Обработанное исключение", e);
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

        setConnected(false);
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
            case SerialPortEvent.BI:                    // Break Interrupt
                LOGGER.warning("Break Interrupt");
                break;
            case SerialPortEvent.CTS:                   // Clear to Send (готовность передачи)
                boolean status = checkConnection();
                if (connected ^ status) {
                    if (status)
                        notifyOnMessage(Messages.CONNECTED);
                    else {
                        notifyOnMessage(Messages.DISCONNECTED);
                        disconnect();
                    }
                }
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                dataAvailable();
                break;
            case SerialPortEvent.DSR:                   // Data set Ready (готовность источника данных)
                break;
            case SerialPortEvent.FE:                    // Framing Error
                LOGGER.warning("Framing Error");
                break;
            case SerialPortEvent.OE:        // Overrun Error
                LOGGER.warning("Overrun Error");
                break;
            case SerialPortEvent.PE:        // Parity Error
                LOGGER.warning("Parity error");
                break;
        }
    }

    private void dataAvailable() {
        ArrayList<Byte> bytes = new ArrayList<>(Frame.MAX_DATA_SIZE);

        try {
            byte start = (byte) inputStream.read();
            if (start == START_BYTE) {
                byte b;
                while (true) {
                    b = (byte) inputStream.read();
                    if (b == STOP_BYTE)
                        break;
                    bytes.add(b);
                }
            }
            else {
                LOGGER.log(Level.WARNING, "Принятый байт данных != START_BYTE");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Обработанное исключение", e);
        }

        // Медленно
        byte[] frame = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i) {
            frame[i] = bytes.get(i);
        }
        getUpperLayer().receive(frame);
    }

    public DatalinkLayer getUpperLayer() {
        return datalinkLayer;
    }

    private boolean checkConnection() {
        return serialPort.isCD() && serialPort.isDSR() && serialPort.isCTS();
    }

    private void notifyOnMessage(Messages message) {
        getUpperLayer().notifyOnMessage(message);
    }
}