package layers.application;

public class User{

    private String userName;



    public User(String name) {
        this.userName = name;


    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String ToString(){
        return(userName);

    }

}