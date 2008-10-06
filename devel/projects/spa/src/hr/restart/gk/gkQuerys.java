/****license*****************************************************************
**   file: gkQuerys.java
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
import hr.restart.baza.raDataSet;
import hr.restart.util.Aus;
import hr.restart.util.Util;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;

public class gkQuerys {
//static hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private static QueryDataSet parZiroSet = null;
  protected gkQuerys() {
  }

  public static int getMaxNaloziRBR(String knjig, String god, String cvrnal) {
    hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();
    Vl.execSQL(
      "select max(rbr) as maxrbr from nalozi " +
      "where knjig = '"+knjig+"' and god = '"+god+"' and cvrnal = '"+cvrnal+"'"
    );
    Vl.RezSet.open();
    return Vl.RezSet.getInt("maxrbr");
  }

  public static int getMaxGkstavkeradRBS(String knjig, String god, String cvrnal, int rbr) {
    return getMaxGkstavke___RBS(knjig, god, cvrnal, rbr, "gkstavkerad");
  }
  
  public static int getMaxGkstavke___RBS(String knjig, String god, String cvrnal, int rbr, String tablename) {
    hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();
    Vl.execSQL(
      "select max(rbs) as maxrbr from "+tablename+
      " where knjig = '"+knjig+"' and god = '"+god+"' and cvrnal = '"+cvrnal+"' and rbr = "+rbr
    );
    Vl.RezSet.open();
    return Vl.RezSet.getInt("maxrbr");
  }
  public static QueryDataSet getNewQueryDataSet(String qS) {
    return Util.getNewQueryDataSet(qS);
  }

  public static QueryDataSet getNewQueryDataSet(String qS,boolean toOpen) {
    return Util.getNewQueryDataSet(qS,toOpen);
  }

  public static QueryDataSet getGKKumulMj(String godmj) {
    String qS = "select * from gkkumulativi where godmj = '"+godmj+"'";
    QueryDataSet retSet = getNewQueryDataSet(qS);
//ST.prn("getGKKumulMj - "+qS);
//ST.prn(retSet);
    return retSet;
  }
//.getString("KNJIG"),kumul.getString("GOD"),kumul.getString("CVRNAL"),kumul.getInt("RBR"));
  public static QueryDataSet getKumulGKStavkeNal(ReadRow nalog) {
    String tabela;
    if (nalog.getString("STATUS").equals("K")) {
      tabela = "gkstavke";
    } else {
      tabela = "gkstavkerad";
    }
    String knjig = nalog.getString("KNJIG");
    String god = nalog.getString("GOD");
    String cvrnal = nalog.getString("CVRNAL");
    int rbr = nalog.getInt("RBR");
    String qS = "select sum(ID) as SUMID, sum(IP) as SUMIP, count(*) AS CNT from " + tabela +
    " where knjig = '"+knjig+"' and god = '"+god+"' and cvrnal = '"+cvrnal+"' and rbr = "+rbr;
    QueryDataSet retSet = getNewQueryDataSet(qS);
    return retSet;
  }
  public static QueryDataSet getGKStavkeNal(String knjig, String god, String cvrnal, int rbr) {
    String qS = "select * from gkstavke " +
    "where knjig = '"+knjig+"' and god = '"+god+"' and cvrnal = '"+cvrnal+"' and rbr = "+rbr;
    QueryDataSet retSet = getNewQueryDataSet(qS);
//ST.prn("getGKStavkeNal - "+qS);
//ST.prn(retSet);
    return retSet;
  }
  public static void delKumulNul(String godmj) {
    hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();
    String qS = "delete from gkkumulativi where id = 0.0 and ip = 0.0 and godmj = '"+godmj+"'";
    Vl.runSQL(qS);
  }
  public static QueryDataSet getKumulStavkeQuery(QueryDataSet kumul) {
    return getKumulStavkeQuery(kumul, false);
  }
  
  public static QueryDataSet getKumulStavkeQuery(QueryDataSet kumul, boolean group) {
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int year = new Integer(kumul.getString("GODMJ").substring(0,4)).intValue();
    int month = new Integer(kumul.getString("GODMJ").substring(4)).intValue()-1;
//System.out.println(kumul.getString("GODMJ")+":: y="+year+"  m="+month);
    cal.set(year,month,1);
    java.sql.Timestamp datKum = new java.sql.Timestamp(cal.getTime().getTime());
    String dateRange = vl.getBetweenQuerySintax(
      hr.restart.baza.dM.getDataModule().getGkstavke().getColumn("DATUMKNJ"),
      ut.getFirstDayOfMonth(datKum).toString(),
      ut.getLastDayOfMonth(datKum).toString(),
      true
    );
    String qry = group ? "SELECT sum(ID) as SUMID,sum(IP) as SUMIP,count(*) AS CNT,corg,brojkonta" 
        +" from GKSTAVKE where "+dateRange+" AND cvrnal!='00' AND " + Aus.getKnjigCond() +
        " group by corg, brojkonta"
      : "SELECT sum(ID) as SUMID, sum(IP) as SUMIP, count(*) AS CNT from GKSTAVKE where "
      +"CORG = '"+kumul.getString("CORG")+"' "
      +"AND BROJKONTA = '"+kumul.getString("BROJKONTA")+"'"
      +" AND "+dateRange+" AND cvrnal!='00'";
    QueryDataSet stavkesum = getNewQueryDataSet(qry, !group);
    if (group) {
      stavkesum.setMetaDataUpdate(stavkesum.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
      stavkesum.open();
      stavkesum.getColumn("CORG").setRowId(true);
      stavkesum.getColumn("BROJKONTA").setRowId(true);
    }
//ST.prn("getKumulStavkeQuery - "+qry);
//ST.prn(stavkesum);
    return stavkesum;
  }
  public static int getNextBrojIzv(String ziro, String god) {
    String qry = "SELECT max(BROJIZV) from IZVODI where KNJIG = '"
      +hr.restart.zapod.OrgStr.getKNJCORG(false)+"' and ZIRO = '"+ziro+"' and god = '"+god+"'";
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    vl.execSQL(qry);
    vl.RezSet.open();
    if (vl.RezSet.rowCount() == 0) return 1;
    return vl.RezSet.getInt(0) + 1;
  }
  public static java.math.BigDecimal getPrethStanje(String ziro,String god,int brojizv) {
    String qry = "SELECT NOVOSTANJE from IZVODI where KNJIG = '"
      +hr.restart.zapod.OrgStr.getKNJCORG(false)+
      "' and ZIRO = '"+ziro+"' and god = '"+god+"' and BROJIZV = "+brojizv;
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    vl.execSQL(qry);
    vl.RezSet.open();
    if (vl.RezSet.rowCount() == 0) return new java.math.BigDecimal(0);
    return vl.RezSet.getBigDecimal("NOVOSTANJE");
  }
  public static QueryDataSet getParZiro() {
    if (parZiroSet == null) {
      parZiroSet = new raDataSet();
      Aus.setFilter(parZiroSet, 
        "SELECT ZIROPAR.ZIRO,ZIROPAR.OZNVAL,PARTNERI.CPAR,PARTNERI.NAZPAR,PARTNERI.MJ,PARTNERI.ADR,".concat(
        "PARTNERI.MB,PARTNERI.ULOGA,PARTNERI.DI,PARTNERI.DOSP,ZIROPAR.CPAR ").concat(
        "FROM PARTNERI,ZIROPAR WHERE PARTNERI.CPAR = ZIROPAR.CPAR AND PARTNERI.AKTIV = 'D'"));
      hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
      QueryDataSet partneri = dm.getPartneri();
      QueryDataSet ziropar = dm.getZiropar();
      Column ciZIROPAR_CPAR = (Column)ziropar.getColumn("CPAR").clone();
      ciZIROPAR_CPAR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

      parZiroSet.setColumns(new Column[] {
        (Column)ziropar.getColumn("ZIRO").clone(),
        (Column)ziropar.getColumn("OZNVAL").clone(),
        (Column)partneri.getColumn("CPAR").clone(),
        (Column)partneri.getColumn("NAZPAR").clone(),
        (Column)partneri.getColumn("MJ").clone(),
        (Column)partneri.getColumn("ADR").clone(),
        (Column)partneri.getColumn("MB").clone(),
        (Column)partneri.getColumn("ULOGA").clone(),
        (Column)partneri.getColumn("DI").clone(),
        (Column)partneri.getColumn("DOSP").clone(),
        ciZIROPAR_CPAR
      });
    }
    return parZiroSet;
  }
}