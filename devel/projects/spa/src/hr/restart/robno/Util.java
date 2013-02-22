/****license*****************************************************************
**   file: Util.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.SanityException;
import hr.restart.util.SeqLockedException;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raJPTableView;
import hr.restart.util.raLoader;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raProcess;
import hr.restart.zapod.OrgStr;

import java.awt.Window;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class Util {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  public int STR_LEFT=0;
  public int STR_CENTER=1;
  public int STR_RIGHT=2;
  public int NUM_FIRST=0;
  public int NUM_LAST=1;
  public int MOD_STR=0;
  public int MOD_NUM=1;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private static Util myUtil;
  short nTemp;
  com.borland.dx.sql.dataset.QueryDataSet qdsMPSkl;
  com.borland.dx.sql.dataset.QueryDataSet qdsNorme;
  public java.math.BigDecimal sto = new java.math.BigDecimal(100);
  public java.math.BigDecimal nul = new java.math.BigDecimal(0);
  public java.math.BigDecimal one = new java.math.BigDecimal(1);
  public java.math.BigDecimal _sto = new java.math.BigDecimal(0.01);
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  
  public static String[] mkey = {"CSKL", "VRDOK", "GOD", "BRDOK"};
  public static String[] dkey = {"CSKL", "VRDOK", "GOD", "BRDOK", "RBR"};
  
  private Util() {
    initDocClasses();
  }

  public static Util getUtil() {
    if (myUtil==null) myUtil=new Util();

    return myUtil;
  }
/**
 * Pronalaženje broj dokumenta za displey na ekranu
 * - mod rada 'N' - novi
 *            'I' - ispravak
 */
  public void findBRDOK(char mod, com.borland.dx.sql.dataset.QueryDataSet data) {
    if (mod=='N') {
//      jlDOK.setText(data.getString("VRDOK")+" - "+data.getString("CSKL").trim());
    }
    else {
//      jlDOK.setText(data.getString("VRDOK")+" - "+data.getString("CSKL").trim()+" / "+data.getString("BRDOK").substring(0,2)+" - "+data.getString("BRDOK").substring(2));
    }
//    jlDOKST.setText(jlDOK.getText());
  }
/**
 * Pronalazak posljednje stavke na detailu
 * (radi se na entry point ili kod zapisa u tablicu)
 */
  public short findRBR(com.borland.dx.sql.dataset.QueryDataSet data) {
    data.first();
    nTemp=data.getShort("RBR");
    do {
      if (data.getShort("RBR") > nTemp) {
        nTemp=data.getShort("RBR");
      }
      data.next();
    } while (!data.atLast());
    return nTemp;
  }
/**
 * Postavljanje novih rednih brojeva stavki nakon brisanja
 * (npr. ako pobrišemo stavku broj 1, onda bivša broj 2 postaje 1, 3 postaje 2, itd..)
 */
  public void calcRBR(com.borland.dx.sql.dataset.QueryDataSet data, short del) {
    data.first();
    do {
      if (data.getShort("RBR") > del) {
        data.setShort("RBR", (short)(data.getShort("RBR")-1));
        data.post();
      }
      data.next();
    } while (data.atLast());
    data.saveChanges();
    if (data.getShort("RBR") > del) {
      data.setShort("RBR", (short)(data.getShort("RBR")-1));
      data.post();
    }
  }
  
  public String getSeqString(DataSet ds) {
    
    
    
    boolean cskl = frmParam.getParam("robno", "seqCskl", "D",
        "Ima li svako skladište zaseban brojaè (D,N)").equalsIgnoreCase("D");
    if (!cskl) return OrgStr.getKNJCORG(false) +
         ds.getString("VRDOK")+ds.getString("GOD");
    else if (ds.hasColumn("CSKLIZ") != null)
      return ds.getString("CSKLIZ")+"-"+ds.getString("CSKLUL")+
            ds.getString("VRDOK")+ds.getString("GOD");
    else return ds.getString("CSKL") + 
          ds.getString("VRDOK") + ds.getString("GOD");
  }
  
  public boolean checkSeq(String cOpis, String cBroj) {
/*    hr.restart.util.lookupData lF;
    String[] filter = {cOpis,""};
    String[] colnames = {"OPIS","BROJ"};
    lF=hr.restart.util.lookupData.getlookupData();
    dm.getSeq().close();
    dm.getSeq().open();
    String[] result = lF.lookUp(null,dm.getSeq(),colnames,filter,null);
    dm.getSeq().interactiveLocate(cOpis,"OPIS",com.borland.dx.dataset.Locate.FIRST, false); */
    try {
      Valid.getValid().setSeqFilter(cOpis);
    } catch (SeqLockedException e) {
      JOptionPane.showMessageDialog(null, "Drugi korisnik upravo radi s istom vrstom dokumnenta!\n"+
        "Pokušajte ponovo nakon nekoliko trenutaka.", "Greška", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (Integer.parseInt(cBroj)==(int) dm.getSeq().getDouble("BROJ")) return true;
    JOptionPane.showConfirmDialog(null,"Brisati možete samo posljednji dokument: "+Integer.parseInt(cBroj)+"<>"+(int) dm.getSeq().getDouble("BROJ")+" !",
                                  "Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return false;
  }
  public void delSeq(String cOpis) {
    delSeq(cOpis, false);
  }
  public void delSeq(String cOpis, boolean trans) {
/*    hr.restart.util.lookupData lF;
    String[] filter = {cOpis,""};
    String[] colnames = {"OPIS","BROJ"};
    lF=hr.restart.util.lookupData.getlookupData();
    dm.getSeq().close();
    dm.getSeq().open();
    String[] result = lF.lookUp(null,dm.getSeq(),colnames,filter,null);
    dm.getSeq().interactiveLocate(cOpis,"OPIS",com.borland.dx.dataset.Locate.FIRST, false);
    if (result!=null) {
      dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")-1);
    } else {
      dm.getSeq().insertRow(true);
      dm.getSeq().setString("OPIS", cOpis);
      dm.getSeq().setDouble("BROJ", 1);
    }*/
    Valid.getValid().setSeqFilter(cOpis);
    if (dm.getSeq().getDouble("BROJ") > 0)
      dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")-1);
    dm.getSeq().post();
    if (trans) hr.restart.util.raTransaction.saveChanges(dm.getSeq());

//    else dm.getSeq().saveChanges();
  }
  
  public void delSeqCheck(String cOpis, boolean trans, int delnum) {
  	Valid.getValid().setSeqFilter(cOpis);
    if ((int) dm.getSeq().getDouble("BROJ") == delnum) {
      dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")-1);
      dm.getSeq().post();
      if (trans) hr.restart.util.raTransaction.saveChanges(dm.getSeq());
    }
  }
 /**
 Rekalkulacija novih rednih brojeva stavki nakon brisanja
 * - qds - QueryDataSet
 * - delStavka - redni broj stavke koja se briše
 * - mode - 'N' - normal
 *        - 'P' - POS (ne ide saveChanges())
 */
  public void recalcRBR(com.borland.dx.sql.dataset.QueryDataSet qds, short delStavka) {
    recalcRBR(qds, delStavka, 'N');
  }
  public void recalcRBR(com.borland.dx.sql.dataset.QueryDataSet qds, short delStavka, char mode) {
    qds.first();
    do {
      if (qds.getShort("RBR") > delStavka) {
        qds.setShort("RBR", (short)(qds.getShort("RBR")-1));
        qds.post();
      }
      qds.next();
    } while (!qds.atLast());
    if (qds.getShort("RBR") > delStavka) {
      qds.setShort("RBR", (short)(qds.getShort("RBR")-1));
      qds.post();
    }
    if (mode=='N') {
      qds.saveChanges();
    }
  }
  public String findCSKL() {
    dm.getSklad().open();
    return dm.getSklad().getString("CSKL");
  }
  public String findMPCSKL() {
    dm.getSklad().open();
    return dm.getSklad().getString("CSKL");
  }
  public java.sql.Timestamp findFirstDayOfYear(int god) {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.YEAR, god);
    variant.setTimestamp(new java.sql.Timestamp( calendar.getTime().getTime()));
    return variant.getTimestamp();
  }
  public java.sql.Timestamp findFirstDayOfYear() {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    variant.setTimestamp(new java.sql.Timestamp( calendar.getTime().getTime()));
    return variant.getTimestamp();
  }
  public java.sql.Timestamp findLastDayOfYear(int god) {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 31);
    calendar.set(Calendar.MONTH, 11);
    calendar.set(Calendar.YEAR, god);
    variant.setTimestamp(new java.sql.Timestamp( calendar.getTime().getTime()));
    return variant.getTimestamp();
  }
  public java.sql.Timestamp findFirstDayOfMonth(int mjesec, int god) {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, mjesec);
    calendar.set(Calendar.YEAR, god);
    variant.setTimestamp(new java.sql.Timestamp( calendar.getTime().getTime()));
    return variant.getTimestamp();
  }
  public java.sql.Timestamp findLastDayOfMonth(int mjesec, int god) {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(Calendar.MONTH, mjesec);
    calendar.set(Calendar.YEAR, god);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    variant.setTimestamp(new java.sql.Timestamp(calendar.getTime().getTime()));
    return variant.getTimestamp();
  }

/*  public int locateSeqInteger(java.lang.String cOpis) {
    hr.restart.util.lookupData lF;
    String[] filter = {cOpis,""};
    String[] colnames = {"OPIS","BROJ"};
    lF=hr.restart.util.lookupData.getlookupData();
    dm.getSeq().open();
    String[] result = lF.lookUp(null,dm.getSeq(),colnames,filter,null);
    dm.getSeq().interactiveLocate(cOpis,"OPIS",Locate.FIRST, false);
    if (result!=null) {
      return ((int) dm.getSeq().getDouble("BROJ"))+1;
    }
    return 1;
  } */
/**
 * Rachunanje iznosa od postotka
 */
  public java.math.BigDecimal findIznos(java.math.BigDecimal Osnovica, java.math.BigDecimal Postotak) {
    return Osnovica.multiply(Postotak.multiply(_sto)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
/**
 * Rachunanje postotka od iznosa
 */
  public java.math.BigDecimal findPostotak(java.math.BigDecimal Iznos, java.math.BigDecimal Osnovica) {
    if (Iznos.doubleValue()>0) {
      return Osnovica.multiply(sto).divide(Iznos, 1).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
    }
    else {
      return nul;
    }
  }
  /**
   * Rachunanje postotka od iznosa
   */
    public java.math.BigDecimal findPostotak6(java.math.BigDecimal Iznos, java.math.BigDecimal Osnovica) {
      if (Iznos.doubleValue()>0) {
        return Osnovica.multiply(sto).divide(Iznos, 6, BigDecimal.ROUND_HALF_UP);
      }
      else {
        return nul;
      }
    }
    /**
     * Rachunanje postotka od iznosa
     */
      public java.math.BigDecimal findPostotak7(java.math.BigDecimal Iznos, java.math.BigDecimal Osnovica) {
        if (Iznos.doubleValue()>0) {
          return Osnovica.multiply(sto).divide(Iznos, 7, BigDecimal.ROUND_HALF_UP);
        }
        else {
          return nul;
        }
      }
/**
 * Zbrajanje
 */
  public java.math.BigDecimal sumValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return Osnovica1.add(Osnovica2);
  }
  public java.math.BigDecimal sumValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2, java.math.BigDecimal Osnovica3) {
    return Osnovica1.add(Osnovica2).add(Osnovica3);
  }
  public java.math.BigDecimal sumValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2, java.math.BigDecimal Osnovica3, java.math.BigDecimal Osnovica4) {
    return Osnovica1.add(Osnovica2).add(Osnovica3).add(Osnovica4);
  }
/**
 * Oduzimanje
 */
  public java.math.BigDecimal negateValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return Osnovica1.add(Osnovica2.negate());
  }
/**
 * Mnozhenje
 */
  public java.math.BigDecimal multiValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    return (Osnovica1.multiply(Osnovica2)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  }
/**
 * Djelenje
 */
  public java.math.BigDecimal divideValue(java.math.BigDecimal Osnovica1, java.math.BigDecimal Osnovica2) {
    if (Osnovica2.doubleValue()!=0) {
      return Osnovica1.divide(Osnovica2, BigDecimal.ROUND_HALF_UP);
    }
    else {
      return nul;
    }
  }
/**
 * Dataset za dohvat maloprodajnog skladista
 */
  public static com.borland.dx.sql.dataset.QueryDataSet getMPSklDataset() {
    QueryDataSet qdsVrati;
    hr.restart.zapod.OrgStr or = hr.restart.zapod.OrgStr.getOrgStr();
    hr.restart.baza.Sklad skl = hr.restart.baza.Sklad.getDataModule();
    String q;
    qdsVrati=skl.getFilteredDataSet(q="vrzal='M' and (corg in "+or.getInQuery(or.getOrgstrAndCurrKnjig())+")");
    System.out.println("getMPSklDataset q::: "+q);
    qdsVrati.open();
    return qdsVrati;

/*    if (qdsMPSkl==null) {
      qdsMPSkl=new com.borland.dx.sql.dataset.QueryDataSet();
      qdsMPSkl.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from sklad where vrzal=\'M\'", null, true, com.borland.dx.sql.dataset.Load.ALL));
    }
    qdsMPSkl.open();
    return qdsMPSkl;*/
  }
  public boolean findOtherForMP(hr.restart.util.JlrNavField jrfCSKL, hr.restart.util.JlrNavField jrfNAZSKL, hr.restart.swing.JraButton jbCSKL) {
    if (getMPSklDataset().rowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema definiranog maloprodajnog skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    else if (getMPSklDataset().rowCount()==1){
      jrfCSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
      jrfNAZSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
    }
    else {
      rcc.setLabelLaF(jrfCSKL,true);
      rcc.setLabelLaF(jrfNAZSKL,true);
      rcc.setLabelLaF(jbCSKL,true);
      jrfCSKL.requestFocus();
    }
    return true;
  }
/**
 * Vracha da li je String Numeric
 * @param str
 */
  public boolean isNumeric(String str) {
    java.math.BigDecimal num;
    try {
      num=new java.math.BigDecimal(str);
    }
    catch(Exception ex) {
      JOptionPane.showConfirmDialog(null,"Pogrešan unos !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
/**
 * Vracha da li je sadrzhaj TextFields Numeric
 * @param jtf
 */
  public boolean isNumeric(hr.restart.swing.JraTextField jtf) {
    java.math.BigDecimal num;
    try {
      num=new java.math.BigDecimal(jtf.getText());
    }
    catch(Exception ex) {
      JOptionPane.showConfirmDialog(jtf.getParent(),"Pogrešan unos !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      jtf.requestFocus();
      return false;
    }
    return true;
  }
/**
 * Vracha BigDecimal iz String
 * @param str
 */
  public java.math.BigDecimal getNumeric(String str) {
    java.math.BigDecimal num;
    try {
      num=new java.math.BigDecimal(str);
    }
    catch(Exception ex) {
      JOptionPane.showConfirmDialog(null,"Pogrešan unos !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return _Main.nul;
    }
    return num;
  }
/**
 * Vracha BigDecimal iz TextFielda
 * @param jtf
 */
  public java.math.BigDecimal getNumeric(hr.restart.swing.JraTextField jtf) {
    java.math.BigDecimal num;
    try {
      num=new java.math.BigDecimal(jtf.getText());
    }
    catch(Exception ex) {
      JOptionPane.showConfirmDialog(jtf.getParent(),"Pogrešan unos !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return _Main.nul;
    }
    return num;
  }
/**
 * Brisanje svih redaka iz dataSeta
 * @param qds - dataSet
 */
  public void killTable(com.borland.dx.sql.dataset.QueryDataSet qds) {
    qds.open();
    qds.deleteAllRows();
  }
/**
 * Brisanje svih redaka iz dataSeta
 * @param qds - dataSet
 */
  public void emptyTable(com.borland.dx.sql.dataset.QueryDataSet qds) {
    qds.open();
    qds.deleteAllRows();
    qds.saveChanges();
  }
/**
 * Vraca String za vezu izmedu Zaglavlja i Stavki kod dviju tablica
 * @param baza1 - tablica
 * @param baza2 - tablica
 * @return - String za query
 */
  public String getDoc(String baza1, String baza2) {
    return baza1+".CSKL="+baza2+".CSKL and "+baza1+".VRDOK="+baza2+".VRDOK and "+baza1+".GOD="+baza2+".GOD and "+baza1+".BRDOK="+baza2+".BRDOK";
  }
  public String getDocMes(String baza1, String baza2) {
    return baza1+".CSKLUL="+baza2+".CSKLUL and "+
           baza1+".CSKLIZ="+baza2+".CSKLIZ and "+
           baza1+".VRDOK="+baza2+".VRDOK and "+baza1+
           ".GOD="+baza2+".GOD and "+baza1+".BRDOK="+baza2+".BRDOK";
  }
  public String getSkl(String baza) {
    return "sklad.cskl="+baza+".cskl and sklad.god="+baza+".god and sklad.cart="+baza+".cart";
  }
/**
 * Vraca String sa razmacima
 * @param text - String koji uredjujemo
 * @param broj - broj razmaka
 * @param pos - pozicija razmaka (STR_LEFT, STR_RIGHT, STR_CENTER)
 * @return - String sa praznim poljima
 */
  public String getStr(String text, int broj, int pos) {
    String str;
    if (pos==STR_LEFT) {
      str=vl.maskString("", ' ', broj);
      return (text+str).substring(0,broj);
    }
    else if (pos==STR_RIGHT) {
      str=vl.maskString("", ' ', broj-text.length());
      return str+text;
    }
    else if (pos==STR_CENTER) {

    }
    return "";
  }
/**
 * Trazenje broja dokumenta koji odgovara uvijetima
 * @param doc - vrsta dokumenta
 * @param god - godina
 * @param skl - sifra skladista
 * @param date - datum u Timestamp-u
 * @param mode - mod rada NUM_FIRST, NUM_LAST
 * @return - int vrijednost od polja BRDOK u tablici DOC
 */
  public int findNumFromDate(String doc, String god, String skl, java.sql.Timestamp date, int mode) {
    return findNumFromDate(doc, god, skl, date, mode, doc);
  }
/**
 * Trazenje broja dokumenta koji odgovara uvijetima
 * @param doc - vrsta dokumenta
 * @param god - godina
 * @param skl - sifra skladista
 * @param date - datum u Timestamp-u
 * @param mode - mod rada NUM_FIRST, NUM_LAST
 * @param baza - baza iz koje vraca vrijednost
 * @return - int vrijednost od polja BRDOK u tablici BAZA
 */
  public int findNumFromDate(String doc, String god, String skl, java.sql.Timestamp date, int mode, String baza) {
    String qStr="";
    if (mode==NUM_FIRST) {
      qStr="select min(brdok) as brdok from "+baza+" where CSKL='"+skl+"' and GOD='"+god+"' and VRDOK='"+doc+"' and DATDOK>="+getTimestampValue(date, mode);
    }
    else if (mode==NUM_LAST) {
      qStr="select max(brdok) as brdok from "+baza+" where CSKL='"+skl+"' and GOD='"+god+"' and VRDOK='"+doc+"' and DATDOK<="+getTimestampValue(date, mode);
    }
    vl.execSQL(qStr);
    vl.RezSet.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("BRDOK").clone()});
    vl.RezSet.open();
    return vl.RezSet.getInt("brdok");
  }
/**
 * Vraca String u formatu za trazenje kod SQL upita sa Timestamp-om zavisno od moda
 * @param date - TimeStamp polje koje pretvaramo u Sting
 * @param mode - mod rada NUM_FIRST ili NUM_LAST
 * @return - Timestam to String sa podesenim vremenom
 */
  public String getTimestampValue(java.sql.Timestamp date, int mode) {
    String cVrati="";
    if (mode==NUM_FIRST) {
      cVrati="'"+date.toString().substring(0,10)+" 00:00:00'";
    }
    else if (mode==NUM_LAST) {
      cVrati="'"+date.toString().substring(0,10)+" 23:59:59'";
    }
    return cVrati;
  }
  public String getRange(java.sql.Timestamp pocDatum, java.sql.Timestamp zavDatum, String field) {
    String cVrati="";
    cVrati=field+">='"+pocDatum.toString().substring(0,10)+" 00:00:00' and "+field+"<='"+zavDatum.toString().substring(0,10)+" 23:59:59'";
    return cVrati;
  }
  /*public void updateStanje(BigDecimal oldPORAVMAR, BigDecimal oldPORAVPOR, BigDecimal oldPORAV, char mode, boolean isFind, char doc) {
    updateStanje(this.nul, this.nul, this.nul, this.nul, this.nul, oldPORAVMAR, oldPORAVPOR, oldPORAV, mode, isFind, doc);
  }
  public void updateStanje(BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, BigDecimal oldPORAVMAR, BigDecimal oldPORAVPOR, BigDecimal oldPORAV, char mode, boolean isFind, char doc) {
    updateStanje(oldKOL, oldNAB, oldMAR, oldPOR, oldZAD, oldPORAVMAR, oldPORAVPOR, oldPORAV, mode, isFind, doc, dm.getStanje(), dm.getStdoku());
  }*/
  public void updateStanje(BigDecimal oldKOL, BigDecimal oldNAB, BigDecimal oldMAR, BigDecimal oldPOR, BigDecimal oldZAD, BigDecimal oldPORAVMAR, BigDecimal oldPORAVPOR, BigDecimal oldPORAV, char mode, boolean isFind, char doc, com.borland.dx.sql.dataset.QueryDataSet qds, com.borland.dx.sql.dataset.QueryDataSet dds) {
    System.out.println("isFind: "+isFind);
    System.out.println("oldMar: "+oldMAR);
    System.out.println("oldPor: "+oldPOR);
    System.out.println("oldPorav: "+oldPORAV);
    System.out.println("VC: "+dds.getBigDecimal("SVC"));
    System.out.println("MC: "+dds.getBigDecimal("SMC"));
    qds.open();
    dds.open();
    if (!isFind) {
      System.out.println("nije isFind");
      qds.insertRow(true);
      qds.setString("GOD", dds.getString("GOD"));
      qds.setString("CSKL",   dds.getString("CSKL"));
      qds.setInt("CART",      dds.getInt("CART"));
    } else if (!hr.restart.util.lookupData.getlookupData().raLocate(
        dm.getSklad(), "CSKL", qds.getString("CSKL"))) {
      System.out.println("Uff, nisam našao sklad u updateStanje! " + qds.getString("CSKL"));
      throw new SanityException("Nije pronaðen slog stanja za artikl!");
    }
    
    if (doc=='S') {   // Ako je pocetno stanje
      qds.setBigDecimal("KOLPS",   negateValue(dds.getBigDecimal("KOL"), oldKOL));
      qds.setBigDecimal("KOLSKLADPS",   negateValue(dds.getBigDecimal("KOL"), oldKOL));
      qds.setBigDecimal("NABPS",   negateValue(dds.getBigDecimal("INAB"), oldNAB));
      qds.setBigDecimal("MARPS",   negateValue(dds.getBigDecimal("IMAR"), oldMAR));
      qds.setBigDecimal("PORPS",   negateValue(dds.getBigDecimal("IPOR"), oldPOR));
      qds.setBigDecimal("VPS",     negateValue(dds.getBigDecimal("IZAD"), oldZAD));
    }
    qds.setBigDecimal("KOLUL",     negateValue(sumValue(qds.getBigDecimal("KOLUL"), dds.getBigDecimal("KOL")), oldKOL));
    qds.setBigDecimal("KOLSKLADUL",     negateValue(sumValue(qds.getBigDecimal("KOLSKLADUL"), dds.getBigDecimal("KOL")), oldKOL));
    System.out.println("Mar1: "+sumValue(qds.getBigDecimal("MARUL"), dds.getBigDecimal("IMAR"), dds.getBigDecimal("DIOPORMAR")));
    System.out.println("Mar2: "+sumValue(oldMAR, oldPORAVMAR));
    qds.setBigDecimal("NABUL",     negateValue(sumValue(qds.getBigDecimal("NABUL"), dds.getBigDecimal("INAB")), oldNAB));
    qds.setBigDecimal("MARUL",     negateValue(sumValue(qds.getBigDecimal("MARUL"), dds.getBigDecimal("IMAR"), dds.getBigDecimal("DIOPORMAR")), sumValue(oldMAR, oldPORAVMAR)));
    qds.setBigDecimal("PORUL",     negateValue(sumValue(qds.getBigDecimal("PORUL"), dds.getBigDecimal("IPOR"), dds.getBigDecimal("DIOPORPOR")), sumValue(oldPOR, oldPORAVPOR)));
    qds.setBigDecimal("VUL",       negateValue(sumValue(qds.getBigDecimal("VUL"), dds.getBigDecimal("IZAD"), dds.getBigDecimal("PORAV")), sumValue(oldZAD, oldPORAV)));
    qds.setBigDecimal("VRI",       negateValue(qds.getBigDecimal("VUL"), qds.getBigDecimal("VIZ")));
    qds.setBigDecimal("KOL",       negateValue(qds.getBigDecimal("KOLUL"), qds.getBigDecimal("KOLIZ")));
    qds.setBigDecimal("KOLSKLAD",       negateValue(qds.getBigDecimal("KOLSKLADUL"), qds.getBigDecimal("KOLSKLADIZ")));
    qds.setBigDecimal("NC", findNC(qds));
    
    // ukoliko je stanje na nuli, pretpostavimo da stavka ulaza definira NC:
    if (qds.getBigDecimal("NC").signum() == 0 && 
        dds.getBigDecimal("KOL").signum() != 0) {
      Aus.set(qds, "NC", dds, "INAB");
      Aus.div(qds, "NC", dds.getBigDecimal("KOL"));
    }
      //qds.setBigDecimal("NC", divideValue(dds.getBigDecimal("INAB"),
      //    dds.getBigDecimal("KOL")));
      
    if (mode=='B') {
//      System.out.println("VC: "+dds.getBigDecimal("SVC"));
//      System.out.println("MC: "+dds.getBigDecimal("SMC"));
    	/*if (dds.getBigDecimal("SVC").doubleValue()!=0) {
    		qds.setBigDecimal("VC", dds.getBigDecimal("SVC"));
    		qds.setBigDecimal("MC", dds.getBigDecimal("SMC"));
    	}*/
    }
    else {    // Ako nije brisanje
      qds.setBigDecimal("VC", dds.getBigDecimal("VC"));
      qds.setBigDecimal("MC", dds.getBigDecimal("MC"));
    }
    qds.setBigDecimal("ZC", findZC(qds));
    qds.post();
//    qds.saveChanges();
  }
/**
 * @deprecated
 */
  java.math.BigDecimal findNC() {
    if (dm.getStanje().getBigDecimal("KOL").doubleValue()>0) {
      return (dm.getStanje().getBigDecimal("NABUL").add(dm.getStanje().getBigDecimal("NABIZ").negate())).divide(dm.getStanje().getBigDecimal("KOL"),1);
    }
    return (this.nul);
  }
/**
 * Traženje nabavne cijene u dataSetu
 * @return
 */
  java.math.BigDecimal findNC(com.borland.dx.sql.dataset.QueryDataSet qds) {
    if (qds.getBigDecimal("KOL").signum() != 0) {
      int scale = qds.getColumn("NC").getScale();
      if (scale < 2) scale = 2;
      if (scale > 6) scale = 6;
      return qds.getBigDecimal("NABUL").subtract(qds.getBigDecimal("NABIZ")).divide(qds.getBigDecimal("KOL"), scale, BigDecimal.ROUND_HALF_UP);
      
      //return this.divideValue(this.negateValue(qds.getBigDecimal("NABUL"), qds.getBigDecimal("NABIZ")),qds.getBigDecimal("KOL"));
    }
    return (this.nul);
  }
/**
 * @deprecated
 */
  java.math.BigDecimal findZC() {
    if (dm.getStanje().getBigDecimal("KOL").doubleValue()>0) {
      return dm.getStanje().getBigDecimal("VRI").divide(dm.getStanje().getBigDecimal("KOL"),1);
    }
    return (this.nul);
  }
/**
 * Traženje cijene zalihe iz dataSeta
 * @param qds
 * @return
 */
  java.math.BigDecimal findZC(com.borland.dx.sql.dataset.QueryDataSet qds) {
    if (dm.getSklad().getString("VRZAL").equals("N")) {
      return qds.getBigDecimal("NC");
    }
    else if (dm.getSklad().getString("VRZAL").equals("V")) {
      return qds.getBigDecimal("VC");
    }
    else if (dm.getSklad().getString("VRZAL").equals("M")) {
      return qds.getBigDecimal("MC");
    }
    return (this.nul);
  }
  boolean findStanje() {
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    if (ld.raLocate(dm.getStanje(),
      new com.borland.dx.dataset.DataSet[] {dm.getDoku(),dm.getStdoku()},
      new java.lang.String[] {"CSKL","CART"},
      new java.lang.String[] {"CSKL","CART"})) {
      return true;
    }
    return false;
  }
  public boolean chkIsDeleteable(String tablica, String polje, String vrijednost, int mode, boolean sameSet) {
    String qStr;
    int nRec=0;
    if (sameSet) {
      nRec=1;
    }
    qStr="select * from "+tablica+" where "+polje+"="+findDelimiter(mode)+vrijednost+findDelimiter(mode);
    vl.execSQL(qStr);
    vl.RezSet.open();
    if (vl.RezSet.rowCount()>nRec) {
      return false;
    }
    return true;
  }
  public boolean chkIsDeleteable(String tablica, String polje, String vrijednost, int mode) {
    return this.chkIsDeleteable(tablica, polje, vrijednost, mode, false);
  }
  public boolean isDeleteable(String tablica, String polje, String vrijednost, int mode, boolean sameSet) {
    if (chkIsDeleteable(tablica, polje, vrijednost, mode, sameSet)) {
      return true;
    }
    JOptionPane.showConfirmDialog(null,"Brisanje nije dozvoljeno zbog povrede referencijalnog integriteta !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return false;
  }
  public boolean isDeleteable(String tablica, String polje, String vrijednost, int mode) {
    return isDeleteable(tablica, polje, vrijednost, mode, false);
  }
  public String findDelimiter(int mode) {
    if (mode==this.MOD_NUM) {
      return "";
    }
    return "'";
  }
  public boolean isDocSaveable(com.borland.dx.sql.dataset.QueryDataSet qds) {
//    if (qds.getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATINV"))==true) {
    if (TypeDoc.getTypeDoc().isDocOJ(qds.getString("VRDOK"))) return true;
      
    dm.getSklad().open();
    hr.restart.util.lookupData.getlookupData().raLocate(dm.getSklad(),
        new String[] {"CSKL"},new String[] {qds.getString("CSKL")});

    if (qds.getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATINV"))==true) {
      if (qds.getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATULDOK"))==true) {
        if (qds.getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATKNJIZ"))==true) {
          return true;
        }
        else {
          JOptionPane.showConfirmDialog(null,"Datum manji ili jednak datumu prijenosa u financijsko knjigovodstvo !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      else {
        JOptionPane.showConfirmDialog(null,"Datum manji od datuma zadnjeg dokumenta !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    else {
      JOptionPane.showConfirmDialog(null,"Datum manji ili jednak datumu inventure !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }
/*  public boolean isDocSaveable(hr.restart.swing.JraTextField jtf) {
    if (jtf.getDataSet().getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATINV"))==true) {
      if (jtf.getDataSet().getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATULDOK"))==true) {
        if (jtf.getDataSet().getTimestamp("DATDOK").after(dm.getSklad().getTimestamp("DATKNJIZ"))==true) {
          if (jtf.getDataSet().getTimestamp("DATDOK").after(findLastDayOfYear(Integer.parseInt(vl.findYear())))==false) {
            return true;
          }
          else {
            jtf.requestFocus();
            JOptionPane.showConfirmDialog(jtf.getParent(),"Datum nije iz tekuæe godine  !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            return false;
          }
        }
        else {
          jtf.requestFocus();
          JOptionPane.showConfirmDialog(jtf.getParent(),"Datum manji ili jednak datumu prijenosa u financijsko knjigovodstvo !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      else {
        jtf.requestFocus();
        JOptionPane.showConfirmDialog(jtf.getParent(),"Datum manji od datuma zadnjeg dokumenta !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    else {
      jtf.requestFocus();
      JOptionPane.showConfirmDialog(jtf.getParent(),"Datum manji ili jednak datumu inventure !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }*/
  public boolean isDocChangeable(com.borland.dx.sql.dataset.QueryDataSet qds) {
    return true;
  }
/**
 * Vraca datum rednog broj rata (prva == 0)
 * @param i - redni broj rate
 * @return
 */
  public java.sql.Timestamp findDatum(int i) {
    return vl.findDate(false, i*(new java.lang.Integer(hr.restart.sisfun.frmParam.getParam("robno","danaOdgode")).intValue()));
  }
/**
 * Traženje maloprodajne cijene po artiklima
 * @param qds
 * @param cArt
 * @return
 */
  public java.math.BigDecimal getMC(int cArt) {
    System.out.println("getMC - Artikli: "+cArt);
    String str="select MC from ARTIKLI where CART="+cArt;
    vl.execSQL(str);
    vl.RezSet.open();
    if (vl.RezSet.rowCount()>0) {
      return vl.RezSet.getBigDecimal("MC");
    }
    return this.nul;
  }
/**
 * Tražene maloprodajne cijene po stanju
 * @param qds
 * @param cSkl
 * @param cArt
 * @return
 */
  public java.math.BigDecimal getMC(String qds, String cSkl, String cArt) {
    String str="select MC,VRART from STANJE where CART="+cArt+" AND CSKL='"+cSkl+"'";
    vl.execSQL(str);
    vl.RezSet.open();
    if (vl.RezSet.rowCount()>0) {
      return vl.RezSet.getBigDecimal("MC");
    }
    return this.nul;
  }
/**
 * Brisanje rocorda od rata u POS-u
 */
  public void delOldRate() {
    String str="delete from RATE where CSKL='"+dm.getPos().getString("CSKL")+"' and GOD='"+dm.getPos().getString("GOD")+"' and VRDOK='"+dm.getPos().getString("VRDOK")+"' and BRDOK="+dm.getPos().getInt("BRDOK");
    vl.runSQL(str);
  }
/**
 * Dohvat broja dokumenta
 * @param ds
 */
  public void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
    String Godina;
    Integer Broj;
    Godina=vl.findYear(ds.getTimestamp("DATDOK"));
    if (ds.hasColumn("SYSDAT") != null)
      ds.setTimestamp("SYSDAT", ut.getCurrentDatabaseTime());
    ds.setString("GOD",Godina);
    Broj=vl.findSeqInteger(ds);
//    System.out.println(Broj);
    
    ds.setInt("BRDOK",Broj.intValue());
  }
  private static QueryDataSet skladFromCorg;
  private static QueryDataSet matSkladFromCorg;

  public static com.borland.dx.sql.dataset.QueryDataSet getMatSkladFromCorg() {
    if (matSkladFromCorg==null) {
      final hr.restart.zapod.OrgStr or = hr.restart.zapod.OrgStr.getOrgStr();
      final hr.restart.baza.Sklad skl = hr.restart.baza.Sklad.getDataModule();
      matSkladFromCorg = skl.getFilteredDataSet("tipskl='M' and (corg in "+or.getInQuery(or.getOrgstrAndCurrKnjig())+")");
      hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
        public void knjigChanged(String a1, String a2) {
          skl.setFilter(matSkladFromCorg, "tipskl='M' and (corg in "+or.getInQuery(or.getOrgstrAndCurrKnjig())+")");
          matSkladFromCorg.open();
        }
      });
      matSkladFromCorg.open();
    }
    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
    st.prn(matSkladFromCorg);
    return matSkladFromCorg;
  }

  public static com.borland.dx.sql.dataset.QueryDataSet getSkladFromCorg() {
    if (skladFromCorg==null) {
      final hr.restart.zapod.OrgStr or = hr.restart.zapod.OrgStr.getOrgStr();
      final hr.restart.baza.Sklad skl = hr.restart.baza.Sklad.getDataModule();
      skladFromCorg = skl.getFilteredDataSet("corg in "+or.getInQuery(or.getOrgstrAndCurrKnjig()));
      hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
        public void knjigChanged(String a1, String a2) {
          skl.setFilter(skladFromCorg, "corg in "+or.getInQuery(or.getOrgstrAndCurrKnjig()));
          skladFromCorg.open();
        }
      });
      skladFromCorg.open();
    }
    return skladFromCorg;
  }
  public String getDefaultMxHeader() {
    repMemo rm = repMemo.getrepMemo();
    String cVrati="";
    if (hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D")){
    cVrati=
        "<$DoubleWidthON$>"+rm.getLogoNazivlog()+"<$DoubleWidthOFF$><$newline$>"+
        rm.getLogoAdresa()+", "+rm.getLogoPbr()+" "+rm.getLogoMjesto()+"<$newline$>"+
        "Telefon: "+rm.getLogoTel1()+", "+rm.getLogoTel2()+",Fax: "+rm.getLogoFax()+"<$newline$>"+
        "Žiro raèun: "+rm.getLogoZiro()+" OIB: "+rm.getLogoOIB()+
        "";
    } else {
      String slogo = hr.restart.sisfun.frmParam.getParam("robno","sirLogo");
      int sl;
      try {
        sl = Integer.parseInt(slogo.substring(0,slogo.indexOf(".")));
        System.out.println("sirlogo = " + sl);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        sl = 0;
      }
      cVrati = "<$newline$>";
      for (int i = 1; i < (sl*2); i++) {
        cVrati +="<$newline$>";
      }
    }
    System.out.println("Vrati: "+cVrati);
    return cVrati;
  }
  /**
   * Dohvat rednog broj rate
   * @return
   */
  public short getMaxRbr4Rate(QueryDataSet qds) {
    vl.execSQL(sjQuerys.getMaxRbr4Rate(qds.getString("CSKL"), qds.getString("VRDOK"), qds.getString("GOD"), qds.getInt("BRDOK")));
    vl.RezSet.open();
    return (short) (vl.RezSet.getShort(0)+1);
  }
  public BigDecimal getNaplac(QueryDataSet qds) {
    vl.execSQL(sjQuerys.getNaplac(qds.getString("CSKL"), qds.getString("VRDOK"), qds.getString("GOD"), qds.getInt("BRDOK")));
    vl.RezSet.open();
    return (vl.RezSet.getBigDecimal(0));
  }
  public String getPripCorg(String org) {
    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjj");
    StorageDataSet sDS = new StorageDataSet();
    sDS = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(org);
    String corg = "('";

    sDS.first();
    while (sDS.inBounds())
    {
      if(sDS.getRow()< sDS.getRowCount()-1)
        corg += sDS.getString("CORG") + "', '";
      else
        corg += sDS.getString("CORG") + "')";
      sDS.next();
    }
    return corg;
  }
  
  public void showKartica(raMasterDetail md) {
    DataSet ds = md.getDetailSet();
    if (ds.rowCount() == 0) return;
    
    String cskl = ds.getString("CSKL");
    String god = ds.getString("GOD");
    String vrdok = ds.getString("VRDOK");
    if (TypeDoc.getTypeDoc().isDocOJ(vrdok)) {
      cskl = ds.getString("CSKLART");
      if (cskl.trim().length() == 0) {
        JOptionPane.showMessageDialog(md.raDetail.getWindow(),
            "Nije odabrano skladište!", "Kartica artikla",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    
    showKartica(cskl, ds.getInt("CART"), ut.getYearBegin(god),
        ut.getYearEnd(god), vrdok, ds.getInt("BRDOK"));
  }
  
  public void showKartica(String cskl, int cart, Timestamp upto) {
    showKartica(cskl, cart, ut.getFirstDayOfYear(upto), upto, null, 0);
  }
  
  public void showKartica(String cskl, int cart, String god) {
    showKartica(cskl, cart, ut.getYearBegin(god), ut.getYearEnd(god), null, 0);
  }
  
  public void showKartica(String cskl, int cart, 
      Timestamp from, Timestamp to, String locVrdok, int locBrdok) {
    
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", 
        15, "Kartica artikla", false);
    
    upKartica.getupKartica().setOutsideData(cskl, cart, from, to);
    if (locVrdok != null)
      upKartica.getupKartica().setOutsideLocation(locVrdok, locBrdok);
    
    if (upKartica.getupKartica().isShowing())
      upKartica.getupKartica().cancelPress();

    _Main.getStartFrame().showFrame(
        "hr.restart.robno.upKartica", "Kartica artikla");
  }
  
  /**
   * Prikaz dokumenata na ekranu iz raUpita najcesce double klikom
   * @param skl - Skladište na kojem radimo
   * @param jptv - jpTableView sa ekrana
   */

  public void showDocs(String skl, raJPTableView jptv) {
    showDocs(
        skl, "",
        jptv.getMpTable().getDataSet().getString("VRDOK"),
        jptv.getMpTable().getDataSet().getInt("BRDOK"),
        vl.findYear(jptv.getMpTable().getDataSet().getTimestamp("DATDOK"))
    );
  }
  
  public void showDocs(String skliz, String sklul, raJPTableView jptv) {
    showDocs(
        skliz, sklul,
        jptv.getMpTable().getDataSet().getString("VRDOK"),
        jptv.getMpTable().getDataSet().getInt("BRDOK"),
        vl.findYear(jptv.getMpTable().getDataSet().getTimestamp("DATDOK"))
    );
  }
  

  public void showDocs(String skl, String sklul, 
      final String vrdok, int brdok, String god) {
    showDocs(skl, sklul, vrdok, brdok, god, null);
  }
  
  public void showDocs(String skl, String sklul, 
      final String vrdok, int brdok, String god, 
      final String cart) {
    System.out.println("skl="+skl+" vrdk="+vrdok+" brdok"+brdok+"god "+god);

    if (!docClass.containsKey(vrdok)) {
      JOptionPane.showMessageDialog(null, "Nepoznata vrsta dokumenta: "+vrdok,
          "Prikaz dokumenta", JOptionPane.WARNING_MESSAGE);
      return;
    }
    raProcess.runChild(new Runnable() {
      public void run() {
        boolean mes = TypeDoc.getTypeDoc().isDocMeskla(vrdok);
        String className = (String) docClass.get(vrdok);
        String presName = (mes ? "select" : "pres") + vrdok;
        raMasterDetail md = (raMasterDetail) raLoader.load(className);
        try {
          Class presClass = Class.forName("hr.restart.robno." + presName);
          Method getPress = presClass.getMethod("getPres", null);
          if (mes) {
            jpSelectMeskla pres = (jpSelectMeskla) getPress.invoke(null, null);
            pres.showJpSelectDoc(vrdok, md, false);
          } else {
            jpPreselectDoc pres = (jpPreselectDoc) getPress.invoke(null, null);
            pres.showJpSelectDoc(vrdok, md, false);
          }
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        raProcess.yield(md);
      }
    });
    
    final raMasterDetail md = (raMasterDetail) raProcess.getReturnValue();
    if (md == null) {
      JOptionPane.showMessageDialog(null, "Greška kod otvaranja dokumenta tipa "+
          vrdok, "Prikaz dokumenta", JOptionPane.WARNING_MESSAGE);
      return;
    }
    DataSet pres = md.getPreSelect().getSelRow();
    pres.setString("VRDOK", vrdok);
    pres.setTimestamp("DATDOK-from", findFirstDayOfYear(Integer.parseInt(god)));
    pres.setTimestamp("DATDOK-to", findLastDayOfYear(Integer.parseInt(god)));
    Runnable afterShow = cart == null ? null :
      new Runnable() {
        public void run() {
          lookupData.getlookupData().raLocate(md.getDetailSet(), "CART", cart);
        }
      };      
    if (TypeDoc.getTypeDoc().isDocMeskla(vrdok)) {
      pres.setString("CSKLIZ", skl);
      pres.setString("CSKLUL", sklul);
      ((jpSelectMeskla) md.getPreSelect()).memorize();
      md.showRecord(new String[] {skl, sklul, vrdok, 
          god, Integer.toString(brdok)}, afterShow);
    } else {
      pres.setString("CSKL", skl);
      ((jpPreselectDoc) md.getPreSelect()).memorize();
      md.showRecord(new String[] {skl, vrdok, god, 
          Integer.toString(brdok)}, afterShow);
    }
  }
  
  Map docClass = new HashMap();
  private void initDocClasses() {
    String[][] docs = {
        {"MES", "hr.restart.robno.raMeskla"},
        {"MEU", "hr.restart.robno.raMEU"},
        {"MEI", "hr.restart.robno.raMEI"},
        
        {"PRI", "hr.restart.robno.frmPRI"},
        {"OTP", "hr.restart.robno.raOTP"},
        {"IZD", "hr.restart.robno.raIZD"},
        {"REV", "hr.restart.robno.raREV"},
        {"PRV", "hr.restart.robno.raPRV"},
        {"PRK", "hr.restart.robno.frmPRK"},
        {"PTE", "hr.restart.robno.frmPTE"},
        {"KAL", "hr.restart.robno.frmKAL"},
        {"PON", "hr.restart.robno.raPON"},
        {"ROT", "hr.restart.robno.raROT"},
        {"POD", "hr.restart.robno.frmPovratKupca"},
        {"NKU", "hr.restart.robno.raNKU"},
        {"NDO", "hr.restart.robno.frmNarDob"},
        {"GOT", "hr.restart.robno.raGOT"},
        {"GRN", "hr.restart.robno.raGRN"},
        {"POS", "hr.restart.robno.raPOS"},
        {"POV", "hr.restart.robno.raPOV"},
        {"PRE", "hr.restart.robno.frmPRE"},
        {"PRD", "hr.restart.robno.raPRD"},
        {"RAC", "hr.restart.robno.raRAC"},
        {"TER", "hr.restart.robno.raTeret"},
        {"ODB", "hr.restart.robno.raOdobrenje"},
        {"PST", "hr.restart.robno.frmPST"},
        {"OTR", "hr.restart.robno.frmOtpisRobe"},
        {"INM", "hr.restart.robno.frmPregledManjak"},
        {"INV", "hr.restart.robno.frmPregledVisak"},
        {"POR", "hr.restart.robno.frmNivelacija"},  
        {"DOS", "hr.restart.robno.raDOS"}
    };
    for (int i = 0; i < docs.length; i++)
      docClass.put(docs[i][0], docs[i][1]);
  }
  
  public String[] showStanje(Window owner, String cskl, String god, 
      String polje, String value)
  {
    lookupData LD = lookupData.getlookupData();
    QueryDataSet qdsstanjef8 = null;
    StorageDataSet stanjef8 = null;
    
    String nula=" and stanje.kol>0 ";
    if (frmParam.getParam("robno", "nulaF8", "N",
        "Prikaz nul-stanja kod dohvata stanja F8").
        equalsIgnoreCase("D")) nula="";
    
    String kolskl = "STANJE.KOLSKLAD";
    if (frmParam.getParam("robno", "kolsklF8", "N",
      "Prikaz stanja minus rezervirano kod dohvata stanja F8").
      equalsIgnoreCase("D"))
      kolskl = "STANJE.KOL - STANJE.KOLREZ";
    
    if (LD.raLocate(dm.getSklad(), "CSKL", cskl) &&
        dm.getSklad().getString("VRSKL").equals("Z"))
      nula = "";
    
    boolean begNazart = frmParam.getParam("robno", "nazartDohvat", "S",
        "Naèin dohvata artikla po nazivu (P - poèetak, S - sve)", true)
        .equalsIgnoreCase("P");
    
    boolean ignore = frmParam.getParam("robno", "ignoreF8case", "D",
        "Ignorirati mala/velika slova u šiframa kod dohvata s F8 (D, N)")
        .equalsIgnoreCase("D");
    
    boolean tmb = frmParam.getParam("robno", "tmbF8", "N",
      "F8 za TMB, dodaje crticu naprijed (D, N)")
        .equalsIgnoreCase("D");
    
    if (begNazart && value.startsWith("*")) {
      begNazart = false;
      value = value.substring(1);
    }
    
    String qStr = "select ARTIKLI.CART AS CART, ARTIKLI.CART1 AS CARTX, ARTIKLI.BC AS BC, ARTIKLI.CGRART AS CGRART, ARTIKLI.NAZART AS NAZART,"+
                  " STANJE.KOL AS KOL," + kolskl + " AS KOLSKLAD, ARTIKLI.JM AS JM, STANJE.KOLREZ AS KOLREZ, STANJE.NC as NC, STANJE.VC as VC, STANJE.MC as MC,ARTIKLI.BRJED AS BRJED, "+
                  "ARTIKLI.JMPAK AS JMPAK  from ARTIKLI, STANJE WHERE ARTIKLI.CART = STANJE.CART "+
                  "AND ARTIKLI.AKTIV='D' AND STANJE.GOD='"+god+"' AND STANJE.CSKL = '"+cskl+"' "+nula ;
    if (!value.equalsIgnoreCase("")) {
      if (polje.equalsIgnoreCase("CART")) {
        qStr = qStr+" and artikli."+polje + "="+value;
      } else if (polje.equalsIgnoreCase("CART1") || polje.equalsIgnoreCase("BC")) {
        if (tmb) qStr = qStr+" and artikli."+ polje+ " like '%-"+value+"%'";
        else if (!ignore) qStr = qStr+" and artikli."+ polje+ " like '"+value+"%'";
        else {
          String vLo = value.toLowerCase();
          String vUp = value.toUpperCase();
          qStr = qStr+" and (artikli."+ polje+ " like '"+vLo+"%' or artikli."+
                 polje+ " like '"+vUp+"%' or artikli."+polje+" like '"+value+"%') ";
        }
      } else if (polje.equalsIgnoreCase("NAZART")) {
        String ch = begNazart ? "" : "%";
        String vLo = value.toLowerCase();
        String vUp = value.toUpperCase();
        String vCap = value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase();
        qStr = qStr+" and ("+ 
               polje+ " like '"+ch+value+"%' or "+
               polje+ " like '"+ch+vLo+"%' or "+
               polje+ " like '"+ch+vUp+"%' or "+
               polje+" like '"+ch+vCap+"%') ";
      } else if (polje.equalsIgnoreCase("CGRART")) {
        qStr = qStr+" and CGRART ='"+value+"'";  
      }
      
    }
    
    //qStr = qStr +" order by ARTIKLI."+polje;
//System.out.println(qStr);
    Column kolpak = dm.getStanje().getColumn("KOL").cloneColumn();
    kolpak.setColumnName("KOLPAK");
    kolpak.setCaption("Pakiranje");
    kolpak.setPrecision(3);
    Column jmpak = dm.getArtikli().getColumn("JMPAK").cloneColumn();
    jmpak.setCaption("JM pak.");
    Column brjed = dm.getArtikli().getColumn("BRJED").cloneColumn();
//    brjed.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    stanjef8 = new StorageDataSet();
    
    stanjef8.setColumns(new Column[] {
            (Column) dm.getArtikli().getColumn("CART").clone(),
             dm.getArtikli().getColumn("CART1").cloneColumn(),
            (Column) dm.getArtikli().getColumn("BC").clone(),
            (Column) dm.getArtikli().getColumn("CGRART").clone(),
            (Column) dm.getArtikli().getColumn("NAZART").clone(),
            (Column) dm.getStanje().getColumn("KOL").clone(),
            (Column) dm.getStanje().getColumn("KOLSKLAD").clone(),
            (Column) dm.getStanje().getColumn("KOLREZ").clone(),
            dm.getArtikli().getColumn("JM").cloneColumn() ,
            kolpak,jmpak,brjed,
            (Column) dm.getArtikli().getColumn("NC").clone() ,
            (Column) dm.getStanje().getColumn("VC").clone() ,
            (Column) dm.getStanje().getColumn("MC").clone()
        });
        stanjef8.getColumn("CART").setWidth(10);
        stanjef8.getColumn("CART1").setWidth(15);
        stanjef8.getColumn("BC").setWidth(15);
        stanjef8.getColumn("NAZART").setWidth(60);
        stanjef8.open();
        
        qdsstanjef8 = ut.getNewQueryDataSet(qStr);
        for (qdsstanjef8.first();qdsstanjef8.inBounds();qdsstanjef8.next()){
            stanjef8.insertRow(false);
            stanjef8.setInt("CART",qdsstanjef8.getInt("CART"));
            stanjef8.setString("CART1",qdsstanjef8.getString("CARTX"));
            stanjef8.setString("BC",qdsstanjef8.getString("BC"));
            stanjef8.setString("CGRART",qdsstanjef8.getString("CGRART"));
            stanjef8.setString("NAZART",qdsstanjef8.getString("NAZART"));
            stanjef8.setBigDecimal("KOL",qdsstanjef8.getBigDecimal("KOL"));
            stanjef8.setBigDecimal("KOLSKLAD",qdsstanjef8.getBigDecimal("KOLSKLAD"));
            stanjef8.setBigDecimal("KOLREZ",qdsstanjef8.getBigDecimal("KOLREZ"));
            stanjef8.setBigDecimal("KOLPAK",qdsstanjef8.getBigDecimal("KOL"));
            stanjef8.setString("JM",qdsstanjef8.getString("JM"));
            stanjef8.setString("JMPAK",qdsstanjef8.getString("JMPAK"));
            stanjef8.setBigDecimal("BRJED",qdsstanjef8.getBigDecimal("BRJED"));
            stanjef8.setBigDecimal("NC",qdsstanjef8.getBigDecimal("NC"));
            stanjef8.setBigDecimal("VC",qdsstanjef8.getBigDecimal("VC"));
            stanjef8.setBigDecimal("MC",qdsstanjef8.getBigDecimal("MC"));
            if (stanjef8.getBigDecimal("BRJED").doubleValue()!=0){
                stanjef8.setBigDecimal("KOLPAK",            
                        stanjef8.getBigDecimal("KOL").
                            divide(stanjef8.getBigDecimal("BRJED"),3,BigDecimal.ROUND_HALF_DOWN));
//                  System.out.println("Radim pet 1 "+stanjef8.getBigDecimal("KOLPAK"));
                } else {
//                  System.out.println("Radim pet 2 "+stanjef8.getBigDecimal("KOLPAK"));
                    stanjef8.setBigDecimal("BRJED",new BigDecimal("1.00"));
                    stanjef8.setBigDecimal("KOLPAK",            
                            stanjef8.getBigDecimal("KOL"));
                }
            }
            if (stanjef8.getString("JMPAK").equalsIgnoreCase("") || 
                    stanjef8.getString("JMPAK") == null){
                stanjef8.setString("JMPAK",stanjef8.getString("JM"));
            }
    stanjef8.setTableName("STANJAZADOHVATSAF8");
    //lookupData LD = lookupData.getlookupData();
//    result = LD.lookUp((java.awt.Frame)c, vl.RezSet, new int[] {0, 1, 2, 3, 4, 5, 6});

    LD.lupFrWidth = 750;
    return LD.lookUp(owner, stanjef8, 
       new String[] {polje,"CART","CART1","BC"}, 
       new String[] {"", "", "", ""}, 
       new int[] {0, 4, 5,6,7,8 });
  }


/*  public void createKPR(java.sql.Timestamp ts, String cskl) {
    Date dateZ = new Date(ts.getDate());
    String qStr;
// Primke
    qStr="SELECT MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, "+
         "MAX(DOKU.DATDOK) AS DATDOK, STDOKU.VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, "+
         "SUM(STDOKU.IZAD+STDOKU.PORAV) AS SUMZAD, 0.00 AS SUMRAZ "+
         "from STDOKU, DOKU "+
         "where STDOKU.CSKL='"+cskl+"' AND "+util.getDoc("DOKU", "STDOKU")+
         " AND DOKU.DATDOK <= "+dateZ+" GROUP BY STDOKU.VRDOK, STDOKU.BRDOK "+
         "UNION "+
// Poravnanje
    "select MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKU.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, SUM(STDOKU.PORAV) AS SUMZAD, 0.00 AS SUMRAZ "+
         "from STDOKU,DOKU "+
         "where STDOKU.CSKL='"+cskl+"' AND "+util.getDoc("DOKU", "STDOKU")+" AND STDOKU.PORAV<>0 AND STDOKU.VRDOK NOT IN ('POR') "+
         " AND DOKU.DATDOK <= "+dateZ+" GROUP BY  STDOKU.VRDOK, STDOKU.BRDOK  "+
         "UNION "+
// Medjuskladisnice - ulaz
    "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND,  MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZUL) AS SUMZAD, 0.00 AS SUMRAZ "+
         "from STMESKLA, MESKLA "+
         "where  STMESKLA.CSKLUL='"+cskl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
         " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +
         " AND MESKLA.DATDOK <= "+dateZ+" GROUP BY MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " +
         "UNION "+
// Medjuskladisnice - Poravnanje
    "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.PORAV) AS SUMZAD, 0.00 AS SUMRAZ "+
         "from STMESKLA,MESKLA "+
         "where STMESKLA.CSKLUL='"+cskl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 "+
         " AND MESKLA.DATDOK <= "+dateZ+" GROUP BY MESKLA.VRDOK,  MESKLA.BRDOK " + izlazi;


  }
  private String izlazi(String cSkl, String dateP, String dateZ, String parametar) {
    String izl="";
    if (parametar.equals("N")) {
// Izlazi
//      MAX(DOKU.CSKL)
//      izl="UNION select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, "+
      izl="UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, "+
          "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  0.00 AS SUMZAD, SUM(STDOKI.ISP) AS SUMRAZ "+
          "from STDOKI, DOKI "+
          "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiN("STDOKI")+
          " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" GROUP BY STDOKI.BRDOK, STDOKI.VRDOK "+
          "UNION "+
// Medjuskladisnice izlaz
      "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, 0.00 AS SUMZAD, SUM(STMESKLA.ZADRAZIZ) AS SUMRAZ "+
          "from STMESKLA, MESKLA "+
          "where  STMESKLA.CSKLIZ='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
          " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +
          dokumentiN("STMESKLA")+
          " AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <="+dateZ+" GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLIZ, MESKLA.CSKLUL, MESKLA.GOD ";
    } else {
// Izlazi -> tu je promjena
//      izl="UNION select MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(-STDOKI.UIRAB) AS SUMZAD, SUM(STDOKI.IPRODSP) AS SUMRAZ "+
      izl="UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, "+
          "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, "+
          "SUM(STDOKI.IPRODSP) AS SUMRAZ "+
          "from STDOKI, DOKI "+
          "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiE("STDOKI")+
          " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" GROUP BY STDOKI.BRDOK, STDOKI.VRDOK "+
          "UNION "+
// Medjuskladisnice izlaz
      "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, 0.00 AS SUMRAZ "+
          "from STMESKLA, MESKLA "+
          "where  STMESKLA.CSKLIZ='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
          " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +dokumentiE("STMESKLA")+
          " AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <="+dateZ+" GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL UNION "
         + dodaciIzlaza(cSkl, dateP, dateZ);
    }
    return izl;
  }
*/

}