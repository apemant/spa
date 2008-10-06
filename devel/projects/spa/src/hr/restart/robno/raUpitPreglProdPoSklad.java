/****license*****************************************************************
**   file: raUpitPreglProdPoSklad.java
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

import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class raUpitPreglProdPoSklad extends raUpitLite {

  private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  private XYLayout xylayout = new XYLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private JPanel jpmain = new JPanel();
  private JLabel datum = new JLabel("Datum (od - do)");
  private JraTextField datumod = new JraTextField();
  private JraTextField datumdo = new JraTextField();
  private jpMoreCsklad jMCs = new jpMoreCsklad();
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private raCommonClass rcc = raCommonClass.getraCommonClass();

  // main datasets -BOD

  private StorageDataSet sdstmp;
  private QueryDataSet qds1 = new QueryDataSet();

  // main datasets -EOD


  private static raUpitPreglProdPoSklad instanceOfMe;

  public static raUpitPreglProdPoSklad getInstance(){
    return instanceOfMe;
  }

  public raUpitPreglProdPoSklad() {
    try {
      jbInit();
      instanceOfMe=this;
    }
    catch (Exception ex) {
      System.err.println("jbInit failed!!!");
      ex.printStackTrace();
    }
  }

  public boolean runFirstESC() {
    return (!jMCs.skladista.getText().equalsIgnoreCase(""));
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jMCs.skladista.setText("");
      }
    });
  }

  public void componentShow() {
    sdstmp.setTimestamp("DATUMOD", hr.restart.util.Util.getUtil().getFirstDayOfMonth(val.getToday()));
    sdstmp.setTimestamp("DATUMDO", hr.restart.util.Util.getUtil().getLastDayOfMonth(val.getToday()));
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jMCs.skladista.setText("");
      }
    });
  }

  public void okPress() {

    String cskl ="";
    if (!jMCs.getTokeni().equalsIgnoreCase("")){
      cskl ="and doki.cskl in"+jMCs.getTokeni();
    }

    String datumod = hr.restart.util.Util.getUtil().getFirstSecondOfDay(sdstmp.getTimestamp("DATUMOD")).toString();
    String datumdo = hr.restart.util.Util.getUtil().getLastSecondOfDay(sdstmp.getTimestamp("DATUMDO")).toString();

    String sqlupit = "SELECT MAX(doki.cskl) as cskl,SUM(inab) as inab,SUM(iprodbp) as iprodbp,"+
                     "SUM(iprodsp) as iprodsp FROM doki,stdoki WHERE doki.cskl = stdoki.cskl AND"+
                     " doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND doki.brdok = "+
                     "stdoki.brdok and vrdok in ('GOT','GRN','POS','ROT') and doki.datdok "+
                     "between '"+datumod+"' and '"+datumdo+"' "+cskl+" group by cskl";

    System.out.println("\nQS"+sqlupit+"\n");

    QueryDataSet qds_tmp = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);
    qds1.deleteAllRows();

    for (qds_tmp.first();qds_tmp.inBounds();qds_tmp.next()) {
      if (!ld.raLocate(qds1,"CSKL",qds_tmp.getString("CSKL"))) {
        qds1.insertRow(true);
        qds1.setString("CSKL",qds_tmp.getString("CSKL"));
      }
      qds1.setBigDecimal("STZALNC",qds1.getBigDecimal("STZALNC").add(
          qds_tmp.getBigDecimal("INAB")));
      qds1.setBigDecimal("UPVNOPDV",qds1.getBigDecimal("UPVNOPDV").add(
          qds_tmp.getBigDecimal("IPRODBP")));
      qds1.setBigDecimal("UNABVRI",qds1.getBigDecimal("UNABVRI").add(
          qds_tmp.getBigDecimal("INAB")));
      qds1.setTimestamp("DATUMOD",sdstmp.getTimestamp("DATUMOD"));
      qds1.setTimestamp("DATUMDO",sdstmp.getTimestamp("DATUMDO"));
    }
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    syst.prn(qds1);

  }

  private void initSets(){
    sdstmp = new StorageDataSet();
    sdstmp.setColumns(new Column[] {
                      dm.createTimestampColumn("DATUMOD"),
                      dm.createTimestampColumn("DATUMDO")
    });
    sdstmp.open();
    qds1.setColumns(new Column[] {
                   dm.createStringColumn("CSKL",12),
                   dm.createBigDecimalColumn("STZALNC",2),
                   dm.createBigDecimalColumn("UPVNOPDV",2),
                   dm.createBigDecimalColumn("UNABVRI",2),
                   dm.createBigDecimalColumn("PROVKART",2),
                   dm.createBigDecimalColumn("RUC",2),
                   dm.createBigDecimalColumn("PRUC",2),
                   dm.createBigDecimalColumn("UKUTR",2),
                   dm.createTimestampColumn("DATUMOD"),
                   dm.createTimestampColumn("DATUMDO")
    });
    qds1.open();
  }



  private void jbInit() {
    initSets();

//    this.addReport("hr.restart.robno.repMjeIzvjMaloprodaja",
//                   "hr.restart.robno.repMjeIzvjMaloprodaja",
//                   "MjeIzvjMaloprodaja",
//                   "Izvještaj maloprodaje");

    datumod.setDataSet(sdstmp);
    datumod.setColumnName("DATUMOD");
    datumdo.setDataSet(sdstmp);
    datumdo.setColumnName("DATUMDO");
    xylayout.setWidth(370);
    xylayout.setHeight(75);
    jpmain.setLayout(xylayout);
    jMCs.setFrameOwner((Frame) this.getFrameOwner());

    jpmain.setBorder(BorderFactory.createEtchedBorder());
    jpmain.add(jMCs,new XYConstraints(0,15,-1,-1));
    jpmain.add(datum, new XYConstraints(15,40,100,-1));
    jpmain.add(datumod, new XYConstraints(150,40,100,-1));
    jpmain.add(datumdo, new XYConstraints(255,40,100,-1));
    setJPan(jpmain);
    new raDateRange(datumod, datumdo);
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow(){
    return false;
  }

  public java.sql.Timestamp getDATUMOD() {
    return sdstmp.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDATUMDO() {
    return sdstmp.getTimestamp("DATUMDO");
  }


  public  QueryDataSet getQds(){
    return qds1;
  }

}
