/****license*****************************************************************
**   file: jpGetArt.java
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
 * @author REST-ART development team
 * @version 1.0
 */

public class jpGetArt extends JPanel implements jpGetArtInterface{
  private char mode;
  private com.borland.dx.sql.dataset.QueryDataSet qds;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCART = new JLabel();
  JLabel jlNAZART = new JLabel();
  JLabel jlCGRART = new JLabel();
  JLabel jlBC = new JLabel();
  JLabel jlCART1 = new JLabel();
  JLabel jlArtikl = new JLabel();
  JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
      majfter();
      System.out.println("after lukap - BC");
    }
  };
  JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      majfter();
      System.out.println("after lukap - CART");
    }
  };
  JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp() {
      majfter();
      System.out.println("after lukap - NAZART");
    }
  };
  JraButton jbCART = new JraButton();
  JlrNavField jrfCGRART = new JlrNavField() {
    public void after_lookUp() {
      majfter();
      System.out.println("after lukap - CGRART");
    }
  };
  JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
      majfter();
      System.out.println("after lukap - CART1");
    }
  };
  JlrNavField jrfJM = new JlrNavField();
  JlrNavField jrfCPOR = new JlrNavField();
  JlrNavField jrfISB = new JlrNavField();

  public jpGetArt() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jrfCART.setColumnName("CART");
    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART","CPOR","ISB"});
    if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("CART")) {
      jrfCART.setVisCols(new int[]{0,3,4});
    }
    else if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("CART1")) {
      jrfCART.setVisCols(new int[]{1,3,4});
    }
    else if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("BC")) {
      jrfCART.setVisCols(new int[]{2,3,4});
    }
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART,jrfCPOR,jrfISB});
    jrfCART.setRaDataSet(dm.getArtikli());

    jrfCART1.setColumnName("CART1");
    jrfCART1.setSearchMode(0);
    jrfCART1.setNavProperties(jrfCART);

    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setSearchMode(2);
    jrfCGRART.setNavProperties(jrfCART);

    jrfBC.setColumnName("BC");
    jrfBC.setSearchMode(0);
    jrfBC.setNavProperties(jrfCART);

    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setSearchMode(1);
    jrfNAZART.setNavProperties(jrfCART);

    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(1);
    jrfJM.setNavProperties(jrfCART);

    jrfCPOR.setColumnName("CPOR");
    jrfCPOR.setNavProperties(jrfCART);
    jrfCPOR.setSearchMode(-1);
    jrfCPOR.setVisible(false);

    jrfISB.setColumnName("ISB");
    jrfISB.setNavProperties(jrfCART);
    jrfISB.setSearchMode(-1);
    jrfISB.setVisible(false);

    jlArtikl.setText("Artikl");
    jlCART1.setText("Oznaka");
    jlCART1.setHorizontalAlignment(SwingConstants.LEFT);
    jlBC.setText("Barcode");
    jlBC.setHorizontalAlignment(SwingConstants.LEFT);
    jlCGRART.setText("Grupa");
    jlCGRART.setHorizontalAlignment(SwingConstants.LEFT);
    jlNAZART.setText("Naziv");
    jlCART.setText("Šifra");
    jlCART.setHorizontalAlignment(SwingConstants.LEFT);
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(95);
    this.setLayout(xYLayout1);
    jbCART.setToolTipText("Status");
    jbCART.setText("...");
    this.add(jlArtikl,  new XYConstraints(15, 30, -1, -1));
    this.add(jlNAZART, new XYConstraints(15, 55, -1, -1));
    this.add(jlCART, new XYConstraints(150, 13, 60, -1));
    this.add(jlCART1, new XYConstraints(220, 13, 130, -1));
    this.add(jlBC, new XYConstraints(360, 13, 130, -1));
    this.add(jlCGRART, new XYConstraints(500, 13, 100, -1));
    this.add(jrfCART, new XYConstraints(150, 30, 65, -1));
    this.add(jrfBC, new XYConstraints(360, 30, 135, -1));
    this.add(jrfNAZART, new XYConstraints(150, 55, 454, -1));
    this.add(jbCART, new XYConstraints(609, 30, 21, 21));
    this.add(jrfCGRART, new XYConstraints(500, 30, 104, -1));
    this.add(jrfCART1, new XYConstraints(220, 30, 135, -1));
  }
  public void setMode(char newMode) {
    mode=newMode;
  }
  public char getMode() {
    return mode;
  }
  public void setTable(com.borland.dx.sql.dataset.QueryDataSet newQds) {
    qds=newQds;
    System.out.println("Aut: "+qds.rowCount());
    jrfCART.setDataSet(qds);
    jrfCART1.setDataSet(qds);
    jrfBC.setDataSet(qds);
    jrfNAZART.setDataSet(qds);
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQds() {
    return qds;
  }
  public void majfter() {
  }
  public void SetFocus(String param){
    if (param.equals("SIFRA")) {
      if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("CART")) {
        jrfCART.requestFocus();
      }
      else if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("CART1")) {
        jrfCART1.requestFocus();
      }
      else if (hr.restart.sisfun.frmParam.getParam("robno","indiCart").trim().equals("BC")) {
        jrfBC.requestFocus();
      }
    }
  }
  public String getCART() {
    return jrfCART.getText().trim();
  }
  public String getCART1() {
    return jrfCART1.getText().trim();
  }
  public String getBC() {
    return jrfBC.getText().trim();
  }
  public String getNAZART() {
    return jrfNAZART.getText().trim();
  }
  public String getCGRART() {
    return this.jrfCGRART.getText().trim();
  }
  public String getCPOR() {
    return jrfCPOR.getText();
  }
  public String getISB() {
    return jrfISB.getText();
  }
}