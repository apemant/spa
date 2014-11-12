/****license*****************************************************************
**   file: frmNalozi.java
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
import hr.restart.baza.*;
import hr.restart.robno.raRobno;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raAdditionalLookupFilter;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raLoader;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.reports.ReportMailDialog;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TooManyListenersException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.sql.dataset.QueryDataSet;
/*@todo
 * 1: ako na predselekciji ne unese vrstu naloga daje sve vrste
 * 2: multiple knjizenje sa oznacavanjem
 */
public class frmNalozi extends raMasterDetail {
  //sysoutTEST ST = new sysoutTEST(false);
  private static final int[] visibleColsDetailNalozi = new int[] {4,5,6,18,19};
  private int[] visibleColsMasterNalozi = new int[] {0,5,6,7,8,9};
  jpNaloziMaster jpMaster;
  public jpNaloziDetail jpDetail;
  private static frmNalozi fNal; // za ispise
  raObrNaloga obrada;
  String oldstatus = "X";
  dM dm;
  //  preSelNalozi psNal=null;
  double oldID = 0.0;
  double oldIP = 0.0;
  int oldRBS = 0;
  

  raNavAction rnvObrNaloga = new raNavAction("Obrada",raImages.IMGHISTORY,KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      obrNaloga_action();
    }
  };
 raNavAction rnvObrNalogaMas = new raNavAction("Obradi sve odabrane",raImages.IMGMOVIE,KeyEvent.VK_F10,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      masovnaObrNaloga_action();
    }
  };
  raNavAction rnvToggleSelect = new raNavAction("Okreni odabir",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_A,KeyEvent.CTRL_MASK) {
    public void actionPerformed(ActionEvent e) {
      toggleSelection();
    }
  };
 raNavAction rnvSendData = new raNavAction("Pošalji",raImages.IMGEXPORT,KeyEvent.VK_S,KeyEvent.CTRL_MASK) {
    public void actionPerformed(ActionEvent e) {
      sendData();
    }
  };

  raKeyAction rkaDetEscape = new raKeyAction(KeyEvent.VK_ESCAPE,"Ponovni unos broja konta") {
    public void keyAction() {
      detEscape_action();
    }
  };
  private String[] keys = new String[] {"KNJIG","GOD","CVRNAL","RBR"};
  private String[] dkeys = new String[] {"KNJIG","GOD","CVRNAL","RBR","RBS"};
  raCommonClass rCC = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  gkStatusColorModifier statusColorModifier = new gkStatusColorModifier();
  boolean obradaInProgress = false;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  boolean fake = false;
  com.borland.dx.dataset.DataSet fakeDetail;
  private JLabel jlMsg = new JLabel();
  private JraCheckBox jcMsg = new JraCheckBox("Pokrivanje");
  private JPanel jpMsg = new JPanel(new BorderLayout());
  private String[][] items4racVRDOK_I;
  private String[][] items4racVRDOK_U;
  private String[][] items4racVRDOK;

  private boolean deletingNalog = false;
  private boolean deletingStavkaWithGK = false;
  private boolean isHereza;
  private boolean copyGKuSKsucc = true;
  ReadRow nalogdoknjiz = null;


  public frmNalozi() {
    super(1,2);
    try {
      jbInit();
      fNal = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmNalozi getFrmNalozi() {
    if (fNal == null)
      fNal = (frmNalozi)raLoader.load("hr.restart.gk.frmNalozi");//fNal = new frmNalozi();
    return fNal;
  }

  public static com.borland.dx.dataset.DataSet getRepDetailSet() {
    if (getFrmNalozi().fake) { //samo ispis iz privremene tabele
      return getFrmNalozi().fakeDetail;
    } else {
      getFrmNalozi().refilterDetailSet();
      return getFrmNalozi().getDetailSet();
    }
  }

  public static com.borland.dx.dataset.DataSet getRepMasterSet() {
    return getFrmNalozi().getMasterSet();
  }

  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    obrada = raObrNaloga.getRaObrNaloga();
    setMasterSet(dm.getNalozi());
    setDetailSet(dm.getGkstavkerad());
    Nalozi.getDataModule().setFilter("1=0");
    Gkstavkerad.getDataModule().setFilter("1=0");
    Gkstavke.getDataModule().setFilter("1=0");
//    stozbrojiti_detail(new String[] {"ID","IP"});
    initJpMaster();
    initJpDetail();
    //raMaster.getNavBar().getColBean().setSaveSettings(false);
    //raDetail.getNavBar().getColBean().setSaveSettings(false);
    setVisibleColsMaster(visibleColsMasterNalozi);
    setVisibleColsDetail(visibleColsDetailNalozi);

//nepotrebno ako je detail version = 2 ili 3
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"ID","IP"});
    setnaslovi_detail(new String[] {"Iznos duguje","Iznos potražuje"});

    raMaster.addOption(rnvObrNaloga,5,false);
    raMaster.addOption(rnvObrNalogaMas,6,false);
    raMaster.addOption(rnvToggleSelect,7,false);
    raMaster.addOption(rnvSendData,8,false);
    setMasterKey(keys);
    setDetailKey(dkeys);
    setNaslovMaster("Nalozi - temeljnice");
    setNaslovDetail("Stavke naloga - temeljnice ");
    raDetail.addKeyAction(rkaDetEscape);
    setMasterDeleteMode(raMasterDetail.DELDETAIL);
    raDetail.setFastDelAll(true);
    raMaster.getJpTableView().addTableModifier(statusColorModifier);
    //    addNoSaldaKontaFilter();
    jpMsg.add(jlMsg,BorderLayout.CENTER);
    jpMsg.add(jcMsg,BorderLayout.SOUTH);
    raMaster.installSelectionTracker("CNALOGA");
    raDetail.getJpTableView().addTableModifier(new raTableColumnModifier("CPAR", new String[] {"NAZPAR", "CPAR"}, dm.getPartneri()));
  }
  void addNoSaldaKontaFilter() {
    jpDetail.kcGroup.getJlrBROJKONTA().setAdditionalLookupFilter(noSkFilter);
    jpDetail.kcGroup.getJlrNAZIVKONTA().setAdditionalLookupFilter(noSkFilter);
  }

  public boolean validateSelectionMaster(com.borland.dx.dataset.ReadRow row, boolean silent) {
    if (!row.getString("STATUS").equals("S")) {
      if (!silent) JOptionPane.showMessageDialog(raMaster, "Moguæe je odabrati samo naloge spremne za obradu!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  public boolean validateSelectionMaster(com.borland.dx.dataset.ReadRow row) {
    return validateSelectionMaster(row, false);
  }
  /*public void setItemsForVRDOK(char mode) {
    if (!getDetailSet().isOpen()) {
      return;
    }
    if (mode == 'B') {
      jpDetail.rcVRDOK.setRaItems(items4racVRDOK);
    } else {
      String kto = getDetailSet().getString("BROJKONTA");
      if (!kto.equals("") && raKonta.isSaldak(kto)) {
        if (raKonta.isKupac(kto)) {
          jpDetail.rcVRDOK.setRaItems(items4racVRDOK_I);
        } else if (raKonta.isDobavljac(kto)) {
          jpDetail.rcVRDOK.setRaItems(items4racVRDOK_U);
        }
      }
    }
  }*/
  
  public void Funkcija_ispisa_detail() {
    raDetail.getJpTableView().enableEvents(false);
    raDetail.getRepRunner().clearAllCustomReports();
    fillReports(raDetail, true);
    super.Funkcija_ispisa_detail();
    raDetail.getJpTableView().enableEvents(true);
  }
  /**
   * @todo napravit kopi pejst svega izmedju zvezda ;)
   */
  //******************************************************************************************************

  public void Funkcija_ispisa_master() {
    if (!raDetail.isShowing()) {
      forceChangeDetailViewStatus();
    }
    raMaster.getRepRunner().clearAllCustomReports();
    fillReports(raMaster, false);
    super.Funkcija_ispisa_master();
  }

  public void fillReports(raMatPodaci raMPa, boolean det) {
    raMPa.getRepRunner().addReport("hr.restart.gk.repNalog","hr.restart.gk.repNalog","Nalog","Ispis naloga - temeljnice");
    raMPa.getRepRunner().addReport("hr.restart.gk.repNalogSK","hr.restart.gk.repNalogSK","Nalog","Ispis temeljnice sa partnerom i brojem dokumenta");

    setStavkeSK(); // ovo vako zbog ucestalog pozivanja getstavkesk sdts-a iz providera,
    // da ne bi bilo bombardiranja baze.

    if (getStavkeSK() != null) {
      raMPa.getRepRunner().addReport("hr.restart.gk.repTemeljSK","Ispis stavaka za salda konti",5);
    }
    if (getDetailSet() != null && getDetailSet().isOpen())
    if (getDetailSet().getString("OPIS").startsWith("Kompenzacija po raèunu - ")) {
      raMPa.getRepRunner().addReport("hr.restart.gk.repKompNalog","hr.restart.gk.repNalog","Komp","Ispis kompenzacije");
    }
    if (!fake && !det) raMPa.getRepRunner().addReport("hr.restart.gk.repNalozi","Ispis zaglavlja temeljnica",5);
  }

  com.borland.dx.dataset.StorageDataSet temeljSkSet;// = new com.borland.dx.dataset.StorageDataSet();

  private void setStavkeSK() {
    temeljSkSet = new com.borland.dx.dataset.StorageDataSet();
/*    if (frmNalozi.getFrmNalozi().obrada.fKnjizenje == null){
      temeljSkSet = null;
      return;
    }*/ //trebao bih probati dohvatiti stavkeSK i kad nije knjizenje analitika u pitanju
    com.borland.dx.dataset.StorageDataSet stavkeSK;
    if (frmNalozi.getFrmNalozi().obrada.fKnjizenje == null){
      if (getMasterSet().getString("STATUS").equals("K")) {
        stavkeSK = Skstavke.getDataModule()
            .getTempSet("cgkstavke like '"+getMasterSet().getString("CNALOGA")+"%' "+getDoknjCGKStavkeSQL());
      } else {
        stavkeSK = getDetailSet();
      }
    } else {
        try {
          stavkeSK = frmNalozi.getFrmNalozi().obrada.fKnjizenje.getKnjizenje().getStavkaSK();
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return;
        }
    }
    stavkeSK.open();
    stavkeSK.setSort(new com.borland.dx.dataset.SortDescriptor(new String[]{"BROJKONTA","BROJDOK"}));
    if (stavkeSK.getRowCount() == 0) {
        temeljSkSet = null;
        return;
    }
    try {
      temeljSkSet.setColumns(new com.borland.dx.dataset.Column[] {
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("BROJKONTA").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("CPAR").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("BROJDOK").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("EXTBRDOK").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("DATDOK").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("ID").clone(),
        (com.borland.dx.dataset.Column)stavkeSK.getColumn("IP").clone()
      });

      temeljSkSet.open();
    }
    catch (Exception ex) {
      temeljSkSet.deleteAllRows(); // sto volim zloupotrebljavat try catch ;)
    }

    stavkeSK.first();
    do {
      if(hr.restart.zapod.raKonta.isSaldak(stavkeSK.getString("BROJKONTA"))){
        temeljSkSet.insertRow(false);
        temeljSkSet.setString("BROJKONTA",stavkeSK.getString("BROJKONTA"));
        temeljSkSet.setInt("CPAR",stavkeSK.getInt("CPAR"));
        temeljSkSet.setString("BROJDOK",stavkeSK.getString("BROJDOK"));
        temeljSkSet.setString("EXTBRDOK",stavkeSK.getString("EXTBRDOK"));
        temeljSkSet.setTimestamp("DATDOK",stavkeSK.getTimestamp("DATDOK"));
        temeljSkSet.setBigDecimal("ID",stavkeSK.getBigDecimal("ID"));
        temeljSkSet.setBigDecimal("IP",stavkeSK.getBigDecimal("IP"));
      }
    } while (stavkeSK.next());
  }

  /**
   * @return sve cgkstavke gdje od gkstavaka tekuce temeljnice
   */
  private String getDoknjCGKStavkeSQL() {
    if (!getDetailSet().isOpen()) {
       refilterDetailSet();
    }
    HashSet scgkset = new HashSet();
    String mcgks = getMasterSet().getString("CNALOGA");
    for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
      String dcgks = getDetailSet().getString("CNALOGA");
      if (!dcgks.startsWith(mcgks)) {
        scgkset.add(dcgks);
      }
    }
    String ret = "";
    for (Iterator iter = scgkset.iterator(); iter.hasNext();) {
      String element = iter.next().toString();
      if (!element.equals(""))//ako nije ispunjen cgkstavke nekim cudom
        ret = ret+" OR cgkstavke like '"+element+"%'";
    }
    return ret;
  }

  public com.borland.dx.dataset.StorageDataSet getStavkeSK(){
    // ovo se zove iz providera, i to puno puta :/
    return temeljSkSet;
  }

  /** */
  public java.sql.Timestamp getDatumKnjizenja(){
    // geter za datum knjizenja jer ga nisam moga uguzit u temeljSkSet :(
    // ako ti to podje za rukom NOTIFAJ MI!!!
    return getMasterSet().getTimestamp("DATUMKNJ");
  }

  //******************************************************************************************************

  private void initJpMaster() {
    jpMaster = new jpNaloziMaster(this);
    setJPanelMaster(jpMaster);
  }
  void rebindJpDetail() {
    //System.out.println("REBIND");
    /** @todo zamijeni poslije sa novonastalom metodom u ramatpodaci */
    Object[] arr = Util.getUtil().getDBComps(jpDetail.jpEntry).toArray();
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] instanceof com.borland.dx.dataset.ColumnAware) {
        com.borland.dx.dataset.ColumnAware cola = (com.borland.dx.dataset.ColumnAware)arr[i];
        if (getDetailSet().hasColumn(cola.getColumnName()) != null) {
          cola.setDataSet(getDetailSet());
        }
      }
    }
    /*SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jpDetail.setKontoConcat(getDetailSet().getRowCount() > 0 ? 1 : 0);
      }
    });*/
  }
  private void changeDetailViewStatus() {
    changeDetailViewStatus(getMasterSet().getString("STATUS"));
  }
  private void changeDetailViewStatus(String newstatus) {
/*
    System.out.println("changeDetailViewStatus "+oldstatus+"->"+newstatus);
    System.out.println("============== S T A C K  T R A C E ===============");
    new Throwable().printStackTrace();
    System.out.println("============== END STACK TRACE ===============");
*/
    jpMaster.jpBrNal.initJP(getMasterSet());
    jpDetail.jpBrNal.initJP(getMasterSet());

//    if (oldstatus.equals("K") && newstatus.equals("K")) return;
//    if (!oldstatus.equals("K") && !newstatus.equals("K")) return;
    if (oldstatus.equals(newstatus)) return;
    oldstatus = newstatus;
    if (newstatus.equals("K")) {
      setDetailSet(dm.getGkstavke());
      setVisibleColsDetail(new int[] {4,5,6,10,11});
      if (raDetail.isShowing()) {
        raDetail.getNavBar().getColBean().initialize();
      } else raDetail.forceColInitOnShow();
      raDetail.setEditEnabled(false);
      raDetail.setEnabledNavAction(
          raDetail.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE), true);
      raDetail.setTitle("Pregled stavaka proknjiženog naloga "+jpMaster.jpBrNal.getBrojNalogaText());
    } else {
      setDetailSet(dm.getGkstavkerad());
      setVisibleColsDetail(visibleColsDetailNalozi);
      if (raDetail.isShowing()) {
        raDetail.getNavBar().getColBean().initialize();
      } else raDetail.forceColInitOnShow();
      raDetail.setEditEnabled(true);
      raDetail.setTitle("Stavke naloga - temeljnice "+jpMaster.jpBrNal.getBrojNalogaText());
    }
    rebindJpDetail();
  }
  
  //boolean inClosing = false;
  
  public void refilterDetailSet() {
    if (!obradaInProgress) {
      forceChangeDetailViewStatus();
    }
    super.refilterDetailSet();
  }

  public void beforeShowMaster() {
    try {
      handleVRNAL_UI(true,((preSelNalozi)getPreSelect()).isVrnalEntered());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    oldstatus = "X";
  }
  public void handleVRNAL_UI(boolean doenab, boolean isVrnal) {
//    System.out.println("frmNalozi.handleVRNAL_ui");
    try {
      if (!isVrnal) {
//System.out.println("cvrnal nije upisan");
        if (doenab) rCC.EnabDisabAllLater(jpMaster.jpGetVrnal,true);
      } else {
//System.out.println("cvrnal JE upisan");
        jpMaster.jpBrNal.initJP(
          hr.restart.zapod.dlgGetKnjig.getKNJCORG(false),
          vl.findYear(getPreSelect().getSelRow().getTimestamp("DATUMKNJ-to")),
          getPreSelect().getSelRow().getString("CVRNAL"),
          0
        );
        if (doenab) {
          jpMaster.setCVRNALFromPreSelect(false);
          rCC.EnabDisabAllLater(jpMaster.jpGetVrnal,false);
        }
      }
      jpMaster.jpBrNal.jlBROJNALOGA.setVisible(isVrnal);
      setKontriznosDoknjizavanje();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    setTitleMaster();
  }
  private boolean setKontriznosDoknjizavanje() {
    if (isDoknjizavanje() && isDoknjizPrometNula()) {
      if (jpMaster.jtKONTRIZNOS.getDataSet().getRowCount()>0) {
        jpMaster.jtKONTRIZNOS.getDataSet().setBigDecimal(jpMaster.jtKONTRIZNOS.getColumnName(),new BigDecimal(0));
      }
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rCC.setLabelLaF(jpMaster.jtKONTRIZNOS,false);
        }
      });
      return true;
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          rCC.setLabelLaF(jpMaster.jtKONTRIZNOS,true);
        }
      });
      return false;
    }
    
  }

  void setTitleMaster() {
    setNaslovMaster("Nalozi - temeljnice - "+jpMaster.jpBrNal.cvrnalText);
    try {
      boolean enab = ((preSelNalozi)getPreSelect()).checkVrnal(false);
    }
    catch (Exception ex) {
      setNaslovMaster("Nalog - temeljnica");
    }
    //    raMaster.setEditEnabled(enab);
    //    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
    //    raDetail.setEditEnabled(enab);
  }
  public void EntryPointMaster(char mod) {
    if (mod == 'N') {
      oldstatus = "X";
      changeDetailViewStatus("N");
    }
  }
  
  public void EntryPointDetail(char mod) {
    if (mod == 'I' && getMasterSet().getString("STATUS").equals("K")) {
      rcc.EnabDisabAll(jpDetail, false);
      rcc.setLabelLaF(jpDetail.jtOPIS, true);
    }
  }
  
  private void forceChangeDetailViewStatus() {
    oldstatus = "X";
    changeDetailViewStatus();
  }

  private java.sql.Timestamp oldDATUMKNJ = null;
  public void SetFokusMaster(char mod) {
    if (mod == 'N') {
      preSelNalozi pressNal = (preSelNalozi)getPreSelect();
      handleVRNAL_UI(true,pressNal.isVrnalEntered());
      jpMaster.jpBrNal.noviBrojNaloga();
      rCC.setLabelLaF(jpMaster.jtDATUMKNJ,true);
      getMasterSet().setTimestamp("DATUMKNJ",pressNal.getDefaultDate());
      //AI555 set status da nakon knjizenja kad unese novi nalog enabla za unos
      getMasterSet().setString("STATUS","N");
      if (pressNal.isVrnalEntered()) jpMaster.jtDATUMKNJ.requestFocus();
      else jpMaster.jpGetVrnal.jlrCVRNAL.requestFocus();
    } else if (mod == 'I') {
      jpMaster.jpBrNal.initJP(getMasterSet());
//      rCC.setLabelLaF(jpMaster.jtDATUMKNJ,false);
      oldDATUMKNJ = getMasterSet().getTimestamp("DATUMKNJ");
      rCC.EnabDisabAll(jpMaster.jpGetVrnal,false);
      if (!setKontriznosDoknjizavanje()) jpMaster.jtKONTRIZNOS.requestFocus();
    } else {
      jpMaster.jpBrNal.initJP(getMasterSet());
    }
  }
  void newNalog() {
    jpMaster.jpBrNal.copyBroj(getMasterSet());
    getMasterSet().setString("CNALOGA",jpMaster.jpBrNal.getCNaloga());
    getMasterSet().setString("STATUS","N");
  }
  public boolean ValidacijaMaster(char mod) {
    if (mod == 'N') {
      newNalog();
      return !vl.isEmpty(jpMaster.jtDATUMKNJ);
    } else if (mod == 'I') {
      setStatus();
    }
    return true;
  }
  public void AfterAfterSaveMaster(char mode) {
    if (changeDatumKnjizenjaMade) getDetailSet().refresh();
    super.AfterAfterSaveMaster(mode);
  }
  private void initJpDetail() {
    jpDetail = new jpNaloziDetail(this);
    setJPanelDetail(jpDetail);
    /*String[] excl = new String[] {"UPL","IPL"};
    items4racVRDOK = frmKnjSKRac.getCbVrdokItems(false,null,excl);
    items4racVRDOK_I = frmKnjSKRac.getCbVrdokItems(false,"I",excl);
    items4racVRDOK_U = frmKnjSKRac.getCbVrdokItems(false,"U",excl);
    setItemsForVRDOK('B');*/
  }

  public void beforeShowDetail() {
    //    jpDetail.setPanelsVisible(0);
    jpDetail.jpBrNal.initJP(getMasterSet());
    checkSkKonto();
  }

  void newDetail() {
    jpDetail.kcGroup.setEnabledKonto(true);
    jpDetail.setScr(false,false,false);
    jpDetail.kcGroup.setEnabledCorg(false);
    jpDetail.jtOPIS.requestFocus();
  }
  public void afterSetModeDetail(char oM,char nM) {
    if (getDetailSet().getString("BROJKONTA").equals("")) {
      jpDetail.setVisibleVRDOK(false);
    } else {
      jpDetail.setVisibleVRDOK(raKonta.isSaldak(getDetailSet().getString("BROJKONTA")));
    }
    jpDetail.setKontoConcat(1);
    if (nM == 'B') {
      jpDetail.kcGroup.setCORGDataSet(dm.getOrgstruktura());

    }
    shutTheFuckUp_setScrKonto = (nM != 'B');
  }
  boolean shutTheFuckUp_setScrKonto =false;
  String lastOpis="";
  String lastBrdok="";
  public void SetFokusDetail(char mod) {
    lastBrdok="";
    rCC.setLabelLaF(jpDetail.jtHor1, false);
    rCC.setLabelLaF(jpDetail.jtVer1, false);
    if (mod == 'N') {
      jpDetail.setKontoConcat(0);
      rcc.setLabelLaF(jpDetail.jpBrNal.jralID, false);
      rcc.setLabelLaF(jpDetail.jpBrNal.jralIP, false);
      rcc.setLabelLaF(jpDetail.jpBrNal.jralKONTRIZNOS, false);
      rcc.setLabelLaF(jpDetail.jpBrNal.jralSALDO, false);
      //jpDetail.jpTSK.enable(false,raMaster); -> setKontoConcat;
      newStavka();
      jpDetail.jpBrNal.noviBrojStavke();
      getDetailSet().setTimestamp("DATDOK",getMasterSet().getTimestamp("DATUMKNJ"));
      jpDetail.kcGroup.getJlrBROJKONTA().emptyTextFields();
      newDetail();
      if (frmParam.getParam("gk", "lvNalog", "D", "Prenijeti opis na slijedecu novu stavku naloga").equals("D")) {
        getDetailSet().setString("OPIS", lastOpis);
      }
    } else if (mod == 'I') {
      if (!getMasterSet().getString("STATUS").equals("K")) {
        rcc.setLabelLaF(jpDetail.jpBrNal.jralID, false);
        rcc.setLabelLaF(jpDetail.jpBrNal.jralIP, false);
        rcc.setLabelLaF(jpDetail.jpBrNal.jralKONTRIZNOS, false);
        rcc.setLabelLaF(jpDetail.jpBrNal.jralSALDO, false);
        loadStavka();
        //      jpDetail.jpBrNal.initJP(getDetailSet().getInt("RBS"));
        jpDetail.kcGroup.setEnabledKonto(false);
        jpDetail.kcGroup.setScrKonto(getDetailSet().getString("BROJKONTA"));
      } else lastOpis = getDetailSet().getString("OPIS");
      jpDetail.jtOPIS.requestFocus();
      if (getDetailSet().hasColumn("BROJDOK") != null)
        lastBrdok = getDetailSet().getString("BROJDOK");
    } else {
      boolean vrDokVis;
      try {
        vrDokVis = hr.restart.zapod.raKonta.isSaldak(getDetailSet().getString("BROJKONTA"));
        hr.restart.sk.raGkSkUnosHandler.checkParam();
        vrDokVis = vrDokVis && hr.restart.sk.raGkSkUnosHandler.isGKSK;
      }
      catch (Exception ex) {
        vrDokVis = false;
      }
      jpDetail.setVisibleVRDOK(vrDokVis);
    }
  }
  public void prepareForSaveStavka(com.borland.dx.dataset.DataSet ds) {
    jpDetail.jpBrNal.copyBroj(ds);
    ds.setTimestamp("DATUMKNJ",getMasterSet().getTimestamp("DATUMKNJ"));
    ds.setString("GODMJ",obrada.getGodMj(ds));
  }
  void prepareForSaveStavka() {
    prepareForSaveStavka(getDetailSet());
  }
  
  QueryDataSet skup = null;
  public boolean ValidacijaDetailK(char mod) {
    if (!lastOpis.equals(getDetailSet().getString("OPIS"))) {
      
      skup = raSaldaKonti.getSkstavkaFromGK(getDetailSet());
      if (skup != null && !skup.getString("OPIS").equals(lastOpis))
        skup = null;
        
      lastOpis = getDetailSet().getString("OPIS");
    }
    return true;
  }
  
  public boolean ValidacijaDetail(char mod) {
    skup = null;
    if (getMasterSet().getString("STATUS").equals("K"))
      return ValidacijaDetailK(mod);
    
    lastOpis = getDetailSet().getString("OPIS");
    if (mod == 'N') {
      if (vl.isEmpty(jpDetail.kcGroup.getJlrBROJKONTA())) return false;
      prepareForSaveStavka();
    }
    if (mod != 'B') {
      if (vl.isEmpty(jpDetail.jtOPIS)) return false;
      if (vl.isEmpty(jpDetail.kcGroup.getJlrCORG())) return false;
      //if (vl.isEmpty(jpDetail.jtDATDOK)) return false;
      if (vl.chkIsEmpty(jpDetail.jtID)&&vl.chkIsEmpty(jpDetail.jtIP)) {
        jpDetail.jtID.requestFocus();
        JOptionPane.showMessageDialog(
        jpDetail.jtIP.getTopLevelAncestor(),
        "Potrebno je upisati iznos veæi od 0",
        "Greška",
        JOptionPane.ERROR_MESSAGE
        );
        return false;
      }
      if (raKonta.isSaldak(getDetailSet().getString("BROJKONTA"))) {
        if (getDetailSet().getBigDecimal("ID").signum() != 0 &&
            getDetailSet().getBigDecimal("IP").signum() != 0) {
          jpDetail.jtID.requestFocus();
          JOptionPane.showMessageDialog(jpDetail, "Salda konti stavke ne mogu imati "+
              "istovremeno i drugovnu i potražnu stranu!",
              "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    //return addSkPart();
    getDetailSet().setString("VRDOK", izmisliVrdok());
    if (!jpDetail.jpTSK.validate(mod)) return false;
    if (raKonta.isSaldak(getDetailSet().getString("BROJKONTA")) &&
        !lastBrdok.equals(getDetailSet().getString("BROJDOK"))) {
      String vrdok = getDetailSet().getString("VRDOK");
      int skType = 0;
      if (vrdok.equals("URN") || vrdok.equals("OKD")) skType = 1;
      else if (vrdok.equals("IRN") || vrdok.equals("OKK")) skType = 2;
      if (skType == 1 || skType == 2) {
        if (raSaldaKonti.existingDocument(getDetailSet().getInt("CPAR"), 
            getDetailSet().getString("BROJDOK"), skType == 1)) {
          jpDetail.jpTSK.jraBrojdok.requestFocus();
          JOptionPane.showMessageDialog(jpMaster, "Dokument istog broja za ovog partnera " +
                "veæ postoji u evidenciji saldakonti!", "Greška",
            JOptionPane.ERROR_MESSAGE);
          return false;
        }
        if (raRobno.isDocumentExist(getDetailSet().getString("BROJDOK"), 
            getDetailSet().getInt("CPAR"), skType == 1)) {
          jpDetail.jpTSK.jraBrojdok.requestFocus();
          JOptionPane.showMessageDialog(jpMaster, "Dokument istog broja za ovog partnera " +
                "veæ postoji u evidenciji robnog knjigovodstva!", "Greška",
            JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    return true;
  }
  /* NE KORISTI SE
  private boolean showSKUnosIzvod(String vrdok) {
    //new frame sa CPAR (req), BRIZV, VRDOK (off)
    jpTemIzvod.showTemIzvod(raDetail,vrdok);
    return jpTemIzvod.isSuccFull();
  }
   */
  private static boolean unosURNiIRNfromNalog() {
    return hr.restart.sisfun.frmParam.getParam("gk", "temURNIRN", "N", "Unose li se samo ?RN dokumenti kroz temeljnicu (D) ili samo OK? dokumenti (N)")
      .equals("D");
  }
  private String izmisliVrdok() {
    return determineVrdok(getDetailSet());
  }
  
  /**
   * Poziva determineVrdok(detSet, true)
   * @param detSet
   * @return
   */
  public static String determineVrdok(com.borland.dx.dataset.ReadRow detSet) {
    return determineVrdok(detSet, true);
  }
  /**
   * Vraca Vrstu dokumenta u ovisnosti od konta (detSet.getString("BROJKONTA")) i vrijednostima u ID i IP
   * detSet.getString("ID ili IP")
   * <pre>
   * ako je konto kupac
   *    id<0 OKK
   *    id>0 IRN ili OKK(*)
   *    ip!=0 OKD
   *    ostalo vidi izvode
   * ako je konto dobavljac
   *    ip<0 OKD
   *    ip>0 URN ili OKD(*)
   *    id!=0 OKK
   * (*)sa iskljucenim temURNIRN (unosURNiIRNfromNalog())
   * </pre>
   * @todo kontrola da li je PARTNER kupac ili dobavljac pri unosu konta
   * @param detSet slog za koji treba determinirati VRDOK, mora imati kolone BROJKONTA, ID, IP
   * @param nalog ako je true uzima u obzir parametar temURNIRN
   * @return oznaku vrste dokumenta koji bi odgovarao ovisno o glob.parametru temURNIRN
   */
  public static String determineVrdok(com.borland.dx.dataset.ReadRow detSet, boolean nalog) {//prepisano iz addSkPart koji se BTW vise ne koristi
    if (!raKonta.isSaldak(detSet.getString("BROJKONTA"))) return "";
    //NOVO & DOBRO ? :)
    int id_signum = detSet.getBigDecimal("ID").signum();
    int ip_signum = detSet.getBigDecimal("IP").signum();
    if (raKonta.isKupac()) {
      if (id_signum>0) return unosURNiIRNfromNalog()||!nalog?"IRN":"OKK";
      else if (ip_signum>0) return unosURNiIRNfromNalog()||!nalog?"UPL":"OKK";
      return "OKK";
    } else if (raKonta.isDobavljac()) {
      if (ip_signum>0) return unosURNiIRNfromNalog()||!nalog?"URN":"OKD";
      else if (id_signum>0) return unosURNiIRNfromNalog()||!nalog?"IPL":"OKD";
      return "OKD";
    }

    /* STARO & KRIVO :(
    if (raKonta.isKupac() && detSet.getBigDecimal("IP").compareTo(nula)!=0) {
      return "OKK";
    } else if (raKonta.isDobavljac() && detSet.getBigDecimal("ID").compareTo(nula)!=0) {
      return "OKD";
    } else if (raKonta.isKupac() && detSet.getBigDecimal("ID").compareTo(nula)!=0) {
      if (detSet.getBigDecimal("ID").compareTo(nula)>0 && unosURNiIRNfromNalog()) {
        return "IRN";
      }
      return "OKD";
    } else if (raKonta.isDobavljac() && detSet.getBigDecimal("IP").compareTo(nula)!=0) {
      if (detSet.getBigDecimal("IP").compareTo(nula)>0 && unosURNiIRNfromNalog()) {
        return "URN";
      }
      return "OKK";
    }*/
    if (detSet.hasColumn("VRDOK") != null) {
      return detSet.getString("VRDOK");
    } else return "";
  }
  /* NE KORISTI SE
   private boolean addSkPart() {
    if (raKonta.isSaldak(getDetailSet().getString("BROJKONTA"))) {
      BigDecimal nula = new BigDecimal(0);
      if (raKonta.isKupac() && getDetailSet().getBigDecimal("IP").compareTo(nula)!=0) {
        return showSKUnosIzvod("OKK");
      } else if (raKonta.isDobavljac() && getDetailSet().getBigDecimal("ID").compareTo(nula)!=0) {
        return showSKUnosIzvod("OKD");
      } else {
        showSKUnos();//unos racuna
        hr.restart.sk.raGkSkUnosHandler.checkParam();
        if (!hr.restart.sk.raGkSkUnosHandler.isGKSK) {
          int ret = JOptionPane.showOptionDialog(jpMaster.getTopLevelAncestor(),
          "Raèuni i uplate mogu se unositi kroz modul salda konti (Obrade - Raèuni/Uplate/Knjižne obavijesti) ",
          "Unos SK",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {"OK", "Ispravak"}, "OK");
          //System.out.println("ret = "+ret);
          if (ret == 1) {
            //ispravak: HEREZA to se kosi sa svim mojim uvjerenjima o odvajanju sk i gk
            if (raKonta.isKupac() && getDetailSet().getBigDecimal("ID").compareTo(nula)!=0) {
              return showSKUnosIzvod("OKD");
            } else if (raKonta.isDobavljac() && getDetailSet().getBigDecimal("IP").compareTo(nula)!=0) {
              return showSKUnosIzvod("OKK");
            }
            //eof hereze
          }
        }
        return false;
      }
    }
    return true;
  }*/
  /* NE KORISTI SE
  hr.restart.sk.raGkSkUnosHandler skhandler;
   */
  /* NE KORISTI SE
  private void showSKUnos() {
    /
     * offati frmNalozi.raMaster i frmNalozi.raDetail, nakon toga prikazati
     * extendani hr.restart.sk.frmSalKon ili hr.restart.sk.frmSalKonOK ovisno o vrdok i podmetnuti skstavkerad
     * koje treba oznaciti za copyGKuSK pri knjizenju temeljnice. Na ZatvoriOstaloMaster() on-ati frmNalozi i
     * refreshati detailSet da se pojave unesene stavke
     *
     /
    try {
      skhandler = new hr.restart.sk.raGkSkUnosHandler(
                          this,
                          getDetailSet().getString("OPIS"),
                          getDetailSet().getString("BROJKONTA"),
                          getDetailSet().getString("VRDOK"),
                          getDetailSet().getTimestamp("DATDOK"),
                          getDetailSet().getInt("RBS"),
                          getDetailSet().getBigDecimal("ID").add(getDetailSet().getBigDecimal("IP"))
                        );
      skhandler.prviput = true;
      skhandler.showSalKon();
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(raDetail, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
*/
  public void AfterSaveDetail(char mod) {
    if (!getMasterSet().getString("STATUS").equals("K"))
      updStavka();
  }

  public boolean DeleteCheckDetail() {
    if (!deletingNalog) loadStavka();
//    checkSalKon();
    return true;
  }
/* NE KORISTI SE
  String porukaDelDetail = null;
  String porukaDelMaster = null;
 */
  /*
  private void checkSalKon() {
    if (getDetailSet().getString("BROJDOK").equals("")) return;
    if (deletingNalog) return;
    porukaDelDetail = hr.restart.sk.raGkSkUnosHandler.getDeleteWithStavkaGKWarning(this);
    deletingStavkaWithGK = porukaDelDetail!=null;
  }
*/
  public void AfterDeleteDetail() {
    delStavka();
    deletingStavkaWithGK = false;
  }
/* NE KORISTI SE
  public boolean PorukaDeleteDetail() {
    if (porukaDelDetail == null) {
      return super.PorukaDeleteDetail();
    } else {
      boolean ret = raDetail.PorukaDelete(porukaDelDetail);
      porukaDelDetail = null;
      return ret;
    }
  }
 */
  /* NE KORISTI SE
  public boolean PorukaDeleteMaster() {
    if (porukaDelMaster == null) {
      return super.PorukaDeleteMaster();
    } else {
      boolean ret = raMaster.PorukaDelete(porukaDelMaster);
      porukaDelMaster = null;
      return ret;
    }
  }
   */
  private void disposeFKnjizenje() {
    if (rdw != null) {
      rdw.close();
      rdw = null;
    }
    if (obrada.fKnjizenje != null) {
      if (obrada.fKnjizenje.dwin != null) {
        obrada.fKnjizenje.dwin.close();
        obrada.fKnjizenje.dwin = null;
      }
      obrada.fKnjizenje = null;
    }
  }
  public void AfterDeleteMaster() {
    disposeFKnjizenje();
    deletingNalog = false;
  }
  private boolean deleteCheckMasterStatus() {
    String status = getMasterSet().getString("STATUS");
    if (status.equals("K")) {
      JOptionPane.showMessageDialog(jpMaster.getTopLevelAncestor(),
      "Prvo je potrebno dearhivirati (rasknjižiti) nalog da bi ga obrisali!",
      "Brisanje naloga",JOptionPane.INFORMATION_MESSAGE);
      return false;
    } else return true;
  }
  public boolean DeleteCheckMaster() {
    if (!deleteCheckMasterStatus()) return false;
    jpMaster.jpBrNal.initJP(getMasterSet());
//    porukaDelMaster = hr.restart.sk.raGkSkUnosHandler.getDeleteWithNalogGKWarning(this);
    deletingNalog = true;
    if (jpMaster.jpBrNal.isLastNalog()) return true;
    deleteStavke();
    return false;
  }
  private void deleteStavke() {
//    String addmsgSK = porukaDelMaster==null?"":" Napomena: Brisanjem stavaka obrisat æe se i vezani dokumenti SK.";
    if (askDialog("Nalog nije moguæe obrisati jer postoji unesen nalog s veæim brojem. Želite li obrisati stavke?"))//+addmsgSK))
      delAllDetailSet();
  }

  public boolean doWithSaveDetail(char mode) {
    if (mode == 'B' && !deletingNalog) {//brisanje
      return hr.restart.sk.raGkSkUnosHandler.deleteWithStavkaGK(this);
    }
    if (skup != null) {
      raTransaction.saveChanges(skup);
      skup = null;
    }
    return true;
  }
  private boolean changeDatumKnjizenjaMade = false;
  public boolean doWithSaveMaster(char mode) {
    changeDatumKnjizenjaMade = false;
    if (mode == 'B') {//brisanje
      return hr.restart.sk.raGkSkUnosHandler.deleteWithNalogGK(this);
    }
    if (mode == 'I' && getMasterSet().getTimestamp("DATUMKNJ").compareTo(oldDATUMKNJ) == 0) {
      String ret = changeDatumKnjizenja(getMasterSet().getString("CNALOGA"), getMasterSet().getTimestamp("DATUMKNJ"), false);
      if (!ret.equals("")) {
//        System.out.println("changeDatumKnjizenja: "+ret);
        return false;
      }
      changeDatumKnjizenjaMade = true;
    }
    return true;
  }
  
/*  public boolean ValidacijaPrijeIzlazaDetail() {
    boolean ret;
    inClosing = true;
    try {
      ret = _ValidacijaPrijeIzlazaDetail();
      if (!ret) inClosing = false;
    } finally {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          inClosing = false;
        }
      });
    }
    return ret;
  }*/

  public boolean ValidacijaPrijeIzlazaDetail() {
    if (hr.restart.sk.raGkSkUnosHandler.inSalkon) return false;
    Util ut = Util.getUtil();
    BigDecimal saldo = ut.setScale(getMasterSet().getBigDecimal("SALDO"),2);
    BigDecimal koniz = ut.setScale(getMasterSet().getBigDecimal("KONTRIZNOS"),2);
    BigDecimal nalID = ut.setScale(getMasterSet().getBigDecimal("ID"),2);
    String status = getMasterSet().getString("STATUS");
    BigDecimal nula = Aus.zero2;
    //    if (saldo == 0 && nalID != koniz && getDetailSet().getRowCount() > 0 && !status.equals("K")) {
    if (saldo.compareTo(nula) == 0 && nalID.compareTo(koniz) != 0 && getDetailSet().getRowCount() > 0 && !status.equals("K")) {
      if (isDoknjizavanje() && isDoknjizPrometNula()) return true;
      String msg = "Nalog je u balansu (ID=IP="+nalID+" dok je kontrolni iznos "+koniz+
      " Želite li ispraviti kontrolni iznos na "+nalID+" ?";
      int ret = JOptionPane.showConfirmDialog(
      jpDetail.getTopLevelAncestor(),
      msg,"Kontrolni iznos",
      JOptionPane.YES_NO_CANCEL_OPTION
      );
      if (ret == JOptionPane.CANCEL_OPTION) return false;
      if (ret == JOptionPane.NO_OPTION) return true;
      getMasterSet().setBigDecimal("KONTRIZNOS",nalID);
      setStatus();
      getMasterSet().saveChanges();
      raMaster.getJpTableView().fireTableDataChanged();
      //      jpDetail.setPanelsVisible(0);
      return true;
    }
    return true;
  }

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent ev) {
    if (raDetail.isShowing() || isStartShowingDetail()) return;
    handleRnvActionsWithStatus_masterSet_navigated(this);
  }
  public static void handleRnvActionsWithStatus_masterSet_navigated(raMasterDetail that, boolean additionalcase) {
    that.raMaster.setEditEnabled(!that.getMasterSet().getString("STATUS").equals("K") && additionalcase);
    that.raMaster.setEnabledNavAction(that.getRnvStavke(),true);
    that.raMaster.setEnabledNavAction(that.raMaster.getNavBar().getStandardOption(raNavBar.ACTION_ADD), true);    
  }
  public static void handleRnvActionsWithStatus_masterSet_navigated(raMasterDetail that) {
    handleRnvActionsWithStatus_masterSet_navigated(that,true);
  }
  
  private void checkSkKonto() {
    boolean isKontoSaldak;
    if (getDetailSet().getRowCount() == 0 ||
        getDetailSet().getString("BROJKONTA").length() == 0) {
      isKontoSaldak = false;
    } else {
      isKontoSaldak = raKonta.isSaldak(getDetailSet().getString("BROJKONTA"));
    }
    //setItemsForVRDOK(raDetail.getMode());

    jpDetail.setKontoConcat(getDetailSet().getString("BROJKONTA"));
    jpDetail.setVisibleVRDOK(isKontoSaldak);
/*    if (!oldstatus.equals("K") && !getDetailSet().getString("VRDOK").equals("")) {
    jpDetail.rcVRDOK.findCombo();
  }*/
  }

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (!raDetail.isShowing()) return;
    if (raDetail.getMode() == 'B') {
      jpDetail.jpBrNal.initJP(getDetailSet().getInt("RBS"));
  
      checkSkKonto();
    }
  }

  /*public void findDefaultComboVRDOK() {
    if (!getDetailSet().getString("VRDOK").equals("")) return;
    getDetailSet().setString("VRDOK", jpDetail.rcVRDOK.getDataValue());
  }*/

  void updStavka() {
    updStavka(true);
  }
  void updStavka(boolean real) {
    updStavka(getDetailSet(),real);
  }
  void updStavka(com.borland.dx.dataset.DataSet ds,boolean real) {
    double stvID = ds.getBigDecimal("ID").doubleValue();
    double stvIP = ds.getBigDecimal("IP").doubleValue();
    updNalog(stvID,stvIP,real);
  }

  void delStavka() {
    if (deletingNalog) return;
    recountStavke();
    if (deletingStavkaWithGK) {
      oldID = hr.restart.sk.raGkSkUnosHandler.deletedIznos.doubleValue();
      oldIP = hr.restart.sk.raGkSkUnosHandler.deletedIznos.doubleValue();
    }
    updNalog(0.0,0.0);
  }

  void recountStavke() {
    if (deletingStavkaWithGK) {
      fullRecountStavke();
      hr.restart.sk.raGkSkUnosHandler.refreshRecords(this);
    } else {
      vl.recountDataSet(raDetail,"RBS",oldRBS);
    }
  }

  private void fullRecountStavke() {
    int umanj = hr.restart.sk.raGkSkUnosHandler.deletedGKSCount;
    VarStr vq1 = new VarStr(getSQLFilterQuery());
//    System.out.println("vq1 = "+vq1);
    vq1 = vq1.leftChop(vq1.indexOf("WHERE")+6);
//    System.out.println("vq1 = "+vq1);
    String qry = "UPDATE gkstavkerad SET RBS = RBS - "+umanj+" where RBS > "+oldRBS+" AND "+vq1;
//    System.out.println("qry="+qry);
    Valid.getValid().runSQL(qry);
  }

  void updNalog(double stvID, double stvIP) {
    updNalog(stvID,stvIP,true);
  }
  public void updNalog(double stvID, double stvIP, boolean forReal) {
    double nalID = getMasterSet().getBigDecimal("ID").doubleValue();
    double nalIP = getMasterSet().getBigDecimal("IP").doubleValue();
    BigDecimal newNalID = new BigDecimal((nalID-oldID+stvID));
    BigDecimal newNalIP = new BigDecimal((nalIP-oldIP+stvIP));
    getMasterSet().setBigDecimal("ID",newNalID);
    getMasterSet().setBigDecimal("IP",newNalIP);
    getMasterSet().setBigDecimal("SALDO",newNalID.add(newNalIP.negate()));

    /*
System.out.println("*********************************************************");
System.out.println("   updNalog");
System.out.println("*********************************************************");
System.out.println("nalID     nalIP     oldID    oldIP   newNalID   newNalIP");
System.out.println(nalID+"   "+nalIP+"   "+oldID+"   "+oldIP+"   "+newNalID+"   "+newNalIP);
    */

    if (forReal) {
      setStatus();//pazi!! radi s detailSetom
      if (getMasterSet().getDatabase().getAutoCommit()) {
        getMasterSet().saveChanges();
      } else {
        raTransaction.saveChanges(getMasterSet());
      }
      raMaster.getJpTableView().fireTableDataChanged();
    }
  }

  public void setStatus() {
    getMasterSet().setString("STATUS",calcSTATUS());
  }

  String calcSTATUS() {
    Util ut = Util.getUtil();
    BigDecimal nID = ut.setScale(getMasterSet().getBigDecimal("ID"),2);
    BigDecimal nIP = ut.setScale(getMasterSet().getBigDecimal("IP"),2);
    BigDecimal nKI = ut.setScale(getMasterSet().getBigDecimal("KONTRIZNOS"),2);
    BigDecimal nSAL = ut.setScale(getMasterSet().getBigDecimal("SALDO"),2);
    BigDecimal nula = Aus.zero2;
    if (getDetailSet().getRowCount() == 0) {
      return "N";
    }
    if (nSAL.compareTo(nula) != 0) {
      return "N";
    }
    if (nKI.compareTo(nID) != 0) {
      return "N";
    }
    return "S";
  }

  void loadStavka() {
    loadStavka(getDetailSet());
  }
  public void loadStavka(com.borland.dx.dataset.DataSet ds) {
    oldID = ds.getBigDecimal("ID").doubleValue();
    oldIP = ds.getBigDecimal("IP").doubleValue();
    oldRBS = ds.getInt("RBS");
  }

  public void newStavka() {
    oldID = 0.0;
    oldIP = 0.0;
  }

  boolean askDialog(String msgObr) {
    return askDialog(msgObr,"Obrada naloga",false);
  }
  boolean askDialog(String msgObr, String titl, boolean saPokrivanjem, boolean silent, boolean defaultPokriv) {
    if (!silent) return askDialog(msgObr,"Obrada naloga",saPokrivanjem);
    jcMsg.setSelected(saPokrivanjem && defaultPokriv);
    return true;
  }
  boolean askDialog(String msgObr, String titl, boolean saPokrivanjem) {
    jlMsg.setText(msgObr);
    jcMsg.setVisible(saPokrivanjem);
    jcMsg.setSelected(saPokrivanjem);
    int ret = JOptionPane.showConfirmDialog(
    jpMaster.getTopLevelAncestor(),
    jpMsg,
    titl,
    JOptionPane.YES_NO_OPTION
    );
    return ret == JOptionPane.YES_OPTION;
  }
  boolean isDoknjizavanje() {
    String _dcvrnal = null;
//    if (getMasterSet().getRowCount() == 0) {
      if (((preSelNalozi)getPreSelect()).isVrnalEntered()) {//cvrnal je upisan
        _dcvrnal = getPreSelect().getSelRow().getString("CVRNAL");
      } else {
        return false;
      }
//    } else {
//      _dcvrnal = getMasterSet().getString("CVRNAL");
//    }
    return _dcvrnal
    		.equals(frmParam.getParam("gk","vrnal_doknjiz","","Vrsta naloga u kojoj se vrsi doknjizavanje temeljnica"));

    }
  boolean isDoknjizPrometNula() {
    return "D"
    		.equals(frmParam.getParam("gk","doknj_promet0","D","Mora li ID=IP=KONTRIZNOS=0 na nalogu za doknjizavanje"));
  }
  boolean isTransferSK() {
    if (obrada.fKnjizenje == null) return false;
    raKnjizenje currKnjizenje = obrada.fKnjizenje.getKnjizenje();
    if (!currKnjizenje.isSKRacKnj()) return false;
    if (currKnjizenje.uistavke == null) return false;
    if (currKnjizenje.skstavke == null) return false;
    if (currKnjizenje.skstavke.getRowCount() == 0) return false;
    if (currKnjizenje.uistavke.getRowCount() == 0) return false;
    return true;
  }

  void obrNaloga_action() {
    raMaster.getSelectionTracker().removeFromSelection(getMasterSet());
    new Thread() {
      public void run() {
        obrNaloga_inThread(false, false, false, false);
      }
    }.start();
  }
  boolean masovnaObrNaloga_defPokr = false;
  boolean skErrorsOccured = false;
  private boolean alwaysAskAutoFixVrdok() {
    return frmParam.getParam("gk", "pitajfixvrdok", "N", "Pitati uvijek za automatski popravak jednakih vr.dok. pri (masovnoj) obradi naloga").equals("D");
  }
  void masovnaObrNaloga_action() {
    if (raMaster.getSelectionTracker().countSelected() == 0) {
      JOptionPane.showMessageDialog(raMaster, "Nema odabranih naloga za obradu! Odaberite ih tipkom ENTER!","Obavijest",JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (!askDialog("Obraditi odabrane naloge ?", "Višestruka obrada naloga", true)) return;
    masovnaObrNaloga_defPokr = jcMsg.isSelected();
    final boolean autoRepairUniqueSK = (skErrorsOccured||alwaysAskAutoFixVrdok())?askDialog("Automatski popraviti jednake brojeve dokumenata dodavanjem \".\" ?"):false;
    final boolean batchObr = askDialog("Nastaviti sa obradom ako obrada pojedinog naloga ne uspije?");
    new Thread() {
      public void run() {
        raMaster.getJpTableView().enableEvents(false);
        getMasterSet().first();
        do {
          String status = getMasterSet().getString("STATUS");
          if (raMaster.getSelectionTracker().isSelected(getMasterSet()) && status.equals("S")) {
            refilterDetailSet();
            //        if (!raObrIzvoda.getRaObrIzvoda().obradaIzvoda(this,pokriv)) {
            if (!obrNaloga_inThread(true, masovnaObrNaloga_defPokr, autoRepairUniqueSK, batchObr)) {
              if (!batchObr) {
                JOptionPane.showMessageDialog(
                raMaster,
                "Nalog "+getMasterSet().getString("CNALOGA")+" neuspješno obraðen! Prekidam!",
                "Obrada naloga",JOptionPane.ERROR_MESSAGE);
                break;
              }
            } else {
              raMaster.getJpTableView().fireTableDataChanged();
            }
          }
        } while (getMasterSet().next());
        raMaster.getSelectionTracker().clearSelection();
        raMaster.getJpTableView().enableEvents(true);
      }
    }.start();
  }

  private hr.restart.sisfun.raDelayWindow rdw = null;
  private void startProcessMessage() {
    if (obrada.fKnjizenje == null) {
      rdw = hr.restart.sisfun.raDelayWindow.show(this,"Obrada","Obrada naloga u tijeku ..........");
    } else {
      obrada.fKnjizenje.setProcessMessage("Obrada naloga u tijeku...");
      rdw = obrada.fKnjizenje.dwin;
    }
  }
  private void clearProcessMessage() {
    if (rdw == null) return;
    rdw.close();
    rdw = null;
  }
//  private raPreparedStatement checkUnqSkstavke = new raPreparedStatement("skstavke",raPreparedStatement.COUNT);
  private boolean retValue;
  private boolean chkExistStavkeSK(final boolean autoRepairUnique) throws Exception {
    retValue = false;
    raProcess.runChild(frmNalozi.this, "Provjera u tijeku", "Provjera stavki salda konti", new Runnable() {
      public void run() {
        raProcess.installErrorTrace(null, "Greške pri provjeri stavaka salda konti");
        setDetailSet(dm.getGkstavkerad());
        refilterDetailSet(); //zove se i u obrada.obradaNaloga(...) pa optimizirati ?
        boolean snimitiPromjene = false;
        java.util.HashSet unqset = new java.util.HashSet();
        String[] unqkeys = new String[] {"CPAR", "VRDOK", "BROJKONTA", "BROJDOK", "BROJIZV"};
        for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
          if (raKonta.isSaldak(getDetailSet().getString("BROJKONTA"))) {
            //usput provjeriti da li su uneseni partner, vrsta i broj dokumenta
            /**@todo: static provjeru istog toga na svim gkstavkerad (a i grstavke) */
            String stavkaerr = "Stavka RBS="+getDetailSet().getInt("RBS")+": ";
            hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
            if (!lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",(getDetailSet().getInt("CPAR")+"")))
                raProcess.addError(stavkaerr+"Nepostojeci partner!");
            if (isEmpty("BROJDOK")) raProcess.addError(stavkaerr+"Nije upisan broj dokumenta!");
            if (isEmpty("DATDOSP")) {
              getDetailSet().setTimestamp("DATDOSP", getDetailSet().getTimestamp("DATDOK"));
              snimitiPromjene = true;
            }

            String unky = getConcatKey(unqkeys, getDetailSet());
            if (!unqset.add(unky)) {
              if (autoRepairUnique) {
                while (true) {
                  getDetailSet().setString("BROJDOK", getDetailSet().getString("BROJDOK")+".");
                  if (unqset.add(getConcatKey(unqkeys, getDetailSet()))) {
                    snimitiPromjene = true;
                    break;
                  }
                }
              } else {
                raProcess.addError(stavkaerr+" Zapis postoji-(P+VD+KTO+BRDOK+IZV) "+unky);
                skErrorsOccured = true;
              }
            }
            if (autoRepairUnique) {//provjera i ispravak u skstavke
              while (true) {
/*                for (int i = 0; i < unqkeys.length; i++)  {
                  checkUnqSkstavke.setValue(unqkeys[i],raVariant.getDataSetValue(getDetailSet(), unqkeys[i]), true);
                }
                checkUnqSkstavke.setValue("BROJKONTA", getDetailSet().getString("BROJKONTA"),true);
                if (checkUnqSkstavke.isExist()) {*/
                int skrowcount = Skstavke.getDataModule().getRowCount(Condition.whereAllEqual(unqkeys,getDetailSet()));
                if (skrowcount>0) {
                  getDetailSet().setString("BROJDOK", getDetailSet().getString("BROJDOK")+".");
                  snimitiPromjene = true;
                } else {
                  break;
                }
              }
            }

            retValue=true;
          }
        }
        if (snimitiPromjene) getDetailSet().saveChanges();
      }
    });
    if (raProcess.getErrors().countErrors() != 0 || raProcess.isFailed())
        throw new Exception("Greške u provjeri stavaka salda konti!");
    return retValue;
  }
  private String getConcatKey(String[] unqkeys, com.borland.dx.dataset.ReadRow row) {
    String unky = "";
    for (int i = 0; i < unqkeys.length; i++)  {
      unky = unky+"+"+hr.restart.db.raVariant.getDataSetValue(row, unqkeys[i]).toString();
    }
    return unky;
  }
  private boolean isEmpty(String colName) {
    return raKnjizenje.isEmpty(colName, getDetailSet());
  }

  boolean obrNaloga_inThread(boolean silent, boolean defaultPokriv, boolean autoRepairUniqueSK, boolean batch) {
    String strSTATUS = getMasterSet().getString("STATUS");
    String strCNAL = getMasterSet().getString("CNALOGA");
    String msgObr = "";
    boolean isObrNalogaSucc = false;
    if (strSTATUS.equals("N")) {
      msgObr = "Nalog nije spreman za obradu!";
    }
    //Obrada
    else if (strSTATUS.equals("S")) {
      boolean isTranSK = isTransferSK();
      if (obrada.fKnjizenje == null/* || getMasterSet().getString("CVRNAL").equals("00")*/) {
        try {
          isHereza = chkExistStavkeSK(autoRepairUniqueSK);
        } catch (Exception ex) {
          if (!batch) raProcess.report(ex.getMessage());
          return false;
        }
      } else isHereza = false;
/////////////////////
//System.out.println("isTranSK = "+isTranSK);
//System.out.println("isHereza = "+isHereza);
/////////////////////
      if (isDoknjizavanje()) {
        //dohvat naloga u koji treba doknjiziti
        nalogdoknjiz = getNalogDoknjiz();
        System.out.println(nalogdoknjiz);
        if (nalogdoknjiz == null) return false;
      } else {
        nalogdoknjiz = null;
      }
      if (askDialog((nalogdoknjiz == null)?"Želite li obraditi (proknjižiti) nalog?":"Želite li doknjižiti stavke ovog naloga u nalog "+nalogdoknjiz.getString("CNALOGA"),(isTranSK||isHereza)?"Obrada sa prijenosom u SK":"Obrada naloga",(isTranSK||isHereza),silent,defaultPokriv)) {
        startProcessMessage();
        obradaInProgress = true;
        if (isHereza) {
          //ako ima hereticnih stavaka sk u nalogu one ce se pokriti u nize navedenom raKnjizenjeSK.copyGKuSK(...
          obrada.pokriti = false;
        } else {
          obrada.pokriti = jcMsg.isSelected();
        }
        copyGKuSKsucc = true;
        boolean obtrans = new raLocalTransaction() {
          public boolean transaction() throws Exception {
            boolean _inobrtr = obrNalTransaction();
            boolean _incommittrans = obrada.commitTransferAfterObradaNaloga();//u istoj transakciji kao i obrada
            return _inobrtr && _incommittrans;
          }
        }.execTransaction();
        if (obtrans /*&& obrada.commitTransferAfterObradaNaloga()*/) {
          //obrada.fKnjizenje = null;
          disposeFKnjizenje();
          msgObr = "Nalog je uspješno obraðen!";
          dM.getSynchronizer().markAsDirty("pokriveni");
          isObrNalogaSucc = true;
        } else {
          msgObr = "Obrada naloga je neuspješna!";
          isObrNalogaSucc = false;
          getMasterSet().refresh();
          getDetailSet().refresh();
        }
        obradaInProgress = false;
      }
    }
    //Rasknji?avanje
    else if (strSTATUS.equals("K")) {
      System.out.println("cgkstavke like '"+getMasterSet().getString("CNALOGA")+"%'");
      raProcess.runChild("Provjera", "Provjera salda konti stavaka...", new Runnable() {
        public void run() {
          DataSet ds = Skstavke.getDataModule().getTempSet("cgkstavke like '"+getMasterSet().getString("CNALOGA")+"%'");
          ds.open();
          raProcess.yield(ds);
        }
      });
      DataSet sk = (DataSet) raProcess.getReturnValue();
      DataSet ui = UIstavke.getDataModule().getTempSet(Condition.in("CSKSTAVKE",  sk));
      
      boolean pok = false;
      for (sk.first(); sk.inBounds(); sk.next())
        if (sk.getBigDecimal("SALDO").compareTo(sk.getBigDecimal("SSALDO")) != 0) pok = true;
      
      if (pok) {
        msgObr = "Nalog je nemoguæe rasknjižiti jer sadrži stavke sada konti koje su pokrivene!";
      } else if (askDialog("Nalog je obraðen. Želite li poništiti obradu?")) {
        startProcessMessage();
        
        rdw.setMessage("Provjera knjiženja iz robnog ...");
        
        String god = getMasterSet().getString("GOD").substring(2, 4);
        String vrnal = getMasterSet().getString("CVRNAL");
        String rbr = vl.maskZeroInteger(new Integer(getMasterSet().getInt("RBR")), 4);
        String brnal = god.concat(vrnal).concat(rbr); 
        
        DataSet di = doki.getDataModule().getTempSet(Condition.equal("BRNAL",  brnal));
        di.open();
        DataSet du = Doku.getDataModule().getTempSet(Condition.equal("BRNAL",  brnal));
        du.open();
        DataSet mi = Meskla.getDataModule().getTempSet(Condition.equal("BRNAL",  brnal));
        mi.open();
        DataSet mu = Meskla.getDataModule().getTempSet(Condition.equal("BRNALU",  brnal));
        mu.open();
        boolean robno = di.rowCount() > 0 || du.rowCount() > 0 || mi.rowCount() > 0 || mu.rowCount() > 0;
        
        System.out.println("Rasknj: saldak = " + sk.rowCount() + "  robno: " + robno);
        
        rdw.setMessage("Rasknjižavanje naloga ...");
        obradaInProgress = true;
        setDetailSet(dm.getGkstavkerad());
        if (obrada.raskNaloga(this, sk.rowCount() > 0 ? sk : null)) {
          msgObr = "Obrada naloga je uspješno poništena!";
          
          if (sk.rowCount() > 0) {
            rdw.setMessage("Ažuriranje statusa salda konti stavaka ...");
            ui.open();
            if (ui.rowCount() > 0 && !robno) {
              for (sk.first(); sk.inBounds(); sk.next()) sk.setString("CGKSTAVKE", "");
              raTransaction.saveChanges((QueryDataSet) sk);
            } else {
              ui.deleteAllRows();
              sk.deleteAllRows();
              
              raTransaction.saveChanges((QueryDataSet) sk);
              raTransaction.saveChanges((QueryDataSet) ui);
            }
          } 
          if (robno) {
            for (di.first(); di.inBounds(); di.next()) {
              di.setString("BRNAL", "");
              di.setAssignedNull("DATKNJ");
              di.setString("STATKNJ", "N");
            }
            for (du.first(); du.inBounds(); du.next()) {
              du.setString("BRNAL", "");
              du.setAssignedNull("DATKNJ");
              du.setString("STATKNJ", "N");
            }
            for (mi.first(); mi.inBounds(); mi.next()) {
              mi.setString("BRNAL", "");
              mi.setAssignedNull("DATKNJ");
              mi.setString("STATKNJI", "N");
            }
            for (mu.first(); mu.inBounds(); mu.next()) {
              mi.setString("BRNALU", "");
              mi.setAssignedNull("DATKNJU");
              mi.setString("STATKNJU", "N");
            }
            raTransaction.saveChanges((QueryDataSet) di);
            raTransaction.saveChanges((QueryDataSet) du);
            raTransaction.saveChanges((QueryDataSet) mi);
            raTransaction.saveChanges((QueryDataSet) mu);
          }
          
          isObrNalogaSucc = true;
        } else {
          msgObr = "Poništenje obrade naloga je neuspješno!";
          isObrNalogaSucc = false;
          getMasterSet().refresh();
          getDetailSet().refresh();
        }
        obradaInProgress = false;
      }
    }
    clearProcessMessage();
    if (batch) return isObrNalogaSucc;//preskoci ovo dolje ako je batch
    if (!msgObr.equals("")) {
      if (!(silent && isObrNalogaSucc)) {
        JOptionPane.showMessageDialog(jpMaster.getTopLevelAncestor(),msgObr,"Obrada naloga",JOptionPane.INFORMATION_MESSAGE);
        raMaster.getJpTableView().fireTableDataChanged();
        changeDetailViewStatus();
      }
      if (!copyGKuSKsucc) raKnjizenjeSK.getRaKnjizenjeSK().showErrors();
    }
    return isObrNalogaSucc;
  }

  /**
   * koristi predselekciju kao za naloge te nakon nje daje dohvat 
   * proknjizenih naloga za doknjizavanje 
   * @return slog naloga u koji treba doknjiziti stavke
   */
  private ReadRow getNalogDoknjiz() {
    preSelNalozi psDoknjNal = preSelNalozi.getNewPreSelect();
    QueryDataSet nalozizadoknj = Nalozi.getDataModule().getTempSet(
        Condition.equal("STATUS","K")+" AND "+Condition.where("CVRNAL",Condition.NOT_EQUAL,getMasterSet()));
    psDoknjNal.setSelDataSet(nalozizadoknj);
    psDoknjNal.rcbSTATUS.setEnabled(false);
    psDoknjNal.rcbSTATUS.setSelectedIndex(3);
    psDoknjNal.rcbSTATUS.getDataSet().setString("STATUS","K");
    psDoknjNal.showPreselect((JFrame)null,"Odabir naloga za doknjizavanje u ..");
    try {
      nalozizadoknj.addRowFilterListener(new RowFilterListener() {
        public void filterRow(ReadRow arg0, RowFilterResponse arg1) {
          if (arg0.getString("CVRNAL").equals(getMasterSet().getString("CVRNAL"))) {
            arg1.ignore();
          } else {
            arg1.add();
          }
        }
      });
    } catch (TooManyListenersException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    lookupFrame lf = lookupFrame.getLookupFrame((Frame)getWindow(),nalozizadoknj,visibleColsMasterNalozi);
    lf.ShowCenter(true,0,0);
    if (lf.getRetValuesUI() == null) return null;
    return nalozizadoknj;
  }

  void detEscape_action() {
    if (!getMasterSet().getString("STATUS").equals("K") &&
        raDetail.getMode()!='B' && !jpDetail.kcGroup.getJlrBROJKONTA().isEnabled()) {
      if (raDetail.getMode()=='N') jpDetail.clrScr();
      newDetail();
    } else {
      raDetail.getOKpanel().jPrekid_actionPerformed();
    }
  }
  private boolean obrNalTransaction() {
		///////////////
		// System.out.println("Punim CNALOGA = "+jpMaster.jpBrNal.getCNaloga());
		///////////////
    String _cnalstav = null;
    int _drbsplus = -1;
    if (isDoknjizavanje() && nalogdoknjiz != null) {
      //napuni cnaloga na stavkama sa cnalogom od odabranog naloga za doknjizavanje zbog izvoda
      _cnalstav = nalogdoknjiz.getString("CNALOGA");
      _drbsplus = gkQuerys.getMaxGkstavke___RBS(nalogdoknjiz.getString("KNJIG"), 
          nalogdoknjiz.getString("GOD"), nalogdoknjiz.getString("CVRNAL"), 
          nalogdoknjiz.getInt("RBR"),"gkstavke");
      QueryDataSet updZag = Nalozi.getDataModule().getFilteredDataSet(
          Condition.equal("CNALOGA", _cnalstav));
      updZag.open();
      updZag.setBigDecimal("ID",updZag.getBigDecimal("ID").add(getMasterSet().getBigDecimal("ID")));
      updZag.setBigDecimal("IP",updZag.getBigDecimal("IP").add(getMasterSet().getBigDecimal("IP")));
      updZag.setBigDecimal("KONTRIZNOS",updZag.getBigDecimal("KONTRIZNOS").add(getMasterSet().getBigDecimal("KONTRIZNOS")));
      raTransaction.saveChanges(updZag);
    } else {
      _cnalstav = jpMaster.jpBrNal.getCNaloga();
    }
		for (getDetailSet().last(); getDetailSet().inBounds(); getDetailSet().prior()) {
			getDetailSet().setString("CNALOGA", _cnalstav);
			if (_drbsplus>-1) {
			  getDetailSet().setInt("RBS",(getDetailSet().getInt("RBS")+_drbsplus));
			}
			getDetailSet().post();
		}
    if (isHereza) {
      /*  getDetailSet().first();
        while (getDetailSet().inBounds()) {
          getDetailSet().setBigDecimal("TECAJ", new BigDecimal(1));
          getDetailSet().setBigDecimal("DEVID", getDetailSet().getBigDecimal("ID"));
          getDetailSet().setBigDecimal("DEVIP", getDetailSet().getBigDecimal("IP"));
          getDetailSet().next();
        }*/
      //if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKuSK(getDetailSet(),jcMsg.isSelected(),"",hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
      hr.restart.zapod.Tecajevi tec=null;
////////////////
//System.out.println("smarterCopyGKuSK(.... ==> ");
////////////////
      if (!raObrIzvoda.smarterCopyGKuSK(getDetailSet(), tec.getTecaj(getMasterSet().getTimestamp("DATUMKNJ"),tec.getDomOZNVAL()), tec.getJedVal(tec.getDomOZNVAL()), "", tec.getDomOZNVAL(), jcMsg.isSelected())) {
        copyGKuSKsucc = false;
        skErrorsOccured = true;
        return false;
      }
    }
    if (!obrada.obradaNaloga(frmNalozi.this)) {
//      System.out.println("obradaNaloga nije uspijo");
      return false;
    }
    return true;    
  }
  public boolean ValidacijaPrijeIzlazaMaster() {
    if (hr.restart.sk.raGkSkUnosHandler.inSalkon) return false;
    if (validateTransfer()) {
      if (Valid.getTableName(getDetailSet().getQuery().getQueryString()).toLowerCase().equals("gkstavke")) {
        //za svaki slucaj ako je ostao u modu proknjizenog naloga, i neko uleti sa raknjizenje...
        setDetailSet(dm.getGkstavkerad());
      }
      frmGK.getFrmGK().closeNalog();
      //      ((frmGK)raLLFrames.getRaLLFrames().getMsgStartFrame()).closeNalog();
      return true;
    } else return false;
  }
  public boolean beforePreSel() {
    return validateTransfer();
  }

  private boolean alowCommitTransfer = false;

  private String def_confirmTransferMsg = "Temeljnica je nastala prijenosom iz analitièke evidencije. Želite li potvrditi prijenos?";
  private String confirmTransferMsg = def_confirmTransferMsg;
  /**
   * Text poruke koja se pojavljuje na pocetku obrade temeljnice.
   * default: Temeljnica je nastala prijenosom iz analitièke evidencije. ®elite li potvrditi prijenos?
   * Setira se metodom frmNalozi.getFrmNalozi().setConfirmTransferMsg(String).
   * Setira se na default metodom frmNalozi.getFrmNalozi().setConfirmTransferMsg(null).
   * @param msg poruka
   */
  public void setConfirmTransferMsg(String msg) {
    confirmTransferMsg = msg;
  }

  private String def_errorTransferMsg = "Temeljnica je nastala prijenosom iz analitièke evidencije. Potrebno je ili obrisati ili proknjižiti temeljnicu!";
  private String errorTransferMsg = def_errorTransferMsg;
  /**
   * Text poruke koja se pojavljuje kada user pokusa izaci iz temeljnice koja je nastala automatskim knjizenjem.
   * default: Temeljnica je nastala prijenosom iz analitièke evidencije. Potrebno je ili obrisati ili proknji?iti temeljnicu!
   * Setira se metodom frmNalozi.getFrmNalozi().setErrorTransferMsg(String).
   * Setira se na default metodom frmNalozi.getFrmNalozi().setErrorTransferMsg(null).
   * @param msg poruka
   */
  public void setErrorTransferMsg(String msg) {
    if (msg == null) errorTransferMsg = def_errorTransferMsg;
    errorTransferMsg = msg;
  }

  private boolean validateTransfer() {
    if (obrada.fKnjizenje == null) return true;
    if (alowCommitTransfer) {
      int odg = JOptionPane.showConfirmDialog(raMaster,
      confirmTransferMsg,
      "Nalog za knjiženje - temeljnica",JOptionPane.YES_NO_CANCEL_OPTION
      );
      //da = 0; ne = 1; ponisti = 2
      if (odg == 2) return false;
      //ai:30052005 da ne bi slucajno se ovo pozvalo iako je alowCommitTransfer uvijek false
      // pozvalo if (odg == 0) obrada.fKnjizenje.commitTransfer();
       
      //obrada.fKnjizenje = null;
      disposeFKnjizenje();
      return true;
    } else {
      JOptionPane.showMessageDialog(raMaster,
      errorTransferMsg,
      "Nalog za knjiženje - temeljnica",JOptionPane.WARNING_MESSAGE
      );
      return false;
    }
  }
  raAdditionalLookupFilter noSkFilter = new raAdditionalLookupFilter() {
    public boolean isRow(com.borland.dx.dataset.ReadRow row) {
      String saldak = row.getString("SALDAK");
      boolean isSaldak = saldak.equals("D") || saldak.equals("K");
      boolean isRow = !isSaldak;
      return isRow;
    }
  };

  private void toggleSelection() {
    int row = getMasterSet().getRow();
    raMaster.getJpTableView().enableEvents(false);
    for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next()) {
      if (validateSelectionMaster(getMasterSet(),true))
        raMaster.getSelectionTracker().toggleSelection(getMasterSet());
    }
    raMaster.getJpTableView().enableEvents(true);
  }
  private static String changeDatumKnjizenja_ret = "";
  /**
   * Mijenja datum knjizenja neproknjizenog naloga U TRANSAKCIJI
   * @param cnaloga - oznaka naloga u formatu KNJIG-GODINA-CVRNAL-RBR sa 6 znamenaka i vodecim nulama
   * @param newDatum - novi datum knjizenja
   * @return prazan string ako je sve proslo ok, ili poruku za joptionpane sto je poslo po zlu
   */
  public static String changeDatumKnjizenjaTrans(final String cnaloga, final java.sql.Timestamp newDatum) {
    boolean trans_ret = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        changeDatumKnjizenja(cnaloga, newDatum, true);
        return true;
      }
    }.execTransaction();
    if (!trans_ret) changeDatumKnjizenja_ret = "Transakcija nije uspjela";
    return changeDatumKnjizenja_ret;
  }
  /**
   * Mijenja datum knjizenja neproknjizenog naloga (pozvati unutar transakcije (dowithSave))
   * @return prazan string ako je sve proslo ok, ili poruku za joptionpane sto je poslo po zlu
   * @param updateNalog - treba li update-ati i nalog ili samo stavke
   * @param cnaloga - oznaka naloga u formatu KNJIG-GODINA-CVRNAL-RBR sa 6 znamenaka i vodecim nulama
   * @param newDatum - novi datum knjizenja
   */
  public static String changeDatumKnjizenja(final String cnaloga, final java.sql.Timestamp newDatum, boolean updateNalog) {
    changeDatumKnjizenja_ret = "";
    QueryDataSet nalog = hr.restart.baza.Nalozi.getDataModule().getTempSet(Condition.equal("CNALOGA", cnaloga));
    nalog.open();
    if (nalog.getRowCount() == 0) {
      changeDatumKnjizenja_ret = "Nije pronaðen zadani nalog za knjiženje";
      return changeDatumKnjizenja_ret;
    }
    if (nalog.getString("STATUS").equals("K")) {
      changeDatumKnjizenja_ret = "Nalog je veæ proknjižen";
      return changeDatumKnjizenja_ret;
    }
    if (updateNalog) nalog.setTimestamp("DATUMKNJ", newDatum);

    QueryDataSet stavke = hr.restart.baza.Gkstavkerad.getDataModule().getFilteredDataSet(
      Condition.whereAllEqual(new String[] {"KNJIG","GOD","CVRNAL","RBR"},nalog));
    stavke.open();
    if (stavke.getRowCount() > 0) {
      for (stavke.first(); stavke.inBounds(); stavke.next()) {
        stavke.setTimestamp("DATUMKNJ", newDatum);
      }
      raTransaction.saveChanges(stavke);
    }
    if (updateNalog) raTransaction.saveChanges(nalog);
    return changeDatumKnjizenja_ret;
  }
  
  //zove skriptu za pripremu i šalje na zadanu e-mail adresu
  protected void sendData() {
    
    File attachment = GKDataTransfer.serializeData(getMasterSet(), frmParam.getParam("gk", "mode4data", "", "Mod prijenosa podataka mailom iz GK")); // tu zdampaj
    if (attachment == null) return;
    String kome = frmParam.getParam("gk", "mail4data", "andrej@rest-art.hr", "email adresa na koju se šalju podaci iz temeljnice");
    String naslov = "Podaci iz naloga za knjiženje "+getMasterSet().getString("CNALOGA")+" od "+getMasterSet().getTimestamp("DATUMKNJ");
    String txt = "U privitku ...";
    ReportMailDialog.sendMail(attachment, ReportMailDialog.showMailDialog(kome, naslov, txt));
  }
}
