package org.wangchenlong.mvpdatabindingdemo.statistics;

import android.content.Context;
import android.content.res.Resources;

import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.data.Task;

import java.util.List;

/**
 * 统计的ViewModel, 与View的状态绑定
 * <p>
 * Created by wangchenlong on 16/7/15.
 */
public class StatisticsViewModel implements StatisticsContract.View {

    private int mNumberOfCompletedTasks = 0; // 完成任务的数量
    private int mNumberOfActiveTasks = 0; // 激活任务的数量
    private Context mContext; // 上下文
    private Resources mResources; // 资源
    private boolean mIsLoading; // 是否加载完成
    private boolean mError; // 是否错误

    public StatisticsViewModel(Context context) {
        mContext = context;
        mResources = mContext.getResources();
        mIsLoading = true;
        mError = false;
    }

    /**
     * 获取激活任务的数量
     *
     * @return 激活任务的数量
     */
    public String getNumberOfActiveTasks() {
        return mResources.getString(R.string.statistics_active_tasks) + " "
                + mNumberOfActiveTasks;
    }

    /**
     * 获取完成任务的数量
     *
     * @return 完成任务的数量
     */
    public String getNumberOfCompletedTasks() {
        return mResources.getString(R.string.statistics_completed_tasks) + " "
                + mNumberOfCompletedTasks;
    }

    /**
     * 任务是否为空, 控制页面的显示
     *
     * @return 激活任务和完成任务的数量
     */
    public boolean isEmpty() {
        return (mNumberOfActiveTasks + mNumberOfCompletedTasks) == 0;
    }

    /**
     * 获取当前状态
     *
     * @return 获取状态
     */
    public String getStatus() {
        if (mError) {
            return mResources.getString(R.string.loading_tasks_error);
        }

        if (mIsLoading) {
            return mResources.getString(R.string.loading);
        }

        return null;
    }

    /**
     * 显示状态, 有错误与加载时都会显示提示
     *
     * @return 当前状态
     */
    public boolean showStatus() {
        return mError || mIsLoading;
    }

    /**
     * 设置当前处理表示符
     *
     * @param active 活跃
     */
    @Override public void setProgressIndicator(boolean active) {
        mIsLoading = active;
    }

    /**
     * 统计任务, 统计完成表示加载完成
     *
     * @param tasks 任务
     */
    @Override public void displayStatistics(List<Task> tasks) {
        mNumberOfCompletedTasks = 0;
        mNumberOfActiveTasks = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                mNumberOfCompletedTasks++;
            } else {
                mNumberOfActiveTasks++;
            }
        }

        mIsLoading = false;
    }

    /**
     * 显示加载统计错误
     */
    @Override public void showLoadingStatisticsError() {
        mError = true; // 加载错误
        mIsLoading = false; // 加载未完成
    }
}
