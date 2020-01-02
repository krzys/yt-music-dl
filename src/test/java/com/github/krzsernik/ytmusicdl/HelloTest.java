package com.github.krzsernik.ytmusicdl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloTest {
    private Hello subject;

    @Before
    public void setup() {
        subject = new Hello();
    }

    @Test
    public void testGetHello() {
        assertEquals("Hello, World!", subject.getHello());
    }
}
