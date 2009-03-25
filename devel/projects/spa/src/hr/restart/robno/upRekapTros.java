/****license*****************************************************************
**   file: upRekapTros.java
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
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class upRekapTros extends raUpitLite {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  StorageDataSet stds = new StorageDataSet();
  StorageDataSet tempStds = new StorageDataSet();
  StorageDataSet repStds = new StorageDataSet();

  JPanel jpDetail = new JPanel();
  JPanel mainPanel = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlGod = new JLabel();
  JraTextField jraGodina = new JraTextField();
  JraButton jbSelCorg = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  private BorderLayout borderLayout1 = new BorderLayout();

  public upRekapTros() {
    try {
      jbInit();
      uretro = this;
    }
    catch (Exception ex) {

    }
  }

  static upRekapTros uretro;

  public static upRekapTros getInstance(){
    return uretro;
  }

  public void componentShow() {
    initialValues();
  }

  private void initialValues() {
    stds.setString("GODINA", ut.getYear(vl.getToday()));
  }

  boolean pressed = false;

  public void okPress() {
    pressed = true;
    int month;
    String corg = "swahillylilili";

    if(!repStds.isOpen()) repStds.open();
    else repStds.deleteAllRows();

    tempStds.first();

    do{
      if(!tempStds.getString("CORG").equals(corg)){
        corg = tempStds.getString("CORG");
        repStds.insertRow(false);
      }
      month = Integer.parseInt(ut.getMonth(tempStds.getTimestamp("DATDOK")));
      repStds.setString("CORG", corg);
      switch (month) {
        case 1 :
          repStds.setBigDecimal("MJESEC01", repStds.getBigDecimal("MJESEC01").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 2 :
          repStds.setBigDecimal("MJESEC02", repStds.getBigDecimal("MJESEC02").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 3 :
          repStds.setBigDecimal("MJESEC03", repStds.getBigDecimal("MJESEC03").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 4 :
          repStds.setBigDecimal("MJESEC04", repStds.getBigDecimal("MJESEC04").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 5 :
          repStds.setBigDecimal("MJESEC05", repStds.getBigDecimal("MJESEC05").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 6 :
          repStds.setBigDecimal("MJESEC06", repStds.getBigDecimal("MJESEC06").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("PPGOD", repStds.getBigDecimal("PPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 7 :
          repStds.setBigDecimal("MJESEC07", repStds.getBigDecimal("MJESEC07").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 8 :
          repStds.setBigDecimal("MJESEC08", repStds.getBigDecimal("MJESEC08").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 9 :
          repStds.setBigDecimal("MJESEC09", repStds.getBigDecimal("MJESEC09").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 10 :
          repStds.setBigDecimal("MJESEC10", repStds.getBigDecimal("MJESEC10").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 11 :
          repStds.setBigDecimal("MJESEC11", repStds.getBigDecimal("MJESEC11").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
        case 12 :
          repStds.setBigDecimal("MJESEC12", repStds.getBigDecimal("MJESEC12").add(tempStds.getBigDecimal("IRAZ")));
          repStds.setBigDecimal("DPGOD", repStds.getBigDecimal("DPGOD").add(tempStds.getBigDecimal("IRAZ")));
          break;
      }
      repStds.setBigDecimal("GODINA", repStds.getBigDecimal("GODINA").add(tempStds.getBigDecimal("IRAZ")));

    } while (tempStds.next());

//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(repStds);

  }

  public void firstESC() {
    pressed = false;
    defaultValues(true);
    jlrCorg.setText("");
    jlrCorg.emptyTextFields();
    jlrCorg.requestFocus();
  }
  public boolean runFirstESC() {
    if (pressed) return true;
    return false;
  }
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }

  public boolean Validacija(){
    String god = stds.getString("GODINA");
    String corg = stds.getString("CORG");
    String qStr ="";

    String qstr = "SELECT doki.corg, doki.datdok, stdoki.iraz " +
                  "FROM doki, stdoki " +
                  "WHERE doki.vrdok = 'IZD' " +
                  "AND doki.cskl = stdoki.cskl " +
                  "AND doki.vrdok = stdoki.vrdok " +
                  "AND doki.god = stdoki.god " +
                  "AND doki.brdok = stdoki.brdok ";

    String acorg = "AND doki.corg ='" + corg + "' ";

    String bcorg = "AND (doki.corg in " + OrgStr.getOrgStr().getInQuery(jlrCorg.getRaDataSet(),"doki.corg") + ") ";

    String nadopr = "AND doki.datdok between '" + god + "-01-01 00:00:00.0' and '" + god + "-12-31 23:59:59.0' "+
                    "ORDER BY doki.corg";

    if(corg.equals("")){
      qStr = qstr.concat(bcorg.concat(nadopr));
    } else {
      qStr = qstr.concat(acorg.concat(nadopr));
    }

    vl.execSQL(qStr);
    tempStds = vl.RezSet;
    tempStds.open();

    repStds.open();

    if (tempStds.rowCount() == 0){
      jlrCorg.requestFocus();
      jlrCorg.selectAll();
      JOptionPane.showMessageDialog(this.mainPanel,
                                    new raMultiLineMessage(new String[] {"Za zadanu organizacijsku jedinicu","nema podataka o troškovima"}),
                                    "Rekapitulacija troškova",
                                    JOptionPane.INFORMATION_MESSAGE);
//      System.out.println("nema podataka");
      return false;
    }
    defaultValues(false);
    return true;
  }

  private void defaultValues(boolean inOrOut){
    rcc.EnabDisabAll(this.mainPanel,inOrOut);
  }

  Column colCorg = new Column();
  Column colGod = new Column();
  Column colCorgRep = new Column();
  Column colMjesec1 = new Column();
  Column colMjesec2 = new Column();
  Column colMjesec3 = new Column();
  Column colMjesec4 = new Column();
  Column colMjesec5 = new Column();
  Column colMjesec6 = new Column();
  Column colMjesec7 = new Column();
  Column colMjesec8 = new Column();
  Column colMjesec9 = new Column();
  Column colMjesec10 = new Column();
  Column colMjesec11 = new Column();
  Column colMjesec12 = new Column();
  Column colGodina = new Column();
  Column colGodinaDP = new Column();
  Column colGodinaOP = new Column();


  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repRekapTrosPP", "Rekapitulacija troškova za prvu polovicu godine",2);
    this.addReport("hr.restart.robno.repRekapTrosDP", "Rekapitulacija troškova za drugu polovicu godine",2);
    this.addReport("hr.restart.robno.repRekapTros", "Rekapitulacija troškova za cijelu godinu",2);

    setTempStds();
    setRepStds();

    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(90);

    jbSelCorg.setText("...");
    jlCorg.setText("Org. jedinica");
    jlGod.setText("Za godinu");

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(stds);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jraGodina.setDataSet(stds);
    jraGodina.setColumnName("GODINA");

    mainPanel.setLayout(borderLayout1);
    this.setJPan(mainPanel);
    jpDetail.add(jbSelCorg, new XYConstraints(555, 20, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 295, -1));
    jpDetail.add(jlGod, new XYConstraints(15,45,-1,-1));
    jpDetail.add(jraGodina, new XYConstraints(150,45,50,-1));

    mainPanel.add(jpDetail, BorderLayout.CENTER);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  private void setTempStds() {
    colCorg.setColumnName("CORG");
    colCorg.setDataType(com.borland.dx.dataset.Variant.STRING);

    colGod.setColumnName("GODINA");
    colGod.setDataType(com.borland.dx.dataset.Variant.STRING);

    stds.setColumns(new Column[] {colCorg, colGod});
    try {
      stds.open();
    }
    catch (Exception ex) {
    }
  }

  void makeBDCol(Column col, String difolt) {
    col.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    col.setDefault(difolt);
  }

  private void setRepStds() {

//    colCorgRep.setColumnName("CORG");
//    colCorgRep.setDataType(com.borland.dx.dataset.Variant.STRING);

    colCorgRep = dm.createStringColumn("CORG",0);

    colMjesec1.setColumnName("MJESEC01");
    makeBDCol(colMjesec1, "0.00");

    colMjesec2.setColumnName("MJESEC02");
    makeBDCol(colMjesec2, "0.00");

    colMjesec3.setColumnName("MJESEC03");
    makeBDCol(colMjesec3, "0.00");

    colMjesec4.setColumnName("MJESEC04");
    makeBDCol(colMjesec4, "0.00");

    colMjesec5.setColumnName("MJESEC05");
    makeBDCol(colMjesec5, "0.00");

    colMjesec6.setColumnName("MJESEC06");
    makeBDCol(colMjesec6, "0.00");

    colMjesec7.setColumnName("MJESEC07");
    makeBDCol(colMjesec7, "0.00");

    colMjesec8.setColumnName("MJESEC08");
    makeBDCol(colMjesec8, "0.00");

    colMjesec9.setColumnName("MJESEC09");
    makeBDCol(colMjesec9, "0.00");

    colMjesec10.setColumnName("MJESEC10");
    makeBDCol(colMjesec10, "0.00");

    colMjesec11.setColumnName("MJESEC11");
    makeBDCol(colMjesec11, "0.00");

    colMjesec12.setColumnName("MJESEC12");
    makeBDCol(colMjesec12, "0.00");

    colGodina.setColumnName("GODINA");
    makeBDCol(colGodina, "0.00");

    colGodinaDP.setColumnName("PPGOD");
    makeBDCol(colGodinaDP, "0.00");

    colGodinaOP.setColumnName("DPGOD");
    makeBDCol(colGodinaOP, "0.00");

    repStds.setColumns(new Column[] {colCorgRep, colMjesec1, colMjesec2, colMjesec3, colMjesec4, colMjesec5, colMjesec6,
        colMjesec7, colMjesec8, colMjesec9, colMjesec10, colMjesec11, colMjesec12, colGodina, colGodinaDP, colGodinaOP});
  }

  public DataSet getRepStDS(){
    return repStds;
  }

  public String getGodinu(){
    return stds.getString("GODINA");
  }



}