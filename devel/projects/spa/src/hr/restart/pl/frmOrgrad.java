/****license*****************************************************************
**   file: frmOrgrad.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmOrgrad extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet paramDS;
  String cradnik;
  frmRadnicipl parent;

  jpOrgrad jpDetail;

// konstruktor
  public frmOrgrad(frmRadnicipl parent, QueryDataSet paramDS, String cradnik) {
    try {
      this.paramDS = paramDS;
      this.cradnik = cradnik;
      this.parent = parent;



      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

// Disabla tekst komponente kljuca kod izmjene
  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCorg, false);
      rcc.setLabelLaF(jpDetail.jlrNaziv, false);
      rcc.setLabelLaF(jpDetail.jbSelCorg, false);
    }
  }

// setiranje fokusa ovisno o modu rada
  public void SetFokus(char mode) {
    getRaQueryDataSet().setString("CRADNIK", cradnik);
    if (mode == 'N') {
      jpDetail.jlrCorg.forceFocLost();
      jpDetail.jlrCorg.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraUdiorada.requestFocus();
    }
  }

// validacija
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCorg))
      return false;
    if (mode == 'N' && notUnique())
    {
      jpDetail.jlrCorg.requestFocus();
      JOptionPane.showConfirmDialog(jpDetail.jlrCorg, "Zapis postoji !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

//provjera jedinstvenosti kljuceva
  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkOrgRadUnique(getRaQueryDataSet().getString("CORG"),
                                                getRaQueryDataSet().getString("CRADNIK"));
  }

// init
  private void jbInit() throws Exception {
    parent.setEnabled(false);
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-630;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-260;
    this.setLocation((int)x/2,(int)y/2);
    this.setSize(630, 260);
    this.setTitle("Udio rada");
    this.setRaQueryDataSet(paramDS);
    this.setVisibleCols(new int[] {0, 1, 2});
    jpDetail = new jpOrgrad(this);
    this.setRaDetailPanel(jpDetail);
  }

  public void this_hide()
  {
    parent.setEnabled(true);
    super.this_hide();
  }
}
