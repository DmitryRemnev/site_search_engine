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
    public static final int FIVE_SECOND = 5000;
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
                    .timeout(FIVE_SECOND)
                    .ignoreHttpErrors(true);

            Document document = connect.ignoreContentType(true).get();

            Elements elements = document.select(CSS_QUERY);
            elements.forEach(element -> {
                String s = element.attr(ATTRIBUTE_KEY);
                if (!linksSeen.contains(s) && s.startsWith(this.url) && !s.contains("#")) {
                    linksSeen.add(s);
                    links.add(s);
                }
            });

            System.out.println("/" + path);
            System.out.println(connect.response().statusCode());
            System.out.println(document.html());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void compute() {
        for (String link : links) {
            String path = link.replace(url, "");
            new SiteRecursiveAction(url, path).invoke();
        }
    }
}