package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleLogin {
    private Connection con;
    private PreparedStatement preSql;
    ResultSet rs;

    public HandleLogin() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String id, String pass) {
        String sqlStr = "update `user` set isLogin=? where id=? and password=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setBoolean(1,true);
            preSql.setString(2, id);
            preSql.setString(3, pass);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "登录成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("Login:" + e);
        }
        return false;
    }
}
