/****license*****************************************************************
**   file: frmEvidencijaDjel.java
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
/*
 * frmEvidencijaDjel.java
 *
 * Created on 2004. travanj 05, 13:04
 */

package hr.restart.pl;
import hr.restart.baza.dM;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;

import com.borland.dx.dataset.StorageDataSet;
/**
 *
 * @author  andrej
 */
public class frmEvidencijaDjel extends raFrame {
  private raExtendedTable table;
  private raJPTableView jptv;
  private OKpanel okp;
  private hr.restart.util.reports.JTablePrintRun TPRun = new hr.restart.util.reports.JTablePrintRun();
  private raNavAction rnvPrint = new raNavAction("Ispis",raImages.IMGPRINT,java.awt.event.KeyEvent.VK_F5) {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        print_action();
      }
  };
  private static frmEvidencijaDjel fEvidencijaDjel;
  /** Creates a new instance of frmEvidencijaDjel */
  public frmEvidencijaDjel() {
     table = new raExtendedTable();
     table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
     jptv = new raJPTableView(table);
     jptv.setVisibleCols(new int[] {0,1,2,5,30});
     jptv.getNavBar().addOption(rnvPrint);
     jptv.getColumnsBean().setSaveName("EvidencijaDjelatnika");
     getContentPane().add(jptv,BorderLayout.CENTER);
     fEvidencijaDjel = this;
  }
  
  public void pack() {
    super.pack();
    setSize(575, 275);
  }

  public void show() {
    raProcess.runChild(new Runnable() {
      public void run() {
        allset = (StorageDataSet)frmRadnicipl.getInstance().jpDetail.getCustomPanel()
          .getHorizontalSet("radnici", "radnicipl", 
              "radnici.cradnik=radnicipl.cradnik and (radnicipl.corg in "+OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "radnicipl.corg"));
        allset.setRowId("CRADNIK",true);
        allset.setTableName("EviDjel");
        jptv.setDataSet(allset); 
        jptv.getColumnsBean().eventInit();
      }
    });
    super.show();
  }
  private StorageDataSet allset;
  public StorageDataSet getReportSet() {
    return allset;
  }
  public static frmEvidencijaDjel getInstance() {
    return fEvidencijaDjel;
  }
  public void print_action() {
    jptv.getColumnsBean().saveSettings();
    getRepRunner().clearAllCustomReports();
    dM.getDataModule().getAllMjesta().open();
    dM.getDataModule().getAllZpZemlje().open();
    dM.getDataModule().getAllOrgstruktura().open();
    dM.getDataModule().getRadMJ().open();
    dM.getDataModule().getVrodn().open();
    getRepRunner().addReport("hr.restart.pl.repMatKnjigDjel1","hr.restart.pl.repMatKnjigDjel","MatKnjigDjelLeftSide","Matièna knjiga djelatnika - lijeva strana");
    getRepRunner().addReport("hr.restart.pl.repMatKnjigDjel2","hr.restart.pl.repMatKnjigDjel","MatKnjigDjelRightSide","Matièna knjiga djelatnika - desna strana");
    jptv.enableEvents(false);
    getTablePrinter().runIt();
    jptv.enableEvents(true);
  }
  
  public hr.restart.util.reports.raRunReport getRepRunner() {
    return getTablePrinter().getReportRunner();
  }

  private hr.restart.util.reports.JTablePrintRun getTablePrinter() {
    jptv.getColumnsBean().setSumRow(jptv.getSumRow());
    TPRun.setInterTitle(getClass().getName());
    TPRun.setColB(jptv.getColumnsBean());
    TPRun.setRTitle(this.getTitle());
    return TPRun;
  }
}
