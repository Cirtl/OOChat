package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询房间密码
 *
 * @author 郭英贤
 */
public class HandleGetHomePass {
    private final Connection con;
    private PreparedStatement preSql;

    HandleGetHomePass() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询房间密码
     *
     * @param hid 房间号
     * @return 房间密码
     * @see SQLException
     */
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
