package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 向数据库添加好友
 *
 * @author 郭英贤
 */
public class HandleAddFriend {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleAddFriend() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库添加好友
     *
     * @param id1 friend1.
     * @param id2 friend2.
     * @return 添加成功与否
     * @see SQLException
     */
    public boolean writeRegisterModel(String id1, String id2) {
        String sqlStr = "insert into `friend` values(?,?),(?,?)";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id1);
            preSql.setString(2, id2);
            preSql.setString(3, id2);
            preSql.setString(4, id1);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                // JOptionPane.showMessageDialog(null, "好友添加成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                // JOptionPane.showMessageDialog(null, "好友添加失败，请重试", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("AddFriend:" + e);
        }
        return false;
    }
}
