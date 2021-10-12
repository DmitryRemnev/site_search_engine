package main;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<SiteRecursiveAction> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public void addTask(SiteRecursiveAction task) {
        taskList.add(task);
    }

    public void cancelTask(SiteRecursiveAction cancelTask) {
        for (SiteRecursiveAction task : taskList) {
            if (task == cancelTask) {
                task.cancel(true);
            }
        }
    }

    public void cancelAllTasks(SiteRecursiveAction cancelTask) {
        for (SiteRecursiveAction task : taskList) {
            if (task != cancelTask) {
                task.cancel(true);
            }
        }
    }
}