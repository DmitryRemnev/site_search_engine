package main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static main.db.DBConnection.getConnection;

public class PageTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _page(path, code, content, site_id) VALUES (?, ?, ?, ?)";
    public static final String QUERY_SELECT_PAGE = "SELECT * FROM _page WHERE site_id = ?;";
    public static final String SQL_QUERY_SELECT = "SELECT path, content FROM _page WHERE id = ?";

    public static void pageInsert(String path, int code, String content, int siteId) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_INSERT)) {
            statement.setString(1, path);
            statement.setInt(2, code);
            statement.setString(3, content);
            statement.setInt(4, siteId);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getResultSet(int siteId) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(QUERY_SELECT_PAGE);
            statement.setInt(1, siteId);

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getResultPage(int pageId) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_SELECT);
            statement.setInt(1, pageId);

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}