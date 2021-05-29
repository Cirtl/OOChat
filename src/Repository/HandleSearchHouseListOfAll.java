package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HandleSearchHouseListOfAll {
    private Connection con;
    private PreparedStatement preSql;

    HandleSearchHouseListOfAll() {
        con = new JDBC().getCon();
    }

    public Map<String,String> queryVerify() {
        String sqlStr = "select * from `house`";
        Map<String,String> houseList = new HashMap<String,String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            ResultSet rs = preSql.executeQuery();
            while(rs.next()){
                houseList.put(rs.getString("id"),rs.getString("name"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchHouseListOfAll:"+e);
        }
        return houseList;
    }
}
