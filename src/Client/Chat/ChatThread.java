package src.Client.Chat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatThread {
    ChatServerInput serverInput;
    ChatServerOutput serverOutput;
    Socket socket;
    List<ChatInterface> chatInterfaceList = new ArrayList<>();

    /**
     * 建立客户端并连接服务器
     * @param host 服务器地址
     * @param port 服务器端口号
     * @throws IOException 链接服务器失败
     */
    public ChatThread(String host, int port) throws IOException {
        initConnect(host, port);
    }

    public void initConnect(String host, int port) throws IOException {
        socket = new Socket(host,port);
        serverInput = new ChatServerInput(socket, chatInterfaceList);
        serverOutput = new ChatServerOutput(socket);
    }

    public void closeClient() throws IOException {
        serverOutput.close();
        serverInput.close();
        socket.close();
    }

    public void runClient(){
        new Thread(serverInput).start();
    }

    public void addCallback(ChatInterface e){
        chatInterfaceList.add(e);
    }

    public void removeCallback(ChatInterface e){
        chatInterfaceList.remove(e);
    }

    public void write(String msg){
        serverOutput.write(msg);
    }
}
