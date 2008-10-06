/****license*****************************************************************
**   file: jpBlag.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpBlag extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  sgStuff ss = sgStuff.getStugg();

  frmBlag fBlag;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCblag = new JLabel();
  JLabel jlKnjig = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlOznval = new JLabel();
  JLabel jlPvsaldo = new JLabel();
  JLabel jlSaldo = new JLabel();
  JLabel jlaBrizv = new JLabel();
  JLabel jlaDatizv = new JLabel();
  JLabel jlaSaldo = new JLabel();
  JraButton jbSelKnjig = new JraButton();
  JraButton jbSelOznval = new JraButton();
  JraTextField jraBrizv = new JraTextField();
  JraTextField jraCblag = new JraTextField();
  JraTextField jraDatizv = new JraTextField()/*{
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      datumFokuslost();
    }
  }*/;
  JraTextField jraNaziv = new JraTextField();
  JraTextField jraPvsaldo = new JraTextField();
  JraTextField jraSaldo = new JraTextField(){
    public void valueChanged() {
      jraSaldo_focusLost();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      jraSaldo_focusLost();
    }*/
  };
  JraCheckBox jrcbBrezgot = new JraCheckBox();
  JlrNavField jlrKnjig = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
      lookup();
    }
  };
  JlrNavField jlrNazval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazknjig = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  private JLabel jlBezgotovinska = new JLabel();

  public jpBlag(frmBlag f) {
    try {
      fBlag = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(225);

    jlCblag.setText("Broj blagajne");
    jlKnjig.setText("Knjigovodstvo");
    jlNaziv.setText("Naziv blagajne");
    jlOznval.setText("Valuta");
    jlPvsaldo.setText("Protuvrijednost salda");
    jlSaldo.setText("Zadnji bl. izvještaj");

    jlaBrizv.setHorizontalAlignment(SwingConstants.CENTER);
    jlaBrizv.setText("Broj");
    jlaDatizv.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatizv.setText("Datum");
    jlaSaldo.setHorizontalAlignment(SwingConstants.CENTER);
    jlaSaldo.setText("Saldo");

    jraBrizv.setColumnName("BRIZV");

    jraCblag.setColumnName("CBLAG");

    jraDatizv.setColumnName("DATIZV");

    jraNaziv.setColumnName("NAZIV");

    jraPvsaldo.setColumnName("PVSALDO");

    jraSaldo.setColumnName("SALDO");

    jrcbBrezgot.setColumnName("BREZGOT");
    jrcbBrezgot.setSelectedDataValue("D");
    jrcbBrezgot.setUnselectedDataValue("N");
    jrcbBrezgot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrcbBrezgot_actionPerformed(e);
      }
    });
    jrcbBrezgot.setHorizontalAlignment(SwingConstants.LEADING);

    jlrKnjig.setColumnName("KNJIG");
    jlrKnjig.setNavColumnName("CORG");
    jlrKnjig.setColNames(new String[] {"NAZIV"});
    jlrKnjig.setTextFields(new JTextComponent[] {jlrNazknjig});
    jlrKnjig.setVisCols(new int[] {0, 1});
    jlrKnjig.setSearchMode(0);
    jlrKnjig.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrKnjig.setNavButton(jbSelKnjig);

    jlrNazknjig.setColumnName("NAZIV");
    jlrNazknjig.setNavProperties(jlrKnjig);
    jlrNazknjig.setSearchMode(1);

    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setColNames(new String[] {"CVAL", "NAZVAL"});
    jlrOznval.setTextFields(new JTextComponent[] {jlrCval, jlrNazval});
    jlrOznval.setVisCols(new int[] {0, 1});
    jlrOznval.setSearchMode(0);
    jlrOznval.setRaDataSet(dm.getValute());
    jlrOznval.setNavButton(jbSelOznval);

    jlrCval.setColumnName("CVAL");
    jlrCval.setNavProperties(jlrOznval);
    jlrCval.setSearchMode(1);

    jlrNazval.setColumnName("NAZVAL");
    jlrNazval.setNavProperties(jlrOznval);
    jlrNazval.setSearchMode(1);

    jlBezgotovinska.setText("Bezgotovinska");
    jpDetail.add(jbSelKnjig, new XYConstraints(555, 20, 21, 21));
    jpDetail.add(jbSelOznval, new XYConstraints(555, 95, 21, 21));
    jpDetail.add(jlCblag, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlKnjig, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlNaziv, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlOznval, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlPvsaldo,  new XYConstraints(15, 190, -1, -1));
    jpDetail.add(jlSaldo,  new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jlaBrizv,  new XYConstraints(451, 148, 98, -1));
    jpDetail.add(jlaDatizv,  new XYConstraints(346, 148, 98, -1));
    jpDetail.add(jlaSaldo,  new XYConstraints(151, 148, 188, -1));
    jpDetail.add(jlrCval, new XYConstraints(200, 95, 50, -1));
    jpDetail.add(jlrKnjig, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNazknjig, new XYConstraints(255, 20, 295, -1));
    jpDetail.add(jlrNazval, new XYConstraints(255, 95, 295, -1));
    jpDetail.add(jlrOznval, new XYConstraints(150, 95, 45, -1));
    jpDetail.add(jraBrizv,  new XYConstraints(450, 165, 100, -1));
    jpDetail.add(jraCblag, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatizv,  new XYConstraints(345, 165, 100, -1));
    jpDetail.add(jraNaziv, new XYConstraints(150, 70, 400, -1));
    jpDetail.add(jraPvsaldo,  new XYConstraints(150, 190, 190, -1));
    jpDetail.add(jraSaldo,   new XYConstraints(150, 165, 190, -1));
    jpDetail.add(jrcbBrezgot,      new XYConstraints(150, 123, -1, -1));
    jpDetail.add(jlBezgotovinska,          new XYConstraints(15, 124, -1, -1));
    rebind();
    this.add(jpDetail, BorderLayout.CENTER);
  }

//  void datumFokuslost(){
//    String oznval = fBlag.getRaQueryDataSet().getString("OZNVAL");
//    if (!oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL()) && !jraDatizv.getText().equals("")){
//      fBlag.getRaQueryDataSet().setBigDecimal("PVSALDO",
//          ss.currrencyConverterToKN(fBlag.getRaQueryDataSet().getBigDecimal("SALDO"),oznval,
//          fBlag.getRaQueryDataSet().getTimestamp("DATIZV")));
//    }
//  }

  void rebind() {
    jrcbBrezgot.setDataSet(fBlag.getRaQueryDataSet());
    jraCblag.setDataSet(fBlag.getRaQueryDataSet());
    jraBrizv.setDataSet(fBlag.getRaQueryDataSet());
    jraDatizv.setDataSet(fBlag.getRaQueryDataSet());
    jraNaziv.setDataSet(fBlag.getRaQueryDataSet());
    jraPvsaldo.setDataSet(fBlag.getRaQueryDataSet());
    jraSaldo.setDataSet(fBlag.getRaQueryDataSet());
    jlrKnjig.setDataSet(fBlag.getRaQueryDataSet());
    jlrOznval.setDataSet(fBlag.getRaQueryDataSet());   
  }

  void lookup(){
    if (fBlag.getMode() == 'N') {
      if (!fBlag.getRaQueryDataSet().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
        rcc.setLabelLaF(jraPvsaldo, true);
      } else {
        rcc.setLabelLaF(jraPvsaldo, false);
      }
    }
  }

  void jrcbBrezgot_actionPerformed(ActionEvent e) {
    fBlag.enableDisableSaldo(jrcbBrezgot.isSelected());
  }

  void jraSaldo_focusLost() {
    String oznval = fBlag.getRaQueryDataSet().getString("OZNVAL");
    if (!oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      if (fBlag.getRaQueryDataSet().getTimestamp("DATIZV").before(java.sql.Timestamp.valueOf("1990-01-01 00:00:00.0"))){
//        System.out.println("datizv " + fBlag.getRaQueryDataSet().getTimestamp("DATIZV"));
        fBlag.getRaQueryDataSet().setBigDecimal("PVSALDO",
            ss.currrencyConverterToKN(fBlag.getRaQueryDataSet().getBigDecimal("SALDO"),oznval,
            vl.getToday()));
      } else {
        fBlag.getRaQueryDataSet().setBigDecimal("PVSALDO",
            ss.currrencyConverterToKN(fBlag.getRaQueryDataSet().getBigDecimal("SALDO"),oznval,
            fBlag.getRaQueryDataSet().getTimestamp("DATIZV")));
      }
    }
  }
}