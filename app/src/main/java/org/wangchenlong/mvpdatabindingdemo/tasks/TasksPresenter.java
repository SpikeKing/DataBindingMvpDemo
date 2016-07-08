package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 表示层
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public class TasksPresenter implements TasksContract.Presenter {
    private final TasksRepository mTasksRepository; // Tasks的数据源
    private final TasksContract.View mTasksView; // View显示界面
    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS; // 默认显示全部任务

    public TasksPresenter(@NonNull TasksRepository tasksRepository, @NonNull TasksContract.View tasksView) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mTasksView.setPresenter(this); // View设置Presenter
    }

    /**
     * 用于加载Task, 使用TasksRepository
     */
    @Override public void start() {
        // TODO: 加载数据
    }

    @Override public void result(int requestCode, int resultCode) {
        // TODO: 处理返回数据
    }

    /**
     * 设置显示的过滤类型
     *
     * @param requestType 需要显示的Task的类型
     */
    @Override public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    /**
     * 获取当前的过滤类型
     *
     * @return 获取过滤类型
     */
    @Override public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    /**
     * 加载任务
     *
     * @param forceUpdate 是否强制更新
     */
    @Override public void loadTasks(boolean forceUpdate) {
        mTasksView.showTasks(null);
    }

    /**
     * 添加新的Task
     */
    @Override public void addNewTask() {
        mTasksView.showAddTask();
    }
}
