package Server.Room;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import Repository.User;
import Server.ServerInterfaces.ChatterInterface;
import Server.ServerThread;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public class RoomThread extends ServerThread implements ChatterInterface {

    private boolean isRunning;

    private String userID;

    private RoomServer roomServer;

    protected Map<String, RoomThread> clientMap;

    private Scanner receiver;

    private User user;

    public RoomThread(Socket client, Map<String, RoomThread> clientMap, RoomServer roomServer) {
        super(client);
        this.clientMap = clientMap;
        this.roomServer = roomServer;
        this.isRunning = true;
    }

    public boolean sameUser(String id) {
        return id.equals(userID);
    }

    @Override
    public void sendToMe(String msg) {
        try {
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(msg);
        } catch (IOException e) {
            System.out.println(e + "   when room send to me");
        }
    }

    @Override
    public void closeThread() {
        try {
            sendToMe(makeOrder(DISCONNECT, "CHAT"));
            this.isRunning = false;
            clientMap.remove(userID);
            //房间内其他成员刷新用户列表
            StringBuilder userIDs = new StringBuilder();
            for (String userID : clientMap.keySet()) {
                userIDs.append(userID).append(DIVIDER);
            }
            sendToAll(makeOrder(ROOM_INFO, userIDs.toString()));
            //
            client.close();
            System.out.println(userID + " leave room " + roomServer.portNum);
        } catch (IOException e) {
            System.out.println("in close chat" + "  "+e);
        }

    }

    @Override
    public void sendToAll(String msg) {
        for (Map.Entry<String, RoomThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().sendToMe(msg);
        }
    }

    @Override
    public void sendToSomeOne(String toID, String msg) {
        for (Map.Entry<String, RoomThread> stringChatThreadEntry : clientMap.entrySet()) {
            if (stringChatThreadEntry.getKey().equals(toID)) {
                stringChatThreadEntry.getValue().sendToMe(msg);
                break;
            }
        }
    }

    /**
     * 初始化聊天端口并加入房间
     *
     * @param userID
     */
    private void init(String userID) {
        user = new User(userID,"-1");
        this.userID = userID;
        //todo:修改房间
        if (this.userID != null && !this.clientMap.containsKey(userID)) {
            this.clientMap.put(userID, this);
            //房间内其他成员刷新用户列表
            StringBuilder idBuilder = new StringBuilder();
            for (String id : clientMap.keySet()) {
                idBuilder.append(id).append(DIVIDER);
            }
            sendToAll(makeOrder(ROOM_INFO, idBuilder.toString()));
            sendMsg("我进入了聊天室");
        } else {
            sendToMe(SEND_MSG + DIVIDER + userID + "你已经在聊天室中啦");
            leaveRoom(0);
        }
    }

    @Override
    public void run() {
        try {
            this.receiver = new Scanner(client.getInputStream());
            while (isRunning) {
                String data = "";
                if (receiver.hasNext())
                    data = receiver.nextLine();
                if (data.isEmpty()) {
                    System.out.println("chat break down ");
                    leaveRoom(1);
                    break;
                }
                System.out.println("receive from CHAT " + client + " " + data);
                if (data.startsWith(ChatterInterface.SEND_MSG)) {
                    String[] info = data.split(DIVIDER, 2);
                    if (userID == null) {
                        //初始化
                        if (info.length > 1)
                            init(info[1]);
                    } else {
                        //群发
                        if (info.length > 1) {
                            sendMsg(info[1]);
                        }
                    }
                } else if (data.startsWith(WHISPER)) {
                    //私发
                    String[] info = data.split(DIVIDER, 3);
                    if (info.length > 2) {
                        whisperMsg(info[1], info[2]);
                    }
                } else if (data.startsWith(LEAVE_ROOM)) {
                    leaveRoom(0);
                } else if (data.startsWith(REMOVE_FROM_ROOM)) {
                    String[] info = data.split(DIVIDER, 2);
                    if (info.length > 1) {
                        removeFromRoom(info[1]);
                    }
                } else if (data.startsWith(ROOM_INFO)) {
                    getRoomInfo();
                } else if (data.startsWith(DISCONNECT)) {
                    leaveRoom(0);
                }
            }
        } catch (IOException e) {
            System.out.println(e + "in room running");
        }
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {
        sendToSomeOne(receiverID, ChatterInterface.WHISPER + DIVIDER + userID + DIVIDER + msg);
    }

    @Override
    public void removeFromRoom(String receiverID) {
        if (this.userID.equals(roomServer.host)) {
            for (Map.Entry<String, RoomThread> stringChatThreadEntry : clientMap.entrySet()) {
                if (stringChatThreadEntry.getKey().equals(receiverID)) {
                    stringChatThreadEntry.getValue().leaveRoom(1);
                    break;
                }
            }
        }
    }

    @Override
    public void getRoomInfo() {
        StringBuilder userIDs = new StringBuilder();
        for (String userID : clientMap.keySet()) {
            userIDs.append(userID).append(DIVIDER);
        }
        sendToMe(makeOrder(ROOM_INFO, userIDs.toString()));
    }

    @Override
    public void leaveRoom(int way) {
        //todo:数据库测试
        if(user.quitHouse(roomServer.portNum)){
            //发送离开信息
            sendMsg("[离开了聊天室]");
            sendToMe(ChatterInterface.LEAVE_ROOM + DIVIDER + way);
            closeThread();
        }else{
            //todo:退出房间失败
            sendToMe(makeOrder(LEAVE_ROOM,FAIL));
        }
    }

    @Override
    public void sendMsg(String msg) {
        sendToAll(ChatterInterface.SEND_MSG + DIVIDER + userID + DIVIDER + msg);
    }
}
