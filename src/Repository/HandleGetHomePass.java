package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleGetHomePass {
    private Connection con;
    private PreparedStatement preSql;

    HandleGetHomePass() {
        con = new JDBC().getCon();
    }

    public String queryVerify(int hid) {
        String sqlStr = "select password from `house` where id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, hid);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                String pass = rs.getString("password");
                con.close();
                return pass;
            } else {
                con.close();
                JOptionPane.showMessageDialog(null, "房间不存在或已被销毁", "警告", JOptionPane.WARNING_MESSAGE);
                return "";
            }
        } catch (SQLException e) {
            System.out.println("GetHomePass:" + e);
        }
        return "";
    }
}
