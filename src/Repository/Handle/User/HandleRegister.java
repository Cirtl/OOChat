package Repository.Handle.User;

import Repository.Handle.JDBC;
import Repository.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库操作用户注册
 *
 * @author 郭英贤
 */
public class HandleRegister {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleRegister() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库添加新用户
     *
     * @param user 用户类
     * @return 添加成功与否
     * @see SQLException
     */
    public boolean writeRegisterModel(User user) {
        String sqlStr = "insert into `user` values(?,?,?)";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, user.getId());
            preSql.setString(2, user.getPass());
            preSql.setString(3, user.getIp());
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                // JOptionPane.showMessageDialog(null, "注册成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                // JOptionPane.showMessageDialog(null, "注册失败，用户名重复", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "注册失败，用户名重复", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("Repository.Handle.User.HandleRegister:" + e);
        }
        return false;
    }
}
