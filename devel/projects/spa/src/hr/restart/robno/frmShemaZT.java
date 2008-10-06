/****license*****************************************************************
**   file: frmShemaZT.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmShemaZT extends hr.restart.util.raMasterDetail {
  hr.restart.baza.dM dm=hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST ST  = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.raCommonClass rCC= hr.restart.util.raCommonClass.getraCommonClass();
  JPanel jp = new JPanel();
  JPanel jpD = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jraCSHZT = new JraTextField();
  JraTextField jraNSHZT = new JraTextField();
  Rbr rbr = Rbr.getRbr();
  JlrNavField jlrCZT1 = new JlrNavField(){
    public void after_lookUp() {
      Mafter_lookUp(jlrCZT1,"CZT1");
      jlrNAZZT1.setCaretPosition(0);
    }
  };

  JlrNavField jlrNAZZT1 = new JlrNavField();
  JlrNavField jltIZT = new JlrNavField();
  JlrNavField jlrPZT1 = new JlrNavField();
  JraButton jbZT1 = new JraButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JraTextField jlrZTPUK = new JraTextField();
  JraTextField jlrZTIUK = new JraTextField();
  short nStavka = 1 ;
  Valid vl = Valid.getValid();


  public frmShemaZT() {
    super.AfterCancelMaster();
    try {
      dm.getShzavtr().open();
      dm.getZavtr().open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    java.lang.String [] key={"CSHZT"};

    this.setMasterSet(dm.getShzavtr());
    this.setDetailSet(dm.getvshztr_ztr());
    this.setMasterKey(key);
    this.setDetailKey(key);
    this.setVisibleColsMaster(new int[] {0,1});
    this.setVisibleColsDetail(new int[] {2,3,4});
    jp.setLayout(xYLayout1);

    jraCSHZT.setDataSet(this.getMasterSet());
    jraCSHZT.setColumnName("CSHZT");
    jraNSHZT.setDataSet(this.getMasterSet());
    jraNSHZT.setColumnName("NSHZT");

    this.raMaster.getRepRunner().addReport("hr.restart.robno.repShemaZT", "Formatirani ispis sheme zavisnih troškova", 5);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repStavkeZT", "Formatirani ispis stavki zavisnih troškova", 5);

    jraNSHZT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jraNSHZT_actionPerformed(e);
      }
    });

    jLabel2.setText("Shema ZT");
    jLabel3.setText("Šifra");
    jLabel4.setText("Naziv");

    jlrPZT1.setEnabled(false);
    jlrPZT1.setDisabledTextColor(Color.black);
    jlrPZT1.setEditable(false);
    jlrPZT1.setHorizontalAlignment(SwingConstants.LEFT);
    jlrPZT1.setEnableClearAll(false);
    jLabel5.setText("Postotak");

//    jp.setMinimumSize(   new Dimension(550, 75));
//    jp.setPreferredSize( new Dimension(555, 75));
//    jpD.setMinimumSize(  new Dimension(550, 90));
//    jpD.setPreferredSize(new Dimension(555, 90));

    jLabel6.setText("%");
    xYLayout2.setWidth(550);
    xYLayout2.setHeight(90);
    xYLayout1.setWidth(550);
    xYLayout1.setHeight(75);
    jp.add(jLabel2,     new XYConstraints( 15, 25, 139, 19));
    jp.add(jraCSHZT,    new XYConstraints(150, 25,  50, -1));
    jp.add(jraNSHZT,    new XYConstraints(205, 25, 335, -1));
    jp.add(jLabel3,     new XYConstraints(150,  4,  33, -1));
    jp.add(jLabel4,     new XYConstraints(205,  4, 107, -1));

    jpD.setLayout(xYLayout2);
    jLabel1.setText("Zavisni trošak");

    jlrCZT1.setRaDataSet(dm.getZtr()); // dm.getZavtr());
    jlrCZT1.setVisCols(new int[]{0,1});
    jlrCZT1.setColNames(new String[] {"CZT","NZT","PZT"});
    jlrCZT1.setTextFields(new javax.swing.text.JTextComponent[] {jltIZT,jlrNAZZT1,jlrPZT1});
    jlrCZT1.setDataSet(this.getDetailSet());
    jlrCZT1.setColumnName("CZT");
    jlrCZT1.setNavButton(this.jbZT1);

    jlrNAZZT1.setNavProperties(jlrCZT1);
    jlrNAZZT1.setSearchMode(1);
    jlrNAZZT1.setColumnName("NZT");

    jlrPZT1.setNavProperties(jlrCZT1);
    jlrPZT1.setColumnName("PZT");
    jlrPZT1.disable();

    jltIZT.setNavProperties(jlrCZT1);
    jltIZT.setColumnName("IZT");
    jbZT1.setText("...");

    jlrZTPUK.setDataSet(this.getMasterSet());
    jlrZTPUK.setColumnName("ZTPUK");
    jlrZTIUK.setDataSet(this.getMasterSet());
    jlrZTIUK.setColumnName("ZTIUK");

    jpD.add(jLabel1,   new XYConstraints(15, 20, 76, -1));
    jpD.add(jlrCZT1,   new XYConstraints(150, 20, 100, -1));
    jpD.add(jlrNAZZT1,        new XYConstraints(255, 20, 263, -1));
    jpD.add(jlrPZT1,      new XYConstraints(150, 45, 60, -1));
    jpD.add(jLabel5,   new XYConstraints(15, 45, 64, -1));
    jpD.add(jbZT1,   new XYConstraints(518, 20, 21, 21));
    jpD.add(jLabel6,    new XYConstraints(215, 47, 34, -1));
    this.setNaslovMaster("Zaglavlje sheme zavisnih troškova");
    this.setNaslovDetail("Stavka sheme zavisnih troškova");
    this.setJPanelMaster(jp);
    this.setJPanelDetail(jpD);
  }

  public void Mafter_lookUp(JlrNavField jnf,String what){}

  public void SetFokusMaster( char mode){
    if (mode =='N'){
      rCC.setLabelLaF(jraCSHZT, true);
      jraCSHZT.requestFocus();
    }
    else if (mode=='I'){
      rCC.setLabelLaF(jraCSHZT,false);
      jraNSHZT.requestFocus();
    }
  }

  void findNSTAVKA() {
      nStavka=rbr.vrati_rbr(getDetailSet().getTableName(),"where cshzt = '"+jraCSHZT.getText()+"'");
  }
  public boolean ValidacijaDetail( char mode) {
    if (mode=='N') {
        findNSTAVKA();
        getDetailSet().setShort("rbr",nStavka);
        getDetailSet().setBigDecimal("PZT",parseTextField(jlrPZT1.getText()));
        getDetailSet().setBigDecimal("IZT",parseTextField(jltIZT.getText()));
    }
    return true;
  }

  private QueryDataSet QDSSTAVKE = new QueryDataSet();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();

  public void AfterSaveDetail(char mode){
    RecalcMaster();
  }

  public void SetFokusDetail(char mode){
    rCC.setLabelLaF(jlrPZT1, false);
    jlrCZT1.requestFocus();
    if(mode=='N')
      this.jlrPZT1.setText("");
  }

  public void RecalcMaster(){

    java.math.BigDecimal tmpBD = new java.math.BigDecimal(0);
    java.math.BigDecimal tmpID = new java.math.BigDecimal(0);

    String queryString = rdUtil.getUtil().recMaster(getMasterSet().getString("CSHZT"), 'Z') ;
    if (QDSSTAVKE.isOpen())
      QDSSTAVKE.close();
    QDSSTAVKE.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    QDSSTAVKE.executeQuery();
    QDSSTAVKE.first();

    do {
        tmpBD=tmpBD.add(QDSSTAVKE.getBigDecimal("PZT"));
        tmpID=tmpID.add(QDSSTAVKE.getBigDecimal("IZT"));
    }
    while (QDSSTAVKE.next());

    getMasterSet().setBigDecimal("ZTPUK",tmpBD);
    getMasterSet().setBigDecimal("ZTIUK",tmpID);
    getMasterSet().saveChanges();
    this.jlrNAZZT1.setText("");
  }
  public void AfterDeleteDetail(){
    RecalcMaster();
  }

  void jraNSHZT_actionPerformed(ActionEvent e) {
  }

  public boolean ValidacijaMaster(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().isEmpty(jraCSHZT)) {
        this.jraCSHZT.requestFocus();
        return false;
      } else if (hr.restart.util.Valid.getValid().isEmpty(jraNSHZT)) {
        this.jraNSHZT.requestFocus();
        return false;
      } else {
        if(hr.restart.util.Valid.getValid().notUnique(jraCSHZT)) {
          return false;
        } else  return true;
      }
    }
    return true;
  }

   public boolean DeleteCheckDetail() {
    return true;
  }


  //prepare report
  static public QueryDataSet qDS = new QueryDataSet();
  static public QueryDataSet getQuery() {
    return qDS;
  }
  public void prepareStavke(String qStr) {
    qDS.close();
    qDS.closeStatement();
    qDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
    qDS.open();
  }
  public void Funkcija_ispisa_detail() {
      String qStr = rdUtil.getUtil().detailZT(this.jraCSHZT.getText());
      prepareStavke(qStr);
      super.Funkcija_ispisa_detail();
  }
  public void Funkcija_ispisa_master() {
      String qStr = rdUtil.getUtil().masterZT();
      prepareStavke(qStr);
      super.Funkcija_ispisa_master();
  }

  private BigDecimal parseTextField (String valueStr) {
    String decPart = "";
    String intPart = "";
    String temp="";
    String parseStr="";
    int i = 0;
    do {
      parseStr = valueStr.substring(i,i+1);

      if (!(parseStr.equals(".") || (parseStr.equals(","))))
        temp = temp + parseStr;
      if (parseStr.equals(",")) {
        intPart = temp;
        temp = "";
      }
      i++;
    }while(i<valueStr.length());
    decPart = temp;
    BigDecimal returnValue = new BigDecimal(intPart + "." + decPart);
    return returnValue;
  }

  public boolean DeleteCheckMaster() {
    deleteStavke(this.getMasterSet().getString("CSHZT"));
    return true;
  }

  private void deleteStavke(String cshZt) {
    String qStr = rdUtil.getUtil().getShemeStavkeDelStr(cshZt,1);
    vl.runSQL(qStr);
  }

}