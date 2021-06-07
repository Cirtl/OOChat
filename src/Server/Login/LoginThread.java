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
import java.util.Scanner;

import Repository.*;
import Repository.User;
import Server.Interfaces.UserInterface;
import Server.ServerThread;

public class LoginThread  extends ServerThread implements UserInterface {
    private static final String REGISTER_FAIL = "register_success";
    private static final String REGISTER_SUCCESS = "register_success";
    private static final String LOGIN_FAIL = "login_fail";
    private static final String LOGIN_SUCCESS = "login_success";
    private static final String LOGOUT_SUCCESS = "logout_success";
    private static final String QUIT = "quit";
    private static final String LOGOUT = "logout";
    private static final String DIVIDER = " ";
    //储存所有登录线程的用户
    private static List<LoginThread> loginThreadList = new ArrayList<>();
    //用户服务器
    Socket client;
    //用户信息
    User user;
    //是否登录成功
    Boolean isLogin;
    //对数据的访问
    HandleLogin handleLogin;
    HandleRegister handleRegister;

    LoginThread(Socket client) {
        super(client);
        isLogin = false;
        handleLogin = new HandleLogin();
        handleRegister = new HandleRegister();
    }

    private boolean login(String id,String pwd){
        return true;
    }

    private boolean register(String id,String pwd){
        return true;
    }

    private void logout(){

    }

    private void closeThread(){

    }

    @Override
    public void run() {
        //建立输入输出流
        try{
            loginThreadList.add(this);
            Scanner inputStream = new Scanner(client.getInputStream());
            PrintStream outputStream = new PrintStream(client.getOutputStream(),true);

            outputStream.println("进入登录注册服务器");
            while(inputStream.hasNext()){
                String info = inputStream.nextLine();
                String[] strings = info.split(DIVIDER);
                System.out.println(client.getLocalSocketAddress() + " login: "+ info);
                if(strings.length==3){
                    String id = strings[1],pwd = strings[2];
                    if(strings[0].equals("login")){
                        if(login(id,pwd)){
                            outputStream.println(LOGIN_SUCCESS);
                        }
                        else
                            outputStream.println(LOGIN_FAIL);
                    }
                    else if(strings[0].equals("register")){
                        if(register(id,pwd))
                            outputStream.println(REGISTER_SUCCESS);
                        else
                            outputStream.println(REGISTER_FAIL);
                    }
                }
                else if(strings.length==1){
                    if(strings[0].equals(QUIT)){
                        closeThread();
                    }
                    else if(strings[0].equals(LOGOUT)){
                        logout();
                        outputStream.println(LOGOUT_SUCCESS);
                    }
                }
            }
            client.close();
            loginThreadList.remove(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void userLogout() {

    }

    @Override
    public void userLogin(String id, String pwd) {

    }

    @Override
    public void userRegister(String id, String pwd) {

    }

    @Override
    public void makeFriend(String receiverID) {

    }
}
