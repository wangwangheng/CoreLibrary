package com.droid.library.app.mvp.list;

import android.os.Bundle;


import com.droid.library.app.adapter.BaseAbstractAdapter;
import com.droid.library.app.emptyview.BaseCallback;
import com.droid.library.app.emptyview.BaseEmptyViewManager;
import com.droid.library.app.mvp.IUI;

import java.io.Serializable;


/**
 * ListView的UI的协议.
 *
 * @author wangheng
 */
public interface IListUI<T extends Serializable> extends IUI {

    /**
     * getAdapter:列表数据的适配器
     *
     * @return 适配器
     */
    BaseAbstractAdapter<T> getAdapter();

    /**
     * 下拉刷新完成. <br/>
     */
    void onPullDownRefreshCompleted();

    /**
     * 上拉刷新完成
     */
//    void onPullUpRefreshCompleted();

    /**
     * getFooterManager:得到FooterManager. <br/>
     *
     * @return FooterManager
     */
    FooterManager getFooterManager();

    /**
     * 其他界面传递过来的Bundle数据
     *
     * @return 其他界面传递过来的Bundle对象
     */
    Bundle getExtras();

    /**
     * 得到空View管理器
     *
     * @return 空View管理器
     */
    BaseEmptyViewManager<? extends BaseCallback> getEmptyViewManager();
}
