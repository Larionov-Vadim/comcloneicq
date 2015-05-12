package layers.application;
import javax.swing.*;
import  java.io.*;
import java.io.Serializable;


/**
хурма про файлики
 */
public class FilesClass implements Serializable {

    /* Вывести содержимое каталога, каталог - фиксированная папочка, но где? надо ли её задавать??
    * функция взять файл TODO
    *если захотелось файл - кнопка получить файл, выводится список доступных, в это время хер ты что сделаешь, пишешь название файлов,
    *внизу по лэйблу идут сообщения
    *как докачался - можно открыть
    *
    *
    *
    *
    * */


    private String fileName;
    private String wholeFile;
    private String path;

    public String getWholeFile() {
        return wholeFile;
    }

    public void setWholeFile(String symbol) {
        this.wholeFile="";
    }

    public void incrFile(String symb){

        wholeFile = wholeFile + symb;

    }
    public void wholeFile(String name)
    {
        this.fileName = name;


    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;


    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //private String   ????? TODO
    //подумай


   // public String fileReader(){

        //File myFile = new File("Test.txt");
        //FileReader fileReader = new FileReader(myFile);

//}

    public void workMethod(){






    }



    }






