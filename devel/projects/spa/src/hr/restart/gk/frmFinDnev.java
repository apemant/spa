/****license*****************************************************************
**   file: frmFinDnev.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class frmFinDnev extends raUpitLite { //raIspisDialog {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  Valid vl = Valid.getValid();

  TableDataSet tds = new TableDataSet();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jrfPocDatum = new JraTextField();
  JraTextField jrfZavDatum = new JraTextField();
  
  protected raComboBox rcmbPrikazDonosa = new raComboBox();
  protected JLabel jlPrikazDonosa = new JLabel("Prikaz donosa");
  
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  dM dm;

  public frmFinDnev() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void okPress() {    
    qds.close();
    prepareIspis();
  }
  
  

  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return true;
  }
  
  public boolean Validacija() {
    if (!ut.getYear(tds.getTimestamp("pocDatum")).equals(ut.getYear(tds.getTimestamp("zavDatum")))){
      jrfPocDatum.requestFocus();
      jrfPocDatum.selectAll();
      JOptionPane.showConfirmDialog(this.getJPan(),"Datumski period nije ispravan","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
        
    if (!Aus.checkGKDateRange(jrfPocDatum, jrfZavDatum)) return false;
    return super.Validacija();
  }
  
  //Init
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setJPan(jp);
    jPanel3.setMaximumSize(new Dimension(385, 60));
    jPanel3.setPreferredSize(new Dimension(385, 60));
    jPanel3.setLayout(xYLayout1);
    jLabel1.setText("Datum (od-do)");
    
    JPanel pd = new JPanel(null);
    pd.setLayout(new BoxLayout(pd, BoxLayout.X_AXIS));
    JraButton checkUI = new JraButton();
    checkUI.setText("Prikaži");
    checkUI.setIcon(raImages.getImageIcon(raImages.IMGALLBACK));
    checkUI.setPreferredSize(new Dimension(100,25));
    pd.add(checkUI);
    okp.add(pd, BorderLayout.CENTER);
    okp.revalidate();
    okp.repaint();
    checkUI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (busy || !Validacija()) return;
        try {
          busy = true;
          showDataSet();
        } finally {
          busy = false;
        }
      }
    });
    
    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"), 
        dM.createTimestampColumn("zavDatum"),
        dM.createStringColumn("donos",1)});

    jrfZavDatum.setDataSet(tds);
    jrfZavDatum.setColumnName("zavDatum");

    jrfPocDatum.setDataSet(tds);
    jrfPocDatum.setColumnName("pocDatum");

    rcmbPrikazDonosa.setRaDataSet(tds);
    rcmbPrikazDonosa.setRaColumn("donos");
    rcmbPrikazDonosa.setRaItems(new String[][] {
      {"Prikazati donos","D"},
      {"Donos se ne prikazuje","N"}
    });



    jp.setLayout(borderLayout1);

    xYLayout1.setWidth(385);
    xYLayout1.setHeight(85);
    jp.setMaximumSize(new Dimension(385, 85));
    jp.setPreferredSize(new Dimension(385, 85));
    
    jPanel3.add(jLabel1,  new XYConstraints(20, 20, 0, 0));
    
    jPanel3.add(jrfPocDatum,   new XYConstraints(150, 20, 100, -1));
    jPanel3.add(jrfZavDatum,  new XYConstraints(255, 20, 100, -1));
    
    jPanel3.add(jlPrikazDonosa,  new XYConstraints(20, 45, 0, 0));
    jPanel3.add(rcmbPrikazDonosa,  new XYConstraints(150, 45, 205, -1));
    
    jp.add(jPanel3, BorderLayout.CENTER);
  }

  public void componentShow() {
    showDefaultValues();
  }
  void showDefaultValues() {
    tds.setTimestamp("zavDatum", hr.restart.util.Util.getUtil().getLastDayOfMonth());
    tds.setTimestamp("pocDatum", hr.restart.util.Util.getUtil().getFirstDayOfYear());
//    tds.setTimestamp("zavDatum", vl.getToday());
    tds.setString("donos","D");
    rcmbPrikazDonosa.setSelectedIndex(0);
    this.jrfPocDatum.requestFocus();
    this.jrfPocDatum.selectAll();
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    this.jrfPocDatum.requestFocus();
    this.jrfPocDatum.selectAll();
  }

  public boolean runFirstESC() {
    return false;
  }


  //******** report dataset getter
  private static QueryDataSet qds = new QueryDataSet();
  public static double [] sume;
  public static Timestamp datumOd=null;
  public static Timestamp datumDo=null;

  public static QueryDataSet getQds() {
    return qds;
  }
  
  void showDataSet() {
    qds = new QueryDataSet();
    raProcess.runChild(new Runnable() {
      public void run() {
        String q = "select nalozi.CNALOGA as cnaloga, gkstavke.brojizv as brojizv, nalozi.datumknj as datumknj, " +
                "gkstavke.rbs as rbs, gkstavke.brojkonta as brojkonta, gkstavke.corg as corg, " +
             "gkstavke.opis as opis, gkstavke.ID as id, gkstavke.IP as ip, gkstavke.cpar as cpar " +
             "from gkstavke, nalozi where gkstavke.rbr=nalozi.rbr and gkstavke.knjig = nalozi.knjig " +
             "AND gkstavke.god = nalozi.god AND gkstavke.cvrnal = nalozi.cvrnal and " +
             Aus.getKnjigCond().and(Condition.between("datumknj", 
                 tds.getTimestamp("pocDatum"), 
                 tds.getTimestamp("zavDatum"))).qualified("nalozi") +
          " order by nalozi.datumknj, gkstavke.rbs";
        Aus.setFilter(qds, q);
        qds.setColumns(new Column[] {
            (Column) dm.getNalozi().getColumn("CNALOGA").clone(),
            (Column) dm.getGkstavke().getColumn("BROJIZV").clone(),
            (Column) dm.getNalozi().getColumn("DATUMKNJ").clone(),
            (Column) dm.getGkstavke().getColumn("RBS").clone(),
            (Column) dm.getGkstavke().getColumn("BROJKONTA").clone(),
            (Column) dm.getGkstavke().getColumn("CORG").clone(),
            (Column) dm.getGkstavke().getColumn("OPIS").clone(),
            (Column) dm.getGkstavke().getColumn("ID").clone(),
            (Column) dm.getGkstavke().getColumn("IP").clone(),
            (Column) dm.getGkstavke().getColumn("CPAR").clone()
        });
        openScratchDataSet(qds);
        qds.getColumn("BROJIZV").setCaption("Izvod");
      }
    });
    if (raProcess.isCompleted()) {
      frmTableDataView view = new frmTableDataView();
      view.setDataSet(qds);
      view.setSums(new String[] {"ID", "IP"});
      view.setTitle("Financijski dnevnik  " + 
          "od " + Aus.formatTimestamp(tds.getTimestamp("pocDatum")) +
          " do " + Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
      view.show();
    }
  }

  private void prepareIspis() {
    BigDecimal sumaDuguje = new BigDecimal(0);
    BigDecimal sumaPotrazuje = new BigDecimal(0);
    QueryDataSet donosQDS = new QueryDataSet();
    String datPoc=util.getTimestampValue(tds.getTimestamp("pocDatum"),0);
    String datZav=util.getTimestampValue(tds.getTimestamp("zavDatum"),1);
    String dat0101 = util.getTimestampValue(util.findFirstDayOfYear(),0);

    String donosQSTR="";
    String qStr="";
    qds = new QueryDataSet();

      qStr = "select gkstavke.rbr as rbr, gkstavke.rbs as rbs, gkstavke.corg as corg, gkstavke.opis as opis, "+
              "gkstavke.ID as id, gkstavke.IP as ip, gkstavke.brojkonta as brojkonta, nalozi.CNALOGA as cnaloga, "+
              "nalozi.datumknj as datumknj from gkstavke, nalozi "+
              "where gkstavke.rbr=nalozi.rbr and gkstavke.knjig = nalozi.knjig AND gkstavke.god = nalozi.god "+
              "AND gkstavke.cvrnal = nalozi.cvrnal and gkstavke.knjig = '" + hr.restart.zapod.OrgStr.getKNJCORG() +"'"+
              " and nalozi.datumknj between "+datPoc+" and "+datZav;

    Aus.refilter(qds, qStr);    
    
    if (qds.getRowCount() < 1) setNoDataAndReturnImmediately();
    
    datumOd = tds.getTimestamp("pocDatum");
    datumDo = tds.getTimestamp("zavDatum");
    
    killAllReports();

    qds.first();
    do {
      sumaDuguje =sumaDuguje.add(qds.getBigDecimal("ID"));
      sumaPotrazuje =sumaPotrazuje.add(qds.getBigDecimal("IP"));
      qds.next();
    }
    while(qds.inBounds());

    //DONOS RAÈUNICA!!!

    sume = new double[] {0.0,0.0,sumaDuguje.doubleValue(),sumaPotrazuje.doubleValue()};
    if (dat0101.compareTo(datPoc) != 0 ){
      if (tds.getString("donos").equals("D")){
        Timestamp dppd = tds.getTimestamp("pocDatum");
        
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(tds.getTimestamp("pocDatum"));
        
        int dan = cal.get(Calendar.DAY_OF_MONTH);
        int mje = cal.get(Calendar.MONTH);

        if (dan == 0) {
          cal.set(Calendar.MONTH, mje-1);
          dppd = new java.sql.Timestamp(cal.getTime().getTime());
          dppd = ut.getLastDayOfMonth(dppd);
        } else {
          cal.set(Calendar.DAY_OF_MONTH,dan-1);
          dppd = new java.sql.Timestamp(cal.getTime().getTime());
        }
        
        String dppdS=util.getTimestampValue(dppd,1);
        
        
        donosQSTR = "select sum(gkstavke.ID) as id, sum(gkstavke.IP) as ip from gkstavke, nalozi "+
              "where gkstavke.rbr=nalozi.rbr and gkstavke.knjig = nalozi.knjig AND gkstavke.god = nalozi.god "+
              "AND gkstavke.cvrnal = nalozi.cvrnal and gkstavke.knjig = '" + hr.restart.zapod.OrgStr.getKNJCORG() +"' "+
        "and nalozi.datumknj between "+dat0101+" and "+dppdS+"";

        Aus.refilter(donosQDS, donosQSTR);    
        donosQDS.first();
        
        do {
          sume[0] += donosQDS.getBigDecimal("ID").doubleValue();
          sume[1] += donosQDS.getBigDecimal("IP").doubleValue();
          sume[2] += donosQDS.getBigDecimal("ID").doubleValue();
          sume[3] += donosQDS.getBigDecimal("IP").doubleValue();

        } while (donosQDS.next());
      } 
    }
    
    if (tds.getString("donos").equals("D")){
      this.addReport("hr.restart.gk.repFinDnev", "hr.restart.gk.repFinDnev", "FinDnev", "Financijski dnevnik");
    } else {
      this.addReport("hr.restart.gk.repFinDnev", "hr.restart.gk.repFinDnev", "FinDnevNoRekap", "Financijski dnevnik");
    }
  }

}
