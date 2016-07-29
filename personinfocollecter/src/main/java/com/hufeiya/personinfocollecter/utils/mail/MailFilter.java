package com.hufeiya.personinfocollecter.utils.mail;

/**
 * Created by hufeiya on 16-7-26.
 */
public class MailFilter {
    private static String[] targetMailList = new String[]{"ccsvc@message.cmbchina.com",
            "citiccard@citiccard.com",
            "PersonalService@bank-of-china.com"};

    public static boolean isInMailList(String mailAddress){
        for(int i = 0;i < targetMailList.length;i++){
            if(targetMailList[i].equals(mailAddress)){
                return true;
            }
        }
        return false;
    }
}
