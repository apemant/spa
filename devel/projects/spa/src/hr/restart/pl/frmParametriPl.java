/****license*****************************************************************
**   file: frmParametriPl.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raMatPodaci;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
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

public class frmParametriPl extends raFrame{
  JraScrollPane jScroll = new JraScrollPane();
  OKpanel okp = new OKpanel()
  {
    public void jBOK_actionPerformed()
    {
      OKPress();
    }
    public void jPrekid_actionPerformed()
    {
      cancelPress();
    }
  };
  JPanel jPanel = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  BorderLayout BorderLayout1 = new BorderLayout();

  JraTextField jraMinosndop = new JraTextField();
  JraTextField jraMinpl = new JraTextField();
  JPanel jPanel1 = new JPanel();
  JLabel jlOsn3 = new JLabel();
  JLabel jlOsn2 = new JLabel();
  JLabel jlOsn1 = new JLabel();
  JraTextField jraOsnpor3 = new JraTextField();
  JraTextField jraOsnpor2 = new JraTextField();
  JraTextField jraOsnpor1 = new JraTextField();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlMinPl = new JLabel();
  JLabel jlMinOsnDop = new JLabel();
  Border border1;
  TitledBorder titledBorder1;
  dM dm = dM.getDataModule();
  Column plOlak = new Column();


  public frmParametriPl() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Osnovice");
    jPanel.setLayout(xYLayout1);
    xYLayout1.setWidth(500);
    xYLayout1.setHeight(180);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(xYLayout2);
    jlOsn3.setText("Tre\u0107i porez");
    jlOsn2.setText("Drugi porez");
    jlOsn1.setText("Prvi porez");
    jlMinPl.setText("Min. pla\u0107a - olakšice");
    jlMinOsnDop.setText("Min. osn. za doprinose");
    this.getContentPane().add(okp, BorderLayout1.SOUTH);
    this.getContentPane().add(jScroll, BorderLayout1.CENTER);
    raMatPodaci.addCentered(jPanel, jScroll.getViewport());



    jPanel.add(jraMinpl,  new XYConstraints(150, 20, 100, -1));
    jPanel.add(jraMinosndop,     new XYConstraints(150, 45, 100, -1));
    jPanel1.add(jraOsnpor2,    new XYConstraints(340, 5, 100, -1));
    jPanel1.add(jraOsnpor3,      new XYConstraints(130, 30, 100, -1));
    jPanel1.add(jraOsnpor1,      new XYConstraints(130, 5, 100, -1));
    jPanel1.add(jlOsn3,   new XYConstraints(15, 30, -1, -1));
    jPanel1.add(jlOsn1,    new XYConstraints(15, 5, -1, -1));
    jPanel1.add(jlOsn2,   new XYConstraints(265, 5, -1, -1));
    jPanel.add(jlMinPl,   new XYConstraints(16, 23, -1, -1));
    jPanel.add(jlMinOsnDop,   new XYConstraints(16, 49, -1, -1));
    jPanel.add(jPanel1,      new XYConstraints(15, 74, 467, 89));
    BindComp();
  }

  public void cancelPress()
  {
    hide();
  }

  public void OKPress()
  {
    dm.getParametripl().saveChanges();
    hide();
  }

  public void BindComp()
  {
    jraMinpl.setDataSet(dm.getParametripl());
    jraMinpl.setColumnName("MINPL");
    jraMinosndop.setDataSet(dm.getParametripl());
    jraMinosndop.setColumnName("MINOSDOP");

    jraOsnpor1.setDataSet(dm.getParametripl());
    jraOsnpor1.setColumnName("OSNPOR1");
    jraOsnpor2.setDataSet(dm.getParametripl());
    jraOsnpor2.setColumnName("OSNPOR2");
    jraOsnpor3.setDataSet(dm.getParametripl());
    jraOsnpor3.setColumnName("OSNPOR3");
  }

  public void show()
  {
    if(dm.getParametripl().rowCount()==0)
      dm.getParametripl().insertRow(false);
    super.show();
  }
}