/****license*****************************************************************
**   file: raSkStatusColorModifier.java
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
package hr.restart.ur;

import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;

import java.awt.Color;

import javax.swing.JComponent;

import com.borland.dx.dataset.Variant;

public class raSkStatusColorModifier extends raTableModifier {
  Color colorS = hr.restart.swing.raColors.grey;//Color.green.darker().darker();
  public raSkStatusColorModifier() {
  }
  private Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }
  public boolean doModify() {
//    return (!isSelected()); // ako je selektiran boje ostaju iste
    return true;
  }
  public void modify() {
    Variant v = new Variant();
    ((JraTable2)getTable()).getDataSet().getVariant("STATKNJ",getRow(),v); //vrijednost iz dataseta u tom trenutku moze se dobiti jedino na ovaj nacin
    JComponent jRenderComp = (JComponent)renderComponent;
    if (isSelected()) {
      if (v.getString().equals("D")) {
        jRenderComp.setBackground(halfTone(jRenderComp.getBackground(), 
            hr.restart.swing.raColors.grey, 0.5f));
      }
      //jRenderComp.setForeground(getTable().getSelectionForeground());
    } else {
      if (v.getString().equals("D")) {
        jRenderComp.setForeground(halfTone(jRenderComp.getForeground(), 
            hr.restart.swing.raColors.grey, 0.5f));
      }
    }
  }
}