package com.suirui.srpaas.base.util.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import com.suirui.srpaas.base.util.log.SRLog;

import java.util.ArrayList;


/**
 * Created by cui.li on 2017/2/27.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private final static String ANDROID_PHONE_STATUS_CHANGE_ACTION = "android.intent.action.PHONE_STATE";
    private final static SRLog log = new SRLog(PhoneStateReceiver.class.getName());
    private static ArrayList<PhoneChangeObserver> mphoneStatusObservers = new ArrayList<PhoneChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;
    private TelephonyManager tm = null;

    public static void registerPhoneSetReceiver(Context context) {
        log.E("PhoneStateReceiver...registerPhoneSetReceiver.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_PHONE_STATUS_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void unRegisterPhoneSetReceiver(Context context) {
        log.E("PhoneStateReceiver...unRegisterPhoneSetReceiver");
        if (mBroadcastReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerObserver(PhoneChangeObserver observer) {
        log.E("PhoneStateReceiver...registerObserver.");
        if (mphoneStatusObservers == null)
            mphoneStatusObservers = new ArrayList<PhoneChangeObserver>();
        mphoneStatusObservers.add(observer);
    }

    public static void removeRegisterObserver(PhoneChangeObserver observer) {
        log.E("PhoneStateReceiver...removeRegisterObserver.");
        if (mphoneStatusObservers != null) {
            if (mphoneStatusObservers.contains(observer)) {
                mphoneStatusObservers.remove(observer);
            }
        }
    }

    public static BroadcastReceiver getReceiver() {
        log.E("PhoneStateReceiver...getReceiver.");
        if (null == mBroadcastReceiver) {
            synchronized (PhoneStateReceiver.class) {
                mBroadcastReceiver = new PhoneStateReceiver();
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log.E("PhoneStateReceiver...onReceive.000");
        mBroadcastReceiver = PhoneStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_PHONE_STATUS_CHANGE_ACTION)) {
            log.E("PhoneStateReceiver...onReceive.111");
            // 如果是来电
            tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);

            if (tm != null) {
                notifyObserver(tm.getCallState());
            }


        }
    }

    private void notifyObserver(int phoneStatus) {
        if (!mphoneStatusObservers.isEmpty()) {
            int size = mphoneStatusObservers.size();
            for (int i = 0; i < size; i++) {
                PhoneChangeObserver observer = mphoneStatusObservers.get(i);
                if (observer != null) {
                    log.E("PhoneStateReceiver...notifyObserver.phoneStatus:" + phoneStatus);
                    observer.onPhoneChangeStatus(phoneStatus);
                }
            }
        }
    }
}
