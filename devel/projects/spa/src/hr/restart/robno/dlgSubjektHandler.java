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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttrib;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raPartialIncrementor;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class dlgSubjektHandler {
  
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JraDialog win;
  jpCustomAttrib jpPodaci = new jpCustomAttrib();
  int emptyHeight;
  
  XYLayout layVrsub = new XYLayout();
  XYLayout layDetail = new XYLayout();
  JPanel jpVrsub = new JPanel();
  JPanel jpDetail = new JPanel();
  
  JPanel jpMain = new JPanel();
  
  QueryDataSet sub = RN_subjekt.getDataModule().getTempSet("1=0");
  
  JlrNavField jlrNazvrsub = new JlrNavField() {
    public void after_lookUp() {
      vrsubChanged(isLastLookSuccessfull());
    }
  };
  JlrNavField jlrVrsub = new JlrNavField() {
    public void after_lookUp() {
      vrsubChanged(isLastLookSuccessfull());
    }
  };
  JraButton jbVrsub = new JraButton();
  JLabel jlVrsub = new JLabel();
  
  JLabel jlSubjekt = new JLabel();
  JraTextField jraSubjekt = new JraTextField();
  JraTextField jraBroj = new JraTextField() {
    public void valueChanged() {
      if (brojToSub) sub.setString("CSUBRN", jraBroj.getText());
    }
  };
  
  JLabel jlSifra = new JLabel();
  JLabel jlBroj = new JLabel();
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    };
    public void jPrekid_actionPerformed() {
      CancelPress();
    };
  };
  
  private raPartialIncrementor pi;
  
  boolean ok = false;
  
  short fixCvrs;
  short cvrsubj;
  boolean edit;
  String serbr;
  String sif;
  String cradnal;
  
  public dlgSubjektHandler() {
    try {
      initDlg();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void OKPress() {
    if (!Validacija()) return;
    ok = true;
    dM.getSynchronizer().markAsDirty("RN_znacsub");
    dM.getSynchronizer().markAsDirty("RN_subjekt");
    CancelPress();
  }
  
  void CancelPress() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
  
  public void showHistory(String csubrn) {
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
  
  private void initVrsub(short _cvrs) {
    if (cvrsubj == _cvrs) return;
    layDetail.setHeight(70);
    cvrsubj = _cvrs;
    System.out.println("Setting vrsub " + cvrsubj);
    lookupData.getlookupData().raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(_cvrs));
    serbr = dm.getRN_vrsub().getString("NAZSERBR");
    if (serbr == null || serbr.length() == 0) serbr = "S/B";
    sif = dm.getRN_vrsub().getString("NAZSIF");
    if (sif == null || sif.length() == 0) sif = "Šifra";
    jlSifra.setText(sif);
    jlBroj.setText(serbr);
  //  System.out.println("cvrsubj "+cvrsubj);
    jpPodaci.setFields(cvrsubj);
    layDetail.setWidth(Math.max(500, jpPodaci.getLayWidth()));
  //  System.out.println(jpPodaci.getLayHeight());
    layDetail.setHeight(emptyHeight + jpPodaci.getLayHeight() +
                        (jpPodaci.getLayHeight() > 0 ? 10 : 0));
    adjustFixedFields(jpPodaci.getLabelWidth(), jpPodaci.getTextWidth());
    
    if (win == null) {
      JraDialog tmp = new JraDialog();
      tmp.setContentPane(jpMain);
      tmp.pack();
      tmp.dispose();
    }
  //  System.out.println(xYLayout2.getWidth() + "  " + xYLayout2.getHeight());
  //  SetList();
  }
  
  public void editSubjekt(String csubrn) {
    edit = true;
    jpPodaci.setValues(csubrn, cradnal == null || cradnal.length() == 0 ?
        Condition.emptyString("CRADNAL") : Condition.equal("CRADNAL", cradnal));
    RN_subjekt.getDataModule().setFilter(sub, Condition.equal("CSUBRN", csubrn));
    sub.open();
    rcc.setLabelLaF(jraSubjekt, false);
    rcc.setLabelLaF(jraBroj, false);
    title = "Izmjena podataka za " + jpPodaci.getVrsub().toLowerCase() + " " + csubrn;
  }
  
  public void newSubjekt() {
    edit = false;
    RN_subjekt.getDataModule().setFilter(sub, "1=0");
    sub.open();
    if (cvrsubj >= 0) {
      jpPodaci.cancel();
      jpPodaci.insert();
    }
    pi.init(hr.restart.util.Util.getNewQueryDataSet("SELECT csubrn FROM RN_subjekt"), "CSUBRN");
    rcc.setLabelLaF(jraSubjekt, true);
    rcc.setLabelLaF(jraBroj, true);
    sub.insertRow(false);
    title = "Unos podataka za novi subjekt";
  }
  
  boolean brojToSub = false;
  String title;
  public boolean show(Container parent) {
    brojToSub = frmParam.getParam("rn", "brojToSub", "N", 
        "Kopirati jedinstveni broj u šifru subjekta (D,N)?").equalsIgnoreCase("D");
    Container realparent = null;
    
    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    
    win.setDefaultCloseOperation(win.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
      public void windowOpened(WindowEvent e) {
        setFokus();
      }
    });
    okp.registerOKPanelKeys(win);
    win.setContentPane(jpMain);
    win.pack();
    win.setLocationRelativeTo(parent);
    System.out.println("fixcvrs = " + fixCvrs);
    if (fixCvrs >= 0) {
      jlrVrsub.setText(Short.toString(cvrsubj));
      jlrVrsub.forceFocLost();
      rcc.EnabDisabAllLater(jpVrsub, false);
    }
    ok = false;
    win.show();
    rcc.EnabDisabAll(jpVrsub, true);
    return ok;
  }
  
  public boolean isAccepted() {
    return ok;
  }
  
  public void setVrsub(short _cvrs) {
    fixCvrs = _cvrs;
    if (fixCvrs < 0) {
      layDetail.setHeight(70);
      cvrsubj = _cvrs;
    } else {
      initVrsub(_cvrs);
      jpPodaci.setCsubrn("");
/*      jlrVrsub.setText(Short.toString(_cvrs));
      jlrVrsub.forceFocLost();
      rcc.EnabDisabAllLater(jpVrsub, false);*/
    }
  }
  
  public void setCradnal(String _cradnal) {
    cradnal = _cradnal;
  }
  
  public String getSBText() {
    return serbr;
  }
  
  public String getBroj() {
    return sub.getString("BROJ");
  }
  
  public String getSifText() {
    return sif;
  }

  public String getVrsubName() {
    return jpPodaci.getVrsub();
  }

  public String getSubrn() {
    return sub.getString("CSUBRN");
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

  public void setFieldValue(int idx, String text) {
    jpPodaci.setFieldValue(idx, text);
  }
  
  public String getFieldValueFormatted(int idx) {
    return jpPodaci.getFieldValueFormatted(idx);
  }
  
  void setFokus() {
    System.out.println("setFokus, brojToSub = " + brojToSub);
    if (edit) jpPodaci.SetFokus();
    else if (fixCvrs < 0) jlrVrsub.requestFocusLater();
    else if (brojToSub) jraBroj.requestFocusLater(); 
    else jraSubjekt.requestFocusLater();
  }
  
  boolean Validacija() {
    if (vl.isEmpty(jlrVrsub)) return false;
    if (vl.isEmpty(jraBroj)) return false;
    if (!edit && checkSerial()) return false;
    if (!jpPodaci.Validacija())
      return false;
    if (vl.isEmpty(jraSubjekt)) return false;
    if (!edit && vl.notUnique(jraSubjekt)) return false;
    
    if (!edit)
      jpPodaci.setCsubrn(sub.getString("CSUBRN"));

    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.saveChanges(sub);
        } catch (Exception e) {
          sub.refresh();
          if (!edit) {
            sub.insertRow(false);
            jpPodaci.clearValues();
          }
          return false;
        }
        raTransaction.saveChanges(jpPodaci.updateValues("CRADNAL", ""));
        if (cradnal != null && cradnal.length() > 0)
          raTransaction.saveChanges(jpPodaci.updateValues("CRADNAL", cradnal));
        return true;
      }
    }.execTransaction();
  }
  
  private boolean checkSerial() {
    DataSet olds = RN_subjekt.getDataModule().getTempSet(Condition.whereAllEqual(
        new String[] {"CVRSUBJ", "BROJ"}, sub));
    olds.open();
    if (olds.rowCount() == 0) return false;
    
    jraBroj.requestFocus();
    int response = JOptionPane.showConfirmDialog(win, 
        "Subjekt je veæ unesen u evidenciju! Prekinuti unos i odabrati postojeæeg?", 
        "Provjera jedinstvenog broja", JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.WARNING_MESSAGE);
    
    if (response == JOptionPane.OK_OPTION) simulateSelect(olds);
    return true;
  }
  
  void simulateSelect(DataSet olds) {
    sub.setShort("CVRSUBJ", olds.getShort("CVRSUBJ"));
    sub.setString("CSUBRN", olds.getString("CSUBRN"));
    sub.setString("BROJ", olds.getString("BROJ"));
    jpPodaci.setValues(olds.getString("CSUBRN"));
    ok = true;
    CancelPress();
  }
  
  void vrsubChanged(boolean succ) {
    if (fixCvrs >= 0) return; 
    
    System.out.println("vrsub changed");
    if (succ) {
      short ncv = Short.parseShort(jlrVrsub.getText());
      if (cvrsubj != ncv) {
        jpPodaci.cancel();
        initVrsub(Short.parseShort(jlrVrsub.getText()));
        jpPodaci.setCsubrn("");
        jpPodaci.insert();
      }
      jlSubjekt.setText(jpPodaci.getVrsub());
    } else {
      jpPodaci.cancel();
      layDetail.setHeight(70);
      cvrsubj = -1;
      jlSubjekt.setText("Subjekt");
    }
    if (win != null) {
      win.pack();
      if (!succ) jlrVrsub.requestFocusLater();
      else if (brojToSub) jraBroj.requestFocusLater();
      else jraSubjekt.requestFocusLater();
    }
  }
  
  private void adjustFixedFields(int lw, int tw) {
    jpVrsub.remove(jlrVrsub);
    jpVrsub.remove(jlrNazvrsub);
    jpVrsub.remove(jbVrsub);
    jpVrsub.add(jlrVrsub, new XYConstraints(lw, 20, 100, -1));
    jpVrsub.add(jlrNazvrsub, new XYConstraints(lw + 105, 20, tw - 105, -1));
    jpVrsub.add(jbVrsub, new XYConstraints(lw + tw + 5, 20, 21, 21));
    
    jpDetail.remove(jraSubjekt);
    jpDetail.remove(jraBroj);
    jpDetail.remove(jlSifra);
    jpDetail.remove(jlBroj);
    jpDetail.add(jraSubjekt, new XYConstraints(lw, 40, 100, -1));
    jpDetail.add(jraBroj, new XYConstraints(lw + 105, 40, tw - 105, -1));
    jpDetail.add(jlSifra, new XYConstraints(lw, 22, -1, -1));
    jpDetail.add(jlBroj, new XYConstraints(lw + 105, 22, -1, -1));
  }
    
  void initDlg() throws Exception {
    sub.open();
    sub.insertRow(false);
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
    
    jlVrsub.setText("Vrsta subjekta");
    jbVrsub.setText("...");

    jlrVrsub.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setDataSet(sub);
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);

    jlrNazvrsub.setNavProperties(jlrVrsub);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setSearchMode(1);
   
    jlSubjekt.setText("Subjekt");
    jlSifra.setText("Šifra");
    jlBroj.setText("Jedinstveni broj");
    jraSubjekt.setColumnName("CSUBRN");
    jraSubjekt.setDataSet(sub);
    jraBroj.setColumnName("BROJ");
    jraBroj.setDataSet(sub);
    
    jpVrsub.setLayout(layVrsub);
    layVrsub.setWidth(480);
    layVrsub.setHeight(50);
    
    jpDetail.setLayout(layDetail);
    layDetail.setWidth(500);
    layDetail.setHeight(70);
    jpDetail.add(jlSubjekt, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jpPodaci, new XYConstraints(0, 70, -1, -1));

    jpVrsub.add(jlVrsub, new XYConstraints(15, 20, -1, -1));
    
    jpMain.setLayout(new BorderLayout());
    jpMain.add(jpVrsub, BorderLayout.NORTH);
    jpMain.add(jpDetail);
    jpMain.add(okp, BorderLayout.SOUTH);
    
    adjustFixedFields(150, 310);
    
    pi = new raPartialIncrementor(jraSubjekt);
    pi.setNextField(jraBroj);
  }
}
