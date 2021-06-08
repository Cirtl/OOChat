package Server.Info;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.Login.LoginThread;
import Server.Room.ChatThread;
import Server.Room.RoomServer;

public class InfoServer implements Runnable{

    ServerSocket serverSocket;

    protected final ExecutorService executorService;

    Boolean isRunning = false;

    protected Map<String, InfoThread> clientMap;//存储所有的用户信息

    public InfoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        clientMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(100);
        System.out.println(serverSocket.getLocalSocketAddress() + ":信息服务器启动");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("新用户链接信息端口:" + client.getInetAddress() + ",端口" + client.getPort());
                //新建服务端线程去处理客户
                executorService.submit(new InfoThread(client,clientMap));
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
