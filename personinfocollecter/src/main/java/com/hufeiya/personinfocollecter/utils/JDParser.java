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
 * Parse html from Jing Dong to {@link PersonalInfo} List.
 */
public class JDParser implements Parser {
    @Override
    public List<PersonalInfo> parse(String html) {
        List<PersonalInfo> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements liElements = document.select("ul[class=new-mu_l2w]").get(0).getElementsByTag("li");
        for(Element li : liElements) {
            Log.d("JdParser",li.text());
            PersonalInfo item = new PersonalInfo();
            item.setName(li.select("span[class=new-txt]").text());
            item.setPhone(li.select("span[class=new-txt-rd2]").text());
            item.setAddress(li.select("span[class=new-mu_l2cw]").text());
            list.add(item);
        }
        return list;
    }
}
