package Server.Login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginServer implements Runnable {

    LoginServer loginServer;

    ServerSocket serverSocket;

    protected final ExecutorService executorService;

    Boolean isRunning = false;

    protected Map<String, LoginThread> clientMap;//存储所有的用户信息

    private LoginServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        clientMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(100);
        System.out.println(serverSocket.getInetAddress() + ":登录注册服务器启动");
    }

    public LoginServer getInstance(int port) throws IOException {
        if(loginServer==null)
            loginServer = new LoginServer(port);
        return loginServer;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("新用户链接登录服务器:" + client.getInetAddress() + ",端口" + client.getPort());
                //新建服务端线程去处理客户
                executorService.submit(new LoginThread(client,clientMap));
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
