/****license*****************************************************************
**   file: raTableValueModifier.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raTableValueModifier extends raTableColumnModifier {
  private String name;
  private HashMap map;
  private Variant v = new Variant();
  private int width;

  public raTableValueModifier(String column, Map map) {
    super(column, null, null);
    name = column;
    this.map = new HashMap(map);
    width = 0;
    for (Iterator i = this.map.keySet().iterator(); i.hasNext(); ) {
      int w = this.map.get(i.next()).toString().length();
      if (w > width) width = w;
    }
  }

  public raTableValueModifier(String column, String[] originals, String[] replacements) {
    super(column, null, null);
    name = column;
    map = new HashMap(originals.length * 2);
    width = 0;
    for (int i = 0; i < originals.length; i++) {
      map.put(originals[i].toLowerCase(), replacements[i]);
      if (replacements[i].length() > width) width = replacements[i].length();
    }
  }

  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 tab = (JraTable2) getTable();
      Column col = tab.getDataSetColumn(getColumn());
      return (col != null && col.getColumnName().equalsIgnoreCase(name));
    }
    return false;
  }

  public void modify() {
    ((JraTable2) getTable()).getDataSet().getVariant(name, getRow(), v);
    String key = v.getString().toLowerCase();
    if (width < 5 && renderComponent instanceof JLabel)
      ((JLabel)renderComponent).setHorizontalAlignment(JLabel.CENTER);
    if (map.containsKey(key)) setComponentText(map.get(key).toString());
  }

  public int getMaxModifiedTextLength() {
    return width;
  }

  public String toString() {
    return "raTableModifier "+this.getClass()+"\n"
    +" To replace text in column "+name;
  }
}
