/****license*****************************************************************
**   file: raJpanelPdv.java
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
package hr.restart.sk;

import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raJpanelPdv extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  XYLayout xYLay1 = new XYLayout();
  XYLayout xYLay2 = new XYLayout();
  XYLayout xYLay3 = new XYLayout();
  XYLayout xYLay456 = new XYLayout();

  XYLayout xYLayFooter = new XYLayout();
  BorderLayout bLay = new BorderLayout();
  BorderLayout bLay1 = new BorderLayout();
  BorderLayout bLay2 = new BorderLayout();

  JPanel jpPanel1 = new JPanel();
  JPanel jpPanel2 = new JPanel();
  JPanel footer = new JPanel();

  JPanel jpS = new JPanel();
  JPanel jpI = new JPanel();
  JPanel jpII = new JPanel();
  JPanel jpIII = new JPanel();
  JPanel jpIV = new JPanel();
  JPanel jpV = new JPanel();
  JPanel jpVI = new JPanel();
  JPanel jpDOD = new JPanel();
  

  JLabel as = new JLabel();

  JraTextField S_I_II = new JraTextField();
  JLabel jlS_I_II = new JLabel("Obavljene isporuke - UKUPNO");
  JraTextField I = new JraTextField();
  JLabel jlI = new JLabel("I. Ukupne isporuke bez poreza");
  JraTextField I1 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I1");
    }
  };
  JLabel jlI1 = new JLabel(" 1. Koje ne podlježu oporezivanju");
  JraTextField I2 = new JraTextField();
  JLabel jlI2 = new JLabel(" 2. Oslobo\u0111ene poreza - UKUPNO");
  JraTextField I21 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I21");
    }
  };
  JLabel jlI21 = new JLabel("  2.1. Tuzemne");
  JraTextField I22 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I22");
    }
  };
  JLabel jlI22 = new JLabel("  2.2. Prijevoz");
  JraTextField I23 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I23");
    }
  };
  JLabel jlI23 = new JLabel("  2.3. Inozemne");
  JraTextField I24 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I24");
    }
  };
  JLabel jlI24 = new JLabel("  2.4. Ostalo");
  JraTextField I3 = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("I3");
    }
  };
  JLabel jlI3 = new JLabel(" 3. Isporuke po stopi 0%");
  JraTextField IIp = new JraTextField();
  JraTextField IIv = new JraTextField();
  JLabel jlII = new JLabel("II. Oporezive isporuke - UKUPNO");
  JraTextField II1v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II1v");
    }
  };
  JraTextField II1p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II1p");
    }
  };
  JLabel jlII1 = new JLabel(" 1. Za koje su izdani ra\u010Duni po stopi 5% i 10%");
  JraTextField II2v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II2v");
    }
  };
  JraTextField II2p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II2p");
    }
  };
  JLabel jlII2 = new JLabel(" 2. Za koje su izdani ra\u010Duni po stopi 22% i 23%");
  JraTextField II3v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II3v");
    }
  };
  JraTextField II3p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II3p");
    }
  };
  JLabel jlII3 = new JLabel(" 3. Za koje su izdani ra\u010Duni po stopi 25%");
  JraTextField II4v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II4v");
    }
  };
  JraTextField II4p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II4p");
    }
  };
  JLabel jlII4 = new JLabel(" 4. Nenaplaæeni izvoz");
  JraTextField II5v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II5v");
    }
  };
  JraTextField II5p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("II5p");
    }
  };
  JLabel jlII5 = new JLabel(" 5. Naknadno osloboðenje izvoza");
  
  JraTextField IIIp = new JraTextField();
  JraTextField IIIv = new JraTextField();
  JLabel jlIII = new JLabel("III. Obra\u010Dunani pretporez - UKUPNO");
  JraTextField III1v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III1v");
    }
  };
  JraTextField III1p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III1p");
    }
  };
  JLabel jlIII1 = new JLabel(" 1. Pretporez u prim. ra\u010D. po stopi 5% i 10%");
  JraTextField III2v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III2v");
    }
  };
  JraTextField III2p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III2p");
    }
  };
  JLabel jlIII2 = new JLabel(" 2. Pretporez u prim. ra\u010D. po stopi 22% i 23%");
  
  JraTextField III3v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III3v");
    }
  };
  JraTextField III3p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III3p");
    }
  };
  JLabel jlIII3 = new JLabel(" 3. Pretporez u prim. ra\u010D. po stopi 25%");
  JraTextField III4v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III4v");
    }
  };
  JraTextField III4p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III4p");
    }
  };
  JLabel jlIII4 = new JLabel(" 4. Pla\u0107eni pretporez pri uvozu");
  JraTextField III5v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III5v");
    }
  };
  JraTextField III5p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III5p");
    }
  };
  JLabel jlIII5 = new JLabel(" 5. Pla\u0107eni pretporez na usl. ino. pod. 5% i 10%");
  
  JraTextField III6v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III6v");
    }
  };
  JraTextField III6p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III6p");
    }
  };
  JLabel jlIII6 = new JLabel(" 6. Pla\u0107eni pretporez na usl. ino. pod. 22% i 23%");
  
  JraTextField III7v = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III7v");
    }
  };
  JraTextField III7p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III7p");
    }
  };
  JLabel jlIII7 = new JLabel(" 7. Pla\u0107eni pretporez na usl. ino. pod. 25%");
  
  JraTextField III8p = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("III8p");
    }
  };
  JLabel jlIII8 = new JLabel(" 8. Ispravci pretporeza");
  JraTextField IV = new JraTextField();
  JLabel jlIV = new JLabel("IV. Porezna obveza u obra\u010Dunskom razdoblju");
  JraTextField V = new JraTextField(){
    public void valueChanged() {
      frpdv.focusLostEv("V");
    }
  };
  JLabel jlV = new JLabel("V. Po prethodnom obra\u010Dunu neupl. -više upl. -kredit");
  JraTextField VI = new JraTextField();
  JLabel jlVI = new JLabel("VI. Ukupna razlika");

  JLabel jlPov = new JLabel("Za povrat");
  JLabel jlPred = new JLabel("Predujam");
  JLabel jlUst = new JLabel("Ustup povrata");
  
  JraTextField POV = new JraTextField();
  JraTextField PRED = new JraTextField();
  JraTextField UST = new JraTextField();
  
  frmPDV frpdv;

  public raJpanelPdv(frmPDV f) {
    frpdv = f;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    rebindFields();

    this.setLayout(bLay);
    jpPanel1.setLayout(bLay1);
    jpPanel2.setLayout(bLay2);
    footer.setLayout(xYLayFooter);
    xYLayFooter.setWidth(570);
    xYLayFooter.setHeight(20);

    jpS.setLayout(xYLay456);
    jpI.setLayout(xYLay1);
    xYLay1.setWidth(550);
    xYLay1.setHeight(225);
    jpII.setLayout(xYLay2);
    xYLay2.setWidth(550);
    xYLay2.setHeight(160);
    jpIII.setLayout(xYLay3);
    xYLay3.setWidth(550);
    xYLay3.setHeight(235);
    jpIV.setLayout(xYLay456);
    jpV.setLayout(xYLay456);
    jpVI.setLayout(xYLay456);
    xYLay456.setWidth(550);
    xYLay456.setHeight(30);

    jpS.setBorder(BorderFactory.createEtchedBorder());
    jpS.add(S_I_II, new XYConstraints(340, 5, 100 , -1));
    jpS.add(jlS_I_II, new XYConstraints(15, 5, -1, -1));

    jpI.setBorder(BorderFactory.createEtchedBorder());
    jpI.add(I, new XYConstraints(340, 5, 100 , -1));
    jpI.add(jlI, new XYConstraints(15, 5, -1 ,-1));
    jpI.add(I1, new XYConstraints(340, 35, 100 , -1));
    jpI.add(jlI1, new XYConstraints(15, 35, -1, -1));
    jpI.add(I2, new XYConstraints(340, 70, 100 , -1));
    jpI.add(jlI2, new XYConstraints(15,70,-1,-1));
    jpI.add(I21, new XYConstraints(340, 95, 100 , -1));
    jpI.add(jlI21, new XYConstraints(15, 95 ,-1,-1));
    jpI.add(I22, new XYConstraints(340, 120, 100 , -1));
    jpI.add(jlI22, new XYConstraints(15, 120 ,-1,-1));
    jpI.add(I23, new XYConstraints(340, 145, 100 , -1));
    jpI.add(jlI23, new XYConstraints(15, 145 ,-1,-1));
    jpI.add(I24, new XYConstraints(340, 170, 100 , -1));
    jpI.add(jlI24, new XYConstraints(15, 170 ,-1,-1));
    jpI.add(I3, new XYConstraints(340, 200, 100 , -1));
    jpI.add(jlI3, new XYConstraints(15, 200 ,-1,-1));

    jpII.setBorder(BorderFactory.createEtchedBorder());
    jpII.add(IIv, new XYConstraints(340, 5, 100 , -1));
    jpII.add(IIp, new XYConstraints(445, 5, 100 , -1));
    jpII.add(jlII, new XYConstraints(15, 5 ,-1,-1));
    jpII.add(II1v, new XYConstraints(340, 35, 100 , -1));
    jpII.add(II1p, new XYConstraints(445, 35, 100 , -1));
    jpII.add(jlII1, new XYConstraints(15, 35 ,-1,-1));
    jpII.add(II2v, new XYConstraints(340, 60, 100 , -1));
    jpII.add(II2p, new XYConstraints(445, 60, 100 , -1));
    jpII.add(jlII2, new XYConstraints(15, 60 ,-1,-1));
    jpII.add(II3v, new XYConstraints(340, 85, 100 , -1));
    jpII.add(II3p, new XYConstraints(445, 85, 100 , -1));
    jpII.add(jlII3, new XYConstraints(15, 85 ,-1,-1));
    jpII.add(II4v, new XYConstraints(340, 110, 100 , -1));
    jpII.add(II4p, new XYConstraints(445, 110, 100 , -1));
    jpII.add(jlII4, new XYConstraints(15, 110 ,-1,-1));
    jpII.add(II5v, new XYConstraints(340, 135, 100 , -1));
    jpII.add(II5p, new XYConstraints(445, 135, 100 , -1));
    jpII.add(jlII5, new XYConstraints(15, 135 ,-1,-1));

    jpIII.setBorder(BorderFactory.createEtchedBorder());
    jpIII.add(IIIv, new XYConstraints(340, 5, 100 , -1));
    jpIII.add(IIIp, new XYConstraints(445, 5, 100 , -1));
    jpIII.add(jlIII, new XYConstraints(15, 5 ,-1,-1));
    jpIII.add(III1v, new XYConstraints(340, 35, 100 , -1));
    jpIII.add(III1p, new XYConstraints(445, 35, 100 , -1));
    jpIII.add(jlIII1, new XYConstraints(15, 35 ,-1,-1));
    jpIII.add(III2v, new XYConstraints(340, 60, 100 , -1));
    jpIII.add(III2p, new XYConstraints(445, 60, 100 , -1));
    jpIII.add(jlIII2, new XYConstraints(15, 60 ,-1,-1));
    jpIII.add(III3v, new XYConstraints(340, 85, 100 , -1));
    jpIII.add(III3p, new XYConstraints(445, 85, 100 , -1));
    jpIII.add(jlIII3, new XYConstraints(15, 85 ,-1,-1));
    jpIII.add(III4v, new XYConstraints(340, 110, 100 , -1));
    jpIII.add(III4p, new XYConstraints(445, 110, 100 , -1));
    jpIII.add(jlIII4, new XYConstraints(15, 110 ,-1,-1));
    jpIII.add(III5v, new XYConstraints(340, 135, 100 , -1));
    jpIII.add(III5p, new XYConstraints(445, 135, 100 , -1));
    jpIII.add(jlIII5, new XYConstraints(15, 135 ,-1,-1));
    jpIII.add(III6v, new XYConstraints(340, 160, 100 , -1));
    jpIII.add(III6p, new XYConstraints(445, 160, 100 , -1));
    jpIII.add(jlIII6, new XYConstraints(15, 160 ,-1,-1));
    jpIII.add(III7v, new XYConstraints(340, 185, 100 , -1));
    jpIII.add(III7p, new XYConstraints(445, 185, 100 , -1));
    jpIII.add(jlIII7, new XYConstraints(15, 185 ,-1,-1));
    jpIII.add(III8p, new XYConstraints(445, 210, 100 , -1));
    jpIII.add(jlIII8, new XYConstraints(15, 210 ,-1,-1));

    jpIV.setBorder(BorderFactory.createEtchedBorder());
    jpIV.add(IV, new XYConstraints(445, 5, 100 , -1));
    jpIV.add(jlIV, new XYConstraints(15, 5 ,-1,-1));

    jpV.setBorder(BorderFactory.createEtchedBorder());
    jpV.add(V, new XYConstraints(445, 5, 100 , -1));
    jpV.add(jlV, new XYConstraints(15, 5 ,-1,-1));

    jpVI.setBorder(BorderFactory.createEtchedBorder());
    jpVI.add(VI, new XYConstraints(445, 5, 100 , -1));
    jpVI.add(jlVI, new XYConstraints(15, 5 ,-1,-1));
    
    jlPov.setHorizontalAlignment(SwingConstants.CENTER);
    jlPred.setHorizontalAlignment(SwingConstants.CENTER);
    jlUst.setHorizontalAlignment(SwingConstants.CENTER);
    jpDOD.setLayout(new XYLayout(550, 55));
    jpDOD.setBorder(BorderFactory.createEtchedBorder());
    jpDOD.add(jlPov, new XYConstraints(110, 5, 100, -1));
    jpDOD.add(jlPred, new XYConstraints(225, 5, 100, -1));
    jpDOD.add(jlUst, new XYConstraints(340, 5, 100, -1));
    jpDOD.add(POV, new XYConstraints(110, 25, 100, -1));
    jpDOD.add(PRED, new XYConstraints(225, 25, 100, -1));
    jpDOD.add(UST, new XYConstraints(340, 25, 100, -1));

    
    jpPanel1.add(jpI, BorderLayout.NORTH);
    jpPanel1.add(jpII, BorderLayout.CENTER);
    jpPanel1.add(jpIII, BorderLayout.SOUTH);

    jpPanel2.add(jpIV, BorderLayout.NORTH);
    jpPanel2.add(jpV, BorderLayout.CENTER);
    jpPanel2.add(jpVI, BorderLayout.SOUTH);

    JPanel bot = new JPanel(new BorderLayout());
    bot.add(jpPanel2, BorderLayout.CENTER);
    bot.add(jpDOD, BorderLayout.SOUTH);
    
    this.add(jpS, BorderLayout.NORTH);
    this.add(jpPanel1, BorderLayout.CENTER);
    this.add(bot, BorderLayout.SOUTH);
  }

  public void rebindFields() {
    S_I_II.setDataSet(frpdv.reportSet);
    S_I_II.setColumnName("UKUPNO_I_II");
    I.setDataSet(frpdv.reportSet);
    I.setColumnName("UKUPNO_I");
    I1.setDataSet(frpdv.reportSet);
    I1.setColumnName("BEZ_POREZA");
    I2.setDataSet(frpdv.reportSet);
    I2.setColumnName("UKUPNO_I2");
    I21.setDataSet(frpdv.reportSet);
    I21.setColumnName("TUZEMNE");
    I22.setDataSet(frpdv.reportSet);
    I22.setColumnName("PRIJEVOZ");
    I23.setDataSet(frpdv.reportSet);
    I23.setColumnName("IZVOZNE");
    I24.setDataSet(frpdv.reportSet);
    I24.setColumnName("OSTALO_I24");
    I3.setDataSet(frpdv.reportSet);
    I3.setColumnName("STOPA_NULA");
    IIv.setDataSet(frpdv.reportSet);
    IIv.setColumnName("UKUPNO_II_V");
    IIp.setDataSet(frpdv.reportSet);
    IIp.setColumnName("UKUPNO_II_P");
    II1v.setDataSet(frpdv.reportSet);
    II1v.setColumnName("IZDANI_RACUNI_V");
    II1p.setDataSet(frpdv.reportSet);
    II1p.setColumnName("IZDANI_RACUNI_P");
    II2v.setDataSet(frpdv.reportSet);
    II2v.setColumnName("NEZARACUNANE_V");
    II2p.setDataSet(frpdv.reportSet);
    II2p.setColumnName("NEZARACUNANE_P");
    
    II3v.setDataSet(frpdv.reportSet);
    II3v.setColumnName("R23_V");
    II3p.setDataSet(frpdv.reportSet);
    II3p.setColumnName("R23_P");
    
    II4v.setDataSet(frpdv.reportSet);
    II4v.setColumnName("VLASTITA_POT_V");
    II4p.setDataSet(frpdv.reportSet);
    II4p.setColumnName("VLASTITA_POT_P");
    II5v.setDataSet(frpdv.reportSet);
    II5v.setColumnName("NENAP_IZVOZ_V");
    II5p.setDataSet(frpdv.reportSet);
    II5p.setColumnName("NENAP_IZVOZ_P");
/*    II5v.setDataSet(frpdv.reportSet);
    II5v.setColumnName("NAK_OSL_IZV_V");
    II5p.setDataSet(frpdv.reportSet);
    II5p.setColumnName("NAK_OSL_IZV_P");*/
    IIIv.setDataSet(frpdv.reportSet);
    IIIv.setColumnName("UKUPNO_III_V");
    IIIp.setDataSet(frpdv.reportSet);
    IIIp.setColumnName("UKUPNO_III_P");
    III1v.setDataSet(frpdv.reportSet);
    III1v.setColumnName("PPOR_PR_RAC_V");
    III1p.setDataSet(frpdv.reportSet);
    III1p.setColumnName("PPOR_PR_RAC_P");
    III2v.setDataSet(frpdv.reportSet);
    III2v.setColumnName("PL_PPOR_UVOZ_V");
    III2p.setDataSet(frpdv.reportSet);
    III2p.setColumnName("PL_PPOR_UVOZ_P");
    III3v.setDataSet(frpdv.reportSet);
    III3v.setColumnName("PL_PPOR_USLUGE_V");
    III3p.setDataSet(frpdv.reportSet);
    III3p.setColumnName("PL_PPOR_USLUGE_P");
    III4v.setDataSet(frpdv.reportSet);
    III4v.setColumnName("III4V");
    III4p.setDataSet(frpdv.reportSet);
    III4p.setColumnName("III4P");
    III5v.setDataSet(frpdv.reportSet);
    III5v.setColumnName("III5V");
    III5p.setDataSet(frpdv.reportSet);
    III5p.setColumnName("III5P");
    
    III6v.setDataSet(frpdv.reportSet);
    III6v.setColumnName("III6V");
    III6p.setDataSet(frpdv.reportSet);
    III6p.setColumnName("III6P");
    III7v.setDataSet(frpdv.reportSet);
    III7v.setColumnName("III7V");
    III7p.setDataSet(frpdv.reportSet);
    III7p.setColumnName("III7P");
    
    III8p.setDataSet(frpdv.reportSet);
    III8p.setColumnName("ISPRAVCI_PPORA");
    IV.setDataSet(frpdv.reportSet);
    IV.setColumnName("POR_OBV");
    V.setDataSet(frpdv.reportSet);
    V.setColumnName("PO_PRETHOD_OBR");
    VI.setDataSet(frpdv.reportSet);
    VI.setColumnName("RAZLIKA");
    
    POV.setDataSet(frpdv.reportSet);
    POV.setColumnName("POV");
    PRED.setDataSet(frpdv.reportSet);
    PRED.setColumnName("PRED");
    UST.setDataSet(frpdv.reportSet);
    UST.setColumnName("UST");
  }

  void disableDownwards(boolean enable){
    if (frpdv.calcPPDV()) {
      rcc.EnabDisabAll(jpS, enable);
      rcc.EnabDisabAll(jpI, enable);
      rcc.EnabDisabAll(jpII, enable);
      rcc.EnabDisabAll(jpIII, enable);
      rcc.EnabDisabAll(jpIV, enable);
  
      rcc.setLabelLaF(S_I_II, false);
      rcc.setLabelLaF(I,false);
      rcc.setLabelLaF(I2,false);
      rcc.setLabelLaF(IIv,false);
      rcc.setLabelLaF(IIp,false);
      rcc.setLabelLaF(IIIv,false);
      rcc.setLabelLaF(IIIp,false);
      rcc.setLabelLaF(IV,false);
      rcc.setLabelLaF(V,enable);
      rcc.setLabelLaF(VI,false);
      rcc.setLabelLaF(IV,false);
  //    rcc.setLabelLaF(V,false);
      rcc.setLabelLaF(VI,false);
    } else {
      rcc.EnabDisabAll(this, enable);
    }
  }
}