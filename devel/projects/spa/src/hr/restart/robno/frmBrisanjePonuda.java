/****license*****************************************************************
**   file: frmBrisanjePonuda.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmBrisanjePonuda extends raTwoTableFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid val= Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  dM dm = dM.getDataModule();
  
  private final String vr = "PON";
  
  JPanel jpUpit = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  
  TableDataSet fieldSet = new TableDataSet();
  
  JLabel jlDatumrange = new JLabel("Period OD-DO");
  JraTextField jtfPocDat = new JraTextField();
  JraTextField jtfZavDat = new JraTextField();
  
  jpCorg corging = new jpCorg(){
    public void afterLookUp(boolean succ) {
//      System.out.println("corging afterLookUp succesfulnes is "+succ);
      if (succ) {
        fieldSet.setString("CORG",corging.getCorg());
        jtfPocDat.requestFocus();
      }
      else fieldSet.setString("CORG","");
//      printDataset(fieldSet);
    }
  };
  
  public frmBrisanjePonuda(){
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    getJdialog().setModal(false);
    fieldSet.setColumns(new Column[] {
        dm.createStringColumn("CORG",13),
        dm.createTimestampColumn("DATOD"),
        dm.createTimestampColumn("DATDO")}
    );
    fieldSet.open();
    
    jtfPocDat.setDataSet(fieldSet);
    jtfPocDat.setColumnName("DATOD");
    
    jtfZavDat.setDataSet(fieldSet);
    jtfZavDat.setColumnName("DATDO");
    
    jpUpit.setLayout(xYLayout1);
    xYLayout1.setWidth(400);
    xYLayout1.setHeight(75);
    
    jpUpit.add(corging, new XYConstraints(0,25,-1,-1));
    jpUpit.add(jlDatumrange, new XYConstraints(15,50,-1,-1));
    jpUpit.add(jtfPocDat, new XYConstraints(150,50,100,-1));
    jpUpit.add(jtfZavDat, new XYConstraints(255,50,100,-1));
    
    this.setJPan(jpUpit);
  }

  public void componentShow() {
    getTTC().setLeftDataSet(null);
    getTTC().setRightDataSet(null);
    getTTC().initialize();
    fieldSet.setTimestamp("DATOD",Util.getUtil().findFirstDayOfYear());
    fieldSet.setTimestamp("DATDO",val.getToday());
//    printDataset(fieldSet);
  }

  public boolean runFirstESC() {
    return false;
  }

  public void firstESC() {

  }
  
  private boolean proshlo, showaj;
  
  public void okPress() {
    if (getTTC().getLeftDataSet()== null) {
//      System.out.println("Left side is null");
      showaj = false;
      String upitString ="SELECT cskl,vrdok,god,brdok,datdok,cpar FROM doki WHERE vrdok = 'PON' and cskl = '"+fieldSet.getString("CORG")+"' and statira = 'N' and datdok between '"+fieldSet.getTimestamp("DATOD")+"' and '"+fieldSet.getTimestamp("DATDO")+"' order by brdok";
//      System.out.println("Upit String - "+upitString);
      
      QueryDataSet lightSide = ut.getNewQueryDataSet(upitString,false);
      
      lightSide.setColumns(new Column[] {
          dm.getDoki().getColumn("CSKL").cloneColumn(),
          dm.getDoki().getColumn("VRDOK").cloneColumn(),
          dm.getDoki().getColumn("GOD").cloneColumn(),
          dm.getDoki().getColumn("BRDOK").cloneColumn(),
          dm.getDoki().getColumn("DATDOK").cloneColumn(),
          dm.getDoki().getColumn("CPAR").cloneColumn()}
      );
      lightSide.setMetaDataUpdate(MetaDataUpdate.NONE);
      lightSide.setRowId("CSKL",true);
      lightSide.setRowId("VRDOK",true);
      lightSide.setRowId("GOD",true);
      lightSide.setRowId("BRDOK",true);
      lightSide.getColumn("CSKL").setVisible(0);
      lightSide.getColumn("VRDOK").setVisible(0);
      lightSide.getColumn("GOD").setVisible(0);
      lightSide.getColumn("BRDOK").setWidth(20);
      lightSide.getColumn("DATDOK").setWidth(35);
      lightSide.getColumn("CPAR").setWidth(110);
      lightSide.open();
      
      QueryDataSet darkSide = new QueryDataSet();
      darkSide.setColumns(lightSide.cloneColumns());
      darkSide.setMetaDataUpdate(MetaDataUpdate.NONE);
      darkSide.setRowId("CSKL",true);
      darkSide.setRowId("VRDOK",true);
      darkSide.setRowId("GOD",true);
      darkSide.setRowId("BRDOK",true);
      darkSide.getColumn("CSKL").setVisible(0);
      darkSide.getColumn("VRDOK").setVisible(0);
      darkSide.getColumn("GOD").setVisible(0);
      darkSide.getColumn("BRDOK").setWidth(20);
      darkSide.getColumn("DATDOK").setWidth(35);
      darkSide.getColumn("CPAR").setWidth(110);
      darkSide.open();
      
//      printDataset(lightSide);
      lightSide.setTableName("delPON");
      darkSide.setTableName("delPON");
      getTTC().setLeftDataSet(lightSide);
      getTTC().setRightDataSet(darkSide);
      getTTC().initialize();
      
    } else {
//      System.out.println("Left side is NOT null");
//      printDataset(getTTC().getRightDataSet());
      if (getTTC().getRightDataSet() == null ||getTTC().getRightDataSet().isEmpty()){
        return;
      }
      showaj = true;
      getTTC().getRightDataSet().first();
      String brdoksi="";
      for (int i = 0;; i++) {
        brdoksi += getTTC().getRightDataSet().getInt("BRDOK");
        if (getTTC().getRightDataSet().next()) brdoksi += ",";
        else break;
      }
//      System.out.println(brdoksi);
      messWithMe(brdoksi);
//      proshlo = true;
      if (saveChangesInTransaction(brdoksi)){
        getTTC().getRightDataSet().emptyAllRows();
        proshlo = true;
      } else proshlo = false;
    }
  }
  
  public void afterOKPress() {
    String[] uspjeh;
    String naslov;
    int vrsta;
    if (proshlo){
      uspjeh = new String[]{"Nevažeæe ponude uspješno poništene"};
      naslov = "Obavjest";
      vrsta = JOptionPane.INFORMATION_MESSAGE;
    } else {
      uspjeh = new String[]{"Nevažeæe ponude nisu poništene"};
      naslov = "Greška";
      vrsta = JOptionPane.ERROR_MESSAGE;
    }
    if (showaj)
      JOptionPane.showMessageDialog(this.getJPan(),
          							uspjeh,
          							naslov,
          							vrsta);
  }
  
  QueryDataSet dummyStanje;
  
  private void messWithMe(String brdoksi){
    String stdokiUpitString = "select cart, kol, csklart, rezkol from stdoki where vrdok = 'PON' and cskl = '"+fieldSet.getString("CORG")+"' and god = '"+ut.getYear(fieldSet.getTimestamp("DATOD"))+"' and brdok in ("+brdoksi+") and rezkol = 'D'";
    QueryDataSet temporary = ut.getNewQueryDataSet(stdokiUpitString);
//    printDataset(temporary);
    dummyStanje = hr.restart.baza.Stanje.getDataModule().getTempSet("god = '"+ut.getYear(fieldSet.getTimestamp("DATOD"))+"'");
    dummyStanje.open();
//    printDataset(dummyStanje);
    temporary.first();
    lookupData ld = lookupData.getlookupData();
    do {
      if (ld.raLocate(dummyStanje ,new String[] {"CSKL","CART"}, new String[] {temporary.getString("CSKLART"),temporary.getInt("CART")+""})){
        if (dummyStanje.getBigDecimal("KOLREZ").compareTo(_Main.nul) == 0 || dummyStanje.getBigDecimal("KOLREZ").compareTo(_Main.nul) < 0) continue;
//        System.out.print("Mjenjam za cskl= '"+temporary.getString("CSKLART")+"' rez kol cart-a "+temporary.getInt("CART")+" na stanju sa "+dummyStanje.getBigDecimal("KOLREZ"));
        dummyStanje.setBigDecimal("KOLREZ",dummyStanje.getBigDecimal("KOLREZ").subtract(temporary.getBigDecimal("KOL")));
//        System.out.println(" na "+dummyStanje.getBigDecimal("KOLREZ"));
      } else {
//        System.out.println("ZBUNJ -- "+temporary.getString("CSKLART")+"  "+temporary.getInt("CART"));
      }
    } while (temporary.next());
  }
  
  private boolean saveChangesInTransaction(String brdoksi){
    final String updateDoki = "update doki set statira = 'X' where vrdok = 'PON' and cskl = '"+fieldSet.getString("CORG")+"' and datdok between '"+fieldSet.getTimestamp("DATOD")+"' and '"+fieldSet.getTimestamp("DATDO")+"' and brdok in ("+brdoksi+")";

    raLocalTransaction saveOurData = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.saveChanges(dummyStanje);
          raTransaction.runSQL(updateDoki);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return saveOurData.execTransaction();
  }
  
//  private void printDataset(DataSet ds){
//   sysoutTEST st = new sysoutTEST(false);
//   st.prn(ds);
//  }
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return false;
  }
  
  public boolean Validacija() {
//    System.out.println("Validacija kaze - (!hr.restart.util.Valid.getValid().isEmpty(corging.corg)) = "+(!hr.restart.util.Valid.getValid().isEmpty(corging.corg)) + " hr.restart.util.Valid.getValid().isValidRange(jtfPocDat,jtfZavDat) = "+hr.restart.util.Valid.getValid().isValidRange(jtfPocDat,jtfZavDat));
    if (hr.restart.util.Valid.getValid().isEmpty(corging.corg))
      return false;
    if (!hr.restart.util.Valid.getValid().isValidRange(jtfPocDat,jtfZavDat))
      return false;
    return true;
  }
}
