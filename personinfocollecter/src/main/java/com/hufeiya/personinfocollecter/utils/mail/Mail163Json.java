package com.hufeiya.personinfocollecter.utils.mail;

import java.util.Date;

/**
 * Created by hufeiya on 16-7-26.
 */
public class Mail163Json {

    /**
     * id : 62:1tbiPhxOalXlfuEaaAAAsb
     * fid : 1
     * size : 4446
     * from : "chenzhengrong" <notifications@github.com>
     * to : "hufeiya/fuckCancer" <fuckCancer@noreply.github.com>
     * subject : [hufeiya/fuckCancer] ready 里面 语法错误 (#1)
     * priority : 3
     * backgroundColor : 0
     * antiVirusStatus : novirus
     * label0 : 0
     * flags : {}
     * hmid : <hufeiya/fuckCancer/issues/1@github.com>
     */

    private String id;
    private int fid;
    private int size;
    private String from;
    private String to;
    private String subject;
    private String sentDate;
    private String receivedDate;
    private int priority;
    private int backgroundColor;
    private int label0;
    private String hmid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getLabel0() {
        return label0;
    }

    public void setLabel0(int label0) {
        this.label0 = label0;
    }

    public String getHmid() {
        return hmid;
    }

    public void setHmid(String hmid) {
        this.hmid = hmid;
    }

}