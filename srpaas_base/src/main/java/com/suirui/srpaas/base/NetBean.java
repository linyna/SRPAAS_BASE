package com.suirui.srpaas.base;

import com.suirui.srpaas.base.util.receiver.NetUtils;

/**
 * Created by cui.li on 2017/7/4.
 */

public class NetBean {
    private NetUtils.NetType type;
    private int netType;

    public NetUtils.NetType getType() {
        return type;
    }

    public void setType(NetUtils.NetType type) {
        this.type = type;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }
}
