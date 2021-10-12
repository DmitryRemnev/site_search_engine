package main.db;

import java.sql.*;

public class DBConnection {
    public static final String DB_NAME = "search_engine";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "Tgb10Ujm73";
    public static final String LOCAL_HOST = "jdbc:mysql://localhost:3306/";
    public static final String USER = "?user=";
    public static final String PASSWORD = "&password=";
    public static final String TIME_ZONE = "&serverTimezone=UTC";
    public static final String USE_SSL = "&useSSL=false";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        LOCAL_HOST +
                                DB_NAME +
                                USER +
                                DB_USER +
                                PASSWORD +
                                DB_PASS +
                                TIME_ZONE +
                                USE_SSL);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}