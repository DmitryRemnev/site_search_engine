package main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static main.db.DBConnection.getConnection;

public class LemmaTableWorker {
    public static final String SQL_QUERY_INSERT = "INSERT INTO _lemma(lemma, frequency, site_id) VALUES (?, ?, ?)";
    public static final String SQL_QUERY_SELECT = "SELECT * FROM _lemma WHERE lemma = ?";
    public static final String SQL_QUERY_UPDATE = "UPDATE _lemma SET frequency = frequency + 1 WHERE lemma = ?";

    public static void executeInsert(String lemma, int frequency, int siteId) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_INSERT)) {
            statement.setString(1, lemma);
            statement.setInt(2, frequency);
            statement.setInt(3, siteId);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getResultLemma(String lemma) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_SELECT);
            statement.setString(1, lemma);

            return statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void executeUpdate(String lemma) {
        try (PreparedStatement statement = getConnection().prepareStatement(SQL_QUERY_UPDATE)) {
            statement.setString(1, lemma);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}