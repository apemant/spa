/****license*****************************************************************
**   file: ispPOS_Total.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class ispPOS_Total extends raUpitLite { //raFlatIspis {
  static ispPOS_Total iPOS_Total;
//  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.reports.raRunReport RepRun;
  com.borland.dx.sql.dataset.QueryDataSet tempDs;
  com.borland.dx.sql.dataset.QueryDataSet tempAs;
  com.borland.dx.sql.dataset.QueryDataSet reportQDS = new com.borland.dx.sql.dataset.QueryDataSet();
  com.borland.dx.sql.dataset.QueryDataSet artikliReportQDS = new com.borland.dx.sql.dataset.QueryDataSet();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JraCheckBox jCheckBox1 = new JraCheckBox();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocBroj = new JraTextField();
  JraTextField jtfZavBroj = new JraTextField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZBLAG = new JlrNavField();
  JraButton jbCBLAG = new JraButton();
  JlrNavField jrfCBLAG = new JlrNavField();
  JlrNavField jrfNAZOPER = new JlrNavField();
  JraButton jbCOPER = new JraButton();
  JlrNavField jrfCOPER = new JlrNavField();
  TableDataSet tds = new TableDataSet();
  Column column0 = new Column();
  Column column1 = new Column();
  Column column2 = new Column();
  Column column3 = new Column();
  Column column4 = new Column();
  Column column5 = new Column();
  dM dm;
  JraRadioButton jrbDatum = new JraRadioButton();
  JraRadioButton jrbRacun = new JraRadioButton();
  raButtonGroup ispisChoozer = new raButtonGroup();

  HashMap hm;

  public ispPOS_Total() {
    try {
      jbInit();
      iPOS_Total=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public static ispPOS_Total getispPOS_Total() {
    if (iPOS_Total == null) {
      iPOS_Total = new ispPOS_Total();
    }
    return iPOS_Total;
  }

  public void componentShow(){

  }

  java.math.BigDecimal sumaIznosa;

  public com.borland.dx.sql.dataset.QueryDataSet getReportQDS(){
    return reportQDS;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getArtikliReportQDS(){
    String stringQDS ="select max(STPOS.vrdok) as vrdok, max(STPOS.cart) as cart, max(STPOS.cart1) as cart1, " +
                      "max(STPOS.bc) as bc, max(STPOS.nazart) as nazart,max(STPOS.jm) as jm, sum(STPOS.kol) as kol "+
                      "from STPOS, POS "+
                      "WHERE stpos.cskl = pos.cskl "+
                      "AND stpos.vrdok = pos.vrdok "+
                      "AND stpos.god = pos.god "+
                      "AND stpos.brdok = pos.brdok "+
                      this.getCProdmj()+this.getCUser()+ this.getDokBrojRange("POS")+
                      " and cskl='" + jrfCSKL.getText().trim() + "' "+
                      "group by STPOS.CART";

    artikliReportQDS.close();
    artikliReportQDS.closeStatement();
    artikliReportQDS.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),stringQDS));
    artikliReportQDS.open();
    return artikliReportQDS;
  }

  void setSum(){

    String stringQDS = "select max(POS.vrdok) as vrsta, max(NACPL.naznacpl) as naziv, sum(RATE.irata) as iznos, max(NACPL.cnacpl) as npl, '    ' as banka "+
                       "from NACPL, RATE, POS "+
                       "WHERE rate.cskl = pos.cskl "+
                       "AND rate.vrdok = pos.vrdok "+
                       "AND rate.god = pos.god "+
                       "AND rate.brdok = pos.brdok "+
                       "AND pos.cnacpl = nacpl.cnacpl "+ this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
                       " and cskl='" + jrfCSKL.getText().trim() + "' "+
                       "group by RATE.cnacpl ";
/*    "UNION "+
    "select max(POS.vrdok) as vrsta, max(BANKE.naziv) as naziv, sum(RATE.irata) as iznos, max(NACPL.CNACPL) as npl, max(RATE.cbanka) as banka "+
                       "from POS, NACPL, BANKE, RATE "+
                       "WHERE pos.cnacpl = nacpl.cnacpl "+
                       "AND pos.cskl = rate.cskl "+
                       "AND pos.vrdok = rate.vrdok "+
                       "AND pos.god = rate.god "+
                       "AND pos.brdok = rate.brdok "+
                       "AND rate.cnacpl = nacpl.cnacpl "+
                       "AND rate.cbanka = banke.cbanka "+
                       "and NACPL.fl_cek='D' "+ this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
                       " and cskl='" + jrfCSKL.getText().trim() + "' "+
                       "group by RATE.cbanka "+

    "UNION "+
    "select max(POS.vrdok) as vrsta, max(KARTICE.NAZIV) as naziv, sum(RATE.irata) as iznos, max(NACPL.CNACPL) as npl, max(RATE.cbanka) as banka "+
                       "from POS, NACPL, KARTICE, RATE "+
                       "WHERE pos.cnacpl = nacpl.cnacpl "+
                       "AND pos.cskl = rate.cskl "+
                       "AND pos.vrdok = rate.vrdok "+
                       "AND pos.god = rate.god "+
                       "AND pos.brdok = rate.brdok "+
                       "AND rate.cnacpl = nacpl.cnacpl "+
                       "AND rate.cbanka = banke.cbanka "+
                       "and NACPL.fl_kartica='D' "+ this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
                       " and cskl='" + jrfCSKL.getText().trim() + "' "+
                       "group by RATE.cbanka order by 4,5";
*/
//    System.out.println("kriticni string  " + sgQuerys.getSgQuerys().getStringIspPOS_Total_UNION(this.getDokBrojRange("RATE")));
//    String stringQDS = sgQuerys.getSgQuerys().getStringIspPOS_Total_UNION(this.getDokBrojRange("RATE"));
    reportQDS.close();
    reportQDS.closeStatement();
    reportQDS.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),stringQDS));
//    System.out.println("string: " + stringQDS);
    reportQDS.open();
    reportQDS.first();
    sumaIznosa = _Main.nul;
    while(reportQDS.inBounds()) {
      if (reportQDS.getString("BANKA").equals("")) {
        sumaIznosa = sumaIznosa.add(reportQDS.getBigDecimal("IZNOS"));
      }
      reportQDS.next();
    }
//    System.out.println("suma : " + sumaIznosa);
  }

  public java.math.BigDecimal getSum() {
    return sumaIznosa;
  }

  private void jbInit() throws Exception {


    if (!jCheckBox1.isSelected()) this.addReport("hr.restart.robno.repIspPOS_Total", "hr.restart.robno.repIspPOS_Total", "RekapitulacijaUplata",  "Ispis stanja - vrijednosni");
    else this.addReport("hr.restart.robno.repIspPOS_Total_Kolicinski", "Ispis stanja - koli\u010Dinski", 2);
    this.addReport("hr.restart.robno.repTotalPOS", "Ispis stanja - matri\u010Dni", 2);


    dm = hr.restart.baza.dM.getDataModule();
    jp.setLayout(xYLayout1);
    this.setJPan(jp);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_ComponentShown(e);
      }
    });

    column0 = dm.createStringColumn("CSKL",10);
    column1 = dm.createTimestampColumn("pocDatum");
    column2 = dm.createTimestampColumn("zavDatum");
    column3 = dm.createIntColumn("pocBroj");
    column4 = dm.createIntColumn("zavBroj");
    column5 = dm.createStringColumn("IspisRek",0);


    tds.setColumns(new Column[] {column0, column1, column2, column3, column4, column5});

    jLabel1.setText("Skladište");
    jLabel2.setText("Blagajna");
    jLabel3.setText("Operater");
    jCheckBox1.setText("Ispis rekapitulacije po artiklima");
    jCheckBox1.setSelectedDataValue("D");
    jCheckBox1.setUnselectedDataValue("N");
    jCheckBox1.setHorizontalTextPosition(SwingConstants.LEFT);
    jCheckBox1.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    jtfPocBroj.setColumnName("pocBroj");
    jtfPocBroj.setDataSet(tds);
    jtfZavBroj.setColumnName("zavBroj");
    jtfZavBroj.setDataSet(tds);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{2,3});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
    jrfCSKL.setDataSet(tds); //Util.getSkladFromCorg());
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setNavButton(jbCSKL);
    jrfCSKL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfCSKL_focusLost(e);
      }
    });
    jrfCSKL.setNavProperties(null);

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setEditable(false);
    jrfNAZSKL.setSearchMode(1);

    jrfNAZBLAG.setColumnName("NAZPRODMJ");
    jrfNAZBLAG.setSearchMode(1);
    jrfNAZBLAG.setNavProperties(jrfCBLAG);
    jrfNAZBLAG.setEditable(false);

    jrfCBLAG.setRaDataSet(getProdMjZaSklad());
    jrfCBLAG.setDataSet(dm.getProd_mj());
    jrfCBLAG.setNavButton(jbCBLAG);
    jrfCBLAG.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jrfCBLAG_focusGained(e);
      }
    });
    jrfCBLAG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBLAG});
    jrfCBLAG.setVisCols(new int[]{2,3});
    jrfCBLAG.setColNames(new String[] {"NAZPRODMJ"});
    jrfCBLAG.setColumnName("CPRODMJ");
    jrfNAZOPER.setColumnName("NAZIV");
    jrfNAZOPER.setSearchMode(1);
    jrfNAZOPER.setNavProperties(jrfCOPER);
    jrfNAZOPER.setEditable(false);

    jrfCOPER.setRaDataSet(dm.getUseri());
    jrfCOPER.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZOPER});
    jrfCOPER.setVisCols(new int[]{0,1});
    jrfCOPER.setColNames(new String[] {"NAZIV"});
    jrfCOPER.setColumnName("CUSER");
    jrfCOPER.setNavButton(jbCOPER);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(185);
//    jrbDatum.setNextFocusableComponent(jtfPocDatum);
//    jrbDatum.setText();
//    jrbRacun.setNextFocusableComponent(jtfPocBroj);
//    jrbRacun.setText();
    ispisChoozer.add(jrbDatum,"Datum (od-do)");
    ispisChoozer.add(jrbRacun,"Ra\u010Dun (od-do)");
    jrbDatum.setSelected(true);
    jrbDatum.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        radio_actionPerformed(e);
      }
    });
    jrbRacun.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        radio_actionPerformed(e);
      }
    });
    jp.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jp.add(jLabel2, new XYConstraints(15, 45, -1, -1));
//    jp.add(jLabel3, new XYConstraints(15, 70, -1, -1));
    jp.add(jtfPocDatum, new XYConstraints(150, 95, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(260, 95, 100, -1));
    jp.add(jtfPocBroj, new XYConstraints(150, 120, 100, -1));
    jp.add(jtfZavBroj, new XYConstraints(260, 120, 100, -1));
    jp.add(jrfNAZSKL, new XYConstraints(260, 20, 240, -1));
    jp.add(jbCSKL, new XYConstraints(509, 20, 21, 21));
    jp.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZBLAG, new XYConstraints(260, 45, 240, -1));
    jp.add(jrfCBLAG, new XYConstraints(150, 45, 100, -1));
//    jp.add(jrfNAZOPER, new XYConstraints(260, 70, 240, -1));
//    jp.add(jrfCOPER, new XYConstraints(150, 70, 100, -1));
    jp.add(jbCBLAG, new XYConstraints(509, 45, 21, 21));
//    jp.add(jbCOPER, new XYConstraints(509, 70, 21, 21));
    jp.add(jCheckBox1, new XYConstraints(230, 145, 300, -1));
    jp.add(jrbDatum,   new XYConstraints(15, 95, -1, -1));
    jp.add(jrbRacun,   new XYConstraints(15, 120, -1, -1));
    rcc.setLabelLaF(jtfPocBroj,false);
    rcc.setLabelLaF(jtfZavBroj,false);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jrfCSKL.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
        jrfCSKL.setDataSet(tds);
      }
    });
  }

  private String provider;
  private String rTitle;

//  public hr.restart.util.reports.raRunReport getRepRunner() {
//    if (RepRun == null) {
//      RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();
//      RepRun.addReport("hr.restart.robno.repTotalPOS", "Ispis stanja - matri\u010Dni", 2);
//      RepRun.addReport("hr.restart.robno.repIspPOS_Total", "Ispis stanja - vrijednosni", 2);
//      RepRun.addReport("hr.restart.robno.repIspPOS_Total_Kolicinski", "Ispis stanja - koli\u010Dinski", 2);
//    }
//    return RepRun;
//  }

/** @todo odavde izvuc reporte koji vec postoje.... */

  /*public hr.restart.util.reports.raRunReport getRepRunner() {
    if (RepRun != null) RepRun.clearAllReports();
    RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();
//    if (!jCheckBox1.isSelected()) RepRun.addReport("hr.restart.robno.repIspPOS_Total", "Ispis stanja - vrijednosni", 2);
    if (!jCheckBox1.isSelected()) RepRun.addReport("hr.restart.robno.repIspPOS_Total", "hr.restart.robno.repIspPOS_Total", "RekapitulacijaUplata",  "Ispis stanja - vrijednosni");
    else RepRun.addReport("hr.restart.robno.repIspPOS_Total_Kolicinski", "Ispis stanja - koli\u010Dinski", 2);
//    else RepRun.addReport("hr.restart.robno.repIspPOS_Total_Kolicinski", "hr.restart.robno.repIspPOS_Total_Kolicinski", "IspPOS_Total_Kolicinski", "Ispis stanja - koli\u010Dinski");
    RepRun.addReport("hr.restart.robno.repTotalPOS", "Ispis stanja - matri\u010Dni", 2);
    return RepRun;
  }*/



//  private void ispis(){
//    getRepRunner().go();
//  }

  public boolean Validacija(){
//    this.getDokBrojRange();
    if(vl.isEmpty(jrfCSKL)){
      JOptionPane.showConfirmDialog(null, "Obvezatan unos - SKLADIŠTE !", "Greška", JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if((!vl.isEmpty(jtfPocDatum)||!vl.isEmpty(jtfZavDatum))&&jrbDatum.isSelected()){
//      System.out.println("Oba datuma su puni");
      if(tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))){
        jtfZavDatum.requestFocus();
        JOptionPane.showConfirmDialog(null, "Završni datum veæi od poèetnog !", "Greška", JOptionPane.DEFAULT_OPTION,
                                      JOptionPane.ERROR_MESSAGE);
        return false;
      }
      tds.setInt("pocBroj",
                 util.findNumFromDate("POS", vl.findYear(), jrfCSKL.getText(), tds.getTimestamp("pocDatum"),
                                      util.NUM_FIRST));
      tds.setInt("zavBroj",
                 util.findNumFromDate("POS", vl.findYear(), jrfCSKL.getText(), tds.getTimestamp("zavDatum"),
                                      util.NUM_LAST));
      if(tds.getInt("pocBroj")==0||tds.getInt("zavBroj")==0){
        jtfPocBroj.requestFocus();
        JOptionPane.showConfirmDialog(null, "Nema podataka za prikaz !", "Greška", JOptionPane.DEFAULT_OPTION,
                                      JOptionPane.ERROR_MESSAGE);
        tds.setInt("pocBroj", 0);
        tds.setInt("zavBroj", 0);
        return false;
      }
    } else if((!vl.isEmpty(jtfPocDatum)||vl.isEmpty(jtfZavDatum))&&jrbDatum.isSelected()){
      tds.setTimestamp("zavDatum", tds.getTimestamp("pocDatum"));
      tds.setInt("pocBroj",
                 util.findNumFromDate("POS", vl.findYear(), jrfCSKL.getText(), tds.getTimestamp("pocDatum"),
                                      util.NUM_FIRST));
      tds.setInt("zavBroj",
                 util.findNumFromDate("POS", vl.findYear(), jrfCSKL.getText(), tds.getTimestamp("zavDatum"),
                                      util.NUM_LAST));
    } else if(jrbRacun.isSelected()){
      tds.setInt("pocBroj", java.lang.Integer.parseInt(jtfPocBroj.getText()));
      tds.setInt("zavBroj", java.lang.Integer.parseInt(jtfZavBroj.getText()));
    }
    return true;
  }


  public void okPress() {
      String qStr;

      qStr="select max(NACPL.naznacpl) as naziv, sum(RATE.irata) as iznos, max(NACPL.cnacpl) as npl, '    ' as banka "+
           "from NACPL, RATE, POS "+
           "WHERE rate.cskl = pos.cskl "+
           "AND rate.vrdok = pos.vrdok "+
           "AND rate.god = pos.god "+
           "AND rate.brdok = pos.brdok "+
           "AND pos.cnacpl = nacpl.cnacpl "+ this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
           " and cskl='" + jrfCSKL.getText().trim() + "' "+
           "group by RATE.cnacpl ";

/*      "UNION "+

      "select max(BANKE.naziv) as naziv, sum(RATE.irata) as iznos, max(NACPL.CNACPL) as npl, max(RATE.cbanka) as banka "+
           "from NACPL, BANKE, RATE, POS "+
           "WHERE pos.cnacpl = nacpl.cnacpl "+
           "AND pos.cskl = rate.cskl "+
           "AND pos.vrdok = rate.vrdok "+
           "AND pos.god = rate.god "+
           "AND pos.brdok = rate.brdok "+
           "AND rate.cnacpl = nacpl.cnacpl "+
           "AND rate.cbanka = banke.cbanka "+
           "and NACPL.fl_cek='D' " + this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
           " and cskl='" + jrfCSKL.getText().trim() + "' "+
           "group by RATE.cbanka "+

      "UNION "+

      "select max(KARTICE.NAZIV) as naziv, sum(RATE.irata) as iznos, max(NACPL.CNACPL) as npl, max(RATE.cbanka) as banka "+
           "from NACPL, KARTICE, RATE, POS "+
           "WHERE pos.cnacpl = nacpl.cnacpl "+
           "AND pos.cskl = rate.cskl "+
           "AND pos.vrdok = rate.vrdok "+
           "AND pos.god = rate.god "+
           "AND pos.brdok = rate.brdok "+
           "AND rate.cnacpl = nacpl.cnacpl "+
           "AND rate.cbanka = banke.cbanka "+
           "and NACPL.fl_kartica='D' " + this.getCProdmj()+this.getCUser()+this.getDokBrojRange("RATE")+
           " and cskl='" + jrfCSKL.getText().trim() + "' "+
           "group by RATE.cbanka order by 3,4";
*/
      System.out.println("qStr okpress : " + qStr);

      vl.execSQL(qStr);
      vl.RezSet.open();

//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(vl.RezSet);

      hm = new HashMap();
      for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
        hm.put(vl.RezSet.getString("NPL"), vl.RezSet.getBigDecimal("IZNOS"));
      }


      if (!vl.RezSet.isEmpty()) {
        tempDs=vl.RezSet;
        qStr="select max(STPOS.cart), max(STPOS.nazart), sum(STPOS.kol) "+
             "from STPOS, POS "+
             "WHERE stpos.cskl = pos.cskl "+
             "AND stpos.vrdok = pos.vrdok "+
             "AND stpos.god = pos.god "+
             "AND stpos.brdok = pos.brdok "+
             this.getCProdmj()+this.getCUser()+ this.getDokBrojRange("POS")+
             " and cskl='" + jrfCSKL.getText().trim() + "' "+
             "group by STPOS.CART";
        vl.execSQL(qStr);
//        tempAs.setColumns(vl.RezSet.cloneColumns());
//        vl.RezSet.open();
        tempAs=vl.RezSet;
        setSum();
//        ispis();
      } else {
//        System.out.println("nema podataka.....");
      }
  }



  public void cancelPress() {
    if (runFirstESC()){
      rcc.setLabelLaF(jrfCSKL,true);
      rcc.setLabelLaF(jrfNAZSKL,true);
      rcc.setLabelLaF(jbCSKL,true);
      jrfCSKL.setText("");
      jrfNAZSKL.setText("");
      jrfCSKL.requestFocus();
    } else this.setVisible(false);
  }

  public void firstESC(){

  }

  public boolean runFirstESC(){
    if (!jrfCSKL.getText().equals("")) return true;
    else return false;
  }

  void findMpSkl() {
    if (hr.restart.robno.Util.getUtil().getMPSklDataset().rowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema definiranog maloprodajnog skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.setVisible(false);
    }
    else { //if (hr.restart.robno.Util.getUtil().getMPSklDataset().rowCount()==1) {
      jrfCSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
      jrfNAZSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      jrfCBLAG.requestFocus();
    }
//    else {
//      jrfCSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
//      jrfNAZSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
//      rcc.setLabelLaF(jrfCSKL,true);
//      rcc.setLabelLaF(jrfNAZSKL,true);
//      rcc.setLabelLaF(jbCSKL,true);
//      jrfCSKL.requestFocus();
//    }
  }
  void this_ComponentShown(ComponentEvent e) {
    tds.open();
    tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    jrfCBLAG.setText("");
    jrfNAZBLAG.setText("");
    findMpSkl();
    jrfCBLAG.requestFocus();
  }
  void radio_actionPerformed(ActionEvent e) {
//    if(jrbDatum.isSelected()){
      rcc.setLabelLaF(jtfPocDatum,jrbDatum.isSelected());
      rcc.setLabelLaF(jtfZavDatum,jrbDatum.isSelected());
      rcc.setLabelLaF(jtfPocBroj,!jrbDatum.isSelected());
      rcc.setLabelLaF(jtfZavBroj,!jrbDatum.isSelected());
//      jtfPocDatum.requestFocus();
//    } else {
//      rcc.setLabelLaF(jtfPocDatum,false);
//      rcc.setLabelLaF(jtfZavDatum,false);
//      rcc.setLabelLaF(jtfPocBroj,true);
//      rcc.setLabelLaF(jtfZavBroj,true);
//      jtfPocBroj.requestFocus();
//    }
  }
  public boolean getRedOdDoDatuma(){
    return jrbDatum.isSelected();
  }
  public HashMap getHM(){
    return hm;
  }
  String getDokBrojRange(String baza) {
    return " and "+baza+".brdok>="+tds.getInt("pocBroj")+" and "+baza+".brdok<="+tds.getInt("zavBroj")+" ";
  }
  String getCProdmj(){
    if(!jrfCBLAG.getText().trim().equals("")) return " and pos.cprodmj='" + jrfCBLAG.getText().trim() + "'";
    else return "";
  }
  String getCUser(){
    if(!jrfCOPER.getText().trim().equals("")) return " and pos.cuser='" + jrfCOPER.getText().trim() + "'";
    else return "";
  }

  public com.borland.dx.sql.dataset.QueryDataSet getProdMjZaSklad(){
    String stringQDS = "select * from prod_mj";
//    System.out.println("stringQDS: " + stringQDS);
    com.borland.dx.sql.dataset.QueryDataSet prodMjZaSklad = new com.borland.dx.sql.dataset.QueryDataSet();
    prodMjZaSklad.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),stringQDS));
    prodMjZaSklad.open();
    return prodMjZaSklad;
  }

  void jrfCSKL_focusLost(FocusEvent e) {
    if(!jrfCSKL.getText().trim().equals("")){
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      jrfCBLAG.requestFocus();
    }
  }

  void jrfCBLAG_focusGained(FocusEvent e) {
    if(jrfCSKL.getText().trim().equals("")){
      rcc.setLabelLaF(jrfCSKL,true);
      rcc.setLabelLaF(jrfNAZSKL,true);
      rcc.setLabelLaF(jbCSKL,true);
    }
  }
}
