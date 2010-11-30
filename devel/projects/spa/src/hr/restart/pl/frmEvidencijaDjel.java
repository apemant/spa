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
import hr.restart.baza.Condition;
import hr.restart.baza.PlZnacRad;
import hr.restart.baza.PlZnacRadData;
import hr.restart.baza.Radnici;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.sql.Timestamp;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
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
              "radnici.cradnik=radnicipl.cradnik and (radnicipl.corg in "+OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "radnicipl.corg")+")");
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
  public static StorageDataSet getZR4Set(String cradnik) {
//    String cznacs = "15,20,30,52,54,60,70,74,120,122,123,124,125,126,127,128,129,190,250,210,270,280";
    String cznacs = "126,280,74,380,135,410,285,127,330,72,134,320,300,390,120,310,133,70,20,271,125,400,136,54,340,131,210,122,129,52,370,123,250,128,30,124,360,270,132,350,190,121,60,15";
    QueryDataSet znac = PlZnacRad.getDataModule().getFilteredDataSet("CZNAC in ("+cznacs+") ORDER by SRT");
    znac.open();
    QueryDataSet znacdat = PlZnacRadData.getDataModule().getFilteredDataSet(Condition.equal("CRADNIK", cradnik));
    znacdat.open();
    QueryDataSet radnik = Radnici.getDataModule().getFilteredDataSet(Condition.equal("CRADNIK", cradnik));
    radnik.open();
    radnik.first();
    QueryDataSet radnikpl = Radnicipl.getDataModule().getFilteredDataSet(Condition.equal("CRADNIK", cradnik));
    radnikpl.open();
    radnikpl.first();
    
    StorageDataSet zr4set = new StorageDataSet();
    Column[] cols = new Column[] {znac.getColumn("CZNAC").cloneColumn(), znac.getColumn("ZNACOPIS").cloneColumn(), dM.createStringColumn("VRI","Vrijednost", 300), znac.getColumn("SRT").cloneColumn()};
    zr4set.setColumns(cols);
    zr4set.open();
    
    addZR4(zr4set, 1, "Matièni broj radnika u poduzeæu", radnik.getString("CRADNIK"));
    addZR4(zr4set, 2, "Ime", radnik.getString("IME"));
    addZR4(zr4set, 3, "Ime oca", radnik.getString("IMEOCA"));
    addZR4(zr4set, 4, "Prezime", radnik.getString("PREZIME"));
    addZR4(zr4set, 21, "OIB", radnikpl.getString("OIB"));
    addZR4(zr4set, 22, "JMBG", radnikpl.getString("JMBG"));
    addZR4(zr4set, 71, "Steèena školska ili struèna sprema", radnikpl.getString("CSS"));
    addZR4(zr4set, 75, "Dan poèetka rada", formatTS(radnikpl.getTimestamp("DATDOL")));
    addZR4(zr4set, 209, "Dan prestanka radnog odnosa", formatTS(radnikpl.getTimestamp("DATODL")));
    
    
    for (znac.first(); znac.inBounds(); znac.next()) {
      addZR4(zr4set, znac, znacdat);
    }
    return zr4set;
  }
  public static String formatTS(Timestamp ts) {
    try {
      String ret = new java.text.SimpleDateFormat("dd.MM.yyyy.").format(ts);
      if (ret.equals("01.01.1970.")) return "";
      return ret;
    } catch (Exception e) {
      return "";
    }
  }
  public static void addZR4(StorageDataSet set, QueryDataSet zn, QueryDataSet znd) {
    String vri;
    if (lookupData.getlookupData().raLocate(znd, "CZNAC", zn.getShort("CZNAC")+"")) {
      if (zn.getString("ZNACTIP").equals("D")) {
        try {
          vri = formatTS(Timestamp.valueOf(znd.getString("VRI")));
        } catch (Exception e) {
          vri = "";
        }
      } else if (zn.getString("DOHATTR").endsWith("getMjesta")) {
        dM.getDataModule().getAllMjesta().open();
        if (lookupData.getlookupData().raLocate(dM.getDataModule().getAllMjesta(), "PBR", znd.getString("VRI").trim())) {
          vri = dM.getDataModule().getAllMjesta().getString("NAZMJESTA");
        } else vri = "";
      } else {
        vri = znd.getString("VRI");
      }
    } else vri = "";
    addZR4(set, zn.getShort("CZNAC"), zn.getString("ZNACOPIS"), vri, zn.getInt("SRT"));
  }
  public static void addZR4(StorageDataSet set, int cznac, String opis, String vri) {
    addZR4(set, cznac, opis, vri, cznac*10);
  }
  public static void addZR4(StorageDataSet set, int cznac, String opis, String vri, int srt) {
    set.insertRow(false);
    set.setShort("CZNAC",(short)cznac);
    set.setString("ZNACOPIS", opis);
    set.setString("VRI", vri);
    set.setInt("SRT", srt);
    set.post();
  }
}
