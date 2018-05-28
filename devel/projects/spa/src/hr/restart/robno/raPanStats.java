/****license*****************************************************************
**   file: raPanStats.java
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
import hr.restart.baza.Condition;
import hr.restart.baza.Pjpar;
import hr.restart.baza.Vrart;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;
import hr.restart.util.reports.JasperHook;
import hr.restart.util.reports.raReportDescriptor;
import hr.restart.zapod.OrgStr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public abstract class raPanStats extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  HashMap nazarts = new HashMap();
  HashMap nazPars = new HashMap();

  
  JPanel jp = new JPanel();
  TableDataSet fieldSet = new TableDataSet();

//  Column column1 = new Column();
//  Column column2 = new Column();
//  Column column3 = new Column();
//  Column column4 = new Column();
//  Column column5 = new Column();
//  Column column6 = new Column();
//  Column column7 = new Column();
//  Column column8 = new Column();

  protected XYLayout xYLayout3 = new XYLayout();

  JLabel jlDatum1 = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  jpCpar jpKup = new jpCpar(){
    public void afterLookUp(boolean succ) {
      HandlePjPar(succ);
//      System.out.println("AFRET LOOKUP JPKUP!!!! " + succ);
    }
  };
  jpCpar jpDob = new jpCpar() {
    public void afterLookUp(boolean succ) {
      if (succ) jtfPocDatum.requestFocus();
    }
  };

  public String[] moonshine = new String[] {"Sijeèanj","Veljaèa","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"};

  public JlrNavField jlrCPjPar = new JlrNavField() {
    public void after_lookUp() {

//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(fieldSet);

      if (isLastLookSuccessfull()){
        jpDob.cpar.requestFocus();//System.out.println("request fuckin fokus!!!");
      }

    }
  };
  public JlrNavField jlrNazPjPar = new JlrNavField() /*{
    public void after_lookUp() {
    }
  }*/;
  JraButton jrbPjPar = new JraButton();
  JLabel jlPjPar = new JLabel("Poslovna jedinica");

  protected raComboBox rcmbVrArt = new raComboBox();
  protected JLabel jlVrArt = new JLabel();

  protected raComboBox rcmbPrikaz = new raComboBox();
  protected raComboBox rcmbPoCemu = new raComboBox();

  protected raComboBox rcmbSljed = new raComboBox();
  protected JLabel jlSljed = new JLabel();

  protected rapancskl1 rpcskl = new rapancskl1(false,350) {
    public void findFocusAfter() {
//      System.out.println("rapancskl - findfocusafter");
      rpcart.setDefParam();
//      rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//      rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
//      rpcart.setCART();
//      jraCPARdobavljac.requestFocus();
    }
    public void MYpost_after_lookUp(){
//      System.out.println("rapancskl - mypostafterlookup");
//      rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//      rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
//      rpcart.setCART();
    	jlrAgent.requestFocus();
      //jpKup.focusCpar();
    }
  };

  protected rapancart rpcart = new rapancart(){
  	public QueryDataSet getRaDataSet() {
  		return Artikli.getDataModule().copyDataSet();
  	};
    public void metToDo_after_lookUp() {
//      System.out.println("rapancart - metodoafterlookup");

    }
    public void findFocusAfter() {
      rcmbSljed.requestFocus();
//      System.out.println("rapancart - findfocusafter");
//      rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//      rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
    }
    public boolean isUsluga() {
      return false;
    }
    
  };

  JLabel jlCorg = new JLabel();

  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {
        newCorg();
        enabCorg(false);
      }
    }
  };

  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {
        newCorg();
        enabCorg(false);
      }
    }
  };
  
  JlrNavField jlrAgent = new JlrNavField();
  JlrNavField jlrNazAgent = new JlrNavField();

  JraButton jbSelCorg = new JraButton();

  public raPanStats() {
    try {
      jbInit();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
  public abstract String navDoubleClickActionName();
  public abstract int[] navVisibleColumns();
  public abstract void okPress();

  public void firstESC() {
    rpcart.EnabDisab(true);
    rpcart.clearFields();
    if (jpDob.isDisabled()) {
      getJPTV().clearDataSet();
      removeNav();
      rcc.setLabelLaF(jlrAgent, true);
      rcc.setLabelLaF(jlrNazAgent, true);
      rcc.setLabelLaF(jlrAgent.getNavButton(), true);
      rcc.setLabelLaF(jtfPocDatum, true);
      rcc.setLabelLaF(jtfZavDatum, true);
      rcc.setLabelLaF(rcmbPrikaz, true);
      rcc.setLabelLaF(rcmbPoCemu, fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ") /*true*/);
      rcc.setLabelLaF(rcmbSljed, /*!fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ")*/ true);
      rcc.setLabelLaF(rcmbVrArt, true);
      jpKup.EnabDisabAll(true);
      jpDob.EnabDisabAll(true);
//      rpcart.EnabDisab(true);
//      rpcart.clearFields();
      if (!jpDob.isEmpty()) {
        jpDob.clear();
        jpDob.focusCparLater();
      } else if (!jpKup.isEmpty()){
        jpKup.clear();
        jpKup.focusCparLater();
      } else if (!jlrAgent.isEmpty()){
      	jlrAgent.setText("");
      	jlrAgent.forceFocLost();
      	jlrAgent.requestFocusLater();
      } else {
        rpcskl.disabCSKL(true);
        if (jlrCorg.isEmpty()) enabCorg(true);
        rpcskl.Clear();
        rpcskl.jrfCSKL.requestFocus();
//        rpcart.EnabDisab(true);
//        rpcart.Clear();
      }
    } else if (!jpDob.isEmpty()) {
      jpDob.EnabDisabAll(true);
      jpDob.clear();
      jpDob.focusCparLater();
    } else if (!jpKup.isEmpty()){
      jpKup.EnabDisabAll(true);
      jpKup.clear();
      jpKup.focusCparLater();
    } else if (!rpcskl.getCSKL().equals("")){
      rpcskl.disabCSKL(true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocus();
      rpcart.EnabDisab(true);
//      rpcart.Clear();
    } else {
    	jlrAgent.setText("");
    	jlrAgent.forceFocLost();
      System.out.println("NJULI DODATO - comboboxes dischardzing");
      rcmbPrikaz.setSelectedIndex(0);
      rcmbPoCemu.setSelectedIndex(0);
      rcmbVrArt.setSelectedIndex(0);
      fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
      fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
      enabCorg(true);
      jlrCorg.requestFocus();
    }
  }

  public boolean runFirstESC() {
//    return !rpcskl.getCSKL().equals("");
    return jpDob.isDisabled() || !jlrCorg.isEmpty();
  }

//  public boolean isIspis(){
//    return false;
//  }

  public void componentShow() {
//    this.changeIcon(1);
//    rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//    rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
    showDefaultValues();
    jpKup.focusCparLater();
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(fieldSet);
  }

  protected void showDefaultValues() {
    fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
//    jraCPARdobavljac.setText("");
//    jraNAZPARdobavljac.setText("");
    jpKup.clear();
    jpDob.clear();
    jlrAgent.setText("");
  	jlrAgent.forceFocLost();
    rpcskl.setCSKL(hr.restart.sisfun.raUser.getInstance().getDefSklad());
//    System.out.println("hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG() - " + hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
//    System.out.println(fieldSet.getString("CORG"));
//    jlrCorg.setText(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    jlrCorg.forceFocLost();
    enabCorg(false);
    rcmbPrikaz.setSelectedIndex(0);
    rcmbPoCemu.setSelectedIndex(0);
    rcmbSljed.setSelectedIndex(0);
    rcmbVrArt.setSelectedIndex(0);
  }

  public boolean podgrupe = false;

  public boolean Validacija(){
    /* 
     public static boolean checkDateRange(JraTextField begField, JraTextField endField) {
    Timestamp beginDate = begField.getDataSet().getTimestamp(begField.getColumnName());
    Timestamp endDate = endField.getDataSet().getTimestamp(endField.getColumnName());
    if (beginDate.after(Valid.getValid().getToday())) {
      begField.requestFocus();
      JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
        "Po\u010Detni datum ve\u0107i od današnjeg !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (endDate.after(Valid.getValid().getToday())) {
      endField.requestFocus();
      JOptionPane.showMessageDialog(endField.getTopLevelAncestor(),
        "Završni datum ve\u0107i od današnjeg !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (endDate.before(beginDate)) {
      begField.requestFocus();
      JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
        "Po\u010Detni datum ve\u0107i od završnog !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  } 
    */
    
    /*if (fieldSet.getTimestamp("zavDatum").before(fieldSet.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showMessageDialog(jtfPocDatum.getTopLevelAncestor(),
        "Po\u010Detni datum ve\u0107i od završnog !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    } else *//*if (!Valid.getValid().findYear(fieldSet.getTimestamp("pocDatum")).
        		equalsIgnoreCase(Valid.getValid().findYear(fieldSet.getTimestamp("zavDatum")))){
      jtfPocDatum.requestFocus();
      JOptionPane.showMessageDialog(jtfPocDatum.getTopLevelAncestor(),
        "Period nije u istoj godini !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    
    
    
    
    
    /*if (jlrCorg.getText().equals("")) {
      jlrCorg.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(),"Obvezatan unos - ORG. JEDINICA !","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
//    if (rpcskl.getCSKL().equals("")) {
//      rpcskl.jrfCSKL.requestFocus();
//      JOptionPane.showMessageDialog(this.getWindow(),"Obvezatan unos - SKLADIŠTE !","Greška",JOptionPane.ERROR_MESSAGE);
//      return false;
//    }
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.getWindow(),"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) return false;
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setJPan(jp);

    rpcskl.setDisabAfter(true);

    rpcart.setBorder(null);
    rpcart.setMode("DOH");
    rpcart.setReportMode();
//    rpcart.setAllowUsluga(false);

    /*Column column1 = dm.createTimestampColumn("pocDatum");
    Column column2 = dm.createTimestampColumn("zavDatum");
    Column column3 = dm.createStringColumn("VRART",2);
    Column column4 = dm.createStringColumn("PRIKAZ",3);
    Column column5 = dm.createStringColumn("SLJED",10);
    Column column6 = dm.createStringColumn("CORG",5);
    Column column7 = dm.createStringColumn("PJ",5);
    Column column8 = dm.createStringColumn("POCEMU",10);
    Column column9 = dm.createStringColumn("CAGENT",4);

    fieldSet.setColumns(new Column[] {column1, column2, column3, column4, column5, column6, column7, column8, column9});*/
    
    fieldSet.setColumns(new Column[] {dm.createTimestampColumn("pocDatum"), 
								        dm.createTimestampColumn("zavDatum"), 
								        dm.createStringColumn("VRART",2), 
								        dm.createStringColumn("PRIKAZ",3), 
								        dm.createStringColumn("SLJED",10), 
								        dm.createStringColumn("CORG",5), 
								        dm.createStringColumn("PJ",5), 
								        dm.createStringColumn("POCEMU",10), 
								        dm.createIntColumn("CAGENT")});
    fieldSet.open();

    jlCorg.setText("Org. jedinica");
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getSharedKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlrCPjPar.setColumnName("PJ");
    jlrCPjPar.setDataSet(fieldSet);
    jlrCPjPar.setColNames(new String[] {"NAZPJ"});
    jlrCPjPar.setTextFields(new JTextComponent[] {jlrNazPjPar});
    jlrCPjPar.setVisCols(new int[] {1, 2});
    jlrCPjPar.setSearchMode(0);
//    jlrCPjPar.setRaDataSet(dM.getDataModule().getPartneri());
    jlrCPjPar.setNavButton(jrbPjPar);

    jlrNazPjPar.setColumnName("NAZPJ");
    jlrNazPjPar.setNavProperties(jlrCPjPar);
    jlrNazPjPar.setSearchMode(1);
    

    jlrAgent.setDataSet(fieldSet);
    jlrAgent.setColumnName("CAGENT");
    jlrAgent.setDoh(dm.getAgenti(), new int[] {0,1,2});
    jlrAgent.addNav(jlrNazAgent, "NAZAGENT");

    jlDatum1.setText("Datum (od-do)");

    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(fieldSet);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(fieldSet);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    xYLayout3.setWidth(650);
    xYLayout3.setHeight(315);
    jp.setLayout(xYLayout3);

//    jraCPARdobavljac.setColumnName("CPAR");
//    jraCPARdobavljac.setColNames(new String[] {"NAZPAR"});
//    jraCPARdobavljac.setTextFields(new javax.swing.text.JTextComponent[] {jraNAZPARdobavljac});
//    jraCPARdobavljac.setVisCols(new int[]{0,1});
//    jraCPARdobavljac.setSearchMode(0);
//    jraCPARdobavljac.setRaDataSet(dm.getPartneri());
//    jraCPARdobavljac.setHorizontalAlignment(SwingConstants.LEFT);
//    jraCPARdobavljac.setNavButton(jraBtDohDob);
//
//    jraNAZPARdobavljac.setNavProperties(jraCPARdobavljac);
//    jraNAZPARdobavljac.setColumnName("NAZPAR");
//    jraNAZPARdobavljac.setSearchMode(1);

//    jlDobavljac.setText("Dobavljaè");


    StorageDataSet exvr = Vrart.getDataModule().getScopedSet("cvrart nazvrart");
    exvr.open();
    exvr.insertRow(false);
    exvr.setString("CVRART", "@");
    exvr.setString("NAZVRART", "Sve vrste");
    ut.fillReadonlyData(exvr, "select * FROM vrart WHERE aktiv='D'");

    rcmbVrArt.setRaDataSet(fieldSet);
    rcmbVrArt.setRaColumn("VRART");
    rcmbVrArt.setRaItems(exvr, "CVRART", "NAZVRART");
    rcmbVrArt.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        setRapancartDataset();
      }
    });
    
    jlVrArt.setText("Vrsta artikla");

    rcmbSljed.setRaDataSet(fieldSet);
    rcmbSljed.setRaColumn("SLJED");
    jlSljed.setText("Prikaz / Slijed");


    rcmbPrikaz.setRaDataSet(fieldSet);
    rcmbPrikaz.setRaColumn("PRIKAZ");


    rcmbPoCemu.setRaDataSet(fieldSet);
    rcmbPoCemu.setRaColumn("POCEMU");



    jpKup.setPartnerKup();
    jpDob.setPartnerDob();

    this.setJPan(jp);
    
    whereToGo(rpcskl.jrfCSKL);

//    jlrCorg.setNextFocusableComponent(rpcskl.jrfCSKL);


    jp.add(new JLabel("Šifra"), new XYConstraints(150,10,-1,-1));
    jp.add(new JLabel("Naziv"), new XYConstraints(255,10,-1,-1));
    
    jp.add(jlCorg, new XYConstraints(15, 30, -1, -1));
    jp.add(jlrCorg, new XYConstraints(150, 30, 100, -1));
    jp.add(jlrNaziv, new XYConstraints(255, 30, 350, -1));
    jp.add(jbSelCorg, new XYConstraints(610, 30, 21, 21));

    jp.add(rpcskl, new XYConstraints(0, 55, -1, -1));
    
    jp.add(new JLabel("Agent"), new XYConstraints(15, 80, -1, -1));
    jp.add(jlrAgent, new XYConstraints(150, 80, 100, -1));
    jp.add(jlrNazAgent, new XYConstraints(255, 80, 350, -1));
    jp.add(jlrAgent.getNavButton(), new XYConstraints(610, 80, 21, 21));
    
    jp.add(jpKup, new XYConstraints(0, 105, -1, -1));

    jp.add(jlPjPar, new XYConstraints(15,130,-1,-1));
    jp.add(jlrCPjPar, new XYConstraints(150, 130, 100, -1));
    jp.add(jlrNazPjPar, new XYConstraints(255, 130, 350, -1));
    jp.add(jrbPjPar, new XYConstraints(610, 130, 21, 21));

    jp.add(jpDob, new XYConstraints(0, 155, -1, -1));
    jp.add(jtfPocDatum, new XYConstraints(150, 180, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(255, 180, 100, -1));
    jp.add(jlDatum1, new XYConstraints(15, 182, -1, -1));
    jp.add(rcmbVrArt, new XYConstraints(455, 180, 150, 20));
    jp.add(jlVrArt, new XYConstraints(365, 182, 70, -1));
    jp.add(rpcart, new XYConstraints(0, 200, 630, 77));

//    jp.add(rpcskl, new XYConstraints(0, 0, -1, -1));
//    jp.add(jpKup, new XYConstraints(0, 50, -1, -1));
//    jp.add(jpDob, new XYConstraints(0, 75, -1, -1));
//    jp.add(jtfPocDatum, new XYConstraints(150, 100, 100, -1));
//    jp.add(jtfZavDatum, new XYConstraints(255, 100, 100, -1));
//    jp.add(jlDatum1, new XYConstraints(15, 100, -1, -1));
//    jp.add(rcmbVrArt, new XYConstraints(455, 100, 150, 20));
//    jp.add(jlVrArt, new XYConstraints(365, 102, 70, -1));
//    jp.add(rpcart, new XYConstraints(0, 122, 630, 81));

//    jp.add(jraBtDohDob, new XYConstraints(609, 50, 21, 21));
//    jp.add(jraNAZPARdobavljac, new XYConstraints(255, 50, 349, -1));
//    jp.add(jraCPARdobavljac, new XYConstraints(150, 50, 100, -1));
//    jp.add(jlDobavljac, new XYConstraints(15, 75, -1, -1)); // was 50

    jp.add(jlSljed,  new XYConstraints(15, 277, -1, -1));
    jp.add(rcmbPrikaz,  new XYConstraints(150, 277, 150, 21));
    jp.add(rcmbSljed,  new XYConstraints(305, 277, 150, 21));
    jp.add(rcmbPoCemu,  new XYConstraints(460, 277, 145, 21));
    
    jhook = new JasperHook() {
			public void adjustDesign(String reportName, JasperDesign design) {
				adjustJasper(design);
			}
		};

//    getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
  }

  void HandlePjPar(boolean handleIt){
    if (handleIt){
      QueryDataSet ls = Pjpar.getDataModule().getTempSet("cpar = '"+jpKup.getCpar()+"'");
      ls.open();
//      System.out.println("ls is null   - " +(ls == null));
//      System.out.println("ls row count - " + ls.rowCount());
      if (ls.rowCount() > 0) jlrCPjPar.setRaDataSet(ls);
        else {
          jpDob.focusCpar();
          return;
        }
    } else {
      fieldSet.setString("PJ","");
      jlrCPjPar.setText("");
      jlrCPjPar.emptyTextFields();
      jlrCPjPar.setRaDataSet(null);
    }
    rcc.setLabelLaF(jlrCPjPar,handleIt);
    rcc.setLabelLaF(jlrNazPjPar, handleIt);
    rcc.setLabelLaF(jrbPjPar, handleIt);
    if (handleIt) jlrCPjPar.requestFocus(); 
  }
  
  protected void newCorg(){
    
  }

  protected void enabCorg(boolean yesno) {
//    if (yesno == corgEnab) return;
//    corgEnab = yesno;
    if (yesno) {
//      oldCorg = tds.getString("CORG");
      jlrCorg.setText("");
      jlrNaziv.setText("");
    } else {
      nf.requestFocus();
//      System.out.println("naziv ima fokus");
//      jlrNaziv.forceFocLost();
//      System.out.println("naziv gubi fokus");
    }
    rcc.setLabelLaF(jlrCorg, yesno);
    rcc.setLabelLaF(jlrNaziv, yesno);
    rcc.setLabelLaF(jbSelCorg, yesno);
//    if (yesno && oldCorg != null) {
//      jlrCorg.setText(oldCorg);
//      jlrCorg.forceFocLost();
//    }
  }
  
  private Component nf;
  
  protected void whereToGo(Component toGo){
    nf = toGo;
  }

  protected String getStringCskl() {
    if (rpcskl.getCSKL().equals("")) return "";
    return "AND DOKI.CSKL='" + getCskl() + "' ";
  }

//  protected String getStringCkup() {
//    if(getCkup().equals("")) return "";
//    return "AND DOKI.CPAR='" + getCkup() + "' ";
//  }

//  protected String getStringCpar() {
//    if(getCpar().equals("")) return "";
//    return "AND DOKI.CPAR='" + getCpar() + "' ";
//  }

  public String getCkup() {
    return jpKup.cpar.getText();
  }
  public String getPjCkup(){
    return fieldSet.getString("PJ");
  }

  public String getNazKup() {
    if(getCkup().equals("")) return "Za sve kupce";
    return jpKup.getNazpar();
  }
  
  public String getAgent() {
  	return jlrAgent.getText();
  }
  
  public String getNazAgent() {
  	if (jlrAgent.isEmpty()) return "Za sve agente";
  	return jlrNazAgent.getText();
  }

  public String getCpar() {
    return jpDob.cpar.getText();
  }
  public String getNazPar() {
    if(getCpar().equals("")) return "Za sve dobavljaèe";
    return jpDob.getNazpar();
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }
  
  public String getCorg() {
    return jlrCorg.getText();
  }
  public String getCorgNaziv() {
    return jlrNaziv.getText();
  }

//  public int getCparDob() {
//    return Integer.parseInt(jraCPARdobavljac.getText());
//  }

  public java.sql.Timestamp getPocDatum() {
    return fieldSet.getTimestamp("pocDatum");
  }

  public java.sql.Timestamp getZavDatum() {
    return fieldSet.getTimestamp("zavDatum");
  }

  public String getSorter() {
  	if (fieldSet.getString("SLJED").equals("UKUPNO"))
  		return fieldSet.getString("POCEMU");
    return fieldSet.getString("SLJED");
  }
  public String getSlijed() {
    return rcmbSljed.getSelectedItem().toString();
  }
  public String getVrsteArt() {
    return rcmbVrArt.getSelectedItem().toString();
  }

  public void jptv_doubleClick() {

//  public void jptv_doubleClick(){
//    System.out.println("DOUBLE CLICKED1");
    super.jptv_doubleClick();
  }

  public int getWhatIsCart(){
    if (!rpcart.getCART().equals("")) return 1; // && rpcart.getCGRART().equals("") && rpcart.getCART1().equals("")){
    if (!rpcart.getCGRART().equals("")) return 2;
    if (!rpcart.getCART1().equals("")) return 3;

    return -1;
  }
  
  public boolean isInGroup(String[] grupe, String grupa){
    for (int i = 0; i < grupe.length; i++) {
      if (grupe[i].equals(grupa)) return true;
    }
    return false;
  }
    
  public void setRapancartDataset(){
    System.out.println("Vrsta artikla promjenjena"); //XDEBUG delete when no more needed
    //DataSet vrstaArtikalaDohvatFilterSet = null;
    String vra = fieldSet.getString("VRART");
    Condition cond = null;
    
    System.out.println("Vrsta artikala = "+ vra); //XDEBUG delete when no more needed
    
    rpcart.clearFields();
    if (vra.equals("@") || vra.equals(""))
      //vrstaArtikalaDohvatFilterSet = dm.getArtikli();
    	cond = Condition.none;
    else {
      // vrstaArtikalaDohvatFilterSet = Artikli.getDataModule().getFilteredDataSet("VRART='"+vra+"'");
    	cond = Condition.equal("VRART", vra);
    }
    
    /*rpcart.jrfCART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfCART1.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfBC.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfNAZART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfJM.setRaDataSet(vrstaArtikalaDohvatFilterSet);*/
    rpcart.jrfCART.refilterRaDataSet(cond);
    rpcart.jrfCART1.refilterRaDataSet(cond);
    rpcart.jrfBC.refilterRaDataSet(cond);
    rpcart.jrfNAZART.refilterRaDataSet(cond);
    rpcart.jrfJM.refilterRaDataSet(cond);
    
  }

  JasperHook jhook;
  protected void addHook(String rep) {
  	getRepRunner().addJasperHook(rep, jhook);
	}

  protected void adjustJasper(JasperDesign design) {
  	System.out.println("adjusting " + design.getGroups().length);
  	
  	JRDesignBand band = null;
  	JRElement[] elems = null;
  	JRDesignStaticText sklad = null, org = null;
  	JRDesignTextField cskl = null, nazskl = null;
  	for (int b = -1; b < design.getGroups().length; b++) {
  		band = b == -1 ? (JRDesignBand) design.getTitle() : (JRDesignBand) design.getGroups()[b].getGroupHeader();
  		elems = band.getElements();
  		for (int i = 0; i < elems.length; i++) 
	  		if (elems[i] instanceof JRDesignStaticText) {
	  			JRDesignStaticText lab = (JRDesignStaticText) elems[i];
	  			if (lab.getText().startsWith("Skladište")) sklad = lab;
	  			else if (lab.getText().startsWith("Org.")) org = lab;
	  		} else if (elems[i] instanceof JRDesignTextField) {
	  			JRDesignTextField tx = (JRDesignTextField) elems[i];
	  			if (tx.getExpression().getText().indexOf("CSKL") >= 0) cskl = tx;
	  			else if (tx.getExpression().getText().indexOf("NAZSKL") >= 0) nazskl = tx;
	  		}
  		if (sklad != null && org != null && cskl != null && nazskl != null) break;
  	}
  	if (sklad == null || org == null || cskl == null || nazskl == null) return;
  	  	
  	int drop = sklad.getY() - org.getY();
  	int line = sklad.getY() + drop;
  	for (int i = 0; i < elems.length; i++)
  		if (elems[i].getY() >= line) ((JRDesignElement) elems[i]).setY(elems[i].getY() + drop);
  	
  	JRDesignStaticText alab = (JRDesignStaticText) sklad.clone(band);
  	alab.setY(alab.getY() + drop);
  	alab.setText("Agent:");
  	band.addElement(alab);
  	
  	JRDesignTextField agent = (JRDesignTextField) cskl.clone(band);
  	agent.setY(agent.getY() + drop);
  	JRDesignExpression ae = new JRDesignExpression();
  	ae.setValueClass(String.class);
  	ae.addFieldChunk("Agent");
  	agent.setExpression(ae);
  	band.addElement(agent);
  	
  	JRDesignTextField nazagent = (JRDesignTextField) nazskl.clone(band);
  	nazagent.setY(nazagent.getY() + drop);
  	JRDesignExpression nae = new JRDesignExpression();
  	nae.setValueClass(String.class);
  	nae.addFieldChunk("NazAgent");
  	nazagent.setExpression(nae);
  	band.addElement(nazagent);
  	
  	band.setHeight(band.getHeight() + drop);
  	design.setProperty("data.noremove", "true");
  }

}
