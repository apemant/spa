/****license*****************************************************************
**   file: raUpitMjeIzvjMal.java
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
import hr.restart.util.Aus;

import java.awt.Frame;
import java.math.BigDecimal;

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


public class raUpitMjeIzvjMal extends hr.restart.util.raUpitLite {

  private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private XYLayout xylayout = new XYLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private JPanel jpmain = new JPanel();
  private JLabel datum = new JLabel("Datum (od - do)");
  private JraTextField datumod = new JraTextField();
  private JraTextField datumdo = new JraTextField();
  private jpMoreCsklad jMCs = new jpMoreCsklad();

  private QueryDataSet qds = new QueryDataSet();
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();

  {
    initSDS();
    setupQDS();
  }

  public  QueryDataSet getQds(){
    return qds;
  }

  private static raUpitMjeIzvjMal me;


  public static raUpitMjeIzvjMal getInstance(){
    return me;
  }

  public void componentShow(){

    setDatum();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jMCs.skladista.setText("");
//        datumod.selectAll();
      }
    });
  }

  public void firstESC(){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jMCs.skladista.requestFocus();
//        datumod.requestFocus();
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
    /*
    za skladište
    */

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



//    String sqlupit = "SELECT MAX(doki.cskl) as cskl,SUM(inab) as inab,SUM(iprodbp) as iprodbp,"+
//                     "SUM(iprodsp) as iprodsp ,SUM(irata) as iiRATA,MAX(CNACPL) as ccnacpl, "+
//                     "0.00 as PROVKART ,max(cbanka) as cbanka,MAX(fl_kartica) as fk "+
//                     "FROM doki,stdoki,rate,nacpl WHERE doki.cskl = stdoki.cskl "+
//                     "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "+
//                     "AND doki.brdok = stdoki.brdok AND stdoki.cskl = rate.cskl "+
//                     "AND stdoki.vrdok = rate.vrdok AND stdoki.god = rate.god "+
//                     "AND stdoki.brdok = rate.brdok AND stdoki.rbr = rate.rbr "+
//                     "and vrdok in ('GOT','GRN','POS','ROT') and doki.datdok between '"+
//                     datumod+"' and '"+datumdo+
//                     "' and nacpl.cnacpl=rate.cnacpl "+cskl+
//                     " group by cskl,cnacpl,cbanka";

    System.out.println("\n"+sqlupit+"\n");

    QueryDataSet qds_tmp = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);
    qds.deleteAllRows();

    for (qds_tmp.first();qds_tmp.inBounds();qds_tmp.next()) {

      if (!ld.raLocate(qds,"CSKL",qds_tmp.getString("CSKL"))) {
        qds.insertRow(true);
        qds.setString("CSKL",qds_tmp.getString("CSKL"));
      }
      qds.setBigDecimal("STZALNC",qds.getBigDecimal("STZALNC").add(
          qds_tmp.getBigDecimal("INAB")));
      qds.setBigDecimal("UPVNOPDV",qds.getBigDecimal("UPVNOPDV").add(
          qds_tmp.getBigDecimal("IPRODBP")));
      qds.setBigDecimal("UNABVRI",qds.getBigDecimal("UNABVRI").add(
          qds_tmp.getBigDecimal("INAB")));
      qds.setTimestamp("DATUMOD",sdstmp.getTimestamp("DATUMOD"));
      qds.setTimestamp("DATUMDO",sdstmp.getTimestamp("DATUMDO"));
//      if (qds_tmp.getString("FK").equalsIgnoreCase("D")) {
//        if(ld.raLocate(dm.getKartice(),"CBANKA",qds_tmp.getString("CBANKA"))) {
//          qds.setBigDecimal("PROVKART",qds.getBigDecimal("PROVKART").add(
//              qds_tmp.getBigDecimal("iiRATA").multiply(
//              dm.getKartice().getBigDecimal("PROVIZIJA")).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP)));
//        }
//      }
    }

    sqlupit = "SELECT MAX(doki.cskl) as cskl,SUM(inab) as inab,SUM(iprodbp) as iprodbp,"+
                     "SUM(iprodsp) as iprodsp ,SUM(irata) as iiRATA,MAX(CNACPL) as ccnacpl, "+
                     "0.00 as PROVKART ,max(cbanka) as cbanka,MAX(fl_kartica) as fk "+
                     "FROM doki,stdoki,rate,nacpl WHERE doki.cskl = stdoki.cskl "+
                     "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "+
                     "AND doki.brdok = stdoki.brdok AND stdoki.cskl = rate.cskl "+
                     "AND stdoki.vrdok = rate.vrdok AND stdoki.god = rate.god "+
                     "AND stdoki.brdok = rate.brdok AND stdoki.rbr = rate.rbr "+
                     "and vrdok in ('GOT','GRN','POS','ROT') and doki.datdok between '"+
                     datumod+"' and '"+datumdo+
                     "' and nacpl.cnacpl=rate.cnacpl "+cskl+
                     " group by cskl,cnacpl,cbanka";

    qds_tmp = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);

    for (qds_tmp.first();qds_tmp.inBounds();qds_tmp.next()) {
      if (!ld.raLocate(qds,"CSKL",qds_tmp.getString("CSKL"))) {
        qds.insertRow(true);
        qds.setString("CSKL",qds_tmp.getString("CSKL"));
      }
      if (qds_tmp.getString("FK").equalsIgnoreCase("D")) {
        if(ld.raLocate(dm.getKartice(),"CBANKA",qds_tmp.getString("CBANKA"))) {
          qds.setBigDecimal("PROVKART",qds.getBigDecimal("PROVKART").add(
              qds_tmp.getBigDecimal("iiRATA").multiply(
              dm.getKartice().getBigDecimal("PROVIZIJA")).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP)));
        }
      }
    }

    for (qds.first();qds.inBounds();qds.next()) {
      qds.setBigDecimal("RUC",qds.getBigDecimal("UPVNOPDV").subtract(
      qds.getBigDecimal("UNABVRI")).subtract(
      qds.getBigDecimal("PROVKART")));
      qds.setBigDecimal("PRUC",qds.getBigDecimal("RUC").divide(qds.getBigDecimal("UNABVRI"),2,
          BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100.00")));
    }

    QueryDataSet ss = stanjenab();
    for (ss.first();ss.inBounds();ss.next()) {
      if (ld.raLocate(qds,"CSKL",ss.getString("CSKL"))) {
        qds.setBigDecimal("STZALNC",ss.getBigDecimal("INAB"));
      }
    }

    if (qds.rowCount() == 0){
      nemaPodataka = true;
    }
  }

  public void afterOKPress(){
    if (nemaPodataka){
      JOptionPane.showMessageDialog(null,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.WARNING_MESSAGE);
    }
  }

  private void setupQDS() {
    Column cskl = dm.getStdoki().getColumn("CSKL").cloneColumn();
    cskl.setColumnName("CSKL");
    Column stzalnc = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    stzalnc.setColumnName("STZALNC");
    Column upvnopdv = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    upvnopdv.setColumnName("UPVNOPDV");
    Column unabvri = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    unabvri.setColumnName("UNABVRI");
    Column provkart = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    provkart.setColumnName("PROVKART");
    Column ruc = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    ruc.setColumnName("RUC");
    Column pruc = dm.getStdoki().getColumn("UPRAB").cloneColumn();
    pruc.setColumnName("PRUC");
    Column ukutr = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    ukutr.setColumnName("UKUTR");
    Column cdatumod = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumod.setColumnName("DATUMOD");
    Column cdatumdo = dm.getDoki().getColumn("DATDOK").cloneColumn();
    cdatumdo.setColumnName("DATUMDO");
    qds.setColumns(new Column[] {cskl,stzalnc,upvnopdv,unabvri,provkart,ruc,pruc,ukutr,cdatumod,
        cdatumdo});
    qds.open();
  }

  public boolean runFirstESC(){
    return true;
  }

  public raUpitMjeIzvjMal() {
    jbInit();
    this.addReport("hr.restart.robno.repMjeIzvjMaloprodaja",
                   "hr.restart.robno.repMjeIzvjMaloprodaja",
                   "MjeIzvjMaloprodaja",
                   "Izvještaj maloprodaje");
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

  public java.sql.Timestamp getDATUMOD() {
    return sdstmp.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDATUMDO() {
    return sdstmp.getTimestamp("DATUMDO");
  }

  private void jbInit() {
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
//    jpmain.add(new JLabel("Skladišta "), new XYConstraints(15,15,100,-1));
//    jpmain.add(skladista, new XYConstraints(150,15,100,-1));
//    jpmain.add(jodbir, new XYConstraints(255,15,21,21));
    jpmain.add(datum, new XYConstraints(15,40,100,-1));
    jpmain.add(datumod, new XYConstraints(150,40,100,-1));
    jpmain.add(datumdo, new XYConstraints(255,40,100,-1));
    setJPan(jpmain);
    new raDateRange(datumod, datumdo);
  }

  private void setDatum(){
    sdstmp.setTimestamp("DATUMOD",hr.restart.util.Util.getUtil().getFirstDayOfMonth(val.getToday()));
    sdstmp.setTimestamp("DATUMDO",hr.restart.util.Util.getUtil().getLastDayOfMonth(val.getToday()));
  }

  private QueryDataSet stanjenab() {
    QueryDataSet stanjeqds = new QueryDataSet();
    Column cskl = dm.getStdoki().getColumn("CSKL").cloneColumn();
    cskl.setColumnName("CSKL");
    Column stzalnc = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    stzalnc.setColumnName("INAB");
    stanjeqds.setColumns(new Column[] {cskl,stzalnc});
    stanjeqds.open();
    stanjeqds = retStanje("select MAX(CSKLIZ) as cskl, sum(INABIZ)as INAB from meskla,stmeskla "+
                          "where meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul "+
                          "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god "+
                          "AND meskla.brdok = stmeskla.brdok AND datdok<'"+
                          sdstmp.getTimestamp("DATUMOD").toString()+"' group by cskliz",stanjeqds,false);

    stanjeqds = retStanje("select MAX(CSKLUL) as cskl, sum(INABUL)as INAB from meskla,stmeskla "+
                          "where meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul "+
                          "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god "+
                          "AND meskla.brdok = stmeskla.brdok AND datdok<'"+
                          sdstmp.getTimestamp("DATUMOD").toString()+"' group by csklul",stanjeqds,true);

    stanjeqds = retStanje("select MAX(CSKL) as cskl, sum(INAB)as INAB from doku,stdoku "+
                          "WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok "+
                          "AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok AND "+
                          "datdok<'"+sdstmp.getTimestamp("DATUMOD").toString()+"' group by cskl",
                          stanjeqds,true);


    stanjeqds = retStanje("select MAX(CSKL) as cskl, sum(INAB)as INAB from doki,stdoki "+
                          "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "+
                          "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND "+
                          "doki.vrdok in ('ROT','GOT') AND "+
                          "datdok<'"+sdstmp.getTimestamp("DATUMOD").toString()+"' group by cskl",stanjeqds,false);

    return stanjeqds;

  }
  private QueryDataSet retStanje(String sql,QueryDataSet stanje,boolean dodaj) {
    QueryDataSet stanje_tmp = hr.restart.util.Util.getNewQueryDataSet(sql,true);
    for (stanje_tmp.first();stanje_tmp.inBounds();stanje_tmp.next()){
      if (!ld.raLocate(stanje,"CSKL",stanje_tmp.getString("CSKL"))) {
        stanje.insertRow(true);
        stanje.setString("CSKL",stanje_tmp.getString("CSKL"));
        stanje.setBigDecimal("INAB",Aus.zero2);
      }
      if (dodaj) {
        stanje.setBigDecimal("INAB",stanje.getBigDecimal("INAB").add(
            stanje_tmp.getBigDecimal("INAB")));
      } else {
        stanje.setBigDecimal("INAB",stanje.getBigDecimal("INAB").subtract(
            stanje_tmp.getBigDecimal("INAB")));
      }
    }
    return stanje;
  }
}