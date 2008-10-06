/****license*****************************************************************
**   file: frmAkontacijaPN.java
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
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmAkontacijaPN extends raMatPodaci implements raPreSelectAware {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  QueryDataSet repQDS = new QueryDataSet();
  QueryDataSet repQDSall = new QueryDataSet();
  QueryDataSet rastblandarh;
  sgStuff ss = sgStuff.getStugg();
  PreSelect press;

  String knjig;
  String oznval;
  int cblag;
  int brizv;
  short god;
  BigDecimal izdatak;
  BigDecimal pvizdatak;
  Timestamp datum;

  jpAkontacijaPN jpDetail;

  raNavAction rnvPreselectBotun = new raNavAction("Predselekcija", raImages.IMGZOOM, KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      getPreSelect().showPreselect("Broj putnog naloga");
      setTitle("Isplata akontacije po putnom nalogu broj "+getPreSelect().getSelRow().getString("CPN"));
      getJpTableView().fireTableDataChanged();
    }
  };


  public frmAkontacijaPN() {
    try {
      fapn = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setPreSelect(PreSelect prs){
    press = prs;
  }

  public PreSelect getPreSelect(){
    return press;
  }

  static frmAkontacijaPN fapn;

  public static frmAkontacijaPN getFAPN(){
    return fapn;
  }

  public void beforeShow(){
    startFrame.getStartFrame().centerFrame(this, 0 ,"");
    setTitle("Isplata akontacije po putnom nalogu broj "+getPreSelect().getSelRow().getString("CPN"));
//    String current = "SELECT lokk, aktiv, knjig, cblag, oznval, godina, brizv, rbs, datum, primitak, "+
//                     "izdatak, pvprimitak, pvizdatak, tecaj, cradnik, cpn, opis, tko, cgrstav, stavka, cskl, vrdok, corg "+
//                     "FROM Stavblag "+
//                     "WHERE STAVBLAG.CRADNIK = '" + getPreSelect().getSelRow().getString("CRADNIK") + "' "+
//                     "AND STAVBLAG.CPN = '" + getPreSelect().getSelRow().getString("CPN") + "' "+
//                     "AND STAVBLAG.CSKL = '6' "+
//                     "AND STAVBLAG.STAVKA = '1' "+
//                     "UNION "+
//                     "SELECT lokk, aktiv, knjig, cblag, oznval, godina, brizv, rbs, datum, primitak, "+
//                     "izdatak, pvprimitak, pvizdatak, tecaj, cradnik, cpn, opis, tko, cgrstav, stavka, cskl, vrdok, corg "+
//                     "FROM Stavkeblarh "+
//                     "WHERE Stavkeblarh.CRADNIK = '" + getPreSelect().getSelRow().getString("CRADNIK") + "' "+
//                     "AND Stavkeblarh.CPN = '" + getPreSelect().getSelRow().getString("CPN") + "' "+
//                     "AND Stavkeblarh.CSKL = '6' "+
//                     "AND Stavkeblarh.STAVKA = '1'";
//    QueryDataSet qds = ut.getNewQueryDataSet(current, false);
//    qds.setColumns(dm.getStavblag().cloneColumns());
//    qds.open();
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.showInFrame(qds,"...");
//    this.setRaQueryDataSet(qds);
//    this.setVisibleCols(new int[] {13, 1, 2, 6, 8});
  }

  public void SetFokus(char mode) {
    jpDetail.jlrCpn.requestFocus();
    jpDetail.jlrCpn.forceFocLost();
    rcc.setLabelLaF(jpDetail.jlrCpn, false);
    if (mode == 'N') {
      getPreSelect().copySelValues();
      this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      this.getRaQueryDataSet().setTimestamp("DATUM", ut.clearTime(vl.getToday()));
      this.getRaQueryDataSet().setString("VRDOK", "BL");
      this.getRaQueryDataSet().setShort("GODINA", Short.parseShort(vl.findYear(vl.getToday())));
      if (!jpDetail.jlValuta.getText().equals("")) jpDetail.jlValuta.setText("");
      jpDetail.jlrCblag.emptyTextFields();
      jpDetail.jraDatum.requestFocus();
      } //else if (mode == 'I') {}
  }

  public boolean Validacija(char mode) {
    int brizv = ss.getBrizv(this.getRaQueryDataSet().getInt("CBLAG"), this.getRaQueryDataSet().getString("KNJIG"),
                            this.getRaQueryDataSet().getString("OZNVAL"), this.getRaQueryDataSet().getShort("GODINA"));
    if (brizv == 0) {
      //mozda ima u prosloj godini
      brizv = ss.getBrizv(this.getRaQueryDataSet().getInt("CBLAG"), this.getRaQueryDataSet().getString("KNJIG"),
          this.getRaQueryDataSet().getString("OZNVAL"), (short)(this.getRaQueryDataSet().getShort("GODINA")-1));
      if (brizv != 0) {
        this.getRaQueryDataSet().setShort("GODINA",(short)(this.getRaQueryDataSet().getShort("GODINA")-1));
      }
    }
    if (brizv == 0) {
      jpDetail.jlrCblag.requestFocus();
      JOptionPane.showConfirmDialog(this.jpDetail, "Nema otvorenog izvješ\u0107a za blagajnu \"".concat(this.jpDetail.jlrNaziv.getText()).concat("\"") , "Upozorenje", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (!this.getRaQueryDataSet().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      BigDecimal pvizd = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("IZDATAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      this.getRaQueryDataSet().setBigDecimal("TECAJ", ss.getTECAJ(this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM")));
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", pvizd);
    } else {
      this.getRaQueryDataSet().setBigDecimal("PVIZDATAK", this.getRaQueryDataSet().getBigDecimal("IZDATAK"));
    }
    this.getRaQueryDataSet().setString("TKO", ss.getIme(this.getRaQueryDataSet().getString("CRADNIK")));  // ss.getIme(jpDetail.jlrCradnika.getText()));
    String opis = "Isplata akontacije po PN ".concat(this.getRaQueryDataSet().getString("CPN"));
    this.getRaQueryDataSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    this.getRaQueryDataSet().setString("OPIS", opis);
    this.getRaQueryDataSet().setInt("BRIZV", brizv);
    this.getRaQueryDataSet().setInt("RBS", ss.getNextRBSstavblag(this.getRaQueryDataSet().getString("KNJIG"),
        this.getRaQueryDataSet().getInt("CBLAG"),
        this.getRaQueryDataSet().getString("OZNVAL"),
        this.getRaQueryDataSet().getShort("GODINA"),
        brizv,"I"));
    this.getRaQueryDataSet().setString("VRSTA", "I");
    if (!this.getRaQueryDataSet().getString("OZNVAL").equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      BigDecimal pvizd = ss.currrencyConverterToKN(this.getRaQueryDataSet().getBigDecimal("IZDATAK"),this.getRaQueryDataSet().getString("OZNVAL"),this.getRaQueryDataSet().getTimestamp("DATUM"));
      ss.updatePutniNalogAkontacija(this.getRaQueryDataSet().getString("CPN"),pvizd);
    } else {
      ss.updatePutniNalogAkontacija(this.getRaQueryDataSet().getString("CPN"),this.getRaQueryDataSet().getBigDecimal("IZDATAK"));
    }
    ss.setPutninalogAkontiran(this.getRaQueryDataSet().getString("CPN"));
    return true;
  }

  public void AfterSave(char mode){
    knjig = this.getRaQueryDataSet().getString("KNJIG");
    oznval = this.getRaQueryDataSet().getString("OZNVAL");
    cblag = this.getRaQueryDataSet().getInt("CBLAG");
    brizv = this.getRaQueryDataSet().getInt("BRIZV");
    god = this.getRaQueryDataSet().getShort("GODINA");
    izdatak = this.getRaQueryDataSet().getBigDecimal("IZDATAK");
    pvizdatak = this.getRaQueryDataSet().getBigDecimal("PVIZDATAK");
    datum = this.getRaQueryDataSet().getTimestamp("DATUM");

    ss.updateBlagizvPriAkontacijiRazlici(knjig, oznval, cblag, brizv, god, izdatak, new BigDecimal(0), pvizdatak,  new BigDecimal(0), datum);
  }

  public boolean BeforeDelete(){
    return false;
  }

  public QueryDataSet getRepQDS(){
    return repQDS;
  }

  public QueryDataSet getRepQDSall(){
    return repQDSall;
  }

  public void Funkcija_ispisa(){
  	Aus.refilter(repQDS, ss.getRepIsplatnicaZaAkontacijuRazliku(this.getRaQueryDataSet()));
  	Aus.refilter(repQDSall, ss.getRepSveIsplatniceZaAkontacijuRazliku(this.getRaQueryDataSet()));
//    repQDSall = ut.getNewQueryDataSet(ss.getRepSveIsplatniceZaAkontacijuRazliku(this.getRaQueryDataSet()));
    super.Funkcija_ispisa();
  }

  private void jbInit() throws Exception {
//    rastblandarh = ut.getNewQueryDataSet("select * from stavblag");
    this.addOption(rnvPreselectBotun, 5, false);
    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[1],false);
    this.setEnabledNavAction(this.getNavBar().getNavContainer().getNavActions()[2],false);
    this.setRaQueryDataSet(dm.getStavblag());
    this.setVisibleCols(new int[] {13, 1, 2, 6, 8});
    jpDetail = new jpAkontacijaPN(this);
    this.setRaDetailPanel(jpDetail);
    this.getRepRunner().clearAllReports();
    this.getRepRunner().addReport("hr.restart.blpn.repAkontacijaPNIspl","Isplatnica akontacije putnog naloga",2);
    this.getRepRunner().addReport("hr.restart.blpn.repAkontacijaPNAllIspl","Sve isplatnice akontacije putnog naloga",2);
  }

  public void updateValutaLabel(){
    if (!jpDetail.jlrCblag.getText().equals("")){
      String valutaBLAG;
      String valutaAKON = ss.getAkontacijaValuta(jpDetail.jlrCpn.getText());
      valutaBLAG = ss.getOznvalPrikoBlagajne(this.getRaQueryDataSet().getString("KNJIG"), jpDetail.jlrCblag.getText());
      if(!valutaBLAG.equals(valutaAKON)){
        this.getRaQueryDataSet().setBigDecimal("IZDATAK", ss.currencyConverter(ss.getAkontacija(jpDetail.jlrCpn.getText()),
            valutaAKON,valutaBLAG,vl.getToday()));
      }else{
        this.getRaQueryDataSet().setBigDecimal("IZDATAK", ss.getAkontacija(jpDetail.jlrCpn.getText()));
      }
      this.getRaQueryDataSet().setString("OZNVAL", valutaBLAG);
      jpDetail.jlValuta.setText(valutaBLAG);
    } else {
      jpDetail.jlValuta.setText("");
    }
  }
}