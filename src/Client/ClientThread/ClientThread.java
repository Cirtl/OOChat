package src.Client.ClientThread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread {
    ServerMsgInput serverMsgInput;
    ServerMsgOutput serverMsgOutput;
    Socket socket;
    List<ChatCallback> chatCallbackList = new ArrayList<>();

    /**
     * 建立客户端并连接服务器
     * @param host 服务器地址
     * @param port 服务器端口号
     * @throws IOException 链接服务器失败
     */
    public ClientThread(String host, int port) throws IOException {
        initConnect(host, port);
        runThread();
    }

    public void initConnect(String host, int port) throws IOException {
        socket = new Socket(host,port);
        serverMsgInput = new ServerMsgInput(socket, chatCallbackList);
        serverMsgOutput = new ServerMsgOutput(socket);
    }

    public void closeThread(){
        try{
            serverMsgOutput.close();
            serverMsgInput.close();
            socket.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public void runThread(){
        new Thread(serverMsgInput).start();
    }

    /**
     * 添加接收到信息的回调
     * @param e
     */
    public void addCallback(ChatCallback e){
        chatCallbackList.add(e);
    }

    /**
     * 移出回调
     * @param e
     */
    public void removeCallback(ChatCallback e){
        chatCallbackList.remove(e);
    }

    /**
     * 发送信息
     * @param msg
     */
    public void sendMsg(String msg){
        serverMsgOutput.write(msg);
    }

}
