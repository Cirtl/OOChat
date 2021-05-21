package Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/*
向服务端发送数据
 */
public class ExecuteServerOutput implements Runnable{
    Socket serve;

    public ExecuteServerOutput(Socket serve) {
        this.serve = serve;
    }

    @Override
    public void run() {
        try {
            PrintStream printStream = new PrintStream(serve.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (true){
                if(scanner.hasNext()) {
                    String string = scanner.next();
                    printStream.println(string);
                    if ("QUIT".equals(string)) {
                        System.out.println("退出！");
                        printStream.close();
                        scanner.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
