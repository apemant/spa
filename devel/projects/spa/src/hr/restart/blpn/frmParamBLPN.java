/****license*****************************************************************
**   file: frmParamBLPN.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLoader;
import hr.restart.util.raMatPodaci;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmParamBLPN extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpParamBLPN jpDetail;
  QueryDataSet vrshemek;
  StorageDataSet  stavke;


  public frmParamBLPN() {
    try {
//      za lookup cskl
      vrshemek = ((hr.restart.blpn.frmVrskBLPN)raLoader.load("hr.restart.blpn.frmVrskBLPN")).getRaQueryDataSet();
//      System.out.println("vrshemek : " + vrshemek);
      vrshemek.open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  boolean prviUlaz;

  public void EntryPoint(char mode) {
//    System.out.println("entry pojint");
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      prviUlaz = true;
//      rcc.setLabelLaF(jpDetail.jraCskl, false);
    }
  }

  public void SetFokus(char mode) {
//    System.out.println("setfocus");
    if (mode == 'N') {
      endeFields(false);
      jpDetail.jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
      jpDetail.jlrCorg.forceFocLost();
      getRaQueryDataSet().setString("VRDOK", "PN");
      jpDetail.jlrCskl.requestFocus();
    } else if (mode == 'I') {
      prviUlaz = false;
      endeFields(false);
      jpDetail.jlrCskl.requestFocus();
    }
  }

  void endeFields(boolean stat){
    rcc.setLabelLaF(jpDetail.jlrStavkapni, stat);
    rcc.setLabelLaF(jpDetail.jlrOpispni, stat);
    rcc.setLabelLaF(jpDetail.jbSelStavkapni, stat);

    rcc.setLabelLaF(jpDetail.jlrStavkapnz, stat);
    rcc.setLabelLaF(jpDetail.jlrOpispnz, stat);
    rcc.setLabelLaF(jpDetail.jbSelStavkapnz, stat);

    rcc.setLabelLaF(jpDetail.jlrCskl, !stat);
    rcc.setLabelLaF(jpDetail.jlrOpisvrsk, !stat);
    rcc.setLabelLaF(jpDetail.jbSelCskl, !stat);
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCskl) || vl.isEmpty(jpDetail.jlrStavkapnz) || vl.isEmpty(jpDetail.jlrStavkapni))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jlrCskl))
      return false;
    return true;
  }

  public void AfterSave(char mode){
    if (mode == 'N'){
      jpDetail.jlrCskl.emptyTextFields();
      jpDetail.jlrStavkapni.emptyTextFields();
      jpDetail.jlrStavkapnz.emptyTextFields();
    }
  }

  public void lookThis(char mode){
    String cskl = jpDetail.jlrCskl.getDataSet().getString(jpDetail.jlrCskl.getColumnName());
    stavke = hr.restart.sisfun.Asql.getShkonta("blpn", cskl , "PN");

    jpDetail.jlrStavkapni.setRaDataSet(stavke);
    jpDetail.jlrStavkapnz.setRaDataSet(stavke);

    if (!jpDetail.jlrCskl.getText().equals("") && mode=='N'){
      endeFields(true);
      jpDetail.jlrStavkapnz.requestFocus();
    }

    if (mode =='I' && !prviUlaz){
//      System.out.println("lookThis && mode je I(zmjena)");
      endeFields(true);
      jpDetail.jlrStavkapni.setText("");
      jpDetail.jlrStavkapnz.setText("");
      jpDetail.jlrStavkapni.emptyTextFields();
      jpDetail.jlrStavkapnz.emptyTextFields();
      jpDetail.jlrStavkapnz.requestFocus();
    }
  }

  public boolean ValDPEscape(char mode){
//    System.out.println("ValDPEscape()");
    if(!jpDetail.jlrCskl.getText().equals("")){
      jpDetail.jlrCskl.setText("");
      jpDetail.jlrCskl.emptyTextFields();
      jpDetail.jlrStavkapni.setText("");
      jpDetail.jlrStavkapnz.setText("");
      jpDetail.jlrStavkapni.emptyTextFields();
      jpDetail.jlrStavkapnz.emptyTextFields();
      endeFields(false);
      jpDetail.jlrCskl.requestFocus();
      return false;
    }
    return true;
  }


  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getParamblpn());
    this.setVisibleCols(new int[] {0, 1, 2, 3});
    jpDetail = new jpParamBLPN(this);
    this.setRaDetailPanel(jpDetail);
    getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier(
        "KNJIG",
        new String[] {"CORG","NAZIV"},
        new String[] {"KNJIG"},
        new String[] {"CORG"},
        hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva()
    ));
    getJpTableView().addTableModifier(
    new hr.restart.swing.raTableColumnModifier(
        "CSKL",
        new String[] {"CVRSK","OPISVRSK"},
        new String[] {"CSKL"},
        new String[] {"CVRSK"},
        vrshemek
    ));

  }
}
