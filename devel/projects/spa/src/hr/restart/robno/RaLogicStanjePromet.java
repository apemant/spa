/****license*****************************************************************
**   file: RaLogicStanjePromet.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raGlob;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class RaLogicStanjePromet {
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private dM dm = dM.getDataModule();
  private lookupData ld = lookupData.getlookupData();
  private boolean cached = false;
  private String eskls = null;
  private QueryDataSet povratImovine = null;
  private QueryDataSet povratTmp = null;
  
  public QueryDataSet datasetZaEkran(String cskl, String cart, String grupa, String cartFiltering, boolean kol0, Timestamp dodat, boolean podgrupe, String partArtikl, String partCart1, boolean all) {
    cached = false;
    QueryDataSet ulaz = getUlazData(makeCondition("DOKU","STDOKU","CSKL",cskl,cart,dodat));
    QueryDataSet por = getPorData(makeCondition("DOKU","STDOKU","CSKL",cskl,cart,dodat));
    QueryDataSet porUlaz = getPorUlazData(makeCondition("DOKU","STDOKU","CSKL",cskl,cart,dodat));
    QueryDataSet izlaz = getIzlazData(makeCondition("DOKI","STDOKI","CSKL",cskl,cart,dodat));
    QueryDataSet mesMeu = getMeuData(makeCondition("MESKLA","STMESKLA","CSKLUL",cskl,cart,dodat));
    QueryDataSet mesMei = getMeiData(makeCondition("MESKLA","STMESKLA","CSKLIZ",cskl,cart,dodat));
    QueryDataSet mesMeuPor = getMesPorData(makeCondition("MESKLA","STMESKLA","CSKLUL",cskl,cart,dodat));
    
    String vrzal = "N";
    if (lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", cskl))
      vrzal = dm.getSklad().getString("VRZAL");
    
    boolean mpc = vrzal.equals("M");
    
    povratImovine = new QueryDataSet();
    
    povratImovine.setMetaDataUpdate(MetaDataUpdate.TABLENAME + MetaDataUpdate.PRECISION + MetaDataUpdate.SCALE + MetaDataUpdate.SEARCHABLE);
    povratImovine.setColumns(new Column[] {
        (Column) dm.getStanje().getColumn("CSKL").clone(),
        (Column) dm.getDoki().getColumn("DATDOK").clone(),
        (Column) dm.getArtikli().getColumn("CART").clone(), 
        (Column) dm.getArtikli().getColumn("CART1").clone(), 
        (Column) dm.getArtikli().getColumn("BC").clone(), 
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(), 
        (Column) dm.getStanje().getColumn("KOL").clone(), 
        (Column) dm.getStanje().getColumn("ZC").clone(), 
        (Column) dm.getStanje().getColumn("NC").clone(), 
        (Column) dm.getStanje().getColumn("VC").clone(), 
        (Column) dm.getStanje().getColumn("MC").clone(), 
        (Column) dm.createBigDecimalColumn("NAB", "Nabavna vrijednost", 2), 
        (Column) dm.createBigDecimalColumn("MAR", "Zarada", 2), 
        (Column) dm.createBigDecimalColumn("POR", "Porez", 2), 
        (Column) dm.getStanje().getColumn("VRI").clone(), 
        (Column) dm.createIntColumn("SIGMIN")}
    );

    povratImovine.setRowId("CSKL",true);
    povratImovine.setRowId("CART",true);
    
    povratImovine.getColumn("DATDOK").setVisible(0);
    
    if ("N".equalsIgnoreCase(frmParam.getParam("robno", "allSif", "N",
      "Prikaz sve tri šifre na stanju artikala (D,N)"))) {
      povratImovine.getColumn("CART").setVisible(0);
      povratImovine.getColumn("CART1").setVisible(0);
      povratImovine.getColumn("BC").setVisible(0);
      povratImovine.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
    }

//    povratImovine.getColumn("NAB").setVisible(0);
    povratImovine.getColumn("MAR").setVisible(0); 
    povratImovine.getColumn("POR").setVisible(0);
    povratImovine.getColumn("SIGMIN").setVisible(0);
    
    povratImovine.open();
    
    povratTmp = new QueryDataSet();
    povratTmp.setColumns(new Column[] {
        (Column) dm.getStanje().getColumn("CSKL").clone(),
        (Column) dm.getDoki().getColumn("DATDOK").clone(),
        (Column) dm.getArtikli().getColumn("CART").clone(), 
        (Column) dm.getArtikli().getColumn("CART1").clone(), 
        (Column) dm.getArtikli().getColumn("BC").clone(), 
        (Column) dm.getArtikli().getColumn("NAZART").clone(), 
        (Column) dm.getArtikli().getColumn("JM").clone(), 
        (Column) dm.getStanje().getColumn("KOL").clone(), 
        (Column) dm.getStanje().getColumn("ZC").clone(), 
        (Column) dm.getStanje().getColumn("NC").clone(), 
        (Column) dm.getStanje().getColumn("VC").clone(), 
        (Column) dm.getStanje().getColumn("MC").clone(), 
        (Column) dm.createBigDecimalColumn("NAB", "Nabavna vrijednost", 2), 
        (Column) dm.createBigDecimalColumn("MAR", "Zarada", 2), 
        (Column) dm.createBigDecimalColumn("POR", "Porez", 2), 
        (Column) dm.getStanje().getColumn("VRI").clone()}
    );
    povratTmp.setRowId("CSKL",true);
    povratTmp.setRowId("DATDOK",true);
    povratTmp.setRowId("CART",true);
    povratTmp.open();
 
    
    if (ulaz != null){
      insertIntoPovratTmpDataSet(ulaz);
    }
    if (por != null){
      insertIntoPovratTmpDataSet(por);
    }
    if (porUlaz != null){
      insertIntoPovratTmpDataSet(porUlaz);
    }
    if (izlaz != null){
      insertIntoPovratTmpDataSet(izlaz);
    }
    if (mesMeu != null){
      insertIntoPovratTmpDataSet(mesMeu);
    }
    if (mesMei != null){
      insertIntoPovratTmpDataSet(mesMei);
    }
    if (mesMeuPor != null){
      insertIntoPovratTmpDataSet(mesMeuPor);
    }
    
    povratTmp.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    povratTmp.first();
    
    DataSet art = Aus.q("SELECT cart,vrart FROM artikli");
    HashSet nost = new HashSet();
    for (art.first(); art.inBounds(); art.next())      
      if (raVart.isUsluga(art) || (!all && !raVart.isStanje(art))) 
        nost.add(new Integer(art.getInt("CART")));
    
    System.out.println(nost);
    
    do {
      if (nost.contains(new Integer(povratTmp.getInt("CART")))) continue;
      if (ld.raLocate(povratImovine,
          new String[] {"CART","CSKL"},
          new String[] {povratTmp.getInt("CART")+"",povratTmp.getString("CSKL")})){
        
        if (povratTmp.getBigDecimal("ZC").signum() > 0)
          povratImovine.setBigDecimal("ZC", povratTmp.getBigDecimal("ZC"));
        if (povratTmp.getBigDecimal("NC").signum() > 0)
          povratImovine.setBigDecimal("NC", povratTmp.getBigDecimal("NC"));
        if (povratTmp.getBigDecimal("VC").signum() > 0)
          povratImovine.setBigDecimal("VC", povratTmp.getBigDecimal("VC"));
        if (povratTmp.getBigDecimal("MC").signum() > 0)
          povratImovine.setBigDecimal("MC", povratTmp.getBigDecimal("MC"));
        povratImovine.setBigDecimal("KOL", povratImovine.getBigDecimal("KOL").add(povratTmp.getBigDecimal("KOL")));
        povratImovine.setBigDecimal("VRI", povratImovine.getBigDecimal("VRI").add(povratTmp.getBigDecimal("VRI")));
        povratImovine.setBigDecimal("NAB", povratImovine.getBigDecimal("NAB").add(povratTmp.getBigDecimal("NAB")));
        povratImovine.setBigDecimal("MAR", povratImovine.getBigDecimal("MAR").add(povratTmp.getBigDecimal("MAR")));
        povratImovine.setBigDecimal("POR", povratImovine.getBigDecimal("POR").add(povratTmp.getBigDecimal("POR")));
      } else {
        povratImovine.insertRow(false);
        povratImovine.setString("CSKL", povratTmp.getString("CSKL"));
        povratImovine.setInt("CART", povratTmp.getInt("CART"));
        povratImovine.setString("CART1", povratTmp.getString("CART1"));
        povratImovine.setString("BC", povratTmp.getString("BC"));
        povratImovine.setString("NAZART", povratTmp.getString("NAZART"));
        povratImovine.setString("JM", povratTmp.getString("JM"));
        povratImovine.setBigDecimal("KOL", povratTmp.getBigDecimal("KOL"));
        povratImovine.setBigDecimal("ZC", povratTmp.getBigDecimal("ZC"));
        povratImovine.setBigDecimal("NC", povratTmp.getBigDecimal("NC"));
        povratImovine.setBigDecimal("VC", povratTmp.getBigDecimal("VC"));
        povratImovine.setBigDecimal("MC", povratTmp.getBigDecimal("MC"));
        povratImovine.setBigDecimal("VRI", povratTmp.getBigDecimal("VRI"));
        povratImovine.setBigDecimal("NAB", povratTmp.getBigDecimal("NAB"));
        povratImovine.setBigDecimal("MAR", povratTmp.getBigDecimal("MAR"));
        povratImovine.setBigDecimal("POR", povratTmp.getBigDecimal("POR"));
      }
    } while (povratTmp.next());
    
    for (povratImovine.first(); povratImovine.next(); povratImovine.inBounds()) {
      if (povratImovine.getBigDecimal("KOL").signum() != 0) {
        Aus.div(povratImovine, "NC", "NAB", "KOL");
        if (!mpc) Aus.div(povratImovine, "ZC", "VRI", "KOL");
      }
    }
    
    if (!kol0){
/*      dm.getStanje().open();
      povratImovine.first();
      do {
        if (!ld.raLocate(dm.getStanje(),new String[]{"CSKL","CART"}, new String[] {povratImovine.getString("CSKL"),povratImovine.getInt("CART")+""})){
          povratImovine.insertRow(false);
          ld.raLocate(dm.getArtikli(), "CART", dm.getStanje().getInt("CART") + "");
          povratImovine.setString("CSKL", dm.getStanje().getString("CSKL"));
          povratImovine.setInt("CART", dm.getStanje().getInt("CART"));
          povratImovine.setString("CART1", dm.getArtikli().getString("CART1"));
          povratImovine.setString("BC", dm.getArtikli().getString("BC"));
          povratImovine.setString("NAZART", dm.getArtikli().getString("NAZART"));
          povratImovine.setString("JM", dm.getArtikli().getString("JM"));
          povratImovine.setBigDecimal("KOL",Aus.zero3);
          povratImovine.setBigDecimal("ZC", dm.getStanje().getBigDecimal("ZC"));
          povratImovine.setBigDecimal("NC", dm.getStanje().getBigDecimal("NC"));
          povratImovine.setBigDecimal("VC", dm.getStanje().getBigDecimal("VC"));
          povratImovine.setBigDecimal("MC", dm.getStanje().getBigDecimal("MC"));
          povratImovine.setBigDecimal("VRI", Aus.zero2);
          povratImovine.setBigDecimal("NAB", Aus.zero2);
          povratImovine.setBigDecimal("MAR", Aus.zero2);
          povratImovine.setBigDecimal("POR", Aus.zero2);
        }
      } while (povratImovine.next());
    } else {
*/      povratImovine.first();
      do {
        if (povratImovine.getBigDecimal("KOL").compareTo(Aus.zero3) == 0){
         povratImovine.deleteRow();
         povratImovine.goToRow(povratImovine.getRow()-1);
        }
      } while (povratImovine.next());
      
    }
    
    povratImovine.setSort(new SortDescriptor(new String[] {"CSKL","CART"}));


    if (!partArtikl.equals("")){
      povratImovine = refilter(povratImovine, "NAZART", partArtikl);
    } else if (!partCart1.equals("")){
      povratImovine = refilter(povratImovine, "CART1", partArtikl);
    }

    
    if (cart.equalsIgnoreCase("") && !grupa.equals(""))  { //(cart.equalsIgnoreCase("") && !cartFiltering.equals("")) {
      return filterGroups(refilterSet(povratImovine, grupa, podgrupe));
    }
    return filterGroups(povratImovine);
  }
  
  private void insertIntoPovratTmpDataSet(QueryDataSet setZaSlaganje){
    povratTmp.last();
    setZaSlaganje.first();
    do {
      povratTmp.insertRow(false);
      dM.copyDestColumns(setZaSlaganje, povratTmp);
//      setZaSlaganje.copyTo(povratTmp);
    } while (setZaSlaganje.next());
    
  }
  
  private QueryDataSet getUlazData(Map conds){
    String qs = "SELECT " +
    
            "DOKU.CSKL AS CSKL, " +
            "DOKU.DATDOK AS DATDOK, " +
            "STDOKU.BRDOK AS BRDOK, " +
            "STDOKU.VRDOK AS VRDOK, " +
            "STDOKU.CART AS CART, " +
            "STDOKU.CART1 AS CART1, " +
            "STDOKU.BC AS BC, " +
            "STDOKU.NC AS NC, " +
            "STDOKU.VC AS VC, " +
            "STDOKU.MC AS MC, " +
            "STDOKU.NAZART AS NAZART, " +
            "STDOKU.JM AS JM, " +
            "STDOKU.KOL AS KOL, " +
            "STDOKU.ZC AS ZC, " +
            "STDOKU.IZAD AS VRI, " +
            "STDOKU.IPOR AS POR,  " +
            "STDOKU.IMAR AS MAR, " +
            "STDOKU.INAB AS NAB " +
            
            "from DOKU, STDOKU " +
            "WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god " +
            "AND doku.brdok = stdoku.brdok AND DOKU.VRDOK not in ('POR','KAL') " +  // AND DOKU.VRDOK != 'POR'
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by DOKU.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getPorData(Map conds){
    String qs ="SELECT " +
    
            "DOKU.CSKL AS CSKL, " +
            "DOKU.DATDOK AS DATDOK, " +
            "STDOKU.BRDOK AS BRDOK, " +
            "STDOKU.VRDOK AS VRDOK, " +
            "STDOKU.CART AS CART, " +
            "STDOKU.CART1 AS CART1, " +
            "STDOKU.BC AS BC, " +
            "STDOKU.NC AS NC, " +
            "STDOKU.VC AS VC, " +
            "STDOKU.MC AS MC, " +
            "STDOKU.NAZART AS NAZART, " +
            "STDOKU.JM AS JM, " +
            "0.000 AS KOL, " +
            "STDOKU.ZC AS ZC, " +
            "STDOKU.PORAV AS VRI, " +
            "STDOKU.DIOPORPOR AS POR,  " +
            "STDOKU.DIOPORMAR AS MAR, " +
            "0.00 AS NAB " +
            
            "from DOKU, STDOKU " +
            "WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god " +
            "AND doku.brdok = stdoku.brdok " +
            "and doku.vrdok = 'POR' " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by DOKU.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getPorUlazData(Map conds){
    String qs = "SELECT " +
    
            "DOKU.CSKL AS CSKL, " +
            "DOKU.DATDOK AS DATDOK, " +
            "STDOKU.BRDOK AS BRDOK, " +
            "STDOKU.VRDOK AS VRDOK, " +
            "STDOKU.CART AS CART, " +
            "STDOKU.CART1 AS CART1, " +
            "STDOKU.BC AS BC, " +
            "STDOKU.NC AS NC, " +
            "STDOKU.VC AS VC, " +
            "STDOKU.MC AS MC,  " +
            "STDOKU.NAZART AS NAZART, " +
            "STDOKU.JM AS JM, " +
            "0.000 AS KOL, " +
            "STDOKU.ZC AS ZC, " +
            "STDOKU.PORAV AS VRI, " +
            "STDOKU.DIOPORPOR AS POR,  " +
            "STDOKU.DIOPORMAR AS MAR, " +
            "0.00 AS NAB " +

            "from DOKU, STDOKU " +
            "WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god " +
            "AND doku.brdok = stdoku.brdok AND DOKU.VRDOK not in ('POR','KAL') " +
            "AND stdoku.porav <> 0.00 " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by DOKU.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getIzlazData(Map conds){
    String qs = "SELECT  " +
    
            "DOKI.CSKL AS CSKL, " +
            "DOKI.DATDOK AS DATDOK, " +
            "STDOKI.BRDOK AS BRDOK, " +
            "STDOKI.VRDOK AS VRDOK, " +
            "STDOKI.CART AS CART, " +
            "STDOKI.CART1 AS CART1, " +
            "STDOKI.BC AS BC, " +
            "STDOKI.NC AS NC, " +
            "STDOKI.VC AS VC, " +
            "STDOKI.MC AS MC, " +
            "STDOKI.NAZART AS NAZART, " +
            "STDOKI.JM AS JM, " +
            "-STDOKI.KOL AS KOL, " +
            "STDOKI.ZC AS ZC, " +
            "-STDOKI.IRAZ AS VRI, " +
            "-STDOKI.IPOR AS POR, " +
            "-STDOKI.IMAR AS MAR, " +
            "-STDOKI.INAB AS NAB " +
            
            "from DOKI, STDOKI " +
            "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND " +
            "doki.brdok = stdoki.brdok AND STDOKI.VRDOK NOT IN ('RAC','GRN','PRD','PON','NKU','NDO','DOS','POS','RNL','TER','ODB','TRE','ZAH') " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by DOKI.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getMeuData(Map conds){
    String qs = "SELECT " +
    
            "MESKLA.CSKLUL AS CSKL, " +
            "MESKLA.DATDOK AS DATDOK, " +
            "MESKLA.BRDOK AS BRDOK, " +
            "MESKLA.VRDOK AS VRDOK, " +
            "STMESKLA.CART AS CART, " +
            "STMESKLA.CART1 AS CART1, " +
            "STMESKLA.BC AS BC, " +
            "STMESKLA.NC AS NC, " +
            "STMESKLA.VC AS VC, " +
            "STMESKLA.MC AS MC, " +
            "STMESKLA.NAZART AS NAZART, " +
            "STMESKLA.JM AS JM, " +
            "STMESKLA.KOL AS KOL, " +
            "STMESKLA.ZCUL AS ZC, " +
            "STMESKLA.ZADRAZUL AS VRI, " +
            "STMESKLA.IPORUL AS POR, " +
            "STMESKLA.IMARUL AS MAR, " +
            "STMESKLA.INABUL AS NAB  " +
            
            "from MESKLA, STMESKLA " +
            "WHERE meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul " +
            "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god " +
            "and meskla.brdok = stmeskla.brdok  and meskla.vrdok in ('MEU','MES') " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by MESKLA.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getMeiData(Map conds){
    String qs = "SELECT " +
    
            "MESKLA.CSKLIZ AS CSKL, " +
            "MESKLA.DATDOK AS DATDOK, " +
            "MESKLA.BRDOK AS BRDOK, " +
            "MESKLA.VRDOK AS VRDOK, " +
            "STMESKLA.CART AS CART, " +
            "STMESKLA.CART1 AS CART1, " +
            "STMESKLA.BC AS BC, " +
            "STMESKLA.NC AS NC, " +
            "STMESKLA.VC AS VC, " +
            "STMESKLA.MC AS MC, " +
            "STMESKLA.NAZART AS NAZART, " +
            "STMESKLA.JM AS JM, " +
            "- STMESKLA.KOL AS KOL, " +
            "STMESKLA.ZC AS ZC, " +
            "- STMESKLA.ZADRAZIZ AS VRI,  " +
            "- STMESKLA.IPORIZ AS POR, " +
            "- STMESKLA.IMARIZ AS MAR, " +
            "- STMESKLA.INABIZ AS NAB " +
            
            "from MESKLA, STMESKLA " +
            "WHERE meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul " +
            "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god " +
            "AND meskla.brdok = stmeskla.brdok   and meskla.vrdok in ('MEI','MES') " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by MESKLA.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  private QueryDataSet getMesPorData(Map conds){
    String qs = "SELECT " +
    
            "MESKLA.CSKLUL AS CSKL, " +
            "MESKLA.DATDOK AS DATDOK, " +
            "MESKLA.BRDOK AS CSKL, " +
            "MESKLA.VRDOK AS VRDOK, " +
            "STMESKLA.CART AS CART, " +
            "STMESKLA.CART1 AS CART1, " +
            "STMESKLA.BC AS BC, " +
            "STMESKLA.NC AS NC, " +
            "STMESKLA.VC AS VC, " +
            "STMESKLA.MC AS MC, " +
            "STMESKLA.NAZART AS NAZART, " +
            "STMESKLA.JM AS JM, " +
            "0.000 AS KOL, " +
            "STMESKLA.ZCUL AS ZC, " +
            "STMESKLA.PORAV AS VRI, " +
            "STMESKLA.DIOPORPOR AS POR, " +
            "STMESKLA.DIOPORMAR AS MAR, " +
            "0.00 AS NAB  " +
            
            "from MESKLA, STMESKLA " +
            "WHERE meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul " +
            "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok  " +
            "and stmeskla.porav <> 0.00   and meskla.vrdok in ('MEU','MES') " +
            
            conds.get("CSKL").toString() +
            conds.get("CART").toString() +
            conds.get("GOD").toString() +
            conds.get("DODAT")+
            
            "order by MESKLA.DATDOK";
    
    System.out.println(qs);
    QueryDataSet rs = ut.getNewQueryDataSet(qs);
    if (rs.rowCount() < 1) return null;
    return rs;
  }
  
  private Map makeCondition(String table, String sttable, String ulizob, String cskl, String cart, Timestamp dodat){
    String locDat = "and "+table+".DATDOK <= '"+ ut.getLastSecondOfDay(dodat)+"' ";
    String locCskl = "and "+table+"."+ulizob+"='" + cskl + "' ";
    String locCart = "and "+sttable+".CART = " + cart + " ";
    String locGod =  "and "+table+".GOD = '"+ut.getYear(dodat)+"'";
    
    Map conditionTable = new HashMap();

    if (cskl.equalsIgnoreCase("")) {
      DataSet sfc = hr.restart.robno.Util.getUtil().getSkladFromCorg();
      sfc.open();
      sfc.first();
      String eskling = "";
      if (!cached) {
        if (sfc.rowCount() > 1) {
          do {
            eskling += "'" + sfc.getString("CSKL") + "'";
            if (sfc.next())
              eskling += ", ";
            else {
              break;
            }
          } while (true);
          eskls = eskling;
        } else {
          System.out.println("Skladiste - " + sfc.getString("CSKL")); 
          eskls = "'" + sfc.getString("CSKL") + "'";
        }
        cached = true;
      }
      locCskl = "and " + table + "." + ulizob + " in (" + eskls + ") ";
    }    
    
    if (cart.equalsIgnoreCase("")) {
      locCart = "";
    }
    
    conditionTable.put("CSKL",locCskl);
    conditionTable.put("CART",locCart);
    conditionTable.put("GOD",locGod);
    conditionTable.put("DODAT",locDat);
    
    return conditionTable;
  }

  private QueryDataSet refilterSet(QueryDataSet orginal, /*String grupa,*/ String cartFiltering) {
    String artikli = "SELECT * FROM Artikli WHERE "+cartFiltering;
    /*String grup = "";
    if (podgrupe) {
      QueryDataSet grupe = ut.getNewQueryDataSet("select cgrart, cgrartprip from grupart where cgrartprip = '" + grupa + "'");
      if (grupe.rowCount() < 2)
        grup = "cgrart = '" + grupa + "' ";
      else {
        grup = "cgrart in (";
        grupe.first();
        do {
          grup += "'" + grupe.getString("CGRART") + "'";
          if (grupe.next())
            grup += ", ";
          else {
            grup += ") ";
            break;
          }
        } while (true);

      }
    } else {
      grup = "cgrart = '" + grupa + "' ";
    }

    artikli += grup;*/


    QueryDataSet artiklz = ut.getNewQueryDataSet(artikli);


    orginal.first();

    do {
      if (!ld.raLocate(artiklz, "CART", orginal.getInt("CART") + "")) {
        orginal.deleteRow();
        orginal.goToRow(orginal.getRow() - 1);
      }
    } while (orginal.next());

    return orginal;
  }

  private QueryDataSet refilterSet(QueryDataSet orginal, String grupa, boolean podgrupe) {
    String artikli = "SELECT * FROM Artikli WHERE ";
    String grup = "";
    if (podgrupe) {
      QueryDataSet grupe = ut.getNewQueryDataSet("select cgrart, cgrartprip from grupart where cgrartprip = '" + grupa + "'");
      if (grupe.rowCount() < 2)
        grup = "cgrart = '" + grupa + "' ";
      else {
        grup = "cgrart in (";
        grupe.first();
        do {
          grup += "'" + grupe.getString("CGRART") + "'";
          if (grupe.next())
            grup += ", ";
          else {
            grup += ") ";
            break;
          }
        } while (true);

      }
    } else {
      grup = "cgrart = '" + grupa + "' ";
    }

    artikli += grup;

    QueryDataSet artiklz = ut.getNewQueryDataSet(artikli);
    
    HashMap amp = new HashMap();
    
    for (artiklz.first();artiklz.inBounds();artiklz.next()) {
      amp.put(new Integer(artiklz.getInt("CART")),"postojim");
    }

    if (artiklz.rowCount() <1) return null;

    orginal.first(); 
    
    int row = 0;

//    do {
    for (;orginal.inBounds();){
      if (amp.get(new Integer(orginal.getInt("CART"))) == null || !amp.get(new Integer(orginal.getInt("CART"))).toString().equals("postojim")) {
        row = orginal.getRow();
        orginal.deleteRow();
        if (orginal.getRow() != 0) orginal.goToRow(row-1);
        else orginal.goToRow(row);
      } else {
        orginal.next();
      }
    }
//    } while (orginal.next());
    
    if (orginal.rowCount() < 1) return null;
    
    return orginal;
  }
  
  private QueryDataSet filterGroups(QueryDataSet orginal) {
    dm.getArtikli().open();
    orginal.first();
    do {
      if (ld.raLocate(dm.getArtikli(), "CART", orginal.getInt("CART") + "")) {
        if (dm.getArtikli().getBigDecimal("MINKOL").compareTo(orginal.getBigDecimal("KOL")) >= 0) {
          orginal.setInt("SIGMIN", 2);
        } else if (dm.getArtikli().getBigDecimal("SIGKOL").compareTo(orginal.getBigDecimal("KOL")) >= 0) {
          orginal.setInt("SIGMIN", 1);
        } else {
          orginal.setInt("SIGMIN", 0);
        }
      } else {
        orginal.setInt("SIGMIN", 0);
      }
    } while (orginal.next());
    
    return orginal;
  }
  
  private QueryDataSet refilter(QueryDataSet orginal, String field, String part) {
    if (part.indexOf('%') >= 0 || part.indexOf('*') >= 0 || 
        part.indexOf('?') >= 0 || part.indexOf('[') >= 0)
      return refilterGlob(orginal, field, part);
    
    QueryDataSet filtered = new QueryDataSet();
    filtered.setColumns(orginal.cloneColumns());
    filtered.open();
    
    part = part.toLowerCase();
    for (orginal.first(); orginal.inBounds(); orginal.next())
      if (orginal.getString(field).toLowerCase().indexOf(part) >= 0) {
        filtered.insertRow(false);
        orginal.copyTo(filtered);
      }
    
    return filtered;
  }
  
  private QueryDataSet refilterGlob(QueryDataSet orginal, String field, String part) {    
    QueryDataSet filtered = new QueryDataSet();
    filtered.setColumns(orginal.cloneColumns());
    filtered.open();
    VarStr vp = new VarStr(part.toLowerCase());
    if (vp.indexOf('%') >= 0)
      vp.replace('%', '*').insert(0, '*').append('*');
    raGlob glob = new raGlob(vp.toString());
    
    part = part.toLowerCase();
    for (orginal.first(); orginal.inBounds(); orginal.next())
      if (glob.matches(orginal.getString(field).toLowerCase())) {
        filtered.insertRow(false);
        orginal.copyTo(filtered);
      }
    
    return filtered;
  }
  
/*  private QueryDataSet refilterArtiklz(QueryDataSet orginal, String partart){
    QueryDataSet filtered = null;

    QueryDataSet filter = ut.getNewQueryDataSet("SELECT cart from Artikli WHERE nazart like '"+
        partart+"%' or nazart like '"+
        partart.toUpperCase()+"%' or nazart like '"+
        partart.toLowerCase()+"%' or nazart like '"+
        (partart.substring(0,1).toUpperCase()+partart.substring(1).toLowerCase())+"%'");
    
    
    if (filter.rowCount() < 1) return null;
    
    HashMap artikliuigri = new HashMap();
    for (filter.first();filter.inBounds();filter.next())
      artikliuigri.put(filter.getInt("CART")+"",filter.getInt("CART")+"");
    
    
    filtered = new QueryDataSet();
    filtered.setColumns(orginal.cloneColumns());
    filtered.open();
    
    for (orginal.first();orginal.inBounds();orginal.next()){
      if (artikliuigri.containsKey(orginal.getInt("CART")+"")){
        filtered.insertRow(false);
        orginal.copyTo(filtered);
      }
    }
    
    return filtered;
  }
  
  private QueryDataSet refilterArtiklzCart1(QueryDataSet orginal, String partart){
    QueryDataSet filtered = null;

    QueryDataSet filter = ut.getNewQueryDataSet("SELECT cart from Artikli WHERE cart1 like '"+
        partart+"%' or cart1 like '"+
        partart.toUpperCase()+"%' or cart1 like '"+
        partart.toLowerCase()+"%' or cart1 like '"+
        (partart.substring(0,1).toUpperCase()+partart.substring(1).toLowerCase())+"%'");
    
    if (filter.rowCount() < 1) return null;
    
    HashMap artikliuigri = new HashMap();
    for (filter.first();filter.inBounds();filter.next())
      artikliuigri.put(filter.getInt("CART")+"",filter.getInt("CART")+"");
    
    
    filtered = new QueryDataSet();
    filtered.setColumns(orginal.cloneColumns());
    filtered.open();
    
    for (orginal.first();orginal.inBounds();orginal.next()){
      if (artikliuigri.containsKey(orginal.getInt("CART")+"")){
        filtered.insertRow(false);
        orginal.copyTo(filtered);
      }
    }
    
    return filtered;
  }*/
}
