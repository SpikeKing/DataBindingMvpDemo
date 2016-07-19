package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.databinding.BindingAdapter;

import org.wangchenlong.mvpdatabindingdemo.widgets.ScrollChildSwipeRefreshLayout;

/**
 * DataBinding的下拉刷新, 定制onRefresh方法.
 * <p>
 * Created by wangchenlong on 16/7/18.
 */
public class SwipeRefreshLayoutDataBinding {
    @BindingAdapter("android:onRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(
            ScrollChildSwipeRefreshLayout view,
            final TasksContract.Presenter presenter) {
        view.setOnRefreshListener(() -> presenter.loadTasks(true));
    }

}
