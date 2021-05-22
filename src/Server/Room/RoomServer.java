package Server.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房间服务器 保持运行
 */
public class RoomServer implements Runnable{
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private int maxNum;
    private int portNum;

    public RoomServer(int maxNum, int portNum) {
        this.maxNum = maxNum;
        this.portNum = portNum;
        this.executorService = Executors.newFixedThreadPool(maxNum);//加入线程池
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(portNum);//根据端口号新建房间
            System.out.println(serverSocket.getLocalSocketAddress()+":聊天服务器建立完毕");
            for(int i=0;i<maxNum;i++){
                Socket client = serverSocket.accept();
                System.out.println("新用户链接:"+client.getInetAddress()+",端口"+client.getPort());
                executorService.execute(new ChatThread(client,null));//新建服务端线程去处理客户
            }
            executorService.shutdown();//不再接受新的客户
            //TODO:旧用户退出后 新用户可以加入
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
