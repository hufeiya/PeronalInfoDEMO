package com.hufeiya.personinfocollecter.utils;


import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import java.util.List;

/**
 * Created by hufeiya on 16/6/4.<br />
 * Parse html from target website to {@link PersonalInfo} List.
 */
public interface Parser {

    /**
     * Parse html from target website to {@link PersonalInfo} List.
     * @param html The html contained personal information from target websites.
     * @return The {@link PersonalInfo} list
     */
    public List<PersonalInfo> parse(String html);
}
