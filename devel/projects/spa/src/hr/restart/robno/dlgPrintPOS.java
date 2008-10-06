/****license*****************************************************************
**   file: dlgPrintPOS.java
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

import hr.restart.swing.JraLabel;
import hr.restart.util.raUpitDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;


/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class dlgPrintPOS extends raUpitDialog {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jPanel1 = new JPanel();
//  JraTextField jtfTotal = new JraTextField();
  JraLabel jtfTotal = new JraLabel();
  BorderLayout borderLayout1 = new BorderLayout();

  public dlgPrintPOS() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void componentShow() {
    /**@todo: implement this hr.restart.util.raUpitDialog abstract method*/
  }
  public void okPress() {
    repRacunPOS rpos= new repRacunPOS();
    hr.restart.util.reports.mxRM mxrm = hr.restart.util.reports.mxRM.getDefaultMxRM();
    mxrm.setPrintCommand("hrconv 2 4 < # > lpt1");
    rpos.setRM(mxrm);
    rpos.makeReport();

    rpos.print();
  }
  private void jbInit() throws Exception {
    this.setJPan(jPanel1);
    jtfTotal.setForeground(Color.red);
    jtfTotal.setFont(jtfTotal.getFont().deriveFont(Font.BOLD, Float.parseFloat("80")));
    jtfTotal.setDataSet(dm.getPos());
    jtfTotal.setColumnName("PLATITI");

    jPanel1.setLayout(borderLayout1);
    jPanel1.add(jtfTotal,  BorderLayout.CENTER);
//    this.add(jPanel1, BorderLayout.CENTER)
  }
}