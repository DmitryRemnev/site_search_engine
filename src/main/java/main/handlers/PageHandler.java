package main.handlers;

import main.constants.Constants;
import main.enums.Status;
import main.database.PageTableWorker;
import main.database.SiteTableWorker;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PageHandler {
    private final String url;
    private final String siteName;
    private final String baseUrl;
    private int siteId;
    private final List<String> urls = new ArrayList<>();
    private Document document;
    private Connection connect;

    public PageHandler(String url, String siteName, String baseUrl) {
        this.url = url;
        this.siteName = siteName;
        this.baseUrl = baseUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public List<String> getUrls() {
        addToDataBase();
        return urls;
    }

    private void addToDataBase() {
        try {
            connect = Jsoup.connect(url)
                    .maxBodySize(0)
                    .userAgent(Constants.AGENT)
                    .referrer(Constants.REFERRER)
                    .ignoreHttpErrors(true);

            document = connect.ignoreContentType(true).get();

            addLine();
            sortElement();

        } catch (Exception e) {
            failedSiteRecord(e.getMessage());
            addLine();
            e.printStackTrace();
        }
    }

    private void addLine() {
        PageTableWorker.pageInsert(url.replace(baseUrl, Constants.SLASH),
                connect.response().statusCode(),
                document.html(),
                getSiteId());
    }

    private void failedSiteRecord(String error) {
        SiteTableWorker.failedUpdate(Status.FAILED.getName(), error, siteName);
    }

    private void sortElement() {
        Elements elements = document.select(Constants.CSS_QUERY);
        elements.forEach(element -> {
            String link = element.attr(Constants.ATTRIBUTE_KEY);

            if (isAdd(link)) {
                urls.add(link);
            }
        });
    }

    private boolean isAdd(String link) {
        return link.contains(url) && !link.equals(url);
    }
}