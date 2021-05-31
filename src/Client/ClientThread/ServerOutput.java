package src.Client.ClientThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

/*
    向服务端发送数据
 */
public class ServerOutput {
    Socket serve;

    PrintStream printStream;

    ObjectOutputStream objectOutputStream;

    protected ServerOutput(Socket serve) throws IOException {
        this.serve = serve;
        printStream = new PrintStream(serve.getOutputStream());
        objectOutputStream = new ObjectOutputStream(serve.getOutputStream());
    }

    protected void writeObject(Object object,String type) throws IOException {
        write(type);
        objectOutputStream.writeObject(object);
    }

    protected void write(String str){
        printStream.println(str);
    }

    protected void close() throws IOException {
        objectOutputStream.close();
        printStream.close();
    }
}
