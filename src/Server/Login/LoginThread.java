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

public class LoginThread implements Runnable{

    //储存所有登录线程的用户
    private static List<LoginThread> loginThreadList = new ArrayList<>();
    //用户服务器
    Socket client;
    //用户信息
    User user;
    //是否登录成功
    Boolean isLogin;
    Boolean isRegister;
    //对数据的访问
    HandleLogin handleLogin;
    HandleRegister handleRegister;

    LoginThread(Socket client) {
        this.client = client;
        isLogin = false;
        handleLogin = new HandleLogin();
        handleRegister = new HandleRegister();
    }

    @Override
    public void run() {
        //建立输入输出流
        try{
            loginThreadList.add(this);
            Scanner inputStream = new Scanner(client.getInputStream());
            PrintStream outputStream = new PrintStream(client.getOutputStream(),true);
            outputStream.println("进入登录注册服务器");
            while(!isLogin){
                String info = inputStream.nextLine();
                String[] strings = info.split(",");
                outputStream.println("正在验证》》》");
                if(strings.length==3){
                    String id = strings[1],pwd = strings[2];
                    if(strings[0].equals("Login")){
                        isLogin = handleLogin.queryVerify(id,pwd);
                        outputStream.print(isLogin);
                    }
                    else if(strings[0].equals("Register")){
                        isRegister = handleRegister.writeRegisterModel(new User(id,pwd));
                        outputStream.print(isRegister);
                    }
              }
            }
            client.close();
            loginThreadList.remove(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
