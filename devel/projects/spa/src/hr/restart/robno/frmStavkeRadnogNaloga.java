/****license*****************************************************************
**   file: frmStavkeRadnogNaloga.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmStavkeRadnogNaloga extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  sysoutTEST sys = new sysoutTEST(false);

  static frmStavkeRadnogNaloga frmSRN;

  JPanel jpDetail = new JPanel();
  JPanel jpDetailMain = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlKol = new JLabel();
  JLabel jlCijena = new JLabel();
  JLabel jlIznos = new JLabel();
  JraTextField jraKol = new JraTextField();
  JraTextField jraCijena = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  rapancart rpc = new rapancart();


  public frmStavkeRadnogNaloga() {
    try {
      frmSRN = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmStavkeRadnogNaloga getInstance() {
    return frmSRN;
  }

  public void beforeShow() {
    setSaveChanges(false);
  }

  public void SetFokus(char parm1) {
  }
  public boolean Validacija(char parm1) {
    return false;
  }
  public boolean DeleteCheck() {
    return false;
  }

  public boolean ValidacijaPrijeIzlaza() {
    setSaveChanges(true);
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(frmServis.getInstance().getrepQDSdetails());
    this.setRaDetailPanel(jpDetailMain);
    this.setVisibleCols(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,11,33,34});
    this.setSoftDataLockEnabled(false);

    jpDetail.setLayout(xYLayout2);
    xYLayout2.setWidth(645);
    xYLayout2.setHeight(65);

    jlKol.setText("Koli\u010Dina");
    jlCijena.setHorizontalAlignment(SwingConstants.TRAILING);
    jlCijena.setText("Cijena");
    jlIznos.setHorizontalAlignment(SwingConstants.TRAILING);
    jlIznos.setText("Iznos");

    jraKol.setDataSet(this.getRaQueryDataSet());
    jraKol.setColumnName("KOL");
    jraCijena.setDataSet(this.getRaQueryDataSet());
    jraCijena.setColumnName("ZC");
    jraIznos.setDataSet(this.getRaQueryDataSet());
    jraIznos.setColumnName("IRAZ");

    jpDetail.add(jlIznos, new XYConstraints(530, 8, 74, -1));
    jpDetail.add(jlKol, new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jraKol, new XYConstraints(150, 25, 100, -1));
    jpDetail.add(jraIznos, new XYConstraints(505, 25, 100, -1));
    jpDetail.add(jraCijena, new XYConstraints(400, 25, 100, -1));
    jpDetail.add(jlCijena, new XYConstraints(425, 8, 74, -1));

    this.disableAdd();
    this.setEditEnabled(false);

    jpDetailMain.setLayout(new BorderLayout());
    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.add(rpc, BorderLayout.NORTH);
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);
    initRpcart();
  }

  private void initRpcart() {
    rpc.setTabela(this.getRaQueryDataSet());
    rpc.setMode("DOH");
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
  }
}