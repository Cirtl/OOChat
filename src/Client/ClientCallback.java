package src.Client;

/**
 * 客户端受到服务端信息时的回调
 */
public interface ClientCallback {

    /**
     * 受到信息
     * @param senderName 发送者
     * @param msg 信息
     * @param isWhisper 是否是私聊信息，1表示是，0表示不是
     */
    void onReceiveMsg(String senderName,String msg,int isWhisper);

    /**
     * 登录结果
     * @param result 0为成功，-1为失败
     * @param ID 用户ID，失败时返回null
     */
    void onLogin(int result,String ID);

    /**
     * 登录结果
     * @param result 0为成功，-1为失败
     */
    void onRegister(int result);

    /**
     * 用户登出
     * @param result 0为成功，-1为失败
     */
    void onLogout(int result);

    /**
     * 进入房间
     * @param result 0为成功，-1为失败
     * @param roomPort 成功时返回房间号，失败返回-1
     */
    void onEnterRoom(int result,int roomPort);

    /**
     * 离开房间
     * @param result 0为成功，1为由房主移出，2为房间关闭
     */
    void onLeaveRoom(int result);

    /**
     * 结交好友
     * @param result 0为成功，-1表示对方拒绝，-2表示用户不存在
     */
    void onMakeFriend(int result);

    /**
     * 关闭房间
     * @param result 0为成功，-1表示无权限
     */
    void onShutRoom(int result);

    /**
     * 产生异常
     * @param e 异常
     */
    void onException(Exception e);
}
