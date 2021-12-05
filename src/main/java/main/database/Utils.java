package main.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.database.DBConnection.getConnection;

public class Utils {

    public static ResultSet getResultSet(String query) {
        try {
            Statement statement = getConnection().createStatement();
            if (statement.execute(query)) {
                try {
                    return statement.getResultSet();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet getResultCount(String query, int id) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}