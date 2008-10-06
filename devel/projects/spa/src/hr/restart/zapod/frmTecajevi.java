/****license*****************************************************************
**   file: frmTecajevi.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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



public class frmTecajevi extends raMatPodaci {

//sysoutTEST ST = new sysoutTEST(false);

  private static frmTecajevi ftecajevi;

  JPanel jselp = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  Valid Vl = Valid.getValid();

  PreSelect psFrm = new PreSelect() {

    public void SetFokus() {

      jtDATUMVAL.requestFocus();

    }

    public boolean Validacija() {

      return !Vl.isEmpty(jtDATUMVAL);

    }

  };

  hr.restart.baza.dM dm;

  JlrNavField jtDATUMVAL = new JlrNavField()

  {

    public void keyF9Pressed()

    {

      getDatVal();

    }

  };

  //-> ubacen NavField umjesto raTextFields zbog dohvata na F9 kod predselekcije

//  JraTextField jtDATUMVAL = new JraTextField();

  JLabel jlDatum = new JLabel();

  JPanel jp = new JPanel();

  XYLayout xYLayout2 = new XYLayout();

  JLabel jlTECKUP = new JLabel();

  JraTextField jtTECKUP = new JraTextField();

  JraTextField jtTECSRED = new JraTextField();

  JLabel jlTECSRED = new JLabel();

  JraTextField jtTECPROD = new JraTextField();

  JraCheckBox jcbAktiv = new JraCheckBox();



  JLabel jlTECPROD = new JLabel();

  JLabel jlDatumtec = new JLabel();

  JraLabel jlDATVAL = new JraLabel();

//  JraButton jBpreds = new JraButton();





  jpGetValute jpgetval = new jpGetValute();

  JraButton jBgetDATVAL = new JraButton();



  raNavAction rnvDatum = new raNavAction("Datum",raImages.IMGSTAV,KeyEvent.VK_F6) {

    public void actionPerformed(ActionEvent e) {

      showDatum();

    }

  };



  public frmTecajevi() {

    super(2);

    try {

      jbInit();

      ftecajevi = this;

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

//TEST

//setSaveChanges(false);

/*

System.out.println("frmTecajevi: ");

this.disableKey(KeyEvent.VK_F10);

this.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F7) {

      public void keyAction() {

        System.out.println("Stisnuo si F7 i jako ga boli");

      }

    });

    this.setSaveChanges(false);

*/

//ENDTEST

//    jBpreds.setText("Datum");

//    jBpreds.setToolTipText(jBpreds.getText());

    this.addOption(rnvDatum,3,false);

//    AddButton(jBpreds,3,false);

    dm = hr.restart.baza.dM.getDataModule();



    this.setRaQueryDataSet(dm.getAllTecajevi());

    this.setVisibleCols(new int[] {0,2,3,4});



    jtDATUMVAL.setHorizontalAlignment(SwingConstants.CENTER);

    jtDATUMVAL.setColumnName("DATVAL");

    jtDATUMVAL.setDataSet(dm.getAllTecajevi());
    jtDATUMVAL.setSearchMode(2);
    



    jselp.setLayout(xYLayout1);

    jlDatum.setText("Datum te\u010Dajne liste");

    xYLayout1.setWidth(297);

    xYLayout1.setHeight(60);

    jp.setLayout(xYLayout2);



    jcbAktiv.setColumnName("AKTIV");

    jcbAktiv.setDataSet(getRaQueryDataSet());

    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);

    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbAktiv.setSelectedDataValue("D");

    jcbAktiv.setText("Aktivan");

    jcbAktiv.setUnselectedDataValue("N");



    jlTECKUP.setText("Kupovni");

    jtTECKUP.setColumnName("TECKUP");

    jtTECKUP.setDataSet(getRaQueryDataSet());

    jtTECSRED.setDataSet(getRaQueryDataSet());

    jtTECSRED.setColumnName("TECSRED");

    jlTECSRED.setText("Srednji");

    jtTECPROD.setColumnName("TECPROD");

    jtTECPROD.setDataSet(getRaQueryDataSet());

    jlTECPROD.setText("Prodajni");

    jlDatumtec.setText("DATUM");

    jlDATVAL.setColumnName("DATVAL");

    jlDATVAL.setDataSet(getRaQueryDataSet());

    jlDATVAL.setFont(jlDATVAL.getFont().deriveFont(java.awt.Font.BOLD));

//    jBpreds.addActionListener(new java.awt.event.ActionListener() {

//      public void actionPerformed(ActionEvent e) {

//        jBpreds_actionPerformed(e);

//      }

//    });

    xYLayout2.setWidth(516);

    xYLayout2.setHeight(183);

    jpgetval.setAlwaysSelected(true);

    jpgetval.setRaDataSet(this.getRaQueryDataSet());

//    jpgetval.setDoGetTecaj(false);

    jpgetval.setBorderVisible(false);

    jBgetDATVAL.setText("...");

    jBgetDATVAL.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jBgetDATVAL_actionPerformed(e);

      }

    });

    jselp.add(jtDATUMVAL, new XYConstraints(150, 20, 100, -1));

    jselp.add(jlDatum, new XYConstraints(15, 20, -1, -1));

    jselp.add(jBgetDATVAL,    new XYConstraints(255, 20, 21, 21));

    jp.add(jlTECKUP, new XYConstraints(15, 95, -1, -1));

    jp.add(jtTECKUP, new XYConstraints(150, 95, 150, -1));

    jp.add(jlTECSRED, new XYConstraints(15, 120, -1, -1));

    jp.add(jtTECSRED, new XYConstraints(150, 120, 150, -1));

    jp.add(jlTECPROD, new XYConstraints(15, 145, -1, -1));

    jp.add(jtTECPROD, new XYConstraints(150, 145, 150, -1));

    jp.add(jlDatumtec, new XYConstraints(15, 20, -1, -1));

    jp.add(jlDATVAL, new XYConstraints(150, 20, -1, -1));

    jp.add(jpgetval, new XYConstraints(0, 39, -1, -1));

    jp.add(jcbAktiv,       new XYConstraints(403, 34, 70, -1));

    psFrm.setSelDataSet(dm.getAllTecajevi());

    psFrm.setSelPanel(jselp);

    this.setRaDetailPanel(jp);

    jtTECPROD.setNextFocusableComponent(jcbAktiv);

    jcbAktiv.setNextFocusableComponent(jpgetval);

    jpgetval.setNextFocusableComponent(jtTECKUP);
    setSoftDataLockEnabled(false);
//    this.setSaveChanges(false);

  }

  public static frmTecajevi getFrmTecajevi() {

    if (ftecajevi==null) ftecajevi= new frmTecajevi();



    return ftecajevi;

  }

  public void SetFokus(char mode) {

    if (mode=='N') {

      psFrm.copySelValues();

    } else if (mode=='I') {

      jtTECKUP.requestFocus();

    }

    jpgetval.initJP(mode);

  }

  public boolean Validacija(char mode) {

    if (mode=='N') {

      if (Vl.notUnique(new javax.swing.text.JTextComponent[] {jpgetval.jtOZNVAL,jtDATUMVAL})) return false;

    } else {

    }

    return true;

  }



//  void jBpreds_actionPerformed(ActionEvent e) {

//    psFrm.showPreselect(this,"Datum te\u010Daja");

//    if (this.isShowing()) this.getJpTableView().fireTableDataChanged();

//  }





  void jBgetDATVAL_actionPerformed(ActionEvent e) {

    getDatVal();

  }



  public void getDatVal() {

    com.borland.dx.sql.dataset.QueryDataSet dsDatVal = new com.borland.dx.sql.dataset.QueryDataSet();

    dsDatVal.setQuery(

      new com.borland.dx.sql.dataset.QueryDescriptor(

          dm.getDatabase1(),"SELECT distinct datval as DATVAL from tecajevi",true

        )

    );

    com.borland.dx.dataset.Column colDATVAL =

      (com.borland.dx.dataset.Column)dm.getTecajevi().getColumn("DATVAL").clone();

    colDATVAL.setAlignment(com.borland.dx.text.Alignment.CENTER);

    dsDatVal.setColumns(new com.borland.dx.dataset.Column[] {colDATVAL});

    String[] rValues = lookupData.getlookupData().lookUp(

                          psFrm.getPreSelDialog(),

                          dsDatVal,

                          new String[] {"DATVAL"},

                          new String[] {""},

                          new int[] {0}

                       );

    if (rValues == null) return;

    String rVal = rValues[0];

    com.borland.dx.dataset.Variant vrnt = new com.borland.dx.dataset.Variant();

    com.borland.dx.text.VariantFormatter varFormatter;

    varFormatter=jtDATUMVAL.getDataSet().getColumn(jtDATUMVAL.getColumnName()).getFormatter();

    try {

      varFormatter.parse(rVal,vrnt);

      psFrm.getSelRow().setVariant("DATVAL",vrnt);

    }

    catch (Exception ex) {

      System.out.println("getDatVal ex="+ex);

    }

  }



  public void showDatum()

  {

    psFrm.showPreselect(this,"Datum te\u010Daja");

    if (this.isShowing()) this.getJpTableView().fireTableDataChanged();

  }

}