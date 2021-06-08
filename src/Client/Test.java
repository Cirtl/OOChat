package src.Client;

import java.io.IOException;

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
            public void onException(Exception e) {

            }
        });
        client.getMyRooms();
        Thread.sleep(30);
        client.userLogin("test","test");
        Thread.sleep(30);
        client.newRoom(8003,"new room");
        Thread.sleep(300);
        client.newRoom(8003,"new room");
        Thread.sleep(300);
        client.newRoom(8004,"new room");
        Thread.sleep(300);
        client.getMyRooms();
        Thread.sleep(300);
        client.shutRoom(8004);
        Thread.sleep(300);
        client.getMyRooms();
        Thread.sleep(300);
        client.enterRoom(8003,"8003");
        Thread.sleep(300);
        client.sendMsg("12 34 56");
        Thread.sleep(300);
        client.whisperMsg("test","12 34 56");
        Thread.sleep(300);
        client.getMyRooms();
        Thread.sleep(15000);
        client.closeClient();
    }
}
