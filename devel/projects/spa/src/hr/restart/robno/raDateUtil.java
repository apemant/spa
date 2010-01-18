/****license*****************************************************************
**   file: raDateUtil.java
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

import java.util.Calendar;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raDateUtil {

  private Calendar DateCal1 = Calendar.getInstance();
  private Calendar DateCal2 = Calendar.getInstance();
  private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy.");
  private java.util.Date TmpDate;

  public raDateUtil() {
  }

  private static raDateUtil rdu;

  public static raDateUtil getraDateUtil() {
    if (rdu == null) {
      rdu = new raDateUtil();
    }
    return rdu;
  }
  
  void clear(Calendar cal) {
    cal.set(cal.HOUR_OF_DAY, 0);
    cal.set(cal.MINUTE, 0);
    cal.set(cal.SECOND, 0);
    cal.set(cal.MILLISECOND, 0);
  }

  public int CompareDate(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){

    DateCal1.setTime(Datum1);
    DateCal2.setTime(Datum2);

    if  (DateCal1.get(DateCal1.YEAR) > DateCal2.get(DateCal2.YEAR))
      return 1;
    if  (DateCal1.get(DateCal1.YEAR) < DateCal2.get(DateCal2.YEAR))
      return -1;

    if  (DateCal1.get(DateCal1.MONTH) > DateCal2.get(DateCal2.MONTH))
      return 1;
    if  (DateCal1.get(DateCal1.MONTH) < DateCal2.get(DateCal2.MONTH))
      return -1;
    
    if  (DateCal1.get(DateCal1.DATE) > DateCal2.get(DateCal2.DATE))
      return 1;
    if  (DateCal1.get(DateCal1.DATE) < DateCal2.get(DateCal2.DATE))
      return -1;
    return 0;
  }

  public boolean isGrater(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){
    return (CompareDate(Datum1,Datum2)==1);
  }
  public boolean isEqual(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){
    return (CompareDate(Datum1,Datum2)==0);
  }
  public boolean isGreaterEqual(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){
    return (isGrater(Datum1,Datum2)||isEqual(Datum1,Datum2));
  }
  public boolean isLess(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){
    return (CompareDate(Datum1,Datum2)==-1);
  }
  public boolean isLessEqual(java.sql.Timestamp Datum1,java.sql.Timestamp Datum2){
    return (isLess(Datum1,Datum2)||isEqual(Datum1,Datum2));
  }

  public java.sql.Timestamp addInt2Timestamp(java.sql.Timestamp Datum, int za_koliko){
    return new java.sql.Timestamp(Datum.getTime()+za_koliko* (1000 * 60 * 60 * 24));
  }
  /**
   * Funkcija koja zbraja/oduzima dane od odredjenog datuma
   */

  public java.util.Date addDate(java.util.Date Datum, int za_koliko){

    DateCal1.setTime(Datum);
    DateCal1.add(DateCal1.DATE,za_koliko);
    return DateCal1.getTime();

  }
  public java.util.Date addMonth(java.util.Date Datum, int za_koliko){

    DateCal1.setTime(Datum);
    DateCal1.add(DateCal1.MONTH,za_koliko);
    return DateCal1.getTime();

  }


  public int DateDifference(java.util.Date Datum, java.util.Date Datum2){
/*
    int RazlikaGodina = 0;
    int RazlikaMjeseci = 0;
    int RazlikaDana = 0;
    DateCal1.setTime(Datum);
    DateCal2.setTime(Datum2);

    while (DateCal1.get(DateCal1.DATE) != DateCal2.get(DateCal2.DATE) ||
           DateCal1.get(DateCal1.MONTH) != DateCal2.get(DateCal2.MONTH) ||
           DateCal1.get(DateCal1.YEAR) != DateCal2.get(DateCal2.YEAR)){

      if (DateCal1.get(DateCal1.YEAR) != DateCal2.get(DateCal2.YEAR)){
        RazlikaGodina=RazlikaGodina+365*(DateCal2.get(DateCal2.YEAR) - DateCal1.get(DateCal1.YEAR));
        DateCal1.add(DateCal1.DATE,RazlikaGodina);
      }
      if (DateCal1.get(DateCal1.MONTH) != DateCal2.get(DateCal2.MONTH)){
        RazlikaMjeseci=RazlikaMjeseci+30*(DateCal2.get(DateCal2.MONTH) - DateCal1.get(DateCal1.MONTH));
        DateCal1.add(DateCal1.DATE,RazlikaMjeseci);
      }
      if (DateCal1.get(DateCal1.DATE) != DateCal2.get(DateCal2.DATE)){
        RazlikaDana=RazlikaDana+DateCal2.get(DateCal2.DATE) - DateCal1.get(DateCal1.DATE);
        DateCal1.add(DateCal1.DATE,RazlikaDana);
      }
    }
    return RazlikaDana+RazlikaMjeseci+RazlikaGodina; */
//    hr.restart.util.Util.getUtil().getFirstSecondOfDay()
    
    DateCal1.setTime(Datum);
    clear(DateCal1);
    Datum = DateCal1.getTime();
    DateCal2.setTime(Datum2);
    clear(DateCal2);
    Datum2 = DateCal2.getTime();

    return (int) Math.round((Datum2.getTime() - Datum.getTime()) / (1000 * 60 * 60 * 24.));
  }
  
  public String dataFormatter(java.sql.Timestamp Datum1){
    if (Datum1 == null) return "";
    Calendar c = Calendar.getInstance();
    c.setTime(Datum1);
    if (c.get(c.YEAR) < 1985) return "";

    String date = (Datum1 == null ? null : sdf.format(Datum1));
    return (date == null || date.indexOf("1970")>=0 ? "" : date);

  }
  public String PrepDate(java.sql.Timestamp dat, boolean end){
    DateCal1.setTime(dat);
    DateCal1.set(DateCal1.AM_PM,DateCal1.AM);
    if (end) {
      DateCal1.set(DateCal1.HOUR,0);
      DateCal1.set(DateCal1.MINUTE,0);
      DateCal1.set(DateCal1.SECOND,0);
      DateCal1.set(DateCal1.MILLISECOND,0);
    }
    else {
      DateCal1.set(DateCal1.HOUR,23);
      DateCal1.set(DateCal1.MINUTE,59);
      DateCal1.set(DateCal1.SECOND,59);
      DateCal1.set(DateCal1.MILLISECOND,999);
    }
    return new java.sql.Timestamp(DateCal1.getTime().getTime()).toString();
  }

  public String getMonth(java.sql.Timestamp dat){
    DateCal1.setTime(dat);
    return String.valueOf(DateCal1.get(DateCal1.MONTH)+1);
/*
    if (DateCal1.get(DateCal1.MONTH) <10) {
      return "0"+DateCal1.get(DateCal1.MONTH);
    } else {
      return String.valueOf(DateCal1.get(DateCal1.MONTH));
    }
*/
  }

  public String getYear2Digit(java.sql.Timestamp dat){
    DateCal1.setTime(dat);
    String forReturn = String.valueOf(DateCal1.get(DateCal1.YEAR));
    return Integer.parseInt(forReturn.substring(forReturn.length()-2))+"";

/*
    if (DateCal1.get(DateCal1.YEAR) <10) {
      return "0"+DateCal1.get(DateCal1.YEAR);
    } else if (DateCal1.YEAR >9 || DateCal1.YEAR < 100 ){
      return String.valueOf(DateCal1.YEAR);
    } else return "?";
*/
//    else if (DateCal1.YEAR >99 || DateCal1.YEAR < 1000 ){
//    return String.valueOf(DateCal1.YEAR).;

  }
  public static java.sql.Timestamp String2Timestamp(String date){
      
      Integer year = new Integer(date.substring(0,4));
      Integer month = new Integer(date.substring(5,7));
      Integer dan = new Integer(date.substring(8,10));
      Calendar cal =Calendar.getInstance(); 
      cal.set(year.intValue(),
              month.intValue()+1,dan.intValue(),0,0,0);
      return new java.sql.Timestamp(cal.getTime().getTime());
  }


}