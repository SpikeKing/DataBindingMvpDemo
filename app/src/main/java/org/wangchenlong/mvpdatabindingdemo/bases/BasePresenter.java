package org.wangchenlong.mvpdatabindingdemo.bases;

/**
 * Presenter的基类, 主要是控制生命周期, 也可以绑定其他的生命周期
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public interface BasePresenter {
    void start(); // 启动Presenter, onResume时调用
}
