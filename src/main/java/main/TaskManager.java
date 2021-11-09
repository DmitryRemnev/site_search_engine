package main;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<PageRecursiveAction> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public void addTask(PageRecursiveAction task) {
        taskList.add(task);
    }

    public void cancelTask(PageRecursiveAction cancelTask) {
        for (PageRecursiveAction task : taskList) {
            if (task == cancelTask) {
                task.cancel(true);
            }
        }
    }

    public void cancelAllTasks(PageRecursiveAction cancelTask) {
        for (PageRecursiveAction task : taskList) {
            if (task != cancelTask) {
                task.cancel(true);
            }
        }
    }
}