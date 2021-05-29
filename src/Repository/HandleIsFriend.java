package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleIsFriend {
    private Connection con;
    private PreparedStatement preSql;

    HandleIsFriend() {
        con = new JDBC().getCon();
    }

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
