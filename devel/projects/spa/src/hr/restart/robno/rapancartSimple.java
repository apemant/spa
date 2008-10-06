/****license*****************************************************************
**   file: rapancartSimple.java
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
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

abstract public class rapancartSimple extends JPanel {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private XYLayout xYLayoutDH = new XYLayout();
  private JLabel jlCART = new JLabel();
  private JLabel jLabelSirfa = new JLabel();
  private JLabel jLabelSirfa1 = new JLabel();
  private JLabel jLabelSirfa2 = new JLabel();
  private JLabel jLabelSirfa3 = new JLabel();
  private JraButton jbCART = new JraButton();
  private TitledBorder titledBorder1;
  private JLabel jLabel1 = new JLabel();
  public JlrNavField jrfCART = new JlrNavField() {
    public void after_lookUp() {
      MMyafter_lookUp();
    }
  };
  public JlrNavField jrfCART1 = new JlrNavField() {
    public void after_lookUp() {
      MMyafter_lookUp();
    }
  };
  public  JlrNavField jrfBC = new JlrNavField() {
    public void after_lookUp() {
      MMyafter_lookUp();
    }
  };
  public  JlrNavField jrfJM = new JlrNavField();
  public  JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp() {
      MMyafter_lookUp();
    }
  };
  public  JlrNavField jrfCGRART = new JlrNavField() {
    public void after_lookUp() {
      MMyafter_lookUp();
    }
  };
/*
  private JlrNavField jrfCPOR = new JlrNavField() {
    public void after_lookUp() {
      Myafter_lookUp();
    }
  };
*/

  public rapancartSimple() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {

//    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"");
    this.setLayout(xYLayoutDH);
    ////// DOHVAT ARTIKALA

    jlCART.setText("Artikl");
    jrfCART.setNextFocusableComponent(jrfCART1);
    jrfCART.setColumnName("CART");
//    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART","CPOR","ISB"});
    jrfCART.setColNames(new String[] {"CART1","BC","NAZART","JM","CGRART"});
    jrfCART.setVisCols(new int[]{0,3,4});
//    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART,jrfCPOR});
    jrfCART.setTextFields(new javax.swing.text.JTextComponent[] {jrfCART1,jrfBC,jrfNAZART,jrfJM,jrfCGRART});
    jrfCART.setRaDataSet(dm.getArtikli());
    jrfCART.setNavButton(jbCART);
    jrfCART1.setColumnName("CART1");
    jrfCART1.setSearchMode(3);
    jrfCART1.setNavProperties(jrfCART);
    jrfCART1.setFocusLostOnShow(false);
    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setSearchMode(2);
    jrfCGRART.setNavProperties(jrfCART);
    jrfCGRART.setFocusLostOnShow(false);
    jrfBC.setColumnName("BC");
    jrfBC.setSearchMode(3);
    jrfBC.setNavProperties(jrfCART);
    jrfBC.setFocusLostOnShow(false);
    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setSearchMode(3);
    jrfNAZART.setNavProperties(jrfCART);
    jbCART.setText("...");

    //// jrfJM
    jrfJM.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfJM.setColumnName("JM");
    jrfJM.setSearchMode(2);
    jrfJM.setNavProperties(jrfCART);
    jrfJM.setFocusLostOnShow(false);
    this.setBorder(titledBorder1);
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

    int size = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("robno","cartSize","0"));
    if (size > 0) {
      new hr.restart.swing.raTextMask(jrfCART1,size).setAllowSpaces(false);
    }
    int sizeBC = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("robno","cartSizeBC","0"));
    if (sizeBC > 0) {
      new hr.restart.swing.raTextMask(jrfBC,size).setAllowSpaces(false);
    }

    this.add(jlCART,  new XYConstraints(15, 25, -1, -1));
    this.add(jrfNAZART,         new XYConstraints(150, 50, 410, -1));
    this.add(jrfCART,  new XYConstraints(150, 25, 65, -1));
//    this.add(jrfCPOR, new XYConstraints(355, 80, 100, -1));
//    this.add(jrfISB, new XYConstraints(355, 80, 100, -1));
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

  public void SetDefFocus() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if ("SIFRA".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","focusCart"))) {
          if ("CART".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","indiCart"))) {
            jrfCART.requestFocus();
          }
          else if ("CART1".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","indiCart"))) {
            jrfCART1.requestFocus();
          }
          else if ("BC".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","indiCart"))) {
            jrfBC.requestFocus();
          }
        }
        else {
          jrfNAZART.requestFocus();
        }
      }
    });




  }
//  hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F8);
//    hr.restart.util.Aus.removeSwingKeyRecursive(this.getTopLevelAncestor(),KeyEvent.VK_F6);
//    SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
  private void MMyafter_lookUp() {
      if (jrfCART.getText().trim().equals("")) {
        return;
      }
      Myafter_lookUp();
  }
  abstract public void Myafter_lookUp();
}