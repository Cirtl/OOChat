package Server;

import java.io.IOException;

import Server.Info.InfoServer;
import Server.Login.LoginServer;
import Server.Room.RoomServer;

public class Test {
    static int port_info = 8001;
    static int port_login = 8000;
    static int port_chat = 8002;
    static int maxNum = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
        /** 运行服务器 **/
        LoginServer loginServer = LoginServer.getInstance(port_login);
        InfoServer infoServer = InfoServer.getInstance(port_info);
        new Thread(infoServer,"info").start();
        new Thread(loginServer,"login").start();

//        Thread.sleep(10000);
//        loginServer.closeServer();
//        infoServer.closeServer();

        while(true){
            int nbRunning = 0;
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getState()==Thread.State.RUNNABLE){
                    nbRunning++;
                }
//                    System.out.println(t.getName());
            }
            System.out.println("当前运行线程" + nbRunning);
            Thread.sleep(5000);
        }

    }

}
