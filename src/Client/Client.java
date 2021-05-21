package Client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    ExecuteServerInput serverInput;
    ExecuteServerOutput serverOutput;
    Socket socket;

    public Client() throws IOException {
        socket = new Socket("127.0.0.1",8000);
        serverInput = new ExecuteServerInput(socket);
        serverOutput = new ExecuteServerOutput(socket);
    }

    public void runClient(){
        new Thread(serverInput).start();
        new Thread(serverOutput).start();
    }
}
