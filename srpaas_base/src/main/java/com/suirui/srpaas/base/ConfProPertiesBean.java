package com.suirui.srpaas.base;

/**
 * @authordingna
 * @date2017-06-29
 **/
public class ConfProPertiesBean {
    public String respCode;
    public String codeDesc;
    public String url;//配置文件路径
    public String isForce;//是否强制使用，1表示是，0表示否
    public String lastupdatetime;//最新更新时间的毫秒数

    public ConfProPertiesBean(String respCode, String codeDesc, String url, String isForce, String lastupdatetime) {
        this.respCode = respCode;
        this.codeDesc = codeDesc;
        this.url = url;
        this.isForce = isForce;
        this.lastupdatetime = lastupdatetime;
    }

    @Override
    public String toString() {
        return "ConfProPertiesBean{" +
                "respCode='" + respCode + '\'' +
                ", codeDesc='" + codeDesc + '\'' +
                ", url='" + url + '\'' +
                ", isForce='" + isForce + '\'' +
                ", lastupdatetime='" + lastupdatetime + '\'' +
                '}';
    }
}
