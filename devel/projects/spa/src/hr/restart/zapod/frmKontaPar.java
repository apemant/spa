/****license*****************************************************************
**   file: frmKontaPar.java
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
package hr.restart.zapod;



import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**

 * <p>Title: Robno poslovanje</p>

 * <p>Description: </p>

 * <p>Copyright: Copyright (c) 2000</p>

 * <p>Company: REST-ART</p>

 * @author REST-ART development team

 * @version 1.0

 */



public class frmKontaPar extends raFrame {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");

  TitledBorder titledBorder1;

  TitledBorder titledBorder2;

  BorderLayout borderLayout1 = new BorderLayout();

  JraButton jbBROJKONTA4 = new JraButton();

  JraButton jbBROJKONTA3 = new JraButton();

  JraButton jbBROJKONTA2 = new JraButton();

  JraButton jbBROJKONTA1 = new JraButton();

  JPanel jPanel3 = new JPanel();

  JPanel jPanel2 = new JPanel();

  JPanel jPanel1 = new JPanel();

  JlrNavField jrfNAZIVKONTA4 = new JlrNavField();

  JlrNavField jrfNAZIVKONTA3 = new JlrNavField();

  JlrNavField jrfNAZIVKONTA2 = new JlrNavField();

  JlrNavField jrfNAZIVKONTA1 = new JlrNavField();

  JlrNavField jrfBROJKONTA4 = new JlrNavField();

  JlrNavField jrfBROJKONTA3 = new JlrNavField();

  JlrNavField jrfBROJKONTA2 = new JlrNavField();

  JlrNavField jrfBROJKONTA1 = new JlrNavField();

  JLabel jLabel4 = new JLabel();

  JLabel jLabel3 = new JLabel();

  XYLayout xYLayout3 = new XYLayout();

  JLabel jLabel2 = new JLabel();

  XYLayout xYLayout2 = new XYLayout();

  JLabel jLabel1 = new JLabel();

  XYLayout xYLayout1 = new XYLayout();

  OKpanel okp = new OKpanel() {

    public void jBOK_actionPerformed() {

      pressOK();

    }

    public void jPrekid_actionPerformed() {

      pressCancel();

    }

  };



  public frmKontaPar() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Konto dobavlja\u010Da");

    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Konto kupca");

    jPanel3.setLayout(xYLayout3);

    jPanel3.setBorder(titledBorder2);


//    jrfBROJKONTA1.setRaDataSet(dm.getKonta());

    jrfBROJKONTA1.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());

    jrfBROJKONTA1.setColumnName("BROJKONTA");

    jrfBROJKONTA1.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIVKONTA1});

    jrfBROJKONTA1.setVisCols(new int[]{0,1});

    jrfBROJKONTA1.setColNames(new String[] {"NAZIVKONTA"});

    jrfBROJKONTA1.setDataSet(dm.getKonta_par());

    jrfBROJKONTA1.setNavButton(jbBROJKONTA1);

    jrfNAZIVKONTA1.setNavProperties(jrfBROJKONTA1);

    jrfNAZIVKONTA1.setSearchMode(1);

    jrfNAZIVKONTA1.setColumnName("NAZIVKONTA");




//    jrfBROJKONTA2.setRaDataSet(dm.getKonta());

    jrfBROJKONTA2.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());

    jrfBROJKONTA2.setColumnName("BROJKONTA");

    jrfBROJKONTA2.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIVKONTA2});

    jrfBROJKONTA2.setVisCols(new int[]{0,1});

    jrfBROJKONTA2.setColNames(new String[] {"NAZIVKONTA"});

    jrfBROJKONTA2.setDataSet(dm.getKonta_par());

    jrfBROJKONTA2.setNavButton(jbBROJKONTA2);

    jrfNAZIVKONTA2.setNavProperties(jrfBROJKONTA2);

    jrfNAZIVKONTA2.setSearchMode(1);

    jrfNAZIVKONTA2.setColumnName("NAZIVKONTA");



//    jrfBROJKONTA3.setRaDataSet(dm.getKonta());

    jrfBROJKONTA3.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());

    jrfBROJKONTA3.setColumnName("BROJKONTA");

    jrfBROJKONTA3.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIVKONTA3});

    jrfBROJKONTA3.setVisCols(new int[]{0,1});

    jrfBROJKONTA3.setColNames(new String[] {"NAZIVKONTA"});

    jrfBROJKONTA3.setDataSet(dm.getKonta_par());

    jrfBROJKONTA3.setNavButton(jbBROJKONTA3);

    jrfNAZIVKONTA3.setNavProperties(jrfBROJKONTA3);

    jrfNAZIVKONTA3.setSearchMode(1);

    jrfNAZIVKONTA3.setColumnName("NAZIVKONTA");



//    jrfBROJKONTA4.setRaDataSet(dm.getKonta());

    jrfBROJKONTA4.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());

    jrfBROJKONTA4.setColumnName("BROJKONTA");

    jrfBROJKONTA4.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZIVKONTA4});

    jrfBROJKONTA4.setVisCols(new int[]{0,1});

    jrfBROJKONTA4.setColNames(new String[] {"NAZIVKONTA"});

    jrfBROJKONTA4.setDataSet(dm.getKonta_par());

    jrfBROJKONTA4.setNavButton(jbBROJKONTA4);

    jrfNAZIVKONTA4.setNavProperties(jrfBROJKONTA4);

    jrfNAZIVKONTA4.setSearchMode(1);

    jrfNAZIVKONTA4.setColumnName("NAZIVKONTA");


    jPanel2.setBorder(titledBorder1);

    jPanel2.setLayout(xYLayout2);

    jPanel1.setLayout(xYLayout1);

    jLabel4.setText("Tuzemni");

    jLabel3.setText("Inozemni");

    jLabel2.setText("Inozemni");

    jLabel1.setText("Tuzemni");

    xYLayout1.setWidth(620);

    xYLayout1.setHeight(230);

    jPanel3.add(jLabel3, new XYConstraints(5, 30, -1, -1));

    jPanel3.add(jLabel4, new XYConstraints(5, 5, -1, -1));

    jPanel3.add(jbBROJKONTA3, new XYConstraints(555, 5, 21, 21));

    jPanel3.add(jrfBROJKONTA3, new XYConstraints(100, 5, 100, -1));

    jPanel3.add(jrfNAZIVKONTA3, new XYConstraints(205, 5, 345, -1));

    jPanel3.add(jbBROJKONTA4, new XYConstraints(555, 30, 21, 21));

    jPanel3.add(jrfBROJKONTA4, new XYConstraints(100, 30, 100, -1));

    jPanel3.add(jrfNAZIVKONTA4, new XYConstraints(205, 30, 345, -1));

    jPanel1.add(jPanel2, new XYConstraints(10, 20, 595, 90));

    jPanel1.add(jPanel3, new XYConstraints(10, 120, 595, 90));

    jPanel2.add(jLabel1, new XYConstraints(5, 5, -1, -1));

    jPanel2.add(jLabel2, new XYConstraints(5, 30, -1, -1));

    jPanel2.add(jrfBROJKONTA1, new XYConstraints(100, 5, 100, -1));

    jPanel2.add(jrfNAZIVKONTA1, new XYConstraints(205, 5, 345, -1));

    jPanel2.add(jbBROJKONTA1, new XYConstraints(555, 5, 21, 21));

    jPanel2.add(jbBROJKONTA2, new XYConstraints(555, 30, 21, 21));

    jPanel2.add(jrfBROJKONTA2, new XYConstraints(100, 30, 100, -1));

    jPanel2.add(jrfNAZIVKONTA2, new XYConstraints(205, 30, 345, -1));

    this.getContentPane().add(jPanel1, BorderLayout.CENTER);

//    raMatPodaci.addCentered(jPanel1,getContentPane());

    this.getContentPane().add(okp, BorderLayout.SOUTH);

    okp.registerOKPanelKeys(this);

  }



  void pressOK() {

    dm.getKonta_par().setString("DOB_TUZ", jrfBROJKONTA1.getText());

    dm.getKonta_par().setString("DOB_INO", jrfBROJKONTA2.getText());

    dm.getKonta_par().setString("KUP_TUZ", jrfBROJKONTA3.getText());

    dm.getKonta_par().setString("KUP_INO", jrfBROJKONTA4.getText());

    dm.getKonta_par().post();

    dm.getKonta_par().saveChanges();

    this.hide();

  }

  void pressCancel() {

    this.hide();

  }

  public void show() {

    if (dm.getKonta_par().rowCount()==0) {

      dm.getKonta_par().insertRow(true);

    }

    jrfBROJKONTA1.setText(dm.getKonta_par().getString("DOB_TUZ"));

    jrfBROJKONTA2.setText(dm.getKonta_par().getString("DOB_INO"));

    jrfBROJKONTA3.setText(dm.getKonta_par().getString("KUP_TUZ"));

    jrfBROJKONTA4.setText(dm.getKonta_par().getString("KUP_INO"));

    jrfBROJKONTA1.forceFocLost();

    jrfBROJKONTA2.forceFocLost();

    jrfBROJKONTA3.forceFocLost();

    jrfBROJKONTA4.forceFocLost();

    super.show();

  }

}