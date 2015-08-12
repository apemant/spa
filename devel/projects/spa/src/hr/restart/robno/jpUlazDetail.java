/****license*****************************************************************
**   file: jpUlazDetail.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraTextMultyKolField;
import hr.restart.util.Aus;
import hr.restart.util.Calc;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.zapod.Tecajevi;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class jpUlazDetail extends JPanel {
  String oldValue = "";
  boolean isCSKL=false;
  boolean edion = false;
  _Main main;
  frmUlazTemplate frm;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

//  dlgDodZT dZT;
  Calc calc;
  JPanel jpDetailCenter = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
//  JraTextField jtfKOL = new JraTextField();
	JraTextMultyKolField jtfKOL = new JraTextMultyKolField(){

		public void propertyChange(PropertyChangeEvent evt) {
			
			if (evt.getPropertyName().equalsIgnoreCase("KOL")) {
				System.out.println("kalkulacija(0)");
				kalkulacija(0);
			}
			
		}
        
        public void valueChanged() {
          jtfKOL_focusLost(null);
        }
     };	
  
  JraTextField jtfPRAB = new JraTextField() {
    public void valueChanged() {
      jtfPRAB_focusLost(null);
    }
  };
  JraTextField jtfRAB = new JraTextField() {
    public void valueChanged() {
      jtfRAB_focusLost(null);
    }
  };
  JraTextField jtfNC = new JraTextField() {
    public void valueChanged() {
      jtfNC_focusLost(null);
    }
  };
  JraTextField jtfPMAR = new JraTextField() {
    public void valueChanged() {
      jtfPMAR_focusLost(null);
    }
  };
  JraTextField jtfMAR = new JraTextField() {
    public void valueChanged() {
      jtfMAR_focusLost(null);
    }
  };
  JraTextField jtfVC = new JraTextField() {
    public void valueChanged() {
      jtfVC_focusLost(null);
    }
  };
  JraTextField jtfMC = new JraTextField() {
    public void valueChanged() {
      jtfMC_focusLost(null);
    }
  };
  JLabel jlRAB = new JLabel();
  JraTextField jtfIDOB = new JraTextField() {
    public void valueChanged() {
      jtfIDOB_focusLost(null);
    }
  };
  JraTextField jtfIRAB = new JraTextField();
  JraTextField jtfZT = new JraTextField();
  JraTextField jtfIZT = new JraTextField() {
    public void valueChanged() {
      jtfIZT_focusLost(null);
    }
  };
  JraTextField jtfPOR = new JraTextField();
  JraTextField jtfINAB = new JraTextField();
  JraTextField jtfIMAR = new JraTextField();
  JraTextField jtfIBP = new JraTextField();
  JraTextField jtfIPOR = new JraTextField();
  JraTextField jtfISP = new JraTextField();
  JraButton jbZT = new JraButton();
  JraTextField jtfPZT = new JraTextField() {
    public void valueChanged() {
      jtfPZT_focusLost(null);
    }
  };
  JraTextField jtfPPOR = new JraTextField();
  JLabel jlPostotak = new JLabel();

  JLabel jlKOL = new JLabel();
  JLabel jlZT = new JLabel();
  JLabel jlZaKolicinu = new JLabel();
  JLabel jlZaJedinicu = new JLabel();
  JLabel jlMC = new JLabel();
  JLabel jlPOR = new JLabel();
  JLabel jlVC = new JLabel();
  JLabel jlMAR = new JLabel();
  JLabel jlNC = new JLabel();
  JLabel jlDC = new JLabel();
  JraTextField jtfDC = new JraTextField() {
    public void valueChanged() {
      jtfDC_focusLost(null);
    }
  };
//  raArtiklUnos rpcart = new raArtiklUnos() {
  rapancart rpcart = new rapancart(1){
    public void nextTofocus(){
    }
    public void metToDo_after_lookUp(){
      if (frm.raDetail.getMode() == 'B') return;
      frm.updateHelper();
      if (rpcart.getCART().length() > 0)
      	MYmetToDo_after_lookUp();
    }
  };
  
  JraButton trans = new JraButton();
  
  JPanel edi = new JPanel();
  
  JLabel jlDatPro = new JLabel();
  JLabel jlRok = new JLabel();
  JLabel jlLot = new JLabel();
  JLabel jlReg = new JLabel();
  
  JraTextField jraDatPro = new JraTextField();
  JraTextField jraRok = new JraTextField();
  JraTextField jraLot = new JraTextField();
  JraTextField jraReg = new JraTextField();
  
  
  public void MYmetToDo_after_lookUp(){
    if ("PST".equals(frm.vrDok) &&
        Stdoku.getDataModule().getRowCount(
            Condition.whereAllEqual(Util.mkey, frm.getMasterSet()).
            and(Condition.equal("CART", 
                Integer.parseInt(rpcart.getCART())))) > 0) {
      Aut.getAut().handleRpcErr(rpcart, "Artikl ve\u0107 unesen u poèetno stanje!");
      return;
    }
    if (frm.prSTAT!='R') {
      frm.findDOBART();
      if (!findSTANJE('U')) {
      	System.out.println("Error");
      }
    }
    disableUnosFields(false, frm.prSTAT);
    if ((frm.isTranzit || frm.isNar) && trans.isVisible())
      frm.afterGetArtikl();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jtfKOL.requestFocus();
        jtfKOL.selectAll();        
      }
    });
  }

  JLabel jLabel1 = new JLabel();
  JraTextField jtfDC_VAL = new JraTextField() {
    public void valueChanged() {
      jtfDC_VAL_focusLost(null);
    }
  };
  JraTextField jtfIDOB_VAL = new JraTextField() {
    public void valueChanged() {
      jtfIDOB_VAL_focusLost(null);
    }
  };
  TableDataSet tds = new TableDataSet();

  public jpUlazDetail(frmUlazTemplate fut,boolean cskl) {
  }
  public jpUlazDetail(frmUlazTemplate fut) {
    frm=fut;
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public jpUlazDetail(frmUlazTemplate fut,char mode) {
    frm=fut;
    try {
      jbInit1();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setTransEnabled(boolean enab) {
    trans.setVisible(enab);
    trans.setEnabled(enab);
  }
  
  public void setTransOpis(boolean tranzit) {
    trans.setToolTipText(tranzit 
        ? "Dohvat unaprijed razduženih stavki izlaza"
        : "Dohvat stavki narudžbenice");
  }

  public void jbInit() throws Exception {
    rpcart.setMyAfterLookupOnNavigate(false);
    rpcart.setFocusCycleRoot(true);
    jlKOL.setHorizontalAlignment(SwingConstants.CENTER);
    jlKOL.setText(res.getString("jlKOL_text"));
    jtfKOL.setColumnName("KOL");
    jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfKOL_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfKOL_focusGained(e);
      }
    });
    
    edion = frmParam.getParam("robno", "ediUlaz", "N",
        "Panel za unos EDI podataka na ulazu (D,N)").equals("D");
    
    trans.setIcon(raImages.getImageIcon(raImages.IMGSENDMAIL));
    trans.setAutomaticFocusLost(true);
    trans.setToolTipText("Dohvat unaprijed razduženih stavki izlaza");
    setTransEnabled(false);
    trans.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frm.tranzitDohvat();
      }    
    });
    frm.raDetail.addKeyAction(new raKeyAction(KeyEvent.VK_F7) {
      public void keyAction() {
        if (trans.isVisible() && trans.isEnabled())
          frm.tranzitDohvat();
      }    
    });
    jlZT.setText(res.getString("jlZT_text"));
    jlNC.setText(res.getString("jlNC_text"));
    jlMAR.setText(res.getString("jlMAR_text"));
    jlVC.setText(res.getString("jlVC_text"));
    jlPOR.setText(res.getString("jlPOR_text"));
    jlMC.setText(res.getString("jlMC_text"));
    jlZaKolicinu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaKolicinu.setText(res.getString("jlZaKolicinu_text"));
    jlZaJedinicu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaJedinicu.setText(res.getString("jlZaJedinicu_text"));
    jlDC.setText(res.getString("jlDC_text"));
    jtfDC.setColumnName("DC");
    jtfDC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfDC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfDC_focusGained(e);
      }
    });
    jtfPRAB.setColumnName("PRAB");
    jtfPRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfPRAB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfPRAB_focusGained(e);
      }
    });
    jtfRAB.setColumnName("RAB");
    jtfRAB.setDataSet(tds);
    jtfRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfRAB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfRAB_focusGained(e);
      }
    });
    jtfNC.setColumnName("NC");
    jtfNC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfNC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfNC_focusGained(e);
      }
    });
    jtfPMAR.setColumnName("PMAR");
    jtfPMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfPMAR_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfPMAR_focusGained(e);
      }
    });
    jtfMAR.setColumnName("MAR");
    jtfMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfMAR_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfMAR_focusGained(e);
      }
    });
    jtfVC.setColumnName("VC");
    jtfVC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfVC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfVC_focusGained(e);
      }
    });
    jtfMC.setColumnName("MC");
    jtfMC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfMC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfMC_focusGained(e);
      }
    });
    jlRAB.setText(res.getString("jlRAB_text"));
    jtfIDOB.setColumnName("IDOB");
    jtfIDOB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfIDOB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfIDOB_focusGained(e);
      }
    });
    jtfIRAB.setColumnName("IRAB");
    jtfZT.setColumnName("ZT");
    jtfZT.setDataSet(tds);
    
    jtfIZT.setColumnName("IZT");
    jtfIZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfIZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfZT_focusLost(e);
      }*/
    });
    jtfPOR.setColumnName("POR");
    jtfPOR.setDataSet(tds);
    jtfINAB.setColumnName("INAB");
    jtfIMAR.setColumnName("IMAR");
    jtfIBP.setColumnName("IBP");
    jtfIPOR.setColumnName("IPOR");
    jtfISP.setColumnName("ISP");
    jbZT.setText(res.getString("jbZT_text"));
    jbZT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbZT_actionPerformed(e);
      }
    });
    jtfPZT.setColumnName("PZT");
    jtfPZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfPZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfPZT_focusLost(e);
      }*/
    });
    jtfPPOR.setColumnName("UKUPOR");
    jtfPPOR.setDataSet(dm.getPorezi());
    jlPostotak.setText(res.getString("jlPostotak_text"));
    jlPostotak.setHorizontalAlignment(SwingConstants.CENTER);
    jpDetailCenter.setLayout(xYLayout2);
    jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
    xYLayout2.setWidth(645);
    xYLayout2.setHeight(315);
    jLabel1.setText("Dobavlja\u010Deva cijena (valutna)");
    jtfDC_VAL.setColumnName("DC_VAL");
    jtfDC_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfDC_VAL_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfDC_VAL_focusLost(e);
      }*/
    });
    jtfIDOB_VAL.setColumnName("IDOB_VAL");
    jtfIDOB_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfIDOB_VAL_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfIDOB_VAL_focusLost(e);
      }*/
    });
    jpDetailCenter.add(jlKOL, new XYConstraints(360, 20, 130, -1));
    jpDetailCenter.add(jtfKOL, new XYConstraints(500, 20, 130, -1));
    jpDetailCenter.add(trans, new XYConstraints(470, 20, 21, 21));
    jpDetailCenter.add(jlZaKolicinu, new XYConstraints(500, 50, 130, -1));
    jpDetailCenter.add(jlZaJedinicu, new XYConstraints(360, 50, 130, -1));
    jpDetailCenter.add(jlPostotak, new XYConstraints(250, 50, 100, -1));
    this.setLayout(new BorderLayout());
    tds.setColumns(new Column[] {
    		dM.createBigDecimalColumn("RAB", "Jedinièni RuC", 2),
    		dM.createBigDecimalColumn("ZT", "Jedinièni troškovi", 2),
    		dM.createBigDecimalColumn("POR", "Jedinièni porez", 2)});
    this.add(jpDetailCenter, BorderLayout.CENTER);
    
    jpDetailCenter.add(jlDC, new XYConstraints(15, 100, -1, -1));
    jpDetailCenter.add(jlRAB, new XYConstraints(15, 125, -1, -1));
    jpDetailCenter.add(jlZT, new XYConstraints(15, 150, -1, -1));
    jpDetailCenter.add(jlNC, new XYConstraints(15, 175, -1, -1));
    jpDetailCenter.add(jlMAR, new XYConstraints(15, 200, -1, -1));
    jpDetailCenter.add(jlVC, new XYConstraints(15, 225, -1, -1));
    jpDetailCenter.add(jlPOR, new XYConstraints(15, 250, -1, -1));
    jpDetailCenter.add(jlMC, new XYConstraints(15, 275, -1, -1));
    jpDetailCenter.add(jLabel1, new XYConstraints(15, 75, -1, -1));
    jpDetailCenter.add(jtfDC, new XYConstraints(360, 100, 130, -1));
    jpDetailCenter.add(jbZT, new XYConstraints(150, 150, 90, 21));
    jpDetailCenter.add(jtfPRAB, new XYConstraints(250, 125, 100, -1));
    jpDetailCenter.add(jtfPZT, new XYConstraints(250, 150, 100, -1));
    jpDetailCenter.add(jtfPMAR, new XYConstraints(250, 200, 100, -1));
    jpDetailCenter.add(jtfPPOR, new XYConstraints(250, 250, 100, -1));
    jpDetailCenter.add(jtfRAB, new XYConstraints(360, 125, 130, -1));
    jpDetailCenter.add(jtfZT, new XYConstraints(360, 150, 130, -1));
    jpDetailCenter.add(jtfNC, new XYConstraints(360, 175, 130, -1));
    jpDetailCenter.add(jtfMAR, new XYConstraints(360, 200, 130, -1));
    jpDetailCenter.add(jtfVC, new XYConstraints(360, 225, 130, -1));
    jpDetailCenter.add(jtfPOR, new XYConstraints(360, 250, 130, -1));
    jpDetailCenter.add(jtfMC, new XYConstraints(360, 275, 130, -1));
    jpDetailCenter.add(jtfIDOB, new XYConstraints(500, 100, 130, -1));
    jpDetailCenter.add(jtfIRAB, new XYConstraints(500, 125, 130, -1));
    jpDetailCenter.add(jtfIZT, new XYConstraints(500, 150, 130, -1));
    jpDetailCenter.add(jtfINAB, new XYConstraints(500, 175, 130, -1));
    jpDetailCenter.add(jtfIMAR, new XYConstraints(500, 200, 130, -1));
    jpDetailCenter.add(jtfIPOR, new XYConstraints(500, 250, 130, -1));
    jpDetailCenter.add(jtfISP, new XYConstraints(500, 275, 130, -1));
    jpDetailCenter.add(jtfIBP, new XYConstraints(500, 225, 130, -1));
    jpDetailCenter.add(jtfDC_VAL, new XYConstraints(360, 75, 130, -1));
    jpDetailCenter.add(jtfIDOB_VAL, new XYConstraints(500, 75, 130, -1));
    
    if (edion && frm.prSTAT == 'P') addEdi();
    this.add(rpcart, BorderLayout.NORTH);
    
    tds.open();
    if (tds.rowCount()==0) {
      tds.insertRow(true);
    }
  }
  
  /*JLabel jlDatPro = new JLabel();
  JLabel jlRok = new JLabel();
  JLabel jlLot = new JLabel();
  JLabel jlReg = new JLabel();
  
  JraTextField jraDatPro = new JraTextField();
  JraTextField jraRok = new JraTextField();
  JraTextField jraLot = new JraTextField();
  JraTextField jraReg = new JraTextField();*/
  
  void addEdi() {
    XYLayout lay = new XYLayout(645, 70);
    edi.setLayout(lay);
    edi.setBorder(BorderFactory.createEtchedBorder());
    jlLot.setText("Šarža");
    jlLot.setHorizontalAlignment(JLabel.CENTER);
    jlReg.setText("Reg. oznaka");
    jlReg.setHorizontalAlignment(JLabel.CENTER);
    jlDatPro.setText("Datum proizvodnje");
    jlDatPro.setHorizontalAlignment(JLabel.CENTER);
    jlRok.setText("Rok uporabe");
    jlRok.setHorizontalAlignment(JLabel.CENTER);
    jraLot.setColumnName("LOT");
    jraReg.setColumnName("CREG");
    jraDatPro.setColumnName("DATPRO");
    jraRok.setColumnName("DATROK");
    edi.add(jlLot, new XYConstraints(80, 10, 130, -1));
    edi.add(jlReg, new XYConstraints(220, 10, 130, -1));
    edi.add(jlDatPro, new XYConstraints(360, 10, 130, -1));
    edi.add(jlRok, new XYConstraints(500, 10, 130, -1));
    edi.add(jraLot, new XYConstraints(80, 30, 130, -1));
    edi.add(jraReg, new XYConstraints(220, 30, 130, -1));
    edi.add(jraDatPro, new XYConstraints(360, 30, 130, -1));
    edi.add(jraRok, new XYConstraints(500, 30, 130, -1));
    this.add(edi, BorderLayout.SOUTH);
  }

  void jtfKOL_focusGained(FocusEvent e) {
    oldValue=jtfKOL.getText().toString();
  }
  void jtfKOL_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfKOL.getText().toString())) {
      kalkulacija(0);
    }
  }

  void jtfIDOB_VAL_focusGained(FocusEvent e) {
    oldValue=jtfIDOB_VAL.getText().toString();
  }
  void jtfIDOB_VAL_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfIDOB_VAL.getText().toString())) {
      kalkulacija(1);
    }
  }

  void jtfDC_VAL_focusGained(FocusEvent e) {
    oldValue=jtfDC_VAL.getText().toString();
  }
  void jtfDC_VAL_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfDC_VAL.getText().toString())) {
      kalkulacija(2);
    }
  }

  void jtfIDOB_focusGained(FocusEvent e) {
    oldValue=jtfIDOB.getText().toString();
  }
  void jtfIDOB_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfIDOB.getText().toString())) {
      kalkulacija(3);
    }
  }

  void jtfDC_focusGained(FocusEvent e) {
    oldValue=jtfDC.getText().toString().trim();
  }
  void jtfDC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfDC.getText().trim().toString())) {
      kalkulacija(4);
    }
  }

  void jtfPRAB_focusGained(FocusEvent e) {
    oldValue=jtfPRAB.getText().toString();
  }
  void jtfPRAB_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPRAB.getText().toString())) {
      kalkulacija(5);
    }
  }

  void jtfRAB_focusGained(FocusEvent e) {
    oldValue=jtfRAB.getText().toString();
  }
  void jtfRAB_focusLost(FocusEvent e) {
//    if (!oldValue.equals(jtfRAB.getText().toString())) {
//      kalkulacija(6);
//    }
  }

  void jtfPZT_focusGained(FocusEvent e) {
    oldValue=jtfPZT.getText().toString();
  }
  void jtfPZT_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPZT.getText().toString())) {
      kalkulacija(7);
    }
  }

  void jtfIZT_focusGained(FocusEvent e) {
    oldValue=jtfIZT.getText().toString();
  }
  void jtfIZT_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfIZT.getText().toString())) {
      kalkulacija(8);
    }
  }

  void jtfNC_focusGained(FocusEvent e) {
    oldValue=jtfNC.getText().toString();
  }
  void jtfNC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfNC.getText().toString())) {
      kalkulacija(9);
    }
  }

  void jtfPMAR_focusGained(FocusEvent e) {
    oldValue=jtfPMAR.getText().toString();
  }
  void jtfPMAR_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPMAR.getText().toString())) {
      kalkulacija(10);
    }
  }

  void jtfMAR_focusGained(FocusEvent e) {
    oldValue=jtfMAR.getText().toString();
  }
  void jtfMAR_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfMAR.getText().toString())) {
      kalkulacija(11);
    }
  }

  void jtfVC_focusGained(FocusEvent e) {
    oldValue=jtfVC.getText().toString();
  }
  void jtfVC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfVC.getText().toString())) {
      kalkulacija(12);
    }
  }

  void jtfMC_focusGained(FocusEvent e) {
    oldValue=jtfMC.getText().toString();
  }
  void jtfMC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfMC.getText().toString())) {
      kalkulacija(13);
    }
  }

//  void setCustomZT(boolean czt) {
//    rcc.setLabelLaF(jbZT, czt);
//    rcc.setLabelLaF(jtfPZT, !czt);
//    rcc.setLabelLaF(jtfIZT, !czt);
//  }

  void checkZT() {
    if (frm instanceof IZavtrHandler) {
    	IZavtrHandler izt = (IZavtrHandler) frm;
      if (frm.getMasterSet().getString("CSHZT").equals("YES") && 
      		!izt.getZavtrDetail().needRefresh) {
      	izt.getZavtrDetail().updateZT();
      	izt.getZavtrDetail().changedZT(false);
      }
    }
  }

  void jbZT_actionPerformed(ActionEvent e) {
    if (frm instanceof IZavtrHandler && frm.getMasterSet().getString("CSHZT").equals("YES"))
      ((IZavtrHandler) frm).getZavtrDetail().show();
/*    if (frm.getMasterSet().getString("CSHZT").trim().equals("")) {
      JOptionPane.showConfirmDialog(null,"Shema zavisog tro\u0161ka nije odabrana !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
    else {
     dZT = new dlgDodZT(null, frm, "Dodatni zavisni trošak", true);
     dZT.show();
    }*/

    // (ab.f)
//    if (frm instanceof frmPRK)
//      ((frmPRK) frm).zt.show();
  }

/**
 * Iniciranje RAPANCART-a
 */
  void initRpcart(){
//    frm.getMasterSet().open();
//    frm.getDetailSet().open();
    rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(frm.getMasterSet().getTimestamp("DATDOK")));
    rpcart.setCskl(frm.getDetailSet().getString("CSKL"));
    rpcart.setTabela(frm.getDetailSet());
    rpcart.setDefParam();
    rpcart.setBorder(BorderFactory.createEtchedBorder());
    rpcart.setnextFocusabile(this.jtfKOL);
    rpcart.setMode("N");
    //if (isCSKL) rpcart.addSkladField(Util.getUtil().getSkladFromCorg());
    rpcart.setUlazIzlaz('U');
    rpcart.InitRaPanCart();
  }
/**
 * Disabliranje polja koja ne koristimo za unos
 */
  void disableDefFields() {
    if (jtfISP.isEnabled()) {
      rcc.setLabelLaF(jtfZT, false);
      rcc.setLabelLaF(jtfIRAB, false);
      rcc.setLabelLaF(jtfINAB, false);
      rcc.setLabelLaF(jtfIMAR, false);
      rcc.setLabelLaF(jtfIBP, false);
      rcc.setLabelLaF(jtfPPOR, false);
      rcc.setLabelLaF(jtfPOR, false);
      rcc.setLabelLaF(jtfIPOR, false);
      rcc.setLabelLaF(jtfISP, false);
    }
  }
/**
 * Enable/Disable polja koja koristimo kod unosa
 * - novi - da li je novi
 * - doc - 'P' - primka
 *       - 'S' - pocetno stanje
 */
  void disableUnosFields(boolean novi, char doc) {
    System.out.println("disable unos fields: "+doc);
    if (doc=='P') {
      if (frm.getMasterSet().getBigDecimal("TECAJ").doubleValue()==0) {
        rcc.setLabelLaF(jtfDC_VAL, false);
        rcc.setLabelLaF(jtfIDOB_VAL, false);
      }
      else {
        rcc.setLabelLaF(jtfDC_VAL, !novi);
        rcc.setLabelLaF(jtfIDOB_VAL, !novi);
      }
      boolean czt = frm.getMasterSet().getString("CSHZT").equals("YES");
      rcc.setLabelLaF(jtfDC, !novi);
      rcc.setLabelLaF(jtfIDOB, !novi);
      rcc.setLabelLaF(jbZT, !novi && czt);
      rcc.setLabelLaF(jtfPZT, !novi && !czt);
      rcc.setLabelLaF(jtfIZT, !novi && !czt);
      rcc.setLabelLaF(jtfPRAB, !novi);
      rcc.setLabelLaF(jtfRAB, false);

      rcc.setLabelLaF(jtfNC, false);
      rcc.setLabelLaF(jtfPMAR, !novi);
      rcc.setLabelLaF(jtfMAR, !novi);
      rcc.setLabelLaF(jtfVC, !novi);
      rcc.setLabelLaF(jtfMC, !novi);
    }
    else if (doc=='S') {
      rcc.setLabelLaF(jtfDC_VAL, false);
      rcc.setLabelLaF(jtfIDOB_VAL, false);
      rcc.setLabelLaF(jtfDC, false);
      rcc.setLabelLaF(jtfIDOB, false);
      rcc.setLabelLaF(jbZT, false);
      rcc.setLabelLaF(jtfPZT, false);
      rcc.setLabelLaF(jtfZT, false);
      rcc.setLabelLaF(jtfPRAB, false);
      rcc.setLabelLaF(jtfRAB, false);

      rcc.setLabelLaF(jtfNC, !novi);
      rcc.setLabelLaF(jtfPMAR, !novi);
      rcc.setLabelLaF(jtfMAR, !novi);
      rcc.setLabelLaF(jtfVC, !novi);
      rcc.setLabelLaF(jtfMC, !novi);
    }
    else if (doc=='D') {
      rcc.setLabelLaF(jtfDC_VAL, false);
      rcc.setLabelLaF(jtfIDOB_VAL, false);
      rcc.setLabelLaF(jtfDC, !novi);
      rcc.setLabelLaF(jtfIDOB, !novi);
      rcc.setLabelLaF(jbZT, false);
      rcc.setLabelLaF(jtfPZT, false);
      rcc.setLabelLaF(jtfZT, false);
      rcc.setLabelLaF(jtfPRAB, !novi);
      rcc.setLabelLaF(jtfRAB, false);

      rcc.setLabelLaF(jtfNC, false);
      rcc.setLabelLaF(jtfPMAR, false);
      rcc.setLabelLaF(jtfMAR, false);
      rcc.setLabelLaF(jtfVC, false);
      rcc.setLabelLaF(jtfMC, false);
    }
    else if (doc=='R') {
      rcc.setLabelLaF(jtfDC_VAL, false);
      rcc.setLabelLaF(jtfIDOB_VAL, false);
      rcc.setLabelLaF(jtfDC, false);
      rcc.setLabelLaF(jtfIDOB, false);
      rcc.setLabelLaF(jbZT, false);
      rcc.setLabelLaF(jtfPZT, false);
      rcc.setLabelLaF(jtfZT, false);
      rcc.setLabelLaF(jtfPRAB, false);
      rcc.setLabelLaF(jtfRAB, false);
      rcc.setLabelLaF(jtfNC, false);
      rcc.setLabelLaF(jtfPMAR, false);
      rcc.setLabelLaF(jtfMAR, false);
      rcc.setLabelLaF(jtfVC, false);
      rcc.setLabelLaF(jtfMC, false);
    }
    rcc.setLabelLaF(jtfKOL, !novi);
    dm.getArtikli().open();
    rpcart.EnabDisab(novi);
  }
  void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds, com.borland.dx.sql.dataset.QueryDataSet qds2) {
    jtfKOL.setDataSet(qds);
    jtfDC.setDataSet(qds);
    jtfPRAB.setDataSet(qds);
    jtfNC.setDataSet(qds);
    jtfPMAR.setDataSet(qds);
    jtfMAR.setDataSet(qds);
    jtfVC.setDataSet(qds);
    jtfMC.setDataSet(qds);
    jtfIDOB.setDataSet(qds);
    jtfIRAB.setDataSet(qds);
    jtfIZT.setDataSet(qds);
    jtfINAB.setDataSet(qds);
    jtfIMAR.setDataSet(qds);
    jtfIBP.setDataSet(qds);
    jtfIPOR.setDataSet(qds);
    jtfISP.setDataSet(qds);
    jtfPZT.setDataSet(qds);
    jtfDC_VAL.setDataSet(qds);
    jtfIDOB_VAL.setDataSet(qds);
    if (edion) {
      jraLot.setDataSet(qds);
      jraReg.setDataSet(qds);
      jraDatPro.setDataSet(qds);
      jraRok.setDataSet(qds);
    }
    rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(qds2.getTimestamp("DATDOK")));
//ai//    rpcart.setCskl(qds.getString("CSKL"));
    rpcart.setTabela(qds);
    initRpcart();
    
    calc = new Calc(qds);
    calc.module("tds", tds);
    calc.module("sta", frm.stanjeSet);
  }

  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  boolean findSTANJE(char mode) {

    if (!lD.raLocate(dm.getPorezi(),"CPOR",rpcart.getCPOR())){
      System.err.println("Greška nisu naðeni porezi !!!!" + 
          "  |"+rpcart.getCPOR()+"|"+rpcart.getCART()+"|");
    }
//    dm.getPorezi().interactiveLocate(rpcart.getCPOR(),"CPOR",com.borland.dx.dataset.Locate.FIRST, false);
    
    Aus.setFilter(frm.stanjeSet, rdUtil.getUtil().
        findStanje(frm.getMasterSet().getString("CSKL"),
        		frm.getDetailSet().getInt("CART"),
        		frm.getMasterSet().getString("GOD")));    
    frm.stanjeSet.open();
    boolean hasart = lD.raLocate(dm.getArtikli(), "CART", frm.getDetailSet());
    System.out.println("hasart " + hasart + " " + frm.getDetailSet().getInt("CART"));
    if(frm.stanjeSet.getRowCount()>0) {
      if (mode=='U') {
        if (frm.getDetailSet().getBigDecimal("NC").signum()==0) {
          if (frm.isDobArt) 
          	calc.run("DC = Dob_art.DC;  PRAB = Dob_art.PRAB");
          
          calc.run("VC = sta.VC;  MC = sta.MC");
          calc.run("tds.RAB = DC % PRAB;  tds.ZT = (DC - tds.RAB) % PZT;  NC = DC - tds.RAB + tds.ZT");
          if (frm.getDetailSet().getBigDecimal("NC").signum() == 0)   calc.run("NC = sta.NC");
          if (hasart && frm.getDetailSet().getBigDecimal("VC").signum() == 0)   calc.run("VC = Artikli.VC");
          calc.run("MAR = VC - NC;  PMAR = MAR %% NC;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
          calcFromNC();
        }
      }
      frm.isFind=true;
    } else if (mode!='U') {
    	Calc.run(tds, "RAB = POR = ZT = 0");
    	calc.run("DC_VAL = DC = PRAB = NC = PMAR = VC = MC = KOL = 0");
      frm.isFind=false;
    }
    else {
    	if (frm.getDetailSet().getBigDecimal("NC").signum()==0) {
        if (frm.isDobArt) {
        	calc.run("DC = Dob_art.DC;  PRAB = Dob_art.PRAB");
        	calc.run("tds.RAB = DC % PRAB;  tds.ZT = (DC - tds.RAB) % PZT;  NC = DC - tds.RAB + tds.ZT");
        	if (hasart) calc.run("VC = Artikli.VC;  MAR = VC - NC;  PMAR = MAR %% NC");
        	else calc.run("MAR = PMAR = 0;  VC = NC");
        	calc.run("tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
        } else if (hasart) {
      		calc.run("VC = Artikli.VC;  MAR = VC - NC;  PMAR = MAR %% NC;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
      	}
    	} 
      frm.isFind=false;
    }
    return frm.isFind;
  }
  void findVirtualFields(char mode) {
//    frm.getDetailSet().open();
//    frm.getMasterSet().open();
    if (mode=='N') {
      tds.open();
      if (tds.rowCount()==0) {
        tds.insertRow(true);
      }
      Calc.run(tds, "RAB = POR = ZT = 0");
      if (frm.getMasterSet().getBigDecimal("UINAB").doubleValue()>0) {
      	Aus.set(frm.getDetailSet(), "PZT", frm.getMasterSet(), "UPZT");
      }
      initRpcart();
      disableUnosFields(true, frm.prSTAT);
      rpcart.SetDefFocus();
    }
    else {
    	calc.run("IPOR = ISP - IBP;  IMAR = IBP - INAB");
    	calc.run("tds.ZT = IZT / KOL;  tds.RAB = IRAB / KOL;  tds.POR = IPOR / KOL");
      if (mode=='I') {
        initRpcart();
        if (frm.prSTAT!='K') {
          jtfKOL.requestFocus();
          jtfKOL.selectAll();
        }
      }
      else if (mode=='B') {
        rpcart.setMode("B");
      }
    }
  }

//  public void
  
  public boolean rekalk = false;
  
  void kalkulacija (int mode) {
  	if (frm.raDetail.getMode() == 'B' && !rekalk) return;
    
    boolean pzt = !frm.getMasterSet().getString("CSHZT").equals("YES");
    BigDecimal kol = frm.getDetailSet().getBigDecimal("KOL");
    
  	calc.set("jedval", Tecajevi.getJedVal(frm.getMasterSet().getString("OZNVAL")));
  	calc.set("tecaj", frm.getMasterSet().getBigDecimal("TECAJ"));
  	if (calc.get("tecaj").signum() == 0) calc.set("tecaj", Aus.one0);
  	
  	if (mode==0) {        // Kolicina
      if (frm.vrDok.equals("PRE")) {
        if (kol.signum() != 0) {
        	calc.run("NC = INAB / KOL");
          calcFromINAB();
        }
      } else {
      	calc.run("IDOB_VAL = DC_VAL * KOL;  IDOB = DC * KOL");
        calcFromIDOB();
      }
    } else if (mode==1) {   // Dobavljacev iznos u valuti
      if (kol.signum() == 0) {
      	calc.run("IDOB_VAL = 0");
        return;
      }
      calc.run("DC_VAL = IDOB_VAL / KOL;  IDOB = IDOB_VAL * tecaj / jedval;  DC = IDOB / KOL");
      calcFromIDOB();
    } else if (mode==2) {   // Dobavljaceva cijena u valuti
    	calc.run("IDOB_VAL = DC_VAL * KOL;  IDOB = IDOB_VAL * tecaj / jedval;  DC = IDOB / KOL");
      calcFromIDOB();
    } else if (mode==3) {   // Dobavljacev iznos
    	calc.run("IDOB_VAL = IDOB * jedval / tecaj");
    	calc.run("DC_VAL = IDOB_VAL / KOL;  DC = IDOB / KOL");
      calcFromIDOB();
    } else if (mode==4) {   // Dobavljaceva cijena
    	calc.run("IDOB = DC * KOL;  IDOB_VAL = IDOB * jedval / tecaj");
    	calc.run("DC_VAL = IDOB_VAL / KOL;  DC = IDOB / KOL");
      calcFromIDOB();
    } else if (mode==5) {   // Rabat u postotku
      calcFromIDOB();
    } else if (mode==6) {   // Rabat u iznosu
    } else if (mode==7) {   // ZT u postotku
    	calc.run("IZT = (IDOB - IRAB) % PZT;  INAB = IDOB - IRAB + IZT");
      calcFromINAB();
    } else if (mode==8) {   // ZT u iznosu
    	calc.run("INAB = IDOB - IRAB + IZT;  PZT = IZT %% (IDOB - IRAB)");
      calcFromINAB();
    } else if (mode==9) {   // Nabavna cijena
    	calc.run("INAB = NC * KOL");
      calcFromINAB();
    } else if (mode==10) {  // Marza u postotku
    	calc.run("MAR = NC % PMAR;  VC = NC + MAR;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
    } else if (mode==11) {  // Marza u iznosu
    	calc.run("PMAR = MAR %% NC;  VC = NC + MAR;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
    } else if (mode==12) {  // Veleprodajna cijena
    	calc.run("MAR = VC - NC;  PMAR = MAR %% NC;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
    } else if (mode==13) {  // Maloprodajna cijena
    	calc.run("VC = MC ~% Porezi.UKUPOR;  tds.POR = MC - VC;  MAR = VC - NC;  PMAR = MAR %% NC");
    }  	
  	calcSumFields();
  }

  void calcFromIDOB() {
  	calc.run("IRAB = IDOB % PRAB");
    if (!frm.getMasterSet().getString("CSHZT").equals("YES"))
    	calc.run("IZT = (IDOB - IRAB) % PZT");
    else {
      checkZT();
      calc.run("PZT = IZT %% (IDOB - IRAB)");
    }
    calc.run("INAB = IDOB - IRAB + IZT");
    calcFromINAB();
  }

  void calcFromINAB() {
    if (frm.getDetailSet().getBigDecimal("KOL").signum() == 0 ||
        frm.getDetailSet().getBigDecimal("INAB").signum() == 0) return;
    
    calc.run("NC = INAB / KOL");
    calcFromNC();
  }
  
  void calcFromNC() {
  	String chVC = frmParam.getParam("robno", "kalkchVC", "N", "Naèin kalkulacije cijene (N = fiksni VC, M = fiksni MC, D = fiksni PMAR)");
  	
    if ("D".equalsIgnoreCase(chVC) ||
           ("M".equalsIgnoreCase(chVC) && frm.getDetailSet().getBigDecimal("MC").signum() == 0) ||
           ("N".equalsIgnoreCase(chVC) && frm.getDetailSet().getBigDecimal("VC").signum() == 0))
    	calc.run("MAR = NC % PMAR;  VC = NC + MAR;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
    else if ("M".equalsIgnoreCase(chVC))
    	calc.run("VC = MC ~% Porezi.UKUPOR;  tds.POR = MC - VC;  MAR = VC - NC;  PMAR = MAR %% NC");
    else calc.run("MAR = VC - NC;  PMAR = MAR %% NC;  tds.POR = VC % Porezi.UKUPOR;  MC = VC + tds.POR");
  }
/**
 * Sumiranje za kolichinu
 */
  void calcSumFields() {
  	if (calc.evaluate("KOL").signum() != 0)
  		calc.run("tds.RAB = IRAB / KOL;  tds.ZT = IZT / KOL");
  	calc.run("IBP = VC * KOL;  IMAR = IBP - INAB;  ISP = MC * KOL;  IPOR = ISP - IBP");
  }

  public void jbInit1() throws Exception {
    rpcart.setMyAfterLookupOnNavigate(false);
    jlKOL.setHorizontalAlignment(SwingConstants.CENTER);
    jlKOL.setText(res.getString("jlKOL_text"));
    jtfKOL.setColumnName("KOL");
    jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfKOL_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfKOL_focusGained(e);
      }
    });
    jlZT.setText(res.getString("jlZT_text"));
    jlNC.setText("Proizvodna cijena");
    jlMAR.setText(res.getString("jlMAR_text"));
    jlVC.setText(res.getString("jlVC_text"));
    jlPOR.setText(res.getString("jlPOR_text"));
    jlMC.setText(res.getString("jlMC_text"));
    jlZaKolicinu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaKolicinu.setText(res.getString("jlZaKolicinu_text"));
    jlZaJedinicu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaJedinicu.setText(res.getString("jlZaJedinicu_text"));
    jlDC.setText(res.getString("jlDC_text"));
    jtfDC.setColumnName("DC");
    jtfDC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfDC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfDC_focusGained(e);
      }
    });
    jtfPRAB.setColumnName("PRAB");
    jtfPRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfPRAB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfPRAB_focusGained(e);
      }
    });
    jtfRAB.setColumnName("RAB");
    jtfRAB.setDataSet(tds);
    jtfRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfRAB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfRAB_focusGained(e);
      }
    });
    jtfNC.setColumnName("NC");
    jtfNC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfNC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfNC_focusGained(e);
      }
    });
    jtfPMAR.setColumnName("PMAR");
    jtfPMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfPMAR_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfPMAR_focusGained(e);
      }
    });
    jtfMAR.setColumnName("MAR");
    jtfMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfMAR_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfMAR_focusGained(e);
      }
    });
    jtfVC.setColumnName("VC");
    jtfVC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfVC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfVC_focusGained(e);
      }
    });
    jtfMC.setColumnName("MC");
    jtfMC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfMC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfMC_focusGained(e);
      }
    });
    jlRAB.setText(res.getString("jlRAB_text"));
    jtfIDOB.setColumnName("IDOB");
    jtfIDOB.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfIDOB_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfIDOB_focusGained(e);
      }
    });
    jtfIRAB.setColumnName("IRAB");
    jtfZT.setColumnName("ZT");
    jtfZT.setDataSet(tds);    
    jtfIZT.setColumnName("IZT");
    jtfIZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfIZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfZT_focusLost(e);
      }*/
    });
    jtfPOR.setColumnName("POR");
    jtfPOR.setDataSet(tds);
    jtfINAB.setColumnName("INAB");
    jtfIMAR.setColumnName("IMAR");
    jtfIBP.setColumnName("IBP");
    jtfIPOR.setColumnName("IPOR");
    jtfISP.setColumnName("ISP");
    jbZT.setText(res.getString("jbZT_text"));
    jbZT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbZT_actionPerformed(e);
      }
    });
    jtfPZT.setColumnName("PZT");
    jtfPZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfPZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfPZT_focusLost(e);
      }*/
    });
    jtfPPOR.setColumnName("UKUPOR");
    jtfPPOR.setDataSet(dm.getPorezi());
    jlPostotak.setText(res.getString("jlPostotak_text"));
    jlPostotak.setHorizontalAlignment(SwingConstants.CENTER);
    jpDetailCenter.setLayout(xYLayout2);
    jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
    xYLayout2.setWidth(645);
    xYLayout2.setHeight(215);
    jLabel1.setText("Dobavljaèeva cijena (valutna)");
    jtfDC_VAL.setColumnName("DC_VAL");
    jtfDC_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfDC_VAL_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfDC_VAL_focusLost(e);
      }*/
    });
    jtfIDOB_VAL.setColumnName("IDOB_VAL");
    jtfIDOB_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfIDOB_VAL_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfIDOB_VAL_focusLost(e);
      }*/
    });
    jpDetailCenter.add(jlKOL, new XYConstraints(360, 20, 130, -1));
    jpDetailCenter.add(jtfKOL, new XYConstraints(500, 20, 130, -1));
    jpDetailCenter.add(jlZaKolicinu, new XYConstraints(500, 50, 130, -1));
    jpDetailCenter.add(jlZaJedinicu, new XYConstraints(360, 50, 130, -1));
    jpDetailCenter.add(jlPostotak, new XYConstraints(270, 50, 80, -1));
    this.setLayout(new BorderLayout());
    tds.setColumns(new Column[] {
    		dM.createBigDecimalColumn("RAB", "Jedinièni RuC", 2),
    		dM.createBigDecimalColumn("ZT", "Jedinièni troškovi", 2),
    		dM.createBigDecimalColumn("POR", "Jedinièni porez", 2)});
    this.add(jpDetailCenter, BorderLayout.CENTER);
//    jpDetailCenter.add(jlDC, new XYConstraints(15, 100, -1, -1));
//    jpDetailCenter.add(jlRAB, new XYConstraints(15, 125, -1, -1));
//    jpDetailCenter.add(jlZT, new XYConstraints(15, 150, -1, -1));
    jpDetailCenter.add(jlNC, new XYConstraints(15, 75, -1, -1));
    jpDetailCenter.add(jlMAR, new XYConstraints(15, 100, -1, -1));
    jpDetailCenter.add(jlVC, new XYConstraints(15, 125, -1, -1));
    jpDetailCenter.add(jlPOR, new XYConstraints(15, 150, -1, -1));
    jpDetailCenter.add(jlMC, new XYConstraints(15, 175, -1, -1));
//    jpDetailCenter.add(jLabel1, new XYConstraints(15, 75, -1, -1));
//    jpDetailCenter.add(jtfDC, new XYConstraints(360, 100, 130, -1));
//    jpDetailCenter.add(jbZT, new XYConstraints(150, 150, 110, 21));
//    jpDetailCenter.add(jtfPRAB, new XYConstraints(270, 125, 80, -1));
//    jpDetailCenter.add(jtfPZT, new XYConstraints(270, 150, 80, -1));
    jpDetailCenter.add(jtfPMAR, new XYConstraints(270, 100, 80, -1));
    jpDetailCenter.add(jtfPPOR, new XYConstraints(270, 150, 80, -1));
//    jpDetailCenter.add(jtfRAB, new XYConstraints(360, 125, 130, -1));
//    jpDetailCenter.add(jtfZT, new XYConstraints(360, 150, 130, -1));
    jpDetailCenter.add(jtfNC, new XYConstraints(360, 75, 130, -1));
    jpDetailCenter.add(jtfMAR, new XYConstraints(360, 100, 130, -1));
    jpDetailCenter.add(jtfVC, new XYConstraints(360, 125, 130, -1));
    jpDetailCenter.add(jtfPOR, new XYConstraints(360, 150, 130, -1));
    jpDetailCenter.add(jtfMC, new XYConstraints(360, 175, 130, -1));
//    jpDetailCenter.add(jtfIDOB, new XYConstraints(500, 100, 130, -1));
//    jpDetailCenter.add(jtfIRAB, new XYConstraints(500, 125, 130, -1));
//    jpDetailCenter.add(jtfIZT, new XYConstraints(500, 150, 130, -1));
    jpDetailCenter.add(jtfINAB, new XYConstraints(500, 75, 130, -1));
    jpDetailCenter.add(jtfIMAR, new XYConstraints(500, 100, 130, -1));
    jpDetailCenter.add(jtfIPOR, new XYConstraints(500, 150, 130, -1));
    jpDetailCenter.add(jtfISP, new XYConstraints(500, 175, 130, -1));
    jpDetailCenter.add(jtfIBP, new XYConstraints(500, 125, 130, -1));
//    jpDetailCenter.add(jtfDC_VAL, new XYConstraints(360, 75, 130, -1));
//    jpDetailCenter.add(jtfIDOB_VAL, new XYConstraints(500, 75, 130, -1));
    this.add(rpcart, BorderLayout.NORTH);
    
    tds.open();
    if (tds.rowCount()==0) {
      tds.insertRow(true);
    }
  }
}