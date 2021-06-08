package Server.Login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Server.Room.ChatThread;
import Server.ServerThread;

public class LoginServer implements Runnable {

    ServerSocket serverSocket;

    Boolean isRunning = false;

    protected Map<String, LoginThread> clientMap;//存储所有的用户信息

    public LoginServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        clientMap = new ConcurrentHashMap<>();
        System.out.println(serverSocket.getLocalSocketAddress() + ":登录注册服务器启动");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                new Thread(new LoginThread(client,clientMap)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer() throws IOException {
        for (Map.Entry<String, LoginThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().closeThread();
        }
        this.isRunning = false;
        serverSocket.close();
    }
}
