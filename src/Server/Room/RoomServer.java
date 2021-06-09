package Server.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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

    protected Map<String, RoomThread> clientMap;//存储所有的用户信息

    protected String host;//管理员

    protected String passWord;

    protected ServerSocket serverSocket;

    public RoomServer(int portNum, String host ,String pwd) throws IOException {
        this.portNum = portNum;
        this.host = host;
        this.passWord = pwd;
        this.executorService = Executors.newFixedThreadPool(100);
        this.clientMap = new ConcurrentHashMap<>();
        this.isRunning = true;
        serverSocket = new ServerSocket(portNum);
        System.out.println(serverSocket.getInetAddress() + " 聊天服务器建立完毕，房间号：" + portNum );
    }

    public boolean inRoom(String id){
        for (Map.Entry<String, RoomThread> stringChatThreadEntry : clientMap.entrySet()) {
            if(stringChatThreadEntry.getValue().sameUser(id))
                return true;
        }
        return false;
    }

    public boolean checkPassword(String pwd){
        return pwd.equals(passWord);
    }

    @Override
    public void run() {
        try {
            while(isRunning) {
                Socket client = serverSocket.accept();
                System.out.println("新用户链接房间:" + client.getInetAddress() + ",端口" + client.getPort());
                //新建服务端线程去处理客户
                executorService.submit(new RoomThread(client,clientMap,this));
            }
        } catch (IOException e) {
            System.out.println(e + "  when roomServer");
        }
    }

    public void closeServer() throws IOException {
        System.out.println(portNum+" 聊天室关闭 ");
        isRunning = false;
        executorService.shutdown();
        serverSocket.close();
        for (Map.Entry<String, RoomThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().leaveRoom(2);
        }
    }

    public int getPortNum() {
        return portNum;
    }

    public String getHost() {
        return host;
    }
}
