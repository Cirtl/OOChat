import java.util.Vector;

public class UserList {
    private static UserList list;
    private Vector<User> userList;

    private UserList() {
        userList = new Vector<User>();
    }

    public static UserList getInstance() {
        if (list == null) {
            list = new UserList();
        }
        return list;
    }

    public Vector<User> getUserList() {
        return userList;
    }

    public void setUserList(Vector<User> userList) {
        this.userList = userList;
    }

    public void printInfo() {
        System.out.println("UserList:");
        for (User item : userList) {
            System.out.println("id:" + item.getId() + " password" + item.getPass() +
                    " ip:" + item.getIp() + " isLogin:" + item.isLogin());
        }
    }

    public User searchById(String id) {
        for (User item : userList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
