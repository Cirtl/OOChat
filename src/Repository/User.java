package Repository;

import javax.swing.*;

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

    public void setLogin(boolean login) {
        isLogin = login;
    }

    private boolean judgeLogin(){
        if(!isLogin){
            JOptionPane.showMessageDialog(null, "请先登录", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean register() {
        if (new HandleRegister().writeRegisterModel(this)) {
            UserList.getInstance().getUserList().add(this);
            return true;
        }
        return false;
    }

    public boolean login() {
        isLogin = new HandleLogin().queryVerify(this.id, this.pass);
        return isLogin;
    }



    public void logout() {
        isLogin = false;
    }

    public int createHouse(String houseName, String pass) {
        if(!judgeLogin()){
            return -1;
        }
        House house = new House(houseName, pass, this.id);
        if(new HandleCreateHouse().writeRegisterModel(house)!=-1){
            HouseList.getInstance().getHouseList().add(house);
            return house.getId();
        }
        return -1;
    }

    public boolean enterHouse(int houseId, String pass) {
        if(!judgeLogin() || !HouseList.getInstance().judgeHouseExist(houseId)){
            return false;
        }
        if(new HandleIsUserInHouse().queryVerify(this.id,houseId)){
            JOptionPane.showMessageDialog(null, "已经在房间，不能重复进入", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleEnterHouse().queryVerify(this.id, houseId, pass);
    }

    public void quitHouse(int houseId) {
        if(!judgeLogin() || !HouseList.getInstance().judgeHouseExist(houseId)){
            return;
        }
        if(!new HandleIsUserInHouse().queryVerify(this.id,houseId)){
            JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(new HandleQuitHouse().queryVerify(this.id, houseId) == -1){
            HouseList.getInstance().removeById(houseId);
        }
    }

    public boolean transferHouse(int houseId, String newHostId) {
        if(!judgeLogin() || !HouseList.getInstance().judgeHouseExist(houseId)){
            return false;
        }
        if(!HouseList.getInstance().searchById(houseId).getHost_id().equals(this.id)){
            JOptionPane.showMessageDialog(null, "不是群主，无权限换群主", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(newHostId.equals(this.id)){
            JOptionPane.showMessageDialog(null, "群主已经是本人", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleTransferHouse().queryVerify(houseId, this.id, newHostId);
    }

    public boolean sendMessage(int houseId, String message) {
        if(!judgeLogin() || !HouseList.getInstance().judgeHouseExist(houseId)){
            return false;
        }
        if(!new HandleIsUserInHouse().queryVerify(this.id,houseId)){
            JOptionPane.showMessageDialog(null, "不在房间", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return new HandleSendMessage().queryVerify(this.id, houseId, message);
    }
}
