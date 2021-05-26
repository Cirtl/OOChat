package src.Client;

import java.io.IOException;

import src.Client.Chat.*;

public class Test {
    static int port = 8000;
    static int maxNum = 10;
    static String host = "0.0.0.0";

    public static void main(String[] args) throws IOException {
        ChatThread chatThread = new ChatThread(host,port);
        chatThread.addCallback(msg -> System.out.println(msg));
        chatThread.runClient();
    }
}
