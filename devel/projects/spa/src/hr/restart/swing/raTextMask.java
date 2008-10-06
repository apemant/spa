/****license*****************************************************************
**   file: raTextMask.java
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
package hr.restart.swing;

import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import javax.swing.JTextField;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raTextMask extends raFieldMask {
  public static final int ALL = 0;
  public static final int ALPHANUM = 1;
  public static final int ALPHA = 2;
  public static final int NUMERIC = 3;
  public static final int DIGITS = 4;
  public static final int CUSTOM = 5;

  public static final int PLACEHOLDER = 128;

  private static char defaultPlaceHolderChar;

  private char placeHolderChar;

  private int maxLength, charType;
  private boolean allowSpaces, placeHolder;
  private String allowedChars;

  static {
    setDefaultMaskCharacter('_');
  }

  public static void setDefaultMaskCharacter(char placeHolder) {
    defaultPlaceHolderChar = placeHolder;
  }

  public static char getDefaultMaskCharacter() {
    return defaultPlaceHolderChar;
  }

  public raTextMask(JTextField tf, int maxLength, boolean allowSpaces, int type) {
    super(tf);
    this.maxLength = maxLength;
    this.allowSpaces = allowSpaces;
    charType = type & 127;
    placeHolder = (type & PLACEHOLDER) == PLACEHOLDER;
    placeHolderChar = defaultPlaceHolderChar;
  }

  public raTextMask(JTextField tf, int maxLength, boolean allowSpaces) {
    super(tf);
    this.maxLength = maxLength;
    this.allowSpaces = allowSpaces;
    charType = ALL;
    placeHolder = false;
    placeHolderChar = defaultPlaceHolderChar;
  }

  public raTextMask(JTextField tf, int maxLength) {
    super(tf);
    this.maxLength = maxLength;
    allowSpaces = true;
    charType = ALL;
    placeHolder = false;
    placeHolderChar = defaultPlaceHolderChar;
  }

  public raTextMask(JTextField tf) {
    super(tf);
    maxLength = -1;
    allowSpaces = true;
    charType = ALL;
    placeHolder = false;
    placeHolderChar = defaultPlaceHolderChar;
  }

  public boolean isMasked() {
    return placeHolder;
  }

  public void setMaskCharacter(char placeHolder) {
    placeHolderChar = placeHolder;
  }

  public char getMaskCharacter() {
    return placeHolderChar;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public void setAllowSpaces(boolean allowSpaces) {
    this.allowSpaces = allowSpaces;
  }

  public void setAllowedCharacters(int type) {
    charType = type;
  }

  public void setAllowedCharacters(String chars) {
    charType = CUSTOM;
    allowedChars = chars;
  }

  private void removeSelection(boolean total) {
    tf.replaceSelection(insert || (total && sLen == tLen) ? "" :
           Aus.string(sLen, placeHolder ? placeHolderChar : ' '));
    setCaret(sBeg);
    findPos();
  }

  private void setCaret(int pos) {
    tf.setCaretPosition(Math.max(0, Math.min(pos, tf.getDocument().getLength())));
  }

  private boolean characterAllowed(char ch) {
    if (ch == ' ' || charType == ALL) return true;
    if (charType == ALPHANUM &&
       (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z' ||
        ch >= 'a' && ch <= 'z' || ch == '_')) return true;
    if (charType == ALPHA &&
       (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')) return true;
    if (charType == NUMERIC &&
       (ch >= '0' && ch <= '9' || (ch == '-' && cPos == 0 && tLen > 0))) return true;
    if (charType == DIGITS && ch >= '0' && ch <= '9') return true;
    return (charType == CUSTOM && allowedChars.indexOf(ch) >= 0);
  }

  public boolean keypressCopy() {
    tf.copy();
    return true;
  }

  public boolean keypressCharacter(char ch) {
    if (ch == ' ' && !allowSpaces) return true;
    if (!characterAllowed(ch)) return true;
    if (sel) removeSelection(false);
    if (insert) {
      if (maxLength > 0) {
        if (cPos >= maxLength) return true;
        if (cPos < tLen && tLen >= maxLength) {
          try {
            char last = tf.getText(tLen - 1, 1).charAt(0);
            if (last != ' ' && last != placeHolderChar) return true;
          } catch (Exception e) {}
        }
      }
    } else if (maxLength < 0 || cPos < maxLength) {
      if (cPos < tLen) tf.moveCaretPosition(cPos + 1);
    } else return true;
    tf.replaceSelection(String.valueOf(ch));
    if (!allowSpaces) updateText();
    setCaret(cPos + 1);
    return true;
  }

  public boolean keypressCut() {
    if (!sel) return true;
    if (insert) {
      tf.cut();
      if (!allowSpaces) updateText();
      setCaret(sBeg);
    } else {
      tf.copy();
      removeSelection(false);
    }
    return true;
  }

  public boolean keypressPaste() {
    tf.paste();
    int now = tf.getCaretPosition(), len = tf.getDocument().getLength();
    if (!insert && !sel && now > cPos) {
      tf.moveCaretPosition(now + now - cPos > len ? len : now + now - cPos);
      tf.replaceSelection("");
    }
    updateText();
    setCaret(now);
    return true;
  }

  private boolean delBack(int move) {
    if (sel) {
      removeSelection(true);
      if (!allowSpaces && tLen > 0) updateText();
      setCaret(cPos);
    } else if (cPos + move >= 0 && cPos + move <= tLen) {
      tf.moveCaretPosition(cPos + move);
      findSelection();
      removeSelection(false);
      if (!allowSpaces) updateText();
      setCaret(cPos);
    }
    return true;
  }

  public boolean keypressBackspace() {
    return delBack(-1);
  }

  public boolean keypressDelete() {
    return delBack(1);
  }

  public void updateText() {
    VarStr text = new VarStr(tf.getText());
    if (!allowSpaces) text.trim();
    if (maxLength > 0) text.truncate(maxLength);
    if (placeHolder) text.padd(maxLength - text.length(), placeHolderChar);
    setText(text.toString());
//    if (tf.getCaretPosition() > text.length())
//      tf.setCaretPosition(text.length());
  }

//  public boolean validate() {
//    VarStr text = new VarStr(tf.getText());
//    if (maxLength > 0 && text.length() > maxLength) return false;
//    if (!allowSpaces) {
//      text.replaceAll('_', ' ').trim();
//      if (text.countOccurences(' ') > 0) return false;
//      tf.setText(text.toString());
//    }
//    return true;
//  }

//  public void resolveText() {
//    resolve = false;
//    if (!validate()) tf.setText("");
//  }
}
