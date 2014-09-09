/****license*****************************************************************
**   file: dlgRunReport.java
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
package hr.restart.util.reports;

import hr.restart.baza.Condition;
import hr.restart.baza.Logodat;
import hr.restart.baza.dM;
import hr.restart.baza.kreator;
import hr.restart.robno.TypeDoc;
import hr.restart.robno.repIzlazni;
import hr.restart.robno.repUtil;
import hr.restart.robno.reportsQuerysCollector;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRJdk13Compiler;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.engine.xml.JasperDesignFactory;
import net.sf.jasperreports.view.JRViewer;
import sg.com.elixir.ReportRuntime;
import sg.com.elixir.reportwriter.datasource.DataSourceManager;
import sg.com.elixir.reportwriter.datasource.IDataProvider;
import sg.com.elixir.reportwriter.datasource.IDataSourceInfor;
import sg.com.elixir.reportwriter.rt.ISession;
import sg.com.elixir.reportwriter.xml.IModel;
import sg.com.elixir.reportwriter.xml.ModelFactory;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class dlgRunReport {
//sysoutTEST ST = new sysoutTEST(false);
  boolean singleMode=true, busy;
  JDialog dlg;
  ReportRuntime rt;
  private mxReport mxR;
//  private raReportDescriptor rd;
//  private String[] dataSources;
//  private String[] reportTemplates;
//  private String[] dataSourceNames;
//  private Object[] reportProviders;
//  private String[] reportTitles;
  private int defaultIdx = 0;
  private static dlgRunReport dlgRR;
  
  String expAP = "";
//  private java.util.Hashtable classTemplates;
  raRunReport runner;
  JPanel jp = new JPanel(new BorderLayout());
  raJPNavContainer rjpnc = new raJPNavContainer();
  prViewDialog prw; // = new prViewDialog(this); //(this)
  prExportDialog pre; // = new prExportDialog(this); //(this)
//  JComboBox rnCombo = new JComboBox();
  JList lrep = new JList();
  ArrayList customSections = new ArrayList();
//  DataSet currHeader, currFooter, currPod;
  Properties defaultIndexes = new Properties();
  JraCheckBox logocheck = new JraCheckBox();
  JPanel lpan = new JPanel(new BorderLayout());

  raCommonClass rCC = raCommonClass.getraCommonClass();
  raNavAction rnvPrint = new raNavAction("Ispis",raImages.IMGPRINT,KeyEvent.VK_F10,true) {
    public void actionPerformed(ActionEvent e) {
      printReport();
    }
  };
  raNavAction rnvPreview = new raNavAction("Pregled",raImages.IMGPREVIEW,KeyEvent.VK_F2,true) {
    public void actionPerformed(ActionEvent e) {
      previewReport();
    }
  };
  raNavAction rnvExport = new raNavAction("Snimi",raImages.IMGEXPORT,KeyEvent.VK_F6,true) {
    public void actionPerformed(ActionEvent e) {
      exportReport();
    }
  };
  raNavAction rnvMail = new raNavAction("e-Mail",raImages.IMGCOMPOSEMAIL,KeyEvent.VK_F7,true) {
    public void actionPerformed(ActionEvent e) {
      mailReport();
    }
  };
  raNavAction rnvExit = new raNavAction("Izlaz",raImages.IMGEXIT,KeyEvent.VK_ESCAPE,true) {
    public void actionPerformed(ActionEvent e) {
      exitReport();
    }
  };
  protected dlgRunReport() {
//    super((Frame)null, "Ispis", true);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * @return Returns the dlg.
   */
  public JDialog getDlg() {
      return dlg;
  }
  
  public static void showDlgRunReport(raRunReport runner) {
    initAndShowDlgRunReport(runner);
  }
  /**
   * @return ekran na kojem biras reporte
   */
  public static dlgRunReport getCurrentDlgRunReport() {
    return dlgRR;
  }
  /**
   * @return Title od odabranog reporta na comboboxu na ekranu
   */
  public String getCurrentReportTitle() {
    return getCurrentDescriptor().getTitle();

//    return dlgRR.reportTitles[getSelectedReportIndex()];
  }
  /**
   * @return Index od odabranog reporta na ekranu
   */
  public int getSelectedReportIndex() {
    return getCurrentComboValue();
  }

  public raReportDescriptor getCurrentDescriptor() {
    return runner.getReport(getSelectedReportIndex());
  }

  public raReportDescriptor getDescriptor(int index) {
    return runner.getReport(index);
  }

  private void createDialogs() {
    Component owner = runner.getOwner();
    if (owner instanceof JComponent)
      owner = ((JComponent) owner).getTopLevelAncestor();

    if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, "Ispis", true);
    else if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, "Ispis", true);
    else dlg = new JraDialog((Frame) null, "Ispis", true);

    prw = new prViewDialog(dlg); //(this)
    pre = new prExportDialog(dlg); //(this)
    raKeyActionSupport kysp = new raKeyActionSupport(dlg);
    kysp.setNavContainer(rjpnc);
    FileHandler.loadProperties("report.properties", defaultIndexes);

    addComboIfNeed();
    checkLogoCheck();
    dlg.getContentPane().add(jp);
    packDialog();
    if (dlg.getOwner() != null)
      dlg.setLocationRelativeTo(dlg.getOwner());


    rjpnc.registerNavKeys(dlg);
//    dlg.addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        rjpnc.unregisterNavKeys(dlg);
//        dlg.removeAll();
//        dlg.dispose();
//        System.out.println("disposed");
//      }
//      public void windowClosed(WindowEvent e) {
//
//        rjpnc.unregisterNavKeys(dlg);
//        dlg.removeAll();
//        dlg.dispose();
//        System.out.println("disposed");
  //        dlg.remove(jp);
//      }
//    });
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (!busy) exitReport();
      }
    });
  }

  private static void initAndShowDlgRunReport(raRunReport runner) {
    if (dlgRR == null) dlgRR = new dlgRunReport();
    try {
      dlgRR.runner = runner;
      dlgRR.rt = runner.rt;
      dlgRR.chkParams();
      dlgRR.defaultIdx = runner.getDefListIdx();
//      dlgRR.jbInit();
      dlgRR.createDialogs();
      dlgRR.enableDlg();
      dlgRR.expAP = dlgRR.expAlterPrint();
      dlgRR.updateLogocheck();
      dlgRR.customSections.clear();
      if (runner.getDirectReport() == -1)
        dlgRR.dlg.show();
      else if (runner.isDirectView())
        dlgRR.previewReport();
      else dlgRR.printReport();
    }
    catch(Exception ex) {
      ex.printStackTrace();

    }
  }

  private void disposeDialogs() {
    if (dlg != null) {
      FileHandler.storeProperties("report.properties", defaultIndexes);
      rjpnc.unregisterNavKeys(dlg);
      dlg.dispose();
      dlg = null;
      pre.dispose();
      pre = null;
      prw.dispose();
      prw = null;
    }
  }

  void jbInit() throws Exception {
    rjpnc.addOption(rnvPrint);
    rjpnc.addOption(rnvPreview);
    rjpnc.addOption(rnvExport);
    rjpnc.addOption(rnvMail);
    rjpnc.addOption(rnvExit);
    logocheck.setFocusPainted(false);
    logocheck.setText(" Logotip ");
    logocheck.setHorizontalTextPosition(SwingConstants.LEADING);
    lpan.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
    lpan.add(logocheck, BorderLayout.CENTER);
    lpan.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
    jp.add(rjpnc,BorderLayout.CENTER);
    jp.add(lpan, BorderLayout.EAST);
    lrep.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (!busy && e.getClickCount() == 2) {
          System.out.println("doubleclick "+lrep.getSelectedValue());
        }
      }
    });
    lrep.setBorder(BorderFactory.createLoweredBevelBorder());
    lrep.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    rjpnc.setBorder(BorderFactory.createEtchedBorder());
    lrep.setCellRenderer(new DefaultListCellRenderer() {
      private boolean selected;
      public Component getListCellRendererComponent(JList l, Object v, int idx, boolean sel, boolean focus) {
        return super.getListCellRendererComponent(l, v, idx, selected = sel, false);
      }
      public void setEnabled(boolean enabled) {
        super.setEnabled(enabled || selected);
      }
    });
    
    logocheck.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (logocheck.isEnabled())
          getCurrentDescriptor().setPrintLogo(e.getStateChange() == ItemEvent.SELECTED);
      }
    });
    
    lrep.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) updateLogocheck();
      }
    });
//    rjpnc.registerNavKeys(this);
//  addComboIfNeed();
//    getContentPane().add(jp);
  }
/*  void addComboIfNeed() {
    if (!singleMode) {
//      rnCombo = new JComboBox(reportTitles);
      rnCombo.removeAllItems();
      for (Iterator i = runner.getReports(); i.hasNext();)
        rnCombo.addItem(((raReportDescriptor) i.next()).getTitle());
      jp.add(rnCombo,BorderLayout.NORTH);
      rjpnc.registerNavKeys(rnCombo);
      if (defaultIdx > -1 && defaultIdx < rnCombo.getItemCount()) rnCombo.setSelectedIndex(defaultIdx);
    } else {
      jp.remove(rnCombo);
      rjpnc.unregisterNavKeys(rnCombo);
    }
  } */

  void addComboIfNeed() {
    rjpnc.unregisterNavKeys(lrep);
    jp.remove(lrep);
    if (!singleMode) {
      lrep.setModel(new AbstractListModel() {
        public int getSize() {
          return runner.getReportsCount();
        }
        public Object getElementAt(int idx) {
          return runner.getReport(idx).getTitle();
        }
      });
//      lrep.getCellRenderer().getListCellRendererComponent()

//      for (Iterator i = runner.getReports(); i.hasNext();)
//        lrep..((raReportDescriptor) i.next()).getTitle());
      jp.add(lrep,BorderLayout.NORTH);
      rjpnc.registerNavKeys(lrep);
      String def;

      if (runner.getOwnerName() != null &&
          (def = defaultIndexes.getProperty(runner.getOwnerName())) != null) {
        if (Aus.isNumber(def)) {
          int idx = Integer.parseInt(def);
          if (idx >=0 && idx < lrep.getModel().getSize()) lrep.setSelectedIndex(idx);
          else lrep.setSelectedIndex(0);
        } else {
          for (int i = 0; i < runner.getReportsCount(); i++)
            if (runner.getReport(i).getName().equalsIgnoreCase(def)) {
              lrep.setSelectedIndex(i);
              return;
            }
          lrep.setSelectedIndex(0);
        }
      } else if (defaultIdx >= 0 && defaultIdx < lrep.getModel().getSize())
        lrep.setSelectedIndex(defaultIdx);
//      lrep.getCellRenderer().getListCellRendererComponent()
    }
  }
  
  void checkLogoCheck() {
    boolean need = false;
    for (int i = 0; i < runner.getReportsCount(); i++)
      if (runner.getReport(i).isCustomIzlaz())
        need = true;
    
    if (need && !jp.isAncestorOf(lpan)) jp.add(lpan, BorderLayout.EAST);
    if (!need && jp.isAncestorOf(lpan)) jp.remove(lpan);
  }

  void chkParams() throws java.lang.Exception {

    if (runner.getReportsCount() == 1) singleMode = true;
    else singleMode = false;
  }

  public void packDialog() {
    dlg.pack();
    Dimension screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = dlg.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    int mins = jp.isAncestorOf(lpan) ? 400 : 350;
    if (frameSize.width < mins) frameSize.setSize(mins,frameSize.height);
    dlg.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    dlg.setSize(frameSize.width+100,frameSize.height);
  }

  private String convert2path(String str,String ext) {
    String nwStr = str.substring(0,str.lastIndexOf("."));
    nwStr = nwStr.replace('.','/');
    nwStr = nwStr.concat(ext);
    return nwStr;
  }
  public int getCurrentComboValue() {
    if (runner.getDirectReport() >= 0) return runner.getDirectReport();
    return (singleMode ? 0 : lrep.getSelectedIndex());
  }
  private boolean isElixir() {
    return (getCurrentDescriptor().getProvider() instanceof sg.com.elixir.reportwriter.datasource.IDataProvider) || 
      getCurrentDescriptor().isJasper();
  }

  private DataSet getCurrentDesign(String corg, String vrsta, String vrdok, String vrsec) {
    for (int i = 0; i < customSections.size(); i++) {
      DataSet design = (DataSet) customSections.get(i);
      if (design.getString("CORG").equals(corg) &&
          design.getString("VRSTA").equals(vrsta) &&
          design.getString("VRSEC").equals(vrsec) &&
          design.getString("VRDOK").equals(vrdok)) return design;
    }
    return null;
  }

  private DataSet getDesign(String corg, String vrsta, String vrdok, String vrsec) {
    DataSet design = getCurrentDesign(corg, vrsta, vrdok, vrsec);
    if (design == null) {
      design = Logodat.getDataModule().getTempSet(
          Condition.equal("CORG", corg)
          .and(Condition.equal("VRSTA", vrsta))
          .and(Condition.equal("VRSEC", vrsec))
          .and(Condition.equal("VRDOK", vrdok)));
      design.open();
      design.setSort(new SortDescriptor(new String[] {"RBR"}));
      customSections.add(design);
    }
    return design;
  }

/*  private DataSet getDesign(DataSet ds) {
    return getDesign(ds.getString("CORG"), ds.getString("VRSTA"),
                     ds.getString("VRSEC"), ds.getString("VRDOK"));
  } */

  private String findReportCorg(Object provider) {
    DataSet ds = null;
    String corg = null;
    try {
      Class src = provider.getClass();
      while (ds == null && src != Object.class) {
        Field[] f = src.getDeclaredFields();
        for (int i = 0; i < f.length; i++)
          if (DataSet.class.isAssignableFrom(f[i].getType()) && f[i].getName().equals("ds")) {
            f[i].setAccessible(true);
            ds = (DataSet) f[i].get(provider);
          }
        src = src.getSuperclass();
      }
      boolean financ = ds.hasColumn("VRDOK") != null &&
      (!TypeDoc.getTypeDoc().isCsklSklad(ds.getString("VRDOK")) || ds.getString("VRDOK").equalsIgnoreCase("RNL"));
      
      
      if (ds.hasColumn("VRDOK") != null) System.out.println(ds.getString("VRDOK"));
      System.out.println(financ);
      if (ds.hasColumn("VRDOK") != null && ds.getString("VRDOK").equalsIgnoreCase("PON")){
    	  if (ds.hasColumn("PARAM")!= null) {
    		  if (ds.getString("PARAM").equalsIgnoreCase("OJ")) 
    			  financ = true;
//    		  System.out.println(ds.getString("PARAM"));
    	  } else {
    		  System.out.println("Ne postoji PARAM u ds-u");
    	  }
      }
      
      if (ds.hasColumn("VRDOK") != null && ds.getString("VRDOK").equalsIgnoreCase("PRD")){
        if (ds.hasColumn("PARAM")!= null) {
            if (ds.getString("PARAM").equalsIgnoreCase("K")) 
                financ = false;
//          System.out.println(ds.getString("PARAM"));
        } else {
            System.out.println("Ne postoji PARAM u ds-u");
        }
    }
      
      if (ds.hasColumn("CSKL") != null && ds.getString("CSKL").length() > 0 && (financ ||
         lookupData.getlookupData().raLocate(dM.getDataModule().getSklad(), "CSKL",
            ds.getString("CSKL"))))
        corg = financ ? ds.getString("CSKL") : dM.getDataModule().getSklad().getString("CORG");
      else if (ds.hasColumn("CORG") != null && ds.getString("CORG").length() > 0)
        corg = ds.getString("CORG");
    } catch (Exception e) {
//      e.printStackTrace();
    }
    return (corg != null && corg.length() > 0) ? corg : hr.restart.zapod.OrgStr.getKNJCORG(false);
  }

  private void modifySectionIfNeeded(String corg, String vrsta,
                                     String vrdok, String vrsec, Class src) {
    DataSet ds;
    String model;
    do {
      if (raCustomSection.anythingDefinedFor(corg, vrsta, vrdok, vrsec)) {
        prw.addCustom(ds = getDesign(corg, vrsta, vrdok, vrsec),
                      raReportDescriptor.getSectionName(vrsec),
                      model = raReportDescriptor.getElixirModel(vrsec), src);
//        new sysoutTEST(false).prn(ds);
        raCustomSection.modify(rt.getReportTemplate().getModel(model), ds, src, true);
        if (vrsta.equals("C"))
          raCustomSection.ensureVisibility(rt.getReportTemplate(), model);
        return;
      }
      if (!lookupData.getlookupData().raLocate(dM.getDataModule().getOrgstruktura(), "CORG", corg)) break;
      if (dM.getDataModule().getOrgstruktura().getString("PRIPADNOST").equals(corg)) break;
      corg = dM.getDataModule().getOrgstruktura().getString("PRIPADNOST");
    } while (true);
    if (vrsta.equals("C")) {
      prw.addCustom(ds = getDesign(corg, vrsta, vrdok, vrsec),
                    raReportDescriptor.getSectionName(vrsec),
                    model = raReportDescriptor.getElixirModel(vrsec), src);
      ds.insertRow(false);
      ds.setString("CORG", corg);
      ds.setString("VRSTA", vrsta);
      ds.setString("VRSEC", vrsec);
      ds.setString("VRDOK", vrdok);
      ds.setShort("RBR", (short) 0);
      ds.setInt("SIRINA", 0);
      ds.setInt("VISINA", 0);
      ds.setInt("VPOS", 0);
      ds.post();
      raCustomSection.modify(rt.getReportTemplate().getModel(model), ds, src, true);
      raCustomSection.ensureVisibility(rt.getReportTemplate(), model);
    }
  }

  private void setReportTemplate(raReportDescriptor rd) {
    if (rd.isJasper()) return;
       
    if (rd.isJavaTemplate()) rt.setReportTemplate(rd.getJavaTemplate().getReportTemplate());
    else {
      rt.setReportTemplate(getSystemResourceAsStream(
        convert2path(rd.getTemplate(), ".template")));
      if (runner.getTemplateModifier() != null)
        runner.getTemplateModifier().modify(rd.getName(), rt.getReportTemplate());

      raCustomSection.globalChangeFont(rt.getReportTemplate());
    }
    // custom report section: header i footer izlaznih ili internih ispisa,
    // ili neki drugi promjenjivi dijelovi izvjestaja
    System.out.println(rd.isCustomIzlaz());
    System.out.println(rd.getCustomSect());
    if (rd.isCustomIzlaz() || rd.getCustomSect() != null) {
      if (rd.isJavaTemplate()) {
        rd.getJavaTemplate().recreateReportStructure();
//        rd.getJavaTemplate().setReportProperties();
        rt.setReportTemplate(rd.getJavaTemplate().getReportTemplate());
//        rd.getJavaTemplate().getReportTemplate();
      } else ModelFactory.setCurrentReport(rt.getReportTemplate());

      dM.getDataModule().getLogodat().refresh();

      // nadji corg ovog ispisa, reflectionom iz dataseta print providera
      String corg = findReportCorg(!rd.isExtended() ? rd.getProvider() :
        raElixirDataProvider.getInstance().getReportData());
      System.out.println(corg);
      
   // dohvati klasu data source-a radi gettera koji se mogu ubacivati
      // u custom sectione po principu public String getNAME() =>  $NAME
      Class dsource;
      try {
        dsource = rd.isExtended() ? Class.forName(rd.getDataSource()) : rd.getProvider().getClass();
      } catch (Exception e) {
        dsource = null;
      }

      // modificiraj report header i footer ako treba
      if (rd.isCustomIzlaz()) {
        if (rd.isPrintLogo()) {
          if (rd.isForeignIzlaz()) {
            modifySectionIfNeeded(corg, "H", "I", "PH2", null);
            modifySectionIfNeeded(corg, "F", "I", "PF2", null);
          } else {
            modifySectionIfNeeded(corg, "H", "I", "PH", null);
            modifySectionIfNeeded(corg, "F", "I", "PF", null);
          }
        }
        if (frmParam.getParam("robno", "singleSF", "N", "Podnožje izlaznih dokumenata " +
        		"kao jedna cjelina (D/N)").equalsIgnoreCase("D")) {
          raCustomSection.moveSection(rt.getReportTemplate().getModel(raElixirProperties.SECTION_FOOTER + 1),
              rt.getReportTemplate().getModel(raElixirProperties.SECTION_FOOTER + 0));
        }
      }

      // modificiraj ostale dijelove iz liste promjenjivih ako ih uopce ima
      if (rd.getCustomSect() != null)
        for (Iterator i = rd.getCustomSect().iterator(); i.hasNext(); )
          modifySectionIfNeeded(corg, "C", rd.getCustomVrdok(), (String) i.next(), dsource);
      else modifySectionIfNeeded(corg, "C", rd.getCustomVrdok(), "SF0", dsource);
      
      if (frmParam.getParam("robno", "fiskFooter", "D", 
          "Podaci o fiskalnom raèunu u page footer (D,N)").equalsIgnoreCase("D"))
        raCustomSection.addFisk(dsource, rt.getReportTemplate().getModel(raElixirProperties.PAGE_FOOTER), false);
      else raCustomSection.addFisk(dsource, rt.getReportTemplate().getModel(raElixirProperties.SECTION_FOOTER + 1), true);
      
    } else ModelFactory.setCurrentReport(rt.getReportTemplate());
    if (!rd.isDisabledSignature())
      raCustomSection.ensureSignature(rt.getReportTemplate().getModel(raElixirProperties.PAGE_FOOTER));
    if (rd.isCustomIzlaz() && "D".equalsIgnoreCase(frmParam.getParam("robno", "ispZag",
        "N", "Ispis zaglavlja na svim stranicama izlaznih dokumenata (D/N)")))
      try {
        rt.getReportTemplate().getModel(raElixirProperties.SECTION_HEADER + 0).
          setPropertyValue(raElixirProperties.REPEAT, raElixirPropertyValues.YES);
      } catch (Exception e) {}
  }

  InputStream getSystemResourceAsStream(String string) {
    return dlgRunReport.class.getClassLoader().getResourceAsStream(string);
  }

  private int ensureRange(int val, int def) {
    return val <= 0 || val > 255 ? def : val;
  }

  private void adjustGrayShades(IModel template) {
    String g = frmParam.getParam("zapod", "gray", "210",
      "Intenzitet sive podloge na ispisu, od 0 (vrlo tamna) do 255 (bijela)", true);
    String lg = frmParam.getParam("zapod", "lightGray", "230",
      "Intenzitet svijetlosive podloge na ispisu, od 0 (crna) do 255 (bijela)", true);
    String dg = frmParam.getParam("zapod", "darkGray", "190",
      "Intenzitet tamnosive podloge na ispisu, od 0 (crna) do 255 (bijela)", true);
    raCustomSection.replaceGray(template, ensureRange(Aus.getNumber(g), 210));
    raCustomSection.replaceLightGray(template, ensureRange(Aus.getNumber(lg), 230));
    raCustomSection.replaceDarkGray(template, ensureRange(Aus.getNumber(dg), 190));
  }

  void updateLogocheck() {
    if (!jp.isAncestorOf(lpan) || getCurrentDescriptor() == null) return;
    logocheck.setEnabled(getCurrentDescriptor().isCustomIzlaz());
    logocheck.setSelected(getCurrentDescriptor().isPrintLogo());
  }

  private void runElixir(final int mode) {
    final int idx = getCurrentComboValue();
    raReportDescriptor rd = getDescriptor(idx);
    try {
//      IDataProvider elixProvider = (IDataProvider) rd.getProvider();
//      rt.setReportDataSourceManagerInfo(ClassLoader.getSystemResourceAsStream(convert2path(rd.getDataSource(),".sav")));
      if (!DataSourceManager.current().userDSNNameExist(raReportDescriptor.DYNAMIC_NAME)) {
        System.out.println("nema dynamic providera, dodajem:");
        rt.setReportDataSourceManagerInfo(getSystemResourceAsStream(
            convert2path(raReportDescriptor.DYNAMIC_DSM, ".sav")));
        raElixirDatasource.buildDynamic();
//        rt.setReportDataSourceManagerInfo(ClassLoader.getSystemResourceAsStream(
//            convert2path(raReportDescriptor.DYNAMIC_DSM,".sav")));
//        rt.addDataProvider("JDOrepDynamicProvider", repDynamicProvider.getInstance());
      }
//      System.out.println(VarStr.join(DataSourceManager.current().getSystemDSNNames(), ','));
//      System.out.println(VarStr.join(DataSourceManager.current().getUserDSNNames(), ','));
//      if (!rd.isExtended())
      if (!rd.isJasper()) raElixirDatasource.build(rd);

//      DataSourceManager.current().add
      System.out.println(VarStr.join(DataSourceManager.current().getSystemDSNNames(), ','));
//      System.out.println(VarStr.join(DataSourceManager.current().getUserDSNNames(), ','));
      System.out.println(VarStr.join(rt.getReportSourceNames(), ','));

//      IDataSourceInfor io = DataSourceManager.current().getSystemDSN("Object Data Source").cloneDataSource();
//      IDataSourceInfor i = io.cloneDataSource();
//      i.getModel().removeAllModels();
//      IModel clone = io.getModel().getModel(0).cloneModel();
//      i.getModel().addModel(clone.getPropertyValue("Name"), clone);
//      System.out.println("idataprovider:");
//      Aus.dumpClassName(io.getDataProvider());
//      Aus.dumpClassName(io);
//      System.out.println("idatasource:");
//      Aus.dumpClassName(io.getIDataSource());
//      System.out.println("imodel");
//      Aus.dumpClassName(io.getModel());
//      Aus.dumpModel(io.getModel(),0);
//      System.out.println("description "+io.getDescription());
//      System.out.println("name "+io.getName());
//
//      rt.addDataProvider("Malipokus", new hr.restart.robno.repCjenik());
//      da = rt.getReportDataSourceAccess("Malipokus");
//      System.out.println(da.getName());
//      System.out.println(da.getType());
//      System.out.println(da.getDescription());

//      Aus.dumpModel(da.getModel(), 2);
//      System.out.println("copy:");
//      Aus.dumpClassName(i);
//      System.out.println("idatasource:");
//      Aus.dumpClassName(i.getIDataSource());
//      System.out.println("imodel");
//      Aus.dumpModel(i.getModel(),0);
//      System.out.println("description "+i.getDescription());
//      System.out.println("name "+i.getName());

      setReportTemplate(rd); //tu je iznad metoda
      
      if (!rd.isJasper()) {
        rt.getReportTemplate().setPropertyValue(raElixirProperties.RECORD_SOURCE, rd.getProviderName());
        adjustGrayShades(rt.getReportTemplate());
      }
//      Aus.dumpModel(rt.getReportTemplate(), 0);

//      rt.addDataProvider(rd.getProviderName(), elixProvider);
//      System.out.println(VarStr.join(DataSourceManager.current().getSystemDSNNames(), ','));
//      System.out.println(VarStr.join(DataSourceManager.current().getUserDSNNames(), ','));
      abstractElixirRunner elRunner = new elixirRunnerClientClient();
      elRunner.setMode(mode);
      elRunner.setIdx(idx);
      elRunner.start();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  void enableDlg() {
    busy = false;
    if (dlg!=null) rCC.EnabDisabAll(dlg.getContentPane(),true);
  }
  void disableDlg() {
    busy = true;
    rCC.EnabDisabAll(dlg.getContentPane(),false);
  }
  private void printRtState(int idx) {    
    System.out.println("Current index = "+idx);
    for (int i = 0; i < runner.getReportsCount(); i++)
      System.out.println(runner.getReport(i));
  }
  private void runMx(int mode) {
    int idx = getCurrentComboValue();
    raReportDescriptor rd = getDescriptor(idx);
    mxR = (hr.restart.util.reports.mxReport) rd.getProvider();
    mxR.makeReport();
    if (mode == 0) {
      mxR.print();
      exitReport();
    } else if (mode == 1) {
      prw.jpw.removeAll();
      prw.jpw.add(mxViewer.getViever(),BorderLayout.CENTER);
      prw.pack();
      prw.show();
    } else if (mode == 2) {
//      pre.showSaveDialog();
      if (pre.showSaveDialog(false) == JFileChooser.CANCEL_OPTION) return;
      try {
        String ext = pre.getFileType();
        if (ext.equals("txt")) {
          java.io.File wFile = pre.getSelectedFile();
          wFile.delete();
          java.io.File rFile = new java.io.File(mxReport.TMPPRINTFILE);
          //rFile.renameTo(wFile);
          FileHandler.copy(rFile, wFile);
        } else {
          showNoWayMessage();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (mode == 3) {
      StorageDataSet ret = ReportMailDialog.showMailDialog(null, null, null);
      if (ret != null)
      try {
        mxR.renameFile();
        ReportMailDialog.sendMail(new File(mxR.getFilename()), ret);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Slanje neuspješno!!! Greška :"+e);
        e.printStackTrace();
      }
    }
//    enableDlg();
  }
  private void showNoWayMessage() {
    JOptionPane.showMessageDialog(null,
      "Tekstualni ispis nije mogu\u0107e snimiti u zadani format!",
      "Poruka",
      JOptionPane.INFORMATION_MESSAGE);
  }
  
  private void rememberLast() {
    if (runner.getOwnerName() != null && runner.getReportsCount() > 1)
      defaultIndexes.setProperty(runner.getOwnerName(), getCurrentDescriptor().getName());
  }
  
  private void runReport(int mode) {
    disableDlg();
//    System.out.println(defaultIndexes);
    
    prw.removeDesign();
    if (isElixir()) {
      runElixir(mode);
    } else {
      runMx(mode);
      enableDlg();
      if (runner.getDirectReport() >= 0) {
        runner.clearDirectReport();
        exitReport();
      }
    }
  }
  public void printReport() {
    try {
      rememberLast();
      if (getCurrentDescriptor().getReportType() == raReportDescriptor.TYPE_CHART)
        ((IReport) getCurrentDescriptor().getProvider()).print();
      else runReport(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void previewReport() {
    try {
      rememberLast();
      if (getCurrentDescriptor().getReportType() == raReportDescriptor.TYPE_CHART)
        ((IReport) getCurrentDescriptor().getProvider()).preview();
      else runReport(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void exportReport() {
    try {
      rememberLast();
      if (getCurrentDescriptor().getReportType() == raReportDescriptor.TYPE_CHART)
        ((IReport) getCurrentDescriptor().getProvider()).export();
      else runReport(2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void mailReport() {
    try {
      rememberLast();
//      if (getCurrentDescriptor().getReportType() == raReportDescriptor.TYPE_CHART)
//        ((IReport) getCurrentDescriptor().getProvider()).export();
//      else 
      if (getCurrentDescriptor().getReportType() != raReportDescriptor.TYPE_CHART)
        runReport(3);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  void exitReport() {
//    if (runner != null) runner.rt = null;
    rt = null;
    disposeDialogs();
  }
//  public void hide() {
//    prw.dispose();
//    pre.dispose();
//    super.hide();
//  }

  class prViewDialog extends JraDialog {
//    JPanel design = new JPanel(null);
    Component glue = Box.createHorizontalGlue();
    Box design = Box.createHorizontalBox();
//    DataSet currHeader, currFooter;
    HashMap currCustom = new HashMap();
//    JraButton header = new JraButton();
//    JraButton footer = new JraButton();
    HashMap custom = new HashMap();
    HashMap customObj = new HashMap();
//    JraButton pod = new JraButton();
    ISession is;
    boolean printCalled = false;
    boolean alterPrintCalled = false;
//    Runnable refresher = new Runnable() {
//      public void run() {
//        refresh();
//      }
//    };
    JasperPrint cjp = null;
    JButton jprb = null;
    JPanel jpw = new JPanel(new BorderLayout());
    private OKpanel okpw = new OKpanel() {
      public void jBOK_actionPerformed() {
        printw();
      }
      public void jPrekid_actionPerformed() {
        closew();
      }
    };

    public ActionListener actionLis = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (jprb != null && cjp != null) {
          try {
            JasperPrintManager.printReport(cjp, true);
          } catch (JRException e1) {
            e1.printStackTrace();
          }
        }
        printCalled = true;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            closew();
          }
        });
      }
    };
    public prViewDialog() {
      super((Frame) null, "Pregled ispisa", true);
      prVInit();
    }
    
    public prViewDialog(JDialog dialog) {
      super(dialog, "Pregled ispisa", true);
      prVInit();
    }
//    public void addHeader(DataSet ds) {
//      currHeader = ds;
//      design.remove(glue);
//      design.add(header);
//      design.add(glue);
//    }
    public void addCustom(DataSet ds, String name, String sect, Class src) {
      JraButton jb = new JraButton();
      jb.setText(name);
      currCustom.put(name, ds);
      custom.put(name, sect);
      if (src != null)
        customObj.put(name, src);
      jb.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String name = ((JraButton) e.getSource()).getText();
          DataSet ds = (DataSet) currCustom.get(name);
          raSectionDesigner.setKey(ds.getString("CORG"), ds.getString("VRSTA"),
                                   ds.getString("VRSEC"), ds.getString("VRDOK"));
          if (raSectionDesigner.show(prViewDialog.this, name, ds, true)) {
            if (raSectionDesigner.isSaveRequested()) {
              ds.first();
              QueryDataSet saver = Logodat.getDataModule().getFilteredDataSet(
                  Condition.whereAllEqual(new String[] {"CORG","VRSTA","VRSEC","VRDOK",}, ds));
              saver.open();
              saver.deleteAllRows();
              if (ds.rowCount() > 1)
                for (ds.first(); ds.inBounds(); ds.next()) {
                  saver.insertRow(false);
                  ds.copyTo(saver);
                }
              try {
                saver.saveChanges();
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
            raCustomSection.modify(rt.getReportTemplate().getModel((String) custom.get(name)),
                                   ds, (Class) customObj.get(name), false);
            refresh();
          }
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              okpw.jBOK.requestFocus();
            }
          });
        }
      });
      design.remove(glue);
      design.add(jb);
      design.add(glue);
    }
//    public void addFooter(DataSet ds) {
//      currFooter = ds;
//      design.remove(glue);
//      design.add(footer);
//      design.add(glue);
//    }
    public void dispose() {
      AWTKeyboard.unregisterComponent(this);
      super.dispose();
    }
    public void removeDesign() {
//      currHeader = currFooter = null;
      currCustom.clear();
      custom.clear();
      customObj.clear();
      design.removeAll();
    }
    void prVInit() {
//      setTitle("Pregled ispisa");
//      setModal(true);
//      design.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//      header.setText("Header");
//      header.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          if (raSectionDesigner.show(prViewDialog.this, "Header", null, currHeader)) {
//            raCustomSection.modify(rt.getReportTemplate().getModel(raElixirProperties.PAGE_HEADER), currHeader);
//            refresh();
//          }
//        }
//      });
//      footer.setText("Footer");
//      footer.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          if (raSectionDesigner.show(prViewDialog.this, "Footer", null, currFooter)) {
//            raCustomSection.modify(rt.getReportTemplate().getModel(raElixirProperties.PAGE_FOOTER), currFooter);
//            refresh();
//          }
//        }
//      });
//      pod.setText("Podnožje");
//      pod.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//          if (raSectionDesigner.show(prViewDialog.this, "Podnožje", null, currPod)) refresh();
//        }
//      });
      okpw.add(design, BorderLayout.WEST);
      okpw.change_jBOK("Ispis",raImages.IMGPRINT);
//      int h = okpw.jBOK.getPreferredSize().height;
//      header.setPreferredSize(new Dimension(header.getPreferredSize().width, h));
//      footer.setPreferredSize(new Dimension(footer.getPreferredSize().width, h));
//      pod.setPreferredSize(new Dimension(pod.getPreferredSize().width, h));
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(okpw,BorderLayout.SOUTH);
      getContentPane().add(jpw,BorderLayout.CENTER);
      /*this.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          this_keyPressed(e);
        }
        public void keyTyped(KeyEvent e) {
          this_keyTyped(e);
        }
      });
      this.addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent e) {
          okpw.jBOK.requestFocus();
        }
      });*/
      okpw.registerOKPanelKeys(this);
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          closew();
        }
      });
    }
    
    private void disablePrintIfAlter() {
      if (frmParam.getParam("sisfun", "alterdisprw", "N", "Onemoguciti klasican ispis (s pregleda) ako je alternativni definiran",true).equalsIgnoreCase("D")) {
        if (!expAP.equals("")) {
          //okpw.jBOK.setEnabled(false);
          JButton pb = findPrintButton(jpw);
          if (pb!=null) pb.setEnabled(false);
        }
      }  
    }

    void closew() {
//      if (isElixir() && designer != null && designer.isShowing()) {
//        designer.hide(false);
//      }
      this.hide();
    }
    void printw() {
      printCalled = true;
      if (isElixir()) {
//        rt.print();
        if (expAP.equals("")) {
          if (jprb == null) simulatePrint(jpw);
          else pressButton(jprb);
        } else {
          printCalled = false;
          alterPrintCalled = true;
          closew();
        }
      } else {
        mxR.print();
        closew();
      }
    }
    /*void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode()==e.VK_F10) {
        printw();
      }
      if (e.getKeyCode()==e.VK_ESCAPE) {
        closew();
      }
    }
    void this_keyTyped(KeyEvent e) {
      if (e.getKeyChar()==e.VK_ESCAPE) {
        closew();
      }
    }*/
    private Runnable refresher = new Runnable() {
      public void run() {
        if (is != null) {
          is.close();
          
        }
        raReportDescriptor rd = getCurrentDescriptor();
        if (rd.isExtended())
          raElixirDataProvider.getInstance().setDataClass(rd.getDataSource());
//        getContentPane().setEnabled(false);
//        getContentPane().setEnabled(false);
        getContentPane().remove(jpw);
//        jpw.setEnabled(true);
        jpw.removeAll();
        //setupProgressMonitor();     
        //is = rt.previewReport(jpw);
        cjp = getJasperPrint(jpw);
        is = previewTemplate(jpw, cjp);
        //rt.setProgressListener(null);
        //PrintManager.current().showRangeDialog(false);
        getContentPane().add(jpw, BorderLayout.CENTER);
//        pack();
//        getContentPane().repaint();
        if (isShowing()) show();
      }
    };
    void refresh() {
      new Thread(refresher).start();
    }
    public void show(ISession is) {
      this.is = is;
      printCalled = false;

      if (is != null) {
        JButton prb = findPrintButton(jpw);
        prb.removeActionListener(actionLis);
        prb.addActionListener(actionLis);
      }
//      designer = (raSectionDesigner) startFrame.getStartFrame().
//               showFrame("hr.restart.util.reports.raSectionDesigner", 0, "", false);
//      designer.runOnExit(refresher);
      disablePrintIfAlter();
      super.show();
    }
    public void pack() {
      super.pack();
//      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension screenSize = hr.restart.start.getSCREENSIZE();
      Dimension frameSize = getSize();
      if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
      if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;

      setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      setSize(frameSize.width,frameSize.height);
    }
  }

  class prExportDialog extends JraDialog {
    private int selectedOption;
    String fname;
    exportFileFilter xls, txt, jgf, pdf, htm;
    
    JFileChooser fc = new JFileChooser() {
      public void approveSelection() {
        selectedOption = JFileChooser.APPROVE_OPTION;
        hideDialog();
      }
      public void cancelSelection() {
        selectedOption = JFileChooser.CANCEL_OPTION;
        hideDialog();
      }
    };
    public prExportDialog() {
      prInit();
    }
    public prExportDialog(JDialog owner) {
      super(owner);
      prInit();
//      super(ClassLoader.getSystemClassLoader().getSystemResource("").getPath());
    }
    void prInit() {
      setModal(true);
      fc.setDialogType(fc.SAVE_DIALOG);
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(fc,BorderLayout.CENTER);
      jgf = new exportFileFilter("JGF","Java graphics format (JGF)");
      pdf = new exportFileFilter("PDF","Acrobat reader format (PDF)");
      xls = new exportFileFilter("XLS","Excel document format (XLS)");
      htm = new exportFileFilter("HTML,HTM","Hypertext document (HTML,HTM)");
      txt = new exportFileFilter("TXT","Text file (TXT)");
    }
    void hideDialog() {
      this.hide();
    }
    int showSaveDialog(boolean graphic) {
      selectedOption = JFileChooser.CANCEL_OPTION;
      fc.resetChoosableFileFilters();
      if (!graphic) {
        fc.addChoosableFileFilter(txt);
        fc.setFileFilter(txt);
      } else {
        fc.addChoosableFileFilter(pdf);
        fc.addChoosableFileFilter(htm);
        if (getCurrentDescriptor().getName().equals(raReportDescriptor.DYNAMIC_CLASS)) {
          fc.addChoosableFileFilter(xls);
          fc.setFileFilter(xls);
        } else fc.setFileFilter(pdf);
      }
      startFrame.getStartFrame().centerFrame(this,0,"Snimi u file");
      this.show();
      if (selectedOption == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
        fname = ((exportFileFilter) fc.getFileFilter()).normalize(fc.getSelectedFile().getPath());
      } else fname = null;
      return selectedOption;
    }
    
    String getFilename() {
      return fname;
    }
    
    java.io.File getSelectedFile() {
      return fc.getSelectedFile();
    }
    
    java.io.File getSelectedPath() {
      return fc.getCurrentDirectory();
    }
    
    String getFileType() {
      return ((exportFileFilter) fc.getFileFilter()).getExtension();
    }
  }

  class exportFileFilter extends FileFilter {
    private String desc;
    private String[] ext;
    public exportFileFilter(String extC,String descC) {
      desc = descC;
      ext = new VarStr(extC.toLowerCase()).splitTrimmed(',');
    }

    public String getDescription() {
      return desc;
    }
    
    public String getExtension() {
      return ext[0];
    }

    public String normalize(String fname) {
      String fn = fname;
      int dot = fn.lastIndexOf(".");
      if (dot < 0) return fname + "." + ext[0];
      
      fn = fn.substring(dot + 1).toLowerCase();
      for (int i = 0; i < ext.length; i++)
        if (fn.equals(ext[i])) return fname;
      
      return fname + "." + ext[0]; 
    }

    public boolean accept(java.io.File f) {
      if (f.isDirectory()) return true;
      String fn = f.getPath();
      int dot = fn.lastIndexOf(".");
      if (dot < 0) return false;
      fn = fn.substring(dot + 1).toLowerCase();
      for (int i = 0; i < ext.length; i++)
        if (fn.equals(ext[i])) return true;
      return false;
    }
  }

  class abstractElixirRunner extends Thread {
    int mode = 0;
    int idx = 0;
    public void setMode(int m) {
      mode = m;
    }
    public void setIdx(int i) {
      idx = i;
    }
  }

  public void simulatePrint(JPanel j) {    
    JButton prb = findPrintButton(j);
//    PrintManager.current().showRangeDialog(false);
    pressButton(prb);
  }
  
  public void pressButton(JButton prb) {
    if (prb != null) {
      prb.requestFocus();
      prb.getModel().setArmed(true);
      prb.getModel().setPressed(true);
      prb.getModel().setPressed(false);
      prb.getModel().setArmed(false);
    }
  }

  private String expAlterPrint() {
    final String exp = frmParam.getParam("sisfun", "altprintexp","", "Ispis odmah exportirati u PDF|XLS|JGF|XML",true).toUpperCase().trim();
    return exp;
  }
  
  private boolean alterPreview(String exp, String expfn, Object sessionobj) {
    if ((sessionobj instanceof JasperPrint) && exp.equals("JAS")) {
      try {
        JRSaver.saveObject(
            new JasperRemotePreview((JasperPrint)sessionobj),
            expfn+"."+exp.toLowerCase());
        sendAlterFile(expfn, exp);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } 
    return false;
  }

  private boolean alterExport(String exp, String expfn, Object sessionobj) {
    if ((sessionobj instanceof JasperPrint) && exp.equals("JAS")) {
      try {
        JRSaver.saveObject(
            new JasperRemoteExport((JasperPrint)sessionobj),
            expfn+"."+exp.toLowerCase());
        sendAlterFile(expfn, exp);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } 
    return false;
  }

  private void alterPrint(final String exp, final Object sessionobj) {
    Runnable apt = new Runnable() {
      public void run() {
        ISession i = null;
        JasperPrint j = null;
        if (sessionobj instanceof ISession) {
          i = (ISession) sessionobj;
        }
        if (sessionobj instanceof JasperPrint) {
          j = (JasperPrint) sessionobj;
        }
        boolean expengaged = false;
        String expfn = "ispis";
        expengaged = false;
        try {
          if (exp.equals("XLS")) {
            if (getCurrentDescriptor().getName().equals(raReportDescriptor.DYNAMIC_CLASS)) {
             repDynamicProvider.getInstance().xt.exportToXLS(new File(expfn+"."+exp.toLowerCase()));
             expengaged = true;
            }
          } else if (exp.equals("JAS") || exp.equals("JASD")) {
            if (j == null) throw new RuntimeException("Export to JAS file is possible only with Jasper report");
            try {
              Object o2s = null;
              if (exp.equals("JASD")) {
                o2s = j;
              } else {
                o2s = new JasperRemotePrint(j);
              }
              JRSaver.saveObject(o2s, expfn+"."+exp.toLowerCase());
              expengaged = true;
            } catch (Exception e) {
              expengaged = false;
            }
          } else if (!exp.equals("")) {
            String mep = "saveAs";
            Method me = rt.getClass().getMethod(mep+exp,new Class[] {String.class});
            Object succ = me.invoke(rt,new Object[] {expfn+"."+exp.toLowerCase()});
            expengaged = ((Boolean)succ).booleanValue();
          }
          if (i!=null) i.close();
          if (expengaged) {
            sendAlterFile(expfn, exp);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }      
      }
    };
    //raProcess.runChild(apt);
    apt.run();
    //new Thread(apt).start();
  }
  
  private void sendAlterFile(String expfn, String exp) throws IOException {
    String comm = frmParam.getParam("sisfun","altprintcomm","","OS komanda za ispis exportiranog reporta (#=file)",true);
    String _f = new File(expfn+"."+exp.toLowerCase()).getAbsolutePath();
    if (mxReport.bshEvalPrintCommand(comm, _f)) return;
    String oscomm = new VarStr(comm).replaceAll("#",_f).toString();
    Runtime.getRuntime().exec(oscomm);
  }
  
  public JButton findPrintButton(JPanel cont) {
    int numb = 0;
    JButton butt = null, rb;
    for (int i = 0; i < cont.getComponentCount(); i++) {
      Component c = cont.getComponent(i);
      if (c instanceof JButton) {
        ++numb;
        butt = (JButton) c;
      } else if (c instanceof JPanel) {
        rb = findPrintButton((JPanel) c);
        if (rb != null) return rb;
      }
    }
    if (numb == 1) return butt;
    else return null;
  }
  
  /*private String convertValue(String desc) {
    if (desc == null) return "";
    if (desc.indexOf("page") > 0)
      return "Ispis stranice".concat(desc.substring(desc.lastIndexOf(' ')));
    return "Priprema ispisa ...";
  }

  ProgressMonitor pm = null;
  javax.swing.Timer st = null;
  int myprog;
  void setupProgressMonitor() {
    pm = new ProgressMonitor(dlg, null, "Priprema ispisa...", 0, 100);
    UIManager.put("ProgressMonitor.progressText", "Ispis u tijeku");
    try {
      java.lang.reflect.Field f = pm.getClass().getDeclaredField("cancelOption");
      f.setAccessible(true);
      f.set(pm, new Object[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    pm.setMillisToDecideToPopup(250);
    pm.setMillisToPopup(750);
    rt.setProgressListener(new IProgressListener() {
      public void endProgress() {
      }
      public void progress(int val, String desc) {
        pm.setNote(convertValue(desc));
        if (val <= 1) val = myprog;
        else if (st != null) {
          st.stop();
          st = null;
        }
        pm.setProgress(val);
      }
      public void initProgress() {
      }
    });
    st = new javax.swing.Timer(150, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (st != null) {
          pm.setProgress(++myprog);
          if (myprog > 40) st.setDelay(1000);
          else if (myprog > 30) st.setDelay(500);
          else if (myprog > 20) st.setDelay(300);
          else if (myprog > 10) st.setDelay(200);
        }
      }
    });
    myprog = 0;
    st.start();
  }
*/
  
  JasperPrint getJasperPrint(Container parent) {
    JasperPrint jprint = null;
    raProcess.runChild(parent, new Runnable() {
      public void run() {
        try {
          raProcess.setMessage("Priprema ispisa...", false);
          raReportDescriptor rd = getCurrentDescriptor();          
          if (rd.isJasper()) {
            JasperElixirData data = new JasperElixirData(rd.getDataSource());
            JasperDesign jdes = JRXmlLoader.load(
                getSystemResourceAsStream(rd.getTemplate()));
            jdes.setName(getCurrentDescriptor().getName());
            jdes.setName(jdes.getName().substring(jdes.getName().lastIndexOf('.') + 1));
            ElixirToJasperConverter.adjustReport(jdes);
            rd.adjustJasperDesign(jdes);
            fixMargins(rd.getName(), jdes);
            raProcess.setMessage("Prevoðenje izraza...", false);
            JRProperties.setProperty(JRProperties.COMPILER_KEEP_JAVA_FILE, false);
            JasperReport jcomp = new JRJdk13Compiler().compileReport(jdes);
            if (!data.isJasperSource()) {
              raProcess.setMessage("Punjenje podataka...", false);
              data.buildTable();
            }
            raProcess.setMessage("Formiranje ispisa...", false);
            raProcess.yield(JasperFillManager.fillReport(jcomp, null, data.getSource()));
          } else {
            String dsnName = getCurrentDescriptor().getProviderName();
            DataSourceManager dsm = DataSourceManager.current();
            IDataSourceInfor dsn = dsm.getUserDSN(dsnName);
            IDataProvider provider = getCurrentDescriptor().isExtended() 
                ? raElixirDataProvider.getInstance() 
                : (IDataProvider) getCurrentDescriptor().getProvider();
            
            JasperElixirData data = new JasperElixirData(dsn, provider);
            JasperDesign jdes = JasperBuilder.buildFromElixir(rt.getReportTemplate(), data);
            jdes.setName(getCurrentDescriptor().getName());
            jdes.setName(jdes.getName().substring(jdes.getName().lastIndexOf('.') + 1));
            JRXmlWriter.writeReport(jdes, "design.jrxml", "UTF-8");
            rd.adjustJasperDesign(jdes);
            fixMargins(rd.getName(), jdes);
            raProcess.setMessage("Prevoðenje izraza...", false);
            JRProperties.setProperty(JRProperties.COMPILER_KEEP_JAVA_FILE, false);
            JasperReport jcomp = new JRJdk13Compiler().compileReport(jdes);
            //JasperReport jcomp = new JasperCompiler().compileReport(jdes);
  //          JasperReport jcomp = JasperCompileManager.compileReport(jdes);
            //JRFillTextField
            data.removeUnusedGetters();
            raProcess.setMessage("Punjenje podataka...", false);
            data.buildTable(rt.getReportTemplate());
            raProcess.setMessage("Grupiranje i sortiranje...", false);
            data.sortTable();
            raProcess.setMessage("Formiranje ispisa...", false);
            raProcess.yield(JasperFillManager.fillReport(jcomp, null, data));
          }
        } catch (RuntimeException e) {
          throw e;
        } catch (Exception e) {
          e.printStackTrace();
          raProcess.fail();
        }
      }
    });
    if (!raProcess.isCompleted()) return null; 
    return (JasperPrint) raProcess.getReturnValue();
  }
  
  void fixMargins(String name, JasperDesign jdes) {
    boolean vir = name.startsWith("hr.restart.zapod.repVirman");
    Properties margins = new Properties();
    FileHandler.loadProperties("margins.properties", margins);
    String margs = margins.getProperty(name);
    if (margs == null) {
      margs = (vir?18:jdes.getTopMargin()) + "|" + jdes.getLeftMargin() + "|"
            + jdes.getBottomMargin() + "|" + jdes.getRightMargin();
      margins.setProperty(name, margs);
      FileHandler.storeProperties("margins.properties", margins);
    } else {
      String[] ma = new VarStr(margs).split('|');
      if (ma.length == 4) {
        if (!vir) {
          jdes.setTopMargin(Aus.getNumber(ma[0]));
          jdes.setLeftMargin(Aus.getNumber(ma[1]));
          jdes.setBottomMargin(Aus.getNumber(ma[2]));
          jdes.setRightMargin(Aus.getNumber(ma[3]));
        }
      }
    }
    if (vir) {//za virmane se ne postavljaju margine nego se sve komponente pomicu prema dolje za topMargin i desno za leftMargin, ostale margine ne igraju ulogu.
      int tm, lm;
      try {
        tm = (margs==null)?18:Aus.getNumber(new VarStr(margs).split('|')[0]);
        tm = (tm==0)?18:tm;//default
        lm = (margs==null)?0:Aus.getNumber(new VarStr(margs).split('|')[1]);
      } catch (Exception e) {
        tm = 18;
        lm = 0;
        e.printStackTrace();
      }
      JRElement[] flds = jdes.getDetail().getElements();
      for (int i = 0; i < flds.length; i++) {
        if (flds[i] instanceof JRDesignTextField) {
          JRDesignTextField tf = (JRDesignTextField)flds[i];
          tf.setY(tf.getY()+tm);
          tf.setX(tf.getX()+lm);
        }
      }
    }
  }

  ISession previewTemplate(JPanel panel, JasperPrint jprint) {
    prw.jprb = null;
    if (raProcess.isInterrupted()) return null;
    if (jprint != null) {
      JRViewer jv = new JRViewer(jprint) {
        public void setFitWidthZoomRatio() {
          EventListener[] ll = btnPrint.getListeners(ActionListener.class);
          for (int i = 0; i < ll.length; i++)
            btnPrint.removeActionListener((ActionListener) ll[i]);
//          btnPrint.removeActionListener(prw.actionLis);
          btnPrint.addActionListener(prw.actionLis);
          prw.jprb = btnPrint;
          btnFitWidth.setSelected(true);
          super.setFitWidthZoomRatio();
        }
      };
      if (dlgRR.runner.getSize() != null) 
        jv.setPreferredSize(dlgRR.runner.getSize());
      else {
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        jv.setPreferredSize(new Dimension(scr.height - 60, scr.height - 60));
      }
      jv.setFitWidthZoomRatio();
      panel.add(jv);
      return null;
    }
    return rt.previewReport(panel);
  }
  
  class elixirRunnerClientClient extends abstractElixirRunner {
    public void run() {
      JDialog thd = dlg;
      if (mode == 0)
        try {
          JPanel j;
//          rt.print();
          //setupProgressMonitor();
          //rt.setProgressListener(null);
          //PrintManager.current().showRangeDialog(false);
          String exp = expAP;
          JasperPrint jprint = null;
          try {
            jprint = getJasperPrint(dlg);
          } catch (Exception e){
            e.printStackTrace();
          }
          if (exp.equals("")) {
            /*ISession i = rt.previewReport(j = new JPanel());
            simulatePrint(j);
            i.close();*/
            if (jprint != null) JasperPrintManager.printReport(jprint, true);
            else {
              ISession i = rt.previewReport(j = new JPanel());
              simulatePrint(j);
              i.close();
            }
          } else {
            alterPrint(exp, jprint);
          }
        }
        catch (Exception ex) {
          printRtState(idx);
        }
      else if (mode == 1) {
        prw.jpw.removeAll();
        //setupProgressMonitor();
        //ISession is = rt.previewReport(prw.jpw);
        prw.cjp = getJasperPrint(prw.jpw);
     if (!alterPreview(expAP,"ispis",prw.cjp)) {
        ISession is = previewTemplate(prw.jpw, prw.cjp);
        //rt.setProgressListener(null);
        //PrintManager.current().showRangeDialog(false);
        prw.pack();
        prw.show(is);
        if (prw.is!=null) {
          prw.is.close();
          if (prw.printCalled) mode = 0;
        } else {
         // printRtState(idx);
        }
        if (!expAP.equals("") && prw.alterPrintCalled) {
          prw.alterPrintCalled = false;
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              printReport();
            }
          });
        }        
} //endif alterPreview
      } else if (mode == 2) {
        JasperPrint jpr = null;
        raReportDescriptor rd = getCurrentDescriptor();
        reportsQuerysCollector rqc = reportsQuerysCollector.getRQCModule();
        boolean spec = rd.isCustomIzlaz() && rqc.caller != null &&
            rqc.caller.raMaster.getSelectionTracker().countSelected() > 1;
            
            if (expAP != null && expAP.length() > 0 && rd.getName().equals(raReportDescriptor.DYNAMIC_CLASS)) {
              try {
                repDynamicProvider.getInstance().xt.exportToXLS(new File("ispis.xls"));
                sendAlterFile("ispis", "xls");
              } catch (Exception e) {
                e.printStackTrace();
              }
              
            } else if (!expAP.equals("JAS") || !alterExport(expAP,"ispis",jpr = getJasperPrint(dlg))) {
              
        if (spec) {
          if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(dlg, "Spremiti svaki dokument posebno?", "Višestruki dokumenti", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) spec = false;
          else {
            kreator.SelectPathDialog spd = new kreator.SelectPathDialog(
                dlg, "Putanja za spremanje raèuna");
            String dir = IntParam.getTag("export.file.dir");
            spd.loadsave = (dir == null || dir.length() == 0) ? null : new File(dir);
            spd.show();
            if (spd.oksel) {
              Timestamp now = new Timestamp(System.currentTimeMillis());
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              String pref = sdf.format(now);
              
              IntParam.setTag("export.file.dir", spd.loadsave.getAbsolutePath());
              raSelectTableModifier sel = rqc.caller.raMaster.getSelectionTracker();
              DataSet ds = sel.getSelectedView(rqc.caller.getMasterSet());
              try {
                String[] cc = {"CSKL", "GOD", "BRDOK"};
                for (ds.first(); ds.inBounds(); ds.next()) {
                  rqc.ReSql(" AND " + Condition.whereAllEqual(cc, ds).qualified("doki"), ds.getString("VRDOK"));
                  if (ds.isNull("CPAR") || ds.getInt("CPAR") <= 0) continue;
                  try {
                    if (!rd.isJasper()) raElixirDatasource.build(rd);
                    jpr = getJasperPrint(dlg);
                    File dfile = new File(spd.loadsave, pref + "_" + 
                        ds.getInt("CPAR") + "_" + ds.getString("VRDOK") + "-" + 
                        ds.getString("CSKL") + "-" + ds.getString("GOD") + "-" + 
                        Aus.leadzero(ds.getInt("BRDOK"), 6));
                    if (jpr != null) JasperExportManager.exportReportToPdfFile(jpr, dfile.getAbsolutePath());
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              } finally {
                sel.destroySelectedView();
              }
            }
          }
          
        }
        if (!spec && pre.showSaveDialog(true) != JFileChooser.CANCEL_OPTION) {
          try {
            String ext = pre.getFileType();
            System.out.println(getCurrentDescriptor().getName());
            if (ext.equals("pdf")) {
              if (!expAP.equals("JAS")) jpr = getJasperPrint(dlg);
              if (jpr == null) rt.saveAsPDF(pre.getFilename()); 
              else JasperExportManager.exportReportToPdfFile(jpr, pre.getFilename());
            } else if (ext.equals("htm") || ext.equals("html")) {
              if (!expAP.equals("JAS")) jpr = getJasperPrint(dlg);
              if (jpr == null) {
                JOptionPane.showMessageDialog(dlg, "Nije moguæ izvoz izvještaja u HTML formatu!", 
                    "Greška", JOptionPane.ERROR_MESSAGE);
              } else JasperExportManager.exportReportToHtmlFile(jpr, pre.getFilename());
            } else if (ext.equals("xls") &&
                getCurrentDescriptor().getName().equals(raReportDescriptor.DYNAMIC_CLASS))
              repDynamicProvider.getInstance().xt.exportToXLS(pre.getSelectedFile());
            else JOptionPane.showMessageDialog(dlg, "Pogrešan tip datoteke za izvoz!", 
                "Greška", JOptionPane.ERROR_MESSAGE);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
}//endif alterExport        
      } else if (mode == 3) {
        raReportDescriptor rd = getCurrentDescriptor();
        reportsQuerysCollector rqc = reportsQuerysCollector.getRQCModule();
        boolean spec = rd.isCustomIzlaz() && rqc.caller != null &&
            rqc.caller.raMaster.getSelectionTracker().countSelected() > 1;
        StorageDataSet ret = null;
        if (spec) ret = ReportMailDialog.showMailDialog(false);
        else {
          String kome = null;
          String naslov = null;
          if (rd.isCustomIzlaz() && rqc.caller != null) {
            if (lookupData.getlookupData().raLocate(
                dM.getDataModule().getPartneri(), "CPAR", 
                Integer.toString(rqc.getQueryDataSet().getInt("CPAR"))))
              kome = dM.getDataModule().getPartneri().getString("EMADR");
            if (kome != null && kome.length() < 10) kome = null;
            naslov = TypeDoc.getTypeDoc().nazivDokumenta(
                rqc.getQueryDataSet().getString("VRDOK")) + 
                " br. " + repUtil.getFormatBroj(rqc.getQueryDataSet());
          }
          ret = ReportMailDialog.showMailDialog(kome, naslov, null);
        }
        if (ret != null) {
          if (spec) {
            int sent = 0;
            raSelectTableModifier sel = rqc.caller.raMaster.getSelectionTracker();
            DataSet ds = sel.getSelectedView(rqc.caller.getMasterSet());
            int total = ds.rowCount();
            try {
              String[] cc = {"CSKL", "GOD", "BRDOK"};
              DataSet par = dM.getDataModule().getPartneri();
              for (ds.first(); ds.inBounds(); ds.next()) {
                rqc.ReSql(" AND " + Condition.whereAllEqual(cc, ds).qualified("doki"), ds.getString("VRDOK"));
                if (!lookupData.getlookupData().raLocate(par, "CPAR", 
                    Integer.toString(rqc.getQueryDataSet().getInt("CPAR"))) ||
                    par.getString("EMADR").length() < 10) continue;
                try {
                  ret.setString("EMADR", par.getString("EMADR"));
                  ret.setString("NASLOV", TypeDoc.getTypeDoc().nazivDokumenta(
                  rqc.getQueryDataSet().getString("VRDOK")) + 
                    " br. " + repUtil.getFormatBroj(rqc.getQueryDataSet()));
                  
                  Timestamp now = new Timestamp(System.currentTimeMillis());
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
                  String time_suffix = sdf.format(now);
                  if (!rd.isJasper()) raElixirDatasource.build(rd);
                  JasperPrint jpr = getJasperPrint(dlg);
                  String fn = "SPA-ERP_ispis"+time_suffix+".pdf";
                  if (jpr == null) rt.saveAsPDF(fn); 
                  else JasperExportManager.exportReportToPdfFile(jpr, fn);
                  ReportMailDialog.sendMail(new File(fn), ret);
                  
                  ++sent;
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            } finally {
              sel.destroySelectedView();
            }
            //JOptionPane.showMessageDialog(dlg, "Poslano " + sent + " od " + total + " mailova.");
          } else try {
              Timestamp now = new Timestamp(System.currentTimeMillis());
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
              String time_suffix = sdf.format(now);
              JasperPrint jpr = getJasperPrint(dlg);
              String fn = "SPA-ERP_ispis"+time_suffix+".pdf";
              if (jpr == null) rt.saveAsPDF(fn); 
              else JasperExportManager.exportReportToPdfFile(jpr, fn);
              ReportMailDialog.sendMail(new File(fn), ret);
            } catch (Exception e) {
              JOptionPane.showMessageDialog(null, "Slanje neuspješno!!! Greška :"+e);
              e.printStackTrace();
            }
          System.out.println(ret);
        }
        //JOptionPane.showMessageDialog(null, "Slanje reporta e-mailom nije moguæe");
        
      }
//      if (rt != null) {
//        rt.setReportTemplate(sg.com.elixir.reportwriter.xml.ModelFactory.getModel("Label"));
//        String dataSource = "hr.restart.util.reports/reports/dsm42.sav";
//        rt.setReportDataSourceManagerInfo(ClassLoader.getSystemResourceAsStream(convert2path(dataSource,".sav")));
//      }
//            rt.removeDataProvider(dataSourceNames[idx]);
//            System.gc();
//      System.gc();
//      System.gc();
//      System.runFinalization();
//      System.gc();
//      System.gc();
//      System.runFinalization();
//      System.gc();
//      System.gc();
      System.out.println(Runtime.getRuntime().totalMemory());
      System.out.println(Runtime.getRuntime().freeMemory());
      if (thd == dlg)
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            enableDlg();
            if (runner.getDirectReport() >= 0) {
              runner.clearDirectReport();
              exitReport();
            }
            if (mode == 0)
              exitReport();
          }
        });

    }
  }

}