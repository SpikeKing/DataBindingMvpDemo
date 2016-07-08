package org.wangchenlong.mvpdatabindingdemo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.wangchenlong.mvpdatabindingdemo.Injection;
import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加可编辑的Task
 * <p>
 * Created by wangchenlong on 16/7/7.
 */
public class AddEditTaskActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_TASK = 1;
    @BindView(R.id.task_add_tl_tool_bar) Toolbar mTlToolBar;
    @BindView(R.id.task_add_fl_content_frame) FrameLayout mFlContentFrame;
    @BindView(R.id.task_add_fab_edit_task_done) FloatingActionButton mFabEditTaskDone;
    @BindView(R.id.task_add_cl_coordinator_layout) CoordinatorLayout mClCoordinatorLayout;
    @BindView(R.id.task_add_dl_drawer_layout) DrawerLayout mDlDrawerLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        setSupportActionBar(mTlToolBar);
        ActionBar ab = getSupportActionBar();
        // 在Toolbar上添加Home键返回
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        AddEditTaskFragment addEditTaskFragment =
                (AddEditTaskFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.task_add_fl_content_frame);

        String taskId = null;
        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();

            // 修改Task
            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);
                if (ab != null) {
                    ab.setTitle(R.string.edit_task);
                }
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            } else { // 新任务
                if (ab != null) {
                    ab.setTitle(R.string.add_task);
                }
            }
        }

        // 绑定Fragment
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                addEditTaskFragment, R.id.task_add_fl_content_frame);

        // 初始化Presenter
        new AddEditTaskPresenter(Injection.providerTasksRepository(getApplicationContext()),
                addEditTaskFragment, taskId);
    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
