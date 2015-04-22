package layers.physical;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by Vadim on 19.04.2015.
 */
public class Writer {
    static Enumeration portList;
    static CommPortIdentifier portId;
    static String messageString = "Hello, World!\n";
    static SerialPort serialPort;
    static OutputStream outputStream;

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM5")) {
                    try {
                        serialPort = (SerialPort) portId.open("SimpleWriteApp", 2000);
                        outputStream = serialPort.getOutputStream();
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        outputStream.write(messageString.getBytes());
                    } catch (PortInUseException | UnsupportedCommOperationException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}