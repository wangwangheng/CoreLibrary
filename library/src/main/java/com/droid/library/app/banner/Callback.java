package com.droid.library.app.banner;

import android.view.View;


/**
 * BannerCallback.
 *
 * @author wangheng
 */
public interface Callback {

    /**
     * onBannerItemClick的Item被点击的时候的回调. <br/>
     *
     * @param itemView itemView
     * @param item item
     * @param position position
     */
    void onBannerItemClick(View itemView, Banner item, int position);

    void onBannerSelected(Banner item, int pageIndex);

}
