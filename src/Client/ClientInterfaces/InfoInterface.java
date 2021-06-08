package src.Client.ClientInterfaces;

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
    public void deleteRoom(int port);

    /**
     * 获取我的房间列表
     */
    public void getMyRooms();

    /**
     * 用户请求开新房间
     * @param roomPort 房间号
     * @param pwd 房间密码
     */
    public void newRoom(int roomPort,String pwd);

    /**
     * 向服务端请求进入某个房间
     * @param roomPort 房间号
     * @param pwd 房间密码
     */
    public void enterRoom(int roomPort,String pwd);

    /**
     * 邀请好友加入房间
     * @param friendID 友人ID
     */
    public void inviteFriend(String friendID);

    /**
     * 关闭房间
     */
    public void shutRoom(int roomPort);
}
