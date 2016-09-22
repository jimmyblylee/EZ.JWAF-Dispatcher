/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : SessionControllerTest.java <br>
 * Package Name : com.lee.jwaf.test <br>
 * Create Time : 2016-09-22 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.commons.io.Charsets;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lee.util.StreamUtils;

/**
 * ClassName : SessionControllerTest <br>
 * Description : test session <br>
 * Create Time : 2016-09-22 <br>
 * Create by : jimmyblylee@126.com
 */
public class SessionControllerTest {

    private static HttpClientContext context;
    private static CookieStore cookieStore;
    private static RequestConfig requestConfig;
    private static CloseableHttpClient client;

    @BeforeClass
    public static void init() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000).setConnectionRequestTimeout(60000).build();
        client = HttpClients.custom().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).setRedirectStrategy(new DefaultRedirectStrategy())
                .setDefaultRequestConfig(requestConfig).setDefaultCookieStore(cookieStore).build();
    }

    @Test
    public void testSession() {
        createDataBySession(true);
        getSession();
        clear();
        createDataBySession(false);
        invalidate();
        createDataBySession(true);
    }

    private void clear() {
        CloseableHttpResponse res = null;
        try {
            CloseableHttpClient client = HttpClients.custom().build();
            HttpUriRequest req = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch"))
                    .addParameter("controller", "SessionController").addParameter("method", "clear").build();
            res = client.execute(req, context);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void invalidate() {
        CloseableHttpResponse res = null;
        try {
            CloseableHttpClient client = HttpClients.custom().build();
            HttpUriRequest req = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch"))
                    .addParameter("controller", "SessionController").addParameter("method", "invalid").build();
            res = client.execute(req, context);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDataBySession(boolean newSession) {
        CloseableHttpResponse res = null;
        try {
            HttpUriRequest req = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch"))
                    .addParameter("controller", "SessionController").addParameter("method", "createSessionData").addParameter("key", "jimmy").build();
            res = client.execute(req, context);
            assertThat(res.getHeaders("Set-Cookie").length, newSession ? greaterThan(0) : is(0));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getSession() {
        CloseableHttpResponse res = null;
        try {
            CloseableHttpClient client = HttpClients.custom().build();
            HttpUriRequest req = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch"))
                    .addParameter("controller", "SessionController").addParameter("method", "getSessionDataOfKey").build();
            res = client.execute(req, context);
            String resTxt = EntityUtils.toString(res.getEntity(), Charsets.UTF_8);
            assertThat(resTxt, containsString("jimmy"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
