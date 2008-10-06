/****license*****************************************************************
**   file: frmRabati.java
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

import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmRabati extends raSifraNaziv {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
//  JraTextField jtfIRAB = new JraTextField();
//  JLabel jlIRAB = new JLabel();
  JraTextField jtfPRAB = new JraTextField();
  JraCheckBox jcbRABNARAB = new JraCheckBox();
  JLabel jlPRAB = new JLabel();
  hr.restart.baza.dM dm;

  public frmRabati() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jlPRAB.setText(res.getString("jlPRAB_text"));
    jcbRABNARAB.setUnselectedDataValue("N");
    jcbRABNARAB.setSelectedDataValue("D");
    jcbRABNARAB.setDataSet(dm.getRabati());
    jcbRABNARAB.setColumnName("RABNARAB");
    jcbRABNARAB.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbRABNARAB.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbRABNARAB.setText(res.getString("jcbRABNARAB_text"));
    jcbRABNARAB.addItemListener(new ItemListener(){

		public void itemStateChanged(ItemEvent e) {
			JraCheckBox source = (JraCheckBox)e.getSource();
			if (source.getDataSet() == null) return;
			if (source.isSelected()){
				source.getDataSet().setString(source.getColumnName(),"D");
			} else {
				source.getDataSet().setString(source.getColumnName(),"N");				
			}
		}});
    
    jtfPRAB.setDataSet(dm.getRabati());
    jtfPRAB.setColumnName("PRAB");
//    jlIRAB.setText(res.getString("jlIRAB_text"));
//    jtfIRAB.setDataSet(dm.getRabati());
//    jtfIRAB.setColumnName("IRAB");
    jp.setLayout(xYLayout1);
    this.setRaDataSet(dm.getRabati());
    this.setRaColumnSifra("CRAB");
    this.setRaColumnNaziv("NRAB");
    this.setRaText("Rabat");
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(41);

    jp.add(jlPRAB, new XYConstraints(15, 2, -1, -1));
    jp.add(jtfPRAB, new XYConstraints(150, 0, 100, -1));
    jp.add(jcbRABNARAB, new XYConstraints(355, 0, 80, -1));
    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);
    raDataIntegrity.installFor(this);
    this.setVisibleCols(new int[] {0,1,2,3});
    
  }
  public boolean DeleteCheck() {
    return true;
  }
  public void SetFokus2(char mode) {
  	if (mode=='N') getRaDataSet().setString("RABNARAB","N");
  }
}