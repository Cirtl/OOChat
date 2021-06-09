package Repository.Handle.User_House;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 数据库查询用户进入的房间列表
 *
 * @author 郭英贤
 */
public class HandleGetHouseListByUser {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleGetHouseListByUser() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询用户进入的房间列表
     *
     * @param id 用户ID
     * @return 房间号列表
     * @see SQLException
     */
    public Vector<String> queryVerify(String id) {
        String sqlStr = "select house_id from `user_house` where user_id=?";
        Vector<String> houseList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                houseList.add(rs.getString("house_id"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchHouseListByUser:" + e);
        }
        return houseList;
    }
}
