package com.github.krzsernik.ytmusicdl;

import com.github.krzsernik.ytmusicdl.data.Video;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class VideoTest {
    @Test
    public void testGetVideo() {
        Video.VideoInfo video = Video.GetVideoInfo("58LVoLDBspc");

        assertNotNull(video);
        assertEquals("58LVoLDBspc", video.videoId);
        assertNotEquals(new ArrayList<>(), video.formats);
    }
}