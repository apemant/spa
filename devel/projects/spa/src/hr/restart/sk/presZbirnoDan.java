/****license*****************************************************************
**   file: presZbirnoDan.java
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
package hr.restart.sk;

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.raComboBox;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class presZbirnoDan extends presZbirno {

  frmDugPot frm;

  raComboBox rcbDat = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };
  //JLabel jlDan = new JLabel();
  JLabel jlIznos = new JLabel();
  JraTextField jraIznos = new JraTextField();
  JraCheckBox jcbPS = new JraCheckBox();

  public presZbirnoDan(frmDugPot fdp) {
    try {
      frm = fdp;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void resetDefaults() {
    super.resetDefaults();
    rcbDat.setSelectedIndex(0);
    jcbPS.setSelected(true);
    gmsz.setBigDecimal("MINIZNOS", Aus.one0.movePointLeft(2));
    getSelRow().setTimestamp("DATUMKNJ-to", vl.getToday());
  }
  
  public void SetFokus() {
    super.SetFokus();
    //System.out.println(getSelRow());
  }

  protected void focusAfterZup() {
    jraDatumto.requestFocus();
  }
  
  public boolean Validacija() {
    if (!Aus.checkSanityRange(jraDatumto)) return false;
    return jpc.Validacija();
  }
  
  public boolean isPS() {
    return jcbPS.isSelected();
  }

  private void jbInit() throws Exception {
    lay.setHeight(155 + dkAdd);

    //jlDan.setText("Na dan");
    rcbDat.setRaItems(new String[][] {
        {"Na dan", "D"},
        {"Knjiženo do", "K"}
      });
    rcbDat.setSelectedIndex(0);
    jlIznos.setText("Minimalni iznos");

    jraIznos.setColumnName("MINIZNOS");
    jraIznos.setDataSet(gmsz);

    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumfrom.setVisible(false);
    jraDatumfrom.setEnabled(false);
    
    jcbPS.setText(" Uraèunati stavke iz poèetnog stanja ");
    jcbPS.setHorizontalTextPosition(jcbPS.LEADING);
    jcbPS.setHorizontalAlignment(jcbPS.TRAILING);

    jpDetail.add(rcbDat, new XYConstraints(15, 95 + dkAdd, 130, -1));
    jpDetail.add(jraDatumto, new XYConstraints(150, 95 + dkAdd, 100, -1));
    jpDetail.add(jlIznos, new XYConstraints(320, 97 + dkAdd, -1, -1));
    jpDetail.add(jraIznos, new XYConstraints(445, 95 + dkAdd, 100, -1));
    jpDetail.add(jcbPS, new XYConstraints(260, 120 + dkAdd, 285, -1));

    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jpDetail);
  }
  
  public String getDatumCol() {
    return rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ";
  }
}
