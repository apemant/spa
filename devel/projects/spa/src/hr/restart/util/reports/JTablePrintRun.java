/****license*****************************************************************
**   file: JTablePrintRun.java
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
import hr.restart.util.FileHandler;
import hr.restart.util.columnsbean.ColumnsBean;

import java.util.Iterator;

public class JTablePrintRun {
//  private static JTablePrintRun myJTPR;
  private ColumnsBean colB;
  private String rTitle, interTitle;
  hr.restart.util.reports.raRunReport reportRunner = new hr.restart.util.reports.raRunReport();
  repDynamicProvider JTPProvider;
  JTablePrintProvider JTPProviderHidden;

  public JTablePrintRun() {
  }

  public JTablePrintRun(String tit) {
    interTitle = tit;
    FileHandler f;
  }

  public JTablePrintRun(ColumnsBean incolB,String inrTitle) {
    this.setColB(incolB);
    this.setRTitle(inrTitle);
    runIt();
  }

  public JTablePrintRun(ColumnsBean incolB,String inrTitle, String tit) {
    this.setColB(incolB);
    this.setRTitle(inrTitle);
    interTitle = tit;
    runIt();
  }
/*
  public static JTablePrintRun getJTablePrintRun() {
    if (myJTPR == null) {
      myJTPR = new JTablePrintRun();
    }
    return myJTPR;
  }
*/
/**
 * Vraca raRunReport kakav je
 */
  private boolean hasLived=false;
  public hr.restart.util.reports.raRunReport getReportRunner() {
    JTPProvider = getMyProvider();
    if (JTPProvider == null && !hasLived) {
//      reportRunner.addReport("hr.restart.util.reports.JTablePrintProvider","Grafi\u010Dki ispis tablice",0);
      reportRunner.addReport("hr.restart.util.reports.repDynamicProvider","Grafi\u010Dki ispis tablice",42);
      reportRunner.addReport("hr.restart.util.reports.JTableMxReport","Matri\u010Dni ispis tablice");//matricni
      JTPProvider = getMyProvider();
      JTPProviderHidden = new JTablePrintProvider();
    }
    if (JTPProviderHidden != null) {
//      JTPProvider.createDynamicReport(colB, rTitle);
      JTPProviderHidden.setRepTitle(rTitle);
      JTPProviderHidden.setColumnsB(colB);
//      reportRunner.setTableReportTemplate(colB.getRaJdbTable().getColumnCount());
      try {
        getMyMxReport().setDataObject(JTPProviderHidden);
      } catch (Exception e) {}
    }
    hasLived=true;
    return reportRunner;
  }
/**
 * Za slijedeci getReportRunner(), nakon ove metode, vratit ce standardni reportRunner sa prijavljenim ispisima tablica
 */
  public void resetReportRunner() {
    hasLived=false;
  }
/**
 * Vraca postavljeni raRunReport za ispis tablice
 */
  public hr.restart.util.reports.raRunReport getReportRunner(hr.restart.util.columnsbean.ColumnsBean cb,String titl) {
//    hasLived=false;
    setRTitle(titl);
    setColB(cb);
    return getReportRunner();
  }

  public void runIt() {

    reportRunner = getReportRunner(colB,rTitle);
    if (JTPProvider != null) {
      reportRunner.getReport("hr.restart.util.reports.repDynamicProvider").
          setJavaTemplate(JTPProvider.createDynamicReport(colB, rTitle));
      JTPProvider.activate();
    }
//    if (mxRTable.getDataObject()==null) mxRTable.setDataObject(JTPProvider);
    reportRunner.go();
  }

  private repDynamicProvider getMyProvider() {
    for (Iterator i = reportRunner.getReports(); i.hasNext(); ) {
      raReportDescriptor rd = (raReportDescriptor) i.next();
//      if (rd.getProvider() instanceof hr.restart.util.reports.JTablePrintProvider)
//        return (JTablePrintProvider) rd.getProvider();
      if (rd.getProvider() instanceof hr.restart.util.reports.repDynamicProvider)
        return (repDynamicProvider) rd.getProvider();
    }
    return null;
  }
  private JTableMxReport getMyMxReport() {
    for (Iterator i = reportRunner.getReports(); i.hasNext(); ) {
      raReportDescriptor rd = (raReportDescriptor) i.next();
      if (rd.getProvider() instanceof hr.restart.util.reports.JTableMxReport)
        return (JTableMxReport) rd.getProvider();
    }
    return null;
  }

  public void setRTitle(String newrTitle) {
    rTitle = newrTitle;
  }

  public void setColB(ColumnsBean newcolB) {
    colB = newcolB;
    reportRunner.setOwner(colB, interTitle);
  }
  public void setInterTitle(String interTitle) {
    this.interTitle = interTitle;
  }
}