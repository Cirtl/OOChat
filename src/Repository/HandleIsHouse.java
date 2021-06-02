package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询是否存在该房间
 *
 * @author 郭英贤
 */
public class HandleIsHouse {
    private final Connection con;
    private PreparedStatement preSql;

    HandleIsHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询是否存在该房间
     *
     * @param hid 房间号
     * @return 房间是否仍存在
     * @see SQLException
     */
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
