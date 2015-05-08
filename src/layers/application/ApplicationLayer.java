package layers.application;

import layers.datalink.DatalinkLayer;
import layers.physical.PhysicalLayer;
import layers.physical.Settings.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by HP on 07.05.2015.
 */
public class ApplicationLayer {
    private JPanel panel1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox51;
    private JTextField textField1;
    private JButton GoButton;
    private DatalinkLayer datalinkLayer;

    public DatalinkLayer getDatalinkLayer() {
        return datalinkLayer;
        //comboBox1.set
    }

    public void setDatalinkLayer(DatalinkLayer datalinkLayer) {
        this.datalinkLayer = datalinkLayer;
    }

    public ApplicationLayer() {


        List<String> ports = PhysicalLayer.getAvailablePorts();
        String[] availablePorts = new String[ports.size()];
        for (int i = 0; i < ports.size(); ++i) {
            availablePorts[i] = ports.get(i);
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(availablePorts);
        comboBox51.setModel(model);
        datalinkLayer = new DatalinkLayer(this);
        GoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

               // DatalinkLayer datalinkLayer = new DatalinkLayer();// static ?

                int baudRate = ComPortSettings.getAvailableBaudRates().get(comboBox1.getSelectedIndex());
                DataBitsEnum dataBitsEnum = ComPortSettings.getAvailableDataBits().get(comboBox2.getSelectedIndex());
                ParityEnum parityEnum = ComPortSettings.getAvailableParity().get(comboBox4.getSelectedIndex());
                StopBitsEnum stopBitsEnum = ComPortSettings.getAvailableStopBits().get(comboBox3.getSelectedIndex());
                ComPortSettings settings = new ComPortSettings((String) comboBox51.getSelectedItem(),baudRate,dataBitsEnum,stopBitsEnum,parityEnum);

                datalinkLayer.connect(settings);

                /*
                System.out.println(baudRate);
                System.out.println(dataBitsEnum);
                System.out.println(parityEnum);
                System.out.println(stopBitsEnum);
                textField1.setText("Работает");
                */



                DialogWindow dialogWindow = new DialogWindow(ApplicationLayer.this);

                dialogWindow.callDialogWindow(textField1.getText());

            }

        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
    public JComboBox getComboBox1() {
        return comboBox1;
    }
    public JComboBox getComboBox2() {
        return comboBox2;
    }
    public JComboBox getComboBox3() {
        return comboBox3;
    }
    public JComboBox getComboBox4() {
        return comboBox4;
    }
    public JComboBox getComboBox51() {
        return comboBox51;
    }
    public JTextField getTextField1() {
        return textField1;
    }
    public JButton getGoButtonButton() {
        return GoButton;
    }

    public DatalinkLayer getLowerLayer() {
        return datalinkLayer;
    }



    static public void main(String[] args) {



        ApplicationLayer applicationLayer = new ApplicationLayer();


        applicationLayer.gettingStarted();


    }


    public void gettingStarted()
    {


        //тут меняем стиль
       styleChange();



        JFrame frame = new JFrame("Давайте настроимся!");
        frame.setContentPane(new ApplicationLayer().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }



    public void styleChange(){


        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

    }



}

