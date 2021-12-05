package main.utilities;

import main.handlers.PageRecursiveHandler;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<PageRecursiveHandler> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public void addTask(PageRecursiveHandler task) {
        taskList.add(task);
    }

    public void cancelTask(PageRecursiveHandler cancelTask) {
        for (PageRecursiveHandler task : taskList) {
            if (task == cancelTask) {
                task.cancel(true);
            }
        }
    }

    public void cancelAllTasks(PageRecursiveHandler cancelTask) {
        for (PageRecursiveHandler task : taskList) {
            if (task != cancelTask) {
                task.cancel(true);
            }
        }
    }
}