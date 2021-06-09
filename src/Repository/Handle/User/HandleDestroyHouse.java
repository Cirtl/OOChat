package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库操作用户退出房间
 * 用户是房主则房间销毁
 *
 * @author 郭英贤
 */
public class HandleDestroyHouse {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleDestroyHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库操作用户销毁房间
     * 用户是房主则房间销毁
     *
     * @param uid 用户ID
     * @param hid 房间号
     * @return 销毁返回true，失败返回false
     * @see SQLException
     */
    public boolean queryVerify(String uid, int hid) {
        String sqlStr = "delete from `house` where host=? and id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, uid);
            preSql.setInt(2, hid);
            int ok = preSql.executeUpdate();
            if (ok != 0) {
                // JOptionPane.showMessageDialog(null, "该房间已销毁", "警告", JOptionPane.WARNING_MESSAGE);
                con.close();
                return true;
            }
            con.close();
            return false;
        } catch (SQLException e) {
            System.out.println("QuitHouse_host:" + e);
        }
        return false;
    }
}
