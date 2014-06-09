/****license*****************************************************************
**   file: jpVTZtr.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVTZtr extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVTZtr fVTZtr;
  frmVTZtrstav fVTZtrstav;
  JPanel jpDetail = new JPanel();

  boolean skipCpar = false, skipPzt = false;

  XYLayout lay = new XYLayout();
  JLabel jlBrrac = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlCzt = new JLabel();
  JLabel jlPzt = new JLabel();
  JLabel jlaIzt = new JLabel();
  JLabel jlaPrpor = new JLabel();
  JLabel jlaPzt = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlUldok = new JLabel();
  JLabel jlDatdosp = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JraButton jbSelCzt = new JraButton();
  JraTextField jraUldok = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraBrrac = new JraTextField();
  JraTextField jraDatrac = new JraTextField();
  JraTextField jraIzt = new JraTextField() {
    public void valueChanged() {
      if (fVTZtr != null) fVTZtr.afterIZT();
      if (fVTZtrstav != null) fVTZtrstav.afterIZT();
    }
  };
  JraTextField jraPrpor = new JraTextField();
  JraTextField jraPzt = new JraTextField() {
    public boolean isFocusTraversable() {
      if (skipPzt) return (skipPzt = false);
      else return true;
    }
    public void valueChanged() {
      if (fVTZtr != null) fVTZtr.afterPZT();
      if (fVTZtrstav != null) fVTZtrstav.afterPZT();
    }
  };
  
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
    public boolean isFocusTraversable() {
      return !skipCpar;
    }
  };
  JlrNavField jlrCzt = new JlrNavField() {
    public void after_lookUp() {
      if (fVTZtr != null && fVTZtr.inedit)
        fVTZtr.afterCzt();
    }
  };
  JlrNavField jlrNzt = new JlrNavField();
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
    public boolean isFocusTraversable() {
      if (skipCpar) return (skipCpar = false);
      else return true;
    }
  };

  public jpVTZtr(frmVTZtr f, frmVTZtrstav fs) {
    try {
      fVTZtr = f;
      fVTZtrstav = fs;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setSkipCpar() {
    skipCpar = true;
  }

  public void setSkipPzt() {
    skipPzt = true;
  }

  public void setupPanelForHeader() {
    jlPzt.setText("Ukupni trošak");
    rcc.EnabDisabAll(jpDetail, true);
  }

  public void setupPanelForEntries() {
    jlPzt.setText("Trošak po stavci");
    rcc.setLabelLaF(jlrCzt, false);
    rcc.setLabelLaF(jlrNzt, false);
    rcc.setLabelLaF(jbSelCzt, false);
    rcc.setLabelLaF(jlrCpar, false);
    rcc.setLabelLaF(jlrNazpar, false);
    rcc.setLabelLaF(jbSelCpar, false);
    rcc.setLabelLaF(jraPrpor, false);
    rcc.setLabelLaF(jraDatrac, false);
    rcc.setLabelLaF(jraBrrac, false);
    rcc.setLabelLaF(jraDatdosp, false);
    rcc.setLabelLaF(jraUldok, false);
  }

  public void BindComponents(DataSet ds) {
    jraUldok.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jraBrrac.setDataSet(ds);
    jraDatrac.setDataSet(ds);
    jraIzt.setDataSet(ds);
    jraPrpor.setDataSet(ds);
    jraPzt.setDataSet(ds);
    jlrCpar.setDataSet(ds);
    jlrCzt.setDataSet(ds);    
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(571);
    lay.setHeight(180);

    jbSelCpar.setText("...");
    jbSelCzt.setText("...");
    jlUldok.setText("Ulazni dokument");
    jlBrrac.setText("Broj raèuna");
    jlCpar.setText("Partner");
    jlCzt.setText("Zavisni trošak");
    jlPzt.setText("Ukupni trošak");
    jlaIzt.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaIzt.setText("Iznos");
    jlaPrpor.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaPrpor.setText("Pretporez");
    jlaPzt.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaPzt.setText("Postotak");
    jlDatum.setText("Datum");
    jlDatdosp.setText("Dospjeæe");
    jraUldok.setColumnName("ULDOK");
    jraBrrac.setColumnName("BRRAC");
    jraDatrac.setColumnName("DATRAC");
    jraDatrac.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatdosp.setColumnName("DATDOSP");
    jraDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jraIzt.setColumnName("IZT");
    jraPrpor.setColumnName("PRPOR");
    jraPzt.setColumnName("PZT");

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlrCzt.setColumnName("CZT");
    jlrCzt.setColNames(new String[] {"NZT"});
    jlrCzt.setTextFields(new JTextComponent[] {jlrNzt});
    jlrCzt.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCzt.setSearchMode(0);
    jlrCzt.setRaDataSet(dm.getZtr());
    jlrCzt.setNavButton(jbSelCzt);

    jlrNzt.setColumnName("NZT");
    jlrNzt.setNavProperties(jlrCzt);
    jlrNzt.setSearchMode(1);

    jpDetail.add(jbSelCpar, new XYConstraints(535, 45, 21, 21));
    jpDetail.add(jbSelCzt, new XYConstraints(535, 20, 21, 21));
    jpDetail.add(jlBrrac, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlCzt, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlPzt, new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlaIzt, new XYConstraints(231, 73, 98, -1));
    jpDetail.add(jlaPrpor, new XYConstraints(431, 73, 98, -1));
    jpDetail.add(jlaPzt, new XYConstraints(151, 73, 73, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jlrCzt, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(230, 45, 300, -1));
    jpDetail.add(jlrNzt, new XYConstraints(230, 20, 300, -1));
    jpDetail.add(jraBrrac, new XYConstraints(150, 115, 180, -1));
    jpDetail.add(jlDatum, new XYConstraints(370, 115, -1, -1));
    jpDetail.add(jraDatrac, new XYConstraints(430, 115, 100, -1));
    jpDetail.add(jraIzt, new XYConstraints(230, 90, 100, -1));
    jpDetail.add(jraPrpor, new XYConstraints(430, 90, 100, -1));
    jpDetail.add(jraPzt, new XYConstraints(150, 90, 75, -1));
    jpDetail.add(jlUldok, new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jraUldok, new XYConstraints(150, 140, 180, -1));
    jpDetail.add(jlDatdosp, new XYConstraints(370, 140, -1, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(430, 140, 100, -1));

    /*jraIzt.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (fVTZtr != null) fVTZtr.afterIZT();
        if (fVTZtrstav != null) fVTZtrstav.afterIZT();
      }
    });*/

    /*jraPzt.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (fVTZtr != null) fVTZtr.afterPZT();
        if (fVTZtrstav != null) fVTZtrstav.afterPZT();
      }
    });*/

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
