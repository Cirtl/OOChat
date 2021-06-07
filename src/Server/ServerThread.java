package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import Server.Interfaces.ChatterInterface;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public abstract class ServerThread implements Runnable {
    Socket client;

    protected static final String DIVIDER = " ";

    protected static final String disconnect = "QUIT";

    protected static Map<String, Socket> clientMap = new ConcurrentHashMap<>();//存储所有的用户信息

    public ServerThread(Socket client) {
        this.client = client;
    }

    protected void sendToAll(String msg){
        Set<Map.Entry<String,Socket>> entrySet = clientMap.entrySet();
        for(Map.Entry<String,Socket> entry:entrySet){
            Socket socket = entry.getValue();
            try {
                PrintStream printStream = new PrintStream(socket.getOutputStream(),true);
                printStream.println(msg);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    protected void sendToSomeOne(String to,String msg){
        Set<Map.Entry<String,Socket>> entrySet = clientMap.entrySet();
        for(Map.Entry<String,Socket> entry:entrySet){
            if(entry.getKey().equals(to)){
                Socket socket = entry.getValue();
                try {
                    PrintStream printStream = new PrintStream(socket.getOutputStream(),true);
                    printStream.println(msg);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
