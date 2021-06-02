package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询是否为房主
 *
 * @author 郭英贤
 */
public class HandleIsHost {
    private final Connection con;
    private PreparedStatement preSql;

    HandleIsHost() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询是否为房主
     *
     * @param uid 用户账号
     * @param hid 房间号
     * @return 是否为房主
     * @see SQLException
     */
    public boolean queryVerify(String uid, int hid) {
        String sqlStr = "select * from `house` where host=? and id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, uid);
            preSql.setInt(2, hid);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                con.close();
                return true;
            } else {
                con.close();
                JOptionPane.showMessageDialog(null, "不是群主", "警告", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("IsHouse:" + e);
        }
        return false;
    }
}
