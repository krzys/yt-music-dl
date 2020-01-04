package com.github.krzsernik.ytmusicdl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Request {
    enum Method {
        GET,
        POST
    }

    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpRequestBase _request;

    public Request(String url, Method method) {
        if (method == Method.GET) _request = new HttpGet(url);
        else if (method == Method.POST) _request = new HttpPost(url);
    }

    public void setHeader(String key, String value) {
        _request.setHeader(key, value);
    }

    public String send() {
        try (CloseableHttpResponse response = httpClient.execute(_request)) {
            HttpEntity entity = response.getEntity();
            String result = null;

            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
            response.close();

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    public JsonObject sendJson() {
        try (CloseableHttpResponse response = httpClient.execute(_request)) {
            HttpEntity entity = response.getEntity();
            JsonObject result = null;

            if (entity != null) {
                result = new Gson().fromJson(EntityUtils.toString(entity), JsonObject.class);
            }
            response.close();

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T sendJsonClass(Class<T> tClass) {
        try (CloseableHttpResponse response = httpClient.execute(_request)) {
            HttpEntity entity = response.getEntity();
            T result = null;

            if (entity != null) {
                result = new Gson().fromJson(EntityUtils.toString(entity), tClass);
            }
            response.close();

            return result;
        } catch (IOException e) {
            return null;
        }
    }
}
