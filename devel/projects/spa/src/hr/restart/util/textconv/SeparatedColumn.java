/****license*****************************************************************
**   file: SeparatedColumn.java
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
package hr.restart.util.textconv;


public class SeparatedColumn extends Column implements ISeparatedColumn {
  private int ordinal = 0;
  private String separator;
  public String getSeparator() {
    return separator;
  }

  public void setSeparator(String sep) {
    separator = sep;
  }

  public int getOrdinal() {
    return ordinal;
  }

  public void setOrdinal(int ord) {
    ordinal = ord;
  }

  public boolean parse(ILine line) {
    String cont = line.getContent();
    StringBuffer v = new StringBuffer();
    int seppassed = 1;
    char sepchar = getSeparator().toCharArray()[0];
    char[] cc = cont.toCharArray();
    for (int i = 0; i < cc.length; i++) {
      if (cc[i] == sepchar) {
        seppassed++;
      }
      if (seppassed == getOrdinal() && cc[i] != sepchar) {//here we are
         v.append(cc[i]);
      }
      if (seppassed > getOrdinal()) {
        break;
      }
    }
//System.out.println(getId()+" = "+v);
    Object val = getFormater().applyFormat(v.toString(),getFormat(), getType());
    setValue(val);
    return true;
  }

  public IColumn cloneColumn() {
    SeparatedColumn col = new SeparatedColumn();
    copyColumn(col);
    col.setOrdinal(ordinal);
    col.setSeparator(separator);
    return col;
  }

}
