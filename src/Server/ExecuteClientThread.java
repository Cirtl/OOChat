package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public class ExecuteClientThread implements Runnable{
    Socket client;
    private static Map<String, Socket> clientMap = new ConcurrentHashMap<>();//存储所有的用户信息

    public ExecuteClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            PrintStream sender = new PrintStream(client.getOutputStream());
            Scanner receiver = new Scanner(client.getInputStream());
            clientMap.put("test",client);
            String msg = null;
            while(true){
                if(receiver.hasNext()){
                    msg = receiver.nextLine();
                    if(msg.equals("QUIT")){
                        break;
                    }
                    System.out.println("get mag:"+msg);
                }
            }
            clientMap.remove("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
