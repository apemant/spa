/****license*****************************************************************
**   file: jpIzvodiMaster.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpIzvodiMaster extends JPanel {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmIzvodi fIzvodi;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojizv = new JLabel();
  JLabel jlBrojstavki = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlId = new JLabel();
  JLabel jlPrethstanje = new JLabel();
  JLabel jlaDatum = new JLabel();
  JLabel jlaDatumknj = new JLabel();
  JLabel jlaId = new JLabel();
  JLabel jlaIp = new JLabel();
  JLabel jlaNovostanje = new JLabel();
  JLabel jlaPrethstanje = new JLabel();
  JraTextField jraBrojizv = new JraTextField();
  JraTextField jraBrojstavki = new JraTextField();
  JraTextField jraDatum = new JraTextField() {
    public void valueChanged() {
      setDatumknj();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      setDatumknj();
    }*/
  };
  JraTextField jraDatumknj = new JraTextField();
  JraTextField jraId = new JraTextField() {
    public void valueChanged() {
      fIzvodi.focLostIdIp();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fIzvodi.focLostIdIp();
    }*/
  };
  JraTextField jraIp = new JraTextField(){
    public void valueChanged() {
      fIzvodi.focLostIdIp();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fIzvodi.focLostIdIp();
    }*/
  };
  JraTextField jraNovostanje = new JraTextField();
  JraTextField jraPrethstanje = new JraTextField(){
    public void valueChanged() {
      fIzvodi.focLostIdIp();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fIzvodi.focLostIdIp();
    }*/
  };
  StorageDataSet datknjset = new StorageDataSet();
  Column cDATUMKNJ = (Column)dm.getNalozi().getColumn("DATUMKNJ").clone();
  private JPanel jPanel1 = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JPanel jPanel2 = new JPanel();
  private XYLayout xYLayout2 = new XYLayout();
  private JPanel jPanel3 = new JPanel();
  private XYLayout xYLayout3 = new XYLayout();
  public jpIzvodiMaster(frmIzvodi md) {
    try {
      fIzvodi = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    datknjset.setColumns(new Column[] {cDATUMKNJ});
    jpDetail.setLayout(lay);
    lay.setWidth(565);
    lay.setHeight(255);

    jlBrojizv.setText("Broj izvoda");
    jlBrojstavki.setText("Broj stavaka");
    jlDatum.setText("Datum");
    jlId.setText("Ukupno");
    jlPrethstanje.setText("Stanje");
    jlaDatum.setHorizontalAlignment(SwingConstants.LEFT);
    jlaDatum.setText("Izvoda");
    jlaDatumknj.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatumknj.setText("Knjiženja");
    jlaId.setHorizontalAlignment(SwingConstants.CENTER);
    jlaId.setText("Duguje");
    jlaIp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIp.setText("Potražuje");
    jlaNovostanje.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNovostanje.setText("Novo");
    jlaPrethstanje.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrethstanje.setText("Prethodno");
    jraBrojizv.setColumnName("BROJIZV");
    jraBrojizv.setDataSet(fIzvodi.getMasterSet());
    jraBrojstavki.setColumnName("BROJSTAVKI");
    jraBrojstavki.setDataSet(fIzvodi.getMasterSet());
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(fIzvodi.getMasterSet());
    jraDatumknj.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumknj.setColumnName("DATUMKNJ");
    jraDatumknj.setDataSet(datknjset);
    jraId.setColumnName("ID");
    jraId.setDataSet(fIzvodi.getMasterSet());
    jraIp.setColumnName("IP");
    jraIp.setDataSet(fIzvodi.getMasterSet());
    jraNovostanje.setColumnName("NOVOSTANJE");
    jraNovostanje.setDataSet(fIzvodi.getMasterSet());
    jraPrethstanje.setColumnName("PRETHSTANJE");
    jraPrethstanje.setDataSet(fIzvodi.getMasterSet());

    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jPanel1.setLayout(xYLayout1);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jPanel2.setLayout(xYLayout2);
    jPanel3.setBorder(BorderFactory.createEtchedBorder());
    jPanel3.setLayout(xYLayout3);
    jpDetail.add(jlBrojizv, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlBrojstavki, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlDatum,  new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jraBrojizv, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraBrojstavki, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlPrethstanje,   new XYConstraints(15, 185, -1, -1));
    jpDetail.add(jlId,   new XYConstraints(15, 130, -1, -1));
    jpDetail.add(jPanel1,      new XYConstraints(151, 75, 396, 50));
    jPanel1.add(jraDatum,    new XYConstraints(80, 13, 100, -1));
    jPanel1.add(jlaDatumknj,     new XYConstraints(210, 13, -1, -1));
    jPanel1.add(jraDatumknj,   new XYConstraints(275, 13, 100, -1));
    jPanel1.add(jlaDatum,    new XYConstraints(15, 13, -1, -1));
    jpDetail.add(jPanel2,        new XYConstraints(151, 130, 396, 50));
    jPanel2.add(jlaId,   new XYConstraints(15, 12, -1, -1));
    jPanel2.add(jraId,    new XYConstraints(80, 13, 100, -1));
    jPanel2.add(jlaIp,   new XYConstraints(210, 13, -1, -1));
    jPanel2.add(jraIp,   new XYConstraints(275, 13, 100, -1));
    jpDetail.add(jPanel3,      new XYConstraints(150, 185, 396, 50));
    jPanel3.add(jlaPrethstanje,    new XYConstraints(15, 15, -1, -1));
    jPanel3.add(jraPrethstanje,   new XYConstraints(80, 13, 100, -1));
    jPanel3.add(jlaNovostanje,   new XYConstraints(210, 13, -1, -1));
    jPanel3.add(jraNovostanje,     new XYConstraints(275, 13, 100, -1));
    this.add(jpDetail, BorderLayout.CENTER);

  }
  private void setDatumknj() {
    if (fIzvodi.raMaster.getMode()=='N') {
      datknjset.setTimestamp("DATUMKNJ",fIzvodi.getMasterSet().getTimestamp("DATUM"));
    }
  }
}
