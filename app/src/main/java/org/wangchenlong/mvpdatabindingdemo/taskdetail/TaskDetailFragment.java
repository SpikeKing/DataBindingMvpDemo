package org.wangchenlong.mvpdatabindingdemo.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.addedittask.AddEditTaskActivity;
import org.wangchenlong.mvpdatabindingdemo.addedittask.AddEditTaskFragment;
import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.databinding.FragmentTaskDetailBinding;

/**
 * 任务详情
 * <p>
 * Created by wangchenlong on 16/7/18.
 */
public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {

    public static final String ARGUMENT_TASK_ID = "TaskDetailFragment.TASK_ID"; // 参数Id
    public static final int REQUEST_EDIT_TASK = 1; // 返回编辑任务状态

    private FragmentTaskDetailBinding mViewDataBinding; // DataBinding
    private TaskDetailContract.Presenter mPresenter; // 表示层

    @Override public void setPresenter(TaskDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static TaskDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments); // 设置参数
        return fragment;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewDataBinding.setPresenter(mPresenter);
    }

    @Override public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        mViewDataBinding = FragmentTaskDetailBinding.bind(view); // DataBinding绑定View
        setHasOptionsMenu(true);
        setRetainInstance(true);

        FloatingActionButton fab = (FloatingActionButton)
                getActivity().findViewById(R.id.task_detail_fab_edit_task);

        // Fab点击修改Task
        fab.setOnClickListener(v -> {
            String taskId = getArguments().getString(ARGUMENT_TASK_ID);
            Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
            intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
            startActivityForResult(intent, REQUEST_EDIT_TASK);
        });

        return view;
    }


    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task_detail, menu); // 添加任务详情菜单
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mPresenter.deleteTask(); // 删除任务
                return true;
        }
        return false;
    }

    // 编辑Task
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示任务, 在ViewBinding设置任务
     *
     * @param task 任务
     */
    @Override public void showTask(Task task) {
        mViewDataBinding.setTask(task); // 设置任务
    }

    /**
     * 显示错误, 在ViewBinding设置空任务
     */
    @Override public void showError() {
        Task emptyTask = new Task("", getString(R.string.no_data));
        mViewDataBinding.setTask(emptyTask); // 设置空任务
    }

    /**
     * Fragment绑定Activity
     *
     * @return 是否绑定页面
     */
    @Override public boolean isActive() {
        return isAdded();
    }

    /**
     * 显示任务标记完成
     */
    @Override public void showTaskMarkedComplete() {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, getString(R.string.task_marked_complete),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 显示任务标记激活
     */
    @Override public void showTaskMarkedActive() {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, getString(R.string.task_marked_active),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 显示任务的删除, 直接关闭页面
     */
    @Override public void showTaskDeleted() {
        getActivity().finish(); // 直接关闭页面
    }
}
