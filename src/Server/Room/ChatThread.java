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

    public ChatThread(Socket client,Map<String, ChatThread> clientMap,RoomServer roomServer) throws IOException {
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
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeThread() {
        sendToMe("房间已关闭");
        this.isRunning = false;
    }

    /**
     * 向所有人发送消息，通过调用sendToMe实现
     * @param msg 消息
     */
    private void sendToAll(String msg){
        for (Map.Entry<String, ChatThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().sendToMe(msg);
        }
    }

    @Override
    public void run() {
        while (isRunning){
            if(receiver.hasNext()){
                String data = receiver.nextLine();
                if(data.startsWith(ChatterInterface.SEND_MSG)){

                }else if(data.startsWith(ChatterInterface.WHISPER)){

                }else if(data.startsWith(ChatterInterface.LEAVE_ROOM)){

                }else if(data.startsWith(ChatterInterface.REMOVE_FROM_ROOM)){

                }else
                    break;
            }
        }
    }

    @Override
    public void whisperMsg(String receiverID, String msg) {

    }

    @Override
    public void removeFromRoom(String receiverID) {

    }

    @Override
    public String getRoomInfo() {
        return null;
    }

    @Override
    public void leaveRoom() {

    }

    @Override
    public void sendMsg(String msg) {

    }
}
