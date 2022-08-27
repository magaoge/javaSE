package day08_10_mysql.myPool;

import day08_10_mysql.ConnectMysql;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

public class MyDataSource implements DataSource {

//   1 先自定义连接池
    private static LinkedList<Connection> pool = new LinkedList<Connection>();
//    2 构造方法初始化连接池
    static {
        for (int i = 0; i < 5; i++) {
            Connection connection = MysqlUtil.getConnect();
            MyConnect myConnect = new MyConnect(connection, pool);
            pool.add(myConnect);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
//        3.但是在真的获取的时候，依旧是要判断连接池是否为空，如果为空
        if (pool.size()<0){
            //则先给连接池添加连接对象，再获取
            //获取的时候，用到的是移除获取
            for (int i = 0; i < 5; i++) {
                Connection connection = MysqlUtil.getConnect();
                //   为了调用自定义的close方法，将连接对象地址，让我们自定义连接对象的地址，指向MyDataSource中的连接池和连接对象的地址
                MyConnect myConnect = new MyConnect(connection, pool);
                pool.add(myConnect);
            }
        }
        Connection conn = pool.remove(0);
        return conn;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
