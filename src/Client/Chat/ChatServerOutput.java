package src.Client.Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/*
    向服务端发送数据
 */
public class ChatServerOutput{
    Socket serve;
    PrintStream printStream;

    protected ChatServerOutput(Socket serve) throws IOException {
        this.serve = serve;
        printStream = new PrintStream(serve.getOutputStream());
    }

    protected void write(String str){
        printStream.println(str);
    }

    protected void close(){
        printStream.close();
    }
}
