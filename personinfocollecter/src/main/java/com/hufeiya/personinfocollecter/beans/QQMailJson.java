package com.hufeiya.personinfocollecter.beans;

import java.util.List;

/**
 * Created by hufeiya on 16-7-21.
 */
public class QQMailJson {

    /**
     * id : ZC1615-mFKwJIt1Ijq9MyJ1zTAoN67
     * fid : 1
     * subj : 阿迪气质明星款，穿上正当时（AD）
     * abs : 作为运动品牌界的“科技公司”，耐克和阿迪达斯时不时都会推出一些与前沿科技结合的新品。为了让定制跑鞋
     * date : 1468530401
     * UTC : 1468530401
     * xqqstyle :
     * from : {"uin":"-1810392992","name":"阿迪达斯NMD","addr":"winter@poster.addysmall.com"}
     * toLst : [{"uin":"-1810392992","name":"a4188","addr":"a4188@qq.com"}]
     * tagLst : []
     */

    private String id;
    private String fid;
    private String subj;
    private String abs;
    private int date;
    private int UTC;
    private String xqqstyle;
    /**
     * uin : -1810392992
     * name : 阿迪达斯NMD
     * addr : winter@poster.addysmall.com
     */

    private FromBean from;
    /**
     * uin : -1810392992
     * name : a4188
     * addr : a4188@qq.com
     */

    private List<ToLstBean> toLst;
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

    public List<ToLstBean> getToLst() {
        return toLst;
    }

    public void setToLst(List<ToLstBean> toLst) {
        this.toLst = toLst;
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

    public static class ToLstBean {
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
