/****license*****************************************************************
**   file: jpSifrarniciDetail.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSifrarniciDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSifrarnici fSifrarnici;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCsif = new JLabel();
  JraCheckBox jlCsifprip = new JraCheckBox();
  JLabel jlNaziv = new JLabel();
  JLabel jlOpis = new JLabel();
  JraButton jbSelCsifprip = new JraButton();
  JraTextField jraCsif = new JraTextField();
  JraTextField jraNaziv = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCsifprip = new JlrNavField() {
    public void after_lookUp() {
//      System.out.println("Okinuo loooooooooooooosefocus");
    }
  };

  public jpSifrarniciDetail(frmSifrarnici md) {
    try {
      fSifrarnici = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(135);

    jbSelCsifprip.setText("...");
    jlCsif.setText("Šifra");
    jlCsifprip.setHorizontalTextPosition(SwingConstants.LEADING);
    jlCsifprip.setText("Pripadnost");
    jlCsifprip.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jlCsifprip_actionPerformed(e);
      }
    });
    jlNaziv.setText("Naziv šifre");
    jlOpis.setText("Opis šifre");
    jraCsif.setColumnName("CSIF");
    jraCsif.setDataSet(fSifrarnici.getDetailSet());
    jraCsif.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        setNavs(jlCsifprip.isSelected());
      }
    });
    jraNaziv.setColumnName("NAZIV");
    jraNaziv.setDataSet(fSifrarnici.getDetailSet());
    jraNaziv.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        setNavs(jlCsifprip.isSelected());
      }
    });
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(fSifrarnici.getDetailSet());

    jlrCsifprip.setColumnName("CSIFPRIP");
    jlrCsifprip.setNavColumnName("CSIF");
    jlrCsifprip.setDataSet(fSifrarnici.getDetailSet());
    jlrCsifprip.setColNames(new String[] {"NAZIV"});
    jlrCsifprip.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCsifprip.setVisCols(new int[] {0, 1});
    jlrCsifprip.setSearchMode(0);
    jlrCsifprip.setRaDataSet(Sifrarnici.getSifre(""));
    jlrCsifprip.setNavButton(jbSelCsifprip);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCsifprip);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCsifprip, new XYConstraints(555, 95, 21, 21));
    jpDetail.add(jlCsif, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCsifprip,  new XYConstraints(15, 95, 117, -1));
    jpDetail.add(jlNaziv, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrCsifprip, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 95, 295, -1));
    jpDetail.add(jraCsif, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraNaziv, new XYConstraints(150, 45, 300, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 70, 400, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }

  void jlCsifprip_actionPerformed(ActionEvent e) {
    boolean iskljuciv;
    if(jlCsifprip.isSelected()) iskljuciv = true;
    else iskljuciv = false;
    setNavs(iskljuciv);
  }
  void setNavs(boolean iskljuciv) {
    rcc.setLabelLaF(jlrCsifprip, iskljuciv);
    rcc.setLabelLaF(jlrNaziv, iskljuciv);
    rcc.setLabelLaF(jbSelCsifprip, iskljuciv);
    if (iskljuciv&&false){
      jlrCsifprip.setText("");
      jlrNaziv.setText("");
      jlrCsifprip.requestFocus();
    } else if (!iskljuciv){
      jlrCsifprip.setText(jraCsif.getText().trim());
      jlrNaziv.setText(jraNaziv.getText().trim());
    }
  }
}
