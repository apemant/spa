/****license*****************************************************************
**   file: jpPrimanjaDetail.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPrimanjaDetail extends JPanel {
  raCalcPrimanja rcp = raCalcPrimanja.getRaCalcPrimanja();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmPrimanja fPrimanja;
  JPanel jpNorth = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvrp = new JLabel();
  JLabel jlaBruto = new JLabel();
  JLabel jlaCorg = new JLabel();
  JLabel jlaCvrp = new JLabel();
  JLabel jlaKoef = new JLabel();
  JLabel jlaSati = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCvrp = new JraButton();
  JraTextField jraBruto = new JraTextField() {
    public void valueChanged() {
      calcPrimanja();
    }
    /*public void focusLost(java.awt.event.FocusEvent e) {
      super.focusLost(e);
      calcPrimanja();
    }*/
  };
  JraTextField jraKoef = new JraTextField() {
    public void valueChanged() {
      calcPrimanja();
    }
    /*public void focusLost(java.awt.event.FocusEvent e) {
      super.focusLost(e);
      calcPrimanja();
    }*/
  };
  JraTextField jraSati = new JraTextField() {
    public void valueChanged() {
      calcPrimanja();
    }
    /*public void focusLost(java.awt.event.FocusEvent e) {
      super.focusLost(e);
      calcPrimanja();
    }*/
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrp = new JlrNavField() {
    public void after_lookUp() {
      if (fPrimanja.raDetail.getMode() == 'B') return;
      fPrimanja.findFocusAfter();
      calcPrimanja();
      System.out.println("End after lukap");
    }
  };
  private BorderLayout borderLayout1 = new BorderLayout();
  JraTextField jlIRAZDO = new JraTextField() {
    public void valueChanged() {
      calcPrimanja();
    }
    /*public void focusLost(java.awt.event.FocusEvent e) {
      super.focusLost(e);
      calcPrimanja();
    }*/
  };
  JraTextField jlIRAZOD = new JraTextField() {
    public void valueChanged() {
      calcPrimanja();
    }
    /*public void focusLost(java.awt.event.FocusEvent e) {
      super.focusLost(e);
      calcPrimanja();
    }*/
  };
  JLabel jlRazdoblje = new JLabel();

  public jpPrimanjaDetail(frmPrimanja md) {
    try {
      fPrimanja = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpNorth.setLayout(lay);
    lay.setWidth(621);
    lay.setHeight(105);

    jbSelCorg.setText("...");
    jbSelCvrp.setText("...");
    jlCvrp.setText("Primanja");
    jlaBruto.setHorizontalAlignment(SwingConstants.CENTER);
    jlaBruto.setText("Iznos");
    jlaCorg.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCorg.setText("Org.Jedinica");
    jlaCvrp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCvrp.setText("Vrsta");
    jlaKoef.setHorizontalAlignment(SwingConstants.CENTER);
    jlaKoef.setText("Koeficijent");
    jlaSati.setHorizontalAlignment(SwingConstants.CENTER);
    jlaSati.setText("Sati");
    jraBruto.setColumnName("BRUTO");
    jraBruto.setDataSet(fPrimanja.getDetailSet());
    jraKoef.setColumnName("KOEF");
    jraKoef.setDataSet(fPrimanja.getDetailSet());
    jraSati.setColumnName("SATI");
    jraSati.setDataSet(fPrimanja.getDetailSet());

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fPrimanja.getDetailSet());
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrCvrp.setColumnName("CVRP");
    jlrCvrp.setDataSet(fPrimanja.getDetailSet());
    jlrCvrp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrp.setSearchMode(0);
    jlrCvrp.setRaDataSet(dm.getVrsteprim());
    jlrCvrp.setNavButton(jbSelCvrp);

    this.setLayout(borderLayout1);
    jlIRAZDO.setDataSet(fPrimanja.getDetailSet());
    jlIRAZDO.setHorizontalAlignment(SwingConstants.CENTER);
    jlIRAZDO.setColumnName("IRAZDO");
    jlIRAZOD.setDataSet(fPrimanja.getDetailSet());
    jlIRAZOD.setHorizontalAlignment(SwingConstants.CENTER);
    jlIRAZOD.setColumnName("IRAZOD");
    jlRazdoblje.setText("Razdoblje (od-do)");

    this.dateOn(false);

    jpNorth.add(jbSelCorg, new XYConstraints(585, 40, 21, 21));
    jpNorth.add(jbSelCvrp, new XYConstraints(215, 40, 21, 21));
    jpNorth.add(jlCvrp, new XYConstraints(15, 40, -1, -1));
    jpNorth.add(jlaBruto, new XYConstraints(376, 23, 118, -1));
    jpNorth.add(jlaCorg, new XYConstraints(501, 23, 78, -1));
    jpNorth.add(jlaCvrp, new XYConstraints(151, 23, 58, -1));
    jpNorth.add(jlaKoef, new XYConstraints(311, 23, 58, -1));
    jpNorth.add(jlaSati, new XYConstraints(246, 23, 58, -1));
    jpNorth.add(jlrCorg, new XYConstraints(500, 40, 80, -1));
    jpNorth.add(jlrCvrp, new XYConstraints(150, 40, 60, -1));
    jpNorth.add(jraBruto, new XYConstraints(375, 40, 120, -1));
    jpNorth.add(jraKoef, new XYConstraints(310, 40, 60, -1));
    jpNorth.add(jraSati, new XYConstraints(245, 40, 60, -1));
    jpNorth.add(jlRazdoblje, new XYConstraints(15, 65, -1, -1));
    jpNorth.add(jlIRAZOD, new XYConstraints(150, 65, 100, -1));
    jpNorth.add(jlIRAZDO, new XYConstraints(255, 65, 100, -1));

    this.add(jpNorth, BorderLayout.NORTH);
  }
  void calcPrimanja() {
    System.out.println("calcPrimanja");
    rcp.addCalcSet(fPrimanja.getMasterSet(),"radnicipl");
    rcp.calcPrimanje(fPrimanja.getDetailSet());
  }
  void dateOn(boolean ok) {
    System.out.println("dateOn: "+ok);
    if (ok) {
      jlRazdoblje.setVisible(true);
      jlIRAZDO.setVisible(true);
      jlIRAZOD.setVisible(true);
    }
    else {
      jlRazdoblje.setVisible(false);
      jlIRAZDO.setVisible(false);
      jlIRAZOD.setVisible(false);
    }
  }
}
