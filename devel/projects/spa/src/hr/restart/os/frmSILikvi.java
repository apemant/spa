/****license*****************************************************************
**   file: frmSILikvi.java
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

public class frmSILikvi extends osTemplate{

  public frmSILikvi() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm.getOS_StSI().open();
    dm.getOS_SI().open();
    setMasterSet(dm.getOS_SI());
    setDetailSet(dm.getOS_StSI());
    pres.setSelDataSet(getMasterSet());
    pres.setSelPanel(jpSel);
    super.jbInitA();
    setJPanelMaster(jpMasterOS);
    setJPanelDetail(jpDetailOS);
    setNaslovMaster("Likvidacija");
  }

  public void AfterDeleteDetail() {
    String qStr ="";
    qStr = rdOSUtil.getUtil().deleteLikvidacija("os_SI", getDetailSet().getString("CORG"), getDetailSet().getString("INVBROJ"));
    getMasterSet().getDatabase().executeStatement(qStr);
    getMasterSet().setTimestamp("DATLIKVIDACIJE", nullDate);
    beforeShowDetail();
  }

  public void beforeShowDetail() {
    if (rdOSUtil.getUtil().checkLikvidacija(getMasterSet())) {
      raDetail.disableAdd();
    }
    else {
      raDetail.enableAdd();
    }
  }

  public void beforeShowMaster() {
    raMaster.disableAdd();
  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    rcc.setLabelLaF(jtfUIPRPOR, false);
    if (mode=='N') {
      if (!tds.isOpen()) tds.open();
      if (tds.getRowCount()==0) tds.insertRow(false);
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");
      tds.setBigDecimal("OSNOVICA", (getMasterSet().getBigDecimal("OSNDUGUJE").add(getMasterSet().getBigDecimal("OSNPOCETAK"))));
      tds.setBigDecimal("ISPRAVAK", (getMasterSet().getBigDecimal("ISPPOTRAZUJE").add(getMasterSet().getBigDecimal("ISPPOCETAK"))));
      jrfCPROMJENE.requestFocus();
      rcc.setLabelLaF(jtfOsnovica, false);
      rcc.setLabelLaF(jtfIspravak, false);
//      getDetailSet().setTimestamp("DATPROMJENE", hr.restart.util.Valid.getValid().findDate(false, 0));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());
    }
    else if (mode=='I') {
      if (amor.equals("D")) {
         rcc.EnabDisabAll(jpDetailOS, false);
         JOptionPane.showConfirmDialog(jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      }
      if (!rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L")) {
        rcc.EnabDisabAll(jpDetailOS, false);
        JOptionPane.showConfirmDialog(jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      }
      else {
        rcc.EnabDisabAll(jpDetailOS, true);
      }

      checkLikvidacija();
      jtfDatum.requestFocus();
    }
  }

  public void EntryPointDetail(char mode) {
    if (mode=='I') {
      rcc.EnabDisabAll(jpDetailOS, false);
    }
    else if (mode=='N') {
      if (!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
       JOptionPane.showConfirmDialog(jpDetailOS,"Inventarski broj veæ likvidiran !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
       return;
      }
    }
  }

  public boolean DeleteCheckDetail() {
    oldOSN = hr.restart.os.osUtil.getUtil().getOldSIOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldSIISP(mod);
    if (!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if (compare==0) {
         JOptionPane.showConfirmDialog(jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
      }
      else {
        return true;
      }
    }
    if (!rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L")) {
      JOptionPane.showConfirmDialog(jpDetailOS,"Brisanje nije moguæe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean ValidacijaDetail(char mode) {
    if (!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(jpDetailOS, "Datum mora biti veæi ili jednak datumu na zadnjoj stavci", "Greška", JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      jtfDatum.requestFocus();
      return false;
    }
    return super.ValidacijaDetail(mode);
  }
}