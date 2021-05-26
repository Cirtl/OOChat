package src.Client;

public interface ClientCallback {
    void onLoginSuccess();
    void onLoginFailed();
    void onRegisterSuccess();
    void onRegisterFailed();
}
