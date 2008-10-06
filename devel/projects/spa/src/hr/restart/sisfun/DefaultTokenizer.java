/****license*****************************************************************
**   file: DefaultTokenizer.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.sisfun;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class DefaultTokenizer implements Tokenizer {
  private static final int AFTER_IDENTIFIER = 1;
  private static final int AFTER_WHITESPACE = 2;
  private static final int AFTER_STRING_LITERAL = 3;
  private static final int AFTER_NUMBER_PREFIX = 4;
  private static final int AFTER_NUMBER = 5;
  private static final int AFTER_NUMBER_DOT = 6;
  private static final int AFTER_NUMBER_DOT_ONLY = 7;
  private static final int AFTER_SEPARATOR = 8;


  private String[] twoCharOperators = new String[] {">=", "<="};
  private int numberOfTwoCharOperators = 2;

  private char stringDelimiter = '"';

  private String line;
  private int pos, state;

  public void setStringDelimiter(char strDelim) {
    stringDelimiter = strDelim;
  }

  public void setTwoCharOperators(String[] twoCharOperators) {
    this.twoCharOperators = twoCharOperators;
    numberOfTwoCharOperators = twoCharOperators == null ? 0 : twoCharOperators.length;
  }

  public void addTwoCharOperator(String twoCharOperator) {
    if (numberOfTwoCharOperators == 0)
      twoCharOperators = new String[8];
    else if (numberOfTwoCharOperators == twoCharOperators.length) {
      String[] newArray = new String[numberOfTwoCharOperators * 2];
      System.arraycopy(twoCharOperators, 0, newArray, 0, numberOfTwoCharOperators);
    }
    twoCharOperators[numberOfTwoCharOperators++] = twoCharOperator;
  }

  private boolean isTwoCharOperator(char ch) {
    for (int i = 0; i < numberOfTwoCharOperators; i++)
      if (twoCharOperators[i].charAt(0) == ch) return true;
    return false;
  }

  private boolean isTwoCharOperator(String two) {
    for (int i = 0; i < numberOfTwoCharOperators; i++)
      if (twoCharOperators[i].equals(two)) return true;
    return false;
  }

  public DefaultTokenizer() {
  }

  public void newLine(String line) {
    this.line = line;
    pos = 0;
  }

  public Token next() {
    if (pos >= line.length()) return null;
    int beg = pos;
    state = AFTER_WHITESPACE;
    while (pos <= line.length()) {
      char ch = (pos++ == line.length() ? '\n' : line.charAt(pos - 1));
      switch (state) {
        case AFTER_WHITESPACE:
          if ("\r\n\t ".indexOf(ch) >= 0) beg = pos;
          else if (ch == stringDelimiter) state = AFTER_STRING_LITERAL;
          else if (ch >= '0' && ch <= '9') state = AFTER_NUMBER;
          else if (ch == '+' || ch == '-') state = AFTER_NUMBER_PREFIX;
          else if (ch == '.') state = AFTER_NUMBER_DOT_ONLY;
          else if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') state = AFTER_IDENTIFIER;
          else if (isTwoCharOperator(ch)) state = AFTER_SEPARATOR;
          else return new Token(Token.OPERATOR_OR_SEPARATOR, line.substring(beg, pos));
          break;
        case AFTER_STRING_LITERAL:
          if (ch == stringDelimiter)
            return new Token(Token.STRING_LITERAL, line.substring(beg + 1, pos));
          else if (ch == '\r' || ch == '\n')
            throw new TokenError("Unterminated string constant", pos);
          break;
        case AFTER_IDENTIFIER:
          if (!(ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' ||
              ch >= '0' && ch <= '9' || ch == '_'))
            return new Token(Token.IDENTIFIER_OR_KEYWORD, line.substring(beg, --pos));
          break;
        case AFTER_SEPARATOR:
          if (isTwoCharOperator(line.substring(beg, pos)))
            return new Token(Token.OPERATOR_OR_SEPARATOR, line.substring(beg, pos));
          else return new Token(Token.OPERATOR_OR_SEPARATOR, line.substring(beg, --pos));
        case AFTER_NUMBER_PREFIX:
          if (ch >= '0' && ch <= '9') state = AFTER_NUMBER;
          else if (ch == '.') state = AFTER_NUMBER_DOT;
          else if (isTwoCharOperator(ch)) state = AFTER_SEPARATOR;
          else return new Token(Token.OPERATOR_OR_SEPARATOR, line.substring(beg, --pos));
          break;
        case AFTER_NUMBER:
          if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')
            throw new TokenError("Invalid token", pos);
          else if (ch == '.') state = AFTER_NUMBER_DOT;
          else if (ch < '0' || ch > '9')
            return new Token(Token.INTEGER_LITERAL, line.substring(beg, --pos));
          break;
        case AFTER_NUMBER_DOT_ONLY:
          if (ch >= '0' && ch <= '9') state = AFTER_NUMBER_DOT;
          else if (isTwoCharOperator(ch)) state = AFTER_SEPARATOR;
          else return new Token(Token.OPERATOR_OR_SEPARATOR, line.substring(beg, --pos));
          break;
        case AFTER_NUMBER_DOT:
          if (ch >= '0' && ch <= '9') state = AFTER_NUMBER_DOT;
          else if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')
            throw new TokenError("Invalid token", pos);
          else return new Token(Token.DECIMAL_LITERAL, line.substring(beg, --pos));
          break;
      }
    }
    return null;
  }
}

