package entity;

public class User {
    private String id;
    private String pass;
    private String ip;
    private boolean isLogin;

    public User(String id, String pass, String ip) {
        this.id = id;
        this.pass = pass;
        this.ip = ip;
        this.isLogin = false;
    }
}
