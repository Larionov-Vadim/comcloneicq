package layers.application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class DialogWindow {


    private JPanel panel1;
    private JTextField inputMessage;
    private JButton sendButton;
    private JButton STOPButton;
    private JButton button2;
    private JButton wantFiles;
    private JTextArea areaForMessages;
   // private Image image;
    private User users;

    private ApplicationLayer applicationLayer;

    public DialogWindow(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MessageClass newMessage = new MessageClass();
                newMessage.setWritenMessage(inputMessage.getText());
                newMessage.setInfoValue(1);

                //System.out.println(newMessage.getWritenMessage());

                areaForMessages.append(newMessage.getWritenMessage() + "\n");


                String tempString = newMessage.getWritenMessage();

                applicationLayer.getLowerLayer().send(newMessage.getWritenMessage());


            }
        });
        STOPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationLayer.getLowerLayer().disconnect();
                JFrame closingFrame = new JFrame("Вы разорвали соединение");
                String filename = "C:\\Users\\HP\\comcloneicq\\src\\layers\\application\\stopPic.jpg";
                //Image image = ImageIO.read(new File(filename));


                closingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                closingFrame.pack();
                closingFrame.setVisible(true);




            }
        });
        wantFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AskingClass wantFileMessage = new AskingClass();
                wantFileMessage.setFileAskingMessage("Пользователь" + users.ToString() + " открыл окно передачи файлов");
                //TODO тут отправляется инфо сообщенька о том что юезр захотел файл т.е. открыл окошко хотения файла
                applicationLayer.getLowerLayer().send(wantFileMessage);
                JFrame form = new JFrame();
                InfoForm IF = new InfoForm(form);
                IF.wannaFileInfo(applicationLayer);


            }
        });
    }





    public JTextField getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(JTextField inputMessage) {
        this.inputMessage = inputMessage;
    }

    public void callDialogWindow() {








        JFrame frame = new JFrame("Окно сообщений");


        //frame.setContentPane(new DialogWindow(applicationLayer).panel1);
        frame.setContentPane(applicationLayer.getLinkToAppl().panel1);
//todo конструктор принимает терь параметры
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        this.areaForMessages.append(users.ToString());


        applicationLayer.setLinkToAppl(this);








    }


   // public get

    public void takeSomething (Object object) {

        if (object instanceof AskingClass)
            areaForMessages.append(((AskingClass) object).giveMessage());
//

        if (object instanceof CatalogClass) {






        }


    }

    public void setReference (ApplicationLayer applicationLayer1){

        applicationLayer = applicationLayer1;

    }

    public JTextArea getAreaForMessages() {
        return areaForMessages;
    }

    public void setAreaForMessages(JTextArea areaForMessages) {
        this.areaForMessages = areaForMessages;
    }

    public void getUsers(User name){

        this.users=name;

    }
}
