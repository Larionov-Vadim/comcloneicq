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
    private String path;
    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
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
}






