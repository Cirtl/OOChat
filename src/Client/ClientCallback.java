package src.Client;

import java.util.List;

/**
 * 客户端受到服务端信息时的回调
 * 非负数为成功返回码，负数为失败返回码，其他错误一般是指未登录、未进入房间、未登出时进行的无权限操作。
 */
public interface ClientCallback {

    /**
     * 断开连接
     * @param channel 1表示和登录服务器断开，2表示和信息服务器断开，3表示和房间断开
     */
    void onDisconnect(int channel);

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
     * 删除房间
     * @param result 0为成功,-1为房间运行中，-2为房间不存在，-3为不是房主，-4为其他错误
     */
    void onDeleteRoom(int result);

    /**
     * 进入房间
     * @param result 0为成功，-1为失败
     * @param roomPort 成功时返回房间号，失败返回:-1表示用户已在房间内，-2表示房间不存在，-3表示密码错误，-4表示其他错误
     */
    void onEnterRoom(int result,int roomPort);

    /**
     * 新开房间
     * @param result 0为成功，-1为失败
     * @param roomPort 成功时返回房间号，房间号非法返回-1,房间已存在返回-2，其他错误-3
     */
    void onNewRoom(int result,int roomPort);

    /**
     * 离开房间
     * @param result 0为成功，1为由房主移出，2为房间关闭,-3为其他错误
     */
    void onLeaveRoom(int result);

    /**
     * 返回我的房间信息和对应密码
     * @param result 0表示有房间，-1表示无房间，-2其他错误
     * @param rooms 房间号，无房间时为null
     * @param pwd 密码，无房间时为null
     */
    void onMyRoomList(int result,int[] rooms,int[] pwd);

    /**
     * 结交好友
     * @param result 0为成功，-1表示对方拒绝，-2表示用户不在线,-3其他错误
     * @param friendID 失败返回null 成功返回ID
     */
    void onMakeFriend(int result,String friendID);

    /**
     * 收到来自f某个用户的交友邀请
     * @param friendID 对方用户ID
     * @return 同意或拒绝
     */
    boolean onAskedBeFriend(String friendID);

    /**
     * 关闭房间
     * @param result 0为成功，-1表示不是房主，-2其他错误
     */
    void onShutRoom(int result);

    /**
     * 被邀请加入房间
     * @param invitorID 邀请人ID
     * @param roomPort 房间ID
     */
    void onBeingInvited(String invitorID, int roomPort);

    /**
     * 邀请别人的结果
     * @param result 0成功发送邀请，-1房间不存在，-2用户不存在，-3其他错误
     */
    void onInviteFriend(int result);

    /**
     * 产生异常
     * @param e 异常
     */
    void onException(Exception e);

    /**
     * 用户当前房间信息
     * @param result 结果码，成功为0，其他错误为-1
     * @param chatters 如果用户不在房间中,返回null 否则返回当前用户列表，按顺序为每个用户的ID
     */
    void onGetRoomInfo(int result, List<String> chatters);

}
