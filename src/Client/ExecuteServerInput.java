package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    处理服务端输入
 */
public class ExecuteServerInput implements Runnable{
    Socket serve;
    List<ClientInterface> clientInterfaceList;

    public ExecuteServerInput(Socket serve, List<ClientInterface> list) {
        this.serve = serve;
        this.clientInterfaceList = list;
    }

    private void callback(String msg){
        for(ClientInterface e:clientInterfaceList)
            e.onReceiveMessage(msg);
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(serve.getInputStream());
            while (scanner.hasNext()){
                callback(scanner.nextLine());
            }
            scanner.close();
            serve.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
