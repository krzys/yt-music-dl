package com.github.krzsernik.ytmusicdl;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {
    public Request requestGet;
    public Request requestPost;

    @Test
    public void testGetRequest() {
        requestGet = Request.Get("http://httpbin.org/get");
        requestGet.send();

        String resultGet = requestGet.get();
        assertNotNull("Basic GET Request", resultGet);
    }

    @Test
    public void testPostRequest() {
        requestPost = Request.Post("http://httpbin.org/post");
        requestPost.send();

        String resultPost = requestPost.get();
        assertNotNull("Basic POST Request", resultPost);
    }

    @Test
    public void testPostRequestWithFormData() {
        requestPost = Request.Post("http://httpbin.org/post");
        requestPost.setData("test", "data");

        JsonObject result = requestPost.getJson();
        assertNotNull("POST Request", result);

        String dataValue = result.getAsJsonObject("form").get("test").getAsString();
        assertEquals("POST Request Form Data", "data", dataValue);
    }

    @Test
    public void testPostRequestWithStringData() {
        requestPost = Request.Post("http://httpbin.org/post");
        requestPost.setData("test data");

        JsonObject result = requestPost.getJson();
        String dataValue = result.get("data").getAsString();
        assertEquals("POST Request String Data", "test data", dataValue);
    }

    @Test
    public void testJsonResponse() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);

        JsonObject result = requestGet.getJson();
        assertEquals("JSON Response", "https://httpbin.org/get", result.get("url").getAsString());
    }

    class TestJsonClass {
        String url;
        Map headers;
        Map form;
        String data;
    }

    @Test
    public void testJsonClassResponse() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);

        TestJsonClass classResult = requestGet.getJson(TestJsonClass.class);
        assertEquals("Own Class Json Response", "https://httpbin.org/get", classResult.url);
    }

    @Test
    public void testRequestWithHeader() {
        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        requestGet.setHeader("Test", "secret");

        TestJsonClass classResult = requestGet.getJson(TestJsonClass.class);
        assertEquals("Set Header (JSON)", "secret", classResult.headers.get("Test"));
    }

    @Test
    public void testDownload() throws IOException {
        String filename = "httpbin-get.json";

        requestGet = new Request("http://httpbin.org/get", Request.Method.GET);
        boolean success = requestGet.download(filename);

        assertTrue("Download success", success);

        String content = requestGet.get().trim();
        String fileContent = new String(
                Files.readAllBytes(
                        Paths.get(filename)
                )
        ).trim();

        assertEquals("Downloaded file content equal to get", content, fileContent);

        Files.deleteIfExists(Paths.get(filename));
    }

    @Test
    public void testHttpEntity() {
        requestGet = Request.Get("http://httpbin.org/get");
        requestGet.send();

        assertNotNull("HTTP Entity not null", requestGet.getHttpEntity());
    }
}
