package com.droid.library.widget.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

import com.droid.library.utils.app.DeviceUtils;
import com.droid.library.log.Logger;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * 自定义WebView
 *
 * NOTE:载入URL时加上判断是否destroy和detach的判断的目的是解决下面这个bug:
 *
    <pre>
   java.lang.NullPointerExceptionandroid.webkit.WebViewClassic.loadUrlImpl(WebViewClassic.java:2525)
   android.webkit.WebViewClassic.loadUrlImpl(WebViewClassic.java:2541)
   android.webkit.WebViewClassic.loadUrl(WebViewClassic.java:2534)
   android.webkit.WebView.loadUrl(WebView.java:784)
    </pre>

 【出错原因】
 <pre>
 private void loadUrlImpl(String url, Map<String, String> extraHeaders) {
    switchOutDrawHistory();
    WebViewCore.GetUrlData arg = new WebViewCore.GetUrlData();
    arg.mUrl = url;
    arg.mExtraHeaders = extraHeaders;
    mWebViewCore.sendMessage(EventHub.LOAD_URL, arg);
    clearHelpers();
 </pre>
 出错的地方是在mWebViewCore为空值，它是在webView.destroy()后被销毁的，如果在之后调用了loadurl等方法，
 就会出错。此类问题常见于一些第三方库，比如admob等广告插件，
 也可能在大量频繁使用webview的时候，多线程操作出现。SDK4.1.1，4.2.2都可以重现，而5.1.0（API22）之后就不会报错了，
 因为webview的内核也有变动。

 解决方案:
 <pre>
 @Override
 public void destroy() {
    this.isDestroy = true;
    super.destroy();
 }
 @Override
 public void loadUrl(String url, Map<String, String> extraHeaders) {
    if(this.isDestroy == true){
        return ;
    }
    super.loadUrl(url, extraHeaders);
 }
 </pre>
 *
 * @author wangheng
 */
public class CustomWebView extends ExtendedWebView {


    /**
     * 隐藏方法是否已经加载
     */
    private static boolean sMethodsLoaded = false;

    /**
     * 暂停方法
     */
    private static Method sOnPauseMethod = null;

    /**
     * 恢复方法
     */
    private static Method sOnResumeMethod = null;

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint("NewApi")
    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 如果在自定义控件的构造函数或者其他绘制相关地方使用系统依赖的代码，
        // 会导致可视化编辑器无法识别报错
        if (!isInEditMode()) {
            // 关闭硬件加速，避免白屏问题
            if (DeviceUtils.hasHoneycomb()) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            loadMethods();
            loadSettings();
        }
    }

    /**
     * loadSettings:加载设置. <br/>
     *
     * @author
     */
    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void loadSettings() {
        WebSettings settings = getSettings();
        if (settings != null) {
            // 支持js
            settings.setJavaScriptEnabled(true);
            /**
             * <Pre>
             * 加快HTML网页装载完成的速度
             * 默认情况html代码下载到WebView后，webkit开始解析网页各个节点，发现有外部样式文件或者外部脚本文件时，
             * 会异步发起网络请求下载文件，但如果在这之前也有解析到image节点，那势必也会发起网络请求下载相应的图片
             * 。在网络情况较差的情况下，过多的网络请求就会造成带宽紧张，影响到css或js文件加载完成的时间，造成页面空白loading过久。
             * 解决的方法就是告诉WebView先不要自动加载图片，等页面finish后再发起图片加载。
             * 故在WebView初始化时设置如下代码：
             *  public void int () {
             *   if(Build.VERSION.SDK_INT >= 19) {
             *    webView.getSettings().setLoadsImagesAutomatically(true); }
             *   else {
             *    webView.getSettings().setLoadsImagesAutomatically(false); } }
             *   同时在WebView的WebViewClient实例中的onPageFinished()方法添加如下代码：
             * @Override public void onPageFinished(WebView view, String url) {
             *           if(!webView.getSettings().getLoadsImagesAutomatically()) {
             *           webView.getSettings().setLoadsImagesAutomatically(true); } }
             *    从上面的代码，可以看出我们对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时
             * ,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。
             *
             * <pre>
             */
            // 支持自动加载图片
            // if(DeviceUtils.hasKITKAT()) {
            // settings.setLoadsImagesAutomatically(true);
            // } else {
            // settings.setLoadsImagesAutomatically(false);
            // }
            // 支持通过js打开新的窗口
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            // 设置可以访问文件
            settings.setAllowFileAccess(true);
            // setUseWideViewPort(true)+setLoadWithOverviewMode(true)设置加载进来的页面自适应手机屏幕
            // 无限缩放
            settings.setUseWideViewPort(true);
            // 设置是否以概览模式中的WebView加载的网页，也就是缩小内容由宽度以适合屏幕。
            settings.setLoadWithOverviewMode(true);
            // 设置的WebView是否应该保存表单数据。
            settings.setSaveFormData(true);
            settings.setSavePassword(true);
            // 设置页面默认缩放密度
            settings.setDefaultZoom(ZoomDensity.valueOf(ZoomDensity.MEDIUM
                    .toString()));
            // 设置是否允许定位
            settings.setGeolocationEnabled(true);
            // 告诉web视图可以启用，禁用，或有需求时告诉web视图有插件，不建议使用
            settings.setPluginState(PluginState.valueOf(PluginState.ON
                    .toString()));
            // 接收Cookie
            CookieManager.getInstance().setAcceptCookie(true);
            // 设置的WebView是否应该支持使用其屏幕上的变焦控制和手势缩放。
            settings.setSupportZoom(true);
            if (DeviceUtils.hasHoneycomb()) {
                settings.setDisplayZoomControls(false);
            }
            // WebView是否是否支持多个窗口
            settings.setSupportMultipleWindows(true);
            // 垂直不显示滚动条
            setVerticalScrollBarEnabled(false);
            // 是否支持长按
            setLongClickable(true);
            // 页面不滚动是否隐藏Scrollbar
            setScrollbarFadingEnabled(true);
            // Scrollbar样式
            setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
            // 获取Drawingcache通常会占用一定的内存,关闭
            // setDrawingCacheEnabled(true);

            /*** HTML5 API flags ***/
            // 设置H5缓存
            settings.setAppCacheEnabled(true);
//			settings.setAppCachePath(FileUtils.getCacheDirectory(mContext)
//					.getAbsolutePath());
            // 网页数据缓存
            // 启用获取数据库存储API
            settings.setDatabaseEnabled(true);
//			settings.setDatabasePath(FileUtils.getFileDirectory(mContext)
//					.getAbsolutePath());
            // 设置启用DOM存储API。
            settings.setDomStorageEnabled(true);

        }

    }

    @Override
    public void loadUrl(String url) {
        if(isDetachedFromWindow || isDestroy){
            return;
        }
        url = checkUrl(url);
        // mLoadedUrl = url;
        super.loadUrl(url);
    }

    /**
     * loadRawUrl:加载原始url. <br/>
     * <p/>
     * 一般用于加载js
     * <p/>
     *
     * @param url
     * @author
     */
    public void loadRawUrl(String url) {
        if(isDetachedFromWindow || isDestroy){
            return;
        }
        super.loadUrl(url);
    }

    /**
     * 判断url是否和当前加载url相同
     *
     * @param url
     * @return
     */
    public boolean isSameUrl(String url) {
        if (url != null) {
            return url.equalsIgnoreCase(this.getUrl());
        }

        return false;
    }

    /**
     * checkUrl:检查url.<br/>
     *
     * @param url
     * @return 如有必要，返回修改过的url
     * @author
     */
    public static String checkUrl(String url) {
        if ((url != null) && (url.length() > 0)) {

            if ((!url.startsWith("http://")) && (!url.startsWith("https://"))
                    && (!url.startsWith("file://"))) {

                url = "http://" + url;

            }
        }

        return url;
    }

    /**
     * <Pre>
     * 通过反射执行'onPause'方法,当页面被失去焦点被切换到后台不可见状态，需要执行onPause动作，
     * onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
     * 并且可以减少不必要的CPU和网络开销，可以达到省电、省流量、省资源的效果
     * </Pre>
     */
    public void doOnPause() {
        if (sOnPauseMethod != null) {
            try {

                sOnPauseMethod.invoke(this);

            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("doOnPause(): " + e.getMessage());
            }
        }
        // 需要API11
        // 当应用程序被切换到后台我们使用了webview，
        // 这个方法不仅仅针对当前的webview而是全局的全应用程序的webview，它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        if (DeviceUtils.hasHoneycomb()) {
            pauseTimers();
        }
    }

    /**
     * 通过反射执行'onResume'方法 ,激活WebView为活跃状态，能正常执行网页的响应
     */
    public void doOnResume() {
        if (sOnResumeMethod != null) {
            try {
                sOnResumeMethod.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("doOnResume(): " + e.getMessage());
            }
        }
        // 恢复pauseTimers时的动作
        if (DeviceUtils.hasHoneycomb()) {
            resumeTimers();
        }
    }

    /**
     * 通过反射加载方法
     */
    private void loadMethods() {

        if (!sMethodsLoaded) {

            try {
                sOnPauseMethod = WebView.class.getMethod("onPause");
                sOnResumeMethod = WebView.class.getMethod("onResume");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("loadMethods(): " + e.getMessage());
                sOnPauseMethod = null;
                sOnResumeMethod = null;
            }

            sMethodsLoaded = true;
        }
    }

//	/******************** 屏蔽双击放大功能 ***********************/
//
//	// 判断两次点击的间隔是否小于300ms，如果是，则直接返回，不让webview处理。当然，这样的话webview
//	// 就不能被快速点击了。在移动端，我也想不出什么地方需要快速点击网页的需求
//	long preTouchTime = 0;
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if (ev.getAction() == MotionEvent.ACTION_UP) {
//			long currentTouchTime = System.currentTimeMillis();
//			if (currentTouchTime - preTouchTime <= ViewConfiguration
//					.getDoubleTapTimeout()) {
//				preTouchTime = currentTouchTime;
//				return true;
//			}
//			preTouchTime = currentTouchTime;
//		}
//		return super.dispatchTouchEvent(ev);
//	}

    // 初始值一定要上false....,否则可能出现bug
    private boolean isDetachedFromWindow = false;

    @Override
    protected void onAttachedToWindow() {
        isDetachedFromWindow = false;
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        isDetachedFromWindow = true;
        super.onDetachedFromWindow();
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if(isDetachedFromWindow || isDestroy){
            return;
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        if(isDetachedFromWindow || isDestroy){
            return;
        }
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    // 初始值一定要是false
    private boolean isDestroy = false;

    @Override
    public void destroy() {
        isDestroy = true;
        super.destroy();
    }
}
