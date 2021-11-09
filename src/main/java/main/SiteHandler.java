package main;

import main.db.SiteTableWorker;
import main.db.Utils;
import main.entities.Site;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SiteHandler extends Thread {
    private final PageRecursiveAction pageRecursiveAction;
    private final Site site;

    public SiteHandler(PageRecursiveAction pageRecursiveAction, Site site) {
        this.pageRecursiveAction = pageRecursiveAction;
        this.site = site;
    }

    public void run() {
        addSiteRecord();
        //new ForkJoinPool().invoke(pageRecursiveAction);
        updateSiteRecord();
    }

    private void addSiteRecord() {
        SiteTableWorker.executeInsert(Status.INDEXING.getName(), getDate(), site.getUrl(), site.getName());
    }

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }

    private void updateSiteRecord() {
        SiteTableWorker.executeUpdate(Status.INDEXED.getName(), site.getName());
    }
}