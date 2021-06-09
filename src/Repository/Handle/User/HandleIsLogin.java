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
// * 数据库查询是否登录
// *
// * @author 郭英贤
// */
//public class HandleIsLogin {
//    private final Connection con;
//    private PreparedStatement preSql;
//
//    public HandleIsLogin() {
//        con = new JDBC().getCon();
//    }
//
//    /**
//     * 向数据库查询是否已登录
//     *
//     * @param id 用户ID
//     * @return 是否已登录
//     * @see SQLException
//     */
//    public boolean queryVerify(String id) {
//        String sqlStr = "select isLogin from `user` where id=?";
//        try {
//            preSql = con.prepareStatement(sqlStr);
//            preSql.setString(1, id);
//            ResultSet rs = preSql.executeQuery();
//            if (rs.next() == true) {
//                boolean isLogin = rs.getBoolean("isLogin");
//                con.close();
//                return isLogin;
//            } else {
//                con.close();
//                return false;
//            }
//        } catch (SQLException e) {
//            System.out.println("SearchIsFriend:" + e);
//        }
//        return false;
//    }
//}
