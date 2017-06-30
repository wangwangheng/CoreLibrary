package com.droid.library.toast;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.droid.library.app.App;
import com.droid.library.utils.text.StringUtils;
import com.xinye.lib.R;


/**
 * Toast工具类
 *
 * @author wangheng
 */
public class ToastUtils {

    public static final int LENGTH_SHORT = AppMsg.LENGTH_SHORT;
    public static final int LENGTH_LONG = AppMsg.LENGTH_LONG;

    public static final int STYLE_INFO = 1;
    public static final int STYLE_CONFIRM = 2;
    public static final int STYLE_ALERT = 3;

    public static AppMsg toastInfo(Activity activity, @StringRes int stringRes){
        return toast(activity,stringRes,STYLE_INFO);
    }

    public static AppMsg toastConfirm(Activity activity, @StringRes int stringRes){
        return toast(activity,stringRes,STYLE_CONFIRM);
    }

    public static AppMsg toastAlert(Activity activity, @StringRes int stringRes){
        return toast(activity,stringRes,STYLE_ALERT);
    }

    public static AppMsg toastInfo(Activity activity, String stringRes){
        return toast(activity,stringRes,STYLE_INFO);
    }

    public static AppMsg toastConfirm(Activity activity, String stringRes){
        return toast(activity,stringRes,STYLE_CONFIRM);
    }

    public static AppMsg toastAlert(Activity activity, String stringRes){
        return toast(activity,stringRes,STYLE_ALERT);
    }

    public static AppMsg toast(Activity activity, @StringRes int stringRes, @Style int styleType) {
        return toast(activity,App.getInstance().getString(stringRes),styleType);
    }

    public static AppMsg toast(Activity activity, String msg, @Style int styleType) {
        if (null == activity || activity.isFinishing()) {
            return null;
        }

        int colorRes = R.color.color_red_primary;

        if(styleType == STYLE_INFO){
            colorRes = R.color.info;
        }else if(styleType == STYLE_CONFIRM){
            colorRes = R.color.confirm;
        }else{
            colorRes = R.color.alert;
        }

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT,colorRes);
        return toast(activity,msg,style);
    }



    public static AppMsg toast(Activity activity, String msg, AppMsg.Style style){
        if (null == activity || activity.isFinishing() || style == null || StringUtils.isNullOrEmpty(msg)) {
            return null;
        }
        AppMsg appMsg = AppMsg.makeText(activity, msg, style);
        TextView textView = (TextView) appMsg.getView().findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        appMsg.show();
        return appMsg;
    }

    public static void toastSystem(@StringRes int stringRes, @Duration int duration) {
        int d = Toast.LENGTH_SHORT;
        if (duration == LENGTH_LONG) {
            d = Toast.LENGTH_LONG;
        }
        Toast.makeText(App.getInstance().getContext(), stringRes, d).show();
    }

    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @interface Duration {

    }

    @IntDef({STYLE_INFO,STYLE_CONFIRM,STYLE_ALERT})
    public @interface Style{}
}
