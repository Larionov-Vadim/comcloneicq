package layers.application;

import layers.datalink.Frame;

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
    private JButton BackToMessages;
    private ApplicationLayer applicationLayer;
    private FileForm linkToHimSelfFileForm;



    public FileForm(JFrame frame, ApplicationLayer applicationLayer) {
        chooseCatalogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                chooseCatalog(frame, applicationLayer);
                comboBox1.setModel(setExistFiles(applicationLayer));
            }
        });
        chooseFileButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {

                FileNameClass fileNameSend = new FileNameClass();
                fileNameSend.setTwiceSend(false);
                fileNameSend.setFileName((String) applicationLayer.getLinkToAppl().getFileForm().comboBox1.getSelectedItem());
                //textField1.setText((String) comboBox1.getSelectedItem());
                applicationLayer.getLowerLayer().send(fileNameSend);

            }


        });


        BackToMessages.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
            }
        });
    }

    public void wannaFile(ApplicationLayer applicationLayer) {


        this.setApplicationLayer(applicationLayer);
        JFrame frame = new JFrame("Поработаем с файлами");
        frame.setContentPane(new FileForm(frame, applicationLayer).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }

    public void chooseCatalog (JFrame frame, ApplicationLayer applicationLayer){

        JFileChooser catalog = new JFileChooser();
        catalog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = catalog.showOpenDialog(frame);
       // String stringPath = new String(catalog.);
        File filePath = catalog.getSelectedFile();


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
        System.out.println("FileForm.class filePath:" + catalogClass.getPath().toString());
        applicationLayer.getLowerLayer().send(catalogClass);


    }



    public DefaultComboBoxModel setExistFiles(ApplicationLayer applicationLayer){

        this.setApplicationLayer(applicationLayer);
            List<String> files = applicationLayer.getLinkToAppl().getCatalogClassTemp().getFileCatalog();
            String[] availableFiles = new String[applicationLayer.getLinkToAppl().getCatalogClassTemp().getAmount()];
            for (int i = 0; i < applicationLayer.getLinkToAppl().getCatalogClassTemp().getAmount(); ++i) {
                availableFiles[i] = files.get(i);

            }

           // comboBox1 = new JComboBox();

            DefaultComboBoxModel model = new DefaultComboBoxModel(availableFiles);
            applicationLayer.getLinkToAppl().getFileForm().comboBox1.setModel(model);




        return model;



        }

    public ApplicationLayer getApplicationLayer() {
        return applicationLayer;
    }

    public void setApplicationLayer(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;
    }

    public FileForm getLinkToHimSelfFileForm() {
        return linkToHimSelfFileForm;
    }

    public void setLinkToHimSelfFileForm(FileForm linkToHimSelfFileForm) {
        this.linkToHimSelfFileForm = linkToHimSelfFileForm;
    }

    public void setComboBox1(DefaultComboBoxModel model) {
        comboBox1.setModel(model);
    }
}

