/****license*****************************************************************
**   file: frmPrijavaPN.java
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
import hr.restart.robno.Util;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raPreSelectAware;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmPrijavaPN extends raMatPodaci implements raPreSelectAware {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  sgStuff ss = sgStuff.getStugg();
  QueryDataSet repQDS = new QueryDataSet();

  jpPrijavaPN jpDetail;

  static frmPrijavaPN fppn;

  public static frmPrijavaPN getFrmPrijavaPN(){
    return fppn;
  }

  public frmPrijavaPN() {
    try {
      fppn = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  PreSelect pres;
  raNavAction rnvPreSel = new raNavAction("Predselekcija",raImages.IMGZOOM,KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      getPreSelect().showPreselect(frmPrijavaPN.this,frmPrijavaPN.this.getTitle());
      getJpTableView().fireTableDataChanged();
      jeprazno();
    }
  };

  public PreSelect getPreSelect() {
    return pres;
  }

  public void setPreSelect(PreSelect _pres) {
    pres = _pres;
  }

  public void beforeShow(){
    startFrame.getStartFrame().centerFrame(this, 0 ,"Prijava putnog naloga");
  }

  void enableZemju(boolean jeli){
    rcc.setLabelLaF(jpDetail.jlrCzemlje, jeli);
    rcc.setLabelLaF(jpDetail.jlrNazivzem, jeli);
    rcc.setLabelLaF(jpDetail.jbSelCzemlje, jeli);
  }

  public void SetFokus(char mode) {
    rcc.setLabelLaF(jpDetail.fieldValuta,false);
    if (mode == 'N') {
      jpDetail.rcbIndiPuta.setSelectedIndex(0);

      String knjig = hr.restart.zapod.OrgStr.getKNJCORG();
      short godina = getPreSelect().getSelRow().getShort("GODINA");
      this.getRaQueryDataSet().setString("KNJIG", knjig);
      this.getRaQueryDataSet().setShort("GODINA", godina);
      this.getRaQueryDataSet().setString("STATUS", "P");
      this.getRaQueryDataSet().setString("CZEMLJE", ss.zemZemlje().getString("CZEMLJE"));
      this.getRaQueryDataSet().setTimestamp("DATUMODL", getDefaultDatOdl());
      this.getRaQueryDataSet().setString("CORG", knjig);
      jpDetail.jlrCzemlje.forceFocLost();
      jpDetail.jlrCorg.forceFocLost();
      enableZemju(false);
      jpDetail.jraTrajanje.requestFocus();
      jpDetail.jlrCradnik.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCradnik.requestFocus();
      if(this.getRaQueryDataSet().getString("INDPUTA").equals("Z"))
        enableZemju(false);
      else
        enableZemju(true);
      akontacija(this.getRaQueryDataSet().getString("CZEMLJE"), mode);
    }
  }

  private Timestamp getDefaultDatOdl() {
    Timestamp def = hr.restart.util.Util.getUtil().addDays(vl.getToday(),1);
    if (def.after(getPreSelect().getSelRow().getTimestamp("DATUMODL-to"))) 
      return getPreSelect().getSelRow().getTimestamp("DATUMODL-to");
    if (def.before(getPreSelect().getSelRow().getTimestamp("DATUMODL-from"))) 
      return getPreSelect().getSelRow().getTimestamp("DATUMODL-from"); 
    return def;
  }

  public void AfterAfterSave(char mode){
    if (mode == 'N'){
      jpDetail.jlrCradnik.emptyTextFields();
      jpDetail.jlrCsif.emptyTextFields();
    }
    super.AfterAfterSave(mode);
  }

  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if(jpDetail.rcbIndiPuta.getDataValue().equals("")){
        jpDetail.rcbIndiPuta.requestFocus();
        return false;
      }
    }
    return true;
  }

  /** @todo dowithsave - EXEPSHN */

//  public boolean doWithSave(char mode){
//    return true;
//  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getPutniNalog());
    this.setVisibleCols(new int[] {3, 4, 5});
    jpDetail = new jpPrijavaPN(this);
    this.setRaDetailPanel(jpDetail);
    this.getRepRunner().clearAllReports();
    this.getRepRunner().addReport("hr.restart.blpn.repPutniNalog","Putni nalog",2);
    getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier(
        "CRADNIK",
        new String[] {"CRADNIK","IME","PREZIME"},
        dm.getRadnici()
    ));
    int position = 5;
    addOption(rnvPreSel,position,false);
    raDataIntegrity.installFor(this);
  }

  boolean samoJedan = false;
  private String seqpn;

  public void stateIsChanged(){
    changestate(this.getMode());
  }
  public boolean DeleteCheck() {
    seqpn = "PN" + "-" + this.getRaQueryDataSet().getString("KNJIG") + "-" + this.getRaQueryDataSet().getShort("GODINA") + "-Z";// + this.getRaQueryDataSet().getString("INDPUTA");
    if (!this.getRaQueryDataSet().getString("STATUS").equals("P")) {
      JOptionPane.showMessageDialog(this, "Putni nalog je akontiran ili obraèunat!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    int seq = vl.findSeqInt(seqpn,false);
    if (seq != getRaQueryDataSet().getInt("BROJ")+1 ) {
      JOptionPane.showMessageDialog(this, "Brisati se može samo zadnji putni nalog", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  public boolean doBeforeSave(char mode) {
    getRaQueryDataSet().post();
    System.out.println("seqpn = "+seqpn);
    System.out.println("mode = "+mode);
    if (mode == 'B') {
      try {
        Util.getUtil().delSeq(seqpn,true);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } else if (mode == 'N') {
      seqpn = "PN" + "-" + this.getRaQueryDataSet().getString("KNJIG") + "-" + this.getRaQueryDataSet().getShort("GODINA") + "-Z"; //+ this.getRaQueryDataSet().getString("INDPUTA");
      int broj = vl.findSeqInt(seqpn);
      String cpn = this.getRaQueryDataSet().getString("KNJIG").concat("-").concat(this.getRaQueryDataSet().getShort("GODINA")+"-").
                   concat(vl.maskZeroInteger(new Integer(broj), 5)).concat("-".concat(this.getRaQueryDataSet().getString("INDPUTA")));
      this.getRaQueryDataSet().setInt("BROJ", broj);
      this.getRaQueryDataSet().setString("CPN", cpn);
      this.getRaQueryDataSet().post();      
    }
    return true;
  }
  void changestate(char mode){
    if (mode != 'B'){
      if (samoJedan && jpDetail.rcbIndiPuta.getDataValue().equals("Z")){
        enableZemju(false);
        this.getRaQueryDataSet().setString("CZEMLJE", ss.zemZemlje().getString("CZEMLJE"));
        jpDetail.jlrCzemlje.forceFocLost();
        jpDetail.jraAkontacija.requestFocus();
      } else if (samoJedan){
        enableZemju(true);
        jpDetail.jlrCzemlje.setText("");
        jpDetail.jlrCzemlje.emptyTextFields();
        jpDetail.jlrCzemlje.requestFocus();
      }
      samoJedan = !samoJedan;

      akontacija(this.getRaQueryDataSet().getString("CZEMLJE"), this.getMode());
    }
  }

  public QueryDataSet getrep(){
    return repQDS;
  }

  public void Funkcija_ispisa(){
  	Aus.refilter(repQDS, ss.repQueryStringPutniNalog(this.getRaQueryDataSet().getString("KNJIG"),this.getRaQueryDataSet().getShort("GODINA"), this.getRaQueryDataSet().getInt("BROJ")));
    super.Funkcija_ispisa();
  }


  public void akontacija(String zemlja, char mode){
    QueryDataSet qds = new QueryDataSet();
    BigDecimal dnev, nocen, pokm, pol;
    String oznval;
    qds = ss.DNKL(zemlja);
    dnev   = qds.getBigDecimal("DNEVNICA");
    nocen  = qds.getBigDecimal("NOCENJE");
    pokm   = qds.getBigDecimal("LOCO");
    pol    = qds.getBigDecimal("LITBENZ");
    oznval = qds.getString("OZNVAL");

    if (mode == 'N' || mode == 'I'){
      double tmptrajanje = this.getRaQueryDataSet().getShort("TRAJANJE");
      BigDecimal trajanje = new BigDecimal(tmptrajanje);
      this.getRaQueryDataSet().setBigDecimal("AKONTACIJA",
        dnev.multiply(trajanje).add(nocen.multiply(trajanje.subtract(new BigDecimal(1)))));
    }
//    jpDetail.fieldValuta.setText(oznval);
  }

  public String getRadnika(){
    return ss.getIme(this.getRaQueryDataSet().getString("CRADNIK"));
  }

  public String getPrSr(){
    return ss.getPrijevoznoSredstvo(this.getRaQueryDataSet().getString("CPRIJSRED"));
  }

  public String getValuta(){
    QueryDataSet qds = new QueryDataSet();
    String oznval;
    qds = ss.DNKL(this.getRaQueryDataSet().getString("CZEMLJE"));
    oznval = qds.getString("OZNVAL");
    return oznval;
  }
}
