package Repository;

/**
 * 测试类
 *
 * @author 郭英贤
 */
public class Test {
    public static void main(String[] args) {
        User u1 = new User("u55", "123");
        u1.register();
        u1.login();
        System.out.println(u1.createHouse(1,"1","1"));
        System.out.println(u1.destroyHouse(1));
//        User u2 = new User("u1", "1rgr23");
//        User u3 = new User("u3", "123w");
//
//        u1.register();
//        u2.register();
//        u3.register();
//
//        u1.login();
//        u1.login();
//        int h1 = u1.createHouse("hhh", "h1");
//        int h2 = u1.createHouse("hhh", "h2");
//        int h3 = u1.createHouse("aaa", "bbb");
//
//
//        u2.enterHouse(h1, "h1");
//
//        u1.enterHouse(h1, "h1");
//        u1.enterHouse(h2, "h1");
//        u1.enterHouse(h2, "h2");
//        u1.sendMessage(h1, "message1");
//        u1.sendMessage(h2, "message2");
//        u1.sendMessage(h3, "message3");
//
//        u3.login();
//        u3.enterHouse(h1, "h1");
//
//        u3.quitHouse(h1);
//
//        u1.quitHouse(h2);
//
//        u1.transferHouse(h3, u3.getId());
//
//
//        u1.addFriend(u3.getId());
//        u1.addFriend(u3.getId());
//        u1.inviteFriendToHouse(u3.getId(), h1);
//
//        u1.deleteFriend(u3.getId());
//        u1.deleteFriend(u3.getId());
//        u1.addFriend(u3.getId());
//
//        HouseList.getInstance().printInfo();
//        UserList.getInstance().printInfo();
    }
}
