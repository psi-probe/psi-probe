/*
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class AwtAppContextClassloaderListenerTest.
 */
@ExtendWith(MockitoExtension.class)
class AwtAppContextClassloaderListenerTest {

  /** The listener. */
  @InjectMocks
  AwtAppContextClassloaderListener listener;

  /** The event. */
  @Mock
  ServletContextEvent event;

  /** The image IO. */
  @Mock
  ImageIO imageIO;

  /**
   * Context initialized test.
   */
  @Test
  void contextInitializedTest() {
    try (var mocked = Mockito.mockStatic(ImageIO.class)) {
      listener.contextInitialized(event);
      mocked.verify(() -> ImageIO.getCacheDirectory(), Mockito.times(1));
    }
  }

  /**
   * Context initialized error test.
   */
  @Test
  void contextInitializedErrorTest() {
    try (var mocked = Mockito.mockStatic(ImageIO.class)) {
      mocked.when(ImageIO::getCacheDirectory).thenThrow(new RuntimeException());
      listener.contextInitialized(event);
    }
  }

  /**
   * Context destroyed test.
   */
  @Test
  void contextDestroyedTest() {
    // Dummy Test as method is not implemented
    listener.contextDestroyed(event);
  }

}
