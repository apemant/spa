/****license*****************************************************************
**   file: raPlObrRange.java
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
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.util.Util;
import hr.restart.zapod.dlgGetKnjig;

import java.sql.Timestamp;

public class raPlObrRange {
  private short GODOBRfrom;
  private short MJOBRfrom;
  private short RBROBRfrom;
  private short GODOBRto;
  private short MJOBRto;
  private short RBROBRto;

  public raPlObrRange() {

  }

  public raPlObrRange(short _GODOBRfrom,short _MJOBRfrom,short _RBROBRfrom,short _GODOBRto,short _MJOBRto,short _RBROBRto) {
    GODOBRfrom = _GODOBRfrom;
    MJOBRfrom = _MJOBRfrom;
    RBROBRfrom = _RBROBRfrom;
    GODOBRto = _GODOBRto;
    MJOBRto = _MJOBRto;
    RBROBRto = _RBROBRto;
  }

  public raPlObrRange(short _GODOBR,short _MJOBR,short _RBROBR) {
    GODOBRfrom = _GODOBR;
    MJOBRfrom = _MJOBR;
    RBROBRfrom = _RBROBR;
    GODOBRto = _GODOBR;
    MJOBRto = _MJOBR;
    RBROBRto = _RBROBR;
  }

  /**
   * @return vraca query string kojim se selektiraju ograniciti arhivske tabele
   */
  public String getQuery() {
    return getQuery(null);
  }

  /**
   * @param tableName ime tablice za qualifier
   * @return vraca query string kojim se selektiraju ograniciti arhivske tabele
   */
  public String getQuery(String tableName) {
    String tName = getTName(tableName);
    int godmjobrfrom = GODOBRfrom*100+MJOBRfrom;
    int godmjobrto = GODOBRto*100+MJOBRto;
    return tName+"godobr*100+"+tName+"mjobr BETWEEN "+godmjobrfrom+" AND "+godmjobrto
        +" AND "+tName+"rbrobr BETWEEN "+RBROBRfrom+" AND "+RBROBRto;
  }

  private static String getTName(String _tableName) {
    String tName = null;
    String tableName = checkTabelaArh(_tableName);
    if (tableName == null) return "";
    if (tableName.trim().equals("")) return "";
    return tableName.concat(".");
  }
  /**
   * Da li je tabela arhivska odnosno ima li u sebi kolone godobr, mjobr i rbrobr
   * @param tabela
   * @return ime tabele iz parametra ako je arhivska, a ako nije vraca null (za poziv raPlObrRange.getQuery());
   */
  private static String checkTabelaArh(String tabela) {
//    KreirDrop[] kd = KreirDrop.getModulesWithColumns(new String [] {"godobr","mjobr","rbrobr"});
//    for (int i = 0; i < kd.length; i++) {
//      if (kd[i].get...MA ZNAM NAPAMET)
//      
//    }
    if (tabela == null) return null;
    String[] arhtablenames = new String[] {"odbiciarh","prisutarh","kumulorgarh","kumulradarh","primanjaarh","rsperiodarh"};
    for (int i = 0; i < arhtablenames.length; i++) {
      if (arhtablenames[i].equalsIgnoreCase(tabela)) return tabela;
    }
    return null;
  }

  /**
   *
   * @param godispl
   * @param mjispl
   * @return
   */
  public static String getInQueryIsp(int godispl, int mjispl) {
    return getInQueryIsp(godispl,mjispl,null);
  }
  /**
   *
   * @param godispl
   * @param mjispl
   * @param tableName
   * @return
   */
  public static String getInQueryIsp(int godispl, int mjispl, String tableName) {
    return getInQueryIsp(godispl,mjispl,godispl,mjispl,tableName);
  }

  /**
   *
   * @return short[0] = min godobr; short[1] = min mjobr; short[2] = min rbr; short[3] = max godobr; short[4] = max mjobr; short[5] = max rbr
   */
  public static short[] getMinAndMaxObrada() {
    short[] rets = new short[6];
    java.util.Arrays.fill(rets,(short)0);
    if (qdsQueryIsp == null) return rets;
    if (qdsQueryIsp.getRowCount()==0) return rets;
    rets[0] = 9999;
    rets[1] = 99;
    rets[2] = 9999;
    qdsQueryIsp.first();
    do {
      short god = qdsQueryIsp.getShort("GODOBR");
      short mj = qdsQueryIsp.getShort("MJOBR");
      short rbr = qdsQueryIsp.getShort("RBROBR");
      if (rets[0] > god) rets[0] = god;
      if (rets[1] > mj) rets[1] = mj;
      if (rets[2] > rbr) rets[2] = rbr;
      if (rets[3] < god) rets[3] = god;
      if (rets[4] < mj) rets[4] = mj;
      if (rets[5] < rbr) rets[5] = rbr;
    } while (qdsQueryIsp.next());
    return rets;
  }
  private static com.borland.dx.dataset.DataSet qdsQueryIsp;

  /**
   *
   * @param godisplod
   * @param mjisplod
   * @param godispldo
   * @param mjispldo
   * @param tableName
   * @return
   */
  public static String getInQueryIsp(int godisplod, int mjisplod, int godispldo, int mjispldo, String tableName) {
    return getInQueryIsp(godisplod, mjisplod, godispldo, mjispldo, tableName, dlgGetKnjig.getKNJCORG());
  }
  
  public static String getInQueryIsp(int godisplod, int mjisplod, int godispldo, int mjispldo, String tableName, String knjig) {
    String tName = getTName(tableName);
    String ret = tName+"godobr*10000+"+tName+"mjobr*100+"+tName+"rbrobr in (";
    hr.restart.zapod.OrgStr ors = hr.restart.zapod.OrgStr.getOrgStr();
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.set(godisplod,mjisplod-1,1);
    java.sql.Date d = new java.sql.Date(c.getTime().getTime());
    String datod = d.toString();
    Timestamp cdod = new Timestamp(c.getTime().getTime());
    c.set(godispldo,mjispldo-1,1);
    d = new java.sql.Date(c.getTime().getTime());
    String datdo = new java.sql.Date(Util.getUtil().getLastDayOfMonth(new java.sql.Timestamp(d.getTime())).getTime()).toString();
    Condition datisplbetween = Condition.between("kumulorgarh.datumispl", cdod, Util.getUtil().getLastDayOfMonth(new java.sql.Timestamp(d.getTime())));
    String qry = "SELECT kumulorgarh.godobr,kumulorgarh.mjobr,kumulorgarh.rbrobr FROM kumulorgarh where " + datisplbetween
 //           "kumulorgarh.datumispl between '"+datod+"' AND '"+datdo
    +" AND (kumulorgarh.corg in "+ors.getInQuery(ors.getOrgstrAndKnjig(knjig),"kumulorgarh.corg")+") "
    +" group by kumulorgarh.godobr,kumulorgarh.mjobr,kumulorgarh.rbrobr";
System.out.println(qry);
    qdsQueryIsp = Util.getNewQueryDataSet(qry);
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(qdsQueryIsp);
//    System.out.println("qry : " + qry);
    if (qdsQueryIsp.getRowCount() == 0) return tName+"godobr*10000+"+tName+"mjobr*100+"+tName+"rbrobr in (99999999)";
    qdsQueryIsp.first();
    do {
      int godmjrbr = qdsQueryIsp.getShort("GODOBR")*10000+qdsQueryIsp.getShort("MJOBR")*100+qdsQueryIsp.getShort("RBROBR");
      ret = ret + godmjrbr + ",";
    } while (qdsQueryIsp.next());
    ret = ret.substring(0,ret.length()-1).concat(")");
    return ret;
  }
  //geteri i seteri
  public short getGODOBRfrom() {
    return GODOBRfrom;
  }
  public void setGODOBRfrom(short GODOBRfrom) {
    this.GODOBRfrom = GODOBRfrom;
  }
  public short getGODOBRto() {
    return GODOBRto;
  }
  public void setGODOBRto(short GODOBRto) {
    this.GODOBRto = GODOBRto;
  }
  public void setMJOBRfrom(short MJOBRfrom) {
    this.MJOBRfrom = MJOBRfrom;
  }
  public short getMJOBRfrom() {
    return MJOBRfrom;
  }
  public short getMJOBRto() {
    return MJOBRto;
  }
  public void setMJOBRto(short MJOBRto) {
    this.MJOBRto = MJOBRto;
  }
  public short getRBROBRfrom() {
    return RBROBRfrom;
  }
  public void setRBROBRfrom(short RBROBRfrom) {
    this.RBROBRfrom = RBROBRfrom;
  }
  public short getRBROBRto() {
    return RBROBRto;
  }
  public void setRBROBRto(short RBROBRto) {
    this.RBROBRto = RBROBRto;
  }
}