/****license*****************************************************************
**   file: frmVrijednosti.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.RN_sifznac;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmVrijednosti extends raMatPodaci {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  static frmVrijednosti vrijed;

  JPanel jpDetail = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraVri = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JLabel jlVri = new JLabel();
  JLabel jlOpis = new JLabel();

  QueryDataSet vri = new QueryDataSet();

  short cvrsubj, cznac;

  public frmVrijednosti() {
    try {
      vrijed = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmVrijednosti getInstance() {
    return vrijed;
  }

  public void SetQuery(short _cvrsubj, short _cznac) {
    if (cvrsubj != _cvrsubj || cznac != _cznac) {
      cvrsubj = _cvrsubj;
      cznac = _cznac;
      RN_sifznac.getDataModule().setFilter(vri, Condition.equal("CVRSUBJ", cvrsubj)
         .and(Condition.equal("CZNAC", cznac)));
      vri.open();
//      vri.close();
//      vri.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
//            hr.restart.baza.dM.getDataModule().getDatabase1(),
//            "SELECT * FROM RN_sifznac WHERE cvrsubj = "+cvrsubj+" AND cznac = "+cznac
//          ));
//      vri.setResolver(dm.getQresolver());
//      vri.open();
    }
  }

  public void beforeShow() {
    SetQuery(frmZnacajke.getInstance().getMasterSet().getShort("CVRSUBJ"),
             frmZnacajke.getInstance().getDetailSet().getShort("CZNAC"));
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraVri, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      this.getRaQueryDataSet().setShort("CVRSUBJ", cvrsubj);
      this.getRaQueryDataSet().setShort("CZNAC", cznac);
      jraVri.requestFocus();
    } else if (mode == 'I' ){
      jraOpis.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraVri)) {
      return false;
    }
    if (mode == 'N' && DetailNotUnique()) {
      jraVri.requestFocus();
      JOptionPane.showMessageDialog(this.jpDetail,
         "Ista vrijednost ve\u0107 postoji u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  private boolean doMark = false;
  public boolean doWithSave(char mode) {
    doMark = true;
    return true;
  }
  
  public void AfterSave(char mode) {
    if (doMark) {
      doMark = false;
      dM.getSynchronizer().markAsDirty("RN_sifznac");
    }
  }
  
  protected boolean DetailNotUnique() {
    vl.execSQL("SELECT * FROM RN_sifznac WHERE cvrsubj = "+cvrsubj+
               " AND cznac = "+cznac+
               " AND vriznac = '"+this.getRaQueryDataSet().getString("VRIZNAC")+"'");
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }

  private void jbInit() throws Exception {

    vri.setColumns(dm.getRN_sifznac().cloneColumns());

    cvrsubj = cznac = -1;

    this.setRaDetailPanel(jpDetail);
    this.setRaQueryDataSet(vri);
    this.setVisibleCols(new int[] {2,3});

    jpDetail.setLayout(xYLayout1);
    xYLayout1.setWidth(500);
    xYLayout1.setHeight(85);

    jlVri.setText("Vrijednost");
    jlOpis.setText("Opis vrijednosti");
    jraVri.setDataSet(this.getRaQueryDataSet());
    jraVri.setColumnName("VRIZNAC");
    jraOpis.setDataSet(this.getRaQueryDataSet());
    jraOpis.setColumnName("OPIS");

    jpDetail.add(jraVri, new XYConstraints(150, 20, 330, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 45, 330, -1));
    jpDetail.add(jlVri, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 45, -1, -1));
  }
}