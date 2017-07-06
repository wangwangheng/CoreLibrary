package com.droid.library.app.banner;

import android.view.View;
import android.view.View.OnClickListener;


/**
 * Banner的OnClick事件，需要提供点击的Banner.
 * @author wangheng
 */
class BannerOnClickListener implements OnClickListener {

    private Banner banner;
    private Callback callback;
    private int position;

    public BannerOnClickListener(Callback callback, Banner banner, int realPosition) {
        this.callback = callback;
        this.banner = banner;
        this.position = realPosition;
    }

    @Override
    public void onClick(View v) {
        if (banner != null && callback != null) {
            callback.onBannerItemClick(v, banner,position);
        }
    }
}