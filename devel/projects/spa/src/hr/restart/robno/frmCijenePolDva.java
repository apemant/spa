/****license*****************************************************************
**   file: frmCijenePolDva.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raKeyAction;
import hr.restart.util.raUpitLite;
import hr.restart.util.reports.JasperHook;
import hr.restart.zapod.Tecajevi;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmCijenePolDva extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private raControlDocs rCD = new raControlDocs();

  JPanel dokumentiPanel = new JPanel();
  JPanel soloArtiklPanel = new JPanel();
//  JPanel multiArtikliPanel = new JPanel();
  JTabbedPane tpane = new JTabbedPane(){
    };

  JLabel jlVrDok = new JLabel();
  JLabel jlBrdok = new JLabel();
  JraTextField jraBrDok = new JraTextField();
  raComboBox rcbVrDok = new raComboBox();
  JraButton copyArticles = new JraButton();

  TableDataSet tds = new TableDataSet();
  private QueryDataSet artikliTable = new QueryDataSet();

  JPanel jp = new JPanel();
  JPanel mp = new JPanel();

  private XYLayout xyl1 = new XYLayout();
  private XYLayout xyl2 = new XYLayout();
  private BorderLayout bl1 = new BorderLayout();
  private BorderLayout bl2 = new BorderLayout();

  rapancskl1 rpcskl = new rapancskl1(349) {
    public void findFocusAfter(){}
    public void MYpost_after_lookUp(){
      if(!rpcskl.jrfCSKL.getText().equals("")) {
        if (rpcskl.jrfCSKL.isNavValueChanged()){
//          System.out.println("Namještam cskl rapancartu"); //XDEBUG delete when no more needed
          rpcart.setCskl(rpcskl.jrfCSKL.getText());
//          setJPTV();
        }
        /** @todo  vidi orginal klasu*/
      }
    }
  };

  rapancskl1 rpcsklIzl = new rapancskl1(false, 349,"Izlazno skladište") {
    public void findFocusAfter() {}
    public void MYpost_after_lookUp(){
      if(!rpcskl.jrfCSKL.getText().equals("")) {
        /** @todo  vidi orginal klasu*/
      }
    }
  };

  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          add.requestFocus();
        }
      });
    }
  };

  JraButton add = new JraButton();
  JraButton remove = new JraButton();
  JraButton copy = new JraButton();

  hr.restart.util.raJPTableView jptv = new hr.restart.util.raJPTableView(false) {
    public void mpTable_killFocus(java.util.EventObject e) {
//      System.out.println("killing is my biznis....");
/** @todo cemu ovo */
//      tpane.requestFocus();
    }
    public void mpTable_doubleClicked() {
      /** @todo editThisRow() implementacija */
      /*editThisRow();*/
    }
  };

  ///////////////// - edit panel

  JDialog editDijalog = new JDialog(this.getJframe());
  JPanel editPanel = new JPanel();
  JPanel contentPanel = new JPanel();
  JLabel editCartLabel = new JLabel("Šifra");
  JLabel editCart1Label = new JLabel("Oznaka");
  JLabel editBCLabel = new JLabel("Barcode");
  JLabel editNazartLabel = new JLabel("Naziv");
  JLabel editMCLabel = new JLabel("Cijena s porezom");
  JLabel editGodinaLabel = new JLabel("Godina");
  JLabel editVrdokLabel = new JLabel("Vrsta ul. dokumenta");
  JLabel editBrdokLabel = new JLabel("Broj ul. dokumenta");
  JLabel editTkalLabel = new JLabel("TKAL");
  JraTextField jraCopyNum = new JraTextField();

  rapancart rpc2 = new rapancart();



  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      dispoz();
    }
    public void jPrekid_actionPerformed() {
      dispoz();
    }
  };

  ///////////////// - edit panel

  static frmCijenePolDva instanceOfMe;

  public frmCijenePolDva() {
    try {
      jbInit();
      instanceOfMe = this;
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmCijenePolDva getInstance(){
    if (instanceOfMe == null) instanceOfMe = new frmCijenePolDva();
    return instanceOfMe;
  }

  String godina;

  public String getGodina(){
    return godina;
  }

  public void componentShow() {
    this.changeIcon(1);
    
    
    godina = Aut.getAut().getKnjigodRobno();//dm.getKnjigod().getString("GOD");
    
    if (!godina.equals(hr.restart.util.Util.getUtil().getYear(vl.getToday()))){
      godina = hr.restart.util.Util.getUtil().getYear(vl.getToday());
    }
    
    rpcart.setGodina(godina);

    System.out.println("godina " + godina);
    
    tds.setString("VRDOK", "KAL");
    tds.setInt("BRDOK", 0);
    rcbVrDok.setSelectedIndex(0);
    tpane.setSelectedIndex(0);
//    tds.setInt("TABCHECK", 0);

    rpcart.setDefParam();
//    rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));

    showDefaultValues();
    boolean cskl_corg =
        rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

    rcbVrDok.requestFocus();

    if(cskl_corg) {
      rpcskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
      rpcskl.jrfCSKL.forceFocLost();
      } else if(!cskl_corg) rpcskl.jrfCSKL.requestFocus();
  }

  void showDefaultValues(){
    rpcsklIzl.Clear();
    rcc.EnabDisabAll(rpcsklIzl, false);
  }

  DataSet repSet;
  boolean okpressed;

  public void okPress() {
    String qStr;
    if (tpane.getSelectedIndex() == 0){
      qStr = rdUtil.getUtil().getIspisCijenaPolica(
          rpcskl.jrfCSKL.getText(),
          rpcsklIzl.jrfCSKL.getText(),
          tds.getString("VRDOK"),
          tds.getInt("BRDOK"),
          godina
          );
//          System.out.println("qStr "+qStr);

      try {
        repSet = hr.restart.util.Util.getNewQueryDataSet(qStr);
//        hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//        syst.prn(repSet);
      }
      catch (Exception ex) {
        System.err.println("Exception in module okPress - query String not valid!");
        System.err.println(qStr);
//        firstESC();
        setNoDataAndReturnImmediately();
        return;
      }
    } else if (tpane.getSelectedIndex() == 1){
      if (!htbl.isEmpty()){
        repSet = (DataSet)artikliTable;
//      } else {
//        qStr = rdUtil.getUtil().getIspisCijenaPolica(rpcskl.jrfCSKL.getText(), rpcart.findCART());
//        repSet = hr.restart.util.Util.getNewQueryDataSet(qStr);
      }
      okpressed = true;
    }
  }

  public boolean Validacija(){
    vl.isEmpty(rpcskl.jrfCSKL);
    if (tpane.getSelectedIndex() == 0){
      if (vl.isEmpty(jraBrDok)) return false;
      if ((rcbVrDok.getSelectedIndex()==4 || rcbVrDok.getSelectedIndex()==5) && vl.isEmpty(rpcsklIzl.jrfCSKL)) return false;
    } else if (tpane.getSelectedIndex() == 1){
      if (rpcart.getCART().trim().equals("") && rpcart.getCART1().trim().equals("") &&
          rpcart.getBC().trim().equals("") && rpcart.getNAZART().trim().equals("")){
        if (htbl.isEmpty()) {
          rpcart.SetDefFocus();
          JOptionPane.showMessageDialog(soloArtiklPanel,"Nema artikala","Upozorenje", JOptionPane.WARNING_MESSAGE);
          return false;
        } else return true;
      }
    }
//    testingTesting();
    return true;
  }

  public void firstESC() {
    if (tpane.getSelectedIndex() == 0) {
      tpane.setEnabledAt(1,true);
      rcc.setLabelLaF(rcbVrDok,true);
      rcc.setLabelLaF(jraBrDok,true);
      rcc.setLabelLaF(copyArticles,true);
      if (tds.getInt("BRDOK") != 0){
        rpcsklIzl.setCSKL("");
        rcbVrDok.setSelectedIndex(0);
        tds.setInt("BRDOK",0);
        rcbVrDok.requestFocus();
      } else {
        rcc.EnabDisabAll(rpcskl,true);
        rpcskl.Clear();
        rpcskl.jrfCSKL.requestFocus();
      }
      artikliTable.deleteAllRows();
      htbl.clear();
      jptv.fireTableDataChanged();
    } else if (tpane.getSelectedIndex() == 1) {
      tpane.setEnabledAt(0,true);
      if (!htbl.isEmpty()) { //!rpcart.getCART().equals("")) {
        if (!okpressed){
//          jptv.enableEvents(true);
          artikliTable.deleteAllRows();
          htbl.clear();
          jptv.fireTableDataChanged();
        } else {
//          jptv.fireTableDataChanged();
          okpressed = !okpressed;
        }
        rcc.EnabDisabAll(rpcart,true);
        rcc.setLabelLaF(add,true);
        rcc.setLabelLaF(remove,true);
        rcc.setLabelLaF(copy,true);
        rcc.setLabelLaF(jraCopyNum,true);
        rcc.setLabelLaF(jptv,true);
        
          jptv.enableEvents(true);
        rpcart.setCART();
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            rpcart.SetDefFocus();
          }
        });
      } else if (!rpcart.getCART().equals("") || !rpcart.getCART1().equals("") || !rpcart.getBC().equals("")){
        rcc.EnabDisabAll(rpcart,true);
        rcc.setLabelLaF(add,true);
        rcc.setLabelLaF(remove,true);
        rcc.setLabelLaF(copy,true);
        rcc.setLabelLaF(jraCopyNum,true);
        rpcart.setCART();
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            rpcart.SetDefFocus();
          }
        });
      } else {
        rcc.EnabDisabAll(rpcskl,true);
        rpcskl.Clear();
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            rpcskl.jrfCSKL.requestFocus();
          }
        });
      }
    }
  }

  public boolean runFirstESC() {
//    System.out.println("rpcskl.getCSKL() = '" + rpcskl.getCSKL() +"'");
    if (!rpcskl.getCSKL().equals("")){
      return true;
    } else
    return false;
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    jptv.enableEvents(false);
    return true;
  }

  private void jbInit() throws Exception {
    /** @todo primjer dohvata cjena za more artikala */
//    System.out.println(rdUtil.getUtil().getIspisCijenaPolica("01",new String[] {"345"}));
//    System.out.println(rdUtil.getUtil().getIspisCijenaPolica("01",new String[] {"345","123"}));
//    System.out.println(rdUtil.getUtil().getIspisCijenaPolica("01",new String[] {"345","432","2123","2324",}));

//    this.addKeyListener(new java.awt.event.KeyAdapter() {
//      public void keyTyped(KeyEvent e) {
//        this_keyPressed(e);
//      }
//    });
//TODO marker
    
    this.addKeyAction(new raKeyAction(java.awt.event.KeyEvent.VK_F5) {
      public void keyAction() {
        getOKPanel().jBOK_actionPerformed();
      }
    });

    this.addKeyAction(new raKeyAction(java.awt.event.KeyEvent.VK_F11) {
      public void keyAction() {
        addArtikl(rpcart.getCART());
      }
    });

    this.addKeyAction(new raKeyAction(java.awt.event.KeyEvent.VK_F12) {
      public void keyAction() {
        removeArtikl();
      }
    });

    rpcskl.setDisabAfter(true);

    this.setJPan(mp);
    mp.setLayout(bl1);

    jp.setLayout(xyl1);
    dokumentiPanel.setLayout(xyl2);
    soloArtiklPanel.setLayout(xyl2);
//    multiArtikliPanel.setLayout(bl2);

    xyl1.setWidth(650);
    xyl1.setHeight(65);
    xyl2.setWidth(650);
    xyl2.setHeight(300);

    rpcart.setMode(new String("DOH"));
    rpcart.setBorder(null);

    tds.setColumns(new Column[]{
      dM.createStringColumn("VRDOK",3),
      dM.createIntColumn("BRDOK","Broj dokumenta")/*,
      dM.createIntColumn("TABCHECK")*/
      /** @todo po potrebi dodat jos kolona */
    });
    tds.open();

    tpane.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e){
//        System.out.println("itemstatechanged " + tpane.getSelectedIndex());
        if (tpane.getSelectedIndex() == 0){
          artikliTable.deleteAllRows();
          htbl.clear();
          jptv.fireTableDataChanged();
          rcc.EnabDisabAll(dokumentiPanel,true);
          rcbVrDok.requestFocus();
        } else {
          rpcsklIzl.setCSKL("");
          rcbVrDok.setSelectedIndex(0);
          tds.setInt("BRDOK",0);
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              rcc.EnabDisabAll(soloArtiklPanel,true);
              rpcart.SetDefFocus();
            }
          });
        }
      }
    });

    tpane.addFocusListener(new java.awt.event.FocusListener() {
      public void focusLost(FocusEvent fe) {
//        System.out.println("focus lost");
      }
      public void focusGained(FocusEvent fe) {
//        System.out.println("focus gained");
        if (tpane.getSelectedIndex() == 0){
          rcbVrDok.requestFocus();
        } else {
          rpcart.SetDefFocus();
        }
      }
    });

    rcbVrDok.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        rcbVrDok_itemStateChanged(e);
      }
    });
    rcbVrDok.setRaColumn("VRDOK");
    rcbVrDok.setRaDataSet(tds);
    rcbVrDok.setRaItems(new String[][] {
      {"Kalkulacija","KAL"},
      {"Primka - kalkulacija","PRK"},
      {"Po\u010Detno stanje","PST"},
      {"Poravnanja-nivelacije","POR"},
      {"Me\u0111uskladišnica","MES"},
      {"Ulazna me\u0111uskladišnica","MEU"}
    });

    jlVrDok.setText("Vrsta dokumenta");
    jlBrdok.setText("Broj dokumenta");

    jraBrDok.setDataSet(tds);
    jraBrDok.setColumnName("BRDOK");
    copyArticles.setText("Prijenos artikala");
    copyArticles.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copyArticles_actionPerformed(e);
      }
    });

    add.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addArt();
      }
    });
    remove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeArt();
      }
    });
    copy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copyArt();
      }
    });

    add.setActionCommand("");
    add.setText("Dodaj");
    remove.setText("Obriši");
    copy.setText("Kopiraj");
    dokumentiPanel.add(jlVrDok,  new XYConstraints(15, 20, -1, -1));
    dokumentiPanel.add(rcbVrDok,    new XYConstraints(150, 20, 169, -1));
    dokumentiPanel.add(jraBrDok,   new XYConstraints(523, 20, 81, -1));
    dokumentiPanel.add(jlBrdok,    new XYConstraints(400, 21, -1, -1));
    dokumentiPanel.add(rpcsklIzl,      new XYConstraints(0, 45, 640, -1));
    dokumentiPanel.add(copyArticles,  new XYConstraints(150, 80, 169, -1));

    soloArtiklPanel.add(rpcart,  new XYConstraints(0, 0, -1, 80));
    soloArtiklPanel.add(jptv, new XYConstraints(0,120,650,180));
    soloArtiklPanel.add(add, new XYConstraints(150, 80, 100, -1));
    soloArtiklPanel.add(remove, new XYConstraints(255, 80, 100, -1));
    soloArtiklPanel.add(copy, new XYConstraints(360, 80, 100, -1));
    soloArtiklPanel.add(jraCopyNum, new XYConstraints(480, 80, 50, -1));
    
    new raTextMask(jraCopyNum, 4, false, raTextMask.NUMERIC);
    jraCopyNum.setText("1");

    setJPTV(); /** @todo punjenje JPTV-a */

//    multiArtikliPanel.add(jptv, BorderLayout.CENTER);

    tpane.addTab("Dokumenti", null, dokumentiPanel, "Ispis za artikle na dokumentu");
    tpane.addTab("Artikli", null, soloArtiklPanel, "Ispis za izabrane artikle");

    jp.add(rpcskl, new XYConstraints(0, 1, 640, -1));
    mp.add(jp, BorderLayout.CENTER);
    mp.add(tpane, BorderLayout.SOUTH);
//    this.addReport("hr.restart.robno.repCijenePol", "Ispis cjena za police",5);

    this.addReport("hr.restart.robno.repCijenePol","hr.restart.robno.repCijenePol","CijenePol","Ispis cjena za police 6 kom/str");
    this.addReport("hr.restart.robno.repCijenePolSmall","hr.restart.robno.repCijenePol","CijenePolSmall","Ispis cjena za police 24 kom/str");
    this.addReport("hr.restart.robno.repCijenePolTiny","hr.restart.robno.repCijenePol","CijenePolTiny","Ispis cjena za police 48 kom/str");
    
//    jptv.addTableModifier(dablclickTableColorModifier);
//    jptv.fireTableDataChanged();


    editDijalog.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {}

      public void keyPressed(KeyEvent e) {}

      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ESCAPE) {
          dispoz();
        }
        else if (e.getKeyCode() == e.VK_F10) {
          dispoz();
        }
      }
    });
  }
    
  void setJPTV(){
    artikliTable.close();
    artikliTable.setColumns(new Column[] { /** @todo broj iz KPR */
//                            dm.createIntColumn("TMP"),
      (Column) dm.getArtikli().getColumn("CART").clone(),
      (Column) dm.getArtikli().getColumn("CART1").clone(),
      (Column) dm.getArtikli().getColumn("BC").clone(),
      (Column) dm.getArtikli().getColumn("NAZART").clone(),
      (Column) dm.getArtikli().getColumn("JM").clone(),
      (Column) dm.getStanje().getColumn("ZC").clone(),
      (Column) dm.getStanje().getColumn("GOD").clone(),
      (Column) dm.getStanje().getColumn("CSKL").clone(),
      dm.createStringColumn("CSKLUL", 12),
      dm.createStringColumn("CSKLIZ", 12),
      dm.createStringColumn("VRDOK",3),
      dm.createIntColumn("BRDOK"),
      dm.createStringColumn("TKAL",50),
    });

//    artikliTable.getColumn("MC").setVisible(0);
//    artikliTable.getColumn("TMP").setVisible(0);
    artikliTable.getColumn("GOD").setVisible(0);
    artikliTable.getColumn("CSKL").setVisible(0);
    artikliTable.getColumn("CSKLUL").setVisible(0);
    artikliTable.getColumn("CSKLIZ").setVisible(0);
    artikliTable.getColumn("VRDOK").setVisible(0);
    artikliTable.getColumn("BRDOK").setVisible(0);
    artikliTable.getColumn("TKAL").setVisible(0);
    artikliTable.getColumn("CART").setVisible(0);
    artikliTable.getColumn("CART1").setVisible(0);
    artikliTable.getColumn("BC").setVisible(0);
    artikliTable.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);
    artikliTable.open();
    jptv.setDataSet(artikliTable);
//    jptv.initKeyListener(this);
  }

  void rcbVrDok_itemStateChanged(ItemEvent e) {
    if(rcbVrDok.getSelectedIndex()==4 || rcbVrDok.getSelectedIndex()==5) {
      rcc.EnabDisabAll(rpcsklIzl, true);
    } else {
      rpcsklIzl.Clear();
      rcc.EnabDisabAll(rpcsklIzl, false);
    }
//    this.jraBrDok.requestFocus();
  }
  
  public void addHooks() {
    JasperHook jhook;
    getRepRunner().addJasperHook("hr.restart.robno.repCijenePolTiny",
    jhook = new JasperHook() {
      public void adjustDesign(String reportName, JasperDesign design) {
        adjustReport(reportName, design);        
      }
    });
    
    getRepRunner().addJasperHook("hr.restart.robno.repCijenePol", jhook);
    getRepRunner().addJasperHook("hr.restart.robno.repCijenePolSmall", jhook);

  }
  
  void adjustReport(String reportName, JasperDesign design) {
    if (reportName.equals("hr.restart.robno.repCijenePolTiny")) {
      String hor = frmParam.getParam("robno", "hor40", "0", 
          "Razmak (u pointima) izmeðu dva reda na nalijepnicama 40");
      String ver = frmParam.getParam("robno", "ver40", "0", 
          "Razmak (u pointima) izmeðu dva stupca na nalijepnicama 40");
      ((JRDesignBand) design.getDetail()).setHeight(
          design.getDetail().getHeight() + Aus.getNumber(ver));
      design.setColumnSpacing(Aus.getNumber(hor));
    }
    
    JRDesignBand detail = ((JRDesignBand) design.getDetail());
    for (Iterator i = detail.getChildren().iterator(); i.hasNext(); ) {
      JRDesignElement el = (JRDesignElement) i.next();
      if (el instanceof JRStaticText  &&  ((JRStaticText) el).getText().equals("kn")) {
        ((JRStaticText) el).setText(Tecajevi.getDomOZNVAL());
      }
    }
  }

  java.util.Hashtable htbl = new java.util.Hashtable();
//TODO marker 1
//  protected void this_keyPressed(KeyEvent e){
//
//    if (e.getKeyCode()==e.VK_F10 || e.getKeyCode()==e.VK_F5) {
//      this.getOKPanel().jBOK_actionPerformed();
//    }
//    if(e.getKeyCode()==e.VK_ESCAPE){
//      if(!runFirstESC())this.getOKPanel().jPrekid_actionPerformed();
//      else firstESC();
//    }
//    if (e.getKeyCode()==e.VK_F11) {
//      if (tpane.getSelectedIndex() == 1){
//    //        System.out.println("dodat artikl...");
//        addArtikl(rpcart.getCART());
//      }
//    }
//    if (e.getKeyCode()==e.VK_F12) {
//      if (tpane.getSelectedIndex() == 1){
//    //        System.out.println("miknit artikl...");
//        removeArtikl();
//      }
//    }
//  }

  String noNulls(java.util.HashMap hASh, String key){
    try {
      return hASh.get(key).toString();
    }
    catch (NullPointerException ex) {
      return "";
    }
  }

  void addArtikl(String cart){
	  System.out.println("Artikli: "+cart);
	  //    if (cart.equals("") || rpcart.getCART1().equals("") || rpcart.getBC().equals("") || rpcart.getNAZART().equals("")) return;
    if (cart.equals("")) return;
//    int tmp = 0;
    jptv.enableEvents(false);
//    if (lookupData.getlookupData().raLocate(artikliTable, "CART", cart)) {
//      tmp = artikliTable.getInt("TMP") + 1;
//    }
      if (artikliTable.rowCount() != 0) artikliTable.insertRow(true);
      htbl.put(cart,rpcart.getNAZART());
//      artikliTable.setInt("TMP",tmp);
      artikliTable.setInt("CART",Integer.parseInt(cart));
      artikliTable.setString("CART1",rpcart.getCART1());
      artikliTable.setString("BC",rpcart.getBC());
      artikliTable.setString("NAZART",rpcart.getNAZART());
      artikliTable.setString("JM",rpcart.jrfJM.getText());

      //dm.getStanje().refresh();

      DataRow dr = lookupData.getlookupData().raLookup(dm.getStanje(), new String[] {"CSKL","CART","GOD"}, new String[] {rpcskl.getCSKL(), cart, getGodina()});

//      System.out.println("dataRow null - " + (dr != null));
      
//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
      //st.prn(dr);

      if (dr != null) { // moj propust :(
//        hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//        syst.prn(dr);

        java.util.HashMap hm = rCD.getHashMapKey(dr.getString("TKAL"));
        artikliTable.setString("CSKLUL",noNulls(hm,"CSKLUL"));
        artikliTable.setString("CSKLIZ",noNulls(hm,"CSKLIZ"));
        artikliTable.setString("CSKL",noNulls(hm,"CSKL"));
        artikliTable.setString("VRDOK",noNulls(hm,"VRDOK"));
        artikliTable.setString("GOD",noNulls(hm,"GOD"));
        try {
          artikliTable.setInt("BRDOK", Integer.parseInt(noNulls(hm,"BRDOK")));
        }
        catch (NumberFormatException ex) {
          ex.printStackTrace();
        }
        artikliTable.setBigDecimal("ZC", dr.getBigDecimal("ZC"));
        artikliTable.setString("TKAL", dr.getString("TKAL"));
      }


//      if (lookupData.getlookupData().raLocate(dm.getStanje(), new String[] {"CSKL","CART"}, new String[] {rpcskl.getCSKL(), cart})) {
//        artikliTable.setBigDecimal("ZC", dm.getStanje().getBigDecimal("ZC"));
//        artikliTable.setString("TKAL", dm.getStanje().getString("TKAL"));
//      }
      else {
        lookupData.getlookupData().raLocate(dm.getArtikli(), new String[] {"CART"}, new String[] {cart});
        artikliTable.setBigDecimal("ZC", dm.getArtikli().getBigDecimal("MC"));
        artikliTable.setString("CSKL","");
        artikliTable.setString("CSKLUL","");
        artikliTable.setString("CSKLIZ","");
        artikliTable.setString("VRDOK","");
        artikliTable.setString("GOD","");
        artikliTable.setString("TKAL", "");
      }

      jptv.fireTableDataChanged();
//    }

    jptv.enableEvents(true);
    artikliTable.last();
    rcc.EnabDisabAll(rpcart,true);
    rpcart.setCART();
    rpcart.SetDefFocus();
  }

  void removeArtikl(){
    htbl.remove(artikliTable.getInt("CART")+"");
    try {
      artikliTable.deleteRow();
    }
    catch (Exception ex) {
    }
    jptv.fireTableDataChanged();
  }

//  private void testingTesting(){
//    System.out.println("Selected index = " + rcbVrDok.getSelectedIndex());
//    System.out.println("\n");
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(tds);

//    Object[] aaaa = htbl.keySet().toArray();

//    for (int i = 0; i < aaaa.length; i++) {
//      System.out.println(aaaa[i]);
//    }
//  }

  void copyArticles_actionPerformed(ActionEvent e) {
//    System.out.println("pritisnija si me!!!");
    copyArticlesTo();
  }
  
  void addArt() {
    if (!rpcart.getCART().equals("") || !rpcart.getCART1().equals("") || !rpcart.getBC().equals("")){
      addArtikl(rpcart.getCART());
    } else if (!rpcart.getNAZART().equals("")) {
        String sqlsw="SELECT cart FROM artikli, stanje WHERE artikli.cart=stanje.cart and "
        +" artikli.nazart like ('%"+rpcart.getNAZART().trim()+"%') and god='"+getGodina()+"'";
        System.out.println("Query: "+sqlsw);
        QueryDataSet ds = ut.getNewQueryDataSet(sqlsw);
            for (ds.first(); ds.inBounds(); ds.next()) {
                rpcart.setCART(ds.getInt("cart"));
                rpcart.jrfCART.forceFocLost();
                addArtikl(new Integer(ds.getInt("cart")).toString());
          }
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        rpcart.SetDefFocus();
      }
    });
  }
  
  void removeArt() {
    if (artikliTable.getRowCount() != 0)
      removeArtikl();
  }

  void copyArt() {
    if (artikliTable.getRowCount() == 0) return;
    
    int num = Aus.getNumber(jraCopyNum.getText());
    if (num < 1 || num > 200) num = 1;
    
    DataRow row = new DataRow(artikliTable);
    artikliTable.copyTo(row);
    
    for (int i = 0; i < num; i++) {
      artikliTable.insertRow(false);
      row.copyTo(artikliTable);
      artikliTable.post();
    }
    jptv.fireTableDataChanged();
  }

  void copyArticlesTo(){
//    if (rpcskl.getCSKL().equals("") ||
//        rpcsklIzl.getCSKL().equals("") ||
//        tds.getString("VRDOK").equals("") ||
//        tds.getInt("BRDOK") == 0) return;

//    System.out.println("rpcskl.jrfCSKL.getText()    " + rpcskl.jrfCSKL.getText() + "\n"+
//                       "rpcsklIzl.jrfCSKL.getText() " + rpcsklIzl.jrfCSKL.getText() + "\n" +
//                       "VRDOK " + tds.getString("VRDOK") + "\n" +
//                       "BRDOK " + tds.getInt("BRDOK"));

    String qStr = rdUtil.getUtil().getIspisCijenaPolica(
        rpcskl.jrfCSKL.getText(),
        rpcsklIzl.jrfCSKL.getText(),
        tds.getString("VRDOK"),
        tds.getInt("BRDOK"),
        godina
        );
//    System.out.println("qstr : '" + qStr+ "'");

    if (qStr.equals("")) {
      JOptionPane.showMessageDialog(this.getJPan(),"Nepostojeæi dokument","Upozorenje", JOptionPane.WARNING_MESSAGE);
      return;
    }


    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(qStr);

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(qds);

    qds.first();

    jptv.enableEvents(false);

    do {
      if (!lookupData.getlookupData().raLocate(artikliTable,"CART",qds.getInt("CART")+"")) {
        if (artikliTable.rowCount() != 0) artikliTable.insertRow(true);
        htbl.put(qds.getInt("CART")+"",qds.getString("NAZART"));
        artikliTable.setInt("CART",qds.getInt("CART"));
        artikliTable.setString("CART1",qds.getString("CART1"));
        artikliTable.setString("BC",qds.getString("BC"));
        artikliTable.setString("NAZART",qds.getString("NAZART"));
        artikliTable.setString("JM",qds.getString("JM"));
        artikliTable.setBigDecimal("ZC",qds.getBigDecimal("ZC"));
        artikliTable.setString("GOD",qds.getString("GOD"));
        if (qds.getString("VRDOK").equals("MEU") || qds.getString("VRDOK").equals("MES")){
          artikliTable.setString("CSKL","");
          artikliTable.setString("CSKLUL",qds.getString("CSKLUL"));
          artikliTable.setString("CSKLIZ",qds.getString("CSKLIZ"));
        } else {
          artikliTable.setString("CSKL",qds.getString("CSKL"));
          artikliTable.setString("CSKLUL","");
          artikliTable.setString("CSKLIZ","");
        }
        artikliTable.setString("VRDOK", qds.getString("VRDOK"));
        artikliTable.setInt("BRDOK", qds.getInt("BRDOK"));
        String tkal;

        if(!rpcsklIzl.getCSKL().equals("")) {
          tkal =  rpcskl.getCSKL()+"-"+
                  rpcsklIzl.getCSKL()+"-"+
                  qds.getString("VRDOK")+"-"+
                  qds.getInt("BRDOK");
        } else {
          tkal =  rpcsklIzl.getCSKL()+"-"+
                  qds.getString("VRDOK")+"-"+
                  qds.getInt("BRDOK");
        }
        artikliTable.setString("TKAL",tkal);
      }
    } while (qds.next());

    jptv.enableEvents(true);
    jptv.fireTableDataChanged();
    artikliTable.last();
    rcc.EnabDisabAll(rpcart,true);
    rpcart.setCART();
    tpane.setSelectedIndex(1);
//    tpane.setEnabledAt(0,false);
    rpcart.SetDefFocus();

  }

  public DataSet getQds() {
    return repSet;
  }

  private void editThisRow(){
    setEditDialog();
    editDijalog.setLocation(this.getJframe().getX()+35,
                            this.getJframe().getY()+(this.getJframe().getHeight()/2)-150);
    rpc2.setCART(jptv.getDataSet().getInt("CART"));

    editDijalog.setVisible(true);
  }

  XYLayout xyedit = new XYLayout();

  private void setEditDialog() throws NullPointerException {
    xyedit.setWidth(600);
    xyedit.setHeight(300);
//    contentPanel.setLayout(xyedit);
    rpc2.setMode("DOH");
    rpc2.setBorder(null);
    rpc2.remove(rpc2.jbCART);
    editDijalog.setModal(true);
    editDijalog.setResizable(false);

    editDijalog.setContentPane(contentPanel);

    editDijalog.setTitle("Izmjena vrijednosti");
    editDijalog.setSize(this.getJframe().getWidth(),300);
    editPanel.add(rpc2,new XYConstraints(0,0,-1,-1));
    editDijalog.getContentPane().add(editPanel, BorderLayout.CENTER);
    editDijalog.getContentPane().add(okp, BorderLayout.SOUTH);

//    editDijalog.getContentPane().add(editPanel, BorderLayout.CENTER);
//    editDijalog.getContentPane().add(okp, BorderLayout.SOUTH);
  }

  private void dispoz(){
//    editDijalog.setVisible(false);
    editDijalog.dispose();
  }
  
  /*

  public QueryDataSet getIspisCijenaPolicaSet(String cskl, String csklIz, String vrDok, int brDok, String godina) {
    QueryDataSet cartDS = new QueryDataSet();
    String strCart = "";
    String cartIn = "artikli.cart in(";
    String skl = "";
    String cskliz = "";
    if (vrDok.equals("MES") || vrDok.equals("MEU")) {
      skl = "csklul";
      strCart = "select distinct cart as cart, cskliz as cskliz from stmeskla where csklul='" + cskl + "' and cskliz='" + csklIz + "' and brdok=" + brDok + " and vrdok='" + vrDok + "' and god='" + godina + "'";
      cskliz = " '" + csklIz + "' as cskliz, ";
    } else {
      skl = "cskl";
      strCart = "select distinct cart as cart from stdoku where cskl='" + cskl + "' and brdok=" + brDok + " and vrdok='" + vrDok + "' and god='" + godina + "'";
    }
    cartDS.setQuery(new QueryDescriptor(dm.getDatabase1(), strCart));
    cartDS.open();

    if (cartDS.getRowCount() == 0) {
      return null;
    }
    
    HashMap carts = new HashMap();
    
    for (cartDS.first();cartDS.inBounds();cartDS.next()){
      carts.put(new Integer(cartDS.getInt("CART")),skl);
    }
    

    return null;
  }
  
  */

}
