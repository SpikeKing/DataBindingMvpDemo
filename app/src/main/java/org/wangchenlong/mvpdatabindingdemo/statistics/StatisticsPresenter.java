package org.wangchenlong.mvpdatabindingdemo.statistics;

import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksRepository;
import org.wangchenlong.mvpdatabindingdemo.utils.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 统计展示层
 * <p>
 * Created by wangchenlong on 16/7/15.
 */
public class StatisticsPresenter implements StatisticsContract.Presenter {

    private final TasksRepository mTasksRepository; // 任务仓库
    private final StatisticsContract.View mStatisticsView; // 统计页面

    /**
     * 统计Presenter
     *
     * @param tasksRepository 任务仓库
     * @param statisticsView  页面
     */
    public StatisticsPresenter(@NonNull TasksRepository tasksRepository,
                               @NonNull StatisticsContract.View statisticsView) {
        mTasksRepository = checkNotNull(tasksRepository, "TasksRepository cannot be null!");
        mStatisticsView = checkNotNull(statisticsView, "StatisticsView cannot be null!");
    }

    /**
     * 启动Presenter
     */
    @Override public void start() {
        loadStatistics();
    }

    /**
     * 加载统计信息
     */
    @Override public void loadStatistics() {
        mStatisticsView.setProgressIndicator(true); // 设置加载标记

        // 空闲资源的控制
        EspressoIdlingResource.increment(); // App is busy until further notice

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override public void onTasksLoaded(List<Task> tasks) {

                // 空闲资源是否非闲置
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                mStatisticsView.setProgressIndicator(false); // 加载完成
                mStatisticsView.displayStatistics(tasks); // 显示任务
            }

            @Override public void onDataNotAvailable() {
                mStatisticsView.showLoadingStatisticsError(); // 加载状态错误
            }
        });
    }
}
