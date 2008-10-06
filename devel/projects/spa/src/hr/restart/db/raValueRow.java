/****license*****************************************************************
**   file: raValueRow.java
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
package hr.restart.db;
import java.io.Serializable;

public class raValueRow implements Serializable {

  public raValueRow() {
  }
  private Object[] values;
  private char flag = ' ';

  /**
   * Vraca status reda
   * @return ' ' - netaknut (isti kao u bazi), 'U' - updated - kada bude savechanges onda radi update,
   * 'A' - added dodat ce taj red ako uspije, 'D' - deleted - obrisat ce ga, 'I' - insertiran a nije postan,
   * 'E' - editiran a nije postan
   */
  public char getState() {
    return flag;
  }

  void setState(char s) {
    if (s==' ' || s=='U' || s=='A' || s=='D' || s=='I' || s=='E') {
      flag = s;
      } else throw new IllegalArgumentException("state s must be s==' ' || s=='U' || s=='A' || s=='D' || s=='I' || s=='E'");
    }

  public Object[] getValues() {
    return values;
  }

  public void setValues(Object[] vals) {
    values = vals;
  }

  public Object getValue(int idx) {
    return values[idx];
  }

  public void setValue(int idx, Object val) {
    if (flag == 'I') {
      values[idx] = val;
    } else if (!values[idx].equals(val)) {
      values[idx] = val;
//      if (flag == ' ') flag = 'U';
    }
  }

  public Object clone() {
    raValueRow cloned = new raValueRow();
    cloned.values = (Object[])getValues().clone();
    return cloned;
  }

  public raValueRow getNew() {
    raValueRow newRow = (raValueRow)clone();
    for (int i = 0; i < newRow.values.length; i++) {
      newRow.values[i] = null;
    }
    newRow.flag = 'I';
    return newRow;
  }

}