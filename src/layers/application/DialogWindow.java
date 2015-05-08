package layers.application;

import layers.datalink.DatalinkLayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DialogWindow {

    private JPanel panel1;
    private JTextField введитеСообщениеTextField;
    private JButton отправитьButton;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextArea textArea1;

    private ApplicationLayer applicationLayer;

    public DialogWindow(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;

        отправитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MessageClass newMessage = new MessageClass();
                newMessage.setWritenMessage(введитеСообщениеTextField.getText());
                newMessage.setInfoValue(1);

                System.out.println(newMessage.getWritenMessage());

                textArea1.append(newMessage.getWritenMessage() + "\n");

                String tempString = newMessage.getWritenMessage();
                //TODO вызвать уровень нижееее как
                applicationLayer.getLowerLayer().send(tempString);




        }
        });
    }





    public JTextField getВведитеСообщениеTextField() {
        return введитеСообщениеTextField;
    }

    public void setВведитеСообщениеTextField(JTextField введитеСообщениеTextField) {
        this.введитеСообщениеTextField = введитеСообщениеTextField;
    }

    public void callDialogWindow(String name1) {


        JFrame frame = new JFrame("Окно сообщений");

        frame.setContentPane(new DialogWindow(applicationLayer).panel1);
//todo конструктор принимает терь параметры
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);




    }

   // public get


}
