import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandleRegister {
    Connection con;
    PreparedStatement preSql;

    HandleRegister() {
        con = new JDBC().getCon();
    }

    public boolean writeRegisterModel(User user) {
        String sqlStr = "insert into `user` values(?,?,?)";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, user.getId());
            preSql.setString(2, user.getPass());
            preSql.setString(3, null);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "注册成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "注册失败，用户名重复", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "注册失败，用户名重复", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("HandleRegister:"+e);
        }
        return false;
    }
}
