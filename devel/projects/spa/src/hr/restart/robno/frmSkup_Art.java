/****license*****************************************************************
**   file: frmSkup_Art.java
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

import hr.restart.sisfun.Asql;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmSkup_Art extends raMasterFakeDetailArtikl {
  _Main ma;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  JLabel jlSkupart = new JLabel();
  JraTextField jraCSKUPART = new JraTextField();
  JPanel jpSel = new JPanel();
  JraTextField jraNAZSKUPART = new JraTextField();
  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraKol = new JraTextField();
  JPanel jPanel1 = new JPanel();
  JLabel jlKol = new JLabel();

  String[] key = new String[] {"CSKUPART"};

  public frmSkup_Art() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraCSKUPART, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jraCSKUPART.requestFocus();
    } else {
      jraNAZSKUPART.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jraCSKUPART)) return false;
    if (mode == 'N' && MasterNotUnique()) {
      jraCSKUPART.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Skupina artikala s istom šifrom ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'I') {
      Asql.propagateMasterChanges("skupart", mast, new String[] {"CSKUPART"}, new String[] {"NAZSKUPART"});
    }
    mast.post();
    return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }

  public void SetFokusIzmjena() {
    jraKol.requestFocus();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraKol)) return false;
    this.getDetailSet().setString("NAZSKUPART", mast.getString("NAZSKUPART"));
    return true;
  }

  public void ClearFields() {
//    jraKol.setText("");
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
  }

  public String CheckMasterKeySQLString() {
    return "select * from skupart where cskupart = '" + mast.getString("CSKUPART") + "'";
  }

  private void jbInit() throws Exception {

    Asql.createMasterSkupart(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Skupine artikala");
    this.setVisibleColsMaster(new int[] {0,1});
    this.setMasterKey(key);

    this.setDetailSet(dm.getSkupart());
    this.setNaslovDetail("Stavke skupine artikala");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(2,3,4),5,6,7});
    this.setDetailKey(key);

    jpSel.setLayout(xYLayout2);
    jlSkupart.setText("Skupina artikala");

    xYLayout2.setWidth(555);
    xYLayout2.setHeight(60);
    jraCSKUPART.setDataSet(this.getMasterSet());
    jraCSKUPART.setColumnName("CSKUPART");
    jraCSKUPART.setHorizontalAlignment(SwingConstants.TRAILING);
    jraNAZSKUPART.setDataSet(this.getMasterSet());
    jraNAZSKUPART.setColumnName("NAZSKUPART");
    jraNAZSKUPART.setHorizontalAlignment(SwingConstants.LEADING);

    xYLayout1.setWidth(430);
    xYLayout1.setHeight(45);
    jraKol.setDataSet(this.getDetailSet());
    jraKol.setColumnName("KOL");
    jraKol.setHorizontalAlignment(SwingConstants.TRAILING);
    jPanel1.setLayout(xYLayout1);
    jlKol.setToolTipText("");
    jlKol.setText("Koli\u010Dina");
    jpSel.add(jlSkupart, new XYConstraints(15, 20, -1, -1));
    jpSel.add(jraCSKUPART, new XYConstraints(150, 20, 100, -1));
    jpSel.add(jraNAZSKUPART, new XYConstraints(260, 20, 275, -1));
    jPanel1.add(jlKol, new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jraKol, new XYConstraints(150, 0, 100, -1));

    SetPanels(jpSel, jPanel1, false);
  }

  protected void initRpcart() {
    rpc.setTabela(dm.getSkupart());
    rpc.setBorder(null);
    super.initRpcart();
    rpc.setAllowUsluga(true);
  }
}