/****license*****************************************************************
**   file: upPregledTroskova.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raLLFrames;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportDescriptor;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upPregledTroskova extends raUpitLite {
  boolean lTrans = false;
  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  upStanjeNaSkladistu upSNS = upStanjeNaSkladistu.getInstance();
  TableDataSet tds = new TableDataSet();
  QueryDataSet reportSet, reportSetNew;
  Column colDatumPoc = new Column();
  Column colDatumZav = new Column();
  java.sql.Date dateP = null;
  java.sql.Date dateZ = null;

  private static boolean entered;

  XYLayout xYLayout2 = new XYLayout();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setDefParam();
      rpcart.SetDefFocus();
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();
  rapancart rpcart = new rapancart() /*{
    public void () {
    }
  }*/;

  JLabel jlDatumi = new JLabel();
  JPanel jpContainPanel = new JPanel();
  JPanel jpMainPanel = new JPanel();
  static upPregledTroskova prTr;
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();

  JlrNavField jraCORG = new JlrNavField();
  JlrNavField jraNAZORG = new JlrNavField();
  JraButton jraDohvatOGR = new JraButton();

  JlrNavField jraCVRTR = new JlrNavField();
  JlrNavField jraNAZTR = new JlrNavField();
  JraButton jraDohvatTR = new JraButton();

  JraTextField jraNalog = new JraTextField();
  JraCheckBox jcbPoArtiklima = new JraCheckBox();

  static upPregledTroskova instanceOfMe;

  public static upPregledTroskova getInstance(){
    return instanceOfMe;
  }

  public upPregledTroskova() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    rpcskl.setCSKL(hr.restart.robno.Util.getUtil().findCSKL());
    rpcskl.setDisab('S');
    rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
    rpcart.SetDefFocus();
    showDefaultValues();
  }

  public boolean Validacija(){
    if (tds.getTimestamp("pocDatum").after(hr.restart.util.Valid.getValid().findDate(false, 0))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this,"Po\u010Detni datum ve\u0107i od današnjeg !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (tds.getTimestamp("zavDatum").after(hr.restart.util.Valid.getValid().findDate(false, 0))) {
      jtfZavDatum.requestFocus();
      JOptionPane.showConfirmDialog(this,"Završni datum ve\u0107i od današnjeg !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this,"Po\u010Detni datum mora biti manji od završnog !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress() {
    entered = true;
    rcc.EnabDisabAll(jpMainPanel,false);
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String qStr = "";
    String newDateP, newDateZ;

    newDateP = util.getTimestampValue(tds.getTimestamp("pocDatum"), 0);
    newDateZ = util.getTimestampValue(tds.getTimestamp("zavDatum"), 1);

    String fiveC = Cskl() + Cart() + Corg() + Cvrtr() + Cradnal();

    if(!jcbPoArtiklima.isSelected()) {
      qStr ="SELECT max(doki.corg) as corg, max(doki.cskl) as cskl, " +
      		"max(doki.datdok) as datdok, max(doki.vrdok) as vrdok, " +
            "max(doki.cradnal) as cradnal, max(doki.brdok) as brdok, max(stdoki.cart) as cart, " +
            "max(stdoki.nazart) as nazart, " +
            "max(stdoki.jm) as jm, sum(stdoki.kol) as kol, sum(stdoki.iraz) as iraz, max(sklad.nazskl) as nazskl, " +
            "max(orgstruktura.naziv) as nazorg, max(vrtros.naziv) as vrstros " +
            "FROM DOKI,STDOKI,SKLAD,ORGSTRUKTURA,VRTROS " +
            "WHERE DOKI.VRDOK='IZD' " + fiveC +
            " AND " + util.getDoc("DOKI", "STDOKI") +
            " AND DOKI.DATDOK >=" + newDateP +
            " AND DOKI.DATDOK <=" + newDateZ +
            " AND DOKI.CSKL=SKLAD.CSKL AND DOKI.CORG=ORGSTRUKTURA.CORG"+
            " and DOKI.CVRTR=VRTROS.CVRTR"+
            " GROUP BY doki.cradnal, doki.brdok, doki.cskl";
      if(!Cart().equals("")){
        this.killAllReports();
        this.addReport("hr.restart.robno.repPregledTroskova", "hr.restart.robno.repPregledTroskova", "PregledTroskova", "Pregled troškova");
      } else {
        this.killAllReports();
        this.addReport("hr.restart.robno.repPregledTroskova1", "hr.restart.robno.repPregledTroskova", "PregledTroskova1", "Pregled troškova");
      }
    } else {
      if (rpcart.getCGRART().length()==0)
        qStr ="SELECT max(doki.corg) as corg, max(doki.cskl) as cskl, " +
            "max(stdoki.cart) as cart, max(stdoki.cart1) as cart1, max(stdoki.bc) as bc, max(stdoki.nazart) as nazart, " +
            "max(stdoki.jm) as jm, sum(stdoki.kol) as kol, sum(stdoki.iraz) as iraz, max(sklad.nazskl) as nazskl, " +
            "max(orgstruktura.naziv) as nazorg, max(vrtros.naziv) as vrstros " +
            "FROM DOKI,STDOKI,SKLAD,ORGSTRUKTURA,VRTROS " +
            "WHERE DOKI.VRDOK='IZD' " + fiveC +
            " AND " + util.getDoc("DOKI", "STDOKI") +
            " AND DOKI.DATDOK >=" + newDateP +
            " AND DOKI.DATDOK <=" + newDateZ +
            " AND DOKI.CSKL=SKLAD.CSKL AND DOKI.CORG=ORGSTRUKTURA.CORG"+
            " and DOKI.CVRTR=VRTROS.CVRTR"+
            " GROUP BY stdoki.cart";
      else qStr = "SELECT max(doki.corg) as corg, max(doki.cskl) as cskl, " +
            "max(stdoki.cart) as cart, max(stdoki.cart1) as cart1, max(stdoki.bc) as bc, max(stdoki.nazart) as nazart, " +
            "max(stdoki.jm) as jm, sum(stdoki.kol) as kol, sum(stdoki.iraz) as iraz, max(sklad.nazskl) as nazskl, " +
            "max(orgstruktura.naziv) as nazorg, max(vrtros.naziv) as vrstros " +
            "FROM DOKI,STDOKI,SKLAD,ORGSTRUKTURA,VRTROS,ARTIKLI " +
            "WHERE DOKI.VRDOK='IZD' " + fiveC +
            " AND " + util.getDoc("DOKI", "STDOKI") +
            " AND STDOKI.CART = ARTIKLI.CART AND ARTIKLI.CGRART='" +rpcart.getCGRART()+"'" +
            " AND DOKI.DATDOK >=" + newDateP +
            " AND DOKI.DATDOK <=" + newDateZ +
            " AND DOKI.CSKL=SKLAD.CSKL AND DOKI.CORG=ORGSTRUKTURA.CORG"+
            " and DOKI.CVRTR=VRTROS.CVRTR"+
            " GROUP BY stdoki.cart";
      

      this.killAllReports();
      this.addReport("hr.restart.robno.repPregledTroskova2", "hr.restart.robno.repPregledTroskova", "PregledTroskova2", "Pregled troškova");
    }
    System.out.println("qstr " + qStr);
    reportSet = ut.getNewQueryDataSet(qStr);
    makeNewSet(reportSet);
  }
  
  private void makeNewSet(QueryDataSet rset){
    reportSetNew = new QueryDataSet();
    reportSetNew.setColumns(rset.cloneColumns());
    reportSetNew.open();
    rset.first();
    
    lookupData ld = lookupData.getlookupData();
    
    do {
      if (ld.raLocate(reportSetNew,new String[] {"CART","VRSTROS"},new String[] {rset.getInt("CART")+"",rset.getString("VRSTROS")})){
        reportSetNew.setBigDecimal("KOL",reportSetNew.getBigDecimal("KOL").add(rset.getBigDecimal("KOL")));
        reportSetNew.setBigDecimal("IRAZ",reportSetNew.getBigDecimal("IRAZ").add(rset.getBigDecimal("IRAZ")));
      } else {
        reportSetNew.insertRow(false);
        dm.copyDestColumns(rset,reportSetNew);
      }
    } while (rset.next());
    
    sysoutTEST s = new sysoutTEST(false);
    s.prn(reportSetNew);
    
  }

  public void firstESC() {
    if (!lTrans) {
      if (rpcart.getCART().trim().equals("")) {
        rcc.EnabDisabAll(this.rpcart, true);
        rcc.EnabDisabAll(this.jpContainPanel, true);
        showDefaultValues();
        if(!jcbPoArtiklima.isSelected()){
          rcc.EnabDisabAll(this.rpcskl, true);
          rpcskl.setCSKL("");
        }
      }
      else {
        rcc.EnabDisabAll(this.rpcart, true);
        rcc.EnabDisabAll(this.jpContainPanel, true);
        showDefaultValues();
      }
    }
    else {
      rcc.EnabDisabAll(this.rpcart, true);
      rcc.EnabDisabAll(this.jpContainPanel, true);
      showDefaultValues();
      this.cancelPress();
    }
  }

  public boolean runFirstESC() {
    if (!rpcskl.getCSKL().equals("") || entered) {
      if (entered) entered = false;
      return true;
    }
    else {
      return false;
    }
  }

  private void jbInit() throws Exception {
    this.setJPan(jpMainPanel);
    jpContainPanel.setPreferredSize(new Dimension(555, 120));
    jpContainPanel.setLayout(xYLayout2);
    jlDatumi.setText("Datum (od-do)");
    rpcart.setMode(new String("DOH"));
    rpcart.setBorder(null);

    colDatumPoc = dm.createTimestampColumn("pocDatum");
    colDatumZav = dm.createTimestampColumn("zavDatum");

    tds.setColumns(new Column[] {colDatumPoc, colDatumZav});

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    rpcskl.setRaMode('S');

    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jraCORG.setColumnName("CORG");
    jraCORG.setColNames(new String[] {"NAZIV"});
    jraCORG.setTextFields(new javax.swing.text.JTextComponent[] {jraNAZORG});
    jraCORG.setVisCols(new int[]{0,1});
    jraCORG.setSearchMode(0);
    jraCORG.setDataSet(sgQuerys.getSgQuerys().getMjestoTroska());
    jraCORG.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jraCORG.setNavButton(jraDohvatOGR);


    jraNAZORG.setNavProperties(jraCORG);
    jraNAZORG.setColumnName("NAZIV");
    jraNAZORG.setSearchMode(1);

    jraCVRTR.setColumnName("CVRTR");
    jraCVRTR.setColNames(new String[] {"NAZIV"});
    jraCVRTR.setTextFields(new javax.swing.text.JTextComponent[] {jraNAZTR});
    jraCVRTR.setVisCols(new int[]{0,1});
    jraCVRTR.setSearchMode(0);
    jraCVRTR.setRaDataSet(sgQuerys.getSgQuerys().getVrstaNazivTroska());
    jraCVRTR.setDataSet(dm.getVrtros());
    jraCVRTR.setNavButton(jraDohvatTR);

    jraNAZTR.setNavProperties(jraCVRTR);
    jraNAZTR.setColumnName("NAZIV");
    jraNAZTR.setSearchMode(1);
    
    jpMainPanel.setLayout(borderLayout1);
    jLabel2.setText("Mjesto troška");
    jLabel3.setText("Vrsta troška");
    jLabel4.setText("Radni nalog");
    jraDohvatOGR.setText("...");
    jraDohvatTR.setText("...");
    jcbPoArtiklima.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbPoArtiklima.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPoArtiklima.setText("Po artiklima");
    /*jcbPoArtiklima.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPoArtiklima_actionPerformed(e);
      }
    });*/
    jpContainPanel.add(jtfZavDatum, new XYConstraints(260, 80, 105, -1));
    jpContainPanel.add(jlDatumi, new XYConstraints(15, 80, -1, -1));
    jpContainPanel.add(jLabel2, new XYConstraints(15, 5, -1, -1));
    jpContainPanel.add(jLabel3, new XYConstraints(15, 30, -1, -1));
    jpContainPanel.add(jLabel4, new XYConstraints(15, 55, -1, -1));
    jpContainPanel.add(jraNAZORG, new XYConstraints(260, 5, 345, -1));
    jpContainPanel.add(jraNAZTR, new XYConstraints(260, 30, 345, -1));
    jpContainPanel.add(jraNalog, new XYConstraints(150, 55, 105, -1));
    jpContainPanel.add(jtfPocDatum, new XYConstraints(150, 80, 105, -1));
    jpContainPanel.add(jraCORG, new XYConstraints(150, 5, 105, -1));
    jpContainPanel.add(jraCVRTR, new XYConstraints(150, 30, 105, -1));
    jpContainPanel.add(jraDohvatTR, new XYConstraints(610, 30, 21, 21));
    jpContainPanel.add(jraDohvatOGR, new XYConstraints(610, 5, 21, 21));
    jpContainPanel.add(jcbPoArtiklima,       new XYConstraints(505, 55, 100, -1));
    jpMainPanel.add(rpcart, BorderLayout.CENTER);
    jpMainPanel.add(rpcskl, BorderLayout.NORTH);
    jpMainPanel.add(jpContainPanel, BorderLayout.SOUTH);
    raLLFrames.getRaLLFrames().getMsgStartFrame().centerFrame(this,0,getTitle());

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jraCORG.setDataSet(sgQuerys.getSgQuerys().getMjestoTroska());
        jraCORG.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  void showDefaultValues() {
    jcbPoArtiklima.setSelected(false);
    tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    jraCORG.setText("");
    jraCVRTR.setText("");
    jraNAZORG.setText("");
    jraNAZTR.setText("");
    jraNalog.setText("");
    this.rpcskl.setCSKL(hr.restart.sisfun.raUser.getInstance().getDefSklad()); //sgQuerys.getSgQuerys().getDefSklad());
//    rcc.EnabDisabAll(this.rpcskl, false); 
    if (upSNS.nTransData==0) {
      lTrans=false;
      rpcart.setCART();
    } else {
      rpcart.setCART(upSNS.nTransData);
      rpcart.jrfCART.forceFocLost();
      this.okPress();
      lTrans=true;
      upSNS.nTransData=0;
      rpcskl.jrfCSKL.requestFocus();
    }
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    if (reportSet.rowCount()==0) {
      JOptionPane.showConfirmDialog(this,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      firstESC();
      return false;
    }
    if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
        "Prikazati rezultat u tablici?", "Tablièni prikaz",
        JOptionPane.OK_CANCEL_OPTION)) return true;
      
    showTable();
    firstESC();
    return false;
  }
  
  void showTable() {
    frmTableDataView view = new frmTableDataView();
    view.setTitle("Pregled troškova za skladište " + getCskl());
    updateColumn("CORG", null);
    updateColumn("CSKL", null);
    updateColumn("CART", "Šifra");
    updateColumn("CART1", "Oznaka");
    updateColumn("BC", "Barkod");
    updateColumn("NAZART", "Naziv");
    updateColumn("JM", "Jmj");
    updateColumn("NAZSKL", null);
    updateColumn("NAZORG", null);
    updateColumn("VRSTROS", null);
    view.setDataSet(reportSet);
    //view.setSaveName("pregled-troskova");
    view.show();
    view.resizeLater();
  }
  
  void updateColumn(String name, String title) {
    if (reportSet.hasColumn(name) == null) return;
    if (title == null) reportSet.hasColumn(name).setVisible(0);
    else {
      reportSet.hasColumn(name).setCaption(title);
      reportSet.hasColumn(name).setWidth(
          Artikli.getDataModule().getColumn(name).getWidth());
    }
  }

  private String Cskl() {
    if (rpcskl.getCSKL().equals("")) return "";
    return " AND DOKI.CSKL='" + getCskl() + "' ";
  }
  private String Cart() {
    if (rpcart.getCART().equals("")) return "";
    return " AND STDOKI.CART=" + getCart() + " ";
  }
  private String Corg() {
    if (jraCORG.getText().equals("")) return "";
    return " AND DOKI.CORG='" + jraCORG.getText() + "' ";
  }
  private String Cvrtr() {
    if (jraCVRTR.getText().equals("")) return "";
    return " AND DOKI.CVRTR='" + jraCVRTR.getText() + "' ";
  }
  private String Cradnal() {
    if (jraNalog.getText().equals("")) return "";
    return " AND DOKI.CRADNAL='" + jraNalog.getText() + "' ";
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }
  public int getCart() {
    return Integer.parseInt(rpcart.getCART());
  }
  public String getCvrtr() {
    return jraCVRTR.getText();
  }
  public java.sql.Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }
  public java.sql.Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }

  public QueryDataSet getReportQDS(){
    return reportSet;
  }

  void jcbPoArtiklima_actionPerformed(ActionEvent e) {
    if (jcbPoArtiklima.isSelected()) {
      rpcart.jrfCART.setText("");
      rpcart.jrfCART.emptyTextFields();
      rcc.EnabDisabAll(rpcart, false);
    } else {
      rcc.EnabDisabAll(rpcart, true);
    }
  }
}