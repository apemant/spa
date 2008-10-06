/****license*****************************************************************
**   file: jpAkontacijaPN.java
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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpAkontacijaPN extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmAkontacijaPN fAkontacijaPN;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCblag = new JLabel();
  JLabel jlCpn = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlIzdatak = new JLabel();
  JraButton jbSelCblag = new JraButton();
  JraButton jbSelCpn = new JraButton();
  JraTextField jraDatum = new JraTextField();
  JraTextField jraIzdatak = new JraTextField();
  JlrNavField jlrCblag = new JlrNavField() {
    public void after_lookUp() {
      fAkontacijaPN.updateValutaLabel();
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
  JlrNavField jlrCzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpn = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JLabel jlValuta = new JLabel();

  public jpAkontacijaPN(frmAkontacijaPN f) {
    try {
      fAkontacijaPN = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(606);
    lay.setHeight(110);

    jlCblag.setText("Blagajna");
    jlCpn.setText("Broj putnog naloga");
    jlDatum.setText("Datum isplate");
    jlIzdatak.setText("Akontacija");
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(fAkontacijaPN.getRaQueryDataSet());

    jraIzdatak.setColumnName("IZDATAK");
    jraIzdatak.setDataSet(fAkontacijaPN.getRaQueryDataSet());

    jlrCzem.setColumnName("CZEMLJE");
    jlrCzem.setNavProperties(jlrCpn);
    jlrCzem.setVisible(false);
    jlrCzem.setSearchMode(1);

    jlrCblag.setColumnName("CBLAG");
    jlrCblag.setDataSet(fAkontacijaPN.getRaQueryDataSet());
    jlrCblag.setColNames(new String[] {"OZNVAL", "NAZIV"});
    jlrCblag.setTextFields(new JTextComponent[] {jlrOznval, jlrNaziv});
    jlrCblag.setVisCols(new int[] {1, 2});
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

    jlrCpn.setHorizontalAlignment(SwingConstants.CENTER);
    jlrCpn.setColumnName("CPN");
    jlrCpn.setDataSet(fAkontacijaPN.getRaQueryDataSet());
    jlrCpn.setColNames(new String[] {"CZEMLJE"});
    jlrCpn.setTextFields(new JTextComponent[] {jlrCzem});
    jlrCpn.setVisCols(new int[] {3, 5, 11});
    jlrCpn.setSearchMode(0);
    jlrCpn.setRaDataSet(dm.getPutniNalog());
    jlrCpn.setNavButton(jbSelCpn);

    jpDetail.add(jbSelCblag, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jlCpn, new XYConstraints(300, 20, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlIzdatak, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrCpn,    new XYConstraints(410, 20, 140, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraIzdatak, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlrCzem, new XYConstraints(0, 0, 0, 0));
    jpDetail.add(jlValuta,    new XYConstraints(255, 70, -1, -1));
    jpDetail.add(jlCblag, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCblag, new XYConstraints(150, 45, 45, -1));
    jpDetail.add(jlrOznval, new XYConstraints(200, 45, 50, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 45, 295, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
