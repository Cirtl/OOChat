package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HandleIsLogin {
    private Connection con;
    private PreparedStatement preSql;

    HandleIsLogin() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String id) {
        String sqlStr = "select isLogin from `user` where id=?";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            ResultSet rs = preSql.executeQuery();
            if (rs.next() == true) {
                boolean isLogin = rs.getBoolean("isLogin");
                con.close();
                return isLogin;
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
