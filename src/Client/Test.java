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

            }

            @Override
            public void onLogin(int result, String ID) {
                System.out.println(result + " " +ID + "from login");
            }

            @Override
            public void onRegister(int result) {
                System.out.println(result + " " + "from register");
            }

            @Override
            public void onLogout(int result) {
                System.out.println(result + " " + "from logout");
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
            public void onMyRoomList(int[] rooms, int[] pwd) {

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
        Thread.sleep(30);
        client.shutRoom(8003);
        Thread.sleep(300);
        client.getMyRooms();
        Thread.sleep(10000);
        client.closeClient();
    }
}
