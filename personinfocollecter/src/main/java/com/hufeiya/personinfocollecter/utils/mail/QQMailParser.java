package com.hufeiya.personinfocollecter.utils.mail;

import android.util.Log;

import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hufeiya on 16-7-20.
 */
public class QQMailParser implements MailParser {

    private static final String TAG = "QQMailParser";

    public String getHref(String html, String keyword) {
        Document document = Jsoup.parse(html);
        return document.select("a:contains(" + keyword + ")").get(0).attr("href");
    }

    public String getHrefById(String html,String id){
        Document document = Jsoup.parse(html);
        return document.getElementById(id).attr("href");
    }

    public String getSidFromURL(String url){
        int start = url.indexOf("sid");
        if(start == -1){
            return null;
        }
        int end = start;
        for(int i = start;i < url.length();i++){
            if(url.charAt(i) == '&'){
                end = i;
                break;
            }
        }
        return url.substring(start+4,end);
    }

}
