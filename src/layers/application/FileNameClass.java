package layers.application;

import java.io.Serializable;



public class FileNameClass implements Serializable{
    private String fileName;
    private Boolean twiceSend;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean isTwiceSend() {
        return twiceSend;
    }

    public void setTwiceSend(Boolean twiceSend) {
        this.twiceSend = twiceSend;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
