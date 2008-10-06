/****license*****************************************************************
**   file: gkStatusColorModifier.java
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
package hr.restart.gk;

import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;

import java.awt.Color;

import javax.swing.JComponent;

import com.borland.dx.dataset.Variant;

public class gkStatusColorModifier extends raTableModifier {
    Color colorS = hr.restart.swing.raColors.green;//Color.green.darker().darker();
    Color colorN = hr.restart.swing.raColors.red;//Color.red;
    private raTableModifier _mergedModifier = null;
    public gkStatusColorModifier() {
      this(null);
    }
    public gkStatusColorModifier(raTableModifier mergedModifier) {
      super();
      _mergedModifier  = mergedModifier;
    }    
    public boolean doModify() {
//      return (!isSelected()); // ako je selektiran boje ostaju iste
      return true;
    }
    public void modify() {
      Variant v = new Variant();
      ((JraTable2)getTable()).getDataSet().getVariant("STATUS",getRow(),v); //vrijednost iz dataseta u tom trenutku moze se dobiti jedino na ovaj nacin
      JComponent jRenderComp = (JComponent)renderComponent;
      if (isSelected()) {
        if (v.getString().equals("S")) {
          jRenderComp.setBackground(colorS);
          jRenderComp.setForeground(Color.black);
        } else if (v.getString().equals("N")) {
          jRenderComp.setBackground(colorN);
          jRenderComp.setForeground(Color.black);
        } else {
          jRenderComp.setBackground(getTable().getSelectionBackground());
          jRenderComp.setForeground(getTable().getSelectionForeground());
        }
        //jRenderComp.setForeground(getTable().getSelectionForeground());
      } else {
        if (v.getString().equals("S")) {
          jRenderComp.setForeground(Color.green.darker().darker());
        } else if (v.getString().equals("N")) {
          jRenderComp.setForeground(Color.red);
        } else {
          jRenderComp.setForeground(getTable().getForeground());
        }
        if (_mergedModifier == null) {
          jRenderComp.setBackground(getTable().getBackground());
        } else {
          jRenderComp.setBackground(_mergedModifier.renderComponent.getBackground());
        }
      }
    }
}