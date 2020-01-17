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
    final private static Gson JSON = new Gson();

    public enum Method {
        GET,
        POST
    }

    static CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpRequestBase _request;
    private List<NameValuePair> _formData;
    private HttpEntity _httpEntity;
    String _response;

    private boolean _isSent = false;

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

    CloseableHttpResponse _execute() throws IOException {
        return httpClient.execute(_request);
    }

    public void send() {
        _isSent = true;
        setData();

        try (CloseableHttpResponse response = _execute()) {
            _httpEntity = response.getEntity();
            _response = EntityUtils.toString(_httpEntity);
        } catch (IOException ignored) {
        }
    }

    public String get() {
        if (!_isSent) send();

        return _response;
    }

    public JsonObject getJson() {
        if (!_isSent) send();

        return JSON.fromJson(_response, JsonObject.class);
    }

    public <T> T getJson(Class<T> tClass) {
        if (!_isSent) send();

        return JSON.fromJson(_response, tClass);
    }

    public boolean download(String filename) {
        _isSent = true;
        setData();

        try (CloseableHttpResponse response = _execute()) {
            _httpEntity = response.getEntity();

            if (_httpEntity != null) {
                BufferedInputStream bis = new BufferedInputStream(_httpEntity.getContent());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filename)));

                byte[] contents = new byte[1024];
                int bytesRead;
                _response = "";

                while ((bytesRead = bis.read(contents)) != -1) {
                    bos.write(contents);
                    _response += new String(contents, 0, bytesRead);
                }
                bis.close();
                bos.close();

                return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    public HttpEntity getHttpEntity() {
        return _httpEntity;
    }
}
