/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

public class Tokenizer {
    public static final int TT_TOKEN = 0;
    public static final int TT_SYMBOL = 1;
    public static final int TT_BLOCK = 2;
    public static final int TT_ERROR = 3;

    private Reader reader;
    private final List symbols;
/*
    private boolean enableHidden;
    private boolean hideNonSymbols;
*/
    private int pushCount = 0;
    //
    private final TokenizerToken token;
    private final TokenizerToken upcomingToken;
    //
    private int cachePosition;
    private int cacheSize;
    private final char[] cacheBuffer;
    private int cachePinPosition;

    public Tokenizer() {
        this(null, 4096);
    }

    public Tokenizer(Reader reader) {
        this(reader, 4096);
    }

    public Tokenizer(Reader reader, int cacheBufferSize) {
        symbols = new UniqueList();
        token = new TokenizerToken();
        upcomingToken = new TokenizerToken();
        cacheBuffer = new char[cacheBufferSize];
        setReader(reader);
    }

    private void loadCache(int count) throws IOException {
        int charToRead = count == 0 ? 0 : count - 1;
        if(cachePosition + charToRead  >= cacheSize) {
            if (cacheSize == 0) {
                cacheSize = reader.read(cacheBuffer, 0, cacheBuffer.length);
                cachePosition = 0;
            } else if (cacheSize == cacheBuffer.length){
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

    public Token getToken() throws IOException {
        if (token.type == Tokenizer.TT_ERROR) {
            return nextToken();
        }
        return token;
    }

    public Token nextToken() throws IOException {
        if (pushCount > 0) {
            pushCount--;
            return token;
        } else if (upcomingToken.type != Tokenizer.TT_ERROR) {
            token.assign(upcomingToken);
            upcomingToken.type = Tokenizer.TT_ERROR;
            return token;
        } else {
            token.init();
            char[] b = new char[1];
            while (hasMore()) {
                read(b, 1);

                int symbolIndex = lookupSymbol(b[0]);

                if (symbolIndex != -1) {
                    // we have found a symbol
                    TokenizerToken workToken = token.type == Tokenizer.TT_TOKEN && token.text.length() > 0 ? upcomingToken : token;
                    TokenizerSymbol symbol = ((TokenizerSymbol)symbols.get(symbolIndex));
                    boolean hideSymbol = symbol.hidden;

                    if (!hideSymbol) {
                        workToken.init();
                        workToken.text.append(symbol.startText);
                        workToken.type = Tokenizer.TT_SYMBOL;
                        workToken.name = symbol.name;
                    }

                    if (symbol.tailText != null) {
                        // the symbol is a block
                        // look for the tailText
                        while (hasMore() && !compare(symbol.tailText.toCharArray(), 0)) {
                            read(b, 1);
                            if (!hideSymbol) {
                                workToken.text.append(b);
                                workToken.innerText.append(b);
                            }
                        }

                        if (!hideSymbol) {
                            workToken.text.append(symbol.tailText);
                        }
                        workToken.type = Tokenizer.TT_BLOCK;
                    }

                    //if (!hideSymbol) break;
                    if (token.text.length() > 0) {
                        break;
                    }
                } else {
                    token.text.append(b);
                    token.type = Tokenizer.TT_TOKEN;
                }
            }
        }
        return token;
    }

    public void pushBack() {
        pushCount++;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        cachePosition = 0;
        cachePinPosition = -1;
        cacheSize = 0;
        token.type = TT_ERROR;
        upcomingToken.type = TT_ERROR;
    }

    private boolean compare(char[] chars, int offs) throws IOException {
        char[] b = new char[chars.length-offs];
        cachePinPosition = cachePosition;
        read(b, b.length);
        for (int i=0; i < b.length; i++) {
            if (b[i] != chars[i+offs]) {
                cachePosition = cachePinPosition;
                cachePinPosition = -1;
                return false;
            }
        }
        return true;
    }

    private int lookupSymbol(char b) throws IOException {
        int result = -1;

        Character c = new Character(b);
        int index = Collections.binarySearch(symbols, c);

        if (index >= 0) {
            // the index could be anywhere within a group of sybols with the same first letter
            // so we need to scroll up the group to make sure we start test from the beginning
            while (index > 0 && ((TokenizerSymbol) symbols.get(index-1)).compareTo(c) == 0) {
                index--;
            }
            while (index < symbols.size()) {
                TokenizerSymbol symbol = ((TokenizerSymbol) symbols.get(index));
                if (symbol.compareTo(c) == 0) {
                    if (compare(symbol.startText.toCharArray(), 1)) {
                        result = index;
                        break;
                    } else {
                        index++;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    private void read(char[] b, int count) throws IOException {
        loadCache(count);
        int endPoint = cachePosition + count - 1 >= cacheSize ? cacheSize : cachePosition + count - 1;
        if (cachePosition <= endPoint) {
            System.arraycopy(cacheBuffer, cachePosition, b, 0, endPoint - cachePosition + 1);
        }
        cachePosition = endPoint+1;
    }

    public boolean hasMore() throws IOException {
        loadCache(0);
        return (cachePosition < cacheSize) || upcomingToken.type != Tokenizer.TT_ERROR || pushCount > 0;
    }

    public void addSymbol(String text) {
        symbols.add(new TokenizerSymbol(null, text, null, false, false, true, false));
    }

    public void addSymbol(String text, boolean hidden) {
        symbols.add(new TokenizerSymbol(null, text, null, hidden, false, true, false));
    }

    public void addSymbol(String startText, String endText, boolean hidden) {
        symbols.add(new TokenizerSymbol(null, startText, endText, hidden, false, true, false));
    }

    public void addSymbol(TokenizerSymbol symbol) {
        symbols.add(symbol);
    }

    public String getNextString(String defaultValue) throws IOException {
        return hasMore() ? nextToken().getInnerText() : defaultValue;
    }

    public boolean getNextBoolean(String trueValue, boolean defaultValue) throws IOException {
        return hasMore() ? trueValue.equalsIgnoreCase(nextToken().getInnerText()) : defaultValue;
    }

    public long getNextLong(long defaultValue) throws IOException {
        String stval = getNextString(null);

        if (stval == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(stval);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

class TokenizerToken implements Token{
    final StringBuffer text = new StringBuffer();
    final StringBuffer innerText = new StringBuffer();
    String name = "";
    int type = Tokenizer.TT_ERROR;
    int line = 0;
    int col = 0;

    public TokenizerToken() {
        type = Tokenizer.TT_ERROR;
    }

    public String getText() {
        return text.toString();
    }

    public String getInnerText() {
        return type == Tokenizer.TT_BLOCK ? innerText.toString() : getText();
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return getText();
    }

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

    public void init() {
        text.setLength(0);
        innerText.setLength(0);
        name = "";
    }
}
