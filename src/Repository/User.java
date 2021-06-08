package Repository;

import Repository.Handle.User_House.HandleIsUserInHouse;
import Repository.Handle.User_House.HandleSearchHouseListByUser;
import Repository.Handle.House.HandleGetHomePass;
import Repository.Handle.House.HandleIsHost;
import Repository.Handle.House.HandleIsHouse;
import Repository.Handle.House.HandleIsLogin;
import Repository.Handle.User.*;

import java.util.Vector;

/**
 * 用户类
 *
 * @author 郭英贤
 */
public class User {
    private String id;
    private String pass;
    private String ip;

    /**
     * 构造新用户实例
     *
     * @param id   用户ID
     * @param pass 密码
     */
    public User(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    /**
     * 构造新用户实例
     *
     * @param id   用户ID
     * @param pass 密码
     * @param ip   用户IP地址
     */
    public User(String id, String pass, String ip) {
        this.id = id;
        this.pass = pass;
        this.ip = ip;
    }

    /**
     * Getter 用户ID
     *
     * @return 用户ID
     */
    public String getId() {
        return id;
    }

    /**
     * Setter 用户ID
     *
     * @param id 用户ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter 用户密码
     *
     * @return 用户密码
     */
    public String getPass() {
        return pass;
    }

    /**
     * Setter 用户密码
     *
     * @param pass 用户密码
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Getter 用户IP
     *
     * @return 用户IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * Setter 用户IP
     *
     * @param ip 用户IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 打印用户信息
     */
    public void printInfo() {
        System.out.println("id:" + this.getId() + " password" + this.getPass() +
                " ip:" + this.getIp() + " isLogin:" + new HandleIsLogin().queryVerify(id));
    }

    /**
     * @return 用户所在房间号列表
     */
    public Vector<String> getHouseList() {
        return new HandleSearchHouseListByUser().queryVerify(this.id);
    }

    /**
     * 判断登录状态
     *
     * @return 是否成功登录
     */
    private boolean judgeLogin() {
        if (!new HandleIsLogin().queryVerify(this.id)) {
            // JOptionPane.showMessageDialog(null, "请先登录", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 注册
     *
     * @return 注册成功与否
     */
    public boolean register() {
        return new HandleRegister().writeRegisterModel(this);
    }

    /**
     * 登录
     *
     * @return 登录成功与否
     */
    public boolean login() {
        if (new HandleIsLogin().queryVerify(this.id)) {
            // JOptionPane.showMessageDialog(null, "您已登录，不能重复登陆", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleLogin().queryVerify(this.id, this.pass);
    }

    /**
     * 注销
     *
     * @return 注销成功与否
     */
    public boolean logout() {
        return new HandleLogout().queryVerify(this.id, this.pass);
    }

    /**
     * 创建房间
     * 默认创建者为该房间房主
     *
     * @param houseName 房间名
     * @param pass      房间密码
     * @return 创建成功返回房间号，创建失败返回-1
     */
    public int createHouse(String houseName, String pass) {
        if (!judgeLogin()) {
            return -1;
        }
        House house = new House(houseName, pass, this.id);
        return new HandleCreateHouse().writeRegisterModel(house);
    }

    /**
     * 进入房间
     *
     * @param houseId 房间号
     * @param pass    房间密码
     * @return 进入房间成功与否
     */
    public boolean enterHouse(int houseId, String pass) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return false;
        }
        if (new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            // JOptionPane.showMessageDialog(null, "已经在房间，不能重复进入", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleEnterHouse().queryVerify(this.id, houseId, pass);
    }

    /**
     * 退出房间
     *
     * @param houseId 房间号
     */
    public void quitHouse(int houseId) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return;
        }
        if (!new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            // JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new HandleQuitHouse().queryVerify(this.id, houseId);
    }

    /**
     * 转移房主
     *
     * @param houseId   房间号
     * @param newHostId 新房主ID
     * @return 转移房主成功与否
     */
    public boolean transferHouse(int houseId, String newHostId) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId) || !new HandleIsHost().queryVerify(this.id, houseId)) {
            return false;
        }
        if (newHostId.equals(this.id)) {
            // JOptionPane.showMessageDialog(null, "群主已经是本人", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleTransferHouse().queryVerify(houseId, this.id, newHostId);
    }

    /**
     * 发消息
     *
     * @param houseId 房间号
     * @param message 消息
     * @return 发消息成功与否
     */
    public boolean sendMessage(int houseId, String message) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return false;
        }
        if (!new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            // JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleSendMessage().queryVerify(this.id, houseId, message);
    }

    /**
     * 添加好友
     *
     * @param id 对方ID
     * @return 添加好友成功与否
     */
    public boolean addFriend(String id) {
        if (!judgeLogin()) {
            return false;
        }
        if (id.equals(this.id)) {
            // JOptionPane.showMessageDialog(null, "不能和自己加好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsUser().queryVerify(id)) {
            // JOptionPane.showMessageDialog(null, "不存在此用户", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (new HandleIsFriend().queryVerify(this.id, id)) {
            // JOptionPane.showMessageDialog(null, "你们已经是好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleAddFriend().writeRegisterModel(this.id, id);
    }

    /**
     * 双向删除好友
     *
     * @param id 好友ID
     * @return 删除好友成功与否
     */
    public boolean deleteFriend(String id) {
        if (!judgeLogin()) {
            return false;
        }
        if (id.equals(this.id)) {
            // JOptionPane.showMessageDialog(null, "不能和自己删好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsUser().queryVerify(id)) {
            // JOptionPane.showMessageDialog(null, "不存在此用户", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsFriend().queryVerify(this.id, id)) {
            // JOptionPane.showMessageDialog(null, "你们还不是好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return !new HandleDeleteFriend().queryVerify(this.id, id);
    }

    /**
     * 邀请好友进入房间
     *
     * @param friendId 好友ID
     * @param houseId  房间号
     * @return 成功邀请与否
     */
    public boolean inviteFriendToHouse(String friendId, int houseId) {
        if (!judgeLogin()) {
            return false;
        }
        if (!new HandleIsFriend().queryVerify(id, friendId)) {
            // JOptionPane.showMessageDialog(null, "不存在该好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (new HandleIsUserInHouse().queryVerify(friendId, houseId)) {
            // JOptionPane.showMessageDialog(null, "好友已经在房间，不能重复进入", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleEnterHouse().queryVerify(friendId, houseId, new HandleGetHomePass().queryVerify(houseId));
    }
}
