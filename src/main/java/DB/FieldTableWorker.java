package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static DB.DBConnection.getConnection;

public class FieldTableWorker {
    public static final String SQL_QUERY_SELECT = "SELECT * FROM _field;";

    public static ResultSet getFields() {
        try {
            Statement statement = getConnection().createStatement();
            if (statement.execute(SQL_QUERY_SELECT)) {
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