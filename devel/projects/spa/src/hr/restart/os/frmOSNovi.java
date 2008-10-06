/****license*****************************************************************
**   file: frmOSNovi.java
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

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmOSNovi extends osTemplate{

  boolean f7mode=false;
  public frmOSNovi() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
//    pres.setSelDataSet(dm.getOS_Sredstvo());
    dm.getOS_Sredstvo().open();
    dm.getOS_Promjene().open();
    this.setMasterSet(dm.getOS_Sredstvo());
    this.setDetailSet(dm.getOS_Promjene());
    pres.setSelDataSet(this.getMasterSet());
    pres.setSelPanel(this.jpSel);
    super.jbInitA();
    setJPanelMaster(jpMasterOS);
    setJPanelDetail(jpDetailOS);
    this.setNaslovMaster("Sredstva iz tekuæe godine");
    raMaster.getJpTableView().getNavBar().addOption(new raNavAction("Aktivnost", raImages.IMGEXPORT, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        changeStatus();
      }
    }, 4);
    //super.BindComp();
  }

  public void beforeShowDetail() {
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate)) {
      raDetail.disableAdd();
    } else {
      raDetail.enableAdd();
    }
    super.beforeShowDetail();
  }

  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    if (mode=='N') {
      if (!prom.equals("D")) {
        rcc.EnabDisabAll(jpDetailOS, false);
        JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæ unos u tekuæu godinu u ovom dijelu programa !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      }
      if(!tds.isOpen()) tds.open();
      if(tds.getRowCount()==0) tds.insertRow(false);
      jrfNAZPROMJENE.setText("");
      jtfDokument.setText("");
//      this.getDetailSet().setTimestamp("DATPROMJENE", hr.restart.util.Valid.getValid().findDate(false, 0));
      getDetailSet().setTimestamp("DATPROMJENE", osUtil.getUtil().findOSDate());

      jrfCPROMJENE.requestFocus();
    }
    else if (mode=='I') {
      if(amor.equals("D")) {
//         rcc.EnabDisabAll(jpDetailOS, false);
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         this.raDetail.getOKpanel().jPrekid_actionPerformed();
      }
      if(!checkInsertedDate('N')) {
//        rcc.EnabDisabAll(this.jpDetailOS, false);
        JOptionPane.showConfirmDialog(this.jpDetailOS,"Nije moguæa promjena !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        this.raDetail.getOKpanel().jPrekid_actionPerformed();
      }
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
//    boolean STPostoji = false;
    getDetailSet().setString("CORG", getMasterSet().getString("CORG"));
//    hr.restart.robno.rdUtil.getUtil().printDSColumnNames(getDetailSet());

    if(!checkInsertedDate('N')) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Pogrešna godina !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }
    if(!kontrolaDatUnosa()) {
      JOptionPane.showConfirmDialog(this.jpDetailOS,"Datum mora biti veæi ili jednak datumu na zadnjoj stavci",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.jtfDatum.requestFocus();
      return false;
    }

/*    getDetailSet().first();
    do
    {
      if(getDetailSet().getTimestamp("DATPROMJENE").after(hr.restart.robno.Util.getUtil().findFirstDayOfYear()))
        STPostoji = true;
      getDetailSet().next();
    }
    while(getDetailSet().inBounds());

    if(!STPostoji)
    {
      int compare = tds.getBigDecimal("OSNOVICA").compareTo(tds.getBigDecimal("ISPRAVAK"));
      if (compare < 0)
      {
        JOptionPane.showConfirmDialog(this.jpDetailOS,"Osnovica mora biti veæa od ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfOsnovica.requestFocus();
        return false;
      }
    }*/
    return super.ValidacijaDetail(mode);
  }

  public boolean DeleteCheckDetail()
  {

    oldOSN = hr.restart.os.osUtil.getUtil().getOldOSN(mod);
    oldISP = hr.restart.os.osUtil.getUtil().getOldISP(mod);
    if(!getMasterSet().getTimestamp("DATLIKVIDACIJE").equals(nullDate))
    {

//      int compare = getDetailSet().getBigDecimal("OSNPOTRAZUJE").compareTo(new BigDecimal(0));
//      if(compare==0)
//      {
         JOptionPane.showConfirmDialog(this.jpDetailOS,"Brisanje nije moguæe ! Sredstvo likvidirano !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
         return false;
//      }
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
    this.refilterDetailSet();
    if (getMasterSet().getString("AKTIV").equals("N")) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo je neaktivno !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (getMasterSet().getString("STARISTATUS").equals("")) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo nije bilo u pripremi !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (getDetailSet().getRowCount()!=1) {
      JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo ima upisane promjene !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (JOptionPane.showConfirmDialog(this.jpMasterOS,"Da li \u017Eelite sredstvo vratiti u pripremu ?","Vraæanje u pripremu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      f7mode=true;
      vl.runSQL(sjQuerys.setAktivToPriprema(getMasterSet().getString("CORG"), getMasterSet().getString("INVBROJ")));
      this.raMaster.rnvDelete_action();
      getMasterSet().refresh();
//      rdOSUtil.getUtil().deleteDetailSet(getMasterSet(), 0);
    }
//    osMain.getStartFrame().showFrame("hr.restart.os.frmPripremaToAktiv", "Priprema u aktiv");
  }
  public boolean DeleteCheckMaster() {
    if (f7mode==false) {
      if (!getMasterSet().getString("STARISTATUS").equals("")) {
        JOptionPane.showConfirmDialog(this.jpMasterOS,"Sredstvo je upisano kroz pripremu, nije dozvoljeno brisanje !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return super.DeleteCheckMaster();
  }
  public boolean PorukaDeleteMaster() {
    if (f7mode==true) {
      return true;
    }
    return super.PorukaDeleteMaster();
  }
  public void AfterDeleteMaster(){
    f7mode=false;
    super.AfterDeleteMaster();
  }
}
