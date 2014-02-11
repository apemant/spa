/****license*****************************************************************
**   file: frmRadnicipl.java
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
package hr.restart.pl;

import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraButton;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmRadnicipl extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
//  QueryDataSet myDS;
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  char modTab;

  jpRadnicipl jpDetail;

  raNavAction rnvIzvProm = new raNavAction("Org. jedinice",raImages.IMGMOVIE,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      orgRad_action();
    }
  };
  raNavAction rnvVrOdb = new raNavAction("Odbici",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      vrOdb_action();
    }
  };
  raNavAction rnvSifre = new raNavAction("Šifre za JOPPD",raImages.IMGALLUP,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
     DlgRadplSifre.showDialog(getRaQueryDataSet().getString("CRADNIK"));
    }
  };
  private static frmRadnicipl fradnicipl;
  
  public static frmRadnicipl getInstance() {
    if (fradnicipl == null) new frmRadnicipl();
    return fradnicipl;
  }
  
  public frmRadnicipl() {
    try {
      jbInit();
      fradnicipl = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    jpDetail.jlrRsz.forceFocLost();
    jpDetail.jlrRsb.forceFocLost();
    jpDetail.jlrRsinv.forceFocLost();
    jpDetail.jlrRsoo.forceFocLost();
    jpDetail.jlrCradmj.forceFocLost();
    jpDetail.jlrCisplmj.forceFocLost();
    jpDetail.jlrCopcine.forceFocLost();
    jpDetail.jlrCvro.forceFocLost();
    jpDetail.jlrCss.forceFocLost();

    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCradnik, false);
      rcc.setLabelLaF(jpDetail.jlrRadIme, false);
      rcc.setLabelLaF(jpDetail.jlrRadNaziv, false);
      rcc.setLabelLaF(jpDetail.jcbSelRad, false);
    }
  }

  public boolean BeforeDelete()
  {
    plUtil.getPlUtil().deleteStandOdb("RA", this.getRaQueryDataSet().getString("CRADNIK"));
    return true;
  }

  public void SetFokus(char mode) {
    modTab = mode;
    this.jpDetail.jTabbedPane1.setSelectedIndex(0);

    rcc.setLabelLaF(jpDetail.jraStopastaz, false);
    rcc.setLabelLaF(jpDetail.jraBrutmr, false);
    rcc.setLabelLaF(jpDetail.jraBrutuk, false);
    rcc.setLabelLaF(jpDetail.jraOluk, false);
    if (mode == 'N') {
      getRaQueryDataSet().setString("RSINV","0");
      getRaQueryDataSet().setString("RSOO","10");
      jpDetail.jlrRsinv.forceFocLost();
      jpDetail.jlrRsoo.forceFocLost();
      getRaQueryDataSet().setBigDecimal("KOEF",new BigDecimal("100"));
      getRaQueryDataSet().setBigDecimal("KOEFZAR",new BigDecimal("100"));
      jpDetail.jlrCradnik.requestFocus();
      jpDetail.getCustomPanel().insert();
    } else if (mode == 'I') {
      jpDetail.jraAdresa.requestFocus();
    }
    if (mode != 'N') {
      System.out.println("KastumPejnelj.setValjuz("+getRaQueryDataSet().getString("CRADNIK"));
      jpDetail.getCustomPanel().setValues("");
      jpDetail.getCustomPanel().setValues(getRaQueryDataSet().getString("CRADNIK"));
    }
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setString("CORG", plUtil.getPlUtil().getRadnikCorg(getRaQueryDataSet().getString("CRADNIK")));
    if (vl.isEmpty(jpDetail.jlrCradnik) || vl.isEmpty(jpDetail.jlrCradmj) || vl.isEmpty(jpDetail.jlrCss) || vl.isEmpty(jpDetail.jlrCvro) || vl.isEmpty(jpDetail.jlrCisplmj) || vl.isEmpty(jpDetail.jlrCopcine))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jlrCradnik))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
//
//    bind();
    String cond = plUtil.getPlUtil().getRadCurKnjig();
    String cond2 = " AND EXISTS (SELECT * FROM radnici where radnicipl.cradnik=radnici.cradnik and radnici.aktiv='D')";
    this.setRaQueryDataSet(Radnicipl.getDataModule().getFilteredDataSet(cond+cond2));
    this.setVisibleCols(new int[] {0});
    jpDetail = new jpRadnicipl(this);

    this.addOption(rnvVrOdb, 4);
    getJpTableView().addTableModifier(
      new raTableColumnModifier("CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getAllRadnici())
    );

    this.addOption(rnvIzvProm,4);
    this.addOption(rnvSifre,4);
    this.setRaDetailPanel(jpDetail);

    getJpTableView().setNoTablePanel(new frmRadnicipl.jpNoTableGetRadnici(getJpTableView()));

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
    new hr.restart.zapod.raKnjigChangeListener()
    {
      public void knjigChanged(String oldKnjig, String newKnjig)
      {
        bind();
      };
    }
    );
    raDataIntegrity rdi = raDataIntegrity.installFor(this);
    rdi.addIgnoreTables(new String[] {
        "doki",
        "Stavblag",
        "OS_SI",
        "Putnalarh",
        "PutniNalog",
        "Radnici",
        "Stavkeblarh",
        "PlZnacRadData",
        "OS_Sredstvo"
        });
  }

  void orgRad_action() {
    QueryDataSet orgRadDS = new QueryDataSet();
    orgRadDS = plUtil.getPlUtil().getOrgRadDS(getRaQueryDataSet().getString("CRADNIK"));
    frmOrgrad fOR = new frmOrgrad(this, orgRadDS, getRaQueryDataSet().getString("CRADNIK"));
    fOR.show();
  }

  public void show()
  {
    this.setTitle("Radnici za obra\u010Dun");
    super.show();
  }

  void vrOdb_action() {
    frmGlobalMaster fGM = new frmGlobalMaster(this, "RA", getRaQueryDataSet().getString("CRADNIK"), "RA");
    fGM.show();
  }

  public void tabStChanged()
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if(modTab == 'N')
        {
          jpDetail.jlrCradnik.requestFocus();
        }
        if(modTab == 'I')
        {
          jpDetail.jlrCradmj.requestFocus();
        }
      }
    });
  }

  public void AfterCancel() {
    jpDetail.getCustomPanel().cancel();
  }

  private void bind()
  {
    getOKpanel().jPrekid_actionPerformed();
    String cond = plUtil.getPlUtil().getRadCurKnjig();
    Radnicipl.getDataModule().setFilter(this.getRaQueryDataSet(), cond);
    jpDetail.jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jpDetail.jlrRadNaziv.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jpDetail.jlrRadIme.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    this.getRaQueryDataSet().open();
    getJpTableView().fireTableDataChanged();
  }
  /*
  public void raQueryDataSet_navigated(NavigationEvent ev) {
    Detail.getCustomPanel().setValues(getRaQueryDataSet().getString("CRADNIK"));
  }
  */
  public boolean doWithSave(char mode) {
    raTransaction.saveChanges(jpDetail.getCustomPanel().updateValues());
    return true;
  }
  public void AfterSave(char mode)
  {
    if(mode=='N')
    {
      plUtil.getPlUtil().addStandOdbici("RA", getRaQueryDataSet().getString("CRADNIK"));
      jpDetail.getCustomPanel().cancel();
    }
  }
  
  public static class jpNoTableGetRadnici extends JPanel {
    JlrNavField jlrCradnik = new JlrNavField();
    JlrNavField jlrPrezime = new JlrNavField();
    JlrNavField jlrIme = new JlrNavField();
    JLabel jlCradnik = new JLabel();
    StorageDataSet dset = new StorageDataSet();
    JraButton jbSelCradnik = new JraButton();
    public jpNoTableGetRadnici(raJPTableView jpTV) {
      super(new XYLayout());
      if (jpTV != null) jpTV.setFocusedNoTablePanelField(jlrCradnik);
      jinit();
    }
    void jinit() {
      dset.addColumn("CRADNIK", Variant.STRING);
      dset.addColumn("IME", Variant.STRING);
      dset.addColumn("PREZIME", Variant.STRING);
      dset.open();
      jlCradnik.setText("Radnik");
      jbSelCradnik.setText("...");
      jlrCradnik.setColumnName("CRADNIK");
      jlrCradnik.setDataSet(dset);
      jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
      jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
      jlrCradnik.setVisCols(new int[] {0, 1, 2});
      jlrCradnik.setSearchMode(0);
      jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
      jlrCradnik.setNavButton(jbSelCradnik);
      
      jlrIme.setColumnName("IME");
      jlrIme.setNavProperties(jlrCradnik);
      jlrIme.setSearchMode(1);
      
      jlrPrezime.setColumnName("PREZIME");
      jlrPrezime.setNavProperties(jlrCradnik);
      jlrPrezime.setSearchMode(1);
      add(jbSelCradnik, new XYConstraints(565, 20, 21, 21));
      add(jlCradnik, new XYConstraints(15, 20, -1, -1));
      add(jlrCradnik, new XYConstraints(150, 20, 100, -1));
      add(jlrPrezime, new XYConstraints(255, 20, 200, -1));
      add(jlrIme, new XYConstraints(460, 20, 100, -1));
      ((XYLayout)getLayout()).setHeight(55);
    }
  }
}


