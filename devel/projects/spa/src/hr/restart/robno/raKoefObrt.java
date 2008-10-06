/****license*****************************************************************
**   file: raKoefObrt.java
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

import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import java.math.BigDecimal;

import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2002. lipanj 20
 */
public class raKoefObrt extends hr.restart.util.raUpit {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  QueryDataSet QDSnabava = new QueryDataSet();
  QueryDataSet QDSprodaja = new QueryDataSet();
  QueryDataSet QDSulazMeskla  = new QueryDataSet();
  QueryDataSet QDSizlazMeskla  = new QueryDataSet();
  QueryDataSet DSkoefobrt = new QueryDataSet();
  StorageDataSet DSkoefobrtSUM = new StorageDataSet();
  StorageDataSet DSkoefobrtPOJ = new StorageDataSet();
  jptestKoef jtKoef = new jptestKoef();
  JPanel jPanel1 = new JPanel();
  hr.restart.swing.raTableColumnModifier TCM  ;
  hr.restart.swing.raTableColumnModifier TCM1 ;
  int brojDana = 0;
  static raKoefObrt rko;
  boolean MyfirstEsc = true;
  hr.restart.util.reports.raRunReport rr = hr.restart.util.reports.raRunReport.getRaRunReport();

  TypeDoc TD = TypeDoc.getTypeDoc();
  String sqlsklad="";
  
  /**
   *  koeficijent obrtaja omjer izmedju nabavljene robe i prodane robe u
   *  odredjenom periodu
   */
  public void componentShow() {
    jtKoef.DummySet.setTimestamp("DATOD", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(hr.restart.util.Valid.getValid().findYear())));
    jtKoef.DummySet.setTimestamp("DATDO", hr.restart.util.Valid.getValid().getToday());
    jtKoef.rpcart.setDefParam();
    jtKoef.rpskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
    jtKoef.rpskl.jrfCSKL.forceFocLost();
    firstESC();
  }
  /**
   *  Description of the Method
   */
  public void firstESC() {
    jtKoef.setEnDis(true);
    getJPTV().removeAllTableModifiers();
    this.getJPTV().setDataSet(null);
    if (!jtKoef.rpskl.jrfCSKL.getText().equalsIgnoreCase("") && !MyfirstEsc) {
        jtKoef.rpskl.jrfCSKL.setText("");
        jtKoef.rpskl.jrfCSKL.forceFocLost();
    }
    jtKoef.rpskl.jrfCSKL.requestFocus();
    MyfirstEsc    = false;
  }

  public void  jptv_doubleClick(){

    DSkoefobrtPOJ.emptyAllRows();
    int cart = DSkoefobrt.getInt("CART");
    int position = DSkoefobrt.getRow();
System.out.println(cart);

    DSkoefobrt.enableDataSetEvents(false);
    this.getJPTV().enableEvents(false);
    DSkoefobrt.first();
    do {
      if (DSkoefobrt.getInt("CART") == cart){
        DSkoefobrtPOJ.insertRow(true);
        DSkoefobrtPOJ.setString("CSKL", DSkoefobrt.getString("CSKL"));
        DSkoefobrtPOJ.setInt("CART", DSkoefobrt.getInt("CART"));
        DSkoefobrtPOJ.setBigDecimal("KOEFICIJENT", DSkoefobrt.getBigDecimal("KOEFICIJENT"));
        DSkoefobrtPOJ.setBigDecimal("DANZAL",DSkoefobrt.getBigDecimal("DANZAL"));
      }
    } while (DSkoefobrt.next());
    DSkoefobrt.goToClosestRow(position);
    DSkoefobrt.enableDataSetEvents(true);
    this.getJPTV().enableEvents(true);

    rr.clearAllReports();
    rr.addReport("hr.restart.robno.repKoefChart","Grafikon koeficijenata",17);
    rr.go();
  }

  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */

  public boolean runFirstESC() {
      System.out.println(MyfirstEsc && !jtKoef.rpskl.jrfCSKL.getText().equalsIgnoreCase(""));
    return (MyfirstEsc || !jtKoef.rpskl.jrfCSKL.getText().equalsIgnoreCase(""));
  }

  /**
   *  Description of the Method
   */

  public void okPress() {
//System.out.println("okPress");     

	    this.getJPTV().setDataSet(null);
      sqlsklad=prepareSkladista();
      MyfirstEsc = true;
      int dd = raDateUtil.getraDateUtil().DateDifference(new java.util.Date(jtKoef.DummySet.getTimestamp("DATOD").getTime()),new java.util.Date(jtKoef.DummySet.getTimestamp("DATDO").getTime()));
      brojDana = java.lang.Math.abs(dd);
      findNabava();
      findProdaja();
      internalSell(true);
      internalSell(false);
      merdjaj();
      checkClosing();
//    System.err.println("merdjanje komenst");

    TCM =  new hr.restart.swing.raTableColumnModifier("CSKL",
        new String [] {"CSKL","NAZSKL"},
        new String[] {"CSKL"},
        dm.getSklad());

    String[] nekivrag = {Aut.getAut().getCARTdependable("CART","CART1","BC"),"NAZART"};

    TCM1 =  new hr.restart.swing.raTableColumnModifier("CART",
        nekivrag,
        new String[] {"CART"},
        dm.getArtikli());
    this.getJPTV().addTableModifier( TCM);
    this.getJPTV().addTableModifier( TCM1);
    this.getJPTV().setDataSet(DSkoefobrt);
  }

  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public boolean Validacija() {
//    System.err.println("validacija proshla....");
    return jtKoef.validacija();
  }

  public raKoefObrt() {
    addReport("hr.restart.robno.repKoefObrt", "Izra\u010Dun koeficienta obrtaja", 2);
    try {
      rr.setOwner(this.getWindow(),getClass().getName());
      dbInit();
      jbInit();
      rko = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static raKoefObrt getInstance() {
    if (rko == null) rko = new raKoefObrt();
    return rko;
  }

  public void dbInit() {
    Column col_1 = (Column) dm.getArtikli().getColumn("CART").clone();
    col_1.setCaption("Artikl");
    col_1.setWidth(20);
    Column col_1a = (Column) dm.getStdoku().getColumn("CSKL").clone();
    col_1a.setCaption("Prod. mjesto");
    col_1a.setWidth(20);
    Column col_2 = (Column) dm.getStdoku().getColumn("IZAD").clone();
    col_2.setColumnName("NABAVA");
    col_2.setDisplayMask("###,##0.000");
    col_2.setCaption("Nabava");
    Column col_3 = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_3.setColumnName("PRODAJA");
    col_3.setCaption("Prodaja");
    col_3.setDisplayMask("###,##0.000");
    Column col_4 = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_4.setColumnName("KOEFICIJENT");
    col_4.setCaption("Koeficijent");
    col_4.setScale(4);
    col_4.setDisplayMask("###,##0.0000");
    Column col_5 = (Column) dm.getDoki().getColumn("DATDOK").clone();
    col_5.setColumnName("DATUMZNAB");
    col_5.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column col_6 = (Column) dm.getDoki().getColumn("DATDOK").clone();
    col_6.setColumnName("DATUMZPROD");
    col_6.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column col_7 = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_7.setColumnName("KOEF");
    col_7.setDisplayMask("###,##0.0000");
    Column col_8 = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_8.setColumnName("SUMNAB");
    col_8.setDisplayMask("###,##0.000");
    col_8.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column col_9 = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_9.setColumnName("SUMPRO");
    col_9.setDisplayMask("###,##0.000");
    col_9.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column col_A = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_A.setColumnName("KOEFSUM");
    col_A.setScale(4);
    col_A.setDisplayMask("###,##0.0000");
    col_A.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    Column col_B = (Column) dm.getStdoki().getColumn("IRAZ").clone();
    col_B.setColumnName("DANZAL");
    col_B.setScale(3);
    col_B.setDisplayMask("###,##0");
    col_B.setCaption("Br. dana vezivanja");
    col_B.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
    Column col_C = (Column) col_B.clone();
    col_C.setColumnName("DANZALSUM");
    col_C.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    DSkoefobrt = new QueryDataSet();
    DSkoefobrt.addColumn(col_1);
    DSkoefobrt.addColumn(col_1a);
    DSkoefobrt.addColumn(col_2);
    DSkoefobrt.addColumn(col_3);
    DSkoefobrt.addColumn(col_4);
    DSkoefobrt.addColumn(col_5);
    DSkoefobrt.addColumn(col_6);
    DSkoefobrt.addColumn(col_8);
    DSkoefobrt.addColumn(col_9);
    DSkoefobrt.addColumn(col_A);
    DSkoefobrt.addColumn(col_B);
    DSkoefobrt.addColumn(col_C);
    DSkoefobrt.open();

    DSkoefobrtSUM.addColumn((Column)col_1.clone());
    DSkoefobrtSUM.addColumn((Column)col_1a.clone());
    DSkoefobrtSUM.addColumn((Column)col_2.clone());
    DSkoefobrtSUM.addColumn((Column)col_3.clone());
    DSkoefobrtSUM.addColumn((Column)col_4.clone());
    DSkoefobrtSUM.addColumn((Column)col_5.clone());
    DSkoefobrtSUM.addColumn((Column)col_6.clone());
    DSkoefobrtSUM.open();
    DSkoefobrtPOJ.addColumn((Column)col_1.clone());
    DSkoefobrtPOJ.addColumn((Column)col_1a.clone());
    DSkoefobrtPOJ.addColumn((Column)col_4.clone());
    DSkoefobrtPOJ.addColumn((Column)col_B.clone());
    DSkoefobrtPOJ.open();
  }

  public String prepareSkladista(){
      VarStr forreturn=new VarStr("('");
      if (jtKoef.rpskl.jrfCSKL.getText().equalsIgnoreCase("")) {
          QueryDataSet qds = ut.getNewQueryDataSet("SELECT CSKL FROM SKLAD WHERE knjig='"+
                  hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG()+"'");
          for (qds.first();qds.inBounds();qds.next()){
              forreturn.append(qds.getString("CSKL")).append("','");
          }
          forreturn.chopRight(2).append(")");
          return forreturn.toString();
      } 
      return forreturn.append(jtKoef.rpskl.jrfCSKL.getText()).append("')").toString();
  }
  
  public String prepareCart(){
      if (!jtKoef.rpcart.getCART().equalsIgnoreCase("")){
          return " and artikli.cart ="+jtKoef.rpcart.getCART();
      } else if (!jtKoef.rpcart.getCART1().equalsIgnoreCase("")){
          return " and artikli.cart1 like ('"+
          jtKoef.rpcart.getCART1()+"%')";
      } else if (!jtKoef.rpcart.getBC().equalsIgnoreCase("")){
          return " and artikli.bc like ('"+
          jtKoef.rpcart.getBC()+"%')";
      } else if (!jtKoef.rpcart.getNAZART().equalsIgnoreCase("")){
          return " and artikli.NAZART like ('"+
          jtKoef.rpcart.getBC()+"%')";
      } else if (!jtKoef.rpcart.getCGRART().equalsIgnoreCase("")){
          return " and artikli.CGRART ='"+
          jtKoef.rpcart.getCGRART()+"'";
      } 
      return "";
  }
  
  
  public String prepareUlazDok(boolean meskla){
    	if (meskla){
      	    return "('MES','MEU')";
      	}
      	VarStr vs = new VarStr("('");
      	for (int i = 0;i<TD.araj_docs.length;i++){
      	    if (TD.isDocSklad(TD.araj_docs[i]) && TD.isDocUlaz(TD.araj_docs[i])
      	            && TD.isDocDiraZalihu(TD.araj_docs[i])){
      	        vs.append(TD.araj_docs[i]).append("','");
      	    }
      	}
  	    vs.chop(2).append(")");
      	return vs.toString();

  }
  
  public String prepareIzlazDok(boolean meskla){
      
      	if (meskla){
      	    return "('MES','MEI')";
      	}
      	VarStr vs = new VarStr("('");
      	for (int i = 0;i<TD.araj_docs.length;i++){
      	    if (TD.isDocSklad(TD.araj_docs[i]) && !TD.isDocUlaz(TD.araj_docs[i])){
      	        vs.append(TD.araj_docs[i]).append("','");
      	    }
      	}
  	    vs.chop(2).append(")");
      	return vs.toString();
  }
  
  
  
  
  /**
   *  Description of the Method
   *
   *@param  grupa  Description of the Parameter
   *@param  cart   Description of the Parameter
   *@param  cskl   Description of the Parameter
   *@param  datod  Description of the Parameter
   *@param  datdo  Description of the Parameter
   */
  public void findNabava() {
    String queryString = "";

    queryString = 
        "select stdoku.cart as CCART, stdoku.cskl as CCSKL, "+
                      "sum(stdoku.kol) as nabava from doku,stdoku,artikli "+
                      "where doku.cskl in"+sqlsklad+
                      " and doku.datdok between '"+ut.clearTime(jtKoef.DummySet.getTimestamp("DATOD")).toString()+
                      "' and '"+ut.clearTime(jtKoef.DummySet.getTimestamp("DATDO")).toString()+"' and "+
                      " doku.cskl=stdoku.cskl and artikli.cart=stdoku.cart and "+
                      "doku.vrdok=stdoku.vrdok and doku.god=stdoku.god and doku.brdok=stdoku.brdok  " +
                      prepareCart()+ " and doku.vrdok in "+prepareUlazDok(false)+
                      " group by stdoku.cskl, stdoku.cart";
System.out.println(queryString);
    QDSnabava = hr.restart.util.Util.getNewQueryDataSet(queryString,false);
    openScratchDataSet(QDSnabava);
  }

  public void findProdaja() {

    String queryString = 
        "select stdoki.cart as CCART, stdoki.cskl as CCSKL, sum(stdoki.kol) as prodaja from doki,stdoki,artikli " +
    "where doki.cskl in "+sqlsklad+" and doki.datdok between '"+ut.clearTime(jtKoef.DummySet.getTimestamp("DATOD")).toString()+
                  "' and '"+ut.clearTime(jtKoef.DummySet.getTimestamp("DATDO")).toString()+"' and "+
                  "doki.cskl=stdoki.cskl and artikli.cart=stdoki.cart and "+
                  "doki.vrdok=stdoki.vrdok and doki.god=stdoki.god and doki.brdok=stdoki.brdok  " +
                  prepareCart() + " and doki.vrdok in "+prepareIzlazDok(false)+ 
                  " group by stdoki.cskl, stdoki.cart";

    System.out.println(queryString);
    QDSprodaja = hr.restart.util.Util.getNewQueryDataSet(queryString,false);
    openScratchDataSet(QDSprodaja);

  }

  public void internalSell(boolean ulaz) {
        String queryString = "";
        if (ulaz) {
            queryString = "select stmeskla.cart as CCART, stmeskla.csklul as CCSKL, "
                    + "sum(stmeskla.kol) as NABAVA from "
                    + "meskla,stmeskla,artikli "
                    + "where meskla.datdok between '"
                    + ut.clearTime(jtKoef.DummySet.getTimestamp("DATOD"))
                            .toString()
                    + "' and '"
                    + ut.clearTime(jtKoef.DummySet.getTimestamp("DATDO"))
                            .toString()
                    + "' and "
                    + " stmeskla.cart=artikli.cart and "
                    + " meskla.csklul=stmeskla.csklul and "
                    + " meskla.cskliz=stmeskla.cskliz and "
                    + " meskla.vrdok=stmeskla.vrdok and meskla.god=stmeskla.god "
                    + " and meskla.brdok=stmeskla.brdok and meskla.csklul in "
                    + sqlsklad
                    + " "
                    + prepareCart()
                    + " and meskla.vrdok in "
                    + prepareUlazDok(true)
                    + " group by stmeskla.csklul, stmeskla.cart";

            System.out.println(queryString);

            QDSulazMeskla = hr.restart.util.Util.getNewQueryDataSet(
                    queryString, true);
        } else {
            queryString = "select stmeskla.cart as CCART, stmeskla.cskliz as CCSKL, "
                    + "sum(stmeskla.kol) as prodaja from "
                    + "meskla,stmeskla,artikli "
                    + "where meskla.datdok between '"
                    + ut.clearTime(jtKoef.DummySet.getTimestamp("DATOD"))
                            .toString()
                    + "' and '"
                    + ut.clearTime(jtKoef.DummySet.getTimestamp("DATDO"))
                            .toString()
                    + "' and "
                    + " stmeskla.cart=artikli.cart and "
                    + " meskla.csklul=stmeskla.csklul and "
                    + " meskla.cskliz=stmeskla.cskliz and "
                    + " meskla.vrdok=stmeskla.vrdok and meskla.god=stmeskla.god "
                    + " and meskla.brdok=stmeskla.brdok and meskla.cskliz in "
                    + sqlsklad
                    + " "
                    + prepareCart()
                    + " and meskla.vrdok in "
                    + prepareUlazDok(false)
                    + " group by stmeskla.cskliz, stmeskla.cart";

//            System.out.println(queryString);
            QDSizlazMeskla = hr.restart.util.Util.getNewQueryDataSet(
                    queryString, true);
        }
    }


  /**
   *  Description of the Method
   */
  public void merdjaj() {
    DSkoefobrt.emptyAllRows();
    BigDecimal tmpBD;
    if (QDSnabava.getRowCount()!=0) {
    for (QDSnabava.first();QDSnabava.inBounds();QDSnabava.next()){
      DSkoefobrt.insertRow(true);
      DSkoefobrt.setInt("CART", QDSnabava.getInt("CCART"));
      DSkoefobrt.setString("CSKL", QDSnabava.getString("CCSKL"));
      DSkoefobrt.setBigDecimal("NABAVA", QDSnabava.getBigDecimal("NABAVA"));
      DSkoefobrt.setBigDecimal("PRODAJA", new BigDecimal("0.0000"));
    }
    }

    if (QDSprodaja.getRowCount()!=0) {
    for (QDSprodaja.first();QDSprodaja.inBounds();QDSprodaja.next()){
      if (!hr.restart.util.lookupData.getlookupData().raLocate(DSkoefobrt, new String[]{"CART", "CSKL"},
                       new String[]{String.valueOf(QDSprodaja.getInt("CCART")), QDSprodaja.getString("CCSKL")},
                       com.borland.dx.dataset.Locate.CASE_INSENSITIVE)) {
        DSkoefobrt.insertRow(true);
        DSkoefobrt.setInt("CART", QDSprodaja.getInt("CCART"));
        DSkoefobrt.setString("CSKL", QDSprodaja.getString("CCSKL"));
        DSkoefobrt.setBigDecimal("NABAVA", new BigDecimal("0.0000"));
      }
      DSkoefobrt.setBigDecimal("PRODAJA", QDSprodaja.getBigDecimal("PRODAJA"));      
      }
    }
    if (QDSulazMeskla.getRowCount()!=0) {
    for (QDSulazMeskla.first();QDSulazMeskla.inBounds();QDSulazMeskla.next()){
        if (!hr.restart.util.lookupData.getlookupData().raLocate(DSkoefobrt, new String[]{"CART", "CSKL"},
               new String[]{String.valueOf(QDSulazMeskla.getInt("CCART")), QDSulazMeskla.getString("CCSKL")},
                    com.borland.dx.dataset.Locate.CASE_INSENSITIVE)) {
          DSkoefobrt.insertRow(true);
          DSkoefobrt.setInt("CART", QDSulazMeskla.getInt("CCART"));
          DSkoefobrt.setString("CSKL", QDSulazMeskla.getString("CCSKL"));
          DSkoefobrt.setBigDecimal("NABAVA", new BigDecimal("0.0000"));
          DSkoefobrt.setBigDecimal("PRODAJA", new BigDecimal("0.0000"));
        }
        DSkoefobrt.setBigDecimal("NABAVA",DSkoefobrt.getBigDecimal("NABAVA").add( 
                QDSulazMeskla.getBigDecimal("NABAVA")));
      }
    }
    if (QDSizlazMeskla.getRowCount()!=0) {
        for (QDSizlazMeskla.first();QDSizlazMeskla.inBounds();QDSizlazMeskla.next()){
            if (!hr.restart.util.lookupData.getlookupData().raLocate(DSkoefobrt, new String[]{"CART", "CSKL"},
                             new String[]{String.valueOf(QDSizlazMeskla.getInt("CCART")), QDSizlazMeskla.getString("CCSKL")},
                             com.borland.dx.dataset.Locate.CASE_INSENSITIVE)) {
              DSkoefobrt.insertRow(true);
              DSkoefobrt.setInt("CART", QDSizlazMeskla.getInt("CCART"));
              DSkoefobrt.setString("CSKL", QDSizlazMeskla.getString("CCSKL"));
              DSkoefobrt.setBigDecimal("NABAVA", new BigDecimal("0.0000"));
              DSkoefobrt.setBigDecimal("PRODAJA", new BigDecimal("0.0000"));
            }
            DSkoefobrt.setBigDecimal("PRODAJA",DSkoefobrt.getBigDecimal("PRODAJA").add(
                    QDSizlazMeskla.getBigDecimal("PRODAJA")));
          }
        }
    
    
    
    
    if (DSkoefobrt.getRowCount()==0) return;

    for (DSkoefobrt.first();DSkoefobrt.inBounds();DSkoefobrt.next()){
      try {
        DSkoefobrt.setBigDecimal("KOEFICIJENT", DSkoefobrt.getBigDecimal("PRODAJA").divide(
            DSkoefobrt.getBigDecimal("NABAVA"), 8, BigDecimal.ROUND_HALF_UP));
      }
      catch (Exception ex) {
        DSkoefobrt.setBigDecimal("KOEFICIJENT", new BigDecimal("0.0000"));
      }
      try {
          
          //vezivanje = dani/koef
          
          
        DSkoefobrt.setBigDecimal("DANZAL", new BigDecimal(String.valueOf(brojDana)).divide(
                DSkoefobrt.getBigDecimal("KOEFICIJENT"),0, BigDecimal.ROUND_HALF_UP));

        
      }
      catch (Exception ex) {
        DSkoefobrt.setBigDecimal("DANZAL", Aus.zero3);
      }
    }
    DSkoefobrt.first();
    boolean first = true;
    int kkk =0;
//    System.err.println("petlja 4 in");
    do {
      kkk++;
      boolean canGo = true;
      if (first) {

        DSkoefobrtSUM.insertRow(false);
        DSkoefobrtSUM.setInt("CART", DSkoefobrt.getInt("CART"));
        DSkoefobrtSUM.setBigDecimal("NABAVA", DSkoefobrt.getBigDecimal("NABAVA"));
        DSkoefobrtSUM.setBigDecimal("PRODAJA", DSkoefobrt.getBigDecimal("PRODAJA"));
        first = false;
      } else {
        DSkoefobrtSUM.first();
        do {
          if(DSkoefobrt.getInt("CART") == DSkoefobrtSUM.getInt("CART")){
            DSkoefobrtSUM.setBigDecimal("NABAVA", DSkoefobrtSUM.getBigDecimal("NABAVA").add(DSkoefobrt.getBigDecimal("NABAVA")));
            DSkoefobrtSUM.setBigDecimal("PRODAJA", DSkoefobrtSUM.getBigDecimal("PRODAJA").add(DSkoefobrt.getBigDecimal("PRODAJA")));
            canGo = false;
          }
        } while (DSkoefobrtSUM.next());
        if (canGo) {
          DSkoefobrtSUM.insertRow(false);
          DSkoefobrtSUM.setInt("CART", DSkoefobrt.getInt("CART"));
          DSkoefobrtSUM.setBigDecimal("NABAVA", DSkoefobrt.getBigDecimal("NABAVA"));
          DSkoefobrtSUM.setBigDecimal("PRODAJA", DSkoefobrt.getBigDecimal("PRODAJA"));
        }
      }
    } while (DSkoefobrt.next());
    DSkoefobrt.first();
    do {
      DSkoefobrtSUM.first();
      do {
        if (DSkoefobrt.getInt("CART") == (DSkoefobrtSUM.getInt("CART"))){
          DSkoefobrt.setBigDecimal("SUMNAB", DSkoefobrtSUM.getBigDecimal("NABAVA"));
          DSkoefobrt.setBigDecimal("SUMPRO", DSkoefobrtSUM.getBigDecimal("PRODAJA"));
          try {
            DSkoefobrt.setBigDecimal("KOEFSUM", DSkoefobrtSUM.getBigDecimal("PRODAJA").divide(
                DSkoefobrtSUM.getBigDecimal("NABAVA"), 8, BigDecimal.ROUND_HALF_UP));
          }
          catch (Exception ex) {
            DSkoefobrt.setBigDecimal("KOEFSUM", new BigDecimal("0.0000"));
          }
          try {
            DSkoefobrt.setBigDecimal("DANZALSUM", new BigDecimal(String.valueOf(brojDana)).divide(new BigDecimal("100.00"),3, BigDecimal.ROUND_HALF_UP)
                                     .divide(DSkoefobrt.getBigDecimal("KOEFSUM"),3, BigDecimal.ROUND_HALF_UP));
          }
          catch (Exception ex) {
            DSkoefobrt.setBigDecimal("DANZALSUM", Aus.zero3);
          }
        }
      } while (DSkoefobrtSUM.next());
    } while (DSkoefobrt.next());
  }

  public StorageDataSet getqDSKoefObrtPOJ(){
    return DSkoefobrtPOJ;
  }
  public StorageDataSet getQDSKoefObrt(){
    return DSkoefobrt;
  }
  public java.sql.Timestamp getPocDatum() {
    return jtKoef.DummySet.getTimestamp("DATOD");
  }
  public java.sql.Timestamp getZavDatum() {
    return jtKoef.DummySet.getTimestamp("DATDO");
  }
  public boolean isGrupa() {
    return (jtKoef.getCART().equals("") && !jtKoef.getCGRART().equals(""));//jtKoef.jrbTip1.isSelected();
  }
  public String getCGART() {
    return jtKoef.getCGRART();//jtKoef.jlrCgrart.getText().trim();
  }
  public String getNAZGRART() {
//    return jtKoef.jlrNazgrart.getText().trim();

    hr.restart.util.lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",jtKoef.getCGRART());
    return dm.getGrupart().getString("NAZGRART");
  }

  private void jbInit() throws Exception {
    setJPan(jtKoef);
  }
}