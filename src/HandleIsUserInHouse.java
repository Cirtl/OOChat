import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleIsUserInHouse {
    Connection con;
    PreparedStatement preSql;

    HandleIsUserInHouse() {
        con = new JDBC().getCon();
    }

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
