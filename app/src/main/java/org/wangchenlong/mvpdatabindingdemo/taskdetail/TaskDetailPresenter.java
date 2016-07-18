package org.wangchenlong.mvpdatabindingdemo.taskdetail;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksRepository;

/**
 * 任务详情的表示层
 * <p>
 * Created by wangchenlong on 16/7/18.
 */
public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TaskDetailContract.View mTaskDetailView; // 任务详情
    private TasksRepository mRepository; // 任务仓库

    @NonNull private String mTaskId; // 任务Id

    public TaskDetailPresenter(
            TaskDetailContract.View taskDetailView,
            @NonNull String taskId,
            TasksRepository repository) {
        mTaskDetailView = taskDetailView;
        mTaskId = taskId;
        mRepository = repository;

        mTaskDetailView.setPresenter(this); // View设置Presenter
    }

    @Override public void start() {
        getTask();
    }

    @Override public void getTask() {
        mRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override public void onTaskLoaded(Task task) {
                if (!mTaskDetailView.isActive()) {
                    return;
                }

                if (task != null) {
                    mTaskDetailView.showTask(task);
                } else {
                    mTaskDetailView.showError();
                }
            }

            @Override public void onDataNotAvailable() {
                if (!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.showError();
            }
        });
    }

    @Override public void completeChanged(Task task, boolean isChecked) {
        task.setCompleted(isChecked);
        if (isChecked) {
            mRepository.completeTask(task);
            mTaskDetailView.showTaskMarkedComplete();
        } else {
            mRepository.activateTask(task);
            mTaskDetailView.showTaskMarkedActive();
        }
    }

    /**
     * 删除任务
     */
    @Override public void deleteTask() {
        // 删除任务
        mRepository.deleteTask(mTaskId);
        mTaskDetailView.showTaskDeleted();
    }
}
