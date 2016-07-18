package org.wangchenlong.mvpdatabindingdemo.taskdetail;

import org.wangchenlong.mvpdatabindingdemo.bases.BasePresenter;
import org.wangchenlong.mvpdatabindingdemo.bases.BaseView;
import org.wangchenlong.mvpdatabindingdemo.data.Task;

/**
 * 任务详情的合同
 * <p>
 * Created by wangchenlong on 16/7/18.
 */
public interface TaskDetailContract {
    interface View extends BaseView<Presenter> {
        void showTask(Task task); // 显示任务

        void showError(); // 显示错误

        boolean isActive(); // Fragment是否绑定View

        // 显示任务已经标记完成
        void showTaskMarkedComplete();

        // 显示任务已经标记激活
        void showTaskMarkedActive();

        // 显示任务删除
        void showTaskDeleted();
    }

    interface Presenter extends BasePresenter {
        void getTask(); // 获取任务

        void deleteTask(); // 删除任务

        void completeChanged(Task task, boolean isChecked); // 完成状态的改变
    }
}
