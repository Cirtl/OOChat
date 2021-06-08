package src.Client;

import java.io.IOException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        client.runClient();
        client.addCallBack(new ClientCallback() {
            @Override
            public void onDisconnect(int channel) {

            }

            @Override
            public void onReceiveMsg(String senderName, String msg, int isWhisper) {
                if(isWhisper==0)
                    System.out.println(senderName + ":" + msg);
                else
                    System.out.println(senderName + "[私聊]:" + msg);
            }

            @Override
            public void onLogin(int result, String ID) {
                System.out.println(result + " " +ID + " from login");
            }

            @Override
            public void onRegister(int result) {
                System.out.println(result + " " + " from register");
            }

            @Override
            public void onLogout(int result) {
                System.out.println(result + " " + " from logout");
            }

            @Override
            public void onDeleteRoom(int result) {

            }

            @Override
            public void onEnterRoom(int result, int roomPort) {
            }

            @Override
            public void onNewRoom(int result, int roomPort) {

            }

            @Override
            public void onLeaveRoom(int result) {

            }

            @Override
            public void onMyRoomList(int result,int[] rooms,int[] pwd) {

            }

            @Override
            public void onMakeFriend(int result) {

            }

            @Override
            public void onShutRoom(int result) {

            }

            @Override
            public void onBeingInvited(String invitorID, int roomPort) {

            }

            @Override
            public void onInviteFriend(int result) {

            }

            @Override
            public void onException(Exception e) {

            }
        });
        client.userLogin("杨东浩","test1");
        Thread.sleep(300);
        client.newRoom(8003,"123");
        Thread.sleep(300);
        client.enterRoom(8003,"123");
        Thread.sleep(300);
        Scanner scanner = new Scanner(System.in);
        while(true){
            String s = scanner.nextLine();
            if(s.equals("QUIT")) break;
            client.sendMsg(s);
        }
        client.closeClient();
    }
}
