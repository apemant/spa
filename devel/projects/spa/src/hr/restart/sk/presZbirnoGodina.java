/****license*****************************************************************
**   file: presZbirnoGodina.java
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
import hr.restart.util.Aus;

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

public class presZbirnoGodina extends presZbirno {

//  frmZbirno frm;

  JLabel jlGod = new JLabel();
  //JraTextField jraGod = new JraTextField();
  JraCheckBox jcbPS = new JraCheckBox();

  public presZbirnoGodina() {
    try {
//      frm = fz;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  protected void focusAfterZup() {
    //jraGod.requestFocus();
    jraDatumto.requestFocus();
  }
  
  public void resetDefaults() {
    super.resetDefaults();
    getSelRow().setTimestamp("DATUMKNJ-to", ut.getLastDayOfMonth());
  }

  public boolean Validacija() {
    if (!Aus.checkSanityRange(jraDatumto)) return false;
    return jpc.Validacija();
  }
  
  public boolean isPS() {
    return jcbPS.isSelected();
  }

  private void jbInit() throws Exception {

    lay.setHeight(130 + dkAdd);
    jlGod.setText("Knjiženo do");

    /*jraGod.setColumnName("GOD");
    jraGod.setDataSet(gmsz);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);*/
    
    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumfrom.setVisible(false);
    jraDatumfrom.setEnabled(false);
    
    jcbPS.setText(" Ukljuèiti poèetno stanje u saldo ");
    jcbPS.setHorizontalTextPosition(jcbPS.LEADING);
    jcbPS.setHorizontalAlignment(jcbPS.TRAILING);

//    jraDatumfrom.setVisible(false);
//    jraDatumfrom.setEnabled(false);
//    jraDatumto.setVisible(false);
//    jraDatumto.setEnabled(false);

    jpDetail.add(jlGod, new XYConstraints(15, 97 + dkAdd, -1, -1));
    jpDetail.add(jraDatumto, new XYConstraints(150, 95 + dkAdd, 100, -1));
    //jpDetail.add(jraGod, new XYConstraints(150, 95 + dkAdd, 50, -1));
    jpDetail.add(jcbPS, new XYConstraints(260, 95 + dkAdd, 285, -1));

//    jpDetail.add(jraDatumfrom, new XYConstraints(130, 70, 5, -1));
//    jpDetail.add(jraDatumto, new XYConstraints(140, 70, 5, -1));
//
    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jpDetail);
  }
}

