package main;

import main.db.Utils;
import main.entities.statistics.Detailed;
import main.entities.statistics.SiteDetailed;
import main.entities.statistics.Statistics;
import main.entities.statistics.Total;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsHandler {
    public static final String QUERY_SITES_COUNT = "SELECT count(*) AS sitesCount FROM _site";
    public static final String QUERY_PAGES_COUNT = "SELECT count(*) AS pagesCount FROM _page";
    public static final String QUERY_LEMMAS_COUNT = "SELECT count(*) AS lemmasCount FROM _lemma";
    public static final String QUERY_STATUS = "SELECT status FROM _site";
    public static final String COLUMN_LABEL_SITES = "sitesCount";
    public static final String COLUMN_LABEL_PAGES = "pagesCount";
    public static final String COLUMN_LABEL_LEMMAS = "lemmasCount";
    public static final String QUERY_SITES_ALL = "SELECT * FROM _site";
    public static final String QUERY_PAGES_COUNT_DETAILED = "SELECT count(*) AS pagesCountDetailed" +
            " FROM _page WHERE site_id = ?";
    public static final String QUERY_LEMMAS_COUNT_DETAILED = "SELECT count(*) AS lemmasCountDetailed" +
            " FROM _lemma WHERE site_id = ?";
    public static final String COLUMN_PAGES_COUNT = "pagesCountDetailed";
    public static final String COLUMN_LEMMAS_COUNT = "lemmasCountDetailed";

    public static Statistics getStatistics() {
        Total total = new Total();
        total.setSites(getCount(QUERY_SITES_COUNT, COLUMN_LABEL_SITES));
        total.setPages(getCount(QUERY_PAGES_COUNT, COLUMN_LABEL_PAGES));
        total.setLemmas(getCount(QUERY_LEMMAS_COUNT, COLUMN_LABEL_LEMMAS));
        total.setIndexing(isIndexing());

        Detailed detailed = new Detailed();
        detailed.setListDetailed(getListDetailed());

        Statistics statistics = new Statistics();
        statistics.setTotal(total);
        statistics.setDetailed(detailed);

        return statistics;
    }

    private static int getCount(String query, String columnLabel) {
        ResultSet resultSet = Utils.getResultSet(query);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    return resultSet.getInt(columnLabel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    private static boolean isIndexing() {
        ResultSet resultSet = Utils.getResultSet(QUERY_STATUS);

        if (resultSet != null) {
            try {
                boolean isIndexing = false;
                while (resultSet.next()) {
                    isIndexing = resultSet.getString(Constants.COLUMN_STATUS).equals(Status.INDEXING.getName());
                }
                return isIndexing;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private static List<SiteDetailed> getListDetailed() {
        List<SiteDetailed> list = new ArrayList<>();
        ResultSet resultSet = Utils.getResultSet(QUERY_SITES_ALL);

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    var siteDetailed = new SiteDetailed();
                    siteDetailed.setUrl(resultSet.getString(Constants.COLUMN_URL));
                    siteDetailed.setName(resultSet.getString(Constants.COLUMN_NAME));
                    siteDetailed.setStatus(resultSet.getString(Constants.COLUMN_STATUS));
                    siteDetailed.setStatusTime(getTimeInMilliseconds(resultSet.getString(Constants.COLUMN_STATUS_TIME)));
                    siteDetailed.setError(resultSet.getString(Constants.COLUMN_LAST_ERROR));

                    int id = resultSet.getInt(Constants.COLUMN_ID);
                    siteDetailed.setPages(getCountDetailed(QUERY_PAGES_COUNT_DETAILED, id, COLUMN_PAGES_COUNT));
                    siteDetailed.setLemmas(getCountDetailed(QUERY_LEMMAS_COUNT_DETAILED, id, COLUMN_LEMMAS_COUNT));

                    list.add(siteDetailed);
                }

                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static int getCountDetailed(String query, int id, String columnLabel) {
        ResultSet countPages = Utils.getResultCount(query, id);

        if (countPages != null) {
            try {
                while (countPages.next()) {
                    return countPages.getInt(columnLabel);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    private static long getTimeInMilliseconds(String time) {
        var format = new SimpleDateFormat(Constants.DATE_FORMAT);

        try {
            Date date = format.parse(time);
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}