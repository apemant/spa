/****license*****************************************************************
**   file: presRPNizUplIspl.java
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
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presRPNizUplIspl extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  sgStuff ss = sgStuff.getStugg();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCpn = new JLabel();
  JLabel jlaCradnik = new JLabel();
  JLabel jlaIme = new JLabel();
  JLabel jlaPrezime = new JLabel();
  JLabel jlRadnik = new JLabel();

  JraButton jbSelCradnik = new JraButton();
  JlrNavField jlrCpn = new JlrNavField();
  JlrNavField jlrCradnik = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();
  JlrNavField jlrPrezime = new JlrNavField();

  JraTextField jraCskl = new JraTextField();
  JraTextField jraStavka = new JraTextField();
  JraTextField jraCblag = new JraTextField();
  JraTextField jraOznval = new JraTextField();

  public presRPNizUplIspl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getStavblag());
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(105);

    jlCpn.setText("Broj putnog naloga");
    jlRadnik.setText("Djelatnik");
    jlaIme.setText("Ime");
    jlaPrezime.setText("Prezime");
    jlaCradnik.setText("M.B.");
    jlaCradnik.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIme.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrezime.setHorizontalAlignment(SwingConstants.CENTER);

    jraCskl.setColumnName("CSKL");
    jraCskl.setDataSet(getSelDataSet());
    jraCskl.setVisible(false);
    jraCskl.setEnabled(false);

    jraStavka.setColumnName("STAVKA");
    jraStavka.setDataSet(getSelDataSet());
    jraStavka.setVisible(false);
    jraStavka.setEnabled(false);

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(getSelDataSet());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME", "CPN"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime, jlrCpn});
    jlrCradnik.setVisCols(new int[] {0, 1, 2, 3});
    jlrCradnik.setSearchMode(0);
    setCradnik();
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCpn.setColumnName("CPN");
    jlrCpn.setNavProperties(jlrCradnik);
    jlrCpn.setSearchMode(1);

    jraCblag.setColumnName("CBLAG");
    jraCblag.setDataSet(getSelDataSet());
    jraCblag.setVisible(false);
    jraCblag.setEnabled(false);

    jraOznval.setColumnName("OZNVAL");
    jraOznval.setDataSet(getSelDataSet());
    jraOznval.setVisible(false);
    jraOznval.setEnabled(false);

    jpDetail.add(jraCblag, new XYConstraints(0, 0, 0, 0));
    jpDetail.add(jraOznval, new XYConstraints(0, 0, 0, 0));

    jpDetail.add(jlaCradnik, new XYConstraints(151,23,48,-1));
    jpDetail.add(jlrCradnik, new XYConstraints(150,40,50,-1));
    jpDetail.add(jlaIme, new XYConstraints(206,23,168,-1));
    jpDetail.add(jlrIme, new XYConstraints(205,40,170,-1));
    jpDetail.add(jlaPrezime, new XYConstraints(381,23,168,-1));
    jpDetail.add(jlrPrezime, new XYConstraints(380,40,170,-1));
    jpDetail.add(jlRadnik, new XYConstraints(15, 40, -1, -1));

    jpDetail.add(jbSelCradnik, new XYConstraints(555, 40, 21, 21));

    jpDetail.add(jlCpn, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlrCpn, new XYConstraints(150, 65, 100, -1));

    jpDetail.add(jraCskl, new XYConstraints(0, 0, 0, 0));
    jpDetail.add(jraStavka, new XYConstraints(0, 0, 0, 0));
    this.setSelPanel(jpDetail);
  }

  protected void setCradnik() {
    jlrCradnik.setRaDataSet(ss.getPutniNaloziPrAk('R'));
  }

  public void SetFokus(){
    getSelRow().deleteAllRows();
    jlrCradnik.emptyTextFields();
    jlrCradnik.getRaDataSet().refresh();
    getSelRow().setString("CSKL", "6");
    getSelRow().setString("STAVKA", "2");
    jlrCradnik.requestFocus();
  }

  public boolean Validacija(){
    return !vl.isEmpty(jlrCpn);
  }
}