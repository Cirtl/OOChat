package Server.ServerInterfaces;

public interface InfoInterface {
    public static String DELETE_ROOM = "deleteRoom";
    public static String MY_ROOMS = "myRooms";
    public static String NEW_ROOM = "newRoom";
    public static String ENTER_ROOM = "enterRoom";
    public static String INVITE_FRIEND = "inviteFriend";
    public static String SHUT_ROOM = "shutRoom";

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
     * 用户邀请友人加入房间
     * @param userID 用户ID
     * @param roomPort 房间端口号
     * @param friendID 友人ID
     */
    public void inviteFriend(String userID,int roomPort,String friendID);

    /**
     * 关闭房间
     */
    public void shutRoom(String userID,int roomPort);
}
