/****license*****************************************************************
**   file: frmBlag.java
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
package hr.restart.blpn;

import hr.restart.baza.Blagajna;
import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmBlag extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  sgStuff ss = sgStuff.getStugg();
  jpBlag detailPanel;

  
  public frmBlag() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'N') {
      enableDisableSaldo(false);
    }
    if (mode == 'I') {
      rcc.EnabDisabAll(detailPanel, false);
      rcc.setLabelLaF(detailPanel.jraNaziv, true);
    }
  }

  public void SetFokus(char mode) {
      if (mode == 'N') {
        this.getRaQueryDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
        detailPanel.jlrKnjig.forceFocLost();
        detailPanel.jraCblag.setText("");
        detailPanel.jraCblag.requestFocus();
        rcc.setLabelLaF(detailPanel.jraPvsaldo, false);
      } else if (mode == 'I') {
      detailPanel.jraNaziv.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(detailPanel.jlrKnjig) || vl.isEmpty(detailPanel.jraCblag) || vl.isEmpty(detailPanel.jlrOznval))
      return false;
    if (mode == 'N' && vl.notUnique(detailPanel.jraCblag))
      return false;
    detailPanel.jlrOznval.emptyTextFields();
    if (detailPanel.jrcbBrezgot.isSelected()) detailPanel.jrcbBrezgot.setSelected(false);
    enableDisableSaldo(false);
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(getBlagajneKnjig());
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        setRaQueryDataSet(getBlagajneKnjig());
        detailPanel.rebind();
      }      
    });
    this.setVisibleCols(new int[] {0, 1, 3, 2});
    detailPanel = new jpBlag(this);
    this.setRaDetailPanel(detailPanel);
    this.getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier(
        "KNJIG",
        new String[] {"CORG","NAZIV"},
        new String[] {"KNJIG"},
        new String[] {"CORG"},
        hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva()
        ));
//    this.getJpTableView().addTableModifier(
//        new hr.restart.swing.raTableColumnModifier(
//        "CBLAG",
//        new String[] {"CBLAG","NAZIV"},
//        new String[] {"CBLAG"},
//        new String[] {"CBLAG"},
//        dm.getBlagajna()
//        ));
//    this.getJpTableView().addTableModifier(
//        new hr.restart.swing.raTableColumnModifier(
//        "OZNVAL",
//        new String[] {"OZNVAL","NAZVAL"},
//        new String[] {"OZNVAL"},
//        new String[] {"OZNVAL"},
//        dm.getValute()
//        ));
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig) {
        detailPanel.jlrKnjig.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        detailPanel.jlrNazknjig.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      };
    });
  }

  protected void enableDisableSaldo(boolean bezgotovinska){
    String oznval = this.getRaQueryDataSet().getString("OZNVAL");
    rcc.setLabelLaF(detailPanel.jraSaldo, !bezgotovinska);
    if (!oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
      rcc.setLabelLaF(detailPanel.jraPvsaldo, !bezgotovinska);
    }
    if(bezgotovinska) {
      this.getRaQueryDataSet().setBigDecimal("SALDO", new java.math.BigDecimal(0));
      if (!oznval.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())){
        this.getRaQueryDataSet().setBigDecimal("PVSALDO", new java.math.BigDecimal(0));
      }
    }
  }
  private static QueryDataSet blagajneknjig;
  private static boolean dirty = false;
  static {
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        dirty = true;
      }
    });
  }
  public static QueryDataSet getBlagajneKnjig() {
    if (blagajneknjig == null || dirty) {
      blagajneknjig = Blagajna.getDataModule().getFilteredDataSet("knjig in "+
          OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(),"knjig"));
    }
    return blagajneknjig;
  }
}