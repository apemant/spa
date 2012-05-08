/****license*****************************************************************
**   file: raDDodRab.java
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
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raDDodRab extends JraDialog {
/**
 * TO-DO
 * chackbox za rabnaran
 * brisanje rabata
 * escape,f10 +
 */

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  JPanel panelDP = new JPanel();
  XYLayout xyl = new XYLayout();
  raFilteriRazni rFR = new raFilteriRazni();
  raFilteriRazni.fVshrab_rab filter;
  raMasterDetail fDI;
  hr.restart.util.raCommonClass rCC= hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  QueryDataSet rabatTMP = new QueryDataSet();
  StorageDataSet rabatTMP4Browse = new StorageDataSet();
  StorageDataSet rabatTMP4Browse2 = new StorageDataSet();
  String queryString = "";
  short brojac_sloga;
  BigDecimal sp = Aus.zero2;

  String greska = "Prvi u nizu popusta ne smije imat oznaku rabnarab = D "+
          "Pobriši stavke popusta i unesi ispravno ";

  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
      pressOK();
    }
    public void jPrekid_actionPerformed(){
      pressCancel();
    }
  };
  raNavAction rnvDelete = new raNavAction("Brisanje",raImages.IMGDELETE,java.awt.event.KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        delete_action();
      }
  };
  public void delete_action(){
    if (!rabatTMP4Browse.isEmpty()) {
      sp = BigDecimal.valueOf(0);
      rabatTMP4Browse.deleteRow();

      if (!rabatTMP4Browse.isEmpty())    renumeracija();
      sumaPopusta();
      ((hr.restart.swing.JraTable2)rjp.getMpTable()).fireTableDataChanged();
    }
  }

/*  public void odi_gore(Frame frame,  String title, boolean modal) {
    super(frame, title, modal);
  }
*/
  public void renumeracija(){
    brojac_sloga = 0;
    rabatTMP4Browse.first();
    do
    {
      brojac_sloga ++;
      rabatTMP4Browse.setShort("LRBR",brojac_sloga);
      addPopust();
    }
    while  (rabatTMP4Browse.next());
  }

  public void pressOK(){
    if (!jlrCRAB1.getText().equals("")){
      brojac_sloga ++;
      insertTempRow(brojac_sloga,
                    jlrCRAB1.getText(),
                    jlrNAZRAB1.getText(),
                    rabatTMP4Browse2.getBigDecimal("PRAB"),
                    /*parseTextField(jlrPRAB1.getText()),*/
                    jcrabnarab.isSelected() && rabatTMP4Browse.rowCount() > 0 ? "D"  :"N");
      rabatTMP4Browse.first();
      if (rabatTMP4Browse.getShort("LRBR")!=1) renumeracija();
      sumaPopusta();
      ((hr.restart.swing.JraTable2)rjp.getMpTable()).fireTableDataChanged();
      emptyRab4Add();
    }
 }
 public void emptyRab4Add(){
   jlrCRAB1.emptyTextFields();
   jlrCRAB1.setText("");
   jlrCRAB1.requestFocus();
 }
 public boolean isRnR() {
  return rabatTMP4Browse.getString("RABNARAB").equals("D");
 }
 public void pressCancel(){
    rabatTMP4Browse.first();
    if (isRnR())
    {
      javax.swing.JOptionPane.showMessageDialog(null,greska,
          "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    else {
      afterJob();
      this.hide();
    }
  }
  public void afterJob() {
//      fDI.jpRabat_afterJob();
  }
  public void  ESC_izlaz(){

    if (!jlrCRAB1.getText().equals("")) emptyRab4Add();
    else  pressCancel();
  }

  BorderLayout borderLayout1 = new BorderLayout();
  hr.restart.util.raJPTableView rjp = new hr.restart.util.raJPTableView(){
  	public boolean isFocusTraversable(){
  		return false;
  	}
  };
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrCRAB1 = new JlrNavField();
  JlrNavField jlrPRAB1 = new JlrNavField();
//  JraTextField jlrPRAB1 = new JraTextField();
  JlrNavField jlrNAZRAB1 = new JlrNavField();
  JlrNavField jlrIRAB1 = new JlrNavField();
  JlrNavField jlrRNR1 = new JlrNavField();
  JlrNavField jlrLOKK1 = new JlrNavField();
  JlrNavField jlrAKTIV1 = new JlrNavField();
  JLabel jLabel6 = new JLabel();
  JraButton jbRAB1 = new JraButton();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel1 = new JLabel();
  javax.swing.JCheckBox jcrabnarab = new javax.swing.JCheckBox("Popust na popust",true);
  JLabel jlUkupno = new JLabel();

  public raDDodRab(Frame frame, raMasterDetail fDI, String title, boolean modal) {
    super(frame, title, modal);
    try {
      dm.getVtrabat().open();
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    this.fDI = fDI;
  }

  public raDDodRab() {
    this(null, null, "", false);
  }

  void jbInit() throws Exception {
  	setupRabatTmp(rabatTMP4Browse);
    setupRabatTmp(rabatTMP4Browse2);
//    rCC.setLabelLaF(jlrPRAB1, false);
    rjp.setVisibleCols(new int[] {0,1,2,3,4});
    rjp.initKeyListener(this);
    rjp.setDataSet(rabatTMP4Browse);
    rjp.getNavBar().addOption(rnvDelete);
    rjp.getColumnsBean().setSaveName(getClass().getName());
    //rjp.getColumnsBean().initialize();

    this.addKeyListener(new java.awt.event.KeyAdapter(){
        public void keyPressed(java.awt.event.KeyEvent e){
          if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
            pressOK();
          }
          else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){
            ESC_izlaz();
          }
        }
    });
    panel1.setLayout(borderLayout1);
    panelDP.setLayout(xYLayout1);
    panelDP.setMinimumSize(new Dimension(555, 80));
    panelDP.setPreferredSize(new Dimension(555, 80));

    jlrCRAB1.setVisCols(new int[]{0,1});
    jlrCRAB1.setTextFields(new  JraTextField[] {jlrNAZRAB1, jlrPRAB1, jlrIRAB1});
    jlrCRAB1.setNavButton(jbRAB1);

//    jlrPRAB1.setEnabled(false);
//    jlrPRAB1.setDisabledTextColor(Color.black);
//    jlrPRAB1.setEditable(false);
    jlrPRAB1.setHorizontalAlignment(SwingConstants.RIGHT);
//    jlrPRAB1.setEnableClearAll(false);
    jlrPRAB1.setNavProperties(jlrCRAB1);
    jlrPRAB1.setSearchMode(JlrNavField.NULL, JlrNavField.NULL);
    jlrNAZRAB1.setNavProperties(jlrCRAB1);
    jlrNAZRAB1.setSearchMode(1);
    jlrIRAB1.setNavProperties(jlrCRAB1);
    jlrIRAB1.setSearchMode(1);
    jLabel6.setText("%");
    jLabel1.setText("Popust");
    jLabel5.setText("Posto");
    jcrabnarab.setHorizontalAlignment(SwingConstants.LEFT);
    jlUkupno.setForeground(Color.red);
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    
    this.addComponentListener(new ComponentAdapter(){
    	public void componentShown(ComponentEvent e){
    		postinit();
    		jlrCRAB1.requestFocus();
    	}
    });
    
    setContentPane(panel1);
    jbInit4overload();
    JPanel cp = new JPanel(new BorderLayout());
    cp.add(panelDP, BorderLayout.CENTER);
    cp.add(okp,BorderLayout.SOUTH);
    panel1.add(rjp,BorderLayout.CENTER);
    panel1.add(cp,BorderLayout.SOUTH);
    panelDP.setBorder(BorderFactory.createEtchedBorder());
    panelDP.add(jlrCRAB1,  new XYConstraints(150, 20, 100, -1));
    panelDP.add(jlrPRAB1,   new XYConstraints(150, 45, 60, -1));
    panelDP.add(jlrNAZRAB1,  new XYConstraints(255, 20, 263, -1));
    panelDP.add(jbRAB1,  new XYConstraints(523, 20, 22, 22));
    panelDP.add(jLabel5,   new XYConstraints(15, 45, -1, -1));
    panelDP.add(jLabel1,   new XYConstraints(15, 20, -1, -1));
    panelDP.add(jcrabnarab, new XYConstraints(255, 45, -1, -1));
    panelDP.add(jlUkupno, new XYConstraints(420, 48, 150, -1));
    //panel1.add(okp,BorderLayout.SOUTH);

//    jlrPRAB1.setEnabled(false);
  }
  public void jbInit4overload(){
  	jlrCRAB1.setDataSet(rabatTMP4Browse2);
    jlrCRAB1.setColumnName("CRAB");
    jlrCRAB1.setRaDataSet(dm.getRabati());
    jlrCRAB1.setColNames(new String[] {"NRAB","PRAB","IRAB"});
    jlrPRAB1.setColumnName("PRAB");
    jlrPRAB1.setDataSet(rabatTMP4Browse2);
    jlrNAZRAB1.setColumnName("NRAB");
    jlrIRAB1.setColumnName("IRAB");
  }

  public void postinit(){
//    rjp.setDataSet(rabatTMP4Browse);
//    jlrPRAB1.setDataSet(rabatTMP4Browse);
    Point rv = new Point();
    getParent().getLocation(rv);
    if (fDI != null) {
    	rv = fDI.raDetail.getLocation();
    }
    rv.x=(int )rv.getX()+90;
    rv.y=(int )rv.getY()+50;
    setLocation(rv);
    boolean packed = rjp.getColumnsBean().isInitialized(); 
    rjp.getColumnsBean().eventInit();
    ((hr.restart.swing.JraTable2)rjp.getMpTable()).fireTableDataChanged(); /// da nema praznih kolona
    if (!packed) pack();
  }
  
/*  
  public void show() {
//  	postinit();
    pack();
    super.show();€
    
  }
*/
  allSelect aSS = new allSelect();
  public void findRabat(int rbr) {
    rabatTMP.close();
    queryString = aSS.getQuery4rDDRB4findRabat(fDI.getDetailSet().getString("CSKL"),
                                    fDI.getDetailSet().getString("VRDOK"),
                                    fDI.getDetailSet().getString("GOD"),
                                    fDI.getDetailSet().getInt("BRDOK"),rbr);
    rabatTMP.closeStatement();
    rabatTMP.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    rabatTMP.open();
  }
  public void getMyDataSet()
  {
    if (fDI.raDetail.getMode()=='N')  findRabat(0);
    else  findRabat(fDI.getDetailSet().getInt("RBSID"));

    fillTmpRab();
  }

  public void setupRabatTmp(StorageDataSet sts){

    sts.close();
    Column lrbr_C = (Column) hr.restart.baza.dM.getDataModule().getVtrabat().getColumn("LRBR").clone();
    lrbr_C.setCaption("Redni broj");
    sts.setColumns(new Column[]
     {lrbr_C ,
      (Column) hr.restart.baza.dM.getDataModule().getVtrabat().getColumn("CRAB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getRabati().getColumn("NRAB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVtrabat().getColumn("PRAB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVtrabat().getColumn("RABNARAB").clone()
    });
    sts.open();    
    }
  public void fillTmpRab() {
    rabatTMP4Browse.open();
    rabatTMP4Browse.emptyAllRows();
    
    brojac_sloga=0;
    sp = BigDecimal.valueOf(0);
    if (!rabatTMP.isEmpty()){
    	for (rabatTMP.first();rabatTMP.inBounds();rabatTMP.next())   {
         transferFill();
    	}
    }
  }
  public void transferFill(){
System.out.println("transferFill()");  	
    insertTempRow(rabatTMP.getShort("LRBR"),
                  rabatTMP.getString("CRAB"),
                  rabatTMP.getString("NRAB"),
                  rabatTMP.getBigDecimal("PRAB"),
                  rabatTMP.getString("RABNARAB"));
    brojac_sloga=rabatTMP.getShort("LRBR");
  }

  public StorageDataSet getDPDataSet()
  {
    return rabatTMP4Browse;
  }

  public void insertTempRow(short lrbr, String crab,String nrab,
                             java.math.BigDecimal prab,String rabnarab )
  {
//  	System.out.println("insertTempRow");
    rabatTMP4Browse.insertRow(true);
    rabatTMP4Browse.setShort("LRBR",lrbr);
    rabatTMP4Browse.setString("CRAB", crab);
    rabatTMP4Browse.setString("NRAB", nrab);
    rabatTMP4Browse.setBigDecimal("PRAB", prab);
    rabatTMP4Browse.setString("RABNARAB", rabnarab);
    addPopust();
  }
  
  public void addPopust(){
    BigDecimal tmpBig = Aus.zero2;
  
    if (rabatTMP4Browse.getString("RABNARAB").equals("N"))
    {
      sp = sp.add(rabatTMP4Browse.getBigDecimal("PRAB"));
    }
    else
    {
      tmpBig = sp;
      tmpBig = (new BigDecimal("100.00").subtract(sp)).multiply(
          rabatTMP4Browse.getBigDecimal("PRAB"));
      tmpBig = tmpBig.divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP);
      sp = sp.add(tmpBig);
    }
    jlUkupno.setText("U K U P N O     "+sp);
  }
  public BigDecimal sumaPopusta()
  {

    sp = BigDecimal.valueOf(0);
    rabatTMP4Browse.first();
    do
    {
      addPopust();
    }
    while  (rabatTMP4Browse.next());
    return sp ;
  }

  /*private BigDecimal parseTextField (String valueStr)
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
  }*/
  public boolean isShemaPodstaveExist(boolean trazi){
    if (trazi) findRabat((int)fDI.getDetailSet().getInt("RBSID"));
    return !rabatTMP.isEmpty();
  }

  void this_windowClosing(WindowEvent e) {
    pressCancel();
  }
}