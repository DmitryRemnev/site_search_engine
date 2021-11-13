package main.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.db.DBConnection.getConnection;

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
}