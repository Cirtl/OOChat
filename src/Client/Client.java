package src.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.Client.ClientThread.ChatInterface;
import src.Client.ClientThread.ClientThread;

/**
 * 向UI层提供的接口
 */
public class Client {
    private static int port_info = 8003;
    private static int port_chat = 8001;
    private static int port_user = 8000;
    private static int maxNum = 10;
    private static String host = "0.0.0.0";
    private ClientThread chatThread,userThread,infoThread;
    private List<ClientCallback> callbackList;
    private boolean isLogin;
    private String name;
    private String id;
    public Client() throws IOException {
        chatThread = new ClientThread(host,port_chat);
        initChatThread();
        userThread = new ClientThread(host,port_user);
        initUserThread();
        infoThread = new ClientThread(host,port_info);
        initInfoThread();
        callbackList = new ArrayList<>();
        isLogin = false;
    }

    /**
     * 当前用户登出
     */
    public void userLogout(){
        if(isLogin)
            userThread.sendMsg("logout"+"\n"+id);
    }

    /**
     * 登录账号
     * @param id 用户id
     * @param pwd 用户密码
     */
    public void userLogin(String id,String pwd){
        userThread.sendMsg("login"+"\n"+id+" "+pwd);
    }

    /**
     * 注册用户
     * @param id 新用户id
     * @param pwd 新用户密码
     */
    public void userRegister(String id,String pwd){
        userThread.sendMsg("register"+"\n"+id+" "+pwd);
    }

    /**
     * 当用户处于登录状态时，向聊天室发送信息
     * @param msg 内容
     */
    public void sendMsg(String msg){
        if(isLogin)
            chatThread.sendMsg(name+":"+msg);
    }

    /**
     * 添加回调
     * @param callback 登录注册回调
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

    /**
     * 当前用户名
     * @return 未登录返回Null 否则返回用户名
     */
    public String getName() {
        return name;
    }

    private void initUserThread(){
        userThread.addCallback(new ChatInterface() {

            @Override
            public void onReceiveObject(String objectName, Object o) {

            }

            @Override
            public void onReceiveMessage(String msg) {
                String[] info = msg.split(" ");
                if(info[0].equals("login success")){
                    for(ClientCallback callback:callbackList)
                        callback.onLoginSuccess();
                }
                else if(info[0].equals("login fail")){
                    for(ClientCallback callback:callbackList)
                        callback.onLoginFailed();
                }
                else if(info[0].equals("register success")){
                    for(ClientCallback callback:callbackList)
                        callback.onRegisterSuccess();
                }
                else if(info[0].equals("register fail")){
                    for(ClientCallback callback:callbackList)
                        callback.onRegisterFailed();
                }
            }
        });
    }

    private void initChatThread(){
        chatThread.addCallback(new ChatInterface() {
            @Override
            public void onReceiveMessage(String msg) {
                System.out.println(msg);
            }

            @Override
            public void onReceiveObject(String objectName, Object o) {

            }
        });
    }

    private void initInfoThread(){

    }


}




