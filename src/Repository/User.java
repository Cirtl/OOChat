package Repository;

import javax.swing.*;
import java.util.Vector;

public class User {
    private String id;
    private String pass;
    private String ip;

    public User(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public User(String id, String pass, String ip) {
        this.id = id;
        this.pass = pass;
        this.ip = ip;
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

    public void printInfo() {
        System.out.println("id:" + this.getId() + " password" + this.getPass() +
                    " ip:" + this.getIp() + " isLogin:" + new HandleIsLogin().queryVerify(id));
    }

    public Vector<String> getHouseList() {
        return new HandleSearchHouseListByUser().queryVerify(this.id);
    }

    private boolean judgeLogin() {
        if (!new HandleIsLogin().queryVerify(this.id)) {
            JOptionPane.showMessageDialog(null, "请先登录", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean register() {
        return new HandleRegister().writeRegisterModel(this);
    }

    public boolean login() {
        if (new HandleIsLogin().queryVerify(this.id)) {
            JOptionPane.showMessageDialog(null, "您已登录，不能重复登陆", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleLogin().queryVerify(this.id, this.pass);
    }


    public boolean logout() {
        return new HandleLogout().queryVerify(this.id, this.pass);
    }

    public int createHouse(String houseName, String pass) {
        if (!judgeLogin()) {
            return -1;
        }
        House house = new House(houseName, pass, this.id);
        return new HandleCreateHouse().writeRegisterModel(house);
    }

    public boolean enterHouse(int houseId, String pass) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return false;
        }
        if (new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            JOptionPane.showMessageDialog(null, "已经在房间，不能重复进入", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleEnterHouse().queryVerify(this.id, houseId, pass);
    }

    public void quitHouse(int houseId) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return;
        }
        if (!new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new HandleQuitHouse().queryVerify(this.id, houseId);
    }

    public boolean transferHouse(int houseId, String newHostId) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId) || !new HandleIsHost().queryVerify(this.id, houseId)) {
            return false;
        }
        if (newHostId.equals(this.id)) {
            JOptionPane.showMessageDialog(null, "群主已经是本人", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleTransferHouse().queryVerify(houseId, this.id, newHostId);
    }

    public boolean sendMessage(int houseId, String message) {
        if (!judgeLogin() || !new HandleIsHouse().queryVerify(houseId)) {
            return false;
        }
        if (!new HandleIsUserInHouse().queryVerify(this.id, houseId)) {
            JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleSendMessage().queryVerify(this.id, houseId, message);
    }

    public boolean addFriend(String id) {
        if (!judgeLogin()) {
            return false;
        }
        if (id.equals(this.id)) {
            JOptionPane.showMessageDialog(null, "不能和自己加好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsUser().queryVerify(id)) {
            JOptionPane.showMessageDialog(null, "不存在此用户", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (new HandleIsFriend().queryVerify(this.id, id)) {
            JOptionPane.showMessageDialog(null, "你们已经是好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleAddFriend().writeRegisterModel(this.id, id);
    }

    public boolean deleteFriend(String id) {
        if (!judgeLogin()) {
            return false;
        }
        if (id.equals(this.id)) {
            JOptionPane.showMessageDialog(null, "不能和自己删好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsUser().queryVerify(id)) {
            JOptionPane.showMessageDialog(null, "不存在此用户", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new HandleIsFriend().queryVerify(this.id, id)) {
            JOptionPane.showMessageDialog(null, "你们还不是好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (new HandleDeleteFriend().queryVerify(this.id,id) == -1) {
            return false;
        }
        return true;
    }

    public boolean inviteFriendToHouse(String friendId, int houseId) {
        if (!judgeLogin()) {
            return false;
        }
        if (!new HandleIsFriend().queryVerify(id, friendId)) {
            JOptionPane.showMessageDialog(null, "不存在该好友", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (new HandleIsUserInHouse().queryVerify(friendId, houseId)) {
            JOptionPane.showMessageDialog(null, "好友已经在房间，不能重复进入", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleEnterHouse().queryVerify(friendId, houseId, new HandleGetHomePass().queryVerify(houseId));
    }
}
