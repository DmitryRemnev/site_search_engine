import java.util.*;
import java.util.concurrent.RecursiveAction;

public class SiteRecursiveAction extends RecursiveAction {

    public static final String REG_TYPES_FILES = ".*\\.(jpg|docx|doc|pdf|png|zip)";
    private final Site site;
    private final Set<String> allUrls;

    public SiteRecursiveAction(Site site) {
        this.site = site;
        allUrls = Collections.synchronizedSet(new HashSet<>());
    }

    public SiteRecursiveAction(Site site, Set<String> allUrls) {
        this.site = site;
        this.allUrls = allUrls;
    }

    @Override
    protected void compute() {
        List<SiteRecursiveAction> taskList = new ArrayList<>();

        for (String link : site.getUrls()) {

            if (checkLink(link, allUrls)) {
                continue;
            }
            allUrls.add(link);
            SiteRecursiveAction task = new SiteRecursiveAction(new Site(link), allUrls);
            task.fork();
            taskList.add(task);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SiteRecursiveAction task : taskList) {
            task.join();
        }
    }

    private boolean checkLink(String link, Set<String> allUrls) {
        return allUrls.contains(link) ||
                link.contains("#") ||
                link.matches(REG_TYPES_FILES);
    }
}