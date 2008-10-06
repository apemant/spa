/****license*****************************************************************
**   file: frmKamateUpl.java
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
package hr.restart.ok;

import hr.restart.baza.KamRac;
import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;

import javax.swing.JOptionPane;


public class frmKamateUpl extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  jpKamateUpl jpDetail;
  presKamate pres;

  static frmKamateUpl frm;

  boolean kupac, inedit;
  int cpar;
  String nazpar;
  Timestamp from, to;

  public frmKamateUpl() {
    super(2);
    try {
      frm = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmKamateUpl getInstance() {
    return frm;
  }

  public void setTitle() {
    VarStr title = new VarStr("Uplate");
    if (cpar != 0) {
      title.append(kupac ? " kupca " : " dobavlja\u010Du ").append(cpar);
      title.append(' ').append(nazpar);
    } else title.append(kupac ? " kupaca" : " dobavlja\u010Dima");
    title.append("  od ").append(rdu.dataFormatter(from));
    title.append(" do ").append(rdu.dataFormatter(to));
    this.setTitle(title.toString());
  }

  public void beforeShow() {
    from = pres.getSelRow().getTimestamp("DATDOK-from");
    to = pres.getSelRow().getTimestamp("DATDOK-to");
    cpar = pres.getSelRow().getInt("CPAR");
    nazpar = pres.jlrNazpar.getText();
    kupac = pres.rcbCpar.getSelectedIndex() == 0;
//    if (kupac) jpMaster.jlrCpar.setRaDataSet(dm.getPartneriKup());
//    else jpMaster.jlrCpar.setRaDataSet(dm.getPartneriDob());
    jpDetail.jlCpar.setText(kupac ? "Kupac" : "Dobavlja\u010D");
    setTitle();
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCpar, false);
      rcc.setLabelLaF(jpDetail.jlrNazpar, false);
      rcc.setLabelLaF(jpDetail.jbSelCpar, false);
      rcc.setLabelLaF(jpDetail.jlrBrojdok, false);
      rcc.setLabelLaF(jpDetail.jbSelBr, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'I') jpDetail.jraIznos.requestFocus();
    else if (mode == 'N') {
      pres.copySelValues();
      partnerChanged();
      getRaQueryDataSet().setTimestamp("DATDOK", vl.getToday());
      if (cpar == 0) {
        jpDetail.jlrCpar.forceFocLost();
        jpDetail.jlrCpar.requestFocus();
      } else {
        jpDetail.jlrNazpar.setText(nazpar);
//        getRaQueryDataSet().setInt("CPAR", cpar);
//        jpDetail.jlrCpar.forceFocLost();
        rcc.setLabelLaF(jpDetail.jlrCpar, false);
        rcc.setLabelLaF(jpDetail.jlrNazpar, false);
        rcc.setLabelLaF(jpDetail.jbSelCpar, false);
        jpDetail.jlrBrojdok.requestFocus();
      }
    }
  }

  public void afterSetMode(char oldm, char newm) {
    inedit = newm == 'N';
  }

  public void partnerChanged() {
    if (!inedit) return;
    jpDetail.jlrBrojdok.setRaDataSet(KamRac.getDataModule().getTempSet(
        "knjig='"+getRaQueryDataSet().getString("KNJIG")+
        "' AND cpar="+getRaQueryDataSet().getInt("CPAR")+" AND dugpot="+
        (kupac ? "'D'" : "'P'")
    ));
    jpDetail.jlrBrojdok.getRaDataSet().open();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrBrojdok)) return false;
    if (getRaQueryDataSet().getBigDecimal("IZNOS").signum() <= 0) {
      jpDetail.jraIznos.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(),
        "Pogrešan iznos uplate!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N') {
      vl.execSQL("SELECT MAX(rbr) AS num FROM kamupl WHERE knjig='"+
         getRaQueryDataSet().getString("KNJIG")+"' AND brojdok='"+
         getRaQueryDataSet().getString("BROJDOK")+"' AND cpar="+
         getRaQueryDataSet().getInt("CPAR"));
      vl.RezSet.open();
      if (vl.RezSet.rowCount() > 0)
        getRaQueryDataSet().setInt("RBR", vl.RezSet.getInt("NUM") + 1);
      else getRaQueryDataSet().setInt("RBR", 1);
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getKamUpl());
    this.setVisibleCols(new int[] {2, 4, 5});
    jpDetail = new jpKamateUpl(this);
    this.setRaDetailPanel(jpDetail);
    this.jpDetail.BindComponents(dm.getKamUpl());

    pres = new presKamate();
    pres.setOwner(this);

    this.addOption(new raNavAction("Predselekcija",raImages.IMGZOOM,KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        pres.showPreselect(frmKamateUpl.this, "Uplate kupca ili dobavlja\u010Du");
      }
    }, 4, false);
  }
}
