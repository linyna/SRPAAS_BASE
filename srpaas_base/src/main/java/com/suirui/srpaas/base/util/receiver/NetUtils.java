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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.suirui.srpaas.base.NetBean;

import java.util.Locale;


/**
 * Author:cui.li
 * Date: by 2016.1.26
 * Description:网络状态工具类
 */
public class NetUtils {

    public static NetType getAPNType(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                return NetType.NONE;
            }
            int nType = networkInfo.getType();

            if (nType == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    return NetType.CMNET;
                } else {
                    return NetType.CMWAP;
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                return NetType.WIFI;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NetType.NONE;
    }

    /**
     * 网络类型
     *
     * @param context
     * @return
     */
    public static NetBean getNetType(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        NetBean netBean = new NetBean();//默认是没有连上的
        netBean.setType(NetType.NONE);
        netBean.setNetType(Net_Type.SR_NETWORK_TYPE_NONE.getValue());

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Log.e("", "NetUtils.....网络:isConnect....SubtypeName: "
                                + info.getSubtypeName() + " Subtype: "
                                + info.getSubtype() + " Type: "
                                + info.getType() + " TypeName: "
                                + info.getTypeName());
                        String netTypeString = "wifi";
                        int nType = info.getType();
                        if (nType == ConnectivityManager.TYPE_WIFI) {//wifi
                            netBean.setType(NetType.WIFI);
                            netBean.setNetType(Net_Type.SR_NETWORK_TYPE_WIFI.getValue());
                        } else {
                            if (nType == ConnectivityManager.TYPE_MOBILE) {
                                if (info.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
                                    netBean.setType(NetType.CMNET);
                                } else {
                                    netBean.setType(NetType.CMWAP);
                                }
                            }
                            int type = isFastMobileNetwork(context);
                            int netType = Net_Type.SR_NETWORK_TYPE_NONE.getValue();
                            switch (getSimType(context)) {
                                case 1:// "中国移动";
                                    if (type == 2) {
                                        netTypeString = "中国移动...2G";
                                        netType = Net_Type.SR_NETWORK_TYPE_2G.getValue();
                                    } else if (type == 3) {
                                        netTypeString = "中国移动...3G";
                                        netType = Net_Type.SR_NETWORK_TYPE_3G_TD.getValue();
                                    } else if (type == 4) {
                                        netTypeString = "中国移动...4G";
                                        netType = Net_Type.SR_NETWORK_TYPE_4G_TDD.getValue();
                                    }
                                    break;
                                case 2:// 联通
                                    if (type == 2) {
                                        netTypeString = "联通...2G";
                                        netType = Net_Type.SR_NETWORK_TYPE_2G.getValue();
                                    } else if (type == 3) {
                                        netTypeString = "联通...3G";
                                        netType = Net_Type.SR_NETWORK_TYPE_3G_WCDMA.getValue();
                                    } else if (type == 4) {
                                        netTypeString = "联通...4G";
                                        netType = Net_Type.SR_NETWORK_TYPE_4G_FDD.getValue();
                                    }
                                    break;
                                case 3:// 电信
                                    if (type == 2) {
                                        netTypeString = "电信...2G";
                                        netType = Net_Type.SR_NETWORK_TYPE_2G.getValue();
                                    } else if (type == 3) {
                                        netTypeString = "电信...3G";
                                        netType = Net_Type.SR_NETWORK_TYPE_3G_CDMA2000.getValue();
                                    } else if (type == 4) {
                                        netTypeString = "电信...4G";
                                        netType = Net_Type.SR_NETWORK_TYPE_4G_FDD.getValue();
                                    }
                                    break;
                            }
                            Log.e("", "NetUtils.....isFastMobileNetwork+.....netType:" + netType + " netTypeString:" + netTypeString);
                            netBean.setNetType(netType);
                        }
                    }
                }
            }
            return netBean;
        } catch (Exception e) {
            Log.e("", e.toString());
            e.printStackTrace();
        }
        return netBean;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netnfo = mgr.getActiveNetworkInfo();
            if (netnfo != null) {
                return netnfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    return mWiFiNetworkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    return mMobileNetworkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                    return mNetworkInfo.getType();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            /***** 2G *****************/
            case TelephonyManager.NETWORK_TYPE_EDGE:// 2
                return 2; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:// 1
                return 2; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:// 4
                return 2; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_IDEN:// 11
                return 2; // ~25 kbps

            /***** 电信3G ******************/
            case TelephonyManager.NETWORK_TYPE_EVDO_0:// 5
                return 3; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:// 6
                return 3; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:// 12
                return 3; // ~ 5 Mbps

            /***** 中国联通 3g *************************/
            case TelephonyManager.NETWORK_TYPE_UMTS:// 3 联通3G
                return 3; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:// 15
                return 3; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 3;
            case TelephonyManager.NETWORK_TYPE_HSUPA:// 9
                return 3; // ~ 1-23 Mbps
            /******** 中国移动 3g ****************************/
            case 17:// 移动3G
                return 3;
            case TelephonyManager.NETWORK_TYPE_HSDPA:// 8
                return 3; // ~ 2-14 Mbps

            /*** 4G ********************/
            case TelephonyManager.NETWORK_TYPE_LTE:// 13
                return 4; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:// 14
                return 4; // ~ 1-2 Mbps

            default:
                return 3;
        }
    }

    public static int getSimType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        String typeString = "";
        int type = 1;
        if (operator != null) {

            if (operator.equals("46000") || operator.equals("46002")
                    || operator.equals("46007")) {
                // 中国移动
                typeString = "中国移动";
                type = 1;

            } else if (operator.equals("46001")) {

                // 中国联通
                typeString = "中国联通";
                type = 2;

            } else if (operator.equals("46003")) {
                typeString = "中国电信";
                // 中国电信
                type = 3;

            }
        }
        Log.e("", "NetUtils.....isFastMobileNetwork....." + telephonyManager.getNetworkType()
                + "  : " + operator + "  typeString:" + typeString);
        return type;
    }


    public static enum NetType {
        WIFI, CMNET, CMWAP, NONE,
    }

    //网络类型
    public enum Net_Type {
        SR_NETWORK_TYPE_NONE(0),
        SR_NETWORK_TYPE_WIRE(1),
        SR_NETWORK_TYPE_VPN(2),
        SR_NETWORK_TYPE_ADSL(3),
        SR_NETWORK_TYPE_WIFI(4),
        SR_NETWORK_TYPE_2G(5),
        SR_NETWORK_TYPE_3G_TD(6),
        SR_NETWORK_TYPE_3G_WCDMA(7),
        SR_NETWORK_TYPE_3G_CDMA2000(8),
        SR_NETWORK_TYPE_4G_TDD(9),
        SR_NETWORK_TYPE_4G_FDD(10);
        private int type;

        private Net_Type(int type) {
            this.type = type;
        }

        public int getValue() {
            return type;
        }
    }
}
