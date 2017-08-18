package com.coursetable;

import java.sql.*;

public class DataBase {
    private static String url = "jdbc:mysql://localhost:3306/chongyou?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    private static String username = "root";
    private static String password = "jxx970224";

    private static Connection connection;

    /**
     * 加载驱动
     */
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例连接
     * @return
     */
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (Object.class) {
                if (connection == null)
                    connection = createConnection();
            }
        }
        return connection;
    }

    /**
     * 关闭连接
     * @param statement
     */
    public static void close(Statement statement) {
        close(null, statement);
    }

    public static void close(ResultSet rs, Statement statement) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DataBase(){}

    /**
     * 创建连接
     * @return
     */
    private static Connection createConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
