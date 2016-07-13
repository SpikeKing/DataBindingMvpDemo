package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.bases.BasePresenter;
import org.wangchenlong.mvpdatabindingdemo.bases.BaseView;
import org.wangchenlong.mvpdatabindingdemo.data.Task;

import java.util.List;

/**
 * 任务接口, View与Presenter的接口
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public interface TasksContract {
    interface View extends BaseView<Presenter> {
        // 显示添加新任务的方法
        void showAddTask();

        // 显示Task的列表
        void showTasks(List<Task> tasks);

        // 设置加载Indicator
        void setLoadingIndicator(boolean active);

        // 加载页面, 用于控制网络状态, 防止在网络返回时, 页面已经卸载
        boolean isActive();

        // 显示加载错误
        void showLoadingTasksError();
    }

    interface Presenter extends BasePresenter {
        // 绑定View的返回值, requestCode和resultCode
        void result(int requestCode, int resultCode);

        // 设置当前的过滤类型, Menu处设置
        void setFiltering(TasksFilterType requestType);

        // 返回当前的过滤类型
        TasksFilterType getFiltering();

        // 加载任务, 强制或非强制
        void loadTasks(boolean forceUpdate);

        // 添加新的Task
        void addNewTask();

        // 完成任务
        void completeTask(@NonNull Task completedTask);

        // 激活任务
        void activateTask(@NonNull Task activeTask);

        // 完成任务详情
        void openTaskDetails(@NonNull Task requestedTask);
    }
}
