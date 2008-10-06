/****license*****************************************************************
**   file: frmSubjekti.java
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
import hr.restart.baza.RN;
import hr.restart.baza.RN_sifznac;
import hr.restart.baza.RN_subjekt;
import hr.restart.baza.RN_vrsub;
import hr.restart.baza.RN_znacajke;
import hr.restart.baza.RN_znachint;
import hr.restart.baza.RN_znacsub;
import hr.restart.baza.dM;
import hr.restart.rn.rnMain;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttrib;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavBar;
import hr.restart.util.raPartialIncrementor;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmSubjekti extends raMatPodaci {

//  sysoutTEST sys = new sysoutTEST(false);
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  static frmSubjekti subjekti;

  public PreSelect pres = new PreSelect() {
    public void SetFokus() {
      jlrVrsub.requestFocus();
      jlrVrsub.getRaDataSet().open();
      if (jlrVrsub.getRaDataSet().getRowCount() == 1) {
        this.getSelRow().setShort("CVRSUBJ",jlrVrsub.getRaDataSet().getShort("CVRSUBJ"));
        jlrVrsub.forceFocLost();
      }
    }
    public boolean Validacija() {
//      System.out.println("Validacija pres subj "+this.getSelRow());
      return SetVrsub(!vl.isEmpty(jlrVrsub), getSelRow().getShort("CVRSUBJ"));
    }
    public boolean applySQLFilter() {
      QueryDataSet selQDS = (QueryDataSet) getSelDataSet();
      String cond = "CVRSUBJ = " + getSelRow().getShort("CVRSUBJ");
      /*if (insideCall && serMode != 'N')
        cond = cond + " AND cradnal = '" + cradnal + "'";
      else cond = cond + " AND cradnal = ''";*/
      Aus.setFilter(selQDS, Valid.getValid().getNoWhereQuery(selQDS)+ " WHERE " + cond);
      selQDS.open();
      return true;
    }
  };

  JPanel jpPres = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrNazvrsub = new JlrNavField();
  JlrNavField jlrVrsub = new JlrNavField();
  JraButton jbVrsub = new JraButton();
  JLabel jlVrsub = new JLabel();
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlSubjekt = new JLabel();
  JraTextField jraSubjekt = new JraTextField();
  JraTextField jraBroj = new JraTextField();
  JLabel jlSifra = new JLabel();
  JLabel jlBroj = new JLabel();

  jpCustomAttrib jpPodaci = new jpCustomAttrib();

  int emptyHeight;
  String serbr, sif;
  String memCsubrn;
  String broj;
  short cvrsubj;
  private boolean insideCall = false;
  private char serMode;
  private boolean selected = false;
  private boolean canNavigate = false;

  private raPartialIncrementor pi;

//  LinkedList podaci;

  hr.restart.util.raNavAction odabir = new hr.restart.util.raNavAction("Odabir",raImages.IMGIMPORT,KeyEvent.VK_ENTER) {
    public void actionPerformed(java.awt.event.ActionEvent ev) {
      selectCurrent();
    }
  };
  
  hr.restart.util.raNavAction pov = new hr.restart.util.raNavAction("Povijest",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(java.awt.event.ActionEvent ev) {
      showCurrentHistory();
    }
  };

  public frmSubjekti() {
    try {
      subjekti = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmSubjekti getInstance() {
    return subjekti;
  }

  public void SetInner(boolean inner) {
//    System.out.println("setinner");
    if (this.getNavBar().contains(odabir) && !inner) {
//      System.out.println("removing");
//      System.out.println("position "+this.getNavBar().getNavContainer().getComponentIndex(odabir));
      this.getNavBar().getNavContainer().remove(odabir);
    } else if (inner && !this.getNavBar().contains(odabir)) {
//      System.out.println("adding");
      this.addOption(odabir,1);
    }
    getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(!inner);
    if (!inner) getNavBar().getStandardOption(raNavBar.ACTION_ADD).setEnabled(true);
    insideCall = inner;
  }
  
  private String cradnal;
  public void setInnerData(String cradnal, char mode) {
    this.cradnal = cradnal;
    serMode = mode;
    getNavBar().getStandardOption(raNavBar.ACTION_ADD).setEnabled(mode == 'N');
    getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(mode != 'B');
  }

  public boolean SetVrsub(boolean _valid, short _cvrs) {
//    System.out.println("set vrsub");
    if (!_valid)
      return false;
    cvrsubj = _cvrs;
    lookupData.getlookupData().raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(_cvrs));
    serbr = dm.getRN_vrsub().getString("NAZSERBR");
    if (serbr == null || serbr.length() == 0) serbr = "S/B";
    sif = dm.getRN_vrsub().getString("NAZSIF");
    if (sif == null || sif.length() == 0) sif = "Šifra";
//    System.out.println("cvrsubj "+cvrsubj);
    jpPodaci.setFields(cvrsubj);
    xYLayout2.setWidth(Math.max(500, jpPodaci.getLayWidth()));
//    System.out.println(jpPodaci.getLayHeight());
    xYLayout2.setHeight(emptyHeight + jpPodaci.getLayHeight() +
                        (jpPodaci.getLayHeight() > 0 ? 10 : 0));
    adjustFixedFields(jpPodaci.getLabelWidth(), jpPodaci.getTextWidth());
//    System.out.println(xYLayout2.getWidth() + "  " + xYLayout2.getHeight());
//    SetList();
    canNavigate = true;
    return true;
  }

  public void SimulatePreselect() {
//    System.out.println("simulate preselect");
    pres.getSelRow().setShort("CVRSUBJ", cvrsubj);
    //Andrej - zbog toga sto je neko dodao NAZVRSUBJ u TABELU SUBJEKATA
//    lookupData.getlookupData().raLocate(jlrVrsub.getRaDataSet(), "CVRSUBJ", String.valueOf(cvrsubj));
//    pres.getSelRow().setString("NAZVRSUBJ",jlrVrsub.getRaDataSet().getString("NAZVRSUBJ"));
    //
    pres.doSelect();
  }

  public void setNavigateFlag() {
    canNavigate = true;
  }

  public void setNavigateFlag(boolean yesno) {
    canNavigate = yesno;
  }

  public String getSBText() {
    return serbr;
  }
  
  public String getSifText() {
    return sif;
  }

  public String getVrsubName() {
    return jpPodaci.getVrsub();
  }

  public String getSubrn() {
    return jpPodaci.getCsubrn();
  }

  public void setBroj(String b) {
    broj = b;
  }

  public String getBroj() {
    return broj;
  }

  public int getFieldCount() {
    return jpPodaci.getFieldCount();
  }

  public String getFieldName(int idx) {
    return jpPodaci.getFieldName(idx);
  }

  public String getFieldValue(int idx) {
    return jpPodaci.getFieldValue(idx);
  }

  public String getFieldValueFormatted(int idx) {
    return jpPodaci.getFieldValueFormatted(idx);
  }

  public boolean isSelected() {
    return selected;
  }

  public void beforeShow() {
    this.setTitle("Subjekti tipa - " + jpPodaci.getVrsub());
    /*if (!insideCall)
      SetList();*/
    if (insideCall) rnMain.rnMnu.enableSubjekt(false);
    jpPodaci.setCsubrn("");
    jlBroj.setText(serbr);
    jlSifra.setText(sif);
//    getRaQueryDataSet().getColumn("BROJ").setCaption(serbr);
    selected = false;
    canNavigate = true;
    super.pack();
  }

  private void eraseFields() {
    /*Iterator it = podaci.iterator();

    while (it.hasNext())
      ((Podatak) it.next()).col.setText(""); */
//    System.out.println("brišem");
    jpPodaci.insert();
//    Podatak.createRow();
//    Podatak.ds.insertRow(false);
//    System.out.println(Podatak.ds);
  }

//  public void EntryPoint(char mode) {
//    if (mode == 'N')
//      eraseFields();
//  }

  public void SetFokus(char mode) {
//    System.out.println("fokus: ");
    pres.copySelValues();

    if (mode != 'N')
      jpPodaci.setValues(this.getRaQueryDataSet().getString("CSUBRN"), Condition.emptyString("CRADNAL"));

    if (mode == 'N') {
      eraseFields();
      pi.init(hr.restart.util.Util.getNewQueryDataSet("SELECT csubrn FROM RN_subjekt"), "CSUBRN");
      jraSubjekt.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraSubjekt, false);
      jraBroj.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraSubjekt))
      return false;
    if (mode == 'N' && vl.notUnique(jraSubjekt))
      return false;
    if (!jpPodaci.Validacija())
      return false;
    if (mode == 'N' && checkSerial()) return false;
    jpPodaci.setCsubrn(this.getRaQueryDataSet().getString("CSUBRN"));
    return true;
  }
  
  private boolean checkSerial() {
    DataSet sub = RN_subjekt.getDataModule().getTempSet(Condition.whereAllEqual(
        new String[] {"CVRSUBJ", "BROJ"}, getRaQueryDataSet()));
    sub.open();
    if (sub.rowCount() == 0) return false;
    
    String[] opts = {"Da", "Povijest", "Prekini"};
    jraBroj.requestFocus();
    int response = JOptionPane.showOptionDialog(getWindow(), 
        "Subjekt je veæ unesen u evidenciju! Prekinuti unos i odabrati postojeæeg?", 
        "Provjera jedinstvenog broja", 0, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
    
    if (response == 1) showHistory(sub.getString("CSUBRN"));
    else simulateSelect(sub.getString("CSUBRN"));          

    return true;
  }
  
  private void navigateSubject(String csubrn) {
    getOKpanel().jPrekid.requestFocus();
    getOKpanel().jPrekid_actionPerformed();
    lookupData.getlookupData().raLocate(getRaQueryDataSet(), "CSUBRN", csubrn);
  }
  
  private void showCurrentHistory() {
    showHistory(getRaQueryDataSet().getString("CSUBRN"));
  }
  
  private void simulateSelect(String csubrn) {
    navigateSubject(csubrn);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        selectCurrent();
      }
    });
  }
  
  private void showHistory(String csubrn) {
    QueryDataSet rns = RN.getDataModule().getTempSet(
        Condition.in("CSKL", OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "CORG").
        and(Condition.equal("CSUBRN", csubrn)));
    rns.open();
    
    frmTableDataView view = new frmTableDataView();
    view.setDataSet(rns);
    view.setVisibleCols(new int[] {3,8,13,27});
    view.setSaveName("RN-history");
    view.setTitle("Povijest subjekta " + csubrn);
    view.jp.addTableModifier(new raTableValueModifier("STATUS",
        new String[] {"P", "O", "F", "Z"}, 
        new String[] {"Prijavljen", "Obraðen", "Fakturiran", "Zatvoren"}));
    view.show();
  }

  private boolean doMark = false;
  public boolean doWithSave(char mode) {
    try {
      if (mode == 'B') {
        raTransaction.runSQL("DELETE FROM RN_znacsub WHERE csubrn = '"+
            memCsubrn+"' and cradnal=''");
      } else {
        raTransaction.saveChanges(jpPodaci.updateValues("CRADNAL", ""));
        if (insideCall && cradnal != null && cradnal.length() > 0)
          raTransaction.saveChanges(jpPodaci.updateValues("CRADNAL", cradnal));
      }
      doMark = true;
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
  
  public boolean ValDPEscape(char mode) {
    if (mode == 'N' && !jraSubjekt.isEnabled()) {
      rcc.setLabelLaF(jraSubjekt, true);
      jraSubjekt.setText("");
      jraSubjekt.requestFocusLater();
      return false;
    }
    return true;
  }

  public void AfterSave(char mode) {
    if (mode == 'N')
      jpPodaci.cancel();
    if (doMark) {
      doMark = false;
      dM.getSynchronizer().markAsDirty("RN_znacsub");
    }
//    dm.getRN_znacsub().refresh();
  }

  public void AfterCancel() {
    jpPodaci.cancel();
    jpPodaci.setCsubrn("");
  }

  public void AfterAfterSave(char mode) {
    if (!insideCall)
      super.AfterAfterSave(mode);
    else selectCurrent();
  }
  
  void selectCurrent() {
    selected = true;
    jpPodaci.setCsubrn(this.getRaQueryDataSet().getString("CSUBRN"));
    rnvExit_action();
  }

  public boolean BeforeDelete() {
    memCsubrn = this.getRaQueryDataSet().getString("CSUBRN");
    return true;
  }

//  public void AfterDelete() {
//    vl.runSQL("DELETE FROM RN_znacsub WHERE csubrn = '"+memCsubrn+"'");
//    dm.getRN_znacsub().refresh();
//  }

  public void rnvExit_action() {
    SetInner(false);
    canNavigate = false;
    rnMain.rnMnu.enableSubjekt(true);
    //clearNavigateFlag();
    super.rnvExit_action();
  }

/*  public void table2Clicked() {
    if (!insideCall)
      super.table2Clicked();
    else {
      selected = true;
      jpPodaci.setCsubrn(this.getRaQueryDataSet().getString("CSUBRN"));
      rnvExit_action();
    }
  }*/

  private void jbInit() throws Exception {

    emptyHeight = 80;
    RN_vrsub.getDataModule();
    RN_znacajke.getDataModule();
    RN_znacsub.getDataModule();
    RN_sifznac.getDataModule();
    RN_znachint.getDataModule();
    jpPodaci.setTables("RN_vrsub", "RN_znacajke", "RN_znacsub", "RN_sifznac", "RN_znachint");
    jpPodaci.setVrsubCols("CVRSUBJ", "NAZVRSUBJ", "CPRIP");
    jpPodaci.setAttrCols("CZNAC", "ZNACOPIS", "ZNACTIP", "ZNACREQ", "ZNACSIF");
    jpPodaci.setAttrDohCol("ZNACDOH");
    jpPodaci.setValueCols("CSUBRN", "VRIZNAC");
    jpPodaci.setSifDesc("OPIS");
    jpPodaci.setHintText("ZHINT");

    this.setRaQueryDataSet(RN_subjekt.getDataModule().getFilteredDataSet("1=0"));
    this.setRaDetailPanel(jpDetail);
    this.setVisibleCols(new int[] {0, 1});

    jpPres.setLayout(xYLayout1);
    xYLayout1.setWidth(535);
    xYLayout1.setHeight(60);

    jlVrsub.setText("Vrsta subjekta");
    jbVrsub.setText("...");

    jlrVrsub.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);

    jlrNazvrsub.setNavProperties(jlrVrsub);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setSearchMode(1);

    jpDetail.setLayout(xYLayout2);
    jlSubjekt.setText("Subjekt");
    xYLayout2.setWidth(Math.max(500, jpPodaci.getLayWidth()));
    xYLayout2.setHeight(emptyHeight);
    jlSifra.setText("Šifra");
    jlBroj.setText("Jedinstveni broj");
    jraSubjekt.setDataSet(this.getRaQueryDataSet());
    jraSubjekt.setColumnName("CSUBRN");
    jraBroj.setDataSet(this.getRaQueryDataSet());
    jraBroj.setColumnName("BROJ");

//    jpPodaci.setLayout(xYLayout3);
//    xYLayout3.setWidth(500);
//    xYLayout3.setHeight(0);

    jpPres.add(jlrNazvrsub, new XYConstraints(255, 20, 250, -1));
    jpPres.add(jlrVrsub, new XYConstraints(150, 20, 100, -1));
    jpPres.add(jbVrsub, new XYConstraints(510, 20, 21, 21));
    jpPres.add(jlVrsub, new XYConstraints(15, 20, -1, -1));

    jpDetail.add(jlSubjekt, new XYConstraints(15, 40, -1, -1));
    adjustFixedFields(150, 205);
    jpDetail.add(jpPodaci, new XYConstraints(0, 70, -1, -1));

    pres.setSelDataSet(this.getRaQueryDataSet());
    pres.setSelPanel(jpPres);
    
    removeRnvCopyCurr();
    getNavBar().removeStandardOption(raNavBar.ACTION_TOGGLE_TABLE);
    addOption(pov, 4);

    pi = new raPartialIncrementor(jraSubjekt);
    pi.setNextField(jraBroj);

/*    jraBroj.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        System.out.println(Podatak.ds);
      }
    }); */
//    Aut.getAut().doTest();
  }

  public void adjustFixedFields(int lw, int tw) {
    jpDetail.remove(jraSubjekt);
    jpDetail.remove(jraBroj);
    jpDetail.remove(jlSifra);
    jpDetail.remove(jlBroj);
    jpDetail.add(jraSubjekt, new XYConstraints(lw, 40, 100, -1));
    jpDetail.add(jraBroj, new XYConstraints(lw + 105, 40, tw - 105, -1));
    jpDetail.add(jlSifra, new XYConstraints(lw, 22, -1, -1));
    jpDetail.add(jlBroj, new XYConstraints(lw + 105, 22, -1, -1));
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (canNavigate) {
      jpPodaci.setValues(this.getRaQueryDataSet().getString("CSUBRN"), Condition.emptyString("CRADNAL"));
      broj = this.getRaQueryDataSet().getString("BROJ");
    }
  }
}
