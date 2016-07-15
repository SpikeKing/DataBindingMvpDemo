package org.wangchenlong.mvpdatabindingdemo.data;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.wangchenlong.mvpdatabindingdemo.BR;

import java.util.Objects;
import java.util.UUID;

/**
 * 任务的具体描述
 * <p>
 * Created by wangchenlong on 16/7/6.
 */
public final class Task {
    private final String mId; //  任务Id

    @Nullable private final String mTitle; // Task的标题

    @Nullable private final String mDescription; // Task的描述

    private boolean mCompleted; // 完成

    /**
     * 使用构造器创建一个新的Task
     *
     * @param title       标题
     * @param description 描述
     */
    public Task(@Nullable String title, @Nullable String description) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    /**
     * 添加Id的构造器
     *
     * @param id          id
     * @param title       标题
     * @param description 描述
     */
    public Task(String id, String title, String description) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    /**
     * 完整的构造器
     *
     * @param id          Id
     * @param title       标题
     * @param description 描述
     * @param completed   是否完成
     */
    public Task(String id, String title, String description, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    // 返回当前的Task的Id
    public String getId() {
        return mId;
    }

    @Nullable public String getTitle() {
        return mTitle;
    }

    @Nullable public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    /**
     * 当标题Title不为空时, 优先返回Title, 其次返回Description
     *
     * @return 用于描述List的标题
     */
    @Nullable public String getTitleForList() {
        if (mTitle != null && !mTitle.equals("")) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    /**
     * 设置是否完成
     *
     * @param completed 完成状态
     */
    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    /**
     * 没有完成就算是激活
     *
     * @return 激活
     */
    public boolean isActive() {
        return !mCompleted;
    }

    /**
     * 判断标题和描述是否是空
     *
     * @return 是否是空
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mDescription);
    }

    /**
     * Task对象的核心内容, id, title, description的HashCode
     *
     * @return Hash值
     */
    @TargetApi(19)
    public int hashCode() {
        return Objects.hash(mId, mTitle, mDescription);
    }

    /**
     * Api19添加若干接口, 判断对象是否相等
     *
     * @param o 对象
     * @return 对象是否相等
     */
    @TargetApi(19)
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(mId, task.mId) &&
                Objects.equals(mTitle, task.mTitle) &&
                Objects.equals(mDescription, task.mDescription);
    }

    @Override public String toString() {
        return "任务: " + mTitle + ", 描述: " + mDescription;
    }
}
