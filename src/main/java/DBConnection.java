import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_NAME = "search_engine";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Tgb10Ujm73";
    private static final StringBuilder insertQuery = new StringBuilder();

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + DB_NAME +
                                "?user=" + DB_USER + "&password=" + DB_PASS + "&serverTimezone=UTC");

                connection.createStatement().execute("DROP TABLE IF EXISTS page");
                connection.createStatement().execute("CREATE TABLE page(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "path TEXT NOT NULL, " +
                        "code INT NOT NULL, " +
                        "content MEDIUMTEXT NOT NULL, " +
                        "PRIMARY KEY(id))");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void addLine(String path, int code, String content) throws SQLException {
        String sqlQuery = "INSERT INTO page(path, code, content) VALUES (?, ?, ?)";

        PreparedStatement statement = getConnection().prepareStatement(sqlQuery);
        statement.setString(1, path);
        statement.setInt(2, code);
        statement.setString(3, content);

        statement.execute();
    }

    public static void executeMultiInsert() throws SQLException {
        String sqlQuery = "INSERT INTO page(path, code, content) VALUES" +
                insertQuery;

        DBConnection.getConnection().createStatement().execute(sqlQuery);
    }
}