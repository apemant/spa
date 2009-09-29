/****license*****************************************************************
**   file: Valid.java
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
package hr.restart.util;

/**
 * Po ulaznim parametrima prikazuje podatke za dohvat. Ne instancira se nego se poziva sa Valid.getValid.metoda
 */
import hr.restart.baza.Condition;
import hr.restart.baza.Knjigod;
import hr.restart.baza.Refresher;
import hr.restart.baza.SEQ;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.zapod.OrgStr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Valid {
//sysoutTEST ST = new sysoutTEST(false);
  private static Valid myValid = new Valid();
  public QueryDataSet RezSet;
  com.borland.dx.dataset.Column col;
  com.borland.dx.dataset.Variant vv = new com.borland.dx.dataset.Variant();
  dM dm = dM.getDataModule();
  
  
  public static String lastApp = null;
  public Valid() {
  }

  public static Valid getValid() {
    return myValid;
  }
  public static void setApp(Class _class) {
    StringTokenizer tok = new StringTokenizer(_class.getPackage().getName(),".");
    String _app = "";
    do {
      _app = tok.nextToken();
    } while (tok.hasMoreTokens());
    if (!_app.equals("util")) lastApp = _app;
//    System.out.println("lastApp = "+lastApp);
  }
  /**
   * Izvrsi zadani SQL query nad bazom i nista ne vrati
   * kod funkcije je: dm.getDatabase1().executeStatement(SQLQuery);
   */
  public boolean runSQL(String SQLQuery) {
    Refresher.postpone();
    try {
      dm.getDatabase1().executeStatement(SQLQuery);
      return true;
    } catch (Exception e) {
      System.out.println("runSQL: Query nije uspio! Query = "+SQLQuery);
      return false;
//      e.printStackTrace();
    }
  }
  /**
   * Izvrsi zadani SQL query nad bazom i vrati podatke u com.borland.dx.sql.dataset.QueryDataSet RezSet.
   * Molim nakon toga pocistiti smece (RezSet=null)
   */
  public void execSQL(String SQLQuery) {
    RezSet = new com.borland.dx.sql.dataset.QueryDataSet();
    Aus.setFilter(RezSet, SQLQuery);
    Refresher.postpone();
//    RezSet.executeQuery();
  }
  
  public QueryDataSet getDataAndClear() {
    QueryDataSet ret = RezSet;
    RezSet = null;
    return ret;
  }
/**
 * Provjerava da li postoji zapis u QueryDataSetu zadane kolone sa zadanom vrijednoscu u zadanoj koloni :)
 */
  public boolean chkExistsSQL(com.borland.dx.dataset.Column col,String colvalue) {
    if (colvalue.trim().equals("")) return false; //ne jebem ako nista nije upisano
    if (colvalue==null) return false; //ne jebem ako nista nije upisano
    execSQL("SELECT * FROM " + col.getDataSet().getTableName() + " WHERE " + getQuerySintax(col,colvalue,true));
    RezSet.open();
    if (RezSet.getRowCount() > 0) {
      RezSet = null;
      col = null;
      return true;
    }
    RezSet = null;
    col = null;
    return false;
  }

  String getQualifier(com.borland.dx.dataset.Column col) {
    String tableName = col.getDataSet().getTableName();
    if (tableName == null) return "";
    if (tableName.equals("")) return "";
    return tableName.trim().concat(".");
  }
  String getQueryColumnName(com.borland.dx.dataset.Column col) {
    return getQualifier(col)+col.getServerColumnName();
  }
  String getQuerySintax(com.borland.dx.dataset.Column col,String colvalue,boolean isLast) {
    String cstr="";
    if (col.getDataType()==com.borland.dx.dataset.Variant.TIMESTAMP) {
      cstr = "("+getQueryColumnName(col)+ " BETWEEN "
          + strLimiter(col) + colvalue.substring(0,10) + " 00:00:00" + strLimiter(col) + " AND "
          + strLimiter(col) + colvalue.substring(0,10) + " 23:59:59" + strLimiter(col) + ") AND ";
    } else {
      cstr = getQueryColumnName(col) + " = " + strLimiter(col) + colvalue + strLimiter(col) + " AND ";
    }
    if (isLast) cstr = cstr.substring(0,cstr.length()-5);
    return cstr;
  }
  private String getNot(boolean b) {
    if (!b) return "not ";
    return "";
  }
  private String getNot2(boolean b) {
    if (!b) return " !";
    return "";
  }

  private String getORAND(boolean b) {
    if (b) return " OR ";
    return " AND ";
  }

  String getNullQuerySintax(com.borland.dx.dataset.Column col,boolean isnull,boolean isLast) {
    String cstr="";
    if (isNumeric(col)) {
      cstr = "(" + getQueryColumnName(col)+getNot2(isnull)+"=0"+getORAND(isnull)+getQueryColumnName(col)+" is "+getNot(isnull)+"null)  AND ";
    } else if (isTime(col)) {
      cstr = getQueryColumnName(col) + " is "+getNot(isnull)+"null AND ";
    } else {
      cstr = "(" + getQueryColumnName(col) + getNot2(isnull)+"=''"+getORAND(isnull)+getQueryColumnName(col)+" is "+getNot(isnull)+"null)  AND ";
    }
    if (isLast) cstr = cstr.substring(0,cstr.length()-5);
    return cstr;
  }

  public String getBetweenQuerySintax(com.borland.dx.dataset.Column col,String colvaluefrom,String colvalueto,boolean isLast) {
    String cstr="";
    if (col.getDataType()==com.borland.dx.dataset.Variant.TIMESTAMP) {
      colvaluefrom = colvaluefrom.substring(0,10) + " 00:00:00";
      colvalueto = colvalueto.substring(0,10) + " 23:59:59";
    }
    cstr = "("+getQueryColumnName(col) + " BETWEEN "
          + strLimiter(col) + colvaluefrom + strLimiter(col) + " AND "
          + strLimiter(col) + colvalueto + strLimiter(col) + ") AND ";

    if (isLast) cstr = cstr.substring(0,cstr.length()-5);
    return cstr;
  }

/**
 * Vraca query od QueryDataSeta u string nakon sto mu stripne moguci WHERE u nastavku i sve nakon njega.
 * Ako je npr. queryDataSet.getOriginalQuery() = "SELECT * FROM stdoku where godina=2002 and vrdok='PRK' and brdok=777";
 * getNoWhereQuery(queryDataSet) = "select * from stdoku", tako da se query moze dinamicki promijeniti
 */
  public String getNoWhereQuery(com.borland.dx.sql.dataset.QueryDataSet qds) {
    String oQ = qds.getOriginalQueryString().toLowerCase();
    String queryFinder = "from "+getTableName(oQ).toLowerCase();
    int startidx = oQ.indexOf(queryFinder);
    int endidx = startidx+queryFinder.length();
    return oQ.substring(0,endidx);
  }
  public static String getTableName(String oQ) {
    try {
      oQ = oQ.toLowerCase();
      StringTokenizer t = new StringTokenizer(oQ," ");
      do {
        String s1 = t.nextToken();
        if (s1.toLowerCase().equals("from")) return t.nextToken();
      } while (t.hasMoreTokens());
    } catch (Exception e) {
      //
    }
    return "unknown";
  }
/**
 * Provjerava unique na kompozitnom kljucu sve zadane kolone moraju pripadati istoj tabeli (isti Column.getTableName)
 * i sve vrijednosti kolona moraju biti popunjene. Ako ti uvjeti nisu zadovoljeni vraca false
 */
  public boolean chkExistsSQL(com.borland.dx.dataset.Column[] cols,String[] colvalues) {
    if (cols==null) return false;
    if (colvalues==null) return false;
    if (cols[0].getTableName()==null) return false;
    if (cols[0].getTableName().trim().equals("")) return false;
    for (int i=0;i<colvalues.length;i++) if (colvalues[i].trim().equals("")) return false;

    String tabName = cols[0].getTableName();
    String sQuery = "SELECT * FROM " + tabName + " WHERE ";

    for (int i=0;i<cols.length;i++) {
      if (cols[i].getDataSet()==null) return false;
      if (!cols[i].getTableName().equals(tabName)) return false;
      sQuery = sQuery + getQuerySintax(cols[i],colvalues[i],false);
    }
    sQuery = sQuery.substring(0,sQuery.length()-5); //stripping "AND"
    //sQuery = sQuery.concat(";");
    //System.out.println("Valid.chkExistsSQL() ::"+sQuery);
    execSQL(sQuery);
    RezSet.open();
    if (RezSet.getRowCount() > 0) {
      RezSet = null;
      return true;
    }
    RezSet = null;
    return false;
  }

  String strLimiter(com.borland.dx.dataset.Column col) {
    if (!isNumeric(col)) return "'";
    return "";
  }
/**
 * Provjerava da li postoji VISE OD JEDAN zapis u QueryDataSetu zadane kolone sa zadanom vrijednoscu u zadanoj koloni :)
 */
  public boolean chkUPDExistsSQL(com.borland.dx.dataset.Column col,String colvalue, String[] keyColNames) {
    if (colvalue.trim().equals("")) return false; //ne jebem ako nista nije upisano
    if (colvalue==null) return false; //ne jebem ako nista nije upisano
    execSQL("SELECT * FROM " + col.getDataSet().getTableName() + " WHERE " + getQuerySintax(col,colvalue,true)/* + ";"*/);
    RezSet.open();
    if (RezSet.getRowCount() > 1) {
      RezSet = null;
      col = null;
      return true;
    }
    if (RezSet.getRowCount() > 0) {
//Ako azurira drugi slog sa podatkom koji je unesen u prvi succcck!!
//Trebalo bi provjeriti po kljucu da li je to taj slog ili nije
      if (!isSameRecord(RezSet,col.getDataSet(),keyColNames)) {
        RezSet = null;
        col = null;
        return true;
      }
    }
    RezSet = null;
    col = null;
    return false;
  }
  private boolean isSameRecord(ReadRow rr1, ReadRow rr2, String[] keyColNames) {
    for (int i=0;i<keyColNames.length;i++) {
      if (!Util.getUtil().equalsBVariant(rr1,rr2,keyColNames[i],keyColNames[i])) {
        return false;
      }
    }
    return true;
  }
/**
 * <pre>
 * Provjerava da li postoji zapis sa podatkom upisanim u zadane parametre pomocu SQL querya
 * Parametri su:
 * - QueryDataSet za provjeru
 * - column name za provjeru
 * - vrijednost kolone za provjeru
 * Vraca:
 * true  - ako postoji takav zapis
 * false - ako ne postoji takav zapis
 * Baca error message:
 * - ako neki od parametara ne valja - vraca false
 * </pre>
 */
  public boolean chkExistsSQL(com.borland.dx.sql.dataset.QueryDataSet qds, String colname, String colvalue) {
    try {
      col = qds.getColumn(colname);
    } catch (Exception e) {
      System.out.println("chkExistsSQL: Neki od parametara nije ispravan");
      return false;
    }
    return chkExistsSQL(col,colvalue);
  }
/**
 * <pre>
 * Provjerava da li postoji zapis sa podatkom upisanim u zadane parametre pomocu SQL querya
 * Parametri su:
 * - QueryDataSet za provjeru
 * - column name za provjeru
 * - vrijednost kolone za provjeru
 * Vraca:
 * true  - ako postoji VISE OD JEDAN takav zapis
 * false - ako postoji JEDAN ILI NITI JEDAN takav zapis
 * Baca error message:
 * - ako neki od parametara ne valja - vraca false
 * </pre>
 */
  public boolean chkUPDExistsSQL(com.borland.dx.sql.dataset.QueryDataSet qds, String colname, String colvalue, String[] keyColNames) {
    try {
      col = qds.getColumn(colname);
    } catch (Exception e) {
      System.out.println("chkExistsSQL: Neki od parametara nije ispravan");
      return false;
    }
    return chkUPDExistsSQL(col,colvalue,keyColNames);
  }

  private String getText(com.borland.dx.dataset.Column col) {
    if (col.getDataType() == com.borland.dx.dataset.Variant.TIMESTAMP) {
        String tsText = col.getDataSet().getTimestamp(col.getColumnName()).toString();
        return tsText;
    } else return lookupData.getlookupData().getColStringVal(col,col.getDataSet());
  }
/**
 * <pre>
 * Provjerava da li postoji zapis sa podatkom upisanim u zadanu db komponentu pomocu SQL querya
 * Kao parametar se daje dbswing (ili raswing) text komponenta, a vraca:
 * true  - ako postoji takav zapis
 * false - ako ne postoji takav zapis
 * Baca error message:
 * - ako text komponenta nije db komponenta - vraca false
 * </pre>
 */
  public boolean chkExistsSQL(javax.swing.text.JTextComponent jt) {
    try {
      col = ((com.borland.dx.dataset.ColumnAware)jt).getDataSet().getColumn(((com.borland.dx.dataset.ColumnAware)jt).getColumnName());
    } catch (Exception e) {
      System.out.println("chkExistsSQL: Komponenta nije bindana na DataSet, kontrola nije moguca");
      return false;
    }
    return chkExistsSQL(col,jt.getText());
  }
  public boolean chkExistsSQL(javax.swing.text.JTextComponent[] jts) {
    com.borland.dx.dataset.Column[] cols = new com.borland.dx.dataset.Column[jts.length];
    String[] colvals = new String[jts.length];
    for (int i=0;i<cols.length;i++) {
      try {
        cols[i] = ((com.borland.dx.dataset.ColumnAware)jts[i]).getDataSet().getColumn(((com.borland.dx.dataset.ColumnAware)jts[i]).getColumnName());
        colvals[i] = getText(cols[i]);
      } catch (Exception e) {
         System.out.println("chkExistsSQL: Jedna od komponenti nije bindana na DataSet, kontrola nije moguca");
         e.printStackTrace();
        return false;
      }
    }
    return chkExistsSQL(cols,colvals);
  }
/**
 * <pre>
 * Provjerava da li postoji VISE OD JEDAN zapis sa podatkom upisanim u zadanu db komponentu pomocu SQL querya
 * Kao parametar se daje dbswing (ili raswing) text komponenta, a vraca:
 * true  - ako postoji VISE OD JEDAN takav zapis
 * false - ako ne postoji VISE OD JEDAN takav zapis
 * Baca error message:
 * - ako text komponenta nije db komponenta - vraca false
 * </pre>
 */
  public boolean chkUPDExistsSQL(javax.swing.text.JTextComponent jt,String[] keyColNames) {
    try {
      col = ((com.borland.dx.dataset.ColumnAware)jt).getDataSet().getColumn(((com.borland.dx.dataset.ColumnAware)jt).getColumnName());
    } catch (Exception e) {
      System.out.println("chkExistsSQL: Komponenta nije bindana na DataSet, kontrola nije moguca");
      return false;
    }
    return chkUPDExistsSQL(col,jt.getText(),keyColNames);
  }

/**
 * <pre>
 * Provjerava da li postoji zapis sa zadanim podatkom upisanim u zadanu kolonu pomocu metoda u DataSetu
 * Parametri:
 *  col - kolona u datasetu za provjeru
 *  colvalue - string prezentacija vrijednosti za provjeru
 * Metoda vraca
 * true  - ako postoji takav zapis
 * false - ako ne postoji takav zapis
 * Baca error message:
 * - ako je neuspjesan com.borland.dx.text.VariantFormatStr.parse - vraca false
 *
 * POZOR!!!:
 *  Metoda koristi metodu DataSet.lookup koja automatski posta current record koji je u datasetu zadane kolone
 *  nije niposto za provjeru uniquea pri dodavanju
 * </pre>
 */
  public boolean chkExistsDS(com.borland.dx.dataset.Column col, String colvalue) {
    com.borland.dx.dataset.DataRow DRow = new com.borland.dx.dataset.DataRow(col.getDataSet(),col.getColumnName());
    com.borland.dx.dataset.DataRow RezDRow = new com.borland.dx.dataset.DataRow(col.getDataSet(),col.getColumnName());
    com.borland.dx.dataset.Variant vV = new com.borland.dx.dataset.Variant();
    com.borland.dx.text.VariantFormatStr vF = new com.borland.dx.text.VariantFormatStr(null,col.getDataType());
    try {
      vF.parse(colvalue,vV);
      DRow.setVariant(col.getColumnName(),vV);
      return col.getDataSet().lookup(DRow,RezDRow,com.borland.dx.dataset.Locate.FIRST);
    } catch (Exception ex) {
      System.out.println("chkExistsDS: Neuspjesno prebacivanje string vrijednosti u DataRow");
      return false;
    }
  }
  public boolean isNumeric(com.borland.dx.dataset.Column col) {
    return 
      col.getDataType() == Variant.BIGDECIMAL ||
      col.getDataType() == Variant.INT ||
      col.getDataType() == Variant.LONG ||
      col.getDataType() == Variant.SHORT ||
      col.getDataType() == Variant.DOUBLE ||
      col.getDataType() == Variant.FLOAT;
    /*
    return (col.getSqlType() == java.sql.Types.NUMERIC)
           ||(col.getSqlType() == java.sql.Types.INTEGER)
           ||(col.getSqlType() == java.sql.Types.BIGINT)
           ||(col.getSqlType() == java.sql.Types.DECIMAL)
           ||(col.getSqlType() == java.sql.Types.DOUBLE)
           ||(col.getSqlType() == java.sql.Types.FLOAT)
           ||(col.getSqlType() == java.sql.Types.SMALLINT)
           ||(col.getSqlType() == java.sql.Types.TINYINT);*/
  }
  public boolean isTime(com.borland.dx.dataset.Column col) {
    return (col.getSqlType() == java.sql.Types.TIME)
           ||(col.getSqlType() == java.sql.Types.TIMESTAMP)
           ||(col.getSqlType() == java.sql.Types.DATE);
  }
  private String getColCaption(javax.swing.text.JTextComponent jt) {
    com.borland.dx.dataset.Column col;
    com.borland.dx.dataset.ColumnAware caw;
    if (jt instanceof com.borland.dx.dataset.ColumnAware) {
      try {
        caw = (com.borland.dx.dataset.ColumnAware)jt;
        col = caw.getDataSet().getColumn(caw.getColumnName());
        return col.getCaption().toUpperCase();
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }
  private String getUniqueErrText(javax.swing.text.JTextComponent jt) {
    java.util.ResourceBundle dmres = java.util.ResourceBundle.getBundle("hr.restart.baza.dmRes");
    return dmres.getString("errRef_int")+" !";
    //+" "+getColCaption(jt)+" !";
  }
  private String getEmptyErrText(javax.swing.text.JTextComponent jt) {
    java.util.ResourceBundle dmres = java.util.ResourceBundle.getBundle("hr.restart.baza.dmRes");
    return dmres.getString("errReq_unos")+" "+getColCaption(jt)+" !";
  }

  public void showValidErrMsg(javax.swing.text.JTextComponent jt,char type) {
    java.util.ResourceBundle dmres = java.util.ResourceBundle.getBundle("hr.restart.baza.dmRes");
    String msgText;
    if (type=='U') {
      msgText = getUniqueErrText(jt);
    } else if (type=='E') {
      msgText = getEmptyErrText(jt);
    } else {
      msgText = "Greška";
    }
    java.awt.Component parent;
    if (jt == null) {
      if (hr.restart.start.isMainFrame())
        parent = hr.restart.mainFrame.getMainFrame();
      else
        parent = null;
    } else {
      parent = jt.getTopLevelAncestor();
    }
    javax.swing.JOptionPane.showMessageDialog(
      parent,
      msgText,
      dmres.getString("errMain"),
      javax.swing.JOptionPane.ERROR_MESSAGE);
  }
  /**
   * Vraca da li je unesena ikakva vrijednost u db textualnu komponentu
   */
  public boolean chkIsEmpty(javax.swing.text.JTextComponent jt) {
    if (jt == null) {
//ST.prn("jt == null");
      return true;
    }
    if (jt.getText() == null) {
//ST.prn("jt.getText() == null");
      return true;
    }
    if (jt.getText().trim().equals("")) {
//ST.prn("jt.getText().trim().equals('')");
      return true;
    }
    try {
      col = ((com.borland.dx.dataset.ColumnAware)jt).getDataSet().getColumn(((com.borland.dx.dataset.ColumnAware)jt).getColumnName());
    } catch (Exception e) {
//ST.prn("EXEPTION: jt.getText().trim().equals('')");
      return (jt.getText().trim().equals(""));
    }
    return chkIsEmpty(col,jt.getText());
  }

  public boolean chkIsEmpty(com.borland.dx.dataset.Column col,String val) {
      if (val==null) return true;
      if (val.equals("")) return true;
      if (isNumeric(col)) {
      java.math.BigInteger iV=null;
      try {
        iV = new java.math.BigInteger(stripNoDigits(val));
      } catch (Exception e2) {
//ST.prn("catch new integer "+stripNoDigits(val));
       return true; //valjda je prazno
      }
//ST.prn("iV.intValue()=="+iV.intValue());
      return (iV.intValue()==0);
    }
    return false;
  }

  private String stripNoDigits(String s) {
    char[] c = s.toCharArray();
    for (int i=0;i<c.length;i++) {
      if (!Character.isDigit(c[i])) c[i] = '0';
    }
    return new String(c);
  }
  /**
   * Provjerava prvo da li je vrijednost upisana, pa baca poruku, ako to prodje provjerava da li ima takav i baca poruku.
   * Ako ne baci niti jednu poruku vraca false
   */
  public boolean notUnique(javax.swing.text.JTextComponent jt) {
    if (isEmpty(jt)) return true;
    if (chkExistsSQL(jt)) {
      jt.requestFocus();
      showValidErrMsg(jt,'U');
      return true;
    }
    return false;
  }
  /**
   * Radi isto sto i {@link #notUnique(javax.swing.text.JTextComponent)} samo za kompozitne kljuceve, odnosno array text komponenti
   */
  public boolean notUnique(javax.swing.text.JTextComponent[] jts) {
    for (int i=0;i<jts.length;i++) if (isEmpty(jts[i])) return true;
    if (chkExistsSQL(jts)) {
      jts[0].requestFocus();
      showValidErrMsg(jts[0],'U');
      return true;
    }
    return false;
  }
  /**
   * <pre>
   * Provjerava prvo da li je vrijednost upisana, pa baca poruku, ako to prodje provjerava da li ima DVA takva i baca poruku.
   * Ako ne baci niti jednu poruku vraca false.
   * Sluzi za kontrolu uniquea kod update-a, a ne radi se o primarnom kljucu.
   * U drugom parametru potrebno je navesti imena kolona koja sadrze unique kljuc te tabele po kojem bi se mogao pronaci taj slog.
   * Npr. tabela partneri ima unique kljuc CPAR, ali su mu unique i MB i ZIRO i onda je najbolje proizveesti ovakav kod:
   * String[] key = new String[] {"CPAR"};
   * if (Valid.notUniqueUPD(jtMB,key) return false
   * if (Valid.notUniqueUPD(jtZIRO,key) return false
   * </pre>
   */
  public boolean notUniqueUPD(javax.swing.text.JTextComponent jt, String[] keyColNames) {
    if (isEmpty(jt)) return true;
    if (chkUPDExistsSQL(jt,keyColNames)) {
      jt.requestFocus();
      showValidErrMsg(jt,'U');
      return true;
    }
    return false;
  }
  /**
   * Provjerava da li je vrijednost upisana, pa baca poruku ako nije
   */
  public boolean isEmpty(javax.swing.text.JTextComponent jt) {
    if (chkIsEmpty(jt)) {
      jt.requestFocus();
      showValidErrMsg(jt,'E');
      return true;
    }
    return false;
  }
  public int getSetCount(com.borland.dx.sql.dataset.QueryDataSet qds,int ordinal) {
    qds.open();
    int setcount;
    if (qds.getColumn(ordinal).getSqlType() == java.sql.Types.INTEGER) {
      setcount = qds.getInt(ordinal);
    } else {
      setcount = (int)qds.getLong(ordinal);
    }
    return setcount;
  }
  /**
   * Provjerava da li query na bazu vraca previse slogova zbog moguceg zagusenja memorije.
   * Ako vraca baca poruku sa poukom o pravnom lijeku.
   */
  public boolean chkTooBigSet(String query) {/*
    int idxfrom = query.indexOf("from");
    if (idxfrom == -1) return true;
    String countQuery = "select count(*) as setcount "+query.substring(idxfrom);
    execSQL(countQuery);
    int setcount = getSetCount(RezSet,0);
    if (setcount > raDataSetAdmin.MAXROWS) {
      javax.swing.JOptionPane.showMessageDialog(null,"Odabrano je previše podataka ("+setcount+") !!! Prikazujem samo prvih "+raDataSetAdmin.MAXROWS);
//      return false;
    }*/
    return true;
  }

  public Double findSeqDouble(String cOpis) {
    return findSeqDouble(cOpis,true);
  }

  public String getKnjigYear(String _app) {
    String god = getLastKnjigYear(_app);
    return god == null ? findYear() : god;
  }

  public String getLastKnjigYear(String _app) {
//    hr.restart.util.lookupData lF = hr.restart.util.lookupData.getlookupData();
//    dm.getKnjigod().open();
//    if (lF.raLocate(dm.getKnjigod(),new String[] {"CORG","APP"},new String[] {OrgStr.getKNJCORG(),_app})) {
//      return dm.getKnjigod().getString("GOD");
//    } else return findYear();
    DataSet ds = Knjigod.getDataModule().getTempSet(Condition.equal("CORG",
        OrgStr.getKNJCORG(false)).and(Condition.equal("APP", _app)));
    ds.open();
    if (ds.rowCount() == 0) return null;
    String god = "1970";
    for (ds.first(); ds.inBounds(); ds.next())
      if (ds.getString("GOD").compareTo(god) > 0)
        god = ds.getString("GOD");
    lookupData.getlookupData().raLocate(dm.getKnjigod(), new String[] {"CORG", "APP", "GOD"},
                                        new String[] {OrgStr.getKNJCORG(false), _app, god});
    return god;
  }

  public void setSeqFilter(String cOpis) {
//    String _god = getKnjigYear(lastApp);
//    String _knjig = OrgStr.getKNJCORG();
//    SEQ.getDataModule().setFilter(Condition.whereAllEqual(
//        new String[] {"OPIS", "KNJIG", "GOD", "APP"},
//        new String[] {cOpis, _knjig, _god, lastApp}
//    ));
//    dm.getSeq().open();
//    if (dm.getSeq().rowCount() == 0) {
      SEQ.getDataModule().setFilter(Condition.equal("OPIS", cOpis));
      if (!dm.getSeq().isOpen()) dm.getSeq().open();
      else dm.getSeq().refresh();
      if (dm.getSeq().rowCount() > 0) {
        if (!dm.getSeq().getString("LOKK").equalsIgnoreCase("N"))
          throw new SeqLockedException();
//        dm.getSeq().setString("KNJIG",_knjig);
//        dm.getSeq().setString("GOD",_god);
//        dm.getSeq().setString("APP",lastApp);
//        System.err.println("Warning: seq sa opisom "+cOpis+" azuriran !");
//        System.err.println("  => knjig = "+_knjig+"; god = "+_god+"; app = "+lastApp);
      } else {
        dm.getSeq().insertRow(true);
//        dm.getSeq().setString("KNJIG",_knjig);
//        dm.getSeq().setString("GOD",_god);
//        dm.getSeq().setString("APP",lastApp);
        dm.getSeq().setString("OPIS", cOpis);
        dm.getSeq().setDouble("BROJ", 0);
      }
//     } else if (!dm.getSeq().getString("LOKK").equalsIgnoreCase("N"))
//       throw new SeqLockedException();
  }

  public Double findSeqDouble(String cOpis, boolean save) {
    return findSeqDouble(cOpis, save, false);
  }

  public Double findSeqDouble(String cOpis, boolean save, boolean lock) {
    setSeqFilter(cOpis);
    if (lock) dm.getSeq().setString("LOKK", "D");
    dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")+1);
    if (!save && !lock) dm.getSeq().post();
    else if (lock && save) dm.getSeq().saveChanges();
    else raTransaction.saveChanges(dm.getSeq());
    return new Double(dm.getSeq().getDouble("BROJ"));
  }

/*  public Double findSeqDouble(String cOpis, boolean save) {
    hr.restart.util.lookupData lF = hr.restart.util.lookupData.getlookupData();
//    String[] filter = {cOpis,""};
//    String[] colnames = {"OPIS","BROJ"};
    String _god = getKnjigYear(lastApp);
    String _knjig = OrgStr.getKNJCORG();
    String[] filter = {cOpis,_knjig,_god,lastApp};
    String[] colnames = {"OPIS","KNJIG","GOD","APP"};
    dm.getSeq().open();
//    String[] result = lF.lookUp(null,dm.getSeq(),colnames,filter,null);
//    dm.getSeq().interactiveLocate(cOpis,"OPIS",Locate.FIRST, false);
//    if (result!=null) {
    if (lF.raLocate(dm.getSeq(),colnames,filter)) {
      dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")+1);
    } else if (lF.raLocate(dm.getSeq(),"OPIS",cOpis)) {
      dm.getSeq().setDouble("BROJ", dm.getSeq().getDouble("BROJ")+1);
      dm.getSeq().setString("KNJIG",_knjig);
      dm.getSeq().setString("GOD",_god);
      dm.getSeq().setString("APP",lastApp);
      System.out.println("Warning: seq sa opisom "+cOpis+" azuriran !");
      System.out.println("  => knjig = "+_knjig+"; god = "+_god+"; app = "+lastApp);

    } else {
      dm.getSeq().insertRow(true);
      dm.getSeq().setString("KNJIG",_knjig);
      dm.getSeq().setString("GOD",_god);
      dm.getSeq().setString("APP",lastApp);
      dm.getSeq().setString("OPIS", cOpis);
      dm.getSeq().setDouble("BROJ", 1);
    }
    dm.getSeq().post();
//    dm.getSeq().saveChanges();
    if (save) raTransaction.saveChanges(dm.getSeq());
    Double pero=new Double(dm.getSeq().getDouble("BROJ"));
    return pero;
  } */

  public void unlockCurrentSeq(boolean save) {
    dm.getSeq().setString("LOKK", "N");
    if (save) dm.getSeq().saveChanges();
    else raTransaction.saveChanges(dm.getSeq());
  }

  public void unlockSeq(String cOpis, boolean save) {
    try {
      setSeqFilter(cOpis);
    } catch (SeqLockedException e) {
      unlockCurrentSeq(save);
    }
  }

  public String findSeq(String cOpis) {
    return findSeqString(cOpis);
  }
  public String findSeq(String cOpis, boolean save) {
    return findSeqString(cOpis, save);
  }

  public String findSeq(String cOpis, boolean save, boolean lock) {
    return findSeqString(cOpis, save, lock);
  }

  public Integer findSeqInteger(String cOpis, boolean save) {
    Double seqDouble = findSeqDouble(cOpis, save);
    return new Integer(seqDouble.intValue());
  }

  public Integer findSeqInteger(String cOpis, boolean save, boolean lock) {
    Double seqDouble = findSeqDouble(cOpis, save, lock);
    return new Integer(seqDouble.intValue());
  }

  public Integer findSeqInteger(String cOpis) {
    return findSeqInteger(cOpis,true);
  }
  
  public Integer findSeqInteger(DataSet ds) {
	return findSeqInteger(hr.restart.robno.Util.getUtil().getSeqString(ds),true);
  }

  public String findSeqString(String cOpis) {
    return findSeqString(cOpis,true);
  }

  public String findSeqString(String cOpis,boolean save) {
    Integer seqInteger = findSeqInteger(cOpis, save);
    return maskZeroInteger(seqInteger,6);
  }

  public String findSeqString(String cOpis,boolean save,boolean lock) {
    Integer seqInteger = findSeqInteger(cOpis, save, lock);
    return maskZeroInteger(seqInteger,6);
  }

  public int findSeqInt(String cOpis) {
    return findSeqInt(cOpis,true);
  }
  public int findSeqInt(String cOpis, boolean save) {
    return findSeqDouble(cOpis,save).intValue();
  }
  public int findSeqInt(String cOpis, boolean save, boolean lock) {
    return findSeqDouble(cOpis,save,lock).intValue();
  }
  /**
   * Vraca u string integer sa vodecim nulama zadane duljine.
   * npr maskZeroInteger(new Integer(17),4) vrati "0017"
   */
  public String maskZeroInteger(Integer cInteger,int len) {
    char[] zeros = new char[len];
    for (int i=0;i<zeros.length;i++) zeros[i]='0';
    String strZeros = new String(zeros);
    return (strZeros+cInteger.toString()).substring((strZeros+cInteger).length()-len,(strZeros+cInteger).length());
  }
  /**
   * <pre>
   * maskString("KUR",'H',7) = "HHHHKUR";
   * maskString("12",'0',7) = "0000012";
   * Dakle:
   *  cString je string koji maskiramo
   *  maskChar je character kojim maskiramo
   *  len je ukupna duljina zeljenog stringa
   * </pre>
   */
  public String maskString(String cString,char maskChar,int len) {
    if (len<cString.length()) return cString.substring(0,len);
    if (len==cString.length()) return cString;
    char[] cmask = new char[len-cString.length()];
    for (int i=0;i<cmask.length;i++) cmask[i]=maskChar;
    return new String(cmask).concat(cString);
  }
  public String maskStringTrailing(String cString,char maskChar,int len) {
    if (len<cString.length()) return cString.substring(0,len);
    if (len==cString.length()) return cString;
    char[] cmask = new char[len-cString.length()];
    for (int i=0;i<cmask.length;i++) cmask[i]=maskChar;
    return cString.concat(new String(cmask));
  }

  /**
   * S
   */
  public java.sql.Timestamp findDate(boolean miliSec, int nDay) {
    Variant variant = new Variant();
    Calendar calendar = new GregorianCalendar();
    if (nDay!=0) {
      calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+nDay);
    }
    if (miliSec==false) {
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
    }
    variant.setTimestamp(new java.sql.Timestamp( calendar.getTime().getTime() ));
    return variant.getTimestamp();
  }
  /**
   *
   */
  public java.sql.Timestamp getToday() {
    return new java.sql.Timestamp(getNowMS());
  }
  
  public Timestamp getPresToday(ReadRow presRow, String datcol) {
    if ("D".equalsIgnoreCase(frmParam.getParam("robno", "datumPresZad", "N",
        "Uzeti zadnji datum sa predselekcije kod novog dokumenta (D,N)", true)))
      return presRow.getTimestamp(datcol+"-to");

    return hr.restart.util.Util.getUtil().getToday(
        presRow.getTimestamp(datcol+"-from"), 
        presRow.getTimestamp(datcol+"-to"));
  }
  
  public Timestamp getPresToday(ReadRow presRow) {
    return getPresToday(presRow, "DATDOK");
  }
  /**
   *
   */
  public long getNowMS() {
    return System.currentTimeMillis();
  }
  public com.borland.dx.dataset.Variant getVarDate() {
    try {
      vv.setTimestamp(getNowMS());
    } catch (Exception e) {
      System.out.print("getVarDate: ");
      System.out.println(e);
    }
    return vv;
  }
  /**
   * @param dan datum iz kojeg treba izvaditi godinu
   * @return godinu od zadanog datuma u stringu
   */
  public String findYear (java.sql.Timestamp dan) {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(dan);
    return Integer.toString(cal.get(cal.YEAR));
  }
  /**
   * @return vraca tekucu godinu
   */
  public String findYear () {
    return findYear(new java.sql.Timestamp(System.currentTimeMillis()));
  }
/**
 * Timestamp datod = jdatOD.getDataSet().getTimestamp(jdatOD.getColumnName());
 * Timestamp datdo = jdatDO.getDataSet().getTimestamp(jdatDO.getColumnName());
 * if (Util.getUtil().isValidRange(datod,datdo)) return true;
 * @param jdatOD
 * @param jdatDO
 * @return
 */
  public boolean isValidRange(JraTextField jdatOD,JraTextField jdatDO) {
    if (jdatOD.getDataSet() == null) return false;
    if (jdatDO.getDataSet() == null) return false;
    Timestamp datod = jdatOD.getDataSet().getTimestamp(jdatOD.getColumnName());
    Timestamp datdo = jdatDO.getDataSet().getTimestamp(jdatDO.getColumnName());
    if (Util.getUtil().isValidRange(datod,datdo)) return true;
    javax.swing.JOptionPane.showMessageDialog(jdatOD,"Datumski period nije ispravan","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
    return false;
  }
  /**
   * Puni zadane text komponente u odnosu na tekuci datum; u prvu stavi prvi dan zadanog mjeseca, a u drugu danasnji dan
   * @param datod text field za prvi dan
   * @param datdo text field za zadnji dan
   */
  public void getCommonRange(JraTextField datod,JraTextField datdo) {
    getCommonRange(datod,datdo,null,false);
  }
  /**
   * Puni zadane text komponente u odnosu na tekuci datum; u prvu stavi prvi dan zadanog mjeseca,
   * a u drugu danasnji dan ako je lastday = false, inace zadnji dan u zadanom mjesecu
   * @param datod text field za prvi dan
   * @param datdo text field za zadnji dan
   * @param lastday ako je true daje zadnji dan u tom mjesecu, ako je false daje today
   */
  public void getCommonRange(JraTextField datod,JraTextField datdo,boolean lastday) {
    getCommonRange(datod,datdo,null,lastday);
  }

  /**
   * Puni zadane text komponente u odnosu na zadani datum; u prvu stavi prvi dan zadanog mjeseca,
   * a u drugu danasnji dan
   * @param datod text field za prvi dan
   * @param datdo text field za zadnji dan
   * @param today zadani dan
   */
  public void getCommonRange(JraTextField datod,JraTextField datdo, Timestamp today) {
    getCommonRange(datod,datdo,today,false);
  }
  /**
   * Puni zadane text komponente u odnosu na zadani datum; u prvu stavi prvi dan zadanog mjeseca,
   * a u drugu danasnji dan ako je lastday = false, inace zadnji dan u zadanom mjesecu
   * @param datod text field za prvi dan
   * @param datdo text field za zadnji dan
   * @param today zadani dan
   * @param lastday ako je true daje zadnji dan u tom mjesecu, ako je false daje today
   */
  public void getCommonRange(JraTextField datod,JraTextField datdo, Timestamp today, boolean lastday) {
    if (!chkIsDate(datod)) return;
    if (!chkIsDate(datdo)) return;
    if (today == null) today = new Timestamp(System.currentTimeMillis());
    Util ut = Util.getUtil();
    setTimestamp(datod,ut.getFirstDayOfMonth(today));
    if (lastday) setTimestamp(datdo,ut.getLastDayOfMonth(today));
    else setTimestamp(datdo,today);
  }
  private void setTimestamp(JraTextField td,Timestamp d) {
    td.getDataSet().setTimestamp(td.getColumnName(),d);
  }
  private boolean chkIsDate(JraTextField d) {
    return d.getDataSet().getColumn(d.getColumnName()).getDataType() == com.borland.dx.dataset.Variant.TIMESTAMP;
//    return true;
  }
  /**
   * Kopira vrijednost iz jedne text komponente u drugu preko dataseta
   * @param copyFrom - text field iz kojeg se kopira
   * @param copyTo - text field u koji se kopira
   */
  public void copyValue(JraTextField copyFrom, JraTextField copyTo) {
    try {
      hr.restart.db.raVariant.setDataSetValue(
        copyTo.getDataSet(),
        copyTo.getColumnName(),
        hr.restart.db.raVariant.getDataSetValue(copyFrom.getDataSet(),copyFrom.getColumnName())
      );
    }
    catch (Exception ex) {
      System.out.println("Valid.copyValue err: "+ex);
    }
  }
  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, true), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   */
  public void recountDataSet(raMatPodaci rMP, String columnName, int deletedRB) {
    recountDataSet(rMP,columnName,deletedRB,true);
  }
  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, true), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   */
  public void recountDataSet(raMatPodaci rMP, String columnName, short deletedRB) {
    recountDataSet(rMP,columnName,deletedRB,true);
  }
  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, saveChanges), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(raMatPodaci rMP, String columnName, int deletedRB, boolean saveChanges) {
    recountDataSet(rMP.getJpTableView(),columnName,deletedRB,saveChanges);
  }
  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, saveChanges), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(raMatPodaci rMP, String columnName, short deletedRB, boolean saveChanges) {
    recountDataSet(rMP.getJpTableView(),columnName,deletedRB,saveChanges);
  }

  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, saveChanges), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(raJPTableView jptv, String columnName, int deletedRB, boolean saveChanges) {
    jptv.enableEvents(false);
    recountDataSet(jptv.getMpTable().getDataSet(),columnName,deletedRB,saveChanges);
    jptv.enableEvents(true);
  }

  /**
   * Zove recountDataSet(raMatPodaci.getRaQueryDataSet(), columnName, deletedRB, saveChanges), ali prije toga
   * iskljucuje UI evente i nakon toga ih ukjucuje
   * @param rMP raMatPodaci koji imaju setiran dataset za recount npr. raMasterDetail.raDetail
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(raJPTableView jptv, String columnName, short deletedRB, boolean saveChanges) {
    jptv.enableEvents(false);
    recountDataSet(jptv.getMpTable().getDataSet(),columnName,deletedRB,saveChanges);
    jptv.enableEvents(true);
  }

  /**
   * Recounta redni broj u datasetu na nacin da sve redne brojeve vece od zadanog (obrisanog) smanjuje za 1
   * @param ds dataset za recount
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(DataSet ds, String columnName, int deletedRB, boolean saveChanges) {
    int currRow = ds.getRow();
    com.borland.dx.dataset.SortDescriptor sort = ds.getSort();
    ds.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {columnName}));
    ds.last();
    do {
      int currRB = ds.getInt(columnName);
//      if (currRB == deletedRB) return; // kraj
      if (currRB > deletedRB) ds.setInt(columnName,(currRB-1));
    } while (ds.prior());
    if (saveChanges) ds.saveChanges();
    ds.setSort(sort);
    ds.goToClosestRow(currRow);
  }
  /** @todo napraviti metodu recountDataSet sa parametrom sorted (da li da sortira ili ne) */

  /**
   * Recounta redni broj u datasetu na nacin da sve redne brojeve vece od zadanog (obrisanog) smanjuje za 1
   * - lagani kopipejst 'recountDataSet' samo izbacen sortDescriptor jerbo radi sranja
   *   kad ima vise recorda sa istim brojem, kao rijesenje se nametje to da mu posaljem
   *   vec sortirani dataSet sa ORDER BY - Sinisa
   * @param ds dataset za recount
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountSortedDataSet(DataSet ds, String columnName, int deletedRB, boolean saveChanges) {
    int currRow = ds.getRow();
    ds.last();
    do {
      int currRB = ds.getInt(columnName);
      if (currRB > deletedRB) ds.setInt(columnName,(currRB-1));
    } while (ds.prior());
    if (saveChanges) ds.saveChanges();
    ds.goToClosestRow(currRow);
  }


  /**
   * Recounta redni broj u datasetu na nacin da sve redne brojeve vece od zadanog (obrisanog) smanjuje za 1
   * @param ds dataset za recount
   * @param columnName ime kolone koja je redni broj
   * @param deletedRB redni broj koji je obrisan
   * @param saveChanges da li da na kraju snimi promjene
   */
  public void recountDataSet(DataSet ds, String columnName, short deletedRB, boolean saveChanges) {
    int currRow = ds.getRow();
    com.borland.dx.dataset.SortDescriptor sort = ds.getSort();
    ds.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {columnName}));
    ds.last();
    do {
      short currRB = ds.getShort(columnName);
//      if (currRB == deletedRB) return; // kraj
      if (currRB > deletedRB) ds.setShort(columnName,(short)(currRB-1));
    } while (ds.prior());
    if (saveChanges) ds.saveChanges();
    ds.setSort(sort);
    ds.goToClosestRow(currRow);
  }

  public static String getCollateSQL() {
    return hr.restart.sisfun.frmParam.getParam("sisfun","CollateSeq","COLLATE PXW_SLOV","Oznaka koja se pri orderu string kolone u SQL-u poziva nakon imena kolone");
  }

  /**
   * Vraca formatirani string koji izgleda ovako
   * TABLE NAME        RECORD COUNT
   *-----------------------------------
   * Artikli           2654
   * itd;
   */
  public static String countAllTables() {
    try {
      int allcnt = 0;
      String ret = " TABLE NAME        RECORD COUNT ";
      ret = ret + "\n---------------------------------";
      //hr.restart.db.raConnectionFactory.getKeyColumns(con, "");
      java.sql.ResultSet tabset = hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection().getMetaData().getTables(null, null, null, null);
      while (tabset.next()) {
        String tabname = tabset.getString("TABLE_NAME");
        if (!tabname.startsWith("RDB$")) {
          int cnt = Valid.getValid().getSetCount(Util.getNewQueryDataSet("SELECT count(*) FROM "+tabname),0);
          ret = ret + "\n "+tabname+"    "+cnt;
          allcnt = allcnt + cnt;
        }
      };
      ret = ret + "\n ----------------------------------- \n ALLCOUNT   "+allcnt;
      return ret;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  public static void main(String[] args) {
    hr.restart.baza.dM.getDataModule().showParams();
    hr.restart.baza.dM.getDataModule().reconnectIfNeeded();
    System.out.println(Valid.countAllTables());
  }


  /**
   * Pretvara ArrayListu napunjenu stringovima u Array Stringova
   *
   * @param al ArrayLista koja je napunjena stringovima
   * @return String[] ako je al puna inaèe null
   */

  public static String[] ArrayList2StringArray(ArrayList al){
    if (al==null) return null;
    if (al.size()==0) return null;
    String[] povrat = new String[al.size()];
    for (int i = 0;i<povrat.length;i++){
      povrat[i]= (String) al.get(i);
    }
    return povrat;
  }

  /**
   * Suprotno od ArrayList2StringArray(ArrayList al)
   * @param al Array nekih objekata
   * @return ArrayListu ako je al != null i al.length ==0
   */
  public static ArrayList ObjectArray2ArrayList(Object[] al){
    if (al==null) return null;
    if (al.length ==0) return null;
    ArrayList aal= new ArrayList();
    for (int i = 0;i<al.length;i++){
      aal.add(al[i]);
    }
    return aal;
  }


}
