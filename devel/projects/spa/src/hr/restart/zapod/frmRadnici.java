/****license*****************************************************************
**   file: frmRadnici.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;

public class frmRadnici extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpRadnici jpDetail;

  public frmRadnici() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private String corgBefore = null;
  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      corgBefore = null;
      rcc.setLabelLaF(jpDetail.jraCradnik, false);
      vl.execSQL("SELECT count(*) FROM RADNICIPL WHERE CRADNIK = '"+getRaQueryDataSet().getString("CRADNIK")+"'");
      boolean enab = vl.getSetCount(vl.RezSet,0)==0;
      if (!enab) {
        corgBefore = getRaQueryDataSet().getString("CORG");
        enab = !hr.restart.pl.raIniciranje.getInstance().isInitObr(corgBefore);
      }
      rcc.setLabelLaF(jpDetail.jlrCorg, enab);
      rcc.setLabelLaF(jpDetail.jlrNaziv, enab);
      rcc.setLabelLaF(jpDetail.jbSelCorg, enab);
      /** @todo setlabelaf(corg, enab) */
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCorg.forceFocLost();
      jpDetail.jraCradnik.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraIme.requestFocus();
    }
  }
  
  private boolean chOrgStr;
  public boolean doBeforeSave(char mode) {
    if (chOrgStr && mode == 'I') {
      try {
        raTransaction.runSQL("UPDATE radnicipl SET CORG = '"+getRaQueryDataSet().getString("CORG")
          +"' where CRADNIK = '"+getRaQueryDataSet().getString("CRADNIK")+"'");
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    return true;
  }
  public boolean Validacija(char mode) {
    chOrgStr = false;
    if (mode == 'I') {
      if (hr.restart.pl.raIniciranje.getInstance().isInitObr(getRaQueryDataSet().getString("CORG"))) {
        JOptionPane.showMessageDialog(getWindow(), "Nije moguæe promijeniti org. jedinicu u "
        +getRaQueryDataSet().getString("CORG")
        +" jer je za nju inicirana obrada!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (corgBefore != null && !corgBefore.equals(getRaQueryDataSet().getString("CORG"))) {
        int odg = JOptionPane.showConfirmDialog(getWindow(),"Promjena org.jedinice radnika iz "+corgBefore
        +" u "+getRaQueryDataSet().getString("CORG")+" izvršit æe se i na podacima za plaæu! Nastaviti?",
        "Pozor!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (odg != JOptionPane.YES_OPTION) return false;
        chOrgStr = true;
      }
    }
    if (vl.isEmpty(jpDetail.jraCradnik) || vl.isEmpty(jpDetail.jraIme) || vl.isEmpty(jpDetail.jraPrezime))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jraCradnik))
      return false;
    return true;
  }
/*
  public boolean DeleteCheck(){
    vl.execSQL("select * from radnicipl where radnicipl.cradnik='" + this.getRaQueryDataSet().getString("CRADNIK") + "'");
    vl.RezSet.open();
    System.out.println("vl.RezSet.rowCount() == 0  " + (vl.RezSet.rowCount() == 0));
    if (vl.RezSet.rowCount() == 0) return true;
    JOptionPane.showMessageDialog(this,
      "Radnik je evidentiran pri pla\u0107i. Brisanje nemogu\u0107e",
      "Konflikt",
      JOptionPane.ERROR_MESSAGE);
    return false;
  }
*/
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getAllRadnici());
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpRadnici(this);
    this.setRaDetailPanel(jpDetail);
    raDataIntegrity.installFor(this);
  }

  public void beforeShow()
  {
    this.getJpTableView().getMpTable().getDataSet().open();
  }
}

