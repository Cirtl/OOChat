package Server;

import java.net.Socket;

/**
 * 服务器基础线程
 */
public abstract class ServerThread implements Runnable {

    public Socket client;

    public static final String DIVIDER = " ";

    public static final String DISCONNECT = "QUIT";

    public static final String FAIL = "FAIL";

    public static final String SUCCESS = "SUCCESS";

    public static final String UNCONFIRMED = "UNCONFIRMED";

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
     * @param order 指令名
     * @param result 指令结果
     * @param info 额外提供的信息（不一定有）
     * @return 添加了DIVIDER的字符串
     */
    public String makeOrder(String order,String result,String...info){
        StringBuilder builder = new StringBuilder();
        builder.append(order).append(DIVIDER).append(result);
        for(int i=0;i<info.length;i++){
            builder.append(DIVIDER).append(info[i]);
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
