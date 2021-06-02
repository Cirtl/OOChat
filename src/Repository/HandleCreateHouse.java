package Repository;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 向数据库添加新房间
 *
 * @author 郭英贤
 */
public class HandleCreateHouse {
    private final Connection con;
    private PreparedStatement preSql;

    HandleCreateHouse() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库添加房间
     *
     * @param register 房间实例
     * @return 房间号，失败返回-1
     * @see SQLException
     */
    public int writeRegisterModel(House register) {
        String sqlStr = "insert into `house` values(?,?,?,?,?)";
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setInt(1, register.getId());
            preSql.setString(2, register.getName());
            preSql.setString(3, register.getPass());
            preSql.setString(4, register.getIp());
            preSql.setString(5, register.getHost_id());
            int ok = preSql.executeUpdate();
            con.close();
            if (ok != 0) {
                JOptionPane.showMessageDialog(null, "房间申请成功", "恭喜", JOptionPane.WARNING_MESSAGE);
                return register.getId();
            } else {
                JOptionPane.showMessageDialog(null, "房间申请失败，请重试", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "房间申请失败，请重试", "警告", JOptionPane.WARNING_MESSAGE);
            System.out.println("CreateHouse:" + e);
        }
        return -1;
    }
}
