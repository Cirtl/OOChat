package Repository.Handle.House;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库查询所有房间
 *
 * @author 郭英贤
 */
public class HandleSearchHouseListOfAll {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleSearchHouseListOfAll() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询所有房间
     *
     * @return 所有房间号、房间名
     * @see SQLException
     */
    public Map<String, String> queryVerify() {
        String sqlStr = "select * from `house`";
        Map<String, String> houseList = new HashMap<String, String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                houseList.put(rs.getString("id"), rs.getString("name"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchHouseListOfAll:" + e);
        }
        return houseList;
    }
}
