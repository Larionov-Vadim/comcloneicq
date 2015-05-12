package layers.application;

public class User{

    private String userName;
    static private int id;


    public User(String name) {

        this.userName = name;
        this.id ++;

    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.id ++;
    }

    public String ToString(){

        return(userName);

    }

}