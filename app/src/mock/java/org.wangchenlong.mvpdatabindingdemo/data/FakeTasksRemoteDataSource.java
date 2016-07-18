package org.wangchenlong.mvpdatabindingdemo.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fake的远程数据源
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class FakeTasksRemoteDataSource implements TasksDataSource {
    private static FakeTasksRemoteDataSource sInstance; // 单例

    // 任务服务数据
    private static final Map<String, Task> TASKS_SERVICE_DATA = new LinkedHashMap<>();

    private FakeTasksRemoteDataSource() {
    }

    public static FakeTasksRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new FakeTasksRemoteDataSource();
        }
        return sInstance;
    }

    @Override public void saveTask(@NonNull Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        Task task = TASKS_SERVICE_DATA.get(taskId);
        callback.onTaskLoaded(task);
    }

    @Override public void getTasks(@NonNull LoadTasksCallback callback) {
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
    }

    @Override public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override public void refreshTasks() {
        // 供{@link TasksRepository}使用
    }

    /**
     * 完成任务
     *
     * @param task 任务
     */
    @Override public void completeTask(@NonNull Task task) {
        Task completedTask = new Task(
                task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override public void completeTask(@NonNull String taskId) {
        // 供{@link TasksRepository}使用
    }

    /**
     * 激活任务
     *
     * @param task 任务
     */
    @Override public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(
                task.getTitle(), task.getDescription(), task.getId(), false);
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override public void activateTask(@NonNull String taskId) {
        // 供{@link TasksRepository}使用
    }

    @Override public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }

    @Override public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }
}
