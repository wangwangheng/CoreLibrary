package com.droid.library.app;

import android.content.Context;

import com.nostra13.universalimageloader.utils.L;

/**
 * libs的配置类.
 *
 * @author wangheng
 */
public class LibraryConfig {

    private Context mAppContext;

    private LibraryConfig() {

    }

    /**
     * 仅仅为了单例而存在.
     *
     * @author wangheng
     */
    private static final class Singleton {
        private static final LibraryConfig INSTANCE = new LibraryConfig();
    }

    /**
     * getInstance:得到Configuration的单例对象. <br/>
     *
     * @return 得到単例
     */
    public static LibraryConfig getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 是否是Debug模式
     **/
    private boolean isDebug = true;

    public boolean isDebug() {
        return isDebug;
    }


    public LibraryConfig setDebug(boolean isDebug) {
        this.isDebug = isDebug;


        return LibraryConfig.this;

    }

    public Context getAppContext() {
        return mAppContext;
    }

    public LibraryConfig setAppContext(Context appContext) {
        this.mAppContext = appContext;
        return LibraryConfig.this;
    }

    public LibraryConfig config() {
        L.writeLogs(isDebug);
        return LibraryConfig.this;
    }

}
