package layers.physical;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Reader implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    public static final int RATE = 9600;        // Скорость передачи [бод/с]

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM4")) {
                    //                if (portId.getName().equals("/dev/term/a")) {
                    Reader reader = new Reader();
                }
            }
        }
    }

    public Reader() {
        try {
            // public CommPortInterface open(java.lang.String TheOwner,int i)
            //      TheOwner - the name of the application requesting the port
            //      i - number of miliseconds to wait for the port to open
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.setSerialPortParams(
                    RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

        } catch (PortInUseException | IOException | TooManyListenersException | UnsupportedCommOperationException e) {
            e.printStackTrace();
        }

        readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:        // Break Interrupt
            case SerialPortEvent.CD:        // Carried Detect (наличие несущей)
            case SerialPortEvent.CTS:       // Clear to Send (готовность передачи)
                break;

            case SerialPortEvent.DATA_AVAILABLE:
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
            case SerialPortEvent.FE:        // Framing Error
            case SerialPortEvent.OE:        // Overrun Error
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            case SerialPortEvent.PE:        // Parity Error
            case SerialPortEvent.RI:        // Ring Indicator (сигнал вызова)
                break;
        }
    }
}
