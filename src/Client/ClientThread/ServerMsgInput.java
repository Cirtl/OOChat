package src.Client.ClientThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
    处理服务端输入
 */
public class ServerMsgInput implements Runnable{
    Socket serve;
    List<ChatCallback> chatCallbackList;
    Scanner scanner;

    protected ServerMsgInput(Socket serve, List<ChatCallback> list) throws IOException {
        this.serve = serve;
        this.chatCallbackList = list;
        scanner = new Scanner(serve.getInputStream());
    }

    private void onReceiveMsg(String msg){
        System.out.println(msg);
        String[] tmp  = msg.split(" ");
        String[] info = new String[tmp.length-1];
        if (tmp.length - 1 >= 0) System.arraycopy(tmp, 1, info, 0, tmp.length - 1);
        for(ChatCallback e: chatCallbackList)
            e.onReceiveMessage(tmp[0],info);
    }

    @Override
    public void run() {
        while (scanner.hasNext()){
            onReceiveMsg(scanner.nextLine());
        }
    }

    protected void close() throws IOException {
        scanner.close();
    }
}
