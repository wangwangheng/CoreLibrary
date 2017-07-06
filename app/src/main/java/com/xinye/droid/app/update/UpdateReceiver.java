package com.xinye.droid.app.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.droid.library.app.App;
import com.droid.library.app.storage.sp.SPManagerDefault;
import com.droid.library.log.Logger;
import com.droid.library.utils.app.DeviceUtils;

import java.io.File;

/**
 * 升级Receiver
 *
 * @author wangheng
 */
public class UpdateReceiver extends BroadcastReceiver {

    private static final long INVALID_DOWNLOAD_ID = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // 下载完成广播
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            UpdateManager.getInstance().setUpdateDownloading(false);
            // 得到DownloadId
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, INVALID_DOWNLOAD_ID);
            long currentId = SPManagerDefault.getInstance().getLong(UpdateManager.KEY_UPDATE_DOWNLOAD_ID
                    , UpdateManager.INVALID_UPDATE_DOWNLOAD_ID);

            if (downloadId == currentId) {
                Logger.i("wangheng", "auto update when download completed");
                installApk(context, downloadId);
            }
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            UpdateManager.getInstance().setUpdateDownloading(false);
            long ids[] = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            if (null == ids) {
                return;
            }
            long currentId = SPManagerDefault.getInstance().getLong(UpdateManager.KEY_UPDATE_DOWNLOAD_ID
                    , UpdateManager.INVALID_UPDATE_DOWNLOAD_ID);
            for (long id : ids) {
                if (id == currentId) {
                    Logger.i("wangheng", "click status bar notification");
                    installApk(context, currentId);
                }
            }
        }
    }

    private void installApk(Context context, long downloadId) {

        DownloadManager dm = (DownloadManager) App.getInstance().getContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = dm.getUriForDownloadedFile(downloadId);
        if (uri == null) {
            return;
        }
        String path = uri.getPath();

        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        // 如果>=7.0,则兼容FileProvider
        if(DeviceUtils.hasN()){
            Uri providerUri = FileProvider.getUriForFile(context.getApplicationContext(),"",new File(path));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(providerUri, "application/vnd.android.package-archive");
        }else{
            installIntent.setDataAndType(Uri.fromFile(new File(path)),
                    "application/vnd.android.package-archive");

        }
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(installIntent);
    }
}
