/****license*****************************************************************
**   file: raVtrabat.java
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
import hr.restart.swing.JraDialog;

import javax.swing.JPanel;

public class raVtrabat extends JraDialog {

  JPanel jPan = new JPanel();
  JPanel jPan1 = new JPanel();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  String cskl  = "";
  String vrdok = "";
  String god   = "";
  int brdok = 0;
  short rbr = 0;
  raFilteriRazni rFR = new raFilteriRazni();
  raFilteriRazni.fVtrabat filter;
//  hr.restart.util.raJPTableView rjp = new hr.restart.util.raJPTableView();

  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
  public void jBOK_actionPerformed(){
    pressOK();
  }
  public void jPrekid_actionPerformed(){
    pressCancel();
  }};
  public void pressOK(){
//    dm.getvshrab_rab().removeRowFilterListener(filter);
    this.hide();
  }
  public void pressCancel(){
//      dm.getvshrab_rab().removeRowFilterListener(filter);
    this.hide();
  }


  public raVtrabat() {
   filter = rFR.getfVtrabat();
   try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setCriteria(String skladiste,String vrstadokumenta,String godina,int brojdok,short rbroj){
      cskl  = skladiste;
      vrdok = vrstadokumenta;
      god   = godina;
      brdok = brojdok;
      rbr = rbroj ;

      filter.setCriteria(cskl,vrdok,god,brdok,rbr);
      try {
        dm.getVtrabat().removeRowFilterListener(filter);
        dm.getVtrabat().addRowFilterListener(filter);
      } catch (Exception e) {}

      dm.getVtrabat().refilter();
  }
  public void show() {
//    ((hr.restart.swing.JraTable2)this.getJpTableView().getMpTable()).fireTableDataChanged(); /// da nema praznih kolona
    super.show();
  }

  private void jbInit() throws Exception {
/*   this.setRaDataSet(dm.getVtrabat());
    this.setRaColumnSifra("CRAB");
//    this.setRaColumnNaziv("NAZRAB");
    this.setRaText("Rabati");*/
  }
}