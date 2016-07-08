package org.wangchenlong.mvpdatabindingdemo.bases;

/**
 * View通过Presenter才能使用, 设置匹配的Presenter
 * <p>
 * Created by wangchenlong on 16/7/5.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
