/****license*****************************************************************
**   file: raJasperLoader.java
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

import hr.restart.baza.Agenti;
import hr.restart.baza.Condition;
import hr.restart.sisfun.frmTableDataView;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRJdk13Compiler;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRProperties;
import sg.com.elixir.ReportRuntime;
import sg.com.elixir.reportwriter.datasource.DataSourceManager;
import sg.com.elixir.reportwriter.datasource.IDataProvider;
import sg.com.elixir.reportwriter.datasource.IDataSourceInfor;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raJasperLoader {
  private static boolean loaded = false;
  private raJasperLoader() {
  }
  public static void load() {
    if (!loaded) {
      loaded = true;
      new Thread() {
        public void run() {
          System.out.println("loading jasper...");
          dummyReport();
          System.out.println("jasper loaded.");
        }
      }.start();
    }
  }
  
  static void dummyReport() {
    try {
      frmTableDataView dummy = new frmTableDataView();
      dummy.setDataSet(Agenti.getDataModule().getReadonlySet());
      dummy.jp.getStorageDataSet().open();
      dummy.jp.getNavBar().getColBean().setRaJdbTable(dummy.jp.getMpTable());
      
      JTablePrintRun tpr = new JTablePrintRun();
      tpr.setInterTitle("dummy");
      tpr.setColB(dummy.jp.getNavBar().getColBean());
      tpr.setRTitle("dummy");
      
      raRunReport runner = tpr.getReportRunner();
      ReportRuntime rt = runner.getRT();
      
      if (!DataSourceManager.current().userDSNNameExist(
          raReportDescriptor.DYNAMIC_NAME)) {
        System.out.println("nema dynamic providera, dodajem:");
        String dsm = raReportDescriptor.DYNAMIC_DSM;
        dsm = dsm.substring(0,dsm.lastIndexOf("."));
        dsm = dsm.replace('.','/').concat(".sav");
        rt.setReportDataSourceManagerInfo(
            ClassLoader.getSystemResourceAsStream(dsm));
        raElixirDatasource.buildDynamic();
      }
      
      System.out.println("building dummy report...");
      raReportDescriptor rd = runner.getReport(raReportDescriptor.DYNAMIC_CLASS);
      rt.setReportTemplate(((repDynamicProvider) rd.getProvider()).
          createDynamicReport(dummy.jp.getColumnsBean(), "dummy").
          getReportTemplate());
      
      String dsnName = runner.getReport(0).getProviderName();
      DataSourceManager dsm = DataSourceManager.current();
      IDataSourceInfor dsn = dsm.getUserDSN(dsnName);
      IDataProvider provider = runner.getReport(0).isExtended() 
          ? raElixirDataProvider.getInstance() 
          : (IDataProvider) runner.getReport(0).getProvider();

      JasperElixirData data = new JasperElixirData(dsn, provider);
      JasperDesign jdes = JasperBuilder.buildFromElixir(rt.getReportTemplate(), data);
      jdes.setName(rd.getName());
      jdes.setName(jdes.getName().substring(jdes.getName().lastIndexOf('.') + 1));
      JRProperties.setProperty(JRProperties.COMPILER_KEEP_JAVA_FILE, false);
      System.out.println("compiling dummy report...");
      JasperReport jcomp = new JRJdk13Compiler().compileReport(jdes);
      data.removeUnusedGetters();
      System.out.println("sorting and grouping dummy report...");
      data.buildTable(rt.getReportTemplate());
      data.sortTable();
      System.out.println("filling dummy report...");
      JasperFillManager.fillReport(jcomp, null, data);      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
