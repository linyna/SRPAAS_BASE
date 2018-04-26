package com.suirui.srpaas.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suirui.srpaas.base.util.CommonUtils;

/**
 * @authordingna
 * @date2017-06-09
 **/
public class MyProgressDialog extends Dialog {


    private Context mContext;
    private int layoutID;
    private String msg = "";
    private String tvID, imgID;
    private boolean isAnimation = true;

    public MyProgressDialog(Context context, int theme, int layoutID, String msg, String tvID, String imgID, boolean isAnimation) {
        super(context, theme);
        this.mContext = context;
        this.layoutID = layoutID;
        this.msg = msg;
        this.tvID = tvID;
        this.imgID = imgID;
        this.isAnimation = isAnimation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(layoutID, null);
        setContentView(view);
        int tv_id_ = mContext.getResources().getIdentifier(tvID, "id", mContext.getPackageName());
        TextView hj_btn_loading = (TextView) view.findViewById(tv_id_);
        hj_btn_loading.setText(msg);
        int img_id_ = mContext.getResources().getIdentifier(imgID, "id", mContext.getPackageName());
        ImageView hj_btn_load_img = (ImageView) view.findViewById(img_id_);
        if (isAnimation) {
            hj_btn_load_img.setVisibility(View.VISIBLE);
            CommonUtils.startLoadImage(hj_btn_load_img);
        } else {
            hj_btn_load_img.setVisibility(View.GONE);
        }
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }
}
