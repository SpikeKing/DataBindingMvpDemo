package org.wangchenlong.mvpdatabindingdemo.data;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

/**
 * Fake的远程数据源
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class FakeTasksRemoteDataSource implements TasksDataSource {
    private static FakeTasksRemoteDataSource sInstance;

    private FakeTasksRemoteDataSource() {
    }

    public static FakeTasksRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new FakeTasksRemoteDataSource();
        }
        return sInstance;
    }

    @Override public void saveTask(@NonNull Task task) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }
}
