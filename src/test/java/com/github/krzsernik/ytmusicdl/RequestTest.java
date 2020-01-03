package com.github.krzsernik.ytmusicdl;

import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RequestTest {
    public Request requestGet;
    public Request requestPost;

    @Test
    public void testSend() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        String result = requestGet.send();
        assertNotNull("Basic GET Request", result);

        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
        result = requestPost.send();
        assertNotNull("Basic POST Request", result);

        requestGet = new Request("http://nonexisting.domain", Request.Method.GET);
        result = requestGet.send();
        assertNull("Null GET Request", result);
    }

    @Test
    public void testSendJson() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        JsonObject result = requestGet.sendJson();
        assertNotNull("Basic GET Request", result);

        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
        result = requestPost.sendJson();
        assertNotNull("Basic POST Request", result);
    }
}
