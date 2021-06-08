package Server.Info;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import Server.ServerInterfaces.InfoInterface;
import Server.Room.RoomServer;
import Server.ServerThread;

public class InfoThread  extends ServerThread implements  InfoInterface {
    //储存所有房间
    private static Map<Integer, RoomServer> rooms = new ConcurrentHashMap<>();
    //储存所有登录线程的用户
    private Map<String, InfoThread> clientMap;
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
            sendToMe(makeOrder(DISCONNECT,"INFO"));
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
            scanner = new Scanner(client.getInputStream());
            sendToMe("进入信息服务器");
            while(isRunning){
                String data="";
                if(scanner.hasNext())
                    data = scanner.nextLine();
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

                }else if(data.startsWith(InfoInterface.INVITE_FRIEND)){

                }else if(data.startsWith(InfoInterface.SHUT_ROOM)){
                    String[] info = data.split(DIVIDER,3);
                    if(info.length>2){
                        String id = info[0],room = info[2];
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
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRoom(String userID, int port) {

    }

    @Override
    public void getMyRooms() {
        StringBuilder builder = new StringBuilder();
        if(rooms.keySet().isEmpty()){
            sendToMe(makeOrder(InfoInterface.MY_ROOMS,"empty"));
        }else{
            for(Integer port:rooms.keySet()){
                builder.append(port).append(DIVIDER);
            }
            sendToMe(makeOrder(InfoInterface.MY_ROOMS,builder.toString()));
        }
    }

    @Override
    public void newRoom(String userID, int roomPort, String pwd) {
        //todo:对数据库操作
        if(roomPort<0||roomPort>9000){
            sendToMe(makeOrder(InfoInterface.NEW_ROOM,FAIL, String.valueOf(-1)));
        }
        else if(rooms.containsKey(roomPort)){
            sendToMe(makeOrder(InfoInterface.NEW_ROOM,FAIL, String.valueOf(-2)));
        }else{
            try{
                RoomServer roomServer = new RoomServer(roomPort,userID);
                rooms.put(roomPort,roomServer);
                new Thread(roomServer).start();
                sendToMe(makeOrder(InfoInterface.NEW_ROOM,SUCCESS, String.valueOf(roomPort)));
            }catch (IOException e){
                e.printStackTrace();
                rooms.remove(roomPort);
                sendToMe(makeOrder(InfoInterface.NEW_ROOM,FAIL, String.valueOf(-3)));
            }
        }
    }

    @Override
    public void enterRoom(String userID, int roomPort, String pwd) {
        //todo:对数据操作
        if(rooms.containsKey(roomPort)){
            RoomServer roomServer = rooms.get(roomPort);
            if(roomServer.inRoom(userID))
                sendToMe(makeOrder(InfoInterface.ENTER_ROOM,FAIL, String.valueOf(-1)));
            else
                sendToMe(makeOrder(InfoInterface.ENTER_ROOM,SUCCESS, String.valueOf(roomPort)));
        }
        else
            sendToMe(makeOrder(InfoInterface.ENTER_ROOM,FAIL, String.valueOf(-2)));
    }

    @Override
    public void inviteFriend(String userID, int roomPort, String friendID) {

    }

    @Override
    public void shutRoom(String userID, int roomPort) {
        if(rooms.containsKey(roomPort)){
            RoomServer roomServer = rooms.get(roomPort);
            try {
                roomServer.closeServer();
            } catch (IOException e) {
                e.printStackTrace();
                sendToMe(makeOrder(InfoInterface.SHUT_ROOM,FAIL));
            }
            sendToMe(makeOrder(InfoInterface.SHUT_ROOM,SUCCESS));
        }else{
            sendToMe(makeOrder(InfoInterface.SHUT_ROOM,FAIL));
        }
    }

}
