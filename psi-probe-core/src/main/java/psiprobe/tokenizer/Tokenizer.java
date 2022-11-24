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
package psiprobe.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Tokenizer.
 */
public class Tokenizer {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);

  /** The Constant TT_TOKEN. */
  public static final int TT_TOKEN = 0;

  /** The Constant TT_SYMBOL. */
  public static final int TT_SYMBOL = 1;

  /** The Constant TT_BLOCK. */
  public static final int TT_BLOCK = 2;

  /** The Constant TT_ERROR. */
  public static final int TT_ERROR = 3;

  /** The reader. */
  private Reader reader;

  /** The symbols. */
  private final List<TokenizerSymbol> symbols;

  /** The push count. */
  private int pushCount;

  /** The token. */
  private final TokenizerToken token;

  /** The upcoming token. */
  private final TokenizerToken upcomingToken;

  /** The cache position. */
  private int cachePosition;

  /** The cache size. */
  private int cacheSize;

  /** The cache buffer. */
  private final char[] cacheBuffer;

  /** The cache pin position. */
  private int cachePinPosition;

  /**
   * Instantiates a new tokenizer.
   */
  public Tokenizer() {
    this(null, 4096);
  }

  /**
   * Instantiates a new tokenizer.
   *
   * @param reader the reader
   */
  public Tokenizer(Reader reader) {
    this(reader, 4096);
  }

  /**
   * Instantiates a new tokenizer.
   *
   * @param reader the reader
   * @param cacheBufferSize the cache buffer size
   */
  public Tokenizer(Reader reader, int cacheBufferSize) {
    symbols = new UniqueList<>();
    token = new TokenizerToken();
    upcomingToken = new TokenizerToken();
    cacheBuffer = new char[cacheBufferSize];
    setReader(reader);
  }

  /**
   * Load cache.
   *
   * @param count the count
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void loadCache(int count) throws IOException {
    int charToRead = count == 0 ? 0 : count - 1;
    if (cachePosition + charToRead >= cacheSize) {
      if (cacheSize == 0) {
        cacheSize = reader.read(cacheBuffer, 0, cacheBuffer.length);
        cachePosition = 0;
      } else if (cacheSize == cacheBuffer.length) {
        // make sure we do not read beyond the stream
        int halfCacheSize = cacheSize / 2;
        // copy the lower half into the upper half
        System.arraycopy(cacheBuffer, halfCacheSize, cacheBuffer, 0, halfCacheSize);
        cachePosition -= halfCacheSize;
        if (cachePinPosition != -1) {
          cachePinPosition -= halfCacheSize;
        }

        int charsRead = reader.read(cacheBuffer, halfCacheSize, cacheSize - halfCacheSize);
        if (charsRead == -1) {
          cacheSize = halfCacheSize;
        } else {
          cacheSize = charsRead + halfCacheSize;
        }
      }
    }
  }

  /**
   * Gets the token.
   *
   * @return the token
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Token getToken() throws IOException {
    if (token.type == Tokenizer.TT_ERROR) {
      return nextToken();
    }
    return token;
  }

  /**
   * Next token.
   *
   * @return the token
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Token nextToken() throws IOException {
    if (pushCount > 0) {
      pushCount--;
      return token;
    }
    if (upcomingToken.type != Tokenizer.TT_ERROR) {
      token.assign(upcomingToken);
      upcomingToken.type = Tokenizer.TT_ERROR;
      return token;
    }

    token.init();
    char[] chr = new char[1];
    while (hasMore()) {
      read(chr, 1);

      int symbolIndex = lookupSymbol(chr[0]);

      if (symbolIndex != -1) {
        // we have found a symbol
        TokenizerToken workToken =
            token.type == Tokenizer.TT_TOKEN && token.text.length() > 0 ? upcomingToken : token;
        TokenizerSymbol symbol = symbols.get(symbolIndex);
        boolean hideSymbol = symbol.hidden;

        if (!hideSymbol) {
          workToken.init();
          workToken.text.append(symbol.startText);
          workToken.type = Tokenizer.TT_SYMBOL;
          workToken.name = symbol.name;
        }

        if (symbol.tailText != null) {
          // the symbol is a block, look for the tailText
          while (hasMore() && !compare(symbol.tailText.toCharArray(), 0)) {
            read(chr, 1);
            if (!hideSymbol) {
              workToken.text.append(chr);
              workToken.innerText.append(chr);
            }
          }

          if (!hideSymbol) {
            workToken.text.append(symbol.tailText);
          }
          workToken.type = Tokenizer.TT_BLOCK;
        }

        if (token.text.length() > 0) {
          break;
        }
      } else {
        token.text.append(chr);
        token.type = Tokenizer.TT_TOKEN;
      }
    }
    return token;
  }

  /**
   * Push back.
   */
  public void pushBack() {
    pushCount++;
  }

  /**
   * Sets the reader.
   *
   * @param reader the new reader
   */
  public void setReader(Reader reader) {
    this.reader = reader;
    cachePosition = 0;
    cachePinPosition = -1;
    cacheSize = 0;
    token.type = TT_ERROR;
    upcomingToken.type = TT_ERROR;
  }

  /**
   * Compare.
   *
   * @param chars the chars
   * @param offs the offs
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private boolean compare(char[] chars, int offs) throws IOException {
    char[] subStr = new char[chars.length - offs];
    cachePinPosition = cachePosition;
    read(subStr, subStr.length);
    for (int i = 0; i < subStr.length; i++) {
      if (subStr[i] != chars[i + offs]) {
        cachePosition = cachePinPosition;
        cachePinPosition = -1;
        return false;
      }
    }
    return true;
  }

  /**
   * Lookup symbol.
   *
   * @param chr the chr
   * @return the int
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private int lookupSymbol(char chr) throws IOException {
    int result = -1;

    Character chrObj = chr;
    int index = Collections.binarySearch(symbols, chrObj);

    if (index >= 0) {
      // the index could be anywhere within a group of symbols with the same first letter
      // so we need to scroll up the group to make sure we start test from the beginning
      while (index > 0 && symbols.get(index - 1).compareTo(chrObj) == 0) {
        index--;
      }
      while (index < symbols.size()) {
        TokenizerSymbol symbol = symbols.get(index);
        if (symbol.compareTo(chrObj) != 0) {
          break;
        }
        if (compare(symbol.startText.toCharArray(), 1)) {
          result = index;
          break;
        }
        index++;
      }
    }
    return result;
  }

  /**
   * Read.
   *
   * @param chrs the chrs
   * @param count the count
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void read(char[] chrs, int count) throws IOException {
    loadCache(count);
    int endPoint = cachePosition + count - 1 >= cacheSize ? cacheSize : cachePosition + count - 1;
    if (cachePosition <= endPoint) {
      System.arraycopy(cacheBuffer, cachePosition, chrs, 0, endPoint - cachePosition + 1);
    }
    cachePosition = endPoint + 1;
  }

  /**
   * Checks for more.
   *
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean hasMore() throws IOException {
    loadCache(0);
    return cachePosition < cacheSize || upcomingToken.type != Tokenizer.TT_ERROR || pushCount > 0;
  }

  /**
   * Adds the symbol.
   *
   * @param text the text
   */
  public void addSymbol(String text) {
    symbols.add(new TokenizerSymbol(null, text, null, false, false, true, false));
  }

  /**
   * Adds the symbol.
   *
   * @param text the text
   * @param hidden the hidden
   */
  public void addSymbol(String text, boolean hidden) {
    symbols.add(new TokenizerSymbol(null, text, null, hidden, false, true, false));
  }

  /**
   * Adds the symbol.
   *
   * @param startText the start text
   * @param endText the end text
   * @param hidden the hidden
   */
  public void addSymbol(String startText, String endText, boolean hidden) {
    symbols.add(new TokenizerSymbol(null, startText, endText, hidden, false, true, false));
  }

  /**
   * Adds the symbol.
   *
   * @param symbol the symbol
   */
  public void addSymbol(TokenizerSymbol symbol) {
    symbols.add(symbol);
  }

  /**
   * Gets the next string.
   *
   * @param defaultValue the default value
   * @return the next string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public String getNextString(String defaultValue) throws IOException {
    return hasMore() ? nextToken().getInnerText() : defaultValue;
  }

  /**
   * Gets the next boolean.
   *
   * @param trueValue the true value
   * @param defaultValue the default value
   * @return the next boolean
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean getNextBoolean(String trueValue, boolean defaultValue) throws IOException {
    return hasMore() ? trueValue.equalsIgnoreCase(nextToken().getInnerText()) : defaultValue;
  }

  /**
   * Gets the next long.
   *
   * @param defaultValue the default value
   * @return the next long
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public long getNextLong(long defaultValue) throws IOException {
    String stval = getNextString(null);

    if (stval == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(stval);
    } catch (NumberFormatException e) {
      logger.trace("", e);
      return defaultValue;
    }
  }

  /**
   * The Class TokenizerToken.
   */
  private static class TokenizerToken implements Token {

    /** The text. */
    final StringBuilder text = new StringBuilder();

    /** The inner text. */
    final StringBuilder innerText = new StringBuilder();

    /** The name. */
    String name = "";

    /** The type. */
    int type = Tokenizer.TT_ERROR;

    /** The line. */
    int line;

    /** The col. */
    int col;

    /**
     * Instantiates a new tokenizer token.
     */
    public TokenizerToken() {
      type = Tokenizer.TT_ERROR;
    }

    @Override
    public String getText() {
      return text.toString();
    }

    @Override
    public String getInnerText() {
      return type == Tokenizer.TT_BLOCK ? innerText.toString() : getText();
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public int getType() {
      return type;
    }

    @Override
    public int getLine() {
      return line;
    }

    @Override
    public int getCol() {
      return col;
    }

    @Override
    public String toString() {
      return getText();
    }

    /**
     * Assign.
     *
     * @param token the token
     */
    public void assign(TokenizerToken token) {
      this.text.setLength(0);
      this.text.append(token.text);
      this.innerText.setLength(0);
      this.innerText.append(token.innerText);
      this.name = token.name;
      this.type = token.type;
      this.col = token.col;
      this.line = token.line;
    }

    /**
     * Inits the.
     */
    public void init() {
      text.setLength(0);
      innerText.setLength(0);
      name = "";
    }

  }

}
