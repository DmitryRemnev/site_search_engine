package main.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static main.db.DBConnection.getConnection;

public class SiteTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _site(status, status_time, url, name)" +
            "VALUES (?, ?, ?, ?)";
    public static final String SQL_QUERY_UPDATE = "UPDATE _site SET status = ? WHERE name = ?";
    public static final String SQL_QUERY_FAILED = "UPDATE _site SET status = ?, last_error = ? WHERE name = ?";

    public static void executeInsert(String status, String statusTime, String url, String name) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_INSERT);
            statement.setString(1, status);
            statement.setString(2, statusTime);
            statement.setString(3, url);
            statement.setString(4, name);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String status, String name) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_UPDATE);
            statement.setString(1, status);
            statement.setString(2, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void failedUpdate(String status, String message, String name) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_FAILED);
            statement.setString(1, status);
            statement.setString(2, message);
            statement.setString(3, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}