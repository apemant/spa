/****license*****************************************************************
**   file: frmBrBilancaPer.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.dlgCompanyTree;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raUpit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmBrBilancaPer extends raUpit {
  StorageDataSet stds = new StorageDataSet();
//  JLabel jlKontoBroj = new JLabel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JPanel mainPanel = new JPanel();
  StorageDataSet ojs;
  boolean okpressed = false;
  hr.restart.robno.Util rutil = hr.restart.robno.Util.getUtil();
  QueryDataSet storageQDS;
  QueryDataSet tempDS = new QueryDataSet();
  XYLayout xYLayout4 = new XYLayout();

  raPanKonto kontoPanel = new raPanKonto(); // ("B_NO");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();

  raComboBox rcbPrivremenost = new raComboBox();
  raComboBox rcbPripOrgJed = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      updateSelectTreeButton();
    }
  };
  raComboBox rcbIspis = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      updateSelectTreeButton();
    }
  };
  raComboBox rcbBilanca = new raComboBox();
  
  JraButton selOrg = new JraButton();

  QueryDataSet beforeRepDS2 = new QueryDataSet();
  public static QueryDataSet repDS2;
  public QueryDataSet repSet;
  public QueryDataSet repSetNalozi = null;
  Column colMjP = new Column();
  Column colMjZ = new Column();
  Column colGod = new Column();
  Column colSaldo = new Column();
  Column colPocID = new Column();
  Column colPocIP = new Column();
  Column colNK = new Column();
  XYLayout layDetail = new XYLayout();
  JLabel jlPeriod = new JLabel();
  private String knjigDifolt;
  public static Timestamp datumP;
  public static Timestamp datumZ;
  protected String corgRemember = "";
  boolean doubleClicked = false;
  boolean entered = false;
  boolean instance = false;
  LinkedList ll = new LinkedList();
  LinkedList pl = new LinkedList();
  String razdoblje, corgSS, privremeni, razdoblje1, corgSS1;
  dM dm = dM.getDataModule();

  JraTextField jtGodina = new JraTextField();

  static frmBrBilancaPer fbb;
  JLabel jLabel2 = new JLabel();
  Border border1;
  Border border3;
  XYLayout xYLayout1 = new XYLayout();

  public frmBrBilancaPer() {
    try {
      jbInit();
      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public frmBrBilancaPer(String dva) {
    try {
      jbInit();
      setHightInstance();
      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void setHightInstance() {
    mainPanel.setMinimumSize(new Dimension(600, 358));
    mainPanel.setPreferredSize(new Dimension(580, 190));
  }

  public frmBrBilancaPer(int tri) {
    this.instance = true;
    try {
      jbInit();
      fbb = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmBrBilancaPer getFrmBrBilancaPer() {
    if (fbb == null) {
      fbb = new frmBrBilancaPer();
    }
    return fbb;
  }
  
  boolean isTreeSelected() {
    return stds.getString("ISPIS").equals("0") && stds.getString("ORGSTR").equals("0");
  }
  
  protected void updateSelectTreeButton() {
    selOrg.setEnabled(isTreeSelected());
  }

  public void componentShow() {
    initialValues();
    updateSelectTreeButton();
  }

  public boolean runFirstESC() {
    if (entered) {
      return true;
    } else if (!kontoPanel.jlrKontoBroj.getText().equals("")){
      return true;
    }
    return false;
  }

  public void firstESC() {
    if (ll.isEmpty()) {
      if (entered){
        doubleClicked = false;
        kontoPanel.setNoLookup(false);
        entered = false;
        showDefaultValues();
      } else if (!kontoPanel.jlrKontoBroj.getText().equals("")) {
//        initialValues();
        this.getJPTV().setDataSet(null);
        kontoPanel.jlrKontoBroj.setText("");
        kontoPanel.jlrKontoBroj.emptyTextFields();
      }
    } else {
      llHandler();
      changeIcon(1);
    }
  }
  
  public void cancelPress() {
    doubleClicked = false;
    kontoPanel.setNoLookup(false);
    if (!ll.isEmpty()) {
      ll.clear();
    }
    rcc.EnabDisabAll(mainPanel, true);
    this.getJPTV().setDataSet(null);
    //showDefaultValues();
    super.cancelPress();
  }

  public boolean Validacija() {
    if (kontoPanel.jlrCorg.getText().equals("")) {
      kontoPanel.jlrCorg.requestFocus();
      JOptionPane.showConfirmDialog(null, "Unesite organizacijsku jedinicu !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!vl.isValidRange(jtfPocDatum, jtfZavDatum)) {
      return false;
    }
    return true;
  }

  QueryDataSet qds;

  public QueryDataSet getBrutoBilQDS() {
    return repSet;
  }

  public QueryDataSet getBrutoBilQDSNalozi() {
    return repSetNalozi;
  }

  public String getGroupMode(){
    return stds.getString("ISPIS");
  }

  public QueryDataSet getRekapQDS() {
    return repDS2;
  }

  BigDecimal[] kumulativi = new BigDecimal[5];

  public BigDecimal[] getKumulative(){
    return kumulativi;
  }
  
  private String knjigovodstvo;

  public void okPress() {
//    System.out.println("START - 0 ms"); //XDEBUG delete when no more needed
    long tajm = System.currentTimeMillis();
    
    datumP = stds.getTimestamp("pocDatum");
    datumZ = stds.getTimestamp("zavDatum");
    corgRemember = kontoPanel.getCorg();
    knjigovodstvo = hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(kontoPanel.getCorg());
    entered = true;
    
    String gkstavkerad = "gkstavkerad.corg";
    String gkstavke = "gkstavke.corg";
    String groupByst = "gkstavke.corg,";
    String groupBystrad = "gkstavkerad.corg,";
    if (stds.getString("ISPIS").equals("1")) {
      gkstavkerad = "max('"+kontoPanel.getCorg()+"')";//knjigovodstvo+"'";
      gkstavke = "max('"+kontoPanel.getCorg()+"')";//knjigovodstvo+"'";
      groupByst = "";
      groupBystrad = "";
    }
    
    /** @todo zrelo za optimizaciju, ali cu pricekat response od korisnika */
    String qStr = "select "+gkstavke+" as realcorg, gkstavke.brojkonta as brojkonta, sum(gkstavke.id) as id, "+
                  "sum(gkstavke.ip) as ip, gkstavke.cvrnal as cvrnal "+
                  "from gkstavke where gkstavke.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavke")+" and "+
//                  "gkstavke.brojkonta = konta.brojkonta and "+
//                  "gkstavke.brojkonta = konta.brojkonta and konta.aktiv='D' "+/*and konta.ispisbb='D' */"and "+
                  "gkstavke.brojkonta like '"+getBrKon()+"%' "+
                  "and gkstavke."+getRange()+" "+
                  "group by "+groupByst+" gkstavke.brojkonta, gkstavke.cvrnal";

//                  datumknj >='" + new java.sql.Date(datumP.getTime()) +
//                  "' and gkstavke.datumknj <'" + new java.sql.Date(Util.getUtil().addDays(datumZ, 1).getTime())+"' ";
    if (stds.getString("PRIVREMENO").equals("1")){ // jcbPrivremeni.isSelected()){
      qStr +=" UNION ALL "+
             "select "+gkstavkerad+" as realcorg, gkstavkerad.brojkonta as brojkonta, sum(gkstavkerad.id) as id, "+
             "sum(gkstavkerad.ip) as ip, gkstavkerad.cvrnal as cvrnal  "+
             "from gkstavkerad where gkstavkerad.knjig='"+knjigovodstvo+"' and "+getCorgs("gkstavkerad")+" and "+
//             "gkstavkerad.brojkonta = konta.brojkonta and "+
//             "gkstavkerad.brojkonta = konta.brojkonta and konta.aktiv='D' "+/*and konta.ispisbb='D' */"and "+
             "gkstavkerad.brojkonta like '"+getBrKon()+"%' "+
             "and gkstavkerad."+getRange()+" "+
             "group by "+groupBystrad+" gkstavkerad.brojkonta, gkstavkerad.cvrnal";

//             datumknj >='" + new java.sql.Date(datumP.getTime()) +
//             "' and gkstavkerad.datumknj <'" + new java.sql.Date(Util.getUtil().addDays(datumZ, 1).getTime())+"' ";
    }
    
//    System.out.println("\n-----\n"+qStr+"\n-----\n");

    qds = util.getNewQueryDataSet(qStr); 
    
    if (qds.isEmpty()) setNoDataAndReturnImmediately();

    QueryDataSet jptvov = getValutaSet(deriveNumber(qds,false));
    
    if (!datumP.equals(util.getFirstDayOfYear(datumP))){
      System.out.println("Nije prvi dan u godini");
      jptvov.getColumn("POCID").setVisible(0);
      jptvov.getColumn("POCIP").setVisible(0);
    }
    
    if (!doubleClicked){
      qds.close();
      qds.addColumn((Column)colNK.clone());
      qds.addColumn((Column) colPocID.clone());
      qds.addColumn((Column) colPocIP.clone());
      qds.addColumn((Column) colSaldo.clone());
      qds.open();
      

      repSet = getValutaSet(makeSetForNewIspis(qds));//makeSomethingUsefull(qds));
      repDS2 = jptvov;
      repSetNalozi = getValutaSet(makeSetIspisPoNalozima(qds));

//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//      st.showInFrame(repSetNalozi,"Dataset Test Frame");

      BigDecimal nulica = Aus.zero2;
      kumulativi[0] = nulica;
      kumulativi[1] = nulica;
      kumulativi[2] = nulica;
      kumulativi[3] = nulica;
      kumulativi[4] = nulica;

      repDS2.first();
      do {
        checkClosing();
        kumulativi[0] = kumulativi[0].add(repDS2.getBigDecimal("POCID"));
        kumulativi[1] = kumulativi[1].add(repDS2.getBigDecimal("POCIP"));
        kumulativi[2] = kumulativi[2].add(repDS2.getBigDecimal("ID"));
        kumulativi[3] = kumulativi[3].add(repDS2.getBigDecimal("IP"));
      } while (repDS2.next());
      kumulativi[4] = (kumulativi[0].subtract(kumulativi[1])).add(kumulativi[2].subtract(kumulativi[3]));

      killAllReports();
      addingReport(kontoPanel.jlrKontoBroj.getText().equals(""));
    }
      this.getJPTV().enableEvents(false);
      jptvov.first();
      this.getJPTV().enableEvents(true);
      this.getJPTV().setDataSetAndSums(jptvov, new String[] {"POCID","POCIP","ID","IP","SALDO"});

//      System.out.println("START - "+(tajm - System.currentTimeMillis())+" ms"); //XDEBUG delete when no more needed
  }

  public QueryDataSet getValutaSet(QueryDataSet olDS){
    return olDS;
  }
  
  private QueryDataSet makeSetIspisPoNalozima(QueryDataSet razvoj){
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(razvoj,"Dataset Test Frame");

    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setColumns(razvoj.cloneColumns());
    try {
      if (razvijeni.getColumn("CORG") == null)
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
      } catch (Exception e) {
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
//        e.printStackTrace();// TODO: handle exception
      }
    razvijeni.open();
    razvoj.first();
    
    Map trans = null;
    if (isTreeSelected())
      trans = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {
      
//      if (stds.getString("ISPIS").equals("1")) {
//        razvoj.setString("CORG",knjigovodstvo);
//      }
      String realcorg = razvoj.getString("REALCORG");
      if (trans != null && trans.containsKey(realcorg)){
        realcorg = (String) trans.get(realcorg);
      }
      
      if (!lookupData.getlookupData().raLocate(
          razvijeni,
          new String[] {"BROJKONTA","CORG","CVRNAL"},
          new String[] {razvoj.getString("BROJKONTA"), realcorg,razvoj.getString("CVRNAL")})
          ){
        razvijeni.insertRow(false);
        razvijeni.setString("CORG", realcorg);
        razvijeni.setString("REALCORG", realcorg);
        try {
        razvijeni.setString("GOD", razvoj.getString("GOD"));
        } catch (Exception e) {
        }
        razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
        razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA")));
        razvijeni.setString("CVRNAL", razvoj.getString("CVRNAL"));
        razvijeni.setBigDecimal("POCID", Aus.zero2);
        razvijeni.setBigDecimal("POCIP", Aus.zero2);
        razvijeni.setBigDecimal("ID", razvoj.getBigDecimal("ID"));
        razvijeni.setBigDecimal("IP", razvoj.getBigDecimal("IP"));
      } else {
        razvijeni.setBigDecimal("ID", razvijeni.getBigDecimal("ID").add(razvoj.getBigDecimal("ID")));
        razvijeni.setBigDecimal("IP", razvijeni.getBigDecimal("IP").add(razvoj.getBigDecimal("IP")));
      }
      razvijeni.setBigDecimal("SALDO", razvijeni.getBigDecimal("SALDO").add((razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID"))).subtract(razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP")))));
    } while (razvoj.next());
    return razvijeni;
  }

  private QueryDataSet makeSetForNewIspis(QueryDataSet razvoj){

    QueryDataSet razvijeni = new QueryDataSet();
    razvijeni.setColumns(razvoj.cloneColumns());
    try {
      if (razvijeni.getColumn("CORG") == null)
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
      } catch (Exception e) {
        razvijeni.addColumn((Column) dm.getGkkumulativi().getColumn("CORG").clone());
//        e.printStackTrace();// TODO: handle exception
      }
    razvijeni.open();
    razvoj.first();
    
    Map trans = null;
    if (isTreeSelected())
      trans = dlgCompanyTree.get("bruto-bilanca").getTransitions(kontoPanel.getCorg());

    do {
      
//      if (stds.getString("ISPIS").equals("1")) {
//        razvoj.setString("CORG",knjigovodstvo);
//      }
      String realcorg = razvoj.getString("REALCORG");
      if (trans != null && trans.containsKey(realcorg))
        realcorg = (String) trans.get(realcorg);
      
      if (!lookupData.getlookupData().raLocate(
          razvijeni,
          new String[] {"BROJKONTA","CORG"},
          new String[] {razvoj.getString("BROJKONTA"), realcorg})
          ){
        razvijeni.insertRow(false);
        razvijeni.setString("CORG", realcorg);
        razvijeni.setString("BROJKONTA", razvoj.getString("BROJKONTA"));
        razvijeni.setString("NK", getNazKOnto(razvoj.getString("BROJKONTA")));
        razvijeni.setString("CVRNAL", razvoj.getString("CVRNAL"));
        if (!razvoj.getString("CVRNAL").equals("00")){
          razvijeni.setBigDecimal("POCID",Aus.zero2);
          razvijeni.setBigDecimal("POCIP",Aus.zero2);
          razvijeni.setBigDecimal("ID",razvoj.getBigDecimal("ID"));
          razvijeni.setBigDecimal("IP",razvoj.getBigDecimal("IP"));
        } else {
          razvijeni.setBigDecimal("POCID",razvoj.getBigDecimal("ID"));
          razvijeni.setBigDecimal("POCIP",razvoj.getBigDecimal("IP"));
          razvijeni.setBigDecimal("ID",Aus.zero2);
          razvijeni.setBigDecimal("IP",Aus.zero2);
        }
      } else {
        if (!razvoj.getString("CVRNAL").equals("00")){
          razvijeni.setBigDecimal("ID",razvijeni.getBigDecimal("ID").add(razvoj.getBigDecimal("ID")));
          razvijeni.setBigDecimal("IP",razvijeni.getBigDecimal("IP").add(razvoj.getBigDecimal("IP")));
        } else {
          razvijeni.setBigDecimal("POCID",razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID")));
          razvijeni.setBigDecimal("POCIP",razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP")));
        }
      }
      razvijeni.setBigDecimal("SALDO", razvijeni.getBigDecimal("SALDO").add((razvijeni.getBigDecimal("POCID").add(razvoj.getBigDecimal("ID"))).subtract(razvijeni.getBigDecimal("POCIP").add(razvoj.getBigDecimal("IP")))));
    } while (razvoj.next());
    return razvijeni;
  }

  private QueryDataSet deriveNumber(QueryDataSet original, boolean crgs){
    QueryDataSet firstStep = new QueryDataSet();
    firstStep.setColumns(new Column[] {
      (Column) dm.getGkstavke().getColumn("CORG").clone(),
      (Column) dm.getGkstavke().getColumn("BROJKONTA").clone(),
      (Column) colNK.clone(),
      (Column) colPocID.clone(),
      (Column) colPocIP.clone(),
      dm.createBigDecimalColumn("ID","Duguje",2),
      dm.createBigDecimalColumn("IP","Potrauje",2),
      (Column) colSaldo.clone(),
      (Column) dm.getGkstavke().getColumn("CVRNAL").clone()
    });
    firstStep.open();
    int position = kontoPanel.getBrKonLength()+1;
    if (position == 4) position = 15;
    original.first();
    do {
      firstStep.insertRow(false);
      firstStep.setString("CORG",original.getString("REALCORG"));
      try {
        firstStep.setString("BROJKONTA",original.getString("BROJKONTA").substring(0,position));
      }
      catch (Exception ex) {
        firstStep.setString("BROJKONTA",original.getString("BROJKONTA"));
      }
      firstStep.setString("NK",getNazKOnto(original.getString("BROJKONTA"))); // was orginal.getString"BROJK....
      firstStep.setString("CVRNAL",original.getString("CVRNAL"));

        firstStep.setBigDecimal("ID",original.getBigDecimal("ID"));
        firstStep.setBigDecimal("IP",original.getBigDecimal("IP"));
        firstStep.setBigDecimal("SALDO",original.getBigDecimal("ID").subtract(original.getBigDecimal("IP")));
    } while (original.next());
    return setPocAndProm(firstStep,crgs);
  }

  private QueryDataSet setPocAndProm(QueryDataSet nebr, boolean crgs){
    nebr.first();
    do {
      if (!nebr.getString("CVRNAL").equals("00")){
        nebr.setBigDecimal("POCID",Aus.zero2);
        nebr.setBigDecimal("POCIP",Aus.zero2);
      } else {
        nebr.setBigDecimal("POCID",nebr.getBigDecimal("ID"));
        nebr.setBigDecimal("POCIP",nebr.getBigDecimal("IP"));
        nebr.setBigDecimal("ID",Aus.zero2);
        nebr.setBigDecimal("IP",Aus.zero2);
      }
    } while (nebr.next());
      return sumSame(nebr,crgs);
  }

  private QueryDataSet sumSame(QueryDataSet differenses, boolean crgs){
    QueryDataSet sumamed = new QueryDataSet();
    sumamed.setColumns(differenses.cloneColumns());
    sumamed.setRowId("BROJKONTA",true);
    sumamed.getColumn("CORG").setVisible(0);
    sumamed.getColumn("CVRNAL").setVisible(0);
    sumamed.getColumn("NK").setVisible(0);
    sumamed.open();
    differenses.first();

    if (crgs){
      do {
        if (lookupData.getlookupData().raLocate(sumamed,new String[] {"BROJKONTA","CORG"},
            new String[] {differenses.getString("BROJKONTA"),differenses.getString("CORG")})){
          sumamed.setBigDecimal("POCID",sumamed.getBigDecimal("POCID").add(differenses.getBigDecimal("POCID")));
          sumamed.setBigDecimal("POCIP",sumamed.getBigDecimal("POCIP").add(differenses.getBigDecimal("POCIP")));
          sumamed.setBigDecimal("ID",sumamed.getBigDecimal("ID").add(differenses.getBigDecimal("ID")));
          sumamed.setBigDecimal("IP",sumamed.getBigDecimal("IP").add(differenses.getBigDecimal("IP")));
          sumamed.setBigDecimal("SALDO",sumamed.getBigDecimal("SALDO").add(differenses.getBigDecimal("SALDO")));
        } else {
          sumamed.insertRow(false);
          differenses.copyTo(sumamed);
        }
      } while (differenses.next());
    } else {
      do {
        if (lookupData.getlookupData().raLocate(sumamed,"BROJKONTA",differenses.getString("BROJKONTA"))){
          sumamed.setBigDecimal("POCID",sumamed.getBigDecimal("POCID").add(differenses.getBigDecimal("POCID")));
          sumamed.setBigDecimal("POCIP",sumamed.getBigDecimal("POCIP").add(differenses.getBigDecimal("POCIP")));
          sumamed.setBigDecimal("ID",sumamed.getBigDecimal("ID").add(differenses.getBigDecimal("ID")));
          sumamed.setBigDecimal("IP",sumamed.getBigDecimal("IP").add(differenses.getBigDecimal("IP")));
          sumamed.setBigDecimal("SALDO",sumamed.getBigDecimal("SALDO").add(differenses.getBigDecimal("SALDO")));
        } else {
          sumamed.insertRow(false);
          differenses.copyTo(sumamed);
        }
      } while (differenses.next());
    }
    sumamed.setSort(new SortDescriptor(new String[] {"BROJKONTA"}));
    return sumamed;
  }
    

  private String getBrKon(){
    if (kontoPanel.getBrojKonta().equals(kontoPanel.jlrKontoBroj.getText().trim())) return kontoPanel.getBrojKonta();
    else return "";
  }

  private String getCorgs(String table){
    String sqlCorgString = "";
    if (stds.getString("ORGSTR").equals("1")){ //jrbOdabrana.isSelected()) {
      sqlCorgString = table+".CORG ='" + kontoPanel.getCorg() + "'";
    } else {
      StorageDataSet ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.getCorg());
      if (ojs.rowCount()==1) return table+".CORG ='" + ojs.getString("CORG").trim() + "'";
      sqlCorgString = Condition.in("CORG",ojs).qualified(table).toString();
    }
    return sqlCorgString;
  }

  private String getRange(){
    return Condition.between("datumknj",datumP,datumZ).toString();
  }

  public boolean modifyOutSet() {
    return true;
  }

  private String range(String god, String mjPoc, String mjZav) {
    Integer PocMj = new Integer(mjPoc);
    Integer ZavMj = new Integer(mjZav);
    String returnStr = "";
    String tempStr = "";

    for (int i = PocMj.intValue(); i <= ZavMj.intValue(); i++) {
      if (i < 10) {
        tempStr = "'" + god + "0" + i + "'";
      } else {
        tempStr = "'" + god + i + "'";
      }
      if (i < ZavMj.intValue()) {
        returnStr += tempStr + ", ";
      } else {
        returnStr += tempStr;
      }
    }
    return returnStr;
  }


  private void llHandler() {
      kontoPanel.jlrKontoBroj.setText(ll.removeLast().toString().trim());
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.jlrCorg.setText(corgRemember);
      kontoPanel.jlrCorg.forceFocLost();
      this.getJPTV().enableEvents(false);

      ok_action();
      
      this.getJPTV().getDataSet().goToRow(Integer.parseInt(pl.removeLast().toString()));
      this.getJPTV().enableEvents(true);
  }
  
  
  
  public boolean isIspis() {
    return super.isIspis() && !doubleClicked;
  }
  public void jptv_doubleClick() {
//    System.out.println("doubleclick - " + doubleClicked); //XDEBUG delete when no more needed
//    System.out.println("ispis now - " + ispisNow()); //XDEBUG delete when no more needed
//    System.out.println("is ispis - "+isIspis()); //XDEBUG delete when no more needed
    if (!doubleClicked)
      kontoPanel.setNoLookup(true);
    doubleClicked = true;
    if (this.getJPTV().getDataSet().getString("BROJKONTA").length() <4) {
      ll.addLast(this.kontoPanel.jlrKontoBroj.getText().trim());
      pl.addLast(this.getJPTV().getDataSet().getRow()+"");
      kontoPanel.jlrKontoBroj.setText(this.getJPTV().getDataSet().getString("BROJKONTA"));
      kontoPanel.jlrKontoBroj.forceFocLost();
      kontoPanel.setcORG(corgRemember);
      this.getJPTV().enableEvents(false);
      
      ok_action();
      
//      okPress();
      this.getJPTV().getDataSet().first();
      this.getJPTV().enableEvents(true);
    } else {
      frmKarticeGK fkgk = new frmKarticeGK(false,0);
      String konto = this.getJPTV().getDataSet().getString("BROJKONTA");
      fkgk.stds.open();
      fkgk.stds.setTimestamp("pocDatum", datumP);
      fkgk.stds.setTimestamp("zavDatum", datumZ);
      fkgk.setSljed("K");// jrbDatKnjiz.setSelected(true);
      fkgk.pack();
      fkgk.setTitle("Kartica glavne knjige za konto "+ konto);
      fkgk.kontoPanel.jlrKontoBroj.setText(konto);
      fkgk.kontoPanel.jlrKontoBroj.forceFocLost();
      fkgk.kontoPanel.jlrCorg.setText(corgRemember);
      fkgk.kontoPanel.jlrCorg.forceFocLost();
      fkgk.setPrivremeno(stds.getString("PRIVREMENO"));
      fkgk.setLocation(this.getWindow().getX(), this.getWindow().getY());
      fkgk.show();
      return;
    }
  }

  protected void addingReport(boolean rekapitulacija){
    this.addReport("hr.restart.gk.repBrBilancaPerNalozi", "hr.restart.gk.repBrBilancaPerNalozi", "BrutoBilancaNal", "Bruto bilanca - po Vrstama naloga");
    if (stds.getString("BILANCA").equals("K")){
      this.addReport("hr.restart.gk.repBrBilancaPerRP2", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP2", "Bruto bilanca - Bez naziva - PS - Promet - Saldo");
      this.addReport("hr.restart.gk.repBrBilancaPerRP3", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP3", "Bruto bilanca - Bez naziva - Uk. promet - Saldo");
      this.addReport("hr.restart.gk.repBrBilancaPerRP4", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP4", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo");
      this.addReport("hr.restart.gk.repBrBilancaPerRP1", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP1", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ");
      this.addReport("hr.restart.gk.repBrBilancaPerRP1A", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP1A", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo primjer \"normalnog\" redosljeda");
      this.addReport("hr.restart.gk.repBrBilancaPerRP5", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP5", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda");
      this.addReport("hr.restart.gk.repBrBilancaPerRP6", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP6", "Bruto bilanca - Sa nazivom konta - Saldo");
      } else {
        this.addReport("hr.restart.gk.repBrBilancaPerRP2", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP2_old", "Bruto bilanca - Bez naziva - PS - Promet - Saldo");
        this.addReport("hr.restart.gk.repBrBilancaPerRP3", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP3_old", "Bruto bilanca - Bez naziva - Uk. promet - Saldo");
        this.addReport("hr.restart.gk.repBrBilancaPerRP4", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP4_old", "Bruto bilanca - Bez naziva - PS - Promet - Uk. saldo");
        this.addReport("hr.restart.gk.repBrBilancaPerRP1", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP1_old", "Bruto bilanca - Sa nazivom - Uk. promet - Saldo ");
        this.addReport("hr.restart.gk.repBrBilancaPerRP5", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP5_old", "Bruto bilanca - Sa nazivom - PS - Promet - Uk. saldo - 3 reda");
        this.addReport("hr.restart.gk.repBrBilancaPerRP6", "hr.restart.gk.repBrBilancaPer", "BrutoBilancaRP6_old", "Bruto bilanca - Sa nazivom konta - Saldo");
      }
      
      
      if(rekapitulacija){
        this.addReport("hr.restart.gk.repBrBilancaPerRP7", "hr.restart.gk.repBrBilRekapitulacijaPer", "BrutoBilancaRP7", "Rekapitulacija - PS - Promet - Saldo");
        this.addReport("hr.restart.gk.repBrBilancaPerRP8", "hr.restart.gk.repBrBilRekapitulacijaPer", "BrutoBilancaRP8", "Rekapitulacija - Uk. promet - Saldo");
        this.addReport("hr.restart.gk.repBrBilancaPerRP9", "hr.restart.gk.repBrBilRekapitulacijaPer", "BrutoBilancaRP9", "Rekapitulacija - PS - Promet - Uk. saldo - 3 reda");
      }
  }

  private void jbInit() throws Exception {
    kontoPanel.setNoLookup(false);

    stds.setColumns(new Column[]{dm.createTimestampColumn("pocDatum", "Po\u010Detni Datum"),
                                 dm.createTimestampColumn("zavDatum", "Krajnji datum"),
                                 dm.createStringColumn("PRIVREMENO",1),
                                 dm.createStringColumn("ORGSTR",1),
                                 dm.createStringColumn("ISPIS",1),
                                 dm.createStringColumn("BILANCA",1)
    });

    rcbPrivremenost.setDataSet(stds);
    rcbPrivremenost.setRaColumn("PRIVREMENO");
    rcbPrivremenost.setRaItems(new String[][] {
      {"Knjieni","0"},
      {"Svi","1"}
    });

    rcbPripOrgJed.setDataSet(stds);
    rcbPripOrgJed.setRaColumn("ORGSTR");
    rcbPripOrgJed.setRaItems(new String[][] {
      {"Cijela struktura organizacijske jedinice" ,"0"},
      {"Odabrana organizacijska jedinica ","1"}
    });

    rcbIspis.setDataSet(stds);
    rcbIspis.setRaColumn("ISPIS");
    rcbIspis.setRaItems(new String[][] {
      {"Skupno","1"},
      {"Pojedinaèno" ,"0"}
    });

    rcbBilanca.setDataSet(stds);
    rcbBilanca.setRaColumn("BILANCA");
    rcbBilanca.setRaItems(new String[][] {
      {"Kompletna" ,"K"},
      {"Sintetièka","S"},
      {"Analitièka","A"}
    });

    colSaldo = dm.createBigDecimalColumn("SALDO", "Saldo", 2);
    colPocID = dm.createBigDecimalColumn("POCID", "Po\u010Detno duguje", 2);
    colPocIP = dm.createBigDecimalColumn("POCIP", "Po\u010Detno potrauje", 2);
    colNK = dm.createStringColumn("NK", 0);

    this.setJPan(mainPanel);

    xYLayout1.setWidth(556);
    xYLayout1.setHeight(225);
    jLabel4.setText("Izbor bilance / izvješ\u0107a");
    jlPeriod.setText("Period (od - do)");
    jLabel1.setText("Dokumenti / Org. str.");
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(stds);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(stds);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    selOrg.setIcon(raImages.getImageIcon(raImages.IMGOPEN));
    selOrg.setToolTipText("Izbor organizacijskih jedinica za grupiranje");
    selOrg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (kontoPanel.getCorg().length() == 0)
          kontoPanel.jlrCorg.requestFocusLater();
        else dlgCompanyTree.get("bruto-bilanca").show(getWindow(), kontoPanel.getCorg(), 
            "Izbor organizacijskih jedinica za grupiranje");
      }
    });
//    jlKontoBroj.setText("Broj konta");
    mainPanel.setMinimumSize(new Dimension(580, 225));
    mainPanel.setPreferredSize(new Dimension(580, 160));
    mainPanel.setLayout(xYLayout1);
    mainPanel.add(kontoPanel, new XYConstraints(15, 20, -1, -1));
    mainPanel.add(jLabel4, new XYConstraints(15, 135, -1, -1));
    mainPanel.add(jlPeriod, new XYConstraints(15, 70, -1, -1));
    mainPanel.add(jLabel1, new XYConstraints(15, 95, -1, -1));
    mainPanel.add(rcbPripOrgJed, new XYConstraints(255,95,285,-1));
    mainPanel.add(rcbPrivremenost, new XYConstraints(150,95,100,-1));
    mainPanel.add(jLabel1,   new XYConstraints(15, 95, -1, -1));
    mainPanel.add(rcbIspis, new XYConstraints(255,120,100,-1));
    mainPanel.add(jLabel4,   new XYConstraints(15, 120, -1, -1));
    mainPanel.add(rcbBilanca, new XYConstraints(150,120,100,-1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 70, 100, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 70, 100, -1));
    mainPanel.add(selOrg, new XYConstraints(360,120,21,21));
//    mainPanel.add(jlKontoBroj, new XYConstraints(15, 20, -1, -1));
    installResetButton();
  }

  public DataSet getRep1QDS() {
    return repSet;
  }
  
  public void showDefaultValues() {
    rcc.EnabDisabAll(mainPanel, true);

    this.getJPTV().setDataSet(null);
    kontoPanel.jlrKontoBroj.requestFocus();
    kontoPanel.jlrKontoBroj_lookup();
  }


   public void resetDefaults() {
     stds.setTimestamp("pocDatum", hr.restart.util.Util.getUtil().getFirstDayOfYear());
     stds.setTimestamp("zavDatum", hr.restart.util.Util.getUtil().getLastDayOfMonth());
     knjigDifolt = hr.restart.zapod.OrgStr.getKNJCORG();
     kontoPanel.jlrCorg.setText(knjigDifolt);
     kontoPanel.jlrCorg.forceFocLost();
     
     rcbPrivremenost.setSelectedIndex(0);
     rcbPripOrgJed.setSelectedIndex(0);
     rcbIspis.setSelectedIndex(0);
     rcbBilanca.setSelectedIndex(0);

     stds.setString("PRIVREMENO","0");
     stds.setString("ORGSTR","0");
     stds.setString("ISPIS","1");
     stds.setString("BILANCA","K");
     kontoPanel.jlrKontoBroj.setText("");
     kontoPanel.jlrKontoBroj.emptyTextFields();
   }
  
  boolean firstReset = true;
  public void initialValues() {
    this.getJPTV().setDataSet(null);
    if (firstReset) {
      firstReset = false;
      resetDefaults();
    }
    showDefaultValues();
  }

  public String getCORG() {
    return kontoPanel.jlrCorg.getText();
  }

  public String getNAZORG() {
    return kontoPanel.jlrNazorg.getText();
  }

  public boolean getPRIVREMENI() {
    return stds.getString("PRIVREMENO").equals("1");//jcbPrivremeni.isSelected();
  }

  public String getBILANCA() {
    return stds.getString("BILANCA");
  }

  public String getSKUPNI() {
    if (stds.getString("ISPIS").equals("1")){ //jrbSkupni.isSelected()) {
      return "ZBIRNO";
    }
    return "CORG";
  }

  public String getNazKOnto(String cKonto) {
    dm.getKonta().open();
    lookupData.getlookupData().raLocate(dm.getKonta(),"BROJKONTA",cKonto);
    return dm.getKonta().getString("NAZIVKONTA");
  }

  JLabel jLabel1 = new JLabel();
  JLabel jLabel4 = new JLabel();
  XYLayout xYLayout3 = new XYLayout();

  public static QueryDataSet getBrutoBilDS2() {
    return repDS2;
  }

  public void corging(){
    if (stds.getString("ORGSTR").equals("1")) {
      kontoPanel.jlrCorg.requestFocus();
    } else {
      kontoPanel.setcORG(knjigDifolt);
      rcbPripOrgJed.requestFocus();
    }
  }
}
