/****license*****************************************************************
**   file: ispStatPar.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.sk.PartnerCache;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.Stopwatch;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.JasperHook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Locale;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ispStatPar extends raPanStats {

  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  QueryDataSet reportSet, reportSet2, reportSetGroups, /*jptvSet,*/ monthSet, monthSetGraph;
  double postoSumRuc = 0;

  //  private String[] moonshine = new String[]
  // {"Sijeèanj","Veljaèa","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"};

  static ispStatPar instanceOfMe;

  public ispStatPar() {
    try {
      jbInit();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void showDefaultValues() {
    super.showDefaultValues();
    fieldSet.setString("PRIKAZ", "IR");
    fieldSet.setString("SLJED", "CPAR");
    setSljed();
    getJPTV().clearDataSet();
  }

  public static ispStatPar getInstance() {
    if (instanceOfMe == null)
      instanceOfMe = new ispStatPar();
    return instanceOfMe;
  }
  
  

  public String navDoubleClickActionName() {
    return "Dokument";
  }
  
  private int[] vcls = new int[] {0,1,2,3,4,5,6};
  
  public int[] navVisibleColumns() {
    return vcls;
  }
  
  //long tim;
  
  private void calcPRUC(DataSet ds) {
    try {
      ds.setBigDecimal("PRUC", ds.getBigDecimal("RUC").
          divide(ds.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).movePointRight(2));
    } catch (Exception ex) {
      ds.setBigDecimal("PRUC", Aus.zero2);
    }
  }
  
  boolean ispRab;
  
  public boolean isIspRab() {
    return ispRab;
  }
  
  public void addHooks() {
    if (ispRab = frmParam.getParam("robno", "ispRab", "N",
        "U ispStatPar staviti kolonu iznos bez popusta (D,N)").
        equalsIgnoreCase("D")) {
      JasperHook jh = new JasperHook() {
        public void adjustDesign(String reportName, JasperDesign design) {
          adjustJasper(design);
        }
      };
      getRepRunner().addJasperHook("hr.restart.robno.repStatPar", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParOne", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParDet", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParDetPJ", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParGroups", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParGroups2", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParGroups3", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatParDetGroups", jh);
      getRepRunner().addJasperHook("hr.restart.robno.repStatsMonths", jh);
    }
  }
  
  void adjustJasper(JasperDesign jas) {
    adjustBand(jas.getColumnHeader());
    adjustBand(jas.getDetail());
    adjustBand(jas.getColumnFooter());
    JRGroup[] grs = jas.getGroups();
    for (int i = 0; i < grs.length; i++) {
      adjustBand(grs[i].getGroupHeader());
      adjustBand(grs[i].getGroupFooter());
    }
  }
  
  private void adjustBand(JRBand band) {
    if (band == null) return;
    JRElement[] els = band.getElements();
    for (int i = 0; i < els.length; i++)
      if (els[i] instanceof JRDesignStaticText)
        adjustStatic((JRDesignStaticText) els[i]);
  }
  
  private void adjustStatic(JRDesignStaticText tf) {
    if (tf.getText().equals("Porez"))
      tf.setText("S porezom");
    else if (tf.getText().equals("S porezom"))
      tf.setText("Bez popusta");
  }
  
  public void okPress() {
    Stopwatch sw = Stopwatch.start("okPress");
    //tim = System.currentTimeMillis();
//    System.out.println("1 - OK PRESSED " + (System.currentTimeMillis()-tim)); //XDEBUG delete when no more needed
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(fieldSet);
    PartnerCache pca = new PartnerCache(true);
    sw.report("partner caching");
    isIspis = true;
    QueryDataSet temporarySet = getRawSet(util.getTimestampValue(fieldSet.getTimestamp("pocDatum"), 0), util.getTimestampValue(fieldSet.getTimestamp("zavDatum"), 1));

    sw.report("dohvat");

    if (temporarySet == null || temporarySet.rowCount() == 0)
      setNoDataAndReturnImmediately();

    System.out.println("ROWS: "+temporarySet.rowCount());
//    syst.prn(temporarySet);

    if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR") || fieldSet.getString("PRIKAZ").equalsIgnoreCase("PJ")) {
//      System.out.println("2 - PRIKAZ = IR " + (System.currentTimeMillis()-tim)); //XDEBUG delete when no more needed
      this.killAllReports();
      if (getCkup().equals("")) {
        this.addReport("hr.restart.robno.repStatPar", "hr.restart.robno.repStatPar", "StatPar", "Top lista kupaca");
      } else {
        this.addReport("hr.restart.robno.repStatParOne", "hr.restart.robno.repStatPar", "StatParOne", "Top lista kupaca");
      }

      //if (fieldSet.getString("SLJED").equals("CPAR")) {
      if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR"))
        temporarySet.setSort(new SortDescriptor(new String[] {"CPAR", "CART"}));
      else
      	temporarySet.setSort(new SortDescriptor(new String[] {"CPAR", "PJ", "CART"}));
        reportSet2 = detaljnoPoArtiklima(temporarySet, pca);
        reportSet2.setLocale(Locale.getDefault());
        reportSet2.setSort(new SortDescriptor(new String[] {"NAZPAR", "CART"}));
        if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR"))
        	this.addReport("hr.restart.robno.repStatParDet", "hr.restart.robno.repStatParDet", "StatParDet", "Top lista kupaca - detaljno s artiklima");
        else
        	this.addReport("hr.restart.robno.repStatParDetPJ", "hr.restart.robno.repStatParDet", "StatParDetPJ", "Top lista kupaca po jedinicama - detaljno s artiklima");

        //        this.addReport("hr.restart.robno.RepStatParChart",
        // "hr.restart.robno.RepStatParChart", "Top lista kupaca - grafikon");
      //}

      reportSet = new QueryDataSet();
      reportSet.setLocale(Locale.getDefault());
      reportSet.setColumns(new Column[]{
          dm.createStringColumn("CSKL", "Skladište", 10), 
          dm.createIntColumn("CPAR", "Oznaka kupca"), 
          dm.createIntColumn("BRD", "Broj raèuna"), 
          dm.createStringColumn("NAZPAR", "Naziv kupca", 150), 
          dm.createStringColumn("BRDOK", "Vrsta i broj dokumenta", 20), 
          dm.createStringColumn("VRDOK", "Vrsta dokumenta", 3), 
          dm.createStringColumn("GOD", "Godina", 4), 
          dm.createTimestampColumn("DATDOK", "Datum"),
          dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
          dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
          dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
          dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
          dm.createBigDecimalColumn("POR", "Porez", 2), 
          dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2),
          dm.createBigDecimalColumn("ITOT", "Prodajni iznos bez popusta", 2)
      });

      reportSet.setRowId("CSKL", true);
      reportSet.setRowId("CPAR", true);
      reportSet.setRowId("BRD", true);
      reportSet.setRowId("VRDOK", true);
      reportSet.setRowId("GOD", true);
      reportSet.open();
      
      temporarySet.setSort(new SortDescriptor(new String[] {"VRDOK", "CSKL", "GOD", "BRDOK"}));
      temporarySet.first();
      sw.report("sorted");
      String brdok = "";
      String oldBrdok = "";
//      System.out.println("2 - PRIKAZ = IR prije petlje " + (System.currentTimeMillis()-tim)); //XDEBUG delete when no more needed
      do {
        checkClosing();
        brdok = temporarySet.getString("VRDOK") + "-" + temporarySet.getString("CSKL") + "/" + temporarySet.getString("GOD") + "-" + vl.maskString(temporarySet.getInt("BRDOK") + "", '0', 6);
        //if (!ld.raLocate(reportSet, new String[]{"CPAR", "BRDOK"}, new String[]{temporarySet.getInt("CPAR") + "", brdok})) {
        if (!oldBrdok.equals(brdok)) {
          if (oldBrdok.length() > 0) calcPRUC(reportSet);
          reportSet.insertRow(false);
          reportSet.setString("CSKL", temporarySet.getString("CSKL"));
          reportSet.setString("GOD", temporarySet.getString("GOD"));
          reportSet.setInt("BRD", temporarySet.getInt("BRDOK"));
          reportSet.setString("VRDOK", temporarySet.getString("VRDOK"));
          reportSet.setInt("CPAR", temporarySet.getInt("CPAR"));
          reportSet.setTimestamp("DATDOK", temporarySet.getTimestamp("DATDOK"));
          reportSet.setString("NAZPAR", pca.getNameNotNull(temporarySet.getInt("CPAR")));
          reportSet.setString("BRDOK", oldBrdok = brdok);
          reportSet.setBigDecimal("INAB", temporarySet.getBigDecimal("INAB"));
          reportSet.setBigDecimal("RUC", temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("IPRODBP", temporarySet.getBigDecimal("IPRODBP"));
          reportSet.setBigDecimal("POR", temporarySet.getBigDecimal("POR"));
          reportSet.setBigDecimal("IPRODSP", temporarySet.getBigDecimal("IPRODSP"));
          reportSet.setBigDecimal("ITOT", temporarySet.getBigDecimal("ITOT"));
        } else {
          reportSet.setBigDecimal("INAB", reportSet.getBigDecimal("INAB").add(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("RUC", reportSet.getBigDecimal("RUC").add(temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB"))));
          reportSet.setBigDecimal("IPRODBP", reportSet.getBigDecimal("IPRODBP").add(temporarySet.getBigDecimal("IPRODBP")));
          reportSet.setBigDecimal("POR", reportSet.getBigDecimal("POR").add(temporarySet.getBigDecimal("POR")));
          reportSet.setBigDecimal("IPRODSP", reportSet.getBigDecimal("IPRODSP").add(temporarySet.getBigDecimal("IPRODSP")));
          reportSet.setBigDecimal("ITOT", reportSet.getBigDecimal("ITOT").add(temporarySet.getBigDecimal("ITOT")));
        }
        
      } while (temporarySet.next());
      if (oldBrdok.length() > 0) calcPRUC(reportSet);
//      System.out.println("3 - PRIKAZ = IR poslje petlje " + (System.currentTimeMillis()-tim)); //XDEBUG delete when no more needed

      // sortiranje dataseta ----
      if (fieldSet.getString("SLJED").equals("CPAR"))
        reportSet.setSort(new SortDescriptor(new String[]{"NAZPAR"}));
      else
        reportSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

      /*jptvSet = new QueryDataSet();
      jptvSet.setColumns(reportSet.cloneColumns());
      jptvSet.getColumn("CSKL").setVisible(0);
      jptvSet.getColumn("VRDOK").setVisible(0);
      jptvSet.getColumn("BRD").setVisible(0);
      jptvSet.getColumn("GOD").setVisible(0);
      jptvSet.open();

      reportSet.first();
      do {
        jptvSet.insertRow(false);
        reportSet.copyTo(jptvSet);
      } while (reportSet.next());

      jptvSet.last();*/
      reportSet.getColumn("CSKL").setVisible(0);
//      reportSet.getColumn("CPAR").setVisible(0);
      reportSet.getColumn("BRD").setVisible(0);
      reportSet.getColumn("VRDOK").setVisible(0);
      reportSet.getColumn("GOD").setVisible(0);
      
//      syst.prn(reportSet);
      reportSet.last();
      reportSet.setTableName("IR");
      setDataSetAndSums(/*jptvSet*/reportSet, new String[]{"INAB", "RUC", "IPRODBP", "POR", "IPRODSP"});

    } else if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("UI") ||
        fieldSet.getString("PRIKAZ").equalsIgnoreCase("UP")) { // prikaz
                                                                      // po
                                                                      // kupcima
      this.killAllReports();
      if (getCkup().equals("")) {
        this.addReport("hr.restart.robno.repStatPar", "hr.restart.robno.repStatPar", "StatPar", "Top lista kupaca");

        this.addReport("hr.restart.robno.RepStatParChart", "hr.restart.robno.RepStatParChart", "Top lista kupaca - grafikon");
      } else
        this.addReport("hr.restart.robno.repStatParOne", "hr.restart.robno.repStatPar", "StatParOne", "Top lista kupaca"); //System.out.println("dodajem
                                                                                                                           // -
                                                                                                                           // hr.restart.robno.repStatPar");

      reportSet = new QueryDataSet();
      reportSet.setLocale(Locale.getDefault());
      reportSet.setColumns(new Column[] {
          dm.createIntColumn("CPAR", "Oznaka kupca"), 
          dm.createStringColumn("NAZPAR", "Naziv kupca", 150), 
          dm.createIntColumn("PJ", "Oznaka PJ"), 
          dm.createStringColumn("NAZPJ", "Naziv jedinice", 150),
          dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
          dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
          dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
          dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
          dm.createBigDecimalColumn("POR", "Porez", 2), 
          dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2),
          dm.createBigDecimalColumn("ITOT", "Prodajni iznos bez popusta", 2)
       });

      reportSet.setRowId("CPAR", true);

      reportSet.open();
      boolean ispj = fieldSet.getString("PRIKAZ").equalsIgnoreCase("UP");
      if (ispj)
        temporarySet.setSort(new SortDescriptor(new String[] {"CPAR","PJ"}));
      else temporarySet.setSort(new SortDescriptor(new String[] {"CPAR"}));
      temporarySet.first();
      sw.report("sorted");
      
//      String brdok;
      int cpar = -1027, pj = -1027;
      do {
        checkClosing();
        //if (!ld.raLocate(reportSet, new String[]{"CPAR"}, new String[] {temporarySet.getInt("CPAR") + ""})) {
        if (temporarySet.getInt("CPAR") != cpar || 
            (ispj && temporarySet.getInt("PJ") != pj)) {
          if (cpar != -1027) calcPRUC(reportSet);
          reportSet.insertRow(false);
          reportSet.setInt("CPAR", cpar = temporarySet.getInt("CPAR"));
          reportSet.setString("NAZPAR", pca.getNameNotNull(cpar));
          if (ispj) {
            DataRow djp = ld.raLookup(dm.getPjpar(), 
                new String[] {"CPAR", "PJ"}, temporarySet);
            reportSet.setInt("PJ", pj = temporarySet.getInt("PJ"));
            if (djp != null)
              reportSet.setString("NAZPJ", djp.getString("NAZPJ"));
          }
          reportSet.setBigDecimal("INAB", temporarySet.getBigDecimal("INAB"));
          reportSet.setBigDecimal("RUC", temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("IPRODBP", temporarySet.getBigDecimal("IPRODBP"));
          reportSet.setBigDecimal("POR", temporarySet.getBigDecimal("POR"));
          reportSet.setBigDecimal("IPRODSP", temporarySet.getBigDecimal("IPRODSP"));
          reportSet.setBigDecimal("ITOT", temporarySet.getBigDecimal("ITOT"));
        } else {
          reportSet.setBigDecimal("INAB", reportSet.getBigDecimal("INAB").add(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("RUC", reportSet.getBigDecimal("RUC").add(temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB"))));
          reportSet.setBigDecimal("IPRODBP", reportSet.getBigDecimal("IPRODBP").add(temporarySet.getBigDecimal("IPRODBP")));
          reportSet.setBigDecimal("POR", reportSet.getBigDecimal("POR").add(temporarySet.getBigDecimal("POR")));
          reportSet.setBigDecimal("IPRODSP", reportSet.getBigDecimal("IPRODSP").add(temporarySet.getBigDecimal("IPRODSP")));
          reportSet.setBigDecimal("ITOT", reportSet.getBigDecimal("ITOT").add(temporarySet.getBigDecimal("ITOT")));
        }
      } while (temporarySet.next());
      if (cpar != -1027) calcPRUC(reportSet);
      // sortiranje dataseta ----
      if (fieldSet.getString("SLJED").equals("CPAR"))
        reportSet.setSort(new SortDescriptor(new String[]{"NAZPAR"}));
      else
        reportSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

      //      jptvSet = reportSet;

      /*jptvSet = new QueryDataSet();
      jptvSet.setColumns(reportSet.cloneColumns());
      jptvSet.open();
      reportSet.first();
      do {
        jptvSet.insertRow(false);
        reportSet.copyTo(jptvSet);
      } while (reportSet.next());
      jptvSet.last();*/
      reportSet.last();
      reportSet.setTableName("UI");
      setDataSetAndSums(/*jptvSet*/reportSet, new String[]{"INAB", "RUC", "IPRODBP", "POR", "IPRODSP"});

    } else if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("GR")) {
      temporarySet.setSort(new SortDescriptor(new String[] {"CGRART", "CPAR", "CART"}));
      reportSet2 = detaljnoPoArtiklima(temporarySet, pca);
      reportSet2.setLocale(Locale.getDefault());
      reportSet2.setSort(new SortDescriptor(new String[] {"CGRART", "NAZPAR", "CART"}));
      this.killAllReports(); //repStatParGroups2
      this.addReport("hr.restart.robno.repStatParGroups", "hr.restart.robno.repStatPar", "StatParGroups", "Grupe artikala");
      this.addReport("hr.restart.robno.repStatParGroups2", "hr.restart.robno.repStatParGroups", "StatParGroups2", "Grupe artikala - po kupcima");
      this.addReport("hr.restart.robno.repStatParGroups3", "hr.restart.robno.repStatParGroups", "StatParGroups3", "Top lista kupaca - po grupama artikala");
      this.addReport("hr.restart.robno.repStatParDetGroups", "hr.restart.robno.repStatParDetGroups", "StatParDetGroups", "Top lista kupaca - detaljno po grupama artikala");
      
      QueryDataSet grupeArt = ut.getNewQueryDataSet("SELECT * FROM Grupart");
      
      reportSet = new QueryDataSet();
      reportSet.setLocale(Locale.getDefault());
      reportSet.setColumns(new Column[]{
          dm.createStringColumn("CSKL", "Skladište", 10), 
          dm.createIntColumn("CPAR", "Oznaka kupca"), 
          dm.createStringColumn("CGRART", "Grupa artikala", 10), 
          dm.createStringColumn("NAZGRART", "Naziv grupe artikala", 50), 
          dm.createStringColumn("NAZPAR", "Naziv kupca", 150), 
          dm.createStringColumn("VRDOK", "Vrsta dokumenta", 3), 
          dm.createStringColumn("GOD", "Godina", 4), 
          dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
          dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
          dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
          dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
          dm.createBigDecimalColumn("POR", "Porez", 2), 
          dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2),
          dm.createBigDecimalColumn("ITOT", "Prodajni iznos bez popusta", 2)
      });
      
      reportSet.setRowId("CSKL", true);
      reportSet.setRowId("CPAR", true);
      reportSet.setRowId("CGRART", true);
      reportSet.setRowId("VRDOK", true);
      reportSet.setRowId("GOD", true);
      
      reportSet.open();
      
      temporarySet.setSort(new SortDescriptor(new String[] {"CGRART", "CPAR"}));
      temporarySet.first();
      sw.report("sorted");
      String cgr = "xnat1027";
      do {
        checkClosing();
        //if (temporarySet.getString("CGRART").equals("")) temporarySet.setString("CGRART","X_X");
        String ncgr = temporarySet.getString("CGRART");
        if (ncgr == null || ncgr.length() == 0) ncgr = "X_X";
        if (!cgr.equals(ncgr)) {
        //if (!ld.raLocate(reportSet, new String[]{"CGRART"/*,"CPAR"*/}, new String[]{temporarySet.getString("CGRART")/*, temporarySet.getInt("CPAR")+""*/})) {
          if (!cgr.equals("xnat1027")) calcPRUC(reportSet); 
          reportSet.insertRow(false);          
          reportSet.setString("CSKL", temporarySet.getString("CSKL"));
          reportSet.setString("GOD", temporarySet.getString("GOD"));
          reportSet.setString("VRDOK", temporarySet.getString("VRDOK"));
          
          reportSet.setInt("CPAR", temporarySet.getInt("CPAR"));
          reportSet.setString("NAZPAR", pca.getNameNotNull(temporarySet.getInt("CPAR")));
          reportSet.setString("CGRART", cgr = ncgr);
          
          if (ld.raLocate(grupeArt,"CGRART",cgr)){
            reportSet.setString("NAZGRART", grupeArt.getString("NAZGRART"));
          } else {
            reportSet.setString("NAZGRART", "Ostalo");
          }
          
          reportSet.setBigDecimal("INAB", temporarySet.getBigDecimal("INAB"));
          reportSet.setBigDecimal("RUC", temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("IPRODBP", temporarySet.getBigDecimal("IPRODBP"));
          reportSet.setBigDecimal("POR", temporarySet.getBigDecimal("POR"));
          reportSet.setBigDecimal("IPRODSP", temporarySet.getBigDecimal("IPRODSP"));
          reportSet.setBigDecimal("ITOT", temporarySet.getBigDecimal("ITOT"));
        } else {
          reportSet.setBigDecimal("INAB", reportSet.getBigDecimal("INAB").add(temporarySet.getBigDecimal("INAB")));
          reportSet.setBigDecimal("RUC", reportSet.getBigDecimal("RUC").add(temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB"))));
          reportSet.setBigDecimal("IPRODBP", reportSet.getBigDecimal("IPRODBP").add(temporarySet.getBigDecimal("IPRODBP")));
          reportSet.setBigDecimal("POR", reportSet.getBigDecimal("POR").add(temporarySet.getBigDecimal("POR")));
          reportSet.setBigDecimal("IPRODSP", reportSet.getBigDecimal("IPRODSP").add(temporarySet.getBigDecimal("IPRODSP")));
          reportSet.setBigDecimal("ITOT", reportSet.getBigDecimal("ITOT").add(temporarySet.getBigDecimal("ITOT")));
        }
/*        try {
          reportSet.setBigDecimal("PRUC", reportSet.getBigDecimal("RUC").divide(reportSet.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        } catch (Exception ex) {
          reportSet.setBigDecimal("PRUC", new java.math.BigDecimal("0.00"));
        }*/
      } while (temporarySet.next());
      if (!cgr.equals("xnat1027")) calcPRUC(reportSet);
      
      reportSetGroups = new QueryDataSet();
      reportSetGroups.setColumns(reportSet.cloneColumns());
      reportSetGroups.open();
      
      temporarySet.first();
      cgr = "xnat1027";
      int cpar = -1027;
      do {
        checkClosing();
        String ncgr = temporarySet.getString("CGRART");
        if (ncgr == null || ncgr.length() == 0) ncgr = "X_X";
        //if (temporarySet.getString("CGRART").equals("")) temporarySet.setString("CGRART","X_X");
        if (temporarySet.getInt("CPAR") != cpar || !cgr.equals(ncgr)) {
        //if (!ld.raLocate(reportSetGroups, new String[]{"CGRART","CPAR"}, new String[]{temporarySet.getString("CGRART"), temporarySet.getInt("CPAR")+""})) {
          if (cpar != -1027 || !cgr.equals("xnat1027")) calcPRUC(reportSetGroups);
          
          reportSetGroups.insertRow(false);
          reportSetGroups.setString("CSKL", temporarySet.getString("CSKL"));
          reportSetGroups.setString("GOD", temporarySet.getString("GOD"));
          reportSetGroups.setString("VRDOK", temporarySet.getString("VRDOK"));
          
          reportSetGroups.setInt("CPAR", cpar = temporarySet.getInt("CPAR"));
          reportSetGroups.setString("NAZPAR", pca.getNameNotNull(cpar));
          reportSetGroups.setString("CGRART", cgr = ncgr);
          
          if (ld.raLocate(grupeArt,"CGRART", cgr)){
            reportSetGroups.setString("NAZGRART", grupeArt.getString("NAZGRART"));
          } else {
            reportSetGroups.setString("NAZGRART", "Ostalo");
          }
          
          reportSetGroups.setBigDecimal("INAB", temporarySet.getBigDecimal("INAB"));
          reportSetGroups.setBigDecimal("RUC", temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB")));
          reportSetGroups.setBigDecimal("IPRODBP", temporarySet.getBigDecimal("IPRODBP"));
          reportSetGroups.setBigDecimal("POR", temporarySet.getBigDecimal("POR"));
          reportSetGroups.setBigDecimal("IPRODSP", temporarySet.getBigDecimal("IPRODSP"));
          reportSetGroups.setBigDecimal("ITOT", temporarySet.getBigDecimal("ITOT"));
        } else {
          reportSetGroups.setBigDecimal("INAB", reportSetGroups.getBigDecimal("INAB").add(temporarySet.getBigDecimal("INAB")));
          reportSetGroups.setBigDecimal("RUC", reportSetGroups.getBigDecimal("RUC").add(temporarySet.getBigDecimal("IPRODBP").subtract(temporarySet.getBigDecimal("INAB"))));
          reportSetGroups.setBigDecimal("IPRODBP", reportSetGroups.getBigDecimal("IPRODBP").add(temporarySet.getBigDecimal("IPRODBP")));
          reportSetGroups.setBigDecimal("POR", reportSetGroups.getBigDecimal("POR").add(temporarySet.getBigDecimal("POR")));
          reportSetGroups.setBigDecimal("IPRODSP", reportSetGroups.getBigDecimal("IPRODSP").add(temporarySet.getBigDecimal("IPRODSP")));
          reportSetGroups.setBigDecimal("ITOT", reportSetGroups.getBigDecimal("ITOT").add(temporarySet.getBigDecimal("ITOT")));
        }
      } while (temporarySet.next());
      if (cpar != -1027 || !cgr.equals("xnat1027")) calcPRUC(reportSetGroups);
      
      reportSet.getColumn("CSKL").setVisible(0);
      reportSet.getColumn("CPAR").setVisible(0);
      reportSet.getColumn("NAZPAR").setVisible(0);
      reportSet.getColumn("CGRART").setVisible(0);
      reportSet.getColumn("VRDOK").setVisible(0);
      reportSet.getColumn("GOD").setVisible(0);
      
      reportSet.setSort(new SortDescriptor(new String[]{"CGRART"}));
      
      reportSet.last();
      reportSet.setTableName("GR");
      setDataSetAndSums(reportSet, new String[]{"INAB", "RUC", "IPRODBP", "POR", "IPRODSP"});
    } else if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ")) { 
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonths", "hr.restart.robno.repStatsMonths", "StatsMonths", "Top lista mjeseèna");
      this.addReport("hr.restart.robno.RepStatParMonthsChart", "hr.restart.robno.RepStatParMonthsChart", "Top lista kupaca mjeseèno - grafikon");

      String _od_ = fieldSet.getTimestamp("pocDatum").toString().substring(5, 7);
      String _do_ = fieldSet.getTimestamp("zavDatum").toString().substring(5, 7);

      System.out.println("od " + _od_ + " mjeseca do " + _do_ + " mjeseca");

      int odInt = Integer.parseInt(_od_);
      int doInt = Integer.parseInt(_do_);

//      String[] includedMonths = new String[(doInt - odInt) + 1];

      Column[] cols = new Column[7 + (doInt - odInt) + 1];
      
      /*int wisCols = (doInt - odInt) + 1;
      
      for (int i=0; i < wisCols; wisCols++){
        
      }*/

      cols[0] = dm.createIntColumn("CART", "Šifra");
      cols[1] = dm.createStringColumn("CART1", "Oznaka", 20);
      cols[2] = dm.createStringColumn("BC", "Barcode", 20);
      cols[3] = dm.createStringColumn("NAZART", "Naziv artikla", 50);
      cols[4] = dm.createIntColumn("CPAR", "Kupac");
      cols[5] = dm.createStringColumn("NAZPAR", "Naziv kupca", 150);

      int iter1 = 6;
      int iter2 = 0;

      String[] prikaz = new String[(doInt - odInt) + 2];

      for (int i = odInt; i <= doInt; i++) {
        if (i < 10){
          cols[iter1++] = dm.createBigDecimalColumn("0"+i, moonshine[i-1], 2);
          prikaz[iter2++] = "0"+i;
        } else {
          cols[iter1++] = dm.createBigDecimalColumn(""+i, moonshine[i-1], 2);
          prikaz[iter2++] = ""+i;
        }
        
        System.out.println("prikaz["+(iter2-1)+"]="+prikaz[iter2-1]);
      }

      cols[iter1] = dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2);
      prikaz[iter2] = "UKUPNO";

      monthSet = new QueryDataSet();
      monthSet.setLocale(Locale.getDefault());
      monthSet.setColumns(cols);
      monthSet.open();

      //      monthSet.setColumns(new Column[] {
      //                          dm.createIntColumn("CART", "Šifra"),
      //                          dm.createStringColumn("CART1", "Oznaka", 20),
      //                          dm.createStringColumn("BC", "Barcode", 20),
      //                          dm.createStringColumn("NAZART", "Naziv artikla", 50),
      //                          dm.createIntColumn("CPAR", "Kupac"),
      //                          dm.createStringColumn("NAZPAR","Naziv kupca",50),
      //                          dm.createBigDecimalColumn("01", moonshine[0], 2),
      //                          dm.createBigDecimalColumn("02", moonshine[1], 2),
      //                          dm.createBigDecimalColumn("03", moonshine[2], 2),
      //                          dm.createBigDecimalColumn("04", moonshine[3], 2),
      //                          dm.createBigDecimalColumn("05", moonshine[4], 2),
      //                          dm.createBigDecimalColumn("06", moonshine[5], 2),
      //                          dm.createBigDecimalColumn("07", moonshine[6], 2),
      //                          dm.createBigDecimalColumn("08", moonshine[7], 2),
      //                          dm.createBigDecimalColumn("09", moonshine[8], 2),
      //                          dm.createBigDecimalColumn("10", moonshine[9], 2),
      //                          dm.createBigDecimalColumn("11", moonshine[10], 2),
      //                          dm.createBigDecimalColumn("12", moonshine[11], 2),
      //                          dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2)
      //      });

      try {
        racunica(temporarySet, pca);
      } /*
         * catch (com.borland.dx.dataset.VariantException ve) {
         * System.err.println("Vaariant Exception"); ve.printStackTrace(); }
         */catch (Exception ex) {
        System.err.println("Other Exception");
        ex.printStackTrace();
        setNoDataAndReturnImmediately();
      }

      //      jptvSet = monthSet;

      monthSetGraph = new QueryDataSet();

      monthSetGraph.setColumns(new Column[]{dm.createIntColumn("CPAR", "Kupac"), dm.createStringColumn("NAZPAR", "Naziv partnera", 150), dm.createStringColumn("MJESEC", "Mjesec", 2), dm.createBigDecimalColumn("IZNOS", "Iznos", 2), dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2)});

      monthSetGraph.open();

      monthSet.first();
      //dm.getPartneri().open();
//      String[] mpb = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

      do {
        for (int i = 0; i < (prikaz.length-1); i++) {
          monthSetGraph.insertRow(false);
          monthSetGraph.setInt("CPAR", monthSet.getInt("CPAR"));

          monthSetGraph.setString("NAZPAR", pca.getNameNotNull(monthSet.getInt("CPAR"))); 
          monthSetGraph.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO"));

          monthSetGraph.setString("MJESEC", prikaz[i]);
          monthSetGraph.setBigDecimal("IZNOS", monthSet.getBigDecimal(prikaz[i]));
        }
      } while (monthSet.next());

      monthSetGraph.setSort(new SortDescriptor(new String[]{"UKUPNO", "CPAR"}, true, true));

      /*jptvSet = new QueryDataSet();
      jptvSet.setColumns(monthSet.cloneColumns());
      jptvSet.open();
      monthSet.first();
      do {
        jptvSet.insertRow(false);
        monthSet.copyTo(jptvSet);
      } while (monthSet.next());
      jptvSet.last();*/
      monthSet.last();
      monthSet.setTableName("MJ");
      setDataSetAndSums(monthSet, prikaz);//new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"}); 2004-09-14 16:54:48.000
    }
    sw.report("over");
  }

  private QueryDataSet detaljnoPoArtiklima(QueryDataSet tmpSet, PartnerCache pca) {
    
    tmpSet.first();
    QueryDataSet dpaSet = new QueryDataSet();
    dpaSet.setColumns(new Column[]{
        dm.createIntColumn("CPAR", "Oznaka kupca"), 
        dm.createStringColumn("NAZPAR", "Naziv kupca", 150),
        dm.createIntColumn("PJ", "Poslovna jedinica"),
        dm.createStringColumn("NAZPJ", "Naziv poslovne jedinice", 150),
        dm.createIntColumn("CART", "Šifra artikla"), 
        dm.createStringColumn("NAZART", "Naziv artikla", 50), 
        dm.createStringColumn("JM", "Jedinica mjere", 5), 
        dm.createStringColumn("CGRART", "Grupa artikala", 10), 
        dm.createBigDecimalColumn("KOL", "Kolièina", 3), 
        dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
        dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
        dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
        dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
        dm.createBigDecimalColumn("POR", "Porez", 2), 
        dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2),
        dm.createBigDecimalColumn("ITOT", "Prodajni iznos bez popusta", 2)
    });
    dpaSet.setRowId("CPAR", true);
    dpaSet.setRowId("PJ", true);
    dpaSet.setRowId("CART", true);
    dpaSet.open();
    int cpar = -1027, cart = -1027, pj = -1027;
    boolean ispj = fieldSet.getString("PRIKAZ").equalsIgnoreCase("PJ");
    DataRow drpj = null;
    do {
      //if (!ld.raLocate(dpaSet, new String[]{"CPAR", "CART"}, new String[]{tmpSet.getInt("CPAR") + "", tmpSet.getInt("CART") + ""})) {
      if (tmpSet.getInt("CPAR") != cpar || (ispj && tmpSet.getInt("PJ") != pj) || tmpSet.getInt("CART") != cart) {
        if (cpar != -1027 || cart != -1027) {
          try {
            dpaSet.setBigDecimal("PRUC", dpaSet.getBigDecimal("RUC").divide(dpaSet.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
          } catch (Exception ex) {
            dpaSet.setBigDecimal("PRUC", new java.math.BigDecimal("0.00"));
          }
        }
        dpaSet.insertRow(false);
        dpaSet.setInt("CPAR", cpar = tmpSet.getInt("CPAR"));
        dpaSet.setString("NAZPAR", pca.getNameNotNull(cpar));
        if (ispj) {
        	if (pj != tmpSet.getInt("PJ"))
        		drpj = ld.raLookup(dm.getPjpar(), new String[] {"CPAR", "PJ"}, tmpSet);
	        dpaSet.setInt("PJ", pj = tmpSet.getInt("PJ"));
	        dpaSet.setString("NAZPJ","");        
         	if (drpj != null) dpaSet.setString("NAZPJ", drpj.getString("NAZPJ"));
        }

        dpaSet.setInt("CART", cart = tmpSet.getInt("CART"));
        dpaSet.setString("NAZART", getString("NAZART", dpaSet, tmpSet));
        dpaSet.setString("CGRART", getString("CGRART", dpaSet, tmpSet));
        dpaSet.setString("JM", getString("JM", dpaSet, tmpSet));
        dpaSet.setBigDecimal("KOL", tmpSet.getBigDecimal("KOL"));
        dpaSet.setBigDecimal("INAB", tmpSet.getBigDecimal("INAB"));
        dpaSet.setBigDecimal("RUC", tmpSet.getBigDecimal("IPRODBP").subtract(tmpSet.getBigDecimal("INAB")));
        dpaSet.setBigDecimal("IPRODBP", tmpSet.getBigDecimal("IPRODBP"));
        dpaSet.setBigDecimal("POR", tmpSet.getBigDecimal("POR"));
        dpaSet.setBigDecimal("IPRODSP", tmpSet.getBigDecimal("IPRODSP"));
        dpaSet.setBigDecimal("ITOT", tmpSet.getBigDecimal("ITOT"));
      } else {
        dpaSet.setBigDecimal("KOL", dpaSet.getBigDecimal("KOL").add(tmpSet.getBigDecimal("KOL")));
        dpaSet.setBigDecimal("INAB", dpaSet.getBigDecimal("INAB").add(tmpSet.getBigDecimal("INAB")));
        dpaSet.setBigDecimal("RUC", dpaSet.getBigDecimal("RUC").add(tmpSet.getBigDecimal("IPRODBP").subtract(tmpSet.getBigDecimal("INAB"))));
        dpaSet.setBigDecimal("IPRODBP", dpaSet.getBigDecimal("IPRODBP").add(tmpSet.getBigDecimal("IPRODBP")));
        dpaSet.setBigDecimal("POR", dpaSet.getBigDecimal("POR").add(tmpSet.getBigDecimal("POR")));
        dpaSet.setBigDecimal("IPRODSP", dpaSet.getBigDecimal("IPRODSP").add(tmpSet.getBigDecimal("IPRODSP")));
        dpaSet.setBigDecimal("ITOT", dpaSet.getBigDecimal("ITOT").add(tmpSet.getBigDecimal("ITOT")));
      }
    } while (tmpSet.next());
    if (cpar != -1027 || cart != -1027) {
      try {
        dpaSet.setBigDecimal("PRUC", dpaSet.getBigDecimal("RUC").divide(dpaSet.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
      } catch (Exception ex) {
        dpaSet.setBigDecimal("PRUC", new java.math.BigDecimal("0.00"));
      }
    }
    try {
    	if (ispj) dpaSet.setSort(new SortDescriptor(new String[]{"CPAR", "PJ", "CART"}));
    	else dpaSet.setSort(new SortDescriptor(new String[]{"CPAR","CART"}));
    } catch (Exception ex){
      ex.printStackTrace(); 
    }
    return dpaSet;
  }

  private String getString(String cn, QueryDataSet destSet,
      QueryDataSet srcSet) {
    try {
      String s = srcSet.getString(cn);
      int l = destSet.hasColumn(cn).getPrecision();
      int sl = s.length();
      if (sl > l) return s.substring(0, l);
    } catch (Exception e) {
      // TODO: handle exception
    }
    return srcSet.getString(cn);
  }

  protected void racunica(QueryDataSet tmpSet, PartnerCache pca) {
    String misec;
    tmpSet.setSort(new SortDescriptor(new String[] {"CPAR"}));
    tmpSet.first();
    int cpar = -1027;
    if (getCkup().equalsIgnoreCase("")) {
      do {
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        //if (!lookupData.getlookupData().raLocate(monthSet, "CPAR", tmpSet.getInt("CPAR") + "")) {
        if (tmpSet.getInt("CPAR") != cpar) {
          monthSet.insertRow(false);
          monthSet.setInt("CPAR", cpar = tmpSet.getInt("CPAR"));
          monthSet.setString("NAZPAR", pca.getNameNotNull(cpar));
          try {
            monthSet.setBigDecimal(misec, tmpSet.getBigDecimal(getKolonu()));
            monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal(getKolonu()));
          } catch (Exception ex) {
            monthSet.setBigDecimal(misec, new BigDecimal(tmpSet.getDouble(getKolonu())));
            monthSet.setBigDecimal("UKUPNO", new BigDecimal(tmpSet.getDouble(getKolonu())));
          }
        } else {
          try {
            monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal(getKolonu())));
            monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal(getKolonu())));
          } catch (Exception ex1) {
            monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(new BigDecimal(tmpSet.getDouble(getKolonu()))));
            monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(new BigDecimal(tmpSet.getDouble(getKolonu()))));
          }
        }
      } while (tmpSet.next());

      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("NAZART").setVisible(0);

      if (fieldSet.getString("SLJED").equals("CPAR"))
        monthSet.setSort(new SortDescriptor(new String[]{"NAZPAR"}));
      else
        monthSet.setSort(new SortDescriptor(new String[]{"UKUPNO"}, true, true));

    } else {
      do {
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        //if (!lookupData.getlookupData().raLocate(monthSet, "CART", tmpSet.getInt("CART") + "")) {
        if (tmpSet.getInt("CPAR") != cpar) {
          monthSet.insertRow(false);
          monthSet.setInt("CART", tmpSet.getInt("CART"));
          monthSet.setInt("CPAR", cpar = tmpSet.getInt("CPAR"));
          monthSet.setString("NAZPAR", pca.getNameNotNull(cpar));
          try {
            monthSet.setString("CART1", tmpSet.getString("CART1"));
          } catch (Exception e) {
            //            System.out.println("Exepshn - int from string");
            monthSet.setString("CART1", tmpSet.getString("CART11"));
          }

          monthSet.setString("BC", tmpSet.getString("BC"));
          monthSet.setString("NAZART", tmpSet.getString("NAZART"));
          try {
            monthSet.setBigDecimal(misec, tmpSet.getBigDecimal(getKolonu()));
            monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal(getKolonu()));
          } catch (Exception ex2) {
            monthSet.setBigDecimal(misec, new BigDecimal(tmpSet.getDouble(getKolonu())));
            monthSet.setBigDecimal("UKUPNO", new BigDecimal(tmpSet.getDouble(getKolonu())));

          }
        } else {
          try {
            monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal(getKolonu())));
            monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal(getKolonu())));
          } catch (Exception ex3) {
            monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(new BigDecimal(tmpSet.getDouble(getKolonu()))));
            monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(new BigDecimal(tmpSet.getDouble(getKolonu()))));

          }
        }
      } while (tmpSet.next());

      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("CPAR").setVisible(0);
      monthSet.getColumn("NAZPAR").setVisible(0);
      //      monthSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);

      monthSet.setSort(new SortDescriptor(new String[]{"UKUPNO"}, true, true));
    }

    monthSet.last();
    //    getJPTV().setDataSetAndSums(monthSet, new String[]{"01", "02", "03",
    // "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"});
  }

  private void jbInit() throws Exception {
    rcmbPrikaz.setRaItems(new String[][]{{"Po raèunima", "IR"}, {"Po raèunima i PJ", "PJ"}, {"Ukupni iznos", "UI"}, {"Ukupni iznos po PJ", "UP"}, {"Po grupama artikala", "GR"}, {"Mjeseèno", "MJ"}});

    rcmbPoCemu.setRaItems(new String[][]{{"Nabavni iznos", "INAB"}, {"Razlika u cijeni", "RUC"}, {"Iznos bez poreza", "IPRODBP"}, {"Porez", "POR"}, {"Iznos s porezom", "IPRODSP"}});

    rcmbPrikaz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSljed();
      }
    });

    rcmbPoCemu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setSljedDva();
      }
    });
    
    rcmbVrArt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setRapancartDataset();
      }
    });
  }
  
//  private void setRapancartDataset(){
//    System.out.println("Vrsta artikla promjenjena"); //XDEBUG delete when no more needed
//    DataSet vrstaArtikalaDohvatFilterSet = null;
//    String vra = fieldSet.getString("VRART");
//    
//    System.out.println("Vrsta artikala = "+ vra); //XDEBUG delete when no more needed
//    
//    rpcart.clearFields();
//    if (vra.equals("X") || vra.equals(""))
//      vrstaArtikalaDohvatFilterSet = dm.getArtikli();
//    else {
//      vrstaArtikalaDohvatFilterSet = Artikli.getDataModule().getFilteredDataSet("VRART='"+vra+"'");
//    }
//    
//    rpcart.jrfCART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
//    rpcart.jrfCART1.setRaDataSet(vrstaArtikalaDohvatFilterSet);
//    rpcart.jrfBC.setRaDataSet(vrstaArtikalaDohvatFilterSet);
//    rpcart.jrfNAZART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
//    rpcart.jrfJM.setRaDataSet(vrstaArtikalaDohvatFilterSet);
//  }

  private void setSljed() {
    rcmbPoCemu.setSelectedIndex(0);
    rcc.setLabelLaF(rcmbPoCemu, false);
    if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR") || fieldSet.getString("PRIKAZ").equalsIgnoreCase("GR")
    		 || fieldSet.getString("PRIKAZ").equalsIgnoreCase("PJ")) {
      rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}, {"Iznos raèuna", "IPRODSP"}});
      fieldSet.setString("SLJED", "CPAR");
    } else if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("UI")) {
      rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}, {"Ukupan iznos", "IPRODSP"}});
      fieldSet.setString("SLJED", "CPAR");
    } else if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("UP")) {
      rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}});
      fieldSet.setString("SLJED", "CPAR");
    } else {
      rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}, {"Ukupno " + rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(), "BRDOK"}});
      fieldSet.setString("SLJED", "CPAR");
      fieldSet.setString("POCEMU", "INAB");
      rcc.setLabelLaF(rcmbPoCemu, true);
    }
    rcmbSljed.setSelectedIndex(0);
  }

  private void setSljedDva() {
    int position;
    try {
      position = rcmbSljed.getSelectedIndex();
    } catch (Exception ex) {
      position = 0;
    }
    rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}, {"Ukupno " + rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(), "BRDOK"}});
    try {
      rcmbSljed.setSelectedIndex(position);
    } catch (Exception ex) {}
  }

  private QueryDataSet getRawSet(String pdat, String zdat) {
    String selStr = "select " + 
    "doki.cskl, " +
    "doki.brdok, " + 
    "doki.vrdok, " + 
    "doki.god, " + 
    "doki.datdok, " + 
    "doki.cpar, " +
    "doki.pj, " + 
    "stdoki.cart, " + "stdoki.cart1, " + 
    "stdoki.bc, " + "stdoki.nazart, " + "stdoki.jm, " + " artikli.cgrart, " + "stdoki.kol, " + 
    "stdoki.iraz, " + "stdoki.iprodbp, " + "CAST ((stdoki.iprodsp - stdoki.iprodbp) AS numeric(12,2)) as por, " + 
    "stdoki.iprodsp, " + "stdoki.inab, " + "CAST ((stdoki.iprodbp-stdoki.inab) AS numeric(12,2)) as ruc, " +
    "CAST ((stdoki.iprodsp+stdoki.uirab) AS numeric(12,2)) as itot ";

    String cskls;
    if (getCskl().equals("")) {
      QueryDataSet sklds = hr.restart.robno.Util.getSkladFromCorg();
      sklds.open();
      sklds.first();

      String sifskl = "";
      
      do {
        sifskl += "'"+sklds.getString("CSKL")+"'";
        if (sklds.next()) sifskl += ",";
        else break;
      } while (true);

      cskls = " AND DOKI.CSKL in ("+sifskl+")";
    } else {
      cskls = " AND DOKI.CSKL = '"+getCskl()+"'";
    }
      
      /*
      cskls = "in (";
      do {
        cskls += "'" + sklds.getString("CSKL") + "',";
      } while (sklds.next());
      cskls = cskls.substring(0, cskls.length() - 1) + ")";
      //        System.out.println("cskls " + cskls);
    } else {
      cskls = "= '" + getCskl() + "'";
    }
*/
      
    String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(fieldSet.getString("CORG"));
    if (corgs.rowCount() == 0)
      inq = "1=1";
    else if (corgs.rowCount() == 1)
      inq = "DOKI.CSKL = '" + fieldSet.getString("CORG") + "'";
    else
      inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs, "DOKI.CSKL")+") ";
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    String exInClude = "AND ((" + oj + " AND " + inq + ") OR (" + oj.not() + cskls + ")) ";//"
                                                                                                               // AND
                                                                                                               // DOKI.CSKL
                                                                                                               // =
                                                                                                               // '"+getCskl()+"'))
                                                                                                               // ";

    String artikliFilter;

    if (fieldSet.getString("VRART").equals("") || fieldSet.getString("VRART").equals("X"))
      artikliFilter = "";
    else
      artikliFilter = " AND ARTIKLI.VRART='" + fieldSet.getString("VRART") + "' ";

    String carting = "";
    if (!rpcart.findCART(podgrupe).equals("")) {
      carting = " AND " + rpcart.findCART(podgrupe);
    }

    String ckupca = "", pjKupca = "";

    if (!getCkup().equals("")) ckupca = "and doki.cpar='" + getCkup() + "' ";
    if (!getPjCkup().equals("")) pjKupca = "and doki.pj='" + getPjCkup() + "' ";

    selStr += " from doki,stdoki,artikli WHERE doki.cskl = stdoki.cskl " +
            "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god " + 
            "AND doki.brdok = stdoki.brdok AND stdoki.cart = artikli.cart " +
            "AND doki.vrdok not in ('PON','TRE','ZAH','NDO','NKU','RNL','REV','PRV','OTR','OTP','INM','INV','IZD','OTP', 'DOS') " + 
            exInClude + ckupca + pjKupca + artikliFilter + carting + 
            " and doki.datdok between " + pdat + " " + "and " + zdat;

    //if ()
    // REMARK!!
    // nešto što sam primjetio, a moglo bi dobro doæi kad se bude implementirao
    // nabavljaè je da postoji i CPAR kolona u tablici artikli
    // i predstavlja dobavljaèa za taj artikl.

        System.out.println("---> "+selStr+" <---");
        
    QueryDataSet ret = ut.getNewQueryDataSet(selStr);

    if (ret.rowCount() == 0)
      return null;
    return ret;
  }

  private String getKolonu() {
    return fieldSet.getString("POCEMU");
  }

  public double getPostoSumRuc() {
    return postoSumRuc;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrucQDS() {
    return /*jptvSet*/reportSet;
  }

  public QueryDataSet getReportSet() {
    return monthSet;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportQDS() {
    return reportSet;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportQDSgrupe() {
    return reportSetGroups;
  }

  public QueryDataSet getChartSet() {
    return monthSetGraph;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportQDSDetaljno() {
    return reportSet2;
  }

  public boolean isIspis() {
    //  System.out.println("\njptv ima dataset - " +
    // (getJPTV().getDataSet()!=null));
    //  System.out.println("isIspis - " + isIspis+"\n");
    return (getJPTV().getDataSet() != null && isIspis);
  }

  QueryDataSet /*cache,*/ repCache;

  boolean doubleClicked, isIspis;

  public void firstESC() {
    //	jbChart.setVisible(false);

    if (doubleClicked) {
      doubleClicked = false;
      this.killAllReports();

      if (getCkup().equals("")) {
        this.addReport("hr.restart.robno.repStatPar", "hr.restart.robno.repStatPar", "StatPar", "Top lista kupaca");

        this.addReport("hr.restart.robno.RepStatParChart", "hr.restart.robno.RepStatParChart", "Top lista kupaca - grafikon");
      } else
        this.addReport("hr.restart.robno.repStatParOne", "hr.restart.robno.repStatPar", "StatParOne", "Top lista kupaca"); //System.out.println("dodajem
                                                                                                                           // -
                                                                                                                           // hr.restart.robno.repStatPar");

      jpKup.setCpar("");

      changeIcon(1);

      /*jptvSet = cache;*/
      reportSet = repCache;
      fieldSet.setString("PRIKAZ", "UI");
      //reportSet.last();
      setDataSetAndSums(/*jptvSet*/reportSet, new String[]{"INAB", "RUC", "IPRODBP", "POR", "IPRODSP"});
    } else
      super.firstESC();
  }

  public void jptv_doubleClick() {
    if (fieldSet.getString("PRIKAZ").equals("IR") || fieldSet.getString("PRIKAZ").equals("PJ")) {
      //      System.out.println("imam - " + getJPTV().getDataSet().getString("CSKL")
      // + " - " + getJPTV().getDataSet().getString("VRDOK") + " - " +
      // getJPTV().getDataSet().getInt("BRD") + " - " +
      // getJPTV().getDataSet().getString("GOD"));
    	DataSet ds = getJPTV().getDataSet();
      util.showDocs(ds.getString("CSKL"), "", ds.getString("VRDOK"), 
      		ds.getInt("BRD"), ds.getString("GOD"));
    } else if (fieldSet.getString("PRIKAZ").equals("UI")) {
      jpKup.setCpar(this.getJPTV().getDataSet().getInt("CPAR"));
      /*cache = getJPTV().getDataSet();*/
      repCache = reportSet;
      doubleClicked = true;
      isIspis = false;
      fieldSet.setString("PRIKAZ", "IR");
      ok_action();
      isIspis = true;
    } else if (fieldSet.getString("PRIKAZ").equals("MJ")) {

    } else
      System.out.println("Not implemented for function yet....");
  }
  
  public void cancelPress() {
    doubleClicked = false;
    repCache = null;
    super.cancelPress();
  }

  public boolean isPoRacunima() {
    //      System.out.println(fieldSet.getString("PRIKAZ") + " - " +
    // fieldSet.getString("PRIKAZ").equals("IR"));
    return fieldSet.getString("PRIKAZ").equals("IR") || fieldSet.getString("PRIKAZ").equals("PJ");
  }
  
  public String getSortCol() {
    if (fieldSet.getString("SLJED").equals("CPAR"))
      return "NAZPAR";
    return "IPRODSP";
  }

  public String getPoCemu() {
    return rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString();
  }
  
  public String getPeriod() {
    return raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("pocDatum")) + " - " + raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("zavDatum"));
  }

}