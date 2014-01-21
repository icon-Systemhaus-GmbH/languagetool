/* LanguageTool, a natural language style checker
 * Copyright (C) 2012 Daniel Naber (http://www.danielnaber.de)
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
package org.languagetool.commandline;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.languagetool.Language;
import org.languagetool.tools.LanguageIdentifierTools;

/**
 * Parser for the command line arguments.
 */
public class CommandLineParser {

  public CommandLineOptions parseOptions(String[] args) {
    if (args.length < 1 || args.length > 10) {
      throw new WrongParameterNumberException();
    }
    final CommandLineOptions options = new CommandLineOptions();
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--version")) {
        options.setPrintVersion(true);
      } else if (args[i].equals("--list")) {
        options.setPrintLanguages(true);
      } else if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help") || args[i].equals("--?")) {
        options.setPrintUsage(true);
      } else if (args[i].equals("-adl") || args[i].equals("--autoDetect")) {    // set autoDetect flag
        // also initialize the other language profiles for the LanguageIdentifier
        LanguageIdentifierTools.addLtProfiles();
        options.setAutoDetect(true);
      } else if (args[i].equals("-v") || args[i].equals("--verbose")) {
        options.setVerbose(true);
      } else if (args[i].equals("-t") || args[i].equals("--taggeronly")) {
        options.setTaggerOnly(true);
        if (options.isListUnknown()) {
          throw new IllegalArgumentException("You cannot list unknown words when tagging only");
        }
        if (options.isApplySuggestions()) {
          throw new IllegalArgumentException("You cannot apply suggestions when tagging only");
        }
      } else if (args[i].equals("-r") || args[i].equals("--recursive")) {
        options.setRecursive(true);
      } else if (args[i].equals("-b2") || args[i].equals("--bitext")) {
        options.setBitext(true);
      } else if (args[i].equals("-d") || args[i].equals("--disable")) {
        if (options.getEnabledRules().length > 0) {
          throw new IllegalArgumentException("You cannot specify both enabled and disabled rules");
        }
        checkArguments("-d/--disable", i, args);
        final String rules = args[++i];
        options.setDisabledRules(rules.split(","));
      } else if (args[i].equals("-e") || args[i].equals("--enable")) {
        if (options.getDisabledRules().length > 0) {
          throw new IllegalArgumentException("You cannot specify both enabled and disabled rules");
        }
        checkArguments("-e/--enable", i, args);
        final String rules = args[++i];
        options.setEnabledRules(rules.split(","));
      } else if (args[i].equals("-l") || args[i].equals("--language")) {
        checkArguments("-l/--language", i, args);
        options.setLanguage(getLanguage(args[++i]));
      } else if (args[i].equals("-m") || args[i].equals("--mothertongue")) {
        checkArguments("-m/--mothertongue", i, args);
        options.setMotherTongue(getLanguage(args[++i]));
      } else if (args[i].equals("-c") || args[i].equals("--encoding")) {
        checkArguments("-c/--encoding", i, args);
        options.setEncoding(args[++i]);
      } else if (args[i].equals("-u") || args[i].equals("--list-unknown")) {
        options.setListUnknown(true);
        if (options.isTaggerOnly()) {
          throw new IllegalArgumentException("You cannot list unknown words when tagging only");
        }
      } else if (args[i].equals("-b")) {
        options.setSingleLineBreakMarksParagraph(true);
      } else if (args[i].equals("--api")) {
        options.setApiFormat(true);
        if (options.isApplySuggestions()) {
          throw new IllegalArgumentException("API format makes no sense for automatic application of suggestions");
        }
      } else if (args[i].equals("-a") || args[i].equals("--apply")) {
        options.setApplySuggestions(true);
        if (options.isTaggerOnly()) {
          throw new IllegalArgumentException("You cannot apply suggestions when tagging only");
        }
        if (options.isApiFormat()) {
          throw new IllegalArgumentException("API format makes no sense for automatic application of suggestions");
        }
      } else if (args[i].equals("-p") || args[i].equals("--profile")) {
        options.setProfile(true);
        if (options.isApiFormat()) {
          throw new IllegalArgumentException("API format makes no sense for profiling");
        }
        if (options.isApplySuggestions()) {
          throw new IllegalArgumentException("Applying suggestions makes no sense for profiling");
        }
        if (options.isTaggerOnly()) {
          throw new IllegalArgumentException("Tagging makes no sense for profiling");
        }
      } else if (args[i].equals("--xmlfilter")) {
        options.setXmlFiltering(true);
      } else if (i == args.length - 1) {
        options.setFilename(args[i]);
      } else {
        throw new UnknownParameterException("Unknown parameter: " + args[i]);
      }
    }
    return options;
  }

  public void printUsage() {
    printUsage(System.out);
  }

  /*
   * NOTE: please keep http://wiki.languagetool.org/command-line-options
   * up-to-date if you add an option
   */
  public void printUsage(PrintStream stream) {
    stream.println("Usage: java -jar LanguageTool.jar [OPTION]... FILE\n"
            + " FILE                      plain text file to be checked\n"
            + " Available options:\n"
            + "  -r, --recursive          work recursively on directory, not on a single file\n"
            + "  -c, --encoding ENC       character set of the input text, e.g. utf-8 or latin1\n"
            + "  -b                       assume that a single line break marks the end of a paragraph\n"
            + "  -l, --language LANG      the language code of the text, e.g. en for English, en-GB for British English\n"
            + "  --list                   print all available languages and exit\n"
            + "  -adl, --autoDetect       auto-detect the language of the input text\n"
            + "  -m, --mothertongue LANG  the language code of your first language, used to activate false-friend checking\n"
            + "  -d, --disable RULES      a comma-separated list of rule ids to be disabled (use no spaces between ids)\n"
            + "  -e, --enable RULES       a comma-separated list of rule ids to be enabled (use no spaces between ids)\n"
            + "  -t, --taggeronly         don't check, but only print text analysis (sentences, part-of-speech tags)\n"
            + "  -u, --list-unknown       also print a summary of words from the input that LanguageTool doesn't know\n"
            + "  -b2, --bitext            check bilingual texts with a tab-separated input file,\n"
            + "                           see http://languagetool.wikidot.com/checking-translations-bilingual-texts\n"
            + "  --api                    print results as XML\n"
            + "  -p, --profile            print performance measurements\n"
            + "  -v, --verbose            print text analysis (sentences, part-of-speech tags) to STDERR\n"
            + "  --version                print LanguageTool version number and exit\n"
            + "  -a, --apply              automatically apply suggestions if available, printing result to STDOUT\n"
            + "  --xmlfilter              remove XML/HTML elements from input before checking (this is deprecated)");
  }

  private void checkArguments(String option, int argParsingPos, String[] args) {
    if (argParsingPos + 1 >= args.length) {
      throw new IllegalArgumentException("Missing argument to " + option + " command line option.");
    }
  }

  private Language getLanguage(String userSuppliedLangCode) {
    try {
      return Language.getLanguageForShortName(userSuppliedLangCode);
    } catch (IllegalArgumentException e){
      final List<String> supportedLanguages = new ArrayList<String>();
      for (final Language lang : Language.LANGUAGES) {
        supportedLanguages.add(lang.getShortNameWithVariant());
      }
      Collections.sort(supportedLanguages);
      throw new IllegalArgumentException("Unknown language '" + userSuppliedLangCode
                  + "'. Supported languages are: " + supportedLanguages);
    }
  }

}
