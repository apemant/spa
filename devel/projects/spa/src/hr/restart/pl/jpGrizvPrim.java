/****license*****************************************************************
**   file: jpGrizvPrim.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpGrizvPrim extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmGrizvPrim fGrizvPrim;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvrp = new JLabel();
  JraButton jbSelCvrp = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrp = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlOznaka = new JLabel();
  JLabel jlOpisVP = new JLabel();

  public jpGrizvPrim(frmGrizvPrim f) {
    try {
      fGrizvPrim = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(70);

    jbSelCvrp.setText("...");
    jlCvrp.setText("Vrsta primanja");

    jlrCvrp.setColumnName("CVRP");
    jlrCvrp.setDataSet(fGrizvPrim.getRaQueryDataSet());
    jlrCvrp.setColNames(new String[] {"NAZIV"});
    jlrCvrp.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCvrp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrp.setSearchMode(0);
    jlrCvrp.setRaDataSet(dm.getVrsteprim());
    jlrCvrp.setNavButton(jbSelCvrp);
    jlrCvrp.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jlrCvrp_keyReleased(e);
      }
    });

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCvrp);
    jlrNaziv.setSearchMode(1);

    jlOznaka.setText("Oznaka");
    jlOpisVP.setText("Opis");
    jpDetail.add(jbSelCvrp,  new XYConstraints(560, 30, 21, 21));
    jpDetail.add(jlCvrp,  new XYConstraints(15, 34, -1, -1));
    jpDetail.add(jlrCvrp,  new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrNaziv,  new XYConstraints(255, 30, 300, -1));
    jpDetail.add(jlOznaka,   new XYConstraints(150, 13, -1, -1));


    this.add(jpDetail, BorderLayout.CENTER);
    jpDetail.add(jlOpisVP,  new XYConstraints(255, 13, -1, -1));
  }

  void jlrCvrp_keyReleased(KeyEvent e) {
    if(e.getKeyCode()== e.VK_ESCAPE)

    {
      fGrizvPrim.getOKpanel().jPrekid_actionPerformed();
    }
  }


}
