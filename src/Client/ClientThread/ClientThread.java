package src.Client.ClientThread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread {
    ServerInput serverInput;
    ServerOutput serverOutput;
    Socket socket;
    List<ChatInterface> chatInterfaceList = new ArrayList<>();

    /**
     * 建立客户端并连接服务器
     * @param host 服务器地址
     * @param port 服务器端口号
     * @throws IOException 链接服务器失败
     */
    public ClientThread(String host, int port) throws IOException {
        initConnect(host, port);
    }

    public void initConnect(String host, int port) throws IOException {
        socket = new Socket(host,port);
        serverInput = new ServerInput(socket, chatInterfaceList);
        serverOutput = new ServerOutput(socket);
    }

    public void closeThread() throws IOException {
        serverOutput.close();
        serverInput.close();
        socket.close();
    }

    public void runThread(){
        new Thread(serverInput).start();
    }

    /**
     * 添加接收到信息的回调
     * @param e
     */
    public void addCallback(ChatInterface e){
        chatInterfaceList.add(e);
    }

    /**
     * 移出回调
     * @param e
     */
    public void removeCallback(ChatInterface e){
        chatInterfaceList.remove(e);
    }

    /**
     * 发送信息
     * @param msg
     */
    public void sendMsg(String msg){
        serverOutput.write(msg);
    }
}
