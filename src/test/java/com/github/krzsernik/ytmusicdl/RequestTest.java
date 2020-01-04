package com.github.krzsernik.ytmusicdl;

import com.google.gson.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals("Json GET Request", "https://httpbin.org/get", result.get("url").getAsString());

        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
        result = requestPost.sendJson();
        assertEquals("Json POST Request", "https://httpbin.org/post", result.get("url").getAsString());
    }

    class TestJsonClass {
        String url;
    }

    @Test
    public void testSendJsonClass() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        TestJsonClass classResult = requestGet.sendJsonClass(TestJsonClass.class);
        assertEquals("Serialized Json Request", "https://httpbin.org/get", classResult.url);
    }
}
