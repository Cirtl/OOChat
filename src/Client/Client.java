package src.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.Client.ClientThread.ChatCallback;
import src.Client.ClientThread.ClientThread;
import src.Client.Interfaces.ChatterInterface;
import src.Client.Interfaces.InfoInterface;
import src.Client.Interfaces.UserInterface;

/**
 * 向UI层提供的接口
 * 需要手动启动和关闭
 * 顺序 user->info->chat
 */
public class Client implements ChatterInterface, InfoInterface, UserInterface {
    private static final String DIVIDER = " ";
    private static final String host = "0.0.0.0";
    private static final int port_user = 8000;
    private static final int port_info = 8001;
    private ClientThread chatThread;
    private final ClientThread userThread;
    private final ClientThread infoThread;
    private final List<ClientCallback> callbackList;
    private boolean isLogin;
    private boolean inRoom;
    private String id;
    private int roomPort;
    /**
     * 新建服务端链接
     * @throws IOException 连接失败
     */
    public Client() throws IOException {
        callbackList = new ArrayList<>();
        isLogin = false;
        inRoom  = false;
        userThread = new ClientThread(host, port_user);
        infoThread = new ClientThread(host, port_info);
        initUserThread();
        initInfoThread();
    }

    /**
     * 启动服务器
     */
    public void runClient(){
        userThread.runThread();
        infoThread.runThread();
    }

    /**
     * 关闭服务端
     */
    public void closeClient(){
        userThread.closeThread();
        infoThread.closeThread();
        if(chatThread!=null)
            chatThread.closeThread();
    }

    /**
     * 添加回调
     * @param callback 回调
     */
    public void addCallBack(ClientCallback callback){
        callbackList.add(callback);
    }

    /**
     * 移出回调
     * @param callback 需要移出的回调
     */
    public void removeCallBack(ClientCallback callback){
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
     * @return 未登录返回Null 否则返回ID
     */
    public String getID() {
        return id;
    }


    private void initUserThread(){
        userThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveObject(String objectName, Object o) {
            }

            @Override
            public void onReceiveMessage(String option,String[] info) {
            }
        });
    }

    private void connectChatRoom(int port_chat) throws IOException {
        try{
            chatThread = new ClientThread(host,port_chat);
            initChatThread(port_chat);
        }catch (IOException e) {
            throw e;
        }
    }

    private void initChatThread(int port_chat) throws IOException {
            chatThread.addCallback(new ChatCallback() {
                @Override
                public void onReceiveMessage(String option,String[] info) {

                }

                @Override
                public void onReceiveObject(String objectName, Object o) {
                }
            });
    }

    private void initInfoThread(){
        infoThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveMessage(String option,String[] info) {
            }
            @Override
            public void onReceiveObject(String objectName, Object o) {

            }
        });
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {
        if(isLogin&&inRoom){
            String builder = "whisper" +  DIVIDER + receiverID + DIVIDER +
                    msg;
            chatThread.sendMsg(builder);
        }

    }

    @Override
    public void removeFromRoom(String receiverID) {
        if(isLogin&&inRoom){
            chatThread.sendMsg("removeFromRoom" + DIVIDER + receiverID);
        }
    }

    @Override
    public String getRoomInfo() {
        return String.format("room port:%d",roomPort);
    }

    @Override
    public void leaveRoom() {
        if(isLogin&&inRoom){
            chatThread.sendMsg("leaveRoom");
        }
    }

    @Override
    public void sendMsg(String msg) {
        if(isLogin&&inRoom){
            chatThread.sendMsg("sendMsg" + DIVIDER + msg);
        }
    }

    @Override
    public void deleteRoom(int port) {
        if(isLogin)
            infoThread.sendMsg("deleteRoom"+DIVIDER+port);
    }

    @Override
    public void getMyRooms() {
        if(isLogin)
            infoThread.sendMsg("myRooms");
    }

    @Override
    public void newRoom(int pwd) {
        if(isLogin&&!inRoom){
            infoThread.sendMsg("newRoom" + DIVIDER + id + DIVIDER + pwd);
        }
    }

    @Override
    public void enterRoom(int roomPort,int pwd) {
        if(isLogin&&!inRoom){
            infoThread.sendMsg("enterRoom" + DIVIDER + roomPort + DIVIDER + id + DIVIDER + roomPort + DIVIDER + pwd);
        }
    }

    @Override
    public void inviteFriend(String friendID) {
        if(isLogin&&inRoom){
            String builder = "inviteFriend" + DIVIDER + friendID + DIVIDER +
                    roomPort;
            infoThread.sendMsg(builder);
        }
    }

    @Override
    public void shutRoom(int roomPort) {
        if(isLogin&&inRoom){
            infoThread.sendMsg("shutRoom" + DIVIDER + roomPort);
        }
    }

    @Override
    public void userLogout() {
        if(isLogin)
            userThread.sendMsg("logout");
    }

    @Override
    public void userLogin(String id, String pwd) {
        if(isLogin)
            userThread.sendMsg("login" + DIVIDER + id + DIVIDER + pwd);
    }

    @Override
    public void userRegister(String id, String pwd) {
        userThread.sendMsg("register" + DIVIDER + id + DIVIDER + pwd);
    }

    @Override
    public void makeFriend(String receiverID) {
        if(isLogin)
            userThread.sendMsg("makeFriend" +  DIVIDER + receiverID);
    }
}




