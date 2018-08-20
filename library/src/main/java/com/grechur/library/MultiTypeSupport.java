package com.grechur.library;

/**
 * Created by zz on 2018/8/17.
 * 多布局支持
 */

public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    public int getLayoutId(T item, int position);
}
