/****license*****************************************************************
**   file: raArtikl.java
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

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class raArtikl extends JPanel {
  private JComponent nextFocusabile;
  private boolean lUsluga=false;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  JPanel jpNorth = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout1 = new XYLayout();

  JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      System.out.println("aftercart sa CART");
      afterCART();
    }
  };
  JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
       afterCART();
    }
  };
  JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
       afterCART();
    }
  };
  JlrNavField jrfCGRART = new JlrNavField() {
    public void after_lookUp() {
       afterCART();
    }
  };
  JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp() {
       afterCART();
    }
  };

  JlrNavField jrfCPOR = new JlrNavField();
  JlrNavField jrfISB = new JlrNavField();
  JlrNavField jrfJM = new JlrNavField();
  JlrNavField jrfVRART = new JlrNavField();

  JraButton jbCART = new JraButton();
  JLabel jlARTIKL = new JLabel();
  JLabel jlCART = new JLabel();
  JLabel jlCART1 = new JLabel();
  JLabel jlBC = new JLabel();
  JLabel jlCGRART = new JLabel();
  JLabel jlNAZART = new JLabel();

  public raArtikl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    jpNorth.setLayout(xYLayout1);

    jrfCART.setColumnName("CART");
    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART","CPOR","ISB","VRART"});
    jrfCART.setVisCols(new int[]{0,3,4});
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART,jrfCPOR,jrfISB,jrfVRART});
    jrfCART.setRaDataSet(dm.getArtikli());
    jrfCART.setNavButton(jbCART);
    jrfCART.setSearchMode(0);
//    jrfCART.setFocusLostOnShow(false);
    jrfCART1.setColumnName("CART1");
    jrfCART1.setNavProperties(jrfCART);
    jrfCART1.setSearchMode(1);
    jrfCART1.setFocusLostOnShow(false);
    jrfBC.setColumnName("BC");
    jrfBC.setNavProperties(jrfCART);
    jrfBC.setSearchMode(1);
    jrfBC.setFocusLostOnShow(false);
    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setNavProperties(jrfCART);
    jrfNAZART.setSearchMode(1);
    jrfNAZART.setFocusLostOnShow(false);
    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setNavProperties(jrfCART);
    jrfCGRART.setSearchMode(1);
    jrfCGRART.setFocusLostOnShow(false);

    jrfJM.setEditable(false);
    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(0);
    jrfJM.setFocusLostOnShow(false);
    jrfISB.setVisible(false);
    jrfISB.setColumnName("ISB");
    jrfISB.setSearchMode(-1);
    jrfISB.setFocusLostOnShow(false);
    jrfCPOR.setVisible(false);
    jrfCPOR.setColumnName("CPOR");
    jrfCPOR.setSearchMode(-1);
    jrfCPOR.setFocusLostOnShow(false);
    jrfVRART.setVisible(false);
    jrfVRART.setColumnName("VRART");
    jrfVRART.setSearchMode(-1);
    jrfVRART.setFocusLostOnShow(false);

    jlARTIKL.setText("Artikl");
    jlNAZART.setText("Naziv / Jm");
    jlCART.setText("Šifra");
    jlCART.setHorizontalAlignment(SwingConstants.LEFT);
    jlCART1.setText("Oznaka");
    jlCART1.setHorizontalAlignment(SwingConstants.LEFT);
    jlBC.setText("Barcode");
    jlBC.setHorizontalAlignment(SwingConstants.LEFT);
    jlCGRART.setText("Grupa");
    jlCGRART.setHorizontalAlignment(SwingConstants.LEFT);
    jbCART.setText("...");

    xYLayout1.setWidth(645);
    xYLayout1.setHeight(85);
    this.add(jpNorth,  BorderLayout.CENTER);
    jpNorth.add(jlARTIKL, new XYConstraints(15, 25, -1, -1));
    jpNorth.add(jlNAZART, new XYConstraints(15, 50, -1, -1));
    jpNorth.add(jlCART, new XYConstraints(150, 8, 60, -1));
    jpNorth.add(jlCART1, new XYConstraints(220, 8, 130, -1));
    jpNorth.add(jlBC, new XYConstraints(360, 8, 130, -1));
    jpNorth.add(jlCGRART, new XYConstraints(500, 8, 104, -1));
    jpNorth.add(jbCART, new XYConstraints(609, 25, 21, 21));
    jpNorth.add(jrfCART, new XYConstraints(150, 25, 65, -1));
    jpNorth.add(jrfCART1, new XYConstraints(220, 25, 135, -1));
    jpNorth.add(jrfBC, new XYConstraints(360, 25, 135, -1));
    jpNorth.add(jrfCGRART, new XYConstraints(500, 25, 104, -1));
    jpNorth.add(jrfNAZART,   new XYConstraints(150, 50, 410, -1));
    jpNorth.add(jrfJM,    new XYConstraints(564, 50, 40, -1));
  }
  private void afterCART() {
    System.out.println("afterCART");
    if (!jrfCART.getText().equals("")) {
      this.EnabDisab(false);
    }
    afterLookup();
  }
  public String getCART() {
    return jrfCART.getText();
  }
  public String getCART1() {
    return jrfCART1.getText();
  }
  public String getBC() {
    return jrfBC.getText();
  }
  public String getCGRART() {
    return jrfCGRART.getText();
  }
  public String getNAZART() {
    return jrfNAZART.getText();
  }
  public String getCPOR() {
    return jrfCPOR.getText();
  }
  public String getISB() {
    return jrfISB.getText();
  }
  public void setCART() {
    System.out.println("Setiram hebeni CART");
    jrfCART.setText("");
    jrfCART.forceFocLost();
    jrfCART.requestFocus();
  }
  public void setCART(int str) {
    jrfCART.setText(String.valueOf(str));
  }
  public void nextTofocus() {
    nextFocusabile.requestFocus();
  }
  public void setnextFocusabile(JComponent jcomp){
    nextFocusabile=jcomp;
  }
  public boolean isUsluga() {
    return lUsluga;
  }
/**
 * @deprecated
 * @param mode
 */
  public void setMode(String mode) {
  }
/**
 * @deprecated
 * @param mode
 */
  public void setParam(String mode) {
//    System.out.println("Metoda je deprecated !!! Koristi raArtikl.setDefFocus();");
  }
/**
 * @deprecated
 * @param mode
 */
  public void SetFocus(String mode) {
//    System.out.println("Metoda je deprecated !!! Koristi raArtikl.setDefFocus();");
    this.setDefFocus();
  }
/**
 * Postavljanje defaultnog focusa u polje
 */
  public void setDefFocus() {
    String param=hr.restart.sisfun.frmParam.getParam("robno","focusCart");
    String sifra=hr.restart.sisfun.frmParam.getParam("robno","indiCart");
    if (param.equals("SIFRA")) {
      if (sifra.equals("CART"))       jrfCART.requestFocus();
      else if (sifra.equals("CART1")) jrfCART1.requestFocus();
      else if (sifra.equals("BC"))    jrfBC.requestFocus();
    }
    else if (param.equals("NAZIV"))   jrfNAZART.requestFocus();
  }
/**
 * Enabliranje/disebliranje polja za unos
 * @param istina
 */
  public void EnabDisab(boolean istina){
    rcc.setLabelLaF(this.jrfCART,istina);
    rcc.setLabelLaF(this.jrfCART1,istina);
    rcc.setLabelLaF(this.jrfBC,istina);
    rcc.setLabelLaF(this.jrfCGRART,istina);
    rcc.setLabelLaF(this.jbCART,istina);
    rcc.setLabelLaF(this.jrfJM,istina);
    if (!this.lUsluga) {
      rcc.setLabelLaF(this.jrfNAZART,istina);
    }
    checkOther(istina);
  }
  public void afterLookup() {
    System.out.println("afterlukap");
  }
  public void checkOther(boolean istina) {
  }
}