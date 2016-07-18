package org.wangchenlong.mvpdatabindingdemo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.wangchenlong.mvpdatabindingdemo.data.Task;
import org.wangchenlong.mvpdatabindingdemo.data.source.local.TasksDbHelper.TaskEntry;
import org.wangchenlong.mvpdatabindingdemo.data.source.TasksDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Tasks的本地数据源
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource sInstance; // 单例
    private TasksDbHelper mDbHelper; // 数据库

    private TasksLocalDataSource(Context context) {
        checkNotNull(context);
        mDbHelper = new TasksDbHelper(context); // 初始化数据库
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new TasksLocalDataSource(context);
        }
        return sInstance;
    }

    @Override public void getTasks(@NonNull LoadTasksCallback callback) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        Cursor c = db.query(TaskEntry.TABLE_NAME,
                projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                Task task = new Task(itemId, title, description, completed);
                tasks.add(task);
            }
        }

        if (c != null) {
            c.close();
        }

        db.close();

        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(tasks);
        }
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 需要从数据库获取的内容
        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        // 查询的列Id
        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId}; // 查询的标志位

        // 获取数据库的游标
        Cursor c = db.query(
                TaskEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null);

        // 从数据库的游标中获取数据
        Task task = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst(); // 移动数据库的光标
            String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            task = new Task(itemId, title, description, completed);
        }

        if (c != null) {
            c.close();
        }

        db.close();

        // 根据Task判断是否查询成功
        if (task != null) {
            callback.onTaskLoaded(task);
        } else {
            callback.onDataNotAvailable();
        }
    }

    /**
     * 存储任务
     *
     * @param task 任务
     */
    @Override public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        db.insert(TasksDbHelper.TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * 删除全部任务
     */
    @Override public void deleteAllTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TasksDbHelper.TaskEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override public void refreshTasks() {
        // 供{@link TaskRepository}使用
    }

    /**
     * 完成任务
     *
     * @param task 任务
     */
    @Override public void completeTask(@NonNull Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // 任务的完成状态
        ContentValues values = new ContentValues();
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_COMPLETED, true);

        // 更新完成状态
        String selection = TasksDbHelper.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionsArgs = {task.getId()};
        db.update(TasksDbHelper.TaskEntry.TABLE_NAME, values, selection, selectionsArgs);
        db.close();
    }

    @Override public void completeTask(@NonNull String taskId) {
        // 供{@link TaskRepository}使用
    }

    /**
     * 激活任务
     *
     * @param task 任务
     */
    @Override public void activateTask(@NonNull Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // 任务的完成状态
        ContentValues values = new ContentValues();
        values.put(TasksDbHelper.TaskEntry.COLUMN_NAME_COMPLETED, false);

        // 更新完成状态
        String selection = TasksDbHelper.TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionsArgs = {task.getId()};
        db.update(TasksDbHelper.TaskEntry.TABLE_NAME, values, selection, selectionsArgs);
        db.close();
    }

    @Override public void activateTask(@NonNull String taskId) {
        // 供{@link TaskRepository}使用
    }

    /**
     * 根据Task的Id删除任务
     *
     * @param taskId 任务Id
     */
    @Override public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase(); // 数据库
        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * 清理完成的任务
     */
    @Override public void clearCompletedTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = {"1"}; // 完成任务的标志
        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
