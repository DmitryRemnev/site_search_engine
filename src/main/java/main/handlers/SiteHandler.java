package main.handlers;

import main.constants.Constants;
import main.enums.Status;
import main.database.SiteTableWorker;
import main.entities.Site;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

public class SiteHandler extends Thread {
    private final PageRecursiveHandler pageRecursiveHandler;
    private final Site site;

    public SiteHandler(PageRecursiveHandler pageRecursiveHandler, Site site) {
        this.pageRecursiveHandler = pageRecursiveHandler;
        this.site = site;
    }

    public void run() {
        long start = System.currentTimeMillis();
        addSiteRecord();
        int siteId = getSiteId();
        pageRecursiveHandler.getPageHandler().setSiteId(siteId);
        new ForkJoinPool().invoke(pageRecursiveHandler);
        new ContentHandler(siteId).toHandle();
        updateSiteRecord();
        System.out.println(siteId + " " + (System.currentTimeMillis() - start));
    }

    private void addSiteRecord() {
        SiteTableWorker.executeInsert(Status.INDEXING.getName(), getDate(), site.getUrl(), site.getName());
    }

    private String getDate() {
        var format = new SimpleDateFormat(Constants.DATE_FORMAT);

        return format.format(new Date());
    }

    private void updateSiteRecord() {
        SiteTableWorker.executeUpdate(Status.INDEXED.getName(), site.getName());
    }

    private int getSiteId() {
        ResultSet siteResultSet = SiteTableWorker.getSiteId(site.getName());

        if (siteResultSet != null) {
            try {
                while (siteResultSet.next()) {
                    return siteResultSet.getInt(Constants.COLUMN_ID);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}