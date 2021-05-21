package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/*
向服务端发送数据
 */
public class ExecuteServerOutput implements Runnable{
    Socket serve;
    InputStream inputStream;
    public ExecuteServerOutput(Socket serve, InputStream inputStream) {
        this.serve = serve;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            PrintStream printStream = new PrintStream(serve.getOutputStream());
            //TODO:用InputStream
            Scanner scanner = new Scanner(inputStream);
            while (true){
                if(scanner.hasNext()) {
                    String string = scanner.next();
                    printStream.println(string);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
