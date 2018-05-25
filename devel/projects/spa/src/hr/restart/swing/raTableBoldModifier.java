/****license*****************************************************************
**   file: raTableBoldModifier.java
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

import java.awt.Font;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;


public class raTableBoldModifier extends raTableModifier {

  private Font bfont;
  private String boldColumn, condColumn, condValue;
  
  private Variant v = new Variant();
  
  public raTableBoldModifier(String column, String condCol, String condVal) {
    boldColumn = column;
    condColumn = condCol;
    condValue = condVal;
  }
  
  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 jtab = (JraTable2)getTable();
      Column dsCol = jtab.getDataSetColumn(getColumn());
      if (dsCol == null) return false;
      if (!dsCol.getColumnName().equals(boldColumn)) return false;
      jtab.getDataSet().getVariant(condColumn, getRow(), v);
      return v.toString().equals(condValue);
    }
    return false;
  }
  
  public void modify() {
    if (bfont == null) bfont = renderComponent.getFont().deriveFont(Font.BOLD);
    renderComponent.setFont(bfont);
  }
  
  public String getBoldColumn() {
    return boldColumn;
  }
}
