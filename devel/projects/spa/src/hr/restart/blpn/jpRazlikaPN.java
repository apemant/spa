/****license*****************************************************************
**   file: jpRazlikaPN.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRazlikaPN extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmRazlikaPN fRazlikaPN;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCblag = new JLabel();
  JLabel jlCpn = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlIzdatak = new JLabel();
  JLabel jlaIzdatak = new JLabel();
  JLabel jlaPrimitak = new JLabel();
  JraButton jbSelCblag = new JraButton();
  JraTextField jraDatum = new JraTextField();
  JraTextField jraIzdatak = new JraTextField();
  JraTextField jraPrimitak = new JraTextField();
  JlrNavField jlrCblag = new JlrNavField() {
    public void after_lookUp() {
      fRazlikaPN.updateValutaLabel();
    }
  };
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraTextField jraCpn = new JraTextField();
  JLabel jlValuta = new JLabel();
  JLabel jlValuta2 = new JLabel();

  public jpRazlikaPN(frmRazlikaPN f) {
    try {
      fRazlikaPN = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(606);
    lay.setHeight(125);

    jlCblag.setText("Blagajna");
    jlCpn.setText("Broj putnog naloga");
    jlDatum.setText("Datum uplate - isplate");
    jlIzdatak.setText("Razlika");
    jlaIzdatak.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIzdatak.setText("Izdatak");
    jlaPrimitak.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrimitak.setText("Primitak");
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(fRazlikaPN.getRaQueryDataSet());

    jraIzdatak.setColumnName("IZDATAK");
    jraIzdatak.setDataSet(fRazlikaPN.getRaQueryDataSet());

    jraPrimitak.setColumnName("PRIMITAK");
    jraPrimitak.setDataSet(fRazlikaPN.getRaQueryDataSet());

    jlrCblag.setColumnName("CBLAG");
    jlrCblag.setDataSet(fRazlikaPN.getRaQueryDataSet());
    jlrCblag.setColNames(new String[] {"OZNVAL", "NAZIV"});
    jlrCblag.setTextFields(new JTextComponent[] {jlrOznval, jlrNaziv});
    jlrCblag.setVisCols(new int[] {1, 2, 3});
    jlrCblag.setSearchMode(0);
    jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrOznval.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrNaziv.setRaDataSet(frmBlag.getBlagajneKnjig());
      }
    });
    jlrCblag.setNavButton(jbSelCblag);

    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setNavProperties(jlrCblag);
    jlrOznval.setSearchMode(1);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCblag);
    jlrNaziv.setSearchMode(1);

    jraCpn.setHorizontalAlignment(SwingConstants.CENTER);
    jraCpn.setColumnName("CPN");
    jraCpn.setDataSet(fRazlikaPN.getRaQueryDataSet());

    jpDetail.setPreferredSize(new Dimension(606, 125));
    jpDetail.add(jbSelCblag, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jlCblag, new XYConstraints(15, 47, -1, -1));
    jpDetail.add(jlCpn, new XYConstraints(300, 22, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 22, -1, -1));
    jpDetail.add(jlIzdatak, new XYConstraints(15, 92, -1, -1));
    jpDetail.add(jlrCblag, new XYConstraints(150, 45, 45, -1));
    jpDetail.add(jraCpn,   new XYConstraints(450, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 45, 295, -1));
    jpDetail.add(jlValuta,    new XYConstraints(255, 92, -1, -1));
    jpDetail.add(jlValuta2,    new XYConstraints(555, 92, -1, -1));
    jpDetail.add(jlrOznval, new XYConstraints(200, 45, 50, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraIzdatak, new XYConstraints(450, 90, 100, -1));
    jpDetail.add(jraPrimitak, new XYConstraints(150, 90, 100, -1));
    jpDetail.add(jlaIzdatak, new XYConstraints(451, 73, 98, -1));
    jpDetail.add(jlaPrimitak, new XYConstraints(151, 73, 98, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
