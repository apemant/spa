/****license*****************************************************************
**   file: rapancart.java
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
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * Intervencije od strane mene (Sinisa)
 * 14.08.2001 * Pomaknuta polja na 150 po x, manje promjene velicine jlrNavFieldova
 *            * Dodano jrfCPOR
 *            * Dodana metoda getCPOR()
 *            * Dodana metoda getCART()
 *            * Dodan seter i geter moda rada
 *            * Promjenjena maska za kolicinu (tri decimale)
 * 14.09.2001 * Dodan seter i geter za searchable (da li da trazi po CART1 i BC)
 *            * Dodane metode getCART1, getBC, getNAZART, getCGRART
 * 30.10.2001 * Dodano jrfISB i metoda getISB()
 * 24.01.2002 * Dodano isUsluga() i sve vezano za usluge
 */
public class rapancart extends JPanel {
  java.math.BigDecimal nul = new java.math.BigDecimal(0);
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  allStanje AST =  allStanje.getallStanje();
  allPorezi alp = allPorezi.getallPorezi();
  java.math.BigDecimal Nula = new java.math.BigDecimal(0);
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private QueryDataSet Glupan = new QueryDataSet();
  private com.borland.dx.dataset.Column KOL = new com.borland.dx.dataset.Column("KOL","Koli\u010Dina na zalihi",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column REZKOL = new com.borland.dx.dataset.Column("REZKOL","Rezervirana kolièina",com.borland.dx.dataset.Variant.BIGDECIMAL);
  private com.borland.dx.dataset.Column TREKOL = new com.borland.dx.dataset.Column("TREKOL","Raspoloživa kolièina",com.borland.dx.dataset.Variant.BIGDECIMAL);
//  private com.borland.dx.dataset.Column CGRART = new com.borland.dx.dataset.Column("CGRART","Grupa artikla",com.borland.dx.dataset.Variant.STRING);
  private String queryString;
  private String godina;
  private String Cskl;
  private QueryDataSet tabela;
  private String tCartSifparam, nazivParam;
  private JComponent nextFocusabile;
  private char ulazIzlaz;
  private boolean enableUsluga = false;
//  private boolean allowUsluga = false;
  private boolean gotFocus = false;
  private boolean lUsluga = false;
  private boolean SerNum = false;
  private boolean enableChange = false;
  private boolean extraSklad = false;
  private boolean bPrikazKolicina = false;
//      frmParam.getParam("robno", "prikazKol", "N","Prikaz kolièina na rapancartu (D/N)").equalsIgnoreCase("D");

  private boolean myAfterLookupOnNavigate = true;
  private String tempNAZART = ""; // member varijabla dodana radi hendlamnja nove i stare usluge
                                  // najgluplje na svijetu = najefikasnije na svijetu
//  private raControlDocs rCD = new raControlDocs();
  private String csklart = null;
  private JPanel artPanel = new JPanel();

  XYLayout lay = new XYLayout();
  XYLayout xYLayoutDH = new XYLayout();
  JLabel jlCART = new JLabel();

  //// JraButton
  JraButton jbCART = new JraButton();
  JraButton jbSERNUM = new JraButton();
  JraButton jbDODTXT = new JraButton();
  boolean dodtxt = false;

  public JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull())
        Myafter_lookUp();
    }
    public void setEnabled(boolean enab) {
      if (!enab) gotFocus = false;
      super.setEnabled(enab);
    }
    protected void this_keyPressed(KeyEvent e) {
      super.this_keyPressed(e);
      checkKeys(e, "CART", super.getText());
    }
  };
  JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull())
        Myafter_lookUp();
    }
    protected void this_keyPressed(KeyEvent e) {
      super.this_keyPressed(e);
      checkKeys(e, "CART1", convertText(this));
    }
  };
  JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
      if (isLastLookSuccessfull())
        Myafter_lookUp();
    }
    protected void this_keyPressed(KeyEvent e) {
      super.this_keyPressed(e);
      checkKeys(e, "BC", convertText(this));
    }
  };

  JlrNavField jrfJM = new JlrNavField();
  JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp() {
//        gotFocus = false;
      if (isLastLookSuccessfull())
        Myafter_lookUp();
    }
    public void setText(String s) {
      if (!enableUsluga || gotFocus) super.setText(s);
    }
    protected void this_keyPressed(KeyEvent e) {
      super.this_keyPressed(e);
      checkKeys(e, "NAZART", super.getText());
    }
  };
  TitledBorder titledBorder1;
  JLabel jlKOLZAL = new JLabel();
  JraTextField jraKOL = new JraTextField();
  JraTextField jraREZKOL = new JraTextField();
  JraTextField jraTREKOL = new JraTextField();
//  JraTextField jrfCGRART = new JraTextField();

  JlrNavField jrfCGRART = new JlrNavField() {
    public void after_lookUp() {
        
    }
    protected void this_keyPressed(KeyEvent e) {
//      super.this_keyPressed(e);
      checkKeys(e, "CGRART", super.getText());
    }
  };

  JraTextField jrfVRART = new JraTextField();
  JLabel jLabelSirfa = new JLabel();
  JLabel jLabelSirfa1 = new JLabel();
  JLabel jLabelSirfa2 = new JLabel();
  JLabel jLabelSirfa3 = new JLabel();
  JlrNavField jrfCPOR = new JlrNavField() {
    public void after_lookUp() {
//      Myafter_lookUp();
    }
  };
  JlrNavField jrfISB = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };
  private String mode;
  JLabel jLabel1 = new JLabel();
  private boolean searchable;
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();

  XYLayout sklay = new XYLayout();
  JPanel jpSklad = new JPanel();
  JLabel jlSklad = new JLabel();
  JlrNavField jlrSklad = new JlrNavField() {
    public void setEnabled(boolean on) {
      super.setEnabled(on);
      if (!on && jlrSklad.getText().length() == 0 &&
          jlrNazSklad.getText().length() != 0)
        jlrNazSklad.setText("");
    }
    public void after_lookUp() {
      if (jlrSklad.isEnabled()) {
        setDefParam();
        setFocusInternal();
      }
    }
  };
  JlrNavField jlrNazSklad = new JlrNavField() {
    public void after_lookUp() {
      if (jlrSklad.isEnabled()) {
        setDefParam();
        setFocusInternal();
      }
    }
  };
  JraButton jbSklad = new JraButton();
  private void setupGlupan(){
    Glupan.close();
    KOL.setDisplayMask("###,###,##0.000");
    KOL.setDefault("0");
    REZKOL.setDisplayMask("###,###,##0.000");
    REZKOL.setDefault("0");
    TREKOL.setDisplayMask("###,###,##0.000");
    TREKOL.setDefault("0");
    Glupan.setColumns(new Column[] {KOL, REZKOL, TREKOL});
    Glupan.open();
    jraKOL.setFont(jraKOL.getFont().deriveFont(Font.BOLD));
    jraKOL.setColumnName("KOL");
    jraKOL.setDataSet(Glupan);
    jraREZKOL.setFont(jraREZKOL.getFont().deriveFont(Font.BOLD));
    jraREZKOL.setColumnName("REZKOL");
    jraREZKOL.setDataSet(Glupan);
    jraTREKOL.setFont(jraTREKOL.getFont().deriveFont(Font.BOLD));
    jraTREKOL.setColumnName("TREKOL");
    jraTREKOL.setDataSet(Glupan);
  }

  public QueryDataSet getRaDataSet(){
    return dm.getArtikli();
  }


  private void setupKolsOnDisplay(){

    artPanel.add(jlKOLZAL,   new XYConstraints(15, 93, -1, -1));
    artPanel.add(jraKOL,    new XYConstraints(150, 93, 130, -1));
    artPanel.add(jraREZKOL,    new XYConstraints(285, 93, 130, -1));
    artPanel.add(jraTREKOL,    new XYConstraints(420, 93, 130, -1));
    artPanel.add(jLabel2,     new XYConstraints(150, 75, 130, -1));
    artPanel.add(jLabel3,   new XYConstraints(285, 75, 130, -1));
    artPanel.add(jLabel4,     new XYConstraints(420, 75, 130, -1));
    rcc.setLabelLaF(jraKOL, false);
    rcc.setLabelLaF(jraREZKOL, false);
    rcc.setLabelLaF(jraTREKOL, false);
  }

  /**
   * Konstruktor
   */

  public rapancart() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }

  }
 /**
  * Ako ja i = 1 ne\u0107e se prikazivati koli\u010Dina na zalihi
  */
  public rapancart(int i) {
    if (i > 0) {
      Glupan.close();
      KOL.setDisplayMask("###,###,##0.000");
      KOL.setDefault("0");
      REZKOL.setDisplayMask("###,###,##0.000");
      REZKOL.setDefault("0");
      TREKOL.setDisplayMask("###,###,##0.000");
      TREKOL.setDefault("0");
      Glupan.setColumns(new Column[] {KOL, REZKOL, TREKOL});
      Glupan.open();
      if (i==2){
        setBPrikazKolicina(true);
      }
      try {
        jbInit();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void dodText(){
   if (!dodtxt) {
     jbDODTXT.setIcon(raImages.getImageIcon(raImages.IMGCOMPOSEMAIL));
     jbDODTXT.setToolTipText("Dodatni text");
     jbDODTXT.addActionListener(new java.awt.event.ActionListener() {
       public void actionPerformed(ActionEvent e) {
         jbDODTXT_actionPerformed(e);
       }
     });
     artPanel.add(jbDODTXT, new XYConstraints(609, 50, 21, 21));
   }
     dodtxt = true;
  }
  public void jbDODTXT_actionPerformed(ActionEvent e){

  }

  public void setSerNum(){
    SerNum=true;
    jbSERNUM.setText("...");
    jbSERNUM.setToolTipText("Serijski brojevi");
    jbSERNUM.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSERNUM_actionPerformed(e);
      }
    });
    artPanel.add(jbSERNUM, new XYConstraints(519, 80, 21, 21));
  }

  public void resetSerNum(){
    if (SerNum=false){
      this.remove(jbSERNUM);
      this.validate();
    }
    SerNum=false;
  }

  public boolean getSerNum(){
    return SerNum;
  }

  public void setnextFocusabile(JComponent jcp){
    nextFocusabile=jcp;
  }

  public void setReportMode() {
    jrfCART1.setSearchMode(JlrNavField.NULL, lookupData.TEXT);
    jrfBC.setSearchMode(JlrNavField.NULL, lookupData.TEXT);
  }

  public void setEntryMode() {
    jrfCART1.setSearchMode(3);
    jrfBC.setSearchMode(3);
  }

  public void normalFousabile(){
//    jrfCART.setNextFocusableComponent(jrfCART1);
//    jrfCART1.setNextFocusableComponent(jrfBC);
//    jrfBC.setNextFocusableComponent(jrfNAZART);
//    jrfNAZART.setNextFocusableComponent(jrfCART);
  }
/*
  public void abnormalFousabile(){
      rcc.setLabelLaF(nextFocusabile, true);
//      nextFocusabile.setEnabled(true);
      nextFocusabile.requestFocus();
  }
*/
  void jbInit() throws Exception {

    setDefParam();
    setupGlupan();
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"");
    artPanel.setLayout(xYLayoutDH);
    jlKOLZAL.setText("Kolièina");

    ////// DOHVAT ARTIKALA
    jbCART.setText("...");
    jbCART.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCART_actionPerformed(e);
      }
    });
    jlCART.setText("Artikl");
//    jrfCART.setNextFocusableComponent(jrfCART1);
    jrfCART.setColumnName("CART");
    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART","CPOR","ISB","VRART"});
//    jrfCART.setVisCols(new int[]{0,3,4});
    InitParam();
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART,jrfCPOR,jrfISB,jrfVRART});
//    jrfCART.setRaDataSet(dm.getArtikli());
    jrfCART.setRaDataSet(getRaDataSet());
//    jrfCART.setRaDataSet(hr.restart.baza.Artikli.getDataModule().getFilteredDataSet(""));
    jrfCART.getRaDataSet().open();

    jrfCART1.setColumnName("CART1");
    jrfCART1.setSearchMode(3);
    jrfCART1.setNavProperties(jrfCART);
    jrfCART1.setFocusLostOnShow(false);

    
    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setSearchMode(2);
    jrfCGRART.setNavProperties(jrfCART);    
    jrfCGRART.setFocusLostOnShow(false);
/*    
    dm.getGrupart().open();
    jrfCGRART.setRaDataSet(dm.getGrupart());
    jrfCGRART.setVisCols(new int[]{0,1,2});
*/    

    
    

    jrfBC.setColumnName("BC");
    jrfBC.setSearchMode(3);
    jrfBC.setNavProperties(jrfCART);
    jrfBC.setFocusLostOnShow(false);

        //// jrfNAZART

//    jrfNAZART.setNextFocusableComponent(jrfCART);
    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setNavProperties(jrfCART);
    jrfNAZART.setSearchMode(1); //3 za po\u010Detak naziva
    jrfNAZART.setFocusLostOnShow(false);

    jrfVRART.setColumnName("VRART");

    //// jrfJM
    jrfJM.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(1);
    jrfJM.setNavProperties(jrfCART);
    jrfJM.setFocusLostOnShow(false);

    this.setBorder(BorderFactory.createEtchedBorder());

    jLabelSirfa.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa.setText("Šifra");
    jLabelSirfa1.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa1.setText("Oznaka");
    jLabelSirfa2.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa2.setText("Barcode");
    jLabelSirfa3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa3.setText("Grupa");
    jrfCPOR.setFocusLostOnShow(false);
    jrfCPOR.setNavProperties(jrfCART);
    jrfCPOR.setSearchMode(-1);
    jrfCPOR.setColumnName("CPOR");
    jrfCPOR.setVisible(false);
    jrfCPOR.setFocusLostOnShow(false);

    jrfISB.setFocusLostOnShow(false);
    jrfISB.setNavProperties(jrfCART);
    jrfISB.setSearchMode(-1);
    jrfISB.setColumnName("ISB");
    jrfISB.setVisible(false);
    jrfISB.setFocusLostOnShow(false);
    jLabel1.setText("Naziv / Jm");

    xYLayoutDH.setWidth(645);
    if (bPrikazKolicina) {
      xYLayoutDH.setHeight(130);
    } else {
      xYLayoutDH.setHeight(90);
    }
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("Stvarna");
    jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel3.setText("Rezervirana");
    jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel4.setText("Trenutna");

    int size = Integer.parseInt(frmParam.getParam("robno","cartSize","0"));
    if (size > 0 ) {
      new raTextMask(jrfCART1,size,false,raTextMask.ALL | raTextMask.PLACEHOLDER).
          setMaskCharacter(frmParam.getParam("robno", "artMask", "#").charAt(0));
    }
    int sizeBC = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("robno","cartSizeBC","0"));
    if (sizeBC > 0) {
      new raTextMask(jrfBC,size,false,raTextMask.ALL | raTextMask.PLACEHOLDER).
          setMaskCharacter(frmParam.getParam("robno", "artMask", "#").charAt(0));
    }

    artPanel.add(jlCART,  new XYConstraints(15, 25, -1, -1));
    artPanel.add(jrfCART,  new XYConstraints(150, 25, 65, -1));
    artPanel.add(jrfCPOR, new XYConstraints(355, 80, 100, -1));
    artPanel.add(jrfISB, new XYConstraints(355, 80, 100, -1));
    artPanel.add(jbCART, new XYConstraints(609, 25, 21, 21));
    artPanel.add(jrfNAZART,         new XYConstraints(150, 50, 410, -1));
    artPanel.add(jLabel1,   new XYConstraints(15, 50, -1, -1));
    artPanel.add(jLabelSirfa, new XYConstraints(150, 8, 60, -1));
    artPanel.add(jrfCART1,  new XYConstraints(220, 25, 135, -1));
    artPanel.add(jrfBC,    new XYConstraints(360, 25, 135, -1));
    artPanel.add(jrfCGRART,   new XYConstraints(500, 25, 104, -1));
    artPanel.add(jrfJM,          new XYConstraints(564, 50, 40, -1));
    artPanel.add(jLabelSirfa1, new XYConstraints(220, 8, 130, -1));
    artPanel.add(jLabelSirfa2, new XYConstraints(360, 8, 130, -1));
    artPanel.add(jLabelSirfa3, new XYConstraints(500, 8, 100, -1));
    if (bPrikazKolicina) setupKolsOnDisplay();

    addListeners();

    artPanel.setBorder(null);

    this.setLayout(lay);
    lay.setWidth(xYLayoutDH.getWidth());
    lay.setHeight(xYLayoutDH.getHeight());
    this.add(artPanel, new XYConstraints(0,0,-1,-1));
System.out.println("layheight="+lay.getHeight());
//    initDummy();
  }
  
  public void setExtraSklad(String cskl) {
    csklart = cskl;
  }

  public void addSkladField(DataSet skladset) {
    extraSklad = true;

    jpSklad.setLayout(sklay);
    sklay.setWidth(lay.getWidth());
    sklay.setHeight(45);
    jlSklad.setText("Skladište");
    jlrSklad.setColumnName("CSKLART");
    jlrSklad.setDataSet(this.getTabela());
    jlrSklad.setNavColumnName("CSKL");
    jlrSklad.setColNames(new String[] {"NAZSKL"});
    jlrSklad.setVisCols(new int[]{0,1});
    jlrSklad.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSklad});
    jlrSklad.setSearchMode(0);
    jlrSklad.setRaDataSet(skladset);
    jlrSklad.setNavButton(jbSklad);

    jlrNazSklad.setNavColumnName("NAZSKL");
    jlrNazSklad.setNavProperties(jlrSklad);
    jlrNazSklad.setSearchMode(1);

    jpSklad.add(jlSklad, new XYConstraints(15, 15, -1, -1));
    jpSklad.add(jlrSklad, new XYConstraints(150, 15, 65, -1));
    jpSklad.add(jlrNazSklad, new XYConstraints(220, 15, 384, -1));
    jpSklad.add(jbSklad, new XYConstraints(609, 15, 21, 21));

    jlrSklad.getDataBinder().setEnableClearAll(true);
    this.remove(artPanel);
    this.add(jpSklad, new XYConstraints(0, 0, -1, -1));
    this.add(artPanel, new XYConstraints(0, sklay.getHeight(), -1, -1));
    lay.setHeight(xYLayoutDH.getHeight() + sklay.getHeight());
  }
  
  public int getTotalHeight() {
    return lay.getHeight() + 7;
  }

/**
 *  Inicjalizacijska rutina
 */

  public void InitRaPanCart(){
    hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F8);
    hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F6);
    InitRaOst();
  }

  public void InitParam(){
    if ("CART".equalsIgnoreCase(tCartSifparam)) jrfCART.setVisCols(new int[]{0,3,4});
    else if ("CART1".equalsIgnoreCase(tCartSifparam)) jrfCART.setVisCols(new int[]{1,3,4});
    else if ("BC".equalsIgnoreCase(tCartSifparam)) jrfCART.setVisCols(new int[]{2,3,4});
    else jrfCART.setVisCols(new int[]{0,3,4});
  }


  public void InitRaOst(){

//    if (getTabela().isOpen()) tempNAZART = this.getTabela().getString("NAZART");
    jrfCART.setDataSet(this.getTabela());
    jrfCART1.setDataSet(this.getTabela());
    jrfBC.setDataSet(this.getTabela());
    jrfNAZART.setDataSet(this.getTabela());
    jrfJM.setDataSet(this.getTabela());
    if (extraSklad) {
      jlrSklad.setDataSet(this.getTabela());
    }
    InitParam();
  }

  public void Clean(){
    Glupan.open();
    Glupan.setBigDecimal("KOL",Nula);
    Glupan.setBigDecimal("REZKOL",Nula);
    Glupan.setBigDecimal("TREKOL",Nula);
    this.jrfCGRART.setText("");

  }
  void jbCART_actionPerformed(java.awt.event.ActionEvent e){
    this.SetDefFocus();

    this.jrfCART.keyF9Pressed();
  }
  
  private boolean kolSkladScreen = false;
  
  public boolean isKolSkladScreen() {
    return kolSkladScreen ;
  }
  
  public void setKolSkladScreen(boolean _b) {
    kolSkladScreen = _b;
  }

  private BigDecimal getStanjeKOL(String god, String cskl, int cart) {
    if (isKolSkladScreen()) {
      return AST.findKOLSKLAD(god,cskl,cart);
    } else {
      return AST.findKOL(god,cskl,cart);
    }
  }
  Thread StanjeFinder = new Thread(new Runnable() {
      public void run() {
//    if (AST.findStanje(this.getGodina(),this.getCskl(),this.getTabela().getInt("CART")) &&
//      this.jrfCART.isValueChanged() && !jrfCART.getText().equals("")) {
        if (AST.findStanje(getGodina(),getCskl(),getTabela().getInt("CART")) &&
          !jrfCART.getText().equals("")) {
            AST.findStanjeUnconditional(getGodina(),getCskl(),getTabela().getInt("CART"));
            Glupan.open();
            Glupan.setBigDecimal("KOL",getStanjeKOL(getGodina(),getCskl(),getTabela().getInt("CART")));
            Glupan.setBigDecimal("REZKOL",AST.gettrenSTANJE().getBigDecimal("KOLREZ"));
            Glupan.setBigDecimal("TREKOL", hr.restart.robno.Util.getUtil().negateValue(Glupan.getBigDecimal("KOL"), Glupan.getBigDecimal("REZKOL")));
        }
        else if (!jrfCART.getText().equals("")){
          normalFousabile();
          Glupan.setBigDecimal("KOL",Nula);
          Glupan.setBigDecimal("REZKOL",Nula);
          Glupan.setBigDecimal("TREKOL",Nula);
        }
      } //end run
});//end thread

  void findStanje(){
    StanjeFinder.run();
  }

  public void findStanjeUnconditional(){
    AST.findStanjeUnconditional(getGodina(),getCskl(),getTabela().getInt("CART"));
    Glupan.open();
    Glupan.setBigDecimal("KOL",getStanjeKOL(getGodina(),getCskl(),getTabela().getInt("CART")));
    Glupan.setBigDecimal("REZKOL",AST.gettrenSTANJE().getBigDecimal("KOLREZ"));
    Glupan.setBigDecimal("TREKOL", hr.restart.robno.Util.getUtil().negateValue(Glupan.getBigDecimal("KOL"), Glupan.getBigDecimal("REZKOL")));	
  }

  private void Myafter_lookUp(){
//    System.out.println("MyAfter_lookUp");
//ST.prnc(jrfCART.getRaDataSet());
//System.out.println(jrfCART.getRaDataSet().getInt("CART")+"-"+
//                   jrfCART.getRaDataSet().getString("CART1")+"-"+
//                   jrfCART.getRaDataSet().getString("BC")+"-"+
//                   jrfCART.getRaDataSet().getString("NAZART"));
//System.out.println(((QueryDataSet)jrfCART.getRaDataSet()).getQuery().getQueryString());


//  	System.out.println("isUsluga() "+isUsluga());
//  	System.out.println("enableUsluga "+enableUsluga);
    if (!isMyAfterLookup()) return;
    if (!this.jrfCART.getText().trim().equals("")) {
      if (isUsluga() && !mode.trim().equals("DOH") && 
          (!enableUsluga || ulazIzlaz == 'U')) {
//        System.out.println("Usluga je a nije dozvoljena");
        this.jrfCART.setText("");
        if (ulazIzlaz == 'U') {
//          System.out.println("Trebao bi ispisati: Stavka usluge ne može se upisati na ulaznom dokumentu!");
          Aut.getAut().handleRpcErr(this,
            "Stavka usluge ne može se upisati na ulaznom dokumentu!");
        } else {
//          System.out.println("Trebao bi ispisati: Artikl usluge nije dopušten na ovom mjestu!");
          Aut.getAut().handleRpcErr(this,
            "Artikl usluge nije dopušten na ovom mjestu!");
        }
        return;
      }
      if (!mode.trim().equals("DOH") || extraSklad) {
        findStanje();
//        if (mode!="B")
//          this.abnormalFousabile();
      }

      if (mode!="B") {
//        if (gotFocus) getTabela().setString(jrfNAZART.getColumnName(), jrfUNAZART.getText());
        nextTofocus();
      }
//      if (isUsluga()) {    // Ako je usluga
//        enableNaziv(true);
//        lUsluga=true;
//        if(tempNAZART.trim().equals(""))
//          jrfNAZART.setDataSet(this.getTabela());
//        else {
//          tabela.setString("NAZART", tempNAZART);
//          jrfNAZART.setDataSet(this.getTabela());
//        }
//        jrfNAZART.setText(this.getTabela().getString("NAZART"));
//
//        ulazIzlaz='I';
//
//        if (this.ulazIzlaz=='U') {
//          JOptionPane.showConfirmDialog(null,"Stavka usluge ne može se upisati na ulaznom dokumentu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//          this.setCART();
//          return;
//        }
//      }
//      else {
//        enableNaziv(false);
//        lUsluga=false;
//      }
//        enableNaziv(false);      // ovo maknuti ako se ovo ispred dekomentira
//      if (this.getMode()=="I") {
//        jrfNAZART.setText(this.getTabela().getString("NAZART"));//-> tu je linija koda koju treba negdje smjestiti
//      }
      boolean grabFocusBack = gotFocus && isAllowNameChange();
      this.EnabDisab(false);
      if (!mode.trim().equals("DOH") || extraSklad) {
        checkKolColor();
      }
      metToDo_after_lookUp();
      if (grabFocusBack) jrfNAZART.requestFocusLater();
    }
  }
  
  private void checkKolColor() {
    if (enableUsluga && isUsluga()) return;
    DataRow art = lookupData.getlookupData().raLookup(getRaDataSet(), "CART", getCART());
    if (art != null) {
      BigDecimal kol = Glupan.getBigDecimal("KOL");
      BigDecimal sig = art.getBigDecimal("SIGKOL");
      BigDecimal min = art.getBigDecimal("MINKOL");
      if (kol.compareTo(min) <= 0)
        setForegroundColor(Color.red);
      else if (kol.compareTo(sig) <= 0)
        setForegroundColor(Color.yellow.darker().darker());
    }
  }
  
  public void setForegroundColor(Color col) {
    jrfCART.setDisabledTextColor(col);
    jrfCART1.setDisabledTextColor(col);
    jrfBC.setDisabledTextColor(col);
    jrfCGRART.setDisabledTextColor(col);
    jrfNAZART.setDisabledTextColor(col);
    jrfJM.setDisabledTextColor(col);
  }

  /**
   * Metoda koja se izvodi nakon pronalaženja artikla npr. služi za popunjavanje nekih dodatnih
   * komponenti kao sto mogu biti rabati, i tomu sli\u010Dno
   */
  public void metToDo_after_lookUp(){
  }

  /**
   * Geter varijable godina
   */

  public String getGodina(){
//System.out.println("godina= "+godina);
     return godina;
  }

  /**
   * Seter za varijablu godina.
   * Godina bi trebala biti zadnje dvije znamenke od godine kad se radi dokument
   * Npr 12.12.2003 --> godina="03"
   */

  public void setGodina(String godina){
    this.godina=godina;
  }

  /**
   * Geter varijable Cskl
   */

  public String getCskl(){
    return extraSklad ? this.getTabela().getString("CSKLART") : Cskl;
  }
  /**
   * Seter varijable Cskl
   */

  public void setCskl(String Cskl){
    this.Cskl=Cskl;
  }
  /**
   * Geter varijable Tabela
   */

  public QueryDataSet getTabela(){
    return tabela;
  }
  /**
   * Seter varijable Tabela. Tabela je QueryDataSet na koju se bandaju CART i NAZART. U pravilu
   * te su tablice stdoku i stdoki.
   */

  public void setTabela(QueryDataSet tabela) {
    this.tabela=tabela;
    enableUsluga = true;
//    enableUsluga = tabela != null && tabela.hasColumn("CART") != null &&
//      "stdoki".equalsIgnoreCase(tabela.hasColumn("CART").getTableName());
    if (tabela != null && tabela.hasColumn("CART") != null &&
      "stdoku".equalsIgnoreCase(tabela.hasColumn("CART").getTableName()))
      ulazIzlaz = 'U';
//    enableNaziv(enableUsluga);
  }
  /**
   * Seter varijable Param. Ako je param CART sifra se odabire prema cartu CART1=prema dodatnoj širfi
   * a BC zna\u010Di da \u0107e se odabirati prema barkodu
   */

  public void setParam(String param) {
    this.tCartSifparam=param;
  }

  public void setDefParam() {
    setParam(frmParam.getParam("robno","indiCart"));
    nazivParam = frmParam.getParam("robno","focusCart");
  }

  /**
   * Geter varijable Param
   */
  public String getParam(){
    return this.tCartSifparam;
  }
  public QueryDataSet gettrenStanje(){
    return AST.gettrenSTANJE();
  }
  public java.math.BigDecimal findKOL(){
    return AST.gettrenSTANJE().getBigDecimal("KOL");
  }
  public java.math.BigDecimal findNC(){
    return AST.gettrenSTANJE().getBigDecimal("NC");
  }
  public java.math.BigDecimal findVC(){
    return AST.gettrenSTANJE().getBigDecimal("VC");
  }
  public java.math.BigDecimal findMC(){
    return AST.gettrenSTANJE().getBigDecimal("MC");
  }
  public java.math.BigDecimal findZC(){
    return AST.gettrenSTANJE().getBigDecimal("ZC");
  }
  public String getCPOR() {
    return jrfCPOR.getText();
  }
  public String getISB() {
    return jrfISB.getText();
  }
  public String getCART() {
    return jrfCART.getText().trim();
  }

  public void clearFields() {
    this.Glupan.open();
    jrfCART.setText("");
    jrfCART.maskCheck();
    jrfCART.emptyTextFields();
    if (enableUsluga) {
      gotFocus = true;
      jrfNAZART.setText("");
      gotFocus = false;
    }
//    jrfCART.forceFocLost();
    this.Glupan.setBigDecimal("KOL", nul);
    this.Glupan.setBigDecimal("REZKOL", nul);
    this.Glupan.setBigDecimal("TREKOL", nul);
  }
  public void setCARTLater() {
    clearFields();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        SetDefFocus();
      }
    });
  }
/**
 * Briše podatke o artiklu
 */
  public void setCART() {
    clearFields();
    this.SetDefFocus();
  }

  public void setCART(int str) {
    System.err.println("in set cart");
    jrfCART.setText(new Integer(str).toString());
    if (enableUsluga) gotFocus = true;
    jrfCART.forceFocLost();
  }
  public void SetFocus(){
    this.Clean();
    setParametrizedNazivArtiklaSearchMode();
    if (enableUsluga) gotFocus = true;
    if (extraSklad && this.getTabela().getString("CSKLART").length() == 0 &&
        (csklart != null || raUser.getInstance().getDefSklad() != null)) {
      if (csklart != null) this.getTabela().setString("CSKLART", csklart);
      else this.getTabela().setString("CSKLART", raUser.getInstance().getDefSklad());
      jlrSklad.forceFocLost();
      return;
    }
    setFocusInternal();
  }
  private void setFocusInternal() {
    setParametrizedNazivArtiklaSearchMode();
    if (enableUsluga) gotFocus = true;
    if (nazivParam==null) nazivParam="SIFRA";
    
    if (nazivParam.equals("SIFRA")) {
      if (this.tCartSifparam.equals("CART"))
        jrfCART.requestFocusLater();
      else if (this.tCartSifparam.equals("CART1"))
        jrfCART1.requestFocusLater();
      else if (this.tCartSifparam.equals("BC"))
        jrfBC.requestFocusLater();
    }
    else if (nazivParam.equals("NAZIV"))
      jrfNAZART.requestFocusLater();
  }

  public void SetDefFocus(){
    setDefParam();
    SetFocus();
  }
  public void nextTofocus(){}

  public void EnabDisab(boolean istina){

    rcc.setLabelLaF(this.jlrSklad,istina);
    rcc.setLabelLaF(this.jlrNazSklad,istina);
    rcc.setLabelLaF(this.jbSklad,istina);
    rcc.setLabelLaF(this.jrfCART,istina);
    rcc.setLabelLaF(this.jrfCART1,istina);
    rcc.setLabelLaF(this.jrfBC,istina);
    rcc.setLabelLaF(this.jrfCGRART,istina);
    rcc.setLabelLaF(this.jbCART,istina);
    if (istina || !enableUsluga || 
        getCART().length() == 0 || !isAllowNameChange()) {
      rcc.setLabelLaF(this.jrfNAZART,istina);
    }
    rcc.setLabelLaF(this.jrfJM,false);
  }
 /**
  * Setiranje moda rada
  *  "DOH" - dohvat artikla, ne vracha stanje
  *  "I" - unos/novi
  *  "B" - spešl kejs za brauz
  */
  public void setMode(String newMode) {
    mode = newMode;
    if (mode.trim().equals("DOH")) {
      jlKOLZAL.setVisible(false);
      jLabel2.setVisible(false);
      jLabel3.setVisible(false);
      jLabel4.setVisible(false);
      jraKOL.setVisible(false);
      jraREZKOL.setVisible(false);
      jraTREKOL.setVisible(false);
//      jrfJM.setVisible(false);
//      this.setPreferredSize(new Dimension(645, extraSklad ? 100 + sklay.getWidth() : 100));
    }
    else {
      rcc.setLabelLaF(jraKOL, false);
      rcc.setLabelLaF(jraREZKOL, false);
      rcc.setLabelLaF(jraTREKOL, false);
//      rcc.setLabelLaF(jrfJM, false);
//      this.setPreferredSize(new Dimension(645, extraSklad ? 133 + sklay.getWidth() : 133));
    }
  }
  public String getMode() {
    return mode;
  }
  void jbSERNUM_actionPerformed(java.awt.event.ActionEvent e){
//    this.jrfCART.keyF9Pressed();
  }
  public void setSearchable(boolean newSearchable) {
//    searchable = newSearchable;
//    if (!searchable) {
//      jrfCART1.setSearchMode(1);
//      jrfBC.setSearchMode(1);
//    }
  }
  public boolean isSearchable() {
    return searchable;
  }
  public String getCART1() {
    String forreturn = jrfCART1.getText().trim();
    if (jrfCART1.getFieldMask() instanceof raTextMask) {
      forreturn = new VarStr(forreturn).remove(((raTextMask) jrfCART1.getFieldMask()).getMaskCharacter()).toString();
    }
    return forreturn;
  }
  public String getBC() {
    String forreturn = jrfBC.getText().trim();
    if (jrfBC.getFieldMask() instanceof raTextMask) {
      forreturn = new VarStr(forreturn).remove(((raTextMask) jrfCART1.getFieldMask()).getMaskCharacter()).toString();
    }
    return forreturn;
  }
  public String getNAZART() {
    return jrfNAZART.getText().trim();
  }
  public String getCGRART() {
    return this.jrfCGRART.getText().trim();
  }
  public void setUlazIzlaz(char ulazIzlaz) {
    this.ulazIzlaz = ulazIzlaz;
    //if (ulazIzlaz == 'U') enableUsluga = false;
    
//    System.out.println("enableUsluga 2="+enableUsluga);
  }
  public void setAllowUsluga(boolean allow) {
    enableUsluga = allow;
//System.out.println("enableUsluga ="+enableUsluga);    
  }
//  public char getUlazIzlaz() {
//    return ulazIzlaz;
//  }
  
  boolean isAllowNameChange() {
    return enableChange || Aus.isNumber(jrfCART.getText()) &&
           raVart.isVarnaziv(Aus.getNumber(jrfCART.getText()));
  }
  
  public void enableNameChange(boolean alwaysVar) {
    enableChange = alwaysVar;
  }
/**
 * Vraca da
 * @return
 */
  public boolean isUsluga() {
   
      // fix za uslugu na kraju popisa
    if (!jrfCART.isEnabled() && isShowing() && 
        jrfCART.getText().length() > 0 && jrfVRART.getText().length() == 0)
       jrfCART.forceFocLost();
//    try {
//      String _cart = (jrfCART.getDataSet() == null)?jrfCART.getText():String.valueOf(jrfCART.getDataSet().getInt("CART"));
//      hr.restart.util.lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", _cart);
//      vl.execSQL("select VRART from artikli where cart="+_cart);
//      vl.RezSet.open();
//      return (dm.getArtikli().getString("VRART").equals("U"));
      return raVart.isUsluga(jrfVRART.getText());
      /*return ("U".equalsIgnoreCase(jrfVRART.getText()) ||
          "T".equalsIgnoreCase(jrfVRART.getText()));*/ // usluga i tranzit se jednako ponašaju
//      return (jrfCART.getRaDataSet().getString("VRART").equalsIgnoreCase("U"));

//    } catch (Exception ex) {
//      ex.printStackTrace();
//      return false;
//    }
  }

  void addListeners() {

/*    jrfCART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCART_keyReleased(e);
      }
    });

    jrfCART1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCART1_keyReleased(e);
      }
    });

    jrfNAZART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfNAZART_keyReleased(e);
      }
    });

    jrfBC.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfBC_keyReleased(e);
      }
    });

    jrfCGRART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCGRART_keyReleased(e);
      }
    });*/
  }
// kopirano iz lookUP-a buu mene
  private String convertText(JlrNavField nav) {
      String text = nav.getText();
      if (nav.getFieldMask() instanceof hr.restart.swing.raTextMask) {
        hr.restart.swing.raTextMask mask = (hr.restart.swing.raTextMask) nav.getFieldMask();
        if (mask.isMasked()) text = text.replace(mask.getMaskCharacter(), ' ').trim();
      }
      return text;
  }

  void checkKeys(KeyEvent e, String field, String value) {
      final String ffield=field;
    if (e.getKeyCode() == e.VK_F8 && field.equals("CGRART")) {
        if (showGroupsF9()== null){
            e.consume();
            return;
        }
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
               if (keyF8Pressed(getCskl(),getTopLevelAncestor(),ffield, jrfCGRART.getText())==null){
                   return;
               }
               jrfCART.forceFocLost();
           } 
        });
        e.consume();
        return;
    }  else if (e.getModifiers()==e.CTRL_MASK && e.getKeyCode() == e.VK_F9 && field.equals("CGRART")) {
        showGroupsF9();
        e.consume();
        return;
    }   else if (e.getKeyCode() == e.VK_F9 && field.equals("CGRART")) {
//        if (value.equalsIgnoreCase("")) {
        if (showGroupsF9()== null){
            e.consume();
            return;
        }
//        }
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jrfCGRART.keyF9Pressed();
                jrfCART.forceFocLost();
            } 
        });
        e.consume();
        return;
    }
      
      
    if(e.getKeyCode() == e.VK_F8) {
//System.out.println("Normalni f8");        
      showStanje(field, value);
      e.consume();
    }
    if (extraSklad && e.getKeyCode() == e.VK_ESCAPE) {
        if (!jrfCART.isEnabled() && jrfNAZART.isEnabled()){
//System.out.println("jrfCART.isEnabled() && jrfNAZART.isEnabled() "+(jrfCART.isEnabled() && jrfNAZART.isEnabled()));            
           return;
        }
      clearFields();
      jlrSklad.setText("");
      jlrNazSklad.setText("");
      jlrSklad.requestFocusLater();
      e.consume();
    }
  }

/*  void jrfCART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8)
    {
      showStanje("CART",jrfCART.getText());
    }
  }
  void jrfCART1_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8)
    {
      showStanje("CART1",getText(jrfCART1));
    }
  }
  void jrfNAZART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("NAZART",jrfNAZART.getText());
    }
  }
  void jrfBC_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("BC",getText(jrfBC));
    }
  }
  void jrfCGRART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("CGRART",jrfCGRART.getText());
    }
  }
*/

  void showStanje(String polje,String value) {
    if (!mode.trim().equals("DOH") || extraSklad ||
        (getCskl() != null && getCskl().length() > 0)) {
      keyF8Pressed(this.getCskl(), this.getTopLevelAncestor(),polje,value);
      jrfCART.forceFocLost();
    }
  }

  void showGroupsF8(){
      
  }
  public String[] showGroupsF9(){
      
      dm.getGrupart().open();
      lookupData LD = lookupData.getlookupData();
//      LD.lupFrWidth = 750;

      String[] result = LD.lookUp(new java.awt.Frame(),dm.getGrupart(), 
                new String[] {"CGRART"}, new String[] {jrfCGRART.getText()},
                new int[] {0, 1,2 });
      if (result!=null) {
      LD.raLocate(dm.getGrupart(),"CGRART",result[0]);
      this.jrfCGRART.setText(dm.getGrupart().getString("CGRART"));
      }
      return result;
  }
  

  String[] keyF8Pressed(String cSkl, Container c,String polje,String value)
  {

    String[] result = Util.getUtil().showStanje(
        (Window) c, cSkl, getGodina(), polje, value);
    if (result != null ) {
      this.jrfCART.setText(result[1]); // bilo pa nije prolazilo!!!
      /*if (!LD.raLocate(stanjef8,polje,result[0])) {
          result=null;
      } else {
          this.jrfCART.setText(String.valueOf(stanjef8.getInt("CART")));
      }*/
    }
    return result;
//    jrfCART.forceFocLost();
  }


/*class F8Pressed
{
  String cSkl;
  Valid vl;
  //JPanel jp = new JPanel();
  JlrNavField jrfCART = new JlrNavField();
  hr.restart.baza.dM dm;
  Frame frame = null;
  Dialog dialog = null;
  lookupFrame lookUp = null;
  Container container = null;
  String[] result;

  F8Pressed( )
  {
    this.cSkl = cSkl;
    this.container = c;
    init();
  }

  void init()
  {
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();

    getStanje();
  }

  void getStanje()
  {
    String qStr = "select ARTIKLI.CART AS CART, ARTIKLI.CART1 AS CART1, ARTIKLI.BC AS BC, ARTIKLI.NAZART AS NAZART, STANJE.KOL AS KOL from ARTIKLI, STANJE"+
                  " where ARTIKLI.CART = STANJE.CART and STANJE.CSKL = "+cSkl;

    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[]
    {
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone()

    });

    vl.RezSet.open();
    vl.RezSet.setTableName("STANJAZADOHVATSAF8");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp((java.awt.Frame)container, vl.RezSet, new int[] {0, 1, 2, 3, 4});
    sysoutTEST ST = new sysoutTEST(false);
    if (result != null ) {
      ST.prn(result);
    } else {
      ST.prn("Stisni ok seljaèino");
    }

    rpc.jrfNAZART.setText(result[3]);
/*
    if (container instanceof java.awt.Frame)
    {
      frame = (java.awt.Frame)container;
      lookUp = lookupFrame.getLookupFrame(frame, vl.RezSet,  new int[] {0, 1, 2, 3, 4});
    }
    else
    {
      dialog = (java.awt.Dialog)container;
      lookUp = lookupFrame.getLookupFrame(dialog, vl.RezSet,  new int[] {0, 1, 2, 3, 4});
    }

    lookUp.ShowCenter();

  }*/
  public void setMyAfterLookupOnNavigate(boolean b) {
    myAfterLookupOnNavigate=b;
  }
  public boolean isMyAfterLookup() {
    return myAfterLookupOnNavigate||jrfCART.isEnabled();
  }
  public void enableNaziv() {
//    enableNaziv(isUsluga());
  }


  public String findCART(){
    return findCART("",false);
  }

  public String findCART(boolean podgrupe){
    return findCART("", podgrupe);
  }

  public String findCART(String tablica) {
    String joint = "";

    if (!this.getCART().trim().equals("")) {
      return joint+tablica.toUpperCase() +".CART="+this.getCART()+" ";
    } else {
      return "";
    }
  }

  /**
   * 23.07.2003. dodano od starane S.G.-a
   *
   * metoda koja vraæa dio sql-a koji se odnosi na dohvat artikala po onome sto je upisano u poljima rapancart-a.
   *
   * @param tablica - ime tablice za koju se radi join ako nije tablica ARTIKLI.
   * @param podgrupe - boolean koji odredjuje da li ce se ispisati i pripadajuce podgrupe ili ne. Preporuceno je handlanje u validaciji.
   * @return ovisno o popunjenosti polja rapancart-a <br>npr. za POTPUNO upisani CART [1231] ili CGRART [01] vraæa CART=1231 ili CGRART='01' (tj. CGRART in ('01','123','42',...) ako je boolean podgrupe = true i ako iste postoje)<br>
   * CART1, BC i NAZART vraca LIKE '####%'
   */

  public String findCART(String tablica, boolean podgrupe) {

    /** @todo Andrej je pametan, a ja ako nisam pitam Andreja :) */

    String joint = "";

    if (!tablica.equals("")){
      joint = tablica.toUpperCase() + ".CART = ARTIKLI.CART AND ";
    }

    if (!this.getCART().trim().equals("")) {
      return joint+"ARTIKLI.CART="+this.getCART()+" ";
    } else {
      if (!this.getCART1().trim().equals("")) {
        return joint+"ARTIKLI.CART1 LIKE '"+this.getCART1()+"%' ";
      }
      if (!this.getNAZART().trim().equals("")) {
        String ch = begNazart ? "" : "%";
        String nazArt = this.getNAZART();
        String nazartLower = nazArt.toLowerCase();
        String nazartUpper = nazArt.toUpperCase();
        String nazartProp = nazArt.substring(0,1).toUpperCase()+nazArt.substring(1).toLowerCase();
        return joint + "(ARTIKLI.NAZART LIKE '"+ch+nazartProp+"%' OR ARTIKLI.NAZART LIKE '"
            +ch+nazartLower+"%' OR ARTIKLI.NAZART LIKE '"+ch+nazartUpper+"%') ";
      }

      if (!this.getCGRART().trim().equals("")) {
        if (Aus.getDataTreeList(this.getCGRART(),dm.getGrupart(),"CGRART","CGRARTPRIP") == null || !podgrupe) return joint+"ARTIKLI.CGRART = '" + this.getCGRART()+"' ";
        return joint+"ARTIKLI."+Aus.getDataTreeList(this.getCGRART(),dm.getGrupart(),"CGRART","CGRARTPRIP")+" ";
      }

      if (!this.getBC().trim().equals("")) {
        return "ARTIKLI.BC LIKE '"+this.getBC()+"%' ";
      }
      return "";
    }
  }
  
  public String[] getInGroupsArray(boolean podgrupe){
   String[] grar = null;
   String joint = "";

     if (!this.getCGRART().trim().equals("")) {
       if (Aus.getDataTreeList(this.getCGRART(),dm.getGrupart(),"CGRART","CGRARTPRIP") == null || !podgrupe) {
         grar = new String[] {this.getCGRART()};
       } else {
         DataSet grset = Aus.getDataBranch(this.getCGRART(),dm.getGrupart(),"CGRART","CGRARTPRIP");
         grset.open();
         grar = new String[grset.getRowCount()];
         for (grset.first(); grset.inBounds(); grset.next()){
           System.out.println("grupa - " + grset.getString("CGRART")); //XDEBUG delete when no more needed
           grar[grset.row()] = grset.getString("CGRART");
         }
       }
     }
   return grar;
  }

  void enableNaziv(boolean tru) {
//    ovo je samo za usluge katkada  ....
//    System.out.println("enable fucki'n naziv: "+tru);
//    if (tru) {
//      jrfCART.setColNames(new String[] {"CART1","BC","JM","CGRART","CPOR","ISB"});
//      jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfUNAZART,jrfJM,jrfCGRART,jrfCPOR,jrfISB,jrfVRART});
//    }
//    else {
//      jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART","CPOR","ISB"});
//      jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART,jrfCPOR,jrfISB,jrfVRART});
//    }

//    jrfCART1.setNavProperties(jrfCART);
//    jrfCGRART.setNavProperties(jrfCART);
//    jrfBC.setNavProperties(jrfCART);
//    jrfNAZART.setNavProperties(jrfCART);

  }

//  private void VTtexthandler(){
//    rCD.
//
//  }
  private void handleExtraText() {
    raHandlerExtraArtText rhh = new raHandlerExtraArtText();
  }


  private boolean begNazart;
  private void setParametrizedNazivArtiklaSearchMode() {
    String t = frmParam.getParam("robno", "nazartDohvat", "S",
                 "Naèin dohvata artikla po nazivu (P - poèetak, S - sve)", true);
    begNazart = "P".equalsIgnoreCase(t);
    jrfNAZART.setSearchMode(JlrNavField.NULL, begNazart ? lookupData.TEXT : lookupData.TEXTAW);
  }

  // ab.f, setter za paljenje focus cycle roota.
  // setFocusCycleRoot(true) postize da fokus kruzno
  // ide po poljima rapancarta, bez onih ruznih
  // setNextFocusable berzerkarija

  private boolean focusCycle = false;

  public void setFocusCycleRoot(boolean focusCycle) {
    this.focusCycle = focusCycle;
  }

  public boolean isFocusCycleRoot() {
    return focusCycle && jrfCART.isEnabled();
  }
  public boolean isbPrikazKolicina() {
    return bPrikazKolicina;
  }
  public void setBPrikazKolicina(boolean bPrikazKolicina) {
    this.bPrikazKolicina = bPrikazKolicina;
  }

/**
 * @return Returns the extraSklad.
 */
public boolean isExtraSklad() {
	return extraSklad;
}
}

