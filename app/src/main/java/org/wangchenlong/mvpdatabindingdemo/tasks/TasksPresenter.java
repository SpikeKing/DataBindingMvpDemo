package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

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

    private boolean mFirstLoad = true; // 第一次加载

    public TasksPresenter(@NonNull TasksRepository tasksRepository, @NonNull TasksContract.View tasksView) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository cannot be null!");
        mTasksView = checkNotNull(tasksView, "tasksView cannot be null!");

        mTasksView.setPresenter(this); // View设置Presenter
    }

    /**
     * 用于加载Task, 使用TasksRepository
     */
    @Override public void start() {
        loadTasks(false);
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
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * 加载任务的核心部分
     *
     * @param forceUpdate   强迫加载
     * @param showLoadingUI 更新没有
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true); // 显示正在加载
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks(); // 确认数据被污染
        }

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>(); // 需要展示的项目列表

                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }

                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false); // 关闭加载
                }

                mTasksView.showTasks(tasksToShow);
            }

            @Override public void onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }

                // 显示加载错误
                mTasksView.showLoadingTasksError();
            }
        });
    }

    /**
     * 添加新的Task
     */
    @Override public void addNewTask() {
        mTasksView.showAddTask();
    }

    /**
     * 完成任务, 点击任务侧面的复选框, 选中即完成
     *
     * @param completedTask 完成的任务
     */
    @Override public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");
        mTasksRepository.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
    }

    /**
     * 激活任务, 未点击任务侧面的复选框, 未选中即激活
     *
     * @param activeTask 激活任务
     */
    @Override public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        mTasksRepository.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
    }

    /**
     * 打开任务详情.
     *
     * @param requestedTask 需要的任务
     */
    @Override public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        // 调用View, 跳转显示Task的UI页面
        mTasksView.showTaskDetailsUi(requestedTask.getId());
    }
}
