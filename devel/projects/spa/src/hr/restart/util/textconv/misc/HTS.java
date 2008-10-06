/****license*****************************************************************
**   file: HTS.java
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
package hr.restart.util.textconv.misc;

import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.dM;
import hr.restart.sk.frmIzborStavki;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;
import hr.restart.util.textconv.Parser2Dataset;
import hr.restart.zapod.OrgStr;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * hr.restart.util.textconv.misc.HTS.getPartneri();
 * @author andrej
 *
 */
public class HTS {

  protected HTS() {
  }
  
  public static QueryDataSet getPartneri(String fn) {
    if (fn == null) fn = "DEBITR.CSV";
    File f = new File(fn);

    QueryDataSet partneri = Partneri.getDataModule().getTempSet("0=1");
    partneri.open();
    Parser2Dataset.fillDataSet("HTS.xml","FPP",f,partneri);
    return partneri;
  }
  
  public static QueryDataSet getSKStavkerad(String knjig, String fn, String konto, String cskl) {
    startFrame.getStartFrame();
    if (fn == null) fn = "ARTRANS.CSV";
    if (knjig == null) knjig = OrgStr.getKNJCORG();
    if (konto == null) konto = "1200";
    if (cskl == null) cskl = "10";
    File f = new File(fn);
    QueryDataSet skstavkerad = Skstavkerad.getDataModule().getTempSet("0=1");
    Column colDANIDOSP = dM.createIntColumn("DANIDOSP");
    colDANIDOSP.setResolvable(false);
    skstavkerad.addColumn(colDANIDOSP);
    skstavkerad.open();
    Parser2Dataset.fillDataSet("HTS.xml","FPS",f,skstavkerad);
    for (skstavkerad.first(); skstavkerad.inBounds(); skstavkerad.next()) {
      skstavkerad.setString("KNJIG", knjig);
      skstavkerad.setShort("STAVKA",(short)1);
      skstavkerad.setString("CSKL",cskl);
      skstavkerad.setString("VRDOK","IRN");
      skstavkerad.setInt("RBS",1);
      skstavkerad.setInt("BROJIZV",0);
      skstavkerad.setString("CORG", knjig);
      skstavkerad.setTimestamp("DATDOSP",
          Util.getUtil().addDays(skstavkerad.getTimestamp("DATDOK"),skstavkerad.getInt("DANIDOSP")));
      skstavkerad.setString("POKRIVENO","D");
      skstavkerad.setBigDecimal("SALDO",skstavkerad.getBigDecimal("ID")
          .add(skstavkerad.getBigDecimal("IP").negate()));
      skstavkerad.setShort("CKOLONE",(short)0);
      skstavkerad.setString("CKNJIGE","A");
      skstavkerad.setString("URAIRA","I");
      skstavkerad.setString("CGKSTAVKE","D");
      skstavkerad.setString("DUGPOT","D");
      skstavkerad.setTimestamp("DATPRI", skstavkerad.getTimestamp("DATDOK"));
      skstavkerad.setTimestamp("DATUNOS", new Timestamp(System.currentTimeMillis()));
      skstavkerad.setBigDecimal("PVID", skstavkerad.getBigDecimal("ID"));
      skstavkerad.setBigDecimal("PVIP", skstavkerad.getBigDecimal("IP"));
      skstavkerad.setString("BROJKONTA",konto);
      skstavkerad.post();
    }
    
    return skstavkerad;
  }
  
  public static void main(String[] args) {
    String pf = null, rf = null, knj = null, konk = null, konp = null, cskl = null;
    if (args.length>0) pf = args[0];
    if (args.length>1) rf = args[1];
    if (args.length>2) knj = args[2];
    if (args.length>3) konk = args[3];
    if (args.length>4) konp = args[4];
    if (args.length>5) cskl = args[5];
    importHTS(pf, rf, knj, konk, konp, cskl);
  }

  /**
   * @param pf file partnera (DEBITR.CSV)
   * @param rf file racuna (ATTRANS.CSV)
   * @param knj knjigovodstvo za import
   * @param konk konto kupca - iznosa racuna (1200)
   * @param konp protukonto iznosa racuna - prihod (75000)
   */
  public static void importHTS(final String pf,final String rf,final String knj,final String konk,final String _konp, final String cskl) {
    raProcess.runChild(new Runnable() {
      public void run() {
        String konp = "75000";
        if (_konp != null) konp = _konp; 
        QueryDataSet HTSpartneri = getPartneri(pf);
        QueryDataSet partneri = Partneri.getDataModule().getQueryDataSet();
        mergePar(HTSpartneri, partneri);
        partneri.saveChanges();
        QueryDataSet HTSskstavkerad = getSKStavkerad(knj, rf, konk, cskl);
        QueryDataSet HTSprotustavkerad = Skstavkerad.getDataModule().getTempSet("0=1");
        HTSprotustavkerad.open();
        for (HTSskstavkerad.first(); HTSskstavkerad.inBounds(); HTSskstavkerad.next()) {
          HTSprotustavkerad.insertRow(false);
          HTSskstavkerad.copyTo(HTSprotustavkerad);
          HTSprotustavkerad.setInt("RBS",2);
          HTSprotustavkerad.setString("BROJKONTA",konp);
          HTSprotustavkerad.setShort("STAVKA",(short)2);
          BigDecimal ip = HTSskstavkerad.getBigDecimal("ID");
          BigDecimal id = HTSskstavkerad.getBigDecimal("IP");
          HTSprotustavkerad.setBigDecimal("IP",ip);
          HTSprotustavkerad.setBigDecimal("ID",id);
          HTSprotustavkerad.setString("DUGPOT","P");
          HTSprotustavkerad.setBigDecimal("SALDO",HTSprotustavkerad.getBigDecimal("ID")
              .add(HTSprotustavkerad.getBigDecimal("IP").negate()));
          HTSprotustavkerad.setBigDecimal("PVID", HTSprotustavkerad.getBigDecimal("ID"));
          HTSprotustavkerad.setBigDecimal("PVIP", HTSprotustavkerad.getBigDecimal("IP"));
          HTSprotustavkerad.post();
        }
        HTSskstavkerad.saveChanges();
        HTSprotustavkerad.saveChanges();
      };
    });
    frmIzborStavki.proknjizi(
        Skstavkerad.getDataModule().getFilteredDataSet(Condition.equal("RBS", 1)));
    System.out.println("Bravo!");
  }

  private static void mergePar(QueryDataSet dsn, QueryDataSet dsc) {
    for (dsn.first(); dsn.inBounds(); dsn.next()) {
      if (lookupData.getlookupData().raLocate(dsc, "CPAR", dsn.getInt("CPAR")+"")) {
//        dsn.copyTo(dsc);
//        dsc.post();
      } else {
        dsc.insertRow(false);
        dsn.copyTo(dsc);
        dsc.post();
      }
    }
  }
}
