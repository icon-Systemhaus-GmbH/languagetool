/* LanguageTool, a natural language style checker 
 * Copyright (C) 2010 Marcin Miłkowski (www.languagetool.org)
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
package de.danielnaber.languagetool.rules.bitext;

import java.io.IOException;

import de.danielnaber.languagetool.AnalyzedSentence;
import de.danielnaber.languagetool.AnalyzedTokenReadings;
import de.danielnaber.languagetool.rules.RuleMatch;

/**
 * Checks if the translation for segments that have more than two words
 * is different.
 * 
 * @author Marcin Miłkowski
 *
 */
public class SameTranslationRule extends BitextRule {

  static final String MSG = "Source and target translation are the same!"; 
  
  @Override
  public String getDescription() { 
    return "Check if translation is the same as source";
  }
  
  @Override
  public String getId() {
    return "SAME_TRANSLATION";
  }

  public String getMessage() {
    return MSG;
  }
  
  /**
   * This method makes no sense for bitext, return null?? 
   */
  @Override
  public RuleMatch[] match(AnalyzedSentence text) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RuleMatch[] match(AnalyzedSentence sourceText,
      AnalyzedSentence targetText) throws IOException {

    //This is just heuristics, checking word count
    if (sourceText.getTokensWithoutWhitespace().length > 3 
        && getPureText(sourceText).equals(getPureText(targetText))) {
      final RuleMatch[] rm = new RuleMatch[1];
      final AnalyzedTokenReadings[] tokens = targetText.getTokens();
      final int len = tokens[tokens.length - 1].getStartPos() + tokens[tokens.length - 1].getToken().length();
      rm[0] = new RuleMatch(this, 1, len, MSG);
      return rm;
    }
    return new RuleMatch[0];
  }
  
  private static String getPureText(AnalyzedSentence text) {
    final StringBuilder sb = new StringBuilder();
    for (AnalyzedTokenReadings token : text.getTokens()) {
      sb.append(token.getToken());
    }
    return sb.toString();
  }

  public void reset() {
  }

}
