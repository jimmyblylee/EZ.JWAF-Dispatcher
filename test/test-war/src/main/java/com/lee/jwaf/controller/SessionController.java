/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : SessionController.java <br>
 * Package Name : com.lee.jwaf.controller <br>
 * Create Time : 2016-09-22 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.controller;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lee.jwaf.context.Context;
import com.lee.jwaf.dto.SessionDTO;
import com.lee.jwaf.dto.WorkDTO;
import com.lee.jwaf.log.Log;

/**
 * ClassName : SessionController <br>
 * Description : test session <br>
 * Create Time : 2016-09-22 <br>
 * Create by : jimmyblylee@126.com
 */
@Controller("SessionController")
@Scope(value = SCOPE_PROTOTYPE)
public class SessionController {
    
    @Context
    private SessionDTO session;
    @Context
    private WorkDTO work;
    @Context
    private HttpServletRequest req;
    @Log
    private Logger log;

    public void createSessionData() {
        log.info("key is {}, and the value is {} in the workDTO", "key", work.get("key"));
        session.put("key", work.get("key"));
    }
    
    public void getSessionDataOfKey() {
        log.info("key is {}, and the value is {} in the sessionDTO", "key", session.get("key"));
        work.clear();
        work.put("key", session.get("key"));
    }
    
    public void clear() {
        session.clear();
    }
    
    public void invalid() {
        req.getSession().invalidate();
    }
}
