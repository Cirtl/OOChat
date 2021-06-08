package Repository.Handle.User_House;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库查询用户是否在房间
 *
 * @author 郭英贤
 */
public class HandleIsUserInHouse {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleIsUserInHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询用户是否在房间
     *
     * @param id  用户ID
     * @param hid 房间号
     * @return 用户是否在该房间
     * @see SQLException
     */
    public boolean queryVerify(String id, int hid) {
        String sqlStr = "select * from `user_house` where user_id=? and house_id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            preSql.setInt(2, hid);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                con.close();
                return true;
            } else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SearchUserInHouse:" + e);
        }
        return false;
    }
}
