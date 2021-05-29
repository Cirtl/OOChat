package Repository;

import java.util.Vector;

public class UserList {
    private static UserList list;
    private Vector<String> userList;

    private UserList() {
    }

    public static UserList getInstance() {
        if (list == null) {
            list = new UserList();
        }
        return list;
    }

    public Vector<String> getUserList() {
        userList = new HandleSearchUserListOfAll().queryVerify();
        return userList;
    }

    public void setUserList(Vector<String> userList) {
        this.userList = userList;
    }

    public void printInfo() {
        System.out.println("UserList:");
        for (String item : this.getUserList()) {
            System.out.println("id: " + item);
        }
    }
}
