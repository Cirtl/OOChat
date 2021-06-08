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
     * 向自己发送消息
     * @param msg 消息
     */
    public abstract void sendToMe(String msg);

    /**
     * 关闭连接
     */
    public abstract void closeThread();

    /**
     * 产生符合格式的指令
     * @param strings 需要合并的指令
     * @return 添加了DIVIDER的字符串
     */
    public String makeOrder(String...strings){
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<strings.length;i++){
            builder.append(strings[i]);
            if(i!=strings.length-1)
                builder.append(DIVIDER);
        }
        return builder.toString();
    }
    /**
     * 向所有人发送消息，通过调用sendToMe实现
     * @param toID 对方ID
     * @param msg 消息
     */
    public abstract void sendToSomeOne(String toID, String msg);

    /**
     * 向所有人发送消息，通过调用sendToMe实现
     * @param msg 消息
     */
    public abstract void sendToAll(String msg);

}
