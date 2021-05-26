package src.Client.ClientThread;

public interface ChatInterface {
    void onReceiveMessage(String msg);
    void onReceiveObject(String objectName,Object o);
}
