/****license*****************************************************************
**   file: raCalcPrimanja.java
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

import hr.restart.baza.dM;
import hr.restart.db.raConnectionFactory;
import hr.restart.db.raVariant;
import hr.restart.util.MathEvaluator;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Klasa za obracun iznosa (bruta) primanja
 */

public class raCalcPrimanja {
  private sysoutTEST st = new sysoutTEST(false);
  private static raCalcPrimanja calcprim;
  private StorageDataSet sume;
  private StorageDataSet radnicipl;
  private StorageDataSet primanjaobr;
  private QueryDataSet qvrsteprim;
  private QueryDataSet qnacobr;
  private Util ut;
  private Valid vl;
  private dM dm;
  private lookupData ld;
  private frmPrimanja fPrimanja;// = null;
  private Hashtable calcSets = new Hashtable();

  protected raCalcPrimanja() {
    ut = Util.getUtil();
    vl = Valid.getValid();
    dm = dM.getDataModule();
    ld = lookupData.getlookupData();
    qvrsteprim = Util.getNewQueryDataSet("SELECT * FROM vrsteprim");
    qnacobr = Util.getNewQueryDataSet("SELECT * FROM nacobr");
    createSums();
  }
  /**
   * staticki getter
   * @return instancu raCalcPrimanja
   */
  public static raCalcPrimanja getRaCalcPrimanja() {
    if (calcprim == null) calcprim = new raCalcPrimanja();
    calcprim.initCalcSets();
    return calcprim;
  }
  /**
   * 
   */
  private void initCalcSets() {
    //FOND SATI
    dm.getFondSati().open();
    raIniciranje.getInstance().posOrgsPl(OrgStr.getKNJCORG());
    ld.raLocate(dm.getFondSati(),new String[] {"KNJIG","GODINA","MJESEC"},
        new String[] {OrgStr.getKNJCORG(), dm.getOrgpl().getShort("GODOBR")+"", dm.getOrgpl().getShort("MJOBR")+""});
    addCalcSet(dm.getFondSati());
    //...
    
  }
  private void createSums() {
    /*
    * kopira tabelu plsume i doda joj jos jedno bigdecimal polje
    * te jedno Object polje u koje se spremiti java.util.HashSet sa CVRP-ovima
    * koji ulaze u tu sumu
    */
    System.out.println("Create sume");
    sume = dm.getSume().cloneDataSetStructure();
    Column cIznos = (Column)dm.getPrimanjaobr().getColumn("BRUTO").clone();
    Column cCVRPS = new Column("CVRPS","Vrste primanja",Variant.OBJECT);
    cIznos.setColumnName("IZSUME");
    sume.addColumn(cIznos);
    sume.addColumn(cCVRPS);
    String[] copycols = new String[] {"CSUME","OPIS","VRSTA","PARAMETRI"};
    BigDecimal nula = new BigDecimal(0);
    dm.getSume().open();
    dm.getSume().first();
    sume.open();
    do {
      sume.insertRow(false);
      QueryDataSet.copyTo(copycols,dm.getSume(),copycols,sume);
      sume.setBigDecimal("IZSUME",nula);
      sume.setObject("CVRPS",getCVRPS(sume.getInt("CSUME")));
      sume.post();
    } while (dm.getSume().next());
  }
  private HashSet getCVRPS(int csume) {
    HashSet cvrps = new HashSet();
    vl.execSQL("SELECT CVRP FROM SUMEPRIM WHERE CSUME = "+csume);
    vl.RezSet.open();
    vl.RezSet.first();
    do {
      cvrps.add(new Short(vl.RezSet.getShort("CVRP")));
    } while (vl.RezSet.next());
    return cvrps;
  }
  boolean isSuma(short cvrp) {
    //vraca da li u current sumu ulazi u zadana vrsta primanja
    Short Icvrp = new Short(cvrp);
    return ((HashSet)sume.getObject("CVRPS")).contains(Icvrp);
  }
/**
 * Poziva rekalkulaciju svih primanja za selekciju u rfmPrimanja
 * ide kroz masterset
 * @param frmP
 * @return jel uspio ili nije
 */
  public boolean calcPrimanja(frmPrimanja frmP) {
    fPrimanja = null;
    frmP.raDetail.getJpTableView().enableEvents(false);
    radnicipl = frmP.getMasterSet();
//    primanjaobr = fPrimanja.getDetailSet();
    primanjaobr = null;
    qvrsteprim.refresh();
    qnacobr.refresh();
    addCalcSet(radnicipl,"radnicipl");
    calcPrimanjaRadnik(radnicipl.getString("CRADNIK")); // izracunava primanja za jednog radnika
    calcPrimanjaRadnik(null); // izracunava primanja za istog radnika
    primanjaobr.saveChanges();
    frmP.getDetailSet().refresh();
    frmP.raDetail.getJpTableView().enableEvents(true);
    return true;
  }

  public boolean calcPrimanja(StorageDataSet _radnici, StorageDataSet _primanja, boolean trans) {
    try {
      radnicipl = _radnici;
      primanjaobr = _primanja;
      qvrsteprim.refresh();
      qnacobr.refresh();
      addCalcSet(radnicipl,"radnicipl");
      fPrimanja = null;
      radnicipl.first();
      do {
        calcPrimanjaRadnik(radnicipl.getString("CRADNIK")); // izracunava primanja za jednog radnika
        calcPrimanjaRadnik(null); // izracunava primanja za istog radnika
        if (trans && primanjaobr instanceof QueryDataSet) raTransaction.saveChanges((QueryDataSet)primanjaobr);
        else primanjaobr.saveChanges();
      } while (radnicipl.next());
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
  void clearSume() {
    BigDecimal nula = new BigDecimal(0);
    sume.first();
    do {
      sume.setBigDecimal("IZSUME",nula);
    } while (sume.next());
//    st.prn(sume);
  }
  void calcSume() {
    for (int i = 0; i < primanjaobr.getRowCount(); i++) {
      Variant v = new Variant();
      Variant key = new Variant();
      primanjaobr.getVariant("CVRP",i,key);
      sume.first();
      do {
        if (isSuma(key.getShort())) {
          String cname = sume.getString("VRSTA").equals("1")?"BRUTO":"SATI";
          primanjaobr.getVariant(cname,i,v);
//System.out.println("Radnik "+primanjaobr.getString("CRADNIK")+": zbrajam cvrp "+key.toString()+" "+cname+" u sumu "+sume.getInt("CSUME")+" +"+v.getBigDecimal());
          sume.setBigDecimal("IZSUME",v.getBigDecimal().add(sume.getBigDecimal("IZSUME")));
        }
      } while (sume.next());
    }
//    st.prn(sume);
  }

  public void setFrmPrimanja(frmPrimanja _frmp) {
    fPrimanja = _frmp;
  }

  void calcPrimanjaRadnik(String cradnik) {
    if (cradnik != null) {
      primanjaobr = getPrimanjaFromFPrimanja(cradnik);
      if (primanjaobr == null) primanjaobr = getPrimanjaFromCradnik(cradnik);
    }
    if (primanjaobr == null) return;
    primanjaobr.first();
    do {
      calcPrimanje(primanjaobr);
    } while (primanjaobr.next());
  }
  public void calcPrimanje(StorageDataSet prim) {
    calcPrimanje(prim,true);
  }
  public void calcPrimanje(StorageDataSet prim, boolean calcsums) {
    System.out.println("prim = "+prim);
//    System.out.println("calcprimanje");
    try {
//      System.out.println("calcPrimanje, radnik: "+prim.getString("CRADNIK"));
      primanjaobr = prim;
      String formula = getFormula(prim.getShort("CVRP"));
      formula = parseFormula(formula,calcsums);
      MathEvaluator me = new MathEvaluator(formula);
      prim.setBigDecimal("BRUTO",Util.getUtil().setScale(new BigDecimal(me.getValue().doubleValue()),2));
//      System.out.println("Radnik: "+prim.getString("CRADNIK"+" , bruto: "+prim.getBigDecimal("BRUTO")));
    }
    catch (Exception ex) {
      System.out.println("calcPrimanje(StorageDataSet prim) ex: "+ex);
      ex.printStackTrace();
    }
  }

  String getFormula(int cvrp) {
    String scvrp = new Integer(cvrp).toString();
    if (!ld.raLocate(qvrsteprim,new String[] {"CVRP"},new String[] {scvrp})) return "";
    String scobr = new Integer(qvrsteprim.getShort("COBR")).toString();
    if (!ld.raLocate(qnacobr,new String[] {"COBR"},new String[] {scobr})) return "";
    return qnacobr.getString("FORMULA");
  }
  String parseFormula(String fja,boolean calcsums) {
//    System.out.println("fja1: "+fja);
    fja = parseValues(fja);
//    System.out.println("fja2: "+fja);
    if (calcsums) fja = parseSums(fja);
    System.out.println("fja3: "+fja);
    fja = parseOsns(fja);
    return fja;
  }
  public String parseValues(String fja) {
    if (fja.equals("")) return fja;
    StringTokenizer token = new StringTokenizer(fja,".");
    String retFja = new String(fja);
    String tableName = null;
    String columnName = null;
    String tok = null;
    try {
      tok = token.nextToken();
    }
    catch (Exception ex) {
      return fja;
    }
    do {
      if (tableName == null) {
        int s1 = tok.lastIndexOf("<");
        if (s1>=0) tableName = tok.substring(s1+1);
      } else if (columnName == null) {
        int s1 = tok.indexOf(">");
        if (s1>=0) columnName = tok.substring(0,s1);
      }
      if (columnName != null && tableName != null) {
        String numVal = getNumValue(tableName,columnName);
        if (numVal!=null) {
          retFja = replaceStr(retFja,"<".concat(tableName).concat(".").concat(columnName).concat(">"),numVal);
        }
        columnName = null;
        tableName = null;
      } else {
        if (token.hasMoreTokens()) {
          tok = token.nextToken();
        } else {
          break;
        }
      }
    } while (true);
    return retFja;
  }
  private String parseSums(String fja) {
    String tok=null;
    StringTokenizer token = new StringTokenizer(fja,"[");
    String retFja = new String(fja);
    if (token.hasMoreTokens()) {
      clearSume();
      calcSume();
    }
    while (token.hasMoreTokens()) {
      tok = token.nextToken();
      if (tok.indexOf("]") > -1) {
        String shuma=tok.substring(0, tok.indexOf("]"));
        hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
        if (ld.raLocate(this.sume, new String[] {"CSUME"}, new String[] {shuma})) {
          retFja=replaceStr(retFja, "["+shuma+"]", sume.getBigDecimal("IZSUME").toString());
        }
      }
    }
    return retFja;
  }
  private String parseOsns(String fja) {
    return fja;
  }
  private String replaceStr(String which, String what, String with) {
    int sub1 = which.indexOf(what);
    if (sub1<0) return which;
    int sub2 = what.length()+sub1;
    String s1 = which.substring(0,sub1);
    String s2 = which.substring(sub2);
    return s1.concat(with).concat(s2);
  }

  public void addCalcSet(DataSet ds, String tableName) {
    if (tableName.equals("radnicipl") && radnicipl == null) {
      try {
        radnicipl = (StorageDataSet)ds;
      } catch (Exception e) {
      }
    }
    calcSets.put(tableName.toLowerCase(),ds);
  }

  public void addCalcSet(QueryDataSet ds) {
    if (ds == null) return;
    String tableName = Valid.getTableName(ds.getQuery().getQueryString()).toLowerCase();
    if (tableName == null) return;
    addCalcSet(ds,tableName);
  }
  public void clearCalcSets() {
    calcSets.clear();
    primanjaobr = null;
    radnicipl = null;
  }
  private String getNumValue(String table,String column) {
    DataSet dset = null;
    java.math.BigDecimal bd = null;
    String columng = column.toUpperCase();
    if (table.toLowerCase().equals("primanjaobr")) dset = primanjaobr;
//    if (table.toLowerCase().equals("radnicipl")) dset = radnicipl;
    if (dset == null) {
      dset = (DataSet)calcSets.get(table.toLowerCase());
    }
    if (dset == null) {//i dalje
      String tableg = "get".concat(table.substring(0,1).toUpperCase().concat(table.substring(1,table.length())));
      //a little reflection
      try {
        java.lang.reflect.Method dsGetter = dm.getClass().getMethod(tableg,null);
        dset = (DataSet)dsGetter.invoke(dm,null);
        dset.open();
      }
      catch (Exception ex) {
        System.out.println("ex = "+tableg+"."+columng);
        ex.printStackTrace();
        //bd = null;
      }
    }
    locateSetForCalc(dset);
    bd = dset.getBigDecimal(columng);
    if (bd == null) return null;
    return bd.toString();
  }
  /**
   * @param dset
   */
  private void locateSetForCalc(DataSet dset) {
    try {
      if (dset instanceof QueryDataSet) {
        QueryDataSet qdset = (QueryDataSet)dset; 
        String[] dsetKeys = raConnectionFactory.getKeyColumns(
            qdset.getDatabase().getJdbcConnection(), Valid.getTableName(qdset.getQuery().getQueryString()));
        String[] radVals = new String[dsetKeys.length]; 
        for (int i = 0; i < dsetKeys.length; i++) {
          if (radnicipl.hasColumn(dsetKeys[i])==null) {
            return;
          } else {
            radVals[i] = raVariant.getDataSetValue(radnicipl,dsetKeys[i]).toString();
          }
        }
        ld.raLocate(dset,dsetKeys,radVals);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private QueryDataSet getPrimanjaFromCradnik(String cradnik) {
    if (cradnik == null) return null;
System.out.println("returning new primanjaobr for "+cradnik);
    return Util.getNewQueryDataSet("SELECT * FROM primanjaobr WHERE cradnik = '"+cradnik+"'");
  }

  private QueryDataSet getPrimanjaFromFPrimanja(String cradnik) {
    if (fPrimanja == null) return null;
    fPrimanja.getDetailSet().first();
    String crad1st = fPrimanja.getDetailSet().getString("CRADNIK");
    fPrimanja.getDetailSet().last();
    String cradlast = fPrimanja.getDetailSet().getString("CRADNIK");
    if (crad1st.equals(cradnik) && cradlast.equals(cradnik)) return fPrimanja.getDetailSet();
    return null;
  }
  ///////////////////////
  // TEST
  ///////////////////////
//  public static void main(String[] args) {
//    raCalcPrimanja rcp = raCalcPrimanja.getRaCalcPrimanja();
//    rcp.createSums();
//    sysoutTEST ST = new sysoutTEST(false);
//      prn(rcp.sume);
//    rcp.sume.first();
//    do {
//      System.out.println("jel u sumu "+rcp.sume.getInt("CSUME")+" ide CVRP = 2 ...."+rcp.isSuma(2));
//    } while (rcp.sume.next());
//
//  }
}