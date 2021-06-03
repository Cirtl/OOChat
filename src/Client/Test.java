package src.Client;

import java.io.IOException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.addCallBack(new ClientCallback() {
            @Override
            public void onReceiveMsg(String senderName, String msg) {
                System.out.println(senderName + ":" + msg);
            }

            @Override
            public void onLoginSuccess() {
                System.out.println("local:login success");
            }

            @Override
            public void onLoginFailed() {
                System.out.println("local:login fail");
            }

            @Override
            public void onRegisterSuccess() {
                System.out.println("local:reg success");
            }

            @Override
            public void onRegisterFailed() {
                System.out.println("local:reg fail");
            }

            @Override
            public void onLogoutSuccess() {
                System.out.println("local:logout");
            }

            @Override
            public void onEnterRoom() {
                System.out.println("local:enter room");
            }

            @Override
            public void onLeaveRoom() {
                System.out.println("local:enter room");
            }

            @Override
            public void onException(Exception e) {
                System.out.println("local" + e);
            }
        });
        client.userLogin("123","213");
        client.userRegister("213","213");
        client.userLogout();
        Thread.sleep(3);
        client.enterRoom("213");
        client.enterRoom("213");
        client.enterRoom("213");
        client.enterRoom("213");
        Thread.sleep(3);
        client.sendMsg("hello from me!!!!");
    }
}
