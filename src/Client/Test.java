package src.Client;

import java.io.IOException;
import java.util.List;
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
            public void onMyRoomList(int result, List<Integer> rooms, List<String> owners) {
                if(result==0){
                    for(int i=0;i<rooms.size();i++)
                        System.out.println(rooms.get(i) + "拥有者为" + owners.get(i));
                }

            }


            @Override
            public void onMakeFriend(int result, String friendID) {

            }

            @Override
            public boolean onAskedBeFriend(String friendID) {
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
        });
        //登录
        client.userLogin("杨东浩","test1");
        Thread.sleep(300);
        //获取房间
        client.getMyRooms();
        Thread.sleep(300);
        //开房
        client.newRoom(8003,"123");
        Thread.sleep(300);
        //进入房间
        client.enterRoom(8003,"123");
        Thread.sleep(300);
        //输入
        Scanner scanner = new Scanner(System.in);
        while(true){
            String s = scanner.nextLine();
            if(s.equals("QUIT")) break;
            client.sendMsg(s);
        }
        client.closeClient();
    }
}
