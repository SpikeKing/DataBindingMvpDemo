package org.wangchenlong.mvpdatabindingdemo.utils;

import android.support.test.espresso.IdlingResource;

/**
 * 资源引用, 用于测试同步
 * Created by wangchenlong on 16/7/18.
 */
public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";

    private static SimpleCountingIdlingResource sCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        sCountingIdlingResource.increment();
    }

    public static void decrement() {
        sCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return sCountingIdlingResource;
    }
}
