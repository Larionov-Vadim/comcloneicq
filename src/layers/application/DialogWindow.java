package layers.application;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DialogWindow {
    private static final Logger LOGGER = Logger.getLogger(DialogWindow.class.getName());

    private JPanel panel1;
    private JTextField inputMessage;
    private JButton sendButton;
    private JButton STOPButton;
    private JButton wantFiles;
    private JTextArea areaForMessages;
    // private Image image;
    private User users;
    private FileForm fileForm;
   private String pathTemp;


    private ApplicationLayer applicationLayer;
    private CatalogClass catalogClassTemp;

    public DialogWindow(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MessageClass newMessage = new MessageClass();
                newMessage.setWritenMessage(inputMessage.getText()+"\n");


                //System.out.println(newMessage.getWritenMessage());

                areaForMessages.append(applicationLayer.getUsers().ToString() + ": " + newMessage.getWritenMessage() + "\n");


                applicationLayer.getLowerLayer().send(newMessage);


            }
        });
        STOPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationLayer.getLowerLayer().disconnect();

                JFrame frame = new JFrame("Соединение Разорвано");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);






            }

        });
        wantFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AskingClass wantFileMessage = new AskingClass();
                wantFileMessage.setFileAskingMessage("Ваш собеседник " + applicationLayer.getUsers().ToString() + " открыл окно передачи файлов\n");
                //TODO тут отправляется инфо сообщенька о том что юезр захотел файл т.е. открыл окошко хотения файла
                applicationLayer.getLowerLayer().send(wantFileMessage);
                JFrame form = new JFrame();

                FileForm fileForm = new FileForm(form, applicationLayer);
                applicationLayer.getLinkToAppl().fileForm = fileForm;
                fileForm.setLinkToHimSelfFileForm(fileForm);


                fileForm.wannaFile(applicationLayer);


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

        // this.areaForMessages.append(applicationLayer.getUsers().ToString());
        this.areaForMessages.append("Приветствую, " + applicationLayer.getUsers().ToString() + ", собеседник пока не\nподключен, стоит немного подождать\n");

        inputMessage.selectAll();
        inputMessage.requestFocus();

        applicationLayer.setLinkToAppl(this);


    }


    // public get

    public void takeSomething(Object object) {

        if (object instanceof AskingClass)
            areaForMessages.append(((AskingClass) object).giveMessage());

        if (object instanceof MessageClass) {

            areaForMessages.append( ((MessageClass) object).getWritenMessage());
            areaForMessages.append("\n");
        }
//

        if (object instanceof CatalogClass) {

            setCatalogClassTemp((CatalogClass) object);


            System.out.println("CatalogClassRecieved:"+ ((CatalogClass) object).getPath());

            applicationLayer.getLinkToAppl().getFileForm().setComboBox1(applicationLayer.getLinkToAppl().fileForm.setExistFiles(applicationLayer));
        }



        if (object instanceof FileNameClass) {
            System.out.println("received FileNameClass");

           if (!((FileNameClass) object).isTwiceSend()) {
                ((FileNameClass) object).setTwiceSend(true);
                applicationLayer.getLowerLayer().send(object);

               applicationLayer.getLinkToAppl().setPathTemp(applicationLayer.getLinkToAppl().getCatalogClassTemp().getPath().toString() + "\\" + ((FileNameClass) object).getFileName());
            System.out.println("\n путь петля true "+ pathTemp  +"\n");

            }
            else {

                String fullPath = applicationLayer.getLinkToAppl().getCatalogClassTemp().getPath().toString() + "\\" + ((FileNameClass) object).getFileName();
                System.out.println("fullPath: " + fullPath);

                FilesClass imFile = new FilesClass();
                imFile.setFileName(((FileNameClass) object).getFileName());
                //imFile.setWholeFile(((FileNameClass) object).toString());

                imFile.setPath(applicationLayer.getLinkToAppl().getCatalogClassTemp().getPath().toString());

                imFile.setData(read(fullPath));
                applicationLayer.getLowerLayer().send(imFile);



            }
        }



        if (object instanceof FilesClass) {
            System.out.println("Пришел файлик FilesClass");
            FilesClass filesClass = (FilesClass) object;
            //File pathTemp = new File(((FilesClass) object).getPath() + "\\" + ((FilesClass) object).getFileName());
            System.out.println(pathTemp);
            File file = new File(pathTemp);

            try {
                //проверяем, что если файл не существует то создаем его
                if (!file.exists()) {
                    file.createNewFile();
                }
                try(FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = filesClass.getData();
                    fos.write(buffer, 0, buffer.length);
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Обработанное исключение", e);
            }
        }
    }

    public void setReference(ApplicationLayer applicationLayer1) {

        applicationLayer = applicationLayer1;

    }

    public JTextArea getAreaForMessages() {
        return areaForMessages;
    }

    public void setAreaForMessages(JTextArea areaForMessages) {
        this.areaForMessages = areaForMessages;
    }

    public void getUsers(User name) {

        this.users = name;

    }

    public CatalogClass getCatalogClassTemp() {
        return catalogClassTemp;
    }

    public void setCatalogClassTemp(CatalogClass catalogClassTemp) {
        this.catalogClassTemp = catalogClassTemp;
    }

    public FileForm getFileForm() {
        return fileForm;
    }

    public void setFileForm(FileForm fileForm) {
        this.fileForm = fileForm;
    }

    public ApplicationLayer getApplicationLayer() {
        return applicationLayer;
    }

    public static byte[] read(String filePath) {
        byte[] buffer = null;
        System.out.println("read filePath: " + filePath);
        try (FileInputStream fin = new FileInputStream(filePath)) {
            buffer = new byte[fin.available()];
            fin.read(buffer, 0, fin.available());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Обработанное исключение", e);
        }
        if (buffer == null)
            LOGGER.warning("buffer is null");
        return buffer;
    }


    public String getPathTemp() {
        return pathTemp;
    }

    public void setPathTemp(String pathTemp) {
        this.pathTemp = pathTemp;
    }
}


class  MyDrawPanel extends JFrame{

    public void MyDrawPanel(){



            ImageIcon image =  new ImageIcon(("stopPic.jpg"));

        String [] columnNames = {"hh","hhh"};

        Object[][] data =
                {
                        {image, "About"},

                };

        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        JTable table = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class

        };
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane( table );
        getContentPane().add(scrollPane);
    }

        }




