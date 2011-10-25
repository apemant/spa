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
package hr.restart.util;



/**

 * Title:        Utilitys

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      RA

 * @author AI

 * @version 1.0

 */

import hr.restart.baza.Refresher;
import hr.restart.baza.dM;
import hr.restart.db.raVariant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;



public class Util {

sysoutTEST ST = new sysoutTEST(false);

  static Util myUtil;

  private java.util.Calendar cal = Calendar.getInstance();

  MathEvaluator mathEval = new MathEvaluator();

  Variant BVar1 = new Variant();

  Variant BVar2 = new Variant();

  Variant BVar3 = new Variant();

  Valid vl = Valid.getValid();

  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.util.Res_");

  hr.restart.robno.raDateUtil rDU = hr.restart.robno.raDateUtil.getraDateUtil();

  com.borland.dx.text.VariantFormatStr BVFormatter;

  java.util.LinkedList linkedlist = new java.util.LinkedList();

//  java.util.Vector linkedlist = new java.util.Vector();

//  Pokusaj sinhroniziranog linkedlista ali nakon POVRSNOG preleta po dokumentaciji mislim da je to Vector

//  java.util.List list = java.util.Collections.synchronizedList(new java.util.LinkedList());

//  java.util.LinkedList linkedlist = (java.util.LinkedList)list;

  public Util() {

  }

/**

 * Vraca staticku instancu klase Util

 */

  public static Util getUtil() {

    if (myUtil==null) myUtil=new Util();



    return myUtil;

  }

/**

 * Vraca Variant koji ima vrijednost value i moguce ga je strpati u kolonu col. Pogodno za metodu:

 * someDataSet.setVariant(col.getColumnName,Util.getBVariant("123",col));

 */

  public Variant getBVariant(String value,Column col) {

    BVFormatter = new com.borland.dx.text.VariantFormatStr("",col.getDataType());

    try {

      BVFormatter.parse(value,BVar1);

    } catch (Exception e) {

      System.out.println("Util.getBVariant: "+e+" "+value);

      BVar1=null;

    }

    return BVar1;

  }

/**

 * <pre>

 * Evo koda:

 * rrow1.getVariant(colName1,BVar1);

 * rrow2.getVariant(colName2,BVar2);

 * return BVar1.equals(BVar2);

 * </pre>

 */

  public boolean equalsBVariant(ReadRow rrow1,ReadRow rrow2,String colName1,String colName2) {

    rrow1.getVariant(colName1,BVar1);

    rrow2.getVariant(colName2,BVar2);

    return BVar1.equals(BVar2);

  }

/**

 * Vraca true ako je vrijednost u koloni rrow1.getColumn(colName1) izmedju vrijednosti u

 * rrow2.getColumn(colNameFrom) i rrow2.getColumn(colNameTo), dok inclusive znaci da li

 * ako je colName1 = colNameFrom ili colName1 = colNameTo da vrati true ili false (ukljucene granice)

 */

  public boolean betweenBVariants(ReadRow rrow1,ReadRow rrow2,String colName1,String colNameFrom,String colNameTo,boolean inclusive) {

    rrow1.getVariant(colName1,BVar1);

    rrow2.getVariant(colNameFrom,BVar2);

    rrow2.getVariant(colNameTo,BVar3);

    if (BVar1.isNull()) return true;

    boolean bfrom = false;

    boolean bto = false;

    if ((BVar1.getType() == Variant.TIMESTAMP)

        &&(BVar2.getType() == Variant.TIMESTAMP)

        &&(BVar3.getType() == Variant.TIMESTAMP)) {//Poseban slucaj pa se koristi hr.restart.robno.raDateUtil

      if (inclusive) {

//        bfrom = (BVar1.compareTo(BVar2)>=0);

        bfrom = (rDU.CompareDate(BVar1.getTimestamp(),BVar2.getTimestamp())>=0);

//        bto = (BVar1.compareTo(BVar3)<=0);

        bto = (rDU.CompareDate(BVar1.getTimestamp(),BVar3.getTimestamp())<=0);

      } else {

//        bfrom = (BVar1.compareTo(BVar2)>0);

        bfrom = (rDU.CompareDate(BVar1.getTimestamp(),BVar2.getTimestamp())>0);

//        bto = (BVar1.compareTo(BVar3)<0);

        bto = (rDU.CompareDate(BVar1.getTimestamp(),BVar3.getTimestamp())<0);

      }

    } else {

      if (inclusive) {

        bfrom = (BVar1.compareTo(BVar2)>=0);

        bto = (BVar1.compareTo(BVar3)<=0);

      } else {

        bfrom = (BVar1.compareTo(BVar2)>0);

        bto = (BVar1.compareTo(BVar3)<0);

      }

    }

    if (BVar2.isNull()) bfrom = true; // ako granica nije upisana, granica ne postoji

    if (BVar3.isNull()) bto = true;

    return (bfrom&&bto);

  }

/**

 * <pre>

 * Usporedjuje vrijednosti dviju kolona u dva dataseta

 * Vraca jedan od integera definiranih u hr.restart.util.Util

 * a to su EQUAL=0, GREATER=1, SMALLER=-1, BEGINS=2, CONTAINS=3, ERROR=-99

 * A) Za Stringove - sve komparacije su ignore case

 * 1. "TEST","TEST" ; "TEST","tEsT" ; "TesT","teSt" = EQUAL

 * 2. "TEsT","TE" = BEGINS

 * 3. "TeST","Es" = CONTAINS

 * 4. "ABC","bce" = GREATER

 * 5. "abc","AAB" = SMALLER

 * B) Za ostale vraca samo GREATER, EQUAL i SMALLER

 *    tako da se moze testirati sa

 *     if (Util.compareBVariants(rrow1,rrow2,colName1,colName2) > ili < ili >= ili <= 0;

 *    Koriste se: com.borland.dx.dataset.Variant.compareTo(Variant) i

 *                hr.restart.robno.raDateUtil.CompareDate(Timestamp,Timestamp)

 * ERROR=-99 vraca ako iti jedan od uvjeta nije zadovoljen odnosno pojavila se greska

 * </pre>

 */

  public static final int GREATER=1;

  public static final int EQUAL=0;

  public static final int SMALLER=-1;

  public static final int BEGINS=2;

  public static final int CONTAINS=3;

  public static final int ERROR=-99;



  public int compareBVariants(ReadRow rrow1,ReadRow rrow2,String colName1,String colName2) {

    int retValue=ERROR;

    rrow1.getVariant(colName1,BVar1);

    rrow2.getVariant(colName2,BVar2);

    if (BVar1.equals(BVar2)) {

      retValue=EQUAL;

    } else {

      ///

       // Stringovi

      ///

      if (BVar1.getType()==Variant.STRING) {

        String string1 = BVar1.toString().toUpperCase().toLowerCase();

        String string2 = BVar2.toString().toUpperCase().toLowerCase();

        if (string1.startsWith(string2)) { // string1="TEST";string2="TE"

          retValue=BEGINS;

        } else if (string1.indexOf(string2)!=-1) {//string1="TEST";string2="ES" ili "ST"

          retValue=CONTAINS;

        } else {

          retValue = string1.compareTo(string2);//Vraca -1==SMALLER ili 1==GREATER ili 0=EQUALS<-mozda je prosao BVar1.equals(BVar2)

        }

      } else if (BVar1.getType()==Variant.TIMESTAMP) {

      ///

       // TimeStampovi

      ///

        retValue = rDU.CompareDate(BVar1.getTimestamp(),BVar2.getTimestamp());

      } else {

      ///

       // Ostalo

      ///

        int mr = BVar1.compareTo(BVar2);

        if (mr>0) {

          retValue=GREATER;

        } else if (mr<0) {

          retValue=SMALLER;

        } else if (mr==0) {

          retValue=EQUAL; //gotovo nemoguce

        }

      }

    }

    return retValue;

  }

/**

 * Vraca u linkedlist sve komponente zadanog containera koje implementiraju ColumnAware interface,

 * odnosno imaju setDataSet i setColumnName metode

 */

  public java.util.LinkedList getDBComps(java.awt.Container cont) {

    return getDBComps(cont,ColumnAware.class);

  }

  public java.util.LinkedList getDBComps(java.awt.Container cont, Class _class) {

//  public java.util.Vector getDBComps(java.awt.Container cont) {

    linkedlist = new java.util.LinkedList();

//    for (int i=0;i<linkedlist.size();i++) linkedlist.remove(i);

    getBindedComponents(cont,_class);

    return linkedlist;

  }

  private void getBindedComponents(java.awt.Container ContainerName,Class _class) {

    java.awt.Component sve_comp;

    int i=ContainerName.getComponentCount();

    int j=0;

    if (i != 0) {

      while (j<i) {

      sve_comp = ContainerName.getComponent(j);

//       if (!(sve_comp instanceof javax.swing.JTabbedPane))

      try {//da li je swing

        if (((javax.swing.JComponent)sve_comp).getComponentCount()>0) {//da li ima komponenti

//ST.prn(sve_comp.getClass().getName()+" ima podkomponente");

          getBindedComponents((java.awt.Container) sve_comp,_class); // zovi da vidim dalje

 //         return;

        }

      } catch (Exception e) {

//ST.prn(sve_comp.getClass().getName()+" Nije JComponent");

      }

      try {//da li je dbswing

        if (_class.isInstance(sve_comp)) {

//ST.prn("DODAJEM "+sve_comp.getClass().getName());

          linkedlist.add(sve_comp);

        }

      } catch (Exception e) {

//ST.prn(sve_comp.getClass().getName()+" Nije dbComponent");

      }



      j++;

      }

    }

  }

  /**

   * iz dva niza objekta daje treci

   */

  public Object[] concatArray(Object[] arr1, Object[] arr2) {

    int len1 = arr1.length;

    Object[] retArr = new Object[arr1.length+arr2.length];

    for (int i=0;i<arr1.length;i++) retArr[i] = arr1[i];

    for (int i=0;i<arr2.length;i++) retArr[i+len1] = arr2[i];

    return retArr;

  }

  /**

   * Iz dva niza komponenti daje treci

   */

  public java.awt.Component[] concatArrComp(java.awt.Component[] arr1, java.awt.Component[] arr2) {

    int len1 = arr1.length;

    java.awt.Component[] retArr = new java.awt.Component[arr1.length+arr2.length];

    for (int i=0;i<arr1.length;i++) retArr[i] = arr1[i];

    for (int i=0;i<arr2.length;i++) retArr[i+len1] = arr2[i];

    return retArr;

  }

  /**

   * String1 = "10"; String2[]={"1","2","3"}; concatArray(String1,String2) = {"10","1","2","3"}

   */

  public Object[] concatArray(Object obj, Object[] arr) {

    Object[] retArr;

    if (arr==null) {

      retArr = new Object[] {obj};

      return retArr;

    } else retArr = new Object[arr.length+1];

    retArr[0] = obj;

    for (int i=0;i<arr.length;i++) retArr[i+1] = arr[i];

    return retArr;

  }

  /**

   * String1 = "10"; String2[]={"1","2","3"}; concatArray(String2,String1) = {"1","2","3","10"}

   */

  public Object[] concatArray(Object[] arr,Object obj) {

    Object[] retArr;

    if (arr==null) {

      retArr = new Object[] {obj};

      return retArr;

    } else retArr = new Object[arr.length+1];

    for (int i=0;i<arr.length;i++) retArr[i] = arr[i];

    retArr[arr.length] = obj;

    return retArr;

  }

  /**

   * String1 = "10"; String2[]={"1","2","3"}; concatArray(String2,String1) = {"1","2","3","10"}

   */

  public String[] concatArrayStr(String[] arr,String obj) {

    String[] retArr;

    if (arr==null) {

      retArr = new String[] {obj};

      return retArr;

    } else retArr = new String[arr.length+1];

    for (int i=0;i<arr.length;i++) retArr[i] = arr[i];

    retArr[arr.length] = obj;

    return retArr;

  }



  /**

   * vraca klonirane kolone dataseta

   * npr. za kopirati strukturu dataseta1 u dataset2 :

   * dataset2.setColumns(cloneCols(dataset1.getColumns()));

   */

  public Column[] cloneCols(Column[] cols) {

    Column[] clonedCols = new Column[cols.length];

    for (int i=0;i<cols.length;i++) {

      clonedCols[i] = (Column)cols[i].clone();

    }

    return clonedCols;

  }

  /**

   * Usporedjuje dva niza objekata, ako imaju iste membere na istim mjestima vraca true

   */

  public boolean equalsArr(Object[] arr1,Object[] arr2) {

    if ((arr1==null)&&(arr2==null)) return true;

    if (arr1==null) return false;

    if (arr2==null) return false;

    if (arr1.length != arr2.length) return false;

    for (int i=0;i<arr1.length;i++) {

      if (!(arr1[i].equals(arr2[i]))) return false;

    }

    return true;

  }

  /**

   * Usporedjuje dva niza int, ako imaju iste membere na istim mjestima vraca true

   */

  public boolean equalsArr(int[] arr1,int[] arr2) {

    if ((arr1==null)&&(arr2==null)) return true;

    if (arr1==null) return false;

    if (arr2==null) return false;

    if (arr1.length != arr2.length) return false;

    for (int i=0;i<arr1.length;i++) {

      if (arr1[i]!=arr2[i]) return false;

    }

    return true;

  }

  public String numToLet(double number) {

    return numToLet(number,null);

  }

/**

 * Prebacuje iz brojki u slova max=999999999.99, za negativne vrijednosti radi abs()

 */

  public String numToLet(double number, String oznVal) {

    String letters="";

    number = java.lang.Math.abs(number);

    if (number > 999999999.99) return "";

    String snumber = String.valueOf(number);

    if (snumber.indexOf("E") == -1) {

      int dotin = snumber.indexOf(".");

      if (dotin > -1) {

        String sdigits = snumber.substring(dotin+1);

        if (sdigits.length()>2) {

          sdigits=sdigits.substring(0,2);

        } else if (sdigits.length()==1){

          sdigits=sdigits.concat("0");

        }

        snumber = snumber.substring(0,dotin).concat(sdigits);

      }

    } else {

      int numE = Integer.parseInt(snumber.substring(snumber.indexOf("E")+1));

      snumber = snumber.substring(0,1).concat(snumber.substring(2));

      if (snumber.length()>=numE+3) {

        snumber = snumber.substring(0,numE+3);

      } else {

        snumber = vl.maskStringTrailing(snumber.substring(0,snumber.indexOf("E")),'0',numE+3);

      }

    }

    String inTxtVal = vl.maskString(snumber,'0',11);

    String sVal;

    String sVal100;

    if (oznVal == null) {

      sVal = " "+hr.restart.zapod.Tecajevi.getDomOZNVAL()+" ";

      sVal100 = " "+hr.restart.zapod.Tecajevi.getDomNIZJED()+" ";

    } else {

      com.borland.dx.sql.dataset.QueryDataSet qdsValute = hr.restart.baza.dM.getDataModule().getValute();

      lookupData.getlookupData().raLocate(qdsValute,new String[] {"OZNVAL"},new String[] {oznVal});

      sVal = " "+oznVal+" ";

      sVal100 = " "+qdsValute.getString("NIZJED")+" ";

    }

    if (Integer.parseInt(inTxtVal.substring(0,9)) == 0) {

      sVal = "";

    }

    if (Integer.parseInt(inTxtVal.substring(9,11)) == 0 ) {

      sVal100 = "";

    }

/*

System.out.println(inTxtVal);

System.out.println("milioni "+inTxtVal.substring(0,3));

System.out.println("tisucice "+inTxtVal.substring(3,6));

System.out.println("stotice "+inTxtVal.substring(6,7));

System.out.println("desetice "+inTxtVal.substring(7,9));

System.out.println("desetice "+inTxtVal.substring(9,11));

*/

    letters = milioni(inTxtVal.substring(0,3))

              .concat(tisucice(inTxtVal.substring(3,6)))

              .concat(stotice(inTxtVal.substring(6,7)))

              .concat(desetice(inTxtVal.substring(7,9))).concat(sVal)

              .concat(desetice(inTxtVal.substring(9,11))).concat(sVal100);

    return letters;

  }

  String jedinice(String val) {

    try {

      return res.getString(val.substring(0,1));

    } catch (Exception e) {

      return "";

    }

  }

  String desetice(String val) {

    String retValue="";

    int intVal = Integer.parseInt(val);

    if (intVal > 9) {

      int qVal = intVal>20 ? intVal-Integer.parseInt(val.substring(1,2)) : intVal;

      retValue = res.getString(Integer.toString(qVal));

      if (intVal>20) retValue=retValue.concat(jedinice(val.substring(1,2)));

    } else if (intVal > 0) {

      retValue=jedinice(val.substring(1,2));

    }

    return retValue;

  }

  String stotice(String val) {

    int intVal = Integer.parseInt(val);

    if ((intVal>0)&&(intVal<10)) {

      return res.getString(val.concat("00"));

    } else {

      return "";

    }

  }

  String tisulioni(String val,String resVal) {

    String retVal = "";

    String val01 = val.substring(1,3);

    if (val01.equals("00")) {

      retVal = stotice(val.substring(0,1)).concat(res.getString(resVal));

      if (val.substring(0,2).equals("00")) retVal="";

    } else {

      int intVal=Integer.parseInt(val01);

      if ((intVal>0)&&(intVal<10)) {

        retVal = stotice(val.substring(0,1)).concat(res.getString(val01.substring(1,2).concat(resVal.substring(1))));

      } else if ((intVal>=10)&&(intVal<20)) {

        retVal = stotice(val.substring(0,1)).concat(desetice(val01)).concat(res.getString(resVal));

      } else if (intVal>=20) {

        retVal = stotice(val.substring(0,1))

                  .concat(desetice(val01.substring(0,1).concat("0")))

                  .concat(tisulioni(vl.maskZeroInteger(new Integer(val01.substring(1,2)),3),resVal));

        if (val01.substring(1,2).equals("0")) retVal = retVal.concat(res.getString(resVal));

      } else {

        retVal = stotice(val.substring(0,1));

      }

    }

    return retVal;

  }

  String tisucice(String val) {

    return tisulioni(val,"0E2");

  }



  String milioni(String val) {

    return tisulioni(val,"0E3");

  }



  String milijarde(String val) {

    return tisulioni(val,"0E4");

  }

/**

 * Vraca string zadane duljine len, pozicioniran zadanim int = javax.swing.SwingConstants.RIGHT,CENTER ili LEFT

 */

  public String alignString(String string,int len, int alignment) {

    if (len < 0) return string;

    if (string.length()>=len) {

      string = string.substring(0,len);

    } else {

      char[] blanks = new char[len-string.length()];

      for (int i=0;i<blanks.length;i++) blanks[i] = ' ';

      String blankStr = new String(blanks);

      if (alignment==javax.swing.SwingConstants.RIGHT) {

        string = blankStr.concat(string);

      } else if (alignment==javax.swing.SwingConstants.CENTER) {

        String bs1 = blankStr.substring(blankStr.length()/2);

        String bs2 = blankStr.substring(blankStr.length() - bs1.length()+1);

        string = bs1.concat(string).concat(bs2);

      } else if (alignment==javax.swing.SwingConstants.LEFT) {

        string = string.trim().concat(blankStr);

      } else {

        string = string.concat(blankStr);

      }

    }

    return string;

  }

  /**

   * Vraca u string vrijednost expressiona (vidi hr.restart.util.MathEvaluator

   */

  public String mathEvalString(String fja) {

    mathEval.setExpression(fja);

    return mathEval.getValue().toString();

  }

  /**

   * Vraca u double vrijednost expressiona (vidi hr.restart.util.MathEvaluator

   */

  public double mathEvalDouble(String fja) {

    mathEval.setExpression(fja);

    return mathEval.getValue().doubleValue();

  }

/**

 * <pre>

 * Vraca index prvog charactera u zadanom stringu koji nije broj. Ako nema nodigita vraca -1.

 * Character '.' (decimalna tocka) tretira se kao digit za razliku od Character.isDigit(char).

 * npr: indexOfFirstNoDigit("123AAA") = "123AAA".indexOf("A") = 3;

 *      indexOfFirstNoDigit("123") = -1

 *      indexOfFirstNoDigit("123.45+7") = "123.45+7".indexOf("+") = 6

 * </pre>

 */

  public int indexOfFirstNoDigit(String str) {

    char[] chr = str.toCharArray();

    for (int i=0;i<chr.length;i++) {

      if (!Character.isDigit(chr[i])) {

        if (chr[i]!='.') return i;

      }

    }

    return -1;

  }

  /**

   * nakon poziva ove metode sve System.out metode idu u file getLogFileName()

   */

  public static void redirectSystemOut() {

    FileHandler FH = new FileHandler(getLogFileName());

    FH.openWrite();
/*
    FH.addFileChangeListener(new java.beans.PropertyChangeListener() {

      public void propertyChange(java.beans.PropertyChangeEvent evt) {

        for (Iterator i = writeListeners.iterator(); i.hasNext(); ) {
          raLogWriteListener lis = (raLogWriteListener)i.next();

          lis.logWritten("");
          //evt.getNewValue().toString()

        }

      }

    });
*/
    System.setOut(new java.io.PrintStream(FH.fileOutputStream));

    System.setErr(new java.io.PrintStream(FH.fileOutputStream));

    System.out.println();

    System.out.println(

      " **** Loging started at "+

      new java.sql.Date(System.currentTimeMillis()) + "  "+

      new java.sql.Time(System.currentTimeMillis()).toString()+

      " **** "

      );

    System.out.println();

  }



  private static HashSet writeListeners = new HashSet();


/**
 * deprecated treba puno zganaca da ovo radi kak spada
 *//*
  public static void addLogWriteListener(raLogWriteListener lis) {

    writeListeners.add(lis);

  }
*/

/**
 * deprecated treba puno zganaca da ovo radi kak spada
 *//*
  public static void removeLogWriteListener(raLogWriteListener lis) {

    writeListeners.remove(lis);

  }
*/

  private static String logFileName = null;
  public static String getLogFileName() {
    if (logFileName == null) logFileName = makeLogFileName();
    return logFileName;
  }
  /**

   *

   */

  private static String makeLogFileName() {
    new File("log").mkdir();
    String fn = "log"+File.separatorChar+"ra"+new java.sql.Date(hr.restart.start.STARTTIME)+".log";
    for (int suff = 0;suff<99; suff++) {
//System.out.println("trying "+fn);      
      File f = new File(fn);
      if (!f.exists()) {
//        System.out.println("redirecting to "+fn);
        return fn;
      }
      fn = "log"+File.separatorChar+"ra"+new java.sql.Date(hr.restart.start.STARTTIME)+"-"+suff+".log";
    }
//    System.out.println("redirecting to "+fn);
    return fn;
  }



  public static boolean containsArr(Object[] arr,Object tocont) {

    for (int i=0;i<arr.length;i++) {

      if (arr[i].equals(tocont)) return true;

    }

    return false;

  }



  public int indexOfArray(Object[] arr, Object obj) {

    for (int i=0;i<arr.length;i++) {

      if (arr[i].equals(obj)) return i;

    }

    return -1;

  }

  public Object[] sortArray(Object[] obj) {

/*

    TreeSet s = new TreeSet();

    for (int i=0;i<obj.length;i++) s.add(obj[i]);

    return s.toArray();

*/

//native method

    java.util.Arrays.sort(obj);

    return obj;

  }

  public String[] rmFromString(String[] strArr, String str) {

    if (strArr == null) return new String[0];

    if (strArr.length == 0) return new String[0];

    int newlen = strArr.length - 1;

    if (newlen == 0) {

      if (strArr[0].equals(str)) {

        return new String[0];

      } else {

        return strArr;

      }

    }

    String[] nwString = new String[newlen];

    int newidx = 0;

    for (int i=0;i<strArr.length;i++) {

      if (strArr[i].equals(str)) {

      } else {

        nwString[newidx] = strArr[i];

        newidx++;

      }

    }

    return nwString;

  }



/**

 * @param timstamp zadani Timestamp

 * @return prvi dan u mjesecu od zadanog Timestampa

 */

  public Timestamp getFirstDayOfMonth(Timestamp timstamp) {

    cal.setTime(timstamp);

    cal.set(cal.DATE,1);

    clearCal(cal);

    return new Timestamp(cal.getTime().getTime());

  }

/**

 * @param timstamp zadani Timestamp

 * @return zadnji dan u mjesecu od zadanog Timestampa

 */

  public Timestamp getLastDayOfMonth(Timestamp timstamp) {

    cal.setTime(timstamp);

    cal.set(cal.DATE,cal.getActualMaximum(cal.DATE));

    clearCal(cal);

    return new Timestamp(cal.getTime().getTime());

  }

/**

 * @param timstamp zadani Timestamp

 * @return prvi dan u godini od zadanog Timestampa

 */

  public Timestamp getFirstDayOfYear(Timestamp timstamp) {

    cal.setTime(timstamp);

    cal.set(cal.get(cal.YEAR),cal.JANUARY,1);

    clearCal(cal);

    return new Timestamp(cal.getTime().getTime());

  }

/**

 * @param timstamp zadani Timestamp

 * @return zadnji dan u godini od zadanog Timestampa

 */

  public Timestamp getLastDayOfYear(Timestamp timstamp) {

    cal.setTime(timstamp);

    cal.set(cal.get(cal.YEAR),cal.DECEMBER,31);

    clearCal(cal);

    return new Timestamp(cal.getTime().getTime());

  }

/**

 * @return prvi dan u mjesecu od System.currentTimeMillis()

 */

  public Timestamp getFirstDayOfMonth() {

    return getFirstDayOfMonth(new Timestamp(System.currentTimeMillis()));

  }

/**

 * @return zadnji dan u mjesecu od System.currentTimeMillis()

 */

  public Timestamp getLastDayOfMonth() {

    return getLastDayOfMonth(new Timestamp(System.currentTimeMillis()));

  }

/**

 * @return prvi dan u godini od System.currentTimeMillis()

 */

  public Timestamp getFirstDayOfYear() {

    return getFirstDayOfYear(new Timestamp(System.currentTimeMillis()));

  }

/**

 * @return zadnji dan u godini od System.currentTimeMillis()

 */

  public Timestamp getLastDayOfYear() {

    return getLastDayOfYear(new Timestamp(System.currentTimeMillis()));

  }

  /**

   * @param timstamp Timestamp za maknut vrijeme iz njega

   * @return vraca Timestamp u kojem su sati, minute, sekunde i ostalo nula, a datum ostaje isti

   */

  public Timestamp clearTime(Timestamp timstamp) {

      cal.setTime(timstamp);

      clearCal(cal);

      Timestamp retT = new Timestamp(cal.getTime().getTime());

      return retT;

  }



  private void clearCal(Calendar calendar) {

    calendar.clear(cal.HOUR_OF_DAY);

    calendar.clear(cal.HOUR);

    calendar.clear(cal.MINUTE);

    calendar.clear(cal.SECOND);

    calendar.clear(cal.MILLISECOND);

  }

  /**

   * Usporedjuje prethodno ociscene (clearTime) timestampove

   * @param datOD Timestamp od

   * @param datDO Timestamp do

   * @return true ako je DATUM od manji od datuma do ili ako su jednaki, otherwise false

   */

  public boolean isValidRange(Timestamp datOD,Timestamp datDO) {

    Timestamp datOD_c = clearTime(datOD);

    int y1 = cal.get(cal.YEAR);

    Timestamp datDO_c = clearTime(datDO);

    int y2 = cal.get(cal.YEAR);

    if (y1 != y2) return false;

    return (datOD_c.before(datDO_c)||datOD_c.equals(datDO_c));

  }



  /**

   * Vraca string reprezentaciju mjeseca od zadanog timestampa dakle za 25-04-2007 vraca "04"

   * @param timstamp zadani timestamp

   * @return

   */

  public String getMonth(Timestamp timstamp) {

    cal.setTime(timstamp);

    return hr.restart.util.Valid.getValid().maskZeroInteger(new Integer(cal.get(cal.MONTH)+1),2);

  }
  
  public String getDay(Timestamp timstamp) {

    cal.setTime(timstamp);

    return hr.restart.util.Valid.getValid().maskZeroInteger(new Integer(cal.get(cal.DAY_OF_MONTH)),2);

  }



  public String getYear(Timestamp timstamp) {

    cal.setTime(timstamp);

    return String.valueOf(cal.get(cal.YEAR));

  }



  public static String extractIPAddr(String dburl) {
    return hr.restart.ftpVersionWorker.extractIPAddr(dburl);
  }



  /**

   * vraca novi QueryDataSet sa zadanim queryjem i otvara ga

   * @param qS zadani query

   * @return novi QueryDataSet sa zadanim queryjem bez njegovih (nasih) kolona.

   * ako zelis i kolone koristi hr.restart.baza.<tabela>.getDataModule().createFilteredDataSet(QueryDataSet,String);

   * gdje je QueryDataSet novi QueryDataSet koji zelis modificirati a String je where statement npr. "CORG = '2' and CSKL = '1'"

   */

  public static com.borland.dx.sql.dataset.QueryDataSet getNewQueryDataSet(String qS) {

    return getNewQueryDataSet(qS,true);

  }



  /**

   * vraca novi QueryDataSet sa zadanim queryjem i otvara ga po zelji

   * @param qS query string

   * @param toOpen da li ga otvoriti ili ne

   * @return novi QueryDataSet sa zadanim queryjem bez njegovih (nasih) kolona.

   * ako zelis i kolone koristi hr.restart.baza.<tabela>.getDataModule().createFilteredDataSet(QueryDataSet,String);

   * gdje je QueryDataSet novi QueryDataSet koji zelis modificirati a String je where statement npr. "CORG = '2' and CSKL = '1'"

   */

  public static com.borland.dx.sql.dataset.QueryDataSet getNewQueryDataSet(String qS,boolean toOpen) {

    com.borland.dx.sql.dataset.QueryDataSet retSet = new com.borland.dx.sql.dataset.QueryDataSet();

    retSet.setLocale(Aus.hr);
    
    retSet.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(hr.restart.baza.dM.getDataModule().getDatabase1(),qS));

    retSet.setResolver(hr.restart.baza.dM.getDataModule().getQresolver());
    
    Refresher.postpone();

    if (toOpen) retSet.open();

    return retSet;

  }
  
  public static ResultSet openQuickSet(String query) {
    try {
      Statement st = dM.getDatabaseConnection().createStatement();
      return st.executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Invalid query: " + query);
    }
  }
  
  public static void closeQuickSet(ResultSet rs) {
    try {
      rs.getStatement().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static ResultSet openAsyncSet(String query) {
    try {
      Statement st = dM.getTempConnection().createStatement();
      return st.executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Invalid query: " + query);
    }
  }
  
  public static void closeAsyncSet(ResultSet rs) {
    try {
      rs.getStatement().close();
      rs.getStatement().getConnection().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void fillReadonlyData(StorageDataSet ds, String query) {
    ds.open();
    ResultSet rs = openQuickSet(query);
    try {
      ResultSetMetaData md = rs.getMetaData();
      LoadingConversionRules lcr = new LoadingConversionRules(ds, md);
      while (rs.next())
        lcr.fillRow(ds, rs);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error executing query: " + query);
    } finally {
      closeQuickSet(rs);
    }
  }
  
  public static void fillAsyncData(StorageDataSet ds, String query) {
    ds.open();
    ResultSet rs = openAsyncSet(query);
    try {
      ResultSetMetaData md = rs.getMetaData();
      LoadingConversionRules lcr = new LoadingConversionRules(ds, md);
      while (rs.next())
        lcr.fillRow(ds, rs);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error executing query: " + query);
    } finally {
      closeAsyncSet(rs);
    }
  }
  
  public static class LoadingConversionRules {
    public static final int IGNORE = 0;
    public static final int STRING = 1;
    public static final int INT = 2;
    public static final int BIGDECIMAL = 3;
    public static final int TIMESTAMP = 4;
    public static final int SHORT = 5;
    public static final int LONG = 6;
    public static final int DOUBLE = 7;
    
    public static final int INT_FROM_LONG = 21;
    public static final int INT_FROM_SHORT = 22;
    
    public static final int SHORT_FROM_LONG = 51;
    public static final int SHORT_FROM_INT = 52;
    
    public static final int LONG_FROM_INT = 61;
    public static final int LONG_FROM_SHORT = 62;
    
    public static final int TIMESTAMP_FROM_DATE = 41;
    
    public static final int DATE_FROM_TIMESTAMP = 91;
    
    public static final int BIGDECIMAL_FROM_INT = 31;
    public static final int BIGDECIMAL_FROM_LONG = 32;
    public static final int BIGDECIMAL_FROM_SHORT = 33;
    public static final int BIGDECIMAL_FROM_DOUBLE = 34;
    public static final int BIGDECIMAL_FROM_FLOAT = 35;
    
    public static final int DOUBLE_FROM_INT = 71;
    public static final int DOUBLE_FROM_LONG = 72;
    public static final int DOUBLE_FROM_SHORT = 73;
    public static final int DOUBLE_FROM_FLOAT = 74;

    private static int[][] ctable = new int[][] {
        {Variant.STRING, Types.CHAR, STRING},
        {Variant.STRING, Types.VARCHAR, STRING},
        
        {Variant.INT, Types.INTEGER, INT},
        {Variant.INT, Types.BIGINT, INT_FROM_LONG},
        {Variant.INT, Types.SMALLINT, INT_FROM_SHORT},
        
        {Variant.BIGDECIMAL, Types.INTEGER, BIGDECIMAL_FROM_INT},
        {Variant.BIGDECIMAL, Types.BIGINT, BIGDECIMAL_FROM_LONG },
        {Variant.BIGDECIMAL, Types.SMALLINT, BIGDECIMAL_FROM_SHORT},
        {Variant.BIGDECIMAL, Types.DOUBLE, BIGDECIMAL_FROM_DOUBLE},
        {Variant.BIGDECIMAL, Types.NUMERIC, BIGDECIMAL_FROM_DOUBLE},
        {Variant.BIGDECIMAL, Types.DECIMAL, BIGDECIMAL_FROM_DOUBLE},
        {Variant.BIGDECIMAL, Types.REAL, BIGDECIMAL_FROM_DOUBLE},
        {Variant.BIGDECIMAL, Types.FLOAT, BIGDECIMAL_FROM_FLOAT},
        
        {Variant.TIMESTAMP, Types.TIMESTAMP, TIMESTAMP},
        {Variant.TIMESTAMP, Types.DATE, TIMESTAMP_FROM_DATE},
        
        {Variant.SHORT, Types.SMALLINT, SHORT},
        {Variant.SHORT, Types.INTEGER, SHORT_FROM_INT},
        {Variant.SHORT, Types.BIGINT, SHORT_FROM_LONG},
        
        {Variant.LONG, Types.BIGINT, LONG},
        {Variant.LONG, Types.INTEGER, LONG_FROM_INT},
        {Variant.LONG, Types.SMALLINT, LONG_FROM_SHORT},
        
        {Variant.DOUBLE, Types.DOUBLE, DOUBLE},
        {Variant.DOUBLE, Types.NUMERIC, DOUBLE},
        {Variant.DOUBLE, Types.DECIMAL, DOUBLE},
        {Variant.DOUBLE, Types.REAL, DOUBLE},
        {Variant.DOUBLE, Types.INTEGER, DOUBLE_FROM_INT},
        {Variant.DOUBLE, Types.BIGINT, DOUBLE_FROM_LONG},
        {Variant.DOUBLE, Types.SMALLINT, DOUBLE_FROM_SHORT},
        {Variant.DOUBLE, Types.FLOAT, DOUBLE_FROM_FLOAT}
    };
    
    private int[] rules;
    private int[] ordinals;
    private int[] scale;
    public LoadingConversionRules(StorageDataSet ds, ResultSetMetaData md)
      throws SQLException {
      rules = new int[md.getColumnCount() + 1];
      ordinals = new int[rules.length];
      scale = new int[rules.length];
      for (int i = 1; i <= md.getColumnCount(); i++) {
        String name = md.getColumnLabel(i);
        if (name == null || name.length() == 0)
          name = md.getColumnName(i);
        Column c = ds.hasColumn(name);
        if (c == null) {
          rules[i] = IGNORE;
          System.out.println("Column "+name+" ignored!");
        } else {
          ordinals[i] = c.getOrdinal();
          rules[i] = getConversionRule(c.getDataType(), md.getColumnType(i));
          if (c.getDataType() == Variant.BIGDECIMAL)
            scale[i] = c.getScale();
        }
      }
    }
    
    private int getConversionRule(int dtype, int stype) {
      for (int i = 0; i < ctable.length; i++)
        if (ctable[i][0] == dtype && ctable[i][1] == stype)
          return ctable[i][2];
      throw new RuntimeException("Unable to convert type " + 
                  stype + " to " + Variant.typeName(dtype));
    }
    
    public void fillRow(StorageDataSet ds, ResultSet rs) throws SQLException {
      ds.insertRow(false);
      for (int i = 1; i < rules.length; i++) {
        int ord = ordinals[i];
        switch (rules[i]) {
          case IGNORE:
            break;
          case STRING:
            String s = rs.getString(i);
            if (s != null) ds.setString(ord, s.trim());
            break;
          case INT:
            ds.setInt(ord, rs.getInt(i));
            break;
          case INT_FROM_LONG:
            ds.setInt(ord, (int) rs.getLong(i));
            break;
          case INT_FROM_SHORT:
            ds.setInt(ord, rs.getShort(i));
            break;
          case BIGDECIMAL_FROM_DOUBLE:
            ds.setBigDecimal(ord, new BigDecimal(rs.getDouble(i)).
                setScale(scale[i], BigDecimal.ROUND_HALF_UP));
            break;
          case BIGDECIMAL_FROM_INT:
            ds.setBigDecimal(ord, new BigDecimal(rs.getInt(i)).
                setScale(scale[i], BigDecimal.ROUND_HALF_UP));
            break;
          case BIGDECIMAL_FROM_LONG:
            ds.setBigDecimal(ord, new BigDecimal(rs.getLong(i)).
                setScale(scale[i], BigDecimal.ROUND_HALF_UP));
            break;
          case BIGDECIMAL_FROM_SHORT:
            ds.setBigDecimal(ord, new BigDecimal(rs.getShort(i)).
                setScale(scale[i], BigDecimal.ROUND_HALF_UP));
            break;
          case BIGDECIMAL_FROM_FLOAT:
            ds.setBigDecimal(ord, new BigDecimal(rs.getFloat(i)).
                setScale(scale[i], BigDecimal.ROUND_HALF_UP));
            break;
          case TIMESTAMP:
            Timestamp t = rs.getTimestamp(i);
            if (t != null) ds.setTimestamp(ord, t);
            break;
          case TIMESTAMP_FROM_DATE:
            Date d = rs.getDate(i);
            if (d != null) ds.setTimestamp(ord, new Timestamp(d.getTime()));
            break;
          case SHORT:
            ds.setShort(ord, rs.getShort(i));
            break;
          case SHORT_FROM_INT:
            ds.setShort(ord, (short) rs.getInt(i));
            break;
          case SHORT_FROM_LONG:
            ds.setShort(ord, (short) rs.getLong(i));
            break;
          case LONG:
            ds.setLong(ord, rs.getLong(i));
            break;
          case LONG_FROM_INT:
            ds.setLong(ord, rs.getInt(i));
            break;
          case LONG_FROM_SHORT:
            ds.setLong(ord, rs.getShort(i));
            break;
          case DOUBLE:
            ds.setDouble(ord, rs.getDouble(i));
            break;
          case DOUBLE_FROM_INT:
            ds.setDouble(ord, rs.getInt(i));
            break;
          case DOUBLE_FROM_LONG:
            ds.setDouble(ord, rs.getLong(i));
            break;
          case DOUBLE_FROM_SHORT:
            ds.setDouble(ord, rs.getShort(i));
            break;
          case DOUBLE_FROM_FLOAT:
            ds.setDouble(ord, rs.getFloat(i));
            break;
        }
      }
      ds.post();
    }
  }


  public static hr.restart.db.raSQLSet getNewSQLSet(String qS, boolean toOpen) {

    try {

      hr.restart.db.raSQLSet retSet = new hr.restart.db.raFastSQLSet(qS);

      if (toOpen) retSet.open();

      return retSet;

    }

    catch (Exception ex) {

      return null;

    }

  }



  public static hr.restart.db.raSQLSet getNewSQLSet(String qS) {

    return getNewSQLSet(qS,true);

  }





  // /begin/ (ab.f)

//  private String cachedGetTime = null;

  /**
   * Vraca tocno vrijeme servera na kojem je baza.
   * @return Timestamp s trenutacnim vremenom baze.
   */
  public Timestamp getCurrentDatabaseTime() {
    try {
//      if (cachedGetTime == null) {
//        hr.restart.baza.DDLCreator ddl = new hr.restart.baza.DDLCreator();
//        cachedGetTime = ddl.getSystemTimeString();
//      }
      Valid.getValid().execSQL(hr.restart.baza.Dialect.getNow());
      Valid.getValid().RezSet.open();
      return Valid.getValid().RezSet.getTimestamp(0);
    } catch (Exception e) {
      System.out.println(e);
      return Valid.getValid().getToday();
    }
  }

  /**

 * @param god godina

 * @return prvu sekundu u godini

 */

  public Timestamp getYearBegin(String god) {

    cal.clear();

    cal.set(Integer.parseInt(god), cal.JANUARY, 1, 0, 0, 0);

    return new Timestamp(cal.getTime().getTime());

  }
  
  /**
   * Provjerava jesu li datum dokumenta i datum knjiženja u istom mjesecu.
   * @param dat
   * @param knj
   * @return
   */
  public boolean sameMonth(Timestamp dat, Timestamp knj) {
    cal.setTime(dat);
    int datm = cal.get(cal.MONTH);
    int daty = cal.get(cal.YEAR);
    
    cal.setTime(knj);
    return cal.get(cal.YEAR) == daty && cal.get(cal.MONTH) == datm;
  }
  
  public boolean sameYear(Timestamp dat, Timestamp knj) {
    cal.setTime(dat);
    int daty = cal.get(cal.YEAR);
    
    cal.setTime(knj);
    return cal.get(cal.YEAR) == daty;
  }
  
  public boolean sameDay(Timestamp dat, Timestamp knj) {
    cal.setTime(dat);
    int datd = cal.get(cal.DATE);
    int datm = cal.get(cal.MONTH);
    int daty = cal.get(cal.YEAR);
    
    cal.setTime(knj);
    return cal.get(cal.YEAR) == daty && 
           cal.get(cal.MONTH) == datm &&
           cal.get(cal.DATE) == datd;
  }

/**

 * @param god godina

 * @return zadnju sekundu u godini

 */

  public Timestamp getYearEnd(String god) {

    cal.clear();

    cal.set(Integer.parseInt(god), cal.DECEMBER, 31, 23, 59, 59);

    return new Timestamp(cal.getTime().getTime());

  }



  /**

   * Vraca danasnji datum, ukoliko je isti u navedenom rasponu.

   * U suprotnom, vraca granicu blizu danasnjem datumu.<p>

   * @param beg Pocetni datum raspona.

   * @param end Krajnji datum raspona.

   * @return Timestamp gorenavedenih svojstava.

   */

  public Timestamp getToday(Timestamp beg, Timestamp end) {

  	Timestamp today = new Timestamp(System.currentTimeMillis());

  	if (today.before(getFirstSecondOfDay(beg))) return beg;

  	else if (today.after(getLastSecondOfDay(end))) return end;

  	else return today;

  }



  /**

   * Vra\u0107a Timestamp istog datuma ali s vremenom 00:00:00.00.<p>

   * @param source izvorni Timestamp.

   * @return Timestamp prve sekunde u istom danu kao i source Timestamp.

   */

  public Timestamp getFirstSecondOfDay(Timestamp source) {

    cal.setTime(new java.util.Date(source.getTime()));

    cal.set(cal.HOUR_OF_DAY, 0);

    cal.set(cal.MINUTE, 0);

    cal.set(cal.SECOND, 0);

    cal.set(cal.MILLISECOND, 0);

    return new Timestamp(cal.getTime().getTime());

  }



  /**

   * Vra\u0107a Timestamp istog datuma ali s vremenom 23:59:59.00.<p>

   * @param source izvorni Timestamp.

   * @return Timestamp zadnje sekunde u istom danu kao i source Timestamp.

   */

  public Timestamp getLastSecondOfDay(Timestamp source) {

    cal.setTime(new java.util.Date(source.getTime()));

    cal.set(cal.HOUR_OF_DAY, 23);

    cal.set(cal.MINUTE, 59);

    cal.set(cal.SECOND, 59);

    cal.set(cal.MILLISECOND, 0);

    return new Timestamp(cal.getTime().getTime());

  }



  /**

   * Vra\u0107a Timestamp koji nastaje dodavanjem odre\u0111enog broja dana

   * izvornom Timestampu.<p>

   * @param fromDate izvorni Timestamp.

   * @param days pomak u danima.

   * @return rezultiraju\u0107i Timestamp.

   */

  public Timestamp addDays(Timestamp fromDate, int days){

    cal.setTime(fromDate);

    int fromDateInt = cal.get(cal.DAY_OF_MONTH);

    cal.set(cal.DAY_OF_MONTH, fromDateInt + days);

    return new Timestamp(cal.getTime().getTime());

  }



  /**

   * Vra\u0107a Timestamp koji nastaje dodavanjem odre\u0111enog broja mjeseci

   * izvornom Timestampu.<p>

   * @param fromDate izvorni Timestamp.

   * @param months pomak u mjesecima.

   * @return rezultiraju\u0107i Timestamp.

   */

  public Timestamp addMonths(Timestamp fromDate, int months){

    cal.setTime(fromDate);

    int fromDateInt = cal.get(cal.MONTH);

    cal.set(cal.MONTH, fromDateInt + months);

    return new Timestamp(cal.getTime().getTime());

  }



  /**

   * Vra\u0107a Timestamp koji nastaje dodavanjem odre\u0111enog broja godina

   * izvornom Timestampu.<p>

   * @param fromDate izvorni Timestamp.

   * @param years pomak u godinama.

   * @return rezultiraju\u0107i Timestamp.

   */

  public Timestamp addYears(Timestamp fromDate, int years){

    cal.setTime(fromDate);

    int fromDateInt = cal.get(cal.YEAR);

    cal.set(cal.YEAR, fromDateInt + years);

    return new Timestamp(cal.getTime().getTime());

  }



  public int getHourDifference(Timestamp datod, Timestamp datdo) {

    return (int) ((datdo.getTime() - datod.getTime()) / (1000 * 60 * 60));

  }



  public String arrayString(Object[] o) {

    StringBuffer b = new StringBuffer();

    b.append("[");

    for (int i = 0; i < o.length; i++)

      b.append(i > 0 ? "," : "").append(o[i]);

    b.append("]");

    return b.toString();

  }



  public String arrayStringLn(Object[] o) {

    StringBuffer b = new StringBuffer();

    for (int i = 0; i < o.length; i++)

      b.append(o[i]).append("\n");

    return b.toString();

  }

  // /end/ (ab.f)

  public java.math.BigDecimal setScale(java.math.BigDecimal bd, int scale) {

    return bd.setScale(scale,java.math.BigDecimal.ROUND_HALF_UP);

  }



  public void applCheck() {

    /** @todo

     usporediti raRes sa tablicom aplikacija i napuniti nepostojece i pitati za svaku jel instalirana

     pozvati u kreatoru

     */

//    ResourceBundle rrs = ResourceBundle.getBundle(hr.restart.start.RESBUNDLENAME);

//    String applcontents = hr.restart.start.

  }
  /**
   * Uzme uzme prvu rijec iz name i doda key u nastavku.
   * npr. obsfucateName("Stol 160x80 sa ladicarom", "007") = "Stol 007"
   * @param name
   * @param key
   * @return
   */
  public static String obsfucateName(String name, String key) {
    String ret = "";
    String firstWord = new StringTokenizer(name," ").nextToken();
    if (firstWord != null) ret = firstWord;
    ret = ret + " " + key;
    return ret;
  }
  /**
   * uzme sve slogove u tablici table i u namecn ubaci obsfucateName (level 0)
   * @param table
   * @param namecn
   * @param keycn
   * @return
   */
  public static DataSet obsfucateTable(DataSet table, String namecn, String keycn) {
    return obsfucateTable(table, namecn, keycn, 0);
  }
  /**
   * uzme sve slogove u tablici table i u namecn ubaci obsfucateName za level 0, a za level > 0
   * otrgne sa pocetka level slova i doda key 
   * @param table
   * @param namecn
   * @param keycn
   * @param level 
   * @return
   */
  public static DataSet obsfucateTable(DataSet table, String namecn, String keycn, int level) {
    return obsfucateTable(table, new String[]{namecn}, keycn, level);
  }
  /**
   * uzme sve slogove u tablici table i u namecn[i] ubaci obsfucateName za level 0, a za level > 0
   * otrgne sa pocetka level slova i doda key 
   * @param table
   * @param namecn
   * @param keycn
   * @param level 
   * @return
   */
  public static DataSet obsfucateTable(DataSet table, String[] namecns, String keycn, int level) {
    for (table.first(); table.inBounds(); table.next()) {
      for (int i = 0; i < namecns.length; i++) {
        String namecn = namecns[i];
        String name = raVariant.getDataSetValue(table, namecn).toString();
        String key = raVariant.getDataSetValue(table, keycn).toString();
        Object obsVal = null;
        if (level == 0) {
          obsVal = obsfucateName(name,key);
        } else {
          String blanks = " ";
          for (int b = 0; b < level; b++) {
            blanks = blanks+" ";
          }
          obsVal = name.concat(blanks).substring(0,level)+" "+key;
        }
        raVariant.setDataSetValue(table, namecn, obsVal);
      }
    }
    return table;
  }
  public static void bufferedReadOut(InputStream in) {
    bufferedReadOut(in, System.out);
  }
  public static void bufferedReadOut(InputStream in, OutputStream out) {
    bufferedReadOut(in,out,1024);
  }
  public static void bufferedReadOut(InputStream in, OutputStream out, int buffersize) {
    byte[] buff = new byte[buffersize];
    int count = 0;
    try {
      do {
        count = in.read(buff);
        if (count > 0) out.write(buff, 0, count);
      } while (count >= 0);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
/* fora ali radi sa java 1.4+  :(
  public static String getMyIpFromWeb(){
    String myip = "";
    String pat = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
    String ipprovider = "http://checkip.dyndns.org/";
    Pattern p = Pattern.compile(pat);
    Matcher m = null;
    String line = "";
    try{
      URL u = new URL(ipprovider);
      InputStream in = u.openStream();
      BufferedReader rr = new BufferedReader(new InputStreamReader(in));
      while((line = rr.readLine()) != null){
        m = p.matcher(line);
	if (m.find()) break;
      }
      myip = m.group();
      return myip;
    }catch(Exception e){
      e.printStackTrace();
      return myip;
    }
  }
  */
}
/* OBSFUKATOR SCRIPT // TODO RADNI NALOZI, BLAGAJNA
ds = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM ORGSTRUKTURA");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZIV", "ADRESA", "ZIRO"}, "CORG",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM PARTNERI");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZPAR", "ADR", "MB", "ZR","TEL","TELFAX","EMADR","KO"}, "CPAR",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from zirorn");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"ZIRO"}, "CORG",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from Radnici");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"IME", "PREZIME", "IMEOCA"}, "CRADNIK",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from Radnicipl");
ods = hr.restart.util.Util.obsfucateTable(ds, 
new String[] {"JMBG","BRRADKNJ","REGBRRK","REGBRMIO","BROSIGZO","BROBVEZE","ADRESA","BROJTEK"}, "CRADNIK",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from logodat");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"TEKST"}, "CORG",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from sklad");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZSKL"}, "CSKL",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from artikli");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZART","NAZPROIZ","NAZORIG","OPIS"}, "CART",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from doki");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"ZIRO"}, "CPAR",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from stdoki");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZART"}, "CART",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from stdoku");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NAZART"}, "CART",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("select * from virmani");
ods = hr.restart.util.Util.obsfucateTable(ds, new String[] {"NATERET","BRRACNT"}, "KNJIG",4);
ods.saveChanges();

ds = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM os_sredstvo");
ods = hr.restart.util.Util.obsfucateTable(ds, "NAZSREDSTVA", "INVBROJ", 4);
ods.saveChanges();

*/