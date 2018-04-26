package com.suirui.srpaas.base.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.suirui.srpaas.base.NetBean;
import com.suirui.srpaas.base.ui.BaseAppManager;
import com.suirui.srpaas.base.util.log.SRLog;
import com.suirui.srpaas.base.util.receiver.BluetoothSetObserver;
import com.suirui.srpaas.base.util.receiver.BluetoothStatusReceiver;
import com.suirui.srpaas.base.util.receiver.HeadSetObserver;
import com.suirui.srpaas.base.util.receiver.HeadStatusReceiver;
import com.suirui.srpaas.base.util.receiver.NetChangeObserver;
import com.suirui.srpaas.base.util.receiver.NetStateReceiver;
import com.suirui.srpaas.base.util.receiver.SensorEventObserver;
import com.suirui.srpaas.base.util.receiver.SensorEventReceiver;

/**
 * Author:cui.li
 * Date: by 2016.1.26
 * Description:基础activity类
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    /**
     * Log tag
     */
    protected static String TAG_LOG = "BaseAppCompatActivity";
    /**
     * 上下文
     */
    protected Context mContext = null;
    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;

    /**
     * 插拔耳机
     */
    protected HeadSetObserver mHeadSetObserver = null;

    protected BluetoothSetObserver mblueBluetoothSetObserver = null;
    SRLog log = new SRLog(TAG_LOG);
    /**
     * 传感器
     */
    private SensorEventObserver mSensorEventObserver = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (toggleOverridePendingTransition()) {
//            switch (getOverridePendingTransitionMode()) {
//                case LEFT:
//                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    break;
//                case RIGHT:
//                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
//                    break;
//                case TOP:
//                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                    break;
//                case BOTTOM:
//                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//                    break;
//                case SCALE:
//                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
//                    break;
//                case FADE:
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    break;
//            }
//        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG_LOG = this.getClass().getSimpleName();
//        SmartBarUtils.hide(getWindow().getDecorView());
//        setTranslucentStatus(true);

        if (isSupportActionBar() && getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        BaseAppManager.getInstance().addActivity(this);
//        if (getContentViewLayoutID() != 0) {
//            setContentView(getContentViewLayoutID());
//        } else {
//            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
//        }
        NetChangeRealization();
        HeadChangeRealization();
        SensorEventRealization();
        BluetoothStatus();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void NetChangeRealization() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetBean netBean) {
                super.onNetConnected(netBean);
                onNetworkConnected(netBean);
                log.E("NetStateReceiver...onNetConnected:");
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
                log.E("NetStateReceiver...onNetDisConnect");
            }
        };
        NetStateReceiver.registerObserver(mNetChangeObserver);
    }


    protected void HeadChangeRealization() {
        mHeadSetObserver = new HeadSetObserver() {

            @Override
            public void onHeadStatus(boolean isHead) {
                super.onHeadStatus(isHead);
                onHeadsetStatus(isHead);
            }
        };
        HeadStatusReceiver.registerObserver(mHeadSetObserver);
    }

    protected void BluetoothStatus() {
        mblueBluetoothSetObserver = new BluetoothSetObserver() {
            @Override
            public void onBluetoothStatus(int staus, BluetoothAdapter mBluetoothAdapter) {
                super.onBluetoothStatus(staus, mBluetoothAdapter);
                onBluetooth(staus, mBluetoothAdapter);
            }
        };
        BluetoothStatusReceiver.registerBluetoothObserver(mblueBluetoothSetObserver);
    }

    protected void SensorEventRealization() {
        mSensorEventObserver = new SensorEventObserver() {
            @Override
            public void onSensorEvent(boolean isonSensorChanged) {
                super.onSensorEvent(isonSensorChanged);
                onSensorEventChange(isonSensorChanged);
            }
        };
        SensorEventReceiver.registerObserver(mSensorEventObserver);
    }

    protected abstract void onSensorEventChange(boolean isonSensorChanged);

    protected abstract void onHeadsetStatus(boolean isHead);

    protected abstract void onBluetooth(int status, BluetoothAdapter mBluetoothAdapter);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        HeadStatusReceiver.removeRegisterObserver(mHeadSetObserver);
        SensorEventReceiver.removeRegisterObserver(mSensorEventObserver);
        BluetoothStatusReceiver.removeRegisterBluetoothObserver(mblueBluetoothSetObserver);
        BaseAppManager.getInstance().removeActivity(this);
//        if (toggleOverridePendingTransition()) {
//            switch (getOverridePendingTransitionMode()) {
//                case LEFT:
//                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                    break;
//                case RIGHT:
//                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
//                    break;
//                case TOP:
//                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                    break;
//                case BOTTOM:
//                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//                    break;
//                case SCALE:
//                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
//                    break;
//                case FADE:
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    break;
//            }
//        }
    }

//    @Override
//    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetChangeObserver = null;
        mHeadSetObserver = null;
        mSensorEventObserver = null;
    }

    /**
     * network connected
     */
    protected abstract void onNetworkConnected(NetBean netBean);

    /**
     * network disconnected
     */
    protected abstract void onNetworkDisConnected();

    //    protected abstract int getContentViewLayoutID();
    protected abstract boolean isSupportActionBar();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /**
     * 屏幕的宽度
     *
     * @return
     */
    public int screenWidth() {
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize.x;
    }

    /**
     * 屏幕的高度
     *
     * @return
     */
    public int screenHeight() {
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize.y;
    }

    /**
     * 获得状态栏高度
     *
     * @return
     */
    public int getStatusbarHeigth() {
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public boolean isNetworkSate() {
        return NetStateReceiver.isNetworkAvailable();
    }

    public NetBean getNetType() {
        return NetStateReceiver.getNetType();
    }
//    /**
//     *
//     * toggle overridePendingTransition
//     *
//     * @return
//     */
//    protected abstract boolean toggleOverridePendingTransition();
//
//    /**
//     * 获取overridePendingTransition 模式
//     * get the overridePendingTransition mode
//     */
//    protected abstract TransitionMode getOverridePendingTransitionMode();

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    /**
     * set status bar translucency
     *
     * @param on
     */
//    protected void setTranslucentStatus(boolean on) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            if (on) {
//                winParams.flags |= bits;
//            } else {
//                winParams.flags &= ~bits;
//            }
//            win.setAttributes(winParams);
//        }
//    }

}
