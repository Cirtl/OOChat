import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class HandleSearchHouseListByUser {
    Connection con;
    PreparedStatement preSql;

    HandleSearchHouseListByUser() {
        con = new JDBC().getCon();
    }

    public Vector<String> queryVerify(String id) {
        String sqlStr = "select house_id from `user_house` where user_id=?";
        Vector<String> houseList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            ResultSet rs = preSql.executeQuery();
            while(rs.next()){
                houseList.add(rs.getString("1"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchHouseListByUser:"+e);
        }
        return houseList;
    }
}
