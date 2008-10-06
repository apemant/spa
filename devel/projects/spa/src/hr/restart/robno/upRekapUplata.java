/****license*****************************************************************
**   file: upRekapUplata.java
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
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class upRekapUplata extends raUpitLite {

  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel mainPanel = new JPanel();
  StorageDataSet fieldSet = new StorageDataSet();
  StorageDataSet sumSet = new StorageDataSet();
  StorageDataSet repSet = new StorageDataSet();
  StorageDataSet repSetPR = new StorageDataSet();

  QueryDataSet firstRepSet;
//  QueryDataSet repSetPoCnacpl;
//  QueryDataSet repSetPoBrRac;


  XYLayout xyMainPanel = new XYLayout();
  JLabel jlSkladiste = new JLabel();
  JLabel jlOrg = new JLabel("Org. jedinica");
  JLabel jlPeriod = new JLabel();
  boolean imaGotovine = false;
  boolean imaCekovine = false;
  boolean imaKarovine = false;

  JraTextField jraPocDat = new JraTextField();
  JraTextField jraZavDat = new JraTextField();

  public JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
      csklOnOff(jlrCskl.getText().equals(""));
      jrfCBlagajnik.requestFocus();
    }
  };
  public JlrNavField jlrNazSkl = new JlrNavField() {
    public void after_lookUp() {
      csklOnOff(jlrCskl.getText().equals(""));
    }
  };
  JraButton jbSelSklad = new JraButton();
  
// TODO handle this carefully

  public JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      corgOnOff(jlrCorg.getText().equals(""));
      jlrCskl.requestFocus();
    }
  };
  public JlrNavField jlrNazOrg = new JlrNavField() {
    public void after_lookUp() {
      corgOnOff(jlrCorg.getText().equals(""));
    }
  };
  JraButton jbSelOrg = new JraButton();
  
  JlrNavField jrfNazivBlagajnika = new JlrNavField(){
    public void after_lookUp() {
      operaterOnOff(jrfCBlagajnik.getText().equals(""));
    }
  };
  JraButton jbSelBlagajnik = new JraButton();
  JlrNavField jrfCBlagajnik = new JlrNavField() {
    public void after_lookUp() {
      operaterOnOff(jlrCskl.getText().equals(""));
      jraPocDat.requestFocus();
    }
  };
  

  raComboBox jcbDokumenti = new raComboBox();
  JLabel jlDoks = new JLabel("Dokumenti");

  public static upRekapUplata uru;

  public static upRekapUplata getInstance(){
    return uru;
  }

  public upRekapUplata() {
    try {
      jbInit();
      uru = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    difolts();
  }
  
  private int minBr, maxBr;

  void makeRepSets(){
    
    minBr=9999999;
    maxBr=0;

    if(sumSet.isOpen()){
      sumSet.deleteAllRows();
    } else{
      sumSet.open();
    }

    if(repSetPR.isOpen()){
      repSetPR.deleteAllRows();
    } else{
      repSetPR.open();
    }

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(firstRepSet);


    firstRepSet.first();
    do{
      
      if (firstRepSet.getInt("BRDOK") < minBr) minBr = firstRepSet.getInt("BRDOK");
      if (firstRepSet.getInt("BRDOK") > maxBr) maxBr = firstRepSet.getInt("BRDOK");
      
      repSetPR.insertRow(false);
      repSetPR.setString("CRACUN", firstRepSet.getString("VRDOK")+"-"+vl.findYear(fieldSet.getTimestamp("DATUMOD"))+"-"+vl.maskZeroInteger(Integer.decode(firstRepSet.getInt("BRDOK")+""),6)); //Integer.ca firstRepSet.getInt("BRDOK"));
      repSetPR.setString("CNACPL", firstRepSet.getString("CNACPL"));
      ld.raLocate(dm.getNacpl(), "CNACPL", firstRepSet.getString("CNACPL"));
      repSetPR.setString("NAZNACPL", dm.getNacpl().getString("NAZNACPL"));
      repSetPR.setString("CBANKA", firstRepSet.getString("CBANKA"));
      repSetPR.setTimestamp("DATUMNP", firstRepSet.getTimestamp("DATUM"));

      if(firstRepSet.getString("CBANKA").equals("")){
        if(ld.raLocate(sumSet, "CNACPL", firstRepSet.getString("CNACPL"))){
          sumSet.setBigDecimal("SUMIRATA", sumSet.getBigDecimal("SUMIRATA").add(firstRepSet.getBigDecimal("IRATA")));
        } else{
          sumSet.insertRow(false);
          sumSet.setString("CNACPL", firstRepSet.getString("CNACPL"));
          sumSet.setString("CBANKA", "");
          sumSet.setBigDecimal("SUMIRATA", firstRepSet.getBigDecimal("IRATA"));
        }
        repSetPR.setBigDecimal("RATA", firstRepSet.getBigDecimal("IRATA"));
      } else{
        if(ld.raLocate(sumSet, new String[]{"CNACPL", "CBANKA"}
                       , new String[]{firstRepSet.getString("CNACPL"), firstRepSet.getString("CBANKA")})){
          sumSet.setBigDecimal("SUMIRATA", sumSet.getBigDecimal("SUMIRATA").add(firstRepSet.getBigDecimal("IRATA")));
        } else{
          sumSet.insertRow(false);
          sumSet.setString("CNACPL", firstRepSet.getString("CNACPL"));
          sumSet.setString("CBANKA", firstRepSet.getString("CBANKA"));
          sumSet.setBigDecimal("SUMIRATA", firstRepSet.getBigDecimal("IRATA"));
        }

        repSetPR.setString("CBANKA", firstRepSet.getString("CBANKA"));
        repSetPR.setBigDecimal("RATA", firstRepSet.getBigDecimal("IRATA"));
        repSetPR.setBigDecimal("UIRAC", firstRepSet.getBigDecimal("UIRAC"));
        ld.raLocate(dm.getNacpl(), "CNACPL", firstRepSet.getString("CNACPL"));

        if(dm.getNacpl().getString("FL_CEK").equals("D")){
          ld.raLocate(dm.getBanke(), "CBANKA", sumSet.getString("CBANKA"));
          repSetPR.setString("NAZBANKA", dm.getBanke().getString("NAZIV"));
        } else if(dm.getNacpl().getString("FL_KARTICA").equals("D")){
          ld.raLocate(dm.getKartice(), "CBANKA", sumSet.getString("CBANKA"));
          repSetPR.setString("NAZBANKA", dm.getKartice().getString("NAZIV"));
        }

      }
    } while(firstRepSet.next());

    sumSet.first();

    BigDecimal ukupno = Aus.zero2;

    StorageDataSet uknpl = new StorageDataSet();

    uknpl.setColumns(new Column[]{
                     dm.createStringColumn("CNACPL", 5),
                     dm.createBigDecimalColumn("UKUNACPL", 2)
    });

    if(uknpl.isOpen()){
      uknpl.deleteAllRows();
    } else{
      uknpl.open();
    }

    do{
      if(!ld.raLocate(uknpl, "CNACPL", sumSet.getString("CNACPL"))){
        uknpl.insertRow(false);
        uknpl.setString("CNACPL", sumSet.getString("CNACPL"));
        uknpl.setBigDecimal("UKUNACPL", sumSet.getBigDecimal("SUMIRATA"));
      } else{
        uknpl.setBigDecimal("UKUNACPL", uknpl.getBigDecimal("UKUNACPL").add(sumSet.getBigDecimal("SUMIRATA")));
      }
    } while(sumSet.next());

    if(repSet.isOpen()){
      repSet.deleteAllRows();
    } else{
      repSet.open();
    }
    sumSet.first();

    do{
      repSet.insertRow(false);
      repSet.setString("CNACPL", sumSet.getString("CNACPL"));
      ld.raLocate(dm.getNacpl(), "CNACPL", sumSet.getString("CNACPL"));
      repSet.setString("NAZNACPL", dm.getNacpl().getString("NAZNACPL"));
      repSet.setString("CBANKA", sumSet.getString("CBANKA"));

      if(dm.getNacpl().getString("FL_CEK").equals("D")){
        ld.raLocate(dm.getBanke(), "CBANKA", sumSet.getString("CBANKA"));
        repSet.setString("NAZBANKA", dm.getBanke().getString("NAZIV"));
        repSet.setBigDecimal("UKUBANKA", sumSet.getBigDecimal("SUMIRATA")); // dodano
      } else if(dm.getNacpl().getString("FL_KARTICA").equals("D")){
        ld.raLocate(dm.getKartice(), "CBANKA", sumSet.getString("CBANKA"));
        repSet.setString("NAZBANKA", dm.getKartice().getString("NAZIV"));
        repSet.setBigDecimal("UKUBANKA", sumSet.getBigDecimal("SUMIRATA")); // dodano
      } else{
        repSet.setString("NAZBANKA", "");
        repSet.setBigDecimal("UKUBANKA", Aus.zero2); // dodano
      }

//      repSet.setBigDecimal("UKUBANKA", sumSet.getBigDecimal("SUMIRATA")); // maknuto
      ld.raLocate(uknpl, "CNACPL", sumSet.getString("CNACPL"));
      repSet.setBigDecimal("UKUNACPL", uknpl.getBigDecimal("UKUNACPL"));
      ukupno = ukupno.add(sumSet.getBigDecimal("SUMIRATA"));

    } while(sumSet.next());

    repSet.first();
    do{
      repSet.setBigDecimal("UKUPNO", ukupno);
    } while(repSet.next());

//    syst.prn(repSetPR);

  }

  public void okPress() {

    String dokumentFilter1 = "";
    String dokumentFilter2 = "";
    String skladisce = "";
    String skladisce2 = "";

    if (!fieldSet.getString("DOKUMENT").equals("")) {
      dokumentFilter1 = "and pos.vrdok='"+fieldSet.getString("DOKUMENT")+"' ";
      dokumentFilter2 = "and doki.vrdok='"+fieldSet.getString("DOKUMENT")+"' ";
    }
    
    if (!fieldSet.getString("CSKL").equals("")) {
      skladisce = "AND rate.cskl='" + fieldSet.getString("CSKL") + "' ";
      skladisce2 = fieldSet.getString("CSKL");
    }

    String qStr="";
    String blagajnik = "";
    
    if (fieldSet.getString("DOKUMENT").equals("GRC") && !fieldSet.getString("CBLAGAJNIK").equals("")){
      blagajnik = "and pos.cblagajnik = '"+fieldSet.getString("CBLAGAJNIK")+"' ";
    }

    String firstQstr = "select pos.vrdok, pos.uirac, rate.brdok, rate.cnacpl, rate.cbanka, rate.irata, rate.datum, rate.datdok from rate, pos " + //pos where vrdok='GOT' and cskl='" + fieldSet.getString("CSKL") + "' " +
                       "where rate.cskl = pos.cskl "+
                       "AND rate.vrdok = pos.vrdok "+
                       "AND rate.god = pos.god "+
                       "AND rate.brdok = pos.brdok "+
//                       "WHERE rate.cnacpl = nacpl.cnacpl "+
                        skladisce + dokumentFilter1 + blagajnik +
                       "and pos.datdok between '" +// WAS "and rate.datdok between '" + // WAS: "and pos.datdok between '" +
                       ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATUMOD")) + "' and '" +
                       ut.getLastSecondOfDay(fieldSet.getTimestamp("DATUMDO"))+"' and "+
                       "rate.DATDOK BETWEEN '"+ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATUMOD"))+"' AND '"+ut.getLastSecondOfDay(fieldSet.getTimestamp("DATUMDO"))+"' "; // + "' order by pos.brdok";

    String secString = "select doki.vrdok, doki.uirac, rate.brdok, rate.cnacpl, rate.cbanka, rate.irata, rate.datum, rate.datdok from rate, doki " + //doki where vrdok='GOT' and cskl='" + fieldSet.getString("CSKL") + "' " +
                       "where rate.cskl = doki.cskl "+
                       "AND rate.vrdok = doki.vrdok "+
                       "AND rate.god = doki.god "+
                       "AND rate.brdok = doki.brdok "+
//                       "WHERE rate.cnacpl = nacpl.cnacpl "+
                       "AND rate.cskl in ('" + skladisce2 + "','" + fieldSet.getString("CORG") + "') " + dokumentFilter2 +
                       "and doki.datdok between '" +// WAS "and rate.datdok between '" + // WAS: "and doki.datdok between '" +
                       ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATUMOD")) + "' and '" +
                       ut.getLastSecondOfDay(fieldSet.getTimestamp("DATUMDO"))+"' and "+
                       "rate.DATDOK BETWEEN '"+ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATUMOD"))+"' AND '"+ut.getLastSecondOfDay(fieldSet.getTimestamp("DATUMDO"))+"' "; // + "' order by rate.brdok";

    if (fieldSet.getString("DOKUMENT").equals("")) qStr = firstQstr + " union all " + secString;
    else if (fieldSet.getString("DOKUMENT").equals("GRN") || fieldSet.getString("DOKUMENT").equals("GOT")) qStr = secString;
    else if (fieldSet.getString("DOKUMENT").equals("GRC")) qStr = firstQstr;



    System.out.println("firstQstr : " +qStr);

    firstRepSet = ut.getNewQueryDataSet(qStr);
//    firstRepSet.setSort(new SortDescriptor(new String[] {"CNACPL"}));

    if (firstRepSet.rowCount() < 1) {
//      jlrCskl.requestFocus();
//      JOptionPane.showMessageDialog(mainPanel,"Nema Podataka koji zadovoljavaju kriterije","Upozorenje",
//                                    JOptionPane.WARNING_MESSAGE);
      setNoDataAndReturnImmediately();
    }

    makeRepSets();
  }

  public boolean Validacija(){
    if (jlrCorg.getText().equals("")){
      jlrCorg.requestFocus();
      javax.swing.JOptionPane.showMessageDialog(
          this,
          "Obvezatan unos CORG",
          "Greška",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
//    if (jlrCskl.getText().equals("")){
//      jlrCskl.requestFocus();
//      javax.swing.JOptionPane.showMessageDialog(
//          this,
//          "Obvezatan unos CSKL",
//          "Greška",
//          javax.swing.JOptionPane.ERROR_MESSAGE);
//      return false;
//    }
    if (!vl.isValidRange(jraPocDat,jraZavDat)) {
      return false;
    }
    return true;
  }

  public void firstESC() {
    if (!fieldSet.getString("CBLAGAJNIK").equals("")) {
      fieldSet.setString("CBLAGAJNIK","");
      jrfCBlagajnik.emptyTextFields();
      rcc.setLabelLaF(jraPocDat, true);
      rcc.setLabelLaF(jraZavDat, true);
      rcc.setLabelLaF(jcbDokumenti, true);
      operaterOnOff(true);
      jrfCBlagajnik.requestFocus();
      
    }else if (!fieldSet.getString("CSKL").equals("")) {
      fieldSet.setString("CSKL", "");
      jlrCskl.emptyTextFields();
      rcc.setLabelLaF(jraPocDat, true);
      rcc.setLabelLaF(jraZavDat, true);
      rcc.setLabelLaF(jcbDokumenti, true);
      operaterOnOff(true);
      csklOnOff(true);
      jlrCskl.requestFocus();
    } else {
      fieldSet.setString("CORG", "");
      jlrCorg.emptyTextFields();
      rcc.setLabelLaF(jraPocDat, true);
      rcc.setLabelLaF(jraZavDat, true);
      rcc.setLabelLaF(jcbDokumenti, true);
      operaterOnOff(true);
      csklOnOff(true);
      corgOnOff(true);
      jlrCorg.requestFocus();
    }
      
  }

  public boolean runFirstESC() {
    if (!fieldSet.getString("CORG").equals("")) return true;
    return false;
  }

  private void jbInit() throws Exception {
    makeStorages();

    jraPocDat.setDataSet(fieldSet);
    jraPocDat.setColumnName("DATUMOD");

    jraZavDat.setDataSet(fieldSet);
    jraZavDat.setColumnName("DATUMDO");

    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazOrg});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setColumnName("CORG");
    jlrCorg.setNavButton(jbSelOrg);

    jlrNazOrg.setSearchMode(1);
    jlrNazOrg.setNavProperties(jlrCorg);
    jlrNazOrg.setColumnName("NAZIV");

    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrCskl.setSearchMode(0);
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSkl});
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setNavButton(jbSelSklad);

    jlrNazSkl.setSearchMode(1);
    jlrNazSkl.setNavProperties(jlrCskl);
    jlrNazSkl.setColumnName("NAZSKL");

    jlSkladiste.setText("Skladište");
    jlPeriod.setText("Datum (od - do)");

    jcbDokumenti.setRaColumn("DOKUMENT");
    jcbDokumenti.setRaDataSet(fieldSet);
    jcbDokumenti.setRaItems(new String[][] {
      {"Svi dokumenti",""},
      {"Gotovinski raèuni","GRN"},
      {"Gotovinski raèuni - otpremnice","GOT"},
      {"Gotovinski raèuni - POS","GRC"}
    });

    jrfCBlagajnik.setRaDataSet(dm.getBlagajnici());
    jrfCBlagajnik.setDataSet(fieldSet);
    jrfCBlagajnik.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazivBlagajnika});
    jrfCBlagajnik.setVisCols(new int[]{0,1});
    jrfCBlagajnik.setColNames(new String[] {"NAZBLAG"});
    jrfCBlagajnik.setColumnName("CBLAGAJNIK");
    jrfCBlagajnik.setNavButton(jbSelBlagajnik);

    jrfNazivBlagajnika.setColumnName("NAZBLAG");
    jrfNazivBlagajnika.setSearchMode(1);
    jrfNazivBlagajnika.setNavProperties(jrfCBlagajnik);


    xyMainPanel.setWidth(590);
    xyMainPanel.setHeight(150);
    this.setJPan(mainPanel);
    mainPanel.setLayout(xyMainPanel);
    
    mainPanel.add(jlrCorg, new XYConstraints(150, 15, 100, -1));
    mainPanel.add(jlrNazOrg,  new XYConstraints(255, 15, 295, -1));
    mainPanel.add(jbSelOrg,   new XYConstraints(555, 15, 21, 21));
    mainPanel.add(jlOrg, new XYConstraints(15, 15, -1, -1));

    mainPanel.add(jlrCskl, new XYConstraints(150, 40, 100, -1));
    mainPanel.add(jlrNazSkl,  new XYConstraints(255, 40, 295, -1));
    mainPanel.add(jbSelSklad,   new XYConstraints(555, 40, 21, 21));
    mainPanel.add(jlSkladiste, new XYConstraints(15, 40, -1, -1));

    mainPanel.add(new JLabel("Operater"), new XYConstraints(15, 65, -1, -1));
    mainPanel.add(jrfCBlagajnik, new XYConstraints(150, 65, 100, -1));
    mainPanel.add(jrfNazivBlagajnika, new XYConstraints(255, 65, 295, -1));
    mainPanel.add(jbSelBlagajnik, new XYConstraints(555, 65, 21, 21));

    mainPanel.add(jraPocDat, new XYConstraints(150, 90,100,-1));
    mainPanel.add(jraZavDat, new XYConstraints(255, 90,100,-1));
    mainPanel.add(jlPeriod, new XYConstraints(15, 90, -1, -1));

    mainPanel.add(jcbDokumenti, new XYConstraints(150, 115, 205,-1));
    mainPanel.add(jlDoks, new XYConstraints(15, 115, -1,-1));

//    this.addReport("hr.restart.robno.repRekapitulacijaUplata", "Rekapitulacija uplata", 2);
//    this.addReport("hr.restart.robno.repRekapitulacijaUplataPoRacunima", "Rekapitulacija uplata po ra\u010Dunima", 2);

    this.addReport("hr.restart.robno.repRekapitulacijaUplata", "hr.restart.robno.repRekapitulacijaUplata", "RekapitulacijaUplata", "Rekapitulacija uplata");
    this.addReport("hr.restart.robno.repRekapitulacijaUplataPoRacunima", "hr.restart.robno.repRekapitulacijaUplataPoRacunima", "RekapitulacijaUplataPoRacunima", "Rekapitulacija uplata po ra\u010Dunima");

    new raDateRange(jraPocDat,jraZavDat);
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return  true;
  }

  void makeStorages(){
    try{
      fieldSet.setColumns(new Column[]{
          				  dm.createStringColumn("CORG", "Organizacijska jedinica", 0),
                          dm.createStringColumn("CSKL", "Šifra skladišta", 0),
                          dm.createStringColumn("CBLAGAJNIK", "Blagajnik", 0),
                          dm.createTimestampColumn("DATUMOD", "Po\u010Detni datum"),
                          dm.createTimestampColumn("DATUMDO", "Završni datum"),
                          dm.createStringColumn("DOKUMENT", "Vrsta dokumenta",3)
      });

      sumSet.setColumns(new Column[]{
                        dm.createStringColumn("CNACPL", 0),
                        dm.createStringColumn("CBANKA", 0),
                        dm.createBigDecimalColumn("SUMIRATA", 2)
      });

      repSet.setColumns(new Column[]{
                        dm.createStringColumn("CNACPL", 0),
                        dm.createStringColumn("NAZNACPL", 0),
                        dm.createStringColumn("CBANKA", 0),
                        dm.createStringColumn("NAZBANKA", 0),
                        dm.createBigDecimalColumn("SUMRATA"),
                        dm.createBigDecimalColumn("UKUBANKA"),
                        dm.createBigDecimalColumn("UKUNACPL"),
                        dm.createBigDecimalColumn("UKUPNO")
      });

      repSetPR.setColumns(new Column[]{
                          dm.createStringColumn("CRACUN", 15),
                          dm.createStringColumn("CNACPL", 0),
                          dm.createStringColumn("NAZNACPL", 0),
                          dm.createStringColumn("CBANKA", 0),
                          dm.createStringColumn("NAZBANKA", 0),
                          dm.createBigDecimalColumn("RATA"),
                          dm.createBigDecimalColumn("UIRAC"),
                          dm.createTimestampColumn("DATUMNP")
      });
    } catch(Exception ex){
      System.err.println("Exception : "+ex.toString());
    }
  }

  void difolts(){
//    fieldSet.setString("CSKL", hr.restart.robno.Util.getUtil().findCSKL());
//    jlrCskl.forceFocLost();
    fieldSet.setTimestamp("DATUMOD", vl.getToday());
    fieldSet.setTimestamp("DATUMDO", vl.getToday());

    jcbDokumenti.setSelectedIndex(0);
    fieldSet.setString("DOKUMENT","");


    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(hr.restart.sisfun.raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());
    jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jlrCorg.forceFocLost();
    if (cskl_corg) {
      jlrCskl.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
      jlrCskl.forceFocLost();
      csklOnOff(false);
      //      jraPocDat.requestFocus();
      jraPocDat.selectAll();
    } else
      jlrCskl.requestFocus();
  }

  void csklOnOff(boolean pauer){
    rcc.setLabelLaF(jlrCskl,pauer);
    rcc.setLabelLaF(jlrNazSkl,pauer);
    rcc.setLabelLaF(jbSelSklad,pauer);
//    if (pauer) jraPocDat.requestFocus();
//    else jraPocDat.requestFocus();
  }

  void corgOnOff(boolean pauer){
    rcc.setLabelLaF(jlrCorg,pauer);
    rcc.setLabelLaF(jlrNazOrg,pauer);
    rcc.setLabelLaF(jbSelOrg,pauer);
//    if (pauer) jraPocDat.requestFocus();
//    else jraPocDat.requestFocus();
  }

  void operaterOnOff(boolean pauer){
    rcc.setLabelLaF(jrfCBlagajnik,pauer);
    rcc.setLabelLaF(jrfNazivBlagajnika,pauer);
    rcc.setLabelLaF(jbSelBlagajnik,pauer);
  }

  public DataSet getRepSet(){
    return repSet;
  }

  public DataSet getRepSetPR(){
    return repSetPR;
  }

  public String getCSKL(){
    return fieldSet.getString("CSKL");
  }

  public String getNAZSKL(){
    ld.raLocate(dm.getSklad(), "CSKL", fieldSet.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }
 
  public String getCOPERATER(){
    return fieldSet.getString("CBLAGAJNIK");
  }
  
  public String getNAZIVOPERATER(){
    return jrfNazivBlagajnika.getText();
  }
  
  public String getPocDat(){
    return hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("DATUMOD"));
  }

  public String getZavDat(){
    return hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(fieldSet.getTimestamp("DATUMDO"));
  }

  public String getMaxBr() {
    return vl.maskZeroInteger(Integer.decode(maxBr+""),6);
  }
  public String getMinBr() {
    return vl.maskZeroInteger(Integer.decode(minBr+""),6);
  }
  public String getVrstaDokumenta(){
    return fieldSet.getString("DOKUMENT");
  }

}
