package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.BR;

/**
 * ViewModel, 继承BaseObservable, 类似观察者模式, {@link Bindable}绑定接口
 * fragment_tasks绑定ViewModel
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class TasksViewModel extends BaseObservable {
    private int mTaskListSize = 0; // 用于判断Task是否为空
    private final Context mContext; // 上下文
    private final Resources mResources; // 资源信息
    private final TasksContract.Presenter mPresenter; // 显示

    public TasksViewModel(Context context, TasksContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
        mResources = mContext.getResources();
    }

    /**
     * 用于判断是否有Tasks, 控制页面的显示, 空页面或者非空页面
     *
     * @return 是否已经添加Tasks
     */
    @Bindable
    public boolean isNotEmpty() {
        return mTaskListSize > 0;
    }

    /**
     * 根据选择Task的类型, 显示不同的文字
     *
     * @return 文字
     */
    @Bindable
    public String getCurrentFilteringLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mResources.getString(R.string.label_all);
            case ACTIVE_TASKS:
                return mResources.getString(R.string.label_active);
            case COMPLETED_TASKS:
                return mResources.getString(R.string.label_completed);
        }
        return null;
    }

    /**
     * 没有任务时, 显示的图片, 根据类型不同, 一共包含三种
     *
     * @return 没有任务的时候显示的图片
     */
    @SuppressWarnings("deprecation") @Bindable
    public Drawable getNoTaskIconRes() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mResources.getDrawable(R.drawable.ic_assignment_turned_in_24dp);
            case ACTIVE_TASKS:
                return mResources.getDrawable(R.drawable.ic_check_circle_24dp);
            case COMPLETED_TASKS:
                return mResources.getDrawable(R.drawable.ic_verified_user_24dp);
        }
        return null;
    }

    /**
     * 没有任务时, 显示的文字内容
     *
     * @return 没有任务时显示的文字
     */
    @Bindable
    public String getNoTasksLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mResources.getString(R.string.no_tasks_all);
            case ACTIVE_TASKS:
                return mResources.getString(R.string.no_tasks_active);
            case COMPLETED_TASKS:
                return mResources.getString(R.string.no_tasks_completed);
        }
        return null;
    }

    /**
     * @return
     */
    @Bindable
    public boolean getTasksAddViewVisible() {
        return mPresenter.getFiltering() == TasksFilterType.ALL_TASKS;
    }

    /**
     * 更新Bindable的数据, 对于DataBinding而言, 生成中如果出现错误, 则全部无法编译通过.
     * 要仔细排除错误位置
     *
     * @param taskListSize 列表长度
     */
    public void setTaskListSize(int taskListSize) {
        mTaskListSize = taskListSize;
        notifyPropertyChanged(BR.noTaskIconRes);
        notifyPropertyChanged(BR.noTasksLabel);
        notifyPropertyChanged(BR.currentFilteringLabel);
        notifyPropertyChanged(BR.notEmpty);
        notifyPropertyChanged(BR.tasksAddViewVisible);
    }
}
