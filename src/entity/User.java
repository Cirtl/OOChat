package src.entity;

public class User {
    private String id;
    private String pass;
    private String ip;
    private boolean isLogin;

    public User(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public User(String id, String pass, String ip) {
        this.id = id;
        this.pass = pass;
        this.ip = ip;
        this.isLogin = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
