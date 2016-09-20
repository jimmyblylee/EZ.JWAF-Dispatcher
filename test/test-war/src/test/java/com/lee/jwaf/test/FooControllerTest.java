package com.lee.jwaf.test;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.Charsets;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import com.lee.util.StreamUtils;

public class FooControllerTest {

    @Test
    public void testFoo() {
        CloseableHttpResponse res = null;
        try {
            CloseableHttpClient client = HttpClients.custom().build();
            HttpUriRequest req = RequestBuilder.post().setUri(new URI("http://localhost:8080/test/mvc/dispatch")).addParameter("controller", "FooController")
                    .addParameter("method", "foo1").addParameter("name", "jimmy").build();
            res = client.execute(req);
            String resTxt = StreamUtils.copyToString(res.getEntity().getContent(), Charsets.UTF_8);
            System.out.println(resTxt);
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
