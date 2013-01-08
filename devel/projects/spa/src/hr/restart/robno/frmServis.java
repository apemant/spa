/****license*****************************************************************
**   file: frmServis.java
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
import hr.restart.baza.Kupci;
import hr.restart.baza.Partneri;
import hr.restart.baza.RN;
import hr.restart.baza.RN_znacsub;
import hr.restart.baza.dM;
import hr.restart.gk.gkStatusColorModifier;
import hr.restart.rn.ServisMailer;
import hr.restart.rn.raSubjektColumnModifier;
import hr.restart.sisfun.Asql;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.raStatusColorModifier;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.VarStr;
import hr.restart.util.raAdditionalLookupFilter;
import hr.restart.util.raComboBox;
import hr.restart.util.raGlob;
import hr.restart.util.raImageUtil;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raLoader;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmServis extends frmRadniNalog {

  static frmServis frmRNL;

  public PreSelect pres = new PreSelect() {
    {
      installResetButton();
    }
    private boolean initialized;
    public void resetDefaults() {
      initialized = true;
      jcbStatus.setSelectedIndex(0);
      
      this.getSelRow().setString("CSKL", hr.restart.sisfun.raUser.getInstance().getDefCorg());
      jlrCorg.forceFocLost();
      this.getSelRow().setTimestamp("DATDOK-from",ut.getFirstDayOfMonth());
//      this.getSelRow().setTimestamp("DATDOK-from",rut.
//        findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
      this.getSelRow().setTimestamp("DATDOK-to",vl.getToday());

      if (jlrVrsub.getRaDataSet().getRowCount() == 1) {
        this.getSelRow().setShort("CVRSUBJ",jlrVrsub.getRaDataSet().getShort("CVRSUBJ"));
        jlrVrsub.forceFocLost();
      }
    }
    public void SetFokus() {
      oldPreVrsub = (short) -1;
      if (!initialized) resetDefaults();
      this.getSelRow().setString("SERPR", "S");
      jcbStatus.setSelectedIndex(jcbStatus.getSelectedIndex());
      updateVrsub(jlrVrsub.getText() != null && jlrVrsub.getText().length() > 0);
      
      if (jlrVrsub.getText().length() > 0) {
        jlrPreSub.requestFocusLater();
      } else {
        jlrVrsub.requestFocusLater();
      }
//      this.getSelRow().post();
    }
    public boolean Validacija() {
//      System.out.println("|"+jlrVrsub.getText()+"|"+getSelRow().getShort("CVRSUBJ")+"|");
      if (jlrVrsub.getText().equals("") || this.getSelRow().getShort("CVRSUBJ") == 0)
        noVrsub = true;
      else
        noVrsub = false;
      return true;
    }
  };
  JPanel jpPres = new JPanel();
  XYLayout xYLayout5 = new XYLayout();
  JlrNavField jlrVrsub = new JlrNavField() {
    public void after_lookUp() {
      afterVrsub(isLastLookSuccessfull());
    }
  };
  JlrNavField jlrNazvrsub = new JlrNavField() {
    public void after_lookUp() {
      afterVrsub(isLastLookSuccessfull());
    }
  };
  JraButton jbVrsub = new JraButton();
  JLabel jlVrsub = new JLabel();
  
  JlrNavField jlrPreSub = new JlrNavField();
  JlrNavField jlrNazSub = new JlrNavField();
  JraButton jbPreSub = new JraButton();
  JLabel jlPreSub = new JLabel();

  rajpBrDok jpMasterHeader = new rajpBrDok();
  JPanel jpMasterMain = new JPanel();
  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpVlasnik = new JPanel();
  JPanel jpSubjekt = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();

  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNazorg = new JlrNavField();
  JraButton jbSelCorg = new JraButton();
  
  JraButton jbImage = new JraButton();
  JraButton jbsImage = new JraButton();

  raComboBox rcbVlasnik = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (this.getSelectedIndex() == 0) kupSelected();
      else parSelected();
    }
  };
//  JLabel jlVlasnik = new JLabel();
  JlrNavField jlrVlasnik = new JlrNavField() {
    public void keyF9Pressed() {
      if (jlrVlasnik.getText().trim().length() >0 && Aus.isDigit(jlrVlasnik.getText()))
        super.keyF9Pressed();
      else {
        String last = jlrVlasnik.getText();
        jlrNazVlasnik.setText(last);
        jlrNazVlasnik.keyF9Pressed();
        if (!jlrVlasnik.getText().equals(last))
          jlrVlasnik.forceFocLost();
      }
    }
    public void after_lookUp() {
//      if (isLastLookSuccessfull()) {
        setVlasnik();
//      }
    }
  };
  JlrNavField jlrNazVlasnik = new JlrNavField();

  raNavAction potob, hist, makerac, chgdat, mailstat;

  JraButton jbSelVlasnik = new JraButton();
  JraButton jbSelSubjekt = new JraButton();
  //JraTextField jraVrsub = new JraTextField();
  JraTextField jraSerpr = new JraTextField();

  JlrNavField jlrSubjekt = new JlrNavField() {
    public void keyF9Pressed() {
      jlrSubjekt.setAdditionalLookupFilter(vlasnikFilter);
      try {
        super.keyF9Pressed();
      } finally {
        jlrSubjekt.setAdditionalLookupFilter(null);
      }
    }
    public void after_lookUp() {
      if (isLastLookSuccessfull()) {
//        System.out.println("after lookup jlrSubjekt");

//        System.out.println("here boy "+jraVrsub.getText());
//        System.out.println(dm.getRN_subjekt().getString("CSUBrN"));
        DataRow dat = ld.raLookup(jlrSubjekt.getRaDataSet(), "CSUBRN", jlrSubjekt.getText()); 
        getMasterSet().setShort("CVRSUBJ", dat.getShort("CVRSUBJ"));
//        System.out.println("vrdok "+getMasterSet().getShort("CVRSUBJ")+" old "+oldVrsub);
        subj.setVrsub(getMasterSet().getShort("CVRSUBJ"));
        /*if (noVrsub && oldVrsub != getMasterSet().getShort("CVRSUBJ"))
          subj.setVrsub(oldVrsub = getMasterSet().getShort("CVRSUBJ"));*/

        if (raMaster.getMode() != 'N' && !subWasEmpty)
          subj.setCradnal(getMasterSet().getString("CRADNAL"));
        else subj.setCradnal("");
        subj.editSubjekt(jlrSubjekt.getText());
        setSubjekt();
      } else clearSubjekt();
    }
  };

  JLabel jlSubjekt = new JLabel();
  JLabel jlIme = new JLabel();
  JLabel jlAdresa = new JLabel();
  JLabel jlTel = new JLabel();
  JLabel jlImeT = new JLabel();
  JLabel jlAdrT = new JLabel();
  JLabel jlTelT = new JLabel();
  JLabel jlRadovi = new JLabel();
  JraButton jbSelRadovi = new JraButton();
  JTextArea jtaRadovi = new JTextArea();
  JLabel jLabel1 = new JLabel();
  JraTextField jraDatum = new JraTextField();
  JLabel jlOsig = new JLabel();
  JlrNavField jlrOsig = new JlrNavField();
  JLabel jlNapom = new JLabel();
  JraTextField jraNapom = new JraTextField();
  JLabel jlNapomInt = new JLabel();
  JraTextField jraNapomInt = new JraTextField();
  JLabel jlServiser = new JLabel();
  JlrNavField jlrSer = new JlrNavField();
  JlrNavField jlrSerIme = new JlrNavField();
  JlrNavField jlrSerPrezime = new JlrNavField();
  JraButton jbSelSer = new JraButton();
  JraCheckBox jcbZamjena = new JraCheckBox();
  
  JLabel jlNapomene = new JLabel();
  JlrNavField jlrNap1 = new JlrNavField();
  JlrNavField jlrNap2 = new JlrNavField();
  JlrNavField jlrNaznap1 = new JlrNavField();
  JlrNavField jlrNaznap2 = new JlrNavField();
  JraCheckBox jcbGaranc = new JraCheckBox();
  JraButton jbSelNap1 = new JraButton();
  JraButton jbSelNap2 = new JraButton();
  JraScrollPane scroller = new JraScrollPane(jtaRadovi);
  JScrollBar hs = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };

  JPanel podaci = new JPanel();
  XYLayout xYLayoutp = new XYLayout();
  JraScrollPane sc2 = new JraScrollPane(jpSubjekt);
  JScrollBar hs2 = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };
  JScrollBar vs2 = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      super.setEnabled(true);
    }
  };

  frmVlasnik vlas;
  dlgSubjektHandler subj = new dlgSubjektHandler();
  
  raImageUtil riu;

  String[] key = new String[] {"CSKL", "VRDOK", "GOD", "BRDOK"};
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  JlrNavField jlrRadovi = new JlrNavField() {
    public void keyF9Pressed() {
      if (Aus.isDigit(jlrRadovi.getText()))
        super.keyF9Pressed();
      else {
        String last = jlrRadovi.getText();
        jlrTekst.setText(last);
        jlrTekst.keyF9Pressed();
        if (!jlrRadovi.getText().equals(last))
          jlrRadovi.forceFocLost();
      }
    }

    public void after_lookUp() {
      if (raMaster.getMode() != 'B' && isLastLookSuccessfull()) {
        String CSKUPART = jraNorm.getText();
        ld.raLocate(jlrRadovi.getRaDataSet(), "CTEKST", jlrRadovi.getText());
        String TEKST = jlrRadovi.getRaDataSet().getString("TEKST");
        String old = jtaRadovi.getText();
        if (TEKST.equalsIgnoreCase(old) || old.indexOf(TEKST) >= 0 ||
            TEKST.indexOf(old) >= 0) jtaRadovi.setText(TEKST);
        else jtaRadovi.setText(old+TEKST);
        if (raMaster.getMode() == 'N') {
          jlrNorm.setText(CSKUPART);
          jlrNorm.forceFocLost();
        }
      }
/*      } else {
        jtaRadovi.setText("");
        jlrNorm.setText("");
        jlrNorm.forceFocLost();
      } */
    }
  };
  JlrNavField jlrTekst = new JlrNavField();
  JlrNavField jlrNazosig = new JlrNavField();
  JraButton jbSelOsig = new JraButton();
  JraTextField jraNorm = new JraTextField();
  JlrNavField jlrNorm = new JlrNavField();
  JlrNavField jlrNaznorm = new JlrNavField();
  JraButton jbSelNorm = new JraButton();
  JLabel jlNorm = new JLabel();
//  JLabel jlArtikl = new JLabel();
  JLabel jlKol = new JLabel();

  protected rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
      if (!rpcLostFocus && raDetail.getMode() == 'N') {
        rpcLostFocus = true;
        rpcOut();
      };
    }
  };
  
  raSubjektColumnModifier subMod = new raSubjektColumnModifier();

/*  JlrNavField jlrArtikl = new JlrNavField() {
    public void after_lookUp() {

        String myself = this.getColumnName();
        String CART = jraCART.getText();
        String CART1 = jraCART1.getText();
        String BC = jraBC.getText();
        String NAZART = jraArtikl.getText();
        String JM = jraJmj.getText();
        if (raDetail.getMode() != 'N')
          NAZART = getDetailSet().getString("NAZART");
        if (!CART.equals("") && !myself.equals("CART"))
          getDetailSet().setInt("CART", Integer.valueOf(CART).intValue());
        if (!myself.equals("CART1"))
          getDetailSet().setString("CART1", CART1);
        if (!myself.equals("BC"))
          getDetailSet().setString("BC", BC);
        getDetailSet().setString("NAZART", NAZART);
        getDetailSet().setString("JM", JM);

    }
  }; */
  JraTextField jraKol = new JraTextField();
/*  JraTextField jraArtikl = new JraTextField();
  JraButton jbSelArt = new JraButton(); */
//  short oldVrsub;
  int oldCart;
/*  JraTextField jraJmj = new JraTextField();
  JraTextField jraCART = new JraTextField();
  JraTextField jraCART1 = new JraTextField();
  JraTextField jraBC = new JraTextField();*/
  JLabel jlStatus = new JLabel();
  raComboBox jcbStatus = new raComboBox();
  JLabel jlDatum = new JLabel();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();
//  JraTextField jraStatus = new JraTextField();
  boolean noVrsub = false, rpcLostFocus;
  boolean forcePar = false;
  boolean lazyIZD = false;
  boolean allowKP = false;

  QueryDataSet repQDSprijava = new QueryDataSet();
  QueryDataSet repQDSdetails = new QueryDataSet() {
    public void saveChanges() {}
    public void refresh() {}
    public boolean saveChangesSupported() {
      return false;
    }
  };
  QueryDataSet repQDSnorm = new QueryDataSet();

  String vlasnikIme, vlasnikAdr, vlasnikMJ, vlasnikPBR, vlasnikTel, vlasnikMatBr;

  String lastcradnal = "";
  VarStr atrNames = new VarStr(128);
  VarStr atrValues = new VarStr(128);
  String vrsub;
  int nspace = 0, nlf = 0;

//  String cosigur, nazosigur;

  public frmServis() {
    try {
      frmRNL = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmServis getInstance() {
    return frmRNL;
  }

  public QueryDataSet getrepQDSprijava() {
    return repQDSprijava;
  }

  public QueryDataSet getrepQDSdetails() {
    return repQDSdetails;
  }

  public QueryDataSet getrepQDSnorm() {
    return repQDSnorm;
  }

  private boolean isKupac() {
    return getMasterSet().getString("KUPPAR").equalsIgnoreCase("K");
  }

  public QueryDataSet getDetailsFor(String cradnal) {
    if (!ld.raLocate(this.getMasterSet(), "CRADNAL", cradnal)) return null;
    super.refilterDetailSet();
    refilterStavkeRNU();
    return repQDSdetails;
  }

  private String findGen() {
    String nom = subj.getVrsubName().trim().toLowerCase();
    String[][] chg = new String[][] {
      {"*ol", "*olova"},
      {"*ka", "*aka"},
      {"*[oa]", "*[]a"},
      {"*?", "*?a"}
    };
    for (int i = 0; i < chg.length; i++) {
      raGlob g = new raGlob(chg[i][0]);
      if (g.matches(nom))
        return g.morphLastMatch(chg[i][1]);
    }
    return nom;
  }

  private void setTitle() {
    VarStr title = new VarStr();
    if (status.equals("P")) title.append("Otvoreni radni");
    else if (status.equals("O")) title.append("Obra\u0111eni radni");
    else if (status.equals("Z")) title.append("Zatvoreni radni");
    else title.append("Radni");
    if (noVrsub) title.append(" nalozi za servis  od ");
    else title.append(" nalozi za servis ").append(findGen()).append("  od ");
    title.append(rdu.dataFormatter(dfrom)).append(" do ").append(rdu.dataFormatter(dto));
    setNaslovMaster(title.toString());
  }

  public void beforeShowMaster() {
    copyPreselectValues();
    subMod.setDisplayVrsub(noVrsub && frmParam.getParam("rn", "singleVrsub", "N", 
        "Postoji li samo jedna vrsta subjekta (D/N)?").equalsIgnoreCase("N"));
    lastcradnal = "";
    //getMasterSet().refresh();
    //Aut.getAut().getNorme();refresh();
//    Aut.getAut().getUsluge().refresh();
    if (!noVrsub) {
      subj.setVrsub(pres.getSelRow().getShort("CVRSUBJ"));
      jlSubjekt.setText(subj.getVrsubName());
    } else {
      //removeSubjektFilter();
      jlSubjekt.setText("Subjekt");
    }
    setTitle();
    checkMasterActions();
    this.getDetailSet().open();
    
    forcePar = frmParam.getParam("rn", "forcePar", "N",
        "Forsirati partnera na radnim nalozima za servis (D,N)?").equalsIgnoreCase("D");
    lazyIZD = frmParam.getParam("robno", "lazyIZD", "N", "Dopustiti prebacivanje" +
      " radnih naloga u raèune prije izdavanja robe? (D,N)").equalsIgnoreCase("D");
    
    allowKP = !frmParam.getParam("zapod", "parToKup", "N", "Dodati/brisati " +
            "slog kupca kod unosa/brisanja partnera (D,N,A)?").equalsIgnoreCase("N");
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        checkMasterActions();
      }
    });
  }

  public void checkMasterActions() {
    String stat = getMasterSet().getString("STATUS");
    boolean enabde = stat.equalsIgnoreCase("P");
    boolean isf = stat.equalsIgnoreCase("F") || stat.equalsIgnoreCase("Z");
    //potob.setEnabled(!getMasterSet().getString("STATUS").equalsIgnoreCase("Z"));
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(enabde);
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(enabde);
    potob.setEnabled(getMasterSet().rowCount() > 0);
    makerac.setEnabled(getMasterSet().rowCount() > 0 && !isf && (lazyIZD || !enabde));
    hist.setEnabled(getMasterSet().rowCount() > 0);
    chgdat.setEnabled(getMasterSet().rowCount() > 0 && 
        (raUser.getInstance().isSuper() || raUser.getInstance().isTest()));
    mailstat.setEnabled(getMasterSet().rowCount() > 0);
  }

  /*private void removeSubjektFilter() {
    oldVrsub = 0;
    RN_subjekt.getDataModule().setFilter("");
//    dm.getRN_subjekt().close();
//    dm.getRN_subjekt().setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
//      hr.restart.baza.dM.getDataModule().getDatabase1(),
//      vl.getNoWhereQuery(dm.getRN_subjekt())
//    ));

    dm.getRN_subjekt().open();
  }*/

  protected void setIzmjenaEntry() {
    if (!getMasterSet().getString("STATUS").equalsIgnoreCase("P")) {
      rcc.setLabelLaF(rcbVlasnik, false);
      rcc.setLabelLaF(jlrVlasnik, false);
      rcc.setLabelLaF(jbSelVlasnik, false);
      rcc.setLabelLaF(jraDatum, false);
      rcc.setLabelLaF(jlrRadovi, false);
      rcc.setLabelLaF(jbSelRadovi, false);
    }
    if (this.getMasterSet().getString("CSUBRN").length() > 0) {
      //rcc.setLabelLaF(jbSelSubjekt, false);
      rcc.setLabelLaF(jlrSubjekt, false);
    }
    rcc.setLabelLaF(jlrNorm, false);
    rcc.setLabelLaF(jlrNaznorm, false);
    rcc.setLabelLaF(jbSelNorm, false);
    jtaRadovi.setText(this.getMasterSet().getString("OPIS"));
  }

  boolean subWasEmpty = false;
  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
//      getMasterSet().setString("KUPPAR", "K");
      jtaRadovi.setText("");
      rcbVlasnik.this_itemStateChanged();
      jlrVlasnik.requestFocus();
      /*subCradnal = "("+raUser.getInstance().getUser()+"):"+
        System.currentTimeMillis();
      if (subCradnal.length() > 30) subCradnal = subCradnal.substring(0, 30);
      Valid.getValid().runSQL("DELETE FROM RN_znacsub WHERE CRADNAL LIKE '("+
          raUser.getInstance().getUser()+"):%'");*/
      if (pres.getSelRow().getString("CSUBRN").trim().length() > 0) {
        jlrSubjekt.setText(pres.getSelRow().getString("CSUBRN").trim());
        jlrSubjekt.forceFocLost();
      } else clearSubjekt();
      if (!noVrsub)
        getMasterSet().setShort("CVRSUBJ", pres.getSelRow().getShort("CVRSUBJ"));
      updateVrsub(!noVrsub);
    } else {
      jpMasterHeader.SetDefTextDOK(mode);
      jtaRadovi.setText(this.getMasterSet().getString("OPIS"));
      if (mode == 'I') {
        jlrOsig.requestFocus();
      }
      //subCradnal = getMasterSet().getString("CRADNAL");
    }
    subWasEmpty = mode == 'I' && getMasterSet().getString("CSUBRN").length() == 0;
    if (mode == 'N') rcc.setLabelLaF(jbImage, false);
    else rcc.setLabelLaF(jbImage,getMasterSet().getString("SLIKA").equalsIgnoreCase("D"));
    rcc.setLabelLaF(jbsImage, mode != 'B');
    img = null;
    //System.out.println("subCradnal="+subCradnal);
  }

  public boolean ValidacijaMaster(char mode) {
    System.out.println(getMasterSet());
    System.out.println(pres.getSelRow());
    if (vl.isEmpty(jlrVlasnik) || // vl.isEmpty(jlrSubjekt) ||
        vl.isEmpty(jraDatum))
      return false;
    String opis = jtaRadovi.getText().substring(0, Math.min(200, jtaRadovi.getText().length()));
    if (getMasterSet().getString("CSUBRN").length() == 0)
      getMasterSet().setShort("CVRSUBJ", pres.getSelRow().getShort("CVRSUBJ"));
    this.getMasterSet().setString("OPIS", opis);
    if (mode == 'N')
      ValidacijaNoviMaster();
    
    if (img != null) getMasterSet().setString("SLIKA", "D");
    return true;
  }
  
  String delSub; 
  boolean hadImg;
  public boolean DeleteCheckMaster() {
    delSub = getMasterSet().getString("CRADNAL");
    hadImg = getMasterSet().getString("SLIKA").equalsIgnoreCase("D");
    return super.DeleteCheckMaster();
  }
  
  public boolean doWithSaveMaster(char mode) {
    if (mode == 'B') {
      try {
        raTransaction.runSQL("DELETE FROM RN_znacsub WHERE cradnal='" + delSub + "'");
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return super.doWithSaveMaster(mode);
  }
  
  public void AfterDeleteMaster() {
    if (hadImg) riu.deleteImage(delSub.replace('-', '_').replace('/', '_'));
  }
  
  private boolean doMark = false; 
  //boolean refreshZnac;
  public boolean doBeforeSaveMaster(char mode) {
    //refreshZnac = false;
    if (!super.doBeforeSaveMaster(mode)) return false;
    if (getMasterSet().getString("CSUBRN").length() > 0 && (mode == 'N' || subWasEmpty)) {
      try {
        /*String upd = "UPDATE RN_znacsub set cradnal ='" +
          getMasterSet().getString("CRADNAL") + "' WHERE cvrsubj = "+
          getMasterSet().getShort("CVRSUBJ") + " AND csubrn = '"+
          getMasterSet().getString("CSUBRN") + "' and cradnal = '" + subCradnal + "'";*/
        
        //if (mode == 'N') {
          /*String upd = "UPDATE RN_znacsub set cradnal ='" +
          getMasterSet().getString("CRADNAL") + "' WHERE cradnal = '" + subCradnal + "'";
          
          System.out.println("servis doBefore: "+upd);
          raTransaction.runSQL(upd);*/
        //}
        /*if (RN_znacsub.getDataModule().getRowCount(Condition.whereAllEqual(
             new String[] {"CVRSUBJ", "CSUBRN", "CRADNAL"}, getMasterSet())) == 0) {*/
        if (RN_znacsub.getDataModule().getRowCount(Condition.equal(
            "CRADNAL", getMasterSet())) == 0) { 
          String add = "INSERT INTO RN_znacsub SELECT csubrn, cvrsubj, cznac, vriznac, '" +
                   getMasterSet().getString("CRADNAL")+"' as cradnal FROM RN_znacsub "+
                   "WHERE cradnal = '' AND cvrsubj = "+getMasterSet().getShort("CVRSUBJ")+
                   " AND csubrn = '"+getMasterSet().getString("CSUBRN")+"'";
          System.out.println("servis doBefore: "+add);
          raTransaction.runSQL(add);
        }
        doMark = true;
        //dM.getSynchronizer().markAsDirty("RN_znacsub");
        //refreshZnac = true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
//  long now;
  
  /*public void AfterAfterSaveMaster(char mode) {
    super.AfterAfterSaveMaster(mode);
    if (refreshZnac) dm.getRN_znacsub().refresh();
  }*/

  public void refilterDetailSet() {
//    now = System.currentTimeMillis();
//    System.out.println("begin " + (System.currentTimeMillis() - now));
    super.refilterDetailSet();
//    System.out.println("after super " + (System.currentTimeMillis() - now));
    String status = this.getMasterSet().getString("STATUS");
    String what = "Predvi\u0111ene stavke ";
    if (status.equals("O")) what = "Stavke obra\u0111enog ";
    if (status.equals("Z")) what = "Stavke zatvorenog ";
    if (!this.getMasterSet().getString("CRADNAL").equals(lastcradnal)) {
      refilterStavkeRNU();
      lastcradnal = this.getMasterSet().getString("CRADNAL");
    }
    if (status.equals("P"))
      this.raDetail.enableAdd();
    else this.raDetail.disableAdd();
    setNaslovDetail(what+"radnog naloga "+this.getMasterSet().getString("CRADNAL"));
    refreshDetails();
    rpc.setGodina(vl.findYear(getMasterSet().getTimestamp("DATDOK")));
    //sys.prn(Aut.getAut().expandArts(this.getDetailSet()));
  }

  public void beforeShowDetail() {
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,11});
//    jlrArtikl.setColumnName(Aut.getAut().getCARTdependable("CART","CART1","BC"));
//    jlrArtikl.setVisCols(new int[] {Aut.getAut().getCARTdependable(0,1,2),3});
//    setNaslovDetail("Predvi\u0111ene stavke radnog naloga "+this.getMasterSet().getString("CRADNAL"));
    if (!this.getMasterSet().getString("CRADNAL").equals(lastcradnal)) {
      refilterStavkeRNU();
      lastcradnal = this.getMasterSet().getString("CRADNAL");
    }
    checkActions();
  }

  private void refilterStavkeRNU() {
    long now = System.currentTimeMillis();
//    System.out.println("begin RNU refilter " + (System.currentTimeMillis() - now));
    repQDSdetails.emptyAllRows();
    QueryDataSet stavke = Asql.getArtikliNaloga(this.getMasterSet().getString("CRADNAL"));
//    System.out.println("after vl.execSQL " + (System.currentTimeMillis() - now));

    repQDSdetails.enableDataSetEvents(false);
    for (stavke.first(); stavke.inBounds(); stavke.next()) {
      repQDSdetails.insertRow(false);
      stavke.copyTo(repQDSdetails);
      repQDSdetails.post();
    }
    repQDSdetails.enableDataSetEvents(true);
//    System.out.println("after copy IZD " + (System.currentTimeMillis() - now));
    generateStavkeRNU();
//    System.out.println("generate " + (System.currentTimeMillis() - now));
//    repQDSdetails.saveChanges();
  }

  private void copyArt(DataSet ds, short rbr, BigDecimal kol) {
    boolean osk = false, art = false;
//    System.out.println("kopiram");
    QueryDataSet temp = null;
    repQDSdetails.insertRow(false);
    initNewDetail(repQDSdetails);
    repQDSdetails.setString("VRDOK", "RNL");
    Aut.getAut().copyArtFields(repQDSdetails, ds);
    repQDSdetails.setBigDecimal("KOL", kol);
    repQDSdetails.setString("CRADNAL", this.getMasterSet().getString("CRADNAL"));
    repQDSdetails.setShort("RBR", rbr);

    // cijene
    if (this.getMasterSet().getInt("CPAR") > 0)
      osk = ld.raLocate(dm.getCjenik(), new String[] {"CPAR", "CART"},
            new String[] {""+this.getMasterSet().getInt("CPAR"), ""+ds.getInt("CART")});

//    if (!osk)
//      osk = ld.raLocate(dm.getCjenik(), new String[] {"CPAR", "CART"},
//            new String[] {""+this.getMasterSet().getInt("CKUPAC"), ""+ds.getInt("CART")});

    if (osk) temp = dm.getCjenik();
    else art = ld.raLocate(dm.getArtikli(), "CART", String.valueOf(ds.getInt("CART")));
    if (art) temp = dm.getArtikli();

    if (temp != null) {
      repQDSdetails.setBigDecimal("VC", temp.getBigDecimal("VC"));
      repQDSdetails.setBigDecimal("MC", temp.getBigDecimal("MC"));
      repQDSdetails.setBigDecimal("IBP", kol.multiply(temp.getBigDecimal("VC")));
      repQDSdetails.setBigDecimal("ISP", kol.multiply(temp.getBigDecimal("MC")));
    } else {
    }

    repQDSdetails.post();
  }

  private void generateStavkeRNU() {
    short rbr = 0;
//    System.out.println("begin RNU generate " + (System.currentTimeMillis() - now));
    dm.getCjenik().open();
    dm.getArtikli().open();

//    System.out.println("after opening " + (System.currentTimeMillis() - now));
    // ekspandaj sve artikle iz stavki ovog radnog naloga
    int row = getDetailSet().getRow();
    raDetail.getJpTableView().enableEvents(false);
    DataSet ds = Aut.getAut().expandArts(this.getDetailSet(), true);
//    System.out.println("after expanding " + (System.currentTimeMillis() - now));
    // prepiši samo artikle usluge u dataset usluga
    for (ds.first(); ds.inBounds(); ds.next())
      if (!raVart.isStanje(ds.getInt("CART")))
          //Aut.getAut().artTipa(ds.getInt("CART"), "U"))
        copyArt(ds, ++rbr, ds.getBigDecimal("KOL"));
    getDetailSet().goToRow(row);
    raDetail.getJpTableView().enableEvents(true);
//    System.out.println("after copying " + (System.currentTimeMillis() - now));
  }

  private void refreshDetails() {
    // refreshaj ekran sa detaljima stavki radnog naloga ako je uop\u0107e u\u010Ditan
//    System.out.println("begin refresh details " + (System.currentTimeMillis() - now));
    if (raLoader.isLoaderLoaded("hr.restart.robno.frmStavkeRadnogNaloga")) {
      frmStavkeRadnogNaloga.getInstance().getJpTableView().fireTableDataChanged();
      frmStavkeRadnogNaloga.getInstance().setTitle("Detaljni prikaz usluga i materijala radnog naloga "+this.getMasterSet().getString("CRADNAL"));
    }
//    System.out.println("END " + (System.currentTimeMillis() - now));
  }
  
  public void EntryPointMaster(char mode) {
    super.EntryPointMaster(mode);
    if (forcePar) {
      if (mode == 'N') {
        rcbVlasnik.setSelectedIndex(1);
        getMasterSet().setString("KUPPAR", "P");
      } 
      if (getMasterSet().getString("KUPPAR").equalsIgnoreCase("P"))
        rcbVlasnik.setEnabled(false);
    }
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      rpcLostFocus = false;
      rcc.setLabelLaF(jraKol, false);
    }
    if (mode == 'I') {
      rpc.EnabDisab(false);
//      rcc.setLabelLaF(jlrArtikl, false);
//      rcc.setLabelLaF(jbSelArt, false);
    }
//    rcc.setLabelLaF(jraJmj, false);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      initNewDetail(this.getDetailSet());
      rpc.setCART();
//      jlrArtikl.requestFocus();
    } else if (mode == 'I') {
//      jraArtikl.requestFocus();
      jraKol.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (rpc.getCART().equals("")) {
//      EraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
//      EraseFields();
      return false;
    }
    if (/*vl.isEmpty(jlrArtikl) ||*/ vl.isEmpty(jraKol))
      return false;
    if (mode == 'N')
      ValidacijaNoviDetail();
    return true;
  }
  
  public void AfterAfterSaveMaster(char mode) {
    if (frmParam.getParam("rn", "servisStavkeGo", "D","Ulazi li nakon snimanja zaglavlja servisnog RN u stavke").equalsIgnoreCase("D")) {
      super.AfterAfterSaveMaster(mode);
    }
    if (doMark) {
      doMark = false;
      dM.getSynchronizer().markAsDirty("RN_znacsub");
    }
    if (img != null) saveImage();
    ServisMailer.sendMailRN(getMasterSet());
  }

  public void AfterDeleteDetail() {

//    Aut.getAut().recalculateRbr(this, "RBR", oldRbr);
    refilterStavkeRNU();
    refreshDetails();
    lastcradnal = this.getMasterSet().getString("CRADNAL");
  }

  public void AfterSaveDetail(char mode) {
    refilterStavkeRNU();
    refreshDetails();
    if (mode == 'N') {
      rpcLostFocus = false;
      rcc.setLabelLaF(jraKol, false);
      rpc.EnabDisab(true);
    }
    lastcradnal = this.getMasterSet().getString("CRADNAL");
  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode == 'N' && rpcLostFocus) {
      rpc.EnabDisab(true);
      rpcLostFocus = false;
      rcc.setLabelLaF(jraKol, false);
      rpc.setCART();
      return false;
    }
    return true;
  }

  public boolean rpcOut() {
    rcc.setLabelLaF(jraKol, true);

    jraKol.requestFocus();
    return true;
  }

  private String oldVlasnik;
  private String oldSubjekt;

  private void fisp() {
    String qstr = "SELECT * FROM RN where cradnal = '"+this.getMasterSet().getString("CRADNAL")+"'";
    repQDSprijava.close();
    repQDSprijava.closeStatement();
    repQDSprijava.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    repQDSprijava.open();

    String qstr2 = "SELECT * FROM stdoki WHERE vrdok = 'RNL' AND cradnal = '"+this.getMasterSet().getString("CRADNAL")+"'";
    repQDSnorm.close();
    repQDSnorm.closeStatement();
    repQDSnorm.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr2));
    repQDSnorm.open();

    super.refilterDetailSet();
    refilterStavkeRNU();

    oldVlasnik = "";
    checkOldVlasnik(repQDSprijava);
/*    jlrVlasnik.setText("" + this.getMasterSet().getInt("CKUPAC"));
    jlrVlasnik.forceFocLost();
*/    oldSubjekt = "";
		checkOldSubjekt(repQDSprijava);
    /*jlrSubjekt.setText(this.getMasterSet().getString("CSUBRN"));
    jlrSubjekt.forceFocLost();*/
  }

  public void Funkcija_ispisa_master() {
    fisp();
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail() {
    fisp();
    super.Funkcija_ispisa_detail();
  }

  private int scale;

  private void checkOldSubjekt(DataSet rn) {
    if (!oldSubjekt.equals(rn.getString("CSUBRN") + "|" + rn.getString("CRADNAL"))) {
      oldSubjekt = rn.getString("CSUBRN") + "|" + rn.getString("CRADNAL");
      /*  no no no
      jlrSubjekt.setText(subjekt);
      jlrSubjekt.forceFocLost();*/
      subj.setVrsub(rn.getShort("CVRSUBJ"));
      subj.setCradnal(rn.getString("CRADNAL"));
      subj.editSubjekt(rn.getString("CSUBRN"));
      vrsub = subj.getVrsubName();
      
      atrNames.clear();
      atrValues.clear();
      for (int j = 0; j < nlf; j++) {
        atrNames.append("\n");
        atrValues.append("\n");
      }
      
      String s_n = subj.getSBText();
      scale = s_n == null ? 0 : s_n.length() + 4;
      if (s_n != null && s_n.length() > 0) {
        atrNames.append(s_n).append(Aus.spc(nspace)).append('\n');
        atrValues.append(Aus.spc(nspace)).append(subj.getBroj()).append('\n');
      }
      for (int i = 0; i < subj.getFieldCount(); i++) {
        if(!subj.getFieldValue(i).equals("")){
          atrNames.append(subj.getFieldName(i));

          if((subj.getFieldName(i).length()+4)>scale) {
            scale = subj.getFieldName(i).length()+4;
          }
          atrNames.append(Aus.spc(nspace)).append("\n");
          atrValues.append(Aus.spc(nspace));
          atrValues.append(subj.getFieldValueFormatted(i)).append("\n");
        }
      }
    }
  }

  public String getNazvrsubj(DataSet rn) {
    checkOldSubjekt(rn);
    return vrsub;
  }

  public String getAtrNames(DataSet rn, int numspace, int numlf) {
    if (nspace != numspace || nlf != numlf)
      oldSubjekt = "";
    nspace = numspace;
    nlf = numlf;
    checkOldSubjekt(rn);
    return atrNames.toString();
  }

  public String getAtrValues(DataSet rn, int numspace, int numlf) {
    if (nspace != numspace || nlf != numlf)
      oldSubjekt = "";
    nspace = numspace;
    nlf = numlf;
    checkOldSubjekt(rn);
    return atrValues.toString();
  }

  public int getScaled(){
    return scale*97;
  }

  private void checkOldVlasnik(DataSet rn) {
    if (!oldVlasnik.equals(rn.getString("KUPPAR") + rn.getInt("CKUPAC"))) {
      oldVlasnik = rn.getString("KUPPAR") + rn.getInt("CKUPAC");
      /*  no no no no
      jlrVlasnik.setText("" + vlasnik);
      jlrVlasnik.forceFocLost();*/
      
      if (rn.getString("KUPPAR").equalsIgnoreCase("K")) {
        if (!ld.raLocate(dm.getKupci(), "CKUPAC", rn.getInt("CKUPAC") + "")) {
        	vlasnikIme = vlasnikAdr = vlasnikMJ = vlasnikPBR = vlasnikTel = vlasnikMatBr = "";
          return;
        }
        vlasnikIme = dm.getKupci().getString("IME")+" "+dm.getKupci().getString("PREZIME");
        vlasnikAdr = dm.getKupci().getString("ADR");
        vlasnikMJ = dm.getKupci().getString("MJ");
        vlasnikPBR = String.valueOf(dm.getKupci().getInt("PBR"));
        vlasnikTel = dm.getKupci().getString("TEL");
        vlasnikMatBr = dm.getKupci().getString("OIB");
      } else {
        if (!ld.raLocate(dm.getPartneri(), "CPAR", rn.getInt("CKUPAC") + "")) {
        	vlasnikIme = vlasnikAdr = vlasnikMJ = vlasnikPBR = vlasnikTel = vlasnikMatBr = "";
          return;
        }
        vlasnikIme = dm.getPartneri().getString("NAZPAR");
        vlasnikAdr = dm.getPartneri().getString("ADR");
        vlasnikMJ = dm.getPartneri().getString("MJ");
        vlasnikPBR = String.valueOf(dm.getPartneri().getInt("PBR"));
        vlasnikTel = dm.getPartneri().getString("TEL");
        vlasnikMatBr = dm.getPartneri().getString("MB");
      }
    }
  }

  public String getVlasnikIme(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikIme;
  }

  public String getVlasnikAdr(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikAdr;
  }

  public String getVlasnikMJ(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikMJ;
  }

  public String getVlasnikPBR(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikPBR;
  }

  public String getVlasnikTel(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikTel;
  }

  public String getVlasnikMatBr(DataSet rn) {
    checkOldVlasnik(rn);
    return vlasnikMatBr;
  }

  Variant shared = new Variant();
  raAdditionalLookupFilter vlasnikFilter = new raAdditionalLookupFilter() {
    public boolean isRow(ReadRow row) {
      if (noVrsub || vlasnikCol == null || vlasnikCol.length() == 0 ||
          jlrVlasnik.getText().trim().length() == 0) return true;
//      row.getVariant(vlasnikCol, shared);
      return jlrVlasnik.getText().trim().equals(row.getString(vlasnikCol));
    }
  };
  
  private void jbInit() throws Exception {
    
    Aus.setFilter(dm.getRNser(), "SELECT * FROM RN WHERE 1=0");
    dm.getRNser().open();

    header = jpMasterHeader;

    repQDSdetails.setColumns(dm.getStdoki().cloneColumns());
    repQDSdetails.open();
    
    riu = new raImageUtil();

    this.setMasterSet(dm.getRNser());
    this.setNaslovMaster("Radni Nalozi");
    this.setMasterKey(Util.mkey);
    this.setVisibleColsMaster(new int[] {3,4,5,10});

    this.setDetailSet(dm.getStRnlSer());
    this.setNaslovDetail("Stavke radnog naloga");
    this.setDetailKey(Util.dkey);
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,11});

    this.raMaster.getRepRunner().addReport("hr.restart.robno.repRadniNalog","hr.restart.robno.repRadniNalog","RadniNalog","Ispis prijave radnog naloga");
    //this.raMaster.getRepRunner().addReport("hr.restart.robno.repRadniNalog2/1","hr.restart.robno.repRadniNalog","PrijavaDvaNaJedan","Ispis prijave radnog naloga 2/1");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repRadniNalogMask","hr.restart.robno.repRadniNalog","EmptyNalog","Ispis maske radnog naloga za ruèni unos podataka");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga","hr.restart.robno.repObracunRadnogNaloga2","ObracunRadnogNaloga","Ispis obra\u0111enog radnog naloga");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga2","hr.restart.robno.repObracunRadnogNaloga2","ObracunRadnogNaloga2","Ispis obra\u0111enog radnog naloga sa cijenama");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repStavkeRadnogNaloga","hr.restart.robno.repStavkeRadnogNaloga","StavkeRadnogNaloga","Ispis stavki radnog naloga");

    this.raDetail.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga","hr.restart.robno.repObracunRadnogNaloga2","ObracunRadnogNaloga","Ispis obra\u0111enog radnog naloga");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga2","hr.restart.robno.repObracunRadnogNaloga2","ObracunRadnogNaloga2","Ispis obra\u0111enog radnog naloga sa cijenama");
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repStavkeRadnogNaloga","hr.restart.robno.repStavkeRadnogNaloga","StavkeRadnogNaloga","Ispis stavki radnog naloga");

    dm.getRN_tekstovi().open();
    dm.getRN_vrsub().open();
    dm.getArtikli().open();
    dm.getStRnlSer().open();

    raMaster.removeRnvCopyCurr();
    raDetail.removeRnvCopyCurr();

    this.setMasterDeleteMode(DELDETAIL);

    xYLayout1.setWidth(675);
    xYLayout1.setHeight(505);
    jpMaster.setLayout(xYLayout1);
    jpVlasnik.setBorder(BorderFactory.createEtchedBorder());
    jpVlasnik.setLayout(xYLayout3);
    //jpSubjekt.setBorder(BorderFactory.createEtchedBorder());
    jpSubjekt.setLayout(xYLayout2);
    rcbVlasnik.setRaColumn("KUPPAR");
    rcbVlasnik.setRaDataSet(this.getMasterSet());
    rcbVlasnik.setRaItems(new String[][] {
      {"Kupac - vlasnik", "K"},
      {"Partner - naruèitelj", "P"}
    });
//    jlVlasnik.setText("Vlasnik");

    jlSubjekt.setText("Subjekt");
    jlIme.setText("Ime i prezime");
    jlAdresa.setText("Adresa");
    jlTel.setText("Telefon");
/*    jlImeT.setHorizontalAlignment(SwingConstants.TRAILING);
    jlAdrT.setHorizontalAlignment(SwingConstants.TRAILING);
    jlTelT.setHorizontalAlignment(SwingConstants.TRAILING); */


    jlRadovi.setText("Naru\u010Deni radovi");

    jLabel1.setText("Datum Otvaranja");
    jlOsig.setText("Osiguravatelj");
    jlNapomene.setText("Napomene");
    jlNorm.setText("Skupina");
    jbSelVlasnik.setText("...");
    jbSelSubjekt.setText("...");
    /*
    jbSelRadovi.setText("...");
    jbSelNap1.setText("...");
    jbSelNap2.setText("...");
    jbSelOsig.setText("...");
    jbSelNorm.setText("...");
    jbVrsub.setText("...");
    */
//    jbSelArt.setText("...");
    jlVrsub.setText("Vrsta subjekta");

    jlrVlasnik.setColumnName("CKUPAC");
    jlrVlasnik.setSearchMode(0);
    jlrVlasnik.setTextFields(new JTextComponent[] {jlrNazVlasnik});
    jlrVlasnik.setColNames(new String[] {"IME"});
    jlrVlasnik.setDataSet(this.getMasterSet());
    jlrVlasnik.setRaDataSet(dm.getKupci());
    jlrVlasnik.setVisCols(new int[] {0,1,2,4});
    jlrVlasnik.removeFieldMask();

    jlrNazVlasnik.setColumnName("IME");
    jlrNazVlasnik.setTextFields(new JTextComponent[] {jlrVlasnik});
    jlrNazVlasnik.setColNames(new String[] {"CKUPAC"});
    jlrNazVlasnik.setRaDataSet(dm.getKupci());
    jlrNazVlasnik.setVisCols(new int[] {0,1,2,4});
    jlrNazVlasnik.setSearchMode(1);
    jlrNazVlasnik.setVisible(false);

//    jlrVlasnik.setFocusLostOnShow(false);

    jlrSubjekt.setColumnName("CSUBRN");
    jlrSubjekt.setSearchMode(3);
    //jlrSubjekt.setTextFields(new JTextComponent[] {jraVrsub});
    //jlrSubjekt.setColNames(new String[] {"CVRSUBJ"});
    jlrSubjekt.setDataSet(this.getMasterSet());
    jlrSubjekt.setRaDataSet(dm.getRN_subjekt());
    jlrSubjekt.setVisCols(new int[] {0,1,2});
    jlrSubjekt.setSearchMode(3);

    jlrRadovi.setSearchMode(0);
    jlrRadovi.setColumnName("CTEKST");
    jlrRadovi.setTextFields(new JTextComponent[] {jlrTekst, jraNorm});
    jlrRadovi.setColNames(new String[] {"TEKST", "CSKUPART"});
    jlrRadovi.setDataSet(this.getMasterSet());
    jlrRadovi.setRaDataSet(dm.getRN_tekstovi());
    jlrRadovi.setVisCols(new int[] {0,2});
    jlrRadovi.setNavButton(jbSelRadovi);
    jlrRadovi.removeFieldMask();

    jlrTekst.setNavProperties(jlrRadovi);
    jlrTekst.setColumnName("TEKST");
    jlrTekst.setSearchMode(1);
    jlrTekst.setVisible(false);
    jraNorm.setColumnName("CSKUPART");
    jraNorm.setVisible(false);

    jlrOsig.setColumnName("CPAR");
    jlrOsig.setTextFields(new JTextComponent[] {jlrNazosig});
    jlrOsig.setColNames(new String[] {"NAZPAR"});
    jlrOsig.setSearchMode(0);
    jlrOsig.setDataSet(this.getMasterSet());
    jlrOsig.setRaDataSet(dm.getPartneri());
    jlrOsig.setVisCols(new int[] {0,1});
    jlrOsig.setNavButton(jbSelOsig);

    jlrNazosig.setSearchMode(1);
    jlrNazosig.setNavProperties(jlrOsig);
    jlrNazosig.setColumnName("NAZPAR");

    jlrNorm.setColumnName("CSKUPART");
    jlrNorm.setTextFields(new JTextComponent[] {jlrNaznorm});
    jlrNorm.setColNames(new String[] {"NAZSKUPART"});
    jlrNorm.setSearchMode(0);
    jlrNorm.setDataSet(this.getMasterSet());
    jlrNorm.setRaDataSet(Aut.getAut().getNorme());
    jlrNorm.setVisCols(new int[] {0,1});
    jlrNorm.setNavButton(jbSelNorm);

    jlrNaznorm.setColumnName("NAZSKUPART");
    jlrNaznorm.setNavProperties(jlrNorm);
    jlrNaznorm.setSearchMode(1);

    jlrNap1.setColumnName("CNAP1");
    jlrNap1.setNavColumnName("CNAP");
    jlrNap1.setTextFields(new JTextComponent[] {jlrNaznap1});
    jlrNap1.setColNames(new String[] {"NAZNAP"});
    jlrNap1.setSearchMode(0);
    jlrNap1.setDataSet(this.getMasterSet());
    jlrNap1.setRaDataSet(dm.getNapomene());
    jlrNap1.setVisCols(new int[] {0,1});
    jlrNap1.setNavButton(jbSelNap1);

    jlrNaznap1.setColumnName("NAZNAP");
    jlrNaznap1.setNavProperties(jlrNap1);
    jlrNaznap1.setSearchMode(1);

    jlrNap2.setColumnName("CNAP2");
    jlrNap2.setNavColumnName("CNAP");
    jlrNap2.setTextFields(new JTextComponent[] {jlrNaznap2});
    jlrNap2.setColNames(new String[] {"NAZNAP"});
    jlrNap2.setSearchMode(0);
    jlrNap2.setDataSet(this.getMasterSet());
    jlrNap2.setRaDataSet(dm.getNapomene());
    jlrNap2.setVisCols(new int[] {0,1});
    jlrNap2.setNavButton(jbSelNap2);

    jlrNaznap2.setColumnName("NAZNAP");
    jlrNaznap2.setNavProperties(jlrNap2);
    jlrNaznap2.setSearchMode(1);
    
    jlNapom.setText("Dodatna napomena");
    jraNapom.setColumnName("NAPOM");
    jraNapom.setDataSet(this.getMasterSet());
    jlNapomInt.setText("Interna napomena");
    jraNapomInt.setColumnName("NAPOMI");
    jraNapomInt.setDataSet(this.getMasterSet());

    jraDatum.setColumnName("DATDOK");
    jraDatum.setDataSet(this.getMasterSet());
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
//    jraDatum.setNextFocusableComponent(jlrOsig);

    jpDetail.setLayout(xYLayout4);
    xYLayout4.setWidth(575);
    xYLayout4.setHeight(60);

    jtaRadovi.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    scroller.setBorder(BorderFactory.createLoweredBevelBorder());
    scroller.setHorizontalScrollBar(hs);
    scroller.setVerticalScrollBar(vs);

    sc2.setHorizontalScrollBar(hs2);
    sc2.setVerticalScrollBar(vs2);
    podaci.setLayout(xYLayoutp);

    jpPres.setLayout(xYLayout5);
    xYLayout5.setHeight(150);
    xYLayout5.setWidth(531);
    jlrVrsub.setHorizontalAlignment(SwingConstants.TRAILING);
    
    jbImage.setText("");
    jbImage.setIcon(raImages.getImageIcon(raImages.IMGMOVIE));
    jbImage.setToolTipText("Povezana slika");
    jbImage.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showImage();
      }
    });
    
    jbsImage.setText("...");
    jbsImage.setToolTipText("Odabir slike");
    jbsImage.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        attachImage();
      }
    });

    jraSerpr.setColumnName("SERPR");
    jraSerpr.setEnabled(false);
    jraSerpr.setVisible(false);

    jlCorg.setText("Org. jedinica");
    jlrCorg.setColumnName("CSKL");
    jlrCorg.setNavColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setColumnName("NAZIV");
    jlrNazorg.setNavProperties(jlrCorg);

    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);

    jlrNazvrsub.setSearchMode(1);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setNavProperties(jlrVrsub);
    
    jlPreSub.setText("Subjekt");
    jlrPreSub.setColumnName("CSUBRN");
    jlrPreSub.setColNames(new String[] {"BROJ"});
    jlrPreSub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSub});
    jlrPreSub.setVisCols(new int[] {0,1});
    jlrPreSub.setSearchMode(3);
    jlrPreSub.setRaDataSet(dm.getRN_subjekt());
    jlrPreSub.setNavButton(jbPreSub);

    jlrNazSub.setSearchMode(1);
    jlrNazSub.setColumnName("BROJ");
    jlrNazSub.setNavProperties(jlrPreSub);

    jcbStatus.setRaDataSet(this.getMasterSet());
    jcbStatus.setRaColumn("STATUS");
    jcbStatus.setRaItems(new String[][] {{" ",""},
        {"Prijavljen","P"},{"Obra\u0111en","O"},
        {"Fakturiran","F"}, {"Zatvoren","Z"}});
//    jraStatus.setColumnName("STATUS");
//    jraStatus.setVisible(false);
    jraDatumOd.setColumnName("DATDOK");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setColumnName("DATDOK");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);

//    jlArtikl.setText("Usluga / proizvod");
    jlKol.setText("Koli\u010Dina");

//    jlrArtikl.setRaDataSet(Aut.getAut().getUsluge());
//    jlrArtikl.setDataSet(this.getDetailSet());
//    jlrArtikl.setSearchMode(0);
//    jlrArtikl.setTextFields(new javax.swing.text.JTextComponent[] {jraCART,jraCART1,jraBC,jraArtikl,jraJmj});
//    jlrArtikl.setColNames(new String[] {"CART","CART1","BC","NAZART","JM"});
    /*jlrArtikl.setColumnName("CART");
    jlrArtikl.setVisCols(new int[] {0,3});*/
//    jlrArtikl.setNavButton(jbSelArt);

    jcbZamjena.setText(" Izdan zamjenski artikl ");
    jcbZamjena.setDataSet(this.getMasterSet());
    jcbZamjena.setSelectedDataValue("D");
    jcbZamjena.setUnselectedDataValue("N");
    jcbZamjena.setColumnName("ZAMJENA");
    jcbZamjena.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbZamjena.setHorizontalTextPosition(SwingConstants.LEADING);
    
    jlServiser.setText("Serviser");
    jlrSer.setColumnName("CRADNIK");
    jlrSer.setDataSet(this.getMasterSet());
    jlrSer.setRaDataSet(dm.getRadnici());
    jlrSer.setColNames(new String[] {"IME", "PREZIME"});
    jlrSer.setTextFields(new JTextComponent[] {jlrSerIme, jlrSerPrezime});
    jlrSer.setVisCols(new int[] {0, 1, 2});
    jlrSer.setSearchMode(0);
    jlrSer.setNavButton(jbSelSer);

    jlrSerIme.setColumnName("IME");
    jlrSerIme.setNavProperties(jlrSer);
    jlrSerIme.setSearchMode(1);
    jlrSerPrezime.setColumnName("PREZIME");
    jlrSerPrezime.setNavProperties(jlrSer);
    jlrSerPrezime.setSearchMode(1);

    jcbGaranc.setText(" Servis u garantnom roku ");
    jcbGaranc.setDataSet(this.getMasterSet());
    jcbGaranc.setSelectedDataValue("D");
    jcbGaranc.setUnselectedDataValue("N");
    jcbGaranc.setColumnName("GARANC");
    jcbGaranc.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbGaranc.setHorizontalTextPosition(SwingConstants.LEADING);

    jraKol.setDataSet(this.getDetailSet());
    jraKol.setColumnName("KOL");
//    jraArtikl.setDataSet(this.getDetailSet());
//    jraArtikl.setColumnName("NAZART");
//    jraJmj.setDataSet(this.getDetailSet());
//    jraJmj.setColumnName("JM");
//    jraCART.setDataSet(this.getDetailSet());
//    jraCART.setColumnName("CART");
//    jraCART.setVisible(false);
//    jraCART1.setDataSet(this.getDetailSet());
//    jraCART1.setColumnName("CART1");
//    jraCART1.setVisible(false);
//    jraBC.setDataSet(this.getDetailSet());
//    jraBC.setColumnName("BC");
//    jraBC.setVisible(false);
    jlrVlasnik.setAfterLookAlways(true);
    jlrSubjekt.setAfterLookAlways(true);

    jlStatus.setText("Status");
    jlDatum.setText("Datum (od - do)");
    jpMaster.add(jpVlasnik, new XYConstraints(15, 15, 315, 135));
    jpMaster.add(sc2, new XYConstraints(345, 15, 315, 135));
    jpVlasnik.add(rcbVlasnik, new XYConstraints(15, 15, 125, -1));
//    jpVlasnik.add(jlVlasnik, new XYConstraints(15, 15, -1, -1));
    jpVlasnik.add(jlrVlasnik,   new XYConstraints(170, 15, 100, -1));
    jpVlasnik.add(jbSelVlasnik, new XYConstraints(275, 15, 21, 21));
    jpVlasnik.add(jlrNazVlasnik, new XYConstraints(272, 15, 1, -1));
    jpVlasnik.add(jlIme, new XYConstraints(15, 50, -1, -1));
    jpVlasnik.add(jlAdresa, new XYConstraints(15, 75, -1, -1));
    jpVlasnik.add(jlTel, new XYConstraints(15, 100, -1, -1));
    jpVlasnik.add(jlImeT, new XYConstraints(135, 50, 160, -1));
    jpVlasnik.add(jlAdrT, new XYConstraints(135, 75, 160, -1));
    jpVlasnik.add(jlTelT, new XYConstraints(135, 100, 160, -1));
    jpSubjekt.add(jlrSubjekt, new XYConstraints(125, 15, 140, -1));
    jpSubjekt.add(jbSelSubjekt, new XYConstraints(270, 15, 21, 21));
    jpSubjekt.add(jlSubjekt, new XYConstraints(15, 15, -1, -1));
    jpSubjekt.add(podaci, new XYConstraints(15, 50, 265, 0));
    jpMaster.add(jlRadovi, new XYConstraints(15, 165, -1, -1));
    jpMaster.add(scroller, new XYConstraints(15, 190, 645, 100));
    jpMaster.add(jlNapomene, new XYConstraints(15, 390, -1, -1));
    jpMaster.add(jlrNap1, new XYConstraints(150, 390, 75, -1));
    jpMaster.add(jlrNap2, new XYConstraints(150, 415, 75, -1));
    jpMaster.add(jlrNaznap1, new XYConstraints(230, 390, 400, -1));
    jpMaster.add(jlrNaznap2, new XYConstraints(230, 415, 400, -1));
    jpMaster.add(jlNapom, new XYConstraints(15, 440, -1, -1));
    jpMaster.add(jraNapom, new XYConstraints(150, 440, 480, -1));
    jpMaster.add(jlNapomInt, new XYConstraints(15, 465, -1, -1));
    jpMaster.add(jraNapomInt, new XYConstraints(150, 465, 480, -1));
    jpMaster.add(jbSelNap1, new XYConstraints(635, 390, 21, 21));
    jpMaster.add(jbSelNap2, new XYConstraints(635, 415, 21, 21));
    jpMaster.add(jLabel1, new XYConstraints(360, 162, -1, -1));
    jpMaster.add(jraDatum, new XYConstraints(490, 160, 100, -1));
    jpMaster.add(jbsImage, new XYConstraints(639, 160, 21, 21));
    jpMaster.add(jbImage, new XYConstraints(613, 160, 21, 21));
    jpMaster.add(jbSelRadovi, new XYConstraints(290, 160, 21, 21));
    jpMaster.add(jlrRadovi, new XYConstraints(185, 160, 100, -1));
    jpMaster.add(jlrTekst, new XYConstraints(288, 160, 1, -1));
    jpMaster.add(jlOsig, new XYConstraints(15, 330, -1, -1));
    jpMaster.add(jlrOsig, new XYConstraints(150, 330, 75, -1));
    jpMaster.add(jlrNazosig, new XYConstraints(230, 330, 200, -1));
    jpMaster.add(jbSelOsig, new XYConstraints(435, 330, 21, 21));
    jpMaster.add(jlrNorm, new XYConstraints(150, 300, 75, -1));
    jpMaster.add(jlrNaznorm, new XYConstraints(230, 300, 200, -1));
    jpMaster.add(jbSelNorm, new XYConstraints(435, 300, 21, 21));
    jpMaster.add(jcbZamjena, new XYConstraints(475, 300, 175, -1));
    jpMaster.add(jcbGaranc, new XYConstraints(475, 330, 175, -1));
    jpMaster.add(jlNorm, new XYConstraints(15, 300, -1, -1));
    jpMaster.add(jlServiser, new XYConstraints(15, 360, -1, -1));
    jpMaster.add(jlrSer, new XYConstraints(150, 360, 75, -1));
    jpMaster.add(jlrSerIme, new XYConstraints(230, 360, 145, -1));
    jpMaster.add(jlrSerPrezime, new XYConstraints(380, 360, 250, -1));
    jpMaster.add(jbSelSer, new XYConstraints(635, 360, 21, 21));

    jpPres.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpPres.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpPres.add(jlrNazorg, new XYConstraints(255, 20, 250, -1));
    jpPres.add(jbSelCorg, new XYConstraints(510, 20, 21, 21));

    jpPres.add(jlrNazvrsub, new XYConstraints(255, 45, 250, -1));
    jpPres.add(jlrVrsub, new XYConstraints(150, 45, 100, -1));
    jpPres.add(jbVrsub, new XYConstraints(510, 45, 21, 21));
    jpPres.add(jlVrsub, new XYConstraints(15, 45, -1, -1));
    
    jpPres.add(jlrNazSub, new XYConstraints(255, 70, 250, -1));
    jpPres.add(jlrPreSub, new XYConstraints(150, 70, 100, -1));
    jpPres.add(jbPreSub, new XYConstraints(510, 70, 21, 21));
    jpPres.add(jlPreSub, new XYConstraints(15, 70, -1, -1));
    
    jpPres.add(jlStatus,  new XYConstraints(15, 95, -1, -1));
    jpPres.add(jcbStatus,  new XYConstraints(150, 95, 100, -1));
    jpPres.add(jlDatum,  new XYConstraints(15, 120, -1, -1));
    jpPres.add(jraDatumOd,  new XYConstraints(150, 120, 100, -1));
    jpPres.add(jraDatumDo,  new XYConstraints(255, 120, 100, -1));
    jpPres.add(jraSerpr,  new XYConstraints(360, 105, 5, -1));

//    jpDetail.add(jlArtikl, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlKol, new XYConstraints(15, 20, -1, -1));
//    jpDetail.add(jlrArtikl, new XYConstraints(150, 20, 100, 0));
    jpDetail.add(jraKol, new XYConstraints(150, 20, 100, -1));
//    jpDetail.add(jraArtikl, new XYConstraints(255, 20, 275, -1));
//    jpDetail.add(jbSelArt, new XYConstraints(535, 20, 21, 21));
//    jpDetail.add(jraJmj, new XYConstraints(255, 45, 50, -1));
/*    jpDetail.add(jraCART,new XYConstraints(320, 45, 5, -1));
    jpDetail.add(jraCART1,new XYConstraints(330, 45, 5, -1));
    jpDetail.add(jraBC,new XYConstraints(340, 45, 5, -1)); */
    this.raMaster.getJpTableView().addTableModifier(new raTableValueModifier("STATUS",
        new String[] {"P", "O", "F", "Z"}, 
        new String[] {"Prijavljen", "Obraðen", "Fakturiran", "Zatvoren"}));
    this.raMaster.getJpTableView().addTableModifier(new hr.restart.rn.raVlasnikColumnModifier());
    this.raMaster.getJpTableView().addTableModifier(subMod);

    pres.setSelDataSet(this.getMasterSet());
    pres.addSelRange(jraDatumOd, jraDatumDo);
    pres.setSelPanel(jpPres);

    jpMasterHeader.setDataSet(this.getMasterSet());
    jpMasterHeader.addBorder();

    jpMaster.setBorder(BorderFactory.createEtchedBorder());
    jpMasterMain.setLayout(new BorderLayout());
    jpMasterMain.add(jpMasterHeader, BorderLayout.NORTH);
    jpMasterMain.add(jpMaster, BorderLayout.CENTER);

    this.setUserCheck(false);

    JPanel det = new JPanel(new BorderLayout());
    det.add(jpDetail, BorderLayout.SOUTH);
    det.add(rpc, BorderLayout.NORTH);
    jpDetail.setBorder(BorderFactory.createEtchedBorder());

    this.setJPanelMaster(jpMasterMain);
    this.setJPanelDetail(det);
    raMaster.getJpTableView().addTableModifier(new raStatusColorModifier("STATUS","P",Color.GREEN.brighter().brighter(),Color.GREEN.darker().darker()));
    this.raMaster.addKeyAction(new raKeyAction(KeyEvent.VK_F2, "Unos i izbor vlasnika") {
      public void keyAction() {
        if (jbSelVlasnik.isEnabled() && isKupac()) {
          jlrVlasnik.requestFocus();
          getVlasnik();
        }
      }
    });

    this.raMaster.addKeyAction(new raKeyAction(KeyEvent.VK_F3, "Unos i izbor subjekta") {
      public void keyAction() {
        if (jbSelSubjekt.isEnabled()) {
          //jlrSubjekt.requestFocus();
          getSubjekt();
        }
      }
    });
    
    this.raMaster.addOption(mailstat = new hr.restart.util.raNavAction("Obavijesti e-mailom", raImages.IMGSENDMAIL, KeyEvent.VK_UNDEFINED) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        ServisMailer.sendMailRN(getMasterSet(), true);
      }
    },4);
    
    this.raMaster.addOption(chgdat = new hr.restart.util.raNavAction("Promjena datuma", raImages.IMGTABLE, KeyEvent.VK_UNDEFINED) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        changeDatum();
      }
    },4);
    
    this.raMaster.addOption(makerac = new hr.restart.util.raNavAction("Obraèun naloga", raImages.IMGMOVIE, KeyEvent.VK_F9) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        createRacun();
      }
    },4);

    this.raMaster.addOption(potob = new hr.restart.util.raNavAction("Potvrda / zatvaranje", raImages.IMGIMPORT, KeyEvent.VK_F7) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          Obradi();
          ServisMailer.sendMailRN(getMasterSet());
        }
      },4);
    
    this.raMaster.addOption(hist = new hr.restart.util.raNavAction("Povijest subjekta", raImages.IMGHISTORY, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        if (getMasterSet().getString("CSUBRN").length() > 0)
          subj.showHistory(getMasterSet().getString("CSUBRN"));
      }
    },4); 

    this.raDetail.addOption(new hr.restart.util.raNavAction("Detaljni prikaz", raImages.IMGHISTORY, KeyEvent.VK_F6) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          showDetails();
        }
      },3);

    //subj = (frmSubjekti) startFrame.getStartFrame().showFrame("hr.restart.robno.frmSubjekti",0,"Popis subjekata",false);

    jbSelVlasnik.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isKupac()) {
          jlrVlasnik.requestFocus();
          getVlasnik();
        } else jlrVlasnik.keyF9Pressed();
//        jlrVlasnik.keyF9Pressed();
      }
    });
    jbSelSubjekt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //jlrSubjekt.requestFocus();
        getSubjekt();
//        jlrSubjekt.keyF9Pressed();
      }
    });

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    raDetail.removeRnvCopyCurr();
    initRpcart();
    rpc.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
  }

//  public void afterSetModeMaster(char om, char nm) {
//    if (nm == 'B') jlrVlasnik.setRaDataSet(null);
//    jlrVlasnik.setBrowseMode();
//  }
  
  void changeDatum() {
    if (getMasterSet().rowCount() == 0) return;
    dlgServisDatum dsd = new dlgServisDatum(raMaster.getJframe(), "Promjena datuma naloga");
    dsd.changeDatum(getMasterSet());
    raMaster.getJpTableView().fireTableDataChanged();
  }
  
  void createRacun() {
    if (getMasterSet().rowCount() == 0) return;
    String stat = getMasterSet().getString("STATUS");
    if (stat.equalsIgnoreCase("F") || stat.equalsIgnoreCase("Z")) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), 
          "Raèun za ovaj nalog je veæ napravljen!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (stat.equalsIgnoreCase("P") && !lazyIZD) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), 
          "Nisu napravljene izdatnice za ovaj nalog!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    boolean rac = false;
    if (allowKP) {
      String[] opt = {"GRN", "RAC", "Prekid"};
      int ret = JOptionPane.showOptionDialog(raMaster.getWindow(), 
          "Želite li napraviti gotovinski (GRN) ili bezgotovinski (RAC) raèun?", 
          "Vrsta raèuna", 0, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]);
      if (ret == 1) rac = true;
      else if (ret != 0) return;
      
      if (rac && getMasterSet().getString("KUPPAR").equalsIgnoreCase("K")) {
        DataSet par = Partneri.getDataModule().getTempSet(
            Condition.equal("CKUPAC", getMasterSet()));
        par.open();
        if (par.rowCount() != 1) {
          JOptionPane.showMessageDialog(raMaster.getWindow(), 
              "Ne mogu pronaæi slog partnera za kupca na ovom radnom nalogu!", 
              "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
      } else if (!rac && getMasterSet().getString("KUPPAR").equalsIgnoreCase("P")) {
        DataSet par = Partneri.getDataModule().getTempSet(
            Condition.equal("CPAR", getMasterSet().getInt("CKUPAC")));
        par.open();
        if (par.rowCount() != 1 && Kupci.getDataModule().getRowCount(
               Condition.equal("CKUPAC", par)) != 1) {
          JOptionPane.showMessageDialog(raMaster.getWindow(), 
              "Ne mogu pronaæi slog kupca za partnera na ovom radnom nalogu!", 
              "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
      }
      
    } else if (getMasterSet().getString("KUPPAR").equalsIgnoreCase("P")) rac = true;
    
    if (rac) createRAC();
    else createGRN();
  }
  
  private void createRAC() {
    final raRAC rac = checkRacOpened();
    if (rac == null) return;
    invokeRacun(presRAC.getPres(), rac, "RAC");
  }

  
  private void createGRN() {
    final raGRN grn = checkGrnOpened();
    if (grn == null) return;
    invokeRacun(presGRN.getPres(), grn, "GRN");
  }
  
  private void invokeRacun(final jpPreselectDoc jpd, final raIzlazTemplate rit, String vrdok) {
    jpd.showJpSelectDoc(vrdok, rit, false);
    jpd.getSelRow().setString("CSKL", getMasterSet().getString("CSKL"));
    jpd.getSelRow().setString("VRDOK", vrdok);
    jpd.getSelRow().setUnassignedNull("BRDOK");
    jpd.getSelRow().setTimestamp("DATDOK-from", vl.getToday());
    jpd.getSelRow().setTimestamp("DATDOK-to", vl.getToday());
    jpd.doSelect();
    rit.go();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        rit.doOnFocusNovi(new Runnable() {
          public void run() {
            if (!raMaster.isVisible()) return;
            DataSet rn = RN.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK DATDOK",
                Condition.equal("CRADNAL", getMasterSet()));
            rit.invokeSC("RNL", rn);
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                raMaster.getJpTableView().fireTableDataChanged();
              }
            });
          }
        });
        rit.raMaster.rnvAdd_action();
      }
    });
  }

  protected boolean isAutomaticIZD() {
    return true;
  }

  protected void transferToIZD() {
    final raIZD izd = checkIzdOpened();
    if (izd == null) return;
    String cskl = checkSameSklad();
    if (cskl == null) return;
    if (cskl.length() == 0) cskl = raUser.getInstance().getDefSklad();
    if (!ld.raLocate(dm.getSklad(), "CSKL", cskl)) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Skladište nije definirano!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    presIZD.getPres().showJpSelectDoc("IZD", izd, false);
    presIZD.getPres().getSelRow().setString("CSKL", cskl);
    presIZD.getPres().getSelRow().setString("CORG", getMasterSet().getString("CSKL"));
    presIZD.getPres().getSelRow().setString("VRDOK", "IZD");
    presIZD.getPres().getSelRow().setUnassignedNull("BRDOK");
    presIZD.getPres().getSelRow().setTimestamp("DATDOK-from", vl.getToday());
    presIZD.getPres().getSelRow().setTimestamp("DATDOK-to", vl.getToday());
//    presIZD.getPres().getSelRow().post();
    presIZD.getPres().doSelect();
    izd.go();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        izd.doOnFocusNovi(new Runnable() {
          public void run() {
            if (!raMaster.isVisible()) return;
            String tros = frmParam.getParam("rn", "defVrtros", "",
                "Defaultna vrsta troška izdatnica za radni nalog");
            if (tros != null && tros.length() > 0)
              izd.getMasterSet().setString("CVRTR", tros);
            izd.MP.panelBasic.jpRN.setDefaultCORG(getMasterSet().getString("CSKL"));
            izd.MP.panelBasic.jpRN.setGod(getMasterSet().getString("GOD"));
            if (!izd.MP.panelBasic.jpRN.setAutomaticBroj(String.valueOf(getMasterSet().getInt("BRDOK")))) {
              izd.raMaster.getOKpanel().jPrekid_actionPerformed();
              izd.hide();
              JOptionPane.showMessageDialog(raMaster.getWindow(),
                  "Greška prilikom prijenosa!", "Greška", JOptionPane.ERROR_MESSAGE);
              return;
            }
            izd.raMaster.getOKpanel().jBOK_actionPerformed();
          }
        });
        izd.raMaster.rnvAdd_action();
      }
    });
    return;
  }

  protected raIZD checkIzdOpened() {
    
    raIZD izd = (raIZD) raLoader.load("hr.restart.robno.raIZD");
    if (izd.raMaster.isShowing()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Ekran za izdatnice je zauzet!",
         "Upozorenje", JOptionPane.WARNING_MESSAGE);
      return null;
    }
    return izd;
  }
  
  protected raRAC checkRacOpened() {
    
    raRAC rac = (raRAC) raLoader.load("hr.restart.robno.raRAC");
    if (rac.raMaster.isShowing()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Ekran za raèune je zauzet!",
         "Upozorenje", JOptionPane.WARNING_MESSAGE);
      return null;
    }
    return rac;
  }
  
  protected raGRN checkGrnOpened() {
    
    raGRN grn = (raGRN) raLoader.load("hr.restart.robno.raGRN");
    if (grn.raMaster.isShowing()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Ekran za raèune je zauzet!",
         "Upozorenje", JOptionPane.WARNING_MESSAGE);
      return null;
    }
    return grn;
  }

  protected String checkSameSklad() {
    String cskl = null;
    boolean same = true;
    int row = getDetailSet().getRow();
    raDetail.getJpTableView().enableEvents(false);
    for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
      if (cskl == null || cskl.length() == 0 ||
          cskl.equalsIgnoreCase(getDetailSet().getString("CSKLART")))
        cskl = getDetailSet().getString("CSKLART");
      else same = (getDetailSet().getString("CSKLART").length() == 0);
    getDetailSet().goToClosestRow(row);
    raDetail.getJpTableView().enableEvents(true);
    if (!same)
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Sve stavke nisu sa istog skladišta,\n"+
                                   "moguæ je samo ruèni prijenos!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
    return same ? cskl : null;
  }

  private void initRpcart() {
    rpc.setTabela(dm.getStRnlSer());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.InitRaPanCart();
  }

  protected void initNewMaster() {
    podaci.removeAll();
    jlImeT.setText("");
    jlAdrT.setText("");
    jlTelT.setText("");
    super.initNewMaster();
    this.getMasterSet().setString("SERPR", "S");
  }
  
  File img;
  JFileChooser fc = null;
  void showImage() {
    ImageIcon imgi = null;
    if (img != null) {
      try {
        imgi = new ImageIcon(img.toURL());
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (imgi == null) {
        JOptionPane.showMessageDialog(raMaster.getWindow(), 
            "Neispravan format povezane slike!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
    } else {
      imgi = riu.loadImage(getMasterSet().getString("CRADNAL").
          replace('-', '_').replace('/', '_'));
      if (imgi == null) {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Slika ne postoji ili greška na serveru!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }

    String title = raMaster.getMode() == 'N' ? "Slika za novi radni nalog" :
                    "Slika za radni nalog "+getMasterSet().getString("CRADNAL");
    final JraFrame disp = new JraFrame(title);
    disp.getContentPane().add(new JraScrollPane(new JLabel(imgi)));
    ((JPanel) disp.getContentPane()).setPreferredSize(new Dimension(640, 400));
    disp.pack();
    disp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    AWTKeyboard.registerKeyStroke(disp, AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        disp.dispose();
        return true;
      }
    });
    disp.setVisible(true);
  }
  
  void saveImage() {
    if (!riu.saveImage(img, getMasterSet().getString("CRADNAL").
        replace('-', '_').replace('/', '_'))) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Spremanje slike nije uspjelo!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  void attachImage() {
    if (raMaster.getMode() != 'B') {
      if (fc == null)
        fc = new JFileChooser();
      if (fc.showOpenDialog(raMaster.getWindow()) == JFileChooser.APPROVE_OPTION)
        img = fc.getSelectedFile();
      else img = null;
      
      jbImage.setEnabled(img != null);
    }
  }

  private void getVlasnik() {
    vlas = (frmVlasnik) startFrame.getStartFrame().showFrame("hr.restart.robno.frmVlasnik",0,"Popis vlasnika",false);
    vlas.SetInner(true);
    vlas.show();
    vlas.addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        vlas.removeComponentListener(this);
        if (vlas.isSelected()) {
          jlrVlasnik.setText("" + vlas.getKupac());
          jlrVlasnik.forceFocLost();
        }
      }
    });
  }

  private void clearVlasnik() {
    jlImeT.setText("");
    jlAdrT.setText("");
    jlTelT.setText("");
  }
  
  private void setVlasnik() {
    if (jlrVlasnik.getText().length() == 0) {
      clearVlasnik();
      return;
    }
    if (isKupac()) {
      if (!ld.raLocate(dm.getKupci(), "CKUPAC", jlrVlasnik.getText())) {
        clearVlasnik();
        return;
      }
      vlasnikIme = dm.getKupci().getString("IME")+" "+dm.getKupci().getString("PREZIME");
      vlasnikAdr = dm.getKupci().getString("ADR");
      vlasnikMJ = dm.getKupci().getString("MJ");
      vlasnikPBR = String.valueOf(dm.getKupci().getInt("PBR"));
      vlasnikTel = dm.getKupci().getString("TEL");
      vlasnikMatBr = dm.getKupci().getString("OIB");
      jlImeT.setText(vlasnikIme);
      if (vlasnikAdr.length() > 0)
        jlAdrT.setText(vlasnikAdr+",  "/*+vlasnikPBR+" "*/+vlasnikMJ);
      else jlAdrT.setText("");
      jlTelT.setText(vlasnikTel);
    } else {
      if (!ld.raLocate(dm.getPartneri(), "CPAR", jlrVlasnik.getText())) {
        clearVlasnik();
        return;
      }
      vlasnikIme = dm.getPartneri().getString("NAZPAR");
      vlasnikAdr = dm.getPartneri().getString("ADR");
      vlasnikMJ = dm.getPartneri().getString("MJ");
      vlasnikPBR = String.valueOf(dm.getPartneri().getInt("PBR"));
      vlasnikTel = dm.getPartneri().getString("TEL");
      vlasnikMatBr = dm.getPartneri().getString("MB");
      jlImeT.setText(vlasnikIme);
      jlAdrT.setText(vlasnikAdr);
      jlTelT.setText(vlasnikTel);
    }
  }

  private void getSubjekt() {
/*    subj.SetInner(true);
    subj.setInnerData(subCradnal, subWasEmpty ? 'N' : raMaster.getMode());
    if (noVrsub)
      subj.pres.showPreselect(subj, "Subjekti radnih naloga");
    else {
      subj.setNavigateFlag();
      subj.show();
    }
    subj.addComponentListener(new ComponentAdapter() {
      public void componentHidden(ComponentEvent e) {
        subj.removeComponentListener(this);
        if (subj.isSelected()) {
          //getMasterSet().setShort("CVRSUBJ", subj.cvrsubj);
          Asql.updateHorizontalDataSet(jlrSubjekt.getRaDataSet(), 
              subj.getSubrn(), subj.cvrsubj);
          jlrSubjekt.setText(subj.getSubrn());
          subj.jpPodaci.setCsubrn("");
          jlrSubjekt.forceFocLost();
        }
        if (noVrsub)
          removeSubjektFilter();
      }
    });*/
    if (noVrsub && jlrSubjekt.getText().length() == 0) subj.setVrsub((short) -1);
    else subj.setVrsub(getMasterSet().getShort("CVRSUBJ"));
    if (raMaster.getMode() != 'N' && !subWasEmpty)
      subj.setCradnal(getMasterSet().getString("CRADNAL"));
    else subj.setCradnal("");
    if (jlrSubjekt.getText().length() == 0) {
      subj.newSubjekt();
      if (vlasnikColNum > 0 && jlrVlasnik.getText().length() > 0)
        subj.setFieldValue(vlasnikColNum - 1, jlrVlasnik.getText());
    } else subj.editSubjekt(jlrSubjekt.getText());
    if (subj.show(jraDatum)) {
      if (!noVrsub) Asql.updateHorizontalDataSet(jlrSubjekt.getRaDataSet(), 
          subj.getSubrn(), subj.cvrsubj);
      else {
        if (!jlrSubjekt.getRaDataSet().isOpen())
          jlrSubjekt.getRaDataSet().open();
        else {
          jlrSubjekt.getRaDataSet().insertRow(false);
          subj.sub.copyTo(jlrSubjekt.getRaDataSet());
          jlrSubjekt.getRaDataSet().post();
        }
      }
      jlrSubjekt.setText(subj.getSubrn());
      jlrSubjekt.forceFocLost();
      jlrRadovi.requestFocusLater();
      //setSubjekt();
    }
  }
  
  void clearSubjekt() {
    jpSubjekt.remove(podaci);
    podaci.removeAll();
    atrNames.clear();
    atrValues.clear();
    sc2.revalidate();
  }

  void setSubjekt() {
    if (vlasnikColNum > 0 && jlrVlasnik.getText().trim().length() == 0 
        && raMaster.getMode() != 'B' && jlrVlasnik.isEnabled()) {
      jlrVlasnik.setText(subj.getFieldValue(vlasnikColNum - 1));
      jlrVlasnik.forceFocLost();
    }

    System.out.println("frmServis, setSubject on your service :)");
    String s_n = subj.getSBText();
    jpSubjekt.remove(podaci);
    podaci.removeAll();
    atrNames.clear();
    atrValues.clear();
    for (int j = 0; j < nlf; j++) {
      System.out.println("name '"+atrNames+"' value '"+atrValues+"'");
//      if (!atrValues.equals("")){
        atrNames.append("\n");
        atrValues.append("\n");
//      }
    }
    vrsub = subj.getVrsubName();
//    System.out.println("here");
//    JLabel labName = new JLabel("Vrsta subjekta");
//    JLabel labValue = new JLabel(subj.getVrsubName());
//    podaci.add(labName, new XYConstraints(0, 5, 120, -1));
//    podaci.add(labValue, new XYConstraints(145, 5, 120, -1));
    jlSubjekt.setText(subj.getVrsubName());
    JLabel labName = new JLabel(s_n == null ? "S/B" : s_n);
    JLabel labValue = new JLabel(subj.getBroj());
    podaci.add(labName, new XYConstraints(0, 5, 110, -1));
    podaci.add(labValue, new XYConstraints(110, 5, 170, -1));

    scale = s_n == null ? 0 : s_n.length() + 4;

    if (s_n != null && s_n.length() > 0) {
      atrNames.append(s_n).append(Aus.spc(nspace)).append('\n');
      atrValues.append(Aus.spc(nspace)).append(subj.getBroj()).append('\n');
    }

    int reals = 0;
    for (int i = 0; i < subj.getFieldCount(); i++) {
      if(!subj.getFieldValue(i).equals("")){
        ++reals;
        atrNames.append(subj.getFieldName(i));

//      System.out.println("sta je ovo -> " + subj.getFieldName(i) + " i koliko je dugacko " + subj.getFieldName(i).length());

        if((subj.getFieldName(i).length()+4)>scale){
          scale = subj.getFieldName(i).length()+4;
//        System.out.println("scale = " + scale);
        }

        atrNames.append(Aus.spc(nspace)).append("\n");
        atrValues.append(Aus.spc(nspace));
        atrValues.append(subj.getFieldValueFormatted(i)).append("\n");
        labName = new JLabel(subj.getFieldName(i));
        labValue = new JLabel(subj.getFieldValueFormatted(i));
        podaci.add(labName, new XYConstraints(0, 5+reals*25, 110, -1));
        podaci.add(labValue, new XYConstraints(110, 5+reals*25, 170, -1));
      }
//      for(int g = 0; g<nlf; g++){
//        atrNames.append(" \n");
//        atrValues.append(" \n");
//      }
      jpSubjekt.add(podaci, new XYConstraints(15, 50, 265, 35+reals*25));
      sc2.revalidate();
    }
  }

  private void kupSelected() {
    jlIme.setText("Ime i prezime");
    if (jlrVlasnik.isShowing()) jlrVlasnik.setText("");
    jlrVlasnik.setNavColumnName("CKUPAC");
    jlrVlasnik.setColNames(new String[] {"IME"});
    jlrVlasnik.setRaDataSet(dm.getKupci());
    jlrVlasnik.setLookupFrameWidth(0);
    jlrVlasnik.setVisCols(new int[] {0,1,2,4});

    jlrNazVlasnik.setColumnName("IME");
    jlrNazVlasnik.setNavColumnName("IME");
    jlrNazVlasnik.setColNames(new String[] {"CKUPAC"});
    jlrNazVlasnik.setRaDataSet(dm.getKupci());
    jlrNazVlasnik.setLookupFrameWidth(0);
    jlrNazVlasnik.setVisCols(new int[] {0,1,2,4});


    if (jlrVlasnik.isShowing() && raMaster.getMode() != 'B' 
      && jlrVlasnik.isEnabled()) jlrVlasnik.forceFocLost();
  }

  private void parSelected() {
    jlIme.setText("Naziv");
    if (jlrVlasnik.isShowing()) jlrVlasnik.setText("");
    jlrVlasnik.setNavColumnName("CPAR");
    jlrVlasnik.setColNames(new String[] {"NAZPAR"});
    jlrVlasnik.setRaDataSet(dm.getPartneri());
    jlrVlasnik.setLookupFrameWidth(540);
    jlrVlasnik.setVisCols(new int[] {0,1,2});

    jlrNazVlasnik.setColumnName("NAZPAR");
    jlrNazVlasnik.setNavColumnName("NAZPAR");
    jlrNazVlasnik.setColNames(new String[] {"CPAR"});
    jlrNazVlasnik.setRaDataSet(dm.getPartneri());
    jlrNazVlasnik.setLookupFrameWidth(540);
    jlrVlasnik.setVisCols(new int[] {0,1,2});

    if (jlrVlasnik.isShowing() && raMaster.getMode() != 'B' 
      && jlrVlasnik.isEnabled()) jlrVlasnik.forceFocLost();
  }
  
  void updateVrsub(boolean full) {
    if (full) {
      short cvsub = (short) Aus.getNumber(jlrVrsub.getText());
      if (cvsub != oldPreVrsub) {
        determineVlasnikColumn(cvsub);
        if (sznac == null || cvsub != oldPreVrsubReal || 
            subjId != dm.getSynchronizer().getSerialNumber("RN_subjekt")) {
          sznac = Asql.getSubjektiDetail(cvsub);
          subjId = dm.getSynchronizer().getSerialNumber("RN_subjekt");
        }
        oldPreVrsub = oldPreVrsubReal = cvsub;
        jlrPreSub.setRaDataSet(sznac);
        jlPreSub.setText(jlrNazvrsub.getText());
        int vc = (int) Math.sqrt(jlrPreSub.getRaDataSet().getColumnCount()) + 5;
        if (vc > jlrPreSub.getRaDataSet().getColumnCount())
          vc = jlrPreSub.getRaDataSet().getColumnCount();
        int[] cols = new int[vc];
        for (int i = 1; i < vc; i++) cols[i] = i;
        jlrPreSub.setVisCols(cols);
        jlrNazSub.setRaDataSet(jlrPreSub.getRaDataSet());
        jlrNazSub.setVisCols(cols);
        jlrSubjekt.setRaDataSet(jlrPreSub.getRaDataSet());
        jlrSubjekt.setVisCols(cols);
        List modifs = findModifiers(cvsub);
        jlrPreSub.removeAllModifiers();
        jlrSubjekt.removeAllModifiers();
        for (int i = 0; i < modifs.size(); i++) {
          jlrPreSub.addModifier((raTableModifier) modifs.get(i));
          jlrSubjekt.addModifier((raTableModifier) modifs.get(i));
        }
        //jlrPreSub.setText("");
        //jlrPreSub.forceFocLost();
      }
    } else {
      oldPreVrsub = -1;
      jlPreSub.setText("Subjekt");
      jlrPreSub.setRaDataSet(dm.getRN_subjekt());
      jlrPreSub.setVisCols(new int[] {0,1});
      jlrNazSub.setRaDataSet(dm.getRN_subjekt());
      jlrNazSub.setVisCols(new int[] {0,1});
      jlrSubjekt.setRaDataSet(dm.getRN_subjekt());
      jlrSubjekt.setVisCols(new int[] {0,1,2});
      jlrPreSub.setText("");
      jlrPreSub.forceFocLost();
    }
  }
  
  DataSet sznac = null;
  int subjId;
  short oldPreVrsub = (short) -1;
  short oldPreVrsubReal = (short) -1;
  String vlasnikCol = null;
  int vlasnikColNum = 0;
  void afterVrsub(boolean succ) {
    updateVrsub(succ && jlrVrsub.getText() != null && jlrVrsub.getText().length() > 0);
    jlrPreSub.requestFocusLater();
  }
  
  private void determineVlasnikColumn(short cvsub) {
    vlasnikCol = frmParam.getParam("rn", "vlasnikCol"+cvsub, "", "Broj znaèajke vrste subjekta "+
        cvsub + " u kojoj se nalazi šifra vlasnika");
    if (vlasnikCol.trim().length() > 0) {
      vlasnikColNum = Integer.parseInt(vlasnikCol);
      vlasnikCol = "ZNAC"+vlasnikCol.trim();
    } else vlasnikColNum = 0;
  }
  
  private List findModifiers(short cvsub) {
    List mdf = new ArrayList();
    DataSet ds = Asql.getZnacVrsub(cvsub);
    for (ds.first(); ds.inBounds(); ds.next())
      if (ds.getString("ZNACDOH").length() > 0) {
        String def = ds.getString("ZNACDOH");
        String cols = null;
        if (def.indexOf(":") > 0) {
          cols = def.substring(def.indexOf(":") + 1);
          def = def.substring(0, def.indexOf(":"));
        }
        String parts[] = new VarStr(def).splitTrimmed('+');
        DataSet doh = null;
        try {
          java.lang.reflect.Method m = dM.class.getMethod(parts[2], null);
          doh = (DataSet) m.invoke(dM.getDataModule(), null);
        } catch (Exception e) {}
        if (doh != null)
          mdf.add(new raTableColumnModifier("ZNAC"+ds.getShort("CZNAC"), 
              new String[] {parts[0], parts[1]},
              new String[] {"ZNAC"+ds.getShort("CZNAC")},
              new String[] {parts[0]}, doh));
      }
    return mdf;
  }
  
  private void showDetails() {
//    System.out.println(repQDSdetails);
//    sys.prn(repQDSdetails);
    refilterStavkeRNU();
    startFrame.getStartFrame().showFrame("hr.restart.robno.frmStavkeRadnogNaloga","");
    frmStavkeRadnogNaloga.getInstance().setTitle("Detaljni prikaz usluga i materijala radnog naloga "+this.getMasterSet().getString("CRADNAL"));
//    frmStavkeRadnogNaloga.getFrmStavkeRadnogNaloga().getJpTableView().fireTableDataChanged();
  }
}
