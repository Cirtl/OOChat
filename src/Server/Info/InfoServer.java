package Server.Info;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Server.Login.LoginThread;
import Server.Room.RoomServer;

public class InfoServer implements Runnable{

    ServerSocket serverSocket;

    Boolean isRunning = false;

    protected Map<String, InfoThread> clientMap;//存储所有的用户信息

    public InfoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        clientMap = new ConcurrentHashMap<>();
        System.out.println(serverSocket.getLocalSocketAddress() + ":信息服务器启动");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                new Thread(new InfoThread(client,clientMap)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer() throws IOException {
        for (Map.Entry<String, InfoThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().closeThread();
        }
        this.isRunning = false;
        serverSocket.close();
    }
}
