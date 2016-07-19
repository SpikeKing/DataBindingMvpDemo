package org.wangchenlong.mvpdatabindingdemo;

import android.content.Context;
import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.source.remote.TasksRemoteDataSource;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksRepository;
import org.wangchenlong.mvpdatabindingdemo.data.source.local.TasksLocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 数据注入, 可以使用Dagger
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class Injection {
    public static TasksRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(context));
    }
}
