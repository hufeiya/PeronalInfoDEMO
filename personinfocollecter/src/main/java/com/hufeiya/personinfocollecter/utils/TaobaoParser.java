package com.hufeiya.personinfocollecter.utils;

import android.util.Log;


import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 */
public class TaobaoParser implements Parser {
    @Override
    public List<PersonalInfo> parse(String html) {
        List<PersonalInfo> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements liElements = document.getElementById("addressList").getElementsByTag("li");
        for(Element li : liElements) {
            PersonalInfo item = new PersonalInfo();
            item.setName(li.select("label[name=user-name]").text());
            item.setPhone(li.select("label[name=phone-num]").text());
            item.setAddress(li.select("label[name=address]").text());
            list.add(item);
        }
        return list;
    }
}
