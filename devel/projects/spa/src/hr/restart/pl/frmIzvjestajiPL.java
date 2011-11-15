/****license*****************************************************************
**   file: frmIzvjestajiPL.java
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
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmIzvjestajiPL extends raUpitLite {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  hr.restart.zapod.OrgStr orgs = hr.restart.zapod.OrgStr.getOrgStr();

  static StorageDataSet fieldSet = new StorageDataSet();

  public static StorageDataSet getStaticFieldSet(){
    return fieldSet;
  }

  public StorageDataSet getFieldSet(){
    return fieldSet;
  }


  Column colMjesecOd = new Column();
  Column colGodinaOd = new Column();
  Column colMjesecDo = new Column();
  Column colGodinaDo = new Column();
  Column colRbrOd = new Column();
  Column colRbrDo = new Column();
  Column colCORG = new Column();

  JPanel mainPanel = new JPanel();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();

  JLabel jlCorg = new JLabel();
  JLabel jlMjGodOd = new JLabel();
  JLabel jlRbr = new JLabel();

  private BorderLayout borderLayout1 = new BorderLayout();

  public XYLayout xYLayout1 = new XYLayout();
  public XYLayout xYLayout2 = new XYLayout();
  public JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      aft_lookCorg();
    }
  };
  public JlrNavField jlrNazorg = new JlrNavField() {
    public void after_lookUp() {
      aft_lookCorg();
    }
  };
  JraButton jbSelCorg = new JraButton();

  public JraTextField jraMjesecOd = new JraTextField();
  public JraTextField jraGodinaOd = new JraTextField();
  public JraTextField jraMjesecDo = new JraTextField();
  public JraTextField jraGodinaDo = new JraTextField();
  public JraTextField jraRbrOd = new JraTextField();
  public JraTextField jraRbrDo = new JraTextField();

  private char repMode = 'O';

  public frmIzvjestajiPL() {
    this('O');
  }

  public frmIzvjestajiPL(char _repMode) {
    setRepMode(_repMode);
    setValidacijaInProcess(true);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public boolean isArhMode() {
    return repMode == 'A';
  }

  public void componentShow() {
    setDifolt();
    jlrCorg.requestFocus();
  }

  String getBetweenAhrQuery(){
    return getBetweenAhrQuery(null);
  }

  String getBetweenAhrQuery(String tabela) {
    if (repMode == 'A') { //arhiva
//    System.out.println("FIELDSEEEET: ");
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(fieldSet);
      String q = new raPlObrRange(
        fieldSet.getShort("GODINAOD"),
        fieldSet.getShort("MJESECOD"),
        fieldSet.getShort("RBROD"),
        fieldSet.getShort("GODINADO"),
        fieldSet.getShort("MJESECDO"),
        fieldSet.getShort("RBRDO")).getQuery(tabela);

      return " AND ".concat(q);
    } else return "";
  }

  private int inneeded = -1;
  private boolean inQueryNeeded() {
    if (inneeded < 0) {
      if (frmParam.getParam("pl", "corginopt", "D", "Optimizirati in query u frmIzvjestajiPL (D/N").equalsIgnoreCase("D")) {
        QueryDataSet ojs = Orgstruktura.getDataModule().getTempSet(Condition.equal("NALOG", "1"));
        ojs.open();
        inneeded = ojs.getRowCount()>1?1:0;
      } else inneeded = 1; 
    }
    return inneeded == 1;
  }
  public String getWhereQuery() {
    if (!inQueryNeeded()) return "corg = corg " + getBetweenAhrQuery();
    return   "(corg in" + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG"))) +") "+ getBetweenAhrQuery();
  }

  public String getWhereQuery(String tabela) {
    if (!inQueryNeeded()) return tabela+".corg = "+tabela+".corg"+getBetweenAhrQuery(tabela);
    return   "("+tabela+".corg in" + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),tabela+".corg") +") "+ getBetweenAhrQuery(tabela);
  }

  public boolean Validacija(){
    if (vl.isEmpty(jlrCorg)) return false;
    return true;
  }

  public void okPress() {
    firstesc = true;
    rcc.EnabDisabAll(mainPanel, false);
  }

  public void setMiddlePanel(JPanel pan) {
    mainPanel.add(pan,BorderLayout.CENTER);
  }

  boolean firstesc = false;

  public void firstESC(){
    try {
      if (getRepMode() == 'A'){
        rcc.EnabDisabAll(mainPanel, true);
      } else {
        rcc.EnabDisabAll(jPanel1, true);
      }
      jlrCorg.requestFocus();
      jlrCorg.selectAll();
      firstesc = false;
    }
    catch (Exception ex) {
    }
  }

  public boolean runFirstESC(){
    return firstesc;
  }

  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }

  void makeShortCol(Column col, String difolt) {
    col.setDataType(com.borland.dx.dataset.Variant.SHORT);
    col.setDefault(difolt);
//    col = dm.createShortColumn(difolt);
  }

  public Timestamp getDatumIspl() {
    return datumispl;
  }

  private void jbInit() throws Exception {

//    colCORG.setColumnName("CORG");
//    colCORG.setDataType(com.borland.dx.dataset.Variant.STRING);

    colCORG = dm.createStringColumn("CORG","Oznaka organizacijske jedinice",0);

    colMjesecOd.setColumnName("MJESECOD");
    makeShortCol(colMjesecOd,"00");

    colGodinaOd.setColumnName("GODINAOD");
    makeShortCol(colGodinaOd,"0000");

    colMjesecDo.setColumnName("MJESECDO");
    makeShortCol(colMjesecDo,"00");

    colGodinaDo.setColumnName("GODINADO");
    makeShortCol(colGodinaDo,"0000");

    colRbrOd.setColumnName("RBROD");
    makeShortCol(colRbrOd,"000");

    colRbrDo.setColumnName("RBRDO");
    makeShortCol(colRbrDo,"000");

    try {
      System.out.println("");
      fieldSet.setColumns(new Column[] {colCORG, colMjesecOd, colGodinaOd, colMjesecDo, colGodinaDo, colRbrOd, colRbrDo});
      fieldSet.open();
    }
    catch (Exception ex) {
    }

    jlCorg.setText("Org. jedinica");
    jbSelCorg.setText("...");

    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setColumnName("CORG");
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jraMjesecOd.setDataSet(fieldSet);
    jraMjesecOd.setColumnName("MJESECOD");

    jraGodinaOd.setDataSet(fieldSet);
    jraGodinaOd.setColumnName("GODINAOD");

    jraMjesecDo.setDataSet(fieldSet);
    jraMjesecDo.setColumnName("MJESECDO");

    jraGodinaDo.setDataSet(fieldSet);
    jraGodinaDo.setColumnName("GODINADO");

    jraRbrOd.setDataSet(fieldSet);
    jraRbrOd.setColumnName("RBROD");

    jraRbrDo.setDataSet(fieldSet);
    jraRbrDo.setColumnName("RBRDO");

    this.setJPan(mainPanel);
    xYLayout1.setWidth(590);
    xYLayout1.setHeight(40);
    xYLayout2.setWidth(590);
    xYLayout2.setHeight(60);
    mainPanel.setLayout(borderLayout1);
    jPanel1.setLayout(xYLayout1);
    jPanel2.setLayout(xYLayout2);
    mainPanel.add(jPanel1, BorderLayout.NORTH);
    mainPanel.add(jPanel2, BorderLayout.SOUTH);

    jPanel1.add(jlrNazorg,  new XYConstraints(255, 15, 295, -1));
    jPanel1.add(jbSelCorg,   new XYConstraints(555, 15, 21, 21));
    jPanel1.add(jlCorg, new XYConstraints(15, 15, -1, -1));
    jPanel1.add(jlrCorg, new XYConstraints(150, 15, 100, -1));

    jPanel2.add(jlMjGodOd, new XYConstraints(15, 0, -1, -1));
    jPanel2.add(jraMjesecOd, new XYConstraints(215, 0, 35, -1));
    jPanel2.add(jraGodinaOd, new XYConstraints(150, 0, 60, -1));
    jPanel2.add(jlRbr, new XYConstraints(15, 25, -1, -1));
    jPanel2.add(jraRbrOd, new XYConstraints(150, 25, 35, -1));

    jPanel2.add(jraMjesecDo, new XYConstraints(320, 0, 35, -1));
    jPanel2.add(jraGodinaDo, new XYConstraints(255, 0, 60, -1));
    jPanel2.add(jraRbrDo, new XYConstraints(215, 25, 35, -1));
  }

  public void setDifolt(){
    fieldSet.emptyAllRows();
    fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    jlrCorg.forceFocLost();
    if(repMode == 'O' ) {
      jlMjGodOd.setText("Obrada");
      jlRbr.setText("Redni broj");
      rcc.EnabDisabAll(jPanel2, false);
      jraMjesecDo.setVisible(false);
      jraGodinaDo.setVisible(false);
      jraRbrDo.setVisible(false);
    } else if(repMode == 'A'){
      jlMjGodOd.setText("Obrada (od - do)");
      jlRbr.setText("Redni broj (od - do)");
      fieldSet.setShort("MJESECOD", (short) 1);
      fieldSet.setShort("GODINAOD", Short.parseShort(vl.findYear(vl.getToday())));
      fieldSet.setShort("MJESECDO", Short.parseShort(Util.getUtil().getMonth(vl.getToday())));  /*Util.getNewQueryDataSet("SELECT max(MJOBR) as MJOBR FROM Kumulorgarh WHERE godobr='" + vl.findYear(vl.getToday()) + "'").getShort("MJOBR"));//*/
      fieldSet.setShort("GODINADO", Short.parseShort(vl.findYear(vl.getToday())));
      fieldSet.setShort("RBROD", (short) 1); /*Util.getNewQueryDataSet("SELECT min(RBROBR) as MIRBROBR FROM Kumulorgarh WHERE godobr='" + vl.findYear(vl.getToday()) + "'").getShort("MIRBROBR"));//*/
      fieldSet.setShort("RBRDO", (short) 99); /*Util.getNewQueryDataSet("SELECT max(RBROBR) as MARBROBR FROM Kumulorgarh WHERE godobr='" + vl.findYear(vl.getToday()) + "'").getShort("MARBROBR"));//*/
    }
    jlrCorg.requestFocus();
  }

  public void setRbrDoArh(short x){
    fieldSet.setShort("RBRDO", x);
  }

  static Timestamp datumispl;

//  public static Timestamp getDatumispl(){
//    return datumispl;
//  }

  public void aft_lookCorg() {
    if(getRepMode() == 'O'){
      raIniciranje.getInstance().posOrgsPl(fieldSet.getString("CORG"));
      datumispl = dm.getOrgpl().getTimestamp("DATUMISPL");
      fieldSet.setShort("MJESECOD", dm.getOrgpl().getShort("MJOBR"));
      fieldSet.setShort("GODINAOD", dm.getOrgpl().getShort("GODOBR"));
      fieldSet.setShort("RBROD",dm.getOrgpl().getShort("RBROBR"));
    }
  }

  public char getRepMode() {
    return repMode;
  }

  public void setRepMode(char mod) {
    this.repMode = mod;
  }
}