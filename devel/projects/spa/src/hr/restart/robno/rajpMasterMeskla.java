/****license*****************************************************************
**   file: rajpMasterMeskla.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class rajpMasterMeskla extends JPanel {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  public TableDataSet UlazSkl = new com.borland.dx.dataset.TableDataSet();
  public TableDataSet IzlazSkl = new com.borland.dx.dataset.TableDataSet();
  private Column CSKLU = new Column("CSKL","CSKL",Variant.STRING);
  private Column CSKLI = new Column("CSKL","CSKL",Variant.STRING);
  hr.restart.util.raCommonClass cc = hr.restart.util.raCommonClass.getraCommonClass();
  private char mode ;
  DataSet dsMaster ;

  public void setMode(char mod) {
    mode=mod;
  }

  rajpBrDok rajpBrDok = new rajpBrDok();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanelCentral = new JPanel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel("Opis");
  JLabel jLabel6 = new JLabel("Partner");

  JlrNavField jtfCSKLIZ = new JlrNavField();
  JlrNavField jtfNAZIZ = new JlrNavField();
  JlrNavField jtfCSKLUL = new JlrNavField();
  JlrNavField jtfNAZUL = new JlrNavField();
  
  JlrNavField jtfCPAR = new JlrNavField();
  JlrNavField jtfNAZPAR = new JlrNavField();

  private JraTextField jtfOPIS = new JraTextField();
  JraTextField jtfDATDOK = new JraTextField();
  XYLayout xYLayout2 = new XYLayout();
  jpGetValute jpgetval = new hr.restart.zapod.jpGetValute();

  public void disabCskl(){
    cc.setLabelLaF(jtfNAZIZ,false);
    cc.setLabelLaF(jtfCSKLIZ,false);
    cc.setLabelLaF(jtfNAZUL,false);
    cc.setLabelLaF(jtfCSKLUL,false);
  }

  public rajpMasterMeskla() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

    rajpBrDok.addBorder();
    this.setLayout(borderLayout1);
    jPanelCentral.setLayout(xYLayout2);
    jLabel2.setText("Datum dokumenta");
    jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATDOK.setColumnName("DATDOK");
    jtfOPIS.setColumnName("OPIS");

//////////////////////////
    jtfCSKLIZ.setColumnName("CSKLIZ");
    jtfCSKLIZ.setNavColumnName("CSKL");
    jtfCSKLIZ.setVisCols(new int[]{0,1,2});
    jtfCSKLIZ.setColNames(new String[] {"NAZSKL"});

    jtfCSKLIZ.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jtfCSKLIZ.setTextFields(new javax.swing.text.JTextComponent[] {jtfNAZIZ});
    jtfNAZIZ.setColumnName("NAZSKL");
    jtfNAZIZ.setSearchMode(1);
    jtfNAZIZ.setNavProperties(jtfCSKLIZ);

    jtfCSKLUL.setColumnName("CSKLUL");
    jtfCSKLUL.setNavColumnName("CSKL");
    jtfCSKLUL.setVisCols(new int[]{0,1,2});
    jtfCSKLUL.setColNames(new String[] {"NAZSKL"});

    jtfCSKLUL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jtfCSKLUL.setTextFields(new javax.swing.text.JTextComponent[] {jtfNAZUL});
    jtfNAZUL.setColumnName("NAZSKL");
    jtfNAZUL.setSearchMode(1);
    jtfNAZUL.setNavProperties(jtfCSKLUL);
    
    jtfCPAR.setColumnName("CPAR");
    jtfCPAR.setVisCols(new int[]{0,1,2});
    jtfCPAR.setColNames(new String[] {"NAZPAR"});
    jtfCPAR.setRaDataSet(dm.getPartneri());
    jtfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jtfNAZPAR});
    jtfNAZPAR.setColumnName("NAZPAR");
    jtfNAZPAR.setSearchMode(1);
    jtfNAZPAR.setNavProperties(jtfCPAR);
    
    //////////////////////////

    this.setPreferredSize(new Dimension(647,250));
    jpgetval.setTecajVisible(true);

//    jpgetval.setDoGetTecaj(true);

    jPanelCentral.setBorder(BorderFactory.createEtchedBorder());
    this.add(rajpBrDok, BorderLayout.NORTH);
    this.add(jPanelCentral, BorderLayout.WEST);
    jLabel3.setText("Izlazno skladište");
    jLabel4.setText("Ulazno skladište");
    jPanelCentral.add(jLabel3,new XYConstraints(15, 15, -1, -1));
    jPanelCentral.add(jtfCSKLIZ,new XYConstraints(150, 15, 100, -1));
    jPanelCentral.add(jtfNAZIZ,new XYConstraints(260, 15, 345, -1));
    jPanelCentral.add(jLabel4,new XYConstraints(15, 45, -1, -1));
    jPanelCentral.add(jtfCSKLUL,new XYConstraints(150, 45, 100, -1));
    jPanelCentral.add(jtfNAZUL,new XYConstraints(260, 45, 345, -1));
    jPanelCentral.add(jLabel2, new XYConstraints(15, 75, -1, -1));
    jPanelCentral.add(jtfDATDOK, new XYConstraints(150, 75, 100, -1));
    jPanelCentral.add(jLabel5, new XYConstraints(15,105, -1, -1));
    jPanelCentral.add(jtfOPIS,new XYConstraints(150,105,455,-1));
    jPanelCentral.add(jLabel6,new XYConstraints(15, 135, -1, -1));
    jPanelCentral.add(jtfCPAR,new XYConstraints(150, 135, 100, -1));
    jPanelCentral.add(jtfNAZPAR,new XYConstraints(260, 135, 345, -1));
    jPanelCentral.add(jpgetval, new XYConstraints(0, 160, 0, 0));
  }

/*  public void MYafterGet_Val(){
    if(mode =='N' && !jtfDATDOK.getText().equals("")) {
      dsMaster.setBigDecimal("TECAJ",jpgetval.getVarTecaj());
    }
  }*/

  public void BindComp(DataSet ds) {
    dsMaster = ds;
    rajpBrDok.setDataSet(ds);
    jtfDATDOK.setDataSet(ds);
    jtfCSKLIZ.setDataSet(ds);
    jtfCSKLUL.setDataSet(ds);
    jpgetval.setRaDataSet(ds);
    jtfOPIS.setDataSet(ds);
    jtfCPAR.setDataSet(ds);
  }
}