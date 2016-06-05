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
 * Created by hufeiya on 16/6/4.<br />
 * Parse html from Mei Tuan to {@link PersonalInfo} List.
 */
public class MeiTuanParser implements Parser {
    @Override
    public List<PersonalInfo> parse(String html) {
        List<PersonalInfo> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements liElements = document.body().getElementsByTag("dl");
        for(Element li : liElements) {
            Log.d("JdParser",li.text());
            PersonalInfo item = new PersonalInfo();
            Elements divs = li.select("div[class=kv-line]");
            item.setName(divs.get(0).getElementsByTag("p").text());
            item.setPhone(divs.get(1).getElementsByTag("p").text());
            item.setAddress(divs.get(2).getElementsByTag("p").text() + divs.get(3).getElementsByTag("p").text());
            list.add(item);
        }
        return list;
    }
}
