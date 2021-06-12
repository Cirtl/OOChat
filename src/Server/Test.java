package Server;

import java.io.IOException;
import java.util.Scanner;

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
        System.out.println("服务器启动完成，输入 QUIT 关闭服务器");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String order = scanner.nextLine();
            if (order.equals("QUIT"))
                break;
            else
                System.out.println("未知命令");
        }
        scanner.close();
        loginServer.closeServer();
        infoServer.closeServer();
    }

}
