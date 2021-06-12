package Server.ServerInterfaces;

/**
 * 聊天室接口
 */
public interface ChatterInterface {

    public static String WHISPER = "whisper";
    public static  String REMOVE_FROM_ROOM = "removeFromRoom";
    public static String LEAVE_ROOM = "leaveRoom";
    public static String SEND_MSG = "sendMsg";
    public static String ROOM_INFO = "roomInfo";

    /**
     * 向房间用户发送私聊
     * @param receiverID 对方ID
     * @param msg 需要发送的消息
     */
    public void whisperMsg(String receiverID,String msg);

    /**
     * 将用户移出房间
     * @param receiverID 目标用户ID
     */
    public void removeFromRoom(String receiverID);

    /**
     * 获取当前房间信息
     */
    public void getRoomInfo();

    /**
     * 离开当前房间
     * @param way 离开方式,0表示自己离开，1表示被放逐，2表示房间关闭
     */
    public void leaveRoom(int way);


    /**
     * 向聊天室发送信息
     * @param msg 内容
     */
    public void sendMsg(String msg);

}
