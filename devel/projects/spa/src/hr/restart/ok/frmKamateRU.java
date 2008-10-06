/****license*****************************************************************
**   file: frmKamateRU.java
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

import hr.restart.baza.dM;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.sql.Timestamp;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


public class frmKamateRU extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  presKamateRU pres;
  jpKamateRUMaster jpMaster;
  jpKamateRUDetail jpDetail;

  static frmKamateRU frm;

  boolean inedit,kupac;
  int cpar;
  String nazpar;
  Timestamp from, to;

  public frmKamateRU() {
    super(2,2);
    try {
      frm = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmKamateRU getInstance() {
    return frm;
  }

  private void setTitleMaster() {
    VarStr title = new VarStr("Ra\u010Duni");
    if (cpar != 0) {
      title.append(kupac ? " kupca " : " dobavlja\u010Da ").append(cpar);
      title.append(' ').append(nazpar);
    } else title.append(kupac ? " kupaca" : " dobavlja\u010Da");
    title.append("  od ").append(rdu.dataFormatter(from));
    title.append(" do ").append(rdu.dataFormatter(to));
    this.setNaslovMaster(title.toString());
  }

  public void beforeShowMaster() {
    from = pres.getSelRow().getTimestamp("DATDOK-from");
    to = pres.getSelRow().getTimestamp("DATDOK-to");
    cpar = pres.getSelRow().getInt("CPAR");
    nazpar = pres.jlrNazpar.getText();
    kupac = pres.rcbCpar.getSelectedIndex() == 0;
//    if (kupac) jpMaster.jlrCpar.setRaDataSet(dm.getPartneriKup());
//    else jpMaster.jlrCpar.setRaDataSet(dm.getPartneriDob());
    jpMaster.jlCpar.setText(kupac ? "Kupac" : "Dobavlja\u010D");
    setTitleMaster();
  }

  public void getDosp() {
    if (inedit && ld.raLocate(dm.getPartneri(), "CPAR", jpMaster.jlrCpar.getText())) {
      int dosp = (int) dm.getPartneri().getShort("DOSP");
      getMasterSet().setTimestamp("DATDOSP", Util.getUtil().addDays(
          getMasterSet().getTimestamp("DATDOK"), dosp));
    }
  }

  public void afterSetModeMaster(char oldm, char newm) {
    inedit = (newm != 'B');
    if (!inedit && jpMaster.jlrCpar.getText().length() == 0)
      jpMaster.jlrNazpar.setText("");
  }

  public void EntryPointMaster(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jlrCpar, false);
      rcc.setLabelLaF(jpMaster.jlrNazpar, false);
      rcc.setLabelLaF(jpMaster.jbSelCpar, false);
      rcc.setLabelLaF(jpMaster.jraBrojdok, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'I') jpMaster.jraIznos.requestFocus();
    else if (mode == 'N') {
      pres.copySelValues();
      getMasterSet().setTimestamp("DATDOK", vl.getToday());
      if (cpar == 0) {
        jpMaster.jlrCpar.forceFocLost();
        jpMaster.jlrCpar.requestFocus();
      } else {
        jpMaster.jlrNazpar.setText(nazpar);
//        getRaQueryDataSet().setInt("CPAR", cpar);
//        jpDetail.jlrCpar.forceFocLost();
        rcc.setLabelLaF(jpMaster.jlrCpar, false);
        rcc.setLabelLaF(jpMaster.jlrNazpar, false);
        rcc.setLabelLaF(jpMaster.jbSelCpar, false);
        inedit = true;
        getDosp();
        jpMaster.jraBrojdok.requestFocus();
      }
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jlrCpar) || vl.isEmpty(jpMaster.jraBrojdok))
      return false;
    if (!pres.validateEntry()) {
      jpMaster.jraDatdok.requestFocus();
      JOptionPane.showMessageDialog(raMaster.getWindow(),
        "Uneseni podaci ne odgovaraju zadanim kriterijima u predselekciji",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N' && vl.notUnique(new JTextComponent[]
        {pres.jraKnjig, jpMaster.jlrCpar, jpMaster.jraBrojdok}))
      return false;
    if (getMasterSet().getBigDecimal("IZNOS").signum() <= 0) {
      jpMaster.jraIznos.requestFocus();
      JOptionPane.showMessageDialog(raMaster.getWindow(),
        "Pogrešan iznos ra\u010Duna!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    getMasterSet().setString("CRACUNA", getMasterSet().getString("KNJIG")+
                             '-'+getMasterSet().getInt("CPAR")+'-'+
                             getMasterSet().getString("BROJDOK"));
    return true;
  }

  public void setTitleDetail() {
    VarStr title = new VarStr("Uplate ra\u010Duna ");
    title.append(getMasterSet().getString("BROJDOK")).append(' ');
    title.append(kupac ? " kupca " : " dobavlja\u010Da ");
    title.append(getMasterSet().getInt("CPAR"));
    title.append(' ').append(jpMaster.jlrNazpar.getText());
    this.setNaslovDetail(title.toString());
  }

  public void AfterAfterSaveMaster(char mode) {
    if (raMaster.getMode()=='N' ) {
      raMaster.Insertiraj();
      raMaster.SetFokus(mode);
    }
  }

  public void refilterDetailSet() {
//    System.out.println(getMasterSet());
//    System.out.println(cpar);
//    System.out.println(nazpar);
    super.refilterDetailSet();
    setTitleDetail();
  }

  public void EntryPointDetail(char mode) {
    rcc.setLabelLaF(jpDetail.jlrCpar, false);
    rcc.setLabelLaF(jpDetail.jlrNazpar, false);
    rcc.setLabelLaF(jpDetail.jbSelCpar, false);
    rcc.setLabelLaF(jpDetail.jraBrojdok, false);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jlrNazpar.setText(jpMaster.jlrNazpar.getText());
      jpDetail.jraDatdok.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraIznos.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (getDetailSet().getBigDecimal("IZNOS").signum() <= 0) {
      jpMaster.jraIznos.requestFocus();
      JOptionPane.showMessageDialog(raDetail.getWindow(),
        "Pogrešan iznos ra\u010Duna!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N') {
      vl.execSQL("SELECT MAX(rbr) AS num FROM kamupl WHERE knjig='"+
                 getDetailSet().getString("KNJIG")+"' AND brojdok='"+
                 getDetailSet().getString("BROJDOK")+"' AND cpar="+
                 getDetailSet().getInt("CPAR"));
      vl.RezSet.open();
      if (vl.RezSet.rowCount() > 0)
        getDetailSet().setInt("RBR", vl.RezSet.getInt("NUM") + 1);
      else getDetailSet().setInt("RBR", 1);
    }
    getDetailSet().setString("CRACUNA", getMasterSet().getString("CRACUNA"));
    return true;
  }

  int oldRbr;
  public boolean DeleteCheckDetail() {
    oldRbr = getDetailSet().getInt("RBR");
    return true;
  }

  public boolean doWithSaveDetail(char mode) {
    try {
      if (mode == 'B') {
        vl.recountDataSet(raDetail, "RBR", oldRbr, false);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getKamRac());
    this.setNaslovMaster("KamRac"); /**@todo: Naslov mastera */
    this.setVisibleColsMaster(new int[] {2,5,6}); /**@todo: Odrediti vidljive kolone mastera */
    this.setMasterKey(new String[] {"KNJIG", "CPAR", "BROJDOK"});
    jpMaster = new jpKamateRUMaster(this);
    this.setJPanelMaster(jpMaster);

    raMaster.getJpTableView().addTableModifier(new raTableColumnModifier("CPAR",
        new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));

    pres = new presKamateRU();
    pres.setOwner(this);
    this.setPreSelect(pres, "Ra\u010Duni kupaca ili dobavlja\u010Da", false);
    this.initPreSelect();

//    ((JPanel) raMaster.getContentPane()).setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
//    RepaintManager.currentManager((JPanel) raMaster.getContentPane()).setDoubleBufferingEnabled(false);

    this.setDetailSet(dm.getKamUpl());
    this.setNaslovDetail("Stavke KamUpl"); /**@todo: Naslov detaila */
    this.setVisibleColsDetail(new int[] {3, 4, 5});
    this.setDetailKey(new String[] {"KNJIG", "CPAR", "BROJDOK", "RBR"});
    jpDetail = new jpKamateRUDetail(this);
    this.setJPanelDetail(jpDetail);
  }
}
