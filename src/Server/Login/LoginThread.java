package Server.Login;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Repository.*;
import Repository.User;
import Server.Interfaces.UserInterface;
import Server.ServerThread;

public class LoginThread extends ServerThread implements UserInterface {
    private static final String FAIL = "fail";
    private static final String SUCCESS = "success";
    private static final String DIVIDER = " ";

    //储存所有登录线程的用户
    private Map<String, LoginThread> clientMap;//存储所有的用户信息

    //用户服务器
    Socket client;

    //用户信息
    User user;

    //读入器
    Scanner scanner;

    //是否登录成功
    Boolean isLogin;

    LoginThread(Socket client, Map<String, LoginThread> clientMap) {
        super(client);
        this.clientMap = clientMap;
        isLogin = false;
        user = null;
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
            userLogout();
            scanner.close();
            client.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //建立输入输出流
        try {
            //初始化，如果已存在则返回
            if (clientMap.containsValue(this))
                return;
            clientMap.put(client.toString(), this);
            System.out.println(client.toString());
            scanner = new Scanner(client.getInputStream());
            sendToMe("进入登录注册服务器");
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                if (data.startsWith(UserInterface.LOGIN)) {
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2)
                        userLogin(info[1],info[2]);
                } else if (data.startsWith(UserInterface.LOGOUT)) {
                    userLogout();
                } else if (data.startsWith(UserInterface.MAKE_FRIEND)) {
                    String[] info = data.split(DIVIDER,2);
                    if(info.length>1)
                        makeFriend(info[1]);
                } else if (data.startsWith(UserInterface.REGISTER)) {
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2)
                        userRegister(info[1],info[2]);
                } else if(data.startsWith(DISCONNECT)){
                    closeThread();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userLogout() {
        //todo:对接数据库的操作
        this.user = null;
        sendToMe(UserInterface.LOGOUT + DIVIDER + SUCCESS);
    }

    @Override
    public void userLogin(String id, String pwd) {
        if(this.user==null){
            //todo:对接数据库
            user = new User(id,pwd);
            if(true)
                sendToMe(UserInterface.LOGIN + DIVIDER + SUCCESS + DIVIDER + id);
        }else{
            sendToMe(UserInterface.LOGIN + DIVIDER + FAIL + DIVIDER + id);
        }
    }

    @Override
    public void userRegister(String id, String pwd) {
        //todo:对接数据库
        if(true){
            sendToMe(UserInterface.REGISTER + DIVIDER + SUCCESS);
        }else{
            sendToMe(UserInterface.REGISTER + DIVIDER + FAIL);
        }

    }

    @Override
    public void makeFriend(String receiverID) {
        //todo:实现交友
        if(user!=null){

        }
    }
}
