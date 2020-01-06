package com.github.krzsernik.ytmusicdl;

import com.github.krzsernik.ytmusicdl.data.Video;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VideoTest {
    @Test
    public void testGetVideo() throws Exception {
        Video video = Video.GetVideo("58LVoLDBspc");

        assertNotNull(video);
        assertEquals("58LVoLDBspc", video.videoId);
    }
}