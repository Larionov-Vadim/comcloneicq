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
    private JComboBox speedSettings;
    private JComboBox bitsOfData;
    private JComboBox stopBits;
    private JComboBox paritySettings;
    private JComboBox comPortName;
    private JTextField nameField;
    private JButton GoButton;
    private DatalinkLayer datalinkLayer;
    private DialogWindow linkToAppl;
    private JFrame frame;
    private String name;
    private User users;

    public DatalinkLayer getDatalinkLayer() {
        return datalinkLayer;

            }

    public DialogWindow getLinkToAppl() {
        return linkToAppl;
    }

    public void setLinkToAppl(DialogWindow linkToAppl) {
        this.linkToAppl = linkToAppl;
    }

    public void setDatalinkLayer(DatalinkLayer datalinkLayer) {
        this.datalinkLayer = datalinkLayer;
    }

    public ApplicationLayer giveLinkToHimself(){

        return this;
    }

    public ApplicationLayer() {


        List<String> ports = PhysicalLayer.getAvailablePorts();
        String[] availablePorts = new String[ports.size()];
        for (int i = 0; i < ports.size(); ++i) {
            availablePorts[i] = ports.get(i);
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(availablePorts);
        comPortName.setModel(model);
        datalinkLayer = new DatalinkLayer(this);
        GoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

               // DatalinkLayer datalinkLayer = new DatalinkLayer();// static ?

                int baudRate = ComPortSettings.getAvailableBaudRates().get(speedSettings.getSelectedIndex());
                DataBitsEnum dataBitsEnum = ComPortSettings.getAvailableDataBits().get(bitsOfData.getSelectedIndex());
                ParityEnum parityEnum = ComPortSettings.getAvailableParity().get(paritySettings.getSelectedIndex());
                StopBitsEnum stopBitsEnum = ComPortSettings.getAvailableStopBits().get(stopBits.getSelectedIndex());
                ComPortSettings settings = new ComPortSettings((String) comPortName.getSelectedItem(),baudRate,dataBitsEnum,stopBitsEnum,parityEnum);

                datalinkLayer.connect(settings);


                /*
                System.out.println(baudRate);
                System.out.println(dataBitsEnum);
                System.out.println(parityEnum);
                System.out.println(stopBitsEnum);
                nameField.setText("Работает");
                */




                DialogWindow linkToAppl = new DialogWindow(giveLinkToHimself());
                linkToAppl.setReference(giveLinkToHimself());
                setLinkToAppl(linkToAppl);

                AskingClass greetMessage = new AskingClass();



                linkToAppl.getApplicationLayer().setUsers(new User(nameField.getText()));



                // users.
               greetMessage.setEnterInfoMessage("\nСобеседник " + nameField.getText() + " подключен\n");
               //linkToAppl.takeSomething(greetMessage);

                linkToAppl.callDialogWindow();
                datalinkLayer.send(greetMessage);


              //  ApplicationLayer.this.setFrame(frame.setEnabled(true));


            }

        });
    }



    public DatalinkLayer getLowerLayer() {
        return datalinkLayer;
    }



    static public void main(String[] args) {



        ApplicationLayer applicationLayer = new ApplicationLayer();
        applicationLayer.styleChange();
        applicationLayer.gettingStarted(applicationLayer);





    }


    public void gettingStarted(ApplicationLayer applicationLayer)
    {


        //тут меняем стиль




        //setFrame(new JFrame("Давайте настроимся!"));
        //JFrame
        applicationLayer.frame = new JFrame("Давайте настроимся");
        styleChange();

        applicationLayer.frame.setContentPane(applicationLayer.panel1);
        applicationLayer.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationLayer.frame.pack();
        applicationLayer.frame.setVisible(true);
        applicationLayer.styleChange();



      //if(frame.isEnabled()==true)
         // frame.setVisible(false);

        //setFrame(frame);

    }



    public static void styleChange(){


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


    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setName(String name){
        this.name=name;

    }

    public String getName (){

        return name;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
}





