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
package psiprobe.tools.logging.jdk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import psiprobe.model.Application;

/**
 * The Class Jdk14FileHandlerAccessorTest.
 */
class Jdk14FileHandlerAccessorTest {

  /**
   * Gets the file.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void getFile() throws IOException {
    Jdk14FileHandlerAccessor handlerAccessor = new Jdk14FileHandlerAccessor();
    handlerAccessor.setLoggerAccessor(new Jdk14LoggerAccessor());

    Path testPath = Files.createTempDirectory("psi-probe");
    testPath.toFile().deleteOnExit();
    FileHandler target = new FileHandler(testPath.toString() + "test-%g.log", 1024, 3);

    handlerAccessor.setTarget(target);
    handlerAccessor.setIndex(Integer.toString(0));
    Application testApplication = new Application();
    handlerAccessor.setApplication(testApplication);

    File file = handlerAccessor.getFile();

    Assertions.assertThat(file.getAbsolutePath()).isEqualTo(testPath + "test-0.log");

  }

}
