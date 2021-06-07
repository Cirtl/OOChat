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

    }

    @Override
    public void removeFromRoom(String receiverID) {

    }

    @Override
    public String getRoomInfo() {
        return null;
    }

    @Override
    public void leaveRoom() {

    }

    @Override
    public void sendMsg(String msg) {

    }

    @Override
    public void newRoom() {

    }

    @Override
    public void enterRoom(int roomPort) {

    }

    @Override
    public void inviteFriend(String friendID, int roomPort) {

    }

    @Override
    public void shutRoom(int roomPort) {

    }

    @Override
    public void userLogout() {

    }

    @Override
    public void userLogin(String id, String pwd) {

    }

    @Override
    public void userRegister(String id, String pwd) {

    }

    @Override
    public void makeFriend(String senderID, String receiverID) {

    }
}




