package org.wangchenlong.mvpdatabindingdemo.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

// Google的基础类方法, 判空
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Activity的常用方法
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public class ActivityUtils {
    /**
     * 在Activity中绑定
     *
     * @param fragmentManager fragment管理器
     * @param fragment        Fragment页面
     * @param frameId         布局Id
     */
    public static void addFragmentToActivity(
            @NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
