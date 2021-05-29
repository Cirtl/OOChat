package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandleDeleteFriend {
    private Connection con;
    private PreparedStatement preSql;

    HandleDeleteFriend() {
        con = new JDBC().getCon();
    }

    public int queryVerify(String id1, String id2) {
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
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("QuitHouse_host:" + e);
        }
        return 0;
    }
}
