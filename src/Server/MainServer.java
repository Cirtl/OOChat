package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.IIOException;

/**
 * 主服务器 保持运行
 */
public class MainServer {
    ExecutorService executorService;
    ServerSocket serverSocket;

    public MainServer(int maxNum, int portNum) {
        try{
            executorService = Executors.newFixedThreadPool(maxNum);
            serverSocket = new ServerSocket(portNum);
            for(int i=0;i<maxNum;i++){
                Socket client = serverSocket.accept();
                System.out.println("新用户链接:"+client.getInetAddress()+",端口"+client.getPort());
                executorService.execute(new ExecuteClientThread(client));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
