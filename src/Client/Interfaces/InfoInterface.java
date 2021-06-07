package src.Client.Interfaces;

public interface InfoInterface {

    /**
     * 用户请求开新房间
     */
    public void newRoom();

    /**
     * 向服务端请求进入某个房间
     */
    public void enterRoom(int  roomPort);

    /**
     * 邀请好友加入房间
     * @param friendID 友人ID
     * @param roomPort 房间号
     */
    public void inviteFriend(String friendID,int roomPort);

    /**
     * 关闭房间
     */
    public void shutRoom(int roomPort);
}
