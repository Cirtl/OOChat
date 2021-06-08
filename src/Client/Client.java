package src.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.Client.ClientThread.ChatCallback;
import src.Client.ClientThread.ClientThread;
import src.Client.ClientInterfaces.ChatterInterface;
import src.Client.ClientInterfaces.InfoInterface;
import src.Client.ClientInterfaces.UserInterface;

/**
 * 向UI层提供的接口
 * 需要手动启动和关闭
 * 顺序 user->info->chat
 */
public class Client implements ChatterInterface, InfoInterface, UserInterface {
    public static final String DISCONNECT = "QUIT";
    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String DIVIDER = " ";
    private static final String host = "0.0.0.0";
    private static final int port_user = 8000;
    private static final int port_info = 8001;
    private ClientThread chatThread;
    private ClientThread userThread;
    private ClientThread infoThread;
    private final List<ClientCallback> callbackList;
    private boolean isLogin;
    private boolean inRoom;
    private String id;
    private int roomPort;

    /**
     * 新建服务端链接
     *
     * @throws IOException 连接失败
     */
    public Client() throws IOException {
        callbackList = new ArrayList<>();
        isLogin = false;
        inRoom = false;
        userThread = new ClientThread(host, port_user);
        infoThread = new ClientThread(host, port_info);
        initUserThread();
        initInfoThread();
    }

    /**
     * 启动服务器
     */
    public void runClient() {
        userThread.runThread();
        infoThread.runThread();
    }

    /**
     * 关闭服务端
     */
    public void closeClient() {
        userThread.closeThread();
        infoThread.closeThread();
        if (chatThread != null)
            chatThread.closeThread();
    }

    /**
     * 添加回调
     *
     * @param callback 回调
     */
    public void addCallBack(ClientCallback callback) {
        callbackList.add(callback);
    }

    /**
     * 移出回调
     *
     * @param callback 需要移出的回调
     */
    public void removeCallBack(ClientCallback callback) {
        callbackList.remove(callback);
    }

    /**
     * 当前是否登录
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * 查询是否在房间中
     */
    public boolean isInRoom() {
        return inRoom;
    }

    /**
     * 当前用户ID
     *
     * @return 未登录返回Null 否则返回ID
     */
    public String getID() {
        return id;
    }


    private void initUserThread() {
        userThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveObject(String objectName, Object o) {
            }

            @Override
            public void onReceiveMessage(String option, String[] info) {
                if (option.startsWith(UserInterface.LOGIN)) {
                    if (info[0].equals(SUCCESS) && info.length > 1) {
                        isLogin = true;
                        id = info[1];
                        for (ClientCallback callback : callbackList)
                            callback.onLogin(0, info[1]);
                    } else if (info[0].equals(FAIL)) {
                        for (ClientCallback callback : callbackList)
                            callback.onLogin(-1, null);
                    }
                } else if (option.startsWith(UserInterface.LOGOUT)) {
                    if (info[0].equals(SUCCESS)) {
                        isLogin = false;
                        id = null;
                        for (ClientCallback callback : callbackList)
                            callback.onLogout(0);
                    } else if (info[0].equals(FAIL)) {
                        for (ClientCallback callback : callbackList)
                            callback.onLogout(-1);
                    }
                } else if (option.startsWith(UserInterface.REGISTER)) {
                    if (info[0].equals(SUCCESS)) {
                        for (ClientCallback callback : callbackList)
                            callback.onRegister(0);
                    } else if (info[0].equals(FAIL)) {
                        for (ClientCallback callback : callbackList)
                            callback.onRegister(-1);
                    }
                } else if (option.startsWith(UserInterface.MAKE_FRIEND)) {
                    if (info[0].equals(SUCCESS)) {

                    } else if (info[0].equals(FAIL)) {

                    }
                }
            }
        });
    }

    private void connectChatRoom(int port_chat) throws IOException {
        try {
            chatThread = new ClientThread(host, port_chat);
            initChatThread(port_chat);
        } catch (IOException e) {
            throw e;
        }
    }

    private void initChatThread(int port_chat) throws IOException {
        chatThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveMessage(String option, String[] info) {

            }

            @Override
            public void onReceiveObject(String objectName, Object o) {
            }
        });
    }

    private void initInfoThread() {
        infoThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveMessage(String option, String[] info) {
                if (option.startsWith(InfoInterface.MY_ROOMS)) {

                } else if (option.startsWith(InfoInterface.NEW_ROOM)) {

                } else if (option.startsWith(InfoInterface.ENTER_ROOM)) {

                } else if (option.startsWith(InfoInterface.DELETE_ROOM)) {

                } else if (option.startsWith(InfoInterface.SHUT_ROOM)) {

                } else if (option.startsWith(InfoInterface.INVITE_FRIEND)) {

                }
            }

            @Override
            public void onReceiveObject(String objectName, Object o) {

            }
        });
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {
        if (isLogin && inRoom) {
            String builder = ChatterInterface.WHISPER + DIVIDER + receiverID + DIVIDER +
                    msg;
            chatThread.sendMsg(builder);
        }

    }

    @Override
    public void removeFromRoom(String receiverID) {
        if (isLogin && inRoom) {
            chatThread.sendMsg(ChatterInterface.REMOVE_FROM_ROOM + DIVIDER + receiverID);
        }
    }

    @Override
    public String getRoomInfo() {
        return String.format("room port:%d", roomPort);
    }

    @Override
    public void leaveRoom() {
        if (isLogin && inRoom) {
            chatThread.sendMsg(ChatterInterface.LEAVE_ROOM);
        }
    }

    @Override
    public void sendMsg(String msg) {
        if (isLogin && inRoom) {
            chatThread.sendMsg(ChatterInterface.SEND_MSG + DIVIDER + msg);
        }
    }

    @Override
    public void deleteRoom(int port) {
        if (isLogin)
            infoThread.sendMsg(InfoInterface.DELETE_ROOM + DIVIDER + id + DIVIDER + port);
    }

    @Override
    public void getMyRooms() {
        if (isLogin)
            infoThread.sendMsg(InfoInterface.MY_ROOMS);
    }

    @Override
    public void newRoom(int roomPort, String pwd) {
        if (isLogin && !inRoom) {
            infoThread.sendMsg(InfoInterface.NEW_ROOM + DIVIDER + id + DIVIDER + roomPort + DIVIDER + pwd);
        }
    }

    @Override
    public void enterRoom(int roomPort, String pwd) {
        if (isLogin && !inRoom) {
            infoThread.sendMsg(InfoInterface.ENTER_ROOM + DIVIDER + id + DIVIDER + roomPort + DIVIDER + pwd);
        }
    }

    @Override
    public void inviteFriend(String friendID) {
        if (isLogin && inRoom) {
            String builder = InfoInterface.INVITE_FRIEND + DIVIDER + friendID + DIVIDER +
                    roomPort;
            infoThread.sendMsg(builder);
        }
    }

    @Override
    public void shutRoom(int roomPort) {
        if (isLogin) {
            infoThread.sendMsg(InfoInterface.SHUT_ROOM + DIVIDER + id + DIVIDER + roomPort);
        }
    }

    @Override
    public void userLogout() {
        if (isLogin)
            userThread.sendMsg(UserInterface.LOGOUT);
    }

    @Override
    public void userLogin(String id, String pwd) {
        if (!isLogin)
            userThread.sendMsg(UserInterface.LOGIN + DIVIDER + id + DIVIDER + pwd);
    }

    @Override
    public void userRegister(String id, String pwd) {
        userThread.sendMsg(UserInterface.REGISTER + DIVIDER + id + DIVIDER + pwd);
    }

    @Override
    public void makeFriend(String receiverID) {
        if (isLogin)
            userThread.sendMsg(UserInterface.MAKE_FRIEND + DIVIDER + receiverID);
    }
}




