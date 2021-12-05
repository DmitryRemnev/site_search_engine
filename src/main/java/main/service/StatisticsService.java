package main.service;

import main.constants.Constants;
import main.enums.Status;
import main.database.Utils;
import main.entities.statistics.*;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsService {

    public StatisticsResponse getStatistics() {
        var response = new StatisticsResponse();

        Total total = new Total();
        total.setSites(getCount(Constants.QUERY_SITES_COUNT, Constants.COLUMN_LABEL_SITES));
        total.setPages(getCount(Constants.QUERY_PAGES_COUNT, Constants.COLUMN_LABEL_PAGES));
        total.setLemmas(getCount(Constants.QUERY_LEMMAS_COUNT, Constants.COLUMN_LABEL_LEMMAS));
        total.setIndexing(isIndexing());

        Statistics statistics = new Statistics();
        statistics.setTotal(total);
        statistics.setDetailed(getListDetailed());

        response.setResult(true);
        response.setStatistics(statistics);

        return response;
    }

    private int getCount(String query, String columnLabel) {
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

    private boolean isIndexing() {
        ResultSet resultSet = Utils.getResultSet(Constants.QUERY_STATUS);

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

    private List<SiteDetailed> getListDetailed() {
        List<SiteDetailed> list = new ArrayList<>();
        ResultSet resultSet = Utils.getResultSet(Constants.QUERY_SITES_ALL);

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
                    siteDetailed.setPages(getCountDetailed(Constants.QUERY_PAGES_COUNT_DETAILED, id, Constants.COLUMN_PAGES_COUNT));
                    siteDetailed.setLemmas(getCountDetailed(Constants.QUERY_LEMMAS_COUNT_DETAILED, id, Constants.COLUMN_LEMMAS_COUNT));

                    list.add(siteDetailed);
                }

                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private int getCountDetailed(String query, int id, String columnLabel) {
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

    private long getTimeInMilliseconds(String time) {
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