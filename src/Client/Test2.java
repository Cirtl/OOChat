package src.Client;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Test2 {

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
            public void onMyRoomList(int result, List<Integer> rooms, List<String> owners) {

            }


            @Override
            public void onMakeFriend(int result, String friendID) {

            }

            @Override
            public boolean onAskedBeFriend(String friendID) {
                System.out.println("refuse friend " + friendID);
                return false;
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

            @Override
            public void onGetRoomInfo(int result, List<String> chatters) {

            }

            @Override
            public void onRunRoom(int result, int roomPort) {

            }

            @Override
            public void onGetFriends(int result, List<String> friends) {

            }
        });
        client.userLogin("test2","test2");
        Thread.sleep(300);
        client.newRoom(8004,"213");
        Thread.sleep(30000);
//        Scanner scanner = new Scanner(System.in);
//        while(true){
//            String s = scanner.nextLine();
//            if(s.equals("QUIT")) break;
//            client.sendMsg(s);
//        }
        client.closeClient();
    }
}
