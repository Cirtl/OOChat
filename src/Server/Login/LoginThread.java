package Server.Login;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import Repository.User;
import Server.Info.InfoThread;
import Server.ServerInterfaces.UserInterface;
import Server.ServerThread;

public class LoginThread extends ServerThread implements UserInterface {

    //储存所有登录线程的用户
    private Map<String, LoginThread> clientMap;//存储所有的用户信息

    //用户信息
    private User user;

    //读入器
    private Scanner scanner;

    //
    private boolean isRunning;

    LoginThread(Socket client, Map<String, LoginThread> clientMap) {
        super(client);
        this.clientMap = clientMap;
        isRunning = true;
        user = null;
    }

    @Override
    public void sendToMe(String msg) {
        try {
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(msg);
        } catch (IOException e) {
            System.out.println(e + "  when send to me");
        }
    }

    @Override
    public void closeThread() {
        try{
            sendToMe(makeOrder(DISCONNECT,"LOGIN"));
            isRunning = false;
            scanner.close();
            client.close();
            clientMap.remove(this);
        }catch (IOException e) {
            System.out.println("in close login" + "  "+e);
        }
    }

    @Override
    public void sendToSomeOne(String toID, String msg) {
        for (Map.Entry<String, LoginThread> stringChatThreadEntry : clientMap.entrySet()) {
            if (stringChatThreadEntry.getKey().equals(toID)) {
                stringChatThreadEntry.getValue().sendToMe(msg);
                break;
            }
        }
    }

    @Override
    public void sendToAll(String msg) {

    }

    @Override
    public void run() {
        //建立输入输出流
        try {
            //初始化，如果已存在则返回
            if (clientMap.containsValue(this))
                return;
            clientMap.put(client.toString(), this);
            scanner = new Scanner(client.getInputStream());
            sendToMe("进入登录注册服务器");
            while (isRunning) {
                String data = "";
                if(scanner.hasNext())
                    data = scanner.nextLine();
                if(data.isEmpty()){
                    System.out.println("login break down ");
                    closeThread();
                    break;
                }
                System.out.println("receive from LOGIN " + client +  " " + data);
                System.out.println(client.isOutputShutdown());
                if (data.startsWith(UserInterface.LOGIN)) {
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2)
                        userLogin(info[1],info[2]);
                } else if (data.startsWith(UserInterface.LOGOUT)) {
                    userLogout();
                } else if (data.startsWith(UserInterface.MAKE_FRIEND)) {
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>1)
                        makeFriend(info[1],info[2]);
                } else if (data.startsWith(UserInterface.REGISTER)) {
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2)
                        userRegister(info[1],info[2]);
                } else if(data.startsWith(DISCONNECT)){
                    userLogout();
                    closeThread();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e + "  " + " in login running");
        }
    }

    @Override
    public void userLogout() {
        //todo:对接数据库的操作
        clientMap.remove(user.getId());
        user = null;
        sendToMe(UserInterface.LOGOUT + DIVIDER + SUCCESS);
    }

    @Override
    public void userLogin(String id, String pwd) {
        if(this.user==null){
            //todo:对接数据库
            user = new User(id,pwd);
            if(true){
                clientMap.put(id,this);
                sendToMe(UserInterface.LOGIN + DIVIDER + SUCCESS + DIVIDER + id);
            }
        }else{
            sendToMe(UserInterface.LOGIN + DIVIDER + FAIL + DIVIDER + -1);
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
    public void makeFriend(String toID,String state) {
        //todo:实现交友
        if(user!=null){
            if(state.equals(SUCCESS)){
                //由接受邀请方发来，交友成功
                sendToSomeOne(toID,makeOrder(MAKE_FRIEND,SUCCESS,String.valueOf(0),user.getId()));
                sendToMe(makeOrder(MAKE_FRIEND,SUCCESS,String.valueOf(0),toID));
            }else if(state.equals(FAIL)){
                //由接受邀请方发来，交友拒绝
                sendToSomeOne(toID,makeOrder(MAKE_FRIEND,FAIL,String.valueOf(-1),user.getId()));
            }else if(state.equals(UNCONFIRMED)){
                //由发送邀请方发来，请求未决
                System.out.println(clientMap.keySet());
                if(clientMap.containsKey(toID)){
                    sendToSomeOne(toID,makeOrder(MAKE_FRIEND,UNCONFIRMED,user.getId()));
                }else{
                    //用户不在线
                    sendToMe(makeOrder(MAKE_FRIEND,FAIL,String.valueOf(-2),toID));
                }
            }
        }else{
            sendToMe(makeOrder(MAKE_FRIEND,FAIL,String.valueOf(-3),toID));
        }
    }
}
