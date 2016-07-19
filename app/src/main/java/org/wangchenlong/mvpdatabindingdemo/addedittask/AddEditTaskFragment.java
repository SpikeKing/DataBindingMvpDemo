package org.wangchenlong.mvpdatabindingdemo.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.wangchenlong.mvpdatabindingdemo.R;
import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.databinding.FragmentAddTaskBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 添加或修改新任务的核心界面
 * <p>
 * Created by wangchenlong on 16/7/8.
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {
    public static final String ARGUMENT_EDIT_TASK_ID = "AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID";

    TextView mEtTitle;
    TextView mEtDescription;

    private AddEditTaskContract.Presenter mPresenter;
    private FragmentAddTaskBinding mTaskBinding;

    @Override public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public AddEditTaskFragment() {
    }

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);

        mEtTitle = (TextView) root.findViewById(R.id.task_add_et_title);
        mEtDescription = (TextView) root.findViewById(R.id.task_add_et_description);

        mTaskBinding = FragmentAddTaskBinding.bind(root);

        setHasOptionsMenu(true);
        setRetainInstance(true); // 保留Fragment的单例
        return root;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton)
                getActivity().findViewById(R.id.task_add_fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        // 点击Fab按钮, 保存任务
        fab.setOnClickListener(v -> {
            mPresenter.saveTask(mEtTitle.getText().toString(),
                    mEtDescription.getText().toString());
        });
    }

    /**
     * 判断Fragment是否绑定Activity
     *
     * @return 是否绑定
     */
    @Override public boolean isActive() {
        return isAdded();
    }

    /**
     * 设置Task, 用于修改Task
     *
     * @param task 任务
     */
    @Override public void setTask(Task task) {
        mTaskBinding.setTask(task); // 自动发送通知
    }

    /**
     * 空Task的提示
     */
    @Override public void showEmptyTaskError() {
        Snackbar.make(mEtTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    /**
     * 退回到当前页面
     */
    @Override public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
