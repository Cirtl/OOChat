package src.entity;

import java.util.Vector;

public class House {
    private int id;
    private String name;
    private String pass;
    private User host;
    private Vector<User> users;

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public User getHost() {
        return host;
    }

    public Vector<User> getUsers() {
        return users;
    }
}
