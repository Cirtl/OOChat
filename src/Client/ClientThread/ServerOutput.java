package src.Client.ClientThread;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/*
    向服务端发送数据
 */
public class ServerOutput {
    Socket serve;
    PrintStream printStream;

    protected ServerOutput(Socket serve) throws IOException {
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
