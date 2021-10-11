package com.biaozhunyuan.tianyi.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author GaoB
 * @description:
 * @date : 2020/12/15 10:44
 */
public class OldBacklog implements Serializable {


    /**
     * toreturn : true
     * portal_rcap : [{"current_time":"2020-12-15 10:17:26","current_userName":"","flow_id":"印章使用","open_url":"/jsp/module/chapterManager/fw_web.jsp","prev_userName":"李根","record_id":"764f18a8642b86ae000002c0","record_title":"【印章使用】院新版统一办公平台手机APP将通过用户名密...","waitdo_id":"20201215101726972"},{"current_time":"2020-03-10 10:03:17","current_userName":"张文理","flow_id":"科研管理","open_url":"/yzjj/xmysAction.do?method=doCheck&id=","prev_userName":"","record_id":"8a8281916fb32f0500000170c22e92fa00007514","record_title":"【科研管理】院长基金_中国标准化研究院网络安全评估与公...","waitdo_id":"8a8281916fb32f0500000170c22e931a00007517"},{"current_time":"2020-03-10 10:03:17","current_userName":"张文理","flow_id":"科研管理","open_url":"/yzjj/xmysAction.do?method=doCheck&id=","prev_userName":"","record_id":"8a8281916fb32f0500000170c22e932900007518","record_title":"【科研管理】院长基金_中国标准化研究院行政办公平台功能...","waitdo_id":"8a8281916fb32f0500000170c22e93480000751b"},{"current_time":"2020-03-10 10:03:17","current_userName":"张文理","flow_id":"科研管理","open_url":"/yzjj/xmysAction.do?method=doCheck&id=","prev_userName":"","record_id":"8a8281916fb32f0500000170c22e93580000751c","record_title":"【科研管理】院长基金_中国标准化研究院门户网站升级改造...","waitdo_id":"8a8281916fb32f0500000170c22e93770000751f"},{"current_time":"2020-03-10 10:03:17","current_userName":"张文理","flow_id":"科研管理","open_url":"/yzjj/xmysAction.do?method=doCheck&id=","prev_userName":"","record_id":"8a8281916fb32f0500000170c22e937700007520","record_title":"【科研管理】院长基金_中国标准化研究院行政办公平台开发...","waitdo_id":"8a8281916fb32f0500000170c22e939600007523"}]
     * num : 8
     */

    private boolean toreturn;
    private String num;
    private List<PortalRcapBean> portal_rcap;

    public boolean isToreturn() {
        return toreturn;
    }

    public void setToreturn(boolean toreturn) {
        this.toreturn = toreturn;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<PortalRcapBean> getPortal_rcap() {
        return portal_rcap;
    }

    public void setPortal_rcap(List<PortalRcapBean> portal_rcap) {
        this.portal_rcap = portal_rcap;
    }

    public static class PortalRcapBean {
        /**
         * current_time : 2020-12-15 10:17:26
         * current_userName :
         * flow_id : 印章使用
         * open_url : /jsp/module/chapterManager/fw_web.jsp
         * prev_userName : 李根
         * record_id : 764f18a8642b86ae000002c0
         * record_title : 【印章使用】院新版统一办公平台手机APP将通过用户名密...
         * waitdo_id : 20201215101726972
         */

        private String current_time;
        private String current_userName;
        private String flow_id;
        private String open_url;
        private String prev_userName;
        private String record_id;
        private String record_title;
        private String waitdo_id;

        public String getCurrent_time() {
            return current_time;
        }

        public void setCurrent_time(String current_time) {
            this.current_time = current_time;
        }

        public String getCurrent_userName() {
            return current_userName;
        }

        public void setCurrent_userName(String current_userName) {
            this.current_userName = current_userName;
        }

        public String getFlow_id() {
            return flow_id;
        }

        public void setFlow_id(String flow_id) {
            this.flow_id = flow_id;
        }

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public String getPrev_userName() {
            return prev_userName;
        }

        public void setPrev_userName(String prev_userName) {
            this.prev_userName = prev_userName;
        }

        public String getRecord_id() {
            return record_id;
        }

        public void setRecord_id(String record_id) {
            this.record_id = record_id;
        }

        public String getRecord_title() {
            return record_title;
        }

        public void setRecord_title(String record_title) {
            this.record_title = record_title;
        }

        public String getWaitdo_id() {
            return waitdo_id;
        }

        public void setWaitdo_id(String waitdo_id) {
            this.waitdo_id = waitdo_id;
        }
    }
}
