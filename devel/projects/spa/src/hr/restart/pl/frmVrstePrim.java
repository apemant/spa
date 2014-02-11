/****license*****************************************************************
**   file: frmVrstePrim.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmVrstePrim extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpVrstePrim jpDetail;

// definiranje rnv buttona
  raNavAction rnvSume = new raNavAction("Sume",raImages.IMGSUM,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      sume_action();
    }
  };
  raNavAction rnvOsnovice = new raNavAction("Osnovice",raImages.IMGALLDOWN,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      osnovice_action();
    }
  };
  raNavAction rnvIzvPrim = new raNavAction("Izvještaji",raImages.IMGOPEN,KeyEvent.VK_F9) {
    public void actionPerformed(ActionEvent e) {
      izv_action();
    }
  };
  raNavAction rnvVrOdb = new raNavAction("Odbici",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      vrOdb_action();
    }
  };

// overridana offon metoda
  public void offon(boolean prazno)
  {
    rnvOsnovice.setEnabled(prazno);
    rnvSume.setEnabled(prazno);
    rnvIzvPrim.setEnabled(prazno);
    super.offon(prazno);
  }

// konstruktor
  public frmVrstePrim() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

// provjera stavki prije brisanja
  public boolean BeforeDelete()
  {
    plUtil.getPlUtil().deleteStandOdb("ZA", this.getRaQueryDataSet().getShort("CVRP")+"");
//    if(plUtil.getPlUtil().checkVRPStavke(getRaQueryDataSet().getShort("CVRP")))
//    {
//      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
//      return false;
//    }

    return true;
  }

// Disabla tekst komponente kljuca kod izmjene
  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCvrp, false);
    }
  }

// setiranje fokusa ovisno o modu rara
  public void SetFokus(char mode) {
    if(mode != 'N')
    {
      selectParam(1);
    }

    if (mode == 'N') {
      jpDetail.jlrCsif1.forceFocLost();
      jpDetail.jlrCsif.forceFocLost();
      jpDetail.jraCvrp.requestFocus();
      jpDetail.jlrCvrparh.forceFocLost();
      jpDetail.jlrStavka.forceFocLost();
      selectParam(0);
    } else if (mode == 'I') {
      jpDetail.jraNaziv.requestFocus();
    }
  }

// validacija
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCvrp) || vl.isEmpty(jpDetail.jlrCobr))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCvrp))
      return false;
    raParam.setParam(this.getRaQueryDataSet(), 1, (jpDetail.jcDop.isSelected())? "D": "N");
    raParam.setParam(this.getRaQueryDataSet(), 2, (jpDetail.jcPor.isSelected())? "D": "N");
    raParam.setParam(this.getRaQueryDataSet(), 3, (jpDetail.jcKred.isSelected())? "D": "N");
    raParam.setParam(this.getRaQueryDataSet(), 4, (jpDetail.jcHar.isSelected())? "D": "N");
    return true;
  }

// init
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVrsteprim());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpVrstePrim(this);
    this.addOption(rnvSume,4);
    this.addOption(rnvOsnovice,5);
    this.addOption(rnvIzvPrim,6);
    this.addOption(rnvVrOdb,5);
    this.addOption(new raNavAction("Šifre za JOPPD",raImages.IMGALLUP,KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        DlgRadplSifre.showDialog("P"+getRaQueryDataSet().getShort("CVRP"));
      }
    },7);

    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("COBR", new String[]{"COBR", "OPIS"}, dm.getNacobr())
    );
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }

// pozivanje globalne klase Odbitaka ovisno o parametru (vrsti odbitaka) preko rnv buttona s predefiniranim dataset-om
  void sume_action() {
    QueryDataSet sumePrimDS = new QueryDataSet();
    sumePrimDS = plUtil.getPlUtil().getsumePrimDS(getRaQueryDataSet().getShort("CVRP"));
    frmSumePrim fs = new frmSumePrim(this, sumePrimDS, getRaQueryDataSet().getShort("CVRP"));
    fs.show();
  }
  void osnovice_action() {
    QueryDataSet osnPrimDS = new QueryDataSet();
    osnPrimDS = plUtil.getPlUtil().getosnPrimDS(getRaQueryDataSet().getShort("CVRP"));
    frmOsnPrim fo = new frmOsnPrim(this, osnPrimDS, getRaQueryDataSet().getShort("CVRP"));
    fo.show();
  }
  void izv_action() {
    QueryDataSet grizvPrimDS = new QueryDataSet();
    grizvPrimDS = plUtil.getPlUtil().getgrizvPrimDS2(getRaQueryDataSet().getShort("CVRP"));
    frmGrizvPrim fgp = new frmGrizvPrim(this, grizvPrimDS,getRaQueryDataSet().getShort("CVRP"));
    fgp.show();
  }
  void vrOdb_action() {
    frmGlobalMaster fGM = new frmGlobalMaster(this, "ZA", getRaQueryDataSet().getShort("CVRP")+"","ZA");
    fGM.show();
  }

  public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent ev)
  {
    selectParam(1);
  }

  void selectParam(int i) 
  {
    jpDetail.jcDop.setSelected((i != 1) || raParam.getParam(getRaQueryDataSet(), 1).equals("D"));
    jpDetail.jcPor.setSelected((i != 1) || raParam.getParam(getRaQueryDataSet(), 2).equals("D"));
    jpDetail.jcKred.setSelected((i != 1) || raParam.getParam(getRaQueryDataSet(), 3).equals("D"));
    jpDetail.jcHar.setSelected((i != 1) || Harach.getHaracFlag(getRaQueryDataSet()).equals("D"));
    
    /* kuku menee!
    if(i == 1) {
      if(raParam.getParam(getRaQueryDataSet(), 1).equals("D"))
        jpDetail.jcDop.setSelected(true);
      else
        jpDetail.jcDop.setSelected(false);
      if(raParam.getParam(getRaQueryDataSet(), 2).equals("D"))
        jpDetail.jcPor.setSelected(true);
      else
        jpDetail.jcPor.setSelected(false);
      if(raParam.getParam(getRaQueryDataSet(), 3).equals("D"))
        jpDetail.jcKred.setSelected(true);
      else
        jpDetail.jcKred.setSelected(false);
      if(Harach.getHaracFlag(getRaQueryDataSet()).equals("D"))
        jpDetail.jcHar.setSelected(true);
      else
        jpDetail.jcHar.setSelected(false);
      
    } else {
      jpDetail.jcDop.setSelected(true);
      jpDetail.jcPor.setSelected(true);
      jpDetail.jcKred.setSelected(true);
      jpDetail.jcHar.setSelected(true);
    }
       */
  }

// overridana metoda show
  public void show()
  {
    this.setSize(632, 455);
    super.show();
  }

  public void AfterSave(char mode)
  {
    if(mode=='N')
    {
      plUtil.getPlUtil().addStandOdbici("ZA", getRaQueryDataSet().getShort("CVRP")+"");
    }
  }
}
