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
        }//��� ���� ������� ����

        TextField nameField = new TextField("� �� ������ �� ��� ���");
        TextField comPNameField = new TextField("��� ��������");
        TextField speedField = new TextField("��������");
        TextField bitesOfDataField = new TextField("���� ������");
        TextField stopBitesField = new TextField("���� ����");
        JCheckBox quanCheck = new JCheckBox("�������� �� ��������");

        JList<TextField> list = new JList<>();
        list.add(nameField);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(list);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());//����� ���������� ������

        topPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//�������
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame("������� ����������");
        frame.setMinimumSize(new Dimension(300, 200));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);




    }






}
