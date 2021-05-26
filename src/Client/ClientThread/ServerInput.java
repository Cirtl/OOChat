package src.Client.ClientThread;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/*
    处理服务端输入
 */
public class ServerInput implements Runnable{
    Socket serve;
    List<ChatInterface> chatInterfaceList;
    Scanner scanner;

    protected ServerInput(Socket serve, List<ChatInterface> list) throws IOException {
        this.serve = serve;
        this.chatInterfaceList = list;
        scanner = new Scanner(serve.getInputStream());
    }

    private void callback(String msg){
        for(ChatInterface e: chatInterfaceList)
            e.onReceiveMessage(msg);
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()){
            callback(scanner.nextLine());
        }
    }

    protected void close(){
        scanner.close();
    }
}
