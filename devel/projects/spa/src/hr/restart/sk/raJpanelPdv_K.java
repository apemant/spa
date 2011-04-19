/****license*****************************************************************
**   file: raJpanelPdv_K.java
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
import java.awt.Color;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raJpanelPdv_K extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  BorderLayout bLay = new BorderLayout();
  XYLayout xYLay = new XYLayout();
  JPanel jpVII = new JPanel();

  JraTextField VII = new JraTextField();
  JLabel jlVII = new JLabel("VII Ostali podaci");
  JraTextField VII1 = new JraTextField();
  JLabel jlVII1 = new JLabel(" 1. Za ispravak pretporeza");
  JraTextField VII11 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII11.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII11 = new JLabel("  1.1. Nab. nekret. - isporu\u010Ditelj nekret.");
  JraTextField VII12 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII12.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII12 = new JLabel("  1.2. Prod. nekret. - primatelj nekret.");
  JraTextField VII13 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII13.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII13 = new JLabel("  1.3. Nabava osobnih vozila");
  JraTextField VII14 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII14.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII14 = new JLabel("  1.4. Prodaja osobnih vozila");
  JraTextField VII15 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII15.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII15 = new JLabel("  1.5. Nabava rablj. osobnih vozila");
  
  JraTextField VII16 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII16.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII16 = new JLabel("  1.5 Nabava ostale dugotrajne imovine");
  JraTextField VII17 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII17.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII17 = new JLabel("  1.6. Prodaja ostale dugotrajne imovine");
  JraTextField VII2 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII2.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII2 = new JLabel(" 2. Otu\u0111enje/stjecanje gosp. cjelin. ili pogona");
  JraTextField VII3 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII3.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII3 = new JLabel(" 3. Nabava dobara i usluga za reprezentaciju");//" 3. Po\u010Detak i prestanak obavljanja djelatnosti");
  
  JraTextField VII4 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII4.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII4 = new JLabel(" 4. Nabava osobnih vozila i dr. te povezanih dobara i usluga");
  
  JraTextField VII5 = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VII5.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVII5 = new JLabel(" 5. Osn. za obr. vlast.potr.za os.vozila nabavljena do 31.12.2009.");
  
  JraTextField VIIPP = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (VIIPP.isValueChanged())
        frpdv.focusLostEvK();
    }
  };
  JLabel jlVIIPP = new JLabel("   Poèetak ili prestanak obavljanja djelatnosti");
  
  
  JTextArea jtNapomena = new JTextArea();

  frmPDV frpdv;
  Border border1;
  TitledBorder titledBorder1;

  public raJpanelPdv_K(frmPDV f) {
    frpdv = f;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(124, 124, 124),new Color(178, 178, 178));
    titledBorder1 = new TitledBorder(border1,"NAPOMENA");
    this.setLayout(bLay);
    jpVII.setLayout(xYLay);

    jpVII.setBorder(BorderFactory.createEtchedBorder());
    jtNapomena.setBorder(titledBorder1);
    jtNapomena.setDisabledTextColor(Color.white);
    jtNapomena.setLineWrap(true);
    jtNapomena.setWrapStyleWord(true);
    jpVII.add(VII, new XYConstraints(340, 5, 100 , -1));
    jpVII.add(jlVII, new XYConstraints(15, 5 ,-1,-1));
    jpVII.add(VII1, new XYConstraints(340, 40, 100 , -1));
    jpVII.add(jlVII1, new XYConstraints(15, 40 ,-1,-1));
    jpVII.add(VII11, new XYConstraints(340, 65, 100 , -1));
    jpVII.add(jlVII11, new XYConstraints(15, 65 ,-1,-1));
    jpVII.add(VII12, new XYConstraints(340, 90, 100 , -1));
    jpVII.add(jlVII12, new XYConstraints(15, 90 ,-1,-1));
    jpVII.add(VII13, new XYConstraints(340, 115, 100 , -1));
    jpVII.add(jlVII13, new XYConstraints(15, 115 ,-1,-1));
    jpVII.add(VII14, new XYConstraints(340, 140, 100 , -1));
    jpVII.add(jlVII14, new XYConstraints(15, 140 ,-1,-1));
    jpVII.add(VII15, new XYConstraints(340, 165, 100 , -1));
    jlVII15.setFont(jlVII15.getFont().deriveFont(java.awt.Font.ITALIC));
    jpVII.add(jlVII15, new XYConstraints(15, 165 ,-1,-1));
    jpVII.add(VII16, new XYConstraints(340, 190, 100 , -1));
    jpVII.add(jlVII16, new XYConstraints(15, 190 ,-1,-1));
    jpVII.add(VII17, new XYConstraints(340, 215, 100 , -1));
    jpVII.add(jlVII17, new XYConstraints(15, 215 ,-1,-1));
    jpVII.add(VII2, new XYConstraints(340, 245, 100 , -1));
    jpVII.add(jlVII2, new XYConstraints(15, 245 ,-1,-1));
    jpVII.add(VII3, new XYConstraints(340, 275, 100 , -1));
    jpVII.add(jlVII3, new XYConstraints(15, 275 ,-1,-1));
    
    jpVII.add(VII4, new XYConstraints(340, 305, 100 , -1));
    jpVII.add(jlVII4, new XYConstraints(15, 305 ,-1,-1));
    
    jpVII.add(VII5, new XYConstraints(340, 335, 100 , -1));
    jpVII.add(jlVII5, new XYConstraints(15, 335 ,-1,-1));
    
    jpVII.add(VIIPP, new XYConstraints(340, 365, 100 , -1));
    jpVII.add(jlVIIPP, new XYConstraints(15, 365 ,-1,-1));
    
    jpVII.add(jtNapomena, new XYConstraints(15,395,520,200));

    this.add(jpVII, BorderLayout.CENTER);
  }

  public void rebindFields() {
    VII.setDataSet(frpdv.reportSet);
    VII.setColumnName("UKUPNO_VII");
    VII1.setDataSet(frpdv.reportSet);
    VII1.setColumnName("UKUPNO_VII1");
    VII11.setDataSet(frpdv.reportSet);
    VII11.setColumnName("NAB_NEK_ISPOR");
    VII12.setDataSet(frpdv.reportSet);
    VII12.setColumnName("PRO_NEK_PRIM");
    VII13.setDataSet(frpdv.reportSet);
    VII13.setColumnName("NAB_OSOB_VOZIL");
    VII14.setDataSet(frpdv.reportSet);
    VII14.setColumnName("PRO_OSOB_VOZIL");
    VII15.setDataSet(frpdv.reportSet);
    VII15.setColumnName("NAB_RAB_VOZIL");
    VII16.setDataSet(frpdv.reportSet);
    VII16.setColumnName("NAB_OSTALO");
    VII17.setDataSet(frpdv.reportSet);
    VII17.setColumnName("PRO_OSTALO");
    VII2.setDataSet(frpdv.reportSet);
    VII2.setColumnName("OTUDJ_STJEC");
    
    VII3.setDataSet(frpdv.reportSet);
    VII3.setColumnName("VII3");
    
    VII4.setDataSet(frpdv.reportSet);
    VII4.setColumnName("VII4");
    
    VII5.setDataSet(frpdv.reportSet);
    VII5.setColumnName("VII5");
        
    VIIPP.setDataSet(frpdv.reportSet);
    VIIPP.setColumnName("POC_I_PREST");
  }

  void disableDownwards(boolean enable){
    if (frpdv.calcPPDV()) {
      rcc.EnabDisabAll(jpVII, enable);
      rcc.setLabelLaF(VII,false);
      rcc.setLabelLaF(VII1,false);
    } else {
      rcc.EnabDisabAll(this, enable);
    }
  }

  public String getNapomena(){
    return jtNapomena.getText();
  }

  public void clearNapomena(){
    jtNapomena.setText("");
  }
}