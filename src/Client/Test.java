package Client;

import java.io.IOException;

public class Test {
    static int port = 8000;
    static int maxNum = 10;
    static String host = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        /** 新建客户端连接服务器 **/
        Client client1 = new Client(host,port, System.in);
    }
}
