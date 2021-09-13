package DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static DB.DBConnection.getConnection;

public class IndexTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _index(page_id, lemma_id, rating) VALUES (?, ?, ?)";

    public static void executeInsert(int pageId, int lemmaId, double rating) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_INSERT)) {
            statement.setInt(1, pageId);
            statement.setInt(2, lemmaId);
            statement.setDouble(3, rating);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}