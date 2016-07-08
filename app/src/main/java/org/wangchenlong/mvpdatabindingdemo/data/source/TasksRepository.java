package org.wangchenlong.mvpdatabindingdemo.data.source;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;

/**
 * 数据层, 用于管理Task的数据
 * Created by wangchenlong on 16/7/5.
 */
public class TasksRepository implements TasksDataSource {
    private static TasksRepository sInstance = null;

    private final TasksDataSource mTasksRemoteDataSource;
    private final TasksDataSource mTasksLocalDataSource;

    private TasksRepository(
            @NonNull TasksDataSource tasksRemoteDataSource,
            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = tasksRemoteDataSource;
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(
            @NonNull TasksDataSource tasksRemoteDataSource,
            @NonNull TasksDataSource tasksLocalDataSource) {
        if (sInstance == null) {
            sInstance = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return sInstance;
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override public void saveTask(@NonNull Task task) {

    }
}
