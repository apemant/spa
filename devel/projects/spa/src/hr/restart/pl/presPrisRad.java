/****license*****************************************************************
**   file: presPrisRad.java
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
import hr.restart.swing.JraButton;
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

public class presPrisRad extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  XYLayout lay = new XYLayout();
  JPanel jpDetail = new JPanel();

  JLabel jlRadnik = new JLabel();
  JLabel jlaCradnik = new JLabel();
  JLabel jlaIme = new JLabel();
  JLabel jlaPrezime = new JLabel();
  JlrNavField jlrCradnik = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();
  JlrNavField jlrPrezime = new JlrNavField();
  JraButton jbSelCradnik = new JraButton();

  public presPrisRad() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(105);

    jlRadnik.setText("Djelatnik");
    jlaIme.setText("Ime");
    jlaPrezime.setText("Prezime");
    jlaCradnik.setText("M.B.");
    jlaCradnik.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIme.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrezime.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jpDetail.add(jlaCradnik, new XYConstraints(151,23,48,-1));
    jpDetail.add(jlrCradnik, new XYConstraints(150,40,50,-1));
    jpDetail.add(jlaIme, new XYConstraints(206,23,168,-1));
    jpDetail.add(jlrIme, new XYConstraints(205,40,170,-1));
    jpDetail.add(jlaPrezime, new XYConstraints(381,23,168,-1));
    jpDetail.add(jlrPrezime, new XYConstraints(380,40,170,-1));
    jpDetail.add(jlRadnik, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jbSelCradnik, new XYConstraints(555, 40, 21, 21));

    this.setSelDataSet(dm.getPrisutobr());
    this.setSelPanel(jpDetail);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
      }
    });
  }

//  public void show(){
//    super.show();
//    System.out.println("show!!!!!!");
//    if (!jlrCradnik.getText().equals("")){
//      jlrCradnik.setText("");
//      jlrCradnik.forceFocLost();
//    }
//  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrCradnik))
      return false;
    return true;
  }
}