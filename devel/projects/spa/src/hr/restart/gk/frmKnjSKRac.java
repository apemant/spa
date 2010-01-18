/****license*****************************************************************
**   file: frmKnjSKRac.java
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
import hr.restart.baza.Skstavke;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.db.raPreparedStatement;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.R2Handler;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.layout.raXYConstraints;
import hr.restart.swing.layout.raXYLayout;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmKnjSKRac extends frmKnjizenje {
  QueryDataSet skstavke;
  StorageDataSet saveui;
  raComboBox jcbVrDok;
  raPreparedStatement ps_updstavkeUI;
  String[] skstavkeKeys = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK", "BROJDOK", "BROJIZV"};
  String[] uistavkeKeys = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK", "BROJDOK", "RBS"};
  private boolean prijenosUQNX = false;
  /**
   * Kolone koje su veza izmedju skstavaka i uistavaka.
   * Nisam nasao nista slicno nigdje osim u hr.restart.sk.frmPregledArhive
   * ako se nesto promijeni u toj vezi, hitno promijeniti i ovdje
   * koristi se u this.getUIStavke(QueryDataSet)
   */
  public static String[] skuilinkcols = {"KNJIG", "CPAR", "VRDOK", "BROJDOK", "CKNJIGE"};
  
  public frmKnjSKRac() {
    initVrDok();
    /*
    Prepared statement za naknadno updatanje cgkstavke u uistavke jer ne mogu dobiti kompletan set za transfer
    nego samo po odredjenoj skstavci pa ih dodajem u posebnu tabelu (saveui) koju raKnjizenje azurira
    i nakon toga azuriram prave uistavke preko te tabele.
     */
    ps_updstavkeUI = new raPreparedStatement("uistavke",raPreparedStatement.UPDATE);
    saveui = new StorageDataSet();
    saveui.setColumns(dm.getUIstavke().cloneColumns());
    saveui.open();
  }

  public boolean Validacija() {
    if (hr.restart.sisfun.frmParam.getParam("robno", "prijZim", "N",
      "Transfer temeljnice u Zim aplikaciju opcije:D ili N!")
      .equalsIgnoreCase("D")
      && !getFake()) {
        if (javax.swing.JOptionPane.showConfirmDialog(this,
                "Želite li napraviti prijenos u financijsko (ZIM) ?",
                "Upit", javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
            prijenosUQNX = true;
        } else {
            prijenosUQNX = false;
        }
    } else {
        prijenosUQNX = false;
    }
    return super.Validacija();
  }

  
  private String getSKStavkeQuery() {
    return "SELECT * FROM skstavke WHERE KNJIG = '"+OrgStr.getKNJCORG(false)
    +"' AND (cgkstavke is null or cgkstavke = '') AND " +
    		//"DATUMKNJ < '" + knjdo + "'" +
        Condition.where("DATUMKNJ", Condition.BEFORE, new Timestamp(knjdo.getTime())) + 
    		getVrdokQuery();
  }

  Date knjdo;
  public boolean okPress() throws Exception {
    
	Timestamp kdat = ut.getFirstSecondOfDay(dataSet.getTimestamp("DATUMDO"));
	String kyear = ut.getYear(kdat);
	  
    //knjdo = new Date(Util.getUtil().getLastSecondOfDay(dataSet.getTimestamp("DATUMDO")).getTime());
    knjdo = new Date(Util.getUtil().addDays(kdat, 1).getTime());
    
    skstavke = Util.getNewQueryDataSet(getSKStavkeQuery());
    System.out.println(skstavke.getQuery().getQueryString());
    R2Handler.beginKnjizenje(getVrdokQuery(), knjdo);
    if ((skstavke.getRowCount()+R2Handler.getR2KnjizenjeCount()) == 0) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
      throw new Exception("Nema podataka za knjiženje"); // nema nista za knjizenje
    }
    //tablica u koju dodajem stavke koje se prenose u gk i preko koje ce azurirati cgkstavke
    saveui.empty();
    //pocetak knjizenja
    if (!getKnjizenje().startKnjizenje(this)) return false;
    getKnjizenje().setSKRacKnj(false);
    //setiranje transfer infa
    getKnjizenje().setTransferSet(saveui);
    getKnjizenje().setInfoKeys(uistavkeKeys);
    getKnjizenje().setInfColName("CGKSTAVKE"); // default je CNALOGA
    //petlja-nje
    if (sortExt) skstavke.setSort(new SortDescriptor(new String[] {"EXTBRDOK"}));
    else skstavke.setSort(new SortDescriptor(new String[] {"DATPRI"}));
    
    skstavke.first();
    do {
      QueryDataSet uistavke = getUIStavke(skstavke);
      boolean r2checked = false;
      if (uistavke.getRowCount() > 0) {
          // :spank: Provjera trivijalnog podatka kao što je GODINA 
          //  kad dokument treba biti knjižen!! $&#$%&$#%
       	String ty = ut.getYear(skstavke.getTimestamp("DATUMKNJ"));
        if (!ty.equals(kyear)) {
          String err = "Nemoguæe je knjižiti dokument iz "+
	    	  			ty+". godine u "+kyear+". godinu!";
      	  getKnjizenje().setErrorMessage(err);
     	  throw new Exception(err);
        }
        // provjera mjeseca primitka <=> mjesec knjizenja. Ako je ukljucena opcija.
        checkMonthDok(kdat, "DATUMKNJ", "Dokument");

        do {
        	// ako je uistavka R2 preknjizavanje a datum primitka u mjesecu ispred
        	// onda javi grešku ako je opcija ukljuèena. Ali samo jednom.
        	if (!r2checked && uistavke.getString("CSKL").startsWith("R") &&
        			skstavke.getTimestamp("DATPRI").before(kdat) && 
        			!ut.sameMonth(skstavke.getTimestamp("DATPRI"), kdat))
        		checkMonthDok(kdat, "DATPRI", "R2 preknjižavanje za dokument");
        	r2checked = true;
        	
          // ako je uistavka R2 preknjizavanje a datum primitka IZA
          // datuma do kojeg se knjizi, onda PRESKOCI tu uistavku.
          if (!uistavke.getString("CSKL").startsWith("R") ||
              skstavke.getTimestamp("DATPRI").before(knjdo)) {
            //dodajem u set za prijenos
            saveui.insertRow(false);
            uistavke.copyTo(saveui);
            R2Handler.handleSaveui(saveui);
            saveui.post();
            if (!knjiziUIStavku(uistavke)) return false;
            //azurira transfer info
            if (uistavke.getInt("RBS") == 1) {
              //ako smo na skstavki napuni info
              getKnjizenje().setTransferSet(skstavke);
              getKnjizenje().setInfoKeys(skstavkeKeys);
              getKnjizenje().addTransferInfo(getKnjizenje().cGK);
              //vracanje set infa uistavaka
              getKnjizenje().setTransferSet(saveui);
              getKnjizenje().setInfoKeys(uistavkeKeys);
            }
          }
        } while (uistavke.next());
      }
    } while (skstavke.next());
    skstavke.setSort(null);
    skstavke = null;
    Condition lastc = null;
    R2Handler.getR2KnjizenjeSet().setSort(new SortDescriptor(new String[] {"CPAR", "VRDOK", "BROJDOK"}));
    
    //Handlanje R2:
    if (R2Handler.getR2KnjizenjeCount() > 0) {
      for (R2Handler.getR2KnjizenjeSet().first(); R2Handler.getR2KnjizenjeSet().inBounds(); R2Handler.getR2KnjizenjeSet().next()) {
      	Condition newc = Condition.whereAllEqual(skuiCols, R2Handler.getR2KnjizenjeSet());
      	if (lastc == null || !newc.equals(lastc)) {
	        skstavke = Skstavke.getDataModule().getTempSet(
	            Condition.whereAllEqual(skuiCols, R2Handler.getR2KnjizenjeSet()));
	        skstavke.open();
	        lastc = newc;
       		checkMonthDok(kdat, "DATPRI", "R2 preknjižavanje za dokument");
      	}
        if (!knjiziUIStavku(R2Handler.getR2KnjizenjeSet(), r2opis)) return false;
      }
    }
    transfer2zim();
    return getKnjizenje().saveAll();
  }
  
  private void transfer2zim() {
    if (prijenosUQNX) {
      prepare2Zim pz = new prepare2Zim();
      pz.openFiles();
      pz.setForKnjizenje(getKnjizenje().getStavka());
      pz.makeTransferFiles(false);
      pz.setForKnjizenje(getKnjizenje().getStavkaSK());
      pz.makeTransferFiles(true);
      pz.makeTransferFilesKnjiga();
      pz.closeAll();
      pz.transfer2QnxServer(null, 0, null, null, hr.restart.sisfun.frmParam.getParam("sk","QNXfolderSK","/zimdb/trius09"));
    }
  }

  void checkMonthDok(Timestamp kdat, String col, String dok) throws Exception {
  	if (!checkMonth) return;
  	
  	Timestamp ts = skstavke.getTimestamp(col);
  	if (!ut.sameMonth(ts, kdat)) {
  		int cpar = skstavke.getInt("CPAR");
  		String nazpar = !lookupData.getlookupData().raLocate(dm.getPartneri(), 
  				"CPAR", Integer.toString(cpar)) ? "" : 
  			" - " + dm.getPartneri().getString("NAZPAR");
  		String vrdok = skstavke.getString("VRDOK");
  		String brdok = skstavke.getString("BROJDOK");
  		String extbr = skstavke.getString("EXTBRDOK");
  		String dats = Aus.formatTimestamp(ts);
  		String datk = Aus.formatTimestamp(kdat);
  		if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
  		  new raMultiLineMessage(dok + " " + vrdok + "-" + brdok +
  		   " (" + extbr + ") od partnera " + cpar + nazpar +
  		   " ima datum primitka " + dats + "\nJeste li sigurni da ga" +
  		   " želite proknjižiti s datumom " + datk + " ?"), 
  		   "Pogrešan mjesec knjiženja", JOptionPane.OK_CANCEL_OPTION)) {
  			
  			String err = "Pogrešan mjesec knjiženja!";
  			getKnjizenje().setErrorMessage(err);
				throw new Exception(err);
  		}
  	}
  }
  
  String[] skuiCols = {"KNJIG","CPAR","VRDOK","BROJDOK","CKNJIGE"};
  String r2opis = "R2 preknjižavanje - ";
  
  private boolean knjiziUIStavku(QueryDataSet uistavke) {
    return knjiziUIStavku(uistavke, null);
  }
  private boolean knjiziUIStavku(QueryDataSet uistavke, String opis_to_override) {
    //nova stavka
    StorageDataSet stavka;
    if (uistavke.getInt("RBS") == 1) {
  	stavka = getKnjizenje().getNewStavka(uistavke.getString("BROJKONTA"),uistavke.getString("CORG"));
  } else {
  	String kto = uistavke.getString("BROJKONTA");
    if (kto.length() == 0) kto = getKnjizenje().getKonto(
        uistavke.getString("VRDOK"), uistavke.getString("CSKL"), uistavke.getShort("STAVKA")+"");
  	if (kto.length() == 0 && uistavke.getString("VRDOK").equals("OKD")) {
	  kto = getKnjizenje().getKonto("URN", uistavke.getString("CSKL"), uistavke.getShort("STAVKA")+"");
	} else if (kto.length() == 0 && uistavke.getString("VRDOK").equals("OKK")) {
  	  kto = getKnjizenje().getKonto("IRN", uistavke.getString("CSKL"), uistavke.getShort("STAVKA")+"");
	}
	/*if (kto.equals("")) {
	  kto = uistavke.getString("BROJKONTA");
	}*/
	stavka = getKnjizenje().getNewStavka(kto, uistavke.getString("CORG"));
  }
    //punjenje nove stavke
    stavka.setTimestamp("DATDOK",skstavke.getTimestamp("DATDOK"));
    stavka.setTimestamp("DATDOSP",skstavke.getTimestamp("DATDOSP"));
    //ako je rbs od uistavke 1 onda ona prezentira skstavku i treba drugacije napuniti
    stavka.setString("BROJDOK",skstavke.getString("BROJDOK"));
    stavka.setString("EXTBRDOK",skstavke.getString("EXTBRDOK"));
    stavka.setInt("CPAR",uistavke.getInt("CPAR"));
    VarStr opis = new VarStr();
    if (opis_to_override == null || r2opis.equals(opis_to_override)) {
        if (opis_to_override != null) opis.append(opis_to_override);
	    if (skstavke.getString("VRDOK").equalsIgnoreCase("URN") ||
	        skstavke.getString("VRDOK").equalsIgnoreCase("OKD")) {
	      if (urat.length() > 0) {
	        opis.append(urat);
	        opis.replaceAll("$O", skstavke.getString("OPIS"));
	        opis.replaceAll("$B", skstavke.getString("BROJDOK"));
	        opis.replaceAll("$E", skstavke.getString("EXTBRDOK"));
	        opis.replaceAll("$C", Integer.toString(skstavke.getInt("CPAR")));
	        if (opis.indexOf("$P") >= 0) {
	          String nazpar = "";
	          if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", 
	              Integer.toString(skstavke.getInt("CPAR"))))
	                nazpar = dm.getPartneri().getString("NAZPAR");
	          opis.replaceAll("$P", nazpar);          
	        }
	      } else opis.append(skstavke.getString("OPIS"));
	    } else {
	      if (irat.length() > 0) {
	        opis.append(irat);
	        opis.replaceAll("$O", skstavke.getString("OPIS"));
	        opis.replaceAll("$B", skstavke.getString("BROJDOK"));
	        opis.replaceAll("$E", skstavke.getString("EXTBRDOK"));
	        opis.replaceAll("$C", Integer.toString(skstavke.getInt("CPAR")));
	        if (opis.indexOf("$P") >= 0) {
	          String nazpar = "";
	          if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", 
	              Integer.toString(skstavke.getInt("CPAR"))))
	                nazpar = dm.getPartneri().getString("NAZPAR");
	          opis.replaceAll("$P", nazpar);
	        }
	      } else opis.append(skstavke.getString("OPIS"));
	    }
    } else {
      opis = new VarStr(opis_to_override);
    }
    opis.truncate(100);
    if (uistavke.getInt("RBS") == 1) {
      stavka.setString("OPIS", opis.toString());
      stavka.setString("OZNVAL", skstavke.getString("OZNVAL"));
      stavka.setBigDecimal("TECAJ", skstavke.getBigDecimal("TECAJ"));
      stavka.setBigDecimal("DEVID", skstavke.getBigDecimal("PVID"));
      stavka.setBigDecimal("DEVIP", skstavke.getBigDecimal("PVIP"));
      //            stavka.setString("BROJDOK",skstavke.getString("BROJDOK"));
      //            stavka.setInt("CPAR",skstavke.getInt("CPAR"));
    } else {
      if (getKnjizenje().isLastKontoZbirni()) {
        stavka.setString("OPIS","Protukonto dokumenata - zbirno");
      } else {
        stavka.setString("OPIS", opis.toString());
      }
    }

    getKnjizenje().setID(uistavke.getBigDecimal("ID"));
    getKnjizenje().setIP(uistavke.getBigDecimal("IP"));
    //snima stavku i ako ne uspije prekida se knjizenje
    return getKnjizenje().saveStavka();
  }

  public boolean commitTransfer() {
    super.commitTransfer();
//    return new raLocalTransaction() {
//      public boolean transaction() throws Exception {
        for (saveui.first(); saveui.inBounds(); saveui.next()) {
          
          try {
            System.out.println(saveui);
            ps_updstavkeUI.setKeys(saveui);
            ps_updstavkeUI.setValues(saveui);
            ps_updstavkeUI.execute();
          }
          catch (Exception ex) {
            ex.printStackTrace();
            //throw ex; kad je u svojoj transakciji
            return false;
          }
        }
        return R2Handler.commitTransferR2();
//      }}.execTransaction();
  }
  
  public static QueryDataSet getUIStavke(DataSet sk) {
    /*return Util.getNewQueryDataSet(
    "SELECT * FROM uistavke WHERE KNJIG = '" + skstavke.getString("KNJIG") +
    "' AND CPAR = " + skstavke.getInt("CPAR") +
    //        " AND STAVKA = " + skstavke.getShort("STAVKA") +
    //        " AND CSKL = '" + skstavke.getString("CSKL") +
    " AND VRDOK = '" + skstavke.getString("VRDOK") +
    "' AND BROJDOK = '" + skstavke.getString("BROJDOK")+"'");*/
    QueryDataSet uids = UIstavke.getDataModule().getTempSet(
        	Condition.whereAllEqual(skuilinkcols,sk)
        );
    uids.open();
    return uids;
  }

  private void initVrDok() {
    dataSet.addColumn("VRDOK",Variant.STRING);
    jcbVrDok = new raComboBox();
    JLabel jlVrDok = new JLabel("Vrsta dokumenta");
    JPanel jpAdditional = new JPanel();
    jcbVrDok.setRaDataSet(dataSet);
    jcbVrDok.setRaColumn("VRDOK");
    jcbVrDok.setRaItems(getCbVrdokItems());
    jpAdditional.setLayout(new raXYLayout());
    jpAdditional.add(jlVrDok,new raXYConstraints(15,0,-1,-1));
    jpAdditional.add(jcbVrDok,new raXYConstraints(150,0,370,-1));
    this.jp.add(jpAdditional,BorderLayout.CENTER);
  }

  private String[][] getCbVrdokItems() {
    return getCbVrdokItems(true,null,null);
  }

  /**
   * @return array za raComboBox
   * @param generic prikazati genericke opcije (SVI, SVII, SVIU)
   * @param uraira izlazne ili ulazne null=oboje
   * @param excluded koje vrste dokumenta ne prikazati null - sve prikazi
   */
  public static String[][] getCbVrdokItems(boolean generic, String uraira, String[] excluded) {
    QueryDataSet vrdoks = dM.getDataModule().getVrdokum();
    vrdoks.open();
    int rowcount = 0;
    vrdoks.first();
    do {
      if (chkVrdoks(vrdoks, uraira, excluded)) rowcount++;
    } while (vrdoks.next());
    String[][] ret;
    int arr_i;
    if (generic) {
      ret = new String[rowcount+3][2];
      ret[0][0] = "Svi dokumenti";
      ret[0][1] = "SVI";
      ret[1][0] = "Svi ulazni dokumenti";
      ret[1][1] = "SVIU";
      ret[2][0] = "Svi izlazni dokumenti";
      ret[2][1] = "SVII";
      arr_i=3;
    } else {
      ret = new String[rowcount][2];
      arr_i=0;
    }
    vrdoks.first();
    do {
      if (chkVrdoks(vrdoks, uraira, excluded)) {
        ret[arr_i][0]=vrdoks.getString("NAZDOK")+" ("+vrdoks.getString("VRDOK")+")";
        ret[arr_i][1]=vrdoks.getString("VRDOK");
        arr_i++;
      }
    } while (vrdoks.next());
    return ret;
  }

  private static boolean chkVrdoks(ReadRow vrdoks, String uraira, String[] excluded) {
    if ((vrdoks.getString("APP").equals("sk"))
    && (!(excluded != null && Util.getUtil().containsArr(excluded, vrdoks.getString("VRDOK"))))
    &&  (uraira == null || (uraira != null && uraira.equals(vrdoks.getString("VRSDOK"))))) {
      return true;
    }
    return false;
  }

  private String getVrdokQuery() {
    String vrdok = dataSet.getString("VRDOK");
    if (vrdok.equals("SVI") || vrdok.equals("")) return "";
    String vq = " AND VRDOK ";
    if (vrdok.startsWith("SVI")) {
      String vrsdok = vrdok.substring(3);
      QueryDataSet vrdoks = dM.getDataModule().getVrdokum();
      vrdoks.open();
      vq = vq+"IN (";
      vrdoks.first();
      do {
        if (vrdoks.getString("APP").equals("sk") && vrdoks.getString("VRSDOK").equals(vrsdok)) {
          vq = vq + "'"+vrdoks.getString("VRDOK")+"',";
        }
      } while (vrdoks.next());
      System.out.println("vq="+vq);
      vq = new VarStr(vq).chop().append(")").toString();
      System.out.println("vq = new VarStr(vq).replaceLast(\",\",\")\").toString() => "+vq);
    } else {
      vq = vq + "='"+vrdok+"'";
    }
    return vq;
  }
  
  boolean sortExt, checkMonth;
  String urat, irat;
  public void initInputValues() {
    super.initInputValues();
    jcbVrDok.setSelectedIndex(0);
    System.out.println("Vrsta dokumenta = "+dataSet.getString("VRDOK"));
    urat = frmParam.getParam("sk", "formatURA", "Broj $B, URA $E", 
    	"Format opisa URA kod knjiženja ($O - opis, $B - broj dok., $E - broj URA)");
    irat = frmParam.getParam("sk", "formatIRA", "Broj $O, $B", 
    	"Format opisa IRA kod knjiženja ($O - opis, $B - broj dok., $E - broj IRA)");
    sortExt = frmParam.getParam("sk", "sortTem", "D", "Poredak stavki " +
    		"na temeljnici SK raèuna po dodatnom broju (D/N)").equalsIgnoreCase("D");
    checkMonth = frmParam.getParam("sk", "checkMonth", "D", "Provjera mjeseca " +
		"datuma primitka s datumom knjiženja temeljnice (D/N)").equalsIgnoreCase("D");
  }
//   //test
//  public static void main(String[] args) {
//    System.out.println("1.:");
//    prn(getCbVrdokItems(true, null, null));
//
//    System.out.println("2.:");
//    prn(getCbVrdokItems(false, null, null));
//
//    System.out.println("3.:");
//    prn(getCbVrdokItems(false, "I", null));
//
//    System.out.println("4.:");
//    prn(getCbVrdokItems(false, "U", null));
//
//    System.out.println("5.:");
//    prn(getCbVrdokItems(false, "I", new String[] {"UPL","IPL"}));
//
//    System.out.println("6.:");
//    prn(getCbVrdokItems(false, "U", new String[] {"UPL","IPL"}));
//
//    System.out.println("7 and last.:");
//    prn(getCbVrdokItems(false, null, new String[] {"UPL","IPL"}));
//
//  }
//
//  private static void prn(String[][] toprn) {
//    for (int i=0; i<toprn.length; i++) {
//      System.out.println("item "+i+" = "+toprn[i][0]+" ::: "+toprn[i][1]);
//    }
//
//  }
//  //endtest

}