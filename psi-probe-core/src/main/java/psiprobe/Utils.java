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

import com.google.common.base.Strings;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codelibs.jhighlight.renderer.Renderer;
import org.codelibs.jhighlight.renderer.XhtmlRendererFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.tokenizer.StringTokenizer;
import psiprobe.tokenizer.Token;
import psiprobe.tokenizer.Tokenizer;
import psiprobe.tokenizer.TokenizerSymbol;

/**
 * Misc. static helper methods.
 */
public final class Utils {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(Utils.class);

  /**
   * Prevent Instantiation.
   */
  private Utils() {
    // Prevent Instantiation
  }

  /**
   * Reads a file on disk. The method uses default file encoding (see: file.encoding system
   * property)
   *
   * @param file to be read
   * @param charsetName the charset name
   *
   * @return String representation of the file contents
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String readFile(File file, String charsetName) throws IOException {
    try (InputStream fis = Files.newInputStream(file.toPath())) {
      return readStream(fis, charsetName);
    }
  }

  /**
   * Reads strings from the intput stream using the given charset. This method closes the input
   * stream after it has been consumed.
   *
   * <p>
   * This method uses the system's default charset if the given one is unsupported.
   * </p>
   *
   * @param is the stream from which to read
   * @param charsetName the charset to use when reading the stream
   *
   * @return the contents of the given input stream
   *
   * @throws IOException if reading from the stream fails spectacularly
   */
  public static String readStream(InputStream is, String charsetName) throws IOException {
    Charset charset;
    if (Charset.isSupported(charsetName)) {
      charset = Charset.forName(charsetName);
    } else {
      // use system's default encoding if the passed encoding is unsupported
      charset = Charset.defaultCharset();
    }

    StringBuilder out = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset), 4096)) {
      String line;
      while ((line = reader.readLine()) != null) {
        out.append(line).append('\n');
      }
    }

    return out.toString();
  }

  /**
   * Delete.
   *
   * @param file the file
   */
  public static void delete(File file) {
    if (file != null && file.exists()) {
      if (file.isDirectory()) {
        for (File child : file.listFiles()) {
          delete(child);
        }
      }
      try {
        Files.delete(file.toPath());
      } catch (IOException e) {
        logger.debug("Cannot delete '{}'", file.getAbsolutePath(), e);
      }
    } else {
      logger.debug("'{}' does not exist", file);
    }
  }

  /**
   * To int.
   *
   * @param num the num
   * @param defaultValue the default value
   *
   * @return the int
   */
  public static int toInt(String num, int defaultValue) {
    if (num != null && !num.contains(" ")) {
      try (Scanner scanner = new Scanner(num)) {
        if (scanner.hasNextInt()) {
          return Integer.parseInt(num);
        }
      }
    }
    return defaultValue;
  }

  /**
   * To int hex.
   *
   * @param num the num
   * @param defaultValue the default value
   *
   * @return the int
   */
  public static int toIntHex(String num, int defaultValue) {
    if (num != null && !num.contains(" ")) {
      if (num.startsWith("#")) {
        num = num.substring(1);
      }
      try (Scanner scanner = new Scanner(num)) {
        if (scanner.hasNextInt()) {
          return Integer.parseInt(num, 16);
        }
      }
    }
    return defaultValue;
  }

  /**
   * To long.
   *
   * @param num the num
   * @param defaultValue the default value
   *
   * @return the long
   */
  public static long toLong(String num, long defaultValue) {
    if (num != null && !num.contains(" ")) {
      try (Scanner scanner = new Scanner(num)) {
        if (scanner.hasNextLong()) {
          return Long.parseLong(num);
        }
      }
    }
    return defaultValue;
  }

  /**
   * To long.
   *
   * @param num the num
   * @param defaultValue the default value
   *
   * @return the long
   */
  public static long toLong(Long num, long defaultValue) {
    return num == null ? defaultValue : num;
  }

  /**
   * To float.
   *
   * @param num the num
   * @param defaultValue the default value
   *
   * @return the float
   */
  public static float toFloat(String num, float defaultValue) {
    if (num != null && !num.contains(" ")) {
      try (Scanner scanner = new Scanner(num)) {
        if (scanner.hasNextFloat()) {
          return Float.parseFloat(num);
        }
      }
    }
    return defaultValue;
  }

  /**
   * Gets the jsp encoding.
   *
   * @param is the is
   *
   * @return the jsp encoding
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String getJspEncoding(InputStream is) throws IOException {

    String encoding = null;
    String contentType = null;

    Tokenizer jspTokenizer = new Tokenizer();
    jspTokenizer.addSymbol("\n", true);
    jspTokenizer.addSymbol(" ", true);
    jspTokenizer.addSymbol("\t", true);
    jspTokenizer.addSymbol(new TokenizerSymbol("dir", "<%@", "%>", false, false, true, false));

    StringTokenizer directiveTokenizer = new StringTokenizer();
    directiveTokenizer.addSymbol("\n", true);
    directiveTokenizer.addSymbol(" ", true);
    directiveTokenizer.addSymbol("\t", true);
    directiveTokenizer.addSymbol("=");
    directiveTokenizer.addSymbol("\"", "\"", false);
    directiveTokenizer.addSymbol("'", "'", false);

    StringTokenizer contentTypeTokenizer = new StringTokenizer();
    contentTypeTokenizer.addSymbol(" ", true);
    contentTypeTokenizer.addSymbol(";", true);

    try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
      jspTokenizer.setReader(reader);
      while (jspTokenizer.hasMore()) {
        Token token = jspTokenizer.nextToken();
        if ("dir".equals(token.getName())) {
          directiveTokenizer.setString(token.getInnerText());
          if (directiveTokenizer.hasMore()
              && "page".equals(directiveTokenizer.nextToken().getText())) {
            while (directiveTokenizer.hasMore()) {
              Token directiveToken = directiveTokenizer.nextToken();
              if ("pageEncoding".equals(directiveToken.getText())) {
                if (directiveTokenizer.hasMore()
                    && "=".equals(directiveTokenizer.nextToken().getText())
                    && directiveTokenizer.hasMore()) {
                  encoding = directiveTokenizer.nextToken().getInnerText();
                  break;
                }
              } else if ("contentType".equals(directiveToken.getText())
                  && directiveTokenizer.hasMore()
                  && "=".equals(directiveTokenizer.nextToken().getText())
                  && directiveTokenizer.hasMore()) {
                contentType = directiveTokenizer.nextToken().getInnerText();
              }
            }
          }
        }
      }
    }

    if (encoding == null && contentType != null) {
      contentTypeTokenizer.setString(contentType);
      while (contentTypeTokenizer.hasMore()) {
        String token = contentTypeTokenizer.nextToken().getText();
        if (token.startsWith("charset=")) {
          encoding = token.substring("charset=".length());
          break;
        }
      }
    }

    return encoding != null ? encoding : "UTF_8";
  }

  /**
   * Send file.
   *
   * @param request the request
   * @param response the response
   * @param file the file
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void sendFile(HttpServletRequest request, HttpServletResponse response, File file)
      throws IOException {

    try (OutputStream out = response.getOutputStream();
        RandomAccessFile raf = new RandomAccessFile(file, "r")) {
      long fileSize = raf.length();
      long rangeStart = 0;
      long rangeFinish = fileSize - 1;

      // accept attempts to resume download (if any)
      String range = request.getHeader("Range");
      if (range != null && range.startsWith("bytes=")) {
        String pureRange = range.replace("bytes=", "");
        int rangeSep = pureRange.indexOf('-');

        try {
          rangeStart = Long.parseLong(pureRange.substring(0, rangeSep));
          if (rangeStart > fileSize || rangeStart < 0) {
            rangeStart = 0;
          }
        } catch (NumberFormatException e) {
          // keep rangeStart unchanged
          logger.trace("", e);
        }

        if (rangeSep < pureRange.length() - 1) {
          try {
            rangeFinish = Long.parseLong(pureRange.substring(rangeSep + 1));
            if (rangeFinish < 0 || rangeFinish >= fileSize) {
              rangeFinish = fileSize - 1;
            }
          } catch (NumberFormatException e) {
            logger.trace("", e);
          }
        }
      }

      // set some headers
      response.setContentType("application/x-download");
      response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-Length", Long.toString(rangeFinish - rangeStart + 1));
      response.setHeader("Content-Range",
          "bytes " + rangeStart + "-" + rangeFinish + "/" + fileSize);

      // seek to the requested offset
      raf.seek(rangeStart);

      // send the file
      byte[] buffer = new byte[4096];

      long len;
      int totalRead = 0;
      boolean nomore = false;
      while (true) {
        len = raf.read(buffer);
        if (len > 0 && totalRead + len > rangeFinish - rangeStart + 1) {
          // read more then required?
          // adjust the length
          len = rangeFinish - rangeStart + 1 - totalRead;
          nomore = true;
        }

        if (len <= 0) {
          break;
        }
        out.write(buffer, 0, (int) len);
        totalRead = totalRead + (int) len;
        if (nomore) {
          break;
        }
      }
    }
  }

  /**
   * Gets the thread by name.
   *
   * @param name the name
   *
   * @return the thread by name
   */
  public static Thread getThreadByName(String name) {
    if (name != null) {
      // get top ThreadGroup
      ThreadGroup masterGroup = Thread.currentThread().getThreadGroup();
      while (masterGroup.getParent() != null) {
        masterGroup = masterGroup.getParent();
      }

      Thread[] threads = new Thread[masterGroup.activeCount()];
      int numThreads = masterGroup.enumerate(threads);

      for (int i = 0; i < numThreads; i++) {
        if (threads[i] != null && name.equals(threads[i].getName())) {
          return threads[i];
        }
      }
    }
    return null;
  }

  /**
   * Highlight stream.
   *
   * @param name the name
   * @param input the input
   * @param rendererName the renderer name
   * @param encoding the encoding
   *
   * @return the string
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String highlightStream(String name, InputStream input, String rendererName,
      String encoding) throws IOException {

    Renderer jspRenderer = XhtmlRendererFactory.getRenderer(rendererName);
    if (jspRenderer == null) {
      return null;
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    jspRenderer.highlight(name, input, bos, encoding, true);

    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

    Tokenizer tokenizer = new Tokenizer(new InputStreamReader(bis, Charset.forName(encoding)));
    tokenizer.addSymbol(new TokenizerSymbol("EOL", "\n", null, false, false, true, false));
    tokenizer.addSymbol(new TokenizerSymbol("EOL", "\r\n", null, false, false, true, false));

    //
    // JHighlight adds HTML comment as the first line, so if
    // we number the lines we could end up with a line number and no line
    // to avoid that we just ignore the first line all together.
    //
    StringBuilder buffer = new StringBuilder();
    long counter = 0;
    while (tokenizer.hasMore()) {
      Token tk = tokenizer.nextToken();
      if ("EOL".equals(tk.getName())) {
        counter++;
        buffer.append(tk.getText());
      } else if (counter > 0) {
        buffer.append("<span class=\"codeline\">");
        buffer.append("<span class=\"linenum\">");
        buffer.append(leftPad(Long.toString(counter), 6, " ").replace(" ", "&nbsp;"));
        buffer.append("</span>");
        buffer.append(tk.getText());
        buffer.append("</span>");
      }
    }
    return buffer.toString();
  }

  /**
   * Send compressed file.
   *
   * @param response the response
   * @param file the file
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void sendCompressedFile(HttpServletResponse response, File file)
      throws IOException {
    try (ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
        InputStream fileInput = new BufferedInputStream(Files.newInputStream(file.toPath()))) {

      String fileName = file.getName();

      // set some headers
      response.setContentType("application/zip");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");

      zip.putNextEntry(new ZipEntry(fileName));

      // send the file
      byte[] buffer = new byte[4096];
      long len;

      while ((len = fileInput.read(buffer)) > 0) {
        zip.write(buffer, 0, (int) len);
      }
      zip.closeEntry();
    }
  }

  /**
   * Left pad.
   *
   * @param str the str
   * @param len the len
   * @param fill the fill
   *
   * @return the string
   */
  static String leftPad(final String str, final int len, final String fill) {
    if (str != null && str.length() < len) {
      return Strings.padStart(str, len, fill.charAt(0));
    }
    return str == null ? "" : str;
  }

  /**
   * Gets the names for locale.
   *
   * @param baseName the base name
   * @param locale the locale
   *
   * @return the names for locale
   */
  public static List<String> getNamesForLocale(String baseName, Locale locale) {
    List<String> result = new ArrayList<>(3);
    String language = locale.getLanguage();
    String country = locale.getCountry();
    String variant = locale.getVariant();
    StringBuilder temp = new StringBuilder(baseName);

    if (language.length() > 0) {
      temp.append('_').append(language);
      result.add(0, temp.toString());
    }

    if (country.length() > 0) {
      temp.append('_').append(country);
      result.add(0, temp.toString());
    }

    if (variant.length() > 0) {
      temp.append('_').append(variant);
      result.add(0, temp.toString());
    }

    return result;
  }

  /**
   * Checks if is threading enabled.
   *
   * @return true, if is threading enabled
   */
  public static boolean isThreadingEnabled() {
    try {
      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName threadingOName = new ObjectName("java.lang:type=Threading");
      Set<ObjectInstance> threading = mbeanServer.queryMBeans(threadingOName, null);
      return threading != null && !threading.isEmpty();
    } catch (MalformedObjectNameException e) {
      logger.trace("", e);
      return false;
    }
  }
}
