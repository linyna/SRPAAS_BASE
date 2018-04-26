/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suirui.srpaas.base.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.suirui.srpaas.base.NetBean;
import com.suirui.srpaas.base.util.log.SRLog;

import java.util.ArrayList;


/**
 * Author:cui.li
 * Date: by 2016.1.26
 * Description:广播接受网络状态类
 */

public class NetStateReceiver extends BroadcastReceiver {
    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.github.obsessive.library.net.conn.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String TAG = NetStateReceiver.class.getSimpleName();
    private static boolean isNetAvailable = false;
    private static NetUtils.NetType mNetType;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;
    SRLog log = new SRLog(NetStateReceiver.class.getName());
    private static NetBean netType;

    public NetStateReceiver() {

    }

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    public static void registerNetworkStateReceiver(Context mContext) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
            filter.addAction(ANDROID_NET_CHANGE_ACTION);
            mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
            }
        }

    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public static NetBean getNetType() {
        return netType;
    }
    public static NetUtils.NetType getAPNType() {
        return mNetType;
    }

    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        log.E("NetStateReceiver...onReceive");
        try {
            mBroadcastReceiver = NetStateReceiver.this;
            if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
                if (!NetUtils.isNetworkAvailable(context)) {
                    isNetAvailable = false;
                } else {
                    isNetAvailable = true;
//                    mNetType = NetUtils.getAPNType(context);
                    netType = NetUtils.getNetType(context);
                }
                log.E("NetStateReceiver...onReceive...isNetAvailable：" + isNetAvailable);
                notifyObserver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(netType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }
}