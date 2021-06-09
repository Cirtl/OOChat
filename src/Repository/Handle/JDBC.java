package Repository.Handle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC类，操作JDBC
 *
 * @author 郭英贤
 */
public class JDBC {
    // MySQL 8.0 以上版本 - Repository.Handle.JDBC 驱动名及数据库 URL
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/oo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";
    private Connection con;

    /**
     * 构造
     */
    public JDBC() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Getter 连接
     *
     * @return 连接
     */
    public Connection getCon() {
        return con;
    }
}
