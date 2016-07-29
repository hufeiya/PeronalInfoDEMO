package com.hufeiya.personinfocollecter.utils.mail;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hufeiya on 16-7-26.
 */
public class NetEaseMailParser {

    public String parseDateToTimeStamp(String json){
        //System.out.print(json);
        Pattern pattern = Pattern.compile("new Date\\(\\d\\d\\d\\d,\\d+,\\d+,\\d+,\\d+,\\d+\\)");
        Matcher matcher = pattern.matcher(json);
        int endPoint = 0;
        String result = "";
        int i  = 0;
        while(matcher.find()){
            String item = matcher.group(i);
            String dateString = item.substring(9,item.length()-1);
            System.out.print("时间：" + dateString);
            SimpleDateFormat format =  new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
            String time = dateString;
            Date date = null;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String timestamp = String.valueOf( date.getTime());
            timestamp = timestamp.substring(0,timestamp.length()-3);
            System.out.print("时间戳:" + timestamp);

            result += json.substring(endPoint,matcher.start(i));
            result += "\"" + timestamp + "\"";
            endPoint = matcher.end(i);
        }
        System.out.print("result: " + result);
        return result;
    }
}
