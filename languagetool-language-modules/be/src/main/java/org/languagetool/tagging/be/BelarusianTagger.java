/* LanguageTool, a natural language style checker 
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.tagging.be;

import java.util.ArrayList;
import java.util.List;

import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.Tagger;

/**
 * Belarusian Tagger.
 * 
 * Copyright (C) 2010 Alex Buloichik (alex73mail@gmail.com)
 */
public class BelarusianTagger implements Tagger {

    @Override
    public List<AnalyzedTokenReadings> tag(List<String> sentenceTokens) {
      final List<AnalyzedTokenReadings> tokenReadings = new ArrayList<AnalyzedTokenReadings>();
      for (String word : sentenceTokens) {
        final List<AnalyzedToken> l = new ArrayList<AnalyzedToken>();
        // a real tagger would need to assign a POS tag
        // in the next line instead of null:
        l.add(new AnalyzedToken(word, null, null));
        tokenReadings.add(new AnalyzedTokenReadings(l, 0));
      }
      return tokenReadings;
    }

    @Override
    public AnalyzedTokenReadings createNullToken(String token, int startPos) {
      return new AnalyzedTokenReadings(new AnalyzedToken(token, null, null),
              startPos);
    }

    @Override
    public AnalyzedToken createToken(String token, String posTag) {
      return new AnalyzedToken(token, posTag, null);
    }
}
