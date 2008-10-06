/****license*****************************************************************
**   file: raCurrencyTableModifier.java
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

import hr.restart.sk.raSaldaKonti;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raCurrencyTableModifier extends raTableModifier {
  private String colval;
  private Variant v = new Variant();
  Column vcol;

  public raCurrencyTableModifier(String valueColumn) {
    colval = valueColumn;
  }

  public boolean doModify() {
    JraTable2 tab = (JraTable2) getTable();
    vcol = tab.getDataSetColumn(getColumn());
    if (!vcol.getColumnName().equalsIgnoreCase(colval)) return false;
    tab.getDataSet().getVariant("OZNVAL", getRow(), v);
    return !raSaldaKonti.isDomVal(v.getString());
  }
  public void modify() {
    String val = v.getString();
    ((JraTable2) getTable()).getDataSet().getVariant(colval, getRow(), v);
    setComponentText("(" + val + ") " + vcol.format(v));
  }  
}

