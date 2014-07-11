/****license*****************************************************************
**   file: ReportValuteTester.java
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

import java.math.BigDecimal;

import com.borland.dx.sql.dataset.QueryDataSet;

public class ReportValuteTester {

  private static ReportValuteTester RVT;
  public static String titleROT1R  = "Ra\u010Dun-otpremnica 1 red";
  public static String titleROT1RV = "Ra\u010Dun-otpremnica 1 red u valuti";
  public static String titleROT2R  = "Ra\u010Dun-otpremnica 2 red";
  public static String titleROT2RV = "Ra\u010Dun-otpremnica 2 red u valuti";
  public static String titleROTSKL = "Ra\u010Dun-otpremnica koli\u010Dinska";
  public static String titleROTFIN = "Ra\u010Dun-otpremnica financijska";
  public static String titleRAC1R  = "Ra\u010Dun 1 red";
  public static String titleRAC1RV = "Ra\u010Dun 1 red u valuti";
  public static String titleRAC2R  = "Ra\u010Dun 2 red";
  public static String titleRAC2RV = "Ra\u010Dun 2 red u valuti";
  public static String titleRAC1USL  = "Ra\u010Dun >>više raèuna<<";
  public static String titleRACFROMRNAL = "Raèun iz radnog naloga";


  public static ReportValuteTester getReportValuteTester(){
    if(RVT== null) RVT = new ReportValuteTester();
    return RVT;
  }

  public static boolean isRep4Valute(){
      return
      (hr.restart.util.reports.dlgRunReport.getCurrentDlgRunReport().getCurrentReportTitle().equals(titleROT1RV) ||
       hr.restart.util.reports.dlgRunReport.getCurrentDlgRunReport().getCurrentReportTitle().equals(titleROT2RV) ||
       hr.restart.util.reports.dlgRunReport.getCurrentDlgRunReport().getCurrentReportTitle().equals(titleRAC1RV) ||
       hr.restart.util.reports.dlgRunReport.getCurrentDlgRunReport().getCurrentReportTitle().equals(titleRAC2RV) );
  }

  public static QueryDataSet cambioValute(QueryDataSet repORG) {

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
    QueryDataSet repQDS = new QueryDataSet();
    repQDS.setColumns(repORG.cloneColumns());
    repQDS.open();
    for (repORG.first();repORG.inBounds();repORG.next()) {
      repQDS.insertRow(true);
      dm.copyColumns(repORG,repQDS);
    }
    java.math.BigDecimal tmpBD = Aus.zero2;
//    repQDS.setBigDecimal("UIRAC",Aus.zero2);
    lc.setUseBigDecimal(true);
    
    int decs = Aus.getNumber(frmParam.getParam("robno", "cijenaDec", 
        "2", "Broj decimala za cijenu na izlazu (2-4)").trim());
    if (decs < 2) decs = 2;
    if (decs > 4) decs = 4;
    
    raKalkulBDDoc rkd = new raKalkulBDDoc();
    for (repQDS.first();repQDS.inBounds();repQDS.next()) {
      if (repQDS.getBigDecimal("TECAJ").doubleValue() != 0){
        if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getValute(),
            new String[]{"OZNVAL"},  new String[]{repQDS.getString("OZNVAL")})) {
          BigDecimal jedval = new BigDecimal(((double) dm.getValute().getInt("JEDVAL")));
          BigDecimal tmpTecaj;
          tmpTecaj =repQDS.getBigDecimal("TECAJ").divide(jedval,6,BigDecimal.ROUND_HALF_UP);
          repQDS.setBigDecimal("FC", repQDS.getBigDecimal("FC").divide(tmpTecaj,decs,BigDecimal.ROUND_HALF_UP));
          rkd.stavka.Init();
          lc.TransferFromDB2Class(repQDS,rkd.stavka);
          rkd.stavka.uprab = repQDS.getBigDecimal("UPRAB1");
          rkd.KalkulacijaStavke(repQDS.getString("VRDOK"),"KOL",'N',repQDS.getString("CSKL"),false);
          lc.TransferFromClass2DB(repQDS,rkd.stavka);
          tmpBD = tmpBD.add(repQDS.getBigDecimal("IPRODSP"));
        }
      }
    }
    BigDecimal jedval = new BigDecimal(((double) dm.getValute().getInt("JEDVAL")));
    BigDecimal tmpTecaj;
    tmpTecaj = jedval.signum() != 0 ? repQDS.getBigDecimal("TECAJ").divide(jedval,6,BigDecimal.ROUND_HALF_UP) : Aus.zero0;
    for (repQDS.first();repQDS.inBounds();repQDS.next()) {
      repQDS.setBigDecimal("UIRAC",tmpBD);
      if (tmpTecaj.signum() != 0)
        repQDS.setBigDecimal("UIU", repQDS.getBigDecimal("UIU").divide(tmpTecaj,decs,BigDecimal.ROUND_HALF_UP));
    }
    return repQDS;
  }


}