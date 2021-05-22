package Server.Login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer implements Runnable{
    ServerSocket serverSocket;
    Boolean isRunning = false;

    public LoginServer() throws IOException {
        serverSocket = new ServerSocket(8000);
        isRunning = true;
        System.out.println(serverSocket.getLocalSocketAddress()+":登录注册服务器启动");
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                Socket client = serverSocket.accept();
                new Thread(new LoginThread(client)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer(){
        this.isRunning = false;
    }
}
