/****license*****************************************************************
**   file: frmPregledArhive.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavke;
import hr.restart.baza.Nalozi;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raCurrencyTableModifier;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPregledArhive extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  presPregled pres;

  static frmPregledArhive fpa;

  raNavAction rnaDe;

  jpSalKonMaster jpMaster;
  jpSalKonDetail jpDetail;

  boolean kupci, datdok, autoinc, bookDependant;
  int vrsta, cpar;
  String nazpar, corg, nazorg;
  Timestamp dfrom, dto;

  String[] mkey = {"KNJIG", "CPAR", "VRDOK", "BROJDOK"};
  String[] ekey = {"KNJIG", "CPAR", "BROJDOK"};

  public frmPregledArhive() {
    try {
      fpa = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmPregledArhive getInstance() {
    return fpa;
  }

  private void setPreselectValues() {
    kupci = pres.jpp.isKupci();
    datdok = pres.rcbDat.getSelectedIndex() == 0;
    vrsta = pres.rcbVrsta.getSelectedIndex();    
//    System.out.println(presq);
    cpar = pres.jpp.getCpar();
//    ld.raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[] {String.valueOf(cpar)});
//    nazpar = dm.getPartneri().getString("NAZPAR");
    nazpar = pres.jpp.getNazpar();
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(pres.getSelRow());
    corg = pres.getCorg();
    nazorg = pres.jpc.getNaziv();
    dfrom = ut.getFirstSecondOfDay(pres.getSelRow().getTimestamp("DATDOK-from"));
    dto = ut.getLastSecondOfDay(pres.getSelRow().getTimestamp("DATDOK-to"));
  }

  private void setTitle() {
    String[] what = new String[] {
      "Dokumenti", "Ra\u010Duni", "Uplate", "Knjižne obavijesti", "Ra\u010Duni i uplate"
    };
    VarStr title = new VarStr();
    title.append(what[vrsta]);
    if (cpar > 0)
      title.append(kupci ? " kupca " : " dobavlja\u010Da ").append(cpar).append(" ").append(nazpar);
    else title.append(kupci ? " kupaca" : " dobavlja\u010Da");
    title.append("  od ").append(rdu.dataFormatter(dfrom));
    title.append(" do ").append(rdu.dataFormatter(dto));
    this.setNaslovMaster(title.toString());
  }
  
  public void EntryPointDetail(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCorg, false);
      rcc.setLabelLaF(jpDetail.jlrNaziv, false);
      rcc.setLabelLaF(jpDetail.jbSelCorg, false);
      rcc.setLabelLaF(jpDetail.jlrKonto, false);
      rcc.setLabelLaF(jpDetail.jlrNazKonto, false);
      rcc.setLabelLaF(jpDetail.jbSelKonto, false);
      jpDetail.enableIznos(false, false);
    }
  }
  
  public void SetFokusDetail(char mode) {
    if (mode == 'I') jpDetail.jlrCkolone.requestFocus();
  }
  public void beforeShowMaster() {
    setPreselectValues();
//    prepareColumns();
    setTitle();
    jpMaster.setVrsk(kupci ? "IRN" : "URN");
    jpMaster.jlaDatknj.setVisible(!kupci);
    jpMaster.jraDatknj.setVisible(!kupci);
    
    if (kupci) {
      jpMaster.jlrCknjige.setRaDataSet(dm.getKnjigeI());
      jpDetail.jlrCkolone.setRaDataSet(dm.getIzlazneKolone());
      jpMaster.jpp.setPartnerKup();
    } else {
      jpMaster.jlrCknjige.setRaDataSet(dm.getKnjigeU());
      jpDetail.jlrCkolone.setRaDataSet(dm.getUlazneKolone());
      jpMaster.jpp.setPartnerDob();
    }
    
  	autoinc = frmParam.getParam("sk", "autoIncExt", "D", 
         "Automatsko poveæavanje dodatnog broja URA/IRA (D/N)").equalsIgnoreCase("D");
    bookDependant = frmParam.getParam("sk", "extKnjiga", "D", 
    	"Ima li svaka knjiga zaseban brojaè (D/N)").equalsIgnoreCase("D");
  }

  private String MDP() {
    String vd = getMasterSet().getString("VRDOK");
    if (vd.equals("IRN") || vd.equals("UPL") || vd.equals("OKK")) return "ID";
    else return "IP";
  }

  private String DDP() {
    if (getDetailSet().getInt("RBS") == 1) return MDP();
    else if (getDetailSet().getString("DUGPOT").equals("D")) return "ID";
    else return "IP";
  }

//  private void checkDe() {
//    rnaDe.setEnabled(getMasterSet().rowCount() > 0);
//  }

  public void masterSet_navigated(NavigationEvent e) {
    jpMaster.jraIznos.setColumnName(MDP());
//    checkDe();
  }

  public void detailSet_navigated(NavigationEvent e) {
    jpDetail.jraIznos.setColumnName(DDP());
  }

  private DataRow delStavka;
  
  private String oldOpis, oldBroj, oldExt;
  private Timestamp oldDatdok, oldDatdosp;
  public void EntryPointMaster(char mode) {
    rcc.EnabDisabAll(jpMaster, false);
    rcc.setLabelLaF(jpMaster.jraOpis, true);
    rcc.setLabelLaF(jpMaster.jraBrojdok, true);
    rcc.setLabelLaF(jpMaster.jraExtbrojdok, true);
    rcc.setLabelLaF(jpMaster.jraDatdok, true);
    //if (!getMasterSet().isNull("DATDOSP"))
      rcc.setLabelLaF(jpMaster.jraDatdosp, true);
    oldOpis = getMasterSet().getString("OPIS");
    oldBroj = getMasterSet().getString("BROJDOK");
    oldExt = getMasterSet().getString("EXTBRDOK");
    oldDatdok = ut.getFirstSecondOfDay(getMasterSet().getTimestamp("DATDOK"));
    oldDatdosp = ut.getFirstSecondOfDay(getMasterSet().getTimestamp("DATDOSP"));
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'I') jpMaster.jraOpis.requestFocus();
  }
  
  private Condition getGkstavkeCond(String cgkstavke) {
    try {
      String[] parts = new VarStr(cgkstavke).split('-');
      return Condition.equal("KNJIG", parts[0]).and(
          Condition.equal("GOD", parts[1])).and(
          Condition.equal("CVRNAL", parts[2])).and(
          Condition.equal("RBR", Integer.parseInt(parts[3]))).and(
          Condition.equal("RBS", Integer.parseInt(parts[4])));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public boolean isValidateRange() {
    return false;
  }
  
  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraBrojdok) || vl.isEmpty(jpMaster.jraDatdok) ||
        (jpMaster.jraDatdosp.isEnabled() && vl.isEmpty(jpMaster.jraDatdosp)))
      return false;
    if (jpMaster.jraDatdosp.isEnabled() && 
        !Aus.checkDatAndDosp(jpMaster.jraDatdok, jpMaster.jraDatdosp)) return false;
    if (!jpMaster.jraDatdosp.isEnabled() &&
        !Aus.checkSanityRange(jpMaster.jraDatdok)) return false;
    
    String gks = getMasterSet().getString("CGKSTAVKE");
    if (gks != null && gks.length() > 8) {
      if (!oldDatdok.equals(ut.getFirstSecondOfDay(getMasterSet().getTimestamp("DATDOK")))) {
        Condition gk = getGkstavkeCond(gks);
        if (gk == null || Gkstavke.getDataModule().getRowCount(gk) != 1) {
          jpMaster.jraDatdok.requestFocus();
          JOptionPane.showMessageDialog(jpMaster, "Datum je promijenjen " +
              "a ne mogu naæi odgovarajuæu stavku glavne knjige!",
              "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    if (!oldBroj.equals(getMasterSet().getString("BROJDOK"))) {
      if (frmSalKon.docExists(this, jpMaster)) return false;
      if (frmSalKon.docExistsInArh(this, jpMaster)) return false;
      if (frmSalKon.docExistsInRobno(this, jpMaster)) return false;
    }
    return true;
  }
  
  public boolean DeleteCheckMaster() {
    if (raDetail.isShowing()) raDetail.setVisible(false);
    if (checkDetailFilter()) return false;
    String gks = getMasterSet().getString("CGKSTAVKE");
    if (gks != null && gks.length() > 8) {
      String cnaloga = gks.substring(0, gks.lastIndexOf('-'));
      DataSet nalog = Nalozi.getDataModule().getTempSet(Condition.equal("CNALOGA", cnaloga));
      nalog.open();
      if (nalog.rowCount() > 0 && "K".equalsIgnoreCase(nalog.getString("STATUS"))) {
        JOptionPane.showMessageDialog(jpMaster, "Ra\u010Dun je ve\u0107 proknjižen u glavnoj knjizi!",
                                      "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (getMasterSet().getBigDecimal("ID").add(getMasterSet().getBigDecimal("IP")).
        compareTo(getMasterSet().getBigDecimal("SALDO")) != 0) {
      JOptionPane.showMessageDialog(jpMaster, "Ra\u010Dun je ve\u0107 (djelomi\u010Dno ili potpuno) pokriven!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    delStavka = new DataRow(getMasterSet());
    getMasterSet().copyTo(delStavka);
    return true;
  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'B') {
      try {
        /*raSaldaKonti.setKumInvalid();
        delStavka.setBigDecimal("ID", delStavka.getBigDecimal("ID").negate());
        delStavka.setBigDecimal("IP", delStavka.getBigDecimal("IP").negate());
        raSaldaKonti.addToKumulativ(delStavka);*/
        getDetailSet().deleteAllRows();
        raTransaction.saveChanges(getDetailSet());
        if (autoinc) {
        	int next = Valid.getValid().findSeqInt(OrgStr.getKNJCORG(false) +
        			(kupci ? "IRA-" : "URA-") +
          		  ut.getYear(delStavka.getTimestamp("DATUMKNJ")) +
          		  (bookDependant ? "-" + delStavka.getString("CKNJIGE") : ""), 
          		  false, false);
	    	int ext = Aus.getNumber(delStavka.getString("EXTBRDOK"));
	    	if (ext == next - 1) {
	    	  dm.getSeq().setDouble("BROJ", ext - 1);
	    	  raTransaction.saveChanges(dm.getSeq());
	    	}
        }
        //raTransaction.saveChanges(dm.getSkkumulativi());
        delStavka.clearValues();
        delStavka = null;
      } catch (Exception e) {
        e.printStackTrace();
//        raSaldaKonti.setKumInvalid();
        return false;
      }
    } else if (mode == 'I') {
      boolean dch = !oldDatdok.equals(ut.getFirstSecondOfDay(
          getMasterSet().getTimestamp("DATDOK")));
      boolean och = !oldOpis.equals(getMasterSet().getString("OPIS"));
      boolean bch = !oldBroj.equals(getMasterSet().getString("BROJDOK"));
      if (dch || och) {
        String gks = getMasterSet().getString("CGKSTAVKE");
        if (gks != null && gks.length() > 8) {
          Condition gkc = getGkstavkeCond(gks);
          if (gkc != null) {
            QueryDataSet gk = Gkstavke.getDataModule().getTempSet(gkc);
            gk.open();
            if (gk.rowCount() == 1) {
              if (dch) gk.setTimestamp("DATDOK", getMasterSet().getTimestamp("DATDOK"));
              if (och && oldOpis.equals(gk.getString("OPIS")))
                gk.setString("OPIS", getMasterSet().getString("OPIS"));
              raTransaction.saveChanges(gk);
            }
          }
        }
      }
      if (bch) {
        System.out.println("Changing brojdok...");
        String oldCSK = getMasterSet().getString("CSKSTAVKE");
        String newCSK = raSaldaKonti.findCSK(getMasterSet());
        String newBroj = getMasterSet().getString("BROJDOK");
        getMasterSet().setString("BROJDOK", oldBroj);
        QueryDataSet ui = UIstavke.getDataModule().getTempSet(
            Condition.whereAllEqual(mkey, getMasterSet()));
        ui.open();
        for (ui.first(); ui.inBounds(); ui.next())
          ui.setString("BROJDOK", newBroj);

        getMasterSet().setString("BROJDOK", newBroj);
        getMasterSet().setString("CSKSTAVKE", newCSK);
        
        QueryDataSet poku = Pokriveni.getDataModule().getTempSet(
            Condition.equal("CUPLATE", oldCSK));
        poku.open();
        for (poku.first(); poku.inBounds(); poku.next())
          poku.setString("CUPLATE", newCSK);
        
        QueryDataSet pokr = Pokriveni.getDataModule().getTempSet(
            Condition.equal("CRACUNA", oldCSK));
        pokr.open();
        for (pokr.first(); pokr.inBounds(); pokr.next())
          pokr.setString("CRACUNA", newCSK);
        
        raTransaction.saveChanges(ui);
        raTransaction.saveChanges(poku);
        raTransaction.saveChanges(pokr);
        System.out.println("Done changing brojdok.");
      }
    }
    return true;
  }
  
  void setTitleDetail() {
    boolean rac = raVrdokMatcher.isRacun(getMasterSet());
    boolean upl = raVrdokMatcher.isUplataTip(getMasterSet());
    boolean kup = raVrdokMatcher.isKup(getMasterSet());

    if (getDetailSet().rowCount() == 0 && upl)
       setNaslovDetail("Uplate nemaju UI stavke");
    else {
      String dod = getMasterSet().getString("EXTBRDOK");
      String brdat = getMasterSet().getString("BROJDOK") +
        (dod.length() > 0 ? " (" + dod + ")" : "") + " od " +
        hr.restart.robno.raDateUtil.getraDateUtil().
        dataFormatter(getMasterSet().getTimestamp("DATDOK"));
      if (rac) setNaslovDetail("Stavke " + (kup ? "izlaznog" : "ulaznog") + " raèuna "+brdat);
      else setNaslovDetail("Stavke knjižne obavijesti " + brdat);
    }
  }
  
  public void refilterDetailSet() {    
    super.refilterDetailSet();
    setTitleDetail();
  }

  public void deArchive() {
    if (raDetail.isShowing()) raDetail.setVisible(false);
    if (checkDetailFilter()) return;
    raVrdokMatcher vm = new raVrdokMatcher(getMasterSet());
    if (!vm.isKob() && !vm.isRacun()) {
      JOptionPane.showMessageDialog(jpMaster, "Poništiti se mogu samo dokumenti nastali u modulu SK!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    String what = vm.isRacun() ? "Raèun" : "Knjižna obavijest";
    if (getMasterSet().getBigDecimal("ID").add(getMasterSet().getBigDecimal("IP")).
        compareTo(getMasterSet().getBigDecimal("SALDO")) != 0) {
      JOptionPane.showMessageDialog(jpMaster, what +" je veæ (djelomièno ili potpuno) pokriven"
          +(vm.isRacun() ? "!" : "a!"), "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (getMasterSet().getString("CGKSTAVKE").length() > 0 && 
        !getMasterSet().getString("CGKSTAVKE").equals("#")) {
      JOptionPane.showMessageDialog(jpMaster, what + " je veæ proknjižen"+(vm.isKob() ? "a" : "")+" u glavnu knjigu!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.refilterDetailSet();
    if (getDetailSet().rowCount() == 0) {
      JOptionPane.showMessageDialog(jpMaster, "Ne postoje odgovarajuæe UI stavke za "+
          (vm.isKob() ? "ovu knjižnu obavijest" : " ovaj raèun"), "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    DataSet esk = Skstavke.getDataModule().getTempSet(
        Condition.whereAllEqual(ekey, getMasterSet()).and(
            Condition.where("BROJKONTA", Condition.NOT_EQUAL, getMasterSet())));
    esk.open();
    if (esk.rowCount() > 1) {
      JOptionPane.showMessageDialog(jpMaster, what +
         " se ne može poništiti zbog dvostrukog kljuèa!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    } else if (esk.rowCount() == 1) {
      esk.first();
      if (!esk.getBigDecimal("SSALDO").equals(esk.getBigDecimal("SALDO"))) {
        JOptionPane.showMessageDialog(jpMaster, what + " se ne može poništiti " +
            "zbog pokrivenog dvostrukog kljuca!", "Greška", JOptionPane.ERROR_MESSAGE);
         return;
      }
    }
    if (!getMasterSet().getString("BROJKONTA").equals(raSaldaKonti.getKonto(getMasterSet()))) {
      int ret = JOptionPane.showConfirmDialog(jpMaster,
          new raMultiLineMessage(what + " je knjižen na konto " +
          "razlièit od konta sheme po kojoj je knjižen.\nMoguæe je da se radi o greški " +
          "na shemi. Nastaviti?"), "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (ret != JOptionPane.OK_OPTION) return;
    }
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li poništiti"+
           " (prebaciti natrag u radne) "+(vm.isKob() ? "knjižnu obavijest " : "raèun ")+
           getMasterSet().getString("BROJDOK")+"?", "Potvrda",
       JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
    hr.restart.util.raLocalTransaction dearh = new hr.restart.util.raLocalTransaction() {
      public boolean transaction() throws Exception {
        String[] skscols = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "DATPRI", "DATDOK",
         "DATDOSP", "DATUNOS", "EXTBRDOK", "BROJIZV", "CNACPL", "OZNVAL", "TECAJ",
          "OPIS","ZIRO"};
        String[] uiscols = {"RBS", "CSKL", "STAVKA", "CORG", "CKOLONE",
          "CKNJIGE", "DUGPOT", "URAIRA", "BROJKONTA"};
        QueryDataSet radne = Skstavkerad.getDataModule().getTempSet("1=0");
        radne.open();
        
        QueryDataSet extrask = Skstavke.getDataModule().getTempSet(
            Condition.whereAllEqual(ekey, getMasterSet()).and(
                Condition.where("BROJKONTA", Condition.NOT_EQUAL, getMasterSet())));
        extrask.open();
        
        //raSaldaKonti.setKumInvalid();
        for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
          radne.insertRow(false);
          dM.copyColumns(getMasterSet(), radne, skscols);
          dM.copyColumns(getDetailSet(), radne, uiscols);
          radne.setTimestamp("DATPRI", getMasterSet().getTimestamp("DATUMKNJ"));
          radne.setString("CGKSTAVKE", getDetailSet().getString("CGKSTAVKE").equals("#") ? "N" : "D");
          if (getDetailSet().getInt("RBS") != 1 && getDetailSet().getString("BROJKONTA").
              equals(getMasterSet().getString("BROJKONTA")))
            radne.setString("BROJKONTA", raSaldaKonti.getKonto(getDetailSet()));
          radne.setBigDecimal("PVID", getDetailSet().getBigDecimal("ID"));
          radne.setBigDecimal("PVIP", getDetailSet().getBigDecimal("IP"));
          if (raSaldaKonti.isDomVal(getMasterSet())) {
            radne.setBigDecimal("ID", getDetailSet().getBigDecimal("ID"));
            radne.setBigDecimal("IP", getDetailSet().getBigDecimal("IP"));
          } else {
            radne.setBigDecimal("ID", getDetailSet().getBigDecimal("ID").
              divide(raSaldaKonti.calcTecaj(getMasterSet()), 2, BigDecimal.ROUND_HALF_UP));
            radne.setBigDecimal("IP", getDetailSet().getBigDecimal("IP").
              divide(raSaldaKonti.calcTecaj(getMasterSet()), 2, BigDecimal.ROUND_HALF_UP));
          }
          if (radne.getInt("RBS") != 1)
            radne.setBigDecimal("SALDO", radne.getBigDecimal("ID").subtract(radne.getBigDecimal("IP")));
          else radne.setBigDecimal("SALDO", radne.getBigDecimal("ID").add(radne.getBigDecimal("IP")));
        }
        /*getMasterSet().setBigDecimal("ID", getMasterSet().getBigDecimal("ID").negate());
        getMasterSet().setBigDecimal("IP", getMasterSet().getBigDecimal("IP").negate());
        raSaldaKonti.addToKumulativ(getMasterSet());*/
        if (extrask.rowCount() == 1) {
          /*extrask.first();
          extrask.setBigDecimal("ID", extrask.getBigDecimal("ID").negate());
          extrask.setBigDecimal("IP", extrask.getBigDecimal("IP").negate());
          raSaldaKonti.addToKumulativ(extrask);*/
          extrask.deleteAllRows();
        }
        getDetailSet().deleteAllRows();
        getMasterSet().deleteRow();
        raTransaction.saveChanges(radne);
        raTransaction.saveChanges(getMasterSet());
        raTransaction.saveChanges(getDetailSet());
        raTransaction.saveChanges(extrask);
//        raTransaction.saveChanges(dm.getSkkumulativi());
        return true;
      }
    };
    if (!dearh.execTransaction()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Transakcija nije uspjela!", "Greška",
        JOptionPane.ERROR_MESSAGE);
//      raSaldaKonti.setKumInvalid();
      return;
    }
    raMaster.getJpTableView().fireTableDataChanged();
  }
  
  void joinItems() {
    if (checkDetailFilter()) return;
    if (getDetailSet().rowCount() == 0) return;
    int rbs = getDetailSet().getInt("RBS");
    String side = getDetailSet().getBigDecimal("ID").signum() != 0 ? "ID" : "IP";
    if (rbs == 1) {
      JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Nije moguæe mijenjati stavku salda konti!",
          "Nedopuštena operacija", JOptionPane.ERROR_MESSAGE);
      return;
    }
    DataSet select = UIstavke.getDataModule().getTempSet(
        Condition.whereAllEqual(new String[] {"KNJIG", "CPAR", "VRDOK", 
            "BROJDOK", "BROJKONTA"}, getDetailSet()).and(
                Condition.where("RBS", Condition.NOT_EQUAL, rbs)));
    select.open();
    
    if (select.rowCount() == 0) {
      JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Ne postoji nijedna druga stavka s istim kontom!",
          "Nedopuštena operacija", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    String[] result = lookupData.getlookupData().lookUp(raDetail.getWindow(), select,
        new String[] {"RBS"}, new String[] {""}, new int[] {5,6,7,8});
    
    if (result == null) return;
    
    raDetail.getJpTableView().enableEvents(false);
    if (lookupData.getlookupData().raLocate(getDetailSet(), "RBS", result[0])) {
      BigDecimal val = getDetailSet().getBigDecimal(side);
      getDetailSet().deleteRow();
      if (lookupData.getlookupData().raLocate(getDetailSet(), "RBS", Integer.toString(rbs))) {
        getDetailSet().setBigDecimal(side, getDetailSet().getBigDecimal(side).add(val));
        try {
          getDetailSet().saveChanges();
          lookupData.getlookupData().raLocate(getDetailSet(), "RBS", Integer.toString(rbs));
          raDetail.getJpTableView().enableEvents(true);
          return;
        } catch (Exception e) {
          getDetailSet().refresh();
        }
      }
    }
    raDetail.getJpTableView().enableEvents(true);
    JOptionPane.showMessageDialog(raDetail.getWindow(), "Greška kod spajanja stavki!",
        "Greška", JOptionPane.ERROR_MESSAGE);
  }
  
  void splitItem() {
    if (checkDetailFilter()) return;
    if (getDetailSet().rowCount() == 0) return;
    if (getDetailSet().getInt("RBS") == 1) {
      JOptionPane.showMessageDialog(raDetail.getWindow(), 
          "Nije moguæe razdvajati stavku salda konti!",
          "Nedopuštena operacija", JOptionPane.ERROR_MESSAGE);
      return;
    }
    String side = getDetailSet().getBigDecimal("ID").signum() != 0 ? "ID" : "IP";
    dlgSplitAmount dlg = null;
    if (this.getWindow() instanceof Frame)
      dlg= new dlgSplitAmount((Frame) this.getWindow());
    if (this.getWindow() instanceof Dialog)
      dlg = new dlgSplitAmount((Dialog) this.getWindow());
    BigDecimal result = dlg.performSplit("Stavka br. "+
        getDetailSet().getInt("RBS"), getDetailSet().getBigDecimal(side));
    if (result != null && result.signum() != 0) {
      DataRow copy = new DataRow(getDetailSet());
      getDetailSet().copyTo(copy);
      getDetailSet().setBigDecimal(side, getDetailSet().getBigDecimal(side).subtract(result));
      raDetail.getJpTableView().enableEvents(false);
      int rbs = 0;
      for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
        if (getDetailSet().getInt("RBS") > rbs)
          rbs = getDetailSet().getInt("RBS");
      getDetailSet().insertRow(false);
      copy.copyTo(getDetailSet());
      getDetailSet().setInt("RBS", rbs + 1);
      getDetailSet().setBigDecimal(side, result);
      try {
        getDetailSet().saveChanges();
      } catch (Exception e) {
        getDetailSet().refresh();
        JOptionPane.showMessageDialog(raDetail.getWindow(), "Greška kod dodavanja stavke!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      }
      getDetailSet().last();
      raDetail.getJpTableView().enableEvents(true);
    }
  }
  
  public boolean checkDetailFilter() {
    if (getDetailSet().getRowFilterListener() != null) {
      JOptionPane.showMessageDialog(raDetail.getWindow(), "Prije pozivanja ove " +
            "funkcije potrebno je iskljuèiti filter!", "Upozorenje",
            JOptionPane.WARNING_MESSAGE);
      return true;
    }
    return false;
  }
  
  private void updateBalance() {
    if (getDetailSet().rowCount() <= 1) return;
    if (getMasterSet().getString("CGKSTAVKE").length() > 2) {
      JOptionPane.showMessageDialog(jpDetail, "Ra\u010Dun je ve\u0107 proknjižen u glavnoj knjizi!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (checkDetailFilter()) return;
    
    int row = getDetailSet().getInt("RBS");
    raDetail.getJpTableView().enableEvents(false);
    this.refilterDetailSet();
    getDetailSet().refresh();
    BigDecimal diff = raSaldaKonti.getDokSaldo(getDetailSet());
    lookupData.getlookupData().raLocate(getDetailSet(), "RBS", Integer.toString(row));
    //getDetailSet().goToRow(row);
    raDetail.getJpTableView().enableEvents(true);
    if (diff.signum() == 0) {
      JOptionPane.showMessageDialog(getWindow(), "Raèun je veæ u balansu!",
        "Upozorenje", JOptionPane.INFORMATION_MESSAGE); 
      return;
    }
    if (this.getDetailSet().getInt("RBS") == 1) {
      JOptionPane.showMessageDialog(getWindow(), "Popravak se ne može izvesti na prvoj stavci!",
          "Greška", JOptionPane.ERROR_MESSAGE); 
        return;
    }
    if (JOptionPane.showConfirmDialog(getWindow(), "Želite li popraviti razliku (" + diff +
            ") na ovoj stavci?",
       "Popravak", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
      return;
    if (getDetailSet().getBigDecimal("ID").signum() != 0)
      getDetailSet().setBigDecimal("ID", getDetailSet().getBigDecimal("ID").subtract(diff));
    else if (getDetailSet().getBigDecimal("IP").signum() != 0)
      getDetailSet().setBigDecimal("IP", getDetailSet().getBigDecimal("IP").add(diff));
    else {
      boolean id = true;
      boolean ip = true;
      String kar = jpDetail.jlrKarKonto.getText();    
      if (kar.equalsIgnoreCase("D")) ip = false;
      else if (kar.equalsIgnoreCase("P")) id = false;
      kar = jpDetail.jlrKarkolone.getText();
      if (kar.equalsIgnoreCase("D")) ip = false;
      else if (kar.equalsIgnoreCase("P")) id = false;
      
      if (!ip) getDetailSet().setBigDecimal("ID", diff.negate());
      else if (!id || kupci) getDetailSet().setBigDecimal("IP", diff);
      else getDetailSet().setBigDecimal("ID", diff.negate());
    }
    
    if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] 
                         {getDetailSet()})) {
      JOptionPane.showMessageDialog(getWindow(), "Popravak nije uspio!",
          "Greška", JOptionPane.ERROR_MESSAGE); 
      getDetailSet().refresh();
    } else {
      raDetail.getJpTableView().fireTableDataChanged();
    }
  }

  private void jbInit() throws Exception {

    this.setMasterSet(Skstavke.getDataModule().getFilteredDataSet("1=0"));
    this.setNaslovMaster("Ra\u010Dun salda konti");
    this.setVisibleColsMaster(new int[] {1, 4, 9, 29});
    this.setMasterKey(mkey);
    jpMaster = new jpSalKonMaster(null);
    jpMaster.BindComponents(getMasterSet());
    this.setJPanelMaster(jpMaster);

    pres = new presPregled(this);
    this.setPreSelect(pres, "Pregled arhive", false);
    this.initPreSelect();

    raMaster.removeRnvCopyCurr();
    this.raMaster.getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD,
        raNavBar.ACTION_TOGGLE_TABLE});

    raMaster.addOption(new raNavAction("Virmani", raImages.IMGALIGNRIGHT, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
          virmani();
        }
      },6);
    
    this.setDetailSet(UIstavke.getDataModule().getFilteredDataSet("1=0"));
    this.setNaslovDetail("Stavke ra\u010Duna");
    this.setVisibleColsDetail(new int[] {5,6,7,8});
    this.setDetailKey(new String[] {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "RBS"});
    jpDetail = new jpSalKonDetail(null);
    jpDetail.BindComponents(getDetailSet());
    this.setJPanelDetail(jpDetail);

    raMaster.installSelectionTracker("CSKSTAVKE");

    

//    pres.setSelDataSet(getMasterSet());
    this.raMaster.addOption(rnaDe = new hr.restart.util.raNavAction("Poništi", raImages.IMGEXPORT, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        deArchive();
      }
    }, 3);

    raDetail.removeRnvCopyCurr();
    this.raDetail.getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD,
        raNavBar.ACTION_DELETE, raNavBar.ACTION_TOGGLE_TABLE});

    this.raDetail.addOption(new hr.restart.util.raNavAction("Odvoji iznos", raImages.IMGCOPYCURR, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        splitItem();
      }
    }, 1);
    this.raDetail.addOption(new hr.restart.util.raNavAction("Spoji stavke", raImages.IMGSUM, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        joinItems();
      }
    }, 2);
    
    raDetail.addOption(new raNavAction("Popravi razliku", raImages.IMGIMPORT, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        updateBalance();
      }
    }, 3);
    
    this.raMaster.getJpTableView().addTableModifier(
        new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));

    this.raMaster.getJpTableView().addTableModifier(new raCurrencyTableModifier("PVSALDO"));

    //andrej: da se pravi konto lijepo vidi
    
    this.raDetail.getJpTableView().addTableModifier(
        new raTableModifier() {
          String[] keys = {"CSKL","STAVKA", "VRDOK"};
          String[] vals = new String[3];
          DataSet tableDs;
          Variant shared = new Variant();
          public boolean doModify() {
            if (getTable() instanceof JraTable2) {
              JraTable2 jtab = (JraTable2)getTable();
              Column dsCol = jtab.getDataSetColumn(getColumn());
              if (dsCol == null) return false;
              tableDs = dsCol.getDataSet();              
              return dsCol.getColumnName().equals("BROJKONTA");
            }
            return false;
          }
          public void modify() {
            tableDs.getVariant("BROJKONTA", getRow(), shared);
            if (!getMasterSet().getString("BROJKONTA").equals(shared.toString())) return;
            tableDs.getVariant("RBS", getRow(), shared);
            if (shared.getAsInt() == 1) return;

            tableDs.getVariant("CSKL", getRow(), shared);
            vals[0] = shared.toString();
            tableDs.getVariant("STAVKA", getRow(), shared);
            vals[1] = shared.toString();
            tableDs.getVariant("VRDOK", getRow(), shared);
            vals[2] = shared.toString();
            if (vals[2].equalsIgnoreCase("OKK")) vals[2] = "IRN";
            else if (vals[2].equalsIgnoreCase("OKD")) vals[2] = "URN";
            DataRow dr = lookupData.getlookupData().raLookup(dm.getShkonta(), keys, vals);
//          if (!ld.raLocate(dsToSearch,dsColsKeyS,vars)) return;
            if (dr == null) return;
            setComponentText(dr.getString("BROJKONTA"));
          }
        }
    );

  }
  
  protected void virmani() {
    if (raMaster.getSelectionTracker().countSelected() == 0) {//nista odabrano
        JOptionPane.showMessageDialog(raMaster.getWindow(),"Potrebno je odabrati raèune tipkom Enter za ispis virmana!");
    } else {
        new VirmaniSK(raMaster).show();
    }
  }
}

