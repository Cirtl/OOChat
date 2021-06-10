package src.entity;

import java.util.Vector;

public class House {
    private int id;
    private String name;
    private String pass;
    private User host;
    private Vector<User> users;

    public House(int id, String name) {
        this.id = id;
        this.name = name;
        this.users = new Vector<>();
    }

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

    public void setUsers(Vector<User> users) {
        this.users = users;
    }
}
