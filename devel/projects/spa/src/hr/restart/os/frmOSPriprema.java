/****license*****************************************************************
**   file: frmOSPriprema.java
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

import hr.restart.util.raImages;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmOSPriprema extends osTemplate {

  public frmOSPriprema() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception
  {
//    pres.setSelDataSet(dm.getOS_Sredstvo());
    dm.getOS_Sredstvo().open();
    dm.getOS_Promjene().open();
    this.setVisibleColsMaster(new int[] {3,4,2,32,29,0,30});
    this.setMasterSet(dm.getOS_Sredstvo());
    this.setDetailSet(dm.getOS_Promjene());
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(this.jpSel);
    super.jbInitA();
    setJPanelMaster(jpMasterOS);
    setJPanelDetail(jpDetailOS);
    this.setNaslovMaster("Sredstva u pripremi");
    raMaster.getJpTableView().getNavBar().addOption(new raNavAction("Aktivnost", raImages.IMGEXPORT, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        changeStatus();
      }
    }, 4);


    //super.BindComp();
  }

  public void beforeShowDetail()
  {
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate))
    {
      raDetail.disableAdd();
    }
    else
    {
      raDetail.enableAdd();
    }
    super.beforeShowDetail();
  }

  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    if (mode=='N') {
      if(!tds.isOpen()) tds.open();
      if(tds.getRowCount()==0) tds.insertRow(false);
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");
//      this.getDetailSet().setTimestamp("DATPROMJENE", hr.restart.util.Valid.getValid().findDate(false, 0));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());
      jrfCPROMJENE.requestFocus();
    }
    else if (mode=='I') {
      jtfDatum.requestFocus();
    }
  }

  public void EntryPointDetail(char mode) {
    if (mode=='I') {
      if( !getMasterSet().getTimestamp("DATLIKVIDACIJE").toString().equals(nullDate.toString())) {
        rcc.EnabDisabAll(jpDetailOS, false);
      }
      else {
        rcc.EnabDisabAll(jpDetailOS, true);
      }
    }
  }

  public boolean ValidacijaDetail(char mode) {
    boolean STPostoji = false;
    getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
    if(!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Datum mora biti veæi ili jednak datumu na zadnjoj stavci",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }

/*    getDetailSet().first();
    do {
      if(getDetailSet().getTimestamp("DATPROMJENE").after(hr.restart.robno.Util.getUtil().findFirstDayOfYear()))
        STPostoji = true;
      getDetailSet().next();
    }
    while (getDetailSet().inBounds());

    if(!STPostoji) {
      int compare = tds.getBigDecimal("OSNOVICA").compareTo(tds.getBigDecimal("ISPRAVAK"));
      if (compare < 0) {
        JOptionPane.showConfirmDialog(this.jpDetailOS,"Osnovica mora biti veæa od ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfOsnovica.requestFocus();
        return false;
      }
    }*/
    return super.ValidacijaDetail(mode);
  }

  public boolean DeleteCheckDetail() {
    oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {

      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
      if(compare==0) {
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
      }
    }

    if(rdOSUtil.getUtil().getTipPromjene(getDetailSet().getString("CPROMJENE")).equals("L")) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    int validYear = findYear(hr.restart.util.Valid.getValid().findDate(false, 0),1);
    int datasetYear = findYear(getDetailSet().getTimestamp("DATPROMJENE"),1);
//    if(datasetYear == validYear)
//      return true;
//    else
//    {
//      JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      return false;
//    }
    return super.DeleteCheckDetail();
  }
  private void changeStatus() {
    if (getMasterSet().getString("AKTIV").equals("N")) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo je neaktivno !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (dm.getOS_Kontrola().getString("PROM").equals("N")) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Pogrešan mod rada !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    osMain.getStartFrame().showFrame("hr.restart.os.frmPripremaToAktiv", "Iz pripreme u upotrebu");
  }

}