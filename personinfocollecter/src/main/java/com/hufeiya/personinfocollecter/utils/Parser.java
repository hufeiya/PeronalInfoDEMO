package com.hufeiya.personinfocollecter.utils;


import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 */
public interface Parser {
    public List<PersonalInfo> parse(String html);
}