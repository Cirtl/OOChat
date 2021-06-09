package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库记录用户登录
 *
 * @author 郭英贤
 */
public class HandleLogin {
    ResultSet rs;
    private final Connection con;
    private PreparedStatement preSql;

    public HandleLogin() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库更新登录状态为已登录
     *
     * @param id 用户ID
     * @param pass 密码
     * @return 更新状态成功与否
     * @see SQLException
     */
    public boolean queryVerify(String id, String pass) {
        String sqlStr = "select * from `user`  where id=? and password=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            preSql.setString(2, pass);
            ResultSet rs = preSql.executeQuery();
            con.close();
            if (rs.next()) {
                // JOptionPane.showMessageDialog(null, "登录成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                // JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("Login:" + e);
        }
        return false;
    }
}
