package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 数据库查询所有用户
 *
 * @author 郭英贤
 */
public class HandleSearchUserListOfAll {
    private final Connection con;
    private PreparedStatement preSql;

    HandleSearchUserListOfAll() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询所有用户
     *
     * @return 所有用户ID列表
     * @see SQLException
     */
    public Vector<String> queryVerify() {
        String sqlStr = "select * from `user`";
        Vector<String> userList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                userList.add(rs.getString("id"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchUserListOfAll:" + e);
        }
        return userList;
    }
}
