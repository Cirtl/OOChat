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
        Thread.sleep(3);
        client.userLogin("213","213");
        Thread.sleep(3);
        client.userRegister("213","213");
        Thread.sleep(3);
        client.getMyRooms();
        Thread.sleep(3);
        client.userLogout();
        Thread.sleep(3);
        client.getMyRooms();
        Thread.sleep(3);
        client.userLogin("test","test");
        Thread.sleep(3);
        client.newRoom(8003,"new room");
        Thread.sleep(3);
        client.enterRoom(-1,"we");
        Thread.sleep(3);
        client.enterRoom(100000,"we");
        Thread.sleep(3);
        client.enterRoom(8003,"new room");
        Thread.sleep(3);
        client.shutRoom(8003);
        Thread.sleep(3);
        client.deleteRoom(8003);
        Thread.sleep(3);
        client.inviteFriend("wqe");
        Thread.sleep(3);
        client.closeClient();
    }
}
