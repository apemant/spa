/****license*****************************************************************
**   file: raIniciranje.java
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
import hr.restart.baza.Iniprim;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Odbici;
import hr.restart.baza.Radnici;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raIniciranje {
  private static raIniciranje raInit;
  QueryDataSet qds;
  lookupData ld = lookupData.getlookupData();
  sysoutTEST st = new sysoutTEST(false);
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  String[] nivoidxs = null;
  private QueryDataSet fondsati = null;
  protected raIniciranje() {
  }
  public static raIniciranje getInstance() {
    if (raInit==null) raInit = new raIniciranje();
    return raInit;
  }
  int iniCount = 0;
  int obrCount = 0;
  protected boolean brisinule = frmParam.getParam("pl", "delininule", "N", "Da li da obrise sva primanja koja imaju sate i iznos 0 nakon dodavanja inicijalnih primanja").equals("D");
  public void checkObr(String corg) {
    iniCount = 0;
    obrCount = 0;
    qds = Util.getNewQueryDataSet(sjQuerys.selectOrgStr(corg));
    qds.first();
    dm.getOrgpl().open();
    dm.getOrgpl().refresh();
    do {
      if (posOrgsPl(qds.getString("CORG"))) {
        if (raParam.getParam(dm.getOrgpl(), 1).equals("I")) iniCount++;
        else if (raParam.getParam(dm.getOrgpl(), 1).equals("O")) obrCount++;
      }
    } while(qds.next());
  }
  public boolean isInitObr(hr.restart.pl.frmObradaPL ekr) {
    return isInitObr(ekr.tds.getString("CORG"));
  }
  public boolean isInitObr(String corg) {
    checkObr(corg);
    if (obrCount > 0) return true;
    if (iniCount > 0) return true;
    return false;
  }
  private java.math.BigDecimal getSatiPrim(QueryDataSet inipr) {
    String sfond = inipr.getString("SFOND");
    if (sfond.equals("X") || fondsati == null || fondsati.getRowCount() == 0) {
      return inipr.getBigDecimal("SATI");
    } else {
      return fondsati.getBigDecimal(sfond.equals("R")?"SATIRAD":"SATIPRAZ");
    }
  }
  private QueryDataSet getIniprim(QueryDataSet radpl) {
    /**@todo: selektiraj s obzirom na nivoe */
    if (nivoidxs == null) return null;
    String cond = null;
    for (int i=0; i<nivoidxs.length; i++) {
      if (!nivoidxs[i].equals("-")) {
        String cnivo = null;
        String[] inquery = null;
        if (nivoidxs[i].startsWith("INI")) {
          cnivo = nivoidxs[i]+":";
        } else if (nivoidxs[i].startsWith("SVI")) {
          cnivo = "";
        } else if (nivoidxs[i].startsWith("CORG")) {
          cnivo = null;
          inquery = OrgStr.getBranchCorgs(radpl.getString("CORG"));
          for (int ix=0; ix<inquery.length; ix++) {
            inquery[ix] = "CORG:"+inquery[ix];
          }
        } else {
          cnivo = nivoidxs[i]+":"+hr.restart.db.raVariant.getDataSetValue(radpl,nivoidxs[i]);
        }
        cond = (cond==null?"":cond.toString().concat(" OR "))
            + (inquery!=null?Condition.in("CNIVO",inquery).toString():Condition.equal("CNIVO",cnivo).toString());
      }
    }
    if (cond == null) return null;
    QueryDataSet inipr = Iniprim.getDataModule().getFilteredDataSet(cond+" AND "+Condition.equal("AKTIV","D"));
System.out.println("inipr.query = "+inipr.getQuery().getQueryString());
    inipr.open();
    if (inipr.getRowCount() == 0) return null;
    return inipr;
  }
  public boolean savePRIMANJAOBR(String corg) throws Exception {
    qds = Util.getNewQueryDataSet(sjQuerys.selectRadniciPl(corg,""));
    dm.getIniprim().open();
    if (dm.getIniprim().getRowCount() == 0) return true;
    dm.getPrimanjaobr().open();
    
    raCalcPrimanja.getRaCalcPrimanja().clearCalcSets();
    raCalcPrimanja.getRaCalcPrimanja().setFrmPrimanja(null);//za svaki chajslu
    //qds.first();
    //do {
    for (qds.first(); qds.inBounds(); qds.next()) {
      
//      ld.raLocate(dm.getRadnicipl(),new String[] {"CRADNIK"},new String[] {qds.getString("CRADNIK")});
      QueryDataSet qiniprim = getIniprim(qds);
      if (qiniprim != null) {
        //qiniprim.first();
        //do {
        for (qiniprim.first(); qiniprim.inBounds(); qiniprim.next()) {
          java.math.BigDecimal sat=getSatiPrim(qiniprim);
          java.math.BigDecimal nulj=new java.math.BigDecimal(0);
  //        if (sat.compareTo(nulj)>0 || dm.getIniprim().getBigDecimal("IZNOS").compareTo(nulj)>0) {
          if (sat.compareTo(nulj)==0 && (qiniprim.getString("SFOND").equals("R") || qiniprim.getString("SFOND").equals("P"))) {
            //nemoj nish raditi
            System.out.println("ne dodajem iniprim za "+qds.getString("CRADNIK"));
            System.out.println("sat = "+sat);
            System.out.println("qiniprim.getString(SFOND).equals(R) "+qiniprim.getString("SFOND").equals("R"));
            System.out.println("qiniprim.getString(SFOND).equals(P) "+qiniprim.getString("SFOND").equals("P"));
          }
          else {
            dm.getPrimanjaobr().insertRow(true);
            dm.getPrimanjaobr().setString("CRADNIK", qds.getString("CRADNIK"));
            dm.getPrimanjaobr().setShort("CVRP", qiniprim.getShort("CVRP"));
            dm.getPrimanjaobr().setShort("RBR", qiniprim.getShort("RBR"));
            dm.getPrimanjaobr().setString("CORG", qds.getString("CORG"));
            dm.getPrimanjaobr().setBigDecimal("SATI", sat);
            dm.getPrimanjaobr().setBigDecimal("KOEF",
                                              qiniprim.
                                              getBigDecimal("KOEF"));
            dm.getPrimanjaobr().setBigDecimal("BRUTO",
                                              qiniprim.
                                              getBigDecimal("IZNOS"));
            //dm.getPrimanjaobr().post();
            raCalcPrimanja.getRaCalcPrimanja().addCalcSet(qds, "radnicipl"); //novo iz bakine kuhinje!!!
            raCalcPrimanja.getRaCalcPrimanja().calcPrimanje(dm.getPrimanjaobr(), false); // da ne racuna sume jer ih ima sve vise i vise
          }
        }// while (qiniprim.next());
      } //qiniprim != null
    } //while (qds.next());
    raTransaction.saveChanges(dm.getPrimanjaobr());
    raCalcPrimanja.getRaCalcPrimanja().clearCalcSets();
    raCalcPrimanja.getRaCalcPrimanja().setFrmPrimanja(null);//za svaki chajslu
    raCalcPrimanja.getRaCalcPrimanja().calcPrimanja(qds,null,true);
    raCalcPrimanja.getRaCalcPrimanja().clearCalcSets();
    return true;
  }

  private void mySaveORGPL(com.borland.dx.dataset.DataSet ds) {
    dm.getOrgpl().setShort("GODOBR", ds.getShort("GODINA"));
    dm.getOrgpl().setShort("MJOBR", ds.getShort("MJESEC"));
    dm.getOrgpl().setShort("RBROBR", ds.getShort("RBR"));
    dm.getOrgpl().setTimestamp("DATUMISPL", ds.getTimestamp("DATUM"));
    dm.getOrgpl().setShort("BROJDANA", ds.getShort("DANA"));
    raParam.setParam(dm.getOrgpl(), raParam.ORGPL_STATUS, "I");
  }
  
  public boolean iniciranje(final com.borland.dx.dataset.DataSet ds, String[] _nivoidxs) {
    nivoidxs = _nivoidxs;
    return iniciranje(ds, nivoidxs != null);
  }
  private boolean iniciranje(final com.borland.dx.dataset.DataSet ds, final boolean iniPrim) {
    //ojFor
    if (!copyFromOJFor(ds)) {
      return false;
    }
    
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          //minuli rad
          Timestamp d1, d2;
          Calendar cal = Calendar.getInstance();
          cal.set(Calendar.YEAR,ds.getShort("GODINA"));
          cal.set(Calendar.MONTH,ds.getShort("MJESEC")-1);
          cal.set(Calendar.DATE, 1);
          d1 = new Timestamp(cal.getTime().getTime());
          
          cal.set(Calendar.MONTH,ds.getShort("MJESEC"));
          cal.set(Calendar.DATE, 0);
          d2 = new Timestamp(cal.getTime().getTime());
/*
          String mrstr = "update radnicipl " +
          "set datstaz=datstaz+365,GODSTAZ=GODSTAZ+1,STOPASTAZ=STOPASTAZ+0.5 WHERE " +
          Condition.between("datstaz", d1, d2) +
          //" datstaz between '"+d1+"' and '"+d2+"'" +
          " and radnicipl.aktiv='D'";
          PreparedStatement mrcalc = raTransaction.getPreparedStatement(mrstr);
          System.out.println(mrstr);
          mrcalc.executeUpdate();
*/
          QueryDataSet stazeri = Radnicipl.getDataModule().getTempSet(
              Condition.between("datstaz", d1, d2).and(Condition.equal("aktiv", "D")));
          stazeri.open();
          if (stazeri.getRowCount() > 0) {
            for (stazeri.first(); stazeri.inBounds(); stazeri.next()) {
              stazeri.setTimestamp("DATSTAZ", 
                  Util.getUtil().addYears(stazeri.getTimestamp("DATSTAZ"), 1));
              stazeri.setShort("GODSTAZ", (short)(stazeri.getShort("GODSTAZ")+1));
              stazeri.setBigDecimal("STOPASTAZ",stazeri.getBigDecimal("STOPASTAZ")
                  .add(new BigDecimal("0.5")));
              stazeri.post();
            }
            raTransaction.saveChanges(stazeri);
          }
          boolean b = initObr(ds, iniPrim);
          raTransaction.saveChanges(dm.getPrimanjaobr());
          raTransaction.saveChanges(dm.getOrgpl());
          if (brisinule ) {
            Valid.getValid().runSQL("DELETE from primanjaobr where sati = 0 and bruto = 0");
          }
          return b;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw ex;
        }

      }
    };
    return trans.execTransaction();
  }
  
  protected static boolean deleteFromOJFor() {
    String oj4 = frmIniciranje.getOJFor();
    if ("".equals(oj4)) return true;
    raLocalTransaction trans = raTransaction.getLocalTransaction(new String[] {
        "DELETE FROM odbici WHERE EXISTS (select * from vrsteodb " +
            "WHERE odbici.cvrodb = vrsteodb.cvrodb and nivoodb like 'RA%') AND " +
            "TRIM(ckey) like '%@"+oj4+"'",
         "DELETE from radnicipl WHERE TRIM(cradnik) like '%@"+oj4+"'",
         "DELETE from radnici WHERE TRIM(cradnik) like '%@"+oj4+"'"
    });
    return trans.execTransaction();
  }
  protected boolean copyFromOJFor(com.borland.dx.dataset.DataSet
      ds) {
    // kopiraj radnici, radnicipl, odbici sa getOJFor
    String oj4 = frmIniciranje.getOJFor();
    if ("".equals(oj4)) return true;
    QueryDataSet rpl = Radnicipl.getDataModule().getTempSet(
          Condition.in("CORG", OrgStr.getOrgStr().getOrgstrAndKnjig(oj4))
          .and(Condition.equal("AKTIV", "D"))
        );
    return cpOFR(rpl, ds.getString("CORG"), oj4);
    
  }
  public static boolean cpOFR(String cradnik, String oj, String oj4) {
    QueryDataSet rpl = Radnicipl.getDataModule().getTempSet(Condition.equal("CRADNIK", cradnik));
    return cpOFR(rpl, oj, oj4);
  }
  public static boolean cpOFR(QueryDataSet rpl, String oj, String oj4) {
    rpl.open();
    QueryDataSet erpl = Radnicipl.getDataModule().getTempSet(Condition.nil);
    QueryDataSet erad = Radnici.getDataModule().getTempSet(Condition.nil);
    QueryDataSet eodb = Odbici.getDataModule().getTempSet(Condition.nil);
    erpl.open(); erad.open(); eodb.open();
    for (rpl.first(); rpl.inBounds(); rpl.next()) {
      String orgcrad = rpl.getString("CRADNIK");
      String newcrad = rpl.getString("CRADNIK")+"@"+oj4;
      //radnicipl
      erpl.insertRow(false);
      rpl.copyTo(erpl);
      erpl.setString("CRADNIK", newcrad);
      erpl.setString("PARAMETRI", orgcrad);
      erpl.setString("CORG", oj);
      erpl.post();
      //radnici
      QueryDataSet radnik = Radnici.getDataModule().getTempSet(Condition.equal("CRADNIK", orgcrad));
      radnik.open();
      erad.insertRow(false);
      radnik.copyTo(erad);
      erad.setString("CRADNIK", newcrad);
      erad.setString("CORG", oj);
      erad.post();
      //odbici
      QueryDataSet odbici = Aus.q("SELECT * FROM Odbici where ckey='"+orgcrad+"' and EXISTS (select * from vrsteodb " +
          "WHERE odbici.cvrodb = vrsteodb.cvrodb and nivoodb like 'RA%')");
      odbici.open();
      for (odbici.first(); odbici.inBounds(); odbici.next()) {
        eodb.insertRow(false);
        odbici.copyTo(eodb);
        eodb.setString("ckey", newcrad);
        odbici.post();
      }
    }
    return raTransaction.saveChangesInTransaction(new QueryDataSet[] {erpl,erad,eodb});
  }
  public boolean posOrgsPl(String corg) {
    if (!ld.raLocate(dm.getOrgpl(), new String[] {"CORG"}, new String[] {corg})) {
      ld.raLocate(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), new String[] {"CORG"}, new String[] {corg});
      String prip = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig().getString("PRIPADNOST");
      if (corg.equals(prip)) return false;
      posOrgsPl(prip);
    }
    return true;
  }
  private boolean initObr(com.borland.dx.dataset.DataSet ds, boolean iniPrim) throws Exception {
    fondsati = Util.getNewQueryDataSet("SELECT * FROM fondsati where KNJIG = '"+
                                       hr.restart.zapod.OrgStr.getKNJCORG()+"' AND GODINA = "+
                                       ds.getShort("GODINA")+" AND MJESEC = "
                                       +ds.getShort("MJESEC"));

    qds = Util.getNewQueryDataSet(sjQuerys.selectOrgStr(ds.getString("CORG")));
    qds.first();
    do {
      posOrgsPl(qds.getString("CORG"));
      mySaveORGPL(ds);
    } while(qds.next());
    if (iniPrim) savePRIMANJAOBR(ds.getString("CORG"));
    return true;
  }

  public boolean ponistavanje(String ds) {
    sjUtil.getSjUtil().delIniciranje(ds);
    deleteFromOJFor();
    return true;
  }
}