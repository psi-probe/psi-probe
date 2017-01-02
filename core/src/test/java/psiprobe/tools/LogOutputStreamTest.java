package psiprobe.tools;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;
import org.slf4j.Logger;

import mockit.Mocked;

/**
 * The Class LogOutputStreamTest.
 */
public class LogOutputStreamTest {

    /** The stream. */
    PrintStream stream;

    /** The log. */
    @Mocked
    Logger log;

    /**
     * Logger test.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void loggerTest() throws IOException {
        stream = LogOutputStream.createPrintStream(log, 5);
        stream.write('\u0001');
    }

}
