/****license*****************************************************************
**   file: raGKKontrole.java
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
package hr.restart.gk;
import hr.restart.baza.Condition;
import hr.restart.baza.Gkkumulativi;
import hr.restart.baza.Gkstavke;
import hr.restart.baza.Gkstavkerad;
import hr.restart.baza.Izvodi;
import hr.restart.baza.Konta;
import hr.restart.baza.Nalozi;
import hr.restart.baza.Skstavke;
import hr.restart.baza.Vrstenaloga;
import hr.restart.baza.dM;
import hr.restart.baza.zirorn;
import hr.restart.util.IntParam;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.statusHandler;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Klasa u kojoj se nalaze staticke metode koje rade razne kontrole na tablicama
 * glavne knjige i vezane uz nju
 */
public class raGKKontrole {
  static frmGKKontrole frmKontrole;
  /**
   * Tweak da li da kontrolira kumulative na nalogu, u protivnom kontrolira
   * kumulative gkkumulativi
   */  
  public static boolean checkNalog = false;
  /**
   * Tweak da li da kontrolira kumulative na izvodu, u protivnom kontrolira
   * kumulative gkkumulativi ili kumulative na nalogu. Ako su checkNalog i 
   * checkIzvod true kontrolira samo naloge
   */  
  public static boolean checkIzvod = false;

  /**
   * Nije potrebno instancirati, sve metode su staticke
   */  
  protected raGKKontrole() {
  }
  private static boolean getCheckNalog() {
    if (checkNalog && checkIzvod) {
      checkIzvod = false;
    }
    return checkNalog;
  }
  private static boolean getCheckIzvod() {
    if (checkNalog && checkIzvod) {
      checkIzvod = false;
    }
    return checkIzvod;
  }

  /**
   * Kreira StorageDataSet za greske
   * @param kontriznos da li da ukljuci kontrolni iznos (vidi checkNalog)
   * @return StorageDataSet za greske
   */  
  public static StorageDataSet createCheckErrors(boolean kontriznos) {
    StorageDataSet chkErr = new StorageDataSet();
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    Column[] columns;
    Column colIznos = dm.getGkstavke().getColumn("ID");

    Column colCKEY = new Column();
    colCKEY.setColumnName("CKEY");
    colCKEY.setCaption("Klju\u010D");
    colCKEY.setDataType(Variant.STRING);
    colCKEY.setPrecision(50);

    Column colID = (Column)colIznos.clone();

    Column colIP = (Column)colIznos.clone();
    colIP.setColumnName("IP");
    colIP.setCaption("Iznos potražuje");

    Column colNEWID = (Column)colIznos.clone();
    colNEWID.setColumnName("NEWID");
    colNEWID.setCaption("Ispravan iznos duguje");

    Column colNEWIP = (Column)colIznos.clone();
    colNEWIP.setColumnName("NEWIP");
    colNEWIP.setCaption("Ispravan iznos potrazuje");
   
    Column colACTION = new Column();
    colACTION.setColumnName("ACTION");
    colACTION.setCaption("Akcija");
    colACTION.setDataType(Variant.STRING);
    colACTION.setPrecision(1);

    if (kontriznos) {
      Column colKONTRIZNOS = (Column)dm.getNalozi().getColumn("KONTRIZNOS").clone();

      Column colNEWKONTRIZNOS = (Column)colKONTRIZNOS.clone();
      colNEWKONTRIZNOS.setColumnName("NEWKONTRIZNOS");
      colNEWKONTRIZNOS.setCaption("Ispravan kontrolni iznos");

      columns = new Column[] {colCKEY,colID,colIP,colNEWID,colNEWIP,colKONTRIZNOS,colNEWKONTRIZNOS,colACTION};
    } else {
      columns = new Column[] {colCKEY,colID,colIP,colNEWID,colNEWIP,colACTION};
    }
    chkErr.setColumns(columns);
    chkErr.open();
    return chkErr;
  }
  /**
   * Vraca gkkumulative bez pocetnog stanja, ili naloge ovisno  o checknalog
   * @return gkkumulative bez pocetnog stanja, ili naloge ovisno  o checknalog
   */  
  public static QueryDataSet getKumul() {
    if (getCheckNalog()) {
      return Util.getNewQueryDataSet("SELECT * FROM Nalozi where STATUS in ('K','S')");
    } else if (getCheckIzvod()) {
      return Util.getNewQueryDataSet("SELECT * FROM Izvodi where STATUS in ('K','S')");
    } else {
      return gkQuerys.getNewQueryDataSet(
          "SELECT * FROM GKKUMULATIVI WHERE KNJIG='"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
          "' and GODMJ not like '____00'"
      );
    }
  }
  private static int getKumulCount() {
    Valid vl = Valid.getValid();
    if (getCheckNalog()) {
      vl.execSQL("SELECT count(*) FROM Nalozi where STATUS in ('K','S')");
    } else if (getCheckIzvod()) {
      vl.execSQL("SELECT count(*) FROM Izvodi where STATUS in ('K','S')");
    } else {
      vl.execSQL(
          "SELECT count(*) FROM GKKUMULATIVI WHERE KNJIG='"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
          "' and GODMJ not like '____00'"
      );
    }
    return vl.getSetCount(vl.RezSet,0);
  }
  static String lastGodmj = "";
  /**
   * Provjera kumulativa osim poc stanja u gkkumulativi ili u nalozi ovisno o
   * checkNalog
   * @param errorsSet errorsSet = createCheckErrors(false);
   * @return napunjeni errorsSet
   */
  public static StorageDataSet checkKumul(StorageDataSet errorsSet) {
    QueryDataSet allKumul = getKumul();
    kumstav = null;
    recur = false;
    if (allKumul.getRowCount() > 0) {
      if (!getCheckNalog() && !getCheckIzvod())
        allKumul.setSort(new SortDescriptor(new String[] {"GODMJ"}));
      allKumul.first();
      lastGodmj = "";
      do {
        nexStepUI("Provjera kumulativa "+getKumulKeyUI(allKumul)+"...");
        checkKumul(allKumul,errorsSet);
      } while (allKumul.next());
      checkNonExistantKums(allKumul, errorsSet);
    }
    kumstav = null;
    return errorsSet;
  }
  private static String getKumulKeyUI(ReadRow allKumul) {
    if (getCheckNalog()) {
      return allKumul.getString("CNALOGA");
    } else if (getCheckIzvod()) {
      return allKumul.getString("ZIRO")+"-"+allKumul.getInt("BROJIZV");
    } else {
      return allKumul.getString("CORG")+"-"+allKumul.getString("BROJKONTA")+"-"+allKumul.getString("GODMJ");
    }
  }
  private static boolean jednak(BigDecimal bd1, BigDecimal bd2) {
    return bd1.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(bd2.setScale(2,BigDecimal.ROUND_HALF_UP)) == 0;
  }
  
  private static boolean isZiroPromet(String ziro) {
    QueryDataSet zrn = dM.getDataModule().getAllZirorn();
    lookupData.getlookupData().raLocate(zrn, "ZIRO",ziro);
    if (IntParam.getTag("zrtweak").equals("D")) {
      return false;
    }
    return zrn.getString("PROMET").equals("D");
  }
  static QueryDataSet kumstav;
  static boolean recur;
  static void checkNonExistantKums(QueryDataSet kumul,StorageDataSet errors) {
    if (kumstav != null && kumstav.rowCount() > 0) {
      recur = true;
      try {
        QueryDataSet dummyKum = Gkkumulativi.getDataModule().getTempSet("1=0");
        dummyKum.open();
        dummyKum.insertRow(false);
        dummyKum.setString("KNJIG", kumul.getString("KNJIG"));
        dummyKum.setString("GODMJ", lastGodmj);
        for (kumstav.first(); kumstav.inBounds(); kumstav.first()) {
          dummyKum.setString("BROJKONTA", kumstav.getString("BROJKONTA"));
          dummyKum.setString("CORG", kumstav.getString("CORG"));
          checkKumul(dummyKum, errors);
        }
      } finally {
        recur = false;
      }
    }
  }
  
  
  private static void checkKumul(QueryDataSet kumul,StorageDataSet errors) {
    Valid vl = Valid.getValid();
    QueryDataSet stavsum;
    boolean empty = false;
    if (getCheckNalog()) {
      stavsum = gkQuerys.getKumulGKStavkeNal(kumul);
    } else if (getCheckIzvod()) {
      QueryDataSet _nalog = Nalozi.getDataModule().getTempSet(Condition.equal("CNALOGA",kumul));
      _nalog.open();
      if (_nalog.getRowCount() == 0) throw new RuntimeException("GRRRESKA!! Nema naloga od izvoda "+getKumulKeyUI(kumul));
      stavsum = gkQuerys.getKumulGKStavkeNal(_nalog);
    } else {
      if (!lastGodmj.equals(kumul.getString("GODMJ"))) {
        checkNonExistantKums(kumul, errors);
        lastGodmj = kumul.getString("GODMJ");
        kumstav = gkQuerys.getKumulStavkeQuery(kumul, true);
      }
      stavsum = kumstav;
      empty = !lookupData.getlookupData().raLocate(stavsum, new String[] {"CORG", "BROJKONTA"},
          new String[] {kumul.getString("CORG"), kumul.getString("BROJKONTA")});
    }
    int cnt = vl.getSetCount(stavsum,2);
    String action = "";
    if (cnt == 0 || empty) {
      action = "D"; //delete
    } else if (!getCheckIzvod() && !(jednak(kumul.getBigDecimal("ID"),stavsum.getBigDecimal("SUMID"))
               && jednak(kumul.getBigDecimal("IP"),stavsum.getBigDecimal("SUMIP")))) {
      action = recur ? "A" : "U"; //update
      
    }
    if (getCheckNalog()) {
      if (!jednak(stavsum.getBigDecimal("SUMID"),kumul.getBigDecimal("KONTRIZNOS"))) {
        action = "U";
      }
    }
    if (getCheckIzvod()) {
      if (kumul.getString("STATUS").equals("S") && isZiroPromet(kumul.getString("ZIRO"))) {
        if (!(jednak(kumul.getBigDecimal("ID"),stavsum.getBigDecimal("SUMID"))
               && jednak(kumul.getBigDecimal("IP"),stavsum.getBigDecimal("SUMIP")))) {
          action = "U";
        }
      } else {//ista kontrola i za neproknjizene bez prometa i za proknjizene sa prometom
        if (!(jednak(kumul.getBigDecimal("ID").add(kumul.getBigDecimal("IP")),stavsum.getBigDecimal("SUMID"))
               && jednak(kumul.getBigDecimal("ID").add(kumul.getBigDecimal("IP")),stavsum.getBigDecimal("SUMIP")))) {
          action = "U";
        }
      }
    }
    if (!action.equals("")) {
      errors.insertRow(false);
      if (getCheckNalog() || getCheckIzvod()) {
        errors.setString("CKEY",kumul.getString("CNALOGA"));
      } else {
        errors.setString("CKEY",kumul.getString("CORG")+"-"+kumul.getString("BROJKONTA")+"-"+kumul.getString("GODMJ"));
/*                        kumul.getString("CORG").concat("-")
                        .concat(kumul.getString("BROJKONTA").concat("-")
                        .concat(kumul.getString("GODMJ"))
                      ));*/
      }
      if (action.equals("U") || action.equals("A")) {
        errors.setBigDecimal("ID",kumul.getBigDecimal("ID"));
        errors.setBigDecimal("IP",kumul.getBigDecimal("IP"));
        errors.setBigDecimal("NEWID",stavsum.getBigDecimal("SUMID"));
        errors.setBigDecimal("NEWIP",stavsum.getBigDecimal("SUMIP"));
        if (getCheckNalog()) {
          errors.setBigDecimal("KONTRIZNOS",kumul.getBigDecimal("KONTRIZNOS"));
          errors.setBigDecimal("NEWKONTRIZNOS",stavsum.getBigDecimal("SUMID"));
        }
      } else {
        java.math.BigDecimal nula = new java.math.BigDecimal(0);
        errors.setBigDecimal("ID",kumul.getBigDecimal("ID"));
        errors.setBigDecimal("IP",kumul.getBigDecimal("IP"));
        errors.setBigDecimal("NEWID",nula);
        errors.setBigDecimal("NEWIP",nula);
        if (getCheckNalog()) {
          errors.setBigDecimal("KONTRIZNOS",nula);
          errors.setBigDecimal("NEWKONTRIZNOS",nula);
        }
      }
      errors.setString("ACTION",action);
      errors.post();
    }
    if (!empty && kumstav != null) kumstav.emptyRow();
  }
  private static boolean locateKumul(String ckey, QueryDataSet kumul) {
    boolean loc;
    if (getCheckNalog() || getCheckIzvod()) {
      loc = lookupData.getlookupData().raLocate(kumul, "CNALOGA", ckey);
    } else {
      int _1; //ovo bi mogao i sa StringTokenizer-om, but don't fix if ain't broken
      String corg = ckey.substring(0,_1 = ckey.indexOf("-"));
      ckey = ckey.substring(_1+1);
      String brojkonta = ckey.substring(0,_1 = ckey.indexOf("-"));
      ckey = ckey.substring(_1+1);
      String godmj = ckey.substring(0,ckey.length());
//System.out.println("corg = "+corg);
//System.out.println("brojkonta = "+brojkonta);
//System.out.println("godmj = "+godmj);
      loc = lookupData.getlookupData().raLocate(kumul,new String[] {"CORG","BROJKONTA","GODMJ"},new String[] {corg,brojkonta,godmj});
    }
    return loc;
  }

  /**
   * Popravlja kumulative
   * @param errors StorageDataset za greske (createCheckErrors)
   */  
  public static void repairKumul(StorageDataSet errors) {
    if (getCheckIzvod()) {
      JOptionPane.showMessageDialog(null, "Vrši se popravak samo proknjiženih izvoda!");
//      return;
    }
    lookupData ld = lookupData.getlookupData();
    QueryDataSet kumul = getKumul();
    errors.open();
    errors.first();
    if (errors.getRowCount() == 0) return;
    do {
      String ckey = errors.getString("CKEY");
      nexStepUI("Popravak "+(getCheckNalog()?"naloga ":getCheckIzvod()?"izvoda":"kumulativa ")+ckey);
      String action = errors.getString("ACTION");
      if (locateKumul(ckey, kumul)) {
        if (!(getCheckIzvod() && !kumul.getString("STATUS").equals("K"))) {
          if (action.equals("U")) {
            kumul.setBigDecimal("ID",errors.getBigDecimal("NEWID"));
            kumul.setBigDecimal("IP",errors.getBigDecimal("NEWIP"));
            if (getCheckNalog()) kumul.setBigDecimal("KONTRIZNOS", errors.getBigDecimal("NEWKONTRIZNOS"));
            errors.setString("ACTION","X");
            kumul.post();
            errors.post();
          } else if (action.equals("D")) {
            if (!getCheckNalog()) kumul.deleteRow();
          }
        }
      } else {
        if (action.equals("A")) {
          /** @todo ako je pronasao stavke ali ne i kumulative */
          kumul.insertRow(false);
          kumul.setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
          int pos;
          kumul.setString("CORG", ckey.substring(0, pos = ckey.indexOf("-")));
          ckey = ckey.substring(pos + 1);
          kumul.setString("BROJKONTA", ckey.substring(0, pos = ckey.indexOf("-")));
          kumul.setString("GODMJ", ckey.substring(pos + 1));
          
          kumul.setBigDecimal("ID",errors.getBigDecimal("NEWID"));
          kumul.setBigDecimal("IP",errors.getBigDecimal("NEWIP"));
          kumul.post();
        }
      }
    } while (errors.next());
    nexStepUI("Brisanje podataka za provjeru...");
    RowFilterListener xFilter = new RowFilterListener() {
      public void filterRow(ReadRow row, RowFilterResponse response) {
        if (row.getString("ACTION").equals("X")) {
          response.add();
        } else {
          response.ignore();
        }
      }
    };
    try {
      errors.addRowFilterListener(xFilter);
    }
    catch (Exception ex) {

    }
    errors.emptyAllRows();
    errors.removeRowFilterListener(xFilter);
    nexStepUI("Snimanje promjena...");
kumul.first();
for (kumul.first(); kumul.inBounds(); kumul.next()) System.out.println(kumul);
    kumul.saveChanges();
  }

/*
.
.
.
.
.
*/
  /**
   * Inicijalizira pokazatelj napretka operacije kontrole kumulativa
   * @param frmKont klasa tipa frmGKKontrole
   */  
  public static void initCheckKumulUI(frmGKKontrole frmKont) {
    frmKontrole = frmKont;
    initProgressUI("Provjera kumulativa prometa ...",getKumulCount());
  }
  /**
   * Inicijalizira pokazatelj napretka operacije popravka kumulativa
   * @param errors tablica sa greskama
   * @see createCheckErrors(boolean)
   */  
  public static void initRepairKumulUI(StorageDataSet errors) {
    initProgressUI("Popravak kumulativa prometa ...",errors.getRowCount()+2);
  }
  private static void nexStepUI(String msg) {
    if (frmKontrole == null) return;
    if (frmKontrole.status == null) return;
    System.out.println("raGKKontrole :: "+msg);
    frmKontrole.status.next(msg);
  }
  private static void initProgressUI(String msg, int steps) {
    if (frmKontrole == null) return;
    frmKontrole.msg = msg;
    frmKontrole.steps = steps;
  }
  /**
   * Provjerava i puni slogove ziropar u odnosu na tablicu partneri
   */
  public static void checkZiroPar() {
    statusHandler status = new statusHandler();
    status.startTask(100,"Provjera i popravak žiro ra\u010Duna partnera");
    Valid vl = Valid.getValid();
    String domVal = hr.restart.zapod.Tecajevi.getDomOZNVAL();
    vl.runSQL(
      "INSERT INTO ZIROPAR "+
       "SELECT CPAR,ZR,'N','"+domVal+"' "+
       "FROM PARTNERI WHERE CPAR NOT IN (SELECT CPAR FROM ZIROPAR)"
    );
    status.finnishTask("Provjera i popravak žiro ra\u010Duna partnera završena!");
  }
  static String reportString = "";
  /**
   * Mijenja vrdok shodno globalnom parametru temURNIRN
   */  
  public static void changeVRDOKfrmParam(boolean sk) {
    final String sCVRNAL = JOptionPane.showInputDialog(null, "Oznaka vrste naloga");
    if (sCVRNAL == null) return;
    QueryDataSet vrstanal = hr.restart.baza.Vrstenaloga.getDataModule().getTempSet(hr.restart.baza.Condition.equal("CVRNAL",sCVRNAL));
    vrstanal.open();
    if (vrstanal.getRowCount() == 0 && !(sCVRNAL.equals("") && sk)) {
      JOptionPane.showMessageDialog(null, "Ne postoji vrsta naloga "+sCVRNAL+"!","GREŠKA",JOptionPane.ERROR_MESSAGE);
      return;
    }
    String confirmMsg = "Promijeniti vrste dokumenata kod "+(sk?"PROKNJIžENIH":"neproknjiženih")+" temeljnica ";
    if (sCVRNAL.equals("")) {
      confirmMsg = confirmMsg + "SVIH STAVAKA unesenih kroz temeljnice ";
    } else {
      confirmMsg = confirmMsg + "vrste "+sCVRNAL+" "+vrstanal.getString("OPISVRNAL");
    }   
    confirmMsg = confirmMsg + " sukladno s vrijednosti sistemskog parametra temURNIRN ?";
    int ret = JOptionPane.showConfirmDialog(null, confirmMsg);
    if (ret == 0) {
      raProcess.runChild(getChangeVRDOKfrmParamRunnable(sk, sCVRNAL));
      raProcess.report(reportString);
      /*if (sk) {
        JOptionPane.showMessageDialog(null, "Nastavljam sa kontrolom kumulativa SK. Obavezno pustite ispravak!!!", "Važno upozorenje",JOptionPane.WARNING_MESSAGE);
        hr.restart.sk.raSaldaKonti.checkKumulativ();
      }*/
    }
  }
  private static Condition commonChangeVRDOKfrmParamCondition = Condition.in("VRDOK",new String[] {"OKD","OKK"});
  private static Runnable getChangeVRDOKfrmParamRunnable(boolean sk, final String sCVRNAL) {
    Runnable ret;
    if (sk) {
      if (sCVRNAL.equals("")) {
        ret = new Runnable() {
           public void run() {
            raProcess.setMessage("Dohvat stavaka salda konti ...",true);
            QueryDataSet sks = Skstavke.getDataModule()
              .getTempSet("PVSALDO=PVSSALDO AND BROJIZV=0 and CKNJIGE IS null and "+commonChangeVRDOKfrmParamCondition);
            sks.open();
            if (sks.getRowCount() == 0) {
              reportString = "Nema nepokrivenih stavaka SK unesenih kroz temeljnicu!";
            }
            int cnt = 1;
            determineAndChangeVrdokForDataSet(sks, cnt, true, true);
            reportString = "Uspješno provjereno / ažurirano "+cnt+" stavaka !";
           }
        };
      } else {
        ret = new Runnable() {
          public void run() {
            raProcess.setMessage("Dohvat naloga ...",true);
            QueryDataSet nals = Nalozi.getDataModule().getTempSet(Condition.equal("CVRNAL",sCVRNAL));
            nals.open();
            if (nals.getRowCount() == 0) {
              reportString = "Nema naloga - temeljnica za zadanu vrstu naloga !";
            }
            int cnt = 1;
            for (nals.first(); nals.inBounds(); nals.next()) {
              String cnaloga = jpBrojNaloga.getCNaloga(nals);
              raProcess.setMessage("Dohvat stavaka SK vezanih uz nalog "+cnaloga+" ...",true);
              QueryDataSet sks = Skstavke.getDataModule().getTempSet(
                    " PVSALDO=PVSSALDO AND CGKSTAVKE LIKE '"+cnaloga+"%' AND "+commonChangeVRDOKfrmParamCondition);
              determineAndChangeVrdokForDataSet(sks, cnt, true, true);
            }
            reportString = "Uspješno provjereno / ažurirano "+cnt+" stavaka !";
          }
        };
      }
    } else {
      ret = new Runnable() {
        public void run() {
          raProcess.setMessage("Dohvat neproknjiženih stavki ...",true);
          QueryDataSet gksrad = Gkstavkerad.getDataModule().getTempSet(Condition.equal("CVRNAL",sCVRNAL)+" AND "+commonChangeVRDOKfrmParamCondition);
          gksrad.open();
          if (gksrad.getRowCount() == 0) {
            reportString = "Nema neproknjiženih stavaka za zadanu vrstu naloga !";
          }
          int cnt = 1;
          determineAndChangeVrdokForDataSet(gksrad, cnt, true, false);
          reportString = "Uspješno provjereno / ažurirano "+cnt+" stavaka !";
          
        }
      };
    }
    return ret;
  }
  private static int determineAndChangeVrdokForDataSet(DataSet ds, int cnt, boolean save, boolean sk) {
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next()) {
      String oldvrdok = ds.getString("VRDOK");
      String newvrdok = frmNalozi.determineVrdok(ds);
      ds.setString("VRDOK", newvrdok);
      if (sk && !oldvrdok.equals(newvrdok)) {
        ds.setInt("BROJIZV", 10101010);//da ne pukne na unique slucajno
        if (ds.hasColumn("CSKSTAVKE")!=null) {
          ds.setString("CSKSTAVKE", hr.restart.sk.raSaldaKonti.findCSK(ds));
        }
      }
      raProcess.setMessage("Izmjena vrste dokumenta ..."+cnt,true);
      cnt++;
    }
    if (save) {
      raProcess.setMessage("Snimanje promjena ...",true);
      ds.saveChanges();
    }
    return cnt;
  }
  private static Condition getConditionForVrdok(ReadRow ds) {
    QueryDataSet izvod = Izvodi.getDataModule().getTempSet(Condition.equal("CNALOGA",jpBrojNaloga.getCNaloga(ds)));
    izvod.open();
    if (izvod.getRowCount() > 0) {
    /*
VRDO BROJKONTA               ID               IP 
UPL  120000                0.00           100.00 
OKK  120000                0.00          1550.00 
IPL  220000              100.00             0.00 
OKD  220000              100.00             0.00 
     */
      if (hr.restart.zapod.raKonta.isKupac(ds.getString("BROJKONTA"))) {
        if (ds.getBigDecimal("IP").signum() != 0) return Condition.in("VRDOK",new String[] {"UPL","OKK"});
      } else {
        if (ds.getBigDecimal("ID").signum() != 0) return Condition.in("VRDOK",new String[] {"IPL","OKD"});
      }
    }
    return Condition.equal("VRDOK",frmNalozi.determineVrdok(ds));
  }
  public static void dodajRBSnaCGKSTAVKE() {
    raProcess.runChild(new Runnable() {
      public void run() {
        String[] gkskeys= {"KNJIG","GOD","CVRNAL","RBR"};
        QueryDataSet nalozi = Nalozi.getDataModule().getTempSet(Condition.equal("STATUS","K"));
        QueryDataSet kontask = Konta.getDataModule().getTempSet(Condition.in("SALDAK",new String[]{"K","D"}));
        kontask.open();
        raProcess.openScratchDataSet(nalozi);
        for (nalozi.first(); nalozi.inBounds(); nalozi.next()) {
          String cnaloga = nalozi.getString("CNALOGA");
          QueryDataSet gkstavke = Gkstavke.getDataModule().getTempSet(
              Condition.whereAllEqual(gkskeys,nalozi)+" AND "+Condition.in("BROJKONTA",kontask));
          raProcess.openScratchDataSet(gkstavke);
          for (gkstavke.first(); gkstavke.inBounds(); gkstavke.next()) {
            
            QueryDataSet skstavka = Skstavke.getDataModule().getTempSet(
              "CGKSTAVKE like '"+cnaloga+"%' AND "+getConditionForVrdok(gkstavke)+" AND "
              +Condition.whereAllEqual(
                  new String[] {"CPAR","BROJKONTA","PVID","PVIP"},
                  new Object[] {new Integer(gkstavke.getInt("CPAR")), gkstavke.getString("BROJKONTA"), gkstavke.getBigDecimal("ID"),gkstavke.getBigDecimal("IP")}));
            raProcess.openScratchDataSet(skstavka);
            if (skstavka.getRowCount() == 1) {
              String cgkstavke = cnaloga+"-"+gkstavke.getInt("RBS");
              skstavka.first();
              if (cgkstavke.equals(skstavka.getString("CGKSTAVKE"))) {
System.out.println("CGKSTAVKE ("+cgkstavke+") is OK for "+skstavka);                
              } else {
System.out.println(":):):):)UPDATING "+skstavka.getString("CGKSTAVKE")+" -> "+cgkstavke);
                skstavka.setString("CGKSTAVKE", cgkstavke);
                skstavka.saveChanges();
              }
            } else {
System.out.println(skstavka.getRowCount()+" matches found for "+gkstavke);
            }
          }
        }
      }
    });
  }
  /**
   * Startna metoda u testne svrhe
   * @param args parametri
   */  
  public static void main(String[] args) {
//    popravi_extbrojdok();
//    hr.restart.start.STARTTIME = 0;
//    Util.redirectSystemOut();
//    iskreirajIzvode();
  }
  
  private static void popravi_extbrojdok() {
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    QueryDataSet skstavkerad = hr.restart.baza.Skstavkerad.getDataModule().getTempSet("VRDOK='URN' AND STAVKA=1");
    skstavkerad.open();
    skstavkerad.first();
    String extbrdok = "";
    do {
      try {
        extbrdok = new java.util.StringTokenizer(skstavkerad.getString("EXTBRDOK"),".").nextToken();
        int br_ur = new Integer(extbrdok).intValue();
        if (br_ur > 7) {
          br_ur = br_ur - 3;
          skstavkerad.setString("EXTBRDOK",Integer.toString(br_ur));
        }
      }
      catch (Exception ex) {
        System.out.println("nemrem intat EXTBRDOK = "+extbrdok+" u orginalu :"+skstavkerad.getString("EXTBRDOK"));
      }

    } while (skstavkerad.next());
    hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
    ST.showInFrame(skstavkerad,"Azurirane skstavkerad");
    int odg = JOptionPane.showConfirmDialog(null,"Snimiti promjene","Pitanje",JOptionPane.YES_NO_OPTION);
    if (odg == 0) {
      skstavkerad.saveChanges();
    }
  }
  /**
   *  Ovu funkciju treba nadaleko izbjegavat, napravi takvo s*r*a*nj*e da je to nevjerojatno
   */
  private static void iskreirajIzvode() {
    final String sCVRNAL = JOptionPane.showInputDialog(null, "Oznaka vrste naloga");
    if (sCVRNAL == null) return;
    QueryDataSet vrstanal = Vrstenaloga.getDataModule().getTempSet(hr.restart.baza.Condition.equal("CVRNAL",sCVRNAL));
    vrstanal.open();
    if (vrstanal.getRowCount() == 0 && !(sCVRNAL.equals(""))) {
      JOptionPane.showMessageDialog(null, "Ne postoji vrsta naloga "+sCVRNAL+"!","GREŠKA",JOptionPane.ERROR_MESSAGE);
      return;
    }
    QueryDataSet zirci = zirorn.getDataModule().getTempSet(
      Condition.whereAllEqual(new String[] {"CORG","CVRNAL","PROMET"},new String[] {OrgStr.getKNJCORG(),sCVRNAL,"D"}));
    zirci.open();
    ArrayList brojevikonta = new ArrayList();
    for (zirci.first(); zirci.inBounds(); zirci.next()) {
      brojevikonta.add(zirci.getString("BROJKONTA"));
    }
    QueryDataSet prometi = Gkstavkerad.getDataModule().getTempSet(
      Condition.in("BROJKONTA",zirci)+" AND "+Condition.equal("CVRNAL", sCVRNAL));
    prometi.open();
    if (prometi.getRowCount() == 0) {
      JOptionPane.showMessageDialog(null, "Ne postoje stavke prometa izvoda u vrsti naloga "+sCVRNAL+"!","GREŠKA",JOptionPane.ERROR_MESSAGE);      
      return;
    }
    if (JOptionPane.showConfirmDialog(null, "Pronadjeno "+prometi.getRowCount()+" stavaka prometa izvoda! Nastavkom ce se kreirati novi nalozi i izvodi i stavke biti pridodane njima. Jeste li sigurni da to zelite????")!=0) return;
    Valid.getValid().runSQL("DELETE FROM NALOZI WHERE cvrnal='"+sCVRNAL+"'");
    prometi.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"BROJIZV","DATUMKNJ"}));
    for (prometi.first(); prometi.inBounds(); prometi.next()) {
      QueryDataSet nalog = addNalog(prometi);
      QueryDataSet stavke = Gkstavkerad.getDataModule()
        .getTempSet(Condition.whereAllEqual(new String[] {"KNJIG","GOD","CVRNAL","RBR","DATUMKNJ"},prometi)
        +" AND RBS>="+prometi.getInt("RBS"));//ide uvijek prvo stavka prometa pa onda te stavke do slijedeceg prometa
      stavke.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"KNJIG","GOD","CVRNAL","RBR","RBS"}));
      stavke.open();
      if (stavke.getRowCount() > 0) {
        for (stavke.first(); stavke.inBounds(); stavke.next()) {
          if (brojevikonta.contains(stavke.getString("BROJKONTA")) && stavke.getInt("RBS")!=prometi.getInt("RBS") 
              && !stavke.getString("BROJKONTA").equals(prometi.getString("BROJKONTA"))) {
            break;
          }
          stavke.setInt("RBR", nalog.getInt("RBR"));
          stavke.setString("CNALOGA", nalog.getString("CNALOGA"));
        }
        QueryDataSet izvod = addIzvod(prometi,nalog,zirci,stavke);
      //!!!!!!!!OBRISATI STAVKU PROMETA!!!!!!!!!!
      //
        raTransaction.saveChangesInTransaction(new QueryDataSet[] {nalog,izvod,stavke});
      }
    }
  }
  private static QueryDataSet addNalog(ReadRow pr) {
    QueryDataSet nal = Nalozi.getDataModule().getTempSet("'A'='B'");
    nal.open();
    nal.insertRow(false);
    //cnaloga, knjig, god, cvrnal, rbr, datumknj, kontriznos, id, ip, status, datum, pick, saldo
    String[] nalcols = new String[] {"KNJIG","GOD","CVRNAL","DATUMKNJ"};
    ReadRow.copyTo(nalcols, pr, nalcols, nal);
    nal.setInt("RBR", (gkQuerys.getMaxNaloziRBR(pr.getString("KNJIG"), pr.getString("GOD"), pr.getString("CVRNAL"))+1));
    nal.setString("CNALOGA", jpBrojNaloga.getCNaloga(nal));
    nal.setBigDecimal("KONTRIZNOS", pr.getBigDecimal("ID").add(pr.getBigDecimal("IP")));
    nal.setBigDecimal("ID", pr.getBigDecimal("ID").add(pr.getBigDecimal("IP")));
    nal.setBigDecimal("IP", pr.getBigDecimal("ID").add(pr.getBigDecimal("IP")));
    nal.setString("STATUS", "S");
    nal.setBigDecimal("SALDO", new BigDecimal(0));
    nal.post();
System.out.println("Dodajem nalog "+nal.getString("CNALOGA"));
    return nal;
  }
  private static QueryDataSet addIzvod(ReadRow pr, ReadRow nal, QueryDataSet zirci, QueryDataSet stavke) {
    QueryDataSet izv = Izvodi.getDataModule().getTempSet("'A'='B'");
    izv.open();
    izv.insertRow(false);
    //knjig, ziro, brojizv, brojstavki, prethstanje, id, ip, novostanje, datum, cnaloga, status
    izv.setString("KNJIG", nal.getString("KNJIG"));
    if (!lookupData.getlookupData().raLocate(zirci,"BROJKONTA", pr.getString("BROJKONTA"))) {
      throw new RuntimeException("Nije pronadjen ziro racun sa kontom "+pr.getString("BROJKONTA"));
    }
    izv.setString("ZIRO", zirci.getString("ZIRO"));
    izv.setInt("BROJIZV", pr.getInt("BROJIZV"));//moli boga da ovo prodje
    izv.setInt("BROJSTAVKI",(stavke.getRowCount()-1)); //bez prometa
    izv.setString("GOD", nal.getString("GOD"));
    izv.setBigDecimal("PRETHSTANJE", gkQuerys.getPrethStanje(zirci.getString("ZIRO"), nal.getString("GOD"), (pr.getInt("BROJIZV")-1)));
    izv.setBigDecimal("ID", pr.getBigDecimal("IP"));
    izv.setBigDecimal("IP", pr.getBigDecimal("ID"));
    izv.setBigDecimal("NOVOSTANJE", 
      izv.getBigDecimal("IP").add(izv.getBigDecimal("ID").negate()).add(izv.getBigDecimal("PRETHSTANJE")));
    izv.setTimestamp("DATUM", pr.getTimestamp("DATUMKNJ"));
    izv.setString("CNALOGA", jpBrojNaloga.getCNaloga(nal));
    izv.post();
System.out.println("DODAJEM IZVOD "+izv.getInt("BROJIZV")+"-"+izv.getString("ZIRO")+" od naloga "+izv.getString("CNALOGA"));
    return izv;
  }
  
  private static QueryDataSet gksr;
  public static void fillGODMJinGKStavkeRad() {
    raProcess.runChild("Dohvat podataka","Dohvat podataka u tijeku...",new Runnable() {
      public void run() {
        gksr = Gkstavkerad.getDataModule().getTempSet();
        gksr.open();
      }
    });
    if (JOptionPane.showConfirmDialog(null, "Ažurirati / popraviti godinu i mjesec u odnosu na datum knjiženja kod "+gksr.getRowCount()+" neproknjiženih stavaka?") != 0) return;
    raProcess.runChild("Ažuriranje GODMJ","Ažuriranje GODMJ...",new Runnable() {
      public void run() {
        int i=0;
        int tc = gksr.getRowCount();
        for (gksr.first(); gksr.inBounds(); gksr.next()) {
          gksr.setString("GODMJ",getGodMj(gksr));
          i++;
          raProcess.setMessage("Ažuriranje GODMJ ("+i+" / "+tc+")",true);
        }
        raProcess.setMessage("Snimanje promjena ...", true);
        gksr.saveChanges();
      }
    });
    
  }
  /**
   * kopipejstano iz raObrNaloga zato da patch afektira sto manje fileova, nadam se da se to nece mijenjati
   * @param qds
   * @return
   */
  private static String getGodMj(com.borland.dx.dataset.DataSet qds) {
    if (qds.getTimestamp("DATUMKNJ").equals(Util.getUtil().getFirstDayOfYear(qds.getTimestamp("DATUMKNJ")))
        && qds.getString("CVRNAL").equals("00")
        ) {
      return qds.getString("GOD").concat("00");
    } else return qds.getString("GOD").concat(Util.getUtil().getMonth(qds.getTimestamp("DATUMKNJ")));
  }

}