package Server.Info;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Repository.HandleLogin;
import Repository.HandleRegister;
import Repository.User;

public class InfoThread implements Runnable{
    private static final String ROOM_LIST= "room_list";
    private static final String ROOM_PORT= "room_port";
    private static final String QUIT = "quit";
    private static final String DIVIDER = " ";
    //储存所有登录线程的用户
    private static List<InfoThread> infoThreadList = new ArrayList<>();
    //用户服务器
    Socket client;

    InfoThread(Socket client) {
        this.client = client;
    }

    private boolean login(String id,String pwd){
        return true;
    }

    private boolean register(String id,String pwd){
        return true;
    }

    private void logout(){

    }

    private void closeThread() throws IOException {
        client.close();
        infoThreadList.remove(this);
    }

    @Override
    public void run() {
        //建立输入输出流
        try{
            infoThreadList.add(this);
            Scanner inputStream = new Scanner(client.getInputStream());
            PrintStream outputStream = new PrintStream(client.getOutputStream(),true);
            outputStream.println("进入信息服务器");
            while(inputStream.hasNext()){
                String[] order = inputStream.nextLine().split(DIVIDER);
                System.out.println(client.getLocalSocketAddress() + " info: "+ order[0]);
                if(order[0].equals(ROOM_PORT)){
                    outputStream.println(ROOM_PORT + DIVIDER + "8002");
                }else if(order[0].equals(ROOM_LIST)){
                    outputStream.println("ask room list");
                }
            }
            client.close();
            infoThreadList.remove(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
