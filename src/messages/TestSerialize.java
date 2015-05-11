package messages;

import java.io.Serializable;

/**
 * Created by Vadim on 11.05.2015.
 */
public class TestSerialize implements Serializable {
    private String login;
    private String message;

    public TestSerialize(){}

    public TestSerialize(String login, String message) {
        this.login = login;
        this.message = message;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void print() {
        System.out.print(login + ": ");
        System.out.println(message);
    }
}
