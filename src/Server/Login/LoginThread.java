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

    //运行标志
    private boolean isRunning;

    /**
     * 构造进程来服务用户登录注册
     * @param client 用户socket
     * @param clientMap 用户集合
     */
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
                    //如果用户异常退出 则关闭线程
                    System.out.println(" login break down ");
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
        if(user.logout()){
            clientMap.remove(user.getId());
            user = null;
            sendToMe(LOGOUT + DIVIDER + SUCCESS);
        }else{
            sendToMe(makeOrder(LOGOUT,FAIL));
        }
    }

    @Override
    public void userLogin(String id, String pwd) {
        user = new User(id,pwd);
        if(true){
            //todo:对接数据库
            clientMap.put(id,this);
            sendToMe(LOGIN + DIVIDER + SUCCESS + DIVIDER + id);
        }else{
            sendToMe(LOGIN + DIVIDER + FAIL + DIVIDER + -1);
        }
    }

    @Override
    public void userRegister(String id, String pwd) {
        User newUser = new User(id,pwd);
        //todo:对接数据库
        if(newUser.register()){
            sendToMe(makeOrder(REGISTER,SUCCESS));
        }else{
            sendToMe(makeOrder(REGISTER,FAIL));
        }
    }

    @Override
    public void makeFriend(String toID,String state) {
        //todo:实现交友
        if(user!=null){
            switch (state) {
                case SUCCESS:
                    //由接受邀请方发来，交友成功
                    if (user.addFriend(toID)) {
                        sendToSomeOne(toID, makeOrder(MAKE_FRIEND, SUCCESS, String.valueOf(0), user.getId()));
                        sendToMe(makeOrder(MAKE_FRIEND, SUCCESS, String.valueOf(0), toID));
                    } else {
                        sendToSomeOne(toID, makeOrder(MAKE_FRIEND, FAIL, String.valueOf(-3), user.getId()));
                        sendToMe(makeOrder(MAKE_FRIEND, FAIL, String.valueOf(-3), toID));
                    }
                    break;
                case FAIL:
                    //由接受邀请方发来，交友拒绝
                    sendToSomeOne(toID, makeOrder(MAKE_FRIEND, FAIL, String.valueOf(-1), user.getId()));
                    break;
                case UNCONFIRMED:
                    //由发送邀请方发来，请求未决
                    System.out.println(clientMap.keySet());
                    if (clientMap.containsKey(toID)) {
                        sendToSomeOne(toID, makeOrder(MAKE_FRIEND, UNCONFIRMED, user.getId()));
                    } else {
                        //用户不在线
                        sendToMe(makeOrder(MAKE_FRIEND, FAIL, String.valueOf(-2), toID));
                    }
                    break;
            }
        }else{
            sendToMe(makeOrder(MAKE_FRIEND,FAIL,String.valueOf(-3),toID));
        }
    }
}
