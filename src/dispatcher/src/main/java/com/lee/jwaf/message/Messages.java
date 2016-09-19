/**
 * Project Name : jwaf-dispatcher <br>
 * File Name : Messages.java <br>
 * Package Name : com.lee.jwaf.message <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lee.jwaf.context.ActionContext;
import com.lee.jwaf.exception.AppException;
import com.lee.util.StringUtils;

/**
 * ClassName : Messages <br>
 * Description : International Message manager <br>
 * Create Time : 2016-09-19 <br>
 * Create by : jimmyblylee@126.com
 */
public enum Messages {
    msg;

    private Map<String, Map<String, ResourceBundle>> resources = new HashMap<>();
    private Logger log = LoggerFactory.getLogger(getClass());

    private String getMsgByLocale(String feature, String code, Locale locale) throws AppException {
        log.trace("getting message with feature {} and code {} and locale {}", feature, code, locale.toString());
        createBundleIfNecessary(feature, code, locale);
        try {
            return resources.get(feature).get(locale.toString()).getString(code);
        } catch (MissingResourceException e) {
            throw new AppException("ERR_FRAMEWORK_002", "Can not find {} in {}-messages*.properties by locale {}", code, feature, locale.toString());
        }
    }

    private void createBundleIfNecessary(String feature, String code, Locale locale) throws AppException {
        if (locale == null || StringUtils.isEmpty(feature) || StringUtils.isEmpty(
                code)) { throw new AppException("ERR_FRAMEWORK_002", "Parameters of feature, Code and local should Not be null", feature, code, locale); }
        // fast return
        if (resources.containsKey(feature) && resources.get(feature).containsKey(locale.toString())) { return; }
        synchronized (msg) {
            // second check
            if (resources.containsKey(feature) && resources.get(feature).containsKey(locale.toString())) { return; }
            ResourceBundle bundle;
            try {
                log.trace("creating bundle with base name {} and locale {}", feature, locale.toString());
                bundle = ResourceBundle.getBundle(feature + "-messages", locale);
            } catch (MissingResourceException e) {
                throw new AppException("ERR_FRAMEWORK_002", "Can not find {}-messages.properties", feature);
            }
            if (!resources.containsKey(feature)) {
                resources.put(feature, new HashMap<String, ResourceBundle>());
            }
            if (!resources.get(feature).containsKey(locale.toString())) {
                resources.get(feature).put(locale.toString(), bundle);
            }
        }
    }

    /**
     * Description : get message by given code and current local. <br>
     * this will search the code by every current existing bundle, and will return the first found. <br>
     * if it's not found, it will search it by system property message features with key <b>"system.message.features"</b> If the key and value is not defined in
     * system property, it will only search <b> "framework"</b> bundles<br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param code the message code
     * @return the first found value order by constant <b>"messageFeatures"</b> defined in spring xml configuration file. Or in framework by default
     * @throws AppException the parameter is null or can not find it in any properties
     */
    public static String getMsg(String code) throws AppException {
        // find it by current existing feature
        for (String key : msg.resources.keySet()) {
            try {
                return getMsg(key, code);
            } catch (AppException e) {
                continue;
            }
        } ;
        // if not found, will trace the configuration of features
        try {
            for (String feature : System.getProperty("system.message.features").split(",")) {
                try {
                    return getMsg(feature, code);
                } catch (AppException e) {
                    continue;
                }
            }
            // can not find it, throw out of the method, be care, AppException is not a Exception subClass
            throw new AppException("ERR_FRAMEWORK_002", "Can not find {} in any property files by locale {}", code,
                    ActionContext.getContext().getLocale().toString());
        } catch (Exception e) {
            return getMsg("framework", code);
        }
    }

    /**
     * Description : get message by given code and current locale <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param feature feature name in short, this will be the prefix of message properties
     * @param code the message code
     * @return message
     * @throws AppException paramter is null or can not find it
     */
    public static String getMsg(String feature, String code) throws AppException {
        return getMsg(feature, code, ActionContext.getContext().getLocale());
    }

    /**
     * Description : get message by given feature, code and locale <br>
     * Create Time: 2016-09-19 <br>
     * Create by : jimmyblylee@126.com <br>
     *
     * @param feature feature name in short, this will be the prefix of message properties
     * @param code the message code
     * @param locale the locale
     * @return message
     * @throws AppException paramter is null or can not find it
     */
    public static String getMsg(String feature, String code, Locale locale) throws AppException {
        return msg.getMsgByLocale(feature, code, locale);
    }
}
