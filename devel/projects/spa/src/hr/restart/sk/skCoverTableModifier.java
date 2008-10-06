/****license*****************************************************************
**   file: skCoverTableModifier.java
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

import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;

import java.awt.Color;
import java.math.BigDecimal;

import javax.swing.JComponent;

import com.borland.dx.dataset.Variant;

public class skCoverTableModifier extends raTableModifier {
  Color colorG = Color.green.darker();
  Color colorR = Color.red;
  Variant v = new Variant();
  private Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }
  public boolean doModify() {
//      return (!isSelected()); // ako je selektiran boje ostaju iste
    return true;
  }
  public void modify() {
    ((JraTable2)getTable()).getDataSet().getVariant("SALDO",getRow(),v);
    BigDecimal saldo = v.getBigDecimal();
    ((JraTable2)getTable()).getDataSet().getVariant("RSALDO",getRow(),v);
    BigDecimal rsaldo = v.getBigDecimal();
    boolean pokriveno = saldo.compareTo(rsaldo) == 0;

    JComponent jRenderComp = (JComponent)renderComponent;
    if (isSelected()) {
      if (pokriveno) {
        jRenderComp.setBackground(colorR);
      } else {
        jRenderComp.setBackground(halfTone(getTable().getSelectionBackground(), colorG,
                                  rsaldo.floatValue() / saldo.floatValue()));
      }
      jRenderComp.setForeground(getTable().getSelectionForeground());
    } else {
      if (pokriveno) {
        jRenderComp.setForeground(colorR);
      } else {
        jRenderComp.setForeground(halfTone(getTable().getForeground(), colorG,
                                  rsaldo.floatValue() / saldo.floatValue()));
      }
      jRenderComp.setBackground(getTable().getBackground());
    }
  }
}
