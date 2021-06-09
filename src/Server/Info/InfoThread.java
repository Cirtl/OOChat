package Server.Info;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import Repository.User;
import Server.Room.RoomThread;
import Server.ServerInterfaces.InfoInterface;
import Server.Room.RoomServer;
import Server.ServerThread;

/**
 * 信息线程，用于处理用户新建房间 进出房间 邀请加入房间 获取房间列表等操作
 * @author 杨东浩
 */
public class InfoThread  extends ServerThread implements  InfoInterface {
    //储存所有房间
    private Map<Integer, RoomServer> rooms = new ConcurrentHashMap<>();
    //储存所有登录线程的用户
    private Map<String, InfoThread> clientMap;
    //决定是否在运行
    private  boolean isRunning;
    //读入器
    Scanner scanner;

    public InfoThread(Socket client,Map<String, InfoThread> clientMap,Map<Integer, RoomServer> rooms) {
        super(client);
        this.isRunning = true;
        this.clientMap = clientMap;
        this.rooms = rooms;
    }

    @Override
    public void sendToMe(String msg) {
        try {
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(msg);
        } catch (IOException e) {
            System.out.println(e + "   when info send to me");
        }
    }

    @Override
    public void closeThread() {
        try{
            sendToMe(makeOrder(DISCONNECT,"INFO"));
            isRunning = false;
            scanner.close();
            client.close();
            clientMap.remove(this);
        }catch (IOException e) {
            System.out.println("in close info" + "  "+e);
        }
    }

    @Override
    public void sendToSomeOne(String toID, String msg) {
        for (Map.Entry<String, InfoThread> stringChatThreadEntry : clientMap.entrySet()) {
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
        try{
            if (clientMap.containsValue(this))
                return;
            clientMap.put(client.toString(), this);
            scanner = new Scanner(client.getInputStream());
            sendToMe("进入信息服务器");
            while(isRunning){
                String data="";
                if(scanner.hasNext())
                    data = scanner.nextLine();
                if(data.isEmpty()){
                    System.out.println("info break down ");
                    closeThread();
                    break;
                }
                System.out.println("receive from INFO " + client + " " + data);
                if(data.startsWith(InfoInterface.NEW_ROOM)){
                    String[] info = data.split(DIVIDER,4);
                    if(info.length>3){
                        String id = info[1],room = info[2],pwd = info[3];
                        int room_port;
                        try {
                            room_port = Integer.parseInt(room);
                        }catch (Exception e){
                            room_port = -1;
                        }
                        newRoom(id,room_port,pwd);
                    }
                }else if(data.startsWith(InfoInterface.ENTER_ROOM)){
                    String[] info = data.split(DIVIDER,4);
                    if(info.length>3){
                        String id = info[1],room = info[2],pwd = info[3];
                        int room_port;
                        try {
                            room_port = Integer.parseInt(room);
                        }catch (Exception e){
                            room_port = -1;
                        }
                        enterRoom(id,room_port,pwd);
                    }
                }else if(data.startsWith(InfoInterface.MY_ROOMS)){
                    getMyRooms();
                }else if(data.startsWith(InfoInterface.DELETE_ROOM)){
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2){
                        int port;
                        try {
                            port = Integer.parseInt(info[2]);
                        }catch (Exception e){
                            port = -1;
                        }
                        deleteRoom(info[1],port);
                    }
                }else if(data.startsWith(InfoInterface.INVITE_FRIEND)){
                    String[] info = data.split(DIVIDER,4);
                    if(info.length>3){
                        int port;
                        try {
                            port = Integer.parseInt(info[2]);
                        }catch (Exception e){
                            port = -1;
                        }
                        inviteFriend(info[1],port,info[3]);
                    }

                }else if(data.startsWith(InfoInterface.SHUT_ROOM)){
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2){
                        String id = info[1],room = info[2];
                        int room_port;
                        try {
                            room_port = Integer.parseInt(room);
                        }catch (Exception e){
                            room_port = -1;
                        }
                        shutRoom(id,room_port);
                    }
                }else if(data.startsWith(DISCONNECT)){
                    closeThread();
                }
            }
        }catch (IOException e){
            System.out.println(e + "  " + "in info running");
        }
    }

    @Override
    public void deleteRoom(String userID, int port) {
        //需要数据库访问
        if(rooms.containsKey(port)){
            //房间运行中 无法删除
            sendToMe(makeOrder(DELETE_ROOM,FAIL,String.valueOf(-1)));
        }else{
            //todo:对数据库操作,并发送返回
            if(true){
                //删除成功
                sendToMe(makeOrder(DELETE_ROOM,SUCCESS,String.valueOf(0)));
            }else{
                //房间不存在-2 不是房主-3
                sendToMe(makeOrder(DELETE_ROOM,FAIL,String.valueOf(-2)));
            }
        }
    }

    @Override
    public void getMyRooms() {
        StringBuilder builder = new StringBuilder();
        if(rooms.keySet().isEmpty()){
            sendToMe(makeOrder(MY_ROOMS,FAIL));
        }else{
            int cnt = 0;
            for (Map.Entry<Integer, RoomServer> entry : rooms.entrySet()) {
                String host = entry.getValue().getHost();
                int port = entry.getKey();
                builder.append(port).append("_").append(host).append(DIVIDER);
                cnt++;
            }
            sendToMe(makeOrder(MY_ROOMS,SUCCESS,String.valueOf(cnt),builder.toString()));
        }
    }

    @Override
    public void newRoom(String userID, int roomPort, String pwd) {
        //todo:对数据库操作
        if(roomPort<1024||roomPort>65535){
            sendToMe(makeOrder(InfoInterface.NEW_ROOM,String.valueOf(-1), String.valueOf(-1)));
        }
        else if(rooms.containsKey(roomPort)){
            sendToMe(makeOrder(InfoInterface.NEW_ROOM,String.valueOf(-1), String.valueOf(-2)));
        }
        else{
            boolean flag = true;
            for (Map.Entry<Integer, RoomServer> allRoom : rooms.entrySet()) {
                if (allRoom.getValue().getHost().equals(userID)) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                try{
                    RoomServer roomServer = new RoomServer(roomPort,userID,pwd);
                    rooms.put(roomPort,roomServer);
                    new Thread(roomServer,String.valueOf(roomPort)).start();
                    new User(userID,"test").createHouse(roomPort,String.valueOf(roomPort),pwd);
                    sendToMe(makeOrder(NEW_ROOM,String.valueOf(0), String.valueOf(roomPort)));
                }catch (IOException e){
                    e.printStackTrace();
                    rooms.remove(roomPort);
                    sendToMe(makeOrder(NEW_ROOM,String.valueOf(-1), String.valueOf(-3)));
                }
            }else{
                sendToMe(makeOrder(NEW_ROOM,String.valueOf(1), String.valueOf(-3)));
            }
        }
    }

    @Override
    public void enterRoom(String userID, int roomPort, String pwd) {
        //todo:对数据操作
        if(rooms.containsKey(roomPort)){
            RoomServer roomServer = rooms.get(roomPort);
            if(roomServer.inRoom(userID))
                //已在房间里
                sendToMe(makeOrder(InfoInterface.ENTER_ROOM,FAIL, String.valueOf(-1)));
            else if(!roomServer.checkPassword(pwd))
                //密码错误
                sendToMe(makeOrder(ENTER_ROOM,FAIL,String.valueOf(-3)));
            else {
                if (new User(userID, "test").enterHouse(roomPort, pwd))
                    sendToMe(makeOrder(InfoInterface.ENTER_ROOM, SUCCESS, String.valueOf(roomPort)));
                else
                    sendToMe(makeOrder(ENTER_ROOM, FAIL, String.valueOf(-4)));
            }
        }
        else
            //房间不存在
            sendToMe(makeOrder(InfoInterface.ENTER_ROOM,FAIL, String.valueOf(-2)));
    }

    @Override
    public void inviteFriend(String userID, int roomPort, String friendID) {
        if(rooms.containsKey(roomPort)){
            //todo:邀请好友加入
            if(clientMap.containsKey(friendID)){
                sendToSomeOne(friendID,makeOrder(INVITE_FRIEND,userID,String.valueOf(roomPort)));
                sendToMe(makeOrder(INVITE_FRIEND,String.valueOf(0)));
            }else{
                //好友不在线
                sendToMe(makeOrder(INVITE_FRIEND,String.valueOf(-2)));
            }
        }else{
            //房间不存在
            sendToMe(makeOrder(INVITE_FRIEND,String.valueOf(-1)));
        }
    }

    @Override
    public void shutRoom(String userID, int roomPort) {
        if(rooms.containsKey(roomPort)){
            RoomServer roomServer = rooms.get(roomPort);
            try {
                roomServer.closeServer();
                rooms.remove(roomPort);
                System.out.println(userID + " to delete " + new User(userID,"test").destroyHouse(roomPort) + " on delete room " + roomPort);
            } catch (IOException e) {
                System.out.println(e + "in shut room");
                sendToMe(makeOrder(InfoInterface.SHUT_ROOM,FAIL,String.valueOf(-1)));
            }
            sendToMe(makeOrder(InfoInterface.SHUT_ROOM,SUCCESS,String.valueOf(0)));
        }else{
            sendToMe(makeOrder(InfoInterface.SHUT_ROOM,FAIL,String.valueOf(-2)));
        }
    }

}
