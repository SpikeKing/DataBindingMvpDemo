package org.wangchenlong.mvpdatabindingdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.wangchenlong.mvpdatabindingdemo.addedittask.AddEditTaskActivity;
import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.databinding.FragmentTasksBinding;
import org.wangchenlong.mvpdatabindingdemo.taskdetail.TaskDetailActivity;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksContract;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksFilterType;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksListAdapter;
import org.wangchenlong.mvpdatabindingdemo.tasks.TasksViewModel;
import org.wangchenlong.mvpdatabindingdemo.widgets.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fragment布局
 * Created by wangchenlong on 16/7/5.
 */
public class MainFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter; // Presenter, View绑定Presenter, 控制逻辑操作.
    private TasksViewModel mViewModel; // ViewModel, 在状态改变时, 广播通知, 修改View.
    private TasksListAdapter mTasksListAdapter; // 显示任务列表的适配器.

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 设置DataBinding
        FragmentTasksBinding tasksBinding = FragmentTasksBinding.inflate(inflater, container, false);
        tasksBinding.setTasks(mViewModel);
        tasksBinding.setActionHandler(mPresenter);

        // ListView的设置
        ListView listView = tasksBinding.tasksLvList;
        mTasksListAdapter = new TasksListAdapter(new ArrayList<Task>(0), mPresenter);
        listView.setAdapter(mTasksListAdapter);

        // Fab按钮添加任务
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.main_fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(v -> mPresenter.addNewTask());

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = tasksBinding.tasksScsrlRefreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorPrimary),
                ContextCompat.getColor(getContext(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(listView);

        setHasOptionsMenu(true); // 设置显示菜单

        View root = tasksBinding.getRoot();

        return root;
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

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 清除完成的任务
            case R.id.menu_clear:
                mPresenter.clearCompletedTasks();
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
     * 设置Presenter
     *
     * @param presenter 控制页面
     */
    @Override public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 设置ViewModel
     *
     * @param viewModel ViewModel
     */
    public void setViewModel(TasksViewModel viewModel) {
        mViewModel = viewModel;
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
        mTasksListAdapter.replaceData(tasks);
        mViewModel.setTaskListSize(tasks.size());
    }

    @Override public void setLoadingIndicator(boolean active) {
        // 已经关闭, 直接返回
        if (getView() == null) {
            return;
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout)
                getView().findViewById(R.id.tasks_scsrl_refresh_layout);

        srl.post(() -> srl.setRefreshing(active));
    }

    // 判断是否加载页面, 用于处理一些异步任务, 网络加载完成.
    @Override public boolean isActive() {
        return isAdded();
    }

    // 加载任务错误
    @Override public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    // 显示任务的完成消息
    @Override public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    // 显示任务的激活消息
    @Override public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    // 显示任务详情的UI
    @Override public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId); // 传输TaskId
        startActivity(intent); // 启动业务详情页面
    }

    /**
     * 显示清除的完成任务
     */
    @Override public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    /**
     * 显示成功存储信息
     */
    @Override public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    /**
     * 显示信息
     *
     * @param message 信息
     */
    private void showMessage(String message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }
}

