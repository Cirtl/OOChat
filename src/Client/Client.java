package src.Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    ExecuteServerInput serverInput;
    ExecuteServerOutput serverOutput;
    Socket socket=null;
    List<ClientInterface> clientInterfaceList = new ArrayList<ClientInterface>();
    /**
     * 建立客户端并连接服务器
     * @param host 服务器地址
     * @param port 服务器端口号
     * @param inputStream 数据输入流
     * @throws IOException 链接服务器失败
     */
    public Client(String host,int port,InputStream inputStream) throws IOException {
        initConnect(host, port,inputStream);
    }

    private void initConnect(String host, int port, InputStream inputStream) throws IOException {
        socket = new Socket(host,port);
        serverInput = new ExecuteServerInput(socket,clientInterfaceList);
        serverOutput = new ExecuteServerOutput(socket,inputStream);
    }

    public void closeClient() throws IOException {
        if(socket!=null)
            socket.close();
    }

    public void runClient(){
        new Thread(serverInput).start();
        new Thread(serverOutput).start();
    }

    public void addCallback(ClientInterface e){
        clientInterfaceList.add(e);
    }

    public void removeCallback(ClientInterface e){
        clientInterfaceList.remove(e);
    }

}
