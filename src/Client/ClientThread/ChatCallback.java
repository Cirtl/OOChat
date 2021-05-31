package src.Client.ClientThread;

import java.util.List;

public interface ChatCallback {
    void onReceiveMessage(String option, String[] info);
    void onReceiveObject(String objectName,Object o);
}
