package layers.application;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.UIManager.*;
/**
 тут окно сообщений и кнопка про прием файла, тут нихера не сделано вааааааааааще, чем ты блин занимаешься
 */

/*

курсор в нижнее окно не ставится - поставить
имя поставить
кнопки
файлы
что с исключением????
эксепшены для др кнопок

 */
public class WindowMessages {



JTextArea allMessages;
JTextArea giveMessages;
JLabel info;

/* public static void main(String[] args)
{

    WindowMessages m = new WindowMessages();
    m.work();


}*/



public void work() {



    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JButton sendButton = new JButton("Отправить");
    JButton stopButton = new JButton("Разорвать соединение");
    JButton fileButton = new JButton("Получить файл");
    JLabel info = new JLabel("Инфо строчка");
    //ArrayList<MessageClass> messageStore = new ArrayList<MessageClass>();//очередь сообщений


    //sendButton.addActionListener(new sendButtonListener());

    try {
        for (UIManager.LookAndFeelInfo info1 : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info1.getClassName());
                break;
            }
        }
    } catch (Exception e) {
        // If Nimbus is not available, you can set the GUI to another look and feel.
    }


    giveMessages = new JTextArea(10,20);

    giveMessages.setLineWrap(true);

    allMessages = new JTextArea(20,20);
    allMessages.setLineWrap(true);
    //giveMessages.requestFocus();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


    JScrollPane scroller1 = new JScrollPane(allMessages);
    scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    panel.add(scroller1);


    giveMessages.setText("Введите сообщение");
    giveMessages.selectAll();

    JScrollPane scroller2 = new JScrollPane(giveMessages);
    scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    panel.add(scroller2);
    panel.add(info);



    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(sendButton);
    buttonPanel.add(stopButton);
    buttonPanel.add(fileButton);


    frame.getContentPane().add(BorderLayout.CENTER, panel);
    frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);


    frame.setSize(400, 600);
    frame.setVisible(true);
    stopButton.addActionListener(new stopButtonListener());//какая-то х-ня тут происходит
    sendButton.addActionListener(new sendButtonListener());
    fileButton.addActionListener(new fileButtonListener());


}


class sendButtonListener implements ActionListener{

    public void actionPerformed(ActionEvent ev){

        MessageClass newMes = new MessageClass();
        newMes.setInfoValue(1488);
        newMes.setWritenMessage(giveMessages.getText());



        allMessages.append("\nВы отправили:" + newMes.getWritenMessage());
        giveMessages.setText("");
        giveMessages.requestFocus();

    }



}

class stopButtonListener implements ActionListener{

    public void actionPerformed(ActionEvent ev) {

        info.setText("STOP BUTTON'VE PUSHED");//не работает сука


    }


}


class fileButtonListener implements ActionListener{

    public void actionPerformed(ActionEvent e){

        allMessages.append("Нажали кнопку про файлы");

        FilesClass window3 = new FilesClass();
        window3.workMethod();


    }
}

}
