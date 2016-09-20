/**
 * Project Name : jwaf-dispatcher-test <br>
 * File Name : DispatcherTest.java <br>
 * Package Name : com.lee.jwaf.test <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com <br>
 * Copyright © 2006, 2016, Jimmybly Lee. All rights reserved.
 */
package com.lee.jwaf.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.Charsets;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lee.util.StreamUtils;

/**
 * ClassName : DispatcherTest <br>
 * Description : Integration test of Dispatcher <br>
 * Create Time : 2016-09-20 <br>
 * Create by : jimmyblylee@126.com
 */
public class DispatcherTest {

    private static CloseableHttpClient client;

    private RequestBuilder reqBuilder;
    private CloseableHttpResponse res;

    @BeforeClass
    public static void initHttpClient() {
        client = HttpClients.custom().build();
    }

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Before
    public void initReq() throws URISyntaxException {
        reqBuilder = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch")).addParameter("method", "test");
    }

    @After
    public void closeRes() throws IOException {
        res.close();
    }

    @Test
    public void testContextAware() throws ClientProtocolException, IOException {
        HttpUriRequest req = reqBuilder.addParameter("controller", "ContextAwareController").build();
        res = client.execute(req);
        String resTxt = StreamUtils.copyToString(res.getEntity().getContent(), Charsets.UTF_8);
        assertThat(resTxt, containsString("valid"));
    }

    @Test
    public void testMessage() throws ClientProtocolException, IOException {
        HttpUriRequest req = reqBuilder.addParameter("controller", "MessageController").addHeader("accept-language", "en_US").build();
        res = client.execute(req);
        String resTxt = StreamUtils.copyToString(res.getEntity().getContent(), Charsets.UTF_8);
        assertThat(resTxt, containsString("Unknown Issue"));
    }

}
