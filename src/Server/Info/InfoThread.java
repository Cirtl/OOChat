package Server.Info;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Repository.HandleLogin;
import Repository.HandleRegister;
import Repository.User;
import Server.Interfaces.InfoInterface;
import Server.ServerThread;

public class InfoThread  extends ServerThread implements  InfoInterface {

    //储存所有登录线程的用户
    private Map<String, InfoThread> clientMap;
    //用户服务器
    Socket client;
    //决定是否在运行
    private  boolean isRunning;
    //读入器
    Scanner scanner;

    public InfoThread(Socket client,Map<String, InfoThread> clientMap) {
        super(client);
        this.isRunning = true;
        this.clientMap = clientMap;
    }

    @Override
    public void sendToMe(String msg) {
        try {
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeThread() {
        try{
            isRunning = false;
            scanner.close();
            client.close();
            clientMap.remove(this);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //建立输入输出流
        try{

            if (clientMap.containsValue(this))
                return;
            clientMap.put(client.toString(), this);
            System.out.println(client.toString());
            scanner = new Scanner(client.getInputStream());
            sendToMe("进入信息服务器");

            while(isRunning){
                String data = "";
                if(scanner.hasNext())
                    data = scanner.nextLine();
                if(data.startsWith(InfoInterface.NEW_ROOM)){

                }else if(data.startsWith(InfoInterface.ENTER_ROOM)){

                }else if(data.startsWith(InfoInterface.MY_ROOMS)){

                }else if(data.startsWith(InfoInterface.DELETE_ROOM)){

                }else if(data.startsWith(InfoInterface.INVITE_FRIEND)){

                }else if(data.startsWith(InfoInterface.SHUT_ROOM)){

                }else if(data.startsWith(DISCONNECT)){
                    closeThread();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRoom(int port) {

    }

    @Override
    public void getMyRooms() {

    }

    @Override
    public void newRoom(int roomPort, int pwd) {

    }


    @Override
    public void enterRoom(int roomPort, int pwd) {

    }

    @Override
    public void inviteFriend(String friendID) {

    }

    @Override
    public void shutRoom(int roomPort) {

    }
}
