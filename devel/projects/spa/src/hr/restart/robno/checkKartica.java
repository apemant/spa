/****license*****************************************************************
**   file: checkKartica.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Doku;
import hr.restart.baza.Meskla;
import hr.restart.baza.Stdoku;
import hr.restart.baza.Stmeskla;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.dlgErrors;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class checkKartica {

	private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	//private StorageDataSet greske;

	private raControlDocs rCD = new raControlDocs();

	private StorageDataSet kartica;

	private StorageDataSet karticatmp;

	QueryDataSet ulazi, izlazi, meskla;
  
  /*QueryDataSet ulazHead, izlazHead, mesHead;*/

	private String cskl = "";

	private String god = "";

	private String vrzal = "";	
	
	private String sysdat = "SYSDAT";
  
  int erroneousCards, erroneousRows, erroneousCells;
  
  HashMap mcskl = new HashMap();
  
  int totalCards;
  
  boolean firstRow, fixMes;
  
  BigDecimal maxMinus = Aus.zero0;
  
  VarStr buffer = new VarStr();
  
  dlgErrors errs, fatal;

	private hr.restart.util.lookupData lD = hr.restart.util.lookupData
			.getlookupData();

	public StorageDataSet getKartica() {
		return kartica;
	}

	{
		initKartica();
	}

	private void initKartica() {
		/*greske = new StorageDataSet();
		Column greska = dm.getDoku().getColumn("OPIS").cloneColumn();
		Column key = dm.getStanje().getColumn("TKAL").cloneColumn();
		key.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
		key.setColumnName("KLJUC");
		greska.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
		greska.setColumnName("OPIS");
		greske.setColumns(new Column[] { key, greska });
		greske.open();*/
		
		/*Column sysdat = dm.getDoku().getColumn("SYSDAT").cloneColumn();
		Column zasort = dm.getDoku().getColumn("OPIS").cloneColumn();
		zasort.setColumnName("ZASORT");*/

		kartica = new StorageDataSet();
		Column cskl = dm.getStdoku().getColumn("CSKL").cloneColumn();
		Column csklul = dm.getStmeskla().getColumn("CSKLUL").cloneColumn();
		Column cskliz = dm.getStmeskla().getColumn("CSKLIZ").cloneColumn();
		Column god = dm.getStmeskla().getColumn("GOD").cloneColumn();
		Column vrdok = dm.getStmeskla().getColumn("VRDOK").cloneColumn();
		Column rvrdok = dm.getStmeskla().getColumn("VRDOK").cloneColumn();
		rvrdok.setColumnName("VD");
		rvrdok.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column brdok = dm.getStmeskla().getColumn("BRDOK").cloneColumn();
		Column rbr = dm.getStmeskla().getColumn("RBR").cloneColumn();
		Column datdok = dm.getMeskla().getColumn("DATDOK").cloneColumn();
		Column kol = dm.getStmeskla().getColumn("KOL").cloneColumn();
		Column kol_trenutno = dm.getStmeskla().getColumn("KOL").cloneColumn();
		kol_trenutno.setColumnName("KOL_TRENUTNO");
		kol_trenutno.setCaption("Suma kol.");
		
		/*
		 * Column vri_trenutno = dm.getStdoku().getColumn("IZAD").cloneColumn();
		 * vri_trenutno.setColumnName("VRI_TRENUTNO");
		 * vri_trenutno.setCaption("Suma iznosa");
		 */
		Column nc = dm.getStmeskla().getColumn("NC").cloneColumn();
		Column nc_good = dm.getStmeskla().getColumn("NC").cloneColumn();
		nc_good.setColumnName("NC_GOOD");
		nc_good.setCaption("NC_GOOD");
		Column inab = dm.getStdoku().getColumn("INAB").cloneColumn();
		Column inab_good = dm.getStdoku().getColumn("INAB").cloneColumn();
		inab_good.setColumnName("INAB_GOOD");
		inab_good.setCaption("INAB_GOOD");
		Column vc = dm.getStmeskla().getColumn("VC").cloneColumn();
		Column vc_good = dm.getStmeskla().getColumn("VC").cloneColumn();
		vc_good.setColumnName("VC_GOOD");
		vc_good.setCaption("VC_GOOD");
		Column svc = dm.getStmeskla().getColumn("SVC").cloneColumn();
		svc.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column imar = dm.getStdoki().getColumn("IMAR").cloneColumn();
		Column imar_good = dm.getStdoki().getColumn("IMAR").cloneColumn();
		imar_good.setColumnName("IMAR_GOOD");
		imar_good.setCaption("IMAR_GOOD");
		Column ibp = dm.getStdoku().getColumn("IBP").cloneColumn();
		Column ibp_good = dm.getStdoku().getColumn("IBP").cloneColumn();
		ibp_good.setColumnName("IBP_GOOD");
		ibp_good.setCaption("IBP_GOOD");
		Column mc = dm.getStmeskla().getColumn("MC").cloneColumn();
		Column mc_good = dm.getStmeskla().getColumn("MC").cloneColumn();
		mc_good.setColumnName("MC_GOOD");
		mc_good.setCaption("MC_GOOD");
		Column smc = dm.getStmeskla().getColumn("SMC").cloneColumn();
		smc.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column ipor = dm.getStdoku().getColumn("IPOR").cloneColumn();
		Column ipor_good = dm.getStdoku().getColumn("IPOR").cloneColumn();
		ipor_good.setColumnName("IPOR_GOOD");
		ipor_good.setCaption("IPOR_GOOD");
		Column isp = dm.getStdoku().getColumn("ISP").cloneColumn();
		Column isp_good = dm.getStdoku().getColumn("ISP").cloneColumn();
		isp_good.setColumnName("ISP_GOOD");
		isp_good.setCaption("ISP_GOOD");
		Column zc = dm.getStdoku().getColumn("ZC").cloneColumn();
		Column zc_good = dm.getStdoku().getColumn("ZC").cloneColumn();
		zc_good.setColumnName("ZC_GOOD");
		zc_good.setCaption("ZC_GOOD");
		Column izadraz = dm.getStdoku().getColumn("IZAD").cloneColumn();
		Column izadraz_good = dm.getStdoku().getColumn("IZAD").cloneColumn();
		izadraz_good.setColumnName("IZAD_GOOD");
		izadraz_good.setCaption("IZAD_GOOD");
		Column izadraz_trenutno = dm.getStdoku().getColumn("IZAD")
				.cloneColumn();
		izadraz_trenutno.setColumnName("IZAD_TRENUTNO");
		izadraz_trenutno.setCaption("IZAD_TREnutni");
		Column inab_trenutno = dm.getStdoku().getColumn("INAB").cloneColumn();
		inab_trenutno.setColumnName("INAB_TRENUTNO");
		inab_trenutno.setCaption("INAB_TRENUTNO");
		inab_trenutno.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column tkal = dm.getStanje().getColumn("TKAL").cloneColumn();
		tkal.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
		Column opis = dm.getDoki().getColumn("OPIS").cloneColumn();
		opis.setCaption("Opis greske");
		Column dobar = dm.getDoki().getColumn("LOKK").cloneColumn();
		dobar.setColumnName("DOBAR");
		dobar.setCaption("Ispravnost");
		kartica.setColumns(new Column[] { dobar, cskl, csklul, cskliz, god,
				vrdok, brdok, rbr, datdok, kol, kol_trenutno, inab_trenutno,
				izadraz_trenutno, nc, nc_good, inab, inab_good, vc, vc_good,
				imar, imar_good, ibp, ibp_good, mc, mc_good, ipor, ipor_good,
				isp, isp_good, zc, zc_good, izadraz, izadraz_good, tkal, opis,
				svc, smc, rvrdok });
		kartica.open();
		karticatmp = new StorageDataSet();
		karticatmp.setColumns(kartica.cloneColumns());
		karticatmp.open();
	}

	public checkKartica() {
	}
  
  public void setMainParams(String _cskl, String _god) {
    cskl = _cskl;
    god = _god;
    vrzal = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT vrzal FROM sklad where cskl='" + cskl + "'", true)
        .getString("VRZAL");
    prepareConditions();
  }
  
  public void setSysdat(boolean sys) {
    sysdat = sys ? "SYSDAT" : "DATDOK";
  }
  
  public void setFixMes(boolean fix) {
    fixMes = fix;
  }
  
  public Condition cGod, cCskl, cMeu, cMei, cUl, cIz;
  
  public void prepareConditions() {
    cGod = Condition.equal("GOD", god);
    cCskl = Condition.equal("CSKL", cskl);
    cMeu = Condition.equal("CSKLUL", cskl).
            and(Condition.in("VRDOK", new String[] {"MES", "MEU"}));
    cMei = Condition.equal("CSKLIZ", cskl).
            and(Condition.in("VRDOK", new String[] {"MES", "MEI"}));
    List ulazDok = new ArrayList();
    List izlazDok = new ArrayList();
    String[] docs = TypeDoc.araj_docs;
    for (int i = 0; i < docs.length; i++)
      if (TypeDoc.getTypeDoc().isDocDiraZalihu(docs[i])) {
        if (TypeDoc.getTypeDoc().isDocUlaz(docs[i])) ulazDok.add(docs[i]);
        else izlazDok.add(docs[i]);
      }

    cUl = Condition.in("VRDOK", ulazDok.toArray(new String[ulazDok.size()]));
    cIz = Condition.in("VRDOK", izlazDok.toArray(new String[ulazDok.size()]));
  }
  
  private int[] getBrdokList(DataSet rows) {
    Set brdoks = new HashSet();
    for (rows.first(); rows.inBounds(); rows.next())
      brdoks.add(new Integer(rows.getInt("BRDOK")));
    int[] ret = new int[brdoks.size()];
    int idx = 0;
    for (Iterator i = brdoks.iterator(); i.hasNext(); ret[idx++] = ((Integer) i.next()).intValue());
    return ret;
  }
  
  Map ulazHead, izlazHead, mesHead;
  Map ulazRows, izlazRows, mesRows;
  
  Set knjHead;

  private String getHeaderKey(DataSet ds) {
  	if (ds.getString("VRDOK").equals("MES") || ds.getString("VRDOK").equals("MEU") || ds.getString("VRDOK").equals("MEI")) 
      return buffer.clear().
        append(ds.getString("CSKLUL")).append('-').
        append(ds.getString("CSKLIZ")).append('-').
        append(ds.getString("VRDOK")).append('-').
        append(ds.getInt("BRDOK")).toString();
    return buffer.clear().
      append(ds.getString("CSKL")).append('-').
      append(ds.getString("VRDOK")).append('-').
      append(ds.getInt("BRDOK")).toString();
  }
  
  private String getRowKey(DataSet ds) {
    if (ds.getString("VRDOK").equals("MES") || ds.getString("VRDOK").equals("MEU") || ds.getString("VRDOK").equals("MEI")) 
      return buffer.clear().
        append(ds.getString("CSKLUL")).append('-').
        append(ds.getString("CSKLIZ")).append('-').
        append(ds.getString("VRDOK")).append('-').
        append(ds.getInt("BRDOK")).append('-').
        append(ds.getShort("RBR")).toString();
    return buffer.clear().
      append(ds.getString("CSKL")).append('-').
      append(ds.getString("VRDOK")).append('-').
      append(ds.getInt("BRDOK")).append('-').
      append(ds.getShort("RBR")).toString();
  }
  
  private void fillHeaderMap(DataSet ds, Map headerMap) {
  	String key;
  	boolean fixknj = frmParam.getParam("robno", "fixKnj", "N", 
  	    "Popraviti i proknjižene iznose u popravku kartice (D,N)?").equalsIgnoreCase("D");
    for (ds.first(); ds.inBounds(); ds.next()) {
      headerMap.put(key = getHeaderKey(ds), new Timestamp(ds.getTimestamp(sysdat).getTime()));
      if (fixknj && ((ds.hasColumn("STATKNJ") != null && ds.getString("STATKNJ").equals("K")) ||
      		(ds.hasColumn("STATKNJU") != null && (ds.getString("STATKNJU").equals("K") || ds.getString("STATKNJI").equals("K")))))
      	knjHead.add(key);
    }
  }
  
  private void prepareHeaderForSingle() {
    QueryDataSet ds;
    knjHead = new HashSet();
    raProcess.setMessage("Dohvat zaglavlja dokumenata na kartici ...", true);
    ds = Doku.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK STATKNJ " + sysdat,
       cCskl.and(cGod).and(cUl).and(Condition.in("BRDOK", getBrdokList(ulazi))));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, ulazHead = new HashMap());

    ds = doki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK STATKNJ " + sysdat, 
       cCskl.and(cGod).and(cIz).and(Condition.in("BRDOK", getBrdokList(izlazi))));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, izlazHead = new HashMap());

    ds = Meskla.getDataModule().getTempSet("CSKLUL CSKLIZ GOD VRDOK BRDOK STATKNJI STATKNJU " + sysdat,
            cGod.and(cMeu.or(cMei)).and(Condition.in("BRDOK", getBrdokList(meskla))));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, mesHead = new HashMap());
  }
  
  public void prepareDataCache() {
    QueryDataSet ds;
    knjHead = new HashSet();
    raProcess.setMessage("Dohvat zaglavlja ulaznih dokumenata ...", true);
    ds = Doku.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK STATKNJ " + sysdat, cCskl.and(cGod).and(cUl));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, ulazHead = new HashMap());
    
    raProcess.setMessage("Dohvat zaglavlja izlaznih dokumenata ...", false);
    ds = doki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK STATKNJ " + sysdat, cCskl.and(cGod).and(cIz));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, izlazHead = new HashMap());
    
    raProcess.setMessage("Dohvat zaglavlja meðuskladišnica ...", false);
    ds = Meskla.getDataModule().getTempSet("CSKLUL CSKLIZ GOD VRDOK BRDOK STATKNJI STATKNJU " + sysdat,
            cGod.and(cMeu.or(cMei)));
    raProcess.openScratchDataSet(ds);
    fillHeaderMap(ds, mesHead = new HashMap());
  }

	/*public boolean checkDetailNotMaster(String cskl, String god, int cart) {

		QueryDataSet qdstdoku = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT * FROM stdoku where cskl='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and cskl||'-'||vrdok||'-'||god||'-'||brdok not in ("
								+ "select cskl||'-'||vrdok||'-'||god||'-'||brdok from doku)",
						true);

		QueryDataSet qdstdoki = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT * FROM stdoki where cskl='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and cskl||'-'||vrdok||'-'||god||'-'||brdok not in ("
								+ "select cskl||'-'||vrdok||'-'||god||'-'||brdok from doki)",
						true);

		QueryDataSet qdstmesklaul = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT * FROM stmeskla where csklul='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and csklul||'-'||cskliz||'-'||vrdok||'-'||god||'-'||brdok not in ("
								+ "select csklul||'-'||cskliz||'-'||vrdok||'-'||god||'-'||brdok from meskla)",
						true);

		QueryDataSet qdstmesklaiz = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT * FROM stmeskla where cskliz='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and csklul||'-'||cskliz||'-'||vrdok||'-'||god||'-'||brdok not in ("
								+ "select csklul||'-'||cskliz||'-'||vrdok||'-'||god||'-'||brdok from meskla)",
						true);

		if (qdstdoku.getRowCount() != 0 && qdstdoki.getRowCount() != 0
				&& qdstmesklaul.getRowCount() != 0
				&& qdstmesklaiz.getRowCount() != 0) {
			if (qdstdoku.getRowCount() != 0) {
				for (qdstdoku.first(); qdstdoku.inBounds(); qdstdoku.next()) {
					insertGreske(qdstdoku, "Ne postoji zaglavlje");
				}
			}
			if (qdstdoki.getRowCount() != 0) {
				for (qdstdoki.first(); qdstdoki.inBounds(); qdstdoki.next()) {
					insertGreske(qdstdoki, "Ne postoji zaglavlje");
				}
			}
			if (qdstmesklaul.getRowCount() != 0) {
				for (qdstmesklaul.first(); qdstmesklaul.inBounds(); qdstmesklaul
						.next()) {
					insertGreske(qdstmesklaul, "Ne postoji zaglavlje");
				}
			}
			if (qdstmesklaiz.getRowCount() != 0) {
				for (qdstmesklaiz.first(); qdstmesklaiz.inBounds(); qdstmesklaiz
						.next()) {
					insertGreske(qdstmesklaiz, "Ne postoji zaglavlje");
				}
			}
			return false;
		}
		return true;
	}*/

	/*public void insertGreske(DataSet ds, String opis) {
		greske.insertRow(true);
		greske.setString("KLJUC", rCD.getKey(ds));
		greske.setString("OPIS", opis);
	}*/

  // prikaz obrade artikla, vrsi se svakih 'delay' milisekundi.
  int delay = 75;
  long nextMessageTime;
  void initMessageTimer() {
    nextMessageTime = System.currentTimeMillis(); 
  }
  void showMessageIfDue(int rbr) {
    if (System.currentTimeMillis() > nextMessageTime) {
      nextMessageTime += delay;
      raProcess.setMessage("Obrada artikla " + rbr + "/" + totalCards + " ...", false);
    }
  }
  
  public dlgErrors getErrors() {
    return errs;
  }
  
  public dlgErrors getFatalErrors() {
    return fatal;
  }
  
  private BigDecimal enc, evc, emc, einab, eimar, eibp, eipor, eisp, eizad;
	public void checkMultiple(Condition arts, boolean autoFix) {
    erroneousCards = erroneousRows = erroneousCells = 0;
    SortDescriptor csort = new SortDescriptor(new String[] {"CART"});
    ulazRows = new HashMap();
    izlazRows = new HashMap();
    mesRows = new HashMap();
    mcskl = new HashMap();
        
    fatal = new dlgErrors(null, "Fatalne greške", true);
    fatal.setData(kartica);

    QueryDataSet ds;
    raProcess.setMessage("Dohvat ulaznih dokumenata ...", false);
    ds = Stdoku.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK RBR CART KOL NC VC MC ZC "+
          "INAB IMAR IBP IPOR ISP IZAD SKOL SVC SMC DIOPORMAR DIOPORPOR PORAV RBSID STATUS",
          cCskl.and(cGod).and(cUl).and(arts));
    raProcess.openScratchDataSet(ds);
    ds.setSort(csort);
    ulazi = ds;

    raProcess.setMessage("Dohvat izlaznih dokumenata ...", false);
    ds = stdoki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK RBR CART KOL NC VC MC ZC "+
          "INAB IMAR IBP IPOR ISP IRAZ ITKAL VEZA RBSID", 
          cCskl.and(cGod).and(cIz).and(arts));
    raProcess.openScratchDataSet(ds);
    ds.setSort(csort);
    izlazi = ds;
    
    raProcess.setMessage("Dohvat meðuskladišnica ...", false);
    System.out.println(cGod.and(cMeu.or(cMei)));
    ds = Stmeskla.getDataModule().getTempSet("CSKLUL CSKLIZ GOD VRDOK BRDOK RBR CART KOL NC VC MC "+
          "ZCUL INABUL IMARUL IPORUL ZADRAZUL ZC INABIZ IMARIZ IPORIZ ZADRAZIZ SKOL SVC SMC DIOPORMAR DIOPORPOR PORAV ITKAL RBSID",
          cGod.and(cMeu.or(cMei)).and(arts));
    raProcess.openScratchDataSet(ds);
    ds.setSort(csort);
    meskla = ds;

    raProcess.setMessage("Formiranje popisa artikala za obradu ...", false);
    Set cartList = new TreeSet();
    for (ulazi.first(); ulazi.inBounds(); ulazi.next())
      cartList.add(new Integer(ulazi.getInt("CART")));

    raProcess.checkClosing();
    for (izlazi.first(); izlazi.inBounds(); izlazi.next())
      cartList.add(new Integer(izlazi.getInt("CART")));

    raProcess.checkClosing();
    for (meskla.first(); meskla.inBounds(); meskla.next())
      cartList.add(new Integer(meskla.getInt("CART")));

    totalCards = cartList.size();
    System.out.println(cartList.size());
    // ako je odabran samo jedan artikl, keševi zaglavlja su prazni, pa ih
    // treba napuniti samo sa potrebnim dokumnetima.
    if (totalCards == 1)
      prepareHeaderForSingle();
    else {
      errs = new dlgErrors(null, "Greške na karticama", true);
      errs.setData(new Column[] {
         dM.createBigDecimalColumn("NC", 2),
         dM.createBigDecimalColumn("VC", 2),
         dM.createBigDecimalColumn("MC", 2),
         dM.createBigDecimalColumn("INAB", 2),
         dM.createBigDecimalColumn("IMAR", 2),
         dM.createBigDecimalColumn("IBP", 2),
         dM.createBigDecimalColumn("IPOR", 2),
         dM.createBigDecimalColumn("ISP", 2),
         dM.createBigDecimalColumn("IZAD", 2)
      });
      errs.setSize(800, 480);
    }
    
    lookupData ld = lookupData.getlookupData();
   
    int obr = 1;
    initMessageTimer();

    for (Iterator i = cartList.iterator(); i.hasNext(); obr++) {
      ulazRows.clear();
      izlazRows.clear();
      mesRows.clear();
      enc = evc = emc = einab = eimar = eibp = eipor = eisp = eizad = _Main.nul;
      
      raProcess.checkClosing();
      int cart = ((Integer) i.next()).intValue();
      showMessageIfDue(obr);
      
      karticatmp.empty();
      karticatmp.setSort(null);
      kartica.empty();
      
      if (ld.raLocate(ulazi, "CART", Integer.toString(cart)))
        fillKartica(cart, ulazi, ulazHead, ulazRows);
      
      if (ld.raLocate(meskla, "CART", Integer.toString(cart)))
        fillKartica(cart, meskla, mesHead, mesRows);
      
      if (ld.raLocate(izlazi, "CART", Integer.toString(cart)))
        fillKartica(cart, izlazi, izlazHead, izlazRows);
      showMessageIfDue(obr);
      
      karticatmp.setSort(new SortDescriptor(new String[] { "DATDOK"}));
      showMessageIfDue(obr);
      
      refillKartica();
      clearKarticaTmp();
      
      raProcess.checkClosing();
      showMessageIfDue(obr);
      firstRow = true;
      for (kartica.first(); kartica.inBounds(); kartica.next()) {
        recalculKartica();
      }
      showMessageIfDue(obr);
      if (!isKarticaOK()) {
        ++erroneousCards;
        showMessageIfDue(obr);
        if (autoFix) popravi();
        if (totalCards != 1)
          errs.addError("Totali grešaka kartice "+cart, 
              new Object[] {enc, evc, emc, einab, eimar, eibp, eipor, eisp, eizad});
      }
    }
	}
  
  public boolean saveAllChanges() {
    return raTransaction.saveChangesInTransaction(new QueryDataSet[] {ulazi, izlazi, meskla});
  }

	public void Izlaz() {
	  
	    if (firstRow) {
	      karticatmp.setBigDecimal("NC_GOOD", kartica.getBigDecimal("NC"));
	      karticatmp.setBigDecimal("VC_GOOD", kartica.getBigDecimal("VC"));
	      karticatmp.setBigDecimal("MC_GOOD", kartica.getBigDecimal("MC"));
	      karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("ZC"));
	    }
	    boolean knj = knjHead.contains(getHeaderKey(kartica));
	    
		karticatmp.setBigDecimal("KOL_TRENUTNO", karticatmp.getBigDecimal(
				"KOL_TRENUTNO").subtract(kartica.getBigDecimal("KOL")));
		
		maxMinus = maxMinus.min(karticatmp.getBigDecimal("KOL_TRENUTNO"));

		if (knj) Aus.sub(karticatmp, "IZAD_TRENUTNO", kartica, "IZAD");
		else karticatmp.setBigDecimal("IZAD_TRENUTNO", karticatmp.getBigDecimal(
				"IZAD_TRENUTNO").subtract(
				kartica.getBigDecimal("KOL").multiply(
						karticatmp.getBigDecimal("ZC_GOOD"))));

		kartica.setBigDecimal("KOL_TRENUTNO", karticatmp
				.getBigDecimal("KOL_TRENUTNO"));
		kartica.setBigDecimal("IZAD_TRENUTNO", karticatmp
				.getBigDecimal("IZAD_TRENUTNO"));

		if (knj) {
			kartica.setBigDecimal("NC_GOOD", kartica.getBigDecimal("NC"));
			kartica.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("ZC"));
		} else if (vrzal.equalsIgnoreCase("N")) {
			kartica.setBigDecimal("NC_GOOD", karticatmp
					.getBigDecimal("ZC_GOOD"));
		} else {
			kartica.setBigDecimal("NC_GOOD", karticatmp
					.getBigDecimal("NC_GOOD"));
		}
/*System.out.println("U izlazima !!! "+karticatmp
					.getBigDecimal("ZC_GOOD"));*/		
		if (knj) {
			kartica.setBigDecimal("VC_GOOD", kartica.getBigDecimal("VC"));
			kartica.setBigDecimal("MC_GOOD", kartica.getBigDecimal("MC"));
		} else {
			kartica.setBigDecimal("VC_GOOD", karticatmp.getBigDecimal("VC_GOOD"));
			kartica.setBigDecimal("MC_GOOD", karticatmp.getBigDecimal("MC_GOOD"));
			kartica.setBigDecimal("ZC_GOOD", karticatmp.getBigDecimal("ZC_GOOD"));
		}

		if (knj) Aus.sub(karticatmp, "INAB_TRENUTNO", kartica, "INAB");
		else karticatmp.setBigDecimal("INAB_TRENUTNO", (karticatmp
				.getBigDecimal("INAB_TRENUTNO").subtract(kartica.getBigDecimal(
				"KOL").multiply(kartica.getBigDecimal("NC_GOOD")))).setScale(2,
				BigDecimal.ROUND_HALF_UP));

		/*
		 * if (vrzal.equalsIgnoreCase("N")) { if
		 * (karticatmp.getBigDecimal("KOL_TRENUTNO").doubleValue()>0) {
		 * karticatmp.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("IZAD_TRENUTNO").divide(
		 * kartica.getBigDecimal("KOL_TRENUTNO"),2,BigDecimal.ROUND_HALF_UP));
		 * kartica.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("ZC_GOOD")); } }
		 * else if (vrzal.equalsIgnoreCase("V")) {
		 * karticatmp.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("VC"));
		 * kartica.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("VC")); } else
		 * if (vrzal.equalsIgnoreCase("M")) {
		 * karticatmp.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("MC"));
		 * kartica.setBigDecimal("ZC_GOOD",kartica.getBigDecimal("MC")); }
		 */
		if (knj) {
			Aus.set(kartica, "INAB_GOOD", "INAB");
			Aus.set(kartica, "IBP_GOOD", "IBP");
			Aus.set(kartica, "ISP_GOOD", "ISP");
			Aus.set(kartica, "IZAD_GOOD", "IZAD");
			Aus.set(kartica, "IMAR_GOOD", "IMAR");
			Aus.set(kartica, "IPOR_GOOD", "IPOR");
		} else {
			kartica.setBigDecimal("INAB_GOOD", (kartica.getBigDecimal("KOL")
					.multiply(kartica.getBigDecimal("NC_GOOD"))).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			kartica.setBigDecimal("IBP_GOOD", (kartica.getBigDecimal("KOL")
					.multiply(kartica.getBigDecimal("VC_GOOD"))).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			kartica.setBigDecimal("ISP_GOOD", (kartica.getBigDecimal("KOL")
					.multiply(kartica.getBigDecimal("MC_GOOD"))).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			kartica.setBigDecimal("IZAD_GOOD", (kartica.getBigDecimal("KOL")
					.multiply(kartica.getBigDecimal("ZC_GOOD"))).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			if (vrzal.equalsIgnoreCase("N")) {
				kartica.setBigDecimal("IMAR_GOOD", Aus.zero2);
				kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
			} else if (vrzal.equalsIgnoreCase("V")) {
				kartica.setBigDecimal("IMAR_GOOD", kartica
						.getBigDecimal("IBP_GOOD").subtract(
								kartica.getBigDecimal("INAB_GOOD")));
				kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
			} else if (vrzal.equalsIgnoreCase("M")) {
				kartica.setBigDecimal("IMAR_GOOD", kartica
						.getBigDecimal("IBP_GOOD").subtract(
								kartica.getBigDecimal("INAB_GOOD")));
				kartica.setBigDecimal("IPOR_GOOD", kartica
						.getBigDecimal("ISP_GOOD").subtract(
								kartica.getBigDecimal("IBP_GOOD")));
			}
		}
	}

	public void Porav() {

		BigDecimal tmpBD = Aus.zero2;
		BigDecimal tmpBD1 = Aus.zero2;

		kartica.setBigDecimal("VC_GOOD", kartica.getBigDecimal("VC"));
		kartica.setBigDecimal("MC_GOOD", kartica.getBigDecimal("MC"));
		kartica.setBigDecimal("KOL_TRENUTNO", karticatmp
            .getBigDecimal("KOL_TRENUTNO"));

		if (vrzal.equalsIgnoreCase("N")) {
			kartica.setBigDecimal("IMAR_GOOD", Aus.zero2);
			kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
		} else if (vrzal.equalsIgnoreCase("V")) {
			tmpBD = kartica.getBigDecimal("KOL_TRENUTNO").multiply(
					kartica.getBigDecimal("SVC"));
			tmpBD1 = kartica.getBigDecimal("KOL_TRENUTNO").multiply(
					kartica.getBigDecimal("VC"));
			kartica.setBigDecimal("IMAR_GOOD", tmpBD1.subtract(tmpBD));
			kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
		} else if (vrzal.equalsIgnoreCase("M")) {
			//      tmpBD=
			// kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("SVC"));
			//      tmpBD1=
			// kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("VC"));
			//      kartica.setBigDecimal("IMAR_GOOD",tmpBD1.subtract(tmpBD));
			tmpBD = kartica.getBigDecimal("VC").subtract(
					kartica.getBigDecimal("SVC"));
			tmpBD1 = kartica.getBigDecimal("KOL_TRENUTNO").multiply(tmpBD);
			kartica.setBigDecimal("IMAR_GOOD", tmpBD1);
			//      tmpBD=
			// kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("SMC"));
			//      tmpBD1=
			// kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("MC"));
			//      kartica.setBigDecimal("IPOR_GOOD",tmpBD1.subtract(tmpBD).subtract(kartica.getBigDecimal("IMAR_GOOD")));
			tmpBD = kartica.getBigDecimal("MC").subtract(
					kartica.getBigDecimal("SMC"));
			tmpBD1 = kartica.getBigDecimal("KOL_TRENUTNO").multiply(tmpBD);
			kartica.setBigDecimal("IPOR_GOOD", tmpBD1.subtract(kartica
					.getBigDecimal("IMAR_GOOD")));
		}
		kartica.setBigDecimal("IZAD_GOOD", kartica.getBigDecimal("IMAR_GOOD")
				.add(kartica.getBigDecimal("IPOR_GOOD")));
		karticatmp.setBigDecimal("IZAD_TRENUTNO", karticatmp.getBigDecimal(
				"IZAD_TRENUTNO").add(kartica.getBigDecimal("IZAD_GOOD")));
		kartica.setBigDecimal("IZAD_TRENUTNO", karticatmp
				.getBigDecimal("IZAD_TRENUTNO"));

		kartica.setBigDecimal("VC", kartica.getBigDecimal("SVC"));
		kartica.setBigDecimal("MC", kartica.getBigDecimal("SMC"));

	}

	public void NulaKarticaGood() {
		kartica.setBigDecimal("NC_GOOD", Aus.zero2);
		kartica.setBigDecimal("VC_GOOD", Aus.zero2);
		kartica.setBigDecimal("MC_GOOD", Aus.zero2);
		kartica.setBigDecimal("ZC_GOOD", Aus.zero2);
		kartica.setBigDecimal("INAB_GOOD", Aus.zero2);
		kartica.setBigDecimal("IBP_GOOD", Aus.zero2);
		kartica.setBigDecimal("ISP_GOOD", Aus.zero2);
		kartica.setBigDecimal("IZAD_GOOD", Aus.zero2);
		kartica.setBigDecimal("IMAR_GOOD", Aus.zero2);
		kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
	}

	public void Ulaz() {

		// ulazi su svetinja !!!
		
		if (kartica.getString("VRDOK").equalsIgnoreCase("INV")){
			Izlaz();
			return;
		}

		if (kartica.getString("VRDOK").equalsIgnoreCase("DPR"))
			System.out.println("Greska ulaz");

		if (TypeDoc.getTypeDoc().isDocSklad(kartica.getString("VRDOK"))) {
			NulaKarticaGood();
		}
		karticatmp.setBigDecimal("KOL_TRENUTNO", karticatmp.getBigDecimal(
				"KOL_TRENUTNO").add(kartica.getBigDecimal("KOL")));

		karticatmp.setBigDecimal("VC_GOOD", kartica.getBigDecimal("VC"));
		karticatmp.setBigDecimal("MC_GOOD", kartica.getBigDecimal("MC"));
		kartica.setBigDecimal("NC_GOOD", kartica.getBigDecimal("NC"));
		kartica.setBigDecimal("VC_GOOD", kartica.getBigDecimal("VC"));
		kartica.setBigDecimal("MC_GOOD", kartica.getBigDecimal("MC"));
//		kartica.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("ZC"));
		kartica.setBigDecimal("KOL_TRENUTNO", karticatmp
				.getBigDecimal("KOL_TRENUTNO"));
		
		
		
		karticatmp.setBigDecimal("INAB_TRENUTNO", karticatmp.getBigDecimal(
				"INAB_TRENUTNO").add(kartica.getBigDecimal("INAB")));

		karticatmp.setBigDecimal("IZAD_TRENUTNO", karticatmp.getBigDecimal(
				"IZAD_TRENUTNO").add(kartica.getBigDecimal("IZAD")));

		kartica.setBigDecimal("INAB_GOOD", kartica.getBigDecimal("INAB"));
		kartica.setBigDecimal("IBP_GOOD", kartica.getBigDecimal("IBP"));
		kartica.setBigDecimal("ISP_GOOD", kartica.getBigDecimal("ISP"));
		kartica.setBigDecimal("IZAD_GOOD", kartica.getBigDecimal("IZAD"));
		kartica.setBigDecimal("IZAD_TRENUTNO", karticatmp
				.getBigDecimal("IZAD_TRENUTNO"));
		
		{
    		BigDecimal ainab = karticatmp.getBigDecimal("INAB_TRENUTNO");
    		BigDecimal akol = karticatmp.getBigDecimal("KOL_TRENUTNO");
    		
    		if (kartica.getBigDecimal("INAB").signum() > 0 && kartica.getBigDecimal("KOL").signum() > 0) {    		  
    		// ako smo bili u minusu:
              if (kartica.getBigDecimal("KOL").compareTo(akol) > 0) {
                karticatmp.setBigDecimal("NC_GOOD", kartica.getBigDecimal("NC").add(
                    ainab.subtract(akol.multiply(kartica.getBigDecimal("NC"))).
                      divide(kartica.getBigDecimal("KOL").subtract(maxMinus), 2, BigDecimal.ROUND_HALF_UP)));
                
                /*ainab = ainab.subtract(kartica.getBigDecimal("INAB")).negate().add(kartica.getBigDecimal("INAB"));
                akol = akol.subtract(kartica.getBigDecimal("KOL")).negate().add(kartica.getBigDecimal("KOL"));*/
              } else 
               karticatmp.setBigDecimal("NC_GOOD", ainab.divide(akol, 2, BigDecimal.ROUND_HALF_UP));
    		}
    		
    		/*if (kartica.getBigDecimal("INAB").signum() * kartica.getBigDecimal("KOL").signum() == 1 &&
    		    ainab.abs().doubleValue() < kartica.getBigDecimal("INAB").abs().doubleValue() * 100 &&
    		    akol.abs().doubleValue() < kartica.getBigDecimal("KOL").abs().doubleValue() * 100) {
    		
        		while ((akol.add(akol)).abs().compareTo(kartica.getBigDecimal("KOL").abs()) < 0 ||
        		    (ainab.add(ainab)).abs().compareTo(kartica.getBigDecimal("INAB").abs()) < 0 ||
        		    akol.signum() != ainab.signum()) {
        		  akol = akol.add(kartica.getBigDecimal("KOL"));
        		  ainab = ainab.add(kartica.getBigDecimal("INAB"));
        		}
        		
        		karticatmp.setBigDecimal("NC_GOOD", ainab.divide(akol, 2, BigDecimal.ROUND_HALF_UP));
    		}*/
		}

		/*if (karticatmp.getBigDecimal("KOL_TRENUTNO").compareTo(kartica.getBigDecimal("KOL")) < 0 
		      && kartica.getBigDecimal("KOL").signum() > 0) {
		  BigDecimal ainab = karticatmp.getBigDecimal("INAB_TRENUTNO").subtract(kartica.getBigDecimal("INAB")).
		                          abs().add(kartica.getBigDecimal("INAB"));
		  BigDecimal akol = karticatmp.getBigDecimal("KOL_TRENUTNO").subtract(kartica.getBigDecimal("KOL")).
		                          abs().add(kartica.getBigDecimal("KOL"));
		  
		  karticatmp.setBigDecimal("NC_GOOD", ainab.divide(akol, 2, BigDecimal.ROUND_HALF_UP));
		  
		} else if (karticatmp.getBigDecimal("KOL_TRENUTNO").signum() != 0) {
			karticatmp.setBigDecimal("NC_GOOD", karticatmp.getBigDecimal(
					"INAB_TRENUTNO").divide(
					karticatmp.getBigDecimal("KOL_TRENUTNO"), 2,
					BigDecimal.ROUND_HALF_UP));
		} else {
			karticatmp.setBigDecimal("NC_GOOD", kartica.getBigDecimal("ZC"));
		}*/
		//
//System.out.println("1.Kartica tmp za "+kartica.getString("VRDOK")+" "+
//				karticatmp.getBigDecimal("ZC_GOOD"));
		
		
		if (vrzal.equalsIgnoreCase("N")) {
		  
		    BigDecimal aizad = karticatmp.getBigDecimal("IZAD_TRENUTNO");
	        BigDecimal akol = karticatmp.getBigDecimal("KOL_TRENUTNO");
	        
	        if (kartica.getBigDecimal("IZAD").signum() > 0 && kartica.getBigDecimal("KOL").signum() > 0) {
	          
	          if (kartica.getBigDecimal("KOL").compareTo(akol) > 0) {
                karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("ZC").add(
                    aizad.subtract(akol.multiply(kartica.getBigDecimal("ZC"))).
                      divide(kartica.getBigDecimal("KOL").subtract(maxMinus), 2, BigDecimal.ROUND_HALF_UP)));
              } else 
               karticatmp.setBigDecimal("ZC_GOOD", aizad.divide(akol, 2, BigDecimal.ROUND_HALF_UP));

	        }
	        
	        /*if (kartica.getBigDecimal("IZAD").signum() * kartica.getBigDecimal("KOL").signum() == 1 &&
                aizad.abs().doubleValue() < kartica.getBigDecimal("IZAD").abs().doubleValue() * 100 &&
                akol.abs().doubleValue() < kartica.getBigDecimal("KOL").abs().doubleValue() * 100) {	        
    	        while ((akol.add(akol)).abs().compareTo(kartica.getBigDecimal("KOL").abs()) < 0 ||
    	            (aizad.add(aizad)).abs().compareTo(kartica.getBigDecimal("IZAD").abs()) < 0 ||
    	            akol.signum() != aizad.signum()) {
    	          akol = akol.add(kartica.getBigDecimal("KOL"));
    	          aizad = aizad.add(kartica.getBigDecimal("IZAD"));
    	        }
    	        
    	        karticatmp.setBigDecimal("ZC_GOOD", aizad.divide(akol, 2, BigDecimal.ROUND_HALF_UP));
	        }*/
		  
		    /*if (karticatmp.getBigDecimal("KOL_TRENUTNO").compareTo(kartica.getBigDecimal("KOL")) < 0 
	              && kartica.getBigDecimal("KOL").signum() > 0) {
		      BigDecimal aizad = karticatmp.getBigDecimal("IZAD_TRENUTNO").subtract(kartica.getBigDecimal("IZAD")).
                                    abs().add(kartica.getBigDecimal("IZAD"));
		      BigDecimal akol = karticatmp.getBigDecimal("KOL_TRENUTNO").subtract(kartica.getBigDecimal("KOL")).
                                    abs().add(kartica.getBigDecimal("KOL"));
		      
		       karticatmp.setBigDecimal("ZC_GOOD", aizad.divide(akol, 2, BigDecimal.ROUND_HALF_UP));
		    } else if (karticatmp.getBigDecimal("KOL_TRENUTNO").signum() != 0) {
				karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal(
						"IZAD_TRENUTNO").divide(
						kartica.getBigDecimal("KOL_TRENUTNO"), 2,
						BigDecimal.ROUND_HALF_UP));
			} else {
			  karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("ZC"));
			}*/
		} else if (vrzal.equalsIgnoreCase("V")) {
			karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("VC"));
		} else if (vrzal.equalsIgnoreCase("M")) {
			karticatmp.setBigDecimal("ZC_GOOD", kartica.getBigDecimal("MC"));
		}
		

		/*
		 * kartica.setBigDecimal("INAB_GOOD",
		 * kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("NC")));
		 * kartica.setBigDecimal("IBP_GOOD",
		 * kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("VC_GOOD")));
		 * kartica.setBigDecimal("ISP_GOOD",
		 * kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("MC_GOOD")));
		 * kartica.setBigDecimal("IZAD_GOOD",
		 * kartica.getBigDecimal("KOL").multiply(kartica.getBigDecimal("ZC_GOOD")));
		 * karticatmp.setBigDecimal("IZAD_TRENUTNO",
		 * karticatmp.getBigDecimal("IZAD_TRENUTNO").
		 * add(kartica.getBigDecimal("IZAD_GOOD")));
		 * kartica.setBigDecimal("IZAD_TRENUTNO",karticatmp.getBigDecimal("IZAD_TRENUTNO"));
		 */
		if (vrzal.equalsIgnoreCase("N")) {
			kartica.setBigDecimal("IMAR_GOOD", Aus.zero2);
			kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
		} else if (vrzal.equalsIgnoreCase("V")) {
			kartica.setBigDecimal("IMAR_GOOD", kartica
					.getBigDecimal("IBP_GOOD").subtract(
							kartica.getBigDecimal("INAB_GOOD")));
			kartica.setBigDecimal("IPOR_GOOD", Aus.zero2);
		} else if (vrzal.equalsIgnoreCase("M")) {
			kartica.setBigDecimal("IMAR_GOOD", kartica
					.getBigDecimal("IBP_GOOD").subtract(
							kartica.getBigDecimal("INAB_GOOD")));
			kartica.setBigDecimal("IPOR_GOOD", kartica
					.getBigDecimal("ISP_GOOD").subtract(
							kartica.getBigDecimal("IBP_GOOD")));
		}
//		System.out.println("2.Kartica tmp za "+kartica.getString("VRDOK")+" "+
//				karticatmp.getBigDecimal("ZC_GOOD"));
		kartica.setBigDecimal("ZC_GOOD", karticatmp.getBigDecimal("ZC_GOOD"));
	}

	public void clearKarticaTmp() {
		karticatmp.empty();
/*		karticatmp.close();
		karticatmp.open();*/
		karticatmp.insertRow(false);
	}

	public void recalculKartica() {
		if (TypeDoc.getTypeDoc().isDocStmeskla(kartica.getString("VRDOK"))) {
			if ((kartica.getString("VRDOK").equalsIgnoreCase("MES") && kartica
					.getString("CSKLUL").equalsIgnoreCase(cskl))
					|| kartica.getString("VRDOK").equalsIgnoreCase("MEU")) {
				Ulaz();
			} else {
				Izlaz();
			}
		} else if (TypeDoc.getTypeDoc().isDocStdoku(kartica.getString("VRDOK"))) {
			Ulaz();
		} else if (TypeDoc.getTypeDoc().isDocStdoki(kartica.getString("VRDOK"))) {
			Izlaz();
		} else if (kartica.getString("VRDOK").equalsIgnoreCase("DPR")) { // specijal
																		 // case
																		 // za
																		 // poravnanja
																		 // po
																		 // dokumentu
			Porav();
		}
		firstRow = false;
	}

	public void kartica2cloneKartica(DataSet orgKartica, DataSet cloneKartica) {
		orgKartica.setString("CSKLUL", cloneKartica.getString("CSKLUL"));
		orgKartica.setString("CSKLIZ", cloneKartica.getString("CSKLIZ"));
		orgKartica.setString("CSKL", cloneKartica.getString("CSKL"));
		orgKartica.setString("GOD", cloneKartica.getString("GOD"));
		orgKartica.setString("VRDOK", cloneKartica.getString("VRDOK"));
		orgKartica.setString("VD", cloneKartica.getString("VD"));
		orgKartica.setInt("BRDOK", cloneKartica.getInt("BRDOK"));
		orgKartica.setShort("RBR", cloneKartica.getShort("RBR"));
		orgKartica.setTimestamp("DATDOK", new Timestamp(cloneKartica.getTimestamp("DATDOK").getTime()));
		orgKartica.setBigDecimal("KOL", cloneKartica.getBigDecimal("KOL"));
		orgKartica.setBigDecimal("NC", cloneKartica.getBigDecimal("NC"));
		orgKartica.setBigDecimal("INAB", cloneKartica.getBigDecimal("INAB"));
		orgKartica.setBigDecimal("IMAR", cloneKartica.getBigDecimal("IMAR"));
		orgKartica.setBigDecimal("IBP", cloneKartica.getBigDecimal("IBP"));
		orgKartica.setBigDecimal("IPOR", cloneKartica.getBigDecimal("IPOR"));
		orgKartica.setBigDecimal("ISP", cloneKartica.getBigDecimal("ISP"));
		orgKartica.setBigDecimal("VC", cloneKartica.getBigDecimal("VC"));
		orgKartica.setBigDecimal("MC", cloneKartica.getBigDecimal("MC"));
		orgKartica.setBigDecimal("SVC", cloneKartica.getBigDecimal("SVC"));
		orgKartica.setBigDecimal("SMC", cloneKartica.getBigDecimal("SMC"));
		orgKartica.setBigDecimal("ZC", cloneKartica.getBigDecimal("ZC"));
		orgKartica.setString("TKAL", cloneKartica.getString("TKAL"));
		orgKartica.setBigDecimal("IZAD", cloneKartica.getBigDecimal("IZAD"));
	}

	public void refillKartica() {

		karticatmp.first();
		//    java.sql.Timestamp ddok = karticatmp.getTimestamp("DATDOK");
		for (karticatmp.first(); karticatmp.inBounds(); karticatmp.next()) {
			if (TypeDoc.getTypeDoc().isDocUlaz(karticatmp.getString("VRDOK"))) {
				if (karticatmp.getString("VRDOK").equalsIgnoreCase("MES")
						&& karticatmp.getString("CSKLIZ").equalsIgnoreCase(
								this.cskl)) {
					//          kartica.last();
					kartica.insertRow(false);
					kartica2cloneKartica(kartica, karticatmp);
				} else {
					if (lD.raLocate(kartica, "TKAL", karticatmp
							.getString("TKAL"))) {
						kartica.insertRow(true);
						kartica2cloneKartica(kartica, karticatmp);
					} else {
						kartica.last();
						kartica.insertRow(false);
						kartica2cloneKartica(kartica, karticatmp);
					}
					//          else {
					//            kartica.interactiveLocate(karticatmp.getString("TKAL"));
					//            lD.raLookup(kartica,"TKAL",karticatmp.getString("TKAL"));
					//
					//          }
				}
			} else {
				kartica.last();
				kartica.insertRow(false);
				kartica2cloneKartica(kartica, karticatmp);
			}
		}
	}

	public void insertNivelacija(DataSet ds, Timestamp datum) {
		karticatmp.insertRow(false);
		if (TypeDoc.getTypeDoc().isDocStmeskla(ds.getString("VRDOK"))) {
			karticatmp.setString("CSKLUL", ds.getString("CSKLUL"));
			karticatmp.setString("CSKLIZ", ds.getString("CSKLIZ"));
		} else {
			karticatmp.setString("CSKL", ds.getString("CSKL"));
		}
		karticatmp.setString("GOD", ds.getString("GOD"));
		karticatmp.setString("VD", ds.getString("VRDOK"));
		karticatmp.setString("VRDOK", "DPR");
		karticatmp.setInt("BRDOK", ds.getInt("BRDOK"));
		karticatmp.setShort("RBR", ds.getShort("RBR"));
		karticatmp.setTimestamp("DATDOK", new Timestamp(datum.getTime() - 1));
		karticatmp.setBigDecimal("KOL", ds.getBigDecimal("SKOL"));

		karticatmp.setBigDecimal("SVC", ds.getBigDecimal("SVC"));
		karticatmp.setBigDecimal("VC", ds.getBigDecimal("VC"));
		karticatmp.setBigDecimal("MC", ds.getBigDecimal("MC"));
		karticatmp.setBigDecimal("SMC", ds.getBigDecimal("SMC"));
		karticatmp.setBigDecimal("IMAR", ds.getBigDecimal("DIOPORMAR"));
		karticatmp.setBigDecimal("IPOR", ds.getBigDecimal("DIOPORPOR"));
		karticatmp.setBigDecimal("IZAD", ds.getBigDecimal("PORAV"));
	}

	public void fillKartica(int cart, DataSet ds, Map zag, Map stav) {
    
    for (; ds.inBounds() && ds.getInt("CART") == cart; ds.next()) {		
			/*if (!TypeDoc.getTypeDoc().isDocDiraZalihu(ds.getString("VRDOK"))) {
				continue;
			}*/
      if (ds.getString("VRDOK").equals("PRI") && ds.getString("STATUS").equals("N")) continue;
      
      String key = getHeaderKey(ds);
      if (!zag.containsKey(key)) {
        fatal.addError("Nema zaglavlja za stavku "+ds.getString("VRDOK")+
            "-"+ds.getInt("BRDOK")+"/"+ds.getShort("RBR"));
        System.err.println("Nema zaglavlja za stavku "+ds);
        continue;
      }
      stav.put(getRowKey(ds), new Integer(ds.getRow()));
      
            if (ds.hasColumn("PORAV") != null) {
              if (ds.getBigDecimal("PORAV").doubleValue() != 0
                      || ds.getBigDecimal("DIOPORPOR").doubleValue() != 0
                      || ds.getBigDecimal("DIOPORMAR").doubleValue() != 0 || "PRK KAL POR MES MEU".indexOf(ds.getString("VRDOK"))>=0) {
                  insertNivelacija(ds, (Timestamp) zag.get(key));
              }
          }   
            
            int add = 0;
            if (TypeDoc.getTypeDoc().isDocStmeskla(ds.getString("VRDOK"))) {
              if ((kartica.getString("VRDOK").equalsIgnoreCase("MES") && kartica
                      .getString("CSKLIZ").equalsIgnoreCase(cskl))
                      || kartica.getString("VRDOK").equalsIgnoreCase("MEI")) add = 1;
            } else if (TypeDoc.getTypeDoc().isDocStdoki(kartica.getString("VRDOK"))) add = 1;
      
      
			karticatmp.insertRow(false);
			karticatmp.setString("GOD", ds.getString("GOD"));
			karticatmp.setString("VRDOK", ds.getString("VRDOK"));
			karticatmp.setInt("BRDOK", ds.getInt("BRDOK"));
			karticatmp.setShort("RBR", ds.getShort("RBR"));
			karticatmp.setTimestamp("DATDOK", new Timestamp(((Timestamp) zag.get(key)).getTime() + add));
			karticatmp.setBigDecimal("KOL", ds.getBigDecimal("KOL"));

			if (TypeDoc.getTypeDoc().isDocStmeskla(ds.getString("VRDOK"))) {
				karticatmp.setString("CSKLUL", ds.getString("CSKLUL"));
				karticatmp.setString("CSKLIZ", ds.getString("CSKLIZ"));
				karticatmp.setBigDecimal("NC", ds.getBigDecimal("NC"));
				karticatmp.setBigDecimal("VC", ds.getBigDecimal("VC"));
				karticatmp.setBigDecimal("MC", ds.getBigDecimal("MC"));
				if (ds.getString("CSKLUL").equalsIgnoreCase(this.cskl)) {
					//karticatmp.setString("ZASORT","A");
					karticatmp.setBigDecimal("ZC", ds.getBigDecimal("ZCUL"));
					karticatmp
							.setBigDecimal("INAB", ds.getBigDecimal("INABUL"));
					karticatmp
							.setBigDecimal("IMAR", ds.getBigDecimal("IMARUL"));
					karticatmp.setBigDecimal("IBP", ds.getBigDecimal("INABUL")
							.add(ds.getBigDecimal("IMARUL")));
					karticatmp
							.setBigDecimal("IPOR", ds.getBigDecimal("IPORUL"));
					karticatmp.setBigDecimal("ISP", ds.getBigDecimal("INABUL")
							.add(ds.getBigDecimal("IMARUL")).add(
									ds.getBigDecimal("IPORUL")));
					karticatmp.setBigDecimal("IZAD", ds
							.getBigDecimal("ZADRAZUL"));
					karticatmp.setString("TKAL", rCD.getKey(ds, new String[] {
							"CSKLIZ", "CSKLUL", "VRDOK", "GOD", "BRDOK",
							"rbsid" }, "stmeskla"));
				} else {
					//karticatmp.setString("ZASORT","B");
					karticatmp.setBigDecimal("ZC", ds.getBigDecimal("ZC"));
					karticatmp
							.setBigDecimal("INAB", ds.getBigDecimal("INABIZ"));
					karticatmp
							.setBigDecimal("IMAR", ds.getBigDecimal("IMARIZ"));
					karticatmp.setBigDecimal("IBP", ds.getBigDecimal("INABIZ")
							.add(ds.getBigDecimal("IMARIZ")));
					karticatmp
							.setBigDecimal("IPOR", ds.getBigDecimal("IPORIZ"));
					karticatmp.setBigDecimal("ISP", ds.getBigDecimal("INABIZ")
							.add(ds.getBigDecimal("IMARIZ")).add(
									ds.getBigDecimal("IPORIZ")));
					karticatmp.setBigDecimal("IZAD", ds
							.getBigDecimal("ZADRAZIZ"));
					karticatmp.setString("TKAL", ds.getString("ITKAL"));
				}

			} else {
				karticatmp.setString("CSKL", ds.getString("CSKL"));
				karticatmp.setBigDecimal("NC", ds.getBigDecimal("NC"));
				karticatmp.setBigDecimal("VC", ds.getBigDecimal("VC"));
				karticatmp.setBigDecimal("MC", ds.getBigDecimal("MC"));
				karticatmp.setBigDecimal("ZC", ds.getBigDecimal("ZC"));
				karticatmp.setBigDecimal("INAB", ds.getBigDecimal("INAB"));
				karticatmp.setBigDecimal("IMAR", ds.getBigDecimal("IMAR"));
				karticatmp.setBigDecimal("IBP", ds.getBigDecimal("IBP"));
				karticatmp.setBigDecimal("IPOR", ds.getBigDecimal("IPOR"));
				karticatmp.setBigDecimal("ISP", ds.getBigDecimal("ISP"));
				if (TypeDoc.getTypeDoc().isDocStdoki(ds.getString("VRDOK"))) {
					//karticatmp.setString("ZASORT","B");
					karticatmp.setBigDecimal("IZAD", ds.getBigDecimal("IRAZ"));
					karticatmp.setString("TKAL", ds.getString("ITKAL"));
				} else if (TypeDoc.getTypeDoc().isDocStdoku(
						ds.getString("VRDOK"))) {
					//karticatmp.setString("ZASORT","A");
					karticatmp.setBigDecimal("IZAD", ds.getBigDecimal("IZAD"));
					karticatmp.setString("TKAL", rCD.getKey(ds, new String[] {
							"CSKL", "VRDOK", "GOD", "BRDOK", "rbsid" },
							"stdoku"));
				}
			}
		}
	}

	public boolean isSlogGood() {
    buffer.clear();
		kartica.setString("DOBAR", "D");    
		if (kartica.getBigDecimal("NC").compareTo(
				kartica.getBigDecimal("NC_GOOD")) != 0) {
      buffer.append(" NC,");
      enc = enc.add(kartica.getBigDecimal("NC").
          subtract(kartica.getBigDecimal("NC_GOOD")).abs());
      ++erroneousCells;
		}
		if (!kartica.getString("VRDOK").equalsIgnoreCase("DPR")) {
			if (kartica.getBigDecimal("VC").compareTo(
					kartica.getBigDecimal("VC_GOOD")) != 0) {
        buffer.append(" VC,");
        evc = evc.add(kartica.getBigDecimal("VC").
            subtract(kartica.getBigDecimal("VC_GOOD")).abs());
        ++erroneousCells;
			}
			if (kartica.getBigDecimal("MC").compareTo(
					kartica.getBigDecimal("MC_GOOD")) != 0) {
        buffer.append(" MC,");
        emc = emc.add(kartica.getBigDecimal("MC").
            subtract(kartica.getBigDecimal("MC_GOOD")).abs());
        ++erroneousCells;
			}
		} else {
		  if (kartica.getBigDecimal("KOL").compareTo(kartica.getBigDecimal("KOL_TRENUTNO")) != 0) {
		    buffer.append(" KOL,");
		    ++erroneousCells;
		  }
		}

		if (kartica.getBigDecimal("INAB").compareTo(
				kartica.getBigDecimal("INAB_GOOD")) != 0) {
      buffer.append(" INAB,");
      einab = einab.add(kartica.getBigDecimal("INAB").
          subtract(kartica.getBigDecimal("INAB_GOOD")).abs());
      ++erroneousCells;
		}
		if (kartica.getBigDecimal("IMAR").compareTo(
				kartica.getBigDecimal("IMAR_GOOD")) != 0) {
      buffer.append(" IMAR,");
      eimar = eimar.add(kartica.getBigDecimal("IMAR").
          subtract(kartica.getBigDecimal("IMAR_GOOD")).abs());
      ++erroneousCells;
		}
		if (!TypeDoc.getTypeDoc().isDocMeskla(kartica.getString("VRDOK"))) {
			if (kartica.getBigDecimal("IBP").compareTo(
					kartica.getBigDecimal("IBP_GOOD")) != 0) {
        buffer.append(" IBP,");
        eibp = eibp.add(kartica.getBigDecimal("IBP").
            subtract(kartica.getBigDecimal("IBP_GOOD")).abs());
        ++erroneousCells;
			}
			if (kartica.getBigDecimal("ISP").compareTo(
					kartica.getBigDecimal("ISP_GOOD")) != 0) {
        buffer.append(" ISP,");
        eisp = eisp.add(kartica.getBigDecimal("ISP").
            subtract(kartica.getBigDecimal("ISP_GOOD")).abs());
        ++erroneousCells;
			}
		}
		if (kartica.getBigDecimal("IPOR").compareTo(
				kartica.getBigDecimal("IPOR_GOOD")) != 0) {
      buffer.append(" IPOR,");
      eipor = eipor.add(kartica.getBigDecimal("IPOR").
          subtract(kartica.getBigDecimal("IPOR_GOOD")).abs());
      ++erroneousCells;
		}

		if (kartica.getBigDecimal("IZAD").compareTo(
				kartica.getBigDecimal("IZAD_GOOD")) != 0) {
      buffer.append(" IZAD,");
      eizad = eizad.add(kartica.getBigDecimal("IZAD").
          subtract(kartica.getBigDecimal("IZAD_GOOD")).abs());
      ++erroneousCells;
		}
    if (buffer.length() > 0) {
      kartica.setString("DOBAR", "N");
      kartica.setString("OPIS", "Neispravne kolone:" + buffer.chop());
      ++erroneousRows;
      return false;
    }
		return true;
	}

	public boolean isKarticaOK() {

		boolean forReturn = true;
		for (kartica.first(); kartica.inBounds(); kartica.next()) {
			if (!isSlogGood())
				forReturn = false;
		}
		return forReturn;
	}
  
	public void popravi() {
		try {
			for (kartica.first(); kartica.inBounds(); kartica.next()) {
				if (kartica.getString("DOBAR").equalsIgnoreCase("D")) {
					continue;
				}
				if (kartica.getString("VRDOK").equalsIgnoreCase("DPR")) {
				  kartica.setString("VRDOK", kartica.getString("VD"));
				  String key = getRowKey(kartica);
                  kartica.setString("VRDOK", "DPR");
                  
				  if (TypeDoc.getTypeDoc().isDocStmeskla(kartica.getString("VD"))) {
				    if (!mesRows.containsKey(key)) {
                      fatal.addError("Ne mogu pronaæi stavku meðuskladišnice "+key);
                      System.err.println("Weird BUG, can't locate stmeskla "+kartica);
                      continue;
                    }
                    meskla.goToRow(((Integer) mesRows.get(key)).intValue());
                    
                    meskla.setBigDecimal("SKOL", kartica.getBigDecimal("KOL_TRENUTNO"));
                    meskla.setBigDecimal("DIOPORMAR", kartica.getBigDecimal("IMAR_GOOD"));
                    meskla.setBigDecimal("DIOPORPOR", kartica.getBigDecimal("IPOR_GOOD"));
                    meskla.setBigDecimal("PORAV", kartica.getBigDecimal("IZAD_GOOD"));
                    meskla.post();
				  } else {
    		          if (!ulazRows.containsKey(key)) {
    		            fatal.addError("Ne mogu pronaæi stavku ulaza "+key);
    		            System.err.println("Weird BUG, can't locate stdoku "+kartica);
    		            continue;
    		          }
    		          ulazi.goToRow(((Integer) ulazRows.get(key)).intValue());
    		          
    		          ulazi.setBigDecimal("SKOL", kartica.getBigDecimal("KOL_TRENUTNO"));
    		          ulazi.setBigDecimal("DIOPORMAR", kartica.getBigDecimal("IMAR_GOOD"));
    		          ulazi.setBigDecimal("DIOPORPOR", kartica.getBigDecimal("IPOR_GOOD"));
    		          ulazi.setBigDecimal("PORAV", kartica.getBigDecimal("IZAD_GOOD"));
    		          ulazi.post();
				  }
				  
				} else if (TypeDoc.getTypeDoc().isDocStmeskla(
						kartica.getString("VRDOK"))) {
          String key = getRowKey(kartica);
          if (!mesRows.containsKey(key)) {
            fatal.addError("Ne mogu pronaæi stavku meðuskladišnice "+key);
            System.err.println("Weird BUG, can't locate meskla "+kartica);
            continue;
          }
          meskla.goToRow(((Integer) mesRows.get(key)).intValue());
          
          meskla.setBigDecimal("NC", kartica.getBigDecimal("NC_GOOD"));
          meskla.setBigDecimal("VC", kartica.getBigDecimal("VC_GOOD"));
          meskla.setBigDecimal("MC", kartica.getBigDecimal("MC_GOOD"));
					if (kartica.getString("CSKLUL").equalsIgnoreCase(this.cskl)) {
            meskla.setBigDecimal("ZCUL", kartica.getBigDecimal("ZC_GOOD"));
            meskla.setBigDecimal("INABUL", kartica.getBigDecimal("INAB_GOOD"));
            meskla.setBigDecimal("IMARUL", kartica.getBigDecimal("IMAR_GOOD"));
            meskla.setBigDecimal("IPORUL", kartica.getBigDecimal("IPOR_GOOD"));
            meskla.setBigDecimal("ZADRAZUL", kartica.getBigDecimal("IZAD_GOOD"));
					} else {
            meskla.setBigDecimal("ZC", kartica.getBigDecimal("ZC_GOOD"));
            meskla.setBigDecimal("INABIZ", kartica.getBigDecimal("INAB_GOOD"));
            meskla.setBigDecimal("IMARIZ", kartica.getBigDecimal("IMAR_GOOD"));
            meskla.setBigDecimal("IPORIZ", kartica.getBigDecimal("IPOR_GOOD"));
            meskla.setBigDecimal("ZADRAZIZ", kartica.getBigDecimal("IZAD_GOOD"));    
            if (fixMes) {
              meskla.setBigDecimal("ZCUL", kartica.getBigDecimal("ZC_GOOD"));
              meskla.setBigDecimal("INABUL", kartica.getBigDecimal("INAB_GOOD"));
              meskla.setBigDecimal("IMARUL", Aus.zero2);
              meskla.setBigDecimal("IPORUL", Aus.zero2);
              meskla.setBigDecimal("ZADRAZUL", kartica.getBigDecimal("IZAD_GOOD"));
              Timestamp old = (Timestamp) mcskl.get(kartica.getString("CSKLUL"));
              if (old == null || kartica.getTimestamp("DATDOK").before(old))
                  mcskl.put(kartica.getString("CSKLUL"), kartica.getTimestamp("DATDOK"));
              
            }
					}
          meskla.post();
				} else if (TypeDoc.getTypeDoc().isDocStdoki(kartica.getString("VRDOK"))) {
          String key = getRowKey(kartica);
          if (!izlazRows.containsKey(key)) {
            fatal.addError("Ne mogu pronaæi stavku izlaza "+key);
            System.err.println("Weird BUG, can't locate stdoki "+kartica);
            continue;
          }
          izlazi.goToRow(((Integer) izlazRows.get(key)).intValue());

          izlazi.setBigDecimal("NC", kartica.getBigDecimal("NC_GOOD"));
          izlazi.setBigDecimal("VC", kartica.getBigDecimal("VC_GOOD"));
          izlazi.setBigDecimal("MC", kartica.getBigDecimal("MC_GOOD"));
          izlazi.setBigDecimal("ZC", kartica.getBigDecimal("ZC_GOOD"));
          izlazi.setBigDecimal("INAB", kartica.getBigDecimal("INAB_GOOD"));
          izlazi.setBigDecimal("IMAR", kartica.getBigDecimal("IMAR_GOOD"));
          izlazi.setBigDecimal("IBP", kartica.getBigDecimal("IBP_GOOD"));
          izlazi.setBigDecimal("IPOR", kartica.getBigDecimal("IPOR_GOOD"));
          izlazi.setBigDecimal("ISP", kartica.getBigDecimal("ISP_GOOD"));
          izlazi.setBigDecimal("IRAZ", kartica.getBigDecimal("IZAD_GOOD"));
          izlazi.post();
				} else if (kartica.getString("VRDOK").equalsIgnoreCase("INV")) {
          String key = getRowKey(kartica);
          if (!ulazRows.containsKey(key)) {
            fatal.addError("Ne mogu pronaæi stavku ulaza "+key);
            System.err.println("Weird BUG, can't locate stdoku "+kartica);
            continue;
          }
          ulazi.goToRow(((Integer) ulazRows.get(key)).intValue());
          
          ulazi.setBigDecimal("NC", kartica.getBigDecimal("NC_GOOD"));
          ulazi.setBigDecimal("VC", kartica.getBigDecimal("VC_GOOD"));
          ulazi.setBigDecimal("MC", kartica.getBigDecimal("MC_GOOD"));
          ulazi.setBigDecimal("ZC", kartica.getBigDecimal("ZC_GOOD"));
          ulazi.setBigDecimal("INAB", kartica.getBigDecimal("INAB_GOOD"));
          ulazi.setBigDecimal("IMAR", kartica.getBigDecimal("IMAR_GOOD"));
          ulazi.setBigDecimal("IBP", kartica.getBigDecimal("IBP_GOOD"));
          ulazi.setBigDecimal("IPOR", kartica.getBigDecimal("IPOR_GOOD"));
          ulazi.setBigDecimal("ISP", kartica.getBigDecimal("ISP_GOOD"));
          ulazi.setBigDecimal("IZAD", kartica.getBigDecimal("IZAD_GOOD"));
          ulazi.post();
				}
			}
		} catch (Exception ex) {
      ex.printStackTrace();
			System.out.println("Transakcija poništena");
			/*raTransaction.rollbackTransaction();*/
		}
	}
}
