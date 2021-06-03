package Server.Info;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InfoServer implements Runnable{
    ServerSocket serverSocket;
    Boolean isRunning = false;

    public InfoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        System.out.println(serverSocket.getLocalSocketAddress()+":信息服务器启动");
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                Socket client = serverSocket.accept();
                new Thread(new InfoThread(client)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer(){
        this.isRunning = false;
    }
}
