/****license*****************************************************************
**   file: frmPrimanja.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class frmPrimanja extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpRadnicipl jpMaster;
  jpPrimanjaDetail jpDetail;
  short oldRBR;
  String[] key = new String[] {"CRADNIK"};
  private frmRSPeriod fRSPeriod;
  raNavAction rnvPeriodRS = new raNavAction("Periodi za RS",raImages.IMGSTAV,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      fRSPeriod = frmRSPeriod.go(getDetailSet());
    }
  };
  raNavAction rnvReCalc  = new raNavAction("Prera\u010Dunaj zarade",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      reCalc();
    }
  };
  public frmPrimanja() {
    super(1, 2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokusMaster(char mode) {
  }
  public boolean ValidacijaMaster(char mode) {
    return true;
  }
  public void SetFokusDetail(char mode) {
    System.out.println("SetFokusDetail: "+mode);
    if (mode == 'N') {
      //jpDetail.jlrCvrp.forceFocLost();
      getDetailSet().setBigDecimal("KOEF", getKoef());
      getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
      jpDetail.jlrCvrp.requestFocus();
    } else if (mode == 'I') {
      findFocusAfter();
      jpDetail.jraSati.requestFocus();
    }
    System.out.println("end FokusDetail");
  }
  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jlrCvrp))
      return false;
    if (vl.isEmpty(jpDetail.jlrCorg))
      return false;
    if (mode == 'N') /**@todo: Provjeriti jedinstvenost kljuca detaila */
      getDetailSet().setShort("RBR", (short) findRBR());
    return true;
  }
  public boolean DeleteCheckDetail() {
    oldRBR=getDetailSet().getShort("RBR");
    return true;
  }
  public void AfterDeleteDetail() {
    vl.recountDataSet(raDetail, "RBR", oldRBR);
  }
    public void refilterDetailSet() {
    super.refilterDetailSet();
    lookupData.getlookupData().raLocate(dm.getRadnici(),new String[] {"CRADNIK"},new String[] {getMasterSet().getString("CRADNIK")});
    raDetail.setTitle("Primanja - "+getMasterSet().getString("CRADNIK")+" "+dm.getRadnici().getString("IME")+" "+dm.getRadnici().getString("PREZIME"));
    if (fRSPeriod!=null && fRSPeriod.isShowing()) fRSPeriod.setPrimanja(getDetailSet());
  }
  public void AfterSaveDetail(char mode) {
    jpDetail.dateOn(false);
  }
  public void reCalc() {
    raCalcPrimanja.getRaCalcPrimanja().calcPrimanja(this);
  }
  private void jbInit() throws Exception {
    this.setMasterSet(dm.getRadnicipl());
    this.setNaslovMaster("Unos primanja - radnici");
    this.setVisibleColsMaster(new int[] {0, 1, 2, 3});
    this.setMasterKey(key);
    jpMaster = new jpRadnicipl(this.raMaster);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getPrimanjaobr());
    this.setNaslovDetail("Primanja");
    this.setVisibleColsDetail(new int[] {1, 4, 5, 6});
    this.setDetailKey(key);
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"SATI", "BRUTO", "DOPRINOSI", "NETO"});
    setnaslovi_detail(new String[] {"Sati", "Iznos", "Doprinosi", "Neto"});

    raMaster.getJpTableView().setNoTablePanel(new frmRadnicipl.jpNoTableGetRadnici(raMaster.getJpTableView()));
    jpDetail = new jpPrimanjaDetail(this);
    this.setJPanelDetail(jpDetail);
    raMaster.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getRadnici())
    );
    raMaster.setEditEnabled(false);
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
    raDetail.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim())
    );
    raDetail.addOption(rnvPeriodRS,3);
    raDetail.addOption(rnvReCalc,4);
  }
  void findFocusAfter() {
    jpDetail.jbSelCvrp.requestFocus();
    sjUtil.getSjUtil().findFocusAfter(jpDetail.jlrCvrp.getText(), jpDetail.jraBruto, jpDetail.jraKoef, jpDetail.jraSati);
    if (dm.getVrsteprim().getString("REGRES").equals("D")) {
      jpDetail.dateOn(true);
      this.getDetailSet().setTimestamp("IRAZOD", hr.restart.robno.Util.getUtil().findFirstDayOfYear());
      this.getDetailSet().setTimestamp("IRAZDO", hr.restart.robno.Util.getUtil().findLastDayOfYear(2002));
    }
    else {
      jpDetail.dateOn(false);
    }
  }
  java.math.BigDecimal getKoef() {
    return new java.math.BigDecimal(100);
  }
  int findRBR() {
    vl.execSQL(sjQuerys.getRbr(getDetailSet().getString("CRADNIK")));
    vl.RezSet.open();
    return vl.RezSet.getShort(0)+1;
  }
}
