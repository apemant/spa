/****license*****************************************************************
**   file: raPrviKupci.java
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

import hr.restart.baza.Agenti;
import hr.restart.baza.Artikli;
import hr.restart.baza.Partneri;
import hr.restart.baza.Telemark;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;

import java.sql.Timestamp;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raPrviKupci extends raUpitFat {
  
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  private TableDataSet tds = new TableDataSet();
  JPanel jp = new JPanel();
  XYLayout myXyLayout = new XYLayout();
  
  private JraTextField jraDatumOd = new JraTextField();
  private JraTextField jraDatumDo = new JraTextField();
  
  private int[] kolone;
  
  public raPrviKupci(){
    try {
      initializer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void componentShow() {
    removeNav();
    doubleclicked = false;
    setDataSet(null);
    jraDatumOd.requestFocusLater();
    sastaviHashMape();
  }

  public void firstESC() {
    if (cache != null) {
      doubleclicked = false;
      setDataSetAndSums(cache,new String[] {"PROMET"});
      try {
      this.getJPTV().getColumnsBean().initialize();
      } catch (Exception e) {
//        e.printStackTrace();
      }
      this.getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);
      if (!"".equalsIgnoreCase(selectidColumn)) {
        System.out.println("selectidColumn "+selectidColumn);
        getJPTV().getColumnsBean().setComboSelectedItem(selectidColumn);
      }
      cache = null;
    } else {
      setDataSet(null);
      raCommonClass.getraCommonClass().EnabDisabAll(this.getJPan(),true);
      removeNav();
      jraDatumOd.requestFocusLater();
    }
  }
  
  public boolean runFirstESC() {
    return (this.getJPTV().getDataSet() != null);
  }
  
  private boolean doubleclicked;
  
  public void okPress() {
    QueryDataSet tmp;
    if (!doubleclicked) {
      kolone = new int[] {0,1,2};
      setDataSetAndSums(querySet1(),new String[] {"PROMET"});
    } else {
      kolone = new int[] {0,1,2,3,4,5};
      setDataSetAndSums(querySet2(),new String[] {"PROMET"});
    }
  }

  private void initializer() throws Exception {
    tds.setColumns(new Column[] {
        dm.createTimestampColumn("DATUMOD", "Po\u010Detni datum"),
        dm.createTimestampColumn("DATUMDO", "Završni datum")}
    );
    
    tds.open();
    
    jp.setLayout(myXyLayout);

    jraDatumOd.setColumnName("DATUMOD");
    jraDatumOd.setDataSet(tds);
    
    jraDatumDo.setColumnName("DATUMDO");
    jraDatumDo.setDataSet(tds);
    
    myXyLayout.setWidth(640);
    myXyLayout.setHeight(45);
    
    jp.add(new JLabel("Period"),  new XYConstraints(15, 12, 100, -1));
    jp.add(jraDatumOd,  new XYConstraints(150, 10, 100, -1));
    jp.add(jraDatumDo,  new XYConstraints(255, 10, 100, -1));
    
    this.setJPan(jp);
    
    tds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(vl.getToday()));
    tds.setTimestamp("DATUMDO",vl.getToday());
  }
  
  private QueryDataSet querySet1(){
    QueryDataSet tmp = ut.getNewQueryDataSet("SELECT doki.cpar, doki.cagent, stdoki.cart as cart, stdoki.kol as kol, stdoki.iprodbp as promet " +
        "FROM doki,stdoki " +
        "WHERE doki.cskl = stdoki.cskl "+
        "AND doki.vrdok = stdoki.vrdok "+
        "AND doki.god = stdoki.god "+
        "AND doki.brdok = stdoki.brdok AND doki.cug = '1' and doki.vrdok in ('ROT','RAC') " +
        "AND doki.cskl = '1' "+
        "AND doki.sysdat between '"+ ut.getFirstSecondOfDay(tds.getTimestamp("DATUMOD"))+"' and '"+ut.getLastSecondOfDay(tds.getTimestamp("DATUMDO"))+"' "+
        /*"group by doki.cpar "+*/"order by doki.cpar"
    );
    
    if (tmp.rowCount() < 1) setNoDataAndReturnImmediately();
    
    QueryDataSet minSet = ut.getNewQueryDataSet("SELECT doki.cpar, min(doki.datdok) as datpf " +
        "FROM doki WHERE doki.vrdok in ('ROT','RAC','POD') AND " +
        "doki.cskl = '1' group by doki.cpar");
    
    if (minSet.rowCount() < 1) setNoDataAndReturnImmediately();

    QueryDataSet prikaz = new QueryDataSet();
    prikaz.setColumns(new Column[] {
        (Column) dm.getPartneri().getColumn("CPAR").clone(),
        dm.createStringColumn("AGENT","Agent",50),
        dm.createStringColumn("TELE","Telemarketer",50),
        (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
        dm.createStringColumn("KUPOD","Kupac od",8),
        dm.createStringColumn("ARTIKLZ", "Roba", 400),
        dm.createBigDecimalColumn("PROMET", "Promet", 2)
    });
    prikaz.open();
    
    /////////
    String grupeAparata = frmParam.getFrmParam().getParam("robno","grAparati","","Grupe artikala - aparati i srodno");
    
    if (grupeAparata.equals("")) setNoDataAndReturnImmediately("Nije definirana grupa (ili grupe) aparata");
    VarStr grupe = new VarStr(grupeAparata);
    
    grupe.removeWhitespace();
    
    grupe.replaceAll(",","','");
    grupe.insert(0,"'");
    grupe.append("'");
    
    String gr = grupe.toString();
    
//    System.out.println("grupe aparat = " + gr); //XDEBUG delete when no more needed
    
    QueryDataSet aparatiSet = Artikli.getDataModule().getTempSet("CART,NAZART","cgrart in ("+gr+")");
    aparatiSet.open();
    
    HashMap aparatiMap = new HashMap();
    
    for (aparatiSet.first();aparatiSet.inBounds();aparatiSet.next()){
      checkClosing();
      aparatiMap.put(aparatiSet.getInt("CART")+"",aparatiSet.getString("NAZART").trim());
    }
    /////////
    
    int cpar = 0;
    String pm = "Potrošni materijal";
    for (tmp.first();tmp.inBounds();tmp.next()){
      if (cpar != tmp.getInt("CPAR")) {
        prikaz.insertRow(false);
        prikaz.setInt("CPAR", tmp.getInt("CPAR"));
        lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", tmp.getInt("CPAR") + "");
        prikaz.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
        prikaz.setBigDecimal("PROMET", tmp.getBigDecimal("PROMET"));
        
        if (ld.raLocate(minSet, "CPAR", String.valueOf(tmp.getInt("CPAR")))) {
          Timestamp beg = minSet.getTimestamp("DATPF");
          prikaz.setString("KUPOD", ut.getMonth(beg) + "." + vl.findYear(beg)+".");
        }
        
        if (agenti.containsKey(tmp.getInt("CAGENT")+""))
          prikaz.setString("AGENT",agenti.get(tmp.getInt("CAGENT")+"").toString());
        else
          prikaz.setString("AGENT","Nema agenta");
        
        if (parttel.containsKey(tmp.getInt("CPAR")+""))
          prikaz.setString("TELE",telemarketeri.get(parttel.get(tmp.getInt("CPAR")+"")).toString());
        else 
          prikaz.setString("TELE","Nema Telemarketera");
        
        if (aparatiMap.containsKey(tmp.getInt("CART")+"")){
          prikaz.setString("ARTIKLZ",tmp.getBigDecimal("KOL").intValue() + " X " + aparatiMap.get(tmp.getInt("CART")+"").toString());
        } else {
          prikaz.setString("ARTIKLZ",pm);
        }
        cpar = tmp.getInt("CPAR");
      } else {
        prikaz.setBigDecimal("PROMET", prikaz.getBigDecimal("PROMET").add(tmp.getBigDecimal("PROMET")));
        if (aparatiMap.containsKey(tmp.getInt("CART")+"")){
          if (prikaz.getString("ARTIKLZ").equals(pm)){
            prikaz.setString("ARTIKLZ",tmp.getBigDecimal("KOL").intValue() + " X " + aparatiMap.get(tmp.getInt("CART")+"").toString());
          } else {
            prikaz.setString("ARTIKLZ",prikaz.getString("ARTIKLZ")+", " + tmp.getBigDecimal("KOL").intValue() + " X " + aparatiMap.get(tmp.getInt("CART")+"").toString());
          }
        }
      }
    }
    prikaz.setTableName("prikaz01");
    return prikaz;
  }
  
  private QueryDataSet querySet2(){
    QueryDataSet tmp = ut.getNewQueryDataSet("SELECT doki.god, doki.cpar, doki.vrdok, max(doki.datdok) as datdok, max(doki.sysdat) as sysdat, doki.brdok, sum(stdoki.iprodbp) as promet " +
        "FROM doki,stdoki " +
        "WHERE doki.cskl = stdoki.cskl "+
        "AND doki.vrdok = stdoki.vrdok "+
        "AND doki.god = stdoki.god "+
        "AND cpar = "+cache.getInt("CPAR")+" and doki.cug = '1' and doki.vrdok in ('ROT','RAC') " +
        "AND doki.brdok = stdoki.brdok " +
        "AND doki.sysdat between '"+ ut.getFirstSecondOfDay(tds.getTimestamp("DATUMOD"))+"' and '"+ut.getLastSecondOfDay(tds.getTimestamp("DATUMDO"))+"' "+ 
        "AND doki.cskl = '1' "+
        "group by doki.god, doki.cpar, doki.vrdok, doki.brdok order by doki.cpar, doki.brdok"
    );
    
//    System.out.println(tmp); //XDEBUG delete when no more needed
    
    QueryDataSet prikaz = new QueryDataSet();
    prikaz.setColumns(new Column[] {
        (Column) dm.getDoki().getColumn("VRDOK").clone(),
        (Column) dm.getDoki().getColumn("BRDOK").clone(),
        (Column) dm.getDoki().getColumn("DATDOK").clone(),
        (Column) dm.getDoki().getColumn("GOD").clone(),
        (Column) dm.getPartneri().getColumn("CPAR").clone(),
        (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
        dm.createBigDecimalColumn("PROMET", "Promet", 2)
    });
    prikaz.getColumn("GOD").setVisible(0);
    prikaz.open();
    
    for (tmp.first();tmp.inBounds();tmp.next()){
      prikaz.insertRow(false);
      prikaz.setInt("CPAR",tmp.getInt("CPAR"));
      lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",tmp.getInt("CPAR")+"");
      prikaz.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
      prikaz.setString("VRDOK", tmp.getString("VRDOK"));
      prikaz.setInt("BRDOK", tmp.getInt("BRDOK"));
      prikaz.setTimestamp("DATDOK", tmp.getTimestamp("DATDOK"));
      prikaz.setBigDecimal("PROMET",tmp.getBigDecimal("PROMET"));
      prikaz.setString("GOD", tmp.getString("GOD"));
    }
    
    prikaz.setTableName("prikaz02");
    return prikaz;
  }

  String selectidColumn;
  
  QueryDataSet cache = null;
  
  public void jptv_doubleClick() {
//    kupac = this.getJPTV().getDataSet().getInt("CPAR");
    if (!doubleclicked){
      doubleclicked = true;
      
      selectidColumn = getJPTV().getColumnsBean()
      .getComboSelectedItem();
      
      // ovdje zapamtiti koji je selektiran
      cache = this.getJPTV().getDataSet();
      setDataSet(null);
      ok_action();
    } else {
        hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
        rut.showDocs("1", "", getJPTV().getDataSet().getString("VRDOK"), 
        		getJPTV().getDataSet().getInt("BRDOK"),
            getJPTV().getDataSet().getString("GOD"));
    }
  }
  
  HashMap agenti = null;
  HashMap telemarketeri = null;
  HashMap parttel = null;
  
  private void sastaviHashMape(){
    agenti = new HashMap();
    
    QueryDataSet ags = Agenti.getDataModule().getTempSet();
    ags.open();
    for(ags.first();ags.inBounds();ags.next())
      agenti.put(ags.getInt("CAGENT")+"",ags.getString("NAZAGENT"));
    
    telemarketeri = new HashMap();
    
    QueryDataSet tls = Telemark.getDataModule().getTempSet();
    tls.open();
    for(tls.first();tls.inBounds();tls.next())
      telemarketeri.put(tls.getInt("CTEL")+"",tls.getString("IME"));
    
    parttel = new HashMap();
    
    QueryDataSet atm = Partneri.getDataModule().getTempSet("CPAR, CTEL","ctel is not null");
    atm.open();
    for(atm.first();atm.inBounds();atm.next())
      parttel.put(atm.getInt("CPAR")+"",atm.getInt("CTEL")+"");
  }

  public String navDoubleClickActionName() {
    return "Fakture";
  }

  public int[] navVisibleColumns() {
    return kolone;
  }
  
  

}
