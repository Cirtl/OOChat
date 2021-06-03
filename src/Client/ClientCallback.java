package src.Client;

public interface ClientCallback {
    void onReceiveMsg(String senderName,String msg);
    void onLoginSuccess();
    void onLoginFailed();
    void onRegisterSuccess();
    void onRegisterFailed();
    void onLogoutSuccess();
    void onEnterRoom();
    void onLeaveRoom();
    void onException(Exception e);
}
