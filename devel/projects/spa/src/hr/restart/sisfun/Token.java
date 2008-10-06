/****license*****************************************************************
**   file: Token.java
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

public class Token {
  public static final int IDENTIFIER_OR_KEYWORD = 1;
  public static final int OPERATOR_OR_SEPARATOR = 2;
  public static final int STRING_LITERAL = 3;
  public static final int INTEGER_LITERAL = 4;
  public static final int DECIMAL_LITERAL = 5;

  private String value;
  private int type;

  public Token(int type, String value) {
    this.type = type;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return value;
  }

  public int getType() {
    return type;
  }

  public boolean isIdent() {
    return type == IDENTIFIER_OR_KEYWORD;
  }

  public boolean isLiteral() {
    return type == STRING_LITERAL || type == INTEGER_LITERAL || type == DECIMAL_LITERAL;
  }

  public boolean isInteger() {
    return type == INTEGER_LITERAL;
  }

  public boolean isString() {
    return type == STRING_LITERAL;
  }

  public boolean isDecimal() {
    return type == DECIMAL_LITERAL;
  }
}
