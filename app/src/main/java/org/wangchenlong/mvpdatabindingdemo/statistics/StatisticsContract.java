package org.wangchenlong.mvpdatabindingdemo.statistics;

import org.wangchenlong.mvpdatabindingdemo.bases.BasePresenter;
import org.wangchenlong.mvpdatabindingdemo.data.Task;

import java.util.List;

/**
 * 统计的合同, 设置接口
 * <p>
 * Created by wangchenlong on 16/7/15.
 */
public interface StatisticsContract {
    interface View {
        // 设置ProgressIndicator加载
        void setProgressIndicator(boolean active);

        // 展示统计信息
        void displayStatistics(List<Task> tasks);

        // 展示加载统计错误
        void showLoadingStatisticsError();
    }

    interface Presenter extends BasePresenter {
        // 加载统计
        void loadStatistics();
    }
}
