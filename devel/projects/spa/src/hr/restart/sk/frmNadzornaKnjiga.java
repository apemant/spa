/****license*****************************************************************
**   file: frmNadzornaKnjiga.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.NadzornaKnjiga;
import hr.restart.baza.dM;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raPreSelectAware;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmNadzornaKnjiga extends raMatPodaci implements raPreSelectAware {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpNadzornaKnjiga jpDetail;
  static frmNadzornaKnjiga fNadzKnjiga;
  PreSelect pres;
  raNavAction rnvPreSel = new raNavAction("Predselekcija",raImages.IMGZOOM,KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      getPreSelect().showPreselect(frmNadzornaKnjiga.this,frmNadzornaKnjiga.this.getTitle());
    }
  };

  public frmNadzornaKnjiga() {
    try {
      jbInit();
      fNadzKnjiga = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmNadzornaKnjiga getInstance() {
    return fNadzKnjiga;
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      if (pres!=null) pres.copySelValues();
      getNewRbr();
      getRaQueryDataSet().setTimestamp("DATUM", Valid.getValid().getToday());
      jpDetail.jlrCparposr.forceFocLost();
      jpDetail.jlrCnacpl.forceFocLost();
      jpDetail.jlrCpar.forceFocLost();
      jpDetail.jlrZempodrijetla.forceFocLost();
      jpDetail.jraRbr.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraRbr, false);
      jpDetail.jraDatum.requestFocus();
    }
  }
  private void getNewRbr() {
    String qry = "SELECT max(RBR) from NadzornaKnjiga where KNJIG='"+getPreSelect().getSelRow().getString("KNJIG")
      +"' AND GOD='"+getPreSelect().getSelRow().getString("GOD")+"'";
    QueryDataSet qds = Util.getNewQueryDataSet(qry);
    int maxrbr = qds.getInt(0)+1;
    getRaQueryDataSet().setInt("RBR", maxrbr);
    getRaQueryDataSet().setInt("BRSTR", maxrbr);
  }
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraRbr)) return false;
    if (vl.isEmpty(jpDetail.jraBrstr)) return false;
		if (vl.isEmpty(jpDetail.jraDatum)) return false;
    if (mode == 'N' && 
        NadzornaKnjiga.getDataModule().getRowCount(Condition.whereAllEqual(
            new String[] {"KNJIG","GOD","RBR"}, getRaQueryDataSet())) > 0) {
        jpDetail.jraRbr.requestFocus();
        vl.showValidErrMsg(jpDetail.jraRbr, 'U');
      return false;
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getNadzornaKnjiga());
    this.setVisibleCols(new int[] {2,7,8,18});
    jpDetail = new jpNadzornaKnjiga(this);
    this.setRaDetailPanel(jpDetail);
    jpDetail.BindComponents(getRaQueryDataSet());
//Predselekcija
    int position = 5;
    addOption(rnvPreSel,position,false);
    getJpTableView().addTableModifier(new raTableColumnModifier("CPAR",new String[] {"CPAR","NAZPAR"},dm.getPartneri()));
//    getJpTableView().addTableModifier(new raTableColumnModifier("CPAR",new String[] {"CPAR","NAZPAR"},dm.getPartneri());
    startFrame.getStartFrame().centerFrame(this, 0, "Nadzorna knjiga uvoza");
    getRepRunner().addReport("hr.restart.sk.repNadzornaKnjiga","hr.restart.sk.repNadzornaKnjiga","NadzornaKnjiga","Nadzorna knjiga uvoza");
  }

  public PreSelect getPreSelect() {
    return pres;
  }

  public void setPreSelect(PreSelect _pres) {
    pres = _pres;
  }
  
  public void getURE(String broj) {
    QueryDataSet ureSet = hr.restart.baza.Skstavke.getDataModule().getFilteredDataSet(
      Condition.whereAllEqual(new String[] {"KNJIG","VRDOK"}, 
        new String[] {OrgStr.getKNJCORG(),"URN"}
      )
      + " AND BROJDOK like '" + broj + "%'");
    lookupFrame lufr = lookupFrame.getLookupFrame(this.getJframe(), ureSet, new int[] {1,4,9,13,17});
    lufr.ShowCenter(true,0,0);
    getRaQueryDataSet().setString("BRISPRAVE", ureSet.getString("BROJDOK"));
    getRaQueryDataSet().setTimestamp("DATDOK", ureSet.getTimestamp("DATDOK"));
    getRaQueryDataSet().setInt("CPAR", ureSet.getInt("CPAR"));
    jpDetail.jlrCpar.forceFocLost();
    getRaQueryDataSet().setBigDecimal("VRIJEDNOST", ureSet.getBigDecimal("IP"));
    int cmjesta = jpDetail.jlrCpar.getRaDataSet().getInt("CMJESTA");
    if (!lookupData.getlookupData().raLocate(dm.getAllMjesta(), "CMJESTA", cmjesta+"")) return;
    String czem = dm.getAllMjesta().getString("CZEM");
    if (!lookupData.getlookupData().raLocate(dm.getAllZpZemlje(), "CZEM", czem)) return;
    getRaQueryDataSet().setString("ZEMPODRIJETLA", dm.getAllZpZemlje().getString("OZNZEM"));
    jpDetail.jlrZempodrijetla.forceFocLost();
  }
  
}
