package com.droid.library.thirdplatform.imageloader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.droid.library.app.App;
import com.droid.library.thirdplatform.IThirdPlatformManager;
import com.droid.library.utils.file.FileSize;
import com.droid.library.utils.app.DeviceUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;

/**
 * PartyImageLoader，对ImageLoader的简单封装
 *
 * @author wangheng
 */
public class PartyImageLoader implements IThirdPlatformManager {

    /**
     * 无效的资源id *
     */
    public static final int INVALID_RES_ID = -1;

    /**
     * 低内存模式 *
     */
    public static final int LOW_MEMERY = 1;

    /**
     * 中内存模式 *
     */
    public static final int MIDDLE_MEMERY = 2;

    /**
     * 高内存模式 *
     */
    public static final int HIGHT_MEMERY = 3;

    /**
     * 默认的显示选项 *
     */
    private DisplayImageOptions defaultImageOptions;
    private ImageLoaderConfiguration mConfiguration;

    private PartyImageLoader() {

    }

    /**
     * getInstance:得到GuamuImageLoader的实例. <br/>
     *
     * @return ReboImageLoader实例
     */
    public static PartyImageLoader getInstance() {
        return Singleton.INSTANCE;
    }

    private static long getDirSize(File file) {
        if (file == null) {
            return 0;
        }
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                if (children == null) {
                    return size;
                }
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * onCreateExecute:ImageLoader初始化. <br/>
     */
    @Override
    public void init() {
        if (ImageLoader.getInstance().isInited()) {
            return;
        }

        ImageLoaderConfiguration.Builder builder;

        int level = getAppMemoryLevel();
        if (LOW_MEMERY == level) {
            builder =
                    new ImageLoaderConfiguration.Builder(App.getInstance().getContext())
                            .defaultDisplayImageOptions(getImageOptions(INVALID_RES_ID))
                            .threadPriority(Thread.MIN_PRIORITY)
                            .threadPoolSize(2)
                            .diskCacheSize(30 * 1024 * 1024)
                            .diskCacheFileCount(100)
                            .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                            .diskCacheExtraOptions(480, 320, null)
                            .tasksProcessingOrder(QueueProcessingType.LIFO);
        } else if (MIDDLE_MEMERY == level) {
            builder =
                    new ImageLoaderConfiguration.Builder(App.getInstance().getContext())
                            .defaultDisplayImageOptions(getImageOptions(INVALID_RES_ID))
                            .threadPriority(Thread.NORM_PRIORITY - 2)
                            .threadPoolSize(3)
                            .memoryCacheSize(2 * 1024 * 1024)
                            .diskCacheSize(30 * 1024 * 1024)
                            .diskCacheFileCount(100)
                            .diskCacheFileNameGenerator(
                                    new HashCodeFileNameGenerator())
                            .tasksProcessingOrder(QueueProcessingType.LIFO);
        } else {
            long availableMemory = Runtime.getRuntime().maxMemory();
            builder =
                    new ImageLoaderConfiguration.Builder(App.getInstance().getContext())
                            .defaultDisplayImageOptions(getImageOptions(INVALID_RES_ID))
                            .threadPriority(Thread.NORM_PRIORITY)
                            .threadPoolSize(5)
                            .memoryCacheSize((int) (availableMemory / 8))
                            .memoryCacheSizePercentage(20)
                            .diskCacheSize(50 * 1024 * 1024)
                            .diskCacheFileCount(150)
                            .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                            .tasksProcessingOrder(QueueProcessingType.LIFO);
        }

        if (App.getInstance().isDebug()) {
            builder.writeDebugLogs(); // Remove for release app
        }
        ImageLoaderConfiguration config = builder.build();
        mConfiguration = config;
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().handleSlowNetwork(true);

    }

    public void clearDiskCache() {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        ImageLoader.getInstance().clearDiskCache();
    }

    /**
     * 得到磁盘缓存目录总大小 bytes
     *
     * @return
     */
    public long getDiskCacheSizeWithByte() {
        File file = StorageUtils.getCacheDirectory(App.getInstance().getContext());
        return getDirSize(file);
    }

    public void loadImage(String url, ImageSize size, ImageLoadingListener listener) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        ImageLoader.getInstance().loadImage(url, size, listener);
    }

    public ImageLoaderConfiguration getImageLoaderConfiguration() {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        return mConfiguration;
    }

    public File getDiskCachedFile(String url) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        if (mConfiguration == null) {
            init();
        }
        if (mConfiguration == null || mConfiguration.getDiskCache() == null) {
            return null;
        }
        return mConfiguration.getDiskCache().get(url);
    }

    /**
     * 取消掉在ImageView上的任务
     *
     * @param imageView imaveView
     */
    public void cancelDisplayTask(ImageView imageView) {
        if (null == imageView) {
            return;
        }
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        ImageLoader.getInstance().cancelDisplayTask(imageView);

    }

    /**
     * 取消掉在ImageAware上的任务
     *
     * @param imageAware imageAware
     */
    public void cancelDisplayTask(ImageAware imageAware) {
        if (null == imageAware) {
            return;
        }
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        ImageLoader.getInstance().cancelDisplayTask(imageAware);

    }

    /**
     * display:显示图片. <br/>
     *
     * @param imageView  要加载图片的ImageView
     * @param drawableId 要加载的Drawable的id
     */
    public void display(ImageView imageView, int drawableId) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }

        if (imageView == null) {
            return;
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage("drawable://" + drawableId, imageView, options);
    }

    /**
     * display:显示图片. <br/>
     *
     * @param imageView  要加载图片的ImageView
     * @param drawableId 要加载的Drawable的id
     * @param options    图片显示选项
     */
    public void display(ImageView imageView, int drawableId, DisplayImageOptions options) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        if (imageView == null) {
            return;
        }
        ImageLoader.getInstance().displayImage("drawable://" + drawableId, imageView, options);
    }

    public void display(ImageView imageView, File file) {
        display(imageView, file, INVALID_RES_ID);
    }

    public void display(ImageView imageView, File file, int resId) {
        if (file == null) {
            return;
        }
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        display(imageView, "file://" + file.getAbsolutePath(), resId);
    }
    public void display(ImageView imageView, File file, DisplayImageOptions options) {
        if (file == null) {
            return;
        }
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        display(imageView, "file://" + file.getAbsolutePath(), options);
    }

    public void display(ImageView imageView, String uri) {
        display(imageView, uri, getImageOptions(INVALID_RES_ID), null, null);
    }

    public void display(ImageView imageView, String uri, DisplayImageOptions options) {
        display(imageView, uri, options, null, null);
    }

    public void display(ImageView imageView, String uri, int resId) {
        display(imageView, uri, getImageOptions(resId), null, null);
    }

    public void display(ImageView imageView, String uri, int loadingResId, int failResId, int emptyUriResId) {
        DisplayImageOptions options = getOptionsBuilder(loadingResId, failResId, emptyUriResId).build();
        display(imageView, uri, options, null, null);
    }

    /**
     * getImageOptions:得到默认的显示选项. <br/>
     *
     * @return 图片显示Options
     */
    public DisplayImageOptions getImageOptions(int resId) {
        DisplayImageOptions options;
        switch (resId) {
            case INVALID_RES_ID: {
                if (defaultImageOptions == null) {
                    defaultImageOptions = getOptionsBuilder(INVALID_RES_ID).build();
                }
                options = defaultImageOptions;
            }
            break;
            default: {
                options = getOptionsBuilder(resId).build();
            }
            break;
        }

        return options;
    }

    /**
     * getOptionsBuilder:得到公共的图片加载选项构建器 <br/>
     *
     * @param resId res
     * @return DisplayImageOptions.Builder
     */
    public DisplayImageOptions.Builder getOptionsBuilder(int resId) {
        return getOptionsBuilder(resId, resId, resId);
    }

    public DisplayImageOptions.Builder getOptionsBuilder(int loadingResId, int failResId, int emptyUriResId) {
        int level = getAppMemoryLevel();
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().cacheOnDisk(true);

        if (loadingResId != INVALID_RES_ID) {
            builder.showImageOnLoading(loadingResId);
        }

        if (failResId != INVALID_RES_ID) {
            builder.showImageOnFail(failResId);
        }

        if (emptyUriResId != INVALID_RES_ID) {
            builder.showImageForEmptyUri(emptyUriResId);
        }

        if (LOW_MEMERY == level) {
            return builder.imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).bitmapConfig(Bitmap.Config.RGB_565);
        } else if (MIDDLE_MEMERY == level) {
            return builder.cacheInMemory(true).resetViewBeforeLoading(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.RGB_565);
        } else {
            return builder.cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).resetViewBeforeLoading(true)
                    // .displayer(new FadeInBitmapDisplayer(300))
                    .bitmapConfig(Bitmap.Config.ARGB_8888);
        }
    }

    public DisplayImageOptions getRoundedOptions(int loadingResId, int failResId, int emptyUriResId, int cornerRadiusPixels) {
        Builder builder = getOptionsBuilder(loadingResId, failResId, emptyUriResId)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels));
        return builder.build();
    }

    public DisplayImageOptions getRoundedOptions(int drawableId, int cornerRadiusPixels) {
        Builder builder = getOptionsBuilder(drawableId)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels));

        return builder.build();
    }

    public void display(ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        if (imageView == null) {
            return;
        }
        ImageLoader.getInstance().displayImage(url, imageView, options, loadingListener, progressListener);
    }

    public void display(ImageView imageView, String url, ImageLoadingListener loadingListener) {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
        if (imageView == null) {
            return;
        }
        ImageLoader.getInstance().displayImage(url, imageView, loadingListener);
    }

    /**
     * 获取APP内存模式
     *
     * @return 内存界别
     */
    private int getAppMemoryLevel() {
        ActivityManager activityManager = (ActivityManager) App.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        int avaialbeMemory = activityManager.getMemoryClass();
        long avaialbeMemorySize = activityManager.getMemoryClass() * FileSize.SIZE_KB;
        long systemAvaialbeMemorySize = DeviceUtils.getSystemAvaialbeMemory(App.getInstance().getContext());
        long systemAvaialbeMemory = systemAvaialbeMemorySize / FileSize.SIZE_KB;
        float ratio = (float) avaialbeMemorySize / (float) systemAvaialbeMemorySize;
        float proportion = (float) 1 / (float) 3;
        Log.d("MEMORY", "memorySize::" + avaialbeMemory + "  systemAvaialbeMemorySize::" + systemAvaialbeMemory + "  ratio::"
                + ratio);
        if (ratio > proportion && systemAvaialbeMemory < 512 && avaialbeMemory < 96) {
            Log.d("MEMORY", "LOW_MEMERY");
            return LOW_MEMERY;
        } else if (ratio <= proportion && systemAvaialbeMemory > 512 && avaialbeMemory > 96) {
            Log.d("MEMORY", "HIGHT_MEMERY");
            return HIGHT_MEMERY;
        }
        Log.d("MEMORY", "MIDDLE_MEMERY");
        return MIDDLE_MEMERY;
    }

    /**
     * 单例生成器.
     *
     * @author wangheng
     */
    private static final class Singleton {
        private static final PartyImageLoader INSTANCE = new PartyImageLoader();
    }
}

