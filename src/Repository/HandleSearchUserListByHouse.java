package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 数据库查询房间内用户列表
 *
 * @author 郭英贤
 */
public class HandleSearchUserListByHouse {
    private final Connection con;
    private PreparedStatement preSql;

    HandleSearchUserListByHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询房间内用户列表
     *
     * @param id 房间号
     * @return 房间内用户ID列表
     * @see SQLException
     */
    public Vector<String> queryVerify(int id) {
        String sqlStr = "select user_id from `user_house` where house_id=?";
        Vector<String> userList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, id);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                userList.add(rs.getString("user_id"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchUserListByHouse:" + e);
        }
        return userList;
    }
}
