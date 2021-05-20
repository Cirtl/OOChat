package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HandleSendMessage {
    Connection con;
    PreparedStatement preSql;

    HandleSendMessage() {
        con = new JDBC().getCon();
    }

    public boolean queryVerify(String uid, int hid, String message) {
        String sqlStrEnter = "insert into `user_house_message` values(?,?,?,default )";
        try {
            preSql = con.prepareStatement(sqlStrEnter);
            preSql.setString(1, uid);
            preSql.setInt(2, hid);
            preSql.setString(3, message);
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "发送消息成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "发送消息失败", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "发送消息失败", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("SendMessage:" + e);
        }
        return false;
    }
}
