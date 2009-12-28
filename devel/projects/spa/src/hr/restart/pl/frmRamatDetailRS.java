/****license*****************************************************************
**   file: frmRamatDetailRS.java
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
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.text.JTextComponent;
//import com.borland.jbcl.layout.*;
//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//import hr.restart.swing.*;
//import hr.restart.baza.*;
//import hr.restart.util.*;


public class frmRamatDetailRS extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  private frmRS fRS;

  raNavAction toRemove0 = this.getNavBar().getNavContainer().getNavActions()[0];
  raNavAction toRemove1 = this.getNavBar().getNavContainer().getNavActions()[1];
  raNavAction toRemove2 = this.getNavBar().getNavContainer().getNavActions()[2];
  raNavAction toRemove3 = this.getNavBar().getNavContainer().getNavActions()[3];
  jpRamatDetailRS jpDetail;

  public frmRamatDetailRS(frmRS _fRS) {
    fRS = _fRS;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {}

  public void SetFokus(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCradnik, false);
      rcc.setLabelLaF(jpDetail.jlrIme, false);
      rcc.setLabelLaF(jpDetail.jlrPrezime, false);
      rcc.setLabelLaF(jpDetail.jbSelCradnik, false);
      jpDetail.jlrRadOdnos.requestFocus();
    } else if (mode == 'N') {
      jpDetail.jlrCradnik.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCradnik)) return false;
    if (vl.isEmpty(jpDetail.jlrRadOdnos)) return false;
//    if (vl.isEmpty(jpDetail.jlrInvalidnost)) return false;
//    if (vl.isEmpty(jpDetail.jlrStaz)) return false;

    if (mode == 'N'){
      setAllTogether();
    }
//    if (!frmRSPeriod.chkDan(fRS.getTweekSet().getShort("ODDANA"), fRS.getTweekSet().getShort("DODANA"),
//                            frmRSPeriod.getZadnjiDan(fRS.getTweekSet().getShort("GODINA"),fRS.getTweekSet().getShort("MJESEC")))){
//      jpDetail.jraOD.requestFocus();
//      javax.swing.JOptionPane.showMessageDialog(this.getRaDetailPanel(),"Od - Do period nije ispravan","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
//      return false;
//    }
    return true;

  }

  public void AfterSave(char mode){
    fRS.okPress();
    fRS.detailRS.refresh();
    fRS.setKveri();
    jpDetail.clearFields();
  }

  private void jbInit() throws Exception {

    System.out.println("Andrej, za sada nije implementiran nikakav racun");

    if (fRS.isArh){
      this.getNavBar().getNavContainer().remove(toRemove0);
      this.getNavBar().getNavContainer().remove(toRemove1);
      this.getNavBar().getNavContainer().remove(toRemove2);
      this.getNavBar().getNavContainer().remove(toRemove3);
    } else {
//      this.getNavBar().getNavContainer().remove(toRemove0);
//      this.getNavBar().getNavContainer().remove(toRemove2);
      this.getNavBar().getNavContainer().remove(toRemove3);
      addOption(new raNavAction("Detaljnija izmjena",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F6) {
        public void actionPerformed(ActionEvent e) {
          frmTableDataView dw = new frmTableDataView(true, true, false) {
            public void hide() {
              super.hide();
              frmRamatDetailRS.this.setEnabled(true);
              frmRamatDetailRS.this.AfterSave('I');
            }
          };
          dw.setDataSet(fRS.getTweekSet());
          frmRamatDetailRS.this.setEnabled(false);
          dw.show();
        }
      },0);
    }
    this.setRaQueryDataSet(fRS.getTweekSet());
    this.getJpTableView().setKumTak(true);
    this.getJpTableView().setStoZbrojiti(frmRS.sumcols);
    this.getJpTableView().getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    this.getJpTableView().addTableModifier(new raTableColumnModifier("CRADNIK",
        new String[] {"CRADNIK","PREZIME","IME"},
        dm.getAllRadnici()));
    jpDetail = new jpRamatDetailRS(this);
    this.setRaDetailPanel(jpDetail);
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(fRS.getHead());
  }

  public void Funkcija_ispisa(){
    // e jesan pametan ;)
    fRS.detailRS.refresh();
    fRS.getOKPanel().jBOK_actionPerformed();
  }

  public void rnvExit_action(){
    super.rnvExit_action();
    rcc.setLabelLaF(fRS, true);
    fRS.afterOKPress();
  }

  public void rnvDelete_action(){
    super.rnvDelete_action();
    fRS.okPress();
    fRS.detailRS.refresh();
  }

  private void setAllTogether(){
    com.borland.dx.sql.dataset.QueryDataSet maxRbr = Util.getUtil().getNewQueryDataSet("SELECT max(rbr)+1 as rbr  FROM RSPeriodobr where cradnik ='" + jpDetail.jlrCradnik.getText().trim() + "'");
    int rbr = maxRbr.getInt("RBR");
    this.getRaQueryDataSet().setInt("RBR",rbr);
    this.getRaQueryDataSet().setShort("MJESEC",fRS.getHead().getShort("MJESEC"));
    this.getRaQueryDataSet().setShort("GODINA",fRS.getHead().getShort("GODINA"));
    lookupData.getlookupData().raLocate(dm.getAllRadnicipl(),"CRADNIK",jpDetail.jlrCradnik.getText().trim());
    this.getRaQueryDataSet().setString("COPCINE",dm.getAllRadnicipl().getString("COPCINE"));
    this.getRaQueryDataSet().setString("JMBG",dm.getAllRadnicipl().getString("JMBG"));
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(this.getRaQueryDataSet());
  }
}
