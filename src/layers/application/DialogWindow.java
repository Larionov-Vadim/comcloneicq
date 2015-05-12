package layers.application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;



public class DialogWindow {


    private JPanel panel1;
    private JTextField inputMessage;
    private JButton sendButton;
    private JButton STOPButton;
    private JButton wantFiles;
    private JTextArea areaForMessages;
   // private Image image;
    private User users;
    private FileForm fileForm;


    private ApplicationLayer applicationLayer;
    private CatalogClass catalogClassTemp;

    public DialogWindow(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MessageClass newMessage = new MessageClass();
                newMessage.setWritenMessage(inputMessage.getText());


                //System.out.println(newMessage.getWritenMessage());

                areaForMessages.append(applicationLayer.getUsers().ToString()+": "+ newMessage.getWritenMessage() + "\n");




                applicationLayer.getLowerLayer().send(newMessage);


            }
        });
        STOPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applicationLayer.getLowerLayer().disconnect();
                JFrame closingFrame = new JFrame("Вы разорвали соединение");

                MyDrawPanel panel2 = new MyDrawPanel();
                closingFrame.add(panel2);

                closingFrame.setSize(600, 600);
                closingFrame.repaint();
                closingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //closingFrame.pack();
                closingFrame.setVisible(true);





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

                FileForm fileForm = new FileForm(form,applicationLayer);
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
        this.areaForMessages.append("Приветствую, "+ applicationLayer.getUsers().ToString() + ", собеседник пока не\nподключен, стоит немного подождать\n");



        applicationLayer.setLinkToAppl(this);








    }


   // public get

    public void takeSomething (Object object) {

        if (object instanceof AskingClass)
            areaForMessages.append(((AskingClass) object).giveMessage());

        if (object instanceof MessageClass) {

            areaForMessages.append(((MessageClass) object).getWritenMessage());
        }
//

        if (object instanceof CatalogClass) {

            setCatalogClassTemp((CatalogClass)object);

            applicationLayer.getLinkToAppl().getFileForm().setComboBox1(applicationLayer.getLinkToAppl().fileForm.setExistFiles(applicationLayer));
        }

        if (object instanceof FileNameClass) {

            if(((FileNameClass) object).isTwiceSend()==false){

                ((FileNameClass) object).setTwiceSend(true);
                applicationLayer.getLowerLayer().send(object);

            }
            else {
                String fullPath = new String(applicationLayer.getLinkToAppl().getCatalogClassTemp().getPath().toString() + "\\" + ((FileNameClass) object).getFileName());
                FilesClass imFile = new FilesClass();
                imFile.setWholeFile(((FileNameClass)object).toString());


           imFile.setPath(applicationLayer.getLinkToAppl().getCatalogClassTemp().getPath().toString());

                imFile.incrFile(read(imFile.getFileName().toString()));
                System.out.println(read(imFile.getFileName().toString()));
                applicationLayer.getLowerLayer().send(imFile);
        }}

        if (object instanceof FilesClass ){

            File pathTemp = new File(((FilesClass) object).getPath()+"\\"+((FilesClass) object).getFileName());
            String fileTemp = new String(((FilesClass) object).getWholeFile());

            File file = new File(pathTemp.toString());

            try {
                //проверяем, что если файл не существует то создаем его
                if(!file.exists()){
                    file.createNewFile();
                }

                //PrintWriter обеспечит возможности записи в файл
                PrintWriter out = new PrintWriter(file.getAbsoluteFile());

                try {
                    //Записываем текст у файл
                    out.print(fileTemp);
                } finally {
                    //После чего мы должны закрыть файл
                    //Иначе файл не запишется
                    out.close();
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }






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

    public static String read(String fileName)  {
        File file = new File(fileName);
        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();



        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {

            System.out.println("HHHHHHHHHHHHHHHH");

        }

        //Возвращаем полученный текст с файла
        return sb.toString();
    }





}


class  MyDrawPanel extends JPanel{

    public void paintComponent (Graphics g){



            Image image = new ImageIcon("stopPic.jpg").getImage();
            g.drawImage(image, 3, 4, this);


            System.out.println("DUMB");

        }

    }


