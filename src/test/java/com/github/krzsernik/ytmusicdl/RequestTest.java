package com.github.krzsernik.ytmusicdl;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Map;

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
        Map headers;
        Map form;
        String data;
    }

    @Test
    public void testSendJsonClass() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        TestJsonClass classResult = requestGet.sendJsonClass(TestJsonClass.class);
        assertEquals("Serialized Json Request", "https://httpbin.org/get", classResult.url);
    }

    @Test
    public void testSetHeader() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        requestGet.setHeader("Test", "secret");
        TestJsonClass classResult = requestGet.sendJsonClass(TestJsonClass.class);
        assertEquals("Set Header (JSON)", "secret", classResult.headers.get("Test"));
    }

    @Test
    public void testSetData() {
        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
        requestPost.setData("TestArg", "secret");
        TestJsonClass classResult = requestPost.sendJsonClass(TestJsonClass.class);
        assertEquals("Json POST Request", "secret", classResult.form.get("TestArg"));

        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
        requestPost.setData("testStringData");
        classResult = requestPost.sendJsonClass(TestJsonClass.class);
        assertEquals("Json POST Request", "testStringData", classResult.data);
    }
}
