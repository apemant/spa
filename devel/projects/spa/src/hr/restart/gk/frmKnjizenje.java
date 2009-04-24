/****license*****************************************************************
**   file: frmKnjizenje.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Reportext;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.OKpanel;
import hr.restart.util.ResetEnabled;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raLLFrames;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raStatusBar;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import bsh.Interpreter;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjizenje extends JraDialog implements ResetEnabled {
  private raKnjizenje knjizenje = null;
  private int progSteps = 100;
  hr.restart.baza.dM dm;
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  lookupData ld = lookupData.getlookupData();
  JPanel jpCommon = new JPanel();
  JPanel jp = new JPanel();
  XYLayout xYLCommon = new XYLayout();
  jpGetVrsteNaloga jpGetVrnal = new jpGetVrsteNaloga();
  StorageDataSet dataSet = new StorageDataSet();
  JraTextField jtDATUMKNJ = new JraTextField();
  JLabel jlDATUMKNJ = new JLabel();
  JraTextField jtDATUMDO = new JraTextField();
  JLabel jlDATUMDO = new JLabel();
  raComboBox rcbNK = new raComboBox();
  hr.restart.sisfun.raDelayWindow dwin = null;
  OKpanel okpanel = new OKpanel() {
    public void jBOK_actionPerformed() {
      okPressed();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  JButton jbIspis = new JButton("Ispis",raImages.getImageIcon(raImages.IMGPRINT));
  private boolean fake;
/**
  * ekran za knjizenje koji se extenda i onda se u toj klasi pripremaju podaci za glavnu knjigu i to ovako:
  * <pre>
  * Najjednostavniji primjer
  * public boolean okPress() {
  *   if (!getKnjizenje().startKnjizenje(this)) return false;
  *   QueryDataSet setzaknjizenje = Util.getNewQueryDataSet("SELECT * FROM nekestavke WHERE (cnaloga is null or cnaloga = '')"+
  *      " AND DATDOK < '"+new java.sql.Date(Util.getUtil().addDays(dataSet.getTimestamp("DATUMDO"),1).getTime()).toString()+"'";
  *   if (setzaknjizenje.getRowCount() == 0) {
  *      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
  *      return false;
  *   }
  *   getKnjizenje().setTransferSet(setzaknjizenje);
  *   getKnjizenje().setInfoKeys(new String[] {"KNJIG","CSKL","VRDOK","STAVKA",...}); //unique key od nekestavke
  *   //getKnjizenje().setInfColName("CNALOGA"); // default pa je nepotrebno
  *   setzaknjizenje.first();
  *   do {
  *     if (!getKnjizenje().newStavka()) return false;
  *       //nova stavka
  *       StorageDataSet stavka = getKnjizenje().getNewStavka(nekestavke); //pod uvjetom da nekestavke ima kolone VRDOK,CSKL,STAVKA i CORG
  *                                                                       //ako nema, postoji getNewStavka(dataSet,corg) ili getNewStavka(brojkonta,corg)
  *       //punjenje nove stavke
  *       stavka.setTimestamp("DATDOK",...);
  *       stavka.setString("OPIS","...");
  *       getKnjizenje().setID(setzaknjizenje.getBigDecimal("ID"));
  *       getKnjizenje().setIP(setzaknjizenje.getBigDecimal("IP"));
  *       .
  *       .
  *       .
  *       .
  *
  *     if (!getKnjizenje().saveStavka()) return false;
  *   } while (setzaknjizenje.next());
  *   if (!getKnjizenje().saveAll()) return false;
  *   return true;
  * }
  *
  *
  * Primjer knjizenja iz master detaila (salda konti racuni) - istestirano i radi:
  *
  * public class frmKnjSKRac extends frmKnjizenje {
  *   QueryDataSet skstavke;
  *   StorageDataSet saveui;
  *   hr.restart.db.raPreparedStatement ps_updstavkeUI;
  *   String[] skstavkeKeys = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK", "BROJDOK", "BROJIZV"};
  *   String[] uistavkeKeys = new String[] {"KNJIG", "CPAR", "STAVKA", "CSKL", "VRDOK", "BROJDOK", "RBS"};
  *
  *   public frmKnjSKRac() {
  *    // Prepared statement za naknadno updatanje cgkstavke u uistavke jer ne mogu dobiti kompletan set za transfer
  *    // nego samo po odredjenoj skstavci pa ih dodajem u posebnu tabelu (saveui) koju raKnjizenje azurira
  *    // i nakon toga azuriram prave uistavke preko te tabele.
  *     ps_updstavkeUI = new raPreparedStatement("uistavke",raPreparedStatement.UPDATE);
  *     saveui = new StorageDataSet();
  *     saveui.setColumns(dm.getUIstavke().cloneColumns());
  *     saveui.open();
  *   }
  *
  *   public boolean okPress() throws Exception {
  *     String qry = "SELECT * FROM skstavke WHERE (cgkstavke is null or cgkstavke = '') AND DATDOK < '"
  *         +new java.sql.Date(Util.getUtil().addDays(dataSet.getTimestamp("DATUMDO"),1).getTime()).toString()+"'";
  *     skstavke = Util.getNewQueryDataSet(qry);
  *     if (skstavke.getRowCount() == 0) {
  *       getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
  *       throw new Exception("Nema podataka za knjiženje"); // nema nista za knjizenje
  *     }
  *     //tablica u koju dodajem stavke koje se prenose u gk i preko koje ce azurirati cgkstavke
  *     saveui.empty();
  *     //pocetak knjizenja
  *     if (!getKnjizenje().startKnjizenje(this)) return false;
  *     //setiranje transfer infa
  *     getKnjizenje().setTransferSet(saveui);
  *     getKnjizenje().setInfoKeys(uistavkeKeys);
  *     getKnjizenje().setInfColName("CGKSTAVKE"); // default je CNALOGA
  *     //petlja-nje
  *     skstavke.first();
  *     do {
  *       QueryDataSet uistavke = getUIStavke(skstavke);
  *       if (uistavke.getRowCount() > 0) {
  *         do {
  *           //dodajem u set za prijenos
  *           saveui.insertRow(false);
  *           uistavke.copyTo(saveui);
  *           saveui.post();
  *           //nova stavka
  *           StorageDataSet stavka = getKnjizenje().getNewStavka(uistavke);
  *           //punjenje nove stavke
  *           stavka.setTimestamp("DATDOK",skstavke.getTimestamp("DATDOK"));
  *           //ako je rbs od uistavke 1 onda ona prezentira skstavku i treba drugacije napuniti
  *           if (uistavke.getInt("RBS") == 1) {
  *             stavka.setString("OPIS",skstavke.getString("OPIS"));
  *             stavka.setString("BROJDOK",skstavke.getString("BROJDOK"));
  *             stavka.setInt("CPAR",skstavke.getInt("CPAR"));
  *           } else {
  *             if (getKnjizenje().isLastKontoZbirni()) {
  *               stavka.setString("OPIS","Protukonta dokumenata");
  *             } else {
  *               stavka.setString("OPIS","Protukonto dokumenta "+skstavke.getString("BROJDOK"));
  *             }
  *           }
  *           getKnjizenje().setID(uistavke.getBigDecimal("ID"));
  *           getKnjizenje().setIP(uistavke.getBigDecimal("IP"));
  *           //snima stavku i azurira transfer info, ako ne uspije prekida se knjizenje
  *           if (!getKnjizenje().saveStavka()) return false;
  *           if (uistavke.getInt("RBS") == 1) {
  *             //ako smo na skstavki napuni info
  *             getKnjizenje().setTransferSet(skstavke);
  *             getKnjizenje().setInfoKeys(skstavkeKeys);
  *             getKnjizenje().addTransferInfo(getKnjizenje().cGK);
  *             //vracanje set infa uistavaka
  *             getKnjizenje().setTransferSet(saveui);
  *             getKnjizenje().setInfoKeys(uistavkeKeys);
  *           }
  *         } while (uistavke.next());
  *       }
  *     } while (skstavke.next());
  *     return getKnjizenje().saveAll();
  *   }
  *   public boolean commitTransfer() {
  *     super.commitTransfer();
  *     if (saveui.getRowCount() == 0) return true;
  *     return new raLocalTransaction() {
  *       public boolean transaction() throws Exception {
  *         saveui.first();
  *         do {
  *           try {
  *             ps_updstavkeUI.setKeys(saveui);
  *             ps_updstavkeUI.setValues(saveui);
  *             ps_updstavkeUI.execute();
  *           }
  *           catch (Exception ex) {
  *             ex.printStackTrace();
  *             throw ex;
  *           }
  *         } while (saveui.next());
  *         return true;
  *       }}.execTransaction();
  *   }
  *   public static QueryDataSet getUIStavke(QueryDataSet skstavke) {
  *     return Util.getNewQueryDataSet(
  *         "SELECT * FROM uistavke WHERE KNJIG = '" + skstavke.getString("KNJIG") +
  *         "' AND CPAR = " + skstavke.getInt("CPAR") +
  *         " AND VRDOK = '" + skstavke.getString("VRDOK") +
  *         "' AND BROJDOK = '" + skstavke.getString("BROJDOK")+"'");
  *   }
  * }
  * </pre>
  */
  public frmKnjizenje() {
    this(false);
  }
  public frmKnjizenje(boolean _fake) {
    super(frmGK.getStartFrame(),false);
    try {
      //optimizacija
      frmNalozi.getFrmNalozi();
      jbInit();
      setFake(_fake);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    initDataSet();
    jpGetVrnal.setDataSet(dataSet);
    jtDATUMKNJ.setColumnName("DATUMKNJ");
    jtDATUMKNJ.setDataSet(dataSet);
    jtDATUMKNJ.setHorizontalAlignment(SwingConstants.CENTER);
    jlDATUMKNJ.setText("Datum knjiženja");
    jtDATUMDO.setColumnName("DATUMDO");
    jtDATUMDO.setDataSet(dataSet);
    jtDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtDATUMDO.setHelpText("Unesite zadnji datum dokumenata koje knjižite");
    jlDATUMDO.setText("Knjižiti do dana");
    rcbNK.setRaColumn("NK");
    rcbNK.setDataSet(dataSet);
    rcbNK.setRaItems(new String[][] {
      {"Zbrojiti stavke prema kontnom planu","K"},
      {"Zbrojiti stavke po kontima i org. jedinicama","Z"},
      {"Svaka stavka pojedina\u010Dno","P"}
    });
    xYLCommon.setHeight(100);
    jp.setLayout(new BorderLayout());
    jpCommon.setLayout(xYLCommon);
    jpCommon.add(jpGetVrnal, new XYConstraints(0,0,-1,41));
    jpCommon.add(jlDATUMKNJ,   new XYConstraints(15, 45, -1, -1));
    jpCommon.add(jtDATUMKNJ,  new XYConstraints(150, 45, 100, -1));
    jpCommon.add(jlDATUMDO,   new XYConstraints(15, 70, -1, -1));
    jpCommon.add(jtDATUMDO,  new XYConstraints(150, 70, 100, -1));
    jpCommon.add(rcbNK,new XYConstraints(255,70,265,-1));
    jp.add(jpCommon,BorderLayout.NORTH);
    jp.add(okpanel,BorderLayout.SOUTH);
    getContentPane().add(jp,BorderLayout.CENTER);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        initInputValues();
        SetFokus();
      }
    });
    jbIspis.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fake = true;
        okPressed();
      }
    });
    okpanel.addResetButton(this);
    okpanel.add(jbIspis,BorderLayout.WEST);
    okpanel.registerOKPanelKeys(this);
  }

  /**
   * Zove se prije SetFokus() i postavlja datume na današnji i combobox na prvu stavku
   */
  private boolean first = true;
  public void initInputValues() {
    rcbNK.setSelectedIndex(rcbNK.getSelectedIndex());
    if (first) resetDefaults();
    first = false;
  }
  
  public void resetDefaults() {
    rcbNK.setSelectedIndex(0);
    dataSet.empty();
    dataSet.setTimestamp("DATUMKNJ",vl.getToday());
    dataSet.setTimestamp("DATUMDO",vl.getToday());
  }
  
  private void initDataSet() {
    dataSet.addColumn((Column)dm.getVrstenaloga().getColumn("CVRNAL").clone());
    dataSet.addColumn((Column)dm.getNalozi().getColumn("DATUMKNJ").clone());
    Column clDATUMDO = (Column)dm.getNalozi().getColumn("DATUMKNJ").clone();
    clDATUMDO.setColumnName("DATUMDO");
    dataSet.addColumn(clDATUMDO);
    Column clNK = new Column("NK","Na\u010Din knjiženja",Variant.STRING);
    clNK.setDefault("K");
    dataSet.addColumn(clNK);
  }

  public boolean Validacija() {
    return true;
  }

  public void setFake(boolean _fake) {
    fake = _fake;
    rcbNK.setVisible(fake);
    jbIspis.setVisible(!fake);
    if (fake) okpanel.change_jBOK("Ispis",raImages.IMGPRINT);
  }
  public boolean getFake() {
    return fake;
  }
  boolean isRealFake() {
    return !jbIspis.isVisible();
  }
  private boolean myValid() {
    if (vl.isEmpty(jpGetVrnal.jlrCVRNAL)) return false;
    if (vl.isEmpty(jtDATUMKNJ)) return false;
    if (vl.isEmpty(jtDATUMDO)) return false;
    return true;
  }

  public void SetFokus() {
    jpGetVrnal.jlrCVRNAL.requestFocus();
  }
  private void okPressed() {
    if (!myValid()) return;
    if (!runPreloader()) return;
    if (!Validacija()) return;
    new knjizThread().start();
  }

  /**
   * Tu se izvrsava kreiranje temeljnice metodama navedenima u primjeru (dokumentacija kostruktora).
   * sve se doga\u0111a u transakciji pa treba paziti na to (qds.saveChanges() -> raTransaction.saveChanges(qds))
   * @return true ako je sve proslo OK, ako vrati false abortira transakciju i javlja da nije uspjelo
   * @throws Exception za svaki exception sve se rollbacka na pocetak
   */
  public boolean okPress() throws Exception {
    return true;
  }

  /**
   * Izvršava se za vrijeme proknjižavanja temeljnice.
   * Tu je potrebno azurirati status stavaka vanjske evidencije koje su prenesene u datu temeljnicu
   * Ako se ne overrida radi se automatski
   * ako overridas commitTransfer i ne zoves super.commitTransfer() OBAVEZNO POZOVI METODU getKnjizenje().commitTransferSK prije svojeg koda
   * @return true ako je sve proslo OK, ako vrati false abortira operaciju i javlja da nije uspjelo
   */
  public boolean commitTransfer() {
    return getKnjizenje().commitTransfer();
  }
  public void cancelPress() {
    hide();
  }
  public raKnjizenje getKnjizenje() {
    if (knjizenje == null) knjizenje = new raKnjizenje(dataSet);
    knjizenje.fake = fake;
    return knjizenje;
  }

  /**
   * Vraæa < '2002-10-16' ako je zadani datumdo na ekranu 15. listopada 2002. godine,
   * tako da bi query mogao zvuèati ovako:
   * SELECT * FROM nesto WHERE KNJIG = + OrgStr.getKnjCorg() + AND DATOBR + getDatumDoSQL()
   */
  public String getDatumDoSQL() {
    return " < '"+new java.sql.Date(Util.getUtil().addDays(dataSet.getTimestamp("DATUMDO"),1).getTime()).toString()+"'";
  }

  public raStatusBar getStatus() {
    raStatusBar status;
    if (hr.restart.start.isMainFrame()) {
      status = hr.restart.mainFrame.getMainFrame().getStatusBar();
    } else {
      status = raLLFrames.getRaLLFrames().getMsgStartFrame().getStatusBar();
    }
    status.setShowDelayWindow(false);
    return status;
  }

  public void setSteps(int steps) {
    progSteps = steps;
  }
  public void setProcessMessage(String msg) {
    setProcessMessage(msg,false);
  }
  public void setProcessMessage(String msg, boolean resize) {
    if (dwin == null) dwin = hr.restart.sisfun.raDelayWindow.show(this,"Obrada",msg);
    else dwin.setMessage(msg,resize,0);
    getStatus().statusMSG(msg);
  }

  public void clearProcessMessage() {
    if (dwin == null) return;
    dwin.close();
    dwin = null;
  }

  private void showError() {
//    JOptionPane.showMessageDialog(this,"Knjiženje nije uspjelo! Greška: "+getKnjizenje().getErrorMessage(),"Greška",JOptionPane.ERROR_MESSAGE);
    int ret = JOptionPane.showOptionDialog(this,
      "Knjiženje nije uspjelo! Greška: "+getKnjizenje().getErrorMessage(),"Greška",
      JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,new String[] {"Ok","Greške","Podaci"},"Greške");
    if (ret == 1) getKnjizenje().kSk.showErrors();
    if (ret == 2) {
      showData();
    }
  }
  private void showData() {
    sysoutTEST ST = new sysoutTEST(false);
    int odg = 0;
    do {
      odg = JOptionPane.showOptionDialog(this,
        "Podaci kreirani u tijeku knjiženja ","Podaci",
        JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,
        new String[] {"Izlaz","Stavke temeljnice","Stavke temeljnice za SK","SK dokumenti","Stavke za knjigu URA/IRA","Greške"},"Izlaz");
      if (odg == 1) {
        ST.showInFrame(getKnjizenje().getStavka(),"Stavke za temeljnice");
        ST.showInFrame(getKnjizenje().getFNalozi().getDetailSet(),"Prave stavke temeljnice");
      } else if (odg == 2) {
        ST.showInFrame(getKnjizenje().getStavkaSK(),"Stavke temeljnice za SK");
      } else if (odg == 3) {
        ST.showInFrame(getKnjizenje().skstavke,"SK dokumenti");
      } else if (odg == 4) {
        ST.showInFrame(getKnjizenje().uistavke,"UI stavke");
      } else if (odg == 5) {
        getKnjizenje().kSk.showErrors();
      }
    } while (odg > 0);
  }
  private void finnishTask() {
    setEnabled(true);
    getStatus().finnishTask();
  }
  private boolean runOkPressTrans() {
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
          return okPress();
      }

    }.execTransaction();
  }
  /**
   * Pokrece pilot-beanshell skripte za ubacivanje podataka iz stranih baza
   * Potrebno je prijaviti reportext sa NASLOVOM kao naziv klase za knjizenje,
   * te aplikacijum sisfun npr. 
   * IME=1, NASLOV=hr.restart.robno.frmKnjRobno, URL=robnoloader.sql, APP=sisfun
   * i sve skripte sa tim naslovom ce se izvrsiti prije validacija() metode u NASLOV klasi
   * sortirane by IME (pazi string sort!)
   * @return
   */
  private boolean runPreloader() {
    try {
      String cname = this.getClass().getName();
      //executeReport(java.net.URL rep, String title, Window owner)
      QueryDataSet preloaders = Reportext.getDataModule().getFilteredDataSet(
          Condition.whereAllEqual(new String[] {"NASLOV","APP"},new String[] {cname, "sisfun"}));
      preloaders.open();
      if (preloaders.getRowCount() == 0) return true;
      preloaders.setSort(new SortDescriptor("IME"));
      for (preloaders.first(); preloaders.inBounds(); preloaders.next()) {
//        raPilot.executeReport(Aus.findFileAnywhere(preloaders.getString("URL")).toURL(),cname, this );
        Interpreter interpreter = new Interpreter();
        Object ret = interpreter.eval(FileHandler.readFile(Aus.findFileAnywhere(preloaders.getString("URL")).getAbsolutePath()));
        return ((Boolean)ret).booleanValue();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  class knjizThread extends Thread {
    public void run() {
      setEnabled(false);      
      getStatus().startTask(progSteps,"Knjiženje u tijeku ...");
      setProcessMessage("Knjiženje u tijeku ...");
      getKnjizenje().kSk.clearErrors();
      boolean isOK = runOkPressTrans();
      clearProcessMessage();
      try {
        if (isOK) {
          if (frmNalozi.getFrmNalozi().getPreSelect() == null) {
            finnishTask();
          } else {
            finnishTask();
//          hide();
            if (fake) {//samo ispis
              frmNalozi.getFrmNalozi().fakeDetail = knjizenje.getStavka();
              frmNalozi.getFrmNalozi().fake = true;
              frmNalozi.getFrmNalozi().Funkcija_ispisa_master();
              frmNalozi.getFrmNalozi().fake = false;
              frmNalozi.getFrmNalozi().fakeDetail = null;
              frmNalozi.getFrmNalozi().obrada.fKnjizenje = null;
            } else {
              frmNalozi.getFrmNalozi().show();
              frmGK.getFrmGK().openNalog();
              setVisible(false);
            }
          }
        } else {
          finnishTask();
          frmNalozi.getFrmNalozi().obrada.fKnjizenje = null;
          showError();
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      fake = isRealFake();
    }
  }
}