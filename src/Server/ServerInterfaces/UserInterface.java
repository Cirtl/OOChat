package Server.ServerInterfaces;

public interface UserInterface {

    public static String LOGOUT = "logout";
    public static String LOGIN = "login";
    public static String REGISTER = "register";
    public static String MAKE_FRIEND = "makeFriend";


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
