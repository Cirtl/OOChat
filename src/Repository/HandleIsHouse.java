package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleIsHouse {
    private Connection con;
    private PreparedStatement preSql;

    HandleIsHouse() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(int hid) {
        String sqlStr = "select * from `house` where id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, hid);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                con.close();
                return true;
            } else {
                con.close();
                JOptionPane.showMessageDialog(null, "房间不存在或已被销毁", "警告", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("IsHouse:" + e);
        }
        return false;
    }
}
