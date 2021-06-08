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
        LoginServer loginServer = new LoginServer(port_login);
        InfoServer infoServer = new InfoServer(port_info);
        new Thread(infoServer).start();
        new Thread(loginServer).start();

        while(true){
            int nbRunning = 0;
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getState()==Thread.State.RUNNABLE)
                    nbRunning++;
            }
            System.out.println("当前运行线程" + nbRunning);
            Thread.sleep(5000);
        }

    }

}
