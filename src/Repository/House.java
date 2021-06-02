package Repository;

import java.util.Random;
import java.util.Vector;

/**
 * 房间类
 *
 * @author 郭英贤
 */
public class House {
    private int id;
    private String name;
    private String pass;
    private String ip;
    private String host_id;

    /**
     * 构造新房间实例
     *
     * @param name    房间名
     * @param pass    密码
     * @param host_id 房主ID
     */
    House(String name, String pass, String host_id) {
        this.id = new Random().nextInt(1000000000);
        this.name = name;
        this.pass = pass;
        this.host_id = host_id;
    }

    /**
     * 构造新房间实例
     *
     * @param name    房间名
     * @param pass    密码
     * @param ip      房间IP地址
     * @param host_id 房主ID
     */
    House(String name, String pass, String ip, String host_id) {
        this.id = new Random().nextInt(1000000000);
        this.name = name;
        this.pass = pass;
        this.ip = ip;
        this.host_id = host_id;
    }

    /**
     * Getter 房主ID
     *
     * @return 房主ID
     */
    public String getHost_id() {
        return host_id;
    }

    /**
     * Setter 房主ID
     *
     * @param host_id 房主ID
     */
    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    /**
     * Getter 房间号
     *
     * @return 房间号
     */
    public int getId() {
        return id;
    }

    /**
     * Setter 房间号
     *
     * @param id 房间号
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter 房间名
     *
     * @return 房间名
     */
    public String getName() {
        return name;
    }

    /**
     * Setter 房间名
     *
     * @param name 房间名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter 房间密码
     *
     * @return 房间密码
     */
    public String getPass() {
        return pass;
    }

    /**
     * Setter 房间密码
     *
     * @param pass 房间密码
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Getter 房间IP
     *
     * @return 房间IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * Setter 房间IP地址
     *
     * @param ip 房间IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return 房间内用户ID列表
     */
    public Vector<String> getUserList() {
        return new HandleSearchUserListByHouse().queryVerify(this.id);
    }

    /**
     * 打印房间信息
     */
    public void printInfo() {
        System.out.println("id:" + this.getId() + " name:" + this.getName() +
                " password:" + this.getPass() + " host:" + this.getHost_id());
    }
}
