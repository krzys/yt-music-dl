package com.github.krzsernik.ytmusicdl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RequestTest {
    public Request requestGet;
    public Request requestPost;

    @Before
    public void setup() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        requestPost = new Request("http://httpbin.org/post", Request.Method.POST);
    }

    @Test
    public void testBasicRequests() {
        String result = requestGet.send();
        assertNotNull("Basic GET Request", result);

        result = requestPost.send();
        assertNotNull("Basic POST Request", result);
    }
}
