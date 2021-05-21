package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public class ExecuteClientThread implements Runnable{
    Socket client;

    private String quitKey;//管理员设定关闭指令

    private static Map<String, Socket> clientMap = new ConcurrentHashMap<>();//存储所有的用户信息

    public ExecuteClientThread(Socket client,String quitKey) {
        this.client = client;
        this.quitKey = (quitKey==null?"QUIT":quitKey);
    }

    private void sendToAll(String user,String msg){
        Set<Map.Entry<String,Socket>> entrySet = clientMap.entrySet();
        for(Map.Entry<String,Socket> entry:entrySet){
            Socket socket = entry.getValue();
            try {
                PrintStream printStream = new PrintStream(socket.getOutputStream(),true);
                printStream.println(user+": "+msg);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            Scanner receiver = new Scanner(client.getInputStream());
            clientMap.put("test",client);
            while(true){
                if(receiver.hasNext()){
                    String msg = receiver.nextLine();
                    System.out.println("从"+client.getInetAddress()+"收到信息: "+msg);
                    if(msg.equals(quitKey)){
                        System.out.println("服务器关闭");
                        break;
                    }
                    sendToAll("test",msg);
                }
            }
            clientMap.remove("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
