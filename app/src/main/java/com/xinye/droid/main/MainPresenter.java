package com.xinye.droid.main;


import com.droid.library.app.mvp.BasePresenter;
import com.droid.library.toast.ToastUtils;
import com.xinye.app.R;
import com.xinye.droid.app.bean.ResponseUpdate;
import com.xinye.droid.app.http.ServiceFactory;
import com.xinye.droid.app.update.UpdateManager;
import com.xinye.droid.user.UserManager;

import retrofit2.Call;

/**
 * 主界面Presenter
 *
 * @author wangheng
 */
public class MainPresenter extends BasePresenter<IMainUI> {

    /**
     * 检查新版本
     */
    public void checkNewVersion() {
        if (UpdateManager.getInstance().isUpdateDownloading()) {
            return;
        }

        Call<ResponseUpdate> call = UpdateManager.getInstance()
            .setFrom(UpdateManager.FROM_MAIN)
            .checkUpdate(getActivity(), new UpdateManager.ICheckUpdateCallback() {

                @Override
                public void onSuccess(ResponseUpdate info) {
                    if (isUIDestroyed()) {
                        return;
                    }

                    if (info != null && info.isRequestSuccess()) {
                        getUI().showUpdateDialog(info);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                }
            });
        addCallToCache(call);
    }
}
