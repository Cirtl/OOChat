package src.Client.Interfaces;

public interface UserInterface {

    /**
     * 当前用户登出请求
     */
    public void userLogout();

    /**
     * 登录账号请求
     * @param id 用户id
     * @param pwd 用户密码
     */
    public void userLogin(String id,String pwd);

    /**
     * 注册用户请求
     * @param id 新用户id
     * @param pwd 新用户密码
     */
    public void userRegister(String id,String pwd);

    /**
     * 交友请求
     * @param senderID 发送者id
     * @param receiverID 接受者id
     */
    public void makeFriend(String senderID,String receiverID);

}
