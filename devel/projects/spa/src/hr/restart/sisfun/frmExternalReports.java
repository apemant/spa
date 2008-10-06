/****license*****************************************************************
**   file: frmExternalReports.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.ReportImporter;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFileFilter;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;


public class frmExternalReports extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpExternalReports jpDetail;

  JFileChooser jf = new JFileChooser();

  public frmExternalReports() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraIme, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrApp.forceFocLost();
      jpDetail.jraIme.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNaslov.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraIme) || vl.isEmpty(jpDetail.jraNaslov) ||
        vl.isEmpty(jpDetail.jraUrl) || vl.isEmpty(jpDetail.jlrApp))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraIme))
      return false;
    return true;
  }

  public void selectUrl() {
    if (jf.showOpenDialog(this.getWindow()) == jf.APPROVE_OPTION) {
      if (jf.getSelectedFile().getAbsolutePath().endsWith(".sql")) {
        try {
          int pos;
          String urls = jf.getSelectedFile().toURL().toString();
          if ((pos = urls.toLowerCase().indexOf("hr/restart/util/reports")) >= 0)
            this.getRaQueryDataSet().setString("URL", urls.substring(pos));
          else this.getRaQueryDataSet().setString("URL", urls);
        } catch (Exception e) {}
      } else
        JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna ekstenzija!", "Greška",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void exportReports() {
    DataSet seldds = getSelectionTracker().getSelectedView();
    if (seldds.getRowCount() == 0) {
      JOptionPane.showMessageDialog(this, "Potrebno je odabrati reporte za export!","Info",JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    JFileChooser saver = new JFileChooser();
    saver.setDialogTitle("Snimi definiciju odabranih reporta u ...");
    if (saver.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    String s_xml = saver.getSelectedFile().getAbsolutePath();
    //sqlovi
    String[] s_sqls = new String[seldds.getRowCount()];
    int s_sqls_idx = 0;
    for (seldds.first(); seldds.inBounds(); seldds.next()) {
      String s_sql = seldds.getString("URL");
      File f_sql = null;
      try {
        f_sql = new File(new java.net.URL(s_sql).getFile());
      } catch (Exception e) {
      }
      if (f_sql==null || !f_sql.exists()) {
        f_sql = Aus.findFileAnywhere(s_sql);
      }
      if (f_sql!=null && f_sql.exists()) {
        s_sqls[s_sqls_idx] = f_sql.getAbsolutePath();
        seldds.setString("URL",f_sql.getName()); //na drugom racunalu ce se sql-ovi kreirati u start diru i biti dostupni preko Aus.findFileAnywhere 
      } else {
        //s_sqls[s_sqls_idx] = "";
        JOptionPane.showMessageDialog(this,"Izvještaj "+seldds.getString("IME")+", odnosno file "+s_sql+" ne postoji!","GREŠKA!!",JOptionPane.ERROR_MESSAGE);
        return;
      }
      s_sqls_idx++;
    }
    //datovi
    hr.restart.baza.Tablice.getDataModule().dumpTable(seldds,new File("."),"_exprepext");
    String s_dat = new File("_exprepext.dat").getAbsolutePath();
    try {
      new ReportImporter().exportReports(s_xml,s_dat, s_sqls);
      JOptionPane.showMessageDialog(this,"Odabrani izvještaji uspješno snimljeni u "+s_xml+"!","Poruka",JOptionPane.INFORMATION_MESSAGE);      
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,"Snimanje (export) neuspješan! Greška "+e,"GREŠKA!!!",JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm.getReportext().open();
    this.setRaQueryDataSet(dm.getReportext());
    this.setVisibleCols(new int[] {0, 1, 2, 3});
    jpDetail = new jpExternalReports(this);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    this.setRaDetailPanel(jpDetail);

    jf.setFileFilter(new raFileFilter("Datoteke SQL naredbi (*.sql)"));
    jf.setCurrentDirectory(new File("."));
    installSelectionTracker("IME");
    addOption(new raNavAction("Export",raImages.IMGEXPORT,KeyEvent.VK_F12) {
       public void actionPerformed(ActionEvent e) {
         exportReports();
       }
    },3);
  }
}
