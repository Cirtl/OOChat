package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleLogin {
    Connection con;
    PreparedStatement preSql;
    ResultSet rs;

    public HandleLogin() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String id, String pass) {
        String sqlStr = "select id from `user` where id=? and password=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            preSql.setString(2, pass);
            rs = preSql.executeQuery();
            if (rs.next() == true) {
                JOptionPane.showMessageDialog(null, "登录成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                con.close();
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "登录失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("Login:" + e);
        }
        return false;
    }
}
