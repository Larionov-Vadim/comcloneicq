package layers.application;



import layers.physical.Settings.ComPortSettings;
import layers.physical.Settings.DataBitsEnum;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.UIManager.*;

/**Тут лежит начальное окно
*
* в итоге после кнопки "поехали!" создается объект класса настроек
*
* по филдам можно прыгать табом, но энтер пока не работает (а должен)
*
* скорость и чухня про биты должна быть списком
*
* имена ком портов что там
*
*
* рамочки сделать красивыми!!!!!!!!!!
*
*почему объявив лист в мэйне я не могу из эксепшенов в него пихать сообщения????
*короче косяк со списком
*
* */

/* here comes first window with settings and so on */

/* for textfields do the action listener to catch enter without other settings*/

/* checking right data*/

public class WindowHi {

    JFrame frame; //just a frame, no purpose
    JLabel checkLabel; //text stuff underneath, for understanding what is happening
    JLabel nameLabel, quanCheckLabel, comPNameLabel, speedLabel,bitesOfDataLabel, stopBitesLabel;
    JList comPNamelist, speedList, bitesOfDataList, stopBitesList, quanCheckList;

    TextField nameField, comPNameField, speedField, bitesOfDataField, stopBitesField; //enter your name
    JCheckBox quanCheck; //for checking quantity

    public static void main(String[] args) {

        WindowHi window1 = new WindowHi();

        window1.work();

    }


    /* here comes a magic: event listener, whatta hell will happen if u push the button*/
    public void work() {
//////////////////////////////////ELEMENTS///////////////////////////////////////////////////////////////////////////
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        frame = new JFrame("Предварительные настройки:");




        //list = new Jlist()
        JPanel panel1 = new JPanel();//panel
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JButton button = new JButton("Поехали!"); //connection button

        checkLabel = new JLabel("Мама я в дубае");//checking





        nameLabel = new JLabel("Ваше имя");
        quanCheckLabel = new JLabel("Проверка на четность");
        comPNameLabel = new JLabel("Имя COM-порта");
        speedLabel = new JLabel("Скорость");
        bitesOfDataLabel = new JLabel("Биты данных");
        stopBitesLabel = new JLabel("Стоп биты");


        nameField = new TextField("а не ввести ли вам имя");
        /*
        comPNameField = new TextField("Имя компорта");
        speedField = new TextField("Скорость");
        bitesOfDataField = new TextField("Биты данных");
        stopBitesField = new TextField("Стоп биты");
        quanCheck = new JCheckBox("Проверка на четность");
        */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////SETTINGS//OF//ELEMENTS//////////////////////////////////////////////////////////////
        //nameField.setSize(1, 1);
        nameField.selectAll();//select all text
        nameField.requestFocus(); // put focus on the end for convinient typing

        comPNameField.selectAll();
        comPNameField.requestFocus();

        speedField.selectAll();
        speedField.requestFocus();

        bitesOfDataField.selectAll();
        bitesOfDataField.requestFocus();

        stopBitesField.selectAll();
        stopBitesField.requestFocus();




       // panel1.setBackground(Color.pink);
       // panel2.setBackground(Color.white);

        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        button.addActionListener(new GoButtonListener());

        //button.setBackground(Color.getHSBColor()); colour of the button SET ME SET ME SET ME!!!!!!!!!!!!!!!!!!


///////////////////////////////////ADDING///////////////////////////////////////////////////////////////////////////
        panel2.add(nameLabel);
        panel2.add(comPNameLabel);
        panel2.add(speedLabel);
        panel2.add(bitesOfDataLabel);
        panel2.add(stopBitesLabel);
        panel2.add(quanCheckLabel);

        panel1.add(nameField);
        panel1.add(comPNameField);
        panel1.add(speedField);
        panel1.add(bitesOfDataField);
        panel1.add(stopBitesField);

        panel1.add(quanCheck);
        panel1.add(button);
        //panel1.setBackground(Color.LIGHT_GRAY);
        //frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().add(BorderLayout.EAST, panel1);
        frame.getContentPane().add(BorderLayout.WEST,panel2);


        //frame.getContentPane().add(button);
        frame.getContentPane().add(BorderLayout.AFTER_LAST_LINE, checkLabel);
        frame.setSize(400, 300);
        frame.setVisible(true);







    }


    //event for button
    class GoButtonListener implements ActionListener {

        public  void actionPerformed(ActionEvent event){

            SettingsStuff set1 = new SettingsStuff();
           // ComPortSettings set1 = new ComPortSettings(comPNameField.getText(), );
            set1.setName(nameField.getText());
            set1.setComPorName(comPNameField.getText());

            try {

                set1.setSpeed(Integer.parseInt(speedField.getText()));
            }
            catch(NumberFormatException e) {

                checkLabel.setText("В скорости не цифры ");
            }

            try {
                set1.setBitesOfData(Integer.parseInt(bitesOfDataField.getText()));
            }

            catch(NumberFormatException e) {

                checkLabel.setText("В битах данных не цифры ");
            }

            try {
                set1.setStopBites(Integer.parseInt(stopBitesField.getText()));
            }
            catch(NumberFormatException e) {

                checkLabel.setText("В стоповых битах не цифры ");
            }

            try {
                set1.setQuantity(quanCheck.isSelected());
            }

            catch(NumberFormatException e) {

                checkLabel.setText("Тут вот хз что не то можно было ввести чесслово");
            }

            checkLabel.setText(set1.ToString());



            WindowMessages window2 = new WindowMessages();
            window2.work();


        }
}



}




