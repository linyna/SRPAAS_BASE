package com.suirui.srpaas.base.util.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cui.li on 2017/3/20.
 */

public class BluetoothStatusReceiver extends BroadcastReceiver {

    private static BroadcastReceiver mBroadcastReceiver;
    private static ArrayList<BluetoothSetObserver> mbluetoothSetObservers = new ArrayList<BluetoothSetObserver>();
    private static int bluesstatus = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionStr = intent.getAction();
        if (actionStr == null)
            return;
        Log.e("","actionStr........LocalControlModelImpl.."+actionStr);
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
                .equals(actionStr)) {
            bluesstatus = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
            notifyObserver(mBluetoothAdapter);
        }
    }

    private static void notifyObserver(BluetoothAdapter mBluetoothAdapter) {
        if (!mbluetoothSetObservers.isEmpty()) {
            int size = mbluetoothSetObservers.size();
            for (int i = 0; i < size; i++) {
                BluetoothSetObserver observer = mbluetoothSetObservers.get(i);
                if (observer != null) {
                    observer.onBluetoothStatus(bluesstatus, mBluetoothAdapter);
                }
            }
        }
    }

    /**
     * 蓝牙耳机
     */
    private static BluetoothAdapter mBluetoothAdapter = null;

    public static void registerBluetoothSetReceiver(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 获得本机蓝牙适配器对象引用
        if (mBluetoothAdapter == null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);

        mBluetoothAdapter.startDiscovery();
        bluesstatus = mBluetoothAdapter
                .getProfileConnectionState(BluetoothProfile.HEADSET);
        Log.e("","LocalControlModelImpl........bluesstatus:"+bluesstatus);
        notifyObserver(mBluetoothAdapter);

    }


    public static void unRegisterBluetoothSetRReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerBluetoothObserver(BluetoothSetObserver observer) {
        if (mbluetoothSetObservers == null)
            mbluetoothSetObservers = new ArrayList<BluetoothSetObserver>();
        mbluetoothSetObservers.add(observer);
    }

    public static void removeRegisterBluetoothObserver(BluetoothSetObserver observer) {
        if (mbluetoothSetObservers != null) {
            if (mbluetoothSetObservers.contains(observer)) {
                mbluetoothSetObservers.remove(observer);
            }
        }
    }

    public static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (BluetoothStatusReceiver.class) {
                mBroadcastReceiver = new BluetoothStatusReceiver();
            }
        }
        return mBroadcastReceiver;
    }

}
