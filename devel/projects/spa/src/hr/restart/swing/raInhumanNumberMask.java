/****license*****************************************************************
**   file: raInhumanNumberMask.java
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

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.SwingConstants;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raInhumanNumberMask extends raFieldMask {
  private static char dotChar, separatorChar;
  private static String emptyMask;

  protected int decs;

  static {
    NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    String deci = nf.format(0.1);
    if (deci.indexOf('.') >= 0) setMaskCharacters('.', ',');
    else setMaskCharacters(',', '.');
  }

  public static void setMaskCharacters(char dot, char separator) {
    dotChar = dot;
    separatorChar = separator;
    emptyMask = "0" + dot + Aus.string(16, '0');
  }

  public raInhumanNumberMask(JTextField tf) {
    this(tf, 2);
  }

  public raInhumanNumberMask(JTextField tf, int decimalPlaces) {
    super(tf);
    this.decs = decimalPlaces;
    if (this.decs == 7) this.decs = 2;
    tf.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  private boolean delete(boolean del) {
    if (!sel && (!del && cPos == 0 || del && cPos == tLen)) return true;
    VarStr text = new VarStr(tf.getText());
    if (sel) cPos = delete(text, sBeg, sLen);
    else cPos = delete(text, cPos, del ? 0 : -1);
    setText(text.toString());
    tf.setCaretPosition(cPos);
    return true;
  }

  private int delete(VarStr text, int pos, int len) {
    int beg, end, orig = text.length();
    if (len < 0) pos -= (len = (text.charAt(pos - 1) == separatorChar ? 2 : 1));
    else if (len == 0) len = text.charAt(pos) == separatorChar ? 2 : 1;
    beg = Math.max(pos, orig - decs);
    end = Math.min(pos + len, orig - decs - 1);
    if (beg < pos + len) text.delete(beg, pos + len);
    if (pos < end) text.delete(pos, end);
    if (beg < pos + len) {
      text.insert(text.charAt(0) == '-' ? 1 : 0, Aus.string(pos + len - beg, '0'));
      text.remove(dotChar).remove(separatorChar).insert(text.length() - decs, dotChar);
    }
    reformat(text);
    return Math.max(0, text.length() - orig + pos + len);
  }

  protected int insert(VarStr text, int pos, char ch) {
    int orig = text.length();
    if (pos == 0 && orig > 0 && text.charAt(0) == '-') pos = 1;
    text.insert(pos, ch);
    if (pos >= orig - decs)
      text.remove(dotChar).insert(orig - decs, dotChar);        
    reformat(text);
    return Math.max(0, text.length() - orig + pos);
  }

  private VarStr reformat(VarStr text) {
    String suffix = text.right(decs);
    boolean neg = text.charAt(0) == '-';
    text.insert(neg ? 1 : 0, '0');
    text.chop(decs + 1).remove(separatorChar);
    int z = neg ? 1 : 0;
    while (z < text.length() - 1 && text.charAt(z) == '0') ++z;
    text.chopLeft(z);
    for (int i = text.length() - 3; i > 0; i -= 3)
      text.insert(i, separatorChar);
    if (neg) text.insert(0, '-');
    return text.append(dotChar).append(suffix);
  }

  public boolean keypressCopy() {
    tf.copy();
    return true;
  }

  public boolean keypressCharacter(char ch) {
    if (ch >= '0' && ch <= '9') {
      if (tLen == 0) {
        setText(emptyMask.substring(0, decs + 1) + ch);
        tf.setCaretPosition(decs + 2);
      } else {
        VarStr text = new VarStr(tf.getText());
        if (sel) cPos = delete(text, sBeg, sLen);
        cPos = insert(text, cPos, ch);
        setText(text.toString());
        tf.setCaretPosition(cPos);
      }
    } else if (ch == '-' || ch == '+') {
      if (tLen == 0) {
        setText((ch == '-' ? "-" : "") + emptyMask.substring(0, decs + 1));
        tf.setCaretPosition(decs + 2 + (ch == '-' ? 1 : 0));
      } else if (tf.getText().startsWith("-")) {
        tf.select(0, 1);
        tf.replaceSelection("");
        if (sel) tf.setCaretPosition(Math.max(sBeg - 1, 0));
        else tf.setCaretPosition(Math.max(cPos - 1, 0));
      } else if (ch == '-') {
        tf.setCaretPosition(0);
        tf.replaceSelection("-");
        if (sel) tf.setCaretPosition(sBeg + 2);
        else tf.setCaretPosition(cPos + 1);
      }
    }
    return true;
  }

  public boolean keypressCut() {
    if (!sel) return true;
    tf.copy();
    delete(true);
    return true;
  }

  protected void normalizeNumber() {
    int pos = tf.getDocument().getLength() - tf.getCaretPosition();
    VarStr text = new VarStr(tf.getText());
    boolean dot = true;
    for (int i = text.length() - 1; i >= 0; i--)
      if (!(Character.isDigit(text.charAt(i)) ||        // znamenka
          text.charAt(i) == '-' && i == 0 ||          // predznak na prvom mjestu
          dot && !(dot = dot && !(text.charAt(i) == dotChar))))     // samo jedna decimalna tocka
        text.delete(i, i + 1);
    if (text.indexOf(dotChar) == -1) text.append(dotChar).append(Aus.string(decs, '0'));
    else text.append(Aus.string(decs, '0')).truncate(text.indexOf(dotChar) + decs + 1);
    setText(reformat(text).toString());
    tf.setCaretPosition(text.length() - pos);
  }

  public boolean keypressPaste() {
    tf.paste();
    normalizeNumber();
    return true;
  }

  public boolean keypressBackspace() {
    return delete(false);
  }

  public boolean keypressDelete() {
    return delete(true);
  }
}
