package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询是否为好友
 *
 * @author 郭英贤
 */
public class HandleIsFriend {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleIsFriend() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询是否为好友
     *
     * @param id1 friend1.
     * @param id2 friend2.
     * @return 是否为好友
     * @see SQLException
     */
    public boolean queryVerify(String id1, String id2) {
        String sqlStr = "select * from `friend` where id_1=? and id_2=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id1);
            preSql.setString(2, id2);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                con.close();
                return true;
            } else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SearchIsFriend:" + e);
        }
        return false;
    }
}
