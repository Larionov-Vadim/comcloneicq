package layers.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileForm {
    private JPanel panel1;
    private JTextField textField1;
    private JButton chooseCatalogButton;
    private JButton chooseFileButton;
    private JComboBox comboBox1;
    private JButton button1;
    private JButton обратноКСообщениямButton;
    private ApplicationLayer applicationLayer;


    public FileForm(JFrame frame) {
        chooseCatalogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                chooseCatalog(frame);

            }
        });
    }

    public void wannaFile(ApplicationLayer applicationLayer) {

        JFrame frame = new JFrame("Поработаем с файлами");
        frame.setContentPane(new FileForm(frame).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }

    public void chooseCatalog (JFrame frame){

        JFileChooser catalog = new JFileChooser();
        catalog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = catalog.showOpenDialog(frame);

        File filePath = (catalog.getCurrentDirectory());
        System.out.println("Am i wrong");

        CatalogClass catalogClass = new CatalogClass();
        List <String> fileNames = new ArrayList();
        for (File file : filePath.listFiles()) {
            if (file.isDirectory()) {
                //folderNames.add(file.getName());
                continue;
            } else {
                fileNames.add(file.getName());
            }
        }

        catalogClass.setAmount(fileNames.size());
        catalogClass.setFileCatalog(fileNames);
        catalogClass.setPath(filePath);
        System.out.println("IMA BIRD");
        applicationLayer.getLowerLayer().send(catalogClass);


    }





}

