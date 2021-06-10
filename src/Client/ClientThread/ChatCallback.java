package src.Client.ClientThread;

public interface ChatCallback {
    void onReceiveMessage(String option, String[] info);
    void onReceiveObject(String objectName,Object o);
}
