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

public class TokenizerSymbol implements Comparable{
    final String name;
    final String startText;
    final String tailText;
    final boolean hidden;
    final boolean decodePaired;
    final boolean enabled;
    final boolean canBeNested;

    public TokenizerSymbol(String name, String startText, String tailText,
                           boolean hidden, boolean decodePaired, boolean enabled, boolean canBeNested) {
        this.name = name;
        this.startText = startText;
        this.tailText = tailText;
        this.hidden = hidden;
        this.decodePaired = decodePaired;
        this.enabled = enabled;
        this.canBeNested = canBeNested;
    }

    public int compareTo(Object o) {
        if (o instanceof Character) {
            return compareTo((Character) o);
        } else {
            return compareTo((TokenizerSymbol) o);
        }
    }

    public int compareTo(Character c) {
        return c.charValue() - startText.charAt(0);
    }

    public int compareTo(TokenizerSymbol symbol) {
        return symbol.startText.compareTo(startText);
    }
}
