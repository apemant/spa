/****license*****************************************************************
**   file: frmCorgChanger.java
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
package hr.restart.os;

import javax.swing.JOptionPane;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmCorgChanger extends osTemplate {

  public frmCorgChanger() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm.getOS_Sredstvo().open();
    dm.getOS_Promjene().open();
    this.setMasterSet(dm.getOS_Sredstvo());
    this.setDetailSet(dm.getOS_Promjene());
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(this.jpSel);
    super.jbInitA();
    setJPanelMaster(jpMasterOS);
    setJPanelDetail(jpDetailOS);
    raMaster.setEditEnabled(false);
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[4],true);
    raDetail.setEnabledNavAction(raDetail.getNavBar().getNavContainer().getNavActions()[1],false);
    this.setNaslovMaster("Promjena organizacijske jedinice");
  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    rcc.setLabelLaF(jtfUIPRPOR, false);
    rcc.setLabelLaF(jtfOsnovica, false);
    rcc.setLabelLaF(jtfIspravak, false);
    System.out.println("setFokusDetail: "+mode);
    if (mode=='N') {
    }
    else if (mode=='I') {
      jtfDatum.requestFocus();
    }
    if(mode=='N') {
      if(!tds.isOpen())
        tds.open();
      if(tds.getRowCount()==0) {
        tds.insertRow(false);
      }
      getDetailSet().setString("OLDCORG", getDetailSet().getString("CORG2"));
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");
      tds.setBigDecimal("OSNOVICA", (getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK"))));
      tds.setBigDecimal("ISPRAVAK", (getMasterSet().getBigDecimal("ISPPOTRAZUJE").add(getMasterSet().getBigDecimal("ISPPOCETAK"))));
      jrfCPROMJENE.requestFocus();
//      getDetailSet().setTimestamp("DATPROMJENE", hr.restart.util.Valid.getValid().findDate(false, 0));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());
      rcc.setLabelLaF(jtfOsnovica, false);
      rcc.setLabelLaF(jtfIspravak, false);
      rcc.setLabelLaF(jtfUIPRPOR, false);
      rcc.setLabelLaF(jrfCORG, true);
      rcc.setLabelLaF(jrfNAZORG, true);
      rcc.setLabelLaF(jbCORG, true);
    }
    else if (mode=='I') {
      if(amor.equals("D")) {
         rcc.EnabDisabAll(jpDetailOS, false);
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         raDetail.getOKpanel().jPrekid_actionPerformed();
      }
      if(!rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L")) {
        rcc.EnabDisabAll(this.jpDetailOS, false);
        JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        raDetail.getOKpanel().jPrekid_actionPerformed();
      }
      else {
        rcc.EnabDisabAll(this.jpDetailOS, true);
      }
      checkLikvidacija();
      jtfDatum.requestFocus();
    }
  }
  public boolean DeleteCheckDetail() {
/*    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if(compare==0) {
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
      }
      else {
        return true;
      }
    }
*/
    System.out.println("CORG2: "+getDetailSet().getString("CPROMJENE"));
    if (!getDetailSet().getString("CORG2").equals(getMasterSet().getString("CORG2"))) {
      JOptionPane.showConfirmDialog(jpDetailOS,"Brisanje nije moguæe ! Promjenjena org. jedinica !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if(rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("O")) {
      JOptionPane.showConfirmDialog(jpDetailOS,"Brisanje nije moguæe ! Promjenjena org. jedinica !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
//      return true;
    }
    JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return false;
  }

  public boolean ValidacijaDetail(char mode) {
    if(!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(jpDetailOS, "Datum mora biti veæi ili jednak datumu na zadnjoj stavci", "Greška",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      jtfDatum.requestFocus();
      return false;
    }
    if (getDetailSet().getString("OLDCORG").equals(getDetailSet().getString("CORG2"))) {
      JOptionPane.showConfirmDialog(jpDetailOS, "Nije promjenjena organizacijska jedinica", "Greška",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      jrfCORG.requestFocus();
      return false;
    }
    return super.ValidacijaDetail(mode);
  }
  public void AfterDeleteDetail() {
    oldOSN=util.nul;
    oldISP=util.nul;
    String qStr ="";
    oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
    getDetailSet().deleteRow();
    getDetailSet().deleteRow();
    getDetailSet().saveChanges();
    hr.restart.os.osUtil.getUtil().afterDeleteOS(oldOSN, oldISP, 'O');
    raDetail.refreshTable();
    beforeShowDetail();
  }
}