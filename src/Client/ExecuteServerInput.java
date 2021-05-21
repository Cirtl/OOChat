package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
    处理服务端输入
 */
public class ExecuteServerInput implements Runnable{
    Socket serve;

    public ExecuteServerInput(Socket serve) {
        this.serve = serve;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(serve.getInputStream());
            while (scanner.hasNext()){
                System.out.println(scanner.nextLine());
            }
            scanner.close();
            serve.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
