package com.github.krzsernik.ytmusicdl.data;

import com.github.krzsernik.ytmusicdl.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {
    public final static String GET_VIDEO_INFO_URL = "http://youtube.com/get_video_info?video_id=";
    public final static Gson JSON = new Gson();

    public static class VideoInfo {
        public String videoId;
        public String title;
        public String author;
        public String shortDescription;
        public List<Format> formats;
    }

    public static VideoInfo GetVideoInfo(String videoId) {
        Request videoInfoRequest = Request.Get(GET_VIDEO_INFO_URL + videoId);
        String content = videoInfoRequest.get();

        Map<String, String> videoInfo = new HashMap<>();
        String[] contentParts = content.split("&");

        VideoInfo video = null;
        try {
            for (String part : contentParts) {
                String[] keyValueArr = part.split("=");
                String key = keyValueArr[0];
                String value = URLDecoder.decode(keyValueArr[1], "UTF-8");

                videoInfo.put(keyValueArr[0], value);
            }

            JsonObject playerResponse = JSON.fromJson(videoInfo.get("player_response"), JsonObject.class);
            JsonElement videoDetails = playerResponse.get("videoDetails");

            // cast JsonElement to Video class
            video = JSON.fromJson(videoDetails, VideoInfo.class);

            JsonArray formats = playerResponse.getAsJsonObject("streamingData")
                    .getAsJsonArray("adaptiveFormats");

            video.formats = new ArrayList<>();
            for (JsonElement element : formats) {
                video.formats.add(JSON.fromJson(element, Format.class));
            }
        } catch (Exception ignored) {
        }
        return video;
    }
}
