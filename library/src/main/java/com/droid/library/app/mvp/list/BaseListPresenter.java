package com.droid.library.app.mvp.list;

import com.droid.library.app.App;
import com.droid.library.app.adapter.BaseAbstractAdapter;
import com.droid.library.app.emptyview.BaseEmptyViewManager;
import com.droid.library.app.mvp.BasePresenter;
import com.droid.library.log.Logger;
import com.droid.library.utils.network.NetworkUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 带有下拉刷新，上拉加载的ListView的Presenter.
 *
 * @author wangheng
 */
public abstract class BaseListPresenter<U extends IListUI> extends BasePresenter<U> {

    /**
     * 第一页的页码，base 1 *
     */
    protected static final int PAGE_FIRST = 1;

    // 默认每页加载的数据条数
    private static final int PAGE_SIZE_DEFAULT = 20;

    private static final int TIME_MILLIS_OF_DELAY = 400;


    private static final int PULL_DOWN_TO_REFRESH = 1;
    private static final int PULL_UP_TO_REFRESH = 2;
    /**
     * 是否没有更多数据了
     */
    protected boolean mNoMoreData = false;
    private int mRefreshMode;
    /**
     * 是否正在加载，如果正在加载，则不可以再次加载 *
     */
    private boolean mLoading = false;
    /**
     * 下一次要加载的页面 *
     */
    private int mNextPage = PAGE_FIRST;

    public void requestList() {
        onPullDownToRefresh();
    }

    /**
     * requestList:请求指定页的指定数量的数据列表. <br/>
     *
     * @param page     请求的页面索引
     * @param pageSize 每页展示的数量
     */
    protected abstract void requestList(int page, int pageSize);


    /**
     * getPageSize:得到每页多少条数据. <br/>
     *
     * @return 每页加载的数据条数
     */
    protected int getPageSize() {
        return PAGE_SIZE_DEFAULT;
    }

    /**
     * 下拉刷新
     */
    public void onPullDownToRefresh() {
        Logger.i("wangheng","########loading:" + mLoading);
        if (mLoading) {

            if (mHandler != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getUI() != null) {
                            getUI().onPullDownRefreshCompleted();
                        }
                    }
                }, TIME_MILLIS_OF_DELAY);
            }
            return;
        }
        mLoading = true;
        mRefreshMode = PULL_DOWN_TO_REFRESH;
        if(getUI() != null) {
            FooterManager manager = getUI().getFooterManager();
            if (null != manager) {
                getUI().getFooterManager().hideFooter();
            }
        }
        requestList(PAGE_FIRST, getPageSize());
    }

    /**
     * 上拉刷新
     */
    public void onPullUpRefresh() {
        if (getUI() == null || mLoading) {
            return;
        }

        FooterManager footerManager = getUI().getFooterManager();
        if (mNoMoreData) {
            if (footerManager != null) {
                footerManager.showFooter(true);
            }
            return;
        } else {
            mLoading = true;
            mRefreshMode = PULL_UP_TO_REFRESH;
            if (footerManager != null) {
                footerManager.showFooter(false);
            }
            requestList(mNextPage, getPageSize());
        }
    }

    protected boolean needShowEmptyView() {
        return true;
    }

    /**
     * 请求结果回调 - 无论请求成功还是失败都要调用这个方法,否则,界面可能显示错乱
     *
     * @param page             当前请求页码
     * @param isRequestSuccess 是否请求成功(返回结果有data数据)
     * @param isNoMoreData     是否没有更多数据了
     */
    public void onRefreshCompleted(int page, boolean isRequestSuccess, boolean isNoMoreData) {
        if (isUIDestroyed()) {
            return;
        }

        mNoMoreData = isNoMoreData;
        mLoading = false;

        if (isRequestSuccess) {
            mNextPage = page + 1;
        } else {
            mNextPage = page;
        }

        if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
            getUI().onPullDownRefreshCompleted();
        } else if (mRefreshMode == PULL_UP_TO_REFRESH) {
//            getUI().onPullUpRefreshCompleted();
        }


        FooterManager manager = getUI().getFooterManager();

        if (manager != null) {
            manager.hideFooter();
        }

        BaseAbstractAdapter adapter = getUI().getAdapter();

        if (adapter != null && adapter.getCount() != 0) {
            if (getUI().getEmptyViewManager() != null) {
                getUI().getEmptyViewManager().hideAllEmptyView();
            }
            if (manager != null && mNextPage != PAGE_FIRST
                    && NetworkUtils.isConnected(App.getInstance().getContext())) {
                manager.showFooter(mNoMoreData);
            }
        } else {
            BaseEmptyViewManager emptyViewManager = getUI().getEmptyViewManager();
            if (emptyViewManager != null) {
                emptyViewManager.hideAllEmptyView();
                if (needShowEmptyView()) {
                    if(NetworkUtils.isConnected(App.getInstance().getContext())) {
                        emptyViewManager.showNoDataEmptyView(getActivity());
                    }else{
                        emptyViewManager.showNetworkErrorEmptyView(getActivity());
                    }
                }
            }
        }
    }

    protected boolean isNoMoreData(ArrayList<? extends Serializable> list){
        return list == null || list.size() < 10;
    }

}
