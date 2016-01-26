package com.googlecode.psiprobe;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackConfigTest {

  @Test
  public void test() {
    Logger log = LoggerFactory.getLogger(getClass());
    if ("org.slf4j.impl.JDK14LoggerAdapter".equals(log.getClass().getName())) {
      fail("slf4j-jdk14-1.7.7.jar is on the classpath, but it should NOT be.");
    }
  }
}
