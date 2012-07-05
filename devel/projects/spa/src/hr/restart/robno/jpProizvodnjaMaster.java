/****license*****************************************************************
**   file: jpProizvodnjaMaster.java
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
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpProizvodnjaMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmProizvodnja fProizvodnja;
  JPanel jpDetail = new JPanel();

  rajpBrDok jpMasterHeader = new rajpBrDok();

  XYLayout lay = new XYLayout();
  JLabel jlPar = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JLabel jlRadovi = new JLabel();
  JraButton jbSelRadovi = new JraButton();
  JTextArea jtaRadovi = new JTextArea();
  JraTextField jraRadovi = new JraTextField();
  JLabel jLabel1 = new JLabel();
  JraTextField jraDatum = new JraTextField();
  JlrNavField jlrRadovi = new JlrNavField() {
    public void after_lookUp() {
      if (fProizvodnja.raMaster.getMode() != 'B') {
        String CSKUPART = jraNorm.getText();
        lookupData.getlookupData().raLocate(jlrRadovi.getRaDataSet(), 
            "CTEKST", jlrRadovi.getText());
        String TEKST = jlrRadovi.getRaDataSet().getString("TEKST");
        jtaRadovi.setText(TEKST);
        if (fProizvodnja.raMaster.getMode() == 'N') {
          jlrNorm.setText(CSKUPART);
          jlrNorm.forceFocLost();
        }
      }
    }
  };
  JraTextField jraNorm = new JraTextField();
  JlrNavField jlrNorm = new JlrNavField() {
    public void after_lookUp() {
      if (fProizvodnja.raMaster.getMode() != 'B') {
         fProizvodnja.getMasterSet().setBigDecimal("MULT", Aus.one0);
      }
    }
  };
  JlrNavField jlrNaznorm = new JlrNavField();
  JraButton jbSelNorm = new JraButton();
  JLabel jlNorm = new JLabel();
  
  JLabel jlMult = new JLabel();
  JraTextField jraMult = new JraTextField();

  JLabel jlNapomene = new JLabel();
  JlrNavField jlrNap1 = new JlrNavField();
  JlrNavField jlrNap2 = new JlrNavField();
  JlrNavField jlrNaznap1 = new JlrNavField();
  JlrNavField jlrNaznap2 = new JlrNavField();
  JraButton jbSelNap1 = new JraButton();
  JraButton jbSelNap2 = new JraButton();
  JraScrollPane scroller = new JraScrollPane(jtaRadovi);
  JScrollBar hs = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };

  public jpProizvodnjaMaster(frmProizvodnja md) {
    try {
      fProizvodnja = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jlrCpar.setDataSet(ds);
    jlrNorm.setDataSet(ds);
    jlrNap1.setDataSet(ds);
    jlrNap2.setDataSet(ds);
    jraDatum.setDataSet(ds);
    jraMult.setDataSet(ds);
    jlrRadovi.setDataSet(ds);
    jpMasterHeader.setDataSet(ds);
  }

  public void setIzmjena() {
    if (!fProizvodnja.getMasterSet().getString("STATUS").equalsIgnoreCase("P")) {
      rcc.setLabelLaF(jlrCpar, false);
      rcc.setLabelLaF(jlrNazpar, false);
      rcc.setLabelLaF(jbSelCpar, false);
      rcc.setLabelLaF(jraDatum, false);
      rcc.setLabelLaF(jlrRadovi, false);
      rcc.setLabelLaF(jbSelRadovi, false);
    } 
    rcc.setLabelLaF(jlrNorm, false);
    rcc.setLabelLaF(jlrNaznorm, false);
    rcc.setLabelLaF(jbSelNorm, false);
    rcc.setLabelLaF(jraMult, false);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(675);
    lay.setHeight(305);

    jlPar.setText("Naru\u010Ditelj");
    jlRadovi.setText("Naru\u010Deni radovi");
    jLabel1.setText("Datum otvaranja");
    jlNapomene.setText("Napomene");
    jlNorm.setText("Skupina artikala");
    jlMult.setText("Kolièina skupine");

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

    jlrRadovi.setSearchMode(0);
    jlrRadovi.setColumnName("CTEKST");
    jlrRadovi.setTextFields(new JTextComponent[] {jraRadovi, jraNorm});
    jlrRadovi.setColNames(new String[] {"TEKST", "CSKUPART"});
    jlrRadovi.setRaDataSet(dm.getRN_tekstovi());
    jlrRadovi.setVisCols(new int[] {0,2});
    jlrRadovi.setNavButton(jbSelRadovi);

    jraRadovi.setColumnName("TEKST");
    jraRadovi.setVisible(false);
    jraNorm.setColumnName("CSKUPART");
    jraNorm.setVisible(false);

    jlrNorm.setColumnName("CSKUPART");
    jlrNorm.setTextFields(new JTextComponent[] {jlrNaznorm});
    jlrNorm.setColNames(new String[] {"NAZSKUPART"});
    jlrNorm.setSearchMode(0);
    jlrNorm.setRaDataSet(Aut.getAut().getNorme());
    jlrNorm.setVisCols(new int[] {0,1});
    jlrNorm.setNavButton(jbSelNorm);

    jlrNaznorm.setColumnName("NAZSKUPART");
    jlrNaznorm.setNavProperties(jlrNorm);
    jlrNaznorm.setSearchMode(1);
    
    jraMult.setColumnName("MULT");

    jlrNap1.setColumnName("CNAP1");
    jlrNap1.setNavColumnName("CNAP");
    jlrNap1.setTextFields(new JTextComponent[] {jlrNaznap1});
    jlrNap1.setColNames(new String[] {"NAZNAP"});
    jlrNap1.setSearchMode(0);
    jlrNap1.setRaDataSet(dm.getNapomene());
    jlrNap1.setVisCols(new int[] {0,1});
    jlrNap1.setNavButton(jbSelNap1);

    jlrNaznap1.setColumnName("NAZNAP");
    jlrNaznap1.setNavProperties(jlrNap1);
    jlrNaznap1.setSearchMode(1);

    jlrNap2.setColumnName("CNAP2");
    jlrNap2.setNavColumnName("CNAP");
    jlrNap2.setTextFields(new JTextComponent[] {jlrNaznap2});
    jlrNap2.setColNames(new String[] {"NAZNAP"});
    jlrNap2.setSearchMode(0);
    jlrNap2.setRaDataSet(dm.getNapomene());
    jlrNap2.setVisCols(new int[] {0,1});
    jlrNap2.setNavButton(jbSelNap2);

    jlrNaznap2.setColumnName("NAZNAP");
    jlrNaznap2.setNavProperties(jlrNap2);
    jlrNaznap2.setSearchMode(1);

    jraDatum.setColumnName("DATDOK");
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtaRadovi.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    scroller.setBorder(BorderFactory.createLoweredBevelBorder());
    scroller.setHorizontalScrollBar(hs);
    scroller.setVerticalScrollBar(vs);

    jpDetail.add(jbSelCpar, new XYConstraints(635, 20, 21, 21));
    jpDetail.add(jlPar, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(230, 20, 400, -1));

    jpDetail.add(jlRadovi, new XYConstraints(15, 50, -1, -1));
    jpDetail.add(scroller, new XYConstraints(15, 80, 645, 100));
    jpDetail.add(jlNapomene, new XYConstraints(15, 250, -1, -1));
    jpDetail.add(jlrNap1, new XYConstraints(150, 250, 75, -1));
    jpDetail.add(jlrNaznap1, new XYConstraints(230, 250, 400, -1));
    jpDetail.add(jlrNap2, new XYConstraints(150, 275, 75, -1));
    jpDetail.add(jlrNaznap2, new XYConstraints(230, 275, 400, -1));
    jpDetail.add(jbSelNap1, new XYConstraints(635, 250, 21, 21));
    jpDetail.add(jbSelNap2, new XYConstraints(635, 275, 21, 21));
    jpDetail.add(jLabel1, new XYConstraints(375, 52, -1, -1));
    jpDetail.add(jraDatum, new XYConstraints(530, 50, 100, -1));
    jpDetail.add(jbSelRadovi, new XYConstraints(230, 50, 21, 21));
    jpDetail.add(jlrRadovi, new XYConstraints(150, 50, 75, -1));
    jpDetail.add(jlrNorm, new XYConstraints(150, 190, 75, -1));
    jpDetail.add(jlrNaznorm, new XYConstraints(230, 190, 400, -1));
    jpDetail.add(jbSelNorm, new XYConstraints(635, 190, 21, 21));
    jpDetail.add(jlNorm, new XYConstraints(15, 190, -1, -1));
    jpDetail.add(jlMult, new XYConstraints(15, 215, -1, -1));
    jpDetail.add(jraMult, new XYConstraints(150, 215, 75, -1));

    BindComponents(fProizvodnja.getMasterSet());

    jpMasterHeader.addBorder();

    this.setLayout(new BorderLayout());
    this.add(jpMasterHeader, BorderLayout.NORTH);
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
