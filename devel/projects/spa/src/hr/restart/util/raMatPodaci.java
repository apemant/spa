/****license*****************************************************************
**   file: raMatPodaci.java
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



import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.raDataSet;
import hr.restart.help.raHelpAware;
import hr.restart.help.raHelpContainer;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.sisfun.raUser;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraPanel;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraSplitPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.SharedFlag;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raSelectTableModifier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.StorageDataSet;




/**

 * Title:        Utility za java aplikacije

 * Description:

 * Copyright:    Copyright (c) 2000

 * Company:      REST-ART

 * @author REST-ART development team

 * @version 1.0

 */



public abstract class raMatPodaci extends raFrame implements raHelpAware {

sysoutTEST ST = new sysoutTEST(false);
  /**
   * Da li da gleda na polje LOKK u tablici i da li da ga puni sa D
   **/
  private boolean softDataLock;
  private boolean smartResize = new Boolean(
        hr.restart.sisfun.frmParam.getParam("sisfun", "smartResize", "false", "Resizanje ekrana ovisno odabranom tablicnom ili detaljnom prikazu", true)
      ).booleanValue();
//  int sti=0; //sto li je autor ovime htio?

//  Dimension dim; //sto li je autor ovime htio?

  ResourceBundle res = ResourceBundle.getBundle("hr.restart.util.Res_");

  public hr.restart.util.raCommonClass myCC ;

  int version=getVerParam("ramatversion");

  private String frameTitle;

  private JComponent compToKillFocus = null;

  private raNavAction rnvCopyCurr = new raNavAction("Kopiraj tekuæi zapis",raImages.IMGCOPYCURR,KeyEvent.VK_F2,KeyEvent.SHIFT_MASK) {

      public void actionPerformed(ActionEvent e) {

        rnvCopyCurr_action();

      }

  };

  hr.restart.util.reports.JTablePrintRun TPRun = new hr.restart.util.reports.JTablePrintRun();

//  java.awt.Image backgroundImage = raImages.getImageIcon(raImages.IMGSPLASH).getImage();

  JTabbedPane jTabPane = new JTabbedPane() {

    public void requestFocus() {
      
      if (compToKillFocus!=null) if (compToKillFocus.isShowing()) {

        compToKillFocus.requestFocus();

        return;

      }

      super.requestFocus();

    }

    public boolean isFocusTraversable() {

      if (compToKillFocus!=null) if (compToKillFocus.isShowing()) {

        return false;

      }

      return super.isFocusTraversable();

    }

  };

  raJPTableView jpTableView = new raJPTableView(false) {

    public void mpTable_killFocus(java.util.EventObject e) {

      //jTabPane.requestFocus();

    }

    public void mpTable_doubleClicked() {

      table2Clicked();

    }
    
    public boolean mpTable_allowRowChange(int oldrow, int newrow) {
      return allowRowChange(oldrow, newrow);
    }

    public boolean validateSelection(ReadRow r) {
      return raMatPodaci.this.validateSelection(r);
    }
  };

  public JraPanel jpDetailView = new JraPanel();

  JraSplitPane jSplitPaneMP = new JraSplitPane(JSplitPane.VERTICAL_SPLIT);

  java.awt.event.WindowAdapter raMatPod_WindowAdapter = new java.awt.event.WindowAdapter() {

      public void windowActivated(WindowEvent e) {

//        initHP();

      }

      public void windowClosing(WindowEvent e) {

        this_windowClosing();

      }

      public void windowIconified(WindowEvent e) {

        this_windowIconified(e);

      }

      public void windowDeiconified(WindowEvent e) {

        this_windowDeiconified(e);

      }

   };

  JraScrollPane jScrollPaneDetail = new JraScrollPane();

//  hr.restart.swing.JraTable mpTable; //Privremeno

//  JTable kumTableRow = new JTable();

  private javax.swing.JPanel raDetailPanel;

  private com.borland.dx.sql.dataset.QueryDataSet raQueryDataSet;

  private char mode='B';

  private char lockedMode = '0';

  private boolean saveChanges = true;

  private raMasterDetail raMD = null;

  JPanel jPnibii_cont = new JPanel();

  private raNavAction[] editNavActions;

  KeyListener MojKeyListener= new KeyListener(){

      public void keyPressed( KeyEvent e){

        if (!isKeyDisabled(e.getKeyCode())) tipkaPritisnuta (e);

      }

      public void keyReleased(java.awt.event.KeyEvent e){

      }

      public void keyTyped(java.awt.event.KeyEvent e){

      }

    };

  java.awt.event.ComponentAdapter panelShowAdapter = new java.awt.event.ComponentAdapter() {

    public void componentShown(ComponentEvent e) {

      showDetailsInHelp();

      if (hr.restart.start.FRAME_MODE == raFrame.PANEL) {

        addKeyListeners(hr.restart.mainFrame.getMainFrame().mainTabs);

//        initHP();

      }

      addKeyListeners(hr.restart.mainFrame.getMainFrame());

      this_compShown();

    }

    public void componentHidden(ComponentEvent e) {

      if (hr.restart.start.FRAME_MODE == raFrame.PANEL) {

        removeKeyListeners(hr.restart.mainFrame.getMainFrame().mainTabs);

      }

      removeKeyListeners(hr.restart.mainFrame.getMainFrame());

      this_compHidden();

    }

  };

///////// DEFINICIJE OD raMatPodaci2

 hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){



    public void jBOK_actionPerformed(){

       jBOK_action();

    }



    public void jPrekid_actionPerformed(){

      jPrekid_action();

    }



  };

  SharedFlag ticket = okp.getTicket();
  
  raNavBar ranibii = new raNavBar() {

    public void add_action() {

      rnvAdd_action();

    }

    public void update_action() {

      rnvUpdate_action();

    }

    public void delete_action() {

      rnvDelete_action();

    }

    public void print_action() {

      rnvPrint_action();

    }

    public void exit_action() {

      rnvExit_action();

    }

    public void table_action() {

      rnvTable_action();

    }

    public void afterRefresh() {
      jpTableView.removeSelection();
      navbar_afterRefresh();
    }

  };

///////// KRAJ



  public raMatPodaci() {

    this(raMatPodaci.getVerParam("ramatversion"));

  }

  public raMatPodaci(int versionC){

    initRaMat(versionC);

  }

  public raMatPodaci(int versionC, int frameModeC, Container owner) {

    super(frameModeC,owner);

    initRaMat(versionC);

  }



  private void initRaMat(int versionC) {

    this.version=versionC;

    if (version>1) {

      try {

        jbInit2();

        return;

      }

      catch(Exception ex) {

        ex.printStackTrace();

      }

    }



    else if (version==1) {

      try {

        jbInit();

      }

      catch(Exception ex) {

        ex.printStackTrace();

      }

    }

    else

        System.out.println("Neispravna vrijednost parametara");

  }

  private void jbInit() throws Exception {

//  private void jbInit_() throws Exception {

    jbInitCommon();

    createListeners();

    createVer1();

  }



//  private void jbInit() throws Exception {

  private void jbInit2() throws Exception {

    jbInitCommon();

    createVer2();

  }

  void createVer1() {

    jTabPane.add(jpTableView,res.getString("Tabli_u010Dni_prikaz"),0);

    jTabPane.add(jpDetailView,res.getString("Detaljni_prikaz"),1);
  
  }
  
  public JraPanel getDetailView() {
    return jpDetailView;
  }

  void createVer2() {

    jSplitPaneMP.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

    if (version == 2) {

      jSplitPaneMP.setTopComponent(jpTableView);

      jSplitPaneMP.setBottomComponent(jpDetailView);

      jSplitPaneMP.setResizeWeight(1.00);

    } else if (version == 3) {

      jSplitPaneMP.setTopComponent(jpDetailView);

      jSplitPaneMP.setBottomComponent(jpTableView);

      jSplitPaneMP.setResizeWeight(0.00);

    }

    jTabPane.add(jSplitPaneMP,"",0);

  }

  void createTableView() {//iz jbinit ver1 za jbinit ver2

//    mpTable = jpTableView.getMpTable();

    jPnibii_cont.setLayout(new BorderLayout());

    jpTableView.setNavBar(getNavBar()); //AI 07082002

    getNavBar().setJpTabView(jpTableView);

    if (version == 3)

      getContentPane().add(getNavBar(),BorderLayout.NORTH);

//      okp

    else

      jpTableView.add(getNavBar(),BorderLayout.NORTH);

  }



  void createDetailView() {

    jpDetailView.setLayout(new BorderLayout());
    jpDetailView.setFocusCycleRoot(true);
    jpDetailView.setOwner(this);

    jScrollPaneDetail.setBorder(BorderFactory.createEmptyBorder());

    jScrollPaneDetail.setViewportBorder(null);

    jpDetailView.add(jScrollPaneDetail, BorderLayout.CENTER);

  }



  void createListeners() {
    jTabPane.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (jTabPane.getTabCount()>1) {
          tabStateChanged(jTabPane.getSelectedIndex());
          tabStateChangedLocal(jTabPane.getSelectedIndex());
        }
        if (jTabPane.getSelectedIndex()==1 && getMode() == 'B') {
          applyNavFields();
          ChangeEnabDisab(false);
        }
      }
    });

    jpDetailView.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        detailViewShown(mode);
        addKeyListeners(jpDetailView);
//        if (mode!='B')
          SetFokus(mode);
      }
      public void componentHidden(ComponentEvent e) {
        removeKeyListeners(jpDetailView);
      }
    });

    jpTableView.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        if (getRaMasterDetail() == null) {
          tableViewShown(mode);
          setMode('B');
        }
        //jTabPane.requestFocus();
      }
    });

    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        if (!smartResize) return;
        if (version != 1) return;
        if (jTabPane.getSelectedIndex()!=0) return;
        if (getWindow().getSize() == null) return;
        tableSize = getWindow().getSize();
      }
    });
  }

  /**

   * Zove se uvijek kad se promijeni tab Tablicni - Detaljni prikaz

   * @param idx index taba koji je trenutno u selektiran 0 - tablicni 1 - detaljni

   */

  public void tabStateChanged(int idx) {

  }

  void tabStateChangedLocal(int idx) {
    if (version == 1) {
      if (idx == 0) {//tablica
        set2TableSize();
      } else if (idx == 1) {//detail
        if (mode == 'B') set2DetailSize(); //za izmjenu i novi handla u handle_jTabbedPane
      }
    }
  }

/**

 * Slijedeca promjena moda bit ce sigurno taj mode

 */

  public void setLockedMode(char newMode) {

    lockedMode = newMode;

  }

/**

 * Poziva se kao prva funkcija pri prikazivanju detaljnog pregleda (samo u super(1))

 * Ne radi ni?ta, treba je overridati

 * @param mod - mod rada

 */

  public void detailViewShown(char mod) {

  }

/**

 * Poziva se kao prva funkcija pri prikazivanju tabli\u010Dnog pregleda (samo u super(1))

 * Ne radi ni?ta, treba je overridati

 * @param mod - mod rada

 */

  public void tableViewShown(char mod) {

  }



  void destroyVer() {

    jSplitPaneMP.removeAll();

    jTabPane.removeAll();

  }

  public void switchVersion() {

    if (version==1)

      setDispVersion(2);

    else if (version==2)

      setDispVersion(1);

  }

  public void setDispVersion(int newVersion) {

    if (version == newVersion) return;

    try {

      hide();

      destroyVer();

      if (newVersion==1) createVer1();

        else createVer2();

      version = newVersion;

      pack();

      show();

    } catch (Exception e) {

      e.printStackTrace();

    }

  }



  private void jbInitCommon() throws Exception {

//    this.setIconImage(raImages.getImageIcon(raImages.IMGSPLASH).getImage()); // extenda raframe pa netreba

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    myCC=hr.restart.util.raCommonClass.getraCommonClass();

    jTabPane.removeAll();

    jTabPane.setTabPlacement(hr.restart.start.getTabDetPlacement());

    jTabPane.setBorder(BorderFactory.createEmptyBorder());

    getContentPane().setLayout(new BorderLayout());

    addWindowListener(raMatPod_WindowAdapter);

    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentShown(java.awt.event.ComponentEvent ce) {

        this_compShown();

      }

      public void componentHidden(java.awt.event.ComponentEvent ce) {

        this_compHidden();

      }

    });

    createTableView();

    createDetailView();

    this.getContentPane().add(jTabPane, BorderLayout.CENTER);

//    jpDetailView.add(jPnibii_cont, BorderLayout.SOUTH);

//    jPnibii_cont.add(okp,BorderLayout.CENTER);

    jPnibii_cont.add(okp,BorderLayout.CENTER);

    if (version == 3) {

      getContentPane().add(jPnibii_cont, BorderLayout.SOUTH);

    } else {

      jpDetailView.add(jPnibii_cont, BorderLayout.SOUTH);

    }

    /*jTabPane.addKeyListener(new hr.restart.swing.JraKeyListener());*/

    int pos = getNavBar().getOptionIndex(getNavBar().rnvPrint);

    addOption(rnvCopyCurr,pos,true);
    
    raHelpContainer.registerHelp(this);

  }

  void this_compShown() {

    /*addKeyListeners(jTabPane);*/

    addKeyListeners(this);

  }

  void this_compHidden() {

//    removeKeyListeners(jTabPane);

    removeKeyListeners(this);

  }

  /**

   * Setiranje panela za detaljni prikaz

   */

  public void go(){

    pack();

    show();

  }

  public void pack(){

    if (hr.restart.start.isRESIZABLELAYOUT())
      hr.restart.swing.layout.raLayUtil.parseXYLayout(raDetailPanel);

    super.pack();

    if (version>1) {

      jTabPane.setTitleAt(0,frameTitle);//calcRaDetailPanelSize();

      //getJpTableView().setFewRowSize();
      /*getJpTableView().setPreferredSize(
          new Dimension(getJpTableView().getWidth(), 
              getColumnsBean().getPreferredTableSize().height));
      super.pack();*/
      
      if (version == 3)
      	jSplitPaneMP.setDividerLocation(jSplitPaneMP.getTopComponent().getPreferredSize().height + 1);
      else jSplitPaneMP.setDividerLocation(jSplitPaneMP.getHeight() -
		      		jSplitPaneMP.getBottomComponent().getPreferredSize().height - 1 - jSplitPaneMP.getDividerSize());
      //super.pack();

    } else {
      fillSizes();
      set2TableSize();
    }
  }
  //mijenjanje sizea na super(1) ovisno o tome je li detail ili master
  private Dimension detailSize = null;
  private Dimension tableSize = null;

  private void set2TableSize() {//pozvati kad se prebacuje na tableView
    if (smartResize && tableSize!=null) getWindow().setSize(tableSize);
  }

  private void set2DetailSize() {////pozvati kad se prebacuje na detailView
    if (smartResize && detailSize!=null) getWindow().setSize(detailSize);
  }

  private Dimension calcTableSize() {//kalkulira velicinu tablicnog prikaza ovisno o kolicini redova u tablici
/*    try {
      int w = getWindow().getSize().width;
      int sh = hr.restart.start.getSCREENSIZE().height-70;
      int rh = (getRaQueryDataSet().getRowCount()*getJpTableView().getMpTable().getRowHeight()+70)
      +raNavAction.ACTSIZE+getJpTableView().getInsets().top+getJpTableView().getInsets().bottom;
      if (getRaQueryDataSet().getRowCount() == 0) rh = getJpTableView().getFewRowSize().height+70;
      int h = rh>sh?sh:rh;
      return new Dimension(w, h);
    } catch (Exception ex) {
      System.out.println("calcTableSize ex = "+ex);
      return getWindow().getSize();
    }*/
    return getWindow().getSize();
  }
  private Dimension calcDetailSize() {
    try {
      int w = jpDetailView.getPreferredSize().width+getWindow().getInsets().left+getWindow().getInsets().right+5;
      int h = jpDetailView.getPreferredSize().height+getWindow().getInsets().top+getWindow().getInsets().bottom+
        jTabPane.getInsets().top+jTabPane.getInsets().bottom+30;
      return new Dimension(w,h);
    } catch (Exception ex) {
      return getWindow().getSize();
    }
  }
  private void fillSizes() {//puni sizeove neposredno poslije showa
    if (!smartResize) return;
    //if (detailSize == null)
    detailSize = calcDetailSize();
    if (tableSize != null) tableSize = calcTableSize();
    System.out.println("detailSize = "+detailSize);
    System.out.println("tableSize = "+tableSize);
  }
  /**
   * Definira velicinu ekrana pri tablicnom prikazu
   * @param witdh  sirina ekrana
   * @param height visina ekrana
   */
  public void setTableSize(int width, int height) {
    tableSize = new Dimension(width, height);
  }

  //end of mijenjanje sizea
  /** Makne sa ekrana onu dosadnu ikonicu koja kopira tekuci zapis */
  public void removeRnvCopyCurr() {

    getNavBar().getNavContainer().remove(rnvCopyCurr);

    getNavBar().getNavContainer().clcSize(getNavBar().xyc);

  }
  
  public void removeOption(raNavAction act) {
    getNavBar().getNavContainer().remove(act);
    getNavBar().getNavContainer().clcSize(getNavBar().xyc);
  }

  private void calcRaDetailPanelSize() {

    int panelheight = raDetailPanel.getPreferredSize().height;

    int panelwidth = raDetailPanel.getSize().width;

    int navbarheight = getNavBar().getPreferredSize().height;

    int navbarwidth = getNavBar().getPreferredSize().width;

    this.raDetailPanel.setPreferredSize(new Dimension(

    navbarwidth > panelwidth ? navbarwidth: panelwidth ,

      panelheight

      ));

    jTabPane.setTitleAt(0,frameTitle);

  }

///////////////// KRAJ VARIJANTA BROWSA

  public raMatPodaci(JPanel newJPanel,com.borland.dx.sql.dataset.QueryDataSet newRaQueryDataSet, int version ) {

    this.version=version;

    try {

      this.setRaQueryDataSet( newRaQueryDataSet);

      this.setRaDetailPanel(newJPanel);

      if (version==1)

        jbInit();

      else

        jbInit2();

    }

    catch(Exception ex) {

      ex.printStackTrace();

    }

  }

  /**
   * Funkcija koja se izvrsava kod pritiska na Novi ili Izmjena treba je overidati tako da
   *
   * u njoj bude ovisno o modu ('I' izmjena, ''N' novi) offana polja npr. sifra kod izmjene ili
   *
   * pronalazenje nove sifre i sl.
   * @param mode mod rada (B|I|N)
   */

  public void EntryPoint(char mode) {}

  /**

   * Izvrsava se po zavrsetku snimanja tj pritiska na tipku 'OK' u detaljnom prikazu

   */

  public void ExitPoint(char mode){}

  /**

   * Potrebno je u ovoj funkciji ovisno o modu postaviti fokus na panelu detaljnog prikaza

   */

  public abstract void SetFokus(char mode);

 /**

   * Kao sto i ime govori Provjera prije brisanja

   */

  public boolean DeleteCheck(){

    return true;

  }



  private static void paintViewport(JComponent vp,Graphics g) {

    if (vp.getComponentCount() != 1) return; // pa ti dodavaj

    Rectangle cpr = vp.getComponent(0).getBounds();
    
    drawEtchedLine(vp,g,0,cpr.y,vp.getWidth(),cpr.y);
    drawEtchedLine(vp,g,0,cpr.y+cpr.height-2,vp.getWidth(),cpr.y+cpr.height-2);
  }

  public static void drawEtchedLine(Component c, Graphics g, int x1, int y1, int x2, int y2) {

    g.setColor(c.getBackground().darker());

    g.drawLine(x1,y1,x2,y2);

    g.setColor(c.getBackground().brighter());

    g.drawLine(x1,y1+1,x2,y2+1);

  }

  /**

   * Setiranje panela za detaljni prikaz

   */

  public void setRaDetailPanel(javax.swing.JPanel newRaDetailPanel) {

    raDetailPanel = newRaDetailPanel;
    addScrolledAndCentered(raDetailPanel, jScrollPaneDetail , !hr.restart.start.isRESIZABLELAYOUT());
/*
    jScrollPaneDetail.setViewport(

        new JViewport() {

          public void paint(Graphics g) {

            super.paint(g);

            paintViewport(this,g);

          }

        }

    );

    if (!hr.restart.start.isRESIZABLELAYOUT()) {

      addCenteredLeft(raDetailPanel,jScrollPaneDetail.getViewport());

    } else {

      jScrollPaneDetail.getViewport().add(raDetailPanel);

    }
*/
  }


  public static JraScrollPane addScrolledAndCentered(JComponent compToAdd, JraScrollPane jScrollPane, boolean left) {
    if (jScrollPane == null) jScrollPane = new JraScrollPane();
/*    jScrollPane.setViewport(
        new JViewport() {
          public void paint(Graphics g) {
            super.paint(g);
            paintViewport(this,g);
          }
        }
    );*/

    if (left) {
      addCenteredLeft(compToAdd,jScrollPane.getViewport());
    } else {
      jScrollPane.getViewport().add(compToAdd);
    }
    return jScrollPane;
  }

  public static void addCentered(JComponent compToAdd, Container cotainerForAdd) {
    compToAdd.setBorder(
        BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED)
        );
    cotainerForAdd.setLayout(new hr.restart.swing.layout.raCenteredXYLayout());
    cotainerForAdd.add(compToAdd);
  }

  public static void addCenteredLeft(JComponent compToAdd, Container cotainerForAdd) {
    if (compToAdd.getBorder() == null || compToAdd.getBorder() instanceof javax.swing.border.EmptyBorder) {
      //eh?
    } else {
      compToAdd.setBorder(null);
    }
//    parseComp(compToAdd);
    //compToAdd.setBorder(javax.swing.BorderFactory.createEtchedBorder());
/* pre mac
    cotainerForAdd.setLayout(new hr.restart.swing.layout.raCenteredXYLayout(
        hr.restart.swing.layout.raCenteredXYLayout.LEFT
        ));
    cotainerForAdd.add(compToAdd);
    */
    //post mac
    JPanel p = new JPanel() {
      public void paint(Graphics g) {
        super.paint(g);
        paintViewport(this,g);
      }      
    };
    p.setLayout(new hr.restart.swing.layout.raCenteredXYLayout(
        hr.restart.swing.layout.raCenteredXYLayout.LEFT
    ));
    p.add(compToAdd);
    cotainerForAdd.add(p);
  }



  public static void parseComp(JComponent compToAdd) {
    List comps = raCommonClass.getraCommonClass().getComponentTree(compToAdd);
    for (int i = 0; i < comps.size(); i++) {
      Object memb = comps.get(i);
      if (memb instanceof JPanel || memb instanceof JTabbedPane) {
        JComponent innerPanel = (JComponent)memb;
        if (innerPanel.getBorder() == null || innerPanel.getBorder() instanceof javax.swing.border.EmptyBorder) {
//          System.out.println("NOT removing border");
          //-mozda nesto?
        } else {
//          System.out.println("removing border "+innerPanel.getBorder());
          innerPanel.setBorder(null);
        }
      }
    }
  }

  /**
   * Vraca panel za detaljni prikaz
   * @return panel za detaljni prikaz
   */

  public javax.swing.JPanel getRaDetailPanel() {

    return raDetailPanel;

  }

  /**

   * overridana metoda setTitle zbog toga sto mi treba naslov ekrana

   */

  public void setTitle(String ftitle) {

    super.setTitle(ftitle);

    frameTitle = ftitle;

    jpTableView.setTasTitle(ftitle);

    if (version>1) jTabPane.setTitleAt(0,frameTitle);

  }

  /**
   * Vraca mod u kojem se trenutno nalazimo
   * @return mod u kojem se trenutno nalazimo
   */



  public char getMode() {

    return mode;

  }

  /**

   * Setira mode - koristi se interno

   */

  public void setMode(char newMode) {

    char oldMode = mode;

    if (lockedMode!='0') {

      mode = lockedMode;

      lockedMode='0';

    } else {

      mode=newMode;

    }

    afterSetMode(oldMode,newMode);

//    mode = newMode;

  }

  /**

   * Poziva se nakon promjene moda rada ('B' browse, 'I' izmjena, 'N' novi)

   * @param oldMod mod koji je bio

   * @param newMod mod koji je sada

   */

  public void afterSetMode(char oldMod,char newMod) {

  }

  /**

   * Seter za Query data set obavezno postaviti

   */

  public void setRaQueryDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaQueryDataSet) {

    setRaQueryDataSet(newRaQueryDataSet,true);

  }

  private void setRaQueryDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaQueryDataSet, boolean handleJPTV) {

    if (raQueryDataSet != null) {

      raMPQDSNavListener.uninstall(raQueryDataSet);

//raQueryDataSet.removeOpenListener(notifyOpen);

      rmvTransactionDB(raQueryDataSet.getDatabase());

    }

    raQueryDataSet = newRaQueryDataSet;

//-opn-    raQueryDataSet.open();
    if (raQueryDataSet != null) {
      setSoftDataLockEnabled(true);
      addTransactionDB(raQueryDataSet.getDatabase());
    }

    if (handleJPTV) {

      jpTableView.setDataSet(raQueryDataSet);

    }

    getNavBar().setNavTable(jpTableView.getMpTable());

    try {

      if (raQueryDataSet != null)
        raMPQDSNavListener.install(raQueryDataSet);

//raQueryDataSet.addOpenListener(notifyOpen);

    } catch (Exception e){e.printStackTrace();}

  }

///testic

  com.borland.dx.dataset.OpenListener notifyOpen = new com.borland.dx.dataset.OpenListener() {

    public void opening(com.borland.dx.dataset.DataSet ds) {

      System.out.println("opening "+ds.getTableName());

      if (ds instanceof com.borland.dx.sql.dataset.QueryDataSet)

        System.out.println("query = "+((com.borland.dx.sql.dataset.QueryDataSet)ds).getQuery().getQueryString());

    }

    public void opened(DataSet ds) {

      System.out.println("opened "+ds.getTableName());

      if (ds instanceof com.borland.dx.sql.dataset.QueryDataSet)

        System.out.println("query = "+((com.borland.dx.sql.dataset.QueryDataSet)ds).getQuery().getQueryString());

    }

    public void closing(com.borland.dx.dataset.DataSet ds) {

      System.out.println("closing "+ds.getTableName());

    }

    public void closed(DataSet ds) {

      System.out.println("closed "+ds.getTableName());    }

  };



/**

 * NavigationListener za automatski refilter stavaka u modu Zaglavlje/Stavke

 * U raMasterDetail zove u raQueryDataSet_navigated(e) QueryDataSet.refilter

 */

  private NavigationAdapter raMPQDSNavListener = new NavigationAdapter() {

      public void navigated(DataSet ds) {
        showDetailsInHelp();
        raQueryDataSet_navigated(null);
      }

  };



  public void showDetailsInHelp() {

/*

    if (!hr.restart.start.isMainFrame()) return;

    if (!jpTableView.getMpTable().isShowing()) return;



    String detText = "";

    com.borland.dx.dataset.Column[] cols = raQueryDataSet.getColumns();

    for (int i=0;i<cols.length;i++) {

      if (cols[i].getVisible()!=0)

        detText=detText.concat(cols[i].getCaption()+": "+

                               lookupData.getlookupData().getColStringVal(cols[i],raQueryDataSet))+"\n"+

                               "\n";

    }

    hr.restart.mainFrame.getMainFrame().helpMsg(detText,raImages.DEFAULTBROWSEICON,getTitle());

*/

  }

  public void show() {
    if (hr.restart.start.FRAME_MODE == raFrame.PANEL) {

      javax.swing.JTabbedPane mainTab = hr.restart.mainFrame.getMainFrame().mainTabs;

      if (mainTab.indexOfComponent(getContentPane())==-1) {

        getContentPane().addComponentListener(panelShowAdapter);

      }

    }

    initBefShow();
    if (canAccessTable('P')) {
      super.show();
    }
    initShow();

  }

/**

 * Poziva se neposredno prije prikazivanja raMatPodaci

 */

  public void beforeShow() {

  }



  public raJPTableView getJpTableView() {

    return jpTableView;

  }
  
  public String getAdditionalSaveName() {
    return "";
  }
  
  private String oldSaveName = null;
  public void forceColInitOnShow() {
    oldSaveName = "forceColInitOnShow";
  }
  public void initColBean() {
    String saveName = getClass().getName() + getAdditionalSaveName();

    if (getRaMasterDetail() != null) {
      if (getRaMasterDetail().raMaster == this)
        saveName = getRaMasterDetail().getClass().getName().concat("-master");
      else if (getRaMasterDetail().raDetail == this)
        saveName = getRaMasterDetail().getClass().getName().concat("-detail");
    }

    getNavBar().getColBean().setSaveName(saveName);
    boolean init = getNavBar().getColBean().isInitialized();
    if (!init) getNavBar().getColBean().eventInit();
    else if (oldSaveName != null && !oldSaveName.equals(saveName)) {
      init = false;
      //setRaQueryDataSet(getRaQueryDataSet());
      getNavBar().getColBean().initialize();
    }
    oldSaveName = saveName;
    if (!init) pack();
  }

  void initBefShow() {
    initColBean();
    
    removeSelection();
    
    applySort();
    
    getRaQueryDataSet().open();

    beforeShow();

//    setEnabledNavAction(getNavBar().rnvToggleTable,false);

  }

  private boolean autoFirst = true;
  public void setAutoFirstOnShow(boolean autoFirst) {
    this.autoFirst = autoFirst;
  }
  
  public boolean isNormal() {
    return true;
  }

  void initShow() {

    if (version==1) {
      jTabPane.setSelectedIndex(0); //version=1
    }

    if (version>1 && isNormal()) myCC.EnabDisabAll(getRaDetailPanel(),false); //version=2 i 3
    showing_s2t = true;//da ne unlocka ako je zalokan
    switch2table();

//System.out.println("initShow");

    if (autoFirst && !raQueryDataSet.isEmpty()) raQueryDataSet.first();

    jeprazno();

  }

  public void hide() {

    rnvExit_action();

    //this_hide();

  }



  public void this_hide() {

    super.hide();

    if (hr.restart.start.isMainFrame()) {

      if (hr.restart.start.FRAME_MODE == raFrame.PANEL) {

        removeKeyListeners(hr.restart.mainFrame.getMainFrame().mainTabs);

        removeKeyListeners(hr.restart.mainFrame.getMainFrame());

        this_compHidden();

        getContentPane().removeComponentListener(panelShowAdapter);

        if (hr.restart.mainFrame.getMainFrame().mainTabs.getTabCount()==0) {

//          hr.restart.mainFrame.getMainFrame().raHelpPaneMain.clearHelp();

//          hr.restart.mainFrame.getMainFrame().validate();

        }

      }

    } else {

  //    this.removeComponentListener(panelShowAdapter);

    }
    ZatvoriOstalo();
    removeFilter();
  }
  
  public void removeFilter() {
    if (raQueryDataSet != null) {
      RowFilterListener filter = raQueryDataSet.getRowFilterListener();
      if (filter != null) {
        raQueryDataSet.removeRowFilterListener(filter);
        raQueryDataSet.setSort(raQueryDataSet.getSort());
        try {
          getColumnsBean().checkFilter();
        } catch (Exception ex) {}
      }
    }
  }

  private int answ = -1;

  public int getAnswerSaveAllDataChanges() {

    return answ;

  }

  private String saveChangesMessage = "Želite li snimiti unesene podatke u bazu ?";
  private int defaultSaveChangesAnsw = 0;

  /**
   * ako je null ne baca poruku i koristi setDefaultSaveChangesAnsw(int)
   */
  public void setSaveChangesMessage(String msg) {
    saveChangesMessage = msg;
  }
  /**
   * 0 - snimi promjene
   * 1 - nemoj snimit promjene
   */
  public void setDefaultSaveChangesAnsw(int ans) {
    defaultSaveChangesAnsw = ans;
  }

  boolean saveAllDataChanges() {
    if (saveChanges) return true;
    if (raQueryDataSet.isEmpty()) {
      answ = 1;
      return true;
    }
    if (saveChangesMessage == null) {
      answ = defaultSaveChangesAnsw;
    } else {
      answ = javax.swing.JOptionPane.showOptionDialog(
        null,
        saveChangesMessage,
        getTitle(),
        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,new String[] {"Da","Ne","Natrag"},"Da");
    }
    if (answ == 0) {
      try {
        saveChanges = true;
        saveData();
        saveChanges = false;
        //raQueryDataSet.saveChanges();
        return true;
      } catch (Exception e) {
        raDBErrHandler.handleErr(e,raQueryDataSet);
        return false;
      }
    } else if (answ == 1) {

      refreshTable();

      return true;

    } else if (answ == 2) {

      return false;

    }

    return false;

  }

/**

 * Kao da si pritisnuo akciju refresh na columnsBeanu

 */

  public void refreshTable() {

//    jpTableView.getColumnsBean().rnvRefresh.actionPerformed(null);

//    raQueryDataSet.refresh();

//    jpTableView.getColumnsBean().initialize();

  }

  /**

   * Disabla add action (onaj gumbi\u0107 sa lijepom slikicom u vrhu za dodavanje).

   * u tom slucaju ne rade niti tipke njemu pridru?ene

   */

  public void disableAdd() {

    getNavBar().rnvAdd.setLockEnabled(false);

    getNavBar().rnvAdd.setEnabled(false);

    getNavBar().rnvAdd.setLockEnabled(true);

  }

  /**

   * suprotno od disableAdd()

   */

  public void enableAdd() {

    getNavBar().rnvAdd.setLockEnabled(false);

    getNavBar().rnvAdd.setEnabled(true);

  }

/**

 * Disabla ili enabla raNavAction

 * @param rnv navAction za operaciju

 * @param enabled true je enabla, false je disabla

 */

  public void setEnabledNavAction(raNavAction rnv,boolean enabled) {

    rnv.setLockEnabled(false);

    rnv.setEnabled(enabled);

    if (!enabled) rnv.setLockEnabled(true);

  }

/**

 * Omogucuje ili onemogucuje edit dataseta odnosno sprjecava

 * novi (F2) izmjena (F4) brisanje (F3) koji su setirani u editNavActions

 * @param enabled

 */

  public void setEditEnabled(boolean enabled) {

    if (editNavActions == null) setEditNavActions(getDefaultEditNavActions());

    for (int i=0;i<editNavActions.length;i++) setEnabledNavAction(editNavActions[i],enabled);

  }

/**

 * Setira editNavActions koji se disablaju i enablaju sa setEditEnabled(boolean)

 * @param edrnvs raNavActioni koji se disablaju i enablaju sa setEditEnabled(boolean)

 */

  public void setEditNavActions(raNavAction[] edrnvs) {

    editNavActions = edrnvs;

  }

/**

 * @return raNavActione koji se disablaju i enablaju sa setEditEnabled(boolean)

 */

  public raNavAction[] getEditNavActions() {

    return editNavActions;

  }

/**

 * @return defaultne raNavActione koji se disablaju i enablaju sa setEditEnabled(boolean) a to

 * su: novi (F2) izmjena (F4) brisanje (F3)  do prvog overrida :))

 */

  public raNavAction[] getDefaultEditNavActions() {

    return new raNavAction[] {getNavBar().rnvAdd,getNavBar().rnvDelete,getNavBar().rnvUpdate};

  }

/**

 * Dodaje jednu raNavAction u editNavActions

 * @param newrnv ta jedna jel

 */

  public void addEditNavAction(raNavAction newrnv) {

    if (editNavActions == null) setEditNavActions(getDefaultEditNavActions());

    int newLen = editNavActions.length+1;

    raNavAction[] newEditNavActions = new raNavAction[newLen];

    for (int i=0;i<editNavActions.length;i++) newEditNavActions[i] = editNavActions[i];

    newEditNavActions[newLen-1] = newrnv;

    editNavActions = newEditNavActions;

  }



  private void addKeyListeners(java.awt.Component cmp) {

    cmp.addKeyListener(MojKeyListener);

    jpTableView.initKeyListener(cmp);
    
    AWTKeyboard.registerKeyListener(cmp, MojKeyListener);
    
    okp.registerOKEnter(cmp);

    //getNavBar().registerNavBarKeys(cmp);

  }

  private void addKeyListeners(raFrame rfr) {

    rfr.addKeyListener(MojKeyListener);

    jpTableView.initKeyListener(rfr);
    
    AWTKeyboard.registerKeyListener(rfr.getWindow(), MojKeyListener);
    
    okp.registerOKEnter(rfr);

    //getNavBar().registerNavBarKeys(rfr);

  }

  private void removeKeyListeners(java.awt.Component cmp) {
    
    cmp.removeKeyListener(MojKeyListener);
    
    jpTableView.rmKeyListener(cmp);
    
    AWTKeyboard.unregisterKeyListener(cmp, MojKeyListener);
    
    okp.unregisterOKEnter(cmp);

//  getNavBar().unregisterNavBarKeys(cmp);

  }

  private void removeKeyListeners(raFrame rfr) {

    rfr.removeKeyListener(MojKeyListener);

    jpTableView.rmKeyListener(rfr);
    
    AWTKeyboard.unregisterKeyListener(rfr.getWindow(), MojKeyListener);
    
    okp.unregisterOKEnter(rfr);

//   getNavBar().unregisterNavBarKeys(rfr);

  }



  public void jeprazno()  {

    try{

      if (!getJpTableView().isTableView()) {

        offon(true);

        return;

      }

      if (raQueryDataSet.isEmpty()) {

        offon(false);

      } else {

        offon(true);

      }

    } catch (Exception e){e.printStackTrace();}

  }



  /**

   * Vraca Query DataSet

   */

  public com.borland.dx.sql.dataset.QueryDataSet getRaQueryDataSet() {

    return raQueryDataSet;

  }



  void handle_jTabbedPane() {
    if (version == 1) set2DetailSize();
    jTabPane.setSelectedIndex(1);
    jTabPane.setEnabledAt(1,true);
    jTabPane.setEnabledAt(0,false);
  }



  public void prepareDetails() {
    
    unFixJlrNavFieldDataSetStatus();

    if (version==1) {

//20.11.2001 na kraj      handle_jTabbedPane();

      ChangeEnabDisab(true);

    }

    else {

      switchPanel(false,true);

    }

    switchButton(false,true);

    if (getMode() == 'N') {

      Insertiraj();

    }

    EntryPoint(mode);

    applyNavFields();

    // za provjeru integriteta (ab.f)
    if (myItg != null && getMode() == 'I') myItg.saveRow();

    if (version==1 && jTabPane.getComponentCount() > 1) {

      handle_jTabbedPane();

    } else {

      SetFokus(mode);

    }

  }



  public void rnvCopyCurr_action() {

    if (!rnvCopyCurr.isEnabled()) return;

    if (!getNavBar().rnvAdd.isEnabled()) return;
    
    if (!canAccessTable('N')) {
      return;
    }

    copyToNew = true;

    setMode('N');

    prepareDetails();

  }



  public void rnvAdd_action() {

    if (!getNavBar().rnvAdd.isEnabled()) return;
    if (!checkSoftLockMaster(true)) return;
    if (!canAccessTable('N')) {
      return;
    }
    copyToNew = false;

    setMode('N');

    prepareDetails();

  }

  public void rnvUpdate_action() {

    if (!getNavBar().rnvUpdate.isEnabled()) return;

    if (raQueryDataSet.isEmpty()) return;
    if (!canAccessTable('I')) {
      return;
    }
    // provjera zakljucavanja
    if (!checkSoftLock()) return;
    // provjera integriteta (ab.f)
    if (myItg != null && !myItg.checkEdit()) return;

    setMode('I');

    prepareDetails();

  }



  public void applyNavFields() {

    if (getRaDetailPanel() == null) return;

    java.util.LinkedList ll = myCC.getComponentTree(getRaDetailPanel());

    for (int i=0;i<ll.size();i++) {

      Object ob = ll.get(i);

      if (ob instanceof hr.restart.util.JlrNavField) {

        ((JlrNavField)ob).doOnShow();

      }

    }

  }



  public void rnvDelete_action() {

    if (!getNavBar().rnvDelete.isEnabled()) return;



   if (raQueryDataSet.isEmpty()) {

      JOptionPane.showMessageDialog(null,"Nema podataka za brisanje !","Poruka",JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    LegalDelete(false);

    jeprazno();

  }



  boolean LegalDelete(boolean fakemode) {

    return LegalDelete(fakemode,true);

  }

  /** zove deleteAll sa svim porukama deleteAll(true)
   * @return jel uspio il nije
   */
  public boolean deleteAll() {
    return deleteAll(true);
  }
  private boolean deletingAll = false;
  /** Brise sve stavke u getRaQueryDataset(), postoji mogucnost prikazivanja dviju
   * poruka:
   * <PRE>
   * - ako nema niti jednog podatka <I>Nema podataka za brisanje !</I>
   * - pitanje <I>Zelite li obrisati sve zapise na ekranu?</I>
   * obje se poruke iskljucuju parametrom messages = false
   * </PRE>
   * @param messages da li da prikazuje navedene poruke ili ne
   * @return jel uspio il nije
   */
  public boolean deleteAll(boolean messages) {
    deletingAll = true;
    boolean ret;
    try {
      jpTableView.enableEvents(false);
      ret = delAll(messages);
    } catch (Exception ex) {
      System.out.println("deleteAll:");
      ex.printStackTrace();
      ret = false;
    }
    deletingAll = false;
    jpTableView.enableEvents(true);
    return ret;
  }
  
  boolean fastDel = false;
  
  public void setFastDelAll(boolean fast) {
    fastDel = fast;
  }

  private boolean delAll(boolean messages) {
    if (!canAccessTable('B')) {
      return false;
    }
    if (getRaQueryDataSet().getRowCount() == 0) {
      if (messages)
        JOptionPane.showMessageDialog(null,"Nema podataka za brisanje !","Poruka",JOptionPane.INFORMATION_MESSAGE);

      return true;

    }
    if (messages)
      if (!PorukaDelete("Želite li obrisati sve zapise na ekranu?")) return false;

    if (fastDel) {
      try {
        getRaQueryDataSet().deleteAllRows();
        getRaQueryDataSet().saveChanges();
        if (!(getRaQueryDataSet() instanceof raDataSet))
          dM.getSynchronizer().propagateChanges(getRaQueryDataSet());
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    
    getRaQueryDataSet().last();
    do {
      if (!LegalDelete(true)) {
        return false;
      }

    } while (getRaQueryDataSet().prior());

    do {
      getRaQueryDataSet().last();
      if (!LegalDelete(false,false)) {
        return false;
      }

    } while (!getRaQueryDataSet().isEmpty());

    return true;

  }

  /** Brise tekuci slog

   * @param fakemode ako je true samo provjeri da li je moguce obrisati slog (DeleteCheck,BeforeDelete), ako je false stvarno ga i obrise

   * @param poruka da li da izbaci poruku sa pitanjem Stvarno zelite obrisati ?

   * @return true ako je brisanje ili provjere uspjelo

   */

  public boolean LegalDelete(boolean fakemode,boolean poruka) {

    if (!canAccessTable('B')) {
      return false;
    }

    String msg = null;

    if (poruka) msg = "Stvarno želite obrisati ?";

    try {
			if (!DeleteCheck()) return false;
		} catch (SanityException e) {
			JOptionPane.showMessageDialog(getWindow(), "Brisanje nije moguæe: " + e.getMessage(),
					"Greška", JOptionPane.ERROR_MESSAGE);
			return false;
		}

    if (!fakemode) {

      if (poruka) {

        if (!PorukaDelete()) return false;

      }

    }

    try {
			if (!BeforeDelete()) return false;
		} catch (SanityException e) {
			JOptionPane.showMessageDialog(getWindow(), "Brisanje nije moguæe: " + e.getMessage(),
					"Greška", JOptionPane.ERROR_MESSAGE);
			return false;
		}

    // provjera integriteta (ab.f)
    if (myItg != null && !myItg.checkDelete()) return false;


    if (fakemode) return true; //tu je gotov sa provjerama
    //e nije!
    if (isRowLocked(true)) return false;
    try {

      raQueryDataSet.deleteRow();

      saveData();

      posTable();

      AfterDelete();

      return true;

    } catch (Exception e) {

      AfterSaveFailed(mode);

      raDBErrHandler.handleErr(e,raQueryDataSet);

      return false;

    }

  }



  private void posTable() {

    if (jpTableView.getMpTable() instanceof hr.restart.swing.JraTable2) {

      ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).pos();

    }

  }

  public boolean BeforeDelete(){

    return true;

  }

  public boolean PorukaDelete(){

    return PorukaDelete("Stvarno želite obrisati ?");

  }

  public boolean PorukaDelete(String txt){

    if (txt==null) return true;

    if (JOptionPane.showConfirmDialog(null,txt,"Potvrda brisanja",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)

      return true ;

    else

      return false ;

  }

  /**

   * Poruka koja se prikazuje nakon uspjesnog brisanja

   */

  public void AfterDelete(){

   // JOptionPane.showMessageDialog(null,"Brisanje uspje?no obavljeno !","Poruka",JOptionPane.INFORMATION_MESSAGE);

  }





  public void rnvExit_action() {

//    if (mode!='B') return;
    jpTableView.hidePopups();

    if (!getNavBar().rnvExit.isEnabled()) {
      return;
    }


    try {
//treba i odlokat jer je mozda stisnuo [x] usred update-a
      softUnlock();
      canceliraj();

    } catch (Exception e) {

      raDBErrHandler.handleErr(e,raQueryDataSet);

    }

    if (ValidacijaPrijeIzlaza()) {

      if (saveAllDataChanges()) {

        this_hide();



      }

    }

  }



  public void setToggleTableEnabled(boolean enab) {

    getJpTableView().setToggleTableEnabled(enab);

  }



  public boolean isToggleTableEnabled() {

    return getJpTableView().isToggleTableEnabled();

  }



  public void beforeToggleTable() {

    getJpTableView().findNoTablePanelValues(null);

  }



  public void afterToggleTable() {

  }



  public void rnvTable_action() {

    beforeToggleTable();

    if (getJpTableView().toggleTableView()) {

      setRaQueryDataSet(getJpTableView().getDataSet(),false);

      rebindRaDetailPanel();

      if (getJpTableView().isTableView()) {

        compToKillFocus = null;

      } else {

        compToKillFocus = getJpTableView().getFocusedNoTablePanelField();

      }

      jeprazno();

    }

    afterToggleTable();

  }



  public void rebindRaDetailPanel() {

    Object[] arr = Util.getUtil().getDBComps(getRaDetailPanel(),com.borland.dx.dataset.DataSetAware.class).toArray();

    for (int i = 0; i < arr.length; i++) {

      if (arr[i] instanceof com.borland.dx.dataset.DataSetAware) {

        com.borland.dx.dataset.DataSetAware dsa = (com.borland.dx.dataset.DataSetAware)arr[i];

        if (dsa instanceof com.borland.dx.dataset.ColumnAware) {

          com.borland.dx.dataset.ColumnAware cola = (com.borland.dx.dataset.ColumnAware)dsa;

          if (getRaQueryDataSet().hasColumn(cola.getColumnName()) != null) {

            cola.setDataSet(getRaQueryDataSet());

          }

        } else {

          dsa.setDataSet(getRaQueryDataSet());

        }

      }

    }

  }



  public void requestFocus() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jTabPane.requestFocus();//11.09.2001. .. maknuto sa zadnje linije metode 10.10.2001
      }
    });
    
  }

/**

 * Kad se zatvori raMatpodaci window

 */

  public void ZatvoriOstalo(){}

/**

 * Izvrsava se prije zatvaranja raMatPodaci windowa,

 * odnosno pritiskom na tipku escape u browse modu odnosno na gumbic 'izlaz'

 */

  public boolean ValidacijaPrijeIzlaza(){

    return true;

  }

  /**

   * Izvr?ava se nakon raQueryDataSet.cancel() pri pritiku na Prekid gumbic na detailViewu

   */

  public void AfterCancel() {

  }



  private void canceliraj() {

    raQueryDataSet.cancel();

    if (raQueryDataSet.getStatus()==com.borland.dx.dataset.RowStatus.INSERTED&&saveChanges) {

//stanoviti bug fix kod borlanda (valjda)

      raQueryDataSet.deleteRow();

    }

  }



  void jPrekid_action() {

//    if (!okp.jPrekid.isEnabled()) return;
  	if (getMode() != 'N') memorizePreviousValues();

    startTableFire();

    try {

      canceliraj();

      AfterCancel();

    } catch (Exception e) {

      raDBErrHandler.handleErr(e,raQueryDataSet);

    }

    switch2table();

    switchButton(true,false);

    setMode('B');

    fixJlrNavFieldDataSetStatus();

  }

  /**

   * Zbog jlrNavfieldova koji kontroliraju status pa da ga vrati na onaj pri kojem rade

   */

  private void fixJlrNavFieldDataSetStatus() {

    raQueryDataSet.statusMessage(com.borland.dx.dataset.StatusEvent.EDIT_STARTED,"");

  }

  private void unFixJlrNavFieldDataSetStatus() {

    raQueryDataSet.statusMessage(com.borland.dx.dataset.StatusEvent.DATA_CHANGE,"");

  }

  void jBOK_action() {

    if (!okp.jBOK.isEnabled()) return;

    boolean valid = false;
    try {
			valid = Validacija(mode);
		} catch (SanityException e) {
			JOptionPane.showMessageDialog(getWindow(),
					(mode == 'N' ? "Dodavanje nije moguæe: " : "Izmjena nije moguæa: ") + 
					e.getMessage(),	"Greška", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}
    
    // provjera integriteta (ab.f)
    if(valid && (myItg == null || mode != 'I' || myItg.checkCommit())) {
    	
    	memorizePreviousValues();

      startTableFire();

      try {

        saveData();

        AfterSave(mode);

        AfterAfterSave(mode);

      } catch (Exception e) {
        try {
          softUnlock(); // ako je puklo snimanje, nema smisla da ostane zakljucan
        } catch (Exception ex) {
        }
        // (ab.f) recovery nakon neuspjesne transakcije:
        // pozovi AfterSaveFailed(mode), za overridanje
        // prebaci na tablicni prikaz, mod 'browse'
        AfterSaveFailed(mode);
        raDBErrHandler.handleErr(e,raQueryDataSet);
        switch2table();
        switchButton(true,false);
        setMode('B');
      }

      if (getMode()=='I') setMode('B');

      if (getMode()=='N' && version==1) jeprazno();

      ExitPoint(mode);

      fixJlrNavFieldDataSetStatus();

    }
  }

  void fireTableDataChanged() {

    if (jpTableView.getMpTable() instanceof hr.restart.swing.JraTable2) {

      ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).fireTableDataChanged();

    } else {

      ((javax.swing.table.AbstractTableModel)jpTableView.getMpTable().getModel()).fireTableDataChanged();

    }

  }



  void stopTableFire() {

    ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).stopFire();

  }

  void startTableFire() {

    ((hr.restart.swing.JraTable2)jpTableView.getMpTable()).startFire();

  }

/////////////////////////////// T R A N S

  private java.util.HashSet databases = new java.util.HashSet();



  /**

   * Ako je potrebno da se transakcija odigra na vise baza istovremeno onda se tu te baze i dodaju

   * @param db baza koja ide u parametar raAbstractTransaction klona (automatski se dodaje baza od raQueryDataSeta)

   */

  public void addTransactionDB(com.borland.dx.sql.dataset.Database db) {

    databases.add(db);

  }



  /**

   * suprotna operacija od addTransactionDB

   * @param db

   */

  public void rmvTransactionDB(com.borland.dx.sql.dataset.Database db) {

    databases.remove(db);

  }



  /**

   *

   * @return

   */

  public com.borland.dx.sql.dataset.Database[] getTransactionDB() {

    com.borland.dx.sql.dataset.Database[] dbs = new com.borland.dx.sql.dataset.Database[databases.size()];

    dbs = (com.borland.dx.sql.dataset.Database[])databases.toArray(dbs);

    return dbs;

  }

  private raAbstractTransaction prepareTransaction() {

    raLocalTransaction saveTransaction = new raLocalTransaction(getTransactionDB()) {

      public boolean transaction() throws Exception {

        try {

          if (doBeforeSave(mode)) {
            saveChanges(raQueryDataSet);

            return doWithSave(mode);
          } else return false;

        }

        catch (Exception ex) {
System.out.println("Mode kod sranja ovog je = "+getMode());
ST.prnc(raQueryDataSet);
          ex.printStackTrace();

          throw ex;

        }

      }

    };

    return saveTransaction;

  }

  /**
  * Ova metoda izvrsava se unutar iste transakcije kao i raQueryDataSet.saveChanges()
  * i to POSLIJE snimanja raQueryDataSet-a ako vrati false.
  * Ako vrati false ili baci exception transakcija je neuspjesna i nije snimljen ni raQueryDataSet.
  * NAPOMENA: za saveChanges koristiti metodu raTransaction.saveChanges(QueryDataSet)
  * jer ona ne commita transakciju za razliku od QueryDataSet.saveChanges()
  * @param mode vraca mode rada 'B' je brisanje (browse) 'I' - izmjena 'N' - novi
  * @return
  */
  public boolean doWithSave(char mode) {
    return true;
  }

  /**
  * Ova metoda izvrsava se unutar iste transakcije kao i raQueryDataSet.saveChanges()
  * i to PRIJE snimanja raQueryDataSet-a ako vrati false.
  * KORISTITI ZA getSEQ !!
  * Ako vrati false ili baci exception transakcija je neuspjesna i nije snimljen ni raQueryDataSet.
  * NAPOMENA: za saveChanges koristiti metodu raTransaction.saveChanges(QueryDataSet)
  * jer ona ne commita transakciju za razliku od QueryDataSet.saveChanges()
  * @param mode vraca mode rada 'B' je brisanje (browse) 'I' - izmjena 'N' - novi
  * @return
  */
  public boolean doBeforeSave(char mode) {
    return true;
  }
///////////////////////////////
  
  public boolean isDeletingAll() {
    return deletingAll;
  }
  
  // koje tablice i datasetove treba markirati kao promijenjene
  private Set markTables = new HashSet();
  private Set markDatasets = new HashSet();
  

  public void markChange(String table) {
    markTables.add(table.toUpperCase());
  }
  
  public void markChange(DataSet ds) {
    markDatasets.add(ds);
  }
  
  void saveData() throws java.lang.Exception {

    if (saveChanges) {

//      raQueryDataSet.saveChanges();
      markTables.clear();
      markDatasets.clear();
      raAbstractTransaction trans = prepareTransaction();
      if (!trans.execTransaction()) {

        getRaQueryDataSet().refresh();

        if (trans.getLastException() instanceof SanityException)
        	throw trans.getLastException();
        
        throw new Exception("Transakcija nije uspjela!");
      }
      
      // ab.f notifikacija promjene u tablici, samo ako dataset nije raDataSet
      // jer isti to ima rijeseno unutar savechanges() metode.
      //if (!(getRaQueryDataSet() instanceof raDataSet))
        dM.getSynchronizer().propagateChanges(getRaQueryDataSet());
        
        // oznaci eventualne promijenjene tablice i datasetove 
        // unutar transakcije doWithSave i doBeforeSave
        for (Iterator i = markTables.iterator(); i.hasNext();
          dM.getSynchronizer().markAsDirty((String) i.next()));
        for (Iterator i = markDatasets.iterator(); i.hasNext();
          dM.getSynchronizer().propagateChanges((DataSet) i.next()));
        
        markTables.clear();
        markDatasets.clear();
    }

    if (!deletingAll) fireTableDataChanged();

  }

/**

 * Ako je saveChanges == true, pritiskom na OK snima podatke u bazu,

 * a ako je saveChanges == false snima ih na izlasku iz ekrana

 */

  public void setSaveChanges(boolean newSaveChanges) {

    saveChanges = newSaveChanges;

  }

  /**

   * Validacija koja se izvrsava prije snimanja ako funkcija vraca true

   * onda se narafski moze snimiti

   */

  public abstract boolean Validacija(char mode);



  /**

   * Operacija koja se izvodi nakon snimanja promjena

   */

  public void AfterSave(char mode){

//    JOptionPane.showMessageDialog(null,"Promjene uspje?no snimljene","Poruka",JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Pozove se ako je transakcija snimanja pukla. Za dodatne potrebe recovera...
     *
     */

  public void AfterSaveFailed(char mode){

  }


  public void AfterAfterSave(char mode){

    if (getMode()=='N' ) {

      Insertiraj();

      SetFokus(mode);

    } else if (getMode()=='I') {

      switch2table();

      switchButton(true,false);

    }

  }

  private boolean showing_s2t = false;

  private void switch2table() {

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jTabPane.requestFocus();//11.09.2001. .. maknuto sa zadnje linije metode 10.10.2001
      }
    });

    if (showing_s2t) showing_s2t = false;
    else softUnlock();

    if (version==1) {

      jTabPane.setEnabledAt(0,true);

      jTabPane.setSelectedIndex(0);

    } else {

      switchPanel(true,false);

    }

  }

/*

  Runnable raQueryDataSet_first = new Runnable() {

    public void run() {

      raQueryDataSet.first();

    }

  };

*/

  public boolean copyToNew = false;

  public void Insertiraj() {
  	
    stopTableFire();

    //StorageDataSet currentSet = null;
    DataRow curr = null;

    if (copyToNew && raQueryDataSet.getRowCount() > 0 && raQueryDataSet.inBounds()) {
      curr = new DataRow(raQueryDataSet);
      raQueryDataSet.copyTo(curr);
    }

    if (version!=1) raQueryDataSet.last();

    raQueryDataSet.insertRow(version==1);

    if (curr != null) {

//      currentSet.copyTo(raQueryDataSet);

      String[] kys = jpTableView.getKeyColumns();

      for (int i = 0; i < raQueryDataSet.getColumnCount(); i++) {

        String cn = raQueryDataSet.getColumn(i).getColumnName();

        if (!Util.getUtil().containsArr(kys,cn) && !curr.isAssignedNull(cn)) {

//System.out.println(cn+" ass = "+currentSet.isAssignedNull(cn)+" - unass = "+currentSet.isUnassignedNull(cn));

          Object value = hr.restart.db.raVariant.getDataSetValue(curr,cn);

          hr.restart.db.raVariant.setDataSetValue(raQueryDataSet,cn,value);

        }

      }

      copyToNew = false;

    }

  }
  
  
  Map textValues = new HashMap();
  void memorizePreviousValues() {
  	textValues.clear();
  	
  	List cs = myCC.getComponentTree(raDetailPanel);
  	for (Iterator i = cs.iterator(); i.hasNext(); ) {
  		Object obj = i.next();
  		if (obj instanceof JTextComponent) {
  			JTextComponent tf = (JTextComponent) obj;
  			if (tf.isVisible())
  				textValues.put(getKey(tf), tf.getText());
  		}
  	}
  }
  
  Object getKey(JTextComponent tf) {
  	return tf.getBounds()+"#"+tf.getParent().getBounds();
  }
  
  public void restorePreviousValue(JTextComponent tf) {
  	String val = (String) textValues.get(getKey(tf));
  	if (val != null) {
  		tf.setText(val);
  		tf.selectAll();
  	}
  }

  private void ChangeEnabDisab(boolean onoff){

    if (raDetailPanel!=null && isNormal()) myCC.EnabDisabAll(raDetailPanel,onoff);

    okp.jBOK.setEnabled(onoff);

  }



  public void tipkaPritisnuta(KeyEvent e){

    if (e==null) return;

    processCustomKeyAction(e);

    processOkpKeyEvent(e);

    e=null;

  }



  void processCustomKeyAction(KeyEvent e) {

    if (e.isConsumed()) return;

//    if (!okp.isShowing()) return;

//    if (!okp.jBOK.isEnabled()) return;

    if (getMode() == 'B') return;

    final raKeyAction rKeyAc = getKeyAction(e);

    if (rKeyAc != null) {
      e.consume();
      rKeyAc.invokeLater();
    }
  }

/**

 * Izvodi se pri pritisku na tipku escape u detailpanelu, ako vrati false prekida se izvrsavanje daljnjih metoda

 * (pritisak na button Prekid). Ako vrati true nastavlja se po starom. Koristiti ako prvi escape inicijalizira (cisti)

 * dio ekrana a drugi tek izlazi.

 */

  public boolean ValDPEscape(char mode) {

    return true;

  }

  void processOkpKeyEvent(final KeyEvent e) {

    if (e.isConsumed()) return;
    
    if (okp.isShowing()) {
      if (e.getKeyCode()==e.VK_ESCAPE && okp.jPrekid.isEnabled() ||
          e.getKeyCode()==e.VK_F10 && okp.jBOK.isEnabled()) e.consume();
      else return;
      if (!ticket.request()) return;
      try {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
  	          if (e.getKeyCode()==e.VK_ESCAPE && okp.jPrekid.isEnabled()) {            
  	            if (ValDPEscape(mode)) {
  	              okp.jPrekid.requestFocus();
  	              jPrekid_action();
  	            }
  	          } else if (e.getKeyCode()==e.VK_F10 && okp.jBOK.isEnabled()) {
  	            okp.jBOK.requestFocus();
  	            jBOK_action();
  	          }
            } finally {
          	  ticket.release();
            }
          }
        });
      } catch (RuntimeException t) {
        ticket.release();
        throw t;
      }
    }
  }

  public void offon(final boolean kako){
        
        getNavBar().rnvUpdate.setEnabled(kako);
        
        getNavBar().rnvDelete.setEnabled(kako);
        
        getNavBar().rnvPrint.setEnabled(kako);
        
        if (editNavActions != null)
          for (int i = 0; i < editNavActions.length; i++)
            if (editNavActions[i] != getNavBar().rnvAdd &&
            editNavActions[i] != getNavBar().rnvDelete &&
            editNavActions[i] != getNavBar().rnvUpdate)
              editNavActions[i].setEnabled(kako);
        
        if (version==1 && jTabPane.getComponentCount() > 1)
          jTabPane.setEnabledAt(1,kako);


  }



  public void setVisibleCols(int[] newVisibleCols) {

    getNavBar().setNavVisibleCols(newVisibleCols);

  }



  void this_windowClosing() {
    printFrameID(this);
    rnvExit_action();
//    clearHP();
  }

  static void printFrameID(Object obj) {

/*

    String frID=obj.getClass().getName();

    if (obj.getClass().getDeclaringClass() != null)

      frID=frID+" DCL--"+obj.getClass().getDeclaringClass().getName();



    frID = frID + " MDF--"+obj.getClass().getModifiers();



    frID = frID + " PKG--"+obj.getClass().getPackage();





    System.out.println(frID);

*/

  }
  
  /*
   * Provjerava je li dopuštena navigacija iz oldrow u newrow
   */
  public boolean allowRowChange(int oldrow, int newrow) {
    return true;
  }

/**

 * Pokre\u0107e se ako se ide iz redka u redak

 */

  public void raQueryDataSet_navigated(NavigationEvent e) {



  }

  // kad se lupi refresh na columnsbeanuu

  public void navbar_afterRefresh() {

  }


  public void rnvPrint_action() {

    if (!getNavBar().rnvPrint.isEnabled()) return;

    Funkcija_ispisa();

  }

/**

 * Pokrece se klikom na ispis gumb ili na F5

 * bolje upotrijebi {@link #getRepRunner() getRepRunner.addReport(provider, naziv)}

 */

  public void Funkcija_ispisa(){

    getTablePrinter().runIt();

    removeSelection();

  }

/**

 * Vraca raRunReport kojem se moze dodati report koji ce se pojaviti u comboboxu na print ekranu

 * u jbInit: getRepRunner().addReport("hr.restart.robno.repArtikli","Popis artikala");

 */

  public hr.restart.util.reports.raRunReport getRepRunner() {

    return getTablePrinter().getReportRunner();

  }



  private hr.restart.util.reports.JTablePrintRun getTablePrinter() {

    getNavBar().getColBean().setSumRow(jpTableView.getSumRow());

    TPRun.setInterTitle(getClassName());

    TPRun.setColB(getNavBar().getColBean());

    TPRun.setRTitle(this.getTitle());

    return TPRun;

  }



  public void setkum_tak(boolean bulin){

    jpTableView.setKumTak(bulin);

  }



  public String[] getstozbrojiti(){

    return jpTableView.getStoZbrojiti();

  }



  public void setstozbrojiti(String[] sto){

    jpTableView.setStoZbrojiti(sto);

  }

  public String[] getnaslovi(){

    return jpTableView.getNaslovi();

  }



  public void setnaslovi(String[] Nas){

    jpTableView.setNaslovi(Nas);

  }



  void this_windowIconified(WindowEvent e) {

      Minimiziraj();

  }



  public void Minimiziraj(){}



  void this_windowDeiconified(WindowEvent e) {

      Restauriraj();

  }



  public void Restauriraj(){}

/**

 * @deprecated

 * Dodaje raNavAction sa defaultnom tipkom F6 u toolbar i radi doClick() na klik.

 * Bolje koristi {@link #addOption(raNavAction,int)}

 */

  public void AddButton(final javax.swing.JButton button,int pozicija, boolean resiz) {

      raNavAction rnvAct = new raNavAction(button.getText(),raImages.IMGSTAV,KeyEvent.VK_F6) {

        public void actionPerformed(ActionEvent e) {

          button.doClick();

        }

      };

      this.addOption(rnvAct,pozicija);

  }

/**

 * @deprecated koristi addOption

 * @param button

 * @param pozicija

 * @param resiz

 */

  public void AddButton(final hr.restart.swing.JraButton button,int pozicija, boolean resiz) {

      raNavAction rnvAct = new raNavAction(button.getText(),raImages.IMGSTAV,KeyEvent.VK_F6) {

        public void actionPerformed(ActionEvent e) {

          button.doClick();

        }

      };

      this.addOption(rnvAct,pozicija);

  }



/**

 * dodaje raNavAction u toolbar. Vidi dokumentaciju od raNavAction, drugi parametar je pozicija gdje da ga stavi

 */

  public void addOption(raNavAction nav, int pos) {

    addOption(nav,pos,true);

  }

/**

 * dodaje raNavAction u toolbar. Vidi dokumentaciju od raNavAction, drugi parametar je pozicija gdje da ga stavi, a

 * treci je da li je to edit opcija odnosno da li se offa sa setEditEnabled

 */

  public void addOption(raNavAction nav, int pos,boolean isEditable) {

    if (getNavBar().contains(nav)) return;

    getNavBar().addOption(nav,pos);

    if (isEditable) {

      addEditNavAction(nav);

    }

  }

/**

 * Enabla odre\u0111eni keypressed na detailpanelu. Parametar key je jedna od varijabli java.awt.event.KeyEvent .

 * npr: this.enableKey(java.awt.event.KeyEvent.VK_F10).

 * Defaultno su sve tipke enablane.

 */

  public void enableKey(int key) {

    Integer keyInteger = new Integer(key);

    if (disabledKeys.contains(keyInteger)) {

      disabledKeys.removeElement(keyInteger);

    }

  }

/**

 * Disabla odre\u0111eni keypressed na DETAIL PANELU (nakon pritiska novi ili izmjena). Parametar key je jedna od varijabli java.awt.event.KeyEvent;

 * npr: this.disableKey(java.awt.event.KeyEvent.VK_F10).

 * Defaultno su sve tipke enablane.

 */

  public void disableKey(int key) {

    Integer keyInteger = new Integer(key);

    if (!disabledKeys.contains(keyInteger)) {

      disabledKeys.addElement(keyInteger);

    }

  }

  private java.util.Vector disabledKeys = new java.util.Vector();

  private boolean isKeyDisabled(int key) {

    return disabledKeys.contains(new Integer(key));

  }

/**

 * <pre>

 * Registrira akciju na zadani keystroke koja ce se izvrsavati na DETAIL PANELU (nakon pritiska novi ili izmjena).

 * npr: addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F7) {

 *        public void keyAction() {

 *          System.out.println("Stisnuo si F7 i jako ga boli");

 *        }

 *      });

 * </pre>

 */

  public void addKeyAction(raKeyAction keyaction) {

    if (!keyStrokes.contains(keyaction)) keyStrokes.addElement(keyaction);

  }



  public raKeyAction[] getKeyActions() {

    return (raKeyAction[])keyStrokes.toArray(new raKeyAction[keyStrokes.size()]);

  }



  private java.util.Vector keyStrokes = new java.util.Vector();





  private raKeyAction getKeyAction(KeyEvent ke) {

    for (int i=0;i<keyStrokes.size();i++) {

      try {

        raKeyAction keyActionMember = (raKeyAction)keyStrokes.get(i);

        if (keyActionMember.equals(ke)) return keyActionMember;

      } catch (Exception e) {

        System.out.print(e);

      }

    }

    return null;

  }



/**

 * Stavlja setAutoResizeMode na tablicu. Modovi su definirani u javax.swing.JTable pa vidi...

 */

  public void setAutoResizeMode(int mod) {

    jpTableView.getMpTable().setAutoResizeMode(mod);

  }

/**

 * Getter za okPanel tako da se mogu pozivati doClick, setText, setEnabled ...etc etc na buttone

 * getOKpanel().jBOK i getOKpanel().jPrekid

 */

 public OKpanel getOKpanel() {

  return okp;

 }

///////// Z A J E D N I C K E   F U N K C I J E   raMat2 //////////////////////

  public void switchButton(boolean prvi,boolean drugi){

    if (prvi) {

      okp.jBOK.setEnabled(false);

    } else {

      okp.jBOK.setEnabled(true);

    }

    if (version!=1) okp.jPrekid.setEnabled(!prvi);

  }



  public void switchPanel(boolean prvi,boolean drugi){

//      myCC.EnabDisabAll(jpTableView,prvi);

      myCC.EnabDisabAll2(jpTableView,prvi,myCC.DEFENABMODE,jpTableView.getNoTablePanel());

//      if (!jpTableView.isTableView()) myCC.EnabDisabAll(jpTableView.getNoTablePanel(),prvi,myCC.STANDARD);

//

      if (version == 3) myCC.EnabDisabAll(getNavBar(),prvi);//jer navbar nije u jptableviewu

      myCC.EnabDisabAll(jpDetailView,drugi);

      myCC.EnabDisabAll(jPnibii_cont,false);

  }

  /**

   * Poziva se pri double clicku na tablicu

   */

  public void table2Clicked() {

    rnvUpdate_action();

  }
/*
  private void initHP() {
    if (hr.restart.start.isMainFrame()) {
//      hr.restart.mainFrame.getMainFrame().raHelpPaneMain.initHP(this);
    } else {
      raLLFrames.getRaLLFrames().getMsgStartFrame().initHP(this);
    }
  }

  private void clearHP() {
    if (hr.restart.start.isMainFrame()) {
//      hr.restart.mainFrame.getMainFrame().raHelpPaneMain.forceInitHP(null);
    } else {
      raLLFrames.getRaLLFrames().getMsgStartFrame().clearHP();
    }
  }
*/
  public raNavBar getNavBar() {

    return ranibii;

  }



  void setRaMasterDetail(raMasterDetail rmd) {

    raMD = rmd;

  }

/**

 * Ako je ovaj raMatPodaci dio raMasterDetaila tu vrati taj raMasterDetail. U protivnom vrati null

 * @return raMasterDetail kojeg je ovaj dio

 */

  public raMasterDetail getRaMasterDetail() {

    return raMD;

  }


  // Provjera integriteta (ab.f)

  private raDataIntegrity myItg = null;

  public void setDataIntegrity(raDataIntegrity itg) {
    myItg = itg;
  }

  public raDataIntegrity dataIntegrity() {
    return myItg;
  }

  public JTabbedPane getTab() {
    return jTabPane;
  }

  private String[] sortColumns;

  /**

   * Definira po kojim kolonama trebaju biti sortirani podaci u tablici.

   * @param sortcols string array kolona

   */

  public void setSort(String[] sortcols) {

    sortColumns = sortcols;

    applySort();

  }

  public String[] getSort() {

    if (sortColumns == null) {

      if (!getNavBar().getColBean().isInitialized()) {

        return null;

      }
      
      if (getRaMasterDetail() == null) {

        String sort0 = jpTableView.getMpTable().getRealColumnName(0);

        if (sort0 == null || sort0.length() == 0) return null;

        sortColumns = new String[] {sort0};
      }

    }

    return sortColumns;

  }

  private void applySort() {

    try {

      if (raQueryDataSet == null) return;

      if (getSort() == null) return;

      raQueryDataSet.setSort(new com.borland.dx.dataset.SortDescriptor(getSort()));
      if (getJpTableView().getMpTable() instanceof raExtendedTable) {
    	((raExtendedTable) getJpTableView().getMpTable()).resetSortColumns();
      }

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }



  }



  public static int getVerParam(String tag) {

    try {

      int verParam = Integer.parseInt(IntParam.getTag(tag));

      if (verParam == 1 || verParam == 2 || verParam == 3)

        return verParam;

      else

        return 1;

    }

    catch (Exception ex) {

      return 1;

    }

  }
  /** Ukljucuje i iskljucuje softversko lokiranje tablice.
   * <PRE>
   * Uvjeti: tablica mora sadrzavati column LOKK
   * Nacin rada: Pri svakom ulazu pokusava postaviti vrijednost LOKK polja na 'D'.
   * Ako LOKK vec je 'D' baca poruku upozorenja i ulazi u browse mode.
   * Koristi funkciju raUser.lockRow(Dataset, String[] keys);
   * raMatsterDetail.raDetail ima to po defaultu iskljuceno.
   * </PRE>
   * @param enabled true - ukljucuje softversko lockiranje tablice ako je to moguce
   * false - iskljucuje ga
   * @return vraca vrijednost na koju je stvarno postavljen parametar.
   * Ako tablica NE sadrzi lock polje uvijek vraca false
   */
  public boolean setSoftDataLockEnabled(boolean enabled) {
    if (!enabled) {
      softDataLock = false;
      return false;
    }
    if (getRaQueryDataSet() == null) throw new RuntimeException("Prvo je potrebno setirati dataset, pa onda enablati SoftDataLock");
    if (getRaQueryDataSet().hasColumn("LOKK") == null)
      return setSoftDataLockEnabled(false);
    softDataLock = true;
    return softDataLock;
  }
  /**
   * Da li je ukljuceno softversko zakljucavanje podataka
   * @return true - ukljuceno je, false - nije
   */
  public boolean isSoftDataLockEnabled() {
    return softDataLock;
  }

  /**
   * lokira tekuci record u raMatPodaci i(ili) njegov master record (getRaMasterDetail().raMaster.getRaQueryDataSet,
   * a buni se prigodnim JOptionPane.showMessageDialog-om ako je ukljuceno lokiranje i zapis je vec zalokan
   * @return true ako je ukljuceno lokiranje i ako je uspio zalokirati.
   * Ako nije ukljuceno lokiranje opet vraca true, a ako je zapis vec zakljucan vraca false
   */
  public boolean checkSoftLock() {
    return checkSoftLock(true);
  }

  /**
   * lokira tekuci record u raMatPodaci i(ili) njegov master record (getRaMasterDetail().raMaster.getRaQueryDataSet
   * @param showMsg - da li da se pobuni prigodnim JOptionPane.showMessageDialog,
   * a buni se ako je ukljuceno lokiranje i zapis je vec zalokan
   * @return true ako je ukljuceno lokiranje i ako je uspio zalokirati.
   * Ako nije ukljuceno lokiranje opet vraca true, a ako je zapis vec zakljucan vraca false
   */
  public boolean checkSoftLock(boolean showmsg) {
    if (isSoftDataLockEnabled()&&!raUser.getInstance().lockRow(getRaQueryDataSet(), getJpTableView().getKeyColumns())) {

      // ab.f - ako je red zakljucao isti korisnik, pusti ga dalje, vjerojatno je
      // rijec o zaostalom kljucu
      if (raUser.getInstance().checkSameUser(getRaQueryDataSet(), getJpTableView().getKeyColumns()))
        return checkSoftLockMaster(showmsg);

      if (showmsg) raUser.getInstance().showSoftLockWarning(getWindow(), getRaQueryDataSet(), getJpTableView().getKeyColumns());
      return false;
    }
    return checkSoftLockMaster(showmsg);
  }

  public boolean checkSoftLockMaster(boolean showmsg) {
    if (getRaMasterDetail() == null) return true;
    if (getRaMasterDetail().raMaster.equals(this)) return true;
    isLastLockSuccesfull_raDetail = getRaMasterDetail().raMaster.checkSoftLock(showmsg);//gle! zovem private metodu iz klase koja nije this
    return isLastLockSuccesfull_raDetail;
  }

  //da li je uspio zalokat zapis, odnosno da li da unloka pri izlazu ako je u pitanju raDetail
  private boolean isLastLockSuccesfull_raDetail = false;
  /**
   * Unlockira tekuci record (ili njegov master)
   * Zove se IZVAN doWithSave jer, naravno, ne podrzava transakcije
   */
  public void softUnlock() {
    if (getMode()=='I' && isSoftDataLockEnabled()) {
      raUser.getInstance().unlockRow(getRaQueryDataSet(), getJpTableView().getKeyColumns());
    }
    if (getRaMasterDetail()!=null
          && (!getRaMasterDetail().raMaster.equals(this)) //nisam ja master
          && getRaMasterDetail().raMaster.isSoftDataLockEnabled()&&isLastLockSuccesfull_raDetail) {
      raUser.getInstance().unlockRow(getRaMasterDetail().raMaster.getRaQueryDataSet(),
        getRaMasterDetail().raMaster.getJpTableView().getKeyColumns());
    }
  }
  /**
   * Provjerava da li je redak zakljucan, ali ga ne pokusava zakljucati
   * @param showmsg prikazati prigodnu poruku ako je zakljucan
   * @return true ako je red zakljucan, false ako nije
   */
  public boolean isRowLocked(boolean showmsg) {
    boolean mylock = (isSoftDataLockEnabled()
      && raUser.getInstance().isRowLocked(getRaQueryDataSet(), getJpTableView().getKeyColumns()));
    boolean masterlock=false;
    if (getRaMasterDetail()==null) {
      masterlock = false;
    } else if (!getRaMasterDetail().raMaster.equals(this)) {
      masterlock = getRaMasterDetail().raMaster.isRowLocked(showmsg);
    }
    if (mylock && showmsg) {
      raUser.getInstance().showSoftLockWarning(getWindow(), getRaQueryDataSet(), getJpTableView().getKeyColumns());
    }
    return mylock||masterlock;
  }

  public void installSelectionTracker(String column) {    
    jpTableView.installSelectionTracker(column);
  }

  public void uninstallSelectionTracker() {
    jpTableView.uninstallSelectionTracker();
  }

  public Condition getSelectCondition() {
    return jpTableView.getSelectCondition();
  }

  public void removeSelection() {
    jpTableView.removeSelection();
  }

  public raSelectTableModifier getSelectionTracker() {
    return jpTableView.getSelectionTracker();
  }
  public boolean validateSelection(ReadRow rowToSelect) {
    return true;
  }
  public String getClassName() {
    return getClass().getName();
  }
//raHelpAware
  public raNavAction[] getNavActions() {
    return getNavBar().getNavContainer().getNavActions();
  }
  
  public hr.restart.util.columnsbean.ColumnsBean getColumnsBean() {
    return getJpTableView().getColumnsBean();
  }  
  
  //Provjera prava na tablicu
  private boolean canAccessTable(char mode) {
    if (getRaQueryDataSet() == null || getRaQueryDataSet().getQuery() == null) return true;
    String tabName = Valid.getTableName(getRaQueryDataSet().getQuery().getQueryString()).trim();
    if (tabName.indexOf(",")!=-1) {//ako je join uzmi prvu tablicu
      tabName = new StringTokenizer(tabName,",").nextToken().trim();
    }
    String action = 
      (mode == 'N')?"DODAVANJE":
        ((mode == 'I')?"IZMJENU":
          ((mode == 'B')?"BRISANJE":"PREGLED"));
    boolean chk = raUser.getInstance().canAccessTable(tabName, action);
    if (!chk) {
      JOptionPane.showMessageDialog(getWindow(), "Nemate pravo na "+action+"!!", "UPOZORENJE!", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    //ako nema pravo na master nema ni na detail i obrnuto
    if (getRaMasterDetail() != null) {
      if (getRaMasterDetail().raDetail.equals(this)) {
        return getRaMasterDetail().raMaster.canAccessTable(mode);
      }
/*      if (getRaMasterDetail().raMaster.equals(this)) {
        return getRaMasterDetail().raDetail.canAccessTable(mode);
      }*/
    }
    return true;
  }
}