package com.biaozhunyuan.tianyi.attendance;

import java.io.Serializable;

/**
 * Created by wam on 2017/5/8.
 */
public class WifiInfo implements Serializable {
    public String SSID;
    public String BSSID;
    public boolean isSelect = false;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WifiInfo)) {
            return false;
        }
        WifiInfo info = (WifiInfo) o;
        return this.BSSID.equals(info.BSSID) && this.SSID.equals(info.SSID);
    }

    @Override
    public int hashCode() {
        return 60;
    }
}
