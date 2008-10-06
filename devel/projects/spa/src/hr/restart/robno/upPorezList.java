/****license*****************************************************************
**   file: upPorezList.java
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

import hr.restart.baza.Vrdokum;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upPorezList extends raUpitLite {
  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel glavniPanel = new JPanel();
  JPanel panelJedan = new JPanel();

  boolean csklEnab, corgEnab, vrdokEnab;
  String oldCskl, oldCorg;
  static String ulazniUvjeti = "", izlazniUvjeti = "";

  private BorderLayout borderLayout1 = new BorderLayout();
  private XYLayout kickInTheTeeth = new XYLayout();

  TableDataSet tds = new TableDataSet();
  QueryDataSet porezSet = new QueryDataSet();
  QueryDataSet kumulPorezSet = new QueryDataSet();
  StorageDataSet sumistuff = new StorageDataSet();

  raComboBox rcbULIZ = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
//      setAllStrings();
      if (this.getSelectedIndex() == 1) ulazSelected();
      else if (this.getSelectedIndex() == 2)  izlazSelected();
      else sveSelected();
    }
  };

  JLabel jlDok = new JLabel();
  JlrNavField jlrDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  JlrNavField jlrNazDok = new JlrNavField() {
    public void after_lookUp() {
      dokChanged();
    }
  };
  JraButton jbDok = new JraButton();
  
  jpCorg jpc = new jpCorg(100, 350, true);

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazskl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();

  JLabel jlDatums = new JLabel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();

  static upPorezList upl;

  public upPorezList() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    upl = this;
  }

  public static upPorezList getInstance(){
    return upl;
  }

  public void componentShow() {
    tds.setTimestamp("PDAT", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
    tds.setTimestamp("ZDAT", vl.findDate(false,0));
    jlrCskl.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jlrCskl.forceFocLost();
    jpc.setCorg("");
    
    jpc.rcb.setSelectedIndex(1);
    
    rcbULIZ.setSelectedIndex(0);
    tds.setString("ULIZ","S");
    sveSelected();
    rcbULIZ.requestFocusLater();
  }

  boolean lupiEsc = false;

  public void okPress() {
    if (porezSet.isOpen()) porezSet.close();
    porezSet.setQuery(new QueryDescriptor(dm.getDatabase1(),getQueryString()));
    
    openDataSet(porezSet);
    
    porezSet.close();

    porezSet.getColumn("GOD").setRowId(true);
    porezSet.getColumn("CSKL").setRowId(true);
    porezSet.getColumn("VRDOK").setRowId(true);
    porezSet.getColumn("BRDOK").setRowId(true);
    porezSet.getColumn("ULIZ").setVisible(0);

    porezSet.open();
    
    if (porezSet.rowCount() == 0) setNoDataAndReturnImmediately();

    lupiEsc = true;
    pritokaMocna();
  }

  Variant v = new Variant();
  
  QueryDataSet vtztr, brrac, pnbz2;
  
  private void makeAddSets(){
    vtztr = ut.getNewQueryDataSet("SELECT cskl, vrdok, god, brdok, czt,  cpar, izt, prpor, brrac, datrac FROM VTZtr WHERE god ='"+
        ut.getYear(tds.getTimestamp("PDAT"))+"' and rbr = 0 and (cpar is not null and cpar != 0) order by cskl, vrdok, brdok, czt");
    
    pnbz2 = ut.getNewQueryDataSet("SELECT doki.cskl, doki.god, doki.brdok, doki.vrdok, doki.pnbz2 from doki  where " + 
        izlazniUvjeti + "and doki.datdok between '" + ut.getFirstSecondOfDay(tds.getTimestamp("PDAT")) + "' and '" + ut.getLastSecondOfDay(tds.getTimestamp("ZDAT")) + "' order by doki.cskl, doki.vrdok, doki.god, doki.brdok ");
    
    brrac = ut.getNewQueryDataSet("select  doku.god, doku.cskl, doku.vrdok, doku.brdok, doku.brrac from doku where " + 
        ulazniUvjeti + "and doku.datdok between '" + ut.getFirstSecondOfDay(tds.getTimestamp("PDAT")) + "' and '" + ut.getLastSecondOfDay(tds.getTimestamp("ZDAT")) + "' order by doku.cskl, doku.vrdok, doku.god, doku.brdok ");
  }
  
  private StorageDataSet finalSet;
  
  private void pritokaMocna(){
    finalSet = new StorageDataSet();
    finalSet.setColumns(new Column [] {
       dm.createStringColumn("CSKL",0),
       dm.createStringColumn("VRDOK",0),
       dm.createStringColumn("GOD",0),
       dm.createIntColumn("BRDOK"),
       dm.createShortColumn("RBRC"),
       dm.createTimestampColumn("DATUM"),
       dm.createIntColumn("CPAR"),
       dm.createBigDecimalColumn("ISP"),
       dm.createBigDecimalColumn("IBP"),
       dm.createBigDecimalColumn("PREPOR"),
       dm.createBigDecimalColumn("POR"),
       dm.createStringColumn("BROJ",0)
    });
    finalSet.open();
    
/*
    kumulPorSet = new StorageDataSet();
    kumulPorSet.setColumns(new Column [] {
       dm.createStringColumn("CSKL",0),
       dm.createStringColumn("VRDOK",0),
       dm.createStringColumn("GOD",0),
       dm.createIntColumn("BRDOK"),
       dm.createTimestampColumn("DATUM"),
       dm.createBigDecimalColumn("ISP"),
       dm.createBigDecimalColumn("IBP"),
       dm.createBigDecimalColumn("PREPOR"),
       dm.createBigDecimalColumn("POR"),
       dm.createStringColumn("BROJ",0)
    });
    kumulPorSet.open();
*/
    
    makeAddSets();
    java.math.BigDecimal 
    sumIsp = new java.math.BigDecimal("0.00"), 
    sumIbp = new java.math.BigDecimal("0.00"),
    sumPrpor = new java.math.BigDecimal("0.00"), 
    sumPor = new java.math.BigDecimal("0.00"),
    razPov = new java.math.BigDecimal("0.00"), 
    razPla = new java.math.BigDecimal("0.00");
    
    BigDecimal
    sumPrporzt = new java.math.BigDecimal("0.00"), 
    sumPorzt = new java.math.BigDecimal("0.00"),
    razPovzt = new java.math.BigDecimal("0.00"), 
    razPlazt = new java.math.BigDecimal("0.00");

    porezSet.first();
    lookupData ld = lookupData.getlookupData();
    
    do {
      checkClosing();
      BigDecimal por; 
      
      try {
      por = new BigDecimal(porezSet.getDouble("POR"));
      } catch (Exception e){
        por = porezSet.getBigDecimal("POR");
      }

      if (porezSet.getString("VRDOK").equals("IZD") && (por.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(Aus.zero2) == 0)) {
        continue;
      }
        
        
        
      finalSet.insertRow(false);
      finalSet.setString("CSKL",porezSet.getString("CSKL")); 
      finalSet.setString("VRDOK",porezSet.getString("VRDOK"));
      finalSet.setString("GOD",porezSet.getString("GOD"));
      finalSet.setInt("BRDOK",porezSet.getInt("BRDOK"));
      finalSet.setShort("RBRC",(short)0);
      finalSet.setTimestamp("DATUM",porezSet.getTimestamp("DATUM"));
      finalSet.setInt("CPAR",porezSet.getInt("CPAR"));
      try {
        finalSet.setBigDecimal("ISP",porezSet.getBigDecimal("ISP"));
      } catch (Exception e ){
        BigDecimal tmpBD = new BigDecimal(porezSet.getDouble("ISP"));
        tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
        finalSet.setBigDecimal("ISP",tmpBD);
      }
      try {
        finalSet.setBigDecimal("IBP",porezSet.getBigDecimal("IBP"));
      } catch (Exception e ){
        BigDecimal tmpBD = new BigDecimal(porezSet.getDouble("IBP"));
        tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
        finalSet.setBigDecimal("IBP",tmpBD);
      }
      finalSet.setBigDecimal("PREPOR",porezSet.getBigDecimal("PREPOR"));
      finalSet.setBigDecimal("POR",por);


      sumPrporzt = sumPrporzt.add(porezSet.getBigDecimal("PREPOR"));
      porezSet.getVariant("POR",v);
      sumPorzt = sumPorzt.add(v.getAsBigDecimal().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

      
      if (porezSet.getString("ULIZ").equals("I")){
        if (ld.raLocate(pnbz2,new String[] {"CSKL","VRDOK","GOD","BRDOK"},new String[] {porezSet.getString("CSKL"),porezSet.getString("VRDOK"),porezSet.getString("GOD"),porezSet.getInt("BRDOK")+""})){
          finalSet.setString("BROJ",pnbz2.getString("PNBZ2"));
        }
      } else {
        if (ld.raLocate(brrac,new String[] {"CSKL","VRDOK","GOD","BRDOK"},new String[] {porezSet.getString("CSKL"),porezSet.getString("VRDOK"),porezSet.getString("GOD"),porezSet.getInt("BRDOK")+""})){
          finalSet.setString("BROJ",brrac.getString("BRRAC"));
        }
      }
      
      if (porezSet.getString("ULIZ").equals("U")){
        vtztr.first();
        if (ld.raLocate(vtztr,new String[] {"CSKL","VRDOK","GOD","BRDOK"}, new String[] {porezSet.getString("CSKL"),
            porezSet.getString("VRDOK"),porezSet.getString("GOD"),porezSet.getInt("BRDOK")+""})){
          do {
            checkClosing();
            if (porezSet.getString("CSKL").equals(vtztr.getString("CSKL")) &&
                porezSet.getString("VRDOK").equals(vtztr.getString("VRDOK")) &&
                porezSet.getString("GOD").equals(vtztr.getString("GOD")) &&
                (porezSet.getInt("BRDOK") == vtztr.getInt("BRDOK"))){
              finalSet.insertRow(false);
              finalSet.setString("CSKL",vtztr.getString("CSKL")); 
              finalSet.setString("VRDOK",vtztr.getString("VRDOK"));
              finalSet.setString("GOD",vtztr.getString("GOD"));
              finalSet.setInt("BRDOK",vtztr.getInt("BRDOK"));
              finalSet.setShort("RBRC",vtztr.getShort("CZT"));
              finalSet.setTimestamp("DATUM",vtztr.getTimestamp("DATRAC"));
              finalSet.setInt("CPAR",vtztr.getInt("CPAR"));
              finalSet.setBigDecimal("ISP",vtztr.getBigDecimal("IZT").add(vtztr.getBigDecimal("PRPOR")));
              finalSet.setBigDecimal("IBP",vtztr.getBigDecimal("IZT"));
              finalSet.setBigDecimal("PREPOR",vtztr.getBigDecimal("PRPOR"));
              finalSet.setBigDecimal("POR",Aus.zero2);
              finalSet.setString("BROJ",vtztr.getString("BRRAC"));

              sumPrporzt = sumPrporzt.add(vtztr.getBigDecimal("PRPOR"));
              
            } else break;
          } while (vtztr.next());
        }
      }
      
    } while (porezSet.next());
    
    if (sumPorzt.subtract(sumPrporzt).compareTo(new java.math.BigDecimal("0.00")) > 0){
      razPlazt = sumPorzt.subtract(sumPrporzt);
    } else if (sumPorzt.subtract(sumPrporzt).compareTo(new java.math.BigDecimal("0.00")) < 0){
      razPovzt = (sumPorzt.subtract(sumPrporzt)).negate();
    }
    
    /*
     * Sljedeæa for petlja hendla izdatnice. 
     * Naime, zbog izdatnica - internih raèuna ovo je napravljeno da bi se 
     * odstranile normalne izdatnice koje nemaju u sebi sadržan porez.
     */

    boolean wasFirst = false;

    for (porezSet.first();porezSet.inBounds();porezSet.next()){
      
      if (wasFirst) {
        porezSet.goToRow(0);
        wasFirst = false;
      }
      
      BigDecimal por; 
      
      try {
      por = new BigDecimal(porezSet.getDouble("POR"));
      } catch (Exception e){
        por = porezSet.getBigDecimal("POR");
      }

      if (porezSet.getString("VRDOK").equals("IZD") && (
          por.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(Aus.zero2) == 0)){
        
        porezSet.deleteRow();
        if (porezSet.getRow() != 0){
          porezSet.goToRow(porezSet.getRow()-1); 
        } else {
          wasFirst = true;
        }
      }
    }
    
    
    porezSet.first();
    
    do {
      checkClosing();
      try {
      sumIsp = sumIsp.add(porezSet.getBigDecimal("ISP"));
      } catch (Exception e ){
        BigDecimal tmpBD = new BigDecimal(porezSet.getDouble("ISP"));
        tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
        sumIsp = sumIsp.add(tmpBD);
      }
      try {
      sumIbp = sumIbp.add(porezSet.getBigDecimal("IBP"));
      } catch (Exception e ){
        BigDecimal tmpBD = new BigDecimal(porezSet.getDouble("IBP"));
        tmpBD.setScale(2,BigDecimal.ROUND_HALF_UP);
        sumIbp = sumIbp.add(tmpBD);
      }
      sumPrpor = sumPrpor.add(porezSet.getBigDecimal("PREPOR"));
      porezSet.getVariant("POR",v);
      sumPor = sumPor.add(v.getAsBigDecimal().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
      
      if (porezSet.getString("ULIZ").equals("I")){
        if (ld.raLocate(pnbz2,new String[] {"CSKL","VRDOK","GOD","BRDOK"},new String[] {porezSet.getString("CSKL"),porezSet.getString("VRDOK"),porezSet.getString("GOD"),porezSet.getInt("BRDOK")+""})){
          porezSet.setString("BROJ",pnbz2.getString("PNBZ2"));
        }
      } else {
        if (ld.raLocate(brrac,new String[] {"CSKL","VRDOK","GOD","BRDOK"},new String[] {porezSet.getString("CSKL"),porezSet.getString("VRDOK"),porezSet.getString("GOD"),porezSet.getInt("BRDOK")+""})){
          porezSet.setString("BROJ",brrac.getString("BRRAC"));
        }
      }
      
    } while (porezSet.next());
    
    if (sumPor.subtract(sumPrpor).compareTo(new java.math.BigDecimal("0.00")) > 0){
      razPla = sumPor.subtract(sumPrpor);
    } else if (sumPor.subtract(sumPrpor).compareTo(new java.math.BigDecimal("0.00")) < 0){
      razPov = (sumPor.subtract(sumPrpor)).negate();
    }
    if (sumistuff.isOpen()) sumistuff.deleteAllRows();
    else sumistuff.open();
    sumistuff.setBigDecimal("SUMISP", sumIsp);
    sumistuff.setBigDecimal("SUMIBP", sumIbp);
    sumistuff.setBigDecimal("SUMPRPOR", sumPrpor);
    sumistuff.setBigDecimal("SUMPOR", sumPor);
    sumistuff.setBigDecimal("RAZPOV", razPov);
    sumistuff.setBigDecimal("RAZPLA", razPla);
    sumistuff.setBigDecimal("RAZPOVZT", razPovzt);
    sumistuff.setBigDecimal("RAZPLAZT", razPlazt);
    
    QueryDataSet tmp = porezSet;
    tmp.open();
    
    kumulateByDays(tmp);
  }
  
  private void kumulateByDays(QueryDataSet orginale){
    kumulPorezSet = new QueryDataSet();
    kumulPorezSet.setColumns(orginale.cloneColumns());
    kumulPorezSet.open();
    
    for (orginale.first();orginale.inBounds();orginale.next())
      orginale.setTimestamp("DATUM",ut.getFirstSecondOfDay(orginale.getTimestamp("DATUM")));
    
    orginale.setSort(new SortDescriptor(new String[] {"DATUM"}));
    
    for (orginale.first();orginale.inBounds();orginale.next()){
      if (orginale.atFirst()){
        kumulPorezSet.insertRow(false);
        kumulPorezSet.setTimestamp("DATUM",orginale.getTimestamp("DATUM"));
        try {
        kumulPorezSet.setBigDecimal("IBP",orginale.getBigDecimal("IBP"));
        kumulPorezSet.setBigDecimal("ISP",orginale.getBigDecimal("ISP"));
        } catch (Exception e) {
          kumulPorezSet.setDouble("IBP",orginale.getDouble("IBP"));
          kumulPorezSet.setDouble("ISP",orginale.getDouble("ISP"));
        }
        kumulPorezSet.setBigDecimal("PREPOR",orginale.getBigDecimal("PREPOR"));
        try{
          kumulPorezSet.setBigDecimal("POR",orginale.getBigDecimal("POR"));
        }catch (Exception e) {
          kumulPorezSet.setDouble("POR",orginale.getDouble("POR"));
        }
        continue;
      }
      
      if (kumulPorezSet.getTimestamp("DATUM").before(orginale.getTimestamp("DATUM"))){
        kumulPorezSet.insertRow(false);
        kumulPorezSet.setTimestamp("DATUM",orginale.getTimestamp("DATUM"));
        try {
          kumulPorezSet.setBigDecimal("IBP",orginale.getBigDecimal("IBP"));
          kumulPorezSet.setBigDecimal("ISP",orginale.getBigDecimal("ISP"));
          } catch (Exception e) {
            kumulPorezSet.setDouble("IBP",orginale.getDouble("IBP"));
            kumulPorezSet.setDouble("ISP",orginale.getDouble("ISP"));
          }
        kumulPorezSet.setBigDecimal("PREPOR",orginale.getBigDecimal("PREPOR"));
        try{
          kumulPorezSet.setBigDecimal("POR",orginale.getBigDecimal("POR"));
        }catch (Exception e) {
          kumulPorezSet.setDouble("POR",orginale.getDouble("POR"));
        }
      } else {
        try {
        kumulPorezSet.setBigDecimal("IBP",kumulPorezSet.getBigDecimal("IBP").add(orginale.getBigDecimal("IBP")));
        kumulPorezSet.setBigDecimal("ISP",kumulPorezSet.getBigDecimal("ISP").add(orginale.getBigDecimal("ISP")));
        } catch (Exception e) {
          kumulPorezSet.setDouble("IBP",kumulPorezSet.getDouble("IBP")+orginale.getDouble("IBP"));
          kumulPorezSet.setDouble("ISP",kumulPorezSet.getDouble("ISP")+orginale.getDouble("ISP"));
        }
        kumulPorezSet.setBigDecimal("PREPOR",kumulPorezSet.getBigDecimal("PREPOR").add(orginale.getBigDecimal("PREPOR")));
        try {
          kumulPorezSet.setDouble("POR",kumulPorezSet.getDouble("POR") + orginale.getDouble("POR"));
        } catch (Exception e) {
          kumulPorezSet.setBigDecimal("POR",kumulPorezSet.getBigDecimal("POR").add(orginale.getBigDecimal("POR")));
        }
      }
    }
  }

  public boolean Validacija() {
    tds.setTimestamp("PDAT",ut.getFirstSecondOfDay(tds.getTimestamp("PDAT")));

    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;

    return true;
  }

  private void setAllStrings(){
    String filterForCorgSet;
    if (jpc.getCorg().equals(""))
      filterForCorgSet = "Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'";
    else {
      filterForCorgSet = Aus.getKnjigCond().and(jpc.getCondition()).toString();
    }
    
    QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet(filterForCorgSet);
    
    tempSkl.open();

    String ulazniUvjetiS = "";
    String ulazniUvjetiF = "";
    String izlazniUvjetiS = "";
    String izlazniUvjetiF = "";
    String uldokumentiS = "doku.vrdok in (";
    String izdokumentiS = "doki.vrdok in (";
    String uldokumentiF = "doku.vrdok in (";
    String izdokumentiF = "doki.vrdok in (";
    
    //or vrdok = 'IZD'
    
    
    QueryDataSet allUlazS = Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'U' and TIPDOK in ('SF') and APP = 'robno'");
    QueryDataSet allIzlazS = Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'I' and TIPDOK in ('SF') and APP in ('robno','rac','pos','mp') or vrdok = 'IZD'");
    QueryDataSet allUlazF = Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'U' and TIPDOK in ('F') and APP = 'robno'");
    QueryDataSet allIzlazF = Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'I' and TIPDOK in ('F') and APP in ('robno','rac','pos','mp')");

    allUlazS.open();
    allIzlazS.open();
    allUlazF.open();
    allIzlazF.open();
    
    boolean ulazS = (allUlazS.rowCount() > 1);
    boolean izlazS = (allIzlazS.rowCount() > 1);
    boolean ulazF = (allUlazF.rowCount() > 1);
    boolean izlazF = (allIzlazF.rowCount() > 1);


    if (tds.getString("VRDOK").equals("")){
      if (ulazS){
        allUlazS.first();
        do {
          if (allUlazS.getRow() != allUlazS.getRowCount()-1){
            uldokumentiS += "'"+allUlazS.getString("VRDOK")+"', ";
          } else {
            uldokumentiS += "'"+allUlazS.getString("VRDOK")+"') ";
          }
        } while (allUlazS.next());
      }
      
      if (ulazF){
        allUlazF.first();
        do {
          if (allUlazF.getRow() != allUlazF.getRowCount()-1){
            uldokumentiF += "'"+allUlazF.getString("VRDOK")+"', ";
          } else {
            uldokumentiF += "'"+allUlazF.getString("VRDOK")+"') ";
          }
        } while (allUlazF.next());
      }
      
      if (izlazS){
        allIzlazS.first();
        do {
          if (allIzlazS.getRow() != allIzlazS.getRowCount()-1){
            izdokumentiS += "'"+allIzlazS.getString("VRDOK")+"', ";
          } else {
            izdokumentiS += "'"+allIzlazS.getString("VRDOK")+"') ";
          }
        } while (allIzlazS.next());
      }
      
      if (izlazF){
        allIzlazF.first();
        do {
          if (allIzlazF.getRow() != allIzlazF.getRowCount()-1){
            izdokumentiF += "'"+allIzlazF.getString("VRDOK")+"', ";
          } else {
            izdokumentiF += "'"+allIzlazF.getString("VRDOK")+"') ";
          }
        } while (allIzlazF.next());
      }
    } else {
      uldokumentiS = " doku.vrdok = '"+ tds.getString("VRDOK") + "' ";
      uldokumentiF = " doku.vrdok = '"+ tds.getString("VRDOK") + "' ";
      izdokumentiS = " doki.vrdok = '"+ tds.getString("VRDOK") + "' ";
      izdokumentiF = " doki.vrdok = '"+ tds.getString("VRDOK") + "' ";
    }
    
    if (jpc.getCorg().equals("")) {
      ulazniUvjetiF += " doku.cskl = '"+hr.restart.zapod.OrgStr.getKNJCORG()+"' ";
      izlazniUvjetiF += " doki.cskl = '"+hr.restart.zapod.OrgStr.getKNJCORG()+"' ";
    } else {    
      ulazniUvjetiF += " doku.cskl"+ jpc.getCondition().toString().substring(4) + " ";
      izlazniUvjetiF += " doki.cskl"+ jpc.getCondition().toString().substring(4) + " ";
    } 
    
    if (!tds.getString("CSKL").equals("")){
      ulazniUvjetiS += " doku.cskl ='"+tds.getString("CSKL")+"' ";
      izlazniUvjetiS += " doki.cskl ='"+tds.getString("CSKL")+"' ";
    } else {
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next())  {
          skls += ",";
        }
        else {
          break;
        }
      } while (true);
      
      skls += ")";
      
      ulazniUvjetiS += " doku.cskl in "+ skls + " ";
      izlazniUvjetiS += " doki.cskl in "+ skls + " ";
    }
    
    if (ulazS){
      if (ulazF){
        if (jpc.getCorg().equals("")){
          if (tds.getString("CSKL").equals("")) ulazniUvjeti = " (("+ulazniUvjetiS+" and "+ uldokumentiS+") or ("+ulazniUvjetiF+" and "+ uldokumentiF+")) ";
          else ulazniUvjeti = " ("+ulazniUvjetiS+" and "+ uldokumentiS+") ";
        } else {
          if (tds.getString("CSKL").equals("")) ulazniUvjeti = " ("+ulazniUvjetiF+" and "+ uldokumentiF+") ";
        }
      } else {
        ulazniUvjeti = " ("+ulazniUvjetiS+" and "+ uldokumentiS+") ";
      }
    } else {
      if (ulazF){
        ulazniUvjeti = " ("+ulazniUvjetiF+" and "+ uldokumentiF+") ";
      } else {
        ulazniUvjeti = "1=1";
      }
    }
    
    if (izlazS){
      if (izlazF){
        if (jpc.getCorg().equals("")){
          if (tds.getString("CSKL").equals("")) izlazniUvjeti = " (("+izlazniUvjetiS+" and "+ izdokumentiS+") or ("+izlazniUvjetiF+" and "+ izdokumentiF+")) ";
          else izlazniUvjeti = " ("+izlazniUvjetiS+" and "+ izdokumentiS+") ";
        } else {
          if (tds.getString("CSKL").equals("")) izlazniUvjeti = " ("+izlazniUvjetiF+" and "+ izdokumentiF+") ";
        }
      } else {
        izlazniUvjeti = " ("+izlazniUvjetiS+" and "+ izdokumentiS+") ";
      }
    } else {
      if (izlazF){
        izlazniUvjeti = " ("+izlazniUvjetiF+" and "+ izdokumentiF+") ";
      } else {
        izlazniUvjeti = "1=1";
      }
    }
    if (izlazniUvjeti.length() == 0) izlazniUvjeti = "1=1";
    if (ulazniUvjeti.length() == 0) ulazniUvjeti = "1=1";
  }

  private String getQueryString(){
    setAllStrings();
    
    String qs = "";

    String izlazi = "select  max(doki.cpar) as cpar, max(doki.god) as GOD, max(doki.cskl) as CSKL, max(doki.datdok) as DATUM, " +
    				"max(doki.vrdok) as VRDOK, max(stdoki.brdok) as BRDOK, "+
                    "sum(stdoki.iprodbp) as IBP, sum(stdoki.iprodsp) as ISP, (0.0) as PREPOR, "+
                    "sum(stdoki.por1 + stdoki.por2 + stdoki.por3) as POR, '' as broj, " +
                    "'I' as uliz ";
    
    String ulazi = 	"select  max(doku.cpar) as cpar, max(doku.god) as GOD, max(doku.cskl) as CSKL, max(doku.datdok) as DATUM, " +
    				"max(doku.vrdok) as VRDOK, "+
    				"max(stdoku.brdok) as BRDOK, "+
    				"sum(stdoku.idob - stdoku.irab) as IBP, (sum(stdoku.idob - stdoku.irab) + " +
    				"max(doku.uiprpor)) as ISP, max(doku.uiprpor) as PREPOR , " +
    				"(0.0) as POR, '' as broj, " +
    				"'U' as uliz ";

    String groupUlazi = " group by doku.cskl, doku.vrdok, doku.god, doku.brdok ";
    String groupIzlazi = " group by doki.cskl, doki.vrdok, doki.god, doki.brdok ";

    String datRangeUlaz = "and doku.datdok between '" + ut.getFirstSecondOfDay(tds.getTimestamp("PDAT")) + "' and '" + ut.getLastSecondOfDay(tds.getTimestamp("ZDAT")) + "' ";
    String datRangeIzlaz = "and doki.datdok between '" + ut.getFirstSecondOfDay(tds.getTimestamp("PDAT")) + "' and '" + ut.getLastSecondOfDay(tds.getTimestamp("ZDAT")) + "' ";

    String vezaUlzlaz = "and " + rut.getDoc("DOKU","STDOKU");
    String vezaIzlaz = "and " + rut.getDoc("DOKI","STDOKI");

    String union = " union all ";

    if (tds.getString("ULIZ").equals("S")){
      qs = izlazi + " from doki, stdoki where " + izlazniUvjeti + datRangeIzlaz + vezaIzlaz + groupIzlazi +
      	   union +
           ulazi + " from doku, stdoku where " + ulazniUvjeti + datRangeUlaz + vezaUlzlaz + groupUlazi;
      
    } else if (tds.getString("ULIZ").equals("U")){
      qs = ulazi +  " from doku, stdoku where " + ulazniUvjeti + datRangeUlaz + vezaUlzlaz + groupUlazi;
    } else if (tds.getString("ULIZ").equals("I")){
      qs = izlazi + " from doki, stdoki where " + izlazniUvjeti + datRangeIzlaz + vezaIzlaz + groupIzlazi;
    }
    
    System.out.println("QUERY:\n"+qs+"\n");
    return qs;
  }

  public void firstESC() {
    rcc.EnabDisabAll(getJPan(),true);
    
//    setAllStrings();
    if (rcbULIZ.getSelectedIndex() == 1) {
      ulazSelected();
    } else if (rcbULIZ.getSelectedIndex() == 2) {
      izlazSelected();
    } else {
      sveSelected();
    }
    
    rcbULIZ.requestFocusLater();
    lupiEsc = false;
  }

  public boolean runFirstESC() {
    if (lupiEsc) return true;
    return false;
  }

  private void jbInit() throws Exception {
//    addReport("hr.restart.robno.repPorList", "hr.restart.robno.repPorList", "PorezList", "Ispis liste poreza");

    addReport("hr.restart.robno.repPorList", "hr.restart.robno.repPorList", "PorezListT", "Ispis liste poreza");
    addReport("hr.restart.robno.repPorList", "hr.restart.robno.repPorListKumul", "PorezListKumul", "Ispis liste poreza kumulativno");
    addReport("hr.restart.robno.repPorList", "hr.restart.robno.repPorListZT", "PorezListD", "Ispis liste poreza sa zavisnim troškovima");
    
    tds.setColumns(new Column[] {
        dm.createTimestampColumn("PDAT", "Po\u010Detni datum"),
        dm.createTimestampColumn("ZDAT", "Završni datum"),
        dm.createStringColumn("ULIZ", "Ulaz/Izlaz", "I", 1),
        dm.createStringColumn("VRDOK", "Vrsta dokumenta", 3),
        dm.createStringColumn("CSKL", "Skladište", 12),
        dm.createStringColumn("CORG", "Org. jedinica", 12)
    });
    tds.open();
    sumistuff.setColumns(new Column[] {
      dm.createBigDecimalColumn("SUMISP",2),
      dm.createBigDecimalColumn("SUMIBP",2),
      dm.createBigDecimalColumn("SUMPRPOR",2),
      dm.createBigDecimalColumn("SUMPOR",2),
      dm.createBigDecimalColumn("RAZPOV",2),
      dm.createBigDecimalColumn("RAZPLA",2),
      dm.createBigDecimalColumn("RAZPOVZT",2),
      dm.createBigDecimalColumn("RAZPLAZT",2)
    });

    rcbULIZ.setRaDataSet(this.tds);
    rcbULIZ.setRaColumn("ULIZ");
    rcbULIZ.setRaItems(new String[][] {
      {"Svi", "S"},
      {"Ulazni", "U"},
      {"Izlazni", "I"}
    });
    
    jlDok.setText("Dokumenti");

    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setDataSet(this.tds);
    jlrDok.setColNames(new String[] {"NAZDOK"});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok});
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setSearchMode(0);
    jlrDok.setRaDataSet(Vrdokum.getDataModule().getFilteredDataSet("TIPDOK in ('F','SF') and APP in ('robno','rac','pos','mp') or vrdok = 'IZD' order by vrdok"));
    jlrDok.setNavButton(jbDok);

    jlrNazDok.setNavProperties(jlrDok);
    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setSearchMode(1);
    
    jlCskl.setText("Skladište");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rut.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    jlDatums.setText("Datum (od - do)");

    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("PDAT");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("ZDAT");
    jtfZavDatum.setDataSet(tds);

    glavniPanel.setLayout(borderLayout1);
    kickInTheTeeth.setWidth(645);
    kickInTheTeeth.setHeight(130);
    panelJedan.setLayout(kickInTheTeeth);

    this.setJPan(glavniPanel);

    panelJedan.add(jlDok, new XYConstraints(15, 20, -1, -1));
    panelJedan.add(rcbULIZ, new XYConstraints(150, 20, 100, -1));
    panelJedan.add(jlrDok, new XYConstraints(255, 20, 50, -1));
    panelJedan.add(jlrNazDok, new XYConstraints(310, 20, 295, -1));
    panelJedan.add(jbDok, new XYConstraints(610, 20, 21, 21));
    
    panelJedan.add(jpc, new XYConstraints(0, 45, -1, -1));

    panelJedan.add(jlCskl, new XYConstraints(15, 70, -1, -1));    // was 70 all
    panelJedan.add(jlrCskl, new XYConstraints(150, 70, 100, -1));
    panelJedan.add(jlrNazskl, new XYConstraints(255, 70, 350, -1));
    panelJedan.add(jbSelCskl, new XYConstraints(610, 70, 21, 21));

    panelJedan.add(jlDatums, new XYConstraints(15, 95, -1, -1));     // was 95 all
    panelJedan.add(jtfPocDatum, new XYConstraints(150, 95, 100, -1));
    panelJedan.add(jtfZavDatum, new XYConstraints(255, 95, 100, -1));

    new raDateRange(jtfPocDatum, jtfZavDatum);

    glavniPanel.add(panelJedan, BorderLayout.CENTER);
  }

  void sveSelected() {
    enabSkladCorg(true, false);
  }

  void ulazSelected() {
    enabSkladCorg(true, true);
    jlrDok.setRaDataSet(Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'U' and TIPDOK in ('F','SF') and APP = 'robno' order by vrdok"));
//    jlrDok.setText("");
//    jlrDok.emptyTextFields();
  }

  void izlazSelected() {
    enabSkladCorg(true, true);
    jlrDok.setRaDataSet(Vrdokum.getDataModule().getFilteredDataSet("VRSDOK = 'I' and TIPDOK in ('F','SF') and APP in ('robno','rac','pos','mp') or vrdok = 'IZD' order by vrdok"));
//    jlrDok.setText("");
//    jlrDok.emptyTextFields();
  }

  void enabSkladCorg(boolean sklad, boolean vrdok) {
    enabSklad(sklad);
//    enabCorg(corg);
    enabVrdok(vrdok);
  }

  void enabSklad(boolean ocunecu) {
    if (ocunecu == csklEnab) return;
    csklEnab = ocunecu;
    if (!csklEnab) {
      oldCskl = tds.getString("CSKL");
      jlrCskl.setText("");
      jlrCskl.emptyTextFields();
    }
    rcc.setLabelLaF(jlrCskl, csklEnab);
    rcc.setLabelLaF(jlrNazskl, csklEnab);
    rcc.setLabelLaF(jbSelCskl, csklEnab);
    if (ocunecu && oldCskl != null) {
      jlrCskl.setText(oldCskl);
      jlrCskl.forceFocLost();
    }
  }

  void enabVrdok(boolean ocunecu){
    vrdokEnab = ocunecu;
    if (!vrdokEnab){
      jlrDok.setText("");
      jlrDok.emptyTextFields();
    }
    rcc.setLabelLaF(jlrDok, vrdokEnab);
    rcc.setLabelLaF(jlrNazDok, vrdokEnab);
    rcc.setLabelLaF(jbDok, vrdokEnab);
  }

  void dokChanged() {
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public DataSet getReportSet(){
    return porezSet;
  }
  
  public DataSet getReportSetKumul(){
    return kumulPorezSet;
  }

  public DataSet getReportSetZT(){
    return finalSet; //porezSet;
  }

  public DataSet getSumSet(){
    return sumistuff;
  }

  public java.sql.Timestamp getDatumOD(){
    return tds.getTimestamp("PDAT");
  }

  public java.sql.Timestamp getDatumDO(){
    return tds.getTimestamp("ZDAT");
  }

  public String getSklad(){
    return tds.getString("CSKL");
  }

  public String getSUI(){
    return tds.getString("ULIZ");
  }
}

/*
Speculative strategy - use warlock ss and pally/shaman 
DI to do 2 separate death runs to cut through even more mobs.

I've attached a map (edited from the maps already posted in this thread) 
with my suggested route of where you would run through mobs and then soulstone or DI to cut down on time.

http://i35.photobucket.com/albums/d169/Prydaine/gauntlet2.jpg

Since it's so hard for me to get a group together for this quest 
on my server, if anyone tries this method - please respond on this site 
with how/if it works and how much time was saved.
Thank you.
*/