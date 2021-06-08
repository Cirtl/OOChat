package Server.Room;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

import Server.ServerInterfaces.ChatterInterface;
import Server.ServerThread;

/**
 * 服务器线程处理，一个线程对应一个客户端
 */
public class ChatThread extends ServerThread implements ChatterInterface {

    private boolean isRunning;

    private String userID;

    private RoomServer roomServer;

    protected Map<String, ChatThread> clientMap;

    private Scanner receiver;

    public ChatThread(Socket client, Map<String, ChatThread> clientMap, RoomServer roomServer) {
        super(client);
        this.clientMap = clientMap;
        this.roomServer = roomServer;
        this.isRunning = true;
    }

    public boolean sameUser(String id){
        return id.equals(userID);
    }

    @Override
    public void sendToMe(String msg) {
        try {
            PrintStream printStream = new PrintStream(client.getOutputStream());
            printStream.println(msg);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void closeThread() {
        try {
            sendToMe(makeOrder(DISCONNECT,"CHAT"));
            this.isRunning = false;
            receiver.close();
            client.close();
            System.out.println(userID + "leave room");
            clientMap.remove(userID);
        }catch (IOException e){
            System.out.println(e);
        }

    }

    /**
     * 向所有人发送消息，通过调用sendToMe实现
     *
     * @param msg 消息
     */
    private void sendToAll(String msg) {
        for (Map.Entry<String, ChatThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().sendToMe(msg);
        }
    }

    /**
     * 向所有人发送消息，通过调用sendToMe实现
     *
     * @param msg 消息
     */
    private void sendTSomeOne(String toID, String msg) {
        for (Map.Entry<String, ChatThread> stringChatThreadEntry : clientMap.entrySet()) {
            if (stringChatThreadEntry.getKey().equals(toID)) {
                stringChatThreadEntry.getValue().sendToMe(WHISPER + DIVIDER + userID + DIVIDER + msg);
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
        this.userID = userID;
        if (this.userID != null && !this.clientMap.containsKey(userID)) {
            this.clientMap.put(userID, this);
            sendMsg("我进入了聊天室，请大家欢迎我！");
        }else{
            sendToMe(SEND_MSG + DIVIDER + userID + "你已经在聊天室中啦");
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try{
                this.receiver = new Scanner(client.getInputStream());
                if (receiver.hasNext()) {
                    String data = receiver.nextLine();
                    System.out.println("receive from CHAT " + client + " " + data);
                    if (data.startsWith(ChatterInterface.SEND_MSG)) {
                        String[] info = data.split(DIVIDER, 2);
                        if (userID==null) {
                            //初始化
                            if (info.length > 1)
                                init(info[1]);
                        }
                        else{
                            //群发
                            if (info.length > 1) {
                                sendMsg(info[1]);
                            }
                        }
                    } else if (data.startsWith(WHISPER)) {
                        //私发
                        String[] info = data.split(DIVIDER, 3);
                        if (info.length > 2) {
                            whisperMsg(info[1],info[2]);
                        }
                    } else if (data.startsWith(LEAVE_ROOM)) {
                        leaveRoom(0);
                    } else if (data.startsWith(REMOVE_FROM_ROOM)) {
                        String[] info = data.split(DIVIDER, 2);
                        if (info.length > 1) {
                            removeFromRoom(info[1]);
                        }
                    } else if(data.startsWith(DISCONNECT)){
                        leaveRoom(0);
                    }
                }
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {
        sendTSomeOne(receiverID,ChatterInterface.WHISPER + DIVIDER + userID + DIVIDER + msg);
    }

    @Override
    public void removeFromRoom(String receiverID) {
        if(this.userID.equals(roomServer.host)){
            for (Map.Entry<String, ChatThread> stringChatThreadEntry : clientMap.entrySet()) {
                if (stringChatThreadEntry.getKey().equals(receiverID)) {
                    stringChatThreadEntry.getValue().leaveRoom(1);
                    break;
                }
            }
        }
    }

    @Override
    public String getRoomInfo() {
        return null;
    }

    @Override
    public void leaveRoom(int way) {
        sendToMe(ChatterInterface.LEAVE_ROOM+DIVIDER+way);
        closeThread();
    }

    @Override
    public void sendMsg(String msg) {
        sendToAll(ChatterInterface.SEND_MSG + DIVIDER + userID + DIVIDER + msg);
    }
}
