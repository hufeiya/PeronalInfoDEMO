package com.hufeiya.personinfocollecter.beans;

import java.util.List;

/**
 * Created by hufeiya on 16-7-21.
 */
public class QQMailJson {

    /**
     * id : ZC2614-R417Mp3bOCCncGE2oUtkK67
     * fid : 1
     * subj : 阿里云备案信息公安备案通知
     * abs : 首页 产品服务 备案专区 管理控制台 用户中心 帮助中心 尊敬的用户，您好： 阿里云接到公安部门关于网站
     * date : 1468483318
     * UTC : 1468483318
     * xqqstyle :
     * from : {"uin":"-1810392992","name":"阿里云计算","addr":"system@notice.aliyun.com"}
     * toLst : [{"uin":"-1810392992","name":"a4188","addr":"a4188@qq.com"}]
     * tagLst : []
     */

    private InfBean inf;
    /**
     * stamp : 01
     */

    private IdxBean idx;

    public InfBean getInf() {
        return inf;
    }

    public void setInf(InfBean inf) {
        this.inf = inf;
    }

    public IdxBean getIdx() {
        return idx;
    }

    public void setIdx(IdxBean idx) {
        this.idx = idx;
    }

    public static class InfBean {
        private String id;
        private String fid;
        private String subj;
        private String abs;
        private int date;
        private int UTC;
        private String xqqstyle;
        /**
         * uin : -1810392992
         * name : 阿里云计算
         * addr : system@notice.aliyun.com
         */

        private FromBean from;
        private List<?> tagLst;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getSubj() {
            return subj;
        }

        public void setSubj(String subj) {
            this.subj = subj;
        }

        public String getAbs() {
            return abs;
        }

        public void setAbs(String abs) {
            this.abs = abs;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getUTC() {
            return UTC;
        }

        public void setUTC(int UTC) {
            this.UTC = UTC;
        }

        public String getXqqstyle() {
            return xqqstyle;
        }

        public void setXqqstyle(String xqqstyle) {
            this.xqqstyle = xqqstyle;
        }

        public FromBean getFrom() {
            return from;
        }

        public void setFrom(FromBean from) {
            this.from = from;
        }

        public List<?> getTagLst() {
            return tagLst;
        }

        public void setTagLst(List<?> tagLst) {
            this.tagLst = tagLst;
        }

        public static class FromBean {
            private String uin;
            private String name;
            private String addr;

            public String getUin() {
                return uin;
            }

            public void setUin(String uin) {
                this.uin = uin;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }
        }
    }

    public static class IdxBean {
        private String stamp;

        public String getStamp() {
            return stamp;
        }

        public void setStamp(String stamp) {
            this.stamp = stamp;
        }
    }
}
