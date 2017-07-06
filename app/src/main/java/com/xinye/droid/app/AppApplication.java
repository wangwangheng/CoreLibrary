package com.xinye.droid.app;

import android.app.Application;

import com.droid.library.app.LibraryConfig;
import com.droid.library.app.storage.file.FileManager;
import com.droid.library.log.Logger;
import com.droid.library.thirdplatform.gson.GsonManager;
import com.droid.library.utils.BugFixedUtils;


public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LibraryConfig.getInstance()
                .setDebug(true)
                .setAppContext(getApplicationContext())
                .config();

        initApplication();
    }

    private void initApplication() {

        Logger.i("wangheng","#############Downloader init start:" + System.currentTimeMillis());
        // Gson
        GsonManager.getInstance().init();

        // 解决MediaSessionLegacyHelper单例导致的内存泄漏问题
        BugFixedUtils.fixedMediaSessionLegacyHelperMemoryLeak();
        // 初始化文件管理器
        FileManager.getInstance().init();
    }
}
