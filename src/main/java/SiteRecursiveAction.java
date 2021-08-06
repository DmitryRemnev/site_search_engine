import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

public class SiteRecursiveAction extends RecursiveAction {

    public static final String AGENT = "SearchBot";
    public static final String REFERRER = "http://www.google.com";
    public static final String CSS_QUERY = "a[href]";
    public static final String ATTRIBUTE_KEY = "abs:href";
    public static final String REG_TYPES_FILES = ".*\\.(jpg|docx|doc|pdf|png|zip)";
    private static final Set<String> linksSeen = ConcurrentHashMap.newKeySet();
    private final String url;
    private final List<String> links;

    public SiteRecursiveAction(String url, String path) {
        this.url = url;
        links = new ArrayList<>();

        try {
            Connection connect = Jsoup.connect(url + path)
                    .maxBodySize(0)
                    .userAgent(AGENT)
                    .referrer(REFERRER)
                    .ignoreHttpErrors(true);

            Document document = connect.ignoreContentType(true).get();

            Elements elements = document.select(CSS_QUERY);
            elements.forEach(element -> {
                String link = element.attr(ATTRIBUTE_KEY);
                if (checkLink(link)) {
                    linksSeen.add(link);
                    links.add(link);
                }
            });

            DBConnection.addLine("/" + path, connect.response().statusCode(), document.html());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void compute() {
        for (String link : links) {
            String path = link.replace(url, "");
            new SiteRecursiveAction(url, path).invoke();

            /*try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    private boolean checkLink(String link) {
        return !linksSeen.contains(link) &&
                link.startsWith(this.url) &&
                !link.contains("#") &&
                !link.matches(REG_TYPES_FILES);
    }
}