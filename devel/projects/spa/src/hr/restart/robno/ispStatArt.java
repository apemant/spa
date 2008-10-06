/****license*****************************************************************
**   file: ispStatArt.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class ispStatArt extends raPanStats {

  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  QueryDataSet reportSet, reportSetDet, reportSet2, jptvSet, monthSet, prikazSet, monthSetGraph;

  double postoSumRuc = 0;

  static ispStatArt instanceOfMe;

  public ispStatArt() {
    try {
      jbInit();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void showDefaultValues() {
    super.showDefaultValues();
    fieldSet.setString("SLJED", Aut.getAut().getCARTdependable("CART", "CART1", "BC"));
    setSljed();
    getJPTV().clearDataSet();
  }

  public static ispStatArt getInstance() {
    if (instanceOfMe == null)
      instanceOfMe = new ispStatArt();
    return instanceOfMe;
  }

  double sumNab, sumRuc;

  public String navDoubleClickActionName() {
    return "Dokumenti";
  }
  public int[] navVisibleColumns() {
    System.out.println("Dobavljam vidljive kolone"); //XDEBUG delete when no more needed
    if (doubleclicked){ 
      return new int[] {1,2,4,5,Aut.getAut().getCARTdependable(6,7,8),9,10,13,15,16,17,18};
    }
    return new int[] {0,1,2,3,4,5};
  }
  
  private boolean doubleclicked = false, isIspis;
  
  public void okPress() {
//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(fieldSet);
    String newDateP, newDateZ;

    newDateP = util.getTimestampValue(fieldSet.getTimestamp("pocDatum"), util.NUM_FIRST);
    newDateZ = util.getTimestampValue(fieldSet.getTimestamp("zavDatum"), util.NUM_LAST);
    
    String /* qStr, /*qDodatakNaStr, */artikliFilter;

    if (fieldSet.getString("VRART").equals("") || fieldSet.getString("VRART").equals("X"))
      artikliFilter = "";
    else
      artikliFilter = " AND ARTIKLI.VRART='" + fieldSet.getString("VRART") + "' ";

    String carting = "";
    if (!rpcart.findCART(podgrupe).equals("")) {
      carting = " AND " + rpcart.findCART(podgrupe);
    }
    
    if (!doubleclicked) {

      killAllReports();
      addReport("hr.restart.robno.repStatArtDva", "hr.restart.robno.repStatArtDva", "StatArtDva", "Top lista artikala");
      if (jpKup.isEmpty())
        addReport("hr.restart.robno.repStatArtDetaljno", "hr.restart.robno.repStatArtDetaljno", "StatArtDetaljno", "Top lista artikala - detaljno");
      
      this.addReport("hr.restart.robno.RepStatArtChart", "hr.restart.robno.RepStatArtChart", "Top lista artikala - grafikon");
//      String /* qStr, /*qDodatakNaStr, */artikliFilter;
//
//      if (fieldSet.getString("VRART").equals("") || fieldSet.getString("VRART").equals("X"))
//        artikliFilter = "";
//      else
//        artikliFilter = " AND ARTIKLI.VRART='" + fieldSet.getString("VRART") + "' ";
//
//      String carting = "";
//      if (!rpcart.findCART(podgrupe).equals("")) {
//        carting = " AND " + rpcart.findCART(podgrupe);
//      }

      //  qDodatakNaStr = getStringCskl() + getStringCkup() /*+ getStringCpar()
      // */+
      // carting;
      //  qStr = sgQuerys.getSgQuerys().getStringIspStatArt( newDateP, newDateZ,
      // qDodatakNaStr, artikliFilter);

      //  sgQuerys.getSgQuerys().getIspStatArtDS(newDateP, newDateZ, "IPRODSP",
      // getStringCskl(), getStringCkup());
      String sort;

      if (getSorter().equals("CART"))
        sort = Aut.getAut().getCARTdependable("CART", "CART1", "BC");
      else
        sort = getSorter();
      //  System.out.println("before");
      reportSet = sgQuerys.getSgQuerys().getIspStatArtDS(newDateP, newDateZ, sort, getCskl(), getCkup(), getPjCkup(), getCpar(), artikliFilter, carting, fieldSet.getString("CORG"));//ut.getNewQueryDataSet(qStr);
      //  System.out.println("after");
      //    syst.prn(reportSet);
      checkClosing();
      if (reportSet == null || reportSet.rowCount() == 0)
        setNoDataAndReturnImmediately();

      /** @todo detaljni ispis */
      if (jpKup.isEmpty()) {
        reportSetDet = sgQuerys.getSgQuerys().getIspStatArtDetailsDS(newDateP, newDateZ, sort, getCskl(), getCkup(), getCpar(), artikliFilter, carting, fieldSet.getString("CORG"));
      } else {
        //    System.out.println("ubijam detaljni dataset");
        reportSetDet = null;
      }
      checkClosing();

      reportSet.first();

      sumNab = 0;
      sumRuc = 0;
      postoSumRuc = 0;

      do {
        sumNab += reportSet.getBigDecimal("INAB").doubleValue();
        sumRuc += reportSet.getBigDecimal("RUC").doubleValue();
      } while (reportSet.next());

      if (sumNab == 0.00)
        postoSumRuc = 0.00;
      else
        postoSumRuc = sumRuc / sumNab * 100;

      reportSet.getColumn("CPAR").setVisible(0);
      reportSet.getColumn("CSKL").setVisible(0);
      //    reportSet.getColumn("IPRODBP").setVisible(0);
      reportSet.getColumn("IRAZ").setVisible(0);

      reportSet.getColumn("CART").setVisible(0);
      reportSet.getColumn("CART1").setVisible(0);
      reportSet.getColumn("BC").setVisible(0);

      reportSet.getColumn("CPAR").setVisible(0);

      reportSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);

      reportSet.last();
//      this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      setDataSetAndSums(reportSet, new String[]{"INAB", "RUC", "POR", "IPRODBP", "IPRODSP"});
    } else if (doubleclicked && !returnFromDoubleClick){ // DOUBLECLICKED!!!
      System.out.println("Action should be taken imidijetli..."); //XDEBUG delete when no more needed
      
      /*
      killAllReports();
      addReport("repStatArtDoubleclick","repStatArtDoubleclick","StatArtDoubleclick","Formatirani ispis artikala");
      addReport("repStatArtDoubleclickDok","repStatArtDoubleclick","StatArtDoubleclickDok","Formatirani ispis artikala po dokumentima");
      */
      
      int cart = getJPTV().getDataSet().getInt("CART");
      /*String upit = "select " +
      		"stdoki.brdok,stdoki.vrdok,stdoki.god,stdoki.cskl,stdoki.rbr,stdoki.cart,stdoki.cart1,stdoki.bc," +
      		"stdoki.nazart,stdoki.kol " +
      		"from doki, stdoki " +
      		"where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok and doki.god=stdoki.god " +
      		"and doki.vrdok=stdoki.vrdok and stdoki.cart = "+cart+
      		" and doki.datdok between "+newDateP+" and "+newDateZ;*/
      
      String ckupca = " ", pjKupca = " ", dobart=" ", caprDobart="", sklad="";
//    System.out.println("CPAR = " + cpart);
    if (!getCkup().equals("")) ckupca = "and doki.cpar='" + getCkup() + "' ";
    if (!getPjCkup().equals("")) pjKupca = "and doki.pj='" + getPjCkup() + "' ";
    if (!getCpar().equals("")){
      dobart = ", dob_art ";
      ckupca = ckupca + "and dob_art.cpar = '" + getCpar() + "' and dob_art.cart = stdoki.cart ";
      caprDobart = ", dob_art.cpar as dcp ";
    }
    
    if (getCskl().equals("")){
      
      QueryDataSet skls;
      
      if (fieldSet.getString("CORG").equals("")) skls = dm.getSklad();
      else skls = hr.restart.robno.Util.getUtil().getSkladFromCorg();//ut.getNewQueryDataSet("SELECT * FROM Sklad WHERE corg = '"+corg+"'");
      
      skls.open();
      skls.first();
      
      String sifskl = "";
      
      do {
        sifskl += "'"+skls.getString("CSKL")+"'";
        if (skls.next()) sifskl += ",";
        else break;
      } while (true);

      sklad = " AND DOKI.CSKL in ("+sifskl+")";
    } else {
      sklad = " AND DOKI.CSKL = '"+getCskl()+"'";
    }
    
    String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(fieldSet.getString("CORG"));
    if (corgs.rowCount() == 0 || fieldSet.getString("CORG").equals("")) inq = "1=1";
    else if (corgs.rowCount() == 1) inq = "DOKI.CSKL = '" + fieldSet.getString("CORG") + "'";
    else inq = "DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs);
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    String exInClude = "AND (("+oj+" AND "+inq+") OR ("+oj.not()+sklad+"))";

    String upit = "select "+
	 "doki.vrdok, "+
	 "doki.brdok, "+
	 "doki.god, "+
	 "doki.cskl, "+
	 "doki.datdok, "+
	 "stdoki.rbr, "+
	 "stdoki.cart, "+
	 "stdoki.cart1, "+
	 "stdoki.bc, "+
	 "artikli.vrart, "+
	 "stdoki.nazart, "+
	 "stdoki.jm, "+
	 "stdoki.kol, "+
	 "stdoki.iraz, "+
	 "stdoki.iprodbp, "+
	 "(stdoki.iprodsp - stdoki.iprodbp) as por, "+
	 "stdoki.iprodsp, "+
	 "stdoki.inab, "+
	 "(stdoki.iprodbp-stdoki.inab) as ruc "+caprDobart+
	 "from artikli, doki, stdoki "+dobart+
	 "where doki.cskl=stdoki.cskl and doki.brdok=stdoki.brdok "+
	 "and doki.god=stdoki.god and doki.vrdok=stdoki.vrdok AND stdoki.cart = artikli.cart "+
	 " AND doki.vrdok not in ('PON','TRE','ZAH','NDO','NKU','RNL','REV','PRV','OTR','INM','INV','IZD','GOT','GRN','OTP') "+
	 exInClude+
	 ckupca+
	 pjKupca+
	 " and stdoki.cart = "+cart+" "+
	 " and doki.datdok between "+
	 newDateP+" "+"and "+newDateZ +
	 " ORDER BY DOKI.DATDOK";
      
      
    QueryDataSet dkSet = ut.getNewQueryDataSet(upit);
    
    dkSet.first();
    
    prikazSet = new QueryDataSet();
    
    prikazSet.setColumns(new Column[] {
        dm.getStdoki().getColumn("CSKL").cloneColumn(),
        dm.getStdoki().getColumn("VRDOK").cloneColumn(),
        dm.getDoki().getColumn("BRDOK").cloneColumn(),
        dm.getDoki().getColumn("GOD").cloneColumn(),
        dm.getStdoki().getColumn("RBR").cloneColumn(),
        dm.getDoki().getColumn("DATDOK").cloneColumn(),
        dm.getStdoki().getColumn("CART").cloneColumn(),
        dm.getStdoki().getColumn("CART1").cloneColumn(),
        dm.getStdoki().getColumn("BC").cloneColumn(),
        dm.getStdoki().getColumn("NAZART").cloneColumn(),
        dm.getStdoki().getColumn("JM").cloneColumn(),
        dm.getStdoki().getColumn("KOL").cloneColumn(),
        dm.getStdoki().getColumn("IRAZ").cloneColumn(),
        dm.getStdoki().getColumn("INAB").cloneColumn(),
        dm.createBigDecimalColumn("RUC","RuC",2),
        dm.createBigDecimalColumn("PostoRUC","% RuC",2),
        dm.getStdoki().getColumn("IPRODBP").cloneColumn(),
        dm.createBigDecimalColumn("POR","Porez",2),
        dm.getStdoki().getColumn("IPRODSP").cloneColumn(),
    });
    prikazSet.getColumn("VRDOK").setCaption("Dokument");
    prikazSet.getColumn("RBR").setCaption("Stavka");
    prikazSet.open();
    
    do {
      prikazSet.insertRow(false);
      prikazSet.setString("CSKL",dkSet.getString("CSKL"));
      prikazSet.setTimestamp("DATDOK",dkSet.getTimestamp("DATDOK"));
      prikazSet.setString("VRDOK",dkSet.getString("VRDOK"));
      prikazSet.setInt("BRDOK",dkSet.getInt("BRDOK"));
      prikazSet.setString("GOD",dkSet.getString("GOD"));
      prikazSet.setShort("RBR",dkSet.getShort("RBR"));
      prikazSet.setInt("CART",dkSet.getInt("CART"));
      prikazSet.setString("CART1",dkSet.getString("CART1"));
      prikazSet.setString("BC",dkSet.getString("BC"));
      prikazSet.setString("JM",dkSet.getString("JM"));
      prikazSet.setString("NAZART",dkSet.getString("NAZART"));
      prikazSet.setBigDecimal("KOL",dkSet.getBigDecimal("KOL"));
      prikazSet.setBigDecimal("IRAZ",dkSet.getBigDecimal("IRAZ"));
      prikazSet.setBigDecimal("INAB",dkSet.getBigDecimal("INAB"));
      prikazSet.setBigDecimal("IPRODBP",dkSet.getBigDecimal("IPRODBP"));
      prikazSet.setBigDecimal("POR",new java.math.BigDecimal(dkSet.getDouble("POR")));
      prikazSet.setBigDecimal("RUC",new java.math.BigDecimal(dkSet.getDouble("RUC")));
      prikazSet.setBigDecimal("IPRODSP",dkSet.getBigDecimal("IPRODSP"));
    } while (dkSet.next());

    /*dkSet.getColumn("CSKL").setVisible(0);
    dkSet.getColumn("CSKL1").setVisible(0);
    dkSet.getColumn("GOD").setVisible(0);
    dkSet.getColumn("GOD1").setVisible(0);
    dkSet.getColumn("VRDOK1").setVisible(0);
    dkSet.getColumn("BRDOK1").setVisible(0);
    dkSet.getColumn("IPRODBP").setVisible(0);
    dkSet.getColumn("IRAZ").setVisible(0);

    dkSet.getColumn("CART").setVisible(0);
    dkSet.getColumn("CART1").setVisible(0);
    dkSet.getColumn("BC").setVisible(0);

    dkSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);*/

//    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    setDataSetAndSums(prikazSet, new String[]{"KOL","INAB", "RUC", "POR", "IPRODBP", "IPRODSP"});
//    getJPTV().setDataSet(prikazSet);
    } else if (returnFromDoubleClick){
      returnFromDoubleClick = false;
      doubleclicked = false;
      reportSet.getColumn("CPAR").setVisible(0);
      reportSet.getColumn("CSKL").setVisible(0);
      //    reportSet.getColumn("IPRODBP").setVisible(0);
      reportSet.getColumn("IRAZ").setVisible(0);

      reportSet.getColumn("CART").setVisible(0);
      reportSet.getColumn("CART1").setVisible(0);
      reportSet.getColumn("BC").setVisible(0);

      reportSet.getColumn("CPAR").setVisible(0);

      reportSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
//      this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      setDataSetAndSums(reportSet, new String[]{"INAB", "RUC", "POR", "IPRODBP", "IPRODSP"}); 
    }
  }
  
  boolean returnFromDoubleClick = false;

  public void firstESC() {
    if (doubleclicked) {
      returnFromDoubleClick = true;
      isIspis = false;
      ok_action();
      isIspis = true;
      doubleclicked = false;
/*
      reportSet.getColumn("CPAR").setVisible(0);
      reportSet.getColumn("CSKL").setVisible(0);
      //    reportSet.getColumn("IPRODBP").setVisible(0);
      reportSet.getColumn("IRAZ").setVisible(0);

      reportSet.getColumn("CART").setVisible(0);
      reportSet.getColumn("CART1").setVisible(0);
      reportSet.getColumn("BC").setVisible(0);

      reportSet.getColumn("CPAR").setVisible(0);

      reportSet.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
//      this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      getJPTV().setDataSetAndSums(reportSet, new String[]{"INAB", "RUC", "POR", "IPRODBP", "IPRODSP"});*/
    } else {
	  super.firstESC();
      rcc.setLabelLaF(rcmbPrikaz,false);
    }
  }
  

  private void jbInit() throws Exception {
    
    rcmbSljed.setRaItems(new String[][] {
      {"�ifra","CART"},
      {"Naziv","NAZART"},
      {"Koli�ina","KOL"},
      {"RuC","RUC"},
      {"RuC %","PostoRUC"},
      {"Vrijednost s porezom","IPRODSP"}
    });
    
    rcmbPrikaz.setRaItems(new String[][]{{"Ukupni iznos", "UI"}, {"Mjese�no", "MJ"}});

    rcmbPoCemu.setRaItems(new String[][]{{"Nabavni iznos", "INAB"}, {"Razlika u cijeni", "RUC"}, {"Iznos bez poreza", "IPRODBP"}, {"Porez", "POR"}, {"Iznos s porezom", "IPRODSP"}});

    rcmbPrikaz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSljed();
      }
    });

    rcmbPoCemu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSljed();
      }
    });

  }

  private void setSljed() {
    rcmbPoCemu.setSelectedIndex(0);
    rcc.setLabelLaF(rcmbPoCemu, false);
    rcc.setLabelLaF(rcmbPrikaz, false);
    /*
     * if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR")){
     * rcmbSljed.setRaItems(new String[][] { {"Kupac", "CPAR"} , {"Iznos
     * ra�una", "IPRODSP"}}); fieldSet.setString("SLJED","CPAR"); } 
     * else
     */
    if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("UI")) {
       rcmbSljed.setRaItems(new String[][]{{"�ifra", "CART"}, {"Naziv", "NAZART"}, {"Koli�ina", "KOL"}, {"RuC", "RUC"}, {"RuC %", "PostoRUC"}, {"Vrijednost", "IPRODSP"}});
      fieldSet.setString("SLJED", "CART");
    } else {
//      rcmbSljed.setRaItems(new String[][]{{"�ifra", "CART"}, {"Ukupno " +
// rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(),
// "BRDOK"}});
      fieldSet.setString("SLJED", "CART");
//      fieldSet.setString("POCEMU", "INAB");
//      rcc.setLabelLaF(rcmbPoCemu, true);
    }
    rcmbSljed.setSelectedIndex(0);
  }

//  private void setSljedDva() {
//    int position;
//    try {
//      position = rcmbSljed.getSelectedIndex();
//    } catch (Exception ex) {
//      position = 0;
//    }
//    rcmbSljed.setRaItems(new String[][]{{"�ifra", "CART"}, {"Ukupno " + rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(), "BRDOK"}});
//    try {
//      rcmbSljed.setSelectedIndex(position);
//    } catch (Exception ex) {}
//  }

  private String getKolonu() {
    return fieldSet.getString("POCEMU");
  }

  public double getPostoSumRuc() {
    return postoSumRuc;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrucQDS() {
    return jptvSet;
  }
  
  public QueryDataSet getDoubleclickSet(){
    return prikazSet;
  }

  public QueryDataSet getReportSet() {
    return monthSet;
  }

  public QueryDataSet getReportQDS() {
    return reportSet;
  }

  public QueryDataSet getReportQDSDet(){
    return reportSetDet;
  }

  public QueryDataSet getChartSet() {
    return monthSetGraph;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportQDSDetaljno() {
    return reportSet2;
  }

//  public boolean isIspis() {
//    return (getJPTV().getDataSet() != null);
//  }

  public boolean isIspis() {
    //  System.out.println("\njptv ima dataset - " +
    // (getJPTV().getDataSet()!=null));
    //  System.out.println("isIspis - " + isIspis+"\n");
    return (getJPTV().getDataSet() != null && isIspis);
  }

  public void jptv_doubleClick() {
    if (!doubleclicked) {
      doubleclicked = true;
      isIspis = false;
      ok_action();
      isIspis = true;
    } else {
    	DataSet ds = getJPTV().getDataSet();
      util.showDocs(ds.getString("CSKL"), "",	ds.getString("VRDOK"), 
      		ds.getInt("BRDOK"), ds.getString("GOD"),
      		Integer.toString(ds.getInt("CART")));
    }
  }

  public boolean isPoRacunima() {
    return fieldSet.getString("PRIKAZ").equals("IR");
  }

  public String getPoCemu() {
    return rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString();
  }

  public String getPeriod() {
    return raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("pocDatum")) + " - " + raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("zavDatum"));
  }
}