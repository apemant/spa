/****license*****************************************************************
**   file: frmBrisanje.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

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

public class frmBrisanje extends raUpitLite {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JraCheckBox jcbOS = new JraCheckBox();
  JraCheckBox jcbSI = new JraCheckBox();
  JraCheckBox jcbPrometi = new JraCheckBox();
  JraCheckBox jcbMatPod = new JraCheckBox();
  JraButton jbCOrg = new JraButton();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();

  Border border1;
  TitledBorder titledBorder1;

  JlrNavField jrfCOrgNaz = new JlrNavField();
  JlrNavField jrfCOrg = new JlrNavField(){
    public void after_lookUp()
    {
    }
  };

  public frmBrisanje() {
    try {
      this.nextStep();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Knjigovodstvo");
    jPanel1.setLayout(borderLayout1);
    jPanel2.setLayout(xYLayout1);
    xYLayout1.setWidth(480);
    xYLayout1.setHeight(300);
    jrfCOrg.setNavButton(this.jbCOrg);
    jrfCOrg.setRaDataSet(dm.getKnjig());
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setSearchMode(0);
    jPanel3.setLayout(xYLayout4);
    jbCOrg.setText("...");
    jrfCOrgNaz.setColumnName("NAZIV");
    jrfCOrgNaz.setNavProperties(jrfCOrg);
    jPanel3.setBorder(titledBorder1);
    jcbOS.setText("Osnovna sredstva");
    jcbSI.setActionCommand("jrbSI");
    jcbSI.setText("Sitni inventar");
    jcbPrometi.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPrometi.setText("Prometi");
    jcbMatPod.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbMatPod.setText("Matièni podaci");
    jPanel1.setMinimumSize(new Dimension(480, 180));
    jPanel1.setPreferredSize(new Dimension(480, 180));
    jPanel1.add(jPanel2, BorderLayout.EAST);
    jPanel2.add(jPanel3,    new XYConstraints(15, 20, 450, 70));
    jPanel3.add(jrfCOrg, new XYConstraints(15, 7, 100, -1));
    jPanel3.add(jrfCOrgNaz, new XYConstraints(120, 7, 275, -1));
    jPanel3.add(jbCOrg, new XYConstraints(401, 7, 21, 21));
    jPanel2.add(jcbOS,      new XYConstraints(37, 95, -1, -1));
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel2.add(jcbSI,  new XYConstraints(37, 120, -1, -1));
    jPanel2.add(jcbPrometi,     new XYConstraints(380, 95, -1, -1));
    jPanel2.add(jcbMatPod,     new XYConstraints(345, 120, -1, -1));
  }

  public void pressCancel()
  {
    this.hide();
  }

  public void okPress()
  {
      this.getRepRunner().clearAllReports();
      rdOSUtil.getUtil().deleteOS(jrfCOrg.getText(), jcbOS.isSelected(), jcbSI.isSelected(),
                                  jcbPrometi.isSelected(), jcbMatPod.isSelected());
      setDefaultValues();
      this.cancelPress();
  }

  private void setDefaultValues()
  {
    jcbOS.setSelected(false);
    jcbSI.setSelected(false);
    jcbPrometi.setSelected(false);
    jcbMatPod.setSelected(false);

    jrfCOrg.setText("");
    jrfCOrg.forceFocLost();
  }

  public void firstESC(){
  };
  public void componentShow(){
    jcbOS.setSelected(false);
    jcbSI.setSelected(false);
    jcbPrometi.setSelected(false);
    jcbMatPod.setSelected(false);
    jrfCOrg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
    jrfCOrg.forceFocLost();
    rcc.setLabelLaF(jrfCOrg, false);
    rcc.setLabelLaF(jrfCOrgNaz, false);
    rcc.setLabelLaF(jbCOrg, false);
    jcbOS.requestFocus();
  };
  public boolean runFirstESC(){
    return false;
  }

  public boolean isIspis()
  {
    return false;
  }
}