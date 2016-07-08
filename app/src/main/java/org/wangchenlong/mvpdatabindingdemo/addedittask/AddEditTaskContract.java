package org.wangchenlong.mvpdatabindingdemo.addedittask;

import org.wangchenlong.mvpdatabindingdemo.bases.BasePresenter;
import org.wangchenlong.mvpdatabindingdemo.bases.BaseView;
import org.wangchenlong.mvpdatabindingdemo.data.Task;

/**
 * 添加或编辑任务的接口
 * <p>
 * Created by wangchenlong on 16/7/8.
 */
public interface AddEditTaskContract {
    interface View extends BaseView<Presenter> {
        // Fragment是否被添加到Activity
        boolean isActive();

        // 设置任务
        void setTask(Task task);

        // 显示空任务的错误提示
        void showEmptyTaskError();

        // 完成添加或者编辑Task以后, 显示Task的List
        void showTasksList();
    }

    interface Presenter extends BasePresenter {

        // 点击Fab按钮, 保存任务
        void saveTask(String title, String description);
    }
}
