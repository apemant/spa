/****license*****************************************************************
**   file: upRacuniArtikliPOS.java
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
import hr.restart.util.raUpitFat;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upRacuniArtikliPOS extends raUpitFat {
  hr.restart.robno._Main main;
  hr.restart.robno.Util utilRobno = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  XYLayout layout = new XYLayout();
  
  JPanel jpDetail = new JPanel();
  XYLayout layDetail = new XYLayout();

  private rapancart rpcart = new rapancart(){
    public void nextTofocus(){
      jtfPocDat.requestFocus();
    }
  };
  
  private TableDataSet fieldSet = new TableDataSet();
  
  private JLabel jlDatumi = new JLabel("Period (od / do)");
  private JraTextField jtfPocDat = new JraTextField();
  private JraTextField jtfZavDat = new JraTextField();
  
  private JLabel jlProdMj = new JLabel("Prodajno mjesto");
  private JlrNavField jlrProdMj = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) {
        jlrCkase.requestFocus();
        enableProdMjesta(false);
        jlrCkase.setRaDataSet(getKase());
      }
    }
  };
  private JlrNavField jlrNazProdMj = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) {
        jlrCkase.requestFocus();
        enableProdMjesta(false);
        jlrCkase.setRaDataSet(getKase());
      }
    }
  };
  private JraButton jbDohRM = new JraButton();
  
  private JLabel jlBlagajna = new JLabel("Blagajna");
  private JlrNavField jlrCkase = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) {
        rpcart.jrfCART.requestFocus();
        enableKase(false);
      }
    }
  };
  private JlrNavField jlrNazKase = new JlrNavField(){
    public void after_lookUp() {
      if (isLastLookSuccessfull()) {
        rpcart.jrfCART.requestFocus();
        enableKase(false);
      }
    }
  };
  private JraButton jbDohKase = new JraButton();
  
  private static upRacuniArtikliPOS instanceOfMe = null;
  
  public static upRacuniArtikliPOS getInstance(){
    if (instanceOfMe == null) instanceOfMe = new upRacuniArtikliPOS();
    return instanceOfMe;
  }
  
  public QueryDataSet getReportSet(){
   return this.getJPTV().getDataSet(); 
  }
  
  private Map zaglavljeHashMap = new HashMap();
  
  public Map getZaglavljeMap(){
    return zaglavljeHashMap;
  }
  
  public upRacuniArtikliPOS() {
    try {
      jbInit();
      instanceOfMe = this;
    } catch (Exception ble){
    }
  }
  
  private int row = -1;
  
  
  public void componentShow() {
    doubleClicked = false;
    row = -1;
    setDataSet(null);
    enableKase(true);
    enableProdMjesta(true);
    jlrCkase.setText("");
    jlrCkase.emptyTextFields();
    fieldSet.setTimestamp("DATOD", vl.getToday()); // rut.getTimestampValue(tds.getTimestamp("pocDatum"), 0);
    fieldSet.setTimestamp("DATDO", vl.getToday());
    if (!rpcart.getCART().equals("")) {
      rpcart.clearFields();
    }
    jlrProdMj.requestFocusLater();
  }
  
  QueryDataSet tableSet;
  
  private String getNewUpit() {
    String prodMj = "";
    String carting = "";
    String cskling = "";
    String vrbr = "max(pos.vrdok) as vrdok, max(pos.god) as god, max(pos.brdok) as brdok, ";
    String grp = " group by stpos.cart, pos.cprodmj, pos.cskl ";
    if (!fieldSet.getString("CPRODMJ").equalsIgnoreCase("")){
      prodMj = "and pos.cprodmj ='"+fieldSet.getString("CPRODMJ")+"' ";
    }
    
    if (!rpcart.getCART().equals("")){
      carting = " and stpos.cart = "+ rpcart.getCART() + " ";
      vrbr = "pos.vrdok as vrdok, max(pos.god) as god, pos.brdok as brdok, ";
      grp = " group by stpos.cart, pos.cprodmj, pos.cskl, pos.vrdok, pos.brdok ";
    }
    
    if (!fieldSet.getString("CSKL").equals("")){
      cskling = "and pos.cskl = '" + fieldSet.getString("CSKL") + "' ";
    }
    
    String qu = "SELECT " +
//            "pos.cskl as cskl, pos.cprodmj as cprodmj, pos.vrdok as vrdok, pos.god as god, " +
//            "pos.brdok as brdok, stpos.cart as cart, stpos.cart1 as cart1, " +
//            "stpos.bc as bc, stpos.nazart as nazart, stpos.kol as kol, stpos.jm as jm, stpos.mc as mc, " +
//            "stpos.ipopust1 as ipopust1, stpos.ipopust2 as ipopust2, stpos.por1 as por1, stpos.por2 as por2, " +
//            "stpos.por3 as por3, stpos.neto as neto, stpos.ukupno as ukupno, stpos.iznos as iznos " +
    
    "pos.cskl as cskl, pos.cprodmj as cprodmj, " +
    vrbr +
    "stpos.cart as cart, max(stpos.cart1) as cart1, max(stpos.bc) as bc, " +
    "max(stpos.nazart) as nazart, sum(stpos.kol) as kol, max(stpos.jm) as jm, max(stpos.mc) as mc, " +
    "sum(stpos.ipopust1) as ipopust1, sum(stpos.ipopust2) as ipopust2, sum(stpos.por1) as por1, " +
    "sum(stpos.por2) as por2, sum(stpos.por3) as por3, sum(stpos.neto) as neto, sum(stpos.ukupno) as ukupno, " +
    "sum(stpos.iznos) as iznos "+
            
            "FROM pos,stpos WHERE pos.cskl = stpos.cskl AND pos.vrdok = stpos.vrdok " +
            "AND pos.god = stpos.god AND pos.brdok = stpos.brdok AND pos.cprodmj = stpos.cprodmj " +
            cskling + prodMj + 
            "and pos.datdok between "+utilRobno.getTimestampValue(fieldSet.getTimestamp("DATOD"), utilRobno.NUM_FIRST) +
            " and "+utilRobno.getTimestampValue(fieldSet.getTimestamp("DATDO"), utilRobno.NUM_LAST) + carting + 
            grp; //order by stpos.cart";
    
    System.out.println(qu); //XDEBUG delete when no more needed
    return qu;
  }

  int[] viskols; 
  
  private void setReports(){
    killAllReports();
    this.addReport("hr.restart.robno.repRacuniArtikli", "hr.restart.robno.repRacuniArtikliPOSDataProvider", "RacuniArtikliPOS01", "Ispis");
  }
  
  public void okPress(){
    QueryDataSet temporarySet = util.getNewQueryDataSet(getNewUpit());
    if (temporarySet.rowCount() <= 0) setNoDataAndReturnImmediately();
    
//    setReports();
//    setMap();
    
    if (rpcart.getCART().equals("")){
      killAllReports();
      this.addReport("hr.restart.robno.repRacuniArtikli", "hr.restart.robno.repRacuniArtikliPOSDataProvider", "RacuniArtikliPOS01", "Ispis");
      filterFirstSet(temporarySet);
      viskols = new int[] {0,1,2,3,4,5,6,7,8};
      if (row > -1) tableSet.goToRow(row);
      tableSet.setTableName("racartpossvi");
      setDataSetAndSums(tableSet,new String[] {"POP","IBP","POR","POP","ISP"});
    } else {
      killAllReports();
//      this.addReport("hr.restart.robno.repRacuniArtikli", "hr.restart.robno.repRacuniArtikliPOSDataProvider", "RacuniArtikliPOS01", "Ispis");
      filterNextSet(temporarySet);
      viskols = new int[] {0,1,2,3,4,5,6,7};
      tableSet.setTableName("racartposcart");
      setDataSetAndSums(tableSet,new String[] {"KOL","POP","IBP","POR","POP","ISP"});
    }
  }
  
  private void setMap() {
    try {
      zaglavljeHashMap.clear();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!fieldSet.getString("CSKL").equals("")) {
      zaglavljeHashMap.put("CSKL",jlrProdMj.getText());
      zaglavljeHashMap.put("NAZSKL",jlrNazProdMj.getText());
    } else {
      zaglavljeHashMap.put("NAZSKL","Sva prodajna mjesta");
    }
    
    if (!fieldSet.getString("CPRODMJ").equals("")) {
      zaglavljeHashMap.put("CPRODMJ",jlrCkase.getText());
      zaglavljeHashMap.put("NAZPRODMJ",jlrNazKase.getText());
    } else {
      zaglavljeHashMap.put("NAZPRODMJ","Sva blagajne");
    }
    
    if (!rpcart.getCART().equals("")) {
      zaglavljeHashMap.put("CART",rpcart.getCART());
      zaglavljeHashMap.put("NAZART",rpcart.getNAZART());
    } else {
      zaglavljeHashMap.put("NAZART","Svi artikli");
    }
    
    zaglavljeHashMap.put("SUBTITLE","U preriodu od "+ 
        raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("DATOD"))+ " do"
        + raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("DATDO")));
  }
  
  lookupData ld = lookupData.getlookupData();
  
  private void filterFirstSet(QueryDataSet tmpSet){ //to znaèi nije unesen artikl.
    tableSet = new QueryDataSet();
    tableSet.setColumns(new Column[] {
        dm.createStringColumn("CSKL","Prodajno Mjesto",10),
        dm.createStringColumn("CPRODMJ","Blagajna",3),
        dm.createIntColumn("CART","Šifra"),
        dm.createStringColumn("CART1","Oznaka",20),
        dm.createStringColumn("BC","Barcode",20),
        dm.createStringColumn("NAZART","Naziv",150),
        dm.createBigDecimalColumn("KOL","kolièina"),
        dm.createStringColumn("JM","JM",5),
        dm.createBigDecimalColumn("MC","Cijena"), //TODO dodati iznos bez poreza i popusta... možda
        dm.createBigDecimalColumn("POP","Ukupan popust"),
        dm.createBigDecimalColumn("IBP","Iznos bez poreza"),
        dm.createBigDecimalColumn("POR","Porez"),
        dm.createBigDecimalColumn("ISP","Iznos s porezom")
    });
    
    tableSet.setRowId("CPRODMJ",true);
    tableSet.setRowId("CART",true);
    tableSet.open();
    
    tmpSet.first();
    do {
//      if (!ld.raLocate(tableSet, new String[] {"CSKL", "CPRODMJ", "CART"}, new String[] {tmpSet.getString("CSKL"), tmpSet.getString("CPRODMJ"), tmpSet.getInt("CART")+""})){
        tableSet.insertRow(false);
        tableSet.setString("CSKL", tmpSet.getString("CSKL"));
        tableSet.setString("CPRODMJ", tmpSet.getString("CPRODMJ"));
        tableSet.setInt("CART", tmpSet.getInt("CART"));
        tableSet.setString("CART1", tmpSet.getString("CART1"));
        tableSet.setString("BC", tmpSet.getString("BC"));
        tableSet.setString("NAZART", tmpSet.getString("NAZART"));
        tableSet.setBigDecimal("KOL", tmpSet.getBigDecimal("KOL"));
        tableSet.setString("JM", tmpSet.getString("JM"));
        tableSet.setBigDecimal("MC", tmpSet.getBigDecimal("MC"));
        tableSet.setBigDecimal("IBP", tmpSet.getBigDecimal("IZNOS").subtract((tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3"))))));
        tableSet.setBigDecimal("POP", (tmpSet.getBigDecimal("IPOPUST1").add(tmpSet.getBigDecimal("IPOPUST2"))));
        tableSet.setBigDecimal("POR", (tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3")))));
        tableSet.setBigDecimal("ISP", tmpSet.getBigDecimal("IZNOS"));
//      } else {
//        tableSet.setBigDecimal("KOL", tableSet.getBigDecimal("KOL").add(tmpSet.getBigDecimal("KOL")));
//        tableSet.setBigDecimal("IBP", tableSet.getBigDecimal("IBP").add(tmpSet.getBigDecimal("IZNOS").subtract((tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3")))))));
//        tableSet.setBigDecimal("POP", tableSet.getBigDecimal("POP").add((tmpSet.getBigDecimal("IPOPUST1").add(tmpSet.getBigDecimal("IPOPUST2")))));
//        tableSet.setBigDecimal("POR", tableSet.getBigDecimal("POR").add((tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3"))))));
//        tableSet.setBigDecimal("ISP", tableSet.getBigDecimal("ISP").add(tmpSet.getBigDecimal("IZNOS")));
//      }
    } while (tmpSet.next());

    if (!jlrProdMj.getText().equals(""))
      tableSet.getColumn("CSKL").setVisible(0);
    if (!jlrCkase.getText().equals(""))
      tableSet.getColumn("CPRODMJ").setVisible(0);
    tableSet.getColumn("CART").setVisible(Aut.getAut().getCARTdependable(1, 0, 0));
    tableSet.getColumn("CART1").setVisible(Aut.getAut().getCARTdependable(0, 1, 0));
    tableSet.getColumn("BC").setVisible(Aut.getAut().getCARTdependable(0, 0, 1));
    
    tableSet.setSort(new SortDescriptor(new String[] {"CSKL","CPRODMJ",Aut.getAut().getCARTdependable("CART", "CART1", "BC")}));
    tableSet.last();
  }
  
  private void filterNextSet(QueryDataSet tmpSet){ //to znaèi unesen artikl 
    tableSet = new QueryDataSet();
    tableSet.setColumns(new Column[] {
        dm.createStringColumn("CSKL","Prodajno Mjesto",10),
        dm.createStringColumn("CPRODMJ","Blagajna",3),
        dm.createIntColumn("CART","Šifra"),
        dm.createStringColumn("BRDOK","Raèun broj",20),
        dm.createBigDecimalColumn("KOL","kolièina"),
        dm.createBigDecimalColumn("MC","Cijena"),
        dm.createBigDecimalColumn("POP","Popust"),
        dm.createBigDecimalColumn("IBP","Iznos bez poreza"),
        dm.createBigDecimalColumn("POR","Porez"),
        dm.createBigDecimalColumn("ISP","Iznos s porezom")
    });
    
    tableSet.setRowId("CPRODMJ",true);
    tableSet.setRowId("CART",true);
    tableSet.open();
    
    tmpSet.first();
    do {
//      if (!ld.raLocate(tableSet, new String[] {"CSKL", "CPRODMJ"}, new String[] {tmpSet.getString("CSKL"), tmpSet.getString("CPRODMJ")})){
        tableSet.insertRow(false);
        tableSet.setString("CSKL", tmpSet.getString("CSKL"));
        tableSet.setString("CPRODMJ", tmpSet.getString("CPRODMJ"));
        tableSet.setString("BRDOK", tmpSet.getString("VRDOK")+"-"+tmpSet.getString("CPRODMJ")+"/"+tmpSet.getString("GOD")+"-"+hr.restart.util.Valid.getValid().maskZeroInteger(new Integer(tmpSet.getInt("BRDOK")),6));
        tableSet.setBigDecimal("KOL", tmpSet.getBigDecimal("KOL"));
        tableSet.setBigDecimal("MC", tmpSet.getBigDecimal("MC"));
        tableSet.setBigDecimal("IBP", tmpSet.getBigDecimal("IZNOS").subtract((tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3"))))));
        tableSet.setBigDecimal("POP", (tmpSet.getBigDecimal("IPOPUST1").add(tmpSet.getBigDecimal("IPOPUST2"))));
        tableSet.setBigDecimal("POR", (tmpSet.getBigDecimal("POR1").add(tmpSet.getBigDecimal("POR2").add(tmpSet.getBigDecimal("POR3")))));
        tableSet.setBigDecimal("ISP", tmpSet.getBigDecimal("IZNOS"));
//      }
    } while (tmpSet.next());
    if (!jlrProdMj.getText().equals(""))
      tableSet.getColumn("CSKL").setVisible(0);
    if (!jlrCkase.getText().equals(""))
      tableSet.getColumn("CART").setVisible(0);
    tableSet.setSort(new SortDescriptor(new String[] {"CSKL","CPRODMJ","BRDOK"}));
    tableSet.last();
    
  }

  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return true;
    if (!rpcart.getCART().equals("")) return true;
    else if (!jlrCkase.getText().equals("")) return true;
    else if (!jlrProdMj.getText().equals("")) return true;
    return false;
  }
  
  private boolean doubleClicked, isIspis;
  
  public void firstESC() {
    if (doubleClicked) {
      undoubleclick();
      return;
    }
    if (this.getJPTV().getDataSet() != null) {
      setDataSet(null);
      if (rpcart.getCART().equals("") && jlrProdMj.getText().equals("") && jlrCkase.getText().equals("")){
        rcc.EnabDisabAll(this.getJPan(),true);
        jlrProdMj.requestFocus();
        removeNav();
        return;
      }
      rcc.setLabelLaF(jtfPocDat, true);
      rcc.setLabelLaF(jtfZavDat, true);
      rpcart.EnabDisab(true);
      enableKase(true);
      enableProdMjesta(true);
      if (!rpcart.getCART().equals("")) {
        rpcart.setCARTLater();
      } else if (!jlrCkase.getText().equals("")) {
        rpcart.EnabDisab(true);
        jlrCkase.setText("");
        jlrCkase.emptyTextFields();
        jlrCkase.requestFocus();
      } else if (!jlrProdMj.getText().equals("")) {
        rpcart.EnabDisab(true);
        jlrCkase.setText("");
        jlrCkase.emptyTextFields();
        jlrProdMj.setText("");
        jlrProdMj.emptyTextFields();
        jlrCkase.setRaDataSet(getKase());
        jlrProdMj.requestFocus();
      }
      row = -1;
      removeNav();
    } else {
      rcc.setLabelLaF(jtfPocDat, true);
      rcc.setLabelLaF(jtfZavDat, true);
      rpcart.EnabDisab(true);
      enableProdMjesta(true);
      rpcart.EnabDisab(true);
      enableKase(true);
      if (!rpcart.getCART().equals("")) {
        rpcart.setCARTLater();
      } else if (!jlrCkase.getText().equals("")) {
        jlrCkase.setText("");
        jlrCkase.emptyTextFields();
        jlrCkase.requestFocus();
      } else if (!jlrProdMj.getText().equals("")) {
        jlrProdMj.setText("");
        jlrProdMj.emptyTextFields();
        jlrCkase.setRaDataSet(getKase());
        jlrProdMj.requestFocus();
      }
    }
  }
  
  private void jbInit() throws Exception {
    fieldSet.setColumns(new Column[] {
        dm.createStringColumn("CSKL","Prodajno mjesto",15),
        dm.createStringColumn("CPRODMJ","Blagajna",15),
        dm.createTimestampColumn("DATOD", "Poèetni datum"),
        dm.createTimestampColumn("DATDO", "Završni Datum")
        });
    fieldSet.open();
    
    jtfPocDat.setDataSet(fieldSet);
    jtfPocDat.setColumnName("DATOD");
    jtfPocDat.setHorizontalAlignment(SwingConstants.CENTER);
    
    jtfZavDat.setDataSet(fieldSet);
    jtfZavDat.setColumnName("DATDO");
    jtfZavDat.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCkase.setColumnName("CPRODMJ");
    jlrCkase.setDataSet(fieldSet);
    jlrCkase.setColNames(new String[] {"NAZPRODMJ"});
    jlrCkase.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKase});
    jlrCkase.setVisCols(new int[] {0,1});
    jlrCkase.setSearchMode(0);
    System.out.println("postavljam kase...."); //XDEBUG delete when no more needed
    jlrCkase.setRaDataSet(this.getKase());
    System.out.println("postavljeno"); //XDEBUG delete when no more needed
    jlrCkase.setNavButton(jbDohKase);

    jlrNazKase.setColumnName("NAZPRODMJ");
    jlrNazKase.setNavProperties(jlrCkase);
    jlrNazKase.setSearchMode(1);
    
    jlrProdMj.setColumnName("CSKL");
    jlrProdMj.setDataSet(fieldSet);
    jlrProdMj.setColNames(new String[] {"NAZSKL"});
    jlrProdMj.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazProdMj});
    jlrProdMj.setVisCols(new int[] {0,1});
    jlrProdMj.setSearchMode(0);
    jlrProdMj.setRaDataSet(getProdajnaMjesta());
    jlrProdMj.setNavButton(jbDohRM);

    jlrNazProdMj.setColumnName("NAZSKL");
    jlrNazProdMj.setNavProperties(jlrProdMj);
    jlrNazProdMj.setSearchMode(1);
    
    layDetail.setWidth(650);
    layDetail.setHeight(200);
    jpDetail.setLayout(layDetail);
    this.setJPan(jpDetail);
    
    jpDetail.add(jlProdMj, new XYConstraints(15,20,-1,-1));
    jpDetail.add(jlrProdMj, new XYConstraints(150,20,100,-1));
    jpDetail.add(jlrNazProdMj, new XYConstraints(255,20,349,-1));
    jpDetail.add(jbDohRM, new XYConstraints(609,20,21,21));
    
    jpDetail.add(jlBlagajna, new XYConstraints(15,45,-1,-1));
    jpDetail.add(jlrCkase, new XYConstraints(150,45,100,-1));
    jpDetail.add(jlrNazKase, new XYConstraints(255,45,349,-1));
    jpDetail.add(jbDohKase, new XYConstraints(609,45,21,21));
    
    jpDetail.add(rpcart, new XYConstraints(0,65,-1,75));
    jpDetail.add(jlDatumi, new XYConstraints(15,140,-1,-1));
    jpDetail.add(jtfPocDat, new XYConstraints(150,140,100,-1));
    jpDetail.add(jtfZavDat, new XYConstraints(255,140,100,-1));
    

    rpcart.setMode("DOH");
    rpcart.setBorder(null);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCkase.setRaDataSet(getProdajnaMjesta());
      }
    });
  }
  
  private void enableKase(boolean enableIt){
    rcc.setLabelLaF(jlrCkase,enableIt);
    rcc.setLabelLaF(jlrNazKase,enableIt);
    rcc.setLabelLaF(jbDohKase,enableIt);
  }
  
  private void enableProdMjesta(boolean enableIt){
    rcc.setLabelLaF(jlrProdMj,enableIt);
    rcc.setLabelLaF(jlrNazProdMj,enableIt);
    rcc.setLabelLaF(jbDohRM,enableIt);
  }
  
  private QueryDataSet getProdajnaMjesta(){
    QueryDataSet pm = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("knjig = '"+hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG()+"'");
    pm.open();
    return pm;
  }
  
  private QueryDataSet getKase(){    
    //if (fieldSet.getString("CSKL").equalsIgnoreCase("")) return null;
    
    String where = "";
    if (!fieldSet.getString("CSKL").equals(""))
      where = "where cskl = '"+fieldSet.getString("CSKL")+"'";
    
    String upitnik = "SELECT cprodmj, nazprodmj, cskl FROM Prod_mj " + where;
    System.out.println(upitnik);
    QueryDataSet rm = new QueryDataSet();
    rm = util.getNewQueryDataSet(upitnik,false);
    rm.setColumns(new Column[]{(Column) dm.getProd_mj().getColumn("CPRODMJ").clone(), (Column) dm.getProd_mj().getColumn("NAZPRODMJ").clone(), (Column) dm.getProd_mj().getColumn("CSKL").clone()});
    rm.open();
    return rm;
  }

  public boolean isIspis() {
    return (getJPTV().getDataSet() != null && isIspis);
  }
  
//  QueryDataSet cacheSet;
  
  private String blagajnaCache = ""; 
  private String radnomjCache = ""; 
  
  public void jptv_doubleClick() {
    if (!rpcart.getCART().equalsIgnoreCase("") || !rpcart.getBC().equalsIgnoreCase("") || doubleClicked) return;
//    cacheSet = this.getJPTV().getDataSet();
    row = this.getJPTV().getDataSet().getRow();
    rpcart.setCART(this.getJPTV().getDataSet().getInt("CART"));
    blagajnaCache = jlrCkase.getText();
    jlrCkase.setText(this.getJPTV().getDataSet().getString("CPRODMJ"));
    jlrCkase.forceFocLost();
    radnomjCache = jlrProdMj.getText();
    jlrProdMj.setText(this.getJPTV().getDataSet().getString("CSKL"));
    jlrProdMj.forceFocLost();
    doubleClicked = true;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        isIspis = false;
        ok_action();
        isIspis = true;
      }
    });
  }
  
  private void undoubleclick(){
    doubleClicked = false;
    rpcart.setCARTLater();
    jlrCkase.setText(blagajnaCache);
    jlrCkase.forceFocLost();
    jlrProdMj.setText(radnomjCache);
    jlrProdMj.forceFocLost();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        isIspis = false;
        ok_action();
        isIspis = true;
      }
    });
  }


  public String navDoubleClickActionName() {
    return "Dokimenti s artiklom";
  }


  public int[] navVisibleColumns() {
    return viskols;
  }

  public boolean Validacija() {
    return true;
  }
  
  
  
  
}
