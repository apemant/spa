/****license*****************************************************************
**   file: frmArtikli.java
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

import hr.restart.baza.Artnap;
import hr.restart.baza.Condition;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.baza.norme;
import hr.restart.baza.raDataSet;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raPartialIncrementor;
import hr.restart.util.raTransaction;
import hr.restart.zapod.FrmPartneriArtikli;
import hr.restart.util.ImageLoad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import com.borland.dbswing.JdbLabel;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
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

public class frmArtikli extends raMatPodaci {
  String oldValue;      // stara vrijednost kod focus_Gain
  short findCGRART=0;
  short findCPOR=0;
  char cMode;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm;
  lookupData ld = lookupData.getlookupData();
  
  JPanel jp = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JraTextField jtfMC = new JraTextField() {
    public void valueChanged() {
      jtfMC_focusLost(null);
    }
  };
  JraButton jbDODTXT = new JraButton();
  JLabel jlNAZPROIZ = new JLabel();
  JPanel jpOsnovniPodaci = new JPanel();
  JraTextField jtfNAZART = new JraTextField() {
    public void valueChanged() {
      if (getRaQueryDataSet().getString("NAZPRI").trim().length() == 0)
        getRaQueryDataSet().setString("NAZPRI", jtfNAZART.getText());
    }
  };
  JLabel jlNAZPRI = new JLabel();
  JraTextField jtfNAZPRI = new JraTextField();
  JLabel jlVC = new JLabel();
  JraTextField jtfIPOR = new JraTextField();
  JraTextField jtfSIFZANAR = new JraTextField();
  JLabel jlCART = new JLabel();
  JLabel jlCPOR = new JLabel();
  JLabel jlSIFZANAR = new JLabel();
  JLabel jlMAR = new JLabel();
  JraTextField jtfCART1 = new JraTextField() {
	    public void valueChanged() {
	        jtfCART1_focusLost(null);
	      }
	    };
  JLabel jlBC = new JLabel();
  JLabel jlPOR = new JLabel();
  JraTextField jtfMINKOL = new JraTextField();
  JLabel jlZT = new JLabel();
  JPanel jpCijene = new JPanel();
  JLabel jlNC = new JLabel();
  JTabbedPane jtpArtikli = new JTabbedPane();
  JLabel jlKOLZANAR = new JLabel();
  JLabel jlMINKOL = new JLabel();
  JraTextField jtfDC = new JraTextField() {
    public void valueChanged() {
      jtfDC_focusLost(null);
    }
  };
  JLabel jlCART1 = new JLabel();
  JLabel jlMC = new JLabel();
  JraTextField jtfIMAR = new JraTextField() {
    public void valueChanged() {
      jtfIMAR_focusLost(null);
    }
  };
  JraTextField jtfVC = new JraTextField() {
    public void valueChanged() {
      jtfVC_focusLost(null);
    }
  };
  JdbLabel jdblNAZART4 = new JdbLabel();
  JraButton jbCGRART = new JraButton();
  JdbLabel jdblNAZART3 = new JdbLabel();
  JdbLabel jdblNAZART2 = new JdbLabel();
  JraTextField jtfPMAR = new JraTextField() {
    public void valueChanged() {
      jtfPMAR_focusLost(null);
    }
  };
  JPanel jpNarucivanje = new JPanel();
  JraTextField jtfNAZORIG = new JraTextField();
  JLabel jlNAZART4 = new JLabel();
  JLabel jlNAZART3 = new JLabel();
  JLabel jlNAZART2 = new JLabel();
  JLabel jlCGRART = new JLabel();
  JLabel jlNAZORIG = new JLabel();
  XYLayout xYLayout4 = new XYLayout();
  JLabel jlNAZART = new JLabel();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jtfBC = new JraTextField();
  JraTextField jtfZT = new JraTextField() {
    public void valueChanged() {
      jtfZT_focusLost(null);
    }
  };
  JraTextField jtfPZT = new JraTextField() {
    public void valueChanged() {
      jtfPZT_focusLost(null);
    }
  };
  JraTextField jtfNC = new JraTextField() {
    public void valueChanged() {
      jtfNC_focusLost(null);
    }
  };
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraTextField jtfNAZPROIZ = new JraTextField();
  JraTextField jtfCART = new JraTextField() {
    public void valueChanged() {
      jtfCART_focusLost(null);
    }
  };
  JraTextField jtfSIGKOL = new JraTextField();
  JLabel jlSIGKOL = new JLabel();
  JPanel jpPakiranje = new JPanel();
  JLabel jlJM = new JLabel();
  JraTextField jtfKOLZANAR = new JraTextField();
  JLabel jlDC = new JLabel();
  JlrNavField jrfCGRART = new JlrNavField();
  JlrNavField jrfNAZGRART = new JlrNavField();
  JLabel jlRAB = new JLabel();
  JraTextField jtfRAB = new JraTextField() {
    public void valueChanged() {
      jtfRAB_focusLost(null);
    }
  };
  JraTextField jtfPRAB = new JraTextField() {
    public void valueChanged() {
      jtfPRAB_focusLost(null);
    }
  };
  JraTextField jtfPPOR = new JraTextField();
  JLabel jlVRART = new JLabel();
  JlrNavField jrfNAZPOR = new JlrNavField();
  JlrNavField jrfJM = new JlrNavField();
  JlrNavField jrfNAZJM = new JlrNavField();

  JraButton jbCPOR = new JraButton();
  JlrNavField jrfCPOR = new JlrNavField();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JraCheckBox jdbCheckBox1 = new JraCheckBox();
  JLabel jLabel3 = new JLabel();
  JraTextField jtfPPOP = new JraTextField();
  JraTextField jtfPOP = new JraTextField();
  TableDataSet tds = new TableDataSet();
  QueryDataSet qdsDodTxt ;
//  Column column1 = new Column();
//  Column column2 = new Column();
//  Column column3 = new Column();
//  Column column4 = new Column();
//  Column column5 = new Column();
//  Column column6 = new Column();
  raPartialIncrementor pi;
  JraButton jbJMPAK = new JraButton();
  raComboBox rcbVRART = new raComboBox();
//  JraButton jbCVRART = new JraButton();
//  JlrNavField jrfNAZVRART = new JlrNavField();
//  JlrNavField jrfCVRART = new JlrNavField();
  JLabel jlOPIS = new JLabel();
  hr.restart.swing.JraTextArea jtfOPIS = new hr.restart.swing.JraTextArea();
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout5 = new XYLayout();
  JraTextField jtfTEZPAK = new JraTextField();
  JraTextField jtfBRJED = new JraTextField();
  JlrNavField jrfJMPAK = new JlrNavField();
  JLabel jlBRJED = new JLabel();
  JlrNavField jrfNAZJMPAK = new JlrNavField();
  JLabel jlJMPAK = new JLabel();
  JraButton jbJM = new JraButton();
  JLabel jlTEZPAK = new JLabel();
  TitledBorder titledBorder1;
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder2;
  XYLayout xYLayout6 = new XYLayout();
  JraTextField jtfTEZKOL = new JraTextField();
  JlrNavField jrfNAZJMKOL = new JlrNavField();
  JLabel jlJMPAK1 = new JLabel();
  JlrNavField jrfJMKOL = new JlrNavField();
  JLabel jlBRJEDKOL = new JLabel();
  JLabel jlTEZKOL = new JLabel();
  JraTextField jtfBRJEDKOL = new JraTextField();
  JraButton jbJMKOL = new JraButton();
  
  JLabel jlCPAR = new JLabel("Dobavljaè");
  JlrNavField jlrCPAR = new JlrNavField();
  JlrNavField jlrNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();
  
//  JraComboBox jraInventura = new JraComboBox();

  JraTextField postomanjka = new JraTextField();
  JraTextField postoprov = new JraTextField();
  raComboBox jraTipKalkul = new raComboBox();
  JPanel jpSubjekt = new JPanel();
  JlrNavField jlrPreSub = new JlrNavField();
  JlrNavField jlrNazSub = new JlrNavField();
  JraButton jbPreSub = new JraButton();
  
  JLabel jlNapo = new JLabel("Napomena");
  JlrNavField jlrCNAP = new JlrNavField();
  JlrNavField jlrNAZNAP = new JlrNavField();
  JraButton jbNapo = new JraButton();
  
  JLabel JlNap = new JLabel("Grupa napomena");
  JlrNavField jlrCAN = new JlrNavField();
  JlrNavField jlrNAZAN = new JlrNavField();
  JraButton jbNap = new JraButton();
  
  JraCheckBox jbKasa = new JraCheckBox();
  JraCheckBox jbPov = new JraCheckBox();
  
  raNavAction rnvSifArt = new raNavAction("Šifre Artikala",raImages.IMGOPEN,KeyEvent.VK_F12){
    public void actionPerformed(ActionEvent e) {
      rnvSifArt_actionPerformed(e);
    }
    
  };
  
  raNavAction rnvImgLoad = new raNavAction("Uèitaj sliku", raImages.IMGMOVIE, KeyEvent.VK_F6){
	    public void actionPerformed(ActionEvent e) {
	    	rnvImgLoad_actionPerformed(e);
	    }
	    
	  };
	  
	  raNavAction rnvNorm = new raNavAction("Normativ", raImages.IMGHISTORY, KeyEvent.VK_F7){
        public void actionPerformed(ActionEvent e) {
            rnvNorm_actionPerformed(e);
        }
        
      };
  
  
  JlrNavField jlrVrsub = new JlrNavField() {
	    public void after_lookUp() {
//	      afterVrsub(isLastLookSuccessfull());
	    }
	  };
	  JlrNavField jlrNazvrsub = new JlrNavField() {
	    public void after_lookUp() {
//	      afterVrsub(isLastLookSuccessfull());
	    }
	  };
	  JraButton jbVrsub = new JraButton();

  frmTableDataView viewReq = new frmTableDataView(false, false, true);
  

  public frmArtikli() {
//    super(2);
//    System.out.println("FrmArtikli sada u vlasnistvu S.G.-a");
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"Pakiranja");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"Kolete");
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllArtikli());
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        this_keyReleased(e);
      }
    });

    this.getRepRunner().addReport("hr.restart.robno.repArtikli",
                                  "hr.restart.robno.repArtikli",
                                  "Artikli",
                                  "Formatirani ispis artikla");

    this.setVisibleCols(new int[] {0,1,2,3,4});
    jp.setLayout(borderLayout1);
    jpOsnovniPodaci.setLayout(xYLayout1);
    jpCijene.setLayout(xYLayout2);
    jpNarucivanje.setLayout(xYLayout3);
    jpPakiranje.setLayout(xYLayout4);

    jlCART.setText(res.getString("jlCART_text"));
    jcbAKTIV.setText(res.getString("jcbAKTIV_text"));
    jlCART1.setText(res.getString("jlCART1_text"));
    jlBC.setText(res.getString("jlBC_text"));
    jlNAZART.setText(res.getString("jlNAZART_text"));
    jlNAZPRI.setText("Naziv za prikaz");
    jlNAZART2.setText(res.getString("jlNAZART_text"));
    jlNAZART3.setText(res.getString("jlNAZART_text"));
    jlNAZART4.setText(res.getString("jlNAZART_text"));
    jlJM.setText(res.getString("jlJM_text"));
    jlCPOR.setText(res.getString("jlCPOR_text"));
    jlCGRART.setText(res.getString("jlCGRART_text"));
    jlVRART.setText(res.getString("jlVRART_text"));
    jlNAZPROIZ.setText(res.getString("jlNAZPROIZ_text"));
    jlSIFZANAR.setText(res.getString("jlSIFZANAR_text"));
    jlNAZORIG.setText(res.getString("jlNAZORIG_text"));
    jlKOLZANAR.setText(res.getString("jlKOLZANAR_text"));
    jlSIGKOL.setText(res.getString("jlSIGKOL_text"));
    jlMINKOL.setText(res.getString("jlMINKOL_text"));
    jlDC.setText(res.getString("jlDC_text"));
    jlRAB.setText(res.getString("jlRAB_text"));
    jlZT.setText(res.getString("jlZT_text"));
    jlNC.setText(res.getString("jlNC_text"));
    jlMAR.setText(res.getString("jlMAR_text"));
    jlVC.setText(res.getString("jlVC_text"));
    jlPOR.setText(res.getString("jlPOR_text"));
    jlMC.setText(res.getString("jlMC_text"));
    jbCGRART.setText(res.getString("jbKEYF9_text"));

    jtfMC.setDataSet(getRaQueryDataSet());
    jtfMC.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfMC_focusGained(e);
      }
/*      public void focusLost(FocusEvent e) {
        jtfMC_focusLost(e);
      }*/
    });
    jtfMC.setColumnName("MC");
    jtfNAZART.setColumnName("NAZART");
    jtfNAZART.setDataSet(getRaQueryDataSet());
    jtfNAZPRI.setColumnName("NAZPRI");
    jtfNAZPRI.setDataSet(getRaQueryDataSet());
    jtfIPOR.setColumnName("IPOR");
    jtfIPOR.setDataSet(getRaQueryDataSet());
    jtfSIFZANAR.setColumnName("SIFZANAR");
    jtfSIFZANAR.setDataSet(getRaQueryDataSet());
    jtfCART1.setColumnName("CART1");
    jtfCART1.setDataSet(getRaQueryDataSet());
    jtfMINKOL.setColumnName("MINKOL");
    jtfMINKOL.setDataSet(getRaQueryDataSet());

    jrfJM.setColumnName("JM");
    jrfJM.setDataSet(getRaQueryDataSet());
    jrfJM.setColNames(new String[] {"NAZJM"});
    jrfJM.setVisCols(new int[]{0,1});
    jrfJM.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZJM});
    jrfJM.setRaDataSet(dm.getJedmj());
    jrfJM.setNavButton(jbJM);
    jrfNAZJM.setColumnName("NAZJM");
    jrfNAZJM.setNavProperties(jrfJM);
    jrfNAZJM.setSearchMode(1);
    
    jlrCPAR.setColumnName("CPAR");
    jlrCPAR.setDataSet(getRaQueryDataSet());
    jlrCPAR.setColNames(new String[] {"NAZPAR"});
    jlrCPAR.setVisCols(new int[]{0,1});
    jlrCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZPAR});
    jlrCPAR.setRaDataSet(dm.getPartneri());
    jlrCPAR.setNavButton(jbCPAR);
    jlrNAZPAR.setColumnName("NAZPAR");
    jlrNAZPAR.setNavProperties(jlrCPAR);
    jlrNAZPAR.setSearchMode(1);
    

    jtfDC.setColumnName("DC");
    jtfDC.setDataSet(getRaQueryDataSet());
    jtfDC.addFocusListener(new java.awt.event.FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
        jtfDC_focusLost(e);
      }*/
      public void focusGained(FocusEvent e) {
        jtfDC_focusGained(e);
      }
    });
    jtfVC.setColumnName("VC");
    jtfVC.setDataSet(getRaQueryDataSet());
    jtfVC.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfVC_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfVC_focusLost(e);
      }*/
    });
    jdblNAZART4.setForeground(Color.red);
    jdblNAZART4.setDataSet(getRaQueryDataSet());
    jdblNAZART4.setColumnName("NAZART");
    jbCGRART.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCGRART_actionPerformed(e);
      }
    });
    jdblNAZART3.setForeground(Color.red);
    jdblNAZART3.setDataSet(getRaQueryDataSet());
    jdblNAZART3.setColumnName("NAZART");
    jdblNAZART2.setForeground(Color.red);
    jdblNAZART2.setDataSet(getRaQueryDataSet());
    jdblNAZART2.setColumnName("NAZART");
    jtfPMAR.setColumnName("PMAR");
    jtfPMAR.setDataSet(getRaQueryDataSet());
    jtfPMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfPMAR_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfPMAR_focusLost(e);
      }*/
    });
    jtfNAZORIG.setColumnName("NAZORIG");
    jtfNAZORIG.setDataSet(getRaQueryDataSet());
    jtfBC.setColumnName("BC");
    jtfBC.setDataSet(getRaQueryDataSet());
    jtfZT.setColumnName("ZT");
    jtfZT.setDataSet(getRaQueryDataSet());
    jtfZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfZT_focusLost(e);
      }*/
    });
    jtfNC.setColumnName("NC");
    jtfNC.setDataSet(getRaQueryDataSet());
    jtfNC.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfNC_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfNC_focusLost(e);
      }*/
    });
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jtfNAZPROIZ.setColumnName("NAZPROIZ");
    jtfNAZPROIZ.setDataSet(getRaQueryDataSet());
    jtfCART.setColumnName("CART");
    jtfCART.setDataSet(getRaQueryDataSet());
    /*jtfCART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfCART_focusLost(e);
      }
    });*/
    jtfSIGKOL.setColumnName("SIGKOL");
    jtfSIGKOL.setDataSet(getRaQueryDataSet());
    jtfKOLZANAR.setColumnName("KOLZANAR");
    jtfKOLZANAR.setDataSet(getRaQueryDataSet());
    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setDataSet(getRaQueryDataSet());
    jrfCGRART.setColNames(new String[] {"NAZGRART"});
    jrfCGRART.setVisCols(new int[]{0,2});
    jrfCGRART.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZGRART});
    jrfCGRART.setRaDataSet(dm.getGrupart());
    jrfCGRART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfCGRART_focusLost(e);
      }
    });
    jrfNAZGRART.setColumnName("NAZGRART");
    jrfNAZGRART.setNavProperties(jrfCGRART);
    jrfNAZGRART.setSearchMode(1);
    jrfNAZGRART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfNAZGRART_focusLost(e);
      }
    });
    jrfNAZGRART.setVisCols(jrfCGRART.getVisCols());

    jtfRAB.setColumnName("RAB");
    jtfRAB.setDataSet(tds);
    jtfRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfRAB_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfRAB_focusLost(e);
      }*/
    });
    jtfPRAB.setColumnName("PRAB");
    jtfPRAB.setDataSet(tds);
    jtfPRAB.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfPRAB_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfPRAB_focusLost(e);
      }*/
    });
    jtfPPOR.setColumnName("PPOR");
    jtfPPOR.setDataSet(tds);

    jtfPZT.setColumnName("PZT");
    jtfPZT.setDataSet(tds);
    jtfPZT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfPZT_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfPZT_focusLost(e);
      }*/
    });
    jbCPOR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCPOR_actionPerformed(e);
      }
    });
    jbCPOR.setText(res.getString("jbKEYF9_text"));

    jrfCPOR.setRaDataSet(dm.getPorezi());
    jrfCPOR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPOR});
    jrfCPOR.setVisCols(new int[]{0,1});
    jrfCPOR.setColNames(new String[] {"NAZPOR"});
    jrfCPOR.setDataSet(getRaQueryDataSet());
    jrfCPOR.setColumnName("CPOR");
    jrfCPOR.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfCPOR_focusLost(e);
      }
      public void focusGained(FocusEvent e) {
        jrfCPOR_focusGained(e);
      }
    });

    jrfNAZPOR.setSearchMode(1);
    jrfNAZPOR.setNavProperties(jrfCPOR);
    jrfNAZPOR.setColumnName("NAZPOR");
    jrfNAZPOR.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfNAZPOR_focusLost(e);
      }
    });

    xYLayout1.setWidth(600);
    xYLayout1.setHeight(230);
    xYLayout2.setWidth(600);
    xYLayout2.setHeight(230); //256
    xYLayout3.setWidth(600);
    xYLayout3.setHeight(230);
    xYLayout4.setWidth(600);
    xYLayout4.setHeight(230);

    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("Posto (%)");
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("Iznos");
//    jtpArtikli.setPreferredSize(new Dimension(555, 256));
    jtpArtikli.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jtpArtikli_stateChanged(e);
      }
    });
//    jp.setMinimumSize(new Dimension(560, 281));
//    jp.setPreferredSize(new Dimension(560, 281));
    jdbCheckBox1.setHorizontalAlignment(SwingConstants.RIGHT);
    jdbCheckBox1.setHorizontalTextPosition(SwingConstants.LEFT);
    jdbCheckBox1.setText("Unos serijskih brojeva");
    jdbCheckBox1.setColumnName("ISB");
    jdbCheckBox1.setDataSet(getRaQueryDataSet());
    jdbCheckBox1.setSelectedDataValue("D");
    jdbCheckBox1.setUnselectedDataValue("N");
    jLabel3.setText("Popust na POS-u");
    jtfPPOP.setColumnName("PPOP");
    jtfPPOP.setDataSet(getRaQueryDataSet());

//    column1.setColumnName("PRAB");
//    column1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column1.setDisplayMask("###,###,##0.00");
//    column1.setDefault("0");
//    column1.setServerColumnName("NewColumn1");
//    column1.setSqlType(0);

//    column1 = dm.createBigDecimalColumn("PRAB",2);

//    column2.setColumnName("RAB");
//    column2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column2.setDisplayMask("###,###,##0.00");
//    column2.setDefault("0");
//    column2.setServerColumnName("NewColumn2");
//    column2.setSqlType(0);

//    column2 = dm.createBigDecimalColumn("RAB",2);

//    column3.setColumnName("PZT");
//    column3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column3.setDisplayMask("###,###,##0.00");
//    column3.setDefault("0");
//    column3.setServerColumnName("NewColumn1");
//    column3.setSqlType(0);

//    column3 = dm.createBigDecimalColumn("PZT",2);

//    column4.setColumnName("MAR");
//    column4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column4.setDisplayMask("###,###,##0.00");
//    column4.setDefault("0");
//    column4.setServerColumnName("NewColumn2");
//    column4.setSqlType(0);

//    column4 = dm.createBigDecimalColumn("MAR",2);

//    column5.setColumnName("PPOR");
//    column5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column5.setDisplayMask("###,###,##0.00");
//    column5.setDefault("0");
//    column5.setServerColumnName("NewColumn1");
//    column5.setSqlType(0);

//    column5 = dm.createBigDecimalColumn("PPOR",2);

//    column6.setColumnName("POP");
//    column6.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    column6.setDisplayMask("###,###,##0.00");
//    column6.setDefault("0");
//    column6.setServerColumnName("NewColumn2");
//    column6.setSqlType(0);

//    column6 = dm.createBigDecimalColumn("POP",2);

    tds.setColumns(new Column[] {
      dM.createBigDecimalColumn("PRAB",2),
      dM.createBigDecimalColumn("RAB",2),
      dM.createBigDecimalColumn("PZT",2),
      dM.createBigDecimalColumn("PZT",2),
      dM.createBigDecimalColumn("PPOR",2),
      dM.createBigDecimalColumn("POP",2),
      dM.createBigDecimalColumn("MAR",2),
    });

    jtfPOP.setEditable(false);
    jtfPOP.setColumnName("POP");
    jtfPOP.setDataSet(tds);
    jtfIMAR.setColumnName("MAR");
    jtfIMAR.setDataSet(tds);
    jtfIMAR.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfIMAR_focusGained(e);
      }
      /*public void focusLost(FocusEvent e) {
        jtfIMAR_focusLost(e);
      }*/
    });

/*    jbCVRART.setText(res.getString("jbKEYF9_text"));
    jbCVRART.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCVRART_actionPerformed(e);
      }
    });
    jrfNAZVRART.setColumnName("NAZVRART");
    jrfNAZVRART.setNavProperties(jrfCVRART);
    jrfNAZVRART.setSearchMode(1);
    jrfNAZVRART.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfNAZVRART_focusLost(e);
      }
    });
    jrfCVRART.setColumnName("VRART");
    jrfCVRART.setNavColumnName("CVRART");
    jrfCVRART.setDataSet(getRaQueryDataSet());
    jrfCVRART.setColNames(new String[] {"NAZVRART"});
    jrfCVRART.setVisCols(new int[]{0,1});
    jrfCVRART.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZVRART});
    jrfCVRART.setRaDataSet(dm.getVrart()); */

    rcbVRART.setRaDataSet(getRaQueryDataSet());
    rcbVRART.setRaColumn("VRART");
    rcbVRART.setRaItems(dm.getVrart(), "CVRART", "NAZVRART");

//    jlOPIS.setText("Opis");
//    jtfOPIS.setDataSet(getRaQueryDataSet());
//    jtfOPIS.setColumnName("OPIS");
    jbDODTXT.setIcon(raImages.getImageIcon(raImages.IMGCOMPOSEMAIL));
    jbDODTXT.setToolTipText("Dodatni text");
    jbDODTXT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDODTXT_actionPerformed();
      }
     });
    jPanel1.setBorder(titledBorder1);
    jPanel1.setDebugGraphicsOptions(0);
    jPanel1.setLayout(xYLayout5);
    jtfTEZPAK.setColumnName("BCPAK");
    jtfTEZPAK.setDataSet(getRaQueryDataSet());
    jtfBRJED.setColumnName("BRJED");
    jtfBRJED.setDataSet(getRaQueryDataSet());
    jrfJMPAK.setColumnName("JMPAK");
    jrfJMPAK.setNavColumnName("JM");
    jrfJMPAK.setDataSet(getRaQueryDataSet());
    jrfJMPAK.setColNames(new String[] {"NAZJM"});
    jrfJMPAK.setVisCols(new int[]{0,1});
    jrfJMPAK.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZJMPAK});
    jrfJMPAK.setRaDataSet(dm.getJedmj());
    jrfJMPAK.setNavButton(jbJMPAK);
    jlBRJED.setText(res.getString("jlBRJED_text"));
    jrfNAZJMPAK.setColumnName("NAZJM");
    jrfNAZJMPAK.setNavProperties(jrfJMPAK);
    jrfNAZJMPAK.setSearchMode(1);
    jlJMPAK.setText(res.getString("jlJM_text"));
    jlTEZPAK.setText("Barcode");
    jPanel2.setBorder(titledBorder2);
    jPanel2.setLayout(xYLayout6);
    jtfTEZKOL.setDataSet(getRaQueryDataSet());
    jtfTEZKOL.setColumnName("BCKOL");
    jrfNAZJMKOL.setSearchMode(1);
    jrfNAZJMKOL.setNavProperties(jrfJMKOL);
    jrfNAZJMKOL.setColumnName("NAZJM");
    jlJMPAK1.setText(res.getString("jlJM_text"));
    jrfJMKOL.setNavButton(jbJMKOL);
    jrfJMKOL.setRaDataSet(dm.getJedmj());
    jrfJMKOL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZJMKOL});
    jrfJMKOL.setVisCols(new int[]{0,1});
    jrfJMKOL.setColNames(new String[] {"NAZJM"});
    jrfJMKOL.setDataSet(getRaQueryDataSet());
    jrfJMKOL.setNavColumnName("JM");
    jrfJMKOL.setColumnName("JMKOL");
    jlBRJEDKOL.setText(res.getString("jlBRJED_text"));
    jlTEZKOL.setText("Barcode");
    jtfBRJEDKOL.setDataSet(getRaQueryDataSet());
    jtfBRJEDKOL.setColumnName("BRJEDKOL");
    
    postomanjka.setDataSet(getRaQueryDataSet());
    postomanjka.setColumnName("POSTOINV");
    postoprov.setDataSet(getRaQueryDataSet());
    postoprov.setColumnName("POSTOPROV");
    
//    jraTipKalkul.setDataSet(getRaQueryDataSet());
//    jraTipKalkul.set
    
    
    
    
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[]{dM.createStringColumn("TIPINV","Vrsta inventure",1),
            dM.createStringColumn("OPIS","Vrsta inventure",50)});
    
    qds.open();
    qds.insertRow(false);
    qds.setString("TIPINV","I");
    qds.setString("OPIS","Ukupni izlaz");
    
/*    
    qds.insertRow(false);
    qds.setString("TIPINV","Z");
    qds.setString("OPIS","Zaliha");
*/    

    
    
    jraTipKalkul.setRaDataSet(getRaQueryDataSet());
    jraTipKalkul.setRaColumn("TIPINV");
    jraTipKalkul.setRaItems(qds, "TIPINV", "OPIS");

    
    
    
    jp.add(jtpArtikli, BorderLayout.CENTER);
    jtpArtikli.add(jpOsnovniPodaci, res.getString("jpOsnovniPodaci_text"));
    jpOsnovniPodaci.add(jlCART, new XYConstraints(15, 20, -1, -1));
    jpOsnovniPodaci.add(jlCART1, new XYConstraints(15, 45, -1, -1));
    jpOsnovniPodaci.add(jtfCART, new XYConstraints(150, 20, 100, -1));
    jpOsnovniPodaci.add(jcbAKTIV, new XYConstraints(415, 18, 100, -1));
    jpOsnovniPodaci.add(jtfCART1, new XYConstraints(150, 45, 150, -1));
    jpOsnovniPodaci.add(jdbCheckBox1, new XYConstraints(375, 193, 140, -1));
    jpOsnovniPodaci.add(jlBC, new XYConstraints(315, 50, -1, -1));
    jpOsnovniPodaci.add(jtfBC, new XYConstraints(395, 45, 120, -1));
    jpOsnovniPodaci.add(jlNAZART, new XYConstraints(15, 70, -1, -1));
    jpOsnovniPodaci.add(jtfNAZART, new XYConstraints(150, 70, 365, -1));
    jpOsnovniPodaci.add(jlNAZPRI, new XYConstraints(15, 170, -1, -1));
    jpOsnovniPodaci.add(jtfNAZPRI, new XYConstraints(150, 170, 365, -1));
    jpOsnovniPodaci.add(jbDODTXT, new XYConstraints(519, 70, 21, 21));
    jpOsnovniPodaci.add(jlJM, new XYConstraints(15, 95, -1, -1));
    jpOsnovniPodaci.add(jlCPOR, new XYConstraints(15, 120, -1, -1));
    jpOsnovniPodaci.add(jlCGRART, new XYConstraints(15, 145, -1, -1));
    jpOsnovniPodaci.add(jlVRART, new XYConstraints(15, 195, -1, -1));
    jpOsnovniPodaci.add(jrfJM, new XYConstraints(150, 95, 50, -1));
    jpOsnovniPodaci.add(jrfCPOR, new XYConstraints(150, 120, 50, -1));
    jpOsnovniPodaci.add(jrfCGRART, new XYConstraints(150, 145, -1, -1));
    jpOsnovniPodaci.add(rcbVRART, new XYConstraints(150, 195, 100, -1));
//    jpOsnovniPodaci.add(jrfCVRART, new XYConstraints(150, 170, 50, -1));
    jpOsnovniPodaci.add(jrfNAZJM, new XYConstraints(210, 95, 305, -1));
    jpOsnovniPodaci.add(jrfNAZPOR, new XYConstraints(210, 120, 305, -1));
    jpOsnovniPodaci.add(jrfNAZGRART, new XYConstraints(260, 145, 255, -1));
//    jpOsnovniPodaci.add(jrfNAZVRART, new XYConstraints(210, 170, 305, -1));
    jpOsnovniPodaci.add(jbJM, new XYConstraints(519, 95, 21, 21));
    jpOsnovniPodaci.add(jbCPOR, new XYConstraints(519, 120, 21, 21));
    jpOsnovniPodaci.add(jbCGRART, new XYConstraints(519, 145, 21, 21));
//    jpOsnovniPodaci.add(jbCVRART, new XYConstraints(519, 170, 21, 21));
//    jpOsnovniPodaci.add(jlOPIS, new XYConstraints(15, 195, -1, -1));
//    jpOsnovniPodaci.add(jtfOPIS,  new XYConstraints(150, 195, 365, 45));
    jpCijene.add(jlNAZART2, new XYConstraints(15, 20, -1, -1));
    jpCijene.add(jdblNAZART2, new XYConstraints(150, 20, -1, -1));
    jpCijene.add(jlDC, new XYConstraints(15, 80, -1, -1));
    jpCijene.add(jlNC, new XYConstraints(15, 130, -1, -1));
    jpCijene.add(jtfDC, new XYConstraints(150, 80, 100, -1));
    jpCijene.add(jlRAB, new XYConstraints(260, 80, -1, -1));
    jpCijene.add(jtfPRAB, new XYConstraints(370, 80, 60, -1));
    jpCijene.add(jtfRAB, new XYConstraints(440, 80, 100, -1));
    jpCijene.add(jtfPZT, new XYConstraints(370, 105, 60, -1));
    jpCijene.add(jtfZT, new XYConstraints(440, 105, 100, -1));
    jpCijene.add(jtfNC, new XYConstraints(150, 130, 100, -1));
    jpCijene.add(jlMAR, new XYConstraints(260, 130, -1, -1));
    jpCijene.add(jtfIMAR, new XYConstraints(440, 130, 100, -1));
    jpCijene.add(jlVC, new XYConstraints(15, 155, -1, -1));
    jpCijene.add(jtfVC, new XYConstraints(150, 155, 100, -1));
    jpCijene.add(jlPOR, new XYConstraints(260, 155, -1, -1));
    jpCijene.add(jtfIPOR, new XYConstraints(440, 155, 100, -1));
    jpCijene.add(jlMC, new XYConstraints(15, 180, -1, -1));
    jpCijene.add(jtfMC, new XYConstraints(150, 180, 100, -1));
    jpCijene.add(jtfPMAR, new XYConstraints(370, 130, 60, -1));
    jpCijene.add(jtfPPOR, new XYConstraints(370, 155, 60, -1));
    jpCijene.add(jlZT, new XYConstraints(260, 105, -1, -1));
    jpCijene.add(jLabel1, new XYConstraints(370, 60, 60, -1));
    jpCijene.add(jLabel2, new XYConstraints(440, 60, 100, -1));
    jpCijene.add(jLabel3, new XYConstraints(260, 180, -1, -1));
    jpCijene.add(jtfPPOP, new XYConstraints(370, 180, 60, -1));
    jpCijene.add(jtfPOP, new XYConstraints(440, 180, 100, -1));
    jtpArtikli.add(jpNarucivanje, res.getString("jpNarucivanje_text"));
    jtpArtikli.add(jpCijene, res.getString("jpCijene_text"));
    jpNarucivanje.add(jlNAZART3, new XYConstraints(15, 20, -1, -1));
    jpNarucivanje.add(jlCPAR, new XYConstraints(15, 45, -1, -1));
    jpNarucivanje.add(jlrCPAR, new XYConstraints(150, 45, 100, -1));
    jpNarucivanje.add(jlrNAZPAR, new XYConstraints(255, 45, 285, -1));    
    jpNarucivanje.add(jbCPAR, new XYConstraints(545, 45, 21, 21));

    

    
    jpNarucivanje.add(jlNAZPROIZ, new XYConstraints(15, 70, -1, -1));
    jpNarucivanje.add(jdblNAZART3, new XYConstraints(150, 20, -1, -1));
    jpNarucivanje.add(jtfSIFZANAR, new XYConstraints(150, 95, 150, -1));
    jpNarucivanje.add(jlNAZORIG, new XYConstraints(15, 120, -1, -1));
    jpNarucivanje.add(jlSIFZANAR, new XYConstraints(15, 95, -1, -1));
    jpNarucivanje.add(jtfNAZORIG, new XYConstraints(150, 120, 390, -1));
    jpNarucivanje.add(jtfKOLZANAR, new XYConstraints(150, 145, 100, -1));

    
    jpNarucivanje.add(new JLabel("Postotak provizije"), 
        new XYConstraints(260, 145, 150, -1));    
    jpNarucivanje.add(postoprov, new XYConstraints(440, 145, 100, -1));
    
    jpNarucivanje.add(new JLabel("Postotak otpisa"), 
            new XYConstraints(260, 170, 150, -1));    
    jpNarucivanje.add(postomanjka, new XYConstraints(440, 170, 100, -1));
    jpNarucivanje.add(new JLabel("Naèin otpisa"),
            new XYConstraints(260, 195, 150, -1));
    jpNarucivanje.add(jraTipKalkul, new XYConstraints(392, 195, 150, -1));

    
    // Dio oko subjekta radnog naloga 
    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);
    jlrVrsub.setDataSet(getRaQueryDataSet());

    jlrNazvrsub.setSearchMode(1);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setNavProperties(jlrVrsub);
    XYLayout xysubject = new XYLayout();
    jpSubjekt.setLayout(xysubject);

    jlrPreSub.setColumnName("CSUBRN");
    jlrPreSub.setColNames(new String[] {"BROJ"});
    jlrPreSub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSub});
    jlrPreSub.setVisCols(new int[] {0,1});
    jlrPreSub.setSearchMode(3);
    jlrPreSub.setRaDataSet(dm.getRN_subjekt());
    jlrPreSub.setDataSet(getRaQueryDataSet());
    jlrPreSub.setNavButton(jbPreSub);

    jlrNazSub.setSearchMode(1);
    jlrNazSub.setColumnName("BROJ");
    jlrNazSub.setNavProperties(jlrPreSub);
    
    raDataSet artnap = new raDataSet();
    frmArtNap.createMain(artnap);
    artnap.setTableName("artnap");
    
    jlrCNAP.setColumnName("CNAP");
    jlrCNAP.setColNames(new String[] {"NAZNAP"});
    jlrCNAP.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZNAP});
    jlrCNAP.setVisCols(new int[] {0,1});
    jlrCNAP.setSearchMode(0);
    jlrCNAP.setRaDataSet(dm.getNapomene());
    jlrCNAP.setDataSet(getRaQueryDataSet());
    jlrCNAP.setNavButton(jbNapo);
    
    jlrNAZNAP.setColumnName("NAZNAP");
    jlrNAZNAP.setSearchMode(1);
    jlrNAZNAP.setNavProperties(jlrCNAP);
    
    jlrCAN.setColumnName("CAN");
    jlrCAN.setColNames(new String[] {"NAZAN"});
    jlrCAN.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZAN});
    jlrCAN.setVisCols(new int[] {0,1});
    jlrCAN.setSearchMode(0);
    jlrCAN.setRaDataSet(artnap);
    jlrCAN.setDataSet(getRaQueryDataSet());
    jlrCAN.setNavButton(jbNap);
    
    jlrNAZAN.setColumnName("NAZAN");
    jlrNAZAN.setSearchMode(1);
    jlrNAZAN.setNavProperties(jlrCAN);
    
    jbKasa.setDataSet(getRaQueryDataSet());
    jbKasa.setColumnName("KASA");
    jbKasa.setSelectedDataValue("D");
    jbKasa.setUnselectedDataValue("N");
    jbKasa.setText(" Artikl za kasu ");
    jbKasa.setHorizontalTextPosition(JLabel.LEADING);
    jbKasa.setHorizontalAlignment(JLabel.TRAILING);
    
    jbPov.setDataSet(getRaQueryDataSet());
    jbPov.setColumnName("POV");
    jbPov.setSelectedDataValue("D");
    jbPov.setUnselectedDataValue("N");
    jbPov.setText(" Povratna naknada ");
    jbPov.setHorizontalTextPosition(JLabel.LEADING);
    jbPov.setHorizontalAlignment(JLabel.TRAILING);
    
    jpSubjekt.add(jlrNazvrsub, new XYConstraints(255, 20, 250, -1));
    jpSubjekt.add(jlrVrsub, new XYConstraints(150, 20, 100, -1));
    jpSubjekt.add(jbVrsub, new XYConstraints(510, 20, 21, 21));
    jpSubjekt.add(new JLabel("Vrsta subjekta"), new XYConstraints(15, 20, -1, -1));
    
    jpSubjekt.add(jlrNazSub, new XYConstraints(255, 45, 250, -1));
    jpSubjekt.add(jlrPreSub, new XYConstraints(150, 45, 100, -1));
    jpSubjekt.add(jbPreSub, new XYConstraints(510, 45, 21, 21));
    jpSubjekt.add(new JLabel("Subjekt"), new XYConstraints(15, 45, -1, -1));
    
    jpSubjekt.add(jlNapo, new XYConstraints(15, 80, -1, -1));
    jpSubjekt.add(jlrCNAP, new XYConstraints(150, 80, 100, -1));
    jpSubjekt.add(jlrNAZNAP, new XYConstraints(255, 80, 250, -1));
    jpSubjekt.add(jbNapo, new XYConstraints(510, 80, 21, 21));
    jpSubjekt.add(JlNap, new XYConstraints(15, 105, -1, -1));
    jpSubjekt.add(jlrCAN, new XYConstraints(150, 105, 100, -1));
    jpSubjekt.add(jlrNAZAN, new XYConstraints(255, 105, 250, -1));
    jpSubjekt.add(jbNap, new XYConstraints(510, 105, 21, 21));
    jpSubjekt.add(jbKasa, new XYConstraints(255, 130, 250, -1));
    jpSubjekt.add(jbPov, new XYConstraints(255, 155, 250, -1));

    
    jpNarucivanje.add(jtfNAZPROIZ, new XYConstraints(150, 70, 390, -1));
    jpNarucivanje.add(jlKOLZANAR, new XYConstraints(15, 145, -1, -1));
    jpNarucivanje.add(jlSIGKOL, new XYConstraints(15, 170, -1, -1));
    jpNarucivanje.add(jlMINKOL, new XYConstraints(15, 195, -1, -1));
    jpNarucivanje.add(jtfSIGKOL, new XYConstraints(150, 170, 100, -1));
    jpNarucivanje.add(jtfMINKOL, new XYConstraints(150, 195, 100, -1));
    jtpArtikli.add(jpPakiranje, res.getString("jpPakiranje_text"));
    jpPakiranje.add(jlNAZART4, new XYConstraints(15, 20, -1, -1));
    jpPakiranje.add(jdblNAZART4, new XYConstraints(150, 20, -1, -1));

    jpPakiranje.add(jPanel1,      new XYConstraints(15, 50, 525, 85));
    jPanel1.add(jlJMPAK, new XYConstraints(5, 5, -1, -1));
    jPanel1.add(jlBRJED, new XYConstraints(5, 30, -1, -1));
    jPanel1.add(jrfJMPAK, new XYConstraints(130, 5, 50, -1));
    jPanel1.add(jtfBRJED, new XYConstraints(130, 30, -1, -1));
    jPanel1.add(jrfNAZJMPAK, new XYConstraints(190, 5, 290, -1));
    jPanel1.add(jlTEZPAK, new XYConstraints(275, 30, 78, -1));
    jPanel1.add(jtfTEZPAK,  new XYConstraints(360, 30, 120, -1));
    jPanel1.add(jbJMPAK,   new XYConstraints(485, 5, 21, 21));
    jpPakiranje.add(jPanel2,     new XYConstraints(15, 135, 525, 85));
    jPanel2.add(jtfTEZKOL,   new XYConstraints(360, 30, 120, -1));
    jPanel2.add(jrfNAZJMKOL,   new XYConstraints(190, 5, 290, -1));
    jPanel2.add(jlJMPAK1,  new XYConstraints(5, 5, -1, -1));
    jPanel2.add(jrfJMKOL,   new XYConstraints(135, 5, 50, -1));
    jPanel2.add(jlBRJEDKOL,  new XYConstraints(5, 30, -1, -1));
    jPanel2.add(jlTEZKOL,   new XYConstraints(275, 30, -1, -1));
    jPanel2.add(jtfBRJEDKOL,  new XYConstraints(135, 30, -1, -1));
    jPanel2.add(jbJMKOL,   new XYConstraints(485, 5, 21, 21));
//    jtpArtikli.add(jpOsnovniPodaci, "jpOsnovniPodaci");
//    jp.add(jtpArtikli, BorderLayout.NORTH);
    jtpArtikli.add(jpSubjekt, "Ostalo");
    int size = Integer.parseInt(frmParam.getParam("robno","cartSize","0"));
    if (size > 0 ) {
      new raTextMask(jtfCART1,size,false,raTextMask.ALL | raTextMask.PLACEHOLDER).
          setMaskCharacter(frmParam.getParam("robno", "artMask", "#").charAt(0));
    }
    int sizeBC = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("robno","cartSizeBC","0"));
    if (sizeBC > 0) {
      new raTextMask(jtfBC,size,false,raTextMask.ALL | raTextMask.PLACEHOLDER).
          setMaskCharacter(frmParam.getParam("robno", "artMask", "#").charAt(0));
    }
    pi = new raPartialIncrementor(jtfCART1);
//    raDataIntegrity.installFor(this);
    raDataIntegrity di = raDataIntegrity.installFor(this);
    di.setProtectedColumns(new String[] {"CART1","TIPART","VRART"});
    di.addOtherTable("norme", new String[] {"CARTNOR"});
//    raDataIntegrity.installFor(this).setProtectedColumns(
//        new String[] {"CART1","BC","TIPART","VRART"});
    jtpArtikli.setSelectedIndex(0);
    this.addOption(rnvSifArt,5);
    this.addOption(rnvNorm,6);
    this.addOption(rnvImgLoad,7);
  }
/*

  */

public boolean  doWithSave(char mode) {
//  raControlDocs rCD = new raControlDocs();
  if (mode=='B') {
    dm.getVTText().open();
    if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getVTText(),new String[] {"CKEY"},
                  new String[] {raControlDocs.getKey(this.getRaQueryDataSet())})){
      dm.getVTText().deleteRow();
      raTransaction.saveChanges(dm.getVTText());
      markChange(dm.getVTText());
    }
  } else {
    if (qdsDodTxt != null) {
//      if (qdsDodTxt.getRowCount() !=0)
        raTransaction.saveChanges(qdsDodTxt);
        markChange("vttext");
    }
  }
  return true;
}

  public boolean Validacija(char mode) {

    String[] key = new String[] {"CART"};
    if (mode=='N') {
      if (jtfCART1.getFieldMask() instanceof raTextMask) {
        raTextMask m = (raTextMask) jtfCART1.getFieldMask();
        if (m.isMasked())
          jtfCART1.setText(jtfCART1.getText().replace(m.getMaskCharacter(), ' ').trim());
      }
      if (hr.restart.util.Valid.getValid().notUnique(jtfCART))
        return false;
      if (hr.restart.util.Valid.getValid().notUnique(jtfCART1))
        return false;
      if (hr.restart.util.Valid.getValid().notUnique(jtfBC))
        return false;
    }
    else if (mode=='I') {
      if (hr.restart.util.Valid.getValid().notUniqueUPD(jtfCART1,key))
        return false;
      if (hr.restart.util.Valid.getValid().notUniqueUPD(jtfBC,key))
        return false;
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZART))
      return false;
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZPRI))
      return false;
    if (hr.restart.util.Valid.getValid().isEmpty(jrfJM))
      return false;
    if (hr.restart.util.Valid.getValid().isEmpty(jrfCPOR))
      return false;
    return true;
  }

  public void SetFokus(char mode) {
    cMode=mode;
    qdsDodTxt = null;
    if (tds.rowCount()==0) {
      tds.insertRow(true);
    }
    if (mode=='N') {
      rcbVRART.this_itemStateChanged();
      getRaQueryDataSet().setInt("CART", rdUtil.getUtil().getMaxArtikl());
      pi.init(hr.restart.util.Util.getNewQueryDataSet("SELECT cart1 FROM artikli"), "CART1");
//          Artikli.getDataModule().getTempSet(), "CART1");
      if(!hr.restart.sisfun.frmParam.getParam("robno","indiCart").equals("CART"))
      {
        //rcc.setLabelLaF(jtfCART, false);
        jtfCART1.requestFocus();
      }
      else
      {
        //rcc.setLabelLaF(jtfCART, true);
        jtfCART.requestFocus();
      }
      jrfNAZGRART.setText("");
      jrfNAZPOR.setText("");
      jrfNAZJM.setText("");
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfCART, false);
      rcbVRART.this_itemStateChanged();

//      if (util.chkIsDeleteable("STANJE", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false && util.chkIsDeleteable("STPOS", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false) {
//        rcc.setLabelLaF(jtfCART1, false);
//        rcc.setLabelLaF(jtfBC, false);
//        jtfNAZART.requestFocus();
//      }
//      else {
        jtfCART1.requestFocus();
//      }
    }
    this.jtpArtikli.setSelectedIndex(0);
  }

/*  public boolean DeleteCheck() {
    if (util.isDeleteable("STANJE", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("STDOKU", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("STDOKI", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("NORME", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("CJENIK", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("DOB_ART", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("KUP_ART", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("SKUPART", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("STMESKLA", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    if (util.isDeleteable("STPOS", "CART", String.valueOf(getRaQueryDataSet().getInt("CART")), util.MOD_NUM)==false)
      return false;
    return true;
  }*/
  public void EntryPoint(char mode) {
  }

  void jbCGRART_actionPerformed(ActionEvent e) {
    if (findCGRART==1) {
      this.jrfNAZGRART.keyF9Pressed();
    }
    else {
      this.jrfCGRART.keyF9Pressed();
    }
  }

  void jrfCGRART_focusLost(FocusEvent e) {
    findCGRART=0;
  }

  void jrfNAZGRART_focusLost(FocusEvent e) {
    findCGRART=1;
  }
  void jbCPOR_actionPerformed(ActionEvent e) {
    if (findCPOR==1) {
      this.jrfNAZPOR.keyF9Pressed();
    }
    else {
      this.jrfCPOR.keyF9Pressed();
    }
  }
  void jrfCPOR_focusLost(FocusEvent e) {
    findCPOR=0;
  }

  void jrfNAZPOR_focusLost(FocusEvent e) {
    findCPOR=1;
  }

  void jrfCPOR_focusGained(FocusEvent e) {
    oldValue=jrfCPOR.getText();
  }
  /**
   * Postavljenje fokusa kod promjene tabpanela
   */
  void jtpArtikli_stateChanged(ChangeEvent e) {
    if (jtpArtikli.getSelectedIndex()==0) {
    }
    else if (jtpArtikli.getSelectedIndex()==2) {
      if (this.jrfCPOR.getText().trim().equals("")) {
        JOptionPane.showConfirmDialog(this.jp,"Nije upisana porezna grupa !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        this.jtpArtikli.setSelectedIndex(0);
        this.jrfCPOR.selectAll();
        this.jrfCPOR.requestFocus();
      }
      else {
        findVirtualFields();
        this.jtfDC.requestFocus();
      }
    }
    else if (jtpArtikli.getSelectedIndex()==2) {
      this.jtfNAZPROIZ.requestFocus();
    }
    else if (jtpArtikli.getSelectedIndex()==3) {
//      this.jtfNAZPAK.requestFocus();
    }
  }
  /**
   * Ovo je da se na PG_UP/PG_DW kreche tabovima
   */
  void this_keyReleased(KeyEvent e) {
    if (!jtpArtikli.isVisible()) return;
/*    if (e.getKeyCode()==e.VK_PAGE_UP) {
      if (this.jtpArtikli.getSelectedIndex()==3) {
        this.jtpArtikli.setSelectedIndex(0);
      }
      else {
        this.jtpArtikli.setSelectedIndex(this.jtpArtikli.getSelectedIndex()+1);
      }
    }
    else if (e.getKeyCode()==e.VK_PAGE_DOWN) {
      if (this.jtpArtikli.getSelectedIndex()==0) {
        this.jtpArtikli.setSelectedIndex(3);
      }
      else {
        this.jtpArtikli.setSelectedIndex(this.jtpArtikli.getSelectedIndex()-1);
      }
    }*/
  }
  /**
   * Kalkulacija sa modovima
   * - 0 - DC, PRAB
   * - 1 - RAB
   * - 2 - ZT
   * - 3 - NC
   * - 4 - MAR
   * - 5 - VC
   * - 6 - MC
   */
  void calc(int mode) {
    if (mode==0) {
      tds.setBigDecimal("RAB",              util.findIznos    (getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("PRAB")));
      getRaQueryDataSet().setBigDecimal("ZT",   util.findIznos    (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), tds.getBigDecimal("PZT")));
      getRaQueryDataSet().setBigDecimal("NC",   util.sumValue     (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
      tds.setBigDecimal("MAR",              util.findIznos    (getRaQueryDataSet().getBigDecimal("NC"), getRaQueryDataSet().getBigDecimal("PMAR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==1) {
      tds.setBigDecimal("PRAB",             util.findPostotak (getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")));
      getRaQueryDataSet().setBigDecimal("ZT",   util.findIznos    (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), tds.getBigDecimal("PZT")));
      getRaQueryDataSet().setBigDecimal("NC",   util.sumValue     (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
      tds.setBigDecimal("MAR",              util.findIznos    (getRaQueryDataSet().getBigDecimal("NC"), getRaQueryDataSet().getBigDecimal("PMAR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==2) {
      tds.setBigDecimal("PZT",              util.findPostotak (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
      getRaQueryDataSet().setBigDecimal("NC",   util.sumValue     (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
      tds.setBigDecimal("MAR",              util.findIznos    (getRaQueryDataSet().getBigDecimal("NC"), getRaQueryDataSet().getBigDecimal("PMAR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==3) {
      getRaQueryDataSet().setBigDecimal("ZT",   util.negateValue  (util.sumValue(getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("DC")));
      tds.setBigDecimal("PZT",              util.findPostotak (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
      tds.setBigDecimal("MAR",              util.findIznos    (getRaQueryDataSet().getBigDecimal("NC"), getRaQueryDataSet().getBigDecimal("PMAR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==4) {
      getRaQueryDataSet().setBigDecimal("PMAR", util.findPostotak (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==5) {
      tds.setBigDecimal("MAR",              util.negateValue  (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("NC")));
      getRaQueryDataSet().setBigDecimal("PMAR", util.findPostotak (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("VC"), tds.getBigDecimal("PPOR")));
      getRaQueryDataSet().setBigDecimal("MC",   util.sumValue     (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("IPOR")));
    }
    else if (mode==6) {
      getRaQueryDataSet().setBigDecimal("IPOR", util.findIznos    (getRaQueryDataSet().getBigDecimal("MC"), dm.getPorezi().getBigDecimal("UKUNPOR")));
      getRaQueryDataSet().setBigDecimal("VC",   util.negateValue  (getRaQueryDataSet().getBigDecimal("MC"), getRaQueryDataSet().getBigDecimal("IPOR")));
      tds.setBigDecimal("MAR",              util.negateValue  (getRaQueryDataSet().getBigDecimal("VC"), getRaQueryDataSet().getBigDecimal("NC")));
      getRaQueryDataSet().setBigDecimal("PMAR", util.findPostotak (getRaQueryDataSet().getBigDecimal("NC"), tds.getBigDecimal("MAR")));
    }
  }
  /**
   * Trazhi vrijednosti za virtualna polja
   */
  void findVirtualFields() {
    dm.getPorezi().interactiveLocate(getRaQueryDataSet().getString("CPOR"),"CPOR",com.borland.dx.dataset.Locate.FIRST, false);
    tds.setBigDecimal("PPOR", dm.getPorezi().getBigDecimal("UKUPOR"));
    tds.setBigDecimal("RAB",              util.negateValue    (util.sumValue(getRaQueryDataSet().getBigDecimal("DC"), getRaQueryDataSet().getBigDecimal("ZT")), getRaQueryDataSet().getBigDecimal("NC")));
    tds.setBigDecimal("PRAB",             util.findPostotak   (getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")));
    tds.setBigDecimal("PZT",              util.findPostotak   (util.negateValue(getRaQueryDataSet().getBigDecimal("DC"), tds.getBigDecimal("RAB")), getRaQueryDataSet().getBigDecimal("ZT")));
    rcc.setLabelLaF(jtfPPOR, false);
    rcc.setLabelLaF(jtfIPOR, false);
    rcc.setLabelLaF(jtfPOP, false);
  }

  void jtfDC_focusGained(FocusEvent e) {
    oldValue=jtfDC.getText();
  }
  void jtfDC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfDC.getText())) {
      calc(0);
    }
    if (getRaQueryDataSet().getBigDecimal("DC").doubleValue()==0) {
      tds.setBigDecimal("PRAB", _Main.nul);
      tds.setBigDecimal("PZT", _Main.nul);
      getRaQueryDataSet().setBigDecimal("PMAR", _Main.nul);
    }
  }

  void jtfPRAB_focusGained(FocusEvent e) {
    oldValue=jtfPRAB.getText();
  }
  void jtfPRAB_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPRAB.getText())) {
      calc(0);
    }
  }

  void jtfRAB_focusGained(FocusEvent e) {
    oldValue=jtfRAB.getText();
  }
  void jtfRAB_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfRAB.getText())) {
      calc(1);
    }
  }

  void jtfPZT_focusGained(FocusEvent e) {
    oldValue=jtfPZT.getText();
  }
  void jtfPZT_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPZT.getText())) {
      calc(0);
    }
  }

  void jtfZT_focusGained(FocusEvent e) {
    oldValue=jtfZT.getText();
  }
  void jtfZT_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfZT.getText())) {
      calc(2);
    }
  }

  void jtfNC_focusGained(FocusEvent e) {
    oldValue=jtfNC.getText();
  }
  void jtfNC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfNC.getText())) {
      calc(3);
    }
  }

  void jtfPMAR_focusGained(FocusEvent e) {
    oldValue=jtfPMAR.getText();
  }
  void jtfPMAR_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfPMAR.getText())) {
      calc(0);
    }
  }

  void jtfIMAR_focusGained(FocusEvent e) {
    oldValue=jtfIMAR.getText();
  }
  void jtfIMAR_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfIMAR.getText())) {
      calc(4);
    }
  }

  void jtfVC_focusGained(FocusEvent e) {
    oldValue=jtfVC.getText();
  }
  void jtfVC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfVC.getText())) {
      calc(5);
    }

  }

  void jtfMC_focusGained(FocusEvent e) {
    oldValue=jtfMC.getText();
  }
  void jtfMC_focusLost(FocusEvent e) {
    if (!oldValue.equals(jtfMC.getText())) {
      calc(6);
    }
  }
  
  void jtfCART_focusLost(FocusEvent e) {
    if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").equals("CART")) {
      if (getRaQueryDataSet().getString("CART1").length() == 0)
        getRaQueryDataSet().setString("CART1", jtfCART.getText());
      if (getRaQueryDataSet().getString("BC").length() == 0)
        getRaQueryDataSet().setString("BC", jtfCART.getText());
    }
  }
  void jtfCART1_focusLost(FocusEvent e) {
    if (getRaQueryDataSet().getString("BC").length() == 0)
	  getRaQueryDataSet().setString("BC", jtfCART1.getText());
  }
  
  void jbCVRART_actionPerformed(ActionEvent e) {

  }
  void jrfNAZVRART_focusGained(FocusEvent e) {

  }
  void jrfNAZVRART_focusLost(FocusEvent e) {

  }
  void jbDODTXT_actionPerformed(){
    frmDodatniTxt dtx= new frmDodatniTxt(){
      public void stoakojesnimio(QueryDataSet vtt){
//        vtsnimio(vtt);
        qdsDodTxt = vtt;
//        raTransaction.saveChanges(vtt);
      }
      public void stoakonijesnimio(QueryDataSet qtt){
//        vtnijesnimio();
      }
    };

//    dtx.setUP((java.awt.Frame)fDI.getParent(),fDI.getDetailSet(),fDI.raDetail.getLocation());

    if (qdsDodTxt != null && qdsDodTxt.rowCount() > 0)
      dtx.setUP(getWindow(), getRaQueryDataSet(), getLocation(), qdsDodTxt);
    else dtx.setUP(getWindow(), getRaQueryDataSet(), getLocation());
  }
  
  void rnvSifArt_actionPerformed(ActionEvent e) {
    QueryDataSet qds = VTCartPart.getDataModule().getTempSet("CART ="+getRaQueryDataSet().getInt("CART"));
    FrmPartneriArtikli fPa = new FrmPartneriArtikli(this,qds,getRaQueryDataSet().getInt("CART"),getRaQueryDataSet().getString("CART1"),getRaQueryDataSet().getString("BC"),getRaQueryDataSet().getString("NAZART"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fPa,0,"Šifre partnera artikla " + getRaQueryDataSet().getString("NAZART"));
    fPa.show();
  }
  
  void  rnvNorm_actionPerformed(ActionEvent e) {
    DataSet ds = norme.getDataModule().getTempSet(
        Condition.equal("CART", getRaQueryDataSet()));
    ds.open();
    if (ds.rowCount() == 0) {
      JOptionPane.showMessageDialog(this.getWindow(),
          "Artikl nije sadržan niti u jednom normativu.",
          "Normativ", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    
    StorageDataSet reqs = stdoki.getDataModule().getScopedSet(
        "CART CART1 BC NAZART JM KOL");
    reqs.open();
    String[] cc = {"CART", "CART1", "BC", "NAZART", "JM"};
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      DataRow dr =  ld.raLookup(getRaQueryDataSet(), "CART",
          Integer.toString(ds.getInt("CARTNOR")));
      if (dr != null) {
        reqs.insertRow(false);
        dM.copyColumns(dr, reqs, cc);
        Aus.set(reqs, "KOL", ds);
      }
    }
    
    viewReq.setDataSet(reqs);
    viewReq.setSaveName("Pregled-art-norm");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Popis normativa koji sadrže artikl " + getRaQueryDataSet().getInt("CART"));
    viewReq.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(0, 1, 2), 3, 4, 5});
    viewReq.show();
  }
  
  void  rnvImgLoad_actionPerformed(ActionEvent e) {
	    ImageLoad imgload= new ImageLoad();
	    imgload.Img(this.getJframe(), "artikli", getRaQueryDataSet().getInt("cart")+"","Slika za artikl "+getRaQueryDataSet().getInt("CART")+" "
	        +getRaQueryDataSet().getString("NAZART"));
	   }

}
