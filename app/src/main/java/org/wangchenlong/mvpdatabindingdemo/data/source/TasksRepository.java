package org.wangchenlong.mvpdatabindingdemo.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wangchenlong.mvpdatabindingdemo.data.Task;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据层, 用于管理Task的数据
 * Created by wangchenlong on 16/7/5.
 */
public class TasksRepository implements TasksDataSource {
    private static TasksRepository sInstance = null;

    private final TasksDataSource mTasksRemoteDataSource;
    private final TasksDataSource mTasksLocalDataSource;

    private Map<String, Task> mCachedTasks;// Task的缓存, 也可以供测试使用

    private boolean mCacheIsDirty;

    private TasksRepository(
            @NonNull TasksDataSource tasksRemoteDataSource,
            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = tasksRemoteDataSource;
        mTasksLocalDataSource = tasksLocalDataSource;
    }

    public static TasksRepository getInstance(
            @NonNull TasksDataSource tasksRemoteDataSource,
            @NonNull TasksDataSource tasksLocalDataSource) {
        if (sInstance == null) {
            sInstance = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return sInstance;
    }

    /**
     * 获取全部Tasks
     *
     * @param callback 回调
     */
    @Override public void getTasks(@NonNull LoadTasksCallback callback) {
        checkNotNull(callback);

        // 不为空, 不污染时, 直接返回缓存
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }

        // 数据被污染了, 加载远程
        if (mCacheIsDirty) {
            getTasksFromRemoteDataSource(callback);
        } else { // 没有被污染, 加载本地
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    // 加载缓存任务, 保证数据一致
                    callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override public void onDataNotAvailable() {
                    // 本地加载失败, 继续加载远程
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    /**
     * 从网络中获取任务
     *
     * @param callback 回调
     */
    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override public void onTasksLoaded(List<Task> tasks) {
                // 设置缓存, 设置本地数据
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 刷新内存缓存, 从远端加载缓存
     *
     * @param tasks 任务
     */
    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }

        mCacheIsDirty = false; // 刷新过缓存, 即没有被污染
    }

    /**
     * 刷新本地缓存
     *
     * @param tasks 任务
     */
    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (int i = 0; i < tasks.size(); ++i) {
            mTasksLocalDataSource.saveTask(tasks.get(i));
        }
    }

    /**
     * 获取单个Task
     *
     * @param taskId   Id
     * @param callback 回到
     */
    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        // 有缓存直接返回数据
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        /**
         * 优先获取本地缓存, 失败后获取远程缓存
         */
        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override public void onDataNotAvailable() {
                mTasksRemoteDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override public void onTaskLoaded(Task task) {
                        callback.onTaskLoaded(task);
                    }

                    @Override public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    /**
     * 存储任务, 存储当前任务
     *
     * @param task 任务
     */
    @Override public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);
        mTasksLocalDataSource.saveTask(task);

        // 本地缓存, 加快UI的响应速度
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    /**
     * 获取内存中的缓存
     *
     * @param id Task的Id
     * @return Task任务
     */
    @Nullable Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }

    /**
     * 删除任务
     */
    @Override public void deleteAllTasks() {

    }

    /**
     * 刷新任务
     */
    @Override public void refreshTasks() {
        // 刷新任务后, 数据被污染
        mCacheIsDirty = true;
    }

    /**
     * 完成任务, 处理本地与远程的任务.
     *
     * @param task 任务
     */
    @Override public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
        mTasksRemoteDataSource.completeTask(task);

        Task completedTask = new Task(task.getId(), task.getTitle(),
                task.getDescription(), true);

        // 缓存任务
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    /**
     * 完成任务
     *
     * @param taskId 任务Id
     */
    @Override public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        Task task = getTaskWithId(taskId);
        if (task != null) {
            completeTask(task);
        }
    }

    /**
     * 激活任务, 处理本地与远程任务
     *
     * @param task 任务
     */
    @Override public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.activateTask(task);
        mTasksRemoteDataSource.activateTask(task);

        // 激活任务, 默认是false
        Task activeTask = new Task(task.getId(),
                task.getTitle(), task.getDescription());

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    /**
     * 激活任务
     *
     * @param taskId 任务Id
     */
    @Override public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        Task task = getTaskWithId(taskId);
        if (task != null) {
            activateTask(task);
        }
    }
}
