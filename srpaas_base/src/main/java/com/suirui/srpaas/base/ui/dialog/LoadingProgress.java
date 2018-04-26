package com.suirui.srpaas.base.ui.dialog;

import android.app.Activity;
import android.content.Context;

/**
 * @authordingna
 * @date2017-06-07
 **/
public class LoadingProgress {

    private static MyProgressDialog loadingDialog;
    private static Activity activity;

    /**
     * 显示加载圈(只有ImageView和TextView两种控件)
     *
     * @param context
     * @param theme
     * @param layoutID
     * @param msg
     * @param tvID
     * @param imgID
     * @param isAnimation（是否支持加载的动画）
     */
    public static void showProgress(Context context, int theme, int layoutID, String msg, String tvID, String imgID, boolean isAnimation) {
        if (context == null)
            return;
        activity = (Activity) context;
        if (activity == null)
            return;
        if (loadingDialog == null)
            loadingDialog = new MyProgressDialog(context, theme, layoutID, msg, tvID, imgID, isAnimation);
        if (loadingDialog != null && !loadingDialog.isShowing() && !activity.isFinishing()) {
            loadingDialog.show();
        }
    }

    public static void dismissProgress() {
        if (loadingDialog == null || activity == null)
            return;
        if (loadingDialog != null && !activity.isFinishing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        activity = null;
    }
}
