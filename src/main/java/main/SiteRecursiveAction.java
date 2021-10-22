package main;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class SiteRecursiveAction extends RecursiveAction {
    private final SiteHandler siteHandler;
    private final Set<String> allUrls;
    private final TaskManager taskManager = new TaskManager();
    public AtomicBoolean isCancel = new AtomicBoolean(false);

    public SiteRecursiveAction(SiteHandler siteHandler) {
        this.siteHandler = siteHandler;
        allUrls = Collections.synchronizedSet(new HashSet<>());
    }

    public SiteRecursiveAction(SiteHandler siteHandler, Set<String> allUrls) {
        this.siteHandler = siteHandler;
        this.allUrls = allUrls;
    }

    @Override
    protected void compute() {
        List<SiteRecursiveAction> taskList = new ArrayList<>();

        for (String link : siteHandler.getUrls()) {

            synchronized (allUrls) {
                if (isNotAdd(link, allUrls)) {
                    continue;
                }
                allUrls.add(link);
            }
            SiteRecursiveAction task = new SiteRecursiveAction(new SiteHandler(link), allUrls);
            task.fork();
            taskList.add(task);
            taskManager.addTask(task);

            if (isCancel.get()) {
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