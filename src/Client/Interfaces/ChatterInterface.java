package src.Client.Interfaces;

public interface ChatterInterface {

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
     * @return 如果用户不在房间中,返回null
     */
    public String getRoomInfo();

    /**
     * 离开当前房间
     */
    public void leaveRoom();


    /**
     * 向聊天室发送信息
     * @param msg 内容
     */
    public void sendMsg(String msg);

}
