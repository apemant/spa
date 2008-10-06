/****license*****************************************************************
**   file: raUpitRekapProd.java
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raUpitRekapProd extends hr.restart.util.raUpitLite {

  private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
//  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private XYLayout xylayout = new XYLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private JPanel jpmain = new JPanel();
  private JLabel datum = new JLabel("Datum (od - do)");
  private JraTextField datumod = new JraTextField();
  private JraTextField datumdo = new JraTextField();
  private QueryDataSet qds = new QueryDataSet();
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private jpMoreCsklad jMCs = new jpMoreCsklad();
  private static raUpitRekapProd me;

  {
    initSDS();
    setupQDS();
  }

  public  QueryDataSet getQds(){
    return qds;
  }

  public static raUpitRekapProd getInstance(){
    return me;
  }

  public void componentShow(){
    setDatum();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        datumod.selectAll();
      }
    });
  }
  public void firstESC(){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        datumod.requestFocus();
      }
    });
  }
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow(){
    return !nemaPodataka;
  }
  private boolean nemaPodataka;
  public void okPress() {
    nemaPodataka = false;
    String datumod = hr.restart.util.Util.getUtil().getFirstSecondOfDay(sdstmp.getTimestamp("DATUMOD")).toString();
    String datumdo = hr.restart.util.Util.getUtil().getLastSecondOfDay(sdstmp.getTimestamp("DATUMDO")).toString();
    String cskl ="";
    if (!jMCs.getTokeni().equalsIgnoreCase("")){
      cskl ="and doki.cskl in"+jMCs.getTokeni();
    }

    String sqlupit = "SELECT MAX(doki.cskl) as cskl,"+
                     "SUM(iprodsp) as iprodsp ,SUM(irata) as IIRATA,MAX(CNACPL) as ccnacpl, "+
                     "0.00 as PROVKART ,MAX(fl_kartica) as fk,MAX(fl_cek) as fc "+
                     "FROM doki,stdoki,rate,nacpl WHERE doki.cskl = stdoki.cskl "+
                     "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "+
                     "AND doki.brdok = stdoki.brdok AND stdoki.cskl = rate.cskl "+
                     "AND stdoki.vrdok = rate.vrdok AND stdoki.god = rate.god "+
                     "AND stdoki.brdok = rate.brdok AND stdoki.rbr = rate.rbr "+
                     "and vrdok in ('GOT','GRN','POS') and doki.datdok between '"+
                     datumod+"' and '"+datumdo+
                     "' and nacpl.cnacpl=rate.cnacpl "+cskl+
                     " group by cskl,cnacpl";

System.out.println(sqlupit);
    QueryDataSet qds_tmp = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);
    qds.deleteAllRows();

    for (qds_tmp.first();qds_tmp.inBounds();qds_tmp.next()) {

      if (!ld.raLocate(qds,"CSKL",qds_tmp.getString("CSKL"))) {
        qds.insertRow(true);
        qds.setString("CSKL",qds_tmp.getString("CSKL"));
      }
      qds.setBigDecimal("IZNOSRACUNA",qds.getBigDecimal("IZNOSRACUNA").add(
          qds_tmp.getBigDecimal("IPRODSP")));
      if (qds_tmp.getString("fk").equalsIgnoreCase("D")){
        qds.setBigDecimal("KREDKART",qds.getBigDecimal("KREDKART").add(
            qds_tmp.getBigDecimal("IIRATA")));
      }
      if (qds_tmp.getString("fc").equalsIgnoreCase("D")){
        qds.setBigDecimal("CEK",qds.getBigDecimal("CEK").add(
            qds_tmp.getBigDecimal("IIRATA")));
      }
      qds.setTimestamp("DATOD",sdstmp.getTimestamp("DATUMOD"));
      qds.setTimestamp("DATDO",sdstmp.getTimestamp("DATUMDO"));
    }

/*
SELECT MAX(doki.cskl) as cskl, SUM(iprodsp) as iprodsp
     FROM doki,stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND vrdok in ('ROT') and doki.datdok between '2003-09-01 00:00:00.0' and '2003-09-30 00:00:00.0' group by cskl

     */

    sqlupit = "SELECT MAX(doki.cskl) as cskl, SUM(iprodsp) as iprodsp "+
              "FROM doki,stdoki WHERE doki.cskl = stdoki.cskl "+
              "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "+
              "AND doki.brdok = stdoki.brdok AND vrdok in ('ROT') "+
              "and doki.datdok between '"+
              datumod+"' and '"+datumdo+"' "+cskl+
              " group by cskl";

    System.out.println(sqlupit);

    qds_tmp = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);

    for (qds_tmp.first();qds_tmp.inBounds();qds_tmp.next()) {
      if (!ld.raLocate(qds,"CSKL",qds_tmp.getString("CSKL"))) {
        qds.insertRow(true);
        System.out.println("qds_tmp.getString(CSKL)="+qds_tmp.getString("CSKL"));
        qds.setString("CSKL",qds_tmp.getString("CSKL"));
      }
      qds.setBigDecimal("IZNOSRACUNA",qds.getBigDecimal("IZNOSRACUNA").add(
          qds_tmp.getBigDecimal("IPRODSP")));
      qds.setBigDecimal("VIRMAN",qds.getBigDecimal("VIRMAN").add(
          qds_tmp.getBigDecimal("IPRODSP")));
      qds.setTimestamp("DATOD",sdstmp.getTimestamp("DATUMOD"));
      qds.setTimestamp("DATDO",sdstmp.getTimestamp("DATUMDO"));
    }
//     ST.prn(qds);
    if (qds.rowCount() ==0) nemaPodataka = true;
  }

  public void afterOKPress(){
    if (nemaPodataka){
      JOptionPane.showMessageDialog(null,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.WARNING_MESSAGE);
    }
  }

  private void setupQDS() {
    Column cskl = dm.getStdoki().getColumn("CSKL").cloneColumn();
    cskl.setColumnName("CSKL");
    Column iznosracuna = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    iznosracuna.setColumnName("IZNOSRACUNA");
    Column virman = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    virman.setColumnName("VIRMAN");
    Column cek = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    cek.setColumnName("CEK");
    Column kredkart = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    kredkart.setColumnName("KREDKART");
    Column cdatumod = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumod.setColumnName("DATOD");
    Column cdatumdo = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumdo.setColumnName("DATDO");
    qds.setColumns(new Column[] {cskl,iznosracuna,virman,cek,kredkart,cdatumod,cdatumdo});
    qds.open();
  }

  public boolean runFirstESC(){
    return true;
  }

  public raUpitRekapProd() {

    jbInit();
    this.addReport("hr.restart.robno.repPregledRacunaTbl",
                   "hr.restart.robno.repPregledRacunaTbl",
                   "PregledRacunaTbl",
                   "Rekapitulacija prodaje");


    me = this;
  }

  private StorageDataSet sdstmp;
  private void initSDS(){
    sdstmp = new StorageDataSet();
    Column cdatumod = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumod.setColumnName("DATUMOD");
    Column cdatumdo = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumdo.setColumnName("DATUMDO");
    sdstmp.setColumns(new Column[] {cdatumod,cdatumdo});
    sdstmp.open();
  }

  private void jbInit() {

    datumod.setDataSet(sdstmp);
    datumod.setColumnName("DATUMOD");
    datumdo.setDataSet(sdstmp);
    datumdo.setColumnName("DATUMDO");
    xylayout.setWidth(370);
    xylayout.setHeight(75);
    jpmain.setLayout(xylayout);
    jpmain.setBorder(BorderFactory.createEtchedBorder());
    jpmain.add(jMCs,new XYConstraints(0,15,-1,-1));
    jpmain.add(datum, new XYConstraints(15,40,100,-1));
    jpmain.add(datumod, new XYConstraints(150,40,100,-1));
    jpmain.add(datumdo, new XYConstraints(255,40,100,-1));
//
//    jpmain.add(datum, new XYConstraints(15,15,100,-1));
//    jpmain.add(datumod, new XYConstraints(150,15,100,-1));
//    jpmain.add(datumdo, new XYConstraints(255,15,100,-1));
    setJPan(jpmain);
    new raDateRange(datumod, datumdo);
  }

  private void setDatum(){
    sdstmp.setTimestamp("DATUMOD",hr.restart.util.Util.getUtil().getFirstDayOfMonth(val.getToday()));
    sdstmp.setTimestamp("DATUMDO",hr.restart.util.Util.getUtil().getLastDayOfMonth(val.getToday()));
  }
}