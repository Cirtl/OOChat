package Repository.Handle.User;

import Repository.Handle.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
/**
 * 数据库查询用户好友列表
 *
 * @author 郭英贤
 */
public class HandleGetFriendList {
    private final Connection con;
    private PreparedStatement preSql;

    public HandleGetFriendList() {
        con = new JDBC().getCon();
    }

    /**
     * 向数据库查询用户好友列表
     *ID
     * @param id 用户
     * @return 用户好友ID列表
     * @see SQLException
     */
    public Vector<String> queryVerify(String id) {
        String sqlStr = "select id_1 from `friend` where id_2=?";
        Vector<String> userList = new Vector<String>();
        try {
            preSql = con.prepareStatement(sqlStr);
            preSql.setString(1, id);
            ResultSet rs = preSql.executeQuery();
            while (rs.next()) {
                userList.add(rs.getString("user_id"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("GetFriendList:" + e);
        }
        return userList;
    }


}
