package main;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageRecursiveAction extends RecursiveAction {
    private final PageHandler pageHandler;
    private final Set<String> allUrls;
    private final TaskManager taskManager = new TaskManager();
    public AtomicBoolean isCancel = new AtomicBoolean(false);

    public PageRecursiveAction(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
        allUrls = Collections.synchronizedSet(new HashSet<>());
    }

    public PageRecursiveAction(PageHandler pageHandler, Set<String> allUrls) {
        this.pageHandler = pageHandler;
        this.allUrls = allUrls;
    }

    public PageHandler getPageHandler() {
        return pageHandler;
    }

    @Override
    protected void compute() {
        List<PageRecursiveAction> taskList = new ArrayList<>();

        for (String link : pageHandler.getUrls()) {

            synchronized (allUrls) {
                if (isNotAdd(link, allUrls)) {
                    continue;
                }
                allUrls.add(link);
            }

            var page = new PageHandler(link, pageHandler.getSiteName(), pageHandler.getBaseUrl());
            page.setSiteId(pageHandler.getSiteId());
            var task = new PageRecursiveAction(page, allUrls);

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

        for (PageRecursiveAction task : taskList) {
            task.join();
        }
    }

    private boolean isNotAdd(String link, Set<String> allUrls) {
        return allUrls.contains(link) || link.matches(Constants.REG_TYPES_FILES);
    }
}