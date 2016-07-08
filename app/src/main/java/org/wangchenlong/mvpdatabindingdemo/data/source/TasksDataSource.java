package org.wangchenlong.mvpdatabindingdemo.data.source;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;

/**
 * Model层的接口, 用于管理数据
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public interface TasksDataSource {
    interface GetTaskCallback {
        // 数据加载完毕
        void onTaskLoaded(Task task);

        // 数据无法加载
        void onDataNotAvailable();
    }

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
}
