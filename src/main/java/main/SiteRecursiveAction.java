package main;

import java.util.*;
import java.util.concurrent.RecursiveAction;

public class SiteRecursiveAction extends RecursiveAction {
    private final Site site;
    private final Set<String> allUrls;
    private final TaskManager taskManager = new TaskManager();
    public static boolean isCancel = false;

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

            synchronized (allUrls) {
                if (isNotAdd(link, allUrls)) {
                    continue;
                }
                allUrls.add(link);
            }
            SiteRecursiveAction task = new SiteRecursiveAction(new Site(link), allUrls);
            task.fork();
            taskList.add(task);
            taskManager.addTask(task);

            if (isCancel) {
                taskManager.cancelTask(this);
                taskManager.cancelAllTasks(this);
            }

            try {
                Thread.sleep(Constants.THREAD_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SiteRecursiveAction task : taskList) {
            task.join();
        }
    }

    private boolean isNotAdd(String link, Set<String> allUrls) {
        return allUrls.contains(link) || link.matches(Constants.REG_TYPES_FILES);
    }
}