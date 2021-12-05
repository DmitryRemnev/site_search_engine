package main.handlers;

import main.utilities.TaskManager;
import main.constants.Constants;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageRecursiveHandler extends RecursiveAction {

    private final PageHandler pageHandler;
    private final Set<String> allUrls;
    private final TaskManager taskManager = new TaskManager();
    public AtomicBoolean isCancel = new AtomicBoolean(false);

    public PageRecursiveHandler(PageHandler pageHandler) {
        this.pageHandler = pageHandler;
        allUrls = Collections.synchronizedSet(new HashSet<>());
    }

    public PageRecursiveHandler(PageHandler pageHandler, Set<String> allUrls) {
        this.pageHandler = pageHandler;
        this.allUrls = allUrls;
    }

    public PageHandler getPageHandler() {
        return pageHandler;
    }

    @Override
    protected void compute() {
        List<PageRecursiveHandler> taskList = new ArrayList<>();

        for (String link : pageHandler.getUrls()) {

            synchronized (allUrls) {
                if (isNotAdd(link, allUrls)) {
                    continue;
                }
                allUrls.add(link);
            }

            var page = new PageHandler(link, pageHandler.getSiteName(), pageHandler.getBaseUrl());
            page.setSiteId(pageHandler.getSiteId());
            var task = new PageRecursiveHandler(page, allUrls);

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

        for (PageRecursiveHandler task : taskList) {
            task.join();
        }
    }

    private boolean isNotAdd(String link, Set<String> allUrls) {
        return allUrls.contains(link) || link.matches(Constants.REG_TYPES_FILES);
    }
}