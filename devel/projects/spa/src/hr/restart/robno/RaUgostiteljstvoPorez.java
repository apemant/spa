/****license*****************************************************************
**   file: RaUgostiteljstvoPorez.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class RaUgostiteljstvoPorez extends raUpitLite {
  Valid vl = hr.restart.util.Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  TableDataSet tds = new TableDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  lookupData ld = lookupData.getlookupData();

  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraButton jbSelOrg = new JraButton();
  JlrNavField jlrNazivOrg = new JlrNavField();
  JlrNavField jlrCorg = new JlrNavField();

  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  
  private static RaUgostiteljstvoPorez instanceOfMe = null;
  
  public static RaUgostiteljstvoPorez getInstance(){
    if (instanceOfMe == null) instanceOfMe = new RaUgostiteljstvoPorez();
    return instanceOfMe;
  }
  
  public QueryDataSet getRepSet(){
    return izlaz;
  }
  
  public HashMap getZaglavlje(){
    return zaglavlje;
  }
  
  public RaUgostiteljstvoPorez() {
    try {
      xInit();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    rcc.EnabDisabAll(jp,true);
    jlrCorg.requestFocusLater();
  }
  
  public void firstESC() {
    rcc.EnabDisabAll(jp,true);
    jlrCorg.requestFocusLater();
  }
  
  public void okPress() {
    makeSet();
    napuniNekeHashMape();
  }
  
  HashMap zaglavlje;
  
  private String getUpit(){
   return "SELECT max(doki.cskl) as cskl, stdoki.cart, sum(stdoki.iprodbp) as iprodbp, sum(stdoki.por1) as por1, " +
        "sum(stdoki.por2)as por2, sum(stdoki.por3)as por3, sum(stdoki.iprodsp) as iprodsp "+ 
   "FROM doki, stdoki "+
   "WHERE doki.cskl = stdoki.cskl "+
   "AND doki.vrdok = stdoki.vrdok "+
   "AND doki.god = stdoki.god "+
   "AND doki.brdok = stdoki.brdok "+
   "and doki.vrdok = 'POS' "+
   "and doki.datdok between '"+ut.getFirstSecondOfDay(tds.getTimestamp("POCDATUM"))+"' and '"+ut.getFirstSecondOfDay(tds.getTimestamp("ZAVDATUM"))+"' "+
   "and doki.cskl = '"+tds.getString("CORG")+"' "+
   "group by stdoki.cart"; 
  }
  
  QueryDataSet izlaz = null;
  
  private void makeSet(){
    QueryDataSet prometi = ut.getNewQueryDataSet(getUpit());
    if (prometi.rowCount() < 1) setNoDataAndReturnImmediately();
    QueryDataSet artikliGrupePorez = ut.getNewQueryDataSet("SELECT cart, cgrart, cpor FROM Artikli WHERE vrart='A' " +
            "and (cgrart is not null and cgrart != '')");
    QueryDataSet porezi = ut.getNewQueryDataSet("SELECT cpor, por1, nazpor1, por2, nazpor2, por3, nazpor3 FROM Porezi");
    QueryDataSet grupe = ut.getNewQueryDataSet("SELECT cgrart, nazgrart FROM Grupart");
    
    HashMap grupeArtikli = new HashMap();
    HashMap poreziArtikli = new HashMap();
    for (artikliGrupePorez.first();artikliGrupePorez.inBounds();artikliGrupePorez.next()){
      grupeArtikli.put(new Integer(artikliGrupePorez.getInt("CART")),artikliGrupePorez.getString("CGRART"));
      poreziArtikli.put(new Integer(artikliGrupePorez.getInt("CART")),artikliGrupePorez.getString("CPOR"));
    }
    
    HashMap grupeNazivi = new HashMap();
    for (grupe.first(); grupe.inBounds(); grupe.next()){
      grupeNazivi.put(grupe.getString("CGRART"),grupe.getString("NAZGRART"));
    }
    
    izlaz = new QueryDataSet();
    izlaz.setColumns(new Column[]{
// dm.getOrgstruktura().getColumn("CORG").cloneColumn(),
        dm.getGrupart().getColumn("CGRART").cloneColumn(),
        dm.getGrupart().getColumn("NAZGRART").cloneColumn(),
        dm.createStringColumn("CPOR","TBR",5),
        dm.createStringColumn("NAZPOR","Naziv poreza",50),
        dm.createBigDecimalColumn("PPOR","% poreza",2),
        dm.createBigDecimalColumn("OSN","Osnovica",2),
        dm.createBigDecimalColumn("IPOR","Iznos poreza",2)
    });
    
    izlaz.open();
    
    
//    sysoutTEST st = new sysoutTEST(false); // XDEBUG delete when no more needed
// st.prn(prometi);
// st.prn(artikliGrupePorez);
// st.prn(porezi);
// st.prn(grupe);
    
    QueryDataSet ns = srediPoreze(poreziArtikli,grupeArtikli,grupeNazivi,prometi); 
    
//    st.prn(ns);
    
    HashMap articleMemoriy = new HashMap();
    
    boolean haveValue;
    
    for (ns.first();ns.inBounds();ns.next()){
      haveValue = articleMemoriy.containsKey(new Integer(ns.getInt("CART")));
      if (!haveValue){
        if (!ld.raLocate(izlaz, "CGRART", ns.getString("CGRART"))) {
          izlaz.insertRow(false);
          izlaz.setString("CGRART", ns.getString("CGRART"));
          izlaz.setString("NAZGRART", ns.getString("NAZGRART"));
          izlaz.setString("CPOR", ns.getString("CPOR"));
          izlaz.setString("NAZPOR", ns.getString("NAZPOR"));
          izlaz.setBigDecimal("PPOR", ns.getBigDecimal("PPOR"));
          izlaz.setBigDecimal("OSN", ns.getBigDecimal("OSN"));
          izlaz.setBigDecimal("IPOR", ns.getBigDecimal("IPOR"));
        } else {
          if (!ld.raLocate(izlaz,"CPOR",ns.getString("CPOR"))){
            izlaz.insertRow(false);
            izlaz.setString("CGRART", ns.getString("CGRART"));
            izlaz.setString("NAZGRART", ns.getString("NAZGRART"));
            izlaz.setString("CPOR", ns.getString("CPOR"));
            izlaz.setString("NAZPOR", ns.getString("NAZPOR"));
            izlaz.setBigDecimal("PPOR", ns.getBigDecimal("PPOR"));
            izlaz.setBigDecimal("OSN", ns.getBigDecimal("OSN"));
            izlaz.setBigDecimal("IPOR", ns.getBigDecimal("IPOR"));
          } else {
            izlaz.setBigDecimal("OSN", izlaz.getBigDecimal("OSN").add(ns.getBigDecimal("OSN")));
            izlaz.setBigDecimal("IPOR", izlaz.getBigDecimal("IPOR").add(ns.getBigDecimal("IPOR")));
          }
        }
        articleMemoriy.put(new Integer(ns.getInt("CART")), ns.getBigDecimal("OSN"));
      } else {
        if (!ld.raLocate(izlaz,new String[]{"CGRART","CPOR"},new String[]{ns.getString("CGRART"), ns.getString("CPOR")})){
          izlaz.insertRow(false);
          izlaz.setString("CGRART", ns.getString("CGRART"));
          izlaz.setString("NAZGRART", ns.getString("NAZGRART"));
          izlaz.setString("CPOR", ns.getString("CPOR"));
          izlaz.setString("NAZPOR", ns.getString("NAZPOR"));
          izlaz.setBigDecimal("PPOR", ns.getBigDecimal("PPOR"));
          izlaz.setBigDecimal("OSN", ns.getBigDecimal("OSN"));
          izlaz.setBigDecimal("IPOR", ns.getBigDecimal("IPOR"));
        } else {
          izlaz.setBigDecimal("OSN", izlaz.getBigDecimal("OSN").add(ns.getBigDecimal("OSN")));
          izlaz.setBigDecimal("IPOR", izlaz.getBigDecimal("IPOR").add(ns.getBigDecimal("IPOR")));
        }
      }
    }
    
    HashMap groupMemory = new HashMap();
    HashMap groupPosition = new HashMap();
    for (izlaz.first();izlaz.inBounds();izlaz.next()){
      if (!groupMemory.containsKey(izlaz.getString("CGRART"))){
        groupMemory.put(izlaz.getString("CGRART"),izlaz.getBigDecimal("OSN"));
        groupPosition.put(izlaz.getString("CGRART"),new Integer(izlaz.getRow()));
      } else {
        BigDecimal osnovica = new BigDecimal(groupMemory.get(izlaz.getString("CGRART")).toString()).add(izlaz.getBigDecimal("OSN"));
        int tmp = izlaz.getRow();
        izlaz.setBigDecimal("OSN",osnovica);
        izlaz.goToRow(new Integer(groupPosition.get(izlaz.getString("CGRART")).toString()).intValue());
        izlaz.setBigDecimal("OSN",osnovica);
        izlaz.goToRow(tmp);
      }
    }
    
//    st.prn(izlaz);
  }
  
  private QueryDataSet srediPoreze(HashMap poreziArtikli, HashMap grupeArtikli, HashMap grupeNazivi, QueryDataSet prometi){
    QueryDataSet sredjeniPorezi = new QueryDataSet();
    sredjeniPorezi.setColumns(new Column[] {
        dm.getArtikli().getColumn("CART").cloneColumn(),
        dm.getGrupart().getColumn("CGRART").cloneColumn(),
        dm.getGrupart().getColumn("NAZGRART").cloneColumn(),
        dm.createStringColumn("CPOR","TBR",5),
        dm.createStringColumn("NAZPOR","Naziv poreza",50),
        dm.createBigDecimalColumn("PPOR","% poreza",2),
        dm.createBigDecimalColumn("OSN","Osnovica",2),
        dm.createBigDecimalColumn("IPOR","Iznos poreza",2)
    });
    sredjeniPorezi.open();
    
    for (prometi.first();prometi.inBounds();prometi.next()){
      if (poreziArtikli.get(new Integer(prometi.getInt("CART"))) == null) continue;
      
      if (poreziArtikli.get(new Integer(prometi.getInt("CART"))).toString().equals("1")){
        sredjeniPorezi.insertRow(false);
        
        sredjeniPorezi.setInt("CART", prometi.getInt("CART"));
        sredjeniPorezi.setString("CGRART",grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString());
        sredjeniPorezi.setString("NAZGRART",grupeNazivi.get(grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString()).toString());
        sredjeniPorezi.setString("CPOR","P22");
        sredjeniPorezi.setString("NAZPOR","Porez na dodatnu vrijednost");
        sredjeniPorezi.setBigDecimal("PPOR", new BigDecimal("22.00"));
        sredjeniPorezi.setBigDecimal("OSN", prometi.getBigDecimal("IPRODBP"));
        try {
        sredjeniPorezi.setBigDecimal("IPOR", prometi.getBigDecimal("POR1"));
        } catch (Exception e) {
          e.printStackTrace();
        }
        
      } else if (poreziArtikli.get(new Integer(prometi.getInt("CART"))).toString().equals("2")){
        sredjeniPorezi.insertRow(false);
        
        sredjeniPorezi.setInt("CART", prometi.getInt("CART"));
        sredjeniPorezi.setString("CGRART",grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString());
        sredjeniPorezi.setString("NAZGRART",grupeNazivi.get(grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString()).toString());
        
      } else if (poreziArtikli.get(new Integer(prometi.getInt("CART"))).toString().equals("3")){
        sredjeniPorezi.insertRow(false);

        sredjeniPorezi.setInt("CART", prometi.getInt("CART"));
        sredjeniPorezi.setString("CGRART",grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString());
        sredjeniPorezi.setString("NAZGRART",grupeNazivi.get(grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString()).toString());
        sredjeniPorezi.setString("CPOR","P22");
        sredjeniPorezi.setString("NAZPOR","Porez na dodatnu vrijednost");
        sredjeniPorezi.setBigDecimal("PPOR", new BigDecimal("22.00"));
        sredjeniPorezi.setBigDecimal("OSN", prometi.getBigDecimal("IPRODBP"));
        sredjeniPorezi.setBigDecimal("IPOR", prometi.getBigDecimal("POR1"));
        
        sredjeniPorezi.insertRow(false);

        sredjeniPorezi.setInt("CART", prometi.getInt("CART"));
        sredjeniPorezi.setString("CGRART",grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString());
        sredjeniPorezi.setString("NAZGRART",grupeNazivi.get(grupeArtikli.get(new Integer(prometi.getInt("CART"))).toString()).toString());
        sredjeniPorezi.setString("CPOR","P3");
        sredjeniPorezi.setString("NAZPOR","Porez na potrošnju");
        sredjeniPorezi.setBigDecimal("PPOR", new BigDecimal("3.00"));
        sredjeniPorezi.setBigDecimal("OSN", prometi.getBigDecimal("IPRODBP"));
        sredjeniPorezi.setBigDecimal("IPOR", prometi.getBigDecimal("POR2"));
      }
    }
    
    return sredjeniPorezi;
  }
  
  public boolean runFirstESC() {
    
    return false;
  }
  
  private void napuniNekeHashMape(){
    zaglavlje = new HashMap();
    zaglavlje.put("DOD",tds.getTimestamp("pocDatum"));
    zaglavlje.put("DDO",tds.getTimestamp("zavDatum"));
    zaglavlje.put("CUO",tds.getString("corg"));
    zaglavlje.put("NUO",jlrNazivOrg.getText());
  }
  
  private void xInit() throws Exception{
    this.setJPan(jp);
    
    tds.setColumns(new Column[] {
      dM.createTimestampColumn("pocDatum", "Poèetni datum"),
      dM.createTimestampColumn("zavDatum", "Završni datum"),
      dM.createStringColumn("corg", "Skladište", 12)
    });
    tds.open();
    
    
//    tds.setString("CORG","35");
    tds.setTimestamp("POCDATUM", ut.getFirstSecondOfDay(ut.getFirstDayOfYear()));
    tds.setTimestamp("ZAVDATUM", ut.getLastSecondOfDay(vl.getToday()));
    
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setDataSet(tds);
    
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setDataSet(tds);
    
    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazivOrg});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrFromCurrKnjig());
    jlrCorg.setNavButton(jbSelOrg);
    jlrCorg.setDataSet(this.tds);
    
    jlrNazivOrg.setColumnName("NAZIV");
    jlrNazivOrg.setNavProperties(jlrCorg);
    jlrNazivOrg.setSearchMode(1);

    xYLayout1.setWidth(656);
    xYLayout1.setHeight(80);
    jp.setLayout(xYLayout1);
    
    jp.add(new JLabel("Ugo. objekt"), new XYConstraints(15, 15, -1, -1));
    jp.add(jlrCorg, new XYConstraints(155, 15, -1, -1));
    jp.add(jlrNazivOrg, new XYConstraints(260, 15, 350, -1));
    jp.add(jbSelOrg, new XYConstraints(615, 15, 21, 21));
    jp.add(new JLabel("Period"), new XYConstraints(15, 40, -1, -1));
    jp.add(jtfPocDatum, new XYConstraints(155, 40, -1, -1));
    jp.add(jtfZavDatum, new XYConstraints(260, 40, -1, -1));
    
    addReport("hr.restart.robno.repUgostiteljstvoPorez","hr.restart.robno.repUgostiteljstvoPorez","Ugostiteljstvo01","");
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return true;
  }
  
  
}


// for (prometi.first();prometi.inBounds();prometi.next()){
// if (grupeArtikli.containsKey(new Integer(prometi.getInt("CART")))){
// if (ld.raLocate(izlaz, "CGRART",grupeArtikli.get(new
// Integer(prometi.getInt("CART"))).toString())){
//
// } else {} //nema
//
//
// } else continue;
// }
