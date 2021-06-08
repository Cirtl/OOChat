package Server;

import java.net.Socket;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public abstract class ServerThread implements Runnable {

    public Socket client;

    public static final String DIVIDER = " ";

    public static final String DISCONNECT = "QUIT";

    public static final String FAIL = "FAIL";

    public static final String SUCCESS = "SUCCESS";

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

    public String makeOrder(String...strings){
        StringBuilder builder = new StringBuilder();
        for(String s:strings){
            builder.append(s).append(DIVIDER);
        }
        return builder.toString();
    }


}
