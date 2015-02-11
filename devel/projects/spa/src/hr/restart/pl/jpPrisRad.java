/****license*****************************************************************
**   file: jpPrisRad.java
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

import hr.restart.baza.Sifrarnici;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPrisRad extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmPrisutnost fPrisRad;
  JPanel jpDetail = new JPanel();

  JLabel jlSat = new JLabel();
  JLabel jlKol = new JLabel();
  JLabel jlIznos = new JLabel();
  XYLayout lay = new XYLayout();
  JLabel jlCsif = new JLabel();
  JLabel jlCvrp = new JLabel();
  JLabel jlDan = new JLabel();
  JraButton jbSelCsif = new JraButton();
  JraButton jbSelCvrp = new JraButton();

  JraTextField jraDan = new JraTextField() {
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (fPrisRad.getDetailSet().getShort("DAN") > (short)fPrisRad.getMaxDate()){
        this.setErrText("Nepostojeæi dan");
        this_ExceptionHandling(new java.lang.Exception());
        jraDan.selectAll();
      }
    }
  };

  JraTextField jraSat = new JraTextField() {
    public void valueChanged() {
      if (isValueChanged() && !isEmpty()) fPrisRad.reCalc();
    };
  };
  JraTextField jraKol = new JraTextField() {
    public void valueChanged() {
      if (isValueChanged() && !isEmpty()) fPrisRad.reCalc();
    };
  };
  JraTextField jraIznos = new JraTextField();
  
  JlrNavField jlrCgrup = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv1 = new JlrNavField() {
    public void after_lookUp() {
    	jlrCgrup.requestFocusLater();
    }
  };
  JlrNavField jlrCvrp = new JlrNavField() {
    public void after_lookUp() {
    	jlrCgrup.requestFocusLater();
    	fPrisRad.reCalc();
    }
  };

  public jpPrisRad(frmPrisutnost f) {
    try {
      fPrisRad = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(120);
    
    //focust

    jbSelCsif.setText("...");
    jbSelCvrp.setText("...");
    jlCsif.setText("Grupa primanja");
    jlCvrp.setText("Vrsta primanja");
    jlDan.setText("Dan");
    
    jlSat.setText("Sati");
    jlSat.setHorizontalAlignment(JLabel.CENTER);
    jlKol.setText("Kolièina");
    jlKol.setHorizontalAlignment(JLabel.CENTER);
    jlIznos.setText("Iznos");
    jlIznos.setHorizontalAlignment(JLabel.CENTER);

    jraDan.setColumnName("DAN");
    jraDan.setDataSet(fPrisRad.getDetailSet());

    jraSat.setColumnName("SATI");
    jraSat.setDataSet(fPrisRad.getDetailSet());
    jraKol.setColumnName("KOL");
    jraKol.setDataSet(fPrisRad.getDetailSet());
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fPrisRad.getDetailSet());

    jlrCgrup.setColumnName("GRPRIS");
    jlrCgrup.setNavColumnName("CSIF");
    jlrCgrup.setDataSet(fPrisRad.getDetailSet());
    jlrCgrup.setColNames(new String[] {"NAZIV"});
    jlrCgrup.setTextFields(new JTextComponent[] {jlrNaziv1});
    jlrCgrup.setVisCols(new int[] {0, 2});
    jlrCgrup.setSearchMode(0);
    jlrCgrup.setRaDataSet(Sifrarnici.getDataModule().getFilteredDataSet("VRSTASIF = 'PLGP'"));
    jlrCgrup.setNavButton(jbSelCsif);

    jlrNaziv1.setColumnName("NAZIV");
    jlrNaziv1.setNavProperties(jlrCgrup);
    jlrNaziv1.setSearchMode(1);

    jlrCvrp.setColumnName("CVRP");
    jlrCvrp.setDataSet(fPrisRad.getDetailSet());
    jlrCvrp.setColNames(new String[] {"NAZIV"});
    jlrCvrp.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCvrp.setVisCols(new int[] {0, 1});
    jlrCvrp.setSearchMode(0);
    jlrCvrp.setRaDataSet(dm.getVrsteprim());
    jlrCvrp.setNavButton(jbSelCvrp);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCvrp);
    jlrNaziv.setSearchMode(1);

    
    jpDetail.add(jlDan, new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jraDan, new XYConstraints(150, 30, 75, -1));
    jpDetail.add(jlSat, new XYConstraints(290, 10, 75, -1));
    jpDetail.add(jraSat, new XYConstraints(290, 30, 75, -1));
    jpDetail.add(jlKol, new XYConstraints(370, 10, 75, -1));
    jpDetail.add(jraKol, new XYConstraints(370, 30, 75, -1));
    jpDetail.add(jlIznos, new XYConstraints(450, 10, 100, -1));
    jpDetail.add(jraIznos, new XYConstraints(450, 30, 100, -1));
    
    jpDetail.add(jlCvrp, new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlrCvrp, new XYConstraints(150, 55, 75, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(230, 55, 320, -1));
    jpDetail.add(jbSelCvrp, new XYConstraints(555, 55, 21, 21));
    jpDetail.add(jlCsif, new XYConstraints(15, 80, -1, -1));
    jpDetail.add(jlrCgrup, new XYConstraints(150, 80, 75, -1));
    jpDetail.add(jlrNaziv1, new XYConstraints(230, 80, 320, -1));
    jpDetail.add(jbSelCsif, new XYConstraints(555, 80, 21, 21));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
