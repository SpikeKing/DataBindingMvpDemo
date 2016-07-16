package org.wangchenlong.mvpdatabindingdemo.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.wangchenlong.mvpdatabindingdemo.Injection;
import org.wangchenlong.mvpdatabindingdemo.MainActivity;
import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 统计页面
 * <p>
 * Created by wangchenlong on 16/7/15.
 */
public class StatisticsActivity extends AppCompatActivity {

    @BindView(R.id.statistic_t_toolbar) Toolbar mTToolbar;
    @BindView(R.id.statistic_fl_content_frame) FrameLayout mFlContentFrame;
    @BindView(R.id.statistic_nv_nav_view) NavigationView mNvNavView;
    @BindView(R.id.statistic_dl_drawer_layout) DrawerLayout mDlDrawerLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        // 设置Toolbar
        setSupportActionBar(mTToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.statistics_title);
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDlDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        setupDrawerContent(mNvNavView);

        // 统计页面的显示
        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.statistic_fl_content_frame);

        // 新建统计界面
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.statistic_fl_content_frame);
        }

        // Fragment设置ViewModel
        StatisticsViewModel statisticsViewModel = new StatisticsViewModel(getApplicationContext());
        statisticsFragment.setViewModel(statisticsViewModel);

        // Fragment设置表示层Presenter
        StatisticsPresenter statisticsPresenter = new StatisticsPresenter(
                Injection.providerTasksRepository(getApplicationContext()), statisticsViewModel);
        statisticsFragment.setPresenter(statisticsPresenter);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDlDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置导航页面
     *
     * @param navigationView 导航页面
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.list_navi_menu_item:
                            Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.statistic_navi_menu_item:
                            break;
                        default:
                            break;
                    }
                    menuItem.setChecked(true);
                    mDlDrawerLayout.closeDrawers();
                    return true;
                }
        );
    }


}
