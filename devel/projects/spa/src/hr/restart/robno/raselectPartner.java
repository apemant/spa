/****license*****************************************************************
**   file: raselectPartner.java
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

import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.raTwoTableChooser;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
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

public class raselectPartner extends JraDialog {


  private JPanel panel1 = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private sysoutTEST ST = new sysoutTEST(false);
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  private hr.restart.util.raCommonClass rcc  =  hr.restart.util.raCommonClass.getraCommonClass();
  private QueryDataSet LijeviSet = null;

  private JPanel panelDetail = new JPanel();
  private JLabel lIznos = new JLabel("Iznos bez poreza");
  private JraTextField tfIznos  = new JraTextField();
  private JLabel lTextstav = new JLabel("Tekst na stavci");
  private hr.restart.swing.JraTextArea taTextstav = new hr.restart.swing.JraTextArea();
  private hr.restart.swing.JraButton bpromsve = new hr.restart.swing.JraButton();
  private QueryDataSet DesniSet = null;
  private raTwoTableChooser rTTC = new raTwoTableChooser(this){
    public void actionLtoR(){
      super.actionLtoR();
//      if (DesniSet == null)
    }
    public void actionLtoR_all(){
      super.actionLtoR_all();
    }
    public void actionRtoL(){
      super.actionRtoL();
    }
    public void actionRtoL_all(){
      super.actionRtoL_all();
    }
  };

  private void initPanelDetail() throws Exception {

    XYLayout XYLayout1 = new XYLayout();
    XYLayout1.setWidth(42);
    XYLayout1.setHeight(80);

    panelDetail.setLayout(XYLayout1);
    bpromsve.setText("Promijeni svima");
    bpromsve.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        bpromsve_actionPerformed(e);
      }
    });
    tfIznos.setColumnName("IZNOS");
    taTextstav.setColumnName("TEXTSTAV");
    taTextstav.setBorder(new JTextField().getBorder());
    panelDetail.add(lIznos,    new XYConstraints(15,  55, -1, -1));
    panelDetail.add(tfIznos,   new XYConstraints(150, 55, 100, -1));
    panelDetail.add(bpromsve,  new XYConstraints(255, 55, 150, 21));
    panelDetail.add(lTextstav, new XYConstraints(15,  10, -1, -1));
    panelDetail.add(taTextstav, new XYConstraints(150, 10, 630, 40));
  }

  private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void  jBOK_actionPerformed(){
      presOK();
    }
    public void  jPrekid_actionPerformed(){
      presCancel();
    }
  };

  public raselectPartner(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      initTables();
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  public raselectPartner() {
    this(null, "Odabir partnera za više ra\u010Duna", false);
  }

  public void onoffDetail(boolean istina) {}

  public void componentShow(){
    if (DesniSet == null) {
      LijeviSet = null;
      initSets();
      initTables();
//      rcc.EnabDisabAll(this.panelDetail,false);
    }
  }

  public void initSets() {

    Column iznos = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
    iznos.setCaption("Iznos");
    iznos.setColumnName("IZNOS");
//    iznos.setDataType(dm.getStdoki().getColumn("IPRODBP").getDataType());
//    iznos.setPrecision(dm.getStdoki().getColumn("IPRODBP").getPrecision());
//    iznos.setDisplayMask(dm.getStdoki().getColumn("IPRODBP").getDisplayMask());

    Column dosp = dm.getDoki().getColumn("DDOSP").cloneColumn();
    dosp.setCaption("Rbr");
    dosp.setColumnName("DOSP");
//    dosp.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    dosp.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    Column textstav = new Column();
    textstav.setCaption("Tekst na stavci ra\u010Duna");
    textstav.setColumnName("TEXTSTAV");
    textstav.setDataType(dm.getVTText().getColumn("TEXTFAK").getDataType());

    LijeviSet = new QueryDataSet();
    LijeviSet.setColumns(new Column[] {(Column) dm.getPartneri().getColumn("CPAR").clone(),
                                       (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
                                       iznos,textstav,dosp});
    LijeviSet = hr.restart.util.Util.getNewQueryDataSet("select cpar, nazpar, 0.0 as iznos, '' as textstav, dosp from partneri",false);

    DesniSet = new QueryDataSet();
    DesniSet.setColumns(new Column[] { (Column) dm.getPartneri().getColumn("CPAR").clone(),
                                       (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
                                       (Column) iznos.clone(),(Column) textstav.clone(),
                                       (Column) dosp.clone()});
    LijeviSet.open();
    DesniSet.open();
  }
  private void initTables(){
    LijeviSet.setTableName("sel-cpar");
    DesniSet.setTableName("sel-cpar");
    rTTC.setLeftDataSet(LijeviSet);
    rTTC.setRightDataSet(DesniSet);
    rTTC.rnvSave.setVisible(false);
    rTTC.initialize();
    tfIznos.setDataSet(DesniSet);
    taTextstav.setDataSet(DesniSet);
  }

  private void jbInit() throws Exception {

    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        componentShow();
      }
    });

//    LijeviSet.open();
//    DesniSet.open();

    panel1.setLayout(borderLayout1);
    getContentPane().add(panel1);
    initPanelDetail();
    panel1.add(rTTC,BorderLayout.NORTH);
    panel1.add(panelDetail,BorderLayout.CENTER);
    panel1.add(okp,BorderLayout.SOUTH);
  }

  public void presOK(){
    this.hide();
  }

  public void presCancel(){
    DesniSet = null;
    this.hide();
  }
  public QueryDataSet getDesniSet(){
    return DesniSet;
  }
  public void ocisti() {
    DesniSet = null;
  }

  void bpromsve_actionPerformed(ActionEvent e) {
    DesniSet.enableDataSetEvents(false);
    int gdje = DesniSet.getRow();
    String Text = taTextstav.getText();
    java.math.BigDecimal iznos = DesniSet.getBigDecimal("IZNOS");
    DesniSet.first();
    do{
      DesniSet.setString("TEXTSTAV",Text);
      DesniSet.setBigDecimal("IZNOS",iznos);
    }while (DesniSet.next());
    DesniSet.goToRow(gdje);
    DesniSet.enableDataSetEvents(true);
  }
}