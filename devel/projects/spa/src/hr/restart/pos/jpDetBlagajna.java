/****license*****************************************************************
**   file: jpDetBlagajna.java
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
package hr.restart.pos;

import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class jpDetBlagajna extends JPanel {
  boolean gotFocus = false;
  private static jpDetBlagajna inst = null;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  XYLayout xYLayout2 = new XYLayout();
  JraLabel jLabel2 = new JraLabel();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JPanel jPanel3 = new JPanel();
  JLabel jLabel3 = new JLabel();
  XYLayout xYLayout4 = new XYLayout();
  JLabel jLabel4 = new JLabel();
  JraTextField jtfKOL = new JraTextField() {
    public void addNotify() {
      super.addNotify();
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ENTER_RELEASED, enternext ? enter2 : enter);
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ESC, esc);
    }
    public void valueChanged() {
      jtfPOPUST_focusLost(null);
    }
  };
  JraTextField jtfMC = new JraTextField();
  JraTextField jtfPOPUST = new JraTextField() {
    public void addNotify() {
      super.addNotify();
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ENTER_RELEASED, enter);
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ESC, esc);
    }
    public void valueChanged() {
      jtfPOPUST_focusLost(null);
    }
  };
  JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) afterCART();
    }
    public void setEnabled(boolean enab) {
      if (!enab) gotFocus = false;
      else gotFocus = true;
      super.setEnabled(enab);
    }
  };
  JlrNavField jrfNAZART = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull())
    	gotFocus = false;
      if (isLastLookSuccessfull()) afterCART();
    }
    
    public void setText(String s) {
    	//System.out.println("gotFocus"+gotFocus);
    	if (gotFocus) super.setText(s);
    }
  };
  JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) afterCART();
    }
    public void setEnabled(boolean enab) {
        if (!enab) gotFocus = false;
        else gotFocus = true;
        super.setEnabled(enab);
      }
  };

  JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull()) afterCART();
    }
    public void setEnabled(boolean enab) {
        if (!enab) gotFocus = false;
        else gotFocus = true;
        super.setEnabled(enab);
      }
  };
  JlrNavField jrfJM = new JlrNavField();

  JraTextField jtfIZNOS = new JraTextField();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JPanel jPanel4 = new JPanel();
  XYLayout xYLayout5 = new XYLayout();
  JPanel jPanel5 = new JPanel();
  XYLayout xYLayout6 = new XYLayout();
  JLabel jLabel5 = new JLabel();
  JPanel jPanel6 = new JPanel();
  JPanel jPanel7 = new JPanel();
  XYLayout xYLayout7 = new XYLayout();
  XYLayout xYLayout8 = new XYLayout();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JPanel jPanel8 = new JPanel();
  JPanel jPanel9 = new JPanel();
  JPanel jPanel10 = new JPanel();
  XYLayout xYLayout9 = new XYLayout();
  XYLayout xYLayout10 = new XYLayout();
  XYLayout xYLayout11 = new XYLayout();
  JraLabel jLabel12 = new JraLabel();
  JraLabel jLabel13 = new JraLabel();
  JraLabel jLabel14 = new JraLabel();
  String tCartSifparam, tSifParam;
  boolean enternext = false;
  
  ActionExecutor f8action = new ActionExecutor() {
    public void run() {
       pressF8((String) obj);
    }
  };
  
  KeyListener f8adapter = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() != e.VK_F8) return;
      String src = null;
      if (e.getSource() instanceof JlrNavField) {
        JlrNavField nav = (JlrNavField) e.getSource();
        f8action.invokeLater(nav.getColumnName());
      }
    }
  };
  
  char curMode;
  
  public jpDetBlagajna() {
    try {
      inst = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    enternext = "D".equals(frmParam.getParam("pos","enterPop", "D",
        "Tipka ENTER prvo ide na popust (D,N)", true));
    tCartSifparam= hr.restart.sisfun.frmParam.getParam("robno","indiCart");
    tSifParam = hr.restart.sisfun.frmParam.getParam("robno","focusCart");
    jrfCART.setColumnName("CART");
    if ("CART".equalsIgnoreCase(tCartSifparam)) {
      jrfCART.setVisCols(new int[] {0, 3, 4});
    }
    else if ("CART1".equalsIgnoreCase(tCartSifparam)) {
      jrfCART.setVisCols(new int[] {1, 3, 4});
    }
    else {
    	jrfCART.setVisCols(new int[] {2, 3, 4});
    }
    if (frmParam.getParam("robno", "ugoart", "N", "Samo artikli ugostiteljstva").equalsIgnoreCase("D")) {
      jrfCART.setRaDataSet(hr.restart.baza.dM.getDataModule().getArtikliRoba());
    } else {
      jrfCART.setRaDataSet(hr.restart.baza.dM.getDataModule().getArtikli());
    }
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZART,jrfJM,jrfCART1,jrfBC});
    jrfCART.setColNames(new String[] {"NAZART","JM","CART1","BC"});

    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setSearchMode(1);
    jrfNAZART.setNavProperties(jrfCART);
    jrfNAZART.setFocusLostOnShow(false);
    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(-1);
    jrfJM.setNavProperties(jrfCART);
    jrfJM.setFocusLostOnShow(false);
    jrfCART1.setColumnName("CART1");
    jrfCART1.setSearchMode(3);
    jrfCART1.setNavProperties(jrfCART);
    jrfCART1.setFocusLostOnShow(false);
    jrfBC.setColumnName("BC");
    jrfBC.setSearchMode(3);
    jrfBC.setNavProperties(jrfCART);
    jrfBC.setFocusLostOnShow(false);
    this.setLayout(xYLayout1);
    jPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel1.setLayout(xYLayout2);
    jLabel1.setFont(getLabelBrojRacFont());
    jLabel1.setForeground(Color.black);
    jLabel1.setText("BROJ");
    jLabel2.setFont(getBrojRacFont());
    jLabel2.setForeground(Color.black);
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("jLabel2");
    jLabel2.setColumnName("BRDOK");
    xYLayout1.setWidth(750);
    xYLayout1.setHeight(193);
    jPanel2.setLayout(xYLayout3);
    jPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel3.setLayout(xYLayout4);
    jLabel3.setText("Artikl");
    jLabel4.setText("Stavka");
    jtfIZNOS.setColumnName("IZNOS");
    jLabel10.setFont(getOrgSkladBlagUserFont());
    jLabel10.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel11.setFont(getOrgSkladBlagUserFont());
    jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
    jtfKOL.setColumnName("KOL");
    /*jtfKOL.addKeyListener(new jpDetBlagajna_jtfKOL_keyAdapter(this));*/
    jtfPOPUST.setColumnName("PPOPUST1");
    jtfPOPUST.addFocusListener(new jpDetBlagajna_jtfPOPUST_focusAdapter(this));
    /*jtfPOPUST.addKeyListener(new jpDetBlagajna_jtfPOPUST_keyAdapter(this));*/
    jtfMC.setColumnName("MC");
    jPanel4.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel4.setLayout(xYLayout5);
    jPanel5.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel5.setLayout(xYLayout6);
    jLabel5.setFont(getRightPanelLabelsFont());
    jLabel5.setText("UKUPNO");
    jPanel6.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel6.setLayout(xYLayout7);
    jPanel7.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel7.setLayout(xYLayout8);
    jLabel6.setFont(getRightPanelLabelsFont());
    jLabel6.setForeground(Color.black);
    jLabel6.setText("%");
    jLabel7.setFont(getRightPanelLabelsFont());
    jLabel7.setText("PLATITI");
    jPanel8.setLayout(xYLayout9);
    jPanel9.setLayout(xYLayout10);
    jPanel10.setLayout(xYLayout11);
    jPanel8.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel9.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel10.setBorder(BorderFactory.createRaisedBevelBorder());
    jLabel12.setColumnName("IZNOS");
    jLabel12.setFont(getRightPanelValuesFont());
    jLabel12.setForeground(Color.black);
    jLabel12.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel13.setColumnName("UIPOPUST2");
    jLabel13.setFont(getRightPanelValuesFont());
    jLabel13.setForeground(Color.black);
    jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel14.setColumnName("NETO");
    jLabel14.setForeground(Color.red);
    jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel14.setFont(getRightPanelValuesFont());
//    jtfPOP.setColumns(0);
    jtfPOP.setColumnName("UPPOPUST2");
    jtfPOP.addFocusListener(new jpDetBlagajna_jtfPOP_focusAdapter(this));
    
    JLabel labCART = new JLabel("Šifra");
    labCART.setHorizontalAlignment(SwingConstants.LEADING);
    JLabel labNAZART = new JLabel("Naziv");
    labNAZART.setHorizontalAlignment(SwingConstants.LEADING);
    JLabel labKOL = new JLabel("Kolièina");
    labKOL.setHorizontalAlignment(SwingConstants.TRAILING);
    JLabel labMC = new JLabel("Cijena");
    labMC.setHorizontalAlignment(SwingConstants.TRAILING);
    JLabel labIZNOS = new JLabel("Iznos");
    labIZNOS.setHorizontalAlignment(SwingConstants.TRAILING);
    JLabel labPOP = new JLabel("Pop.");
    labPOP.setHorizontalAlignment(SwingConstants.CENTER);
    
    /*jtfPOP.addKeyListener(new jpDetBlagajna_jtfPOP_keyAdapter(this));*/
    this.add(jPanel2,                         new XYConstraints(540, 80, 200, 105));
    this.add(jPanel3,             new XYConstraints(5, 80, 535, 105));
    jPanel3.add(jLabel3,     new XYConstraints(10, 22, -1, -1));
    jPanel3.add(jLabel4,   new XYConstraints(10, 67, -1, -1));
    if ("CART".equalsIgnoreCase(tCartSifparam)) {
      labCART.setText("Šifra");
        jPanel3.add(jrfCART,  new XYConstraints(100, 20, 100, -1));
        jPanel3.add(jrfCART1,  new XYConstraints(0, 20, 0, -1));
        jPanel3.add(jrfBC,  new XYConstraints(0, 20, 0, -1));
        jrfCART1.setVisible(false);
        jrfBC.setVisible(false);
        jrfCART.setExtraDirectColumn("BC");
        AWTKeyboard.registerKeyListener(jrfCART, f8adapter);
        AWTKeyboard.registerKeyListener(jrfNAZART, f8adapter);
    }
    else if ("CART1".equalsIgnoreCase(tCartSifparam)) {
      labCART.setText("Oznaka");
        jPanel3.add(jrfCART,  new XYConstraints(0, 20, 0, -1));
        jPanel3.add(jrfCART1,  new XYConstraints(100, 20, 100, -1));
        jPanel3.add(jrfBC,  new XYConstraints(0, 20, 0, -1));
        jrfCART.setVisible(false);
        jrfBC.setVisible(false);
        jrfCART1.setExtraDirectColumn("BC");
        AWTKeyboard.registerKeyListener(jrfCART1, f8adapter);
        AWTKeyboard.registerKeyListener(jrfNAZART, f8adapter);
    }
    else {
      labCART.setText("Barcode");
    	jPanel3.add(jrfCART,  new XYConstraints(0, 20, 0, -1));
    	jPanel3.add(jrfCART1,  new XYConstraints(0, 20, 0, -1));
    	jPanel3.add(jrfBC,  new XYConstraints(100, 20, 100, -1));
    	jrfCART.setVisible(false);
        jrfCART1.setVisible(false);
        AWTKeyboard.registerKeyListener(jrfBC, f8adapter);
        AWTKeyboard.registerKeyListener(jrfNAZART, f8adapter);
    }
    jPanel3.add(labCART,  new XYConstraints(102, 3, 98, -1));
    jPanel3.add(labNAZART,  new XYConstraints(207, 3, 253, -1));

    jPanel3.add(jrfNAZART,    new XYConstraints(205, 20, 255, -1));
    jPanel3.add(jtfKOL,   new XYConstraints(100, 65, 100, -1));
    this.add(jPanel1,                 new XYConstraints(5, 5, 535, 75));
    jPanel1.add(jLabel11,            new XYConstraints(20, 35, 460, -1));
    jPanel1.add(jLabel10,    new XYConstraints(20, 10, 460, -1));
    this.add(jPanel4,    new XYConstraints(540, 5, 200, 75));
    jPanel4.add(jLabel1,     new XYConstraints(10, 23, -1, -1));
    jPanel4.add(jLabel2,      new XYConstraints(80, 0, 110, 75));
    jPanel3.add(jtfPOPUST,   new XYConstraints(465, 65, 50, -1));
    jPanel3.add(jrfJM,   new XYConstraints(470, 20, 40, -1));
    
    jPanel3.add(labKOL,  new XYConstraints(100, 48, 98, -1));
    jPanel3.add(labMC,  new XYConstraints(205, 48, 108, -1));
    jPanel3.add(labIZNOS,  new XYConstraints(320, 48, 138, -1));
    jPanel3.add(labPOP,  new XYConstraints(465, 48, 48, -1));
    
    jPanel3.add(jtfMC,  new XYConstraints(205, 65, 110, -1));
    jPanel3.add(jtfIZNOS,    new XYConstraints(320, 65, 140, -1));
    jPanel2.add(jPanel5,          new XYConstraints(0, 0, 76, 34));
    jPanel5.add(jLabel5,      new XYConstraints(5, 7, -1, -1));
    jPanel2.add(jPanel6,       new XYConstraints(0, 34, 76, 34));
    jPanel6.add(jLabel6,   new XYConstraints(5, 7, -1, -1));
    jPanel6.add(jtfPOP,   new XYConstraints(20, 5, 40, -1));
    jPanel2.add(jPanel7,         new XYConstraints(0, 68, 76, 34));
    jPanel7.add(jLabel7,   new XYConstraints(5, 7, -1, -1));
    jPanel2.add(jPanel8,      new XYConstraints(76, 0, 122, 34));
    jPanel8.add(jLabel12,     new XYConstraints(0, -3/*7*/, 110, 34/*-1*/));
    jPanel2.add(jPanel9,     new XYConstraints(76, 34, 122, 34));
    jPanel9.add(jLabel13,     new XYConstraints(0, -3/*7*/, 110, 34/*-1*/));
    jPanel2.add(jPanel10,        new XYConstraints(76, 68, 122, 34));
    jPanel10.add(jLabel14,     new XYConstraints(0, -3/*7*/, 110, 34/*-1*/));
  }
  private Font getRightPanelValuesFont() {
//    return new java.awt.Font("Dialog", 1, 14);
    return new java.awt.Font("Dialog", 1, 22);
  }
  private Font getRightPanelLabelsFont() {
//    return new java.awt.Font("Dialog", 1, 11);
    return new java.awt.Font("Dialog", 1, 14);
  }
  private Font getOrgSkladBlagUserFont() {
//    return new java.awt.Font("Dialog", 1, 13);
    return new java.awt.Font("Dialog", 1, 16);
  }
  private Font getBrojRacFont() {
    return new java.awt.Font("Dialog", 1, 40);
  }
  private Font getLabelBrojRacFont() {
//    return new java.awt.Font("Dialog", 1, 20);
    return new java.awt.Font("Dialog", 1, 24);
  }
  void setDataSet(com.borland.dx.sql.dataset.QueryDataSet master, com.borland.dx.sql.dataset.QueryDataSet detail) {
    jtfKOL.setDataSet(detail);
    jtfMC.setDataSet(detail);
    jtfPOPUST.setDataSet(detail);
    jrfCART.setDataSet(detail);
    jrfCART1.setDataSet(detail);
    jrfBC.setDataSet(detail);
    jrfNAZART.setDataSet(detail);
    jrfJM.setDataSet(detail);
    jtfIZNOS.setDataSet(detail);
    jLabel2.setDataSet(master);
    jLabel12.setDataSet(master);
    jLabel13.setDataSet(master);
    jLabel14.setDataSet(master);
    jtfPOP.setDataSet(master);
  }
  public void afterCART() {
  }
  public void disab(char mode, boolean usluga) {
    curMode = mode;
    rcc.setLabelLaF(jrfCART, mode!='I');
    rcc.setLabelLaF(jrfCART1, mode!='I');
	rcc.setLabelLaF(jrfCART,mode!='I');
    rcc.setLabelLaF(jrfBC, mode!='I');
    if (!usluga) rcc.setLabelLaF(jrfNAZART, mode!='I');
    rcc.setLabelLaF(jtfKOL, mode!='N');
    rcc.setLabelLaF(jtfPOPUST, mode!='N');
    rcc.setLabelLaF(jtfMC, false);
    rcc.setLabelLaF(jtfIZNOS, false);
    rcc.setLabelLaF(jrfJM,false);
    rcc.setLabelLaF(jtfPOP, false);
    if (mode=='I') {
      jtfKOL.selectAll();
      jtfKOL.requestFocus();
    }
  }
  public void fokus(char mode) {
    if (tSifParam != null && tSifParam.equals("NAZIV"))
      jrfNAZART.requestFocusLater();
    else if ("CART".equalsIgnoreCase(tCartSifparam)) {
      	jrfCART.requestFocusLater();    }
      else if ("CART1".equalsIgnoreCase(tCartSifparam)) {
      	jrfCART1.requestFocusLater();    }
      else {
    	jrfBC.requestFocusLater();    }
  }

  /**
   * Tipke u polju koli\u010Dina
   * - ENTER radi saveChanges()
   * - ESC ulazi na cancel()
   */
  /*public void jtfKOL_keyReleased(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ESCAPE) {
      pressESC();
    }
    else if (e.getKeyCode()==e.VK_ENTER) {
      pressENTER();
    }
  }*/
  public void pressESC() {
  }
  public void pressENTER() {
  }
  public void pressENTERPOP() {
  }
  public void pressF8(String f8field) {
  }
  public void focLost() {
  }
  public void focLostPOP() {
  }
  
  KeyAction esc = new KeyAction() {
    public boolean actionPerformed() {
      pressESC();
      return true;
    }
  };
  
  KeyAction enter = new KeyAction() {
    public boolean actionPerformed() {
    	jtfKOL.maskCheck();
    	jtfPOPUST.maskCheck();
    	pressENTER();
      return true;
    }
  };
  
  KeyAction enter2 = new KeyAction() {
    public boolean actionPerformed() {
        jtfKOL.maskCheck();
        jtfPOPUST.requestFocus();
      return true;
    }
  };
  
  KeyAction enterpop = new KeyAction() {
    public boolean actionPerformed() {
    	pressENTERPOP();
      return true;
    }
  };

  void jtfPOPUST_focusLost(FocusEvent e) {
    focLost();
  }

  /*void jtfPOPUST_keyReleased(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ESCAPE) {
      pressESC();
    }
    else if (e.getKeyCode()==e.VK_ENTER) {
      pressENTER();
    }
    e.consume();
  }*/

  void jtfPOP_focusLost(FocusEvent e) {
    System.out.println("FocLostPop");
    if (!pop.equals(jtfPOP.getText())) focLostPOP();
    //ai:temp tweak zeljeznar
    grabFocusPOS();
  }
  /**
   * 
   */
  public static void grabFocusPOS() {
    if (frmMasterBlagajna.getInstance() == null) return;
    if (inst == null) return;
    Component[] cs = new Component[] {/*inst.jrfCART,inst.jrfCART1,inst.jrfBC,
        inst.jrfNAZART,inst.jrfJM,*/frmMasterBlagajna.getInstance().raDetail.getTab()};
    for (int i = 0; i < cs.length; i++) {
      if (cs[i].isEnabled() && cs[i].isShowing()) {
        System.out.println("cs["+i+"] is ENAbLed");
        System.out.println(" and showing = "+cs[i].isShowing());
        final Component comp = cs[i];
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            comp.requestFocus();
          }
        });
        break;
      }
    }
  }

  /*void jtfPOP_keyReleased(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ESCAPE) {
//      focLostPOP();
      pressESC();
    }
    else if (e.getKeyCode()==e.VK_ENTER) {
      System.out.println("Press Enter");
      pressENTERPOP();
    }
  }*/
  String pop;
  JraTextField jtfPOP = new JraTextField() {
    public void addNotify() {
      super.addNotify();
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ENTER_RELEASED, enterpop);
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ESC, esc);
    }
    public void valueChanged() {
      jtfPOP_focusLost(null);
    }
  };
  void jtfPOP_focusGained(FocusEvent e) {
    pop=jtfPOP.getText();
  }
}

/*class jpDetBlagajna_jtfKOL_keyAdapter extends java.awt.event.KeyAdapter {
  jpDetBlagajna adaptee;

  jpDetBlagajna_jtfKOL_keyAdapter(jpDetBlagajna adaptee) {
    this.adaptee = adaptee;
  }
  public void keyReleased(KeyEvent e) {
    adaptee.jtfKOL_keyReleased(e);
  }
}

class jpDetBlagajna_jtfPOPUST_keyAdapter extends java.awt.event.KeyAdapter {
  jpDetBlagajna adaptee;

  jpDetBlagajna_jtfPOPUST_keyAdapter(jpDetBlagajna adaptee) {
    this.adaptee = adaptee;
  }
  public void keyReleased(KeyEvent e) {
    adaptee.jtfPOPUST_keyReleased(e);
  }
}*/

class jpDetBlagajna_jtfPOPUST_focusAdapter extends java.awt.event.FocusAdapter {
  jpDetBlagajna adaptee;

  jpDetBlagajna_jtfPOPUST_focusAdapter(jpDetBlagajna adaptee) {
    this.adaptee = adaptee;
  }
  /*public void focusLost(FocusEvent e) {
    adaptee.jtfPOPUST_focusLost(e);
  }*/
}

class jpDetBlagajna_jtfPOP_focusAdapter extends java.awt.event.FocusAdapter {
  jpDetBlagajna adaptee;

  jpDetBlagajna_jtfPOP_focusAdapter(jpDetBlagajna adaptee) {
    this.adaptee = adaptee;
  }
  /*public void focusLost(FocusEvent e) {
    adaptee.jtfPOP_focusLost(e);
  }*/
  public void focusGained(FocusEvent e) {
    adaptee.jtfPOP_focusGained(e);
  }
}

/*class jpDetBlagajna_jtfPOP_keyAdapter extends java.awt.event.KeyAdapter {
  jpDetBlagajna adaptee;

  jpDetBlagajna_jtfPOP_keyAdapter(jpDetBlagajna adaptee) {
    this.adaptee = adaptee;
  }
  public void keyReleased(KeyEvent e) {
    adaptee.jtfPOP_keyReleased(e);
  }
}*/
