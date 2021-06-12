package src.dao;

import src.Client.Client;
import src.Client.ClientCallback;
import src.entity.Room;
import src.frame.LoginWindow;
import src.frame.MainWindow;
import src.frame.RegisterWindow;
import src.frame.RoomWindow;

import javax.swing.*;
import java.util.List;

public class Repository {
    private static final Repository instance = new Repository();

    private boolean isInit = false;
    private Client client;

    private RegisterWindow registerCaller;
    private LoginWindow loginCaller;
    private MainWindow mainCaller;
    private RoomWindow roomCaller;

    private Repository() {}

    public void setInit(boolean init) {
        isInit = init;
    }

    private void setClient(Client client) {
        this.client = client;
        this.client.addCallBack(new ClientResponse());
    }


    /**
     * 初始化client属性
     * @param client 记得处理好异常
     */
    public static void init(Client client) {
        instance.setClient(client);
        instance.setInit(true);
        instance.client.runClient();
    }


    /**
     * @return null表示未初始化
     */
    public static Repository getRepository() {
        if (instance.isInit) {
            return instance;
        } else {
            return null;
        }
    }


    /**
     * 注册操作
     * @param caller 调用者
     * @param id 用户名
     * @param pwd 密码
     */
    public void register(RegisterWindow caller, String id, String pwd) {
        registerCaller = caller;
        client.userRegister(id, pwd);
    }



    /**
     * 登录操作
     * @param caller 调用者
     * @param id 用户名
     * @param pwd 密码
     */
    public void login(LoginWindow caller, String id, String pwd) {
        loginCaller = caller;
        client.userLogin(id, pwd);
    }


    /**
     * 退出登录操作
     */
    public void logout() {
        client.userLogout();
    }


    /**
     * 刷新房间列表
     * @param caller 调用者
     */
    public void updateRooms(MainWindow caller) {
        mainCaller = caller;
        client.getMyRooms();
    }


    /**
     * 新建房间
     * @param caller 调用者
     * @param roomPort 房间序号
     * @param pwd 房间密码
     */
    public void newRoom(MainWindow caller, int roomPort, String pwd) {
        mainCaller = caller;
        client.newRoom(roomPort, pwd);
    }


    /**
     * 删除房间
     * @param caller 调用者
     * @param roomPort 删除的房间号
     */
    public void deleteRoom(MainWindow caller, int roomPort) {
        mainCaller = caller;
        client.deleteRoom(roomPort);
    }


    /**
     * 用户进入房间
     * @param caller 调用者
     * @param room 对应房间
     * @param inPwd 输入的密码
     */
    public void enterRoom(MainWindow caller, Room room, String inPwd) {
        mainCaller = caller;
        client.enterRoom(room.getId(), inPwd);
    }


    /**
     * 获取所处房间信息
     * @param caller 调用者
     */
    public void updateMembers(RoomWindow caller) {
        roomCaller = caller;
        client.getRoomInfo();
    }


    /**
     * 用户离开房间
     * @param caller 调用者
     */
    public void leaveRoom(RoomWindow caller) {
        roomCaller = caller;
        client.leaveRoom();
    }


    /**
     * 用户在聊天室发送信息
     * @param caller 调用者
     * @param msg 发送的消息
     */
    public void sendMsg(RoomWindow caller, String msg) {
        roomCaller = caller;
        client.sendMsg(msg);
    }


    /**
     * 用户私聊发送消息
     * @param caller 调用者
     * @param msg 消息
     * @param receiver 接收者
     */
    public void whisperMsg(RoomWindow caller, String msg, String receiver) {
        roomCaller = caller;
        client.whisperMsg(receiver, msg);
    }


    /**
     * 踢人，非房主则无事发生
     * @param caller 调用者
     * @param receiver 被踢者
     */
    public void kickUser(RoomWindow caller, String receiver) {
        roomCaller = caller;
        client.removeFromRoom(receiver);
    }


    /**
     * 关闭与服务器的连接，程序推出之前必须执行
     */
    public void close() {
        client.closeClient();
    }








    /**
     * 内部类，负责实现回调函数
     */
    private class ClientResponse implements ClientCallback {
        @Override
        public void onDisconnect(int channel) {

        }

        @Override
        public void onReceiveMsg(String senderName, String msg, int isWhisper) {
            if (roomCaller != null) {
                roomCaller.updateRecord(senderName, null, msg, isWhisper == 1);
            }
        }

        @Override
        public void onLogin(int result, String ID) {
            switch (result) {
                case 0:
                    loginCaller.loginSuccess();
                    break;
                case -1:
                    loginCaller.loginFail();
                    break;
            }
        }

        @Override
        public void onRegister(int result) {
            switch (result) {
                case 0:
                    registerCaller.registerSuccess();
                    break;
                case -1:
                    registerCaller.registerFail();
                    break;
            }
        }

        @Override
        public void onLogout(int result) {

        }

        @Override
        public void onDeleteRoom(int result) {
            mainCaller.deleteRoomResult(result);
        }

        @Override
        public void onEnterRoom(int result, int roomPort) {
            System.out.println("call back");
            switch (result) {
                case 0:
                    mainCaller.enterRoomSuccess(roomPort);
                    break;
                case -1:
                    mainCaller.enterRoomFail(roomPort);
                    break;
            }
        }

        @Override
        public void onNewRoom(int result, int roomPort) {
            String info;
            switch (result) {
                case 0:
                    info = "创建成功";
                    break;
                case -1:
                    info = "创建失败";
                    break;
                case 1:
                    info = "序号对应房间已存在或您已创建自己的房间";
                    break;
                default:
                    info = "默认显示";
                    break;
            }
            JOptionPane.showMessageDialog(mainCaller, info);
        }

        @Override
        public void onLeaveRoom(int result) {
            switch (result) {
                case 0:
                case 1:
                case 2:
                    roomCaller.leaveOk(result);
                    break;
                case -3:
                    roomCaller.leaveFail();
            }
        }

        @Override
        public void onMyRoomList(int result, List<Integer> rooms, List<String> owners) {
            switch (result) {
                case 0:
                case -1:
                    mainCaller.updateRoomsView(rooms, owners);
                    break;
                case -2:
                    mainCaller.fetchRoomsFail();
                    break;
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
        public boolean onBeingInvited(String invitorID, int roomPort) {
            return false;
        }

        @Override
        public void onInviteFriend(int result) {

        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onGetRoomInfo(int result, List<String> chatters) {
            switch (result) {
                case 0:
                    roomCaller.fetchUserListSuccess(chatters);
                    break;
                case 1:
                    roomCaller.fetchFail();
                    break;
            }
        }

        @Override
        public void onRunRoom(int result, int roomPort) {

        }

        @Override
        public void onGetFriends(int result, List<String> friends) {

        }
    }
}
