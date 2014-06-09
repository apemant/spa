/****license*****************************************************************
**   file: raFLH.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Doku;
import hr.restart.baza.Meskla;
import hr.restart.baza.Stdoku;
import hr.restart.baza.Stmeskla;
import hr.restart.baza.VezaFLH;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raFLH {
  
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
    
  public static final raFLH inst = new raFLH();
  
  private raFLH() {    
  }
  
  public static raFLH getInstance() {
    return inst;
  }
  
  //public static void razdu
  
  public boolean isFLH(QueryDataSet st, boolean ulaz) {
    String vrdok = st.getString("VRDOK");
    String cskl = st.hasColumn("CSKL") != null ? st.getString("CSKL") : ulaz ? st.getString("CSKLUL") : st.getString("CSKLIZ");
    if (!TypeDoc.getTypeDoc().isDocSklad(vrdok)  || !TypeDoc.getTypeDoc().isDocDiraZalihu(vrdok)) return false;
    
    if (!ld.raLocate(dm.getSklad(), "CSKL", cskl)) {
      System.out.println("ERROR! Cskl=" + cskl);
      return false;
    }
    
    if (!dm.getSklad().getString("VRZAL").equals("F") &&
        !dm.getSklad().getString("VRZAL").equals("L") &&
        !dm.getSklad().getString("VRZAL").equals("H")) return false;
    
    
    return true;
  }
  
  public boolean provjeraFLH(QueryDataSet st) {
    if (!isFLH(st, true)) return false;
    
    if (st.getBigDecimal("KOL").compareTo(st.getBigDecimal("KOLFLH")) != 0) return true;
    
    QueryDataSet links = VezaFLH.getDataModule().getTempSet(Condition.equal("IDULAZ", st, "ID_STAVKA"));
    links.open();
    
    return links.rowCount() != 0;
  }
  
  public void ulazNoviFLH(QueryDataSet st) {
    if (!isFLH(st, true)) return;
        
    if (st.getBigDecimal("KOL").signum() > 0)
      Aus.set(st, "KOLFLH", "KOL");
  }
  
  private String getKey(DataSet st) {
    if (st.hasColumn("CSKL") != null)
      return st.getString("CSKL") + "-" + st.getString("VRDOK") + "-" + 
              st.getString("GOD") + "-" + st.getInt("BRDOK");
    
    return st.getString("CSKLIZ") + "-" + st.getString("CSKLUL") + "-" + 
              st.getString("VRDOK") + "-" + st.getString("GOD") + "-" + st.getInt("BRDOK");
  }
  
  private Map findZag(DataSet st) {
    DataSet zul = Doku.getDataModule().getTempSet("CSKL VRDOK GOD BRDOK DATDOK", 
        Condition.whereAllEqual(new String[] {"CSKL", "GOD"}, st));
    zul.open();
    
    DataSet zmul = Meskla.getDataModule().getTempSet("CSKLUL CSKLIZ VRDOK GOD BRDOK DATDOK", 
        Condition.equal("GOD", st).and(Condition.equal("CSKLUL", st, "CSKL")));
    zmul.open();
    
    Map ret = new HashMap();
    
    for (zul.first(); zul.inBounds(); zul.next())
      ret.put(getKey(zul), new Long(zul.getTimestamp("DATDOK").getTime()));
      
    for (zmul.first(); zmul.inBounds(); zmul.next())
      ret.put(getKey(zmul), new Long(zmul.getTimestamp("DATDOK").getTime()));
    
    return ret;
    
  }
  
  private String findBest(DataSet st, Map zag) {
    BigDecimal ncbest = null;
    Long timbest = null;
    String idbest = null;
    
    String vrzal = dm.getSklad().getString("VRZAL");
    
    for (st.first(); st.inBounds(); st.next()) {
      if (st.getBigDecimal("KOLFLH").signum() <= 0) continue;
      if (idbest == null 
          || (vrzal.equals("F") && timbest.compareTo((Long) zag.get(getKey(st))) > 0)
          || (vrzal.equals("L") && timbest.compareTo((Long) zag.get(getKey(st))) < 0)
          || (vrzal.equals("H") && ncbest.compareTo(st.getBigDecimal("NC")) < 0)) {
        idbest = st.getString("ID_STAVKA");
        ncbest = st.getBigDecimal("NC");
        timbest = (Long) zag.get(getKey(st));
      }
    }
    
    if (idbest == null || !ld.raLocate(st, "ID_STAVKA", idbest)) return null;
    
    return idbest;
  }
  
  public void izlazBrisanjeFLH(QueryDataSet st, String id) {
    if (!isFLH(st, false)) return;
    
    deleteLinks(id);
  }
  
  public void izlazIzmjenaFLH(QueryDataSet st) {
    if (!isFLH(st, false)) return;
    
    deleteLinks(st.getString("ID_STAVKA"));
    
    addLinks(st);
  }
  
  public void izlazNoviFLH(QueryDataSet st) {
    if (!isFLH(st, false)) return;
    
    addLinks(st);
  }
  
  
  private void deleteLinks(String id) {
    QueryDataSet links = VezaFLH.getDataModule().getTempSet(Condition.equal("IDIZLAZ", id));
    links.open();
    
    QueryDataSet ul = Stdoku.getDataModule().getTempSet(Condition.in("ID_STAVKA", links, "IDULAZ"));
    ul.open();
    
    QueryDataSet mul = Stmeskla.getDataModule().getTempSet(Condition.in("ID_STAVKA", links, "IDULAZ"));
    mul.open();
    
    for (links.first(); links.inBounds(); links.next()) {
      if (ld.raLocate(ul, "ID_STAVKA", links.getString("IDULAZ"))) {
        Aus.add(ul, "KOLFLH", links, "KOL");
      } else if (ld.raLocate(mul, "ID_STAVKA", links.getString("IDULAZ"))) {
        Aus.add(mul, "KOLFLH", links, "KOL");
      } else {
        System.out.println("FLH ERROR! Nema stavke za izmjenu");
        System.out.println(id);
      }
    }
    links.deleteAllRows();
    
    raTransaction.saveChanges(ul);
    raTransaction.saveChanges(mul);
    raTransaction.saveChanges(links);
  }
  
  private void addLinks(QueryDataSet st) {
    BigDecimal kol = st.getBigDecimal("KOL");
    if (kol.signum() <= 0) {
      System.out.println("FLH ERROR! KOL manje od nula");
      System.out.println(st);
      return;
    }
    
    String vrzal = dm.getSklad().getString("VRZAL");
    
    QueryDataSet ul = Stdoku.getDataModule().getTempSet("CSKL VRDOK GOD BRDOK RBR CART NC KOL KOLFLH ID_STAVKA", 
        Condition.whereAllEqual(new String[] {"CSKL", "GOD", "CART"}, st).and(Condition.where("KOLFLH", Condition.GREATER_THAN, 0)));
    ul.open();
    
    QueryDataSet mul = Stmeskla.getDataModule().getTempSet("CSKLUL CSKLIZ VRDOK GOD BRDOK RBR CART NC KOL KOLFLH ID_STAVKA", 
        Condition.whereAllEqual(new String[] {"GOD", "CART"}, st).and(Condition.where("KOLFLH", Condition.GREATER_THAN, 0)));
    mul.open();
    
    Map zag = findZag(st);
    
    QueryDataSet links = VezaFLH.getDataModule().getTempSet("1=0");
    links.open();
    
    BigDecimal inab = Aus.zero0;
    
    while (kol.signum() > 0) {
      String ulbest = findBest(ul, zag);
      String mulbest = findBest(mul, zag);
      
      if (ulbest == null && mulbest == null) {
        System.out.println("FLH ERROR! Nema stavke ulaza za povezat");
        System.out.println(st);
        return;
      }
      
      QueryDataSet best = null;
      if (mulbest == null) best = ul;
      else if (ulbest == null) best = mul;
      else if ((vrzal.equals("F") && ((Long) zag.get(getKey(ul))).compareTo((Long) zag.get(getKey(mul))) <= 0)
          || (vrzal.equals("L") && ((Long) zag.get(getKey(ul))).compareTo((Long) zag.get(getKey(mul))) >= 0)
          || (vrzal.equals("H") && (ul.getBigDecimal("NC").compareTo(mul.getBigDecimal("NC")) >= 0))) best = ul;
      else best = mul;
      
      BigDecimal kv = kol.min(best.getBigDecimal("KOLFLH"));
      
      links.insertRow(false);
      links.setString("IDULAZ", best.getString("ID_STAVKA"));
      links.setString("IDIZLAZ", st.getString("ID_STAVKA"));
      links.setBigDecimal("NC", best.getBigDecimal("NC"));
      links.setBigDecimal("KOL", kv);
      
      inab = inab.add(best.getBigDecimal("NC").multiply(kv).setScale(2, BigDecimal.ROUND_HALF_UP));
      
      Aus.sub(best, "KOLFLH", kv);
      kol = kol.subtract(kv);
    }
    
    QueryDataSet ast = allStanje.getallStanje().gettrenSTANJE();
    
    Aus.sub(ast, "NABIZ", st, "INAB");
    Aus.sub(ast, "VIZ", st, "IRAZ");
    
    Aus.set(st, "INAB", inab);
    Aus.set(st, "IRAZ", inab);
    Aus.div(st, "NC", "INAB", "KOL");
    Aus.div(st, "ZC", "IRAZ", "KOL");
    
    Aus.add(ast, "NABIZ", st, "INAB");
    Aus.add(ast, "VIZ", st, "IRAZ");
    Aus.sub(ast, "VRI", "VUL", "VIZ");
    
    raTransaction.saveChanges(links);
    raTransaction.saveChanges(ast);
    raTransaction.saveChanges(st);
    raTransaction.saveChanges(ul);
    raTransaction.saveChanges(mul);
  }
 
}
