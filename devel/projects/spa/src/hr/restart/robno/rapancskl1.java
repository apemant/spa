/****license*****************************************************************
**   file: rapancskl1.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class rapancskl1 extends JPanel {

  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass cc = hr.restart.util.raCommonClass.getraCommonClass();
  private boolean isDisabAfter = false;
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCSKL = new JLabel();
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCSKL = new JlrNavField() {
    public void after_lookUp() {
      MYafter_lookUp();
    }
  };

  JlrNavField jrfNAZSKL = new JlrNavField() {
    public void after_lookUp() {
      MYafter_lookUp();
    }
  };
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  private String naslov = "Skladište";
  private boolean overcap = true;
  private int yduzina = 260;


  private void initRapancart(){
    try {
     jbInit();
     jbInitRest(overcap);
   }
   catch(Exception ex) {
     ex.printStackTrace();
   }
  }

  public rapancskl1() {
    initRapancart();
  }
  public rapancskl1(int yduzina) {
    this.yduzina=yduzina;
    initRapancart();
  }
  public rapancskl1(boolean overcap) {
    this.overcap = overcap;
    initRapancart();
  }
  public rapancskl1(boolean overcap,int yduzina,String naslov) {
    this.naslov=naslov;
    this.overcap = overcap;
    this.yduzina=yduzina;
    initRapancart();
  }

  public rapancskl1(boolean overcap,int yduzina) {
    this.overcap = overcap;
    this.yduzina=yduzina;
    initRapancart();
  }

  public void setDefaultCSKL() {
//      jrfCSKL.setText(hr.restart.sisfun.frmParam.getParam("robno","defCskl"));
      jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
      jrfCSKL.forceFocLost();
  }

  void jbInit() throws Exception {
    setLayout(xYLayout1);
    jbCSKL.setText("...");
    jlCSKL.setText(naslov);
    jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel4.setText("Naziv");
//    jrfCSKL.setNextFocusableComponent(jrfNAZSKL);
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setNavColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
//    jrfCSKL.setRaDataSet(dm.getSklad());
        jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);
//    jrfNAZSKL.setNextFocusableComponent(jrfCSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);

    jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel3.setText("Šifra");

  }
  void jbInitRest(boolean how) throws Exception {

    if (how) {
      xYLayout1.setWidth(285+yduzina);
      xYLayout1.setHeight(50);
      add(jlCSKL, new XYConstraints(15, 25, -1, -1));
      add(jrfCSKL, new XYConstraints(150, 25, 100, -1));
      add(jrfNAZSKL, new XYConstraints(255, 25, yduzina, -1)); // 260
      add(jbCSKL, new XYConstraints(260+yduzina, 25, 21, 21));      // 519
//      add(jbCSKL, new XYConstraints(519, 25, 21, 21));
//      add(jrfNAZSKL, new XYConstraints(255, 25, 260, -1));
      add(jLabel3, new XYConstraints(150, 8, 100, -1));
      add(jLabel4, new XYConstraints(255, 8, 260, -1));
    }
    else {
      xYLayout1.setWidth(285+yduzina);
//      xYLayout1.setWidth(574);
      xYLayout1.setHeight(25);
      add(jlCSKL, new XYConstraints(15, 0, -1, -1));
      add(jrfCSKL, new XYConstraints(150, 0, 100, -1));
      add(jrfNAZSKL, new XYConstraints(255, 0, yduzina, -1)); // 260
      add(jbCSKL, new XYConstraints(260+yduzina, 0, 21, 21));      // 519
    }
  }


  public void setOverCaption(boolean b0) {
      jLabel3.setVisible(b0);
      jLabel4.setVisible(b0);
  }

  public void setDisabAfter (boolean how) {
      isDisabAfter = how;
  }

  public void MYafter_lookUp() {
    if (!jrfCSKL.getText().trim().equals("")) {
//      System.out.println("desavam se ..");
      if (isDisabAfter) {
//        System.out.println("isdsabled je " + isDisabAfter);
        disabCSKL(false);
      }
//      this.transferFocus();
      MYpost_after_lookUp();
    }
  }
  public void MYpost_after_lookUp(){}

  public void Clear() {
    jrfCSKL.emptyTextFields();
    jrfNAZSKL.emptyTextFields();
  }

  public void disabCSKL(boolean kako) {
    cc.setLabelLaF(jrfCSKL,kako);
    cc.setLabelLaF(jrfNAZSKL,kako);
    cc.setLabelLaF(jbCSKL,kako);
  }
  public void setNaslov(String naslov) {
    this.naslov = naslov;
  }
  /**
   * Setiranje skladišta
   */
    public void setCSKL(String tekst) {
      jrfCSKL.setText(tekst);
      jrfCSKL.forceFocLost();
      if (tekst.equals("")) {
        jrfCSKL.requestFocus();
      }
    }
    /**
     * Dohvat šifre skladišta
     */
      public String getCSKL() {
        return jrfCSKL.getText().trim();
  }
}