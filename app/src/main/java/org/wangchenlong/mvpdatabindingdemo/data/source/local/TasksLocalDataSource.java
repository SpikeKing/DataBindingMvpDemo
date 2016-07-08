package org.wangchenlong.mvpdatabindingdemo.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

/**
 * Tasks的本地数据源
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource sInstance; // 单例
    private final Context mContext; // 上下文

    private TasksLocalDataSource(Context context) {
        mContext = context;
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new TasksLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override public void saveTask(@NonNull Task task) {

    }
}
