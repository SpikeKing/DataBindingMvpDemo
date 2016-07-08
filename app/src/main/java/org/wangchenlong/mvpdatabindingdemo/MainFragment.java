package org.wangchenlong.mvpdatabindingdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.wangchenlong.mvpdatabindingdemo.addedittask.AddEditTaskActivity;
import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.databinding.FragmentTasksBinding;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksContract;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksFilterType;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksViewModel;

import java.util.List;

import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fragment布局
 * Created by wangchenlong on 16/7/5.
 */
public class MainFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter; // Presenter, View绑定Presenter
    private TasksViewModel mViewModel; // ViewModel, View改变时广播通知


    @Override public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override public void onResume() {
        super.onResume();
        // Presenter绑定View的生命周期, 还可以添加
        mPresenter.start();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Presenter处理View的Activity返回值
        mPresenter.result(requestCode, resultCode);
    }

    /**
     * 设置ViewModel
     *
     * @param viewModel ViewModel
     */
    public void setViewModel(TasksViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置DataBinding
        FragmentTasksBinding tasksBinding = FragmentTasksBinding.inflate(inflater, container, false);
        tasksBinding.setTasks(mViewModel);
        tasksBinding.setActionHandler(mPresenter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.main_fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(v -> mPresenter.addNewTask());

        setHasOptionsMenu(true); // 设置显示菜单

        View root = tasksBinding.getRoot();
        ButterKnife.bind(root);

        return root;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 清除完成的任务
            case R.id.menu_clear:
                // TODO: 清除完成的任务
                break;
            // 切换Tasks类型操作
            case R.id.menu_filter:
                showFilteringPopUpMenu(); // 显示过滤的菜单
                break;
            // 刷新操作, 加载任务
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
        }
        return true;
    }

    /**
     * 显示菜单的过滤Menu
     */
    private void showFilteringPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.tasks_filter, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.active:
                    mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                    break;
                case R.id.completed:
                    mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                    break;
                default:
                    mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                    break;
            }
            mPresenter.loadTasks(false);
            return true;
        });

        popupMenu.show();
    }

    /**
     * 启动添加新任务
     */
    @Override public void showAddTask() {
        // 跳转到添加新任务的界面
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    /**
     * 显示Task的列表
     *
     * @param tasks 任务
     */
    @Override public void showTasks(List<Task> tasks) {
        // 显示全部任务
        if (tasks != null) {
            mViewModel.setTaskListSize(tasks.size());
        } else {
            mViewModel.setTaskListSize(0);
        }
    }

    private void testToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

