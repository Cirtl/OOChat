package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandleTransferHouse {
    Connection con;
    PreparedStatement preSql;

    HandleTransferHouse() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(int hid, String oldHid, String newHid) {
        String sqlStr = "update `house` set host=? where host=? and id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, newHid);
            preSql.setString(2, oldHid);
            preSql.setInt(3, hid);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "房间已更换群主", "警告", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "房间更换群主失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "房间更换群主失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("TransferHouse"+e);
        }
        return false;
    }
}
