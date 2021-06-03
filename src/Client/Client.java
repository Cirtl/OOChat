package src.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.Client.ClientThread.ChatCallback;
import src.Client.ClientThread.ClientThread;

/**
 * 向UI层提供的接口
 * 顺序 user->info->chat
 */
public class Client {
    private static final String DIVIDER = " ";
    private static int port_info = 8001;
    private static int port_user = 8000;
    private static int maxNum = 10;
    private static String host = "0.0.0.0";
    private ClientThread chatThread,userThread,infoThread;
    private List<ClientCallback> callbackList;
    private boolean isLogin;
    private boolean inRoom;
    private String name;
    private String id;

    public Client() throws IOException {
        callbackList = new ArrayList<>();
        isLogin = false;
        inRoom  = false;
        userThread = new ClientThread(host,port_user);
        infoThread = new ClientThread(host,port_info);
        initUserThread();
        initInfoThread();
    }

    /**
     * 获取当前房间信息
     * @return 如果用户不在房间中,返回null
     */
    public String getRoomInfo(){
        //TODO:完成获取房间信息
        return  null;
    }

    /**
     * 用户离开当前房间
     */
    public void leaveRoom(){
        if(inRoom)
            chatThread.sendMsg("leave_room" + DIVIDER + id);
    }

    /**
     * 当前用户登出
     */
    public void userLogout(){
        if(isLogin)
            userThread.sendMsg("logout"+ DIVIDER +id);
    }

    /**
     * 登录账号
     * @param id 用户id
     * @param pwd 用户密码
     */
    public void userLogin(String id,String pwd){
        if(!isLogin)
            userThread.sendMsg("login"+ DIVIDER +id+ DIVIDER +pwd);
    }

    /**
     * 注册用户
     * @param id 新用户id
     * @param pwd 新用户密码
     */
    public void userRegister(String id,String pwd){
        userThread.sendMsg("register"+DIVIDER+id+DIVIDER+pwd);
    }

    /**
     * 当用户处于登录状态时，向聊天室发送信息
     * @param msg 内容
     */
    public void sendMsg(String msg){
        if(inRoom)
            chatThread.sendMsg(name+DIVIDER+msg);
    }

    /**
     * 向服务端请求房间端口
     */
    public void enterRoom(String roomID){
        if(isLogin&&!inRoom)
            infoThread.sendMsg("room_port");
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
     * @return 未登录返回false，已登录返回true
     */
    public boolean isLogin() {
        return isLogin;
    }

    public boolean isInRoom() {
        return inRoom;
    }

    /**
     * 当前用户名
     * @return 未登录返回Null 否则返回用户名
     */
    public String getName() {
        return name;
    }

    private void initUserThread(){
        userThread.addCallback(new ChatCallback() {
            @Override
            public void onReceiveObject(String objectName, Object o) {

            }

            @Override
            public void onReceiveMessage(String option,String[] info) {
                if(option.equals("login_success")){
                    isLogin = true;
                    for(ClientCallback callback:callbackList)
                        callback.onLoginSuccess();
                }
                else if(option.equals("login_fail")){
                    for(ClientCallback callback:callbackList)
                        callback.onLoginFailed();
                }
                else if(option.equals("register_success")){
                    for(ClientCallback callback:callbackList)
                        callback.onRegisterSuccess();
                }
                else if(option.equals("register_fail")){
                    for(ClientCallback callback:callbackList)
                        callback.onRegisterFailed();
                }
                else if(option.equals("logout_success")){
                    isLogin  = false;
                    for (ClientCallback callback:callbackList)
                        callback.onLogoutSuccess();
                }
            }
        });
    }

    private void initChatThread(int port_chat) throws IOException {
            chatThread = new ClientThread(host,port_chat);
            chatThread.addCallback(new ChatCallback() {
                @Override
                public void onReceiveMessage(String option,String[] info) {
                    if(option.equals("msg"))
                        for(ClientCallback clientCallback:callbackList)
                            clientCallback.onReceiveMsg(info[0],info[1]);
                    else if(option.equals("leave_room")){
                        inRoom = false;
                        for(ClientCallback clientCallback:callbackList)
                            clientCallback.onLeaveRoom();
                        chatThread.closeThread();
                        chatThread = null;
                    }
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
                if(option.equals("room_port")){
                    //收到聊天端口后开启新的聊天服务器
                    try{
                        System.out.println(info[0]);
                        int port = Integer.parseInt(info[0]);
                        initChatThread(port);
                        inRoom = true;
                        for(ClientCallback callback:callbackList)
                            callback.onEnterRoom();
                    } catch (IOException e) {
                        inRoom = false;
                        for(ClientCallback callback:callbackList)
                            callback.onException(e);
                    }
                }
            }
            @Override
            public void onReceiveObject(String objectName, Object o) {

            }
        });
    }

}




