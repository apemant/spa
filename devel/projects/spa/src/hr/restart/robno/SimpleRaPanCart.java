/****license*****************************************************************
**   file: SimpleRaPanCart.java
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

import java.awt.Container;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class SimpleRaPanCart extends JPanel {

  private hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

  private XYLayout xYLayoutDH = new XYLayout();
  private JLabel jlCART = new JLabel();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabelSirfa = new JLabel();
  private JLabel jLabelSirfa1 = new JLabel();
  private JLabel jLabelSirfa2 = new JLabel();
  private JLabel jLabelSirfa3 = new JLabel();
  private JraButton jbCART = new JraButton();

  JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };

  private JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };
  private JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };

  private JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };
  private JlrNavField jrfCGRART = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };

  private JlrNavField jrfJM = new JlrNavField();

  public SimpleRaPanCart() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

    addListeners();
    this.setLayout(xYLayoutDH);
    jlCART.setText("Artikl");
    jrfCART.setColumnName("CART");
    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART"});
    jrfCART.setVisCols(new int[]{0,3,4});
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART});
    jrfCART.setRaDataSet(dm.getArtikli().cloneDataSetView());
//    jrfCART1.setSearchMode(0);
//    jrfCART.setFocusLostOnShow(false);

    jrfCART1.setColumnName("CART1");
    jrfCART1.setSearchMode(0);
//    jrfCART1.setFocusLostOnShow(false);

    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setSearchMode(2);
//    jrfCGRART.setFocusLostOnShow(false);

    jrfBC.setColumnName("BC");
    jrfBC.setSearchMode(0);
//    jrfBC.setFocusLostOnShow(false);

//    jrfNAZART.setNextFocusableComponent(jrfCART);
    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setSearchMode(1);
//    jrfNAZART.setFocusLostOnShow(false);

    //// jrfJM
    jrfJM.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(-1);
//    jrfJM.setFocusLostOnShow(false);

    jbCART.setText("...");
    jrfCART.setNavButton(jbCART);

    jLabelSirfa.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa.setText("Šifra");
    jLabelSirfa1.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa1.setText("Oznaka");
    jLabelSirfa2.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa2.setText("Barcode");
    jLabelSirfa3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabelSirfa3.setText("Grupa");
    jLabel1.setText("Naziv / Jm");
    xYLayoutDH.setWidth(645);
    xYLayoutDH.setHeight(140);
    this.add(jlCART,  new XYConstraints(15, 25, -1, -1));
    this.add(jrfNAZART,         new XYConstraints(150, 50, 410, -1));
    this.add(jrfCART,  new XYConstraints(150, 25, 65, -1));
    this.add(jbCART, new XYConstraints(609, 25, 21, 21));
    this.add(jLabel1,   new XYConstraints(15, 50, -1, -1));
    this.add(jLabelSirfa, new XYConstraints(150, 8, 60, -1));
    this.add(jrfCART1,  new XYConstraints(220, 25, 135, -1));
    this.add(jrfBC,    new XYConstraints(360, 25, 135, -1));
    this.add(jrfCGRART,   new XYConstraints(500, 25, 104, -1));
    this.add(jrfJM,          new XYConstraints(564, 50, 40, -1));
    this.add(jLabelSirfa1, new XYConstraints(220, 8, 130, -1));
    this.add(jLabelSirfa2, new XYConstraints(360, 8, 130, -1));
    this.add(jLabelSirfa3, new XYConstraints(500, 8, 100, -1));
  }

  public void Myafter_lookUp(){}

  public void setDataSet(DataSet ds) {
    jrfCART.setDataSet(ds);
    jrfCART1.setNavProperties(jrfCART);
    jrfCGRART.setNavProperties(jrfCART);
    jrfBC.setNavProperties(jrfCART);
    jrfNAZART.setNavProperties(jrfCART);
    jrfJM.setNavProperties(jrfCART);
    hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F8);
    hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F6);
  }

  public void forceLookup() {
    jrfCART.forceFocLost();
  }

  public void setParalelColumnCart(String nesto){

    jrfCART.setColumnName(nesto);
    jrfCART.setNavColumnName("CART");
  }

  public void setFocus() {
    String param = hr.restart.sisfun.frmParam.getParam("robno","focusCart");
    String indiCart = hr.restart.sisfun.frmParam.getParam("robno","indiCart");

    if (param.equals("SIFRA")) {
      if (indiCart.equals("CART"))
        jrfCART.requestFocus();
      else if (indiCart.equals("CART1"))
        jrfCART1.requestFocus();
      else if (indiCart.equals("BC"))
        jrfBC.requestFocus();
    }
    else if (param.equals("NAZIV")) jrfNAZART.requestFocus();
  }

  public String keyF8Pressed(Container c,String polje,String value) {
    return null;
  }

  void showStanje(String polje,String value)  {
    String result = keyF8Pressed(this.getTopLevelAncestor(),polje,value);
    if (result != null ) {
      this.jrfCART.setText(result);
     jrfCART.forceFocLost();
    }
  }
  void addListeners() {
    jrfCART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCART_keyReleased(e);
      }
    });

    jrfCART1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCART1_keyReleased(e);
      }
    });

    jrfNAZART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfNAZART_keyReleased(e);
      }
    });

    jrfBC.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfBC_keyReleased(e);
      }
    });

    jrfCGRART.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jrfCGRART_keyReleased(e);
      }
    });
  }
  void jrfCART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("CART",jrfCART.getText());
    }
  }
  void jrfCART1_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("CART1",jrfCART1.getText());
    }
  }
  void jrfNAZART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("NAZART",jrfCART1.getText());
    }
  }
  void jrfBC_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("BC",jrfCART1.getText());
    }
  }
  void jrfCGRART_keyReleased(KeyEvent e) {
    if(e.getKeyCode() == e.VK_F8) {
      showStanje("CART1",jrfCART1.getText());
    }
  }
}
