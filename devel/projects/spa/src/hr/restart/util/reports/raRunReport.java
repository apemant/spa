/****license*****************************************************************
**   file: raRunReport.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import hr.restart.sisfun.frmParam;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;

import sg.com.elixir.IRuntimeOptions;
import sg.com.elixir.ReportRuntime;

public class raRunReport {
//hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
//hr.restart.util.TimeTrack TT = new hr.restart.util.TimeTrack(false);
  private int currDsIdx = 0;
  private java.util.Properties reportProps;
//  private LinkedList list = new LinkedList();
  private ArrayList reports = new ArrayList();
  private ArrayList allreports = new ArrayList();
  
//  private LinkedList disabled = new LinkedList();
//  private HashSet providers = new HashSet();
  private raReportDescriptor rd;
  private java.awt.Component owner;
//  private hr.restart.util.Util UT;
  private int defListIdx = 0;
//  private sg.com.elixir.reportwriter.datasource.IDataProvider[] reportProviders;
//  private Object[] reportProviders;
  private java.awt.Frame frameOwner = new java.awt.Frame();
  static ReportRuntime rt;
  static Frame lastFrame;
  private String ownerName, immedName;
  private Dimension immedSize;
  private boolean ownerlock, immedView;
  private TemplateModifier temod;
//  private static raRunReport rr;
  protected raRunReport() {
//    UT = hr.restart.util.Util.getUtil();
  }
  
  public static raRunReport createEmpty() {
    return new raRunReport();
  }

  public static raRunReport getRaRunReport(String providerClassNameC) {
    raRunReport rr = new raRunReport();
    rr.getReportRuntime();
    rr.setProviderClassName(providerClassNameC);
    return rr;
  }

  /**
   * @param owner komponenta.
   * @deprecated koristi setOwner(Component, String) umjesto ove.
   */
  public void setOwner(java.awt.Component owner) {
    this.owner = owner;
  }

  public void setOwner(java.awt.Component owner, String ownerName) {
    if (!ownerlock) {
      this.owner = owner;
      this.ownerName = ownerName;
    }
  }

  public void lockOwner(java.awt.Component owner, String ownerName) {
    setOwner(owner, ownerName);
    ownerlock = true;
  }

  public java.awt.Component getOwner() {
    return owner;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public static raRunReport getRaRunReport() {
//    if (rr==null) {
//      rr = new raRunReport();
//    }
//    rr.getReportRuntime();
    return new raRunReport();
  }

  public ReportRuntime getRT() {
    getReportRuntime();
    return rt;
  }

  private void getReportRuntime() {
//    Frame o
    if (rt == null) {
      System.out.println("rt == null");
//      if (frameOwner==null) setFrameOwner();
      rt = new ReportRuntime();
    }
    rt.setDynamicParameters(getReportProps());
  }

  private java.util.Properties getDefReportProps() {
    reportProps = new java.util.Properties();
    // ne prikazuj elixirov range dialog
    reportProps.put(IRuntimeOptions.SHOW_RANGE_DIALOG,IRuntimeOptions.NO);
    reportProps.put(IRuntimeOptions.EXPORT_TEXT_DELIMITER, frmParam.getParam(
            "zapod", "exportSep", "#", "Separator Elixirovog exporta u txt", true));
    //reportProps.put("render.pdf.api", IRuntimeOptions.YES);
//    reportProps.put(IRuntimeOptions.)
    return reportProps;
  }
  /**
   * <pre>
   * Vraca java.util.Properties klasu u kojoj su vec stavljene default vrijednosti za report i
   * na nju se mogu kalemiti daljnji propertiesi metodom put(Object key, Object value)
   * npr.: raRunReport.getRaRunReport().getReportProps().put(IRuntimeOptions.SHOW_RANGE_DIALOG,IRuntimeOptions.NO)
   * </pre>
   */
  public java.util.Properties getReportProps() {
    if (reportProps == null) return getDefReportProps();
    return reportProps;
  }

  /**
   * Karakteristicno bas za tablereport triper-cliper je GK za ovo
   */
//  void setTableReportTemplate(int colnum) {
//    for (Iterator i = reports.iterator(); i.hasNext();) {
//      rd = (raReportDescriptor) i.next();
//      if (rd.getProvider() instanceof JTablePrintProvider) {
//        if (((JTablePrintProvider) rd.getProvider()).columnsB.getSumRow() != null)
//          rd.setTemplate("hr.restart.util.reports/reports/JTablePrintProviderLineSum.template");
//        else rd.setTemplate("hr.restart.util.reports/reports/JTablePrintProviderLine.template");
//      }
//    }
//  }

  public void setDataSourceIndex(int idx) {
    currDsIdx = idx;
  }

  /**
   * <pre>
   * Postavlja ime klase koja implementira sg.com.elixir.reportwriter.datasource.IDataProvider i,
   * nadam se, generirana je sa JDOPrintCreatorom.
   * Ujedno (ako nisu prije postavljeni) postavlja:
   *  - dataSource na [ime paketa]+"/reports/dsm.sav"
   *  - dataSourceName na JDO+[ime klase]
   *  - reportTemplate na [ime paketa]+"/reports/"+[ime klase]+".template"
   *  - classTemplate na instancirani objekt [ime paketa]+[ime klase]+"Template" ili [ime paketa]+".reports."+[ime klase]+"Template"
   * Automatski setira providerClassNames
   * </pre>
   */
  public void setProviderClassName(String newProviderClassName) {
    rd = raReportDescriptor.create(newProviderClassName);
    if (rd != null) {
      allreports.clear();
      reports.clear();
      allreports.add(rd);
      reports.add(rd);
    }
  }

/**
 * Vraca sve instancirane providere zbogradi mozebitnog naknadnog setiranja
 */
//  public Object[] getReportProviders() {
//    return providers.toArray();
//  }

/**
 * Dodaje postavljenim reportima jos jedan graficki report za Elixir ili textualni za mxReport
 */
  public void addReport(String newProviderClassName, String newReportTitle) {
    addReport(newProviderClassName, newReportTitle, 0);
  }
/**
 * Dodaje postavljenim reportima jos jedan graficki report za Elixir s tim da je zadnji parametar
 * (int datasourceIndex) index koji se dodaje imenu datasourcea npr. ako je datasourceIndex 4
 * datasource je <packagepath>/reports/dsm4.sav, jedino ako je index 0 onda je datasource
 * <packagepath>/reports/dsm.sav
 */
  public void addReport(String newProviderClassName, String newReportTitle,int dsIndex) {
    currDsIdx = dsIndex;
    rd = raReportDescriptor.create(newProviderClassName, newReportTitle, dsIndex);
    if (rd != null) {
      allreports.add(rd);
      reports.add(rd);
    }
  }
  
  public void addJasperHook(String reportName, JasperHook jhook) {
    rd = getReport(reportName);
    if (rd != null) rd.setJasperHook(jhook);
  }

  public void addReport(String id, String source, String design, String title) {
    rd = raReportDescriptor.create(id, source, design, title);
    if (rd != null) {
      allreports.add(rd);
      reports.add(rd);
    }
  }
  
  public void addJasper(String id, String source, String jasper, String title) {
    rd = raReportDescriptor.create(id, source, jasper, title, true);
    if (rd != null) {
      allreports.add(rd);
      reports.add(rd);
    }
  }
  
  public void addReport(raReportDescriptor rd) {
    allreports.add(rd);
    reports.add(rd);
  }
  
  public void addReport(String id, String source, String title) {
    rd = raReportDescriptor.create(id, source, title);
    if (rd != null) {
      allreports.add(rd);
      reports.add(rd);
    }
  }

/**
 * Dodaje postavljenim reportima jos jedan textualni report
 */

  public void addReport(mxReport report) {
    addReport(report.getClass().getName(),report.getTitle());
  }

  public void disableReport(String reportName) {
    rd = getReport(reportName);
    if (rd != null) reports.remove(rd);
  }

  public void enableReport(String reportName) {
    rd = getReport(reportName);
    if (rd != null && !reports.contains(rd)) {
      int removed = allreports.indexOf(rd);
      for (int i = 0; i < reports.size(); i++)
        if (allreports.indexOf(reports.get(i)) > removed) {
          reports.add(i, rd);
          break;
        }
      if (!reports.contains(rd)) reports.add(rd);
    }
  }

  public void enableReport(String reportName, String title) {
    enableReport(reportName);
    setReportTitle(reportName, title);
  }


  public Iterator getReports() {
    return reports.iterator();
  }

  public int getReportsCount() {
    return reports.size();
  }

  public void disableSignature(String reportName) {
    getReport(reportName).disableSignature();
  }

  public raReportDescriptor getReport(String reportName) {
    for (Iterator i = allreports.iterator(); i.hasNext(); ) {
      rd = (raReportDescriptor) i.next();
      if (rd.getName().equals(reportName)) return rd;
    }
    return null;
  }

  public raReportDescriptor getReport(int index) {
    if (index >= 0 && index < getReportsCount())
      return (raReportDescriptor) reports.get(index);
    return null;
  }

  public void setOneTimeDirectReport(String reportName) {
    immedName = reportName;
    immedView = false;
    immedSize = null;
  }
  
  public void setOneTimeDirectReport(String reportName, boolean view) {
    immedName = reportName;
    immedView = view;
    immedSize = null;
  }
  
  public void setOneTimeDirectReport(String reportName, boolean view, Dimension size) {
    immedName = reportName;
    immedView = view;
    immedSize = size;
  }

  public int getDirectReport() {
    if (immedName == null) return -1;
    for (int i = 0; i < getReportsCount(); i++)
      if (((raReportDescriptor) reports.get(i)).getName().equals(immedName))
        return i;
    return -1;
  }
  
  public Dimension getSize() {
    return immedSize;
  }
  
  public boolean isDirectView() {
    return immedView;
  }

  public void clearDirectReport() {
    immedName = null;
    immedSize = null;
  }

  /**
   * Postavlja naslov nekog reporta.<p>
   * @param reportName ime reporta (e.g. hr.restart.robno.repDnevnik)
   * @param title naslov reporta.
   */
  public void setReportTitle(String reportName, String title) {
    rd = getReport(reportName);
    if (rd != null) rd.setTitle(title);
  }

  /**
   * Brise zadani report iz provider class nameova
   */
  public void removeReport(String reportName) {
    rd = getReport(reportName);
    if (rd != null) {
      allreports.remove(rd);
      reports.remove(rd);
    }
  }
/**
 * Brise sve do tada zadane reporte u tom raRunReportu
 */
  public void clearAllReports() {
    allreports.clear();
    reports.clear();
    temod = null;
  }
  
  public void installTemplateModifier(TemplateModifier tm) {
    temod = tm;
  }
  
  TemplateModifier getTemplateModifier() {
    return temod;
  }

  /**
   * Brise sve reporte osim JTableMxReporta i JTablePrintProvidera.
   */
  public void clearAllCustomReports() {
    for (Iterator i = allreports.iterator(); i.hasNext();) {
      rd = (raReportDescriptor) i.next();
      if (!rd.getName().equals("hr.restart.util.reports.repDynamicProvider") &&
          !rd.getName().equals("hr.restart.util.reports.JTableMxReport")) {
        i.remove();
        reports.remove(rd);
      }
    }
    temod = null;
  }
/**
 * Postavlja se koji je ispis defaultno selektiran u listboxu na ekranu za ispis npr. ako su u lisboxu ovi ispisi:
 * <pre>
 *  Grafi\u010Dki ispis tablice  (defaultListIndex = 0)
 *  Matri\u010Dni ispis tablice  (defaultListIndex = 1)
 *  Ispis k...a             (defaultListIndex = 2)
 *  Ispis palca             (defaultListIndex = 3)
 *  Ispis kriminalca        (defaultListIndex = 4)
 * </pre>
 * i postavljeno je setDefaultListIndex(3) u listboxu ce biti selektiran ispis palca
 * @param idxC
 */
  public void setDefaultListIndex(int idxC) {
    defListIdx = idxC;
  }

  public void setFrameOwner(java.awt.Frame newFrameOwner) {
    frameOwner = newFrameOwner;
  }

  public void setFrameOwner(hr.restart.util.raFrame newRaFrameOwner) {
    hr.restart.start.setFRAME_MODE();
    if (hr.restart.start.FRAME_MODE == hr.restart.util.raFrame.FRAME)
      frameOwner = newRaFrameOwner.getJframe();
  }

  public void setFrameOwner() {
    hr.restart.start.setFRAME_MODE();
    if (
        (hr.restart.start.FRAME_MODE == hr.restart.util.raFrame.PANEL) ||
        (hr.restart.start.FRAME_MODE == hr.restart.util.raFrame.INTERNALFRAME)
       )
    {
      frameOwner = hr.restart.mainFrame.getMainFrame();
    }
  }

  public void go() {
    try {
      getReportRuntime();
//      dlgRunReport drr = new dlgRunReport(rt,dataSources,reportTemplates,dataSourceNames,reportProviders,reportTitles);
//      drr.dispose();
//      drr = null;
//      dlgRunReport.showDlgRunReport(rt,dataSources,reportTemplates,dataSourceNames,reportProviders,reportTitles,defListIdx,classTemplates);

      dlgRunReport.showDlgRunReport(this);
      System.gc();
    } catch(Exception ex) {
      System.out.println("raRunReport.go(): Exception: " + ex);
      ex.printStackTrace();
    }
  }
//geteri



  int getDefListIdx() {
    return defListIdx;
  }
}
