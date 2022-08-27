package day08_10_mysql.myPool;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class MysqlUtil {
    //    1.声明静态变量承接连接信息
    public static String JDBC_DRIVER;
    public static String DB_URL;
    public static String USER;
    public static String PASS;

    //    2.声明静态代码块，进行静态标量的初始化操作
    static {
        ClassLoader classLoader = MysqlUtil.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("db.properties");

        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            JDBC_DRIVER = properties.getProperty("JDBC_DRIVER");
            DB_URL = properties.getProperty("DB_URL");
            USER = properties.getProperty("USER");
            PASS = properties.getProperty("PASS");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    3.连接数据库
    public static Connection getConnect() {
        Connection conn = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //    4.处理关闭的方法
    public void relese(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {    // 完成后关闭
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    //    查
    public void qurey() {
        try {
            //        1.获取连接
            MyDataSource myDataSource = new MyDataSource();
            Connection conn = myDataSource.getConnection();
//            2.执行语句
            String sql = "select * from category;\n";

            PreparedStatement ps = conn.prepareStatement(sql);

//            3.执行sql
//            boolean execute = ps.execute();
            ResultSet rs = ps.executeQuery();
            //4.遍历结果集，展示结果
            while (rs.next()) {
                int cid = rs.getInt("cid");
                String cname = rs.getString("cname");
                System.out.println("ID：" + cid + "-----------name：" + cname);
            }
            //        4.关闭连接
            relese(rs, ps, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MysqlUtil connectMysql = new MysqlUtil();
        connectMysql.qurey();
    }

}
