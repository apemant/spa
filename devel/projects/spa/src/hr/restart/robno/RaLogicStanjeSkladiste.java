/****license*****************************************************************
**   file: RaLogicStanjeSkladiste.java
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
import hr.restart.util.Stopwatch;
import hr.restart.util.TimeTrack;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raGlob;

import java.util.HashMap;
import java.util.HashSet;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.sql.dataset.QueryDataSet;

public class RaLogicStanjeSkladiste {
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  dM dm = dM.getDataModule();

  lookupData ld = lookupData.getlookupData();
  

  public QueryDataSet datasetZaEkran(String cskl, String cart, String grupa, boolean podGrupe, String god, boolean kol0, String partArtikl, String partCart1, boolean all) {
    Stopwatch st = Stopwatch.start("artikli stanje");
    QueryDataSet data = ut.getNewQueryDataSet(queryString(cskl, cart, god, kol0));

    st.report("got data");
    if (data.rowCount() < 1)
      return null;

    QueryDataSet mainDataSet = new QueryDataSet();
    mainDataSet.setLocale(Aus.hr);
    mainDataSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME + MetaDataUpdate.PRECISION + MetaDataUpdate.SCALE + MetaDataUpdate.SEARCHABLE);
    mainDataSet.setColumns(new Column[] {
        (Column) dm.getStanje().getColumn("CSKL").clone(),
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
        dm.createBigDecimalColumn("NAB", "Nabavna vrijednost", 2), 
        dm.createBigDecimalColumn("MAR", "Zarada", 2), 
        dm.createBigDecimalColumn("POR", "Porez", 2), 
        (Column) dm.getStanje().getColumn("VRI").clone(), 
        (Column) dm.getStanje().getColumn("KOLREZ").clone(), 
        dm.createIntColumn("SIGMIN")
      }
    );
    
    mainDataSet.setRowId("CSKL",true);
    mainDataSet.setRowId("CART",true);
    
    if ("N".equalsIgnoreCase(frmParam.getParam("robno", "allSif", "N",
          "Prikaz sve tri šifre na stanju artikala (D,N)"))) {
      mainDataSet.getColumn("CART").setVisible(0);
      mainDataSet.getColumn("CART1").setVisible(0);
      mainDataSet.getColumn("BC").setVisible(0);
      mainDataSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
    }

//    mainDataSet.getColumn("NAB").setVisible(0);
    mainDataSet.getColumn("MAR").setVisible(0); 
    mainDataSet.getColumn("POR").setVisible(0);
    mainDataSet.getColumn("SIGMIN").setVisible(0);

    mainDataSet.open();
    data.first();
    dm.getArtikli().open();
    Artikli.getDataModule().fixSort();
    do {
      ld.raLocate(dm.getArtikli(), "CART", data.getInt("CART") + "");
      if (raVart.isUsluga(dm.getArtikli()) || (!all && !raVart.isStanje(dm.getArtikli()))) continue;
      
      mainDataSet.insertRow(false);
      mainDataSet.setString("CSKL", data.getString("CSKL"));
      mainDataSet.setInt("CART", data.getInt("CART"));
      mainDataSet.setString("CART1", dm.getArtikli().getString("CART1"));
      mainDataSet.setString("BC", dm.getArtikli().getString("BC"));
      mainDataSet.setString("NAZART", dm.getArtikli().getString("NAZART"));
      mainDataSet.setString("JM", dm.getArtikli().getString("JM"));
      mainDataSet.setBigDecimal("KOL", data.getBigDecimal("KOL"));
      mainDataSet.setBigDecimal("ZC", data.getBigDecimal("ZC"));
      mainDataSet.setBigDecimal("NC", data.getBigDecimal("NC"));
      mainDataSet.setBigDecimal("VC", data.getBigDecimal("VC"));
      mainDataSet.setBigDecimal("MC", data.getBigDecimal("MC"));
      mainDataSet.setBigDecimal("VRI", data.getBigDecimal("VRI"));
      mainDataSet.setBigDecimal("KOLREZ", data.getBigDecimal("KOLREZ"));
      mainDataSet.setBigDecimal("NAB", data.getBigDecimal("NABUL").subtract(data.getBigDecimal("NABIZ")));
      mainDataSet.setBigDecimal("MAR", data.getBigDecimal("MARUL").subtract(data.getBigDecimal("MARIZ")));
      mainDataSet.setBigDecimal("POR", data.getBigDecimal("PORUL").subtract(data.getBigDecimal("PORIZ")));
      
      if (ld.raLocate(dm.getArtikli(), "CART", mainDataSet.getInt("CART") + "")) {
        if (dm.getArtikli().getBigDecimal("MINKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >= 0) {
          mainDataSet.setInt("SIGMIN", 2);
        } else if (dm.getArtikli().getBigDecimal("SIGKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >= 0) {
          mainDataSet.setInt("SIGMIN", 1);
        } else {
          mainDataSet.setInt("SIGMIN", 0);
        }
      } else {
        mainDataSet.setInt("SIGMIN", 0);
      }
    } while (data.next());

    st.report("fill data");
    if (!partArtikl.equals("")){
      mainDataSet = refilter(mainDataSet, "NAZART", partArtikl);
    } else if (!partCart1.equals("")){
      mainDataSet = refilter(mainDataSet, "CART1", partCart1);
    }

    st.report("refilter data");
    if (cart.equalsIgnoreCase("") && !grupa.equals("")) {
      return refilterSet(mainDataSet, grupa, podGrupe);
    }

    return mainDataSet;
  }

  private String queryString(String cskl, String cart, String god, boolean kol0) {
    String locCskl = "and STANJE.CSKL='" + cskl + "' ";
    String locCart = "and " + cart + " ";
    String upit = "select * from STANJE where STANJE.GOD = '" + god + "' ";

    if (cskl.equalsIgnoreCase("")) {
      DataSet sfc = hr.restart.robno.Util.getUtil().getSkladFromCorg();
      sfc.open();
      sfc.first();
      String eskling = "";
      do {
        eskling += "'" + sfc.getString("CSKL") + "'";
        if (sfc.next())
          eskling += ", ";
        else {
          break;
        }
      } while (true);

      locCskl = "and STANJE.CSKL in (" + eskling + ") ";
    }

    if (cart.equalsIgnoreCase("")) {
      locCart = "";
    }

    upit += locCskl + locCart;

    if (!kol0) {
      upit += "and STANJE.KOL != 0.000 order by STANJE.CSKL, STANJE.CART";
    } else {
      upit += "order by STANJE.CSKL, STANJE.CART";
    }

    System.out.println("UPIT : " + upit);

    return upit;
  }
  
  /*private QueryDataSet refilterArtiklz(QueryDataSet orginal, String partart){
    QueryDataSet filtered = null;
    QueryDataSet filter = ut.getNewQueryDataSet("SELECT cart from Artikli WHERE nazart like '%"+
        partart+"%' or nazart like '%"+
        partart.toUpperCase()+"%' or nazart like '%"+
        partart.toLowerCase()+"%' or nazart like '%"+
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
  
  /*private QueryDataSet refilterArtiklzCart1(QueryDataSet orginal, String partart){
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

}
