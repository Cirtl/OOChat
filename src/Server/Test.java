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
    static String host = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        /** 运行服务器 **/
        LoginServer loginServer = new LoginServer(port_login);
        InfoServer infoServer = new InfoServer(port_info);
        new Thread(infoServer).start();
        new Thread(loginServer).start();
    }
}
