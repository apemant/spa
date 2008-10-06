/****license*****************************************************************
**   file: jptestKoef.java
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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jptestKoef extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  StorageDataSet DummySet = new StorageDataSet();
  rapancart rpcart = new rapancart() {
    public void metToDo_after_lookUp(){
      jraDatod.requestFocus();
    }
  };
  rapancskl1 rpskl = new rapancskl1(349){
      public void MYpost_after_lookUp(){
          rpcart.SetDefFocus();
      }
    
  };
  XYLayout lay = new XYLayout();
  JLabel jlDatod = new JLabel();
  JraTextField jraDatdo = new JraTextField();
  JraTextField jraDatod = new JraTextField();

  public jptestKoef() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    Column col_1 = dm.createTimestampColumn("DATOD");
    Column col_2 = dm.createTimestampColumn("DATDO");
    DummySet.addColumn(col_1);
    DummySet.addColumn(col_2);
    DummySet.open();
    jlDatod.setText("Datum (od - do)");
    jraDatdo.setColumnName("DATDO");
    jraDatdo.setDataSet(DummySet);
    jraDatdo.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatod.setColumnName("DATOD");
    jraDatod.setDataSet(DummySet);
    jraDatod.setHorizontalAlignment(SwingConstants.CENTER);
    rpcart.setBorder(null);
    rpcart.setMode(new String("DOH"));
    rpcart.setSearchable(false);
    this.setLayout(lay);
    lay.setHeight(145);
    add(rpskl, new XYConstraints(0, 0, -1, -1));
    add(rpcart, new XYConstraints(0, 40, -1, 75));
    add(jlDatod, new XYConstraints(15, 115, -1, -1));
    add(jraDatdo, new XYConstraints(260, 115, 100, -1));
    add(jraDatod, new XYConstraints(150, 115, 100, -1));
  }

  public void ClearART() {
    /** @todo sad clear rapancart!!! */
  }

  public void setEnableRpcart(boolean how) {
    rcc.EnabDisabAll(rpcart, how);
    if (how){
      rpcart.setCART();
    }
  }

  public boolean validacija() {
    return true;
  }

  public void setEnDis(boolean how){
//    setEnableRpcart(how);
    rcc.EnabDisabAll(rpcart, how);
    rpcart.clearFields();
    rcc.EnabDisabAll(rpskl, how);
    rcc.setLabelLaF(jraDatod,how);
    rcc.setLabelLaF(jraDatdo, how);
  }

  public String getCART(){
    return rpcart.getCART();
  }

  public String getCART1(){
    return rpcart.getCART1();
  }

  public String getBC(){
    return rpcart.getBC();
  }

  public String getCGRART(){
    return rpcart.getCGRART();
  }

  public String getNAZART(){
    return rpcart.getNAZART();
  }
}
