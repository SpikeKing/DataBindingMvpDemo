package org.wangchenlong.mvpdatabindingdemo.statistics;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.databinding.FragmentStatisticsBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 统计页面
 * <p>
 * Created by wangchenlong on 16/7/15.
 */
public class StatisticsFragment extends Fragment {
    private StatisticsPresenter mPresenter; // 统计的展示层
    private FragmentStatisticsBinding mViewDataBinding; // 视图数据绑定
    private StatisticsViewModel mViewModel; // 统计视图模型

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    public void setPresenter(StatisticsPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false);
        return mViewDataBinding.getRoot();
    }

    /**
     * Activity的创建
     *
     * @param savedInstanceState 状态
     */
    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 设置DataBinding中的Model, 方法是自动生成
        mViewDataBinding.setStatistics(mViewModel);
    }

    @Override public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * 设置ViewModel
     *
     * @param statisticsViewModel 统计的ViewModel
     */
    public void setViewModel(StatisticsViewModel statisticsViewModel) {
        mViewModel = statisticsViewModel;
    }

    /**
     * Fragment是否绑定Activity
     *
     * @return 是否激活
     */
    public boolean isActive() {
        return isAdded();
    }
}
