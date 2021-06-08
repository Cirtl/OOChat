package Server.Room;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import Server.Interfaces.ChatterInterface;
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

    public ChatThread(Socket client, Map<String, ChatThread> clientMap, RoomServer roomServer) throws IOException {
        super(client);
        this.clientMap = clientMap;
        this.roomServer = roomServer;
        this.isRunning = true;
        this.receiver = new Scanner(client.getInputStream());
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
        try {
            this.isRunning = false;
            receiver.close();
            client.close();
        }catch (IOException e){
            e.printStackTrace();
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
        if (this.userID != null && !this.clientMap.containsKey(userID)) {
            this.clientMap.put(userID, this);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (receiver.hasNext()) {
                String data = receiver.nextLine();
                if (data.startsWith(ChatterInterface.SEND_MSG)) {
                    //群发
                    String[] info = data.split(DIVIDER, 2);
                    if (info.length > 1) {
                        sendMsg(info[1]);
                    }
                } else if (data.startsWith(ChatterInterface.WHISPER)) {
                    //私发
                    String[] info = data.split(DIVIDER, 3);
                    if (info.length > 2) {
                        whisperMsg(info[1],info[2]);
                    }
                } else if (data.startsWith(ChatterInterface.LEAVE_ROOM)) {
                    leaveRoom(0);
                } else if (data.startsWith(ChatterInterface.REMOVE_FROM_ROOM)) {
                    String[] info = data.split(DIVIDER, 2);
                    if (info.length > 1) {
                        removeFromRoom(info[1]);
                    }
                } else if (data.startsWith("userID")) {
                    String[] info = data.split(DIVIDER, 2);
                    if (info.length > 1)
                        init(info[1]);
                }
                break;
            }
        }
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {
        sendTSomeOne(receiverID,ChatterInterface.WHISPER + DIVIDER + userID + DIVIDER + msg);
    }

    @Override
    public void removeFromRoom(String receiverID) {
        if(this.client.equals(roomServer.host)){
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
        clientMap.remove(this);
        closeThread();
    }

    @Override
    public void sendMsg(String msg) {
        sendToAll(ChatterInterface.SEND_MSG + DIVIDER + userID + DIVIDER + msg);
    }
}
