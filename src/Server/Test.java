package Server;

import Server.Login.LoginServer;
import Server.Room.RoomServer;

import java.io.IOException;

public class Test {
    static int port = 8001;
    static int maxNum = 10;
    static String host = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        /** 运行服务器 **/
        LoginServer loginServer = new LoginServer();
        RoomServer roomServer = new RoomServer(maxNum, port);
        new Thread(loginServer).start();
        new Thread(roomServer).start();
    }
}
