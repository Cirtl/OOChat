package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询是否有此用户
 *
 * @author 郭英贤
 */
public class HandleIsUser {
    private final Connection con;
    private PreparedStatement preSql;

    HandleIsUser() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询是否有此用户
     *
     * @param id 用户ID
     * @return 是否有此用户
     * @see SQLException
     */
    public boolean queryVerify(String id) {
        String sqlStr = "select * from `user` where id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                con.close();
                return true;
            } else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SearchIsUser:" + e);
        }
        return false;
    }
}
