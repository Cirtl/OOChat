package src.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String UNCONFIRMED = "UNCONFIRMED";
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
    private boolean ownRoom;
    private int ownRoomPort;
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
                        for(ClientCallback callback:callbackList)
                            callback.onMakeFriend(0,info[2]);
                    } else if (info[0].equals(FAIL)) {
                        int result;
                        try{
                            result = Integer.parseInt(info[1]);
                        }catch (Exception e){
                            result = -3;
                        }
                        for(ClientCallback callback:callbackList)
                            callback.onMakeFriend(result,info[2]);
                    } else if(info[0].equals(UNCONFIRMED)){
                        boolean flag = true;
                        String toID = info[1];
                        for(ClientCallback callback:callbackList)
                            flag = flag && callback.onAskedBeFriend(toID);
                        if(flag)
                            makeFriend(toID,SUCCESS);
                        else
                            makeFriend(toID,FAIL);
                    }
                }
            }
        });
    }

    private void initChatThread(){
        chatThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveMessage(String option, String[] info) {
                if(option.startsWith(SEND_MSG)){
                    String id = info[0];
                    StringBuilder msg = new StringBuilder();
                    for(int i=1;i<info.length;i++){
                        msg.append(info[i]).append(DIVIDER);
                    }
                    for(ClientCallback callback:callbackList)
                        callback.onReceiveMsg(id, msg.toString(),0);
                }else if(option.startsWith(WHISPER)){
                    String id = info[0];
                    StringBuilder msg = new StringBuilder();
                    for(int i=1;i<info.length;i++){
                        msg.append(info[i]).append(DIVIDER);
                    }
                    for(ClientCallback callback:callbackList)
                        callback.onReceiveMsg(id,msg.toString(),1);
                }else if(option.startsWith(LEAVE_ROOM)){
                    inRoom=false;
                    roomPort=-1;
                    int way = 0;
                    try{
                        way = Integer.parseInt(info[0]);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    for(ClientCallback callback:callbackList)
                        callback.onLeaveRoom(way);
                }else if(option.startsWith(ROOM_INFO)){
                    List<String> users = new ArrayList<>(Arrays.asList(info));
                    for(ClientCallback callback:callbackList)
                        callback.onGetRoomInfo(0,users);
                }
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
                if (option.startsWith(MY_ROOMS)) {
                    if (info[0].startsWith("empty")) {
                        for (ClientCallback callback : callbackList)
                            callback.onMyRoomList(-1, null, null);
                    } else {
                        //读出房间号
                        int[] rooms = new int[info.length];
                        try {
                            for (int i = 0; i < info.length; i++) {
                                int room = Integer.parseInt(info[i]);
                                rooms[i] = room;
                            }
                        } catch (Exception e) {
                            for (ClientCallback callback : callbackList)
                                callback.onMyRoomList(-1, null, null);
                        }
                        for (ClientCallback callback : callbackList)
                            callback.onMyRoomList(0, rooms, rooms);
                    }
                } else if (option.startsWith(InfoInterface.NEW_ROOM)) {
                    int flag;
                    int port = Integer.parseInt(info[1]);
                    if(info[0].equals(SUCCESS)){
                        flag = 0;
                        ownRoom = true;
                        ownRoomPort = port;
                    }
                    else
                        flag = -1;
                    for (ClientCallback callback : callbackList)
                        callback.onNewRoom(flag,port);
                } else if (option.startsWith(InfoInterface.ENTER_ROOM)) {
                    //成功进入房间
                    if(info.length>1){
                        int port = Integer.parseInt(info[1]);
                        if(info[0].equals(SUCCESS)){
                            try{
                                chatThread = new ClientThread(host, port);
                                initChatThread();
                                chatThread.runThread();
                                inRoom = true;
                                roomPort = port;
                                sendMsg(id);
                            }catch (IOException e){
                                inRoom = false;
                                System.out.println(e);
                                for (ClientCallback callback : callbackList)
                                    callback.onEnterRoom(-1,-3);
                            }
                        }else{
                            for (ClientCallback callback : callbackList)
                                callback.onEnterRoom(-1,port);
                        }
                    }
                } else if (option.startsWith(InfoInterface.DELETE_ROOM)) {

                } else if (option.startsWith(InfoInterface.SHUT_ROOM)) {
                    int result = Integer.parseInt(info[1]);
                    if(info[0].equals(SUCCESS)){
                        for (ClientCallback callback : callbackList)
                            callback.onShutRoom(result);
                    }else{
                        for (ClientCallback callback : callbackList)
                            callback.onShutRoom(result);
                    }
                } else if (option.startsWith(BE_INVITED)) {
                    String invitor = info[0];
                    int roomPort = Integer.parseInt(info[1]);
                    for (ClientCallback callback : callbackList)
                        callback.onBeingInvited(invitor,roomPort);
                } else if(option.startsWith(INVITE_FRIEND)){
                    int result = Integer.parseInt(info[0]);
                    for (ClientCallback callback : callbackList)
                        callback.onShutRoom(result);
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
            chatThread.sendMsg(REMOVE_FROM_ROOM + DIVIDER + receiverID);
        }
    }

    @Override
    public void getRoomInfo() {
        if(isLogin&&inRoom){
            chatThread.sendMsg(ROOM_INFO);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onGetRoomInfo(-1,null);
        }
    }

    @Override
    public void leaveRoom() {
        if (isLogin && inRoom) {
            chatThread.sendMsg(LEAVE_ROOM);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onLeaveRoom(-3);
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
        else{
            for(ClientCallback callback:callbackList)
                callback.onDeleteRoom(-3);
        }
    }

    @Override
    public void getMyRooms() {
        if (isLogin)
            infoThread.sendMsg(InfoInterface.MY_ROOMS);
        else{
            for(ClientCallback callback:callbackList)
                callback.onMyRoomList(-1,null,null);
        }
    }

    @Override
    public void newRoom(int roomPort, String pwd) {
        if (isLogin) {
            infoThread.sendMsg(InfoInterface.NEW_ROOM + DIVIDER + id + DIVIDER + roomPort + DIVIDER + pwd);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onNewRoom(-1,-3);
        }
    }

    @Override
    public void enterRoom(int roomPort, String pwd) {
        if (isLogin && !inRoom) {
            infoThread.sendMsg(InfoInterface.ENTER_ROOM + DIVIDER + id + DIVIDER + roomPort + DIVIDER + pwd);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onEnterRoom(-1,-1);
        }
    }

    @Override
    public void inviteFriend(String friendID) {
        if (isLogin && inRoom) {
            String builder = InfoInterface.INVITE_FRIEND + DIVIDER + id  + DIVIDER + roomPort + DIVIDER + friendID;
            infoThread.sendMsg(builder);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onInviteFriend(-1);
        }
    }

    @Override
    public void shutRoom(int roomPort) {
        if (isLogin) {
            infoThread.sendMsg(InfoInterface.SHUT_ROOM + DIVIDER + id + DIVIDER + roomPort);
        }else{
            for(ClientCallback callback:callbackList)
                callback.onShutRoom(-1);
        }
    }

    @Override
    public void userLogout() {
        if (isLogin)
            userThread.sendMsg(LOGOUT);
        else{
            for(ClientCallback callback:callbackList)
                callback.onLogout(-1);
        }
    }

    @Override
    public void userLogin(String id, String pwd) {
        if (!isLogin)
            userThread.sendMsg(UserInterface.LOGIN + DIVIDER + id + DIVIDER + pwd);
        else{
            for(ClientCallback callback:callbackList)
                callback.onLogin(-1,null);
        }
    }

    @Override
    public void userRegister(String id, String pwd) {
        userThread.sendMsg(UserInterface.REGISTER + DIVIDER + id + DIVIDER + pwd);
    }

    @Override
    public void makeFriend(String receiverID) {
        if (isLogin)
            makeFriend(receiverID,UNCONFIRMED);
        else{
            for(ClientCallback callback:callbackList)
                callback.onMakeFriend(-3,null);
        }
    }

    private void makeFriend(String receiverID,String state){
        userThread.sendMsg(UserInterface.MAKE_FRIEND + DIVIDER + receiverID + DIVIDER + state);
    }
}




