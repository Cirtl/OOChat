package Repository;

import javax.swing.*;
import java.sql.*;

public class HandleEnterHouse {
    private Connection con;
    private PreparedStatement preSql;

    HandleEnterHouse() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String uid, int houseId, String pass) {
        String sqlStr = "select id from `house` where id=? and password=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, houseId);
            preSql.setString(2, pass);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == false) {
                JOptionPane.showMessageDialog(null, "进入失败,房间号或密码错误", "警告", JOptionPane.WARNING_MESSAGE);
                con.close();
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "进入失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("EnterHouse_select_house:"+e);
        }

        String sqlStrEnter = "insert into `user_house` values(?,?)";
        try {
            preSql = con.prepareStatement(sqlStrEnter);
            preSql.setString(1, uid);
            preSql.setInt(2, houseId);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "进入成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "进入失败，增加信息失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "进入失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("EnterHouse_insert_user_house:"+e);
        }
        return false;
    }
}
