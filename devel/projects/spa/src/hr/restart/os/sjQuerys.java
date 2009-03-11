/****license*****************************************************************
**   file: sjQuerys.java
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
package hr.restart.os;

import hr.restart.baza.Condition;
import hr.restart.util.Aus;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class sjQuerys {

  public sjQuerys() {
  }
  public static String getOldOS4Amor(String ib) {
    String str="SELECT os_sredstvo.invbroj, os_sredstvo.nazsredstva, os_sredstvo.cgrupe, os_amgrupe.zakstopa,"+
               " os_amgrupe.odlstopa, os_sredstvo.osnpocetak, os_sredstvo.isppocetak "+
               "FROM os_sredstvo, os_amgrupe "+
               "WHERE os_sredstvo.cgrupe=os_amgrupe.cgrupe and os_sredstvo.invbroj='"+ib+"' and os_sredstvo.status='A'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getNewOS4Amor(String ib, String dat) {
    String str="select os_sredstvo.invbroj, os_sredstvo.nazsredstva, os_sredstvo.cgrupe, os_amgrupe.zakstopa,"+
                " os_amgrupe.odlstopa, os_promjene.datpromjene, os_promjene.osnduguje, os_promjene.osnpotrazuje,"+
                " os_promjene.ispduguje, os_promjene.isppotrazuje "+
                "FROM os_sredstvo, os_promjene, os_amgrupe "+
                "WHERE os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2=os_promjene.corg2"+
                " and os_sredstvo.cgrupe=os_amgrupe.cgrupe and os_sredstvo.status=os_promjene.status"+
                " and os_sredstvo.invbroj='"+ib+"' and os_promjene.datpromjene>="+dat+" and os_sredstvo.status='A'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getKnjizString() {
    String str="SELECT corg, kontoamor, kontoisp, amortizacija FROM os_obrada2, os_kontoisp where os_obrada2.corg=os_kontoisp.corg and os_obrada2.brojkonta=os_kontoisp.brojkonta";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String updatePromjeneAmor(String cvrp, java.sql.Timestamp dat) {
    String str="update os_promjene set isppotrazuje=isppotrazuje-(select os_obrada2.amortizacija from "+
               "os_obrada2, os_sredstvo where os_obrada2.invbroj=os_sredstvo.invbroj and "+
               "os_obrada2.corg=os_sredstvo.corg and os_promjene.invbroj=os_sredstvo.invbroj and "+
               "os_promjene.corg=os_sredstvo.corg and datlikvidacije is null) where cpromjene='"+cvrp+"'"+
               "os_promjene.aktiv='D'  and datpromjene>'"+hr.restart.util.Util.getUtil().getFirstDayOfYear(dat)+
               "' and datpromjene<'"+hr.restart.util.Util.getUtil().getLastDayOfYear(dat)+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delPromjeneAmor(String prom, java.sql.Timestamp dat) {
    String str="delete from os_promjene where os_promjene.aktiv='D' and os_promjene.cpromjene='"+
               prom+"' and datpromjene>'"+hr.restart.util.Util.getUtil().getFirstDayOfYear(dat)+"' and "+
               "datpromjene<'"+hr.restart.util.Util.getUtil().getLastDayOfYear(dat)+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String updateSredstvaAmor() {
    String str="update os_sredstvo set amortizacija=amortizacija-(select os_obrada2.amortizacija from os_obrada2 where os_obrada2.invbroj=os_sredstvo.invbroj and os_obrada2.corg=os_sredstvo.corg) where datlikvidacije is null";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delSredstvaAmor() {
    String str="update os_sredstvo set amortizacija=0, pamortizacija=0 where datlikvidacije is null";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getNeknjizeniLog() {
    String str="select * from os_log where statknj='N'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String deleteOBR4(String invbroj, String corg) {
    String str="delete from os_obrada4 where invbroj='"+invbroj+"' and CORG='"+corg+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String insertLog(int rbr) {
    String str="insert into OS_LOG (LOKK, AKTIV, CORG, RBR, DATOD, DATDO) select 'N', 'D', CORG, "+rbr+", DATUMOD, DATUMDO from OS_METAOBRADA";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String insertArh(int rbr) {
    String str="insert into OS_ARHIVA  select LOKK, AKTIV, CORG, "+rbr+", INVBROJ, NAZSREDSTVA, CLOKACIJE, "+
              "COBJEKT, CARTIKLA, CPAR, BROJKONTA, CGRUPE, ZAKSTOPA, ODLSTOPA, CSKUPINE, PORIJEKLO, "+
              "RADNIK, DATNABAVE, DATAKTIVIRANJA, 'N', OSNOVICA, ISPRAVAK, SADVRIJED, REVOSN, REVISP, "+
              "REVSAD, AMORTIZACIJA, PAMORTIZACIJA, REVAMOR, PREBACAM from OS_OBRADA2";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getPromjeneAmor(String corg, String invbroj, String godina) {
    String str="select * from os_promjene, os_vrpromjene where os_promjene.corg='"+corg+
        "' and os_promjene.invbroj='"+invbroj+
        "' and os_promjene.cpromjene=os_vrpromjene.cpromjene and os_vrpromjene.tippromjene='A"+
        "' and os_promjene.datpromjene between '"+hr.restart.util.Util.getUtil().getYearBegin(godina)+
        "' and '"+hr.restart.util.Util.getUtil().getYearEnd(godina)+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getMaxInvBroj(String corg) {
    return getMaxInvBroj(corg, false);
  }
  public static String getMaxInvBroj(String corg, boolean stringSearch) {
    String what = stringSearch?"max(invbroj)":"cast(max(cast(invbroj as numeric(10,0))) as char(10))";
    String str="select "+what+" from os_sredstvo where corg='"+corg+"'";//ISPROBAJ!!!
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getAmorRBR(String corg) {
    String str="select max(rbr) from os_log where corg='"+corg+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String setAktivToPriprema(String corg, String broj) {
    String str="update os_sredstvo set aktiv='D' where CORG='"+corg+"' and INVBROJ='"+broj+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOSFromCorg(String corg, char mode, String aktiv) {
    String str="select * from OS_SREDSTVO where "+getPripOrg(corg, "CORG2")+checkMode(mode)+checkAktiv(aktiv);
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOSFromOnlyCorg(String corg, char mode, String aktiv) {
    String str="select * from OS_SREDSTVO where CORG2='"+corg+"'"+checkMode(mode)+checkAktiv(aktiv);
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getSIFromCorg(String corg, String aktiv) {
    String str="select * from OS_SI where "+getPripOrg(corg, "CORG2")+checkAktiv(aktiv);
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getSIFromOnlyCorg(String corg, String aktiv) {
    String str="select * from OS_SI where CORG2='"+corg+"'"+checkAktiv(aktiv);
    System.out.println("SQL: "+str);
    return str;
  }
  static String getPripOrg(String corg, String tekst) {
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
    return Condition.in(tekst, tds, "corg").toString();
//    int i=0;
//    String cVrati=tekst+" in (";
//    tds.first();
//    do {
//      if (i>0) {
//        cVrati=cVrati+',';
//      }
//      i++;
//      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//    } while (tds.next());
//    cVrati=cVrati+")";
//    return cVrati;
  }
  static String checkMode(char mode) {
    if (mode=='P') {
      return " and STATUS='P'";
    }
    return " and STATUS !='P'";
  }
  static String checkAktiv(String mode) {
    if (mode.equals("D")) {
      return " and AKTIV='D'";
    }
    else if (mode.equals("N")) {
      return " and AKTIV ='N'";
    }
    return "";
  }
}
