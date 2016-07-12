package org.wangchenlong.mvpdatabindingdemo.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

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
        // TasksRepository使用
    }
}
