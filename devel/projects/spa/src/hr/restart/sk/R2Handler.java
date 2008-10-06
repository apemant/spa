/****license*****************************************************************
**   file: R2Handler.java
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
 * Created on Dec 16, 2004
 */
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.baza.UIstavke;
import hr.restart.db.raPreparedStatement;
import hr.restart.db.raVariant;
import hr.restart.gk.frmKnjSKRac;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * klasa koja handla racune koji ne idu u knjigu dok se ne plate pri unosu, ispisu, knjizenju ...  
 */
public class R2Handler {
//bazenanje
  private static QueryDataSet pooledShkontaR2 = null;
//log
  private static Logger log = Logger.getLogger(R2Handler.class);
//pamcenje promjena 
  private static QueryDataSet deletedUIstavke;
  private static QueryDataSet addedUIstavke;
//markira da na slijedecem pozivu matchPerformed() stavi deletedUIstavke i addedUIstavke na null
  private static boolean clearMatchPerformedData = false;
//ostalo
  private static String[] skvrdoks = {"URN","IRN","OKD","OKK"};
  public static Timestamp endofmankind;
  private static QueryDataSet uistavke_knjizenje;
  static {
    	Calendar cal = Calendar.getInstance();
    	cal.set(9999,8,9);//09-09-9999
    	endofmankind = new Timestamp(cal.getTime().getTime());
  }
  
  protected R2Handler() {
  }

  /**
   * Provjerava da kolona pocinje sa R, sto znaci da se kroz te stavke sheme po uplati
   * vrsi preknjizavanje iz neplacenog u placeni PDV, a unosi se samo u neplacene
   * (u frmSalKon.fillstavkeSheme())
   * @param rezSet
   * @return
   */
  public static boolean isShemaToUIStavka(QueryDataSet shema) {
    return !shema.getString("POLJE").toUpperCase().startsWith("R");
  }

  /**
   * Sakriva ili prikazuje datum primitka ovisno o zadanoj shemi knjizenja na racunu sk
   * (u jpSalkonMaster.jlrCskl.afterLookup(), frmSalkon.masterSet_navigated(NavigationEvent))
   * @param master
   */
  public static void handleDatPrimitkaUI(jpSalKonMaster master) {
    if (master.jlrCskl.getDataSet().getString("URAIRA").equals("I")) return;
    String cskl = raVariant.getDataSetValue(master.jlrCskl.getDataSet(),master.jlrCskl.getColumnName()).toString();
    String vrdok = raVariant.getDataSetValue(master.jlrCskl.getDataSet(),"VRDOK").toString();
    boolean visible = !isR2Shema(cskl, vrdok);
    master.jlaDatknj.setVisible(visible);
    master.jraDatknj.setVisible(visible);
    master.jraDatknj.setEnabled(visible);
  }

  /**
   * 
   * @param cskl
   * @return
   */
  public static boolean isR2Shema(String cskl, String vrdok) {
    return lookupData.getlookupData().raLocate(getStavkeShemeR2(),new String[] {"CSKL","VRDOK"},new String[] {cskl,vrdok});
  }

  /**
   * @return sve stavke sheme sk dokumenata kojima kolona pocinje sa R (track+issue 24) 
   */
  private static QueryDataSet getStavkeShemeR2() {
/*    "SELECT * FROM shkonta WHERE shkonta.cskl = '"+cskl+
        "' AND shkonta.vrdok = '"+vrdok+"' AND shkonta.polje like 'R%'");*/
    if (pooledShkontaR2 == null) {
      pooledShkontaR2 = Shkonta.getDataModule().getFilteredDataSet(
          Condition.in("VRDOK", skvrdoks)
	        +" AND shkonta.polje like 'R%'");
      
      pooledShkontaR2.open();
      if (log.isDebugEnabled()) {
        log.debug("creating new shkontaR2 with query "+pooledShkontaR2.getQuery().getQueryString());
      }
    }
    return pooledShkontaR2;
  }

  /**
   * stavlja datum primitka u mastersetu na 09.09.9999 ako ima shemu R2
   * (u raSaldaKonti.knjiziStavku())
   * @param masterSet
   */
  public static void handleDatPrimitka(DataSet masterSet) {
    if (isR2Shema(masterSet.getString("CSKL"),masterSet.getString("VRDOK"))) {
      masterSet.setTimestamp("DATPRI", endofmankind);
    }
  }
  /**
   * zove matchPerformed(DataSet, Timestamp)  
   * (u hr.restart.sk.raSaldakonti.matchIznos(DataSet, DataSet, DataSet, BigDecimal, String))
   * @param sk ako je uplata zove se matchPerformed(sk2, sk.datdok), ako nije zove se matchPerformed(sk, sk2.datdok)
   * @param sk2 ako je uplata zove se matchPerformed(sk, sk2.datdok), ako nije zove se matchPerformed(sk2, sk.datdok)
   */
  public static void matchPerformed(DataSet sk, DataSet sk2) {
    DataSet uplata = null;
    DataSet racun = null;
    if (sk.getString("VRDOK").endsWith("PL")) {
      uplata = sk;
      racun = sk2;
    } else if (sk2.getString("VRDOK").endsWith("PL")) {
      uplata = sk2;
      racun = sk;
    }
    if (uplata == null || racun == null) {//uplata je okajac
      if (sk.getString("VRDOK").endsWith("RN")) {
        uplata = sk2;
        racun = sk;
      } else if (sk2.getString("VRDOK").endsWith("RN")) {
        uplata = sk;
        racun = sk2;
      }
    }
    if (uplata == null || racun == null) {//uplata je okajac
      if (sk.getString("VRDOK").endsWith("RN")) {
        uplata = sk2;
        racun = sk;
      } else if (sk2.getString("VRDOK").endsWith("RN")) {
        uplata = sk;
        racun = sk2;
      }
    }
    if (uplata == null || racun == null) {//oboje su okajci jebem mu...
      if (raKonta.isDobavljac(sk.getString("BROJKONTA")) 
          && (sk.getBigDecimal("IP").compareTo(new BigDecimal(0)) == 0)) {//sk je ok na uplatu
        uplata = sk;
        racun = sk2;
      } else if (raKonta.isDobavljac(sk.getString("BROJKONTA")) 
          && (sk.getBigDecimal("ID").compareTo(new BigDecimal(0)) == 0)) {//sk je ok na uru - racun
        uplata = sk2;
        racun = sk;
      } else if (raKonta.isKupac(sk.getString("BROJKONTA")) 
          && (sk.getBigDecimal("ID").compareTo(new BigDecimal(0)) == 0)) {//sk je ok uplatu
        uplata = sk;
        racun = sk2;
      } else if (raKonta.isKupac(sk.getString("BROJKONTA")) 
          && (sk.getBigDecimal("IP").compareTo(new BigDecimal(0)) == 0)) {//sk je ok iru - racun
        uplata = sk2;
        racun = sk;
      }
    }
    matchPerformed(racun,uplata.getTimestamp("DATDOK"));
  }
  /**
   * U parametrima ju dataset skstavka koji je upravo pokriven ili raskriven. 
   * Ako je taj skstavka ?RN ili OK? i ako ima R2 shemu
   * <pre>  
   * - pokriven dodati R2 preknjizavanje sa cskl = R+cskl,
   * - ako je raskriven i ima neproknjizeno dodatno knjizenje 
   *   obrisati sve uistavke tog racuna cskl like 'R%',
   * - ako je raskriven i ima proknjizeno dodatno knjizenje 
   *   stornirati to knjizenje i markirati za gk (cskl like 'R%')
   * </pre> 
   * @param sk stavka koja je upravo pokrivena ili raskrivena
   * @param sk2 stavka s kojom je pokriveno
   */
  public static void matchPerformed(DataSet sk, Timestamp datupl) {
    if (clearMatchPerformedData) {
      deletedUIstavke = null;
      addedUIstavke = null;
      clearMatchPerformedData = false;
    }
    if (!Util.getUtil().containsArr(skvrdoks,sk.getString("VRDOK"))) return;//ova slijedeca bi ove tako odjebala, ali radi brzine...
    if (sk.getString("CSKL").length() == 0) return;
    if (!isR2Shema(sk.getString("CSKL"),sk.getString("VRDOK"))) return;
    if (sk.getString(raSaldaKonti.colPok()).equals("D") && sk.getBigDecimal("PVSALDO").signum() == 0) {//pokrivena
      if (log.isDebugEnabled()) {
        log.debug("Dodajem rasknjizavanje na pokriveni R2");
      }
      addR2Prek(sk, datupl);
      setSkTimestamp(sk,datupl);
    } else if (sk.getString(raSaldaKonti.colPok()).equals("N") && sk.getBigDecimal("PVSALDO")
        .compareTo(sk.getBigDecimal("SSALDO")) == 0) {//raskrivena
      if (log.isDebugEnabled()) {
        log.debug("Storniram/Brisem rasknjizavanje na pokriveni R2");
      }
      removeR2Prek(sk, datupl);
      setSkTimestamp(sk, null);    //ovo nisam siguran kako ce izgledati 
			//jer ovo raskrivene R2 mice iz knjige i rekapitulacije
    }
  }
  /**
   * 
   * @param sk
   * @param ts ako je null stavlja 09.09.9999
   */
  private static void setSkTimestamp(DataSet sk, Timestamp ts) {
    if (ts == null) {
      sk.setTimestamp("DATPRI", endofmankind);
    } else {
      sk.setTimestamp("DATPRI", ts);
    }
    if (log.isDebugEnabled()) {
      log.debug("stavljam datum primitka na "+sk.getTimestamp("DATPRI"));
    }
    sk.post();//ovo ce se valjda sejvati u daljnjem tijeku natjecanja

  }

  /**
   * Dodaje uistavke za preknjizavanje sa csheme 'R'+csheme
   * @param sk
   * @param datupl
   */     
  private static void addR2Prek(DataSet sk, Timestamp datupl) {
    if (unconditionalRemoveR2Prek(sk)) {// 
      /* 
       * ako unconditionalRemoveR2Prek vrati true to znaci da je pokrio 
       * pa proknjizio pa raskrio, pa opet pokrio sto je kao da se nista nije dogodilo
       */
      if (log.isDebugEnabled()) {
        log.debug("Obrisao storno R2 kod prethodnog raskrivanja");
      }
    } else {//ovo je stvarno preknjizavanje 
      QueryDataSet shemeR2 = getStavkeShemeR2();
      QueryDataSet mozedbiti = UIstavke.getDataModule().getTempSet("0=1");
      mozedbiti.open();
      QueryDataSet stornonemozedbiti = UIstavke.getDataModule().getTempSet("0=1");
      stornonemozedbiti.open();
      QueryDataSet uistavke = frmKnjSKRac.getUIStavke(sk);
      if (log.isDebugEnabled()) {
        log.debug("Preknjizavam R2...");
        log.debug(" * shemaR2 :: "+shemeR2.getRowCount());
        for (shemeR2.first(); shemeR2.inBounds(); shemeR2.next()) {
          log.debug("  *** "+shemeR2);
        }
        log.debug(" * uistavaka :: "+uistavke.getRowCount());
        for (uistavke.first(); uistavke.inBounds(); uistavke.next()) {
          log.debug("  *** "+uistavke);
        }
      }
      //zadnji rbs
      int rbs = 0;
      for (uistavke.first(); uistavke.inBounds(); uistavke.next()) {
        if (uistavke.getInt("RBS") > rbs)
          rbs = uistavke.getInt("RBS");
      }
      
      for (shemeR2.first(); shemeR2.inBounds(); shemeR2.next()) {
        if (shemeR2.getString("VRDOK").equals(sk.getString("VRDOK")) 
            && shemeR2.getString("CSKL").equals(sk.getString("CSKL"))) {//mora pasati uz shemu a ne neko naknadno dodano smetje 
          String nemozeodbiti_kolona = shemeR2.getString("POLJE").substring(1);
          if (log.isDebugEnabled()) {
            log.debug("trazim u uistavke slog sa CKOLONE="+nemozeodbiti_kolona);
          }
          if (lookupData.getlookupData().raLocate(uistavke, "CKOLONE", nemozeodbiti_kolona)) {//nadji stavku za storniranje

            stornonemozedbiti.insertRow(true);
            uistavke.copyTo(stornonemozedbiti);
            //stornonemozedbiti.setString("BROJKONTA", raKnjizenje.getBrojKonta(stornonemozedbiti));
            BigDecimal st_id = stornonemozedbiti.getBigDecimal("ID");
            BigDecimal st_ip = stornonemozedbiti.getBigDecimal("IP");
            if (frmParam.getParam("sk","r2promet","storno"
                ,"Kod preknjižavanja R2 radi 'promet' na kontu pdv ne moze se odbiti ili 'storno'?")
                .equals("storno")) { 
	            stornonemozedbiti.setBigDecimal("ID",st_id.negate());
	            stornonemozedbiti.setBigDecimal("IP",st_ip.negate());
            } else {//promet
              stornonemozedbiti.setBigDecimal("ID",st_ip);
              stornonemozedbiti.setBigDecimal("IP",st_id);
            }
            stornonemozedbiti.setString("CSKL","R"+stornonemozedbiti.getString("CSKL"));
            rbs++;
            stornonemozedbiti.setInt("RBS",rbs);
            stornonemozedbiti.post();
            if (log.isDebugEnabled()) {
              log.debug("dodan stornonemozedbiti :: "+stornonemozedbiti);
            }
            mozedbiti.insertRow(true);
            uistavke.copyTo(mozedbiti);
            mozedbiti.setString("CSKL","R"+mozedbiti.getString("CSKL"));
            mozedbiti.setString("BROJKONTA",shemeR2.getString("BROJKONTA"));
            mozedbiti.setShort("STAVKA",shemeR2.getShort("STAVKA"));//kad makne R da dobro prikazuje konto u pregledu arhive
            rbs++;
            mozedbiti.setInt("RBS",rbs);
            mozedbiti.post();
            if (log.isDebugEnabled()) {
              log.debug("dodan mozedbiti :: "+mozedbiti);
            }
            
            // ab.f uistavke je iskoristena za preknjizavanje i treba je makniti
            uistavke.emptyRow();
          } else {
            if (log.isDebugEnabled()) {
              log.debug("nisam nasao u uistavke slog sa CKOLONE="+nemozeodbiti_kolona);
            }
          }
        } else {
          if (log.isDebugEnabled()) {
            log.debug(shemeR2.getString("VRDOK")+" != "+sk.getString("VRDOK") 
                + " ili je "+shemeR2.getString("CSKL")+" != "+sk.getString("CSKL")
                );
          }
        }
      }//endfor
      addUIStavkeLater(stornonemozedbiti);
      addUIStavkeLater(mozedbiti);
    }
  }
  
  /**
   * Brise sve uistavke zadane skstavke i sa csheme like 'R%'
   * @param sk
   * @param datupl
   */
  private static void removeR2Prek(DataSet sk, Timestamp datupl) {
    if (!unconditionalRemoveR2Prek(sk)) {//nije bilo stavaka preknjizavanja
      																	 //raskriva pokriveno i proknjizeno?
      QueryDataSet uisve = frmKnjSKRac.getUIStavke(sk);
      QueryDataSet uistorno = UIstavke.getDataModule().getTempSet("0=1");
      uistorno.open();
      //zadnji rbs
      uisve.last();
      int rbs = uisve.getInt("RBS");
      /* 
       * nadji proknjizenu stavku sa shemek.polje like 'R%' po stavci!, pa ako ima stornirati sve,
       * a poslije pri ponovnom pokrivanju opet obrise te stavke (unconditionalRemoveR2Prek) i sve 5
       */
      HashSet stavkaprek = getR2Stavka(sk.getString("VRDOK"), sk.getString("CSKL"));
      for (uisve.first(); uisve.inBounds(); uisve.next()) {
        if (stavkaprek.contains(new Short(uisve.getShort("STAVKA"))) && uisve.getString("CSKL").equals(sk.getString("CSKL"))) {
          uistorno.insertRow(false);
          uisve.copyTo(uistorno);
          uistorno.setString("CSKL", "R"+uistorno.getString("CSKL"));
          uistorno.setBigDecimal("ID",uistorno.getBigDecimal("ID").negate());
          uistorno.setBigDecimal("IP",uistorno.getBigDecimal("IP").negate());
          rbs++;
          uistorno.setInt("RBS",rbs);
          uistorno.post();
          // i jos treba stornirati storno :) konta ne moze se odbiti
          uistorno.insertRow(false);
          uisve.copyTo(uistorno);
          uistorno.setString("CSKL", "R"+uistorno.getString("CSKL"));
          uistorno.setString("BROJKONTA",getNemozeOdbitiKonto(uistorno, sk));
          rbs++;
          uistorno.setInt("RBS",rbs);
          uistorno.post();          
        }
      }
      addUIStavkeLater(uistorno);
    }
  }

  /**
   * javadoc godine:
   * nalazi konto koji nije onaj na zadanom uistorno, a ima id=-uistorno.id i ip=-uistorno.ip, (ako nisu 0)
   * upotrebljava se za storniranje stornog kod raskrivanja R2 racuna cije je preknjizavanje vec proknjizeno
   * @param uistorno
   * @param sk
   * @return broj konta
   */
  private static String getNemozeOdbitiKonto(QueryDataSet uistorno, DataSet sk) {
    QueryDataSet ui = frmKnjSKRac.getUIStavke(sk);
    String col = (uistorno.getBigDecimal("ID").compareTo(new BigDecimal(0)) == 0)?"IP":"ID"; 
    for (ui.first(); ui.inBounds(); ui.next()) {
      if (!ui.getString("BROJKONTA").equals(uistorno.getString("BROJKONTA"))) {
        if (ui.getBigDecimal(col).compareTo(uistorno.getBigDecimal(col).negate()) == 0) {
          return ui.getString("BROJKONTA");
        }
      }
    }
    return null;
  }

  /**
   * brise sve POLJE like 'R%' uistavke i vraca boolean da li ih je bilo ili ne 
   * @param sk skstavka od koje brisemo uistavke
   * @return true li je bilo stavaka za brisanje
   */
  private static boolean unconditionalRemoveR2Prek(DataSet sk) {
    QueryDataSet uiprek = getR2PrekUIs(sk);
    if (uiprek.getRowCount() > 0 && !areDeleted(uiprek)) {
      deleteUIStavkeLater(uiprek);
      return true;
    } else {
      return false;
    }
  }
  
  
  /**
   * provjeri, da li su sve zadane uistavke markirane za brisanje (dodane u deletedUIstavke())
   * @param uiprek
   * @return
   */
  private static boolean areDeleted(QueryDataSet ui) {
    if (getDeletedUIstavke().getRowCount() == 0) return false; //nista nije markirano za brisanje 
    for (ui.first(); ui.inBounds(); ui.next()) {
      if (!isDeleted(ui)) return false; 
    }
    return true;
  }

  /**
   * Nalazi li se zadana uistavka u deleteduistavke
   * @param ui
   * @return true - nalazi se, false - ne nalazi se
   */
  private static boolean isDeleted(QueryDataSet ui) {
    for (getDeletedUIstavke().first(); getDeletedUIstavke().inBounds(); getDeletedUIstavke().next()) {
      if (getDeletedUIstavke().equals(ui)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Ubaci u QueryDataSet za kasnije brisanje 
   * @param uiprek
   */
  private static void deleteUIStavkeLater(QueryDataSet ui) {
    markForLater(ui,getDeletedUIstavke());
  }
  /**
   * Ubaci u QueryDataSet za kasnije dodavanje
   * @param uistorno
   */
  private static void addUIStavkeLater(QueryDataSet ui) {
    markForLater(ui,getAddedUIstavke());
  }
  
  private static void markForLater(QueryDataSet ui, QueryDataSet markset) {
    for (ui.first(); ui.inBounds(); ui.next()) {
      markset.insertRow(false);
      ui.copyTo(markset);
      markset.post();
    }
  }
  /**
   * Za pozvati u saveChangesInTransaction(QueryDataSet[])
   * (u raOptimisticMatch.performMatching(), raSaldaKonti.matchIfYouCan(ReadRow, boolean)
   *  frmCover.partialMatch(), frmCover.enterPressed(boolean), hr.restart.gk.raKnjizenjeSK.saveSKknjizenje() )
   * @return
   */
/*  private static QueryDataSet getUIStavkeDeleted() {
    clearMatchPerformedData = true;
    QueryDataSet q = getDeletedUIstavke();
    if (log.isDebugEnabled()) {
      log.debug("Dajem markirane za brisanje");
      log.debug("-----------------");
      for (q.first(); q.inBounds(); q.next()) {
        log.debug(q);
      }
      log.debug("-----------------");
    }
    q.deleteAllRows(); //nebu islo moram ih prvo resolvat
    new Throwable().printStackTrace();
    return q;
  }*/
  /**
   * Za pozvati u saveChangesInTransaction(QueryDataSet[])
   * (u raOptimisticMatch.performMatching(), raSaldaKonti.matchIfYouCan(ReadRow, boolean)
   *  frmCover.partialMatch(), frmCover.enterPressed(boolean), hr.restart.gk.raKnjizenjeSK.saveSKknjizenje() )
   * @return
   */
/*  private static QueryDataSet getUIStavkeAdded() {
    clearMatchPerformedData = true;
    QueryDataSet q = getAddedUIstavke();
    if (log.isDebugEnabled()) {
      log.debug("Dajem markirane za dodavanje");
      log.debug("-----------------");
      for (q.first(); q.inBounds(); q.next()) {
        log.debug(q);
      }
      log.debug("-----------------");
    }
    return q;
  }*/
  /**
   * Pozvati umjesto raTransaction.saveChangesInTransaction(QueryDataSet[])
   * u raOptimisticMatch.performMatching(), raSaldaKonti.matchIfYouCan(ReadRow, boolean)
   *  frmCover.partialMatch(), frmCover.enterPressed(boolean), hr.restart.gk.raKnjizenjeSK.saveSKknjizenje() 
   * @param othersets
   * @return
   */
  public static boolean saveChangesInTransaction(final QueryDataSet[] othersets) {
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        for (int i = 0; i < othersets.length; i++) {
          this.saveChanges(othersets[i]);
        }
        saveR2Changes();
        return true;
      }
    }.execTransaction();
  }
  /**
   * Snima promjene na uistavkama pri pokrivanju i raskrivanju racuna
   * (u R2Handler.saveChangesInTransaction(..) i hr.restart.gk.raKnjizenjeSK.saveKnjizenje())
   * @throws SQLException
   */
  public static void saveR2Changes() throws SQLException {
    clearMatchPerformedData = true;
    raPreparedStatement deluistavke = new raPreparedStatement("uistavke",raPreparedStatement.DELETE);
    raPreparedStatement adduistavke = new raPreparedStatement("uistavke",raPreparedStatement.INSERT);
    //deleted
    QueryDataSet deleted = getDeletedUIstavke();
    for (deleted.first(); deleted.inBounds(); deleted.next()) {
      if (log.isDebugEnabled()) {
        log.debug("deleting "+deleted);
      }
      deluistavke.setKeys(deleted);
      deluistavke.execute();
    }
    // added
    QueryDataSet added = getAddedUIstavke();
    for (added.first(); added.inBounds(); added.next()) {
      if (log.isDebugEnabled()) {
        log.debug("adding "+added);
      }
      adduistavke.setValues(added);
      //stavi kolonu na 0 zbog knjige
      adduistavke.setShort("CKOLONE",(short)0,false);
      adduistavke.execute();
    }
  }
  /**
   * Kreira set sa uistavke.stavka=shkonta.stavka iz getStavkeShemeR2() ako odgovaraju VRDOK i CSKL 
   * @param vrdok
   * @param cskl
   * @return HashSet sa brojevima konta
   */
  private static HashSet getR2Stavka(String vrdok, String cskl) {
    HashSet	ret = new HashSet();
    QueryDataSet set = getStavkeShemeR2();
    for (set.first(); set.inBounds(); set.next()) {
      if (set.getString("VRDOK").equals(vrdok) && set.getString("CSKL").equals(cskl)) {
        ret.add(new Short(set.getShort("STAVKA")));
      }
    }
    return ret;
  }

  /**
   * @param sk
   * @return
   */
  private static QueryDataSet getR2PrekUIs(DataSet sk) {
    QueryDataSet qds = UIstavke.getDataModule().getTempSet(
        Condition.whereAllEqual(frmKnjSKRac.skuilinkcols,sk)
        +" AND CSKL LIKE 'R%'");
    qds.open();
    return qds;
  }

	private static QueryDataSet getDeletedUIstavke() {
	  if (deletedUIstavke == null) {
	    deletedUIstavke = UIstavke.getDataModule().getTempSet("0=1");
	    deletedUIstavke.open();
	  }
	  return deletedUIstavke;
	}
  private static QueryDataSet getAddedUIstavke() {
    if (addedUIstavke == null) {
      addedUIstavke = UIstavke.getDataModule().getTempSet("0=1");
      addedUIstavke.open();
    }
    return addedUIstavke;
  }

  /**
   * Inicijalizacija knjizenja R2 u frmKnjSKRac:
   * nadje sve uistavke proknjizenih dokumenata sa csheme like '%R'
   * @param vrdokQuery
   * @param date
   */
  public static void beginKnjizenje(String vrdokQuery, Date date) {
    if (getStavkeShemeR2().rowCount() == 0)
      uistavke_knjizenje = UIstavke.getDataModule().getTempSet("1=0");
    else uistavke_knjizenje = UIstavke.getDataModule().
    	getTempSet(Condition.equal("KNJIG",OrgStr.getKNJCORG())+
    	    " AND CSKL LIKE 'R%' "
/*  //ovo tak vrhunski zakolje situaciju ...
    	    +"AND KNJIG||'#'||CPAR||'#'||VRDOK||'#'||BROJDOK||'#' in (SELECT KNJIG||'#'||CPAR||'#'||VRDOK||'#'||BROJDOK||'#' FROM skstavke WHERE skstavke.datpri < '"
    	    +date+"' "+vrdokQuery+") "
*/    	   //ovo ispod bi trebalo biti brze... isprobati!
    	    +" AND EXISTS (SELECT * FROM skstavke where uistavke.knjig = skstavke.knjig "
    	                                        +" AND uistavke.cpar = skstavke.cpar"
    	                                        +" AND uistavke.vrdok = skstavke.vrdok"
    	                                        +" AND uistavke.brojdok = skstavke.brojdok"
                     +" AND skstavke.cgkstavke is not null and skstavke.cgkstavke != ''"
    	                   +" AND " +
    	                   	//	"skstavke.datpri < '"+date+"'" +
    	                   
    	                   Condition.where("DATPRI",Condition.BEFORE,new Timestamp(date.getTime()))+
    	                   				" "+vrdokQuery+") "
    	    +vrdokQuery);//racunam da puni cgkstavke pri knjizenju
    
    log.debug(uistavke_knjizenje.getQuery().getQueryString());

    uistavke_knjizenje.open();
    if (log.isDebugEnabled()) {
      log.debug("R2Handler - pocinjem knjizenje");
      log.debug(" query je "+uistavke_knjizenje.getQuery().getQueryString());
      log.debug("*** pronadjene uistavke");
      for (uistavke_knjizenje.first(); uistavke_knjizenje.inBounds(); uistavke_knjizenje.next()) {
        log.debug(uistavke_knjizenje);
      }
      log.debug("*******");
      uistavke_knjizenje.first();
    }
    
  }

  /**
   * @return broj uistavki zahvacenih za knjizenje u frmKnjSKRac
   * @see beginKnjizenje()
   */
  public static int getR2KnjizenjeCount() {
    return uistavke_knjizenje==null?0:uistavke_knjizenje.getRowCount();
  }

  /**
   * @return querydataset pripremljen sa beginKnjizenje
   */
  public static QueryDataSet getR2KnjizenjeSet() {
    return uistavke_knjizenje;
  }

  /**
   * Svim uistavke_knjizenje maknuti R iz CSHEME
   * zove se u frmKnjSKRac.commitTransfer()
   * @return jel uspio il nije
   */
  public static boolean commitTransferR2() {
    for (uistavke_knjizenje.first(); uistavke_knjizenje.inBounds(); uistavke_knjizenje.next()) {
      handleSaveui(uistavke_knjizenje);
      uistavke_knjizenje.post();
    }
    raTransaction.saveChanges(uistavke_knjizenje);
    return true;
  }

  /**
   * Makne onaj R u CSHEME sto oznacava da je stavka proknjizena
   * poziva se u frmKnjSKRac.okPress()
   * @param saveui
   */
  public static void handleSaveui(StorageDataSet saveui) {
    if (saveui.getString("CSKL").startsWith("R")) {
      String cskl = saveui.getString("CSKL").substring(1);
      if (log.isDebugEnabled()) {
        log.debug("mijenjam cskl iz "+saveui.getString("CSKL")+" u "+cskl);
      }
      saveui.setString("CSKL",cskl);
    }
  }

  /**
   * pronaci stavku gk sa tim cgkstavke i vratiti njen opis
   * @param knjizenjeSet
   * @return
   */
  public static String getR2opis(QueryDataSet r2opisknjset) {
    String cgkst = r2opisknjset.getString("CGKSTAVKE");
    try {
      StringTokenizer tknzr = new StringTokenizer(cgkst,"-");
      String knjig = tknzr.nextToken();
      String god = tknzr.nextToken();
      String cvrnal = tknzr.nextToken();
      String rbr = tknzr.nextToken();
      String rbs = tknzr.nextToken();
      QueryDataSet opisds = Util.getNewQueryDataSet(
          "SELECT OPIS FROM gkstavke WHERE KNJIG='"+knjig+
          "' AND god='"+god+
          "' AND cvrnal='"+cvrnal+
          "' AND rbr="+Integer.parseInt(rbr)+
          " AND rbs="+Integer.parseInt(rbs));
      if (opisds.getRowCount()==0) throw new RuntimeException("gkstavka "+cgkst+" nije pronadjena :(");
      return opisds.getString("OPIS");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}