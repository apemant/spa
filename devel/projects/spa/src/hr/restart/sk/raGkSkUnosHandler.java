/****license*****************************************************************
**   file: raGkSkUnosHandler.java
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
/*
 * raGkSkUnosHandler.java
 *
 * Created on 2003. srpanj 02, 13:26
 */

package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.dM;
import hr.restart.gk.frmNalozi;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <PRE>
 * Svrha:
 * Handlanje unosa salda konti dokumenata kroz temeljnicu
 * Zadaci:
 * offati frmNalozi.raMaster i frmNalozi.raDetail, nakon toga prikazati
 * extendani hr.restart.sk.frmSalKon ili hr.restart.sk.frmSalKonOK ovisno o vrdok i podmetnuti skstavkerad
 * koje treba oznaciti za copyGKuSK pri knjizenju temeljnice. Na ZatvoriOstaloMaster() on-ati frmNalozi i
 * refreshati detailSet da se pojave unesene stavke. Pri knjizenju prebaciti
 * gkstavkerad u skstavke ili whatever
 * Uveden parametar isGKSK (gk) koji ako je 'D' nudi unos sk kroz temeljnicu, 
 * a ako je 'N' (default) ne dogadja se nista
 * </PRE>
 * @deprected koristi se unos podataka inline u temeljnicu bez prisile za protustavkama
 * @author andrej
 */
public class raGkSkUnosHandler {
  
  /** parametar iz konstruktora */  
  public String opis;
  
  /** parametar iz konstruktora */  
  public String brojkonta;
  
  /** parametar iz konstruktora */  
  public String vrdok;
  
  /** parametar iz konstruktora */  
  public Timestamp datdok;
  
  /** parametar iz konstruktora */  
  public frmNalozi fNalozi;
  
  /** parametar iz konstruktora */  
  public String cnaloga;
  
  /** parametar iz konstruktora */  
  public int rbs;
  
  /** parametar iz konstruktora */  
  public BigDecimal iznos;
  
  /** parametar iz konstruktora */  
  public String csheme;
  
  private static frmSalKon fSalKon;
  private static frmSalKonOK fSalKonOK;
  private static frmSalKon fSalKonSK;
  private static String[] skVrdoks = {"URN","IRN"};
  private static String[] okVrdoks = {"OKK","OKD"};
  
  private static Util ut = Util.getUtil();

  private static QueryDataSet qdsvrdoks = dM.getDataModule().getVrdokum();
  private static QueryDataSet qdsshkonta = dM.getDataModule().getShkonta();
  private static QueryDataSet qdsknjigeui = dM.getDataModule().getKnjigeUI();
  
  private static QueryDataSet skMasterSet = Skstavkerad.getDataModule().getTempSet();
  private static QueryDataSet skDetailSet = Skstavkerad.getDataModule().getTempSet();
  private static JraTextField jtCGKSTAVKE;
  private boolean isIra;
  private static String CNDELIM = " / ";
  private static String[] sks_key = {"KNJIG", "CPAR", "VRDOK", "BROJDOK"};
  private static String[] gks_key = {"KNJIG", "GOD", "CVRNAL", "RBR"};
  private static String lastCgkstav = null;
  
  /** da li je prvi put usao u dodavanje racuna */  
  public boolean prviput = true;
  /**
   * da li je otvoren frmSalkon, pali se u showSalkon, a gasi u afterCloseSalkon
   */
  public static boolean inSalkon = false;
  
  /**
   * da li je parametrom u frmParam dozvoljen unos sk racuna kroz temeljnicu
   * prije testiranja ovog boolean pozvati checkParam() metodu
   */
  public static boolean isGKSK = false;
  
  //TEST
  private static hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  //ETEST
  
  /** Konstruira handler sa zadanim neophodnim podacima
   * @param _fnalozi Instanca frmN>alozi iz koje je pozvan handler
   * @param _opis Opis unesen u formu frmNalozi.raDetail
   * @param _brojkonta unesen u formu frmNalozi.raDetail
   * @param _vrdok odabran na comboboxu u formi frmNalozi.raDetail
   * @param _datdok datum dokumenta unesen u formu frmNalozi.raDetail
   * @param _rbs redni broj stavkegkrad koju se pokusava dodati
   * @param _iznos iznos (id ili ip) unesen u formu frmNalozi.raDetail
   */  
  public raGkSkUnosHandler(frmNalozi _fnalozi, String _opis, String _brojkonta, String _vrdok, Timestamp _datdok, int _rbs, BigDecimal _iznos) {
    fNalozi = _fnalozi;
    opis = _opis;
    brojkonta = _brojkonta;
    vrdok = _vrdok;
    datdok = _datdok;
    rbs = _rbs;
    iznos = _iznos;
    initHandler();
  }
  /** prikazuje ekran za unos salda konti */  
  public void showSalKon() {
    checkParam();
    if (!isGKSK) return;
    fNalozi.raDetail.getOKpanel().jPrekid_actionPerformed();
    fNalozi.raMaster.setEnabled(false);
    fNalozi.raDetail.setEnabled(false);
    hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
    fakePreselect(fSalKon.pres);
    fSalKon.show();
    afterShow();
    inSalkon = true;
  }
  private void afterShow() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        fSalKon.raMaster.rnvAdd_action();
      }
    });
  }
  
/* rijeseno preko predselekcije
  private QueryDescriptor addCNalogaQuery(String qry) {
    return new QueryDescriptor(dM.getDataModule().getDatabase1(),qry + " AND CGKSTAVKE = '"+cnaloga+"'");
  }
*/
  /** Ispisuje ime klase i parametre koji su poslani u konstruktoru
   * @return ime klase i parametre kostruktora
   */  
  public String toString() {
    return getClass().getName()+" :: parametri:\n fNalozi = "+fNalozi+"\n opis = "+opis+"\n brojkonta = "+brojkonta+"\n vrdok = "+vrdok+"\n datdok = "+datdok+"\n iznos = "+iznos;
  }
  
  public static void checkParam() {
    isGKSK = hr.restart.sisfun.frmParam.getParam(
                  "gk", "isGKSK", "N", "Unose li se SK dokumenti kroz temeljnicu (D/N)"
                ).trim().equals("D");
  }
  
  private void initHandler() {
    checkParam();
    if (!isGKSK) return;
    if (fNalozi == null || opis == null || brojkonta == null || vrdok == null || datdok == null || iznos == null
        || brojkonta.equals("") || vrdok.equals("")) 
      throw new IllegalArgumentException("Nisu definirani svi parametri \n "+toString());
    if (ut.containsArr(skVrdoks, vrdok)) {
      fSalKon = getSalKonSK();
    } else if (ut.containsArr(okVrdoks, vrdok)) {
      fSalKon = getSalKonOK();
    } else throw new IllegalArgumentException("Neispravna vrsta dokumenta "+vrdok);
    cnaloga = fNalozi.getMasterSet().getString("CNALOGA");

    qdsvrdoks.open();
    if (!lookupData.getlookupData().raLocate(qdsvrdoks, "VRDOK", vrdok)) throw new IllegalArgumentException("Nepoznata vrsta dokumenta "+vrdok);
    isIra = qdsvrdoks.getString("VRSDOK").equals("I");
    
    qdsshkonta.open();
    if (!lookupData.getlookupData().raLocate(qdsshkonta, new String[] {"VRDOK","BROJKONTA","STAVKA"}, 
                              new String[] {vrdok,brojkonta,"1"})) 
      throw new IllegalArgumentException("Ne postoji shema kontiranja raèuna za konto "+brojkonta+" i vrstu dokumenta "+vrdok);
    csheme = qdsshkonta.getString("CSKL");
  }
  
  private frmSalKon getSalKonSK() {
    if (fSalKonSK == null) {
      fSalKonSK = new salkonSK();
      fSalKonSK.setMasterSet(skMasterSet);
      fSalKonSK.setDetailSet(skDetailSet);
      fSalKonSK.jpMaster.BindComponents(skMasterSet);
      fSalKonSK.jpDetail.BindComponents(skDetailSet);
    }
    ((salkonSK)fSalKonSK).setHandler(this);
    return fSalKonSK;
  }
  
  private frmSalKon getSalKonOK() {
    if (fSalKonOK == null) {
      fSalKonOK = new salkonOK();
      fSalKonOK.setMasterSet(skMasterSet);
      fSalKonOK.setDetailSet(skDetailSet);
      fSalKonOK.jpMaster.BindComponents(skMasterSet);
      fSalKonOK.jpDetail.BindComponents(skDetailSet);
    }
    ((salkonOK)fSalKonOK).setHandler(this);
    return fSalKonOK;
  }
  
  /** zove se u ZatvoriOstaloMaster()
   * <PRE>
   * on-a frmNalozi
   * refresha datasete
   * </PRE>
   */  
  private void afterCloseSalKon() {
    fNalozi.raMaster.setEnabled(true);
    fNalozi.raDetail.setEnabled(true);
    if (!fNalozi.raDetail.isShowing()) return;
    fNalozi.raDetail.getJpTableView().enableEvents(false);
    raProcess.runChild(fNalozi.raDetail,new Runnable() {
      public void run() {
        handleGKStavke();
      }
    });
    refreshRecords(fNalozi);
    inSalkon = false;
  }
  
  public static void refreshRecords(frmNalozi fn) {
    fn.raDetail.getJpTableView().enableEvents(true);
    fn.raDetail.getJpTableView().getColumnsBean().refreshAction();
    fn.raDetail.getJpTableView().Zbrajalo();
    fn.raDetail.jeprazno();
  }
  
  private void handleGKStavke() {
    if (fNalozi.getMasterSet().getRowCount() == 0) return;
    if (skMasterSet.getRowCount() == 0) return;
//  GKSTAVKERAD:
//  knjig, god, cvrnal, rbr, rbs, brojkonta, corg, datumknj, datdok, datdosp, brojdok, vrdok, cpar, extbrdok, brojizv, cnacpl, opis, id, ip, pokriveno, saldo, ckolone, rbsrac, cknjige, uraira, godmj, tecaj, devid, devip, cnaloga    
//  SKSTAVKERAD:
//  knjig, cpar, stavka, cskl, vrdok, brojdok, rbs, brojizv, corg, datumknj, datdok, datdosp, extbrdok, cnacpl, oznval, tecaj, opis, id, ip, pokriveno, saldo, ckolone, cknjige, uraira, cgkstavke, godmj, dugpot, datpri, datunos, pvid, pvip, ziro, brojkonta
    hr.restart.util.raLocalTransaction trans = new hr.restart.util.raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          QueryDataSet sks = ut.getNewQueryDataSet("SELECT * FROM skstavkerad where cgkstavke = '"+cnaloga+"'");
          QueryDataSet gks = fNalozi.getDetailSet();
          BigDecimal sumID = Aus.zero2;
          BigDecimal sumIP = Aus.zero2;
          for (sks.first(); sks.inBounds(); sks.next()) {
            QueryDataSet sks_details = hr.restart.baza.Skstavkerad.getDataModule().getFilteredDataSet(Condition.whereAllEqual(sks_key, sks));
            sks_details.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"STAVKA"}));
            sks_details.open();
            sks_details.first();
            datdok = sks_details.getTimestamp("DATDOK");
            for (sks_details.first(); sks_details.inBounds(); sks_details.next()) {
              fNalozi.raDetail.Insertiraj(); //knjig, god, cvrnal, rbr
              fNalozi.jpDetail.jpBrNal.noviBrojStavke_offline(gks); 
              fNalozi.prepareForSaveStavka(gks); //DATUMKNJ, GODMJ, RBS
              gks.setString("BROJKONTA", hr.restart.gk.raKnjizenje.getBrojKonta(sks_details)); //brojkonta
              String[] commonCpCols = {"CORG","DATDOSP","BROJDOK","VRDOK","CPAR","EXTBRDOK","CNACPL","ID","IP"};
              com.borland.dx.dataset.ReadWriteRow.copyTo(commonCpCols, sks_details, commonCpCols, gks);
              gks.setTimestamp("DATDOK",datdok);
              gks.setString("OPIS", opis);
              gks.setInt("RBSRAC", gks.getInt("RBS"));
              sumID = sumID.add(gks.getBigDecimal("ID"));
              sumIP = sumIP.add(gks.getBigDecimal("IP"));
              sks_details.setString("CGKSTAVKE", cnaloga.concat(CNDELIM+gks.getInt("RBS")));
              sks_details.post();
              gks.post();
            }
            raTransaction.saveChanges(sks_details);
          }
          if (sumID.compareTo(sumIP) != 0) {
            System.out.println("sumID != sumIP !!!!!!!!!!!!!!");
            return false;
          }
          fNalozi.updNalog(sumID.doubleValue(), sumIP.doubleValue(), false);
          raTransaction.saveChanges(gks);
          return true;
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }
    };
    if (trans.execTransaction()) {
      fNalozi.setStatus();
      fNalozi.getMasterSet().saveChanges();
    } else {
      //A kaj sad da mu radim ?
    }
    
    // kopirati iz skstavkerad u gkstavkerad:
    // podesiti rbs-ove da pasu u nalog, napunlici header, azurirati skstavkerad.cgkstavke + " / " + rbs
    // updNalog(sum(detailSet.ID),sum(detailSet(IP)),false <-NE SMIJE BITI U DEBALANSU!!!!!!)
  }
  
  public static BigDecimal deletedIznos;
  
  /** brise sve skstavkerad i gkstavkerad koje se odnose na racun u kojem je
   * odabrana stavka. Poziva se iz frmNalozi.doWithSaveDetail()
   * @param fn - instanca frmNalozi iz koje se poziva
   * @return jel uspio ili nije
   */  
  public static boolean deleteWithStavkaGK(frmNalozi fn) {
    checkParam();
    if (!isGKSK) return true;    
    if (lastCgkstav==null) return true;
    QueryDataSet sks_delete = getSks_query(lastCgkstav, false, true);
    sks_delete.open();
    sks_delete.first();
    hr.restart.baza.Condition csk = hr.restart.baza.Condition.whereAllEqual(sks_key, sks_delete);
    hr.restart.baza.Condition cgk = hr.restart.baza.Condition.whereAllEqual(gks_key, fn.getMasterSet());
    String q1 = "SELECT * FROM gkstavkerad WHERE "+csk+" AND "+cgk;
    System.out.println("qry = "+q1);
    QueryDataSet gks_delete = ut.getNewQueryDataSet(q1);
    gks_delete.open();
    deletedGKSCount = gks_delete.getRowCount()+1;//+ona jedna koju je standardno obrisao
///GLUPANE JOS MORAS SELEKTIRATI OSTALE SKSTAVKE RACUNA
    String q2 = "SELECT * FROM skstavkerad WHERE "+csk;
    QueryDataSet sks_delete_racun = ut.getNewQueryDataSet(q2);
    deletedIznos = Aus.zero2;
    for (sks_delete_racun.first();sks_delete_racun.inBounds();sks_delete_racun.next()) {
      deletedIznos = deletedIznos.add(sks_delete_racun.getBigDecimal("ID"));
    }
System.out.println("deletedIznos = "+deletedIznos);
   
    sks_delete_racun.deleteAllRows();
    gks_delete.deleteAllRows();
    raTransaction.saveChanges(sks_delete_racun);
    raTransaction.saveChanges(gks_delete);
    return true;
  }
  
    /** brise sve skstavkerad i gkstavkerad koje se odnose na racune u 
   * odabranom nalogu. Poziva se iz frmNalozi.doWithSaveMaster()
   * @param fn - instanca frmNalozi iz koje se poziva
   * @return jel uspio ili nije
   */  
  public static boolean deleteWithNalogGK(frmNalozi fn) {
    checkParam();
    if (!isGKSK) return true;
    if (lastCgkstav==null) return true;
    QueryDataSet sks_delete = getSks_query(lastCgkstav, false, false);
    sks_delete.open();
    
System.out.println("deleteWithNalogGK************************************************");
System.out.println(sks_delete.getQuery().getQueryString());
ST.prn(sks_delete);

    sks_delete.deleteAllRows();
    raTransaction.saveChanges(sks_delete);
    return true;
  }
  
  /** Obavjestava korisnika da ce obrisati vise stavaka nego to on ocekuje
   * Poziva se iz frmNalozi.DeleteCheckDetail()
   * @param fn - instanca frmNalozi iz koje se poziva
   * @return poruku ili null ako nema stavaka vezanih uz sk
   */  
  public static String getDeleteWithStavkaGKWarning(frmNalozi fn) {
    checkParam();
    if (!isGKSK) return null;    
    //throw new UnsupportedOperationException("Not yet implemented");
    String cgkstav = fn.getMasterSet().getString("CNALOGA")+CNDELIM+fn.getDetailSet().getInt("RBSRAC");
//    String qry = "SELECT count(*) FROM skstavkerad WHERE "+Condition.where("CGKSTAVKE",Condition.EQUAL,cgkstav);
//    System.out.println("sks_count = "+qry);
    QueryDataSet sks_count = getSks_query(cgkstav, true, true);
    sks_count.open();
    if (Valid.getValid().getSetCount(sks_count, 0) > 0) {
      return "Želite li obrisati sve stavke vezane uz raèun "+fn.getDetailSet().getString("BROJDOK");
    } else {
      return null;
    }
  }
  public static int deletedGKSCount = 0;
  private static QueryDataSet getSks_query(String cgkstav, boolean count, boolean detail) {
    String qry;
    if (count) {
      qry="SELECT count(*) FROM skstavkerad WHERE ";
      lastCgkstav = cgkstav;

System.out.println("lastCgkstav = "+lastCgkstav);
    } else {
      qry="SELECT * FROM skstavkerad WHERE ";
      lastCgkstav = null;
    }
    if (detail) qry = qry+Condition.where("CGKSTAVKE",Condition.EQUAL,cgkstav);
    else qry = qry+"CGKSTAVKE LIKE '"+cgkstav+"%'";
    return ut.getNewQueryDataSet(qry);
  }
  /** Obavjestava korisnika da ce obrisati vise stavaka nego to on ocekuje pri
   * brisanju naloga
   * Poziva se iz frmNalozi.DeleteCheckMaster()
   * @param fn - instanca frmNalozi iz koje se poziva
   * @return poruku ili null ako nema stavaka vezanih uz sk
   */  
  public static String getDeleteWithNalogGKWarning(frmNalozi fn) {
    checkParam();
    if (!isGKSK) return null;
    String cgkstav = fn.getMasterSet().getString("CNALOGA")+CNDELIM;
    QueryDataSet sks_count = getSks_query(cgkstav, true, false);
    sks_count.open();
    int g = 0;
    if ((g = Valid.getValid().getSetCount(sks_count, 0)) > 0) {
      System.out.println("count "+g);
      return "Brisanjem naloga, obrisat æe se i stavke naloga , te vezani dokumenti SK! Nastaviti?";
    } else {
      lastCgkstav = null;
      return null;
    }
  }
  
  /** Na izmjenu u frmNalozi.raDetail samo ponuditi konto i oj, a off-ati iznose.
   * ako se konto promijenio, pronaci pripadajucu skstavkurad i promijeniti joj
   * oznake sheme da pasu uz konto, ako sheme nema dodati je u ostalo?
   * @param cgkstav kljuc za pronalazak stavkeskrad
   * @return jel uspio
   */  
  public static boolean updateWithStavkaGK(String cgkstav) {
    checkParam();
    if (!isGKSK) return true;

    throw new UnsupportedOperationException("Not yet implemented");
  }
  
  /** Arhivira sve skstavkerad koji imaju cgkstavke startswith cnaloga
   * i prebacuje u skstavke i uistavke. Zahvaca skstavkerad i poziva metode
   * arhivhiranja. Po mogucnosti u transakciji od obrade naloga
   * @param cnaloga oznaka naloga koji je u obradi
   * @return jel uspjelo ili nije
   */  
  public static boolean doWithObradaNaloga(String cnaloga) {
    checkParam();
    if (!isGKSK) return true;
    throw new UnsupportedOperationException("Not yet implemented");
  }
  
  
  
  private void fakePreselect(PreSelect pres) {
    presSalKon presk = (presSalKon)pres;

    fSalKon.setPreSelect(presk);
    
    presk.setSelDataSet(skMasterSet);

    JPanel selpanel = presk.getSelPanel();
    if (jtCGKSTAVKE == null) {
      jtCGKSTAVKE = new JraTextField();
      jtCGKSTAVKE.setColumnName("CGKSTAVKE");
    }
    if (!SwingUtilities.isDescendingFrom(jtCGKSTAVKE, selpanel)) {
      System.out.println("adding jtcGKSTAVKE to selpanel....");
      selpanel.add(jtCGKSTAVKE);
    }
    jtCGKSTAVKE.setVisible(false);
    jtCGKSTAVKE.setEnabled(false);

    presk.jpDetail.remove(presk.jlrCknjige);
    
    presk.setSelPanel(selpanel);
    if (isIra) {
      presk.bg1.setSelected(presk.jrbUraira2);
    } else {
      presk.bg1.setSelected(presk.jrbUraira1);
    }
    presk.getSelRow().setString("KNJIG",fNalozi.getMasterSet().getString("KNJIG"));
    //presk.getSelRow().setString("CKNJIGE",""); 
    presk.getSelRow().setInt("RBS", 1);
    presk.getSelRow().setString("VRDOK", vrdok);
    presk.getSelRow().setString("CGKSTAVKE",cnaloga);
    presk.getSelRow().setTimestamp("DATPRI-from",new Timestamp(0));
    presk.getSelRow().setTimestamp("DATPRI-to",ut.getLastDayOfYear());
    presk.doSelect();
    presk.getSelRow().addColumn("CKNJIGE",com.borland.dx.dataset.Variant.STRING);
    presk.getSelRow().setString("CKNJIGE",calcKnjiga());

  }
  
  private boolean checkBalansSK() {
    BigDecimal saldo = new BigDecimal(0);

    for (fSalKon.getDetailSet().first(); fSalKon.getDetailSet().inBounds(); fSalKon.getDetailSet().next())
      saldo = saldo.add(fSalKon.getDetailSet().getBigDecimal("ID")).subtract(fSalKon.getDetailSet().getBigDecimal("IP"));
    
    if (ut.setScale(saldo,2).compareTo(Aus.zero2) != 0) {
      JOptionPane.showMessageDialog(fSalKon,"U ovom modu rada raèun mora biti u balansu","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  private String calcKnjiga() {
    qdsknjigeui.open();
    if (!lookupData.getlookupData().raLocate(
    qdsknjigeui, new String[] {"CSKL","VRDOK"}, new String[] {csheme, vrdok})) return null;
    return qdsknjigeui.getString("CKNJIGE");
  }
  private boolean setCKNJIGE() {
    String cknj = calcKnjiga();
    if (cknj == null) return false;
    skMasterSet.setString("CKNJIGE",cknj);
    return true;
  }
  private void focusMaster(char mode) {
System.out.println("focusMaster mode = "+mode);
System.out.println("prviput = "+prviput);
    if (mode != 'N') return;
    if (!prviput) return;
    skMasterSet.setTimestamp("DATDOK",datdok);
    skMasterSet.setString("OPIS",opis);
    skMasterSet.setBigDecimal(fSalKon.jpMaster.jraIznos.getColumnName(),iznos);
    skMasterSet.setString("CSKL", csheme);
    raCommonClass rcc = raCommonClass.getraCommonClass();
    rcc.setLabelLaF(fSalKon.jpMaster.jlrCknjige, true);
    rcc.setLabelLaF(fSalKon.jpMaster.jlrNazknjige, true);
    rcc.setLabelLaF(fSalKon.jpMaster.jbSelCknjige, true);

/*    if (setCKNJIGE()) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
System.out.println("fSalKon.jpMaster.jlrCpar.requestFocus()");
          fSalKon.jpMaster.jlrCpar.requestFocus();
        }
      });
    }*/
    prviput = false;
  }
  
  /** constructs this class */  
  public static class salkonSK extends frmSalKon {
    
    private raGkSkUnosHandler handler;

    /** setira raGkSkUnosHandler iz kojeg se pozivaju metode
     * @param h raGkSkUnosHandler iz kojeg se pozivaju metode
     */    
    public void setHandler(raGkSkUnosHandler h) {
      handler = h;
    }

    /** overridana metoda iz raMasterDetail
     * @param mode overridana metoda iz raMasterDetail
     */    
    public void SetFokusMaster(char mode) {
      super.SetFokusMaster(mode);
      handler.focusMaster(mode);
    }
    /** overridana metoda iz raMasterDetail */    
    public void ZatvoriOstaloMaster() {
      handler.afterCloseSalKon();
    }
    
    /** overridana metoda iz raMasterDetail
     * @return overridana metoda iz raMasterDetail
     */    
    public boolean ValidacijaPrijeIzlazaDetail() {
      if (super.ValidacijaPrijeIzlazaDetail()) {
        return handler.checkBalansSK();
      } else return false;
    }
  }
  
  /** constructs this class */  
  public static class salkonOK extends frmSalKonOK {

    private raGkSkUnosHandler handler;
    
    /** setira raGkSkUnosHandler iz kojeg se pozivaju metode
     * @param h raGkSkUnosHandler iz kojeg se pozivaju metode
     */    
    public void setHandler(raGkSkUnosHandler h) {
      handler = h;
    }
    
    /** overridana metoda iz raMasterDetail
     * @param mode overridana metoda iz raMasterDetail
     */    
    public void SetFokusMaster(char mode) {
      super.SetFokusMaster(mode);
      handler.focusMaster(mode);
    }

    /** overridana metoda iz raMasterDetail */    
    public void ZatvoriOstaloMaster() {
      handler.afterCloseSalKon();
    }
    
    /** overridana metoda iz raMasterDetail
     * @return overridana metoda iz raMasterDetail
     */    
    public boolean ValidacijaPrijeIzlazaDetail() {
      if (super.ValidacijaPrijeIzlazaDetail()) {
        return handler.checkBalansSK();
      } else return false;
    }

  }
  
}
