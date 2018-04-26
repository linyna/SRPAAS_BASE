package com.suirui.srpaas.base.test;

import android.app.Activity;
import android.os.Bundle;

import com.suirui.srpaas.base.util.BaseUtil;
import com.suirui.srpaas.base.util.log.LogToFile;
import com.suirui.srpaas.base.util.log.SRLog;
import com.suirui.srpaas.base.util.receiver.PhoneChangeObserver;
import com.suirui.srpaas.base.util.receiver.PhoneStateReceiver;

public class BaseDemoActivity extends Activity {
    private String TAG = "BaseDemoActivity.this";
    private SRLog log=new SRLog("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo);
        LogToFile.init(BaseUtil.getSDPath()+"/logs/ui");
//        NeStateReceiver.registerNetworkStateReceiver(this);//注册网络状态

        for(int i=0;i<10;i++){
            LogToFile.e(TAG, "LogToFile ,...e..."+i);
            LogToFile.w(TAG, "LogToFile ,...w..."+i);
        }

        SRLog.DebugType.setSRLog(false);
    }

    private PhoneChangeObserver mPhoneSetObserver = null;

    protected void registerPhoneRegister() {
        PhoneStateReceiver.registerPhoneSetReceiver(this);
        mPhoneSetObserver = new PhoneChangeObserver() {
            @Override
            public void onPhoneChangeStatus(int status) {
                super.onPhoneChangeStatus(status);
            }
        };
        PhoneStateReceiver.registerObserver(mPhoneSetObserver);
    }

    private void removePhoneRegister() {
        if (mPhoneSetObserver != null) {
            PhoneStateReceiver.removeRegisterObserver(mPhoneSetObserver);
        }
    }

}
