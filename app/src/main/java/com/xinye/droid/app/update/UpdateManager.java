package com.xinye.droid.app.update;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.droid.library.app.App;
import com.droid.library.app.BaseActivity;
import com.droid.library.app.storage.file.FileManager;
import com.droid.library.app.storage.sp.SPManagerDefault;
import com.droid.library.dialog.ConfirmTitleDialog;
import com.droid.library.log.Logger;
import com.droid.library.toast.ToastUtils;
import com.droid.library.utils.app.PackageUtils;
import com.xinye.app.R;
import com.xinye.droid.app.bean.ResponseUpdate;
import com.xinye.droid.app.http.ServiceFactory;
import com.xinye.droid.app.http.callback.HttpCallback;

import java.io.File;

import retrofit2.Call;

/**
 * 升级管理器
 *
 * @author wangheng
 */
public class UpdateManager {

    public static final int FROM_MAIN = 1;
    public static final int FROM_SETTING = 2;
    static final String KEY_UPDATE_DOWNLOAD_ID = "keyUpdateDownloadId";
    static final int INVALID_UPDATE_DOWNLOAD_ID = -1;
    private static final String KEY_HAS_NEW_VERSION = "keyHasNewVersion";
    private static final String KEY_DOWNLOADING = "keyDownloading";
    private int mFrom = FROM_MAIN;


    private UpdateManager() {

    }

    public static UpdateManager getInstance() {
        return Generator.INSTANCE;
    }

    public UpdateManager setFrom(int from) {
        this.mFrom = from;
        return getInstance();
    }

    /**
     * 下载
     *
     * @param info 更新信息
     */
    private void download(ResponseUpdate info) {
        DownloadManager dm = (DownloadManager) App.getInstance().getContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(info.getApk()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            dir = App.getInstance().getContext().getExternalCacheDir();
            String path = FileManager.getInstance().getApkSaveDir();
            if (path == null) {

                Toast.makeText(App.getInstance().getContext(),
                        R.string.text_no_sdcard, Toast.LENGTH_LONG).show();
                return;
            }
            dir = new File(path);
            if (!dir.exists()) {
                Toast.makeText(App.getInstance().getContext(),
                        R.string.text_no_sdcard, Toast.LENGTH_LONG).show();
                return;
            }
            Logger.i("wangheng", "###########" + path);
        } else {
            Toast.makeText(App.getInstance().getContext(),
                    R.string.text_no_sdcard, Toast.LENGTH_LONG).show();
            return;
        }

        File file = new File(dir, info.getTitle() + ".apk");
//        if(file.exists()){
//            file.delete();
//        }

//        Logger.i("wangheng",file.getAbsolutePath() + "########file exists :" + file.exists() + "\n" + getMD5(file));
        if (file.exists()) {
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getInstance().getContext().startActivity(installIntent);
            return;
        }

        ToastUtils.toastSystem(R.string.update_downloading,ToastUtils.LENGTH_SHORT);

        request.setDestinationUri(Uri.fromFile(file));

        request.setMimeType("application/vnd.android.package-archive");
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        // 自定义标题和描述
        request.setTitle(App.getInstance().getString(R.string.update_status_bar_title));

        String appName = App.getInstance().getString(R.string.app_name);
        String downloading = App.getInstance().getString(R.string.update_status_bar_description);
        request.setDescription(appName + "V" + info.getTitle() + downloading);

        // 在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，
        // 直到用户点击该Notification或者消除该Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 返回值是系统为当前的下载请求分配的一个唯一的ID，
        // 我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作或者查询
        long downloadId = dm.enqueue(request);
        SPManagerDefault.getInstance().putLong(KEY_UPDATE_DOWNLOAD_ID, downloadId);
        setUpdateDownloading(true);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        App.getInstance().getContext().registerReceiver(new UpdateReceiver(), filter);
    }

    /**
     * 检查新版本
     *
     * @param callback callback
     */
    public Call<ResponseUpdate> checkUpdate(
            final BaseActivity activity,
            final ICheckUpdateCallback callback) {

        if (null == activity || null == callback) {
            return null;
        }

        int versionCode = PackageUtils.getVersionCode(App.getInstance().getContext());

        Call<ResponseUpdate> call = ServiceFactory.getAppService().checkUpdate(
                String.valueOf(versionCode));
        call.enqueue(new HttpCallback<ResponseUpdate>() {
            @Override
            public void onResponse(ResponseUpdate body) {
                if (activity.isActivityDestroyed()) {
                    return;
                }
                if (body == null){
                    SPManagerDefault.getInstance().putBoolean(KEY_HAS_NEW_VERSION, false);
                    callback.onSuccess(null);
                }else if(!body.isRequestSuccess()){
                    SPManagerDefault.getInstance().putBoolean(KEY_HAS_NEW_VERSION, false);
                    callback.onSuccess(body);
                }else{
                    SPManagerDefault.getInstance().putBoolean(KEY_HAS_NEW_VERSION, true);
                    callback.onSuccess(body);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                SPManagerDefault.getInstance().putBoolean(KEY_HAS_NEW_VERSION, false);
                callback.onFailure(t);
            }
        });
        return call;
    }

    public Dialog showUpdateDialog(final BaseActivity activity, final ResponseUpdate info) {
        if (null == info) {
            return null;
        }

        ConfirmTitleDialog dialog = new ConfirmTitleDialog(activity);
        String titleFormat = App.getInstance().getString(R.string.update_dialog_title);
        dialog.setTitle(String.format(titleFormat,info.getTitle()));

        dialog.setMessage(App.getInstance().getString(R.string.update_dialog_message));
        dialog.setLeftButtonText(App.getInstance().getString(R.string.update_dialog_cancel));
        dialog.setRightButtonText(App.getInstance().getString(R.string.update_dialog_commit));
        dialog.setOnButtonClickListener(new ConfirmTitleDialog.OnButtonClickListener() {
            @Override
            public void onLeftButtonClick(ConfirmTitleDialog dialog, View view) {

            }

            @Override
            public void onRightButtonClick(ConfirmTitleDialog dialog, View view) {
                download(info);
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 是否有新版本
     *
     * @return boolean是否有新版本
     */
    public boolean hasNewVersion() {
        return SPManagerDefault.getInstance().getBoolean(KEY_HAS_NEW_VERSION, false);
    }

    /**
     * 是否正在下载非强制更新包
     *
     * @return 是否正在下载非强制更新包
     */
    public boolean isUpdateDownloading() {
        return SPManagerDefault.getInstance().getBoolean(KEY_DOWNLOADING, false);
    }

    /**
     * 设置是否正在下载非强制更新包
     *
     * @param downloading 设置是否正在下载非强制更新包
     */
    void setUpdateDownloading(boolean downloading) {
        SPManagerDefault.getInstance().putBoolean(KEY_DOWNLOADING, downloading);
    }

    public interface ICheckUpdateCallback {
        void onSuccess(ResponseUpdate info);
        void onFailure(Throwable e);
    }

    private static class Generator {
        private static final UpdateManager INSTANCE = new UpdateManager();
    }


}
