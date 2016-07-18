package org.wangchenlong.mvpdatabindingdemo.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.wangchenlong.mvpdatabindingdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详细任务的页面
 * <p>
 * Created by wangchenlong on 16/7/7.
 */
public class TaskDetailActivity extends AppCompatActivity {
    // Task的Id
    public static final String EXTRA_TASK_ID = "TaskDetailActivity.EXTRA_TASK_ID";

    @BindView(R.id.task_detail_t_tool_bar) Toolbar mTToolBar;
    @BindView(R.id.task_detail_fl_content_frame) FrameLayout mFlContentFrame;
    @BindView(R.id.task_detail_fab_edit_task) FloatingActionButton mFabEditTask;
    @BindView(R.id.task_detail_cl_coordinator_layout) CoordinatorLayout mClCoordinatorLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);

        // 设置ActionBar
        setSupportActionBar(mTToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);


    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
