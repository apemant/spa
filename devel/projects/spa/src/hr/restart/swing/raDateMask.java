/****license*****************************************************************
**   file: raDateMask.java
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
import javax.swing.SwingConstants;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raDateMask extends raFieldMask {
  private static boolean defaultSmartDate;
  private static char placeHolderChar, separatorChar;
  private static String placeHolder, separator;
  private static String emptyMask;

  private boolean smartDate = defaultSmartDate;

  static {
    setDefaultSmartDate(true);
    setMaskCharacters('_', '-');
  }

  public static void setDefaultSmartDate(boolean smartDate) {
    defaultSmartDate = smartDate;
  }

  public static void setMaskCharacters(char placeHolder, char separator) {
    placeHolderChar = placeHolder;
    separatorChar = separator;
    raDateMask.placeHolder = String.valueOf(placeHolder);
    raDateMask.separator = String.valueOf(separator);
    emptyMask = Aus.string(2, placeHolder) + separator +
                Aus.string(2, placeHolder) + separator +
                Aus.string(4, placeHolder);
  }

  public void setSmartDate(boolean smartDate) {
    this.smartDate = smartDate;
  }

  public raDateMask(JTextField tf) {
    super(tf);
    tf.setHorizontalAlignment(SwingConstants.CENTER);
  }
  public boolean keypressCopy() {
    tf.copy();
    return true;
  }

  private int getFieldEnd(int pos) {
    return pos <= 2 ? 2 : (pos <= 5 ? 5 : 10);
  }

  private void checkSmartDate(int pos) {
    if (smartDate) {
      DateParser dp = new DateParser(tf.getText());
      if (getFieldEnd(pos) == 2) {
        String guess = dp.getGuessDay(pos != 2);
        if (pos == 2 || dp.d > 3) {
          setText(guess);
          pos = guess.startsWith(placeHolder) ? 0 : 3;
        } else if (!dp.ds.endsWith(placeHolder))
          setText(dp.ds.charAt(0) + placeHolder + separator + dp.ms + separator + dp.ys);
      } else if (getFieldEnd(pos) == 5) {
        String guess = dp.getGuessMonth(pos != 5);
        if (pos == 5 || dp.m > 1 && dp.ms.charAt(0) > '1') {
          setText(guess);
          pos = guess.startsWith(placeHolder) ? 0 : (dp.m == 0 ? 3 : 6);
        } else if (!dp.ms.endsWith(placeHolder))
          setText(dp.ds + separator + dp.ms.charAt(0) + placeHolder + separator + dp.ys);
      } else if (dp.y > 0 && dp.m == 2 && dp.d == 0 && pos == 10) {
        setText(dp.getResult());
        pos = 0;
      }
    } else if (pos == 2 || pos == 5) ++pos;
    tf.setCaretPosition(pos);
  }

  public boolean keypressCharacter(char ch) {
    if (ch >= '0' && ch <= '9') {
      if (!sel) {
        if (tLen == 0) {
          setText(ch + emptyMask.substring(1));
          checkSmartDate(1);
        } else if (cPos != 2 && cPos != 5 && cPos < 10) {
          if (insert) {
            tf.moveCaretPosition(getFieldEnd(cPos));
            String s = tf.getSelectedText();
            tf.replaceSelection(s.length() <= 1 ? String.valueOf(ch) :
                                ch + s.substring(0, s.length() - 1));
          } else {
            tf.moveCaretPosition(cPos + 1);
            tf.replaceSelection(String.valueOf(ch));
          }
          checkSmartDate(cPos + 1);
        }
      } else {
        VarStr text = getSelectionReplaced(ch);
        int off = text.indexOf(ch, sBeg);
        if (off >= sEnd) off = -1;
        if (insert) {
          setText(text.toString());
          tf.setCaretPosition(sEnd);
        } else tf.replaceSelection(text.mid(sBeg, sEnd));
        if (off >= 0) checkSmartDate(off + 1);
      }
    } else if ((ch == '-' || ch == '.' || ch == ',') && !sel &&
      (cPos == 1 || cPos == 2 || (cPos >= 4 && cPos <= 8))) {
      if (cPos == 2 || cPos == 5) tf.setCaretPosition(cPos + 1);
      if (cPos == 1 || cPos == 4 || smartDate) {
        DateParser dp = new DateParser(tf.getText());
        if (cPos == 1 || cPos == 2) {
          setText(dp.getGuessDay(cPos != 2));
          tf.setCaretPosition(3);
        } else if (cPos == 4 || cPos == 5) {
          String guess = dp.getGuessMonth(cPos != 5);
          if (!smartDate) {
            setText(dp.ds + guess.substring(2, 6) + dp.ys);
            tf.setCaretPosition(6);
          } else {
            setText(guess);
            tf.setCaretPosition(guess.startsWith(placeHolder) ? 0 : (dp.m == 0 ? 3 : 6));
          }
        } else if (cPos > 6 && Aus.isDigit(dp.ys.substring(0, cPos - 6))) {
          String prefix = Aus.getNumber(dp.ys.substring(0, cPos - 6)) < 37 ? "2000" : "1900";
          setText(dp.ds + separator + dp.ms + separator +
                     prefix.substring(0, 10 - cPos) + dp.ys.substring(0, cPos - 6));
          checkSmartDate(10);
        }
      }
    }
    return true;
  }

  public boolean keypressCut() {
    if (!sel) return true;
    tf.copy();
    removeSelection();
    return true;
  }

  public void updateText() {
    int pos = tf.getCaretPosition();
    DateParser dp = new DateParser(tf.getText());
    if (dp.d > 0 && dp.m > 0 || dp.d > 0 && dp.y > 0 ||
        dp.m > 0 && dp.y > 0) {
      setText(dp.getResult());
      if (dp.d == 0) pos = 0;
      else if (dp.m == 0) pos = 3;
      else if (dp.y == 0) pos = 6;
    } else
      setText(dp.ds + separator + dp.ms + separator + dp.ys);
    if (pos > dp.getResult().length()) pos = dp.getResult().length();
    tf.setCaretPosition(pos);
  }

  public boolean keypressPaste() {
    tf.paste();
    updateText();
    return true;
  }

  public boolean keypressBackspace() {
    if (isEmpty()) return true;
    else if (sel) return removeSelection();
    else if (cPos == 0) return true;
    if (cPos == 3 || cPos == 6) --cPos;
    tf.setCaretPosition(insert ? getFieldEnd(cPos) : cPos);
    tf.moveCaretPosition(cPos - 1);
    tf.replaceSelection(!insert ? placeHolder :
       tf.getSelectedText().substring(1).concat(placeHolder));
    tf.setCaretPosition(cPos - 1);
    return true;
  }

  public boolean keypressDelete() {
    if (isEmpty()) return true;
    else if (sel) return removeSelection();
    else if (cPos == tLen || cPos == 2 || cPos == 5) return true;
    tf.moveCaretPosition(getFieldEnd(cPos));
    tf.replaceSelection(tf.getSelectedText().substring(1).concat(placeHolder));
    tf.setCaretPosition(cPos);
    return true;
  }

  private VarStr getSelectionReplaced(char ch) {
    VarStr orig = new VarStr(tf.getText());
    int insertPos = sBeg - 1;
    for (int i = sBeg; i < sEnd; i++)
      if (Character.isDigit(orig.charAt(i)) || orig.charAt(i) == placeHolderChar)
        if (ch != '\0' && insertPos < sBeg)
          orig.setCharAt(insertPos = i, ch);
        else orig.setCharAt(i, placeHolderChar);
    if (insert) {
      int pos = orig.lastIndexOf(separatorChar, sEnd);
      int end = orig.indexOf(separatorChar, sEnd);
      if (end < 0) end = orig.length();
      if (pos < insertPos) pos = insertPos;
      orig.insert(end, Aus.string(sEnd - pos - 1, placeHolderChar));
      orig.delete(pos + 1, sEnd);
    }
    return orig;
  }

  private boolean removeSelection() {
    if (sLen < tLen) {
      VarStr text = getSelectionReplaced('\0');
      if (insert) setText(text.toString());
      else tf.replaceSelection(text.mid(sBeg, sEnd));
      tf.setCaretPosition(sBeg);
    } else tf.replaceSelection("");
    return true;
  }

  private boolean isEmpty() {
    if (tLen == 0) {
      setText(emptyMask);
      tf.setCaretPosition(0);
      return true;
    } else return false;
  }

  static class DateParser {
    public int d, m, y;
    public String ds, ms, ys;
    private String result;
    public DateParser(String date) {
      ds = ms = Aus.string(2, placeHolderChar);
      ys = Aus.string(4, placeHolderChar);
      if (date == null || date.length() == 0)
        result = emptyMask;
      else parseDate(date);
    }

    public String getResult() {
      return result;
    }

    public String getGuessDay(boolean mid) {
      int day = Aus.getNumber(ds.replace(placeHolderChar, ' ').trim());
      if (day > 9 && mid) day /= 10;
      return (day < 1 || day > 31 ? Aus.string(2, placeHolderChar) : snum(d = day, 2)) +
        separatorChar + (m > 0 && d > days(m, y) ? Aus.string(2, placeHolderChar) : ms) +
        separatorChar + ys;
    }

    public String getGuessMonth(boolean mid) {
      int month = Aus.getNumber(ms.replace(placeHolderChar, ' ').trim());
      if (month > 9 && mid) month /= 10;
      return (Aus.getNumber(ds) > days(month, 0) ? Aus.string(2, placeHolderChar) : ds) +
        separatorChar + (month < 1 || month > 12 ? Aus.string(2, placeHolderChar) :
        snum(m = month, 2)) + separatorChar + (Aus.getNumber(ds) == 29 && m == 2 &&
        Aus.getNumber(ds) > days(month, y) ? Aus.string(4, placeHolderChar) : ys);
    }

    private int days(int month, int year) {
      int[] days = {31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
      return month > 12 ? 31 : (month != 2 ? days[month] :
        ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)) ? 29 : 28));
    }

    private void parseDate(String date) {
      String[] parts = new VarStr(date).split(separatorChar);
      if (parts.length > 2 && Aus.isDigit(ys = (parts[2] + ys).substring(0, 4)))
        y = Aus.getNumber(ys);
      if (parts.length > 1 && Aus.isDigit(ms = (parts[1] + ms).substring(0, 2)))
        m = Aus.getNumber(ms);
      if (parts.length > 0 && Aus.isDigit(ds = (parts[0] + ds).substring(0, 2)))
        d = Aus.getNumber(ds);
      if (m < 1 || m > 12) m = 0;
      if (d < 1 || d > days(m, y)) d = 0;
      result = snum(d, 2) + separatorChar + snum(m, 2) + separatorChar + snum(y, 4);
    }

    private String snum(int num, int len) {
      return (num == 0 ? Aus.string(len, placeHolderChar) :
      new VarStr(Aus.string(len, '0')).append(num).right(len));
    }
  }
}

