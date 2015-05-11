import layers.datalink.DatalinkLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;
import layers.physical.Settings.ParityEnum;
import layers.physical.Settings.StopBitsEnum;
import messages.TestSerialize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Vadim on 01.05.2015.
 */
public class SecondUser {
    private static DatalinkLayer datalinkLayer = new DatalinkLayer();

    private static void printAvailablePorts() {
        System.out.println("Available ports: ");
        for (String str : PhysicalLayer.getAvailablePorts()) {
            System.out.println(str);
        }
    }

    private static void waitConnection() {
        System.out.println("Waiting for connection...");
        while(!datalinkLayer.isConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static ComPortSettings getDefaultSettings() {
        String portName = PhysicalLayer.getAvailablePorts().get(4);         // TODO индивидуальное значение
        int baudRate = ComPortSettings.getAvailableBaudRates().get(5);
        DataBitsEnum databits = ComPortSettings.getAvailableDataBits().get(3);
        StopBitsEnum stopbits = ComPortSettings.getAvailableStopBits().get(0);
        ParityEnum parity = ComPortSettings.getAvailableParity().get(0);

        return new ComPortSettings(portName, baudRate, databits, stopbits, parity);
    }

    public static void main(String[] args)  {
        printAvailablePorts();
        if (!datalinkLayer.connect(getDefaultSettings())) {
            System.out.println("Error");
            return;
        }

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String login = "unknown";
        String str = "init";

        System.out.print("Your Login: ");
        try {
            login = bufferRead.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TestSerialize packageAppLayer = new TestSerialize();
        packageAppLayer.setLogin(login);

        // Ожидание подключения собеседника
        waitConnection();

        System.out.println("Let's Go!");
        while (str != null && !str.equals("q") && !str.equals("exit") && datalinkLayer.isConnected()) {
            try {
                System.out.print(login + ": ");
                str = bufferRead.readLine();
                packageAppLayer.setMessage(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            datalinkLayer.send(str);
        }
        datalinkLayer.disconnect();
    }

}
