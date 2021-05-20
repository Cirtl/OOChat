package Server;

import java.net.Socket;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public class ExecuteClientThread implements Runnable{
    Socket client;

    public ExecuteClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

    }
}
