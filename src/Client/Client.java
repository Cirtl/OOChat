package Client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    ExecuteServerInput serverInput;
    ExecuteServerOutput serverOutput;
    Socket socket;

    /**
     * 建立客户端并连接服务器
     * @param host 服务器地址
     * @param port 服务器端口号
     * @throws IOException 链接服务器失败
     */
    public Client(String host,int port) throws IOException {
        initUI();
        initConnect(host, port);
    }

    private void initConnect(String host, int port) throws IOException {
        socket = new Socket(host,port);
        serverInput = new ExecuteServerInput(socket);
        serverOutput = new ExecuteServerOutput(socket);
    }

    public void runClient(){
        new Thread(serverInput).start();
        new Thread(serverOutput).start();
    }

    private void initUI(){
        //TODO:前端界面
    }
}
