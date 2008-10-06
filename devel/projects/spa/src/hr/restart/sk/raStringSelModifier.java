/****license*****************************************************************
**   file: raStringSelModifier.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;

import java.awt.Color;
import java.util.HashSet;

import javax.swing.JComponent;

import com.borland.dx.dataset.Variant;

public class raStringSelModifier extends raTableModifier {
  Color m = null;
  Color g = Color.green.darker().darker().darker();
  Variant v = new Variant();
  HashSet sel = new HashSet();
  String column;
  boolean ignore;

  public raStringSelModifier(String colName) {
    column = colName;
  }

  private Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }
  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }
  public boolean doModify() {
    if (ignore) return false;
    ((JraTable2)getTable()).getDataSet().getVariant(column,getRow(),v);
    return sel.contains(v.getString());
  }
  public void clearSelection() {
    sel.clear();
  }
  public void addToSelection(String var) {
    sel.add(var);
  }
  public int countSelected() {
    return sel.size();
  }
  public void removeFromSelection(String var) {
    sel.remove(var);
  }
  public Condition getSelectionCondition(String defvar) {
    if (sel.isEmpty()) return Condition.equal(column, defvar);
    else return Condition.in(column, sel.toArray());
  }
  public void modify() {
    JComponent jRenderComp = (JComponent)renderComponent;
    if (!isSelected()) {
      if (m == null)  m = halfTone(Color.yellow, jRenderComp.getBackground(), 0.75f);
      jRenderComp.setBackground(m);
    } else {
      jRenderComp.setBackground(g);
    }
  }
}
