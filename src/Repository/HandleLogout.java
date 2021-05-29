package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleLogout {
    private Connection con;
    private PreparedStatement preSql;
    ResultSet rs;

    public HandleLogout() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String id, String pass) {
        String sqlStr = "update `user` set isLogin=? where id=? and password=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setBoolean(1,false);
            preSql.setString(2, id);
            preSql.setString(3, pass);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "注销成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "您已注销", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "注销失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("Logout:" + e);
        }
        return false;
    }
}
