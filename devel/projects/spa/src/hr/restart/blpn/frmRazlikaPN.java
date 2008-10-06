/****license*****************************************************************
**   file: frmRazlikaPN.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raPreSelectAware;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmRazlikaPN extends raMatPodaci implements raPreSelectAware {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet repQDSall = new QueryDataSet();
  Util ut = Util.getUtil();
  sgStuff ss = sgStuff.getStugg();
  PreSelect psel;
  static StorageDataSet razlika = new StorageDataSet();
  String updateStavPN;

  jpRazlikaPN jpDetail;

  raNavAction rnvPreselectBotun = new raNavAction("Predselekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      getPreSelect().showPreselect("Broj putnog naloga");
      setTitle("Uplata - isplata razlike po putnom nalogu broj "+getPreSelect().getSelRow().getString("CPN"));
      getJpTableView().fireTableDataChanged();
    }};

  public frmRazlikaPN() {
    try {
      frpn = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setPreSelect(PreSelect prs){
    psel = prs;
  }

  public PreSelect getPreSelect(){
    return psel;
  }

  static frmRazlikaPN frpn;

  public static frmRazlikaPN getFRPN(){
    return frpn;
  }

  private String currCpn = "";

  public void beforeShow(){
    startFrame.getStartFrame().centerFrame(this, 0 ,"");
    setTitle("Uplata - isplata razlike po putnom nalogu broj "+ getPreSelect().getSelRow().getString("CPN"));
    if (!getPreSelect().getSelRow().getString("CPN").equals(currCpn)){
      currCpn = getPreSelect().getSelRow().getString("CPN");
      razlika = (StorageDataSet)ss.getRazlika(currCpn);
      if (ss.getNalogIsplacen(currCpn)) {
        this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[0],false);
        JOptionPane.showMessageDialog(this.getWindow(),
                                      new hr.restart.swing.raMultiLineMessage(new String[] {"Putni nalog broj \"" + currCpn + "\" je ispla\u0107en",
                                                                                            "Mogu\u0107i su samo pregled i ispis"}),
                                      "Obavijest", JOptionPane.INFORMATION_MESSAGE);
      } else {
        this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[0],true);
      }
    }
  }

  public void Funkcija_ispisa(){
  	Aus.refilter(repQDS, ss.getRepIsplatnicaZaAkontacijuRazliku(this.getRaQueryDataSet()));
  	Aus.refilter(repQDSall, ss.getRepSveIsplatniceZaAkontacijuRazliku(this.getRaQueryDataSet())); 
//    repQDSall = ut.getNewQueryDataSet(ss.getRepSveIsplatniceZaAkontacijuRazliku(this.getRaQueryDataSet()));
    super.Funkcija_ispisa();
  }

  public void EntryPoint(char mode){
    rcc.setLabelLaF(jpDetail.jraIzdatak, false);
    rcc.setLabelLaF(jpDetail.jraPrimitak, false);
    rcc.setLabelLaF(jpDetail.jraCpn, false);
    if (mode == 'N') {
      getPreSelect().copySelValues();
      this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      this.getRaQueryDataSet().setTimestamp("DATUM", ut.clearTime(vl.getToday()));
      this.getRaQueryDataSet().setString("VRDOK", "BL");
//      this.getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(vl.getToday())));
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      getPreSelect().copySelValues();
      this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      this.getRaQueryDataSet().setTimestamp("DATUM", ut.clearTime(vl.getToday()));
      this.getRaQueryDataSet().setString("VRDOK", "BL");
//      this.getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(vl.getToday())));
      jpDetail.jlrCblag.emptyTextFields();
      jpDetail.jraDatum.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (this.getRaQueryDataSet().getBigDecimal("PRIMITAK").compareTo(new BigDecimal(0)) == 0 &&
        this.getRaQueryDataSet().getBigDecimal("IZDATAK").compareTo(new BigDecimal(0)) == 0) return false;
    
    getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(getRaQueryDataSet().getTimestamp("DATUM"))));

    int brizv = ss.getBrizv(this.getRaQueryDataSet().getInt("CBLAG"), this.getRaQueryDataSet().getString("KNJIG"),
                    this.getRaQueryDataSet().getString("OZNVAL"), this.getRaQueryDataSet().getShort("GODINA"));
    if (brizv == 0) {
      jpDetail.jlrCblag.requestFocus();
      JOptionPane.showConfirmDialog(this.jpDetail, "Nema otvorenog izvješ\u0107a za blagajnu \"".concat(this.jpDetail.jlrNaziv.getText()).concat("\"") , "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (!this.getRaQueryDataSet().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      this.getRaQueryDataSet().setBigDecimal("TECAJ", ss.getTECAJ(this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM")));
      BigDecimal pvizd = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("IZDATAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", pvizd);
      BigDecimal pvpri = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("PRIMITAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      this.getRaQueryDataSet().setBigDecimal("PVPRIMITAK", pvpri);
    } else {
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", this.getRaQueryDataSet().getBigDecimal("IZDATAK"));
      this.getRaQueryDataSet().setBigDecimal("PVPRIMITAK", this.getRaQueryDataSet().getBigDecimal("PRIMITAK"));
    }
    this.getRaQueryDataSet().setString("TKO", ss.getIme(this.getRaQueryDataSet().getString("CRADNIK")));
    String opis = "Razlika po PN ".concat(this.getRaQueryDataSet().getString("CPN")/*).concat(" u ").concat(this.getRaQueryDataSet().getString("OZNVAL")*/);
    this.getRaQueryDataSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    this.getRaQueryDataSet().setString("OPIS", opis);
    this.getRaQueryDataSet().setInt("BRIZV", brizv);
    String vrsta = "";
    if (this.getRaQueryDataSet().getBigDecimal("PRIMITAK").compareTo(Aus.zero2) != 0) vrsta = "U";
    else vrsta = "I";
    System.out.println("vrsta - " + vrsta);
    this.getRaQueryDataSet().setString("VRSTA", vrsta);
    this.getRaQueryDataSet().setInt("RBS", ss.getNextRBSstavblag(this.getRaQueryDataSet().getString("KNJIG"),
                                    this.getRaQueryDataSet().getInt("CBLAG"),
                                    this.getRaQueryDataSet().getString("OZNVAL"),
                                    this.getRaQueryDataSet().getShort("GODINA"),
                                    brizv,vrsta));

    updateStavPN = "update stavkepn set ispl='D' where oznval='" + this.getRaQueryDataSet().getString("OZNVAL") +
                   "' and cpn='" + this.getRaQueryDataSet().getString("CPN") + "'";
    return true;
  }

  public boolean doWithSave(char mode){
    try {
      raTransaction.runSQL(updateStavPN);
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  public void AfterSave(char mode){
//    updateStavPN = "update stavkepn set ispl='D' where oznval='" + this.getRaQueryDataSet().getString("OZNVAL") +
//                   "' and cpn='" + this.getRaQueryDataSet().getString("CPN") + "'";
//    System.out.println("razlika : " + this.getRaQueryDataSet().getBigDecimal("PRIMITAK").subtract(this.getRaQueryDataSet().getBigDecimal("IZDATAK")));
    ss.updatePutniNalogObracunUplaceno(this.getRaQueryDataSet().getString("CPN"),this.getRaQueryDataSet().getBigDecimal("PVPRIMITAK").subtract(this.getRaQueryDataSet().getBigDecimal("PVIZDATAK")));
    /** @todo ode nesto smrdi */
    String knjig = this.getRaQueryDataSet().getString("KNJIG");
    String oznval = this.getRaQueryDataSet().getString("OZNVAL");
    int cblag = this.getRaQueryDataSet().getInt("CBLAG");
    int brizv = this.getRaQueryDataSet().getInt("BRIZV");
    short god = this.getRaQueryDataSet().getShort("GODINA");
    BigDecimal izdatak = this.getRaQueryDataSet().getBigDecimal("IZDATAK");
    BigDecimal pvizdatak = this.getRaQueryDataSet().getBigDecimal("PVIZDATAK");
    BigDecimal primitak = this.getRaQueryDataSet().getBigDecimal("PRIMITAK");
    BigDecimal pvprimitak = this.getRaQueryDataSet().getBigDecimal("PVPRIMITAK");
    Timestamp datum = this.getRaQueryDataSet().getTimestamp("DATUM");
    ss.updateBlagizvPriAkontacijiRazlici(knjig, oznval, cblag, brizv, god, izdatak, primitak, pvizdatak, pvprimitak, datum);
    razlika.deleteRow();
    if (razlika.rowCount()==0){
      ss.setPutninalogIsplacen(currCpn);
      ss.calculateTecRazlika(currCpn);
      this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[0],false);
      this.getOKpanel().jPrekid_actionPerformed();
//      ss.calculateTecRazlika(this.getRaQueryDataSet().getString("CPN"));
      JOptionPane.showMessageDialog(this.getWindow(),
                                    "Putni nalog je ispla\u0107en",
                                    "Obavijest", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void jbInit() throws Exception {
    this.addOption(rnvPreselectBotun, 5, false);
    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[1],false);
    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[2],false);
    this.setRaQueryDataSet(dm.getStavblag());
    this.setVisibleCols(new int[] {13, 1, 2, 6, 7, 8});
    jpDetail = new jpRazlikaPN(this);
    this.setRaDetailPanel(jpDetail);
    this.getRepRunner().clearAllReports();
    this.getRepRunner().addReport("hr.restart.blpn.repRazlikaPNUplIspl","Uplatnica - isplatnica razlike putnog naloga",2);
    this.getRepRunner().addReport("hr.restart.blpn.repRazlikaPNAllUplIspl","Sve uplatnice - isplatnice razlike putnog naloga",2);
  }

  public QueryDataSet getRepQDS(){
    return repQDS;
  }

  public QueryDataSet getRepQDSall(){
    return repQDSall;
  }

  public void updateValutaLabel(){
    if (!jpDetail.jlrCblag.getText().equals("")){
      this.getRaQueryDataSet().setString("OZNVAL", ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
      jpDetail.jlValuta.setText(ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
      jpDetail.jlValuta2.setText(ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText()));
      if (this.getMode() == 'N') setIznose();
    } else {
      jpDetail.jlValuta.setText("");
      jpDetail.jlValuta2.setText("");
    }
  }

  public void setIznose(){
    if (!this.getRaQueryDataSet().getString("OZNVAL").equals("")){
      razlika.first();
      do{
        if (razlika.getString("VALUTA").equals(this.getRaQueryDataSet().getString("OZNVAL"))){
          if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) < 0){
            this.getRaQueryDataSet().setBigDecimal("PRIMITAK", razlika.getBigDecimal("RAZLIKA").negate());
            this.getRaQueryDataSet().setBigDecimal("IZDATAK", new BigDecimal(0));
          } else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) > 0){
            this.getRaQueryDataSet().setBigDecimal("PRIMITAK", new BigDecimal(0));
            this.getRaQueryDataSet().setBigDecimal("IZDATAK", razlika.getBigDecimal("RAZLIKA"));
          } else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) == 0){
            this.getRaQueryDataSet().setBigDecimal("IZDATAK", new BigDecimal(0));
            this.getRaQueryDataSet().setBigDecimal("PRIMITAK", new BigDecimal(0));
          }
          break;
        } else {
          this.getRaQueryDataSet().setBigDecimal("IZDATAK", new BigDecimal(0));
          this.getRaQueryDataSet().setBigDecimal("PRIMITAK", new BigDecimal(0));
        }
      } while (razlika.next());
    }
  }
}