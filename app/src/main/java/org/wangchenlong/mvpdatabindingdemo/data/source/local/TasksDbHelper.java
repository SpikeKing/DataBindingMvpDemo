package org.wangchenlong.mvpdatabindingdemo.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Tasks的本地缓存数据库
 * <p>
 * Created by wangchenlong on 16/7/11.
 */
public class TasksDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1; // 数据库
    public static final String DATABASE_NAME = "Tasks.db"; // 数据库名称
    private static final String TEXT_TYPE = " TEXT"; // 字符串类型
    private static final String BOOLEAN_TYPE = " INTEGER"; // 数字类型
    private static final String COMMA_SEP = ","; // 逗号分隔符

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    TaskEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE + " )";

    /**
     * 初始化数据库, 数据库的名称与版本都是指定的.
     *
     * @param context 上下文
     */
    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Task的数据库类型, 继承基础列
     */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
