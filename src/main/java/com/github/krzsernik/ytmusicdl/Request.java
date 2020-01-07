package com.github.krzsernik.ytmusicdl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Request {
    public enum Method {
        GET,
        POST
    }

    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpRequestBase _request;
    private List<NameValuePair> _formData;

    public static Request Get(String url) {
        return new Request(url, Method.GET);
    }

    public static Request Post(String url) {
        return new Request(url, Method.POST);
    }

    public Request(String url, Method method) {
        if (method == Method.GET) _request = new HttpGet(url);
        else if (method == Method.POST) _request = new HttpPost(url);
    }

    public void setHeader(String key, String value) {
        _request.setHeader(key, value);
    }

    public boolean setData(String key, String value) {
        if (_formData == null) _formData = new ArrayList<>();

        _formData.add(new BasicNameValuePair(key, value));
        return true;
    }

    public boolean setData(String data) {
        try {
            ((HttpPost) _request).setEntity(new StringEntity(data));
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    private void setData() {
        if (_formData != null) {
            try {
                ((HttpPost) _request).setEntity(new UrlEncodedFormEntity(_formData));
            } catch (UnsupportedEncodingException ignored) {
            }
        }
    }

    public String send() {
        setData();

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
        setData();

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
        setData();

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

    public boolean download(String filename) {
        setData();

        try (CloseableHttpResponse response = httpClient.execute(_request)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filename)));

                int inByte;
                while ((inByte = bis.read()) != -1) bos.write(inByte);
                bis.close();
                bos.close();
            }
            response.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
