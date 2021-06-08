package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库操作转移房主
 *
 * @author 郭英贤
 */
public class HandleTransferHouse {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleTransferHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库转移房主
     *
     * @param hid    房间号
     * @param oldHid 原本房主ID
     * @param newHid 新房主ID
     * @return 更新成功与否
     * @see SQLException
     */
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
                // JOptionPane.showMessageDialog(null, "房间已更换群主", "警告", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                // JOptionPane.showMessageDialog(null, "房间更换群主失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(null, "房间更换群主失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("TransferHouse" + e);
        }
        return false;
    }
}
