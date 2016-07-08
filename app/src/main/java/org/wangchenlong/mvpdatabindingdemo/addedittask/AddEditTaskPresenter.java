package org.wangchenlong.mvpdatabindingdemo.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 添加Task的Presenter
 * <p>
 * Created by wangchenlong on 16/7/8.
 */
public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {
    @NonNull private final TasksDataSource mTasksRepository;
    @NonNull private final AddEditTaskContract.View mAddTaskView;
    @Nullable private String mTaskId;

    public AddEditTaskPresenter(@NonNull TasksDataSource tasksRepository, @NonNull AddEditTaskContract.View addTaskView, String taskId) {
        mTasksRepository = checkNotNull(tasksRepository);
        mAddTaskView = checkNotNull(addTaskView);
        mTaskId = taskId;

        mAddTaskView.setPresenter(this);
    }

    @Override public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description); // 新Task创建
        } else {
            updateTask(title, description); // 旧Task更新
        }
    }

    @Override public void start() {
        if (!isNewTask()) {
            populateTask();
        }
    }

    /**
     * 通过TaskId加载Task
     */
    private void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        if (mTaskId != null) {
            mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
                @Override public void onTaskLoaded(Task task) {
                    if (mAddTaskView.isActive()) {
                        mAddTaskView.setTask(task);
                    }
                }

                @Override public void onDataNotAvailable() {
                    if (mAddTaskView.isActive()) {
                        mAddTaskView.showEmptyTaskError();
                    }
                }
            });
        }
    }

    /**
     * 创建Task任务
     *
     * @param title       标题
     * @param description 描述
     */
    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);

        if (newTask.isEmpty()) { // 任务是空
            mAddTaskView.showEmptyTaskError(); // 没有任务
        } else { // 存储新任务
            mTasksRepository.saveTask(newTask);
            mAddTaskView.showTasksList(); // 存储之后显示列表
        }
    }

    /**
     * 更新Task任务
     *
     * @param title       标题
     * @param description 描述
     */
    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        mTasksRepository.saveTask(new Task(title, description, mTaskId));
        mAddTaskView.showTasksList(); // 编辑完成后显示列表
    }

    /**
     * 判断是否是新任务
     *
     * @return 是否是新任务
     */
    private boolean isNewTask() {
        return mTaskId == null;
    }
}
