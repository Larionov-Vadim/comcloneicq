package layers.application;



/**



 */
public class MessageClass {

    private String writenMessage;
    private int infoValue;

    public void MessageClass() {

        writenMessage = "";
        infoValue = 1488;

    }

    public void MessageClass(String mes, int inf){
        writenMessage = mes;
        infoValue = inf;

    }

    public void setWritenMessage(String writenMessage) {
        this.writenMessage = writenMessage;
    }

    public void setInfoValue(int infoValue) {
        this.infoValue = infoValue;
    }

    public String getWritenMessage() {
        return writenMessage;
    }

    public int getInfoValue() {
        return infoValue;
    }


}


