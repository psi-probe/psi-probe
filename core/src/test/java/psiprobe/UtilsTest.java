package psiprobe;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class UtilsTest {

  @Test
  public void testCloseStream(@Mocked final Closeable stream) throws Exception {
    Utils.closeStream(stream);

    new Verifications() {
      {
        stream.close();
        times = 1;
      }
    };
  };

  @Test
  public void testCloseStreamException(@Mocked final Closeable stream) throws Exception {
    new Expectations() {
      {
        stream.close();
        result = new IOException();
      }
    };

    Utils.closeStream(stream);

    new Verifications() {
      {
        stream.close();
        times = 1;
      }
    };
  };
}
