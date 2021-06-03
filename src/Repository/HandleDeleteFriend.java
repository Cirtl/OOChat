package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 向数据库双向删除好友
 *
 * @author 郭英贤
 */
public class HandleDeleteFriend {
    private final Connection con;
    private PreparedStatement preSql;

    HandleDeleteFriend() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库双向删除好友
     *
     * @param id1 friend1.
     * @param id2 friend2.
     * @return 删除成功与否
     * @see SQLException
     */
    public boolean queryVerify(String id1, String id2) {
        String sqlStr = "delete from `friend` where id_1=? and id_2=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id1);
            preSql.setString(2, id2);
            int ok = preSql.executeUpdate();

            preSql.setString(1, id2);
            preSql.setString(2, id1);
            ok = ok + preSql.executeUpdate();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "已双向删除好友", "警告", JOptionPane.WARNING_MESSAGE);
                con.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("QuitHouse_host:" + e);
        }
        return false;
    }
}
