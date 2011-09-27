/****license*****************************************************************
**   file: raAutomatRac.java
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
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.baza.stugovor;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.*;
import hr.restart.zapod.dlgGetKnjig;
import hr.restart.zapod.frmUgovori;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raAutomatRac extends raFrame {

    private ArrayList alSEQS = new ArrayList();    
    private static final long serialVersionUID = 1L;
    private static raAutomatRac rAR;
    private raCommonClass rcc = raCommonClass.getraCommonClass();
    private boolean escPresBefore;
    private Mnemonik mnem;
    public static raAutomatRac getraAutomatRac() {
        if (rAR == null)
            rAR = new raAutomatRac();
        return rAR;
    }
    private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
    private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    private raControlDocs rCD = new raControlDocs();
    private String greska = "";
    private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
    private Column cGODINA = new Column();
    private Column cMJESEC = new Column();
    private Column cDATUM = new Column();
    private Column cOPIS = new Column();
    private Column cDVO = new Column();
    private Column cDATDOSP = new Column();
    private Column cDANIDOSP = new Column();
    private Column cCNAP;
    private hr.restart.util.reports.raRunReport rr = hr.restart.util.reports.raRunReport.getRaRunReport();
    private QueryDataSet DummyArtiklSet = new QueryDataSet();
    private QueryDataSet DummySet = new QueryDataSet();
    private QueryDataSet zagRac;
    private QueryDataSet stavRac;
    private boolean forreturn = true;
    private hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();
    private QueryDataSet prenosPartner;
    
    {
        cCNAP = (Column) dm.getNapomene().getColumn("CNAP").clone();
        cGODINA.setColumnName("GODINA");
        cGODINA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
        cGODINA.setPrecision(8);
        cGODINA.setDisplayMask("yyyy");
        // cGODINA.setEditMask("yyyy");
        cMJESEC.setColumnName("MJESEC");
        cMJESEC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
        cMJESEC.setPrecision(8);
        cMJESEC.setDisplayMask("MM");
        // cMJESEC.setEditMask("MM");

        cDATUM = dM.createTimestampColumn("DATUM");
        cDVO = dM.createTimestampColumn("DVO"); 
        cDANIDOSP = dM.createShortColumn("DANIDOSP");
        cDATDOSP = dM.createTimestampColumn("DATDOSP");
        cOPIS = dM.createStringColumn("OPIS",200);
        
        DummySet.setColumns(new Column[] { cGODINA, cMJESEC, cDATUM, cDVO,
                cDATDOSP, cDANIDOSP, cOPIS, cCNAP });
        DummySet.open();
    }

//    private QueryDataSet allForIspis = new QueryDataSet();

//    private String[][] mnemonicTable = new String[2][2];
    private QueryDataSet Ugovors = null;
    private QueryDataSet STUgovors = null;

    private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel() {
        
        private static final long serialVersionUID = 1L;

        public void jBOK_actionPerformed() {
            presOK();
        }

        public void jPrekid_actionPerformed() {
            presCancel();
        }
    };

    private JPanel panel1 = new JPanel();

    private JPanel panelmali = new JPanel();

    private BorderLayout borderLayout2 = new BorderLayout();

    private XYLayout xYLayout1 = new XYLayout();

    private XYLayout XYLayoutmali = new XYLayout();
    private boolean datdvo = frmParam.getParam("robno", "autodvo","D","Odredjuje li se datum DVO pri autom. izradi racuna iz ugovora (D) ili dospijece (N)").equalsIgnoreCase("D");
    private String dattxt = datdvo?"Datum dok. / DVO":"Dat.dok / dosp";
    private String vrdok = "RAC";
    private JLabel ldatum = new JLabel(dattxt);
    private raComboBox rcbVRDOK = new raComboBox() {
      public void this_itemStateChanged() {
        // nothing
      }
    };
    private JLabel jlVRDOK = new JLabel();
    

    private JraTextField datum = new JraTextField() {
      public void valueChanged() {
        datum_focusLost(null);
      }
    };

    private JLabel ldvo = new JLabel("DVO");

    private JraTextField dvo = new JraTextField() {
      public void valueChanged() {
        dvo_focusLost(null);
      }
    };

//    private JLabel ldanidosp = new JLabel("Dani dospije\u0107a");

    private JraTextField danidosp = new JraTextField() {
      public void valueChanged() {
        danidosp_focusLost(null);
      }
    };

    private JLabel ldatdosp = new JLabel("Datum dospije\u0107a");

    private JraTextField datosp = new JraTextField() {
      public void valueChanged() {
        datosp_focusLost(null);
      }
    };

    private JLabel lvrugo = new JLabel("Vrsta ugovora");

    private JlrNavField jlrCVRUGO = new JlrNavField();

    private JlrNavField jlrOPIS = new JlrNavField();

    private JraButton jbGetVrugo = new JraButton();
    private JraButton jbGetDodNap = new JraButton();
    private JraButton jbMnemonik = new JraButton();
    private JLabel jlCORG = new JLabel();
    private JlrNavField jlrCORG = new JlrNavField() {
      public void after_lookUp() {
        escPresBefore = false;
      }
    };
    private JlrNavField jlrNAZIV = new JlrNavField();
    private JraButton jbGetCorg = new JraButton();
    private JlrNavField jlrCART = new JlrNavField();
    private JlrNavField jlrCART1 = new JlrNavField();
    private JlrNavField jlrBC = new JlrNavField();
    private JlrNavField jlrNAZART = new JlrNavField();
    private JlrNavField jlrJM = new JlrNavField();
    private JraButton jbGetCart = new JraButton();
    private JLabel jlZiro = new JLabel("Žiro ra\u010Dun");
    private JlrNavField jlrZiro = new JlrNavField();
    private JraButton jbGetZiro = new JraButton();
    private JLabel lgodina = new JLabel("Za godinu");
    private hr.restart.swing.JraRadioButton jbugovori = new hr.restart.swing.JraRadioButton("Ugovori");
    private JLabel jlNapomenaD = new JLabel("Dodatna napomena");
    private hr.restart.swing.JraTextArea jraNapomena = new hr.restart.swing.JraTextArea();
    private JLabel jlNapomena = new JLabel("Napomena ");
    private JlrNavField jlrCNAP = new JlrNavField();
    private JlrNavField jlrNAZNAP = new JlrNavField() {
      public void after_lookUp() {
        jlrNAZNAP.setCaretPosition(0);
      }
    };

    private JraButton jbGetNap = new JraButton();

    public raAutomatRac() {
        // public raAutomatRac(Frame frame, String title, boolean modal) {
        // super(frame, title, modal);
        //dm.getDoki().open();
        //dm.getStdoki().open();
        //dm.getSeq().open();
        //dm.getVTText().open();
        zagRac = doki.getDataModule().getTempSet("1=0");
        stavRac = stdoki.getDataModule().getTempSet("1=0");
        zagRac.open();
        stavRac.open();

        hr.restart.util.Valid.setApp(this.getClass());
        try {
            rAR = this;
            rr.clearAllReports();
            rr.addReport("hr.restart.robno.repRac",
                    "hr.restart.robno.repIzlazni", "Rac", "Ispis ra\u010Duna");

            /*rr.addReport("hr.restart.robno.repRacI",
                "hr.restart.robno.repIzlazni", "RacInvisible",
                "Raèun bez prikazanih popusta");*/
            
            rr.addReport("hr.restart.robno.repRacNp",
                    "hr.restart.robno.repIzlazni", "RacNoPopust",
                    "Ispis raèun bez prikazanih popusta");
            
            
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        mnem = new Mnemonik();
    }

    private void jbInit() throws Exception {
        
        
        jbMnemonik.setText("Mnemonici");
        jbMnemonik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                mnem.pack();
                mnem.setVisible(true);
            }
        });

        rr.setOwner(this, getClass().getName());
        hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
                new hr.restart.zapod.raKnjigChangeListener() {
                    public void knjigChanged(String oldKnjig, String newKnjig) {
 //                       jlrCORG.getRaDataSet().refresh();
                      jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
                          .getOrgstrAndCurrKnjig());
                        jlrNAZIV.setRaDataSet(jlrCORG.getRaDataSet());
                    }
                });

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                componentShow();
            }
        });
 /*       this.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F10) {
                    presOK();
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    presCancel();
                }
            }
        });
*/
        jbugovori.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                jbugovori_itemStateChanged(e);
            }
        });
        /*datum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                datum_focusLost(e);
            }
        });*/
        /*dvo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                dvo_focusLost(e);
            }
        });*/
        /*datosp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                datosp_focusLost(e);
            }
        });*/
        /*danidosp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                danidosp_focusLost(e);
            }
        });*/

        jlrCVRUGO.setPreferredSize(new Dimension(100, 21));
        jlrCVRUGO.setMinimumSize(jlrCVRUGO.getPreferredSize());
        jlrCVRUGO.setColumnName("CVRUGO"); // pretraga prema kljucu
        jlrCVRUGO.setColNames(new String[] { "OPIS" }); // kolone koje se
        jlrCVRUGO.setVisCols(new int[] { 0, 1 });// definiranje kolona za
        jlrCVRUGO.setTextFields(new javax.swing.text.JTextComponent[] { jlrOPIS }); // ubacivanje
        jlrCVRUGO.setRaDataSet(dm.getVrsteugo()); // trazenje po datasetu
        jlrCVRUGO.setNavButton(jbGetVrugo);
        jlrOPIS.setPreferredSize(new Dimension(250, 21));
        jlrOPIS.setMinimumSize(jlrCVRUGO.getPreferredSize());
        jlrOPIS.setColumnName("OPIS");
        jlrOPIS.setSearchMode(1);
        jlrOPIS.setNavProperties(jlrCVRUGO);
        jbGetVrugo.setText("...");
        jbGetVrugo.setPreferredSize(new Dimension(21, 21));
        jbGetVrugo.setMinimumSize(jlrCVRUGO.getPreferredSize());
        jbGetVrugo.setMaximumSize(jlrCVRUGO.getPreferredSize());

        jraNapomena.setColumnName("OPIS");
        jraNapomena.setBorder(datosp.getBorder());
        jraNapomena.setDataSet(DummySet);
        jraNapomena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                keyPresseda(e);
            }
        });

        jbGetZiro.setText("...");
        jlrZiro.setColumnName("ZIRO");
        jlrZiro.setVisCols(new int[] { 0, 1 });
        jlrZiro.setNavButton(jbGetZiro);

        jlCORG.setText("Org. jedinica");
        jlrCORG.setColumnName("CORG");
        jlrCORG.setColNames(new String[] { "NAZIV" });
        jlrCORG.setVisCols(new int[] { 0, 1, 2 });
        jlrCORG
                .setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZIV });
        jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
                .getOrgstrAndCurrKnjig());
        jlrNAZIV.setColumnName("NAZIV");
        jlrNAZIV.setSearchMode(1);
        jlrNAZIV.setNavProperties(jlrCORG);
        jbGetCorg.setText("...");
        jlrCORG.setNavButton(jbGetCorg);
        // odavde je cart ne zaboraviiii treba promijeniti

        jlrCART.setColumnName("CART");
        jlrCART.setColNames(new String[] { "CART1", "BC", "NAZART", "JM" });
        jlrCART.setVisCols(new int[] { 2, 3, 4, 5 });
        jlrCART.setTextFields(new javax.swing.text.JTextComponent[] { jlrCART1,
                jlrBC, jlrNAZART, jlrJM });
        // jlrCART.setRaDataSet(DummyArtiklSet);
        jlrCART1.setColumnName("CART1");
        jlrCART1.setSearchMode(1);
        // jlrCART1.setNavProperties(jlrCART);
        jlrBC.setColumnName("BC");
        jlrBC.setSearchMode(3);
        // jlrBC.setNavProperties(jlrCART);

        jlrNAZART.setColumnName("NAZART");
        jlrNAZART.setSearchMode(1);
        jlrJM.setColumnName("JM");
        jlrJM.setSearchMode(-1);
        // jlrNAZART.setNavProperties(jlrCART);
        jbGetCart.setText("...");
        jlrCART.setNavButton(jbGetCart);
        // ///

        panel1.setBorder(BorderFactory.createEtchedBorder());
        this.getContentPane().setLayout(borderLayout2);

        datum.setDataSet(DummySet);
        datum.setColumnName("DATUM");
        datum.setHorizontalAlignment(SwingUtilities.CENTER);

        dvo.setDataSet(DummySet);
        dvo.setColumnName("DVO");
        dvo.setHorizontalAlignment(SwingUtilities.CENTER);

        danidosp.setDataSet(DummySet);
        danidosp.setColumnName("DANIDOSP");
        danidosp.setHorizontalAlignment(SwingUtilities.CENTER);

        datosp.setDataSet(DummySet);
        datosp.setColumnName("DATDOSP");
        datosp.setHorizontalAlignment(SwingUtilities.CENTER);

        jlrCNAP.setColumnName("CNAP");
        jlrCNAP.setDataSet(DummySet);
        jlrCNAP.setColNames(new String[] { "NAZNAP" });
        jlrCNAP.setVisCols(new int[] { 0, 1 });
        jlrCNAP
                .setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAP });
        jlrCNAP.setRaDataSet(dm.getNapomene());
        jlrCNAP.setNavButton(jbGetNap);
        jlrNAZNAP.setColumnName("NAZNAP");
        jlrNAZNAP.setNavProperties(jlrCNAP);
        jbGetNap.setText("...");

        lgodina.setPreferredSize(new Dimension(100, 21));
        lgodina.setMinimumSize(lgodina.getPreferredSize());
        lvrugo.setPreferredSize(new Dimension(100, 21));
        lvrugo.setMinimumSize(lvrugo.getPreferredSize());
        getContentPane().add(panel1, BorderLayout.CENTER);
        getContentPane().add(okp, BorderLayout.SOUTH);
        panel1.setLayout(xYLayout1);
        xYLayout1.setHeight(235/*250*/);
        xYLayout1.setWidth(570);

        panelmali.setLayout(XYLayoutmali);
        panelmali.setBorder(BorderFactory.createEtchedBorder());
        XYLayoutmali.setHeight(42);
        XYLayoutmali.setWidth(270);
        
        jlVRDOK.setText("Vrsta dokumenta");
        rcbVRDOK.setRaItems(new String[][] {
            {"Raèun", "RAC"},
            {"Ponuda", "PON"},
            {"Predraèun", "PRD"}
          });
        rcbVRDOK.setSelectedIndex(0);

        panel1.add(jlCORG, new XYConstraints(15, 10, -1, -1));
        panel1.add(jlrCORG, new XYConstraints(150, 10, 100, -1));
        panel1.add(jlrNAZIV, new XYConstraints(255, 10, 275, -1));
        panel1.add(jbGetCorg, new XYConstraints(539, 10, 21, 21));

        panel1.add(ldatum, new XYConstraints(15, 35, -1, -1));
        panel1.add(datum, new XYConstraints(150, 35, 100, -1));
//        panel1.add(ldvo, new XYConstraints(255, 35, -1, -1));
        if (datdvo) {
          panel1.add(dvo, new XYConstraints(255, 35, 100, -1));
        } else {
          panel1.add(datosp, new XYConstraints(255, 35, 100, -1));
        }
//        panel1.add(ldanidosp, new XYConstraints(405, 35, -1, -1));
//        panel1.add(danidosp, new XYConstraints(500, 35, 30, -1));
//        panel1.add(ldatdosp, new XYConstraints(15, 60, -1, -1));
        panel1.add(jbMnemonik, new XYConstraints(360, 35, 170, 21));   //252, 60, 21, 21));
//         panel1.add(lmjesec, new XYConstraints(15, 85, -1, -1) );
//         panel1.add(mjesec, new XYConstraints(150, 85, 35, -1) );
//         panel1.add(godina, new XYConstraints(190, 85, 60, -1) );
        
        
        panel1.add(lvrugo, new XYConstraints(15, 70/*110*/, -1, -1));
        panel1.add(jlrCVRUGO, new XYConstraints(150, 70/*110*/, 100, -1));
        panel1.add(jlrOPIS, new XYConstraints(255, 70/*110*/, 275, -1));
        panel1.add(jbGetVrugo, new XYConstraints(539, 70/*110*/, 21, 21));
        panel1.add(jlZiro, new XYConstraints(15, 95/*135*/, -1, -1));
        panel1.add(jbGetZiro, new XYConstraints(539, 95/*135*/, 21, 21));
        panel1.add(jlrZiro, new XYConstraints(150, 95/*135*/, 380, -1));
        panel1.add(jlNapomena, new XYConstraints(15, 120/*160*/, -1, -1));
        panel1.add(jlrCNAP, new XYConstraints(150, 120/*160*/, 100, -1));
        panel1.add(jlrNAZNAP, new XYConstraints(255, 120/*160*/, 275, -1));
        panel1.add(jbGetNap, new XYConstraints(539, 120/*160*/, 21, 21));
        panel1.add(jlNapomenaD, new XYConstraints(15, 145/*185*/, -1, -1));
        panel1.add(jraNapomena, new XYConstraints(150, 145/*185*/, 381, 60));
        panel1.add(jlVRDOK, new XYConstraints(15, 210, -1, -1));
        panel1.add(rcbVRDOK, new XYConstraints(150, 210, 100, -1));
        okp.registerOKPanelKeys(this);
    }

    public void keyPresseda(java.awt.event.KeyEvent e) {
        if (e.isConsumed())
            return;
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F9) {
            int[] viscols = { 0, 1 };
            // String[] result =
            // lD.lookUp(this.getOwner(),dm.getNapomene(),viscols);
            String[] result = lD.lookUp(this.getWindow(), dm.getNapomene(),
                    viscols);
            if (result != null) {
                jraNapomena.setText(result[3]);
            }
            e.consume();
        }
    }

    public boolean findActiveUgovor() {

        String sql = "SELECT ugovori.* FROM Ugovori,Partneri WHERE ugovori.cpar = partneri.cpar and "
                + "ugovori.aktiv ='D' and ugovori.corg = '"
                + jlrCORG.getText()
                + "' AND "+frmUgovori.getCorgCondition("ugovori");

        if (!jlrCVRUGO.getText().equals(""))
            sql = sql + " and ugovori.CVRUGO ='" + jlrCVRUGO.getText() + "'";
        sql = sql + " order by partneri.nazpar";
        Ugovors = hr.restart.util.Util.getNewQueryDataSet(sql, true);
        
        return Ugovors.getRowCount()>0;
    }

    public void componentShow() {
        // inHouse = true;
        mnem.setLocation(jlCORG.getLocationOnScreen().x-10,jlCORG.getLocationOnScreen().y-10);
        mnem.defaultValues(val.getToday());
        
        escPresBefore = false;
        hr.restart.zapod.OrgStr.getOrgStr().getKnjigziro(
                hr.restart.zapod.OrgStr.getKNJCORG());
        hr.restart.zapod.OrgStr.getOrgStr().getCurrentKnjigziro().open();

        DummyArtiklSet = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM Artikli,Porezi WHERE " +
            "artikli.cpor = porezi.cpor and " + raVart.getUslugaCond(), true);

        jlrCART.setText("");
        jlrCART.emptyTextFields();
        jlrCART.setRaDataSet(DummyArtiklSet);
        jlrCART1.setNavProperties(jlrCART);
        jlrBC.setNavProperties(jlrCART);
        jlrNAZART.setNavProperties(jlrCART);

        jlrZiro.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
                .getCurrentKnjigziro());

        hr.restart.util.lookupData.getlookupData()
                .raLocate(
                        hr.restart.zapod.OrgStr.getOrgStr()
                                .getCurrentKnjigziro(),
                        new String[] { "CORG" },
                        new String[] { hr.restart.zapod.OrgStr.getKNJCORG() });
//        jlrZiro.setText(hr.restart.zapod.OrgStr.getOrgStr()
//                .getCurrentKnjigziro().getString("ZIRO"));
         jlrZiro.setText("");
        if (DummyArtiklSet.getRowCount() == 1)
            jlrCART.keyF9Pressed();

        DummySet.reset();
        DummySet.setTimestamp("GODINA", val.getToday());
        DummySet.setTimestamp("MJESEC", val.getToday());
        DummySet.setTimestamp("DATUM", val.getToday());
//        DummySet.setShort("DANIDOSP", (short) 7);
        datum_focusLost(null);

        // jlrCORG.setText("");
        // jlrCORG.emptyTextFields();
        jlrCORG.setText(hr.restart.zapod.OrgStr.getKNJCORG());
        jlrCORG.forceFocLost();
        // jlrCORG.requestFocus();
        jlrCVRUGO.requestFocusLater();
        jbugovori.setSelected(true);
    }

    public java.sql.Timestamp getDatumMjesecManji(java.sql.Timestamp orgTime) {
        return new java.sql.Timestamp(raDateUtil.getraDateUtil().addMonth(
                new java.util.Date(orgTime.getTime()), -1).getTime());
    }

    public boolean Validacija() {

        if (val.isEmpty(jlrCORG)) {
            return false;
        }
        if (val.isEmpty(datum)) {
            return false;
        }
        if (!findActiveUgovor()){
            javax.swing.JOptionPane.showConfirmDialog(this, 
                    "Nema aktivnih ugovora za izradu raèuna !", "Obavijest !",
                   javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.DEFAULT_OPTION);
            return false;
        }
        StorageDataSet greska = ValidacijaStanje();
        if (greska.getRowCount() != 0) {
            ST.showInFrame(greska, "Greške pri izradi raèuna");
            return false;
        }
        
        return provjeraNulaIznosa();
    }
    
    public boolean provjeraNulaIznosa(){
        String upit = "SELECT STUGOVOR.CUGOVOR,STUGOVOR.RBR,STUGOVOR.NAZART,STUGOVOR.JM FROM STUGOVOR WHERE IPRODSP=0"
                + " and " + frmUgovori.getCorgCondition("STUGOVOR")
                + " and exists ("+
        "SELECT * FROM Ugovori WHERE ugovori.cugovor = stugovor.cugovor and "
        + "ugovori.aktiv ='D' and ugovori.corg = '"+ jlrCORG.getText()
        + "' and "+frmUgovori.getCorgCondition("ugovori");
        if (!jlrCVRUGO.getText().equals("")) {
            upit = upit + " and ugovori.CVRUGO ='" + jlrCVRUGO.getText() + "'";
        }
        upit = upit+")";

        System.out.println(upit);
        
        QueryDataSet qdsaa = hr.restart.util.Util.getNewQueryDataSet(upit);

        if (qdsaa.getRowCount()>0) {
            int i = kveshcn("Postoje ugovori koji imaju na \nsebi stavke s iznosima od 0 kuna.\n Želite li nastaviti ?");
            if (i==0){
                return true;
            } else {
                int j = kveshcn("Želite li popis stavaka takvih ugovora  ?");
                if (j==0) {
                    ST.showInFrame(qdsaa,"Stavke ugovora s iznosima 0");
                }
                
                return false;
            }
        }
        return true;
    }

    public StorageDataSet getEmptySumaUgovoraKol() {
        StorageDataSet sumastugovora = new StorageDataSet();
        sumastugovora.setColumns(new Column[] {
                dm.getStanje().getColumn("CSKL").cloneColumn(),
                dm.getStanje().getColumn("GOD").cloneColumn(),
                dm.getStanje().getColumn("CART").cloneColumn(),
                dm.getStanje().getColumn("KOL").cloneColumn() });
        sumastugovora.open();
        return sumastugovora;
    }

    public StorageDataSet getEmptyGreska() {
        StorageDataSet greska = new StorageDataSet();
        Column kola = dm.getStanje().getColumn("KOL").cloneColumn();
        kola.setColumnName("KOLZAH");
        kola.setCaption("Potrebna kolièina");
        greska.setColumns(new Column[] {
                dm.getDoki().getColumn("OPIS").cloneColumn(),
                dm.getStanje().getColumn("KOL").cloneColumn(), kola });

        greska.open();
        return greska;
    }

    public StorageDataSet ValidacijaStanje() {
        StorageDataSet greska = getEmptyGreska();
        StorageDataSet sumastugovora = getEmptySumaUgovoraKol();
        for (Ugovors.first(); Ugovors.inBounds(); Ugovors.next()) {
            STUgovors = stugovor.getDataModule().getTempSet(
                Condition.whereAllEqual(new String[] {"CUGOVOR","KNJIG"}, Ugovors)); 
            STUgovors.open();
//              hr.restart.util.Util
//                    .getNewQueryDataSet("SELECT * from STUGOVOR where CUGOVOR='"
//                            + Ugovors.getString("CUGOVOR") + "' AND "+frmUgovori.getCorgCondition("STUGOVOR"));
            if (STUgovors.getRowCount() == 0) {
                continue;
            }
            for (STUgovors.first(); STUgovors.inBounds(); STUgovors.next()) {
//                if (lD
//                        .raLocate(dm.getArtikli(), new String[] { "CART" },
//                                new String[] { String.valueOf(STUgovors
//                                        .getInt("CART")) })) {
//                    if (dm.getArtikli().getString("VRART")
//                            .equalsIgnoreCase("U")
//                            || dm.getArtikli().getString("VRART")
//                                    .equalsIgnoreCase("T")) {
//                        // preskoci uslugu ili tranzit
//                        continue;
//                    }
//                } else {
//                    continue;
//                }
              // preskoci uslugu ili tranzit
                if (!raVart.isStanje(STUgovors.getInt("CART"))) {
                    //Aut.getAut().artTipa(STUgovors.getInt("CART"), "UT")) {
                  continue;
                }
                if (lD.raLocate(sumastugovora, new String[] { "CSKL", "GOD",
                        "CART" }, new String[] {
                        STUgovors.getString("CSKLART"),
                        val.findYear(DummySet.getTimestamp("DATUM")),
                        String.valueOf(STUgovors.getInt("CART")) })) {

                    sumastugovora.setBigDecimal("KOL", sumastugovora
                            .getBigDecimal("KOL").add(
                                    STUgovors.getBigDecimal("KOL")));
                } else {
                    sumastugovora.insertRow(false);
                    sumastugovora.setString("CSKL", STUgovors
                            .getString("CSKLART"));
                    sumastugovora.setString("GOD", val.findYear(DummySet
                            .getTimestamp("DATUM")));
                    sumastugovora.setInt("CART", STUgovors.getInt("CART"));
                    sumastugovora.setBigDecimal("KOL", STUgovors
                            .getBigDecimal("KOL"));
                }
            }
        }
        for (sumastugovora.first(); sumastugovora.inBounds(); sumastugovora
                .next()) {
            if (!lD.raLocate(dm.getArtikli(), new String[] { "CART" },
                    new String[] { String.valueOf(STUgovors.getInt("CART")) })) {
                greska.insertRow(false);
                greska.setBigDecimal("KOL", Aus.zero3);
                greska.setBigDecimal("KOLZAH", Aus.zero3);
                greska.setString("OPIS", "Ne postoji artikl sa šifrom "
                        + sumastugovora.getInt("CART"));
                continue;
            }

            if (lD.raLocate(dm.getStanje(), new String[] { "CSKL", "GOD",
                    "CART" }, new String[] { sumastugovora.getString("CSKL"),
                    sumastugovora.getString("GOD"),
                    String.valueOf(STUgovors.getInt("CART")) })) {
                if (dm.getStanje().getBigDecimal("KOL").compareTo(
                        sumastugovora.getBigDecimal("KOL")) < 0) {
                    greska.insertRow(false);
                    greska.setBigDecimal("KOL", dm.getStanje().getBigDecimal(
                            "KOL"));
                    greska.setBigDecimal("KOLZAH", sumastugovora
                            .getBigDecimal("KOL"));
                    greska.setString("OPIS", "Nedovoljna zaliha za artikl "
                            + dm.getArtikli().getInt("CART") + " - "
                            + dm.getArtikli().getString("NAZART")
                            + " na skladištu "
                            + sumastugovora.getString("CSKL"));
                }
            } else {

                greska.insertRow(false);
                greska.setBigDecimal("KOL", Aus.zero3);
                greska.setBigDecimal("KOLZAH", sumastugovora
                        .getBigDecimal("KOL"));
                greska.setString("OPIS", "Za artikl "
                        + dm.getArtikli().getInt("CART") + " - "
                        + dm.getArtikli().getString("NAZART")
                        + " ne postoji slog stanja");
            }

        }
        return greska;
    }

    public boolean validUgovor() {
        return true;
    }
   
    public boolean transakcija() {
        return (new raLocalTransaction() {
            public boolean transaction() throws Exception {
                try {
                    maintanceUgovori();
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                } finally {
                    val.unlockCurrentSeq(false);
                    startFrame.getStartFrame().getStatusBar().finnishTask();
                }
            }
        }.execTransaction());
    }

    boolean isPartneriSelected = false;

    boolean isUgovoriSelected = false;

    boolean isOK = true;

    public void presOK() {
        isOK = true;
        vrdok = rcbVRDOK.getDataValue();
        System.out.println(vrdok);
        if (Validacija()) {
            rcc.EnabDisabAll(this, false);

            raProcess.runChild(new Runnable() {
                public void run() {
                    alSEQS.clear();
                    raProcess.setMessage("Formiranje dokumenata u tijeku ...!",
                            true);
                    zagRac.refresh();
                    stavRac.refresh();
                    isOK = transakcija();
                }
            });
            if (!isOK) {
                Greska();
            } else {
                
                javax.swing.JOptionPane.showConfirmDialog(this, 
                         "Raèuni od "+((int[])alSEQS.get(0))[0]+" do "+
                         ((int[])alSEQS.get(alSEQS.size()-1))[0]+" "+                                
                         "su uspješno napravljeni. ", "Obavijest !",
                        javax.swing.JOptionPane.DEFAULT_OPTION,
                         javax.swing.JOptionPane.DEFAULT_OPTION);
                
                maintenceIspis();
                
                if (kveshcn("Želite li ispis iskreiranih raèuna ?") == javax.swing.JOptionPane.YES_OPTION) {
                    ispis();
                }
                
                if (!hr.restart.sisfun.frmParam.getParam("robno", "ponisUgovor",
        				"N", "Poništavanje iznosa nakon ugovora").equalsIgnoreCase("N")) {
                	ponistenjeIznosaGui();
        		}
                
                SwingUtilities.invokeLater(new Runnable(){

                    public void run() {
                        resetInitMeth();
                    }
                });
            }
        }
    }

    public void resetInitMeth() {
        rcc.EnabDisabAll(this, true);
        clearAll(true);//ai:2007-11-08 bilo false, ali Mladen je trazio da se sve ocisti
    }

    public void presCancel() {
        if (escPresBefore) {
            // this.dispose();
            this.hide();
        } else {
            escPresBefore = true;
            clearAll(true);
        }
    }

    public String changeMnemonics(String text) {
      String[][] mnemonicTable = mnem.mnemonicArray();
        StringBuffer buffy = new StringBuffer(text);
        int offset;
        for (int i = 0; i < mnemonicTable.length; i++) {
            offset = 0;
            while ((offset = text.indexOf(mnemonicTable[i][0])) != -1) {
                buffy.replace(offset, offset + mnemonicTable[i][0].length(),
                        mnemonicTable[i][1]);
                text = buffy.toString();
            }
        }
        return buffy.toString();
    }
    
    public void clearAll(boolean all) {
        jbugovori.setSelected(true);
        if (all) {
            jlrCART.setText("");
            jlrCART.emptyTextFields();
            DummySet.reset();
            DummySet.setTimestamp("GODINA", val.getToday());
            DummySet.setTimestamp("MJESEC", val.getToday());
            DummySet.setTimestamp("DATUM", val.getToday());
            DummySet.setString("OPIS", "");
            jlrCORG.setText("");
            jlrCORG.emptyTextFields();
            jraNapomena.setText("");
//            rsP.ocisti();
            jlrCNAP.setText("");
            jlrCNAP.emptyTextFields();
           
            jlrCVRUGO.setText("");
            jlrCVRUGO.emptyTextFields();
            
//            if (jlrZiro.getRaDataSet().getRowCount() > 1) 
              jlrZiro.setText("");
            escPresBefore = true;

            jlrCORG.requestFocusLater();
        } else {
            jlrCVRUGO.setText("");
            jlrCVRUGO.emptyTextFields();
            jlrCVRUGO.requestFocus();
//            rsP.ocisti();
        }
    }

    public void Greska() {
        javax.swing.JOptionPane.showConfirmDialog(this,
                "Generiranje ra\u010Duna uspješno obavljeno !", "Poruka",
                javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.DEFAULT_OPTION);
    }

    public int kveshcn(String str) {
        return javax.swing.JOptionPane.showConfirmDialog(this, str, "Upit",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.DEFAULT_OPTION);
    }

    public void ispis() {
        rr.go();
    }

    public void commonTask4prepareQuery(QueryDataSet zaglav, QueryDataSet stavke) {
    }

    public void prepareQuery(String dodatak) {
        //dm.getVTText().open();
        String sqlzaglav = "select * from doki WHERE vrdok ='"
                + dm.getZagRac().getString("VRDOK") + "' " + "and cskl='"
                + dm.getZagRac().getString("CSKL") + "' " + "and god='"
                + dm.getZagRac().getString("GOD") + "' " + dodatak;
        String sqlstavke = "select * from stdoki WHERE vrdok ='"
                + dm.getZagRac().getString("VRDOK") + "' " + "and cskl='"
                + dm.getZagRac().getString("CSKL") + "' " + "and god='"
                + dm.getZagRac().getString("GOD") + "' " + dodatak;
        QueryDataSet zaglav = hr.restart.util.Util.getNewQueryDataSet(
                sqlzaglav, true);
        QueryDataSet stavke;
        for (zaglav.first(); zaglav.inBounds(); zaglav.next()) {
            sqlstavke = "select * from stdoki WHERE vrdok ='"
                    + dm.getZagRac().getString("VRDOK") + "' " + "and cskl='"
                    + dm.getZagRac().getString("CSKL") + "' " + "and god='"
                    + dm.getZagRac().getString("GOD") + "' " + "and brdok="
                    + zaglav.getInt("BRDOK");
            stavke = hr.restart.util.Util.getNewQueryDataSet(sqlstavke, true);
            for (stavke.first(); stavke.inBounds(); stavke.next()) {

                commonTask4prepareQuery(zaglav, stavke);

            }
        }
    }

    public void prepareQuery() {

        System.err.println("NE SMIJEM BITI TU");

        //dm.getVTText().open();
        QueryDataSet zaglav = hr.restart.util.Util.getNewQueryDataSet(
                "select * from doki WHERE vrdok ='"
                        + dm.getZagRac().getString("VRDOK") + "' "
                        + "and cskl='" + dm.getZagRac().getString("CSKL")
                        + "' " + "and god='" + dm.getZagRac().getString("GOD")
                        + "' " + "and brdok=" + dm.getZagRac().getInt("BRDOK"),
                true);
        QueryDataSet stavke = hr.restart.util.Util.getNewQueryDataSet(
                "select * from stdoki WHERE vrdok ='"
                        + dm.getZagRac().getString("VRDOK") + "' "
                        + "and cskl='" + dm.getZagRac().getString("CSKL")
                        + "' " + "and god='" + dm.getZagRac().getString("GOD")
                        + "' " + "and brdok=" + dm.getZagRac().getInt("BRDOK"),
                true);

        commonTask4prepareQuery(zaglav, stavke);

    }

    public QueryDataSet getQuery() {
        return null;
    }

    void jbugovori_itemStateChanged(ItemEvent e) {
        // System.out.println("jbugovori.isSelected() "+jbugovori.isSelected());
    }
    
    void datum_focusLost(FocusEvent e) {
        DummySet.setTimestamp("DVO", DummySet.getTimestamp("DATUM"));
        dvo_focusLost(null);
    }

    void dvo_focusLost(FocusEvent e) {
        java.util.Date Datum = new java.util.Date(DummySet.getTimestamp("DVO")
                .getTime());
        DummySet.setTimestamp("DATDOSP", new java.sql.Timestamp(raDateUtil
                .getraDateUtil().addDate(Datum,
                        (int) DummySet.getShort("DANIDOSP")).getTime()));
    }

    void datosp_focusLost(FocusEvent e) {
        DummySet.setShort("DANIDOSP", (short) raDateUtil.getraDateUtil()
                .DateDifference(
                        new java.util.Date(DummySet.getTimestamp("DVO")
                                .getTime()),
                        new java.util.Date(DummySet.getTimestamp("DATDOSP")
                                .getTime())));
    }

    void danidosp_focusLost(FocusEvent e) {
        dvo_focusLost(null);
    }
    private String getZiro(ReadRow zag) {
      String p1 = jlrZiro.getText().trim();
      String p2 = zag.getString("ZIRO").trim();
      return (
          p1.length()>0?p1:
            p2.length()>0?p2:
              izmisliZiro());
    }
    private String izmisZR = null;
    private String izmisliZiro() {
      if (izmisZR != null) return izmisZR;
      QueryDataSet tajoj = Orgstruktura.getDataModule().getFilteredDataSet(Condition.equal("CORG", dlgGetKnjig.getKNJCORG()));
      tajoj.open();
      return (izmisZR = tajoj.getString("ZIRO"));
    }
    public void addDoki(QueryDataSet zagugovora) {
        zagRac.insertRow(false);
        zagRac.setString("CUSER",
                hr.restart.sisfun.raUser.getInstance().getUser());
        zagRac.setString("CSKL", jlrCORG.getText());
        zagRac.setString("VRDOK", vrdok);
        zagRac.setString("GOD",
                val.findYear(DummySet.getTimestamp("DATUM")));
        // zagRac.setInt("BRDOK", brrac);
        zagRac.setString("CNACPL", zagugovora.getString("CNACPL"));
        zagRac.setString("CFRA", zagugovora.getString("CFRA"));
        zagRac.setString("CNAC", zagugovora.getString("CNAC"));
        zagRac.setString("CNAMJ", zagugovora.getString("CNAMJ"));
        zagRac.setString("CNAP", DummySet.getString("CNAP"));
        if (vrdok.equals("PON"))
          zagRac.setString("PARAM", "OJ");
        else zagRac.setString("PARAM", "_A_");
        zagRac.setInt("CPAR", zagugovora.getInt("CPAR"));
        zagRac.setInt("PJ", zagugovora.getInt("PJ"));
        zagRac.setTimestamp("DATDOK", DummySet.getTimestamp("DATUM"));

        zagRac.setString("CUG", zagugovora.getString("CUGOVOR"));
        zagRac
                .setTimestamp("DATUG", zagugovora.getTimestamp("DATUGOVOR"));
        
        if (datdvo) {
          //TODO ovdje rjesavat problem ¡
          zagRac.setShort("DDOSP", zagugovora.getShort("DANIDOSP"));
          //TODO ovdje rjesavat problem ^
          //>>>>>>>>ovo ovako za sada ¡
          zagRac.setTimestamp("DATDOSP", new java.sql.Timestamp(raDateUtil
              .getraDateUtil().addDate(DummySet.getTimestamp("DVO"),
                  (int) zagugovora.getShort("DANIDOSP")).getTime())); 
          //>>>>>>>>ovo ovako za sada ^
        } else {
          zagRac.setShort("DDOSP", DummySet.getShort("DANIDOSP"));
          zagRac.setTimestamp("DATDOSP", DummySet.getTimestamp("DATDOSP"));
        }
        zagRac.setTimestamp("DVO", DummySet.getTimestamp("DVO"));
        
//        zagRac.setTimestamp("DATDOSP", DummySet.getTimestamp("DATDOSP"));

        
        Util.getUtil().getBrojDokumenta(zagRac);
        alSEQS.add(new int[]{zagRac.getInt("BRDOK")});
        zagRac.setString("OPIS", changeMnemonics(jraNapomena.getText()));
        zagRac.setString("ZIRO", getZiro(zagugovora));
        zagRac.setString(
                "PNBZ2",
                raPozivNaBroj.getraPozivNaBrojClass().getPozivNaBroj(
                        zagRac));
    }

    public void addStdoki(QueryDataSet zagugovora, QueryDataSet stugovora,
            int rbr) {
        
            if (!lD.raLocate(DummyArtiklSet, new String[] { "CART" },
                    new String[] { String.valueOf(
                    stugovora.getInt("CART"))})) {
                System.out.print("Nema seta za porez "+stugovora.getInt("CART")) ;
            }
        
        stavRac.insertRow(false);
        stavRac.setString("CSKL", zagRac.getString("CSKL"));
        stavRac.setString("VRDOK", vrdok);
        stavRac.setString("GOD", zagRac.getString("GOD"));
        stavRac.setInt("BRDOK", zagRac.getInt("BRDOK"));
        stavRac.setShort("RBR", (short) rbr);
        stavRac.setInt("RBSID", rbr);
        stavRac.setInt("CART", stugovora.getInt("CART"));
        stavRac.setString("CART1", stugovora.getString("CART1"));
        stavRac.setString("BC", stugovora.getString("BC"));
        stavRac.setString("NAZART",
                changeMnemonics(stugovora.getString("NAZART")));
        stavRac.setString("JM", stugovora.getString("JM"));
        stavRac.setBigDecimal("KOL", stugovora.getBigDecimal("KOL"));
        stavRac.setString("CSKLART", stugovora.getString("CSKLART"));
        stavRac.setString(
                "ID_STAVKA",
                raControlDocs.getKey(stavRac, new String[] { "cskl",
                        "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        
        if (stugovora.getBigDecimal("IMINIZNOSBP").compareTo(stugovora.getBigDecimal("IPRODBP"))==1){
            stavRac.setBigDecimal("UPRAB", Aus.zero2);
            stavRac.setBigDecimal("UIRAB", Aus.zero2);
            stavRac.setBigDecimal("UPZT", Aus.zero2);
            stavRac.setBigDecimal("UIZT", Aus.zero2);
            try {
            stavRac.setBigDecimal("FC", stugovora.getBigDecimal("IMINIZNOSBP").
                    divide(stugovora.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
            } catch (Exception ex){
                stavRac.setBigDecimal("FC", Aus.zero2);
            }
            stavRac.setBigDecimal("INETO",stugovora.getBigDecimal("IMINIZNOSBP"));

            stavRac.setBigDecimal("FVC", stavRac.getBigDecimal("FC"));
            stavRac.setBigDecimal("IPRODBP",stugovora.getBigDecimal("IMINIZNOSBP"));
            stavRac.setBigDecimal("PPOR1",DummyArtiklSet.getBigDecimal("POR1"));
            stavRac.setBigDecimal("PPOR2",DummyArtiklSet.getBigDecimal("POR2"));
            stavRac.setBigDecimal("PPOR3",DummyArtiklSet.getBigDecimal("POR3"));
            stavRac.setBigDecimal("POR1", 
                    stugovora.getBigDecimal("IMINIZNOSBP").multiply(DummyArtiklSet.getBigDecimal("POR1")).
                    divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));
            stavRac.setBigDecimal("POR2", 
                    stugovora.getBigDecimal("IMINIZNOSBP").multiply(DummyArtiklSet.getBigDecimal("POR2")).
                    divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));
            stavRac.setBigDecimal("POR3",
                    stugovora.getBigDecimal("IMINIZNOSBP").multiply(DummyArtiklSet.getBigDecimal("POR3")).
                    divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));

            stavRac.setBigDecimal("IPRODSP",stugovora.getBigDecimal("IMINIZNOSBP").
                                                   add(stavRac.getBigDecimal("POR1")).
                                                   add(stavRac.getBigDecimal("POR2")).
                                                   add(stavRac.getBigDecimal("POR3")));
     
            try {
                stavRac.setBigDecimal("FMC", stugovora.getBigDecimal("IPRODSP").
                    divide(stugovora.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
            } catch(Exception ex){
                stavRac.setBigDecimal("FMC", Aus.zero2);
            }

            stavRac.setBigDecimal("UPPOR",
                      stavRac.getBigDecimal("PPOR1").
                      add(stavRac.getBigDecimal("PPOR2")).
                      add(stavRac.getBigDecimal("PPOR3")));
            stavRac.setBigDecimal("UIPOR",
                    stavRac.getBigDecimal("POR1").
                    add(stavRac.getBigDecimal("POR2")).
                    add(stavRac.getBigDecimal("POR3")));
            zagRac.setBigDecimal("UIRAC",
                    zagRac.getBigDecimal("UIRAC").add(
                    		stavRac.getBigDecimal("IPRODSP")));
        } else {
        
        
        stavRac.setBigDecimal("UPRAB", stugovora.getBigDecimal("UPRAB"));
        stavRac.setBigDecimal("UIRAB", stugovora.getBigDecimal("UIRAB"));
        stavRac.setBigDecimal("UPZT", Aus.zero2);
        stavRac.setBigDecimal("UIZT", Aus.zero2);
        stavRac.setBigDecimal("FC", stugovora.getBigDecimal("FC"));
        stavRac.setBigDecimal(
                "INETO",
                stugovora.getBigDecimal("KOL").multiply(
                        stugovora.getBigDecimal("FC")));
        // izrachun
        stavRac.setBigDecimal("FVC", stugovora.getBigDecimal("FVC"));
        stavRac.setBigDecimal("IPRODBP",stugovora.getBigDecimal("IPRODBP"));

        stavRac.setBigDecimal("POR1", stugovora.getBigDecimal("POR1"));
        stavRac.setBigDecimal("POR2", stugovora.getBigDecimal("POR2"));
        stavRac.setBigDecimal("POR3", stugovora.getBigDecimal("POR3"));
        stavRac.setBigDecimal("PPOR1",DummyArtiklSet.getBigDecimal("POR1"));
        stavRac.setBigDecimal("PPOR2",DummyArtiklSet.getBigDecimal("POR2"));
        stavRac.setBigDecimal("PPOR3",DummyArtiklSet.getBigDecimal("POR3"));
 
        stavRac.setBigDecimal("FMC", stugovora.getBigDecimal("FMC"));
        stavRac.setBigDecimal("IPRODSP",stugovora.getBigDecimal("IPRODSP"));
        stavRac.setBigDecimal("UPPOR", stugovora.getBigDecimal("UPPOR"));
        stavRac.setBigDecimal(
                "UIPOR",
                stugovora.getBigDecimal("IPRODSP").subtract(
                        stugovora.getBigDecimal("IPRODBP")));
        zagRac.setBigDecimal(
                "UIRAC",
                zagRac.getBigDecimal("UIRAC").add(
                		stavRac.getBigDecimal("IPRODSP")));
        }
/*
        dm.getVTText().insertRow(true);
        dm.getVTText().setString("CKEY", raControlDocs.getKey(stavRac));
        dm.getVTText().setString("TEXTFAK",
                changeMnemonics(Ugovors.getString("TEXTFAK")));
*/                

    }

    public void maintenceIspis() {
        reportsQuerysCollector.getRQCModule().ReSql("AND doki.god='"+
                Valid.getValid().findYear(datum.getDataSet().getTimestamp("DATUM"))+ "' AND doki.cskl='"+
                jlrCORG.getText().trim()+"' and doki.brdok <="+
                ((int[])alSEQS.get(alSEQS.size()-1))[0]+
                " and doki.brdok>="+((int[])alSEQS.get(0))[0],vrdok);        
    }

    public void maintanceUgovori() {


        String dodatak = "";
        if (!jlrCVRUGO.getText().equalsIgnoreCase("")) {
            dodatak = " and ugovori.CVRUGO='" + jlrCVRUGO.getText()+"' ";
        }

        String sql = "select ugovori.* from ugovori,partneri where ugovori.cpar = partneri.cpar and ugovori.AKTIV='D' AND "
          + " ugovori.corg = '"+ jlrCORG.getText() + "' AND "+frmUgovori.getCorgCondition("ugovori")+dodatak+" ORDER BY partneri.nazpar";
//System.out.println(sql);
        
        QueryDataSet ugovorizag = hr.restart.util.Util
                .getNewQueryDataSet(sql);

        QueryDataSet ugovoristav = null;
        for (ugovorizag.first(); ugovorizag.inBounds(); ugovorizag.next()) {
            ugovoristav = stugovor.getDataModule().getTempSet(
                Condition.whereAllEqual(new String[] {"CUGOVOR","KNJIG"}, ugovorizag)
                .and(Condition.where("STATUSRAC", Condition.NOT_EQUAL, "N")).and(Condition.where("IPRODSP", Condition.NOT_EQUAL, Aus.zero2))); 
            ugovoristav.open();
//              hr.restart.util.Util
//                    .getNewQueryDataSet("select * from stugovor where cugovor='"
//                            + ugovorizag.getString("CUGOVOR") + "' and STATUSRAC !='N' ");
            if (ugovoristav.getRowCount() == 0)
                continue;
            addDoki(ugovorizag);
            int rbr = 1;
            ugovoristav.setSort(new SortDescriptor(new String[]{"CART1"})); 
            for (ugovoristav.first(); ugovoristav.inBounds(); ugovoristav.next()) {
                addStdoki(ugovorizag, ugovoristav, rbr);
                rbr++;
            }
//            maintenceIspis();
        }
        raTransaction.saveChanges(zagRac);
        raTransaction.saveChanges(stavRac);
        raTransaction.saveChanges(dm.getSeq());
//        raTransaction.saveChanges(dm.getVTText());
    }
    
    public void ponistenjeIznosaGui(){
        int i = kveshcn("Želite li poništiti iznose na ugovorima \nkoji su oznaèeni za poništavanje ?");
        if (i==0){
            ponistenjeIznosa();
        }
    }
    public void ponistenjeIznosa(){
        
        raLocalTransaction rLT = new raLocalTransaction(){
            
            public boolean transaction() throws Exception {
                String upit = "SELECT * FROM STUGOVOR WHERE STATUSPON='D' "
                       + " and "+frmUgovori.getCorgCondition("STUGOVOR")
                       + " and exists ("+
                "SELECT * FROM Ugovori WHERE ugovori.cugovor = stugovor.cugovor and "
                + "ugovori.aktiv ='D' and ugovori.corg = '"+ jlrCORG.getText()
                + "' AND "+frmUgovori.getCorgCondition("Ugovori");
                if (!jlrCVRUGO.getText().equals("")) {
                    upit = upit + " and ugovori.CVRUGO ='" + jlrCVRUGO.getText() + "'";
                }
                upit = upit+")";
                System.out.println(upit);
                
                QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(upit);
                BigDecimal nula = Aus.zero2;
                for (qds.first();qds.inBounds();qds.next()) {
                    qds.setBigDecimal("KOL",new BigDecimal("1.00"));
                    qds.setBigDecimal("VAL_VC",nula);
                    qds.setBigDecimal("VAL_MC",nula);
                    qds.setBigDecimal("FC",nula);
                    qds.setBigDecimal("UPRAB",nula);
                    qds.setBigDecimal("UIRAB",nula);
                    qds.setBigDecimal("FVC",nula);
                    qds.setBigDecimal("UPPOR",nula);
                    qds.setBigDecimal("POR1",nula);
                    qds.setBigDecimal("POR2",nula);
                    qds.setBigDecimal("POR3",nula);
                    qds.setBigDecimal("IPRODBP",nula);
                    qds.setBigDecimal("FMC",nula);
                    qds.setBigDecimal("IPRODSP",nula);
                    qds.setBigDecimal("IZNOSRAC",nula);
                    qds.setBigDecimal("IZNOSRACSP",nula);
                }
                
                raTransaction.saveChanges(qds);
                return true;
            }
        };
        
        if (rLT.execTransaction()) {
            javax.swing.JOptionPane.showConfirmDialog(this,
                    "Poništavanje ugovora uspješno !", "Poruka",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.DEFAULT_OPTION);
        } else {
            javax.swing.JOptionPane.showConfirmDialog(this,
                    "Poništavanje ugovora NIJE uspjelo !", "Poruka",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.DEFAULT_OPTION);
        }
    }
}


class Mnemonik extends raFrame {
  
  private raCommonClass rcc = raCommonClass.getraCommonClass();
  private hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();
  
  QueryDataSet sdsmnemonik;
  {
    sdsmnemonik = new QueryDataSet();
    sdsmnemonik.setColumns(new Column[] { 
        dM.createStringColumn("KEY","Kljuè",10),
        dM.createStringColumn("VALUE","Vrijednost",20)});
    sdsmnemonik.open();
  }
  private hr.restart.util.OKpanel localokp = new hr.restart.util.OKpanel() {
    private static final long serialVersionUID = 1L;
    
    public void jBOK_actionPerformed() {
      locpresOK();
    }
    
    public void jPrekid_actionPerformed() {
      locpresCancel();
    }
  };
  
  JraTextField jtfCSIFRA = new JraTextField();
  JraTextField jtfNAZIV = new JraTextField();
  
  raNavAction rnvAdd = new raNavAction("Novi",raImages.IMGADD,KeyEvent.VK_F2) {
    public void actionPerformed(ActionEvent e) {
      sdsmnemonik.insertRow(false);
      rcc.setLabelLaF(jtfCSIFRA,true);
      rcc.setLabelLaF(jtfNAZIV,true);
      rcc.EnabDisabAll(jptv,false);
      jtfCSIFRA.requestFocus();
    }
  };
  raNavAction rnvUpdate = new raNavAction("Izmjena",raImages.IMGCHANGE,KeyEvent.VK_F4) {
    public void actionPerformed(ActionEvent e) {
      izmjena();
    }
  };
  raNavAction rnvDel = new raNavAction("Brisanje",raImages.IMGDELETE,KeyEvent.VK_F3) {
    public void actionPerformed(ActionEvent e) {
      sdsmnemonik.deleteRow();
    }
  };
  
  private raJPTableView jptv = new raJPTableView(){
    public void mpTable_doubleClicked() {
      izmjena();
    }
  };
  
  public void defaultValues(Timestamp ts){
    Calendar cal = Calendar.getInstance();
    cal.setTime(ts);
    
    if (!lD.raLocate(sdsmnemonik,"KEY","<mje>")) {
      sdsmnemonik.insertRow(false);
      sdsmnemonik.setString("KEY","<mje>");
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.MONTH) + 1));
      sdsmnemonik.post();
    } else {
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.MONTH) + 1));
      sdsmnemonik.post();
    } 
    
    if (!lD.raLocate(sdsmnemonik,"KEY","<god>")) {
      sdsmnemonik.insertRow(false);
      sdsmnemonik.setString("KEY","<god>");
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.YEAR)));
      sdsmnemonik.post();
    } else {
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.YEAR)));
      sdsmnemonik.post();
    }
    cal.add(Calendar.MONTH, -1);
    if (!lD.raLocate(sdsmnemonik,"KEY","<mje-1>")) {
      sdsmnemonik.insertRow(false);
      sdsmnemonik.setString("KEY","<mje-1>");
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.MONTH) + 1));
      sdsmnemonik.post();
    } else {
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.MONTH) + 1));
      sdsmnemonik.post();
    } 
    
    if (!lD.raLocate(sdsmnemonik,"KEY","<god-1>")) {
      sdsmnemonik.insertRow(false);
      sdsmnemonik.setString("KEY","<god-1>");
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.YEAR)));
      sdsmnemonik.post();
    } else {
      sdsmnemonik.setString("VALUE",String.valueOf(cal.get(Calendar.YEAR)));
      sdsmnemonik.post();
    }
  }
  
  
  public Mnemonik() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        pressESC();
      }
    });
    
    this.setTitle("Zamjena mnemonika");
    jptv.setDataSet(sdsmnemonik);
    jptv.setVisibleCols(new int[] {0,1});
    jptv.getNavBar().addOption(rnvAdd);
    jptv.getNavBar().addOption(rnvUpdate);
    jptv.getNavBar().addOption(rnvDel);
    
    
    
    jptv.initKeyListener(this);
    jtfCSIFRA.setDataSet(sdsmnemonik);
    jtfCSIFRA.setColumnName("KEY");
    jtfNAZIV.setDataSet(sdsmnemonik);
    jtfNAZIV.setColumnName("VALUE");
    JLabel jlNaziv = new JLabel();
    JLabel jlSifra = new JLabel();
    
    JLabel jlText = new JLabel();
    jptv.getNavBar().registerNavBarKeys(this);
    JPanel panel = new JPanel();
    jlSifra.setText("\u0160ifra");
    jlNaziv.setText("Naziv");
    jlText.setText("Zamjena");
    panel.setPreferredSize(new Dimension(555,73));
    XYLayout xyl = new XYLayout();
    panel.setLayout(xyl);
    xyl.setWidth(555);
    xyl.setHeight(73);
    
    panel.add(jlSifra, new XYConstraints(150, 15, 100, -1));
    panel.add(jlNaziv, new XYConstraints(255, 15, 200, -1));
    panel.add(jlText, new XYConstraints(15, 38, -1, -1));
    panel.add(jtfCSIFRA, new XYConstraints(150, 32, 100, -1));
    panel.add(jtfNAZIV,  new XYConstraints(255, 32, 285, -1));
    panel.setBorder(BorderFactory.createEtchedBorder());
    
    getContentPane().add(panel,BorderLayout.CENTER);
    getContentPane().add(localokp,BorderLayout.SOUTH);
    getContentPane().add(jptv,BorderLayout.NORTH);
    //localokp.registerOKPanelKeys(this);
    pack();
  }
  public void locpresOK(){
    jptv.getDataSet().post();
    pressESC();
//  this.setVisible(false);
  }
  public void locpresCancel(){
    jptv.getDataSet().cancel();
    this.setVisible(false);
  }
  public void pressESC(){
    jptv.getDataSet().cancel();
    rcc.setLabelLaF(jtfCSIFRA,false);
    rcc.setLabelLaF(jtfNAZIV,false);
    rcc.EnabDisabAll(jptv,true);
  }
  public void izmjena(){
    rcc.setLabelLaF(jtfCSIFRA,false);
    rcc.setLabelLaF(jtfNAZIV,true);
    rcc.EnabDisabAll(jptv,false);
    jtfNAZIV.requestFocus();
  }
  
  private String[][] mnemArray = null;
  
  public String[][] mnemonicArray(){
    mnemArray = new String [sdsmnemonik.rowCount()][2];
    for (sdsmnemonik.first();sdsmnemonik.inBounds();sdsmnemonik.next()){
      mnemArray[sdsmnemonik.row()][0] = sdsmnemonik.getString("KEY");
      mnemArray[sdsmnemonik.row()][1] = sdsmnemonik.getString("VALUE");
    }
    return mnemArray;
  }
}
