package layers.application;
import java.io.Serializable;



public class MessageClass implements Serializable{

    private String writenMessage;


    public void MessageClass() {
        writenMessage = "";


    }

    public void MessageClass(String mes, int inf){
        writenMessage = mes;


    }

    public void setWritenMessage(String writenMessage) {
        this.writenMessage = writenMessage;
    }

    public String getWritenMessage() {
        return writenMessage;
    }




}


