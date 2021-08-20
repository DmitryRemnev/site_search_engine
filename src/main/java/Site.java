import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Site {
    public static final String AGENT = "SearchBot";
    public static final String REFERRER = "http://www.google.com";
    public static final String CSS_QUERY = "a[href]";
    public static final String ATTRIBUTE_KEY = "abs:href";

    private final String url;

    public Site(String url) {
        this.url = url;
    }

    public List<String> getUrls() {
        List<String> urls = new ArrayList<>();

        try {
            Connection connect = Jsoup.connect(url)
                    .maxBodySize(0)
                    .userAgent(AGENT)
                    .referrer(REFERRER)
                    .ignoreHttpErrors(true);

            Document document = connect.ignoreContentType(true).get();

            Elements elements = document.select(CSS_QUERY);
            elements.forEach(element -> {
                String link = element.attr(ATTRIBUTE_KEY);

                if (link.contains(url)) {
                    urls.add(link);

                    try {
                        DBConnection.addLine(link, connect.response().statusCode(), document.html());
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return urls;
    }
}