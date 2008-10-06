/****license*****************************************************************
**   file: raStatus.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raStatus extends JraDialog {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jpStatus = new JPanel();
  XYLayout xYLayout = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();
  TitledBorder titledBorder2;
  //JraCheckBox
  JraCheckBox jcbSTATKNJ = new JraCheckBox();
  JraCheckBox jcbSTATPLA = new JraCheckBox();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraCheckBox jcbSTATURA = new JraCheckBox();

  JraTextField jtfBRNAL = new JraTextField();


  public raStatus() {
      try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

      ////// STATUSI
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jpStatus, BorderLayout.CENTER);

    jpStatus.setLayout(xYLayout4);
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Status");
    jpStatus.setBorder(titledBorder2);


    jcbAKTIV.setText("Aktivan");
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(dm.getDoki());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jpStatus.add(jcbAKTIV, new XYConstraints(5, 0, 80, -1));

    jcbSTATPLA.setText("Pla\u0107eno");
    jcbSTATPLA.setColumnName("STATPLA");
    jcbSTATPLA.setDataSet(dm.getDoki());
    jcbSTATPLA.setSelectedDataValue("D");
    jcbSTATPLA.setUnselectedDataValue("N");
    jcbSTATPLA.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbSTATPLA.setHorizontalAlignment(SwingConstants.RIGHT);
    jpStatus.add(jcbSTATPLA, new XYConstraints(5, 25, 80, -1));

    jcbSTATURA.setText("Prijenos");
    jcbSTATURA.setColumnName("STATURA");
    jcbSTATURA.setDataSet(dm.getDoki());
    jcbSTATURA.setSelectedDataValue("D");
    jcbSTATURA.setUnselectedDataValue("N");
    jcbSTATURA.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbSTATURA.setHorizontalAlignment(SwingConstants.RIGHT);
    jpStatus.add(jcbSTATURA, new XYConstraints(5, 50, 80, -1));

    jcbSTATKNJ.setText("Knjiženo");
    jcbSTATKNJ.setColumnName("STATKNJ");
    jcbSTATKNJ.setDataSet(dm.getDoki());
    jcbSTATKNJ.setSelectedDataValue("D");
    jcbSTATKNJ.setUnselectedDataValue("N");
    jcbSTATKNJ.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbSTATKNJ.setHorizontalAlignment(SwingConstants.RIGHT);
    jpStatus.add(jcbSTATKNJ, new XYConstraints(5, 75, 80, -1));

    jtfBRNAL.setColumnName("BRNAL");
    jtfBRNAL.setDataSet(dm.getDoki());
    jtfBRNAL.setEditable(false);
    jpStatus.add(jtfBRNAL, new XYConstraints(5, 100, 80, -1));

  }
  public void go(com.borland.dx.dataset.DataSet ds,int iks,int ipsilon){
    jcbAKTIV.setDataSet(ds);
    jcbSTATPLA.setDataSet(ds);
    jcbSTATURA.setDataSet(ds);
    jcbSTATKNJ.setDataSet(ds);
    jtfBRNAL.setDataSet(ds);
    this.setLocation(iks,ipsilon);
    this.pack();
    hr.restart.util.raCommonClass.getraCommonClass().EnabDisabAll(jpStatus,false);
    this.show();
  }


}