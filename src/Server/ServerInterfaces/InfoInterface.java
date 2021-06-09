package Server.ServerInterfaces;

public interface InfoInterface {
    public static String DELETE_ROOM = "deleteRoom";
    public static String MY_ROOMS = "myRooms";
    public static String NEW_ROOM = "newRoom";
    public static String ENTER_ROOM = "enterRoom";
    public static String SHUT_ROOM = "shutRoom";
    public static String RUN_ROOM = "runRoom";
    public static String GET_FRIENDS = "getFriends";

    /**
     * 删除房间
     * @param port 房间号
     */
    public void deleteRoom(String userID,int port);

    /**
     * 获取我的房间列表
     */
    public void getMyRooms();

    /**
     * 用户开房间
     * @param userID 用户ID
     * @param roomPort 房间端口
     * @param pwd 房间密码
     */
    public void newRoom(String userID,int roomPort,String pwd);

    /**
     * 用户进入房间
     * @param userID 用户ID
     * @param roomPort 房间端口
     * @param pwd 房间密码
     */
    public void enterRoom(String userID,int roomPort,String pwd);


    /**
     * 关闭房间
     */
    public void shutRoom(String userID,int roomPort);

    /**
     * 运行房间
     * @param userID 发起者
     * @param roomPort 房间号
     */
    public void runRoom(String userID,int roomPort);

    /**
     * 获取好友列表
     * @param userID 用户ID
     */
    public void getFriends(String userID);
}
