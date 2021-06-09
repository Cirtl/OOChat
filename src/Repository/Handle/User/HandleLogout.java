//package Repository.Handle.User;
//
//import Repository.Handle.JDBC;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * 数据库记录用户注销
// *
// * @author 郭英贤
// */
//public class HandleLogout {
//    ResultSet rs;
//    private final Connection con;
//    private PreparedStatement preSql;
//
//    public HandleLogout() {
//        con = new JDBC().getCon();
//    }
//
//    /**
//     * 向数据库更新登录状态为未登录
//     *
//     * @param id 用户ID
//     * @param pass 密码
//     * @return 更新状态成功与否
//     * @see SQLException
//     */
//    public boolean queryVerify(String id, String pass) {
//        String sqlStr = "update `user` set isLogin=? where id=? and password=?";
//        try {
//            preSql = con.prepareStatement(sqlStr);
//            preSql.setBoolean(1, false);
//            preSql.setString(2, id);
//            preSql.setString(3, pass);
//            int ok = preSql.executeUpdate();
//            con.close();
//            if (ok != 0) {
//                // JOptionPane.showMessageDialog(null, "注销成功", "恭喜", JOptionPane.WARNING_MESSAGE);
//                return true;
//            } else {
//                // JOptionPane.showMessageDialog(null, "您已注销", "警告", JOptionPane.WARNING_MESSAGE);
//            }
//        } catch (SQLException e) {
//            // JOptionPane.showMessageDialog(null, "注销失败", "警告", JOptionPane.WARNING_MESSAGE);
//            System.out.println("Logout:" + e);
//        }
//        return false;
//    }
//}
