package Repository;

import Repository.Handle.User.HandleGetUserListOfAll;

import java.util.Vector;

/**
 * 用户列表类
 * 单例模式
 * 记录用户姓名
 *
 * @author 郭英贤
 */
public class UserList {
    private static UserList list;
    private Vector<String> userList;

    /**
     * 构造私有化
     */
    private UserList() {
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static UserList getInstance() {
        if (list == null) {
            list = new UserList();
        }
        return list;
    }

    /**
     * Getter 用户列表
     *
     * @return 用户列表
     */
    public Vector<String> getUserList() {
        userList = new HandleGetUserListOfAll().queryVerify();
        return userList;
    }

    /**
     * Setter 用户列表
     *
     * @param userList 用户列表
     */
    public void setUserList(Vector<String> userList) {
        this.userList = userList;
    }

    /**
     * 打印用户列表
     */
    public void printInfo() {
        System.out.println("UserList:");
        for (String item : this.getUserList()) {
            System.out.println("id: " + item);
        }
    }
}
