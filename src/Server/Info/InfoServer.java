package Server.Info;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Repository.HouseList;
import Server.Room.RoomServer;

public class InfoServer implements Runnable{

    private static InfoServer infoServer;

    ServerSocket serverSocket;

    protected final ExecutorService executorService;

    Boolean isRunning = false;

    protected Map<String, InfoThread> clientMap;//存储所有的用户信息

    private static Map<Integer, RoomServer> rooms = new ConcurrentHashMap<>();

    private InfoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        clientMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(100);
    }

    public static InfoServer getInstance(int port) throws IOException {
        if(infoServer==null)
            infoServer = new InfoServer(port);
        return infoServer;
    }

    private boolean init(){
        try{
            Map<Integer, String[]> houseList =  HouseList.getInstance().getHouseList();
            for(Map.Entry<Integer,String[]> house : houseList.entrySet()){
                int roomPort = house.getKey();
                String[] info = house.getValue();
                RoomServer roomServer = new RoomServer(roomPort,info[2],info[1]);
                rooms.put(roomPort,roomServer);
                new Thread(roomServer).start();
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        if(!init()){
            try {
                closeServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(serverSocket.getInetAddress() + ":信息服务器启动失败");
            return;
        }
        System.out.println(serverSocket.getInetAddress() + ":信息服务器启动成功");
        while (isRunning) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("新用户链接信息端口:" + client.getInetAddress() + ",端口" + client.getPort());
                //新建服务端线程去处理客户
                executorService.submit(new InfoThread(client,clientMap,rooms));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer() throws IOException {
        for (Map.Entry<Integer, RoomServer> stringChatThreadEntry : rooms.entrySet()) {
            stringChatThreadEntry.getValue().closeServer();
        }
        for (Map.Entry<String, InfoThread> stringChatThreadEntry : clientMap.entrySet()) {
            stringChatThreadEntry.getValue().closeThread();
        }
        this.isRunning = false;
        serverSocket.close();
    }
}
