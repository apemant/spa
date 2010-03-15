/****license*****************************************************************
**   file: frmShemaRab.java
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
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raMasterDetail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmShemaRab extends raMasterDetail{

  hr.restart.baza.dM dm=hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST ST  = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.raCommonClass rCC= hr.restart.util.raCommonClass.getraCommonClass();

  JPanel jp = new JPanel();
  JPanel jpD = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jraCSHRAB = new JraTextField();
  JraTextField jraNSHRAB = new JraTextField();
  Rbr rbr = Rbr.getRbr();
  JlrNavField jlrCRAB1 = new JlrNavField(){
    public void after_lookUp() {
      Mafter_lookUp(jlrCRAB1,"CRAB1");
      jlrNAZRAB1.setCaretPosition(0);
    }
  };
  JlrNavField jlrNAZRAB1 = new JlrNavField();
  JlrNavField jltRABRAB = new JlrNavField();
  JlrNavField jlrPRAB1 = new JlrNavField();
  JraButton jbRAB1 = new JraButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JraTextField jlrUPRAB = new JraTextField();
  short nStavka = 1 ;
  Valid vl = Valid.getValid();


  public frmShemaRab() {

    try {
      dm.getShrab().open();
      dm.getRabati().open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    java.lang.String [] key={"CSHRAB"};

    this.setMasterSet(dm.getShrab());
    this.setDetailSet(dm.getvshrab_rab());
    this.setMasterKey(key);
    this.setDetailKey(key);
    this.setVisibleColsMaster(new int[] {0,1});
    this.setVisibleColsDetail(new int[] {2,3,4});

    this.raMaster.getRepRunner().addReport("hr.restart.robno.repShemaRab", "Formatirani ispis sheme popusta", 5);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repStavkaRab", "Formatirani ispis stavke popusta", 5);


    jp.setLayout(xYLayout1);
    jraCSHRAB.setDataSet(this.getMasterSet());
    jraCSHRAB.setColumnName("CSHRAB");
    jraNSHRAB.setDataSet(this.getMasterSet());
    jraNSHRAB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jraNSHRAB_actionPerformed(e);
      }
    });
    jraNSHRAB.setColumnName("NSHRAB");
    jLabel2.setText("Shema popusta");
    jLabel3.setText("Šifra");
    jLabel4.setText("Naziv");
    jlrPRAB1.setEnabled(false);
    jlrPRAB1.setDisabledTextColor(Color.black);
    jlrPRAB1.setEditable(false);
    jlrPRAB1.setHorizontalAlignment(SwingConstants.LEFT);
    jlrPRAB1.setEnableClearAll(false);
    jLabel5.setText("Posto");

    jp.setBackground(UIManager.getColor("control"));
    jp.setMinimumSize(new Dimension(550, 75));
    jp.setPreferredSize(new Dimension(555, 75));
    jpD.setMinimumSize(new Dimension(550, 90));
    jpD.setPreferredSize(new Dimension(550, 90));
    jLabel6.setText("%");
    xYLayout2.setWidth(550);
    xYLayout2.setHeight(90);
    jp.add(jLabel2,  new XYConstraints(15, 25, 105, 19));
    jp.add(jraCSHRAB,  new XYConstraints(150, 25, 50, -1));
    jp.add(jraNSHRAB,  new XYConstraints(205, 25, 335, -1));
    jp.add(jLabel3,  new XYConstraints(150, 4, 33, -1));
    jp.add(jLabel4,  new XYConstraints(205, 4, 107, -1));

    jpD.setLayout(xYLayout2);
    jLabel1.setText("Popust ");
    jlrCRAB1.setRaDataSet(dm.getRabati());
    jlrCRAB1.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZRAB1,jlrPRAB1,jltRABRAB});
    jlrCRAB1.setVisCols(new int[]{0,1});
    jlrCRAB1.setColNames(new String[] {"NRAB","PRAB","RABNARAB"});
    jlrCRAB1.setDataSet(this.getDetailSet());
    jlrCRAB1.setColumnName("CRAB");
    jlrCRAB1.setNavButton(this.jbRAB1);

    jlrNAZRAB1.setNavProperties(jlrCRAB1);
    jlrNAZRAB1.setSearchMode(1);
    jlrNAZRAB1.setColumnName("NRAB");

    jlrPRAB1.setNavProperties(jlrCRAB1);
    jlrPRAB1.setColumnName("PRAB");
    //jlrPRAB1.setDataSet(this.getDetailSet());

    //jlrPRAB1.setSearchMode(-1);
    jlrPRAB1.disable();
    jltRABRAB.setNavProperties(jlrCRAB1);
    jltRABRAB.setColumnName("RABNARAB");
    jltRABRAB.setDataSet(this.getDetailSet());
    jbRAB1.setText("...");
    /*jbRAB1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        jbRAB1_actionPerformed(e);
      }
    });*/
    jlrUPRAB.setDataSet(this.getMasterSet());
    jlrUPRAB.setColumnName("UPRAB");

    jpD.add(jLabel1,   new XYConstraints(15, 20, 76, -1));
    jpD.add(jlrCRAB1,   new XYConstraints(150, 20, 100, -1));
    jpD.add(jlrNAZRAB1,        new XYConstraints(255, 20, 263, -1));
    jpD.add(jlrPRAB1,      new XYConstraints(150, 45, 60, -1));
    jpD.add(jLabel5,   new XYConstraints(15, 45, 64, -1));
    jpD.add(jbRAB1,    new XYConstraints(518, 20, 22, 22));
    jpD.add(jLabel6,  new XYConstraints(215, 47, 25, -1));
      this.setNaslovMaster("Zaglavlje sheme popusta");
      this.setNaslovDetail("Stavka sheme popusta");
    this.setJPanelMaster(jp);
    this.setJPanelDetail(jpD);

    //this.raMaster.getRepRunner().addReport("hr.restart.robno.repShemaRab", "Formatirani ispis sheme rabata");
  }
  /*public void jbRAB1_actionPerformed(java.awt.event.ActionEvent e){
    jlrCRAB1.keyF9Pressed();
  }*/
  public void Mafter_lookUp(JlrNavField jnf,String what){}

  public void SetFokusMaster( char mode){
    if (mode =='N'){
      rCC.setLabelLaF(jraCSHRAB,true);
      jraCSHRAB.requestFocus();
    }
    else if (mode=='I'){
      rCC.setLabelLaF(jraCSHRAB,false);
      jraNSHRAB.requestFocus();
    }
  }

  void findNSTAVKA() {
      nStavka=rbr.vrati_rbr(getDetailSet().getTableName(),"where cshrab = '"+jraCSHRAB.getText()+"'");
  }

  public boolean ValidacijaDetail( char mode)
  {
    if (mode=='N')
    {
      findNSTAVKA();
      getDetailSet().setShort("rbr",nStavka);
      getDetailSet().setBigDecimal("PRAB", Aus.getDecNumber(jlrPRAB1.getText()));
      getDetailSet().setString("CRAB",  jlrCRAB1.getText());
      getDetailSet().setString("RABNARAB",  jltRABRAB.getText());
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
    System.out.println("w: " + this.getWidth());
   System.out.println("h: " + this.getHeight());

    rCC.setLabelLaF(jlrPRAB1, false);
    jlrCRAB1.requestFocus();
    if(mode=='N')
      this.jlrPRAB1.setText("");
     //this.jlrPRAB1.emptyTextFields();
  }

  public void RecalcMaster(){

    java.math.BigDecimal tmpBD = new java.math.BigDecimal(0);

	java.math.BigDecimal tmpBD2 = new java.math.BigDecimal(0); // tomo
    //String queryString = "select * from vshrab_rab where cshrab = '"+getMasterSet().getString("CSHRAB")+"' " + "order by rbr";
    String queryString = rdUtil.getUtil().recMaster(getMasterSet().getString("CSHRAB"), 'R') ;
    if (QDSSTAVKE.isOpen())
      QDSSTAVKE.close();
    QDSSTAVKE.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    QDSSTAVKE.executeQuery();
    QDSSTAVKE.first();
    do {
      if (QDSSTAVKE.getString("RABNARAB").equals("D")){
      // dodao Tomo
      	tmpBD2 = (new BigDecimal("100.00").subtract(tmpBD)).multiply(
          QDSSTAVKE.getBigDecimal("PRAB"));
      	tmpBD2 = tmpBD2.divide(BigDecimal.valueOf(100),2,2);
      	tmpBD=tmpBD.add(tmpBD2);
      // i ovo Tomo kraj

        // tmpBD=tmpBD.add(tmpBD.multiply(QDSSTAVKE.getBigDecimal("PRAB").divide(new BigDecimal(100),2)));
      }
      else {
        tmpBD=tmpBD.add(QDSSTAVKE.getBigDecimal("PRAB"));
      }

    }
    while (QDSSTAVKE.next());

    getMasterSet().setBigDecimal("UPRAB",tmpBD);
    getMasterSet().saveChanges();
    jlrNAZRAB1.setText("");

  }
  public void AfterDeleteDetail(){
    RecalcMaster();
  }

  void jraNSHRAB_actionPerformed(ActionEvent e) {
  }

  public boolean ValidacijaMaster(char mode)
  {
    if (mode=='N')
    {
      if (this.jraCSHRAB.getText().equals(""))
      {
        JOptionPane.showConfirmDialog(this.jp,"Obavezan unos šifre popusta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        this.jraCSHRAB.requestFocus();
        return false;
      }
      else if (this.jraNSHRAB.getText().equals(""))
      {
        JOptionPane.showConfirmDialog(this.jp,"Obavezan unos naziva popusta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        this.jraNSHRAB.requestFocus();
        return false;
      }
      else
      {
        if(hr.restart.util.Valid.getValid().notUnique(jraCSHRAB))
        {
          return false;
        }
        else
          return true;
      }
    }
    return true;
  }

  ////priprema za report
  static public QueryDataSet qDS = new QueryDataSet();
  XYLayout xYLayout1 = new XYLayout();
  static public QueryDataSet getQuery()
  {
    return qDS;
  }
  public void prepareStavke(String qStr)
  {
    qDS.close();
    qDS.closeStatement();
    qDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
    qDS.open();
  }
  public void Funkcija_ispisa_detail()
  {
    /*String qStr = "select  RABATI.NRAB AS NRAB, VSHRAB_RAB.CRAB AS CRAB, VSHRAB_RAB.PRAB AS PRAB, VSHRAB_RAB.RBR AS RBR, SHRAB.CSHRAB AS CSHRAB, SHRAB.NSHRAB AS NSHRAB, VSHRAB_RAB.RABNARAB AS RABNARAB, RABATI.IRAB AS IRAB, SHRAB.UPRAB AS UPRAB from SHRAB, VSHRAB_RAB, RABATI where "+
                   "SHRAB.CSHRAB = '"+this.jraCSHRAB.getText() +"' and SHRAB.CSHRAB= VSHRAB_RAB.CSHRAB and RABATI.CRAB = VSHRAB_RAB.CRAB";*/

      String qStr = rdUtil.getUtil().detailRAB(this.jraCSHRAB.getText());
      prepareStavke(qStr);
      super.Funkcija_ispisa_detail();
  }
  public void Funkcija_ispisa_master()
  {
    String qStr = rdUtil.getUtil().masterRAB();
    prepareStavke(qStr);
    super.Funkcija_ispisa_master();
  }

    private BigDecimal parseTextField (String valueStr)
  {
    String decPart = "";
    String intPart = "";
    String temp="";
    String parseStr="";
    int i = 0;
    do
    {
      parseStr = valueStr.substring(i,i+1);

      if (!(parseStr.equals(".") || (parseStr.equals(","))))
        temp = temp + parseStr;
      if (parseStr.equals(","))
      {
        intPart = temp;
        temp = "";
      }
      i++;
    }while(i<valueStr.length());
    decPart = temp;
    BigDecimal returnValue = new BigDecimal(intPart + "." + decPart);
    return returnValue;
  }

  public boolean DeleteCheckMaster()
  {
    deleteStavke(this.getMasterSet().getString("CSHRAB"));
    return true;
  }

  private void deleteStavke(String cshRab)
  {
    String qStr = rdUtil.getUtil().getShemeStavkeDelStr(cshRab,0);
//    String qStr = "delete from vshrab_rab where cshrab='"+cshRab+"'";
    vl.runSQL(qStr);
  }
}