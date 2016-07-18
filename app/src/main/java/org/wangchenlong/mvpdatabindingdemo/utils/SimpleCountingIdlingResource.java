package org.wangchenlong.mvpdatabindingdemo.utils;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 空载资源
 * Created by wangchenlong on 16/7/18.
 */
public class SimpleCountingIdlingResource implements IdlingResource {

    private final String mResourceName; // 资源名称
    private final AtomicInteger counter = new AtomicInteger(0); // 同步管理

    // 主线程写, 其他线程读
    private volatile ResourceCallback mResourceCallback;

    public SimpleCountingIdlingResource(String resourceName) {
        mResourceName = checkNotNull(resourceName);
    }

    @Override public String getName() {
        return mResourceName;
    }

    @Override public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }

    public void increment() {
        counter.getAndIncrement();
    }

    public void decrement() {
        int counterVal = counter.decrementAndGet();
        if (counterVal == 0) {
            if (null != mResourceCallback) {
                mResourceCallback.onTransitionToIdle();
            }
        }

        if (counterVal < 0) {
            throw new IllegalArgumentException("Counter has been corrupted!");
        }
    }
}
