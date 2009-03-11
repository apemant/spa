/****license*****************************************************************
**   file: frmZakList.java
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
import hr.restart.baza.Konta;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
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


public class frmZakList extends raUpitLite {
  raPanKonto kontoPanel = new raPanKonto("C_NO");
  StorageDataSet stds = new StorageDataSet();
  public static QueryDataSet repDS1;
  public static StorageDataSet storageQDS;
  StorageDataSet ojs;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Util util = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Column colMjZ = new Column();
  Column colGod = new Column();
  Column colPocID = new Column();
  Column colPocIP = new Column();
  Column colSalID = new Column();
  Column colSalIP = new Column();
  Column colNK = new Column();
  Column colSort1 = new Column();
  Column colSort2 = new Column();
  Column colSort3 = new Column();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jtMjesecZav = new JraTextField() {
      public boolean maskCheck() {
        if (super.maskCheck()) {
          if (!checkMonthZav()) {
            this.setErrText("Nepostoje\u0107i mjesec");
            this_ExceptionHandling(new java.lang.Exception());
            return false;
          } else {
            return true;
          }
        } else {
          return false;
        }
      }
    };
  JraTextField jtGodina = new JraTextField();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  Border border1;

  public frmZakList() {
    try {
      jbInit();
      zl = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  static frmZakList zl;

  public static frmZakList getFrmZakList() {
    if (zl == null) {zl = new frmZakList();}
    return zl;
  }

  public void componentShow(){
    changeIcon(1);
    initialValues();
  }

  String sqlCorgString;
  QueryDataSet tempDS;

  public boolean Validacija(){
//    ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(kontoPanel.jlrCorg.getText());
//    String sqlCorgString;
//    ojs.first();
//    sqlCorgString = " and gkkumulativi.corg in (";
//    do {
//      sqlCorgString += "'" + ojs.getString("CORG").trim() + "',";
//    } while (ojs.next());
//    sqlCorgString = sqlCorgString.substring(0, sqlCorgString.trim().length()) + ")";
    sqlCorgString = " and "+Aus.getCorgInCond(kontoPanel.jlrCorg.getText()).qualified("gkkumulativi");

    String findK ="select gkkumulativi.brojkonta as brojkonta, gkkumulativi.godmj as godmj,"+
                  " sum(gkkumulativi.id) as id, sum(gkkumulativi.ip) as ip from gkkumulativi where gkkumulativi.brojkonta like '%'" +
                  " and gkkumulativi.godmj in (" + range(jtGodina.getText().trim(), jtMjesecZav.getText().trim()) + ")" +
                  sqlCorgString + " group by brojkonta, godmj";

//    /*QueryDataSet*/ tempDS = new QueryDataSet();
System.out.println(findK);
    tempDS = util.getNewQueryDataSet(findK, false);

    tempDS.addColumn((Column) colPocID.clone());
    tempDS.addColumn((Column) colPocIP.clone());
    tempDS.addColumn((Column) colSalID.clone());
    tempDS.addColumn((Column) colSalIP.clone());

    tempDS.open();
    tempDS.setAllRowIds(true);

    tempDS.first();


    if (tempDS.isEmpty()){
      showDefaultValues();
      JOptionPane.showConfirmDialog(this, "Nema podataka za zadane uvjete!", "Poruka", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
      return false;
    }

    rcc.EnabDisabAll(mainPanel, false);
    return true;
  }
  public void okPress(){
    maketemporaryQDS1(tempDS);
  }

  void maketemporaryQDS1(QueryDataSet tempDS){
    storageQDS = new StorageDataSet();
    tempDS.first();
    storageQDS.setColumns(new Column[]{
      (Column) dm.getGkkumulativi().getColumn("BROJKONTA").clone(),
      (Column) colNK.clone(),
      (Column) colPocID.clone(),
      (Column) colPocIP.clone(),
      (Column) dm.getGkkumulativi().getColumn("ID").clone(),
      (Column) dm.getGkkumulativi().getColumn("IP").clone(),
      (Column) colSalID.clone(),
      (Column) colSalIP.clone(),
      (Column) colSort1.clone(),
      (Column) colSort2.clone(),
      (Column) colSort3.clone()});

    tempDS.first();
    storageQDS.open();
    do {
      if(tempDS.getString("GODMJ").substring(4, 6).equals("00")){
        tempDS.setBigDecimal("POCID", tempDS.getBigDecimal("ID"));
        tempDS.setBigDecimal("POCIP", tempDS.getBigDecimal("IP"));
        tempDS.setBigDecimal("ID", new BigDecimal(0));
        tempDS.setBigDecimal("IP", new BigDecimal(0));
      } else {
        tempDS.setBigDecimal("POCID", new BigDecimal(0));
        tempDS.setBigDecimal("POCIP", new BigDecimal(0));
      }
      BigDecimal saldoIP = tempDS.getBigDecimal("IP").add(tempDS.getBigDecimal("POCIP")).subtract(tempDS.getBigDecimal("ID")).subtract(tempDS.getBigDecimal("POCID"));
      BigDecimal saldoID = tempDS.getBigDecimal("ID").add(tempDS.getBigDecimal("POCID")).subtract(tempDS.getBigDecimal("IP")).subtract(tempDS.getBigDecimal("POCIP"));
      if(saldoID.compareTo(new BigDecimal(0))>=0) tempDS.setBigDecimal("SALDOID", saldoID);
      else tempDS.setBigDecimal("SALDOID", new BigDecimal(0));
      if(saldoIP.compareTo(new BigDecimal(0))>=0) tempDS.setBigDecimal("SALDOIP", saldoIP);
      else tempDS.setBigDecimal("SALDOIP", new BigDecimal(0));
    } while (tempDS.next());

    tempDS.first();

    do {
      if(tempDS.getBigDecimal("SALDOID").compareTo(new BigDecimal(0))>0 &&
         tempDS.getBigDecimal("SALDOIP").compareTo(new BigDecimal(0))>0){
        if((tempDS.getBigDecimal("SALDOID").subtract(tempDS.getBigDecimal("SALDOIP"))).compareTo(new BigDecimal(0))>0){
          tempDS.setBigDecimal("SALDOID", tempDS.getBigDecimal("SALDOID").subtract(tempDS.getBigDecimal("SALDOIP")));
          tempDS.setBigDecimal("SALDOIP", new BigDecimal(0));
        } else {
          tempDS.setBigDecimal("SALDOIP", tempDS.getBigDecimal("SALDOIP").subtract(tempDS.getBigDecimal("SALDOID")));
          tempDS.setBigDecimal("SALDOID", new BigDecimal(0));
        }
      }
    } while (tempDS.next());
    tempDS.first();
    do {
      storageQDS.insertRow(false);
      storageQDS.setString("BROJKONTA", tempDS.getString("BROJKONTA").substring(0, 3));
      storageQDS.setBigDecimal("SALDOIP", tempDS.getBigDecimal("SALDOIP"));
      storageQDS.setBigDecimal("SALDOID", tempDS.getBigDecimal("SALDOID"));
      storageQDS.setBigDecimal("POCID", tempDS.getBigDecimal("POCID"));
      storageQDS.setBigDecimal("POCIP", tempDS.getBigDecimal("POCIP"));
      storageQDS.setBigDecimal("ID", tempDS.getBigDecimal("ID"));
      storageQDS.setBigDecimal("IP", tempDS.getBigDecimal("IP"));
    } while (tempDS.next());
    repDS1 = sumAllSame(storageQDS);
//    repDS1.setSort(new SortDescriptor(new String[] {"BROJKONTA"},true,true));
    repDS1.open();
    repDS1.first();
    do {
      if(repDS1.getBigDecimal("SALDOID").compareTo(new BigDecimal(0))>0 &&
         repDS1.getBigDecimal("SALDOIP").compareTo(new BigDecimal(0))>0){
        if((repDS1.getBigDecimal("SALDOID").subtract(repDS1.getBigDecimal("SALDOIP"))).compareTo(new BigDecimal(0))>0){
          repDS1.setBigDecimal("SALDOID", repDS1.getBigDecimal("SALDOID").subtract(repDS1.getBigDecimal("SALDOIP")));
          repDS1.setBigDecimal("SALDOIP", new BigDecimal(0));
        } else {
          repDS1.setBigDecimal("SALDOIP", repDS1.getBigDecimal("SALDOIP").subtract(repDS1.getBigDecimal("SALDOID")));
          repDS1.setBigDecimal("SALDOID", new BigDecimal(0));
        }
      }
    } while (repDS1.next());
    storageQDS.deleteAllRows();
    repDS1.first();
    do {
      for (int i = 3; i >= 1; i--) {
        storageQDS.insertRow(false);
        storageQDS.setString("BROJKONTA", repDS1.getString("BROJKONTA").substring(0, i));
        storageQDS.setBigDecimal("SALDOIP", repDS1.getBigDecimal("SALDOIP"));
        storageQDS.setBigDecimal("SALDOID", repDS1.getBigDecimal("SALDOID"));
        storageQDS.setBigDecimal("POCID", repDS1.getBigDecimal("POCID"));
        storageQDS.setBigDecimal("POCIP", repDS1.getBigDecimal("POCIP"));
        storageQDS.setBigDecimal("ID", repDS1.getBigDecimal("ID"));
        storageQDS.setBigDecimal("IP", repDS1.getBigDecimal("IP"));
      }
    } while (repDS1.next());
    repDS1 = sumAllSame(storageQDS);
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(repDS1);
  }

  private QueryDataSet sumAllSame(StorageDataSet stdsZaSumiranje) {
    QueryDataSet tempRep = new QueryDataSet();
    tempRep.setColumns(stdsZaSumiranje.cloneColumns());
    tempRep.open();
    stdsZaSumiranje.first();
    boolean passThru = true;
    do {
      if (tempRep.rowCount()==0) {
        tempRep.insertRow(false);
        stdsZaSumiranje.copyTo(tempRep);

      } else {
        tempRep.first();
        do {
          if (tempRep.getString("BROJKONTA").equals(stdsZaSumiranje.getString("BROJKONTA"))) {
            tempRep.setBigDecimal("ID", tempRep.getBigDecimal("ID").add(stdsZaSumiranje.getBigDecimal("ID")));
            tempRep.setBigDecimal("IP", tempRep.getBigDecimal("IP").add(stdsZaSumiranje.getBigDecimal("IP")));
            tempRep.setBigDecimal("POCID", tempRep.getBigDecimal("POCID").add(stdsZaSumiranje.getBigDecimal("POCID")));
            tempRep.setBigDecimal("POCIP", tempRep.getBigDecimal("POCIP").add(stdsZaSumiranje.getBigDecimal("POCIP")));
            tempRep.setBigDecimal("SALDOIP", tempRep.getBigDecimal("SALDOIP").add(stdsZaSumiranje.getBigDecimal("SALDOIP")));
            tempRep.setBigDecimal("SALDOID", tempRep.getBigDecimal("SALDOID").add(stdsZaSumiranje.getBigDecimal("SALDOID")));
            passThru = false;
            break;
          }
        } while (tempRep.next());
        if (passThru) {
          tempRep.insertRow(false);
          stdsZaSumiranje.copyTo(tempRep);

          tempRep.last();
        }
      }
      passThru = true;
    } while (stdsZaSumiranje.next());
    tempRep.setSort(new SortDescriptor(new String[] {"BROJKONTA"},true,true));
    tempRep.first();
    do {
      String ttt="";
      String tttt="";
      tempRep.setString("SORT1", tempRep.getString("BROJKONTA").substring(0,1));
      ttt = tempRep.getString("SORT1").trim();
      if (tempRep.getString("BROJKONTA").length()>1){
        tempRep.setString("SORT2", tempRep.getString("BROJKONTA").substring(0,2));
        tttt = tempRep.getString("SORT2");
      }
      else {
        tempRep.setString("SORT2", ttt.concat("9"));
        tttt = tempRep.getString("SORT2");
      }
      if (tempRep.getString("BROJKONTA").length()>2) tempRep.setString("SORT3", tempRep.getString("BROJKONTA").substring(0,3));
      else tempRep.setString("SORT3", tttt.concat("9"));
    }while (tempRep.next());
    return tempRep;
  }

  public void firstESC(){
    showDefaultValues();
  }
  public boolean runFirstESC(){
    return false;
  }
  private void jbInit() throws Exception {

    this.addReport("hr.restart.gk.repZakList", "Zaklju\u010Dni list", 2);

    colPocID = dm.createBigDecimalColumn("POCID", "Po\u010Detno dugovanje", 2);
    colPocIP = dm.createBigDecimalColumn("POCIP", "Po\u010Detno potraživanje", 2);
    colSalID = dm.createBigDecimalColumn("SALDOID", "Saldo duguje", 2);
    colSalIP = dm.createBigDecimalColumn("SALDOIP", "Saldo potražuje", 2);
    colNK = dm.createStringColumn("NK", 0);
    colSort1 = dm.createStringColumn("SORT1", 0);
    colSort2 = dm.createStringColumn("SORT2", 0);
    colSort3 = dm.createStringColumn("SORT3", 0);

    border1 = BorderFactory.createEtchedBorder(new Color(224, 255, 255),new Color(109, 129, 140));
    this.setJPan(mainPanel);
    mainPanel.setLayout(xYLayout1);

    rcc.EnabDisabAll(kontoPanel, false);

    colMjZ = dm.createStringColumn("jtMjesecZav", "Završni mjesec", 0);
    colGod = dm.createStringColumn("jtGodina", "Godina", 0);

    jtMjesecZav.setHorizontalAlignment(SwingConstants.CENTER);
    jtMjesecZav.setColumnName("jtMjesecZav");
    jtMjesecZav.setDataSet(stds);

    jtGodina.setHorizontalAlignment(SwingConstants.CENTER);
    jtGodina.setDataSet(stds);
    jtGodina.setColumnName("jtGodina");

    stds.setColumns(new Column[]{ colMjZ, colGod});

    super.setStepsNumber(3);

    jLabel1.setText("Period do (mm - gggg)");
    jLabel2.setText("-");
    xYLayout1.setHeight(70);
    mainPanel.setPreferredSize(new Dimension(580, 70));
    mainPanel.add(kontoPanel, new XYConstraints(15, 15, -1, -1));
    mainPanel.add(jtMjesecZav, new XYConstraints(150, 40, 35, -1));
    mainPanel.add(jLabel1, new XYConstraints(15, 40, -1, -1));
    mainPanel.add(jLabel2, new XYConstraints(190, 40, -1, -1));
    mainPanel.add(jtGodina,   new XYConstraints(200, 40, 50, -1));
  }
  private String getNazKOnto(String cKonto) {
  	DataSet k = Konta.getDataModule().getTempSet("nazivkonta", Condition.equal("BROJKONTA", cKonto));    
    k.open();
    return k.getString("NAZIVKONTA");
  }
  private boolean checkMonthZav() {
    if (jtMjesecZav.getText().equals("")) {
      return true;
    }
    try {
      int i = Integer.parseInt(jtMjesecZav.getText());
      return (i < 13);
    } catch (Exception ex) {
      return false;
    }
  }

  private String range(String god, String mjZav) {
    Integer ZavMj = new Integer(mjZav);
    String returnStr = "";
    String tempStr = "";

    for (int i = 0; i <= ZavMj.intValue(); i++) {
      if (i < 10) {tempStr = "'" + god + "0" + i + "'";}
      else {tempStr = "'" + god + i + "'";}
      if (i < ZavMj.intValue()) {returnStr += tempStr + ", ";}
      else {returnStr += tempStr;}
    }
    return returnStr;
  }

  public QueryDataSet getReportQDS(){
    return repDS1;
  }

  public String getCORG(){
    return hr.restart.zapod.OrgStr.getKNJCORG();
  }

  public String getNAZORG(){
    return kontoPanel.jlrNazorg.getText().trim();
  }

  public void showDefaultValues() {
    rcc.setLabelLaF(jtMjesecZav,true);
    rcc.setLabelLaF(jtGodina,true);
    jtGodina.requestFocus();
    jtMjesecZav.requestFocus();
  }

  public void initialValues() {
    kontoPanel.jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    kontoPanel.jlrCorg.forceFocLost();
    stds.setString("jtMjesecZav", util.getMonth(vl.getToday()));
    stds.setString("jtGodina", vl.findYear(vl.getToday()));
    showDefaultValues();
  }

  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }
}