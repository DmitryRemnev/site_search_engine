package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DB.DBConnection.getConnection;

public class IndexTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _index(page_id, lemma_id, rating) VALUES (?, ?, ?)";
    public static final String SQL_QUERY_SELECT_UNION = "SELECT _page.id " +
            "FROM _lemma " +
            "INNER JOIN _index " +
            "ON _lemma.id = _index.lemma_id " +
            "INNER JOIN _page " +
            "ON _index.page_id = _page.id " +
            "WHERE lemma = ?";

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

    public static ResultSet getResultLemma(String lemma) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_SELECT_UNION);
            statement.setString(1, lemma);

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}