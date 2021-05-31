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
public class ServerInput implements Runnable{
    Socket serve;
    List<ChatCallback> chatCallbackList;
    Scanner scanner;
    ObjectInputStream objectInputStream;

    protected ServerInput(Socket serve, List<ChatCallback> list) throws IOException {
        this.serve = serve;
        this.chatCallbackList = list;
        scanner = new Scanner(serve.getInputStream());
        objectInputStream = new ObjectInputStream(serve.getInputStream());
    }

    private void onReceiveMsg(String msg){
        String option  = msg.split(" ")[0];
        String[] info = msg.replaceFirst(option,"").split(" ");
        for(ChatCallback e: chatCallbackList)
            e.onReceiveMessage(option,info);
    }

    private void onReceiveObject(String objName,Object object){
        for(ChatCallback e:chatCallbackList)
            e.onReceiveObject(objName,object);
    }

    private void getObject() throws IOException, ClassNotFoundException {
        String type = scanner.nextLine();
        Object object = objectInputStream.readObject();
        onReceiveObject(type,object);
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()){
            String option = scanner.nextLine();
            if(option=="msg"){
                onReceiveMsg(scanner.nextLine());
            }
            else if(option=="object") {
                try {
                    getObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void close() throws IOException {
        scanner.close();
        objectInputStream.close();
    }
}
