/****license*****************************************************************
**   file: UpUlaziZavTr.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2005.02.05
 * 
 */

public class UpUlaziZavTr extends raUpitLite {
  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();

  JPanel glavniPanel = new JPanel();
  private XYLayout xyLay = new XYLayout();

  TableDataSet tds = new TableDataSet();
  QueryDataSet qds = new QueryDataSet();

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazSkl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();

  JLabel jlDatum = new JLabel();
  JraTextField pocDat = new JraTextField();
  JraTextField zavDat = new JraTextField();
  
  String doki_stdoki, vtztr, vzt;
  QueryDataSet reportSet = new QueryDataSet();
  QueryDataSet rekapSet = new QueryDataSet();
  QueryDataSet rawZtSet;
  sysoutTEST st = new sysoutTEST(false);

  public UpUlaziZavTr() {
    try {
      initilizer();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  static UpUlaziZavTr instanceOfMe;
  
  public static UpUlaziZavTr getInstance() {
    return instanceOfMe;
  }

  public void componentShow() {
    setDefolt();
  }

  private void setDefolt(){
    tds.setTimestamp("PDAT", ut.getFirstDayOfMonth()); // ut.getYearBegin("2004"));// 
    tds.setTimestamp("ZDAT", vl.getToday()); // ut.getYearEnd("2004")); // 
//    tds.setString("CSKL",hr.restart.zapod.OrgStr.getKNJCORG());
    jlrCskl.forceFocLost();
  }
  
  private void initilizer() throws Exception {
    this.addReport("hr.restart.robno.repUlaziZavTros","hr.restart.robno.repUlaziZavTros","UlaziZavTros","Rekapitulacija ulaza po vrstama dokumenata");
    tds.setColumns(new Column[]{
        dm.createTimestampColumn("PDAT", "Po\u010Detni datum"), 
        dm.createTimestampColumn("ZDAT", "Završni datum"), 
        dm.createStringColumn("CSKL", "Skladište", 12)
    });
    tds.open();

    jlCskl.setText("Skladište");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColNames(new String[]{"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[]{jlrNazSkl});
    jlrCskl.setVisCols(new int[]{0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrCskl);
    jlrNazSkl.setSearchMode(1);

    jlDatum.setText("Datum (od - do)");
    pocDat.setDataSet(tds);
    pocDat.setColumnName("PDAT");
    zavDat.setDataSet(tds);
    zavDat.setColumnName("ZDAT");

    glavniPanel.setLayout(xyLay);
    xyLay.setWidth(645);
    xyLay.setHeight(85);
    this.setJPan(glavniPanel);

    glavniPanel.add(jlCskl, new XYConstraints(15, 20, -1, -1));
    glavniPanel.add(jlrCskl, new XYConstraints(150, 20, 100, -1));
    glavniPanel.add(jlrNazSkl, new XYConstraints(255, 20, 350, -1));
    glavniPanel.add(jbSelCskl, new XYConstraints(610, 20, 21, 21));

    glavniPanel.add(jlDatum, new XYConstraints(15, 45, 100, -1));
    glavniPanel.add(pocDat, new XYConstraints(150, 45, 100, -1));
    glavniPanel.add(zavDat, new XYConstraints(255, 45, 100, -1));

  }

  public boolean Validacija(){
    boolean povratak = true;
    if (!vl.isValidRange(pocDat,zavDat)) {
      povratak = false;
    }
    if (!povratak) runFirstESC();
    return povratak;
  }

  boolean lupiEsc = false;

  public void okPress() {
    QueryDataSet dokiStdoki = ut.getNewQueryDataSet(getDokiStdoki());
    if (dokiStdoki.isEmpty()) setNoDataAndReturnImmediately();
    
    lupiEsc = true;
    
    reportSet = makeRepSet(dokiStdoki);
    
    QueryDataSet ztrRawSet = ut.getNewQueryDataSet(getVztr(tds.getString("CSKL")));

    rawZtSet = makeRawZavtrSet(ztrRawSet, dokiStdoki);

    rekapSet = makeZavTrRekap(ztrRawSet,dokiStdoki);
  }
  
  private String getSklFromCorg(){
    String sklfcorg = "(";
    QueryDataSet skladsFromCorg = hr.restart.robno.Util.getUtil().getSkladFromCorg();
    skladsFromCorg.first();
    
    for (;;){
      sklfcorg += "'"+skladsFromCorg.getString("CSKL")+"'";
      if (skladsFromCorg.next()) sklfcorg += ", ";
      else {
        sklfcorg += ")";
       break; 
      }
    }
    return sklfcorg;
  }
  
  private String getDokiStdoki(){
    
    String dc = "";
    if (!tds.getString("CSKL").equals("")){
      dc += "='"+tds.getString("CSKL")+"'";
    } else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      dc += " in "+skls+" ";
    }
    
   return "SELECT doku.cskl, doku.god, doku.brdok, doku.vrdok, doku.cpar, doku.datdok, stdoku.rbr, stdoku.idob, stdoku.irab, stdoku.izt, stdoku.inab FROM doku, stdoku WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok " +
   		"AND ((doku.cskl='"+ hr.restart.zapod.OrgStr.getKNJCORG() +"' and stdoku.csklart " + dc + " and doku.vrdok = 'KAL') " +
   		"OR (doku.cskl " + dc + " and doku.vrdok = 'PRK')) " +
   		"AND " + Condition.between("DOKU.DATDOK",tds.getTimestamp("PDAT"),tds.getTimestamp("ZDAT")) + " " +
   		"order by doku.datdok";
  }
  
  private int minPRK, maxPRK;
  private int minKAL, maxKAL;
  
  private String getVztr(String cskl){
    
    String _cskl = "vtztr.cskl ";
    
    if (!cskl.equals("")) _cskl += "= '"+cskl+"'";
    else {
      
      QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      tempSkl.open();
      tempSkl.first();
      String skls = "(";
      
      do {
        skls += "'"+tempSkl.getString("CSKL")+"'";
        if (tempSkl.next()) skls += ",";
        else break;
      } while (true);
      
      skls += ")";
      
      _cskl += " in "+skls+" ";
    }
    
    return "SELECT vtztr.cskl, vtztr.god, vtztr.brdok, vtztr.vrdok, vtztr.rbr, vtztr.czt, ztr.nzt, vtztr.izt " +
    		"FROM vtztr, Ztr " +
    		"WHERE vtztr.czt = ztr.czt " +
    		"AND god = '" + ut.getYear(tds.getTimestamp("ZDAT")) + "' " +
    		
    		"and ((vtztr.vrdok = 'KAL' and vtztr.cskl = '" + hr.restart.zapod.OrgStr.getKNJCORG() + "' and vtztr.brdok between "+minKAL+" and "+maxKAL+") " +
    				"or (vtztr.vrdok = 'PRK' and " + _cskl + " and vtztr.brdok between "+minPRK+" and "+maxPRK+"))" +
    		
    		"and vtztr.rbr != 0 order by vtztr.vrdok, vtztr.brdok, vtztr.czt";
  }
  
  private QueryDataSet makeRawZavtrSet(QueryDataSet ztr, QueryDataSet dsd){
    QueryDataSet temp = new QueryDataSet();
    temp.setColumns(new Column[]{
        dm.createStringColumn("CSKL",12),
        dm.createStringColumn("VRDOK",4),
        dm.createStringColumn("GOD",5),
        dm.createIntColumn("BRDOK"),
        dm.createShortColumn("CZT"),
        dm.createStringColumn("NZT",30),
        dm.createBigDecimalColumn("IZT",2),
    });
    temp.open();
    ztr.first();
    do { 
      if (!ld.raLocate(
          dsd,
          new String[]{
              "CSKL",
              "VRDOK",
              "GOD",
              "BRDOK",
              "RBR"},
          new String[]{
              ztr.getString("CSKL"),
              ztr.getString("VRDOK"),
              ztr.getString("GOD"),
              ztr.getInt("BRDOK")+"",
              ztr.getShort("RBR")+""})) continue;
      
      if (ld.raLocate(
          temp,
          new String[]{
              "CSKL",
              "VRDOK",
              "GOD",
              "BRDOK",
              "CZT"},
          new String[]{
              ztr.getString("CSKL"),
              ztr.getString("VRDOK"),
              ztr.getString("GOD"),
              ztr.getInt("BRDOK")+"",
              ztr.getShort("CZT")+""})){
        
        temp.setBigDecimal("IZT",temp.getBigDecimal("IZT").add(ztr.getBigDecimal("IZT")));
        
      } else {
        temp.insertRow(false);
        temp.setString("CSKL",ztr.getString("CSKL"));
        temp.setString("VRDOK",ztr.getString("VRDOK"));
        temp.setString("GOD",ztr.getString("GOD"));
        temp.setInt("BRDOK", ztr.getInt("BRDOK"));
        temp.setShort("CZT", ztr.getShort("CZT"));
        temp.setString("NZT", ztr.getString("NZT"));
        temp.setBigDecimal("IZT", ztr.getBigDecimal("IZT"));
      }
    } while (ztr.next());
    
    
    return temp;
  }
  
  
  private QueryDataSet makeRepSet (QueryDataSet dstd){
    QueryDataSet temp = new QueryDataSet();
    temp.setColumns(new Column[]{
        dm.createStringColumn("CSKL",12),
        dm.createStringColumn("VRDOK",4),
        dm.createStringColumn("GOD",5),
        dm.createIntColumn("BRDOK"),
        dm.createStringColumn("BROJ",30),
        dm.createTimestampColumn("DATDOK"),
        dm.createIntColumn("CPAR"),
        dm.createBigDecimalColumn("IDOB",2),
        dm.createBigDecimalColumn("IZT",2),
        dm.createBigDecimalColumn("INAB",2),
    });
    
    dstd.first();
    boolean firstPRK = true;
    boolean firstKAL = true;
    
    do {
      if (ld.raLocate(
          temp,
          new String[]{
              "CSKL",
              "VRDOK",
              "GOD",
              "BRDOK"},
          new String[]{
              dstd.getString("CSKL"),
              dstd.getString("VRDOK"),
              dstd.getString("GOD"),
              dstd.getInt("BRDOK")+""})){
        
//        if (!dstd.getString("CSKL").equals(tds.getString("CSKL"))) continue;
        
        temp.setBigDecimal("IDOB",temp.getBigDecimal("IDOB").add(dstd.getBigDecimal("IDOB").subtract(dstd.getBigDecimal("IRAB"))));
        temp.setBigDecimal("IZT",temp.getBigDecimal("IZT").add(dstd.getBigDecimal("IZT")));
        temp.setBigDecimal("INAB",temp.getBigDecimal("INAB").add(dstd.getBigDecimal("INAB")));
        
        
        
      } else {
        
        if (dstd.getString("VRDOK").equals("PRK")){
          if (firstPRK){
            firstPRK = false;
            minPRK = dstd.getInt("BRDOK");
            maxPRK = dstd.getInt("BRDOK");
          } else {
          if (dstd.getInt("BRDOK") < minPRK) minPRK = dstd.getInt("BRDOK");
          if (dstd.getInt("BRDOK") > maxPRK) maxPRK = dstd.getInt("BRDOK");
          }
        } else {
          
//          if (!dstd.getString("CSKL").equals(tds.getString("CSKL"))) continue;
          
          if (firstKAL){
            firstKAL = false;
            minKAL = dstd.getInt("BRDOK");
            maxKAL = dstd.getInt("BRDOK");
          } else {
          if (dstd.getInt("BRDOK") < minKAL) minKAL = dstd.getInt("BRDOK");
          if (dstd.getInt("BRDOK") > maxKAL) maxKAL = dstd.getInt("BRDOK");
          }
        }
        
        temp.insertRow(false);
        temp.setString("CSKL",dstd.getString("CSKL"));
        temp.setString("VRDOK",dstd.getString("VRDOK"));
        temp.setString("GOD",dstd.getString("GOD"));
        temp.setInt("BRDOK", dstd.getInt("BRDOK"));
        
//        temp.setString("BROJ",
//            dstd.getString("CSKL") + "-" + 
//            dstd.getString("VRDOK") + "-" +
//            dstd.getString("GOD").substring(2,4) + "-" + 
//            vl.maskZeroInteger(Integer.valueOf(String.valueOf(dstd.getInt("BRDOK"))), 4));
        
        temp.setString("BROJ",repUtil.getFormatBroj(dstd.getString("CSKL"),dstd.getString("VRDOK"),
            dstd.getString("GOD"),dstd.getInt("BRDOK")));
        
        temp.setTimestamp("DATDOK",dstd.getTimestamp("DATDOK"));
        temp.setInt("CPAR",dstd.getInt("CPAR"));
        temp.setBigDecimal("IDOB",dstd.getBigDecimal("IDOB").subtract(dstd.getBigDecimal("IRAB")));
        temp.setBigDecimal("IZT",dstd.getBigDecimal("IZT"));
        temp.setBigDecimal("INAB",dstd.getBigDecimal("INAB"));
        
      }
    } while (dstd.next());
    
    return temp;
  }
  
  private QueryDataSet makeZavTrRekap(QueryDataSet ztr, QueryDataSet dstd){
    if (ztr.isEmpty()) return null;
    QueryDataSet temp = new QueryDataSet();
    temp.setColumns(new Column[]{
        dm.createShortColumn("CZT"),
        dm.createStringColumn("NZT",30),
        dm.createBigDecimalColumn("IZT",2),
    });
    
    ztr.first();
    do {
      if (!ld.raLocate(
          dstd,
          new String[]{
              "CSKL",
              "VRDOK",
              "GOD",
              "BRDOK",
              "RBR"},
          new String[]{
              ztr.getString("CSKL"),
              ztr.getString("VRDOK"),
              ztr.getString("GOD"),
              ztr.getInt("BRDOK")+"",
              ztr.getShort("RBR")+""})) continue;
      
      if (ld.raLocate(temp,new String[] {"CZT"}, new String[] {ztr.getShort("CZT")+""})){
        temp.setBigDecimal("IZT",temp.getBigDecimal("IZT").add(ztr.getBigDecimal("IZT")));
      } else {
        temp.insertRow(false);
        temp.setShort("CZT", ztr.getShort("CZT"));
        temp.setString("NZT", ztr.getString("NZT"));
        temp.setBigDecimal("IZT", ztr.getBigDecimal("IZT"));
      }
    
    } while (ztr.next());
    
    temp.close();
    temp.setSort(new SortDescriptor(new String[] {"CZT"}));
    temp.open();
    
    System.out.println("Row Count " + temp.getRowCount());
    System.out.println("Is empty " + temp.isEmpty());
    
    if (temp.getRowCount() == 0) return null;
    
    return temp;
  }
  
  public QueryDataSet getReportSet() {
    return reportSet;
  }
  
  public QueryDataSet getRekapSet() {
    return rekapSet;
  }
  
  public List getZavTr(String cskl, String god, int brdok, String vrdok){
   List tempList = new ArrayList();
   rawZtSet.first();
   
   do {
     if (rawZtSet.getString("CSKL").equals(cskl) &&
         rawZtSet.getString("GOD").equals(god) &&
         rawZtSet.getInt("BRDOK") == brdok &&
         rawZtSet.getString("VRDOK").equals(vrdok)){
       String[] tempArray = new String [3];
       	tempArray[0] = rawZtSet.getShort("CZT") + "";
        tempArray[1] = rawZtSet.getString("NZT");
        tempArray[2] = sgQuerys.getSgQuerys().format(rawZtSet.getBigDecimal("IZT"),2);
        tempList.add(tempArray);
     };
   } while (rawZtSet.next());
   
   return tempList;
  }

  public java.sql.Timestamp getDatumOd(){
    return tds.getTimestamp("PDAT");
  }

  public java.sql.Timestamp getDatumDo(){
    return tds.getTimestamp("ZDAT");
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    jlrCskl.setText("");
    jlrCskl.emptyTextFields();
    lupiEsc = false;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCskl.requestFocus();
      }
    });
  }

  public boolean runFirstESC() {
    return lupiEsc;
  }
  
  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return true;
  }
  
  public String getCSKL(){
    return tds.getString("CSKL");
  }
  
  public String getNazSkl(){
    return jlrNazSkl.getText();
  }
}
