package org.wangchenlong.mvpdatabindingdemo.tasks;

import org.wangchenlong.mvpdatabindingdemo.data.Task;

/**
 * Task的Item的活动, 列表的项, ActionHandler是Presenter的代理
 * <p>
 * Created by wangchenlong on 16/7/13.
 */
public class TasksItemActionHandler {
    private TasksContract.Presenter mPresenter; // 表示层

    /**
     * 设置Presenter
     *
     * @param presenter 处理
     */
    public TasksItemActionHandler(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 完成状态修改
     *
     * @param task      任务
     * @param isChecked 是否点击
     */
    public void completeChanged(Task task, boolean isChecked) {
        if (isChecked) {
            mPresenter.completeTask(task); // 选中完成的Task
        } else {
            mPresenter.activateTask(task); // 激活任务的Task
        }
    }

    /**
     * 点击任务, 显示任务详情
     *
     * @param task 打开任务详情
     */
    public void taskClicked(Task task) {
        mPresenter.openTaskDetails(task); // 显示任务详情
    }
}
