import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Site {

    private final String url;
    private final List<String> urls = new ArrayList<>();
    private Document document;

    public Site(String url) {
        this.url = url;
    }

    public List<String> getUrls() {
        addToDataBase();
        return urls;
    }

    private void addToDataBase() {
        try {
            Connection connect = Jsoup.connect(url)
                    .maxBodySize(0)
                    .userAgent(Constants.AGENT)
                    .referrer(Constants.REFERRER)
                    .ignoreHttpErrors(true);

            document = connect.ignoreContentType(true).get();

            DBConnection.addLine(url.replace(Constants.BASE_URL, Constants.SLASH),
                    connect.response().statusCode(),
                    document.html());

            sortElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
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