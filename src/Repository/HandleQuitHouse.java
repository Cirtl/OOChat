package Repository;

import javax.swing.*;
import java.sql.*;

public class HandleQuitHouse {
    Connection con;
    PreparedStatement preSql;

    HandleQuitHouse() {
        con = new JDBC().getCon();
    }

    public int queryVerify(String uid, int hid) {
        String sqlStr = "delete from `house` where host=? and id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, uid);
            preSql.setInt(2, hid);
            int ok = preSql.executeUpdate();
            if (ok!=0) {
                JOptionPane.showMessageDialog(null, "该房间已销毁", "警告", JOptionPane.WARNING_MESSAGE);
                con.close();
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("QuitHouse_host:"+e);
        }

        sqlStr = "delete from `user_house` where user_id=? and house_id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, uid);
            preSql.setInt(2, hid);
            int ok = preSql.executeUpdate();
            con.close();
            JOptionPane.showMessageDialog(null, "退出房间", "警告", JOptionPane.WARNING_MESSAGE);
            return 1;
        } catch (SQLException e) {
            System.out.println("QuitHouse_notHost:"+e);
        }
        return 0;
    }
}
