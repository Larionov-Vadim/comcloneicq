package layers.application;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;


/**
 * Created by HP on 06.05.2015.
 */
public class HelloWindow {


    public static void main(String[] args){

    HelloWindow hello = new HelloWindow();
        hello.createGUI();



    }


    private static void createGUI() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }//это чтоб красиво было

        TextField nameField = new TextField("а не ввести ли вам имя");
        TextField comPNameField = new TextField("Имя компорта");
        TextField speedField = new TextField("Скорость");
        TextField bitesOfDataField = new TextField("Биты данных");
        TextField stopBitesField = new TextField("Стоп биты");
        JCheckBox quanCheck = new JCheckBox("Проверка на четность");

        JList<TextField> list = new JList<>();
        list.add(nameField);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(list);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());//рамка становится жирнее

        topPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//отступы
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame("Давайте настроимся");
        frame.setMinimumSize(new Dimension(300, 200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);




    }






}
