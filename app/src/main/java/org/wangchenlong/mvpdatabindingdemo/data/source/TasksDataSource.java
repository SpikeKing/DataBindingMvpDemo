package org.wangchenlong.mvpdatabindingdemo.data.source;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;

import java.util.List;

/**
 * Model层的接口, 用于管理数据
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public interface TasksDataSource {

    // 获取全部Tasks的回调
    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    // 获取单个Task的回调
    interface GetTaskCallback {
        // 数据加载完毕
        void onTaskLoaded(Task task);

        // 数据无法加载
        void onDataNotAvailable();
    }

    /**
     * 获取全部的Tasks
     *
     * @param callback 回调
     */
    void getTasks(@NonNull LoadTasksCallback callback);

    /**
     * 获取Task
     *
     * @param taskId   Id
     * @param callback 回到
     */
    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    /**
     * 存储任务
     *
     * @param task 任务
     */
    void saveTask(@NonNull Task task);

    /**
     * 删除所有任务
     */
    void deleteAllTasks();

    /**
     * 刷新任务, 刷新后数据被污染, 需要重新加载
     */
    void refreshTasks();
}
