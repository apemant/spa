/****license*****************************************************************
**   file: raMasterDetail.java
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
package hr.restart.util;

import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raSelectTableModifier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;

/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class raMasterDetail extends raFrame implements raPreSelectAware {

sysoutTEST sT = new sysoutTEST(false);

  static java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.util.Res_");

//  javax.swing.JButton jBStavke= new javax.swing.JButton();

  raNavAction rnvStavke = new raNavAction(res.getString("jBStavke"),raImages.IMGSTAV,KeyEvent.VK_F6) {

    public void actionPerformed(ActionEvent e) {

      jBStavke_actionPerformed(e);

    }

  };



  // Za geter i seter

  public static int DELETE = 0;

  public static int DELDETAIL = 1;

  public static int NODEL = 2;

  public static int EMPTYDEL = 3;

  private PreSelect preSelect;

  private String preSelTitle = null;

  private int masterDeleteMode = DELETE;

  private javax.swing.JPanel JPanelMaster ;

  private javax.swing.JPanel JPanelDetail ;

  private com.borland.dx.sql.dataset.QueryDataSet masterSet;

  private com.borland.dx.sql.dataset.QueryDataSet detailSet;

  private boolean SQLFilter = true;

  boolean showNew = false;

  private String[] MasterKey;

  private String[] DetailKey;

  private int versionmaster=1;

  private int versiondetail=1;

  private int versionall=1;

  private int[] VisibleColsMaster  ;

  private int[] VisibleColsDetail  ;

  private String[] var4sum;

  private String[] nas4sum;

  private boolean kumdane=false;

  private boolean userCheck = false;

  private String userCheckMsg;
  // ab.f, ima li smisla ponuditi promjenu korisnika kod gornje poruke
  private boolean userChangePossible = true;

  private hr.restart.sisfun.raUser ralock = hr.restart.sisfun.raUser.getInstance();

//  private hr.restart.sisfun.raLock ralock = hr.restart.sisfun.raLock.getRaLock();

  private raCommonClass raCC = raCommonClass.getraCommonClass();

  startFrame SF;

  public raMatPodaci raMaster;

  public raMatPodaci raDetail;



  public raMasterDetail() {

    this(raMatPodaci.getVerParam("masterversion"),raMatPodaci.getVerParam("detailversion"));

/*

    versionmaster=raMatPodaci.getVerParam("masterversion");

    versiondetail=raMatPodaci.getVerParam("detaiversion");

    raMDInit();

*/

  }

/*

  public raMasterDetail(int aversion) {

    versionall = aversion;

    raMDInit();

  }

  public raMasterDetail(int vermaster, int verdetail, int verall) {

    versionmaster = vermaster;

    versiondetail = verdetail;

    versionall = verall;

    raMDInit();

  }

*/

  public raMasterDetail(int vermaster, int verdetail) {

    versionmaster = vermaster;

    versiondetail = verdetail;

    raMDInit();

  }

  /**

   * Metoda kojom se inicijalizira master dio

   */

  void raMDInit() {

//    versiondetail = versionall==2?2:versiondetail;

    restoreUserCheckMessage();

    instanceRaMaster();
    
    instanceRaDetail();

    raMaster.setRaMasterDetail(this);

    raDetail.setRaMasterDetail(this);

//    raMaster.getNavBar().getColBean().setSaveSettings(false);

//    raDetail.getNavBar().getColBean().setSaveSettings(false);

    /*maknut RnvCopyCurr jer se na masteru uglavnom nalaze kumulativi detaila 
    koji se onda kopiraju na novi master bez detaila i onda kumulativi ne valjaju*/ 
    raMaster.removeRnvCopyCurr();
    
    raDetail.setToggleTableEnabled(false);

    int masterY = 70;

    raMaster.addOption(rnvStavke,3);

    raMaster.setLocation(0,masterY);

    if (versionall == 2) {

      if (versionmaster == 1) {

        String Tit1 = raMaster.jTabPane.getTitleAt(1);

        String Tit0 = raMaster.jTabPane.getTitleAt(0);

        Component comp1 = raMaster.jTabPane.getComponentAt(1);

        JPanel jpMD = getFixedJpMD();

        raMaster.jTabPane.removeAll();

        raMaster.jTabPane.addTab(Tit0,jpMD);

        raMaster.jTabPane.addTab(Tit1,comp1);



        //tipkice

        raDetail.getNavBar().registerNavBarKeys(raMaster);

        raMaster.addKeyListener(raDetail.MojKeyListener);

        //

      } else if (versionmaster == 2) {



      } else if (versionmaster == 3) {



      }

//      System.out.println("jptvCont: "+jptvCont);

    }

  }



  private JPanel getFixedJpMD() {

    JPanel jpmd = new JPanel(new BorderLayout());

    raMaster.getJpTableView().setFewRowSize();

    switchNavBar(true);

    jpmd.add(raMaster.getJpTableView(),BorderLayout.NORTH);

    jpmd.add(raDetail.getContentPane(),BorderLayout.CENTER);

    return jpmd;

  }



  void switchNavBar(boolean master) {

    raNavBar detNavBar = raDetail.getNavBar();

    raNavBar masNavBar = raMaster.getNavBar();

    detNavBar.setVisible(false);

    masNavBar.setVisible(false);

    Container cont;

     if (versionmaster == 3) {

      cont = raMaster.getContentPane();

    } else {

      cont = raMaster.getJpTableView();

    }

    cont.remove(detNavBar);

    cont.remove(masNavBar);

    if (master) {

      cont.add(masNavBar,BorderLayout.NORTH);

      raCC.EnabDisabAll(masNavBar,true);

      masNavBar.setVisible(true);

    } else {

      cont.add(detNavBar,BorderLayout.NORTH);

      raCC.EnabDisabAll(detNavBar,true);

      detNavBar.setVisible(true);

    }

  }





  public void show() {

    findSF();

    SF.showFrame(raMaster);

  }

  /**

   * Metoda kojom se prikazuje master dio

   */

  public void go() {

    show();

//    raMaster.pack();

//    raMaster.show();

  }

  void findSF() {

    if (SF == null) {

//      SF = startFrame.getStartFrame();

      SF = raLLFrames.getRaLLFrames().getMsgStartFrame();

    }

  }

/**

 * FUNKCIJE MASTER DIJELA

 */

  /** Postavlja vidljive kolone u dbTable-u master dijela

   *

   * newVisibleCols je polje int koje kaze koje kolone se prikazuju

   * @param newVisibleCols

   */

  public void setVisibleColsMaster(int[] newVisibleCols) {

    this.VisibleColsMaster=newVisibleCols;

    raMaster.setVisibleCols(this.VisibleColsMaster);

  }

  public void Funkcija_ispisa_master(){

    raMaster.Funkcija_ispisa();

  }

  /**

   * Postavlja naslov master dijela

   */

  public void setNaslovMaster(String naslov){

    raMaster.setTitle(naslov);

  }

  /**

   * Postavlja naslov detail dijela

   */

  public void setNaslovDetail(String naslov){

    raDetail.setTitle(naslov);

  }

  /**

   * Potrebno iskodirati za provjeru prilikom snimanja podataka

   * mode= 'N'(novi) 'I' izmjena

   */

  public boolean ValidacijaMaster(char mode) {

    return true;

  }

  private boolean validateRange = true;

  public void setValidateRange(boolean b) {

    validateRange = b;

  }

  public boolean isValidateRange() {

    return validateRange;

  }

  private boolean interValid(char mode) {

    if (!isValidateRange()) return true;

    if (getPreSelect() == null) return true;

//    getPreSelect().copySelValues();

    if (getPreSelect().validateEntry()) return true;

    JOptionPane.showMessageDialog(getJPanelMaster(),

      "Uneseni podaci ne odgovaraju zadanim kriterijima u predselekciji",

      "Greška",

      JOptionPane.ERROR_MESSAGE);

    return false;

  }



  /**

   * Izvrsava se prije ulaska u unos novog zaglavlja

   */

  public void EntryPointMaster (char mode) {



  }

  /**

   * Izvrsava se kod izlaza iz master dijela

   */

  public boolean ValidacijaPrijeIzlazaMaster(){

    return true;

  }
  
  public boolean allowRowChangeMaster(int oldrow, int newrow) {
    if (!raMaster.isEnabled()) return false;
    if (!raDetail.isShowing()) return true;
    if (!ValidacijaPrijeIzlazaDetail()) return false;
    ZatvoriOstaloDetail();
    return true;
  }

  /**

   * Izvrsava se kod izlaza iz unosa master dijela

   */

  public void ExitPointMaster (char mode){

  }

  /**

   * Izvrsava ValDPEscape od mastera (vidi raMatPodaci.ValDPEscape)

   */

  public boolean ValDPEscapeMaster(char mode) {

    return true;

  }

  /**

   * Oznacava polje u panelu za unos mastera koje ce biti fokusirano

   */

  public void SetFokusMaster(char mode) {

  }

  /**

   * Provjera kod brisanja

   */

  public boolean DeleteCheckMaster(){

    return true;

  }

  /**

   * Izvrsava se iza snimanja mastera

   */

  public void AfterAfterSaveMaster(char mode) {

    if (mode=='N') {

      raMaster.setMode('I');

      //

      raMaster.AfterAfterSave('I');

      //

      applySQLFilter();

      showNew = true;

//      jBStavke_actionPerformed(null);
      raMaster.jeprazno();

      SwingUtilities.invokeLater(new Runnable() {
      
        public void run() {
          rnvStavke.actionPerformed(null);

        }
      });
      

//      raDetail.rnvAdd_action();

//      raDetail.setLockedMode('N');

    }
  }

   public void AfterDeleteMaster(){}

   public void AfterSaveFailedMaster(char mode) {}

  /**

   * FUNKCIJE VEZANE ZA DETAIL DIO

   */

/**

 * Okida se na gumb ispis

 */

  public void Funkcija_ispisa_detail(){

    raDetail.Funkcija_ispisa();

  }

   /**

    * Provjera kod unosa detail dijela

    */

  public boolean ValidacijaDetail(char mode) {

    return true;

  }

  /**

   * postavlja vidljive kolone u detail dijelu

   */

  public void setVisibleColsDetail(int[] newVisibleCols) {

    this.VisibleColsDetail=newVisibleCols;

    raDetail.setVisibleCols(this.VisibleColsDetail);

  }

/**

 * Izvrsava se nakon snimanja detaljnog dijela

 */



  public void AfterSaveDetail(char mode) {

  }

/**

 * Tu se masterdetailu zadaje predselekcijski ekran. Ako je on zadan pojavljuje se gumb za predselekciju u

 * toolbaru mastera koji zove PreSelect.showPreselect(raMaster,titl). Ako je treci parametar true onda metoda

 * izjednacuje Preselect.getSelDataSet() i raMasterDetail.getMasterSet() tako da, ako su oba vec setirana prioritet

 * ima selDataSet (masterSet = selDataSet, ako bilo koji od njih nije setiran metoda ga setira onim drugim, ako niti jedan

 * nije setiran ne radi ni?ta.

 * @param pres predselekcijski ekran

 * @param titl naslov predselekcijskog ekrana, ako je null naslov je defaultni (Predselekcija)

 * @param handleDataSets da li da izjednaci datasetove

 */

  public void setPreSelect(PreSelect pres,String titl, boolean handleDataSets) {

    preSelect = pres;

    preSelTitle = titl;

    if (handleDataSets) handleDataSetsWithPreSel();

  }

  private void handleDataSetsWithPreSel() {

    if (preSelect.getSelDataSet() == null && getMasterSet() == null) return;

    if (preSelect.getSelDataSet() == null) {

      preSelect.setSelDataSet(getMasterSet());

    } else {

      if (preSelect.getSelDataSet() instanceof com.borland.dx.sql.dataset.QueryDataSet)

       setMasterSet((com.borland.dx.sql.dataset.QueryDataSet)preSelect.getSelDataSet());

    }

  }

  /**

   * Poziva {@link #setPreSelect(PreSelect, String, boolean) setPreSelect(pres,titl,false)}

   * @param pres predselekcijski ekran

   * @param titl naslov predselekcijskog ekrana, ako je null naslov je defaultni (Predselekcija)

   */

  public void setPreSelect(PreSelect pres,String titl) {

    setPreSelect(pres,titl,false);

  }

  /**

   * Poziva {@link #setPreSelect(PreSelect, String, boolean) setPreSelect(pres,null,false)}

   * @param pres predselekcijski ekran

   */

  public void setPreSelect(PreSelect pres) {

    setPreSelect(pres,null,false);

  }

  public PreSelect getPreSelect() {

    return preSelect;

  }

  /**

   * Naslov predselekcije

   * @param titl zadani naslov, ako je null postavlja se defaultni (Predselekcija)

   */

  public void setPreSelTitle(String titl) {

    preSelTitle = titl;

  }

  public String getPreSelTitle() {

    return preSelTitle;

  }

  /**

   * kreira i dodaje gumbich za predselekciju ako to vec nije. Zove se prije beforeShowMaster

   */

  public void initPreSelect() {

    if (!initializePreSel) return;

    if (preSelect == null) return;

    //kao predzadnja

    int position = raMaster.getNavBar().getOptionIndex(raMaster.getNavBar().rnvToggleTable);

    raNavAction rnvPreSel = new raNavAction("Predselekcija",raImages.IMGZOOM,KeyEvent.VK_F12) {

      public void actionPerformed(ActionEvent e) {

        preSel_action();

      }

    };

    raMaster.addOption(rnvPreSel,position,false);

    initializePreSel = false;

  }

  private boolean initializePreSel = true;



  public void preSel_action() {

    if (!beforePreSel()) return;

    preSelect.showPreselect(raMaster,preSelTitle);

    raMaster.fireTableDataChanged();

    raMaster.jeprazno();

  }



  /**

   * zove se prije prikazivanja predselekcije klikom na onaj gumbic sa povecalom (F12)

   * @return ako vratis false nece prikazati preselect

   */

  public boolean beforePreSel() {

    return true;

  }



/**

 * ako ti se ne svidja da nakon noovog opet ide u novi returnaj false, a u medjuvremenu mozes nesto i nadoprogramirati

 */

  public boolean AfterAfterSaveDetail(char mode) {

    return true;

  }

  public void AfterSaveFailedDetail(char mode) {}

  public boolean ValidacijaPrijeIzlazaDetail(){

    return true;

  }

  public void EntryPointDetail(char mode) {

  }

  public void ExitPointDetail (char mode){

  }

  /**

   * Izvrsava ValDPEscape od detaila (vidi raMatPodaci.ValDPEscape)

   */

  public boolean ValDPEscapeDetail(char mode) {

    return true;

  }

  public void SetFokusDetail(char mode) {

  }

  public boolean DeleteCheckDetail(){

    return true;

  }



  public void AfterDeleteDetail(){

  }



  public void ZatvoriOstaloDetail() {

  }



  public void ZatvoriOstaloMaster() {

  }

  /**

   *

   * @param vis 0-nevidljivo, 1-vidljiva detail.tablica, 2-vidljiv detail.detail

   *

   */

/*

  void raMaster_stavVisible(int vis) {

    if (vis==0) {

      enableMasterNavbar(true);

      enableMasterTable(true);

    } else if (vis == 1) {

      enableMasterNavbar(false);

      enableMasterTable(true);

    } else if (vis == 2) {

      enableMasterTable(false);

    }

  }

*/

  private void enableMasterTable(boolean en) {

    ((JraTable2)raMaster.getJpTableView().getMpTable()).setEnabled(en);

  }

  private void enableDetailTable(boolean en) {

    ((JraTable2)raDetail.getJpTableView().getMpTable()).setEnabled(en);

  }

  private void enableMasterNavbar(boolean master) {

    if (versionall > 1)

      visibleMasterNavBar(master);

    else {

      raCC.EnabDisabAll(raMaster.getNavBar(),master);

      raCC.EnabDisabAll(raDetail.getNavBar(),!master);

      if (master) {

        raMaster.jeprazno();

      } else {

        raDetail.jeprazno();

      }

    }

  }



  private void visibleMasterNavBar(boolean master) {

    switchNavBar(master);

  }

/////

  /**

   *

   * @param m mod koji moze biti 1,2,3,4 a znaci:

   * <pre>

   * -------------------------------------------------------------

   * enabled&visible        m.navbar  m.table  d.navbar  d.table

   * -------------------------------------------------------------

   * 1 - browsanje mastera    true     true     false     false

   * 2 - browsanje detaila    false    true     true      true

   * 3 - unos detaila         false    false    false     false

   * 4 - unos mastera         false    false    false     false

   * </pre>

   */

  public void setMasterDetailMode(int m) {

//System.out.println("setMasterDetailMode("+m+")");

    if (m==1) setScrBrwMaster();

    else if (m==2) setScrBrwDetail();

    else if (m==3) setScrEntDetail();

    else if (m==4) setScrEntMaster();

  }



  private void setScrBrwMaster() {

    setScrMasterDetail(true,true,false,false);

  }



  private void setScrBrwDetail() {

    setScrMasterDetail(false,true,true,true);

  }



  private void setScrEntDetail() {

    setScrMasterDetail(false,false,false,false);

    raCC.EnabDisabAll(raDetail.getNavBar(),false);

  }



  private void setScrEntMaster() {

    setScrMasterDetail(false,false,false,false);

  }



  private void setScrMasterDetail(boolean m_navbar,boolean m_table,

                                  boolean d_navbar,boolean d_table) {

    enableMasterNavbar(m_navbar);//true

    enableMasterTable(m_table);//true

    enableDetailTable(d_table);//false

  }



/////


  private boolean startShowingDetail = false;
  public boolean isStartShowingDetail() {
    return startShowingDetail;
  }
  /**
   * Pritisak na Stavke
   */
  public void jBStavke_actionPerformed(java.awt.event.ActionEvent e) {

//    setJPanelDetail(getJPanelDetail()); //jer kolega koristi istu instancu istog panela za dva raDetaila

    if (!(raDetail.isShowing() || isStartShowingDetail())){

      startShowingDetail = true;
      raDetail.pack();

      navigateMasterDetail(true);

      hr.restart.start.setFRAME_MODE();

      if (hr.restart.start.FRAME_MODE != raFrame.PANEL) calcStavkeLocation();

      raDetail.setkum_tak(kumdane);

      raDetail.setstozbrojiti(var4sum);

      raDetail.setnaslovi(nas4sum);
    }

//    raDetail.show();

    findSF();

    SF.showFrame(raDetail);
    startShowingDetail = false;
  }

  private boolean calcedLocation = false;
  void calcStavkeLocation() {
    
    if (calcedLocation) return;
    calcedLocation = true;

    int odmak = 50;

    int xloc=0;

    int yloc=0;

    Dimension screenS = hr.restart.start.getSCREENSIZE();

    Dimension masterS = raMaster.getSize();

    Dimension detailS = raDetail.getSize();

    Point endMaster = new Point(raMaster.getLocation().x+masterS.width,raMaster.getLocation().y+masterS.height);

    if (detailS.width >= masterS.width) {

      xloc = raMaster.getLocation().x + odmak;

    } else {

      xloc = endMaster.x - detailS.width + odmak;

    }

    if (detailS.height >= masterS.height) {

      yloc = raMaster.getLocation().y + odmak;

    } else {

      yloc = endMaster.y - detailS.height + odmak;

    }

    if (xloc < 0) xloc = 0;

    if (yloc < 0) yloc = 0;

    if (xloc+detailS.width > screenS.width) xloc = screenS.width - detailS.width;

    if (yloc+detailS.height > screenS.height) yloc = screenS.height - detailS.height;

    raDetail.setLocation(xloc,yloc);



/* STARO

      if ((raMaster.getLocation().x+150+raDetail.getPreferredSize().width) < raMaster.getToolkit().getScreenSize().width )

        x=raMaster.getLocation().x+150;

      else

        x=raMaster.getLocation().x-150;



      if ((raMaster.getLocation().y+150+raDetail.getPreferredSize().height) < raMaster.getToolkit().getScreenSize().height)

        y=raMaster.getLocation().y+150;

      else

        y=raMaster.getLocation().y-150;



      raDetail.setLocation(x,y);

*/



  }



  /**

   * Poziva se neposredno prije prikazivanja detail raMatPodataka

   */

  public void beforeShowDetail() {

  }

  /**

   * Poziva se neposredno prije prikazivanja master raMatPodataka

   */

  public void beforeShowMaster() {

  }

  /**

   * Postavlja se poruka koja upozorava korisnika da nema pravo na taj dokument, ako poruke nema postavlja se dafaultna koja glasi

   * 'Dokument je izradio drugi korisnik. Mogu\u0107 je samo pregled !'

   * @param msg

   */

  public void setUserCheckMsg(String msg) {

    userCheckMsg = msg;
    userChangePossible = true;

  }


  /**

   * Postavlja se poruka koja upozorava korisnika da nema pravo na taj dokument, ako poruke nema postavlja se dafaultna koja glasi

   * 'Dokument je izradio drugi korisnik. Mogu\u0107 je samo pregled !'

   * @param msg poruka
   * @param enablePromjena treba li staviti mogucnost promjene korisnika

   */

  public void setUserCheckMsg(String msg, boolean enablePromjena) {

    userCheckMsg = msg;
    userChangePossible = enablePromjena;
  }



  public String getUserCheckMsg() {

    return userCheckMsg;

  }

  public void restoreUserCheckMessage() {

    userCheckMsg = "Dokument je izradio drugi korisnik. Mogu\u0107 je samo pregled !";
    userChangePossible = true;

  }

  /**

   * Da li je aktivirana kontrola usera

   * @return true ako setirani masterset ima kolonu cuser i ako je receno setUserCheck(true)

   */

  public boolean isUserCheck() {

    if (!userCheck) return false;

    if (getMasterSet()==null) return false;

    if (getMasterSet().hasColumn("CUSER") == null) return false;

    return true;

  }

  /**

   * Ukljucuje kontrolu usera koja se manifestira tako da ako user nije napravio taj dokument (getMasterSet().getString("CUSER") != current user)

   * moze ga samo gledati ali ne i mijenjati niti dodavati stavke

   * @param chk true - user se kontrolira, false - ne kontrolira se

   */

  public void setUserCheck(boolean chk) {

    userCheck = chk;

  }



  public boolean checkAccess() {

    return true;

  }



  private boolean checkUser() {

    try {

      if (!checkAccess()) return false;

      if (!isUserCheck()) return true;

      if (ralock.isSuper()) return true; // superuser mo?e ?to ho\u0107e

      if (ralock.getUser().equals(getMasterSet().getString("CUSER"))) return true;

      return false;

    }

    catch (Exception ex) {

      return false;

    }

  }

  public boolean showUserCheckMsg() {

    String uCMess = getUserCheckMsg();

    if (uCMess == null) return false;

    if (userChangePossible) {

    String[] ops = {"OK","Promjena korisnika"};

    return JOptionPane.showOptionDialog(

      raMaster.getWindow(),

      uCMess,

      "Sistem",

      JOptionPane.YES_NO_OPTION,

      JOptionPane.WARNING_MESSAGE,

      null,

      ops,

      ops[0]

    ) == 1;//vraca true ako je odabrao promjenu usera
    } else {
      JOptionPane.showMessageDialog(raMaster.getWindow(), uCMess,
        "Sistem", JOptionPane.WARNING_MESSAGE);
      return false;
    }

  }

  public void userEditEnable(boolean en) {

    raDetail.setEditEnabled(en);

/*

    raMaster.setEditEnabled(en);

    System.out.println("userEditEnable("+en+").raDetail.isShowing() = "+raDetail.isShowing());

    if (raDetail.isShowing()) {

    } else {

      raMaster.setEnabledNavAction(raMaster.getNavBar().rnvAdd,true);

      raMaster.setEnabledNavAction(raMaster.getNavBar().rnvUpdate,true);

      raMaster.setEnabledNavAction(raMaster.getNavBar().rnvPrint,true);

      raMaster.setEnabledNavAction(rnvStavke,true);

    }

*/

  }

  public boolean checkAddEnabled() {
    return true;
  }

  private boolean checkUserEdit() {

    if (checkUser()) {

//      userEditEnable(true);

      return true;

    } else {

      if (showUserCheckMsg()) {

        startFrame.changeUser();

        return checkUserEdit();

      } else {

//        userEditEnable(false);

        return false;

      }

    }

  }

//  private void checkUserNav() {

//    if (raDetail.isShowing()) {

//      boolean chusr = checkUser();

//      userEditEnable(chusr);

//    }

//  }

 /**

   * Seteri i geteri na Master i Detail Set

   */

  public void setJPanelMaster(javax.swing.JPanel newJPanelMaster) {

    JPanelMaster = newJPanelMaster;

    raMaster.setRaDetailPanel(JPanelMaster);

    raMaster.pack();

  }



  public javax.swing.JPanel getJPanelMaster() {

    return JPanelMaster;

  }



  public void setJPanelDetail(javax.swing.JPanel newJPanelDetail) {

    JPanelDetail = newJPanelDetail;

    raDetail.setRaDetailPanel(JPanelDetail);

  }

  public javax.swing.JPanel getJPanelDetail() {

    return JPanelDetail;

  }

  public void setMasterSet(com.borland.dx.sql.dataset.QueryDataSet newMasterSet) {

    masterSet = newMasterSet;

    raMaster.setRaQueryDataSet(masterSet);

  }

  public com.borland.dx.sql.dataset.QueryDataSet getMasterSet() {

    return masterSet;

  }

  public void setMasterKey(String[] newMasterKey){

    MasterKey = newMasterKey;

  }

  public void setDetailKey(String[] newDetailKey){

    DetailKey = newDetailKey;

  }

  public void setDetailSet(com.borland.dx.sql.dataset.QueryDataSet newDetailSet) {



    detailSet = newDetailSet;

    raDetail.setRaQueryDataSet(detailSet);

    if (!SQLFilter) addMemFilter();
    //po difoltu za detail lockiranje je iskljuceno, a lokiranje mastera
    //kontrolira i se i na stavkama (u raMatPodaci ako je raMatPodaci.getRaMasterDetail()!=null)
    raDetail.setSoftDataLockEnabled(false);

  }

  private void addMemFilter() {

    try{

      detailSet.removeRowFilterListener(detailSet.getRowFilterListener());

      detailSet.addRowFilterListener(new stavkeFilterListener());

    } catch (Exception e) {

      e.printStackTrace();

    }

  }

  public com.borland.dx.sql.dataset.QueryDataSet getDetailSet() {

    return detailSet;

  }

/**

 * Postavlja pravila pona?anja kada korisnik poku?ava obrisati slog u mastersetu

 * @param masterDelMode moze biti

 * DELETE - Brise master bez pitanja i makinacija kao i do sada

 * DELDETAIL - Brise i pripadajuci mu detail koristeci sve funkcije (kao da si usao u svaku stavku i stisnuo delete)

 * NODEL - Ne dozvoljava obrisati nikada
 * EMPTYDEL - Brise samo ako nema stavki

 */

  public void setMasterDeleteMode(int masterDelMode) {

    masterDeleteMode = masterDelMode;

  }



  public int getMasterDeleteMode() {

    return masterDeleteMode;

  }



  /**

   * Ako je kako true vidi se kumulativ panel

   */

  public void set_kum_detail(boolean kako){

    kumdane=kako;

  }



  /**

   * Funkcija kokjoj se daje arraj stinga za zbrojiti

   */

  public void stozbrojiti_detail(String[] sto){

    var4sum=sto;

  }



  public void setnaslovi_detail(String[] sto){

    nas4sum=sto;

  }



  public void setversionmaster(int version) {

    this.versionmaster=version;

  }



  public int getversionmaster() {

    return this.versionmaster;

  }



  public void setversiondetail(int version) {

    this.versiondetail=version;

  }



  public int getversiondetail() {

    return this.versiondetail;

  }



  private void navigateMasterDetail(boolean navigate) {

    if (!navigate) return;

    refilterDetailSet();

    if (raDetail.getMode() == 'N') return;

    raDetail.jpTableView.Zbrajalo();

    raDetail.fireTableDataChanged();

    raDetail.jeprazno();

  }



  public void refilterDetailSet() {

    boolean filterNeeded;

    if (SQLFilter) {

      filterNeeded = applySQLFilter();

    } else {

      raDetail.getRaQueryDataSet().refilter();

      filterNeeded = true;

    }

    if (filterNeeded) {

      String[] srtCols = raDetail.getSort();

      if (srtCols == null || srtCols.length == 0)
        if (DetailKey != null) srtCols = DetailKey;
      
      if (srtCols == null || srtCols.length == 0)
        srtCols = raDetail.getJpTableView().getKeyColumns();
      
      if (srtCols == null || srtCols.length == 0)
        if (MasterKey != null) srtCols = MasterKey;
      
      if (srtCols!=null && srtCols.length > 0) {
        //System.out.println(VarStr.join(srtCols, ','));
  	    getDetailSet().setSort(new com.borland.dx.dataset.SortDescriptor(srtCols));
    	if (raDetail.getJpTableView().getMpTable() instanceof raExtendedTable) {
    	  ((raExtendedTable) raDetail.getJpTableView().getMpTable()).resetSortColumns();
    	}
      }
      getDetailSet().open();
      getDetailSet().last();
    }
  }



  public String getSQLFilterQuery() {

    Valid vl = Valid.getValid();

    String orgQuery = vl.getNoWhereQuery(detailSet);

    String addQuery = " WHERE ";

//    if (raMaster.getMode() == 'N') {

    if (masterSet.getRowCount() == 0) {

      addQuery = addQuery.concat("'A'='B'");

    } else {

      for (int i=0;i<MasterKey.length;i++) {

        com.borland.dx.dataset.Column col = detailSet.getColumn(DetailKey[i]);

        com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();

        masterSet.getVariant(MasterKey[i],v);

        String val = v.toString();
        if (vl.isNumeric(col) && val.equals("")) val = "0";
        String qsx = vl.getQuerySintax(col,val,i==MasterKey.length-1);

        addQuery = addQuery.concat(qsx);

      }

    }

    String newQuery = orgQuery.concat(addQuery);

    return newQuery;

  }

  private boolean applySQLFilter() {

    String newQuery = getSQLFilterQuery();

    if (detailSet.getQuery().getQueryString().equals(newQuery)) {

      if (!detailSet.isOpen()) detailSet.open();

      return false; //nije morao querat bazu

    } else {

      Aus.refilter(detailSet, newQuery);
      
      try {
        raDetail.getColumnsBean().checkFilter();
      } catch (RuntimeException e) {
        e.printStackTrace();
      }

      return true;

//      detailSet.executeQuery();

    }

  }

/**

 * ako je setSQLFilter(false) stavke se filtriraju dodavanjem rowFilterListenera, a

 * ako je setSQLFilter(true) modificira se query stavaka i tako se filtriraju.

 * Default je true.

 */

  public void setSQLFilter(boolean sqlf) {

    SQLFilter = sqlf;

  }



  public boolean getSQLFilter() {

    return SQLFilter;

  }



  class stavkeFilterListener implements com.borland.dx.dataset.RowFilterListener {

    private boolean FilterSeter = false;



    public stavkeFilterListener() {

    }



    void Profilter(boolean opcija,com.borland.dx.dataset.RowFilterResponse response){

      if (opcija) {

        response.add();

      } else {

        response.ignore();

      }

    }

    private void setFilter(boolean option){

      FilterSeter=option;

    }

    private boolean getFilter(){

      return FilterSeter;

    }

    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse response) {

      if (MasterKey==null) return;

      setFilter(true);

      for (int i=0;i<MasterKey.length;i++) {

        try{

          if (masterSet.getColumn(MasterKey[i]).getSqlType()==java.sql.Types.CHAR  &&

                  row.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.CHAR) {

            if (getFilter()) {

              setFilter(masterSet.getString(MasterKey[i]).equals(row.getString(DetailKey[i])));

            }

          } else if (masterSet.getColumn(MasterKey[i]).getSqlType()==java.sql.Types.INTEGER &&

                      row.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.INTEGER) {

            if (getFilter()) {

              setFilter(masterSet.getInt(MasterKey[i])== row.getInt(DetailKey[i]));

            }

          } else if (masterSet.getColumn(MasterKey[i]).getSqlType()==java.sql.Types.FLOAT &&

                      row.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.FLOAT){

            if (getFilter()) {

              setFilter((masterSet.getFloat(MasterKey[i])== row.getFloat(DetailKey[i])));

            }

          } else if (masterSet.getColumn(MasterKey[i]).getSqlType()==java.sql.Types.SMALLINT &&

                      row.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.SMALLINT){

            if (getFilter()) {

              setFilter((masterSet.getShort(MasterKey[i])== row.getShort(DetailKey[i])));

            }

          } else {

            System.out.println("Neispravni tipovi podatka za filtriranje (raMasterDetail)");

          }

          Profilter(getFilter(),response);

        } catch (Exception e) {

          e.printStackTrace();

        }

      }

    } //filterRow

  } //stavkeFilterListener

/*

  private boolean chkDelAllDetail() {

    if (getDetailSet().getRowCount() == 0) return true;

    getDetailSet().first();

    do {

      if (!raDetail.LegalDelete(true)) return false;

    } while (getDetailSet().next());

    return true;

  }



  private boolean delAllDetail() {

    if (getDetailSet().getRowCount() == 0) return true;

    do {

      try {

        getDetailSet().first();

        raDetail.DeleteCheck(); // znamo u ovom trenutku da prolazi, ali da bi inicijalizirao kojekakve varijable

        getDetailSet().deleteRow();

        raDetail.AfterDelete(); // da bi, vjerujem azurirao nekakve kumulative

      }

      catch (Exception ex) {

        return false;

      }

    } while (!getDetailSet().isEmpty());



    try {

      getDetailSet().saveChanges();

      return true;

    }

    catch (Exception ex) {

      return false;

    }

  }

*/

  public boolean delAllDetailSet() {



    boolean delOK = true;

    refilterDetailSet();

//    if (chkDelAllDetail()) {

      delOK = raDetail.deleteAll(false);//delAllDetail();//zamijenio sa raDetail.deleteAll() kad to sinisa istestira :))

//    } else {

//      delOK = false;

//    }

    if (!delOK) JOptionPane.showMessageDialog(raMaster.getContentPane(),"Brisanje stavaka nije uspjelo! Molim obrišite ih jednu po jednu.");

    return delOK;

  }



  private void instanceRaMaster() {

    raMaster = new raMatPodaci(versionmaster) {//this.getJPanelMaster(),this.getMasterSet(),this.getversionmaster())  {

        public String getClassName() {

          return raMasterDetail.this.getClass().getName().concat("$master");

        }

        public boolean DeleteCheck(){

          refilterDetailSet();

          return DeleteCheckMaster();

        }

        public void table2Clicked() {

//          jBStavke_actionPerformed(null);

          masterSet = raMaster.getRaQueryDataSet();

          if (rnvStavke.isActionPerformed(null)) rnvStavke.actionPerformed(null);

          if (raMaster.version == 1) {

            raMaster.jTabPane.setSelectedIndex(1);

          }

        }



        public void beforeToggleTable() {

          beforeToggleTableMaster();

        }



        public void afterToggleTable() {

          afterToggleTableMaster();

        }



        public boolean doBeforeSave(char mode) {

          return doBeforeSaveMaster(mode);

        }



        public boolean doWithSave(char mode) {

          return doWithSaveMaster(mode);

        }

        public void Insertiraj() {

          super.Insertiraj();

          if (isUserCheck()) getMasterSet().setString("CUSER",ralock.getUser());

        }

        public void rnvAdd_action() {
          if (checkAddEnabled()) super.rnvAdd_action();
        }

        public void rnvUpdate_action() {

          if (checkUserEdit()) super.rnvUpdate_action();

        }

        public void rnvDelete_action() {

          if (checkUserEdit()) super.rnvDelete_action();

        }

      // gleda jel treba i stavke

        public boolean BeforeDelete() {

          if (masterDeleteMode == DELDETAIL) return delAllDetailSet();

          return true;

        }

        public boolean PorukaDelete() {

          return PorukaDeleteMaster();

        }

      //

        public void offon(boolean kako){

          super.offon(kako);

          rnvStavke.setEnabled(kako);

        }



        public void detailViewShown(char mod) {

          detailViewShownMaster(mod);

        }



        public void tableViewShown(char mod) {

          tableViewShownMaster(mod);

        }



        public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {

          if (raDetail.isShowing()) {

            if (getMasterSet().getStatus() == com.borland.dx.dataset.RowStatus.INSERTED) return;

            try {

              navigateMasterDetail(((JraTable2)raDetail.getJpTableView().getMpTable()).isEnabled());

            }

            catch (Exception ex) {

              System.out.println("navigateMasterDetail ex");

              navigateMasterDetail(true);

            }

          }

          masterSet_navigated(e);

//          checkUserNav();

        }



        public boolean Validacija(char mode) {

          if (!ValidacijaMaster(mode)) return false;

          return interValid(mode);

        }

        public void EntryPoint(char mode) {

          EntryPointMaster(mode);

        }

        public void beforeShow() {

          setMasterSetsort();
          if (raMaster.getJpTableView().getMpTable() instanceof raExtendedTable) {
            ((raExtendedTable) raMaster.getJpTableView().getMpTable()).resetSortColumns();
          }

          initPreSelect();

          removeSelection();

          beforeShowMaster();

        }

        public void ExitPoint(char mode) {

          ExitPointMaster(mode);

        }

        public boolean ValDPEscape(char mode) {

          return ValDPEscapeMaster(mode);

        }

        public void SetFokus (char mode) {

          SetFokusMaster(mode);

        }

        public void AfterAfterSave (char mode) {

          if (mode=='I') super.AfterAfterSave(mode);

          AfterAfterSaveMaster(mode);

        }

        public boolean ValidacijaPrijeIzlaza(){

          return ValidacijaPrijeIzlazaMaster();

        }
        
        public boolean allowRowChange(int oldrow, int newrow) {
          return allowRowChangeMaster(oldrow, newrow);
        }

        public void Minimiziraj(){

//          if (raDetail.isShowing())

//            raDetail.setState(java.awt.Frame.ICONIFIED);

        }

        public void ZatvoriOstalo(){

          if (raDetail.isShowing())

            raDetail.setVisible(false);

          ZatvoriOstaloMaster();

        }

        public void Restauriraj(){

//          if (raDetail.isShowing())

//            raDetail.setState(java.awt.Frame.NORMAL);

        }

        public void rnvPrint_action() {

          Funkcija_ispisa_master();

        }

        public void AfterDelete(){

          AfterDeleteMaster();

        }

        public void AfterSaveFailed(char mode) {
          AfterSaveFailedMaster(mode);
        }

        public void AfterCancel() {

          AfterCancelMaster();

        }

        public void tabStateChanged(int idx) {

          tabStateChangedMaster(idx);

        }



        public void show() {

          if (versionall == 2) raDetail.initBefShow();

          super.show();

          if (versionall == 2) raDetail.initShow();

        }



        public void afterSetMode(char oldMod,char newMod) {

          afterSetModeMaster(oldMod, newMod);

        }

        public boolean validateSelection(ReadRow rowToSelect) {
          return validateSelectionMaster(rowToSelect);
        }

        public void hide() {

          super.hide();

          prnID();

        }

    };

  }

  public void tabStateChangedMaster(int idx) {

  }

  public void tabStateChangedDetail(int idx) {

  }

  public void AfterCancelMaster() {

  }

  public void AfterCancelDetail() {

  }

  public void detailViewShownDetail(char mod) {

  }

  public void tableViewShownDetail(char mod) {

  }

  public void detailViewShownMaster(char mod) {

  }

  public void tableViewShownMaster(char mod) {

  }

  /**

   * Overridana raMatPodaci.afterSetMode(char,char)

   * @param oldMod

   * @param newMod

   */

  public void afterSetModeDetail(char oldMod,char newMod) {

  }

  /**

   * Overridana raMatPodaci.afterSetMode(char,char)

   * @param oldMod

   * @param newMod

   */

  public void afterSetModeMaster(char oldMod,char newMod) {

  }



  /**

   * Zove se kad se promijeni current slog u detailSetu i to samo ako je detail prikazan

   * i nije upravo unos novoga

   * @param e navigationevent

   */

  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e) {

  }

  /**

   * Zove se kad se promijeni current slog u masterSetu i to samo ako je master prikazan

   * i nije upravo unos novoga

   * @param e navigationevent

   */

  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e) {

  }



  public boolean isNewDetailNeeded() {

     return true;

  }



  public boolean doWithSaveMaster(char mode) {

    return true;

  }



  public boolean doWithSaveDetail(char mode) {

    return true;

  }



  public boolean doBeforeSaveMaster(char mode) {

    return true;

  }



  public boolean doBeforeSaveDetail(char mode) {

    return true;

  }



  public void afterToggleTableMaster() {

    masterSet = raMaster.getRaQueryDataSet();

  }



  public void beforeToggleTableMaster() {

    if (MasterKey!=null) raDetail.getJpTableView().setKeyColumns(MasterKey);

    raMaster.getJpTableView().findNoTablePanelValues(getPreSelect());

  }



  public void beforeToggleTableDetail() {

  }



  public void afterToggleTableDetail() {

  }



  public boolean PorukaDeleteMaster() {

    if (masterDeleteMode == NODEL) {

      JOptionPane.showMessageDialog(raMaster.getContentPane(),"Zaglavlje nije mogu\u0107e obrisati!");

      return false;

    }

    if (masterDeleteMode == DELDETAIL) {

      return raMaster.PorukaDelete("Brisanjem zaglavlja obrisat \u0107e se i sve pripadaju\u0107e stavke. Nastaviti?");

    }

    if (masterDeleteMode == EMPTYDEL) {
      refilterDetailSet();
      if (getDetailSet().rowCount() > 0) {
        JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Zaglavlje nije mogu\u0107e obrisati dok stavke nisu obrisane!", "Poruka",
          JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }

    return raMaster.PorukaDelete("Stvarno želite obrisati ?");

  }

  public boolean PorukaDeleteDetail() {

    return raDetail.PorukaDelete("Stvarno želite obrisati ?");

  }

  private void instanceRaDetail() {

    hr.restart.start.setFRAME_MODE();

    int frMod = hr.restart.start.FRAME_MODE;

    Window fr = hr.restart.mainFrame.getMainFrame();

    if (frMod == raFrame.FRAME) {

      fr = raMaster.getWindow();

      frMod = raFrame.DIALOG;

    }

//System.out.println("raMatPodaci("+versiondetail+","+frMod+","+fr+")");
    raDetail = new raMatPodaci(versiondetail,frMod,fr) {//this.getJPanelDetail(),this.getDetailSet(),this.getversiondetail()) {

      public String getClassName() {

        return raMasterDetail.this.getClass().getName().concat("$detail");

      }

      public boolean DeleteCheck(){

          return DeleteCheckDetail();

      }



      public void beforeToggleTable() {

        beforeToggleTableDetail();

      }



      public void afterToggleTable() {

        afterToggleTableDetail();

      }



      public boolean doBeforeSave(char mode) {

        return doBeforeSaveDetail(mode);

      }



      public boolean doWithSave(char mode) {

        return doWithSaveDetail(mode);

      }

/*

      public void jeprazno() {

//        if (getMode() == 'B') {

          super.jeprazno();

          System.out.println("raDetail.jeprazno");

//        }

      }

*/

      public void jeprazno() {

        if (userCheck) {

          if (checkUser()) {

            userEditEnable(true);

            super.jeprazno();

          } else {

            userEditEnable(false);

          }

        } else super.jeprazno();

      }

      public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {

        if (!raDetail.isShowing()) return;

        if (getDetailSet().getStatus() == com.borland.dx.dataset.RowStatus.INSERTED) return;

        detailSet_navigated(e);

      }

      public boolean ValidacijaPrijeIzlaza(){

          boolean valDetail;

          if (valDetail=ValidacijaPrijeIzlazaDetail()){

/*          raMaster.setEnabled(true);

          raMaster.requestFocus();

          raMaster.jPrekid_action(); */

          }

          return valDetail;

      }

      public void detailViewShown(char mod) {

        detailViewShownDetail(mod);

      }



      public void tableViewShown(char mod) {

        tableViewShownDetail(mod);

      }



      public boolean Validacija(char mode) {

         return ValidacijaDetail(mode);

      }

      public void EntryPoint(char mode) {

        EntryPointDetail(mode);

      }



      public void beforeShow() {

        beforeShowDetail();

      }



      public void ExitPoint(char mode) {

        ExitPointDetail(mode);

      }

      public boolean ValDPEscape(char mode) {

        return ValDPEscapeDetail(mode);

      }

      public void SetFokus (char mode) {

        setMasterDetailMode(3);

        SetFokusDetail(mode);

      }



      public void Insertiraj() {

        super.Insertiraj();

        for (int i=0; i<MasterKey.length; i++){

          if (detailSet.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.CHAR){

            getRaQueryDataSet().setString(DetailKey[i],masterSet.getString(MasterKey[i]));

          }

          else if (detailSet.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.INTEGER){

            getRaQueryDataSet().setInt(DetailKey[i],masterSet.getInt(MasterKey[i]));

          }

          else if (detailSet.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.FLOAT){

            getRaQueryDataSet().setFloat(DetailKey[i],masterSet.getFloat(MasterKey[i]));

          }

          else if (detailSet.getColumn(DetailKey[i]).getSqlType()==java.sql.Types.SMALLINT){

            getRaQueryDataSet().setShort(DetailKey[i],masterSet.getShort(MasterKey[i]));

          }

          else

            System.out.println("Nepostoje\u0107i tip podataka za insertiranje novog");

       }
     }


     public void AfterSave(char mode){

        AfterSaveDetail(mode);

     }

     public void AfterAfterSave(char mode) {

        if (AfterAfterSaveDetail(mode)) super.AfterAfterSave(mode);

     }

     public void AfterSaveFailed(char mode) {
       AfterSaveFailedDetail(mode);
     }

     public void rnvPrint_action() {

        Funkcija_ispisa_detail();

     }

     public void AfterDelete(){

        AfterDeleteDetail();

     }

     public void ZatvoriOstalo() {

        ZatvoriOstaloDetail();

     }



     public void AfterCancel() {

        AfterCancelDetail();

     }

     public void tabStateChanged(int idx) {

        tabStateChangedDetail(idx);

     }

     void tabStateChangedLocal(int idx) {

//        raMaster_stavVisible(idx+1);

     }

/*

     public void hide() {

System.out.println("Detail.hide()  versionall="+versionall);

       if (versionall==1) super.hide();

       setMasterDetailMode(1);

       prnID();

     }

     */

     // ab.f  hack za izbacivanje treperanja mastera kad se gasi detail.
     // Treperenje nastaje zato sto Swing izvodi repaint mastera prije
     // nego sto AWT pozove PaintEvent.PAINT onog dijela mastera koji je
     // bio pokriven detailom (PaintEvent-i su najnizeg prioriteta i vrlo
     // spori), a AWT RepainArea koji to radi, nema double buffering nego
     // ide po principu "pobrisi & nacrtaj". Stoga se dogadja da izmedju
     // dva AWT dogadjaja - gasenje peer-a detaila i repainta prekrivenog
     // dijela mastera - uleti Swing i repainta sa svoje strane citavog
     // mastera (misli da je master vidljiv, jer peer detaila je vec otisao),
     // a nakon toga dodje napokon AWT PaintEvent i radi "brisi & crtaj".
     // U nedostatku boljeg rjesenja za sada radim sljedece: prije nego
     // sto uopce pozovem hide na detailu, obnavljam kompletnog mastera
     // (dok je jos pokriven), tako sto metodu hide na detailu stavljam
     // u par invokeLater poziva (ergo na kraj EventQueuea). Mislim da je
     // dva dovoljno, no moguce je da se pojavi i situacija u kojoj bude
     // trebalo vise (to ovisi o tome u koliko koraka se izvodi repaint
     // mastera, naime koliko ima ulancanih poziva tipa componentShown
     // koji se svaki put dodaju na kraj EventQueuea. Nama ovdje treba
     // barem toliko ulancanih invokeLatera koliko ima tih koraka).

     public void this_hide() {
       raMaster.setEnabled(true);
       //raMaster.requestFocus();
       raMaster.jPrekid_action();
       setMasterDetailMode(1);
       SwingUtilities.invokeLater(new Runnable() {
         public void run() {
           SwingUtilities.invokeLater(new Runnable() {
             public void run() {
               SwingUtilities.invokeLater(new Runnable() {
                 public void run() {
                   deferredHide();
                 }
               });
             }
           });
         }
       });
     }

     private void deferredHide() {
       super.this_hide();
     }

     void this_compHidden() {

       super.this_compHidden();

       prnID();

     }

     public void setMode(char mod) {

       if (mod == 'B') setMasterDetailMode(2);

       super.setMode(mod);

     }

/*

     void jPrekid_action() {

       super.jPrekid_action();

       setMasterDetailMode(2);

     }



     void jBOK_action() {

      super.jBOK_action();

      setMasterDetailMode(2);

     }

*/

     public void show() {

       if (versionall == 2) this.initBefShow();

       setMasterDetailMode(2);

       if (versionall == 1) super.show();

       if (versionall == 2) this.initShow();

     }



     void this_compShown() {

      super.this_compShown();

      if (showNew&&isNewDetailNeeded()) {

        SwingUtilities.invokeLater(new Runnable() {
        
          public void run() {
            rnvAdd_action();

            showNew = false;        
          }
        });
      }

     }

     public boolean isShowing() {

       if (versionall == 2) {

         return getContentPane().isShowing();

       } else {

         return super.isShowing();

       }

     }

     public void afterSetMode(char oldMod,char newMod) {

        afterSetModeDetail(oldMod,newMod);

     }



     public boolean PorukaDelete() {

       return PorukaDeleteDetail();

     }
     
     public boolean validateSelection(ReadRow rowToSelect) {
       return validateSelectionDetail(rowToSelect);
     }

    };

  }

  public boolean validateSelectionMaster(ReadRow rowToSelect) {
    return true;
  }
  public boolean validateSelectionDetail(ReadRow rowToSelect) {
    return true;
  }


  /**

   * Ako trebas neki drugi sort na masteru, inicijalno je MasterKey

   * odn. getMasterSet().setSort(new com.borland.dx.dataset.SortDescriptor(MasterKey));

   */

  public void setMasterSetsort() {
	String[] sort = raMaster.getSort();
	if (sort == null || sort.length == 0) sort = MasterKey;
    getMasterSet().setSort(new SortDescriptor(sort));
    if (raMaster.getJpTableView().getMpTable() instanceof raExtendedTable) {
      ((raExtendedTable) raMaster.getJpTableView().getMpTable()).resetSortColumns();
    }
  }

  //staticke metode za dohvat jednog recorda mastera

  /**

   * Prikazuje samo odreðeni dokument u masteru i pripadajuce mu stavke ako je potrebno

   * @param className ime klase masterdetaila

   * @param keyNames imena kolona za filter

   * @param keyValues vrijednosti kolona za filter

   * @param showDetail da li prikazati i detail

   */

  public static raMasterDetail showRecord(String className, String[] keyNames, String[] keyValues, boolean showDetail) {

    try {

      final raMasterDetail rmd = (raMasterDetail)raLoader.load(className);

      if (keyNames == null) keyNames = rmd.MasterKey;

      rmd.showRecord(keyNames,keyValues,showDetail);

      return rmd;

    }

    catch (Exception ex) {

      ex.printStackTrace();

      return null;

    }



  }



  /**

   * return showRecord(className,keyNames,keyValues,true);

   */

  public static raMasterDetail showRecord(String className, String[] keyNames, String[] keyValues) {

    return showRecord(className,keyNames,keyValues,true);

  }



  /**

   * zove showRecord(String className, String[] keyNames, String[] keyValues, boolean showDetail)

   * gdje su keyNames = masterKey

   */

  public static raMasterDetail showRecord(String className, String[] keyValues) {

    return showRecord(className,null,keyValues,true);

  }



  /**

   * Prikazuje samo odreðeni dokument u masteru i pripadajuce mu stavke ako je potrebno

   * @param keyNames imena kolona za filter

   * @param keyValues vrijednosti kolona za filter

   * @param showDetail da li prikazati i detail

   */
  
  public void showRecord(String[] keyNames, String[] keyValues, boolean showDetail) throws Exception {
    showRecord(keyNames, keyValues, showDetail, null);
  }

  public void showRecord(String[] keyNames, String[] keyValues, 
      final boolean showDetail, final Runnable afterShow) throws Exception {

    final String qry = getFilteredQuery(getMasterSet(),keyNames,keyValues);
    
    if (raDetail.isShowing()) {
      char dm = raDetail.getMode();
      if (dm != 'B') {
        int response = JOptionPane.showConfirmDialog(raDetail.getWindow(),
            "Prekinuti " + (dm == 'N' ? "unos" : "izmjenu") +" stavke?", 
            "Ekran zauzet", JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        if (response != JOptionPane.OK_OPTION) return;
        raDetail.getOKpanel().jPrekid_actionPerformed();
      }
      if (!ValidacijaPrijeIzlazaDetail()) return;
      ZatvoriOstaloDetail();
    }

    if (raMaster.isShowing()) {
      if (raMaster.getMode() != 'B')
        raMaster.getOKpanel().jPrekid_actionPerformed();
      raMaster.rnvExit_action();
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
      	Aus.refilter(getMasterSet(), qry);        
        show();
        if (showDetail) //raMaster.table2Clicked(); TV. update
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              jBStavke_actionPerformed(null);
              if (afterShow != null)
                afterShow.run();
            }
          });
      }
    });
  }

  /**

   * showRecord(MasterKey,keyValues,true);

   */
  
  public void showRecord(String[] keyValues) {
    showRecord(keyValues, null);
  }

  public void showRecord(String[] keyValues, Runnable afterShow) {

    try {

      showRecord(MasterKey, keyValues, true, afterShow);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  public static String getFilteredQuery(com.borland.dx.sql.dataset.QueryDataSet qds,String[] keyNames,String[] keyValues) throws Exception {

    Valid vl = Valid.getValid();

    String qry = vl.getNoWhereQuery(qds);

    qry = qry.concat(" WHERE ");

    for (int i = 0; i < keyNames.length; i++) {

      qry = qry.concat(

        vl.getQuerySintax(qds.hasColumn(keyNames[i]),keyValues[i],(i==(keyNames.length-1)))

      );

    }

    return qry;

  }

//TEST

  private void prnID() {

    raMatPodaci.printFrameID(this);

  }

  public raNavAction getRnvStavke() {
    return rnvStavke;
  }
  
  public void installDefaultMasterSelectionTracker() {
    raMaster.getJpTableView().installSelectionTracker(MasterKey);
  }
  
  public raSelectTableModifier getTracker() {
    return raMaster.getSelectionTracker(); 
  }

}