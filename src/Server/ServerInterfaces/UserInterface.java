package Server.ServerInterfaces;

/**
 * 用户操作接口
 */
public interface UserInterface {

    public static String LOGOUT = "logout";
    public static String LOGIN = "login";
    public static String REGISTER = "register";
    public static String MAKE_FRIEND = "makeFriend";
    public static String INVITE_FRIEND = "inviteFriend";
    public static String BE_INVITED = "beInvited";

    /**
     * 用户邀请友人加入房间
     * @param userID 用户ID
     * @param roomPort 房间端口号
     * @param pwd 房间密码
     * @param friendID 友人ID
     */
    public void inviteFriend(String userID,int roomPort,String pwd,String friendID);

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
     * @param toID 接受者ID
     * @param state 请求状态，包括SUCCESS FAIL UNCONFIRMED 三种，根据不同的请求状态返回不同的值
     */
    public void makeFriend(String toID,String state);

}
