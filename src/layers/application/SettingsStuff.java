package layers.application;

/**
 don't kill my vibe
 class for settings of COM ports
 */
public class SettingsStuff {

    private String name;
    private String comPorName; //whatta fuck is this
    private int speed;
    private int bitesOfData; //whatta fuck is this
    private int stopBites; //whatta fuck is this
    private boolean quantity; //still don't know



    public void SettingsStuff() {
 /*
        maybe pput here some default name, speed etc.?
 */
        name = "";
        comPorName = "";
        speed = 0;
        bitesOfData = 0;
        stopBites = 0;
        quantity = false;


    }


    public void SettingsStuff(String giveName,String giveCom,int giveSpeed,int giveDataBites,int giveStopBites,boolean giveQuantity) {

        name = giveName;
        comPorName = giveCom;
        speed = giveSpeed;
        bitesOfData = giveDataBites;
        stopBites = giveStopBites;
        quantity = giveQuantity;


    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setComPorName(String comPorName) {
        this.comPorName = comPorName;
    }

    public void setBitesOfData(int bitesOfData) {
        this.bitesOfData = bitesOfData;
    }

    public void setStopBites(int stopBites) {
        this.stopBites = stopBites;
    }

    public void setQuantity(boolean quantity) {
        this.quantity = quantity;
    }

    public String ToString(){

        return (name + " " + comPorName + " " + speed + " " + bitesOfData + " " + stopBites + " " + quantity );

    }





}
