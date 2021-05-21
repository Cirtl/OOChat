package Server;

import java.io.IOException;

public class Test {
    static int port = 8000;
    static int maxNum = 10;
    static String host = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        /** 运行服务器 **/
        RoomServer roomServer = new RoomServer(maxNum,port);
        roomServer.runServe();
    }
}
