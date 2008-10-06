/****license*****************************************************************
**   file: jpIzvjDefDetail.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpIzvjDefDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmIzvjDef fIzvjDef;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCgrizv = new JLabel();
  JLabel jlCizv = new JLabel();
  JLabel jlNaziv = new JLabel();
  JraTextField jraCgrizv = new JraTextField();
  JraTextField jraCizv = new JraTextField();
  JraTextField jraNaziv = new JraTextField();
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraCheckBox jcbBruto = new JraCheckBox();
  JraCheckBox jcbSati = new JraCheckBox();
  JraCheckBox jcbNeto = new JraCheckBox();
  JraCheckBox jcbNeto2 = new JraCheckBox();
  Border border1;
  TitledBorder titledBorder1;
  JraCheckBox jcbAktiv = new JraCheckBox();
  JLabel jlOznaka = new JLabel();

  public jpIzvjDefDetail(frmIzvjDef md) {
    try {
      fIzvjDef = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Suma");
    jpDetail.setLayout(lay);
    lay.setWidth(575);
    lay.setHeight(160);

    jraCgrizv.setNextFocusableComponent(jraNaziv);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fIzvjDef.getDetailSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jlCgrizv.setText("Grupa izvještaja");
    jlCizv.setText("Oznaka izvještaja");
    jlNaziv.setText("Naziv");
    jraCgrizv.setColumnName("CGRIZV");
    jraCgrizv.setDataSet(fIzvjDef.getDetailSet());
    jraCizv.setColumnName("CIZV");
    jraCizv.setDataSet(fIzvjDef.getDetailSet());
    jraNaziv.setColumnName("NAZIV");
    jraNaziv.setDataSet(fIzvjDef.getDetailSet());

    jcbBruto.setColumnName("SUMBRUTO");
    jcbBruto.setDataSet(fIzvjDef.getDetailSet());
    jcbBruto.setSelectedDataValue("D");
    jcbBruto.setUnselectedDataValue("N");

    jcbSati.setColumnName("SUMSATI");
    jcbSati.setDataSet(fIzvjDef.getDetailSet());
    jcbSati.setSelectedDataValue("D");
    jcbSati.setUnselectedDataValue("N");

    jcbNeto.setColumnName("SUMNETO");
    jcbNeto.setDataSet(fIzvjDef.getDetailSet());
    jcbNeto.setSelectedDataValue("D");
    jcbNeto.setUnselectedDataValue("N");

    jcbNeto2.setColumnName("SUMNETO2");
    jcbNeto2.setDataSet(fIzvjDef.getDetailSet());
    jcbNeto2.setSelectedDataValue("D");
    jcbNeto2.setUnselectedDataValue("N");

    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setWidth(465);
    xYLayout1.setHeight(45);
    jcbBruto.setText("Bruto");
    jcbSati.setText("Sati");
    jcbNeto.setText("Neto");
    jcbNeto2.setText("Iznos na ruke");
    //jpDetail.add(jlCizv, new XYConstraints(15, 20, -1, -1));
    //jpDetail.add(jraCizv, new XYConstraints(150, 20, 100, -1));
    jlOznaka.setText("Oznaka");
//    this.setPreferredSize(new Dimension(545, 170));
    jPanel1.add(jcbBruto,   new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jcbNeto2,         new XYConstraints(290, 0, -1, -1));
    jPanel1.add(jcbNeto,      new XYConstraints(200, 0, -1, -1));
    jPanel1.add(jcbSati,      new XYConstraints(110, 0, -1, -1));
    jpDetail.add(jlNaziv,   new XYConstraints(205, 25, -1, -1));
    jpDetail.add(jlOznaka,   new XYConstraints(150, 25, -1, -1));
    jpDetail.add(jcbAktiv,         new XYConstraints(485, 20, 70, -1));
    jpDetail.add(jraCgrizv,    new XYConstraints(150, 45, 50, -1));
    jpDetail.add(jraNaziv,    new XYConstraints(205, 45, 350, -1));
    jpDetail.add(jlCgrizv,  new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jPanel1,      new XYConstraints(150, 72, 405, 62));
    this.add(jpDetail, BorderLayout.CENTER);

  }
}
