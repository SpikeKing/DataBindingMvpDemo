package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.databinding.TaskItemBinding;

import java.util.List;

/**
 * 显示Tasks列表的Adapter
 * <p>
 * Created by wangchenlong on 16/7/13.
 */
public class TasksListAdapter extends BaseAdapter {
    private List<Task> mTasks; // 显示的Tasks
    // Presenter的地位, 就是用户活动监听
    private TasksContract.Presenter mUserActionsListener; // 监听

    /**
     * 任务设置
     *
     * @param tasks               任务
     * @param userActionsListener 监听
     */
    public TasksListAdapter(
            List<Task> tasks,
            TasksContract.Presenter userActionsListener) {
        setList(tasks);
        mUserActionsListener = userActionsListener;
    }

    // 设置任务Task
    private void setList(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    // 替换数据
    public void replaceData(List<Task> tasks) {
        setList(tasks);
    }

    @Override public int getCount() {
        return mTasks != null ? mTasks.size() : 0;
    }

    @Override public Task getItem(int position) {
        return mTasks.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        TaskItemBinding binding;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            binding = TaskItemBinding.inflate(inflater, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        TasksItemActionHandler itemActionHandler
                = new TasksItemActionHandler(mUserActionsListener);
        binding.setActionHandler(itemActionHandler);
        binding.setTask(task);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}
