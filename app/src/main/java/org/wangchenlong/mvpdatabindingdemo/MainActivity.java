package org.wangchenlong.mvpdatabindingdemo;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.wangchenlong.mvpdatabindingdemo.tasks.TasksFilterType;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksPresenter;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksViewModel;
import org.wangchenlong.mvpdatabindingdemo.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // 当前显示的Task类型, 过滤的Key
    private static final String CURRENT_FILTERING_KEY = "MainActivity.CURRENT_FILTERING_KEY";

    @BindView(R.id.main_tl_toolbar) Toolbar mTlToolbar;
    @BindView(R.id.main_dl_drawer_layout) DrawerLayout mDlDrawerLayout;
    @BindView(R.id.main_nv_nav_view) NavigationView mNvNavView;

    private TasksPresenter mTasksPresenter; // Tasks的Presenter, 负责处理页面逻辑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 设置ActionBar
        setSupportActionBar(mTlToolbar); // 设置Toolbar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // 设置抽屉
        mDlDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        setupDrawerContent(mNvNavView);

        // 设置Fragment, 绑定页面的工作在Presenter中进行
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fl_content_frame);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mainFragment, R.id.main_fl_content_frame);
        }

        // 初始化Presenter, 添加数据源和View, 内部View绑定Presenter
        mTasksPresenter = new TasksPresenter(
                Injection.providerTasksRepository(getApplicationContext()),
                mainFragment);

        // View绑定ViewModel
        TasksViewModel tasksViewModel =
                new TasksViewModel(getApplicationContext(), mTasksPresenter);
        mainFragment.setViewModel(tasksViewModel);

        // 读取Tasks的类型
        if (savedInstanceState != null) {
            TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    // 切换页面时, 存储Tasks的类型
    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 从左侧打开抽屉
                mDlDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置抽屉内容
     *
     * @param navigationView 视图
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.list_navi_menu_item:
                    // 列表按钮什么都不做
                    break;
                case R.id.statistic_navi_menu_item:
                    // TODO: 添加统计页面
                    break;
                default:
                    break;
            }
            // 关闭抽屉
            item.setChecked(true);
            mDlDrawerLayout.closeDrawers();
            return true;
        });
    }

}
