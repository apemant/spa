/****license*****************************************************************
**   file: jpUlazMaster.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.zapod.Tecajevi;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class jpUlazMaster extends JPanel {
  _Main main;
  frmUlazTemplate frm;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
//  dlgPregZT dPZT= new dlgPregZT(null,"Pregled zavisnih troškova",true);
  BorderLayout borderLayout1 = new BorderLayout();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JraTextField jtfUINAB = new JraTextField() {
    public void valueChanged() {
      jtfUINAB_focusLost(null);
    }
  };
  JraTextField jtfDEVIZN = new JraTextField() {
    public void valueChanged() {
      BigDecimal _tmp = frm.getMasterSet().getBigDecimal("UINAB");
      calcWithTecaj();
      if (_tmp.compareTo(frm.getMasterSet().getBigDecimal("UINAB"))!=0) {
        jtfUINAB.valueChanged();
      }
    }   
  };
  JPanel jpMasterCenter = new JPanel();
  JraTextField jtfDATDOKUL = new JraTextField();
  JlrNavField jrfCPAR = new JlrNavField(){
    public void after_lookUp(){
      CPARafter_lookUp();
    }
  };
  JLabel jlDVO = new JLabel();
  JraTextField jtfDATRAC = new JraTextField() {
    public void valueChanged() {
      jtfDATRAC_focusLost(null);
    }
  };
//  JlrNavField jrfNAZSHZT = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField(){
    public void after_lookUp(){
        CPARafter_lookUp();
      }
  };
  JlrNavField jrfDOSP = new JlrNavField();
  JPanel jpZT = new JPanel();
//  JlrNavField jrfCSHZT = new JlrNavField();
  JraTextField jtfUPZT = new JraTextField() {
    public void valueChanged() {
      jtfUPZT_focusLost(null);
    }
  };
  XYLayout xYLayout9 = new XYLayout();
  JraTextField jtfUIZT = new JraTextField() {
    public void valueChanged() {
      jtfUIZT_focusLost(null);
    }
  };
//  JraButton jbDetail = new JraButton();
  XYLayout xYLayout3 = new XYLayout();
  JTabbedPane jtabs = new JTabbedPane();
  JraTextField jtfDATDOK = new JraTextField() {
    public void valueChanged() {
      jtfDATDOK_focusLost(null);
    }
  };
  JLabel jlBRDOKUL = new JLabel();
  JLabel jlBRRAC = new JLabel();
  JLabel jlDATDOKUL = new JLabel();
  JraTextField jtfDVO = new JraTextField() {
    public void valueChanged() {
      jtfDVO_focusLost(null);
    }
  };
  JLabel jlCPAR = new JLabel();
  JLabel jlDATRAC = new JLabel();
  JLabel jlDATDOSP = new JLabel();
  JLabel jlUIPRPOR = new JLabel();
//  JraButton jbCSHZT = new JraButton();
  JLabel jlUINAB = new JLabel();
  JLabel jlUPZT = new JLabel();
  JraButton jbCPAR = new JraButton();
//  JrRadioButton jrbLinearniZT = new JrRadioButton();
  public jpGetValute jpGetVal = new hr.restart.zapod.jpGetValute();
//  JLabel jlCSHZT = new JLabel();
  JLabel jlUIZT = new JLabel();
  JLabel jlDATDOK = new JLabel();
//  JraTextField jtfBRDOKUL = new JraTextField();
//
//  JlrNavField jlrBRDOKUL = new JlrNavField();
//  JraButton jbBRDOKUL = new JraButton();

  jpBrdokul jpBRDOKUL;

  JraTextField jtfBRRAC = new JraTextField();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
//  JrRadioButton jrbShemaZT = new JrRadioButton();
  JraCheckBox jcbZT = new JraCheckBox();
  JraButton jbZT = new JraButton();

  JraTextField jtfDATDOSP = new JraTextField();
  JLabel jlPrpor = new JLabel("Pretporez");
  JLabel jlPrp1 = new JLabel("Stopa 25%");
  JLabel jlPrp2 = new JLabel("Stopa 10%");
  JLabel jlPrp3 = new JLabel("Stopa 5%");
  JraTextField jtfUIPRPOR = new JraTextField();
  JraTextField jtfUIPRPOR2 = new JraTextField();
  JraTextField jtfUIPRPOR3 = new JraTextField();
  rajpBrDok jpBRDOK = new rajpBrDok();
//  ButtonGroup buttonGroup1 = new ButtonGroup();
//  private JraButton jbULDOK = new JraButton();
  private int ver= 0;
  int oldcpar = -1;

  public jpUlazMaster(frmUlazTemplate fut,int ver) {
    frm=fut;
//    this.ver=ver;
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public jpUlazMaster(frmUlazTemplate fut) {
    frm=fut;
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    jpMasterCenter.setPreferredSize(new Dimension(650, 245));
    jpMasterCenter.setMinimumSize(new Dimension(650, 245));
    jpMasterCenter.setBorder(BorderFactory.createEtchedBorder());
    jpMasterCenter.setLayout(xYLayout3);
    /*jtfUINAB.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfUINAB_focusLost(e);
      }
    });*/
    jtfUINAB.setColumnName("UINAB");
    
    jtfDEVIZN.setColumnName("DEVIZN");
    this.setLayout(borderLayout1);
    jtfDATDOKUL.setColumnName("DATDOKUL");
    jtfDATDOKUL.setHorizontalAlignment(SwingConstants.CENTER);

    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setColNames(new String[] {"NAZPAR", "DOSP"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR, jrfDOSP});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setNavButton(jbCPAR);
    jlDVO.setText(res.getString("jlDVO_text"));
    /*jtfDATRAC.addFocusListener(new java.awt.event.FocusAdapter() {
		public void focusLost(FocusEvent e) {
		  jtfDATRAC_focusLost(e);
		}
	 });*/
    jtfDATRAC.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATRAC.setColumnName("DATRAC");
//    jrfNAZSHZT.setColumnName("NSHZT");
//    jrfNAZSHZT.setSearchMode(1);
//    jrfNAZSHZT.setNavProperties(jrfCSHZT);
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfDOSP.setColumnName("DOSP");
    jrfDOSP.setNavProperties(jrfCPAR);
    jrfDOSP.setVisible(false);
    jrfDOSP.setEnabled(false);
    jpZT.setBorder(BorderFactory.createEtchedBorder());
    jpZT.setMinimumSize(new Dimension(555, 124));
    jpZT.setPreferredSize(new Dimension(555, 124));
    jpZT.setLayout(xYLayout9);
//    jrfCSHZT.setColumnName("CSHZT");
//    jrfCSHZT.setColNames(new String[] {"NSHZT"});
//    jrfCSHZT.setVisCols(new int[]{0,1});
//    jrfCSHZT.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSHZT});
//    jrfCSHZT.setRaDataSet(dm.getShzavtr());
    jtfUPZT.setColumnName("UPZT");
    /*jtfUPZT.addFocusListener(new java.awt.event.FocusAdapter() {
		public void focusLost(FocusEvent e) {
		  jtfUPZT_focusLost(e);
		}
	 });*/
    xYLayout9.setWidth(555);
    xYLayout9.setHeight(120);
    jtfUIZT.setColumnName("UIZT");
    /*jtfUIZT.addFocusListener(new java.awt.event.FocusAdapter() {
		public void focusLost(FocusEvent e) {
		  jtfUIZT_focusLost(e);
		}
	 });*/
//    jbDetail.setText(res.getString("jbDetail_text"));
//    jbDetail.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jbDetail_actionPerformed(e);
//      }
//    });
    xYLayout3.setWidth(645);
    xYLayout3.setHeight(245);
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATDOK.setColumnName("DATDOK");
    /*jtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
		public void focusLost(FocusEvent e) {
		  jtfDATDOK_focusLost(e);
		}
	 });*/
    jlBRDOKUL.setText(res.getString("jlBRDOKUL_text"));
    jlBRRAC.setText(res.getString("jlBRRAC_text"));
    jlDATDOKUL.setText(res.getString("jlDATDOKUL_text"));
    jtfDVO.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDVO.setColumnName("DVO");
    /*jtfDVO.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfDVO_focusLost(e);
      }
    });*/
    jlCPAR.setText(res.getString("jlCPAR_text"));
    jlDATRAC.setText(res.getString("jlDATRAC_text"));
    jlDATDOSP.setText("Datum dospje\u0107a");
    jlUIPRPOR.setText(res.getString("jlUIPRPOR_text"));

    jlUINAB.setText(res.getString("jlUINAB_text"));
    jlUPZT.setText(res.getString("jlUPZT_text"));
    
    jlPrp1.setHorizontalAlignment(SwingConstants.CENTER);
    jlPrp2.setHorizontalAlignment(SwingConstants.CENTER);
    jlPrp3.setHorizontalAlignment(SwingConstants.CENTER);
//    jrbLinearniZT.setSelected(true);
//    jrbLinearniZT.setText(res.getString("jrbLinearniZT_text"));

    System.out.println("Data: "+frm.getMasterSet());
    jpGetVal.setTecajVisible(true);
    jpGetVal.setRaDataSet(frm.getMasterSet());
System.out.println("frm.getMasterSet() "+frm.getMasterSet());
//    jpGetVal.setTecajVisible(true); comment by TV
    jpGetVal.setTecajEditable(true);
    jpGetVal.setBorderVisible(false);

//    jlCSHZT.setText(res.getString("jlCSHZT_text"));
    jlUIZT.setText(res.getString("jlUIZT_text"));
    jlDATDOK.setText(res.getString("jlDATDOK_text"));


    jpBRDOKUL = new jpBrdokul(ver);
    if (ver==0) {

//      jtfBRDOKUL.setColumnName("BRDOKUL");
    } else if (ver==1) {
//      jlrBRDOKUL.setColumnName("BRDOKUL");
//      jlrBRDOKUL.setVisCols(new int[]{0,1,2});
//      jbBRDOKUL.setText("...");
//      jlrBRDOKUL.setNavButton(jbBRDOKUL);
    }

    jtfBRRAC.setColumnName("BRRAC");
    jLabel2.setText("Naziv");
    jLabel1.setText("Šifra");
//    jrbShemaZT.setText(res.getString("jrbShemaZT_text"));
    jcbZT.setText("Zavisni troškovi preko ulaznih ra\u010Duna");
    jcbZT.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (frm.enableZT)
          ZTchanged();
      }
    });
    jbZT.setText("...");
    jbZT.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (frm instanceof IZavtrHandler)
          ((IZavtrHandler) frm).getZavtrMaster().show();

      }
    });
    jtfDATDOSP.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATDOSP.setColumnName("DATDOSP");
    jtfUIPRPOR.setColumnName("UIPRPOR");
    jtfUIPRPOR2.setColumnName("UIPRPOR2");
    jtfUIPRPOR3.setColumnName("UIPRPOR3");
    jtabs.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jtabs_stateChanged(e);
      }
    });
//    jbULDOK.setText(res.getString("jbKEYF9_text"));
//    jbULDOK.addActionListener(new java.awt.event.ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//		  jbULDOK_actionPerformed(e);
//		}
//	 });
    jpMasterCenter.add(jpGetVal, new XYConstraints(0, 185, -1, -1));
    jpMasterCenter.add(jlCPAR, new XYConstraints(15, 35, -1, -1));
    jpMasterCenter.add(jrfCPAR, new XYConstraints(150, 35, 100, -1));
    jpMasterCenter.add(jrfDOSP, new XYConstraints(606, 35, 0, -1));
    jpMasterCenter.add(jrfNAZPAR, new XYConstraints(260, 35, 345, -1));
    jpMasterCenter.add(jbCPAR, new XYConstraints(609, 35, 21, 21));
    jpMasterCenter.add(jlDATDOK, new XYConstraints(15, 60, -1, -1));
    jpMasterCenter.add(jlBRRAC, new XYConstraints(15, 85, -1, -1));
    jpMasterCenter.add(jlDVO, new XYConstraints(15, 110, -1, -1));
    jpMasterCenter.add(jlBRDOKUL, new XYConstraints(15, 135, -1, -1));
    jpMasterCenter.add(jtfDATDOK, new XYConstraints(150, 60, 100, -1));
    jpMasterCenter.add(jtfBRRAC, new XYConstraints(150, 85, 200, -1));
    jpMasterCenter.add(jtfDVO, new XYConstraints(150, 110, 100, -1));
//    if (ver==0) {
//      jpMasterCenter.add(jtfBRDOKUL, new XYConstraints(150, 135, 200, -1));
//    } else if (ver==1) {
//      jpMasterCenter.add(jlrBRDOKUL, new XYConstraints(150, 135, 200, -1));
//      jpMasterCenter.add(jbBRDOKUL, new XYConstraints(355, 135, 21, 21));
//    }
    jpMasterCenter.add(jpBRDOKUL, new XYConstraints(150, 135, -1, -1));

    //jpMasterCenter.add(jlUIPRPOR, new XYConstraints(400, 60, -1, -1));
    jpMasterCenter.add(jlDATRAC, new XYConstraints(400, 85, -1, -1));
    jpMasterCenter.add(jlDATDOSP, new XYConstraints(400, 110, -1, -1));
    jpMasterCenter.add(jlDATDOKUL, new XYConstraints(400, 135, -1, -1));
    //jpMasterCenter.add(jtfUIPRPOR, new XYConstraints(505, 60, 100, -1));
    jpMasterCenter.add(jtfDATRAC, new XYConstraints(505, 85, 100, -1));
    jpMasterCenter.add(jtfDATDOSP, new XYConstraints(505, 110, 100, -1));
    jpMasterCenter.add(jtfDATDOKUL, new XYConstraints(505, 135, 100, -1));
    jpZT.add(jlUINAB, new XYConstraints(27, 30, -1, -1));
//    jpZT.add(jrbLinearniZT, new XYConstraints(10, 5, -1, -1));
    jpZT.add(jtfUINAB, new XYConstraints(135, 30, 100, -1));
    jpZT.add(jlUIZT, new XYConstraints(280, 30, -1, -1));
    jpZT.add(jtfUIZT, new XYConstraints(385, 30, 100, -1));
    jpZT.add(jcbZT, new XYConstraints(10, 110, -1, -1));
    jpZT.add(jbZT, new XYConstraints(jcbZT.getPreferredSize().width+25, 110, 21, 21));
    
    jpZT.add(jtfUIPRPOR, new XYConstraints(505, 160, 100, -1));
    jpZT.add(jtfUIPRPOR2, new XYConstraints(400, 160, 100, -1));
    jpZT.add(jtfUIPRPOR3, new XYConstraints(295, 160, 100, -1));
    jpZT.add(jlPrpor, new XYConstraints(195, 160, -1, -1));
    jpZT.add(jlPrp1, new XYConstraints(505, 142, 100, -1));
    jpZT.add(jlPrp2, new XYConstraints(400, 142, 100, -1));
    jpZT.add(jlPrp3, new XYConstraints(295, 142, 100, -1));
//    jpZT.add(jrbShemaZT, new XYConstraints(10, 60, -1, -1));
//    jpZT.add(jlCSHZT, new XYConstraints(27, 85, -1, -1));
//    jpZT.add(jrfCSHZT, new XYConstraints(135, 85, 100, -1));
//    jpZT.add(jbDetail, new XYConstraints(510, 85, 80, 21));
//    jpZT.add(jbCSHZT, new XYConstraints(485, 85, 21, 21));
//    jpZT.add(jrfNAZSHZT, new XYConstraints(240, 85, 240, -1));
    jpZT.add(jlUPZT, new XYConstraints(610, 30, -1, -1));
    jpZT.add(jtfUPZT, new XYConstraints(505, 30, 100, -1));
    jpZT.add(new JLabel("Devizni iznos UR-a"), new XYConstraints(27, 70, -1, -1));
    jpZT.add(jtfDEVIZN, new XYConstraints(135, 70, 100, -1));
    this.add(jpBRDOK, BorderLayout.NORTH);
    this.add(jtabs, BorderLayout.CENTER);
    jtabs.addTab("Osnovni podaci", jpMasterCenter);
    jpMasterCenter.add(jLabel1, new XYConstraints(150, 15, -1, -1));
    jpMasterCenter.add(jLabel2, new XYConstraints(260, 15, -1, -1));
//    jpMasterCenter.add(jbULDOK,   new XYConstraints(354, 135, 21, 21));
//    jbDetail.addActionListener(new java.awt.event.ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//		  jbDetail_actionPerformed(e);
//		}
//	 });
    jtabs.addTab("Zavisni trošak", jpZT);
//    jbCSHZT.addActionListener(new java.awt.event.ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//		  jbCSHZT_actionPerformed(e);
//		}
//	 });

   KeyListener f9 = new KeyAdapter() {
     public void keyPressed(KeyEvent e) {
       if (e.getKeyCode() == e.VK_F9 && 
       		(frm instanceof IZavtrHandler) && frm.raMaster.getMode() == 'N') {
          if (!jcbZT.isSelected()) jcbZT.setSelected(true);
          jbZT.doClick();
        }
     }
   };
   jtfUINAB.addKeyListener(f9);
   jtfUIZT.addKeyListener(f9);
   jtfUPZT.addKeyListener(f9);
   jcbZT.addKeyListener(f9);
   jbZT.addKeyListener(f9);
//    jrbLinearniZT.addActionListener(new java.awt.event.ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//		  jrbLinearniZT_actionPerformed(e);
//		}
//	 });
//    jrbShemaZT.addActionListener(new java.awt.event.ActionListener() {
//		public void actionPerformed(ActionEvent e) {
//		  jrbShemaZT_actionPerformed(e);
//		}
//	 });
//    buttonGroup1.add(jrbLinearniZT);
//    buttonGroup1.add(jrbShemaZT);
  }
  void jtfDATRAC_focusLost(FocusEvent e) {
    if (!jtfDATRAC.getText().equals("")) {
      frm.getMasterSet().setTimestamp("DVO", frm.getMasterSet().getTimestamp("DATRAC"));
      findDD();
//      frm.getMasterSet().setTimestamp("DATDOSP", frm.getMasterSet().getTimestamp("DATRAC"));
    }
  }
  void jtfUPZT_focusLost(FocusEvent e) {
    frm.getMasterSet().setBigDecimal("UIZT", util.findIznos(
        frm.getMasterSet().getBigDecimal("UINAB"),frm.getMasterSet().getBigDecimal("UPZT")));
  }
  void jtfUIZT_focusLost(FocusEvent e) {
    if (frm.getMasterSet().getBigDecimal("UINAB").doubleValue()!=0) {
      frm.getMasterSet().setBigDecimal("UPZT", util.findPostotak7(
          frm.getMasterSet().getBigDecimal("UINAB"), frm.getMasterSet().getBigDecimal("UIZT")));
    }
  }
  void jtfUINAB_focusLost(FocusEvent e) {
    calcWithTecaj();
    if (jcbZT.isSelected()) {
      checkZT();
      jtfUIZT_focusLost(null);
    } else if (frm.getMasterSet().getBigDecimal("UINAB").doubleValue()>0) {
      if (frm.getMasterSet().getBigDecimal("UPZT").doubleValue()==0) {
        frm.getMasterSet().setBigDecimal("UPZT", util.findPostotak7(
          frm.getMasterSet().getBigDecimal("UINAB"), frm.getMasterSet().getBigDecimal("UIZT")));
      }
      else {
        frm.getMasterSet().setBigDecimal("UIZT", util.findIznos(
          frm.getMasterSet().getBigDecimal("UINAB"),frm.getMasterSet().getBigDecimal("UPZT")));
      }
    }
  }
  void jtfDATDOK_focusGained(FocusEvent e) {

  }
  void jtfDATDOK_focusLost(FocusEvent e) {
    jpGetVal.setTecajDate(frm.getMasterSet().getTimestamp("DATDOK"));
    jtfDATDOK.setText(jtfDATDOK.getText());
    jtfDVO.setText(jtfDATDOK.getText());
    findDD();
  }

  private void checkZT() {
    if (frm instanceof IZavtrHandler) {
    	IZavtrHandler izt = (IZavtrHandler) frm;
      if (!izt.getZavtrMaster().needRefresh) {
      	izt.getZavtrMaster().updateZT();
//        fp.zt.changedZT();
      }
    }
  }

  private void ZTchanged() {
    if (jcbZT.isSelected()) {
      rcc.setLabelLaF(jtfUIZT, false);
      rcc.setLabelLaF(jtfUPZT, false);
      rcc.setLabelLaF(jbZT, true);
      checkZT();
    } else {
      rcc.setLabelLaF(jtfUIZT, true);
      rcc.setLabelLaF(jtfUPZT, true);
      rcc.setLabelLaF(jbZT, false);
    }
  }
//  void jbDetail_actionPerformed(ActionEvent e) {
  /*  if (jrfCSHZT.getText().equals("")) {
      JOptionPane.showConfirmDialog(null,"Shema zavisog tro\u0161ka nije odabrana !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
    else {
      dPZT.setUvjet(jrfCSHZT.getText());
      dPZT.show();
    } */
    // (ab.f)
//    if (frm instanceof frmPRK)
//      ((frmPRK) frm).zt.show();
//  }
//  void jbCSHZT_actionPerformed(ActionEvent e) {
//    jrfCSHZT.keyF9Pressed();
//  }
//  void jrbLinearniZT_actionPerformed(ActionEvent e) {
//    if (this.jrbLinearniZT.isEnabled()) {
//      findZT();
//    }
//  }
//  void jrbShemaZT_actionPerformed(ActionEvent e) {
//    if (this.jrbShemaZT.isEnabled()) {
//      findZT();
//    }
//  }
/*  void findZT() {
    if (jrbLinearniZT.isSelected()) {
      rcc.setLabelLaF(jtfUINAB,true);
      rcc.setLabelLaF(jtfUIZT,true);
      rcc.setLabelLaF(jtfUPZT,true);
      rcc.setLabelLaF(jrfCSHZT,false);
      rcc.setLabelLaF(jbCSHZT,false);
      rcc.setLabelLaF(jrfNAZSHZT,false);
      rcc.setLabelLaF(jbDetail,false);
      if (!frm.getMasterSet().getString("CSHZT").trim().equals("")) {
        frm.getMasterSet().setString("CSHZT", "");
      }
      jrfNAZSHZT.setText("");
      jtfUINAB.requestFocus();
    }
    else {
      rcc.setLabelLaF(jrfCSHZT,true);
      rcc.setLabelLaF(jbCSHZT,true);
      rcc.setLabelLaF(jrfNAZSHZT,true);
      rcc.setLabelLaF(jbDetail,true);
      rcc.setLabelLaF(jtfUINAB,false);
      rcc.setLabelLaF(jtfUIZT,false);
      rcc.setLabelLaF(jtfUPZT,false);
      frm.getMasterSet().setBigDecimal("UINAB", main.nul);
      frm.getMasterSet().setBigDecimal("UIZT", main.nul);
      frm.getMasterSet().setBigDecimal("UPZT", main.nul);
      jrfCSHZT.requestFocus();
    }
  }*/
  public void initPanel(char mode) {
    if (mode == 'N') oldcpar = -1;
    else oldcpar = jrfCPAR.getDataSet().getInt("CPAR");
    if (frm.enableZT) {
      jcbZT.setSelected(false);
      ZTchanged();
//      findZT();
    } else if (mode=='I') {
      rcc.EnabDisabAll(jpZT, false);
      rcc.setLabelLaF(jtfUIPRPOR, true);
      rcc.setLabelLaF(jtfUIPRPOR2, true);
      rcc.setLabelLaF(jtfUIPRPOR3, true);
      enabdisabUIPRPOR();
    }
    jcbZT.setSelected(frm.getMasterSet().getString("CSHZT").equals("YES"));
    if (jcbZT.isSelected() && !frm.enableZT) rcc.setLabelLaF(jbZT, true);
    jpGetVal.initJP(mode);
    jpBRDOK.SetDefTextDOK(mode);
    if (presPRK.getPres().jrfCPAR.getText().equals("")) {
      if (mode!='B') {
        rcc.setLabelLaF(jrfCPAR, true);
        rcc.setLabelLaF(jrfNAZPAR, true);
        rcc.setLabelLaF(jbCPAR, true);
        jrfCPAR.requestFocus();
      }
    }
    else {
      rcc.setLabelLaF(jrfCPAR, false);
      rcc.setLabelLaF(jrfNAZPAR, false);
      rcc.setLabelLaF(jbCPAR, false);
      jrfCPAR.forceFocLost();
      findDD();
      jtfDATDOK.requestFocus();
    }
//    if (mode == 'I') {
//      frm.getMasterSet().setBigDecimal("TECAJ",);
//      jpGetVal.tecajSet.setBigDecimal("TECKUP",frm.getMasterSet().getBigDecimal("TECAJ"));
//    }
  }
  void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds) {
    jtfUINAB.setDataSet(qds);
    jtfDEVIZN.setDataSet(qds);
    jtfDATDOKUL.setDataSet(qds);
    jrfCPAR.setDataSet(qds);
    jtfDATRAC.setDataSet(qds);
//    jrfCSHZT.setDataSet(qds);
    jtfUPZT.setDataSet(qds);
    jtfUIZT.setDataSet(qds);
    jtfDATDOK.setDataSet(qds);
    jtfDVO.setDataSet(qds);
//    if (ver==0) {
//      jtfBRDOKUL.setDataSet(qds);
//    }
//    else if (ver==1){
//      jlrBRDOKUL.setDataSet(qds);
//    }
    jpBRDOKUL.setDataSet(qds);
    jtfBRRAC.setDataSet(qds);
    jtfDATDOSP.setDataSet(qds);
    jtfUIPRPOR.setDataSet(qds);
    jtfUIPRPOR2.setDataSet(qds);
    jtfUIPRPOR3.setDataSet(qds);
    jpBRDOK.setDataSet(qds);
  }
//  String getCSHZT() {
//    return this.jrfCSHZT.getText();
//  }
  void jtfDVO_focusLost(FocusEvent e) {
    findDD();
//    if (!jtfDVO.getText().equals(""))
//      frm.getMasterSet().setTimestamp("DATDOSP", frm.getMasterSet().getTimestamp("DVO"));
  }

  void jtabs_stateChanged(ChangeEvent e) {
    if (jtabs.getSelectedIndex()==0) {
      jrfCPAR.requestFocus();
    }
    else {
//      jrbLinearniZT.requestFocus();
      jtfUINAB.requestFocus();
    }
  }
  public void CPARafter_lookUp(){
    findDD();
    if (ver==1) {
      jpBRDOKUL.preparePRK(frm.getMasterSet().getString("CSKL"),frm.getMasterSet().getInt("CPAR"));
    }
    SwingUtilities.invokeLater(new Runnable(){

		public void run() {
			enabdisabUIPRPOR();
		}});
  }
  
  void enabdisabUIPRPOR(){
    if (frmParam.getParam("robno","inoPRPOR","D","Onemoguæavanje unosa predporeza za ino kalkulacije").equalsIgnoreCase("D")){
        
        	if (!jrfCPAR.getRaDataSet().isOpen()) return;
        	if (jrfCPAR.getText().equalsIgnoreCase("")) return;
        	
//    System.err.println("sdfsdfsdf "+jrfCPAR.getRaDataSet().getString("NAZPAR")+
//    		"    "+jrfCPAR.getRaDataSet().getString("DI"));    	
        	if (!jrfCPAR.getRaDataSet().getString("DI").equalsIgnoreCase("D")){
        		frm.getMasterSet().setBigDecimal("UIPRPOR",Aus.zero2);
        	    rcc.setLabelLaF(jtfUIPRPOR, false);
        	    rcc.setLabelLaF(jtfUIPRPOR2, false);
        	    rcc.setLabelLaF(jtfUIPRPOR3, false);
        	} else {
        	    rcc.setLabelLaF(jtfUIPRPOR, true);
        	    rcc.setLabelLaF(jtfUIPRPOR2, true);
        	    rcc.setLabelLaF(jtfUIPRPOR3, true);
        	}
        }
  }
  
  private void findDD() {
    if (jrfCPAR.getText().equals("")) return;
    if (oldcpar == jrfCPAR.getDataSet().getInt("CPAR")) return;
    oldcpar = jrfCPAR.getDataSet().getInt("CPAR");
    
    frm.getMasterSet().setTimestamp("DATDOSP", frm.getMasterSet().getTimestamp("DVO"));
    short dosp = (short) Aus.getNumber(jrfDOSP.getText());
    frm.getMasterSet().setShort("DDOSP", dosp);
    java.util.Date Datum = new java.util.Date(frm.getMasterSet().getTimestamp("DVO").getTime());
    frm.getMasterSet().setTimestamp("DATDOSP", new java.sql.Timestamp(
    raDateUtil.getraDateUtil().addDate(Datum, (int) dosp).getTime()));
  }
/**
 * UINAB = DEVIZN*(TECAJ/JEDVAL)
 * DEVIZN = UINAB/(TECAJ/JEDVAL)
 */
  private void calcWithTecaj() {
    BigDecimal _uinab = frm.getMasterSet().getBigDecimal("UINAB");
    BigDecimal _devizn = frm.getMasterSet().getBigDecimal("DEVIZN");
    BigDecimal _tecaj = frm.getMasterSet().getBigDecimal("TECAJ");
    BigDecimal _jedval = Tecajevi.getJedVal(frm.getMasterSet().getString("OZNVAL"));
System.out.println("uinab = "+_uinab);
System.out.println("devizn = "+_devizn);
System.out.println("tecaj = "+_tecaj);
System.out.println("jedval = "+_jedval);
    if (_tecaj.signum()==0 || (_uinab.signum()!=0 && _devizn.signum()!=0)) return;
    if (_uinab.signum()==0) {
      frm.getMasterSet().setBigDecimal("UINAB", 
          _devizn.multiply(
              _tecaj.divide(_jedval, BigDecimal.ROUND_HALF_UP))
              );
    } else if (_devizn.signum()==0) {
      frm.getMasterSet().setBigDecimal("DEVIZN",
          _uinab.divide(
              _tecaj.divide(_jedval, BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP)
              );
    }
  }
  
//  void jbULDOK_actionPerformed(ActionEvent e) {
//    afterJbBRDOKULPress();
//  }
//  public void afterJbBRDOKULPress() {
//
//  }
/*  public void MYafterGet_Val(){
    if (this.jpGetVal.jcbValuta.isSelected()) {
      this.jpGetVal.setTecajDate(frm.getMasterSet().getTimestamp("DATDOK"));
      frm.getMasterSet().setBigDecimal("TECAJ",this.jpGetVal.getTecajUI());

      System.out.println("Tecaj 2: "+this.jpGetVal.getTecajUI());
    }
    else {
      frm.getMasterSet().setBigDecimal("TECAJ",new java.math.BigDecimal("0.0000"));
    }
  }*/

}
