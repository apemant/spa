/****license*****************************************************************
**   file: jpProizvodnjaDetail.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpProizvodnjaDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmProizvodnja fProizvodnja;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlKol = new JLabel();
  JraTextField jraKol = new JraTextField();

  protected rapancart rpc = new rapancart() {
    public QueryDataSet getRaDataSet(){
      return Artikli.getDataModule().getFilteredDataSet(
          raVart.getProizvodCond());
    }

    public void metToDo_after_lookUp() {
      if (!rpcLostFocus && fProizvodnja.raDetail.getMode() == 'N') {
        rpcLostFocus = true;
        fProizvodnja.rpcOut();
      };
    }
  };

  protected boolean rpcLostFocus;

  public jpProizvodnjaDetail(frmProizvodnja md) {
    try {
      fProizvodnja = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EraseFields() {
    rpcLostFocus = false;
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EnableFields() {
    rcc.EnabDisabAll(jpDetail, true);
  }

  public void BindComponents(DataSet ds) {
    jraKol.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    jpDetail.setLayout(lay);
    lay.setWidth(265);
    lay.setHeight(60);

    jlKol.setText("Koli\u010Dina");
    jraKol.setColumnName("KOL");

    jpDetail.add(jlKol, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraKol, new XYConstraints(150, 20, 100, -1));

    BindComponents(fProizvodnja.getDetailSet());
//    initRpcart();
    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    this.add(rpc, BorderLayout.NORTH);
    this.add(jpDetail, BorderLayout.CENTER);
  }

  public void initRpcart() {
    rpc.setTabela(dm.getStRnlPro());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.setFocusCycleRoot(true);
    rpc.setAllowUsluga(false);
    rpc.InitRaPanCart();
  }
}
