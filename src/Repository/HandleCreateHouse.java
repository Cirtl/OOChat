package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandleCreateHouse {
    Connection con;
    PreparedStatement preSql;

    HandleCreateHouse() {
        con = new JDBC().getCon();
    }

    public int writeRegisterModel(House register) {
        String sqlStr = "insert into `house` values(?,?,?,?,?)";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, register.getId());
            preSql.setString(2, register.getName());
            preSql.setString(3, register.getPass());
            preSql.setString(4, null);
            preSql.setString(5, register.getHost_id());
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "房间申请成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return register.getId();
            } else {
                JOptionPane.showMessageDialog(null, "房间申请失败，请重试", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "房间申请失败，请重试", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("CreateHouse:"+e);
        }
        return -1;
    }
}
