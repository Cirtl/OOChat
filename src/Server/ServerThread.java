package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import Server.Interfaces.ChatterInterface;
import Server.Room.ChatThread;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public abstract class ServerThread implements Runnable {

    public Socket client;

    public static final String DIVIDER = " ";

    public static final String DISCONNECT = "QUIT";


    public ServerThread(Socket client){
        this.client = client;
    }

    /**
     * 向用户发送消息
     * @param msg 消息
     */
    public abstract void sendToMe(String msg);

    /**
     * 关闭连接
     */
    public abstract void closeThread();

}
