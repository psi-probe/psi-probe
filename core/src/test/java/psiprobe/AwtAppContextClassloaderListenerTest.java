/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;

import org.junit.jupiter.api.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

/**
 * The Class AwtAppContextClassloaderListenerTest.
 */
public class AwtAppContextClassloaderListenerTest {

  /** The listener. */
  @Tested
  AwtAppContextClassloaderListener listener;

  /** The event. */
  @Mocked
  ServletContextEvent event;

  /** The image IO. */
  @Mocked
  ImageIO imageIO;

  /**
   * Context initialized test.
   */
  @Test
  public void contextInitializedTest() {
    listener.contextInitialized(event);

    new Verifications() {
      {
        ImageIO.getCacheDirectory();
        times = 1;
      }
    };
  }

  /**
   * Context initialized error test.
   */
  @Test
  public void contextInitializedErrorTest() {
    new Expectations() {
      {
        ImageIO.getCacheDirectory();
        result = new Exception();
      }
    };

    listener.contextInitialized(event);
  }

  /**
   * Context destroyed test.
   */
  @Test
  public void contextDestroyedTest() {
    // Dummy Test as method is not implemented
    listener.contextDestroyed(event);
  }

}
