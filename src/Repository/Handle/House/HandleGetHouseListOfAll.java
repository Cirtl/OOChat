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
public class HandleGetHouseListOfAll {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleGetHouseListOfAll() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询所有房间
     *
     * @return 所有房间号、房间信息【0：name；1：pass；2：host】
     * @see SQLException
     */
    public Map<Integer, String[]> queryVerify() {
        String sqlStr = "select * from `house`";
        Map<Integer, String[]> houseList = new HashMap<Integer, String[]>();
        try {
            preSql = con.prepareStatement(sqlStr);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                String[] info = new String[3];
                info[0] = rs.getString("name");
                info[1] = rs.getString("password");
                info[2] = rs.getString("host");
                houseList.put(rs.getInt("id"), info);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchHouseListOfAll:" + e);
        }
        return houseList;
    }
}
