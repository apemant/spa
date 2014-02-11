/****license*****************************************************************
**   file: upProdajaPoDucanima.java
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

//import hr.restart.util.*;

//import hr.restart.util.lookupData;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.sysoutTEST;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

public class upProdajaPoDucanima extends raPanStats {

  QueryDataSet reportSet, chartSet, detaildeSet, detailedChartSet;

  dM dm = hr.restart.baza.dM.getDataModule();

  lookupData ld = lookupData.getlookupData();

  public String[] moonshine = new String[]{"Sijeèanj", "Veljaèa", "Ožujak", "Travanj", "Svibanj", "Lipanj", "Srpanj", "Kolovoz", "Rujan", "Listopad", "Studeni", "Prosinac"};

  protected jpMoreCsklad jMCs = new jpMoreCsklad() {
    public void addFields() {
      this.add(skladista, new XYConstraints(150, 0, 455, -1));
      this.add(jodbir, new XYConstraints(610, 0, 21, 21));
    }
  };

  JLabel jlAgent = new JLabel();

  JlrNavField jlrCagent = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {}
    }
  };

  JlrNavField jlrNazAgent = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {}
    }
  };

  JraButton jbSelCagent = new JraButton();

  private static upProdajaPoDucanima instanceOfMe;

  public upProdajaPoDucanima() {
    init();
    instanceOfMe = this;
  }

  public static upProdajaPoDucanima getInstance() {
    if (instanceOfMe == null)
      instanceOfMe = new upProdajaPoDucanima();
    return instanceOfMe;
  }

  protected void showDefaultValues() {
    super.showDefaultValues();
    rpcart.EnabDisab(true);
    fieldSet.setString("PRIKAZ", "UI");
    setSljed();
    getJPTV().clearDataSet();
    fieldSet.setString("VRART", "S");
    jMCs.skladista.requestFocus();
  }

  public void firstESC() {
    boolean wasTable = this.getJPTV().getDataSet() != null;
    rpcart.EnabDisab(true);
    rpcart.clearFields();
    getJPTV().clearDataSet();

    rcc.setLabelLaF(jtfPocDatum, true);
    rcc.setLabelLaF(jtfZavDatum, true);
    rcc.setLabelLaF(rcmbPrikaz, true);

    rcc.setLabelLaF(jlrCagent, true);
    rcc.setLabelLaF(jlrNazAgent, true);
    rcc.setLabelLaF(jbSelCagent, true);

    rcc.setLabelLaF(rcmbPoCemu, fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ") /* true */);
    rcc.setLabelLaF(rcmbSljed, /* !fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ") */true);
    rcc.setLabelLaF(rcmbVrArt, true);

    rcc.EnabDisabAll(jMCs, true);
    if (!jlrCagent.getText().equalsIgnoreCase("")) {
      jlrCagent.setText("");
      jlrCagent.emptyTextFields();
      jlrCagent.requestFocus();
    } else if (wasTable || !jMCs.skladista.getText().equals("")) {
      jMCs.skladista.setText("");
      jMCs.skladista.requestFocus();
    } else {
//      rcmbPrikaz.setSelectedIndex(0);
//      rcmbPoCemu.setSelectedIndex(0);
//      rcmbVrArt.setSelectedIndex(0);
//      fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
//      fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false, 0));
      enabCorg(true);
      jlrCorg.requestFocus();
    }
    removeNav();
  }

  public boolean runFirstESC() {
    //    return !rpcskl.getCSKL().equals("");
    return !jlrCorg.getText().equals("");
  }

  String[] prikaz;
  
  public String navDoubleClickActionName() {
    return "";
  }
  public int[] navVisibleColumns() {
    return new int[] {0,1,2,3,4,5};
  }

  public void okPress() {
    sklfin = fieldSet.getString("VRART").equalsIgnoreCase("SF");
    fin = fieldSet.getString("VRART").equalsIgnoreCase("F");
    
    System.out.println("SKLADFIN - "+sklfin+" FIN - "+fin);
    
    QueryDataSet jptvSet = new QueryDataSet();
    jptvSet.setLocale(Aus.hr);
    if (!fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ")) {
      
        this.killAllReports(); 
        if (fin)
          this.addReport("hr.restart.robno.repStatSkladista",
                         "hr.restart.robno.repStatSkladista", "StatSkladsFin", "Prodaja po prodajnim mjestima"); 
        else
          this.addReport("hr.restart.robno.repStatSkladista",
                         "hr.restart.robno.repStatSkladista", "StatSklads", "Prodaja po prodajnim mjestima");
        
        this.addReport("hr.restart.robno.RepStatPreglSklad",
                       "hr.restart.robno.RepStatPreglSklad", "Prodaja po prodajnim mjestima - grafikon");
       
      reportSet = racunica(ut.getNewQueryDataSet(getUpit())/*, "PM"*/);
//      sysoutTEST syst = new sysoutTEST(false);
      //    getUpit();
      //    reportSet = ut.getNewQueryDataSet(getUpit());
      jptvSet.setColumns(reportSet.cloneColumns());
      if (fin) {
        jptvSet.getColumn("INAB").setVisible(0);
        jptvSet.getColumn("RUC").setVisible(0);
        jptvSet.getColumn("PRUC").setVisible(0);
      }
      jptvSet.open();
      reportSet.first();
      System.out.println("before do"); //XDEBUG delete when no more needed
      do {
        checkClosing();
        jptvSet.insertRow(false);
        reportSet.copyTo(jptvSet);
      } while (reportSet.next());
      System.out.println("after do"); //XDEBUG delete when no more needed
      if (fin)
        jptvSet.setTableName("ukupno-fin");
      else jptvSet.setTableName("ukupno");
      setDataSetAndSums(jptvSet, new String[]{"INAB", "RUC", "POR", "IPRODBP", "IPRODSP"});
    } else { //TODO ovdje za po mjesecima...
      /*
       * this.killAllReports(); //TODO izvjestaj!!!!!
       * this.addReport("hr.restart.robno.repStatPreglSkladMOnthChart",
       * "hr.restart.robno.repStatPreglSkladMOnthChart", "Prodaja po radnim
       * mjestima mjeseèno - grafikon");
       */
      reportSet = mjesecnaRacunica(ut.getNewQueryDataSet(getUpit()));
      reportSet.setTableName("mj");
      setDataSetAndSums(reportSet, prikaz);

    }
    //    System.out.println(getNewUpit());
    // setNoDataAndReturnImmediately();
  }

protected QueryDataSet racunica(QueryDataSet origigi/*, String sto*/) {
    if (origigi == null || origigi.rowCount() == 0)
      setNoDataAndReturnImmediately();
    checkClosing();

    QueryDataSet tmpSet = new QueryDataSet();
    Column[] tempSetColumns;
//    String oCemSeRadi = "";
    /*if (sto.equals("PM")) {*/
      tempSetColumns = new Column[]{(
          Column) dm.getSklad().getColumn("CSKL").clone(), 
          dm.createStringColumn("NAZSKL", "Naziv prodajnog mjesta", 270), 
          dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
          dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
          dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
          dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
          dm.createBigDecimalColumn("POR", "Porez", 2), 
          dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2)};
//      oCemSeRadi = "CSKL";
    /*} else {
      tempSetColumns = new Column[]{(
          Column) dm.getAgenti().getColumn("CAGENT").clone(), 
          dm.createStringColumn("NAZSKL", "Ime i prezime agenta", 70), 
          dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
          dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
          dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
          dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
          dm.createBigDecimalColumn("POR", "Porez", 2), 
          dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2)};
      oCemSeRadi = "CAGENT";
    }*/

    tmpSet.setColumns(tempSetColumns);

    chartSet = new QueryDataSet();
    Column[] cols;
    if (fin)
      cols = new Column[]{dm.createStringColumn("CSNS", "Oznaka", 65),dm.createStringColumn("CSKL", "Oznaka", 12), dm.createStringColumn("NAZSKL", "Naziv", 250), dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), dm.createBigDecimalColumn("POR", "Porez", 2), dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2)};
    else
      cols = new Column[]{dm.createStringColumn("CSNS", "Oznaka", 65),dm.createStringColumn("CSKL", "Oznaka", 12), dm.createStringColumn("NAZSKL", "Naziv", 250), dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), dm.createBigDecimalColumn("PRUC", "% RUC", 2), dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), dm.createBigDecimalColumn("POR", "Porez", 2), dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2)};

    chartSet.setColumns(cols);
    chartSet.open();

    origigi.first();
    dm.getSklad().open();
    System.out.println("CSKL - " + origigi.getString("CSKL"));

    do {
      String cskl = origigi.getString("CSKL");
      if (origigi.getString("CSKLART").length() > 0) {
        cskl = origigi.getString("CSKLART");
        //System.out.println("CSKLART u CSKL");
      }
      checkClosing();
      if (!ld.raLocate(tmpSet, "CSKL", cskl)) {
        tmpSet.insertRow(false);
        chartSet.insertRow(false);

//        System.out.println("rowCount - " + origigi.getRow() + " od " +
// origigi.getRowCount());
//        System.out.println("CSKL - " + origigi.getString("CSKL"));
//        System.out.println("CSKLART - " + origigi.getString("CSKLART"));

//        if (sto.equals("PM")){
//        if (!origigi.getString("CSKLART").equals("")) {
          tmpSet.setString("CSKL", cskl);
          chartSet.setString("CSKL", cskl);
//        } else {
//          tmpSet.setString("CSKL", origigi.getString("CSKL"));
//          chartSet.setString("CSKL", origigi.getString("CSKL"));
//        }
//        }

        ld.raLocate(dm.getSklad(), "CSKL", cskl);

        tmpSet.setString("NAZSKL", dm.getSklad().getString("NAZSKL"));
        tmpSet.setBigDecimal("INAB", origigi.getBigDecimal("INAB"));
        tmpSet.setBigDecimal("RUC", origigi.getBigDecimal("IPRODBP").subtract(origigi.getBigDecimal("INAB")));
        tmpSet.setBigDecimal("IPRODSP", origigi.getBigDecimal("IPRODSP"));
        tmpSet.setBigDecimal("IPRODBP", origigi.getBigDecimal("IPRODBP"));
        tmpSet.setBigDecimal("POR", origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP")));

        chartSet.setString("CSNS", cskl+" - "+dm.getSklad().getString("NAZSKL"));
        chartSet.setString("NAZSKL", dm.getSklad().getString("NAZSKL"));
        if (fin) {
          chartSet.setBigDecimal("IPRODSP", origigi.getBigDecimal("IPRODSP"));
          chartSet.setBigDecimal("IPRODBP", origigi.getBigDecimal("IPRODBP"));
          chartSet.setBigDecimal("POR", origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP")));
        } else {
          chartSet.setBigDecimal("INAB", origigi.getBigDecimal("INAB"));
          chartSet.setBigDecimal("RUC", origigi.getBigDecimal("IPRODBP").subtract(origigi.getBigDecimal("INAB")));
          chartSet.setBigDecimal("IPRODSP", origigi.getBigDecimal("IPRODSP"));
          chartSet.setBigDecimal("IPRODBP", origigi.getBigDecimal("IPRODBP"));
          chartSet.setBigDecimal("POR", origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP")));
        }
      } else {
        tmpSet.setBigDecimal("INAB", tmpSet.getBigDecimal("INAB").add(origigi.getBigDecimal("INAB")));
        tmpSet.setBigDecimal("RUC", tmpSet.getBigDecimal("RUC").add(origigi.getBigDecimal("IPRODBP").subtract(origigi.getBigDecimal("INAB"))));
        tmpSet.setBigDecimal("IPRODSP", tmpSet.getBigDecimal("IPRODSP").add(origigi.getBigDecimal("IPRODSP")));
        tmpSet.setBigDecimal("IPRODBP", tmpSet.getBigDecimal("IPRODBP").add(origigi.getBigDecimal("IPRODBP")));
        tmpSet.setBigDecimal("POR", tmpSet.getBigDecimal("POR").add(origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP"))));
        //        tmpSet.setBigDecimal("",tmpSet.getBigDecimal(""));

        if (fin) {
          chartSet.setBigDecimal("IPRODSP", chartSet.getBigDecimal("IPRODSP").add(origigi.getBigDecimal("IPRODSP")));
          chartSet.setBigDecimal("IPRODBP", chartSet.getBigDecimal("IPRODBP").add(origigi.getBigDecimal("IPRODBP")));
          chartSet.setBigDecimal("POR", chartSet.getBigDecimal("POR").add(origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP"))));
        } else {
          chartSet.setBigDecimal("INAB", chartSet.getBigDecimal("INAB").add(origigi.getBigDecimal("INAB")));
          chartSet.setBigDecimal("RUC", chartSet.getBigDecimal("RUC").add(origigi.getBigDecimal("IPRODBP").subtract(origigi.getBigDecimal("INAB"))));
          chartSet.setBigDecimal("IPRODSP", chartSet.getBigDecimal("IPRODSP").add(origigi.getBigDecimal("IPRODSP")));
          chartSet.setBigDecimal("IPRODBP", chartSet.getBigDecimal("IPRODBP").add(origigi.getBigDecimal("IPRODBP")));
          chartSet.setBigDecimal("POR", chartSet.getBigDecimal("POR").add(origigi.getBigDecimal("IPRODSP").subtract(origigi.getBigDecimal("IPRODBP"))));

        }
      }
      try {
        tmpSet.setBigDecimal("PRUC", tmpSet.getBigDecimal("RUC").divide(tmpSet.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
      } catch (Exception ex) {
        tmpSet.setBigDecimal("PRUC", new java.math.BigDecimal("0.00"));
      }

      if (!fin) {
        try {
          chartSet.setBigDecimal("PRUC", chartSet.getBigDecimal("RUC").divide(chartSet.getBigDecimal("INAB"), 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100.00")));
        } catch (Exception ex) {
          chartSet.setBigDecimal("PRUC", new java.math.BigDecimal("0.00"));
        }
      }
    } while (origigi.next());

    return tmpSet;
  }  

  protected QueryDataSet mjesecnaRacunica(QueryDataSet origigi) {
    if (origigi == null || origigi.rowCount() == 0)
      setNoDataAndReturnImmediately();
    checkClosing();

    QueryDataSet tmpSet = new QueryDataSet();

    String _od_ = fieldSet.getTimestamp("pocDatum").toString().substring(5, 7);
    String _do_ = fieldSet.getTimestamp("zavDatum").toString().substring(5, 7);

    //    System.out.println("od " + _od_ + " mjeseca do " + _do_ + " mjeseca");

    int odInt = Integer.parseInt(_od_);
    int doInt = Integer.parseInt(_do_);

    String[] includedMonths = new String[(doInt - odInt) + 1];

    Column[] cols = new Column[3 + (doInt - odInt) + 1];
    //    Column[] monthCols = new Column[2 + (doInt - odInt) + 1];

    cols[0] = (Column) dm.getSklad().getColumn("CSKL").clone(); //dm.createStringColumn("CSKL",
                                                                // "Šifra",15);
    cols[1] = dm.createStringColumn("NAZSKL", "Naziv prodajnog mjesta", 270);

    //    monthCols[0] = dm.createStringColumn("CSKL", "Šifra",15);
    //    monthCols[1] = dm.createStringColumn("NAZSKL", "Naziv prodajnog mjesta",
    // 50);

    int iter1 = 2;
    int iter2 = 0;

    prikaz = new String[(doInt - odInt) + 2];
    //    String[] mjesecniPrikaz = new String[(doInt - odInt) + 1];

    for (int i = odInt; i <= doInt; i++) {
      if (i < 10) {
        cols[iter1++] = dm.createBigDecimalColumn("K0" + i, moonshine[i - 1], 2);
        //        monthCols[iter1-1] = dm.createBigDecimalColumn("0"+i, moonshine[i-1],
        // 2);
        prikaz[iter2++] = "K0" + i;
        //        mjesecniPrikaz[iter2++] = "0"+i;
      } else {
        cols[iter1++] = dm.createBigDecimalColumn("K" + i, moonshine[i - 1], 2);
        //        monthCols[iter1-1] = dm.createBigDecimalColumn(""+i, moonshine[i-1],
        // 2);
        prikaz[iter2++] = "K" + i;
        //        mjesecniPrikaz[iter2++] = ""+i;
      }

      //      System.out.println("prikaz["+(iter2-1)+"]="+prikaz[iter2-1]);
    }

    cols[iter1] = dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2);
    prikaz[iter2] = "UKUPNO";

    tmpSet.setColumns(cols);

    String misec;
    origigi.first();
    if (getCkup().equalsIgnoreCase("")) {
      do {
        String cskl = origigi.getString("CSKL");
        if (origigi.getString("CSKLART").length() > 0)
          cskl = origigi.getString("CSKLART");
        misec = "K" + origigi.getTimestamp("DATDOK").toString().substring(5, 7);
        if (!ld.raLocate(tmpSet, "CSKL", cskl)) {
          tmpSet.insertRow(false);

          tmpSet.setString("CSKL", cskl);

          ld.raLocate(dm.getSklad(), "CSKL", cskl);

          tmpSet.setString("NAZSKL", dm.getSklad().getString("NAZSKL"));
          try {
            tmpSet.setBigDecimal(misec, origigi.getBigDecimal(getKolonu()));
            //            chartSet.setBigDecimal(misec,
            // origigi.getBigDecimal(getKolonu()));
            tmpSet.setBigDecimal("UKUPNO", origigi.getBigDecimal(getKolonu()));
          } catch (Exception ex) {
            tmpSet.setBigDecimal(misec, new BigDecimal(origigi.getDouble(getKolonu())));
            //            chartSet.setBigDecimal(misec, new
            // BigDecimal(origigi.getDouble(getKolonu())));
            tmpSet.setBigDecimal("UKUPNO", new BigDecimal(origigi.getDouble(getKolonu())));
          }
        } else {
          try {
            tmpSet.setBigDecimal(misec, tmpSet.getBigDecimal(misec).add(origigi.getBigDecimal(getKolonu())));
            tmpSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("UKUPNO").add(origigi.getBigDecimal(getKolonu())));
          } catch (Exception ex1) {
            tmpSet.setBigDecimal(misec, tmpSet.getBigDecimal(misec).add(new BigDecimal(origigi.getDouble(getKolonu()))));
            tmpSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("UKUPNO").add(new BigDecimal(origigi.getDouble(getKolonu()))));
          }
          //          chartSet.setBigDecimal(misec, tmpSet.getBigDecimal(misec));
        }
      } while (origigi.next());

      tmpSet.first();
      chartSet = new QueryDataSet();
      chartSet.setColumns(new Column[]{dm.createStringColumn("CSNS", "Oznaka", 65), dm.createStringColumn("CSKL", 15), dm.createStringColumn("NAZSKL", 250), dm.createStringColumn("MJESEC", 2), dm.createBigDecimalColumn("IZNOS", 2)});
      chartSet.open();

      do {
        for (int i = 0; i < (prikaz.length - 1); i++) {
          chartSet.insertRow(false);
          chartSet.setString("CSNS", tmpSet.getString("CSKL") + " - " + tmpSet.getString("NAZSKL"));
          chartSet.setString("CSKL", tmpSet.getString("CSKL"));
          chartSet.setString("NAZSKL", tmpSet.getString("NAZSKL"));
          chartSet.setString("MJESEC", prikaz[i].substring(1));
          chartSet.setBigDecimal("IZNOS", tmpSet.getBigDecimal(prikaz[i]));
        }
      } while (tmpSet.next());

      chartSet.setSort(new SortDescriptor(new String[]{"IZNOS", "CSKL"}, true, true));
    }
    sysoutTEST syst = new sysoutTEST(false);
    syst.prn(chartSet);
    return tmpSet;
  }

  private String getKolonu() {
    return fieldSet.getString("POCEMU");
  }

  private void init() {
    //    this.addReport("hr.restart.robno.repStatSkladista",
    // "hr.restart.robno.repStatSkladista", "StatSklads", "Prodaja po radnim
    // mjestima");
    //    this.addReport("hr.restart.robno.RepStatPreglSklad",
    // "hr.restart.robno.RepStatPreglSklad", "Prodaja po radnim mjestima -
    // grafikon");

    //    jlAgent.setText("Agent");
    //    jlrCagent.setColumnName("CAGENT");
    //    jlrCagent.setDataSet(fieldSet);
    //    jlrCagent.setColNames(new String[] {"NAZAGENT"});
    //    jlrCagent.setTextFields(new javax.swing.text.JTextComponent[]
    // {jlrNazAgent});
    //    jlrCagent.setVisCols(new int[] {0,1});
    //    jlrCagent.setSearchMode(0);
    //    jlrCagent.setRaDataSet(dm.getAgenti());
    //    jlrCagent.setNavButton(jbSelCagent);
    //
    //    jlrNazAgent.setColumnName("NAZAGENT");
    //    jlrNazAgent.setNavProperties(jlrCagent);
    //    jlrNazAgent.setSearchMode(1);

    rcmbVrArt.setRaItems(new String[][]{{"Svi dokumenti", "S"}, {"Skladišno financijski", "SF"}, {"Financijski", "F"}});

    rcmbPrikaz.setRaItems(new String[][]{{"Ukupni iznos", "UI"}, {"Mjeseèno", "MJ"}});

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

    /*
     * rcmbVrArt.addActionListener(new ActionListener() { public void
     * actionPerformed(ActionEvent ae){ setKolonePrikaza(); } });
     */

    jMCs.setFrameOwner((Frame) this.getFrameOwner());

    whereToGo(jMCs.skladista);

    //    jlrCorg.setNextFocusableComponent(jMCs.skladista);

    jp.remove(rpcskl);
    jp.remove(jpKup);
    jp.remove(jpDob);
    jp.remove(jlPjPar);
    jp.remove(jlrCPjPar);
    jp.remove(jlrNazPjPar);
    jp.remove(jrbPjPar);

    jlVrArt.setText("Vrsta dok.");

    xYLayout3.setWidth(650);
    xYLayout3.setHeight(235);

    jp.add(jMCs, new XYConstraints(0, 55, -1, -1)); // 45

    //    jp.add(jlAgent, new XYConstraints(15, 70, -1, -1));
    //    jp.add(jlrCagent, new XYConstraints(150, 70, 100, -1));
    //    jp.add(jlrNazAgent, new XYConstraints(255, 70, 350, -1));
    //    jp.add(jbSelCagent, new XYConstraints(610, 70, 21, 21));

    jp.add(jtfPocDatum, new XYConstraints(150, 80, 100, -1)); // 70
    jp.add(jtfZavDatum, new XYConstraints(255, 80, 100, -1)); // 70
    jp.add(jlDatum1, new XYConstraints(15, 80, -1, -1)); // 70
    jp.add(rcmbVrArt, new XYConstraints(455, 80, 150, 20)); // 70
    jp.add(jlVrArt, new XYConstraints(365, 80, 70, -1)); // 70

    jp.add(rpcart, new XYConstraints(0, 100, 630, 77)); // 95
    jp.add(jlSljed, new XYConstraints(15, 177, -1, -1)); // 172
    jp.add(rcmbPrikaz, new XYConstraints(150, 177, 150, 21)); // 172
    jp.add(rcmbSljed, new XYConstraints(305, 177, 150, 21)); // 172
    jp.add(rcmbPoCemu, new XYConstraints(460, 177, 145, 21)); // 172

    rcmbSljed.setRaItems(new String[][]{{"Šifra", "CSKL"}, {"Ukupni iznos", "UKUPNO"}});
  }

  //  private String getNewUpit() {
  //    return "select " +
  //    	   "max (sklad.cskl) as cskl, max (sklad.nazskl) as nazskl, sum (stdoki.inab)
  // as inab, sum (stdoki.iprodbp-stdoki.inab) as ruc, sum (stdoki.IPRODBP) as
  // iprodBP, sum (stdoki.por1+stdoki.por2+stdoki.por3) as ukupor, sum
  // (stdoki.IPRODSP) as iprodsp " + "from doki,stdoki,Sklad " + "WHERE
  // doki.cskl = stdoki.cskl " + "AND doki.vrdok = stdoki.vrdok " + "AND
  // doki.god = stdoki.god " + "AND doki.brdok = stdoki.brdok " + "AND doki.cskl
  // = sklad.cskl " + "and doki.god='2004' " + "and doki.datdok between
  // '2004-01-01 00:00:00' and '2004-08-18 23:59:59' " + "AND doki.vrdok not in
  // ('PON','NDO','NKU','RNL','REV','PRV','OTR','INM','INV','IZD') AND
  // ((DOKI.VRDOK IN ('PRD','RAC','TER','ODB','GRN') AND DOKI.CSKL in
  // ('00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','21','22','30','31','32','33','34','35','36','1'))
  // OR (NOT DOKI.VRDOK IN ('PRD','RAC','TER','ODB','GRN') AND DOKI.CSKL in
  // ('01','02','03','100'))) " + "group by cskl";
  //  }

  public String getCskl() {
    System.out.println("getCskl u po SKLADISTIMA");
    if (!jMCs.isThereTokens()) {

    }
    //    System.out.println("saljem tokene...");
    return jMCs.getTokeni();
  }

  // TODO e

  boolean fin, sklfin; //, svi;

  protected String getUpit() {
//    String dks;
//    if (sklfin || fin)
//      dks = "='" + fieldSet.getString("VRART") + "'";
//    else
//      dks = " in ('F','SF')";
    

    String cskls;
    String csklc = "";
    if (getCskl().equals("")) {
      QueryDataSet sklds = hr.restart.robno.Util.getSkladFromCorg();
      sklds.open();
      sklds.first();
      cskls = "in (";
      do {
        cskls += "'" + sklds.getString("CSKL") + "',";
      } while (sklds.next());
      cskls = cskls.substring(0, cskls.length() - 1) + ")";
      //        System.out.println("cskls " + cskls);
      
      
    } else {
      System.out.println("GETCSKL() = "+getCskl());

        cskls = "in " + getCskl() + " ";

      csklc = " AND stdoki.csklart " + cskls;
    }

    String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(fieldSet.getString("CORG"));
    if (corgs.rowCount() == 0)
      inq = "1=1";
    else if (corgs.rowCount() == 1)
      inq = "DOKI.CSKL = '" + fieldSet.getString("CORG") + "'";
    else
      inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs,"DOKI.CSKL")+") ";
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    
    String dokumsi;
    
    if (sklfin)
      dokumsi = "AND (" + oj.not() + " AND DOKI.CSKL " + cskls + ") ";
    else if (fin)
      dokumsi = "AND (" + oj + " AND " + inq + csklc + ") ";
    else
    dokumsi = "AND ((" + oj + " AND " + inq + csklc + ") OR (" + oj.not() + " AND DOKI.CSKL " + cskls + ")) ";

    
//    String dokumsi = " (";
//    QueryDataSet doksi = hr.restart.baza.Vrdokum.getDataModule().getTempSet("vrsdok = 'I' and tipdok" + dks + " and APP in ('robno','rac','mp')");
//    doksi.open();
//    doksi.first();
//    do {
//      dokumsi += "'" + doksi.getString("VRDOK") + "',";
//    } while (doksi.next());
//    dokumsi = dokumsi.substring(0, dokumsi.length() - 1) + ") ";
//
//    if (sklfin) {
//      //      System.out.println("Skladisno Financijski");
//      dokumsi += "and doki.cskl in " + getCskl() + " ";
//    } else if (fin) {
//      //      System.out.println("Financijski");
//      dokumsi += "and doki.cskl = '" + fieldSet.getString("CORG") + "' AND stdoki.csklart in " + getCskl() + " ";
//    } else {
//      dokumsi += "and (doki.cskl in " + getCskl() + " or (doki.cskl = '" + fieldSet.getString("CORG") + "' AND stdoki.csklart in " + getCskl() + ")) ";
//    }

    //    System.out.println("dokumenti - " + dokumsi);

//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(doksi);

    String carting = "";
    String agent = "";
    String from = "from doki, stdoki";

    if (!rpcart.findCART(podgrupe).equals("")) {
      carting = " AND " + rpcart.findCART(podgrupe);
      from = "from doki, stdoki, artikli";
    }

    if (!fieldSet.getString("CAGENT").equalsIgnoreCase(""))
      agent = " AND doki.cagent = " + fieldSet.getString("CAGENT") + " ";

    String up = "select doki.cskl, doki.datdok, stdoki.csklart, stdoki.inab, stdoki.iprodbp, stdoki.iprodsp "
                 +from+" where doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok " +
                "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok "+ dokumsi + 
                " and doki.datdok between " + util.getTimestampValue(fieldSet.getTimestamp("pocDatum"), 1) + 
                " and " + util.getTimestampValue(fieldSet.getTimestamp("zavDatum"), 1) + " " + 
                carting + agent;

    System.out.println("Upit " + up);

    return up;
  }

  private void setSljed() {
    rcmbPoCemu.setSelectedIndex(0);
    rcc.setLabelLaF(rcmbPoCemu, false);

    /*
     * if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("IR")) {
     * rcmbSljed.setRaItems(new String[][]{{"Kupac", "CPAR"}, {"Iznos raèuna",
     * "IPRODSP"}}); fieldSet.setString("SLJED", "CPAR"); } else
     */

    if (fieldSet.getString("PRIKAZ").equalsIgnoreCase("UI")) {
      rcmbSljed.setRaItems(new String[][]{{"Skladište", "CSKL"}, {"Ukupan iznos", "IPRODSP"}});
      fieldSet.setString("SLJED", "CPAR");
    } else {
      rcmbSljed.setRaItems(new String[][]{{"Skladište", "CSKL"}, {"Ukupno " + rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(), "BRDOK"}});
      fieldSet.setString("SLJED", "BRDOK");
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
    rcmbSljed.setRaItems(new String[][]{{"Skladište", "CSKL"}, {"Ukupno " + rcmbPoCemu.getItemAt(rcmbPoCemu.getSelectedIndex()).toString().toLowerCase(), "BRDOK"}});
    try {
      rcmbSljed.setSelectedIndex(position);
    } catch (Exception ex) {}
  }

  /*
   * private void setKolonePrikaza(){ if
   * (fieldSet.getString("VRART").equalsIgnoreCase("F")){
   * rcmbPoCemu.setRaItems(new String[][]{ {"Iznos bez poreza", "IPRODBP"},
   * {"Porez", "POR"}, {"Iznos s porezom", "IPRODSP"}});
   * rcmbPoCemu.setSelectedIndex(0); fieldSet.setString("POCEMU","IPRODBP"); }
   * else { rcmbPoCemu.setRaItems(new String[][]{{"Nabavni iznos", "INAB"},
   * {"Razlika u cijeni", "RUC"}, {"Iznos bez poreza", "IPRODBP"}, {"Porez",
   * "POR"}, {"Iznos s porezom", "IPRODSP"}}); rcmbPoCemu.setSelectedIndex(0);
   * fieldSet.setString("POCEMU", "INAB"); } }
   */

  protected void newCorg() {}

  public boolean Validacija() {
    //TODO ovdje ishendlat za sve... sve... sve...
    if (jlrCorg.getText().equals("")) {
      jlrCorg.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Obvezatan unos - ORG. JEDINICA !", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }

    /*if (getCskl().equalsIgnoreCase("")) {
      jMCs.skladista.requestFocus();
      JOptionPane.showMessageDialog(jMCs.skladista.getTopLevelAncestor(), "Obavezan unos barem jednog skladišta !", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    return super.Validacija();
  }

  public QueryDataSet getDataset() {
    return reportSet;
  }

  public QueryDataSet getChartDataset() {
    sysoutTEST st = new sysoutTEST(false);
    st.prn(chartSet);
    return chartSet;
  }

  public boolean isFinancijski() {
    return fin;
  }

  public String getCagent() {
    return fieldSet.getString("CAGENT");
  }
}

/*
 * private String getUpit() { String selStr = "select doki.cskl, doki.vrdok,
 * doki.brdok, artikli.cart, artikli.cart1, artikli.bc, artikli.nazart,
 * doki.cpar, doki.datdok, stdoki.kol, stdoki.jm, stdoki.IPRODSP "; String inq;
 * System.out.println(getCskl()); StorageDataSet corgs =
 * hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(fieldSet.getString("CORG"));
 * if (corgs.rowCount() == 0) inq = "1=1"; else if (corgs.rowCount() == 1) inq =
 * "DOKI.CSKL = '" + fieldSet.getString("CORG") + "'"; else inq = "DOKI.CSKL in " +
 * hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs);
 * hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK",
 * TypeDoc.araj_docsOJ); System.out.println(inq); System.out.println(oj); String
 * exInClude = "AND ((" + oj + " AND ( " + inq + " AND STDOKI.CSKLART in
 * "+getCskl()+" )) OR (" + oj.not() + " AND DOKI.CSKL in " + getCskl() + ")) ";
 * String artikliFilter; if (fieldSet.getString("VRART").equals("") ||
 * fieldSet.getString("VRART").equals("X")) artikliFilter = ""; else
 * artikliFilter = " AND ARTIKLI.VRART='" + fieldSet.getString("VRART") + "' ";
 * String carting = ""; if (!rpcart.findCART(podgrupe).equals("")) { carting = "
 * AND " + rpcart.findCART(podgrupe); } // String ckupca = ""; // // if
 * (!getCkup().equals("")) ckupca = "and doki.cpar='" + getCkup() + "' "; selStr += "
 * from doki,stdoki,artikli,partneri WHERE doki.cskl = stdoki.cskl AND
 * doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god " + "AND doki.brdok =
 * stdoki.brdok AND stdoki.cart = artikli.cart AND doki.cpar = partneri.cpar and
 * doki.god='" + hr.restart.robno.Aut.getAut().getKnjigodRobno() + "' AND
 * doki.vrdok not in
 * ('PON','NDO','NKU','RNL','REV','PRV','OTR','INM','INV','IZD') " + exInClude +
 * artikliFilter + carting + " order by datdok"; System.out.println(selStr);
 * return selStr; }
 */