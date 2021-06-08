package Server.Room;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房间服务器 保持运行
 */
public class RoomServer implements Runnable {

    protected final ExecutorService executorService;

    protected final int portNum;

    protected boolean isRunning;

    protected Map<String, ChatThread> clientMap;//存储所有的用户信息

    protected Socket host;//管理员

    protected ServerSocket serverSocket;

    protected RoomServer(int portNum,Socket host) throws IOException {
        this.portNum = portNum;
        this.host = host;
        this.executorService = Executors.newFixedThreadPool(100);
        this.clientMap = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            //根据端口号新建房间
            ServerSocket serverSocket = new ServerSocket(portNum);
            System.out.println(serverSocket.getLocalSocketAddress() + " 聊天服务器建立完毕，房间号：" + portNum );
            while(isRunning) {
                Socket client = serverSocket.accept();
                System.out.println("新用户链接:" + client.getInetAddress() + ",端口" + client.getPort());
                //新建服务端线程去处理客户
                executorService.submit(new ChatThread(client,clientMap,this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeServer() throws IOException {
        System.out.println(portNum+"聊天室关闭");
        isRunning = false;
        executorService.shutdown();
        serverSocket.close();
        for (Map.Entry<String, ChatThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().leaveRoom(2);
        }
    }

}
