package Repository;

import java.util.Random;
import java.util.Vector;

public class House {
    private int id;
    private String name;
    private String pass;
    private String ip;
    private String host_id;

    House(String name, String pass, String host_id) {
        this.id = new Random().nextInt(1000000000);
        this.name = name;
        this.pass = pass;
        this.host_id = host_id;
    }

    House(String name, String pass, String ip, String host_id) {
        this.id = new Random().nextInt(1000000000);
        this.name = name;
        this.pass = pass;
        this.ip = ip;
        this.host_id = host_id;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Vector<String> getUserList() {
        return new HandleSearchUserListByHouse().queryVerify(this.id);
    }

    public void printInfo() {
        System.out.println("id:" + this.getId() + " name:" + this.getName() +
                    " password:" + this.getPass() + " host:" + this.getHost_id());
    }
}
