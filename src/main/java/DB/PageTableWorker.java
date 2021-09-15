package DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static DB.DBConnection.getConnection;

public class PageTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _page(path, code, content) VALUES (?, ?, ?)";

    private static final List<String> PATH_LIST = new ArrayList<>();
    private static final List<Integer> CODE_LIST = new ArrayList<>();
    private static final List<String> CONTENT_LIST = new ArrayList<>();

    public static void addLine(String path, int code, String content) {
        PATH_LIST.add(path);
        CODE_LIST.add(code);
        CONTENT_LIST.add(content);
    }

    public static void executeMultiInsert() {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_INSERT)) {

            for (int i = 0; i < PATH_LIST.size(); i++) {
                statement.setString(1, PATH_LIST.get(i));
                statement.setInt(2, CODE_LIST.get(i));
                statement.setString(3, CONTENT_LIST.get(i));

                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}