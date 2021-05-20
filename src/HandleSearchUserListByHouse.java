import javax.swing.*;
import java.sql.*;
import java.util.Vector;

public class HandleSearchUserListByHouse {
    Connection con;
    PreparedStatement preSql;

    HandleSearchUserListByHouse() {
        con = new JDBC().getCon();
    }

    public Vector<String> queryVerify(int id) {
        String sqlStr = "select user_id from `user_house` where house_id=?";
        Vector<String> userList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, id);
            ResultSet rs = preSql.executeQuery();
            while(rs.next()){
                userList.add(rs.getString("1"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("SearchUserListByHouse:"+e);
        }
        return userList;
    }
}
