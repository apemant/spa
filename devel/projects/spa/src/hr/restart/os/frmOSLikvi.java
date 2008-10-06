/****license*****************************************************************
**   file: frmOSLikvi.java
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

import java.math.BigDecimal;

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmOSLikvi extends osTemplate {
  boolean lLikvidacija;
  public frmOSLikvi() {
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
    this.setNaslovMaster("Likvidacija osnovnog sredstva");
    //super.BindComp();
  }

  private boolean isRemovedMaster = false;
  private boolean isRemovedDetail = false;

  public void beforeShowDetail() {
    if(rdOSUtil.getUtil().checkLikvidacija(getMasterSet()) || !getMasterSet().getString("STATUS").equals("A") && !samoPregled) {
      raDetail.disableAdd();
    } else if (!samoPregled) {
      raDetail.enableAdd();
    }
    if (!isRemovedDetail && samoPregled){
      System.out.println("ditelj disejblam nju i edit :)");
      raDetail.setEnabledNavAction(raDetail.getNavBar().getStandardOption(0),false);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getStandardOption(1),false);
      raDetail.setEnabledNavAction(raDetail.getNavBar().getStandardOption(2),false);
      raDetail.removeRnvCopyCurr();
      isRemovedDetail = true;
    }
  }

  public void beforeShowMaster() {
    if (!isRemovedMaster && samoPregled){
      System.out.println("master disejblam nju i edit :)");
      this.raMaster.setEnabledNavAction(raMaster.getNavBar().getStandardOption(0),false);
      this.raMaster.setEnabledNavAction(raMaster.getNavBar().getStandardOption(1),false);
      this.raMaster.setEnabledNavAction(raMaster.getNavBar().getStandardOption(2),false);
      raMaster.removeRnvCopyCurr();
      isRemovedMaster = true;
    } else if (!samoPregled)
      this.raMaster.disableAdd();
  }

  public void AfterDeleteDetail() {
    hr.restart.os.osUtil.getUtil().afterDeleteOS(oldOSN, oldISP, 'L');
    hr.restart.os.osUtil.getUtil().deleteObrada4(getMasterSet());
    oldOSN=util.nul;
    oldISP=util.nul;
    String qStr ="";
    qStr = rdOSUtil.getUtil().deleteLikvidacija("os_sredstvo", getDetailSet().getString("CORG"), getDetailSet().getString("INVBROJ"));
    getMasterSet().getDatabase().executeStatement(qStr);
    getMasterSet().setTimestamp("DATLIKVIDACIJE", nullDate);
    if (lLikvidacija) {     // Dodano zbog brisanje likvidacije
      lLikvidacija=false;
      hr.restart.os.osUtil.getUtil().afterDeleteOS(/*getDetailSet().getBigDecimal("OSNDUGUJE")*/util.nul, util.nul/*getDetailSet().getBigDecimal("ISPPOTRAZUJE")*/, 'N');
      getDetailSet().deleteRow();
      getDetailSet().saveChanges();
      this.raDetail.refreshTable();
    }
    beforeShowDetail();
    raMaster.refreshTable();
  }

  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    rcc.setLabelLaF(jtfUIPRPOR, false);
    if(mode=='N') {
      if(!tds.isOpen())
        tds.open();

      if(tds.getRowCount()==0) {
        tds.insertRow(false);
      }
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");

      tds.setBigDecimal("OSNOVICA", (getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK"))));
      tds.setBigDecimal("ISPRAVAK", (getMasterSet().getBigDecimal("ISPPOTRAZUJE").add(getMasterSet().getBigDecimal("ISPPOCETAK"))));

      jrfCPROMJENE.requestFocus();

//      getDetailSet().setTimestamp("DATPROMJENE", hr.restart.util.Valid.getValid().findDate(false, 0));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());
//      rcc.setLabelLaF(jtfDatum, false);
      rcc.setLabelLaF(jrfCPAR, false);
      rcc.setLabelLaF(jrfNAZPAR, false);
      rcc.setLabelLaF(jbCPAR, false);
      rcc.setLabelLaF(jtfOsnovica, false);
      rcc.setLabelLaF(jtfIspravak, false);
      rcc.setLabelLaF(jtfUIPRPOR, false);

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

  public void EntryPointDetail(char mode) {
    if (!getMasterSet().getString("STATUS").equals("A")) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Osnovno sredstvo nije aktivno !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (mode=='I') {
      rcc.EnabDisabAll(jpDetailOS, false);
    }
    else if (mode=='N') {
      if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
       JOptionPane.showConfirmDialog(this.jpDetailOS,"Inventarski broj veæ likvidiran !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
       return;
      }
    }
    if (!getMasterSet().getString("STATUS").equals("A")) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Likvidacija nije moguæa ! Sredstvo nije aktivno !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
  }

  public boolean DeleteCheckDetail() {
    if (getDetailSet().getBigDecimal("OSNPOTRAZUJE").doubleValue()>0) {
      lLikvidacija=true;
    }
    System.out.println("deletecheckDetail-sa frmOSLikvi");
    oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if(compare==0) {
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
      }
      else {
        return true;
      }
    }

    if(!rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L")) {
      System.out.println("greška: "+rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")));
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Pogrešan tip promjene.","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean ValidacijaDetail(char mode) {
    if(!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Datum mora biti veæi ili jednak datumu na zadnjoj stavci",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }
    return super.ValidacijaDetail(mode);
  }

//  public void prepareAmorStavka()
//  {
//    dm.getOS_Promjene().insertRow(false);
//    dm.getOS_Promjene().setString("CORG", getMasterSet().getString("CORG"));
//    dm.getOS_Promjene().setString("INVBROJ", getMasterSet().getString("INVBROJ"));
//    dm.getOS_Promjene().setString("CPROMJENE", osUtil.getUtil().getSifraObrAmor());
//
//
//  }
}