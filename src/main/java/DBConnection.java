import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static final List<String> PATH_LIST = new ArrayList<>();
    private static final List<Integer> CODE_LIST = new ArrayList<>();
    private static final List<String> CONTENT_LIST = new ArrayList<>();

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        Constants.LOCAL_HOST +
                                Constants.DB_NAME +
                                Constants.USER +
                                Constants.DB_USER +
                                Constants.PASSWORD +
                                Constants.DB_PASS +
                                Constants.TIME_ZONE +
                                Constants.USE_SSL);

                connection.createStatement().execute(Constants.DROP_TABLE);
                connection.createStatement().execute(Constants.CREATE_TABLE +
                        Constants.ID_INT +
                        Constants.PATH_TEXT +
                        Constants.CODE_INT +
                        Constants.CONTENT_MEDIUMTEXT +
                        Constants.PRIMARY_KEY);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void addLine(String path, int code, String content) {
        PATH_LIST.add(path);
        CODE_LIST.add(code);
        CONTENT_LIST.add(content);
    }

    public static void executeMultiInsert() {
        try (PreparedStatement statement = getConnection().prepareStatement(Constants.SQL_QUERY)) {

            for (int i = 0; i < PATH_LIST.size(); i++) {
                statement.setString(1, PATH_LIST.get(i));
                statement.setInt(2, CODE_LIST.get(i));
                statement.setString(3, CONTENT_LIST.get(i));

                statement.addBatch();
            }

            statement.executeBatch();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}