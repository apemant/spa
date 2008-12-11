/****license*****************************************************************
**   file: jpFondSati.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpFondSati extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmFondSati fFondSati;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlMjesec = new JLabel();
  JLabel jlSatirada = new JLabel();
  JLabel jlSatiuk = new JLabel();
//  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraMjesec = new JraTextField();
  JraTextField jraSatipraz = new JraTextField() {
    public void valueChanged() {
      jraSatipraz_focusLost(null);
    }
  };
  JraTextField jraSatirad = new JraTextField() {
    public void valueChanged() {
      jraSatirad_focusLost(null);
    }
  };
  JraTextField jraSatiuk = new JraTextField();
  JraTextField jraMinpl = new JraTextField();
  JLabel jlSatipraz = new JLabel();

  public jpFondSati(frmFondSati f) {
    try {
      fFondSati = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);

    jlMjesec.setText("Mjesec");
    jlSatirada.setText("Sati rada");
    jlSatiuk.setText("Sati ukupno");
//    jcbAktiv.setColumnName("AKTIV");
//    jcbAktiv.setDataSet(fFondSati.getRaQueryDataSet());
//    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
//    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbAktiv.setSelectedDataValue("D");
//    jcbAktiv.setText("Aktivan");
//    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraMjesec.setColumnName("MJESEC");
    jraMjesec.setDataSet(fFondSati.getRaQueryDataSet());
    jraMjesec.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jraMjesec_keyPressed(e);
      }
    });
    jraSatipraz.setColumnName("SATIPRAZ");
    jraSatipraz.setDataSet(fFondSati.getRaQueryDataSet());
    /*jraSatipraz.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraSatipraz_focusLost(e);
      }
    });*/
    jraSatirad.setColumnName("SATIRAD");
    jraSatirad.setDataSet(fFondSati.getRaQueryDataSet());
    /*jraSatirad.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraSatirad_focusLost(e);
      }
    });*/
    jraSatiuk.setColumnName("SATIUK");
    jraSatiuk.setDataSet(fFondSati.getRaQueryDataSet());
    jraMinpl.setColumnName("MINPL");
    jraMinpl.setDataSet(fFondSati.getRaQueryDataSet());


    jlSatipraz.setText("Sati praznika");
    lay.setWidth(550);
    lay.setHeight(110);
    jpDetail.add(jlMjesec, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlSatirada,   new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlSatiuk,  new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraMjesec,   new XYConstraints(150, 20, 50, -1));
    jpDetail.add(jraSatirad,   new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraSatiuk,  new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraMinpl,  new XYConstraints(430, 70, 100, -1));
    jpDetail.add(jraSatipraz,              new XYConstraints(430, 45, 100, -1));
    jpDetail.add(new JLabel("Olakšica U mj."),  new XYConstraints(320, 70, 100, -1));
    jpDetail.add(jlSatipraz,      new XYConstraints(320, 45, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
//    jpDetail.add(jcbAktiv,     new XYConstraints(460, 20, 70, -1));
  }

  void jraSatirad_focusLost(FocusEvent e) {
    BigDecimal sum = new BigDecimal(0);
    sum = fFondSati.getRaQueryDataSet().getBigDecimal("SATIRAD").add(fFondSati.getRaQueryDataSet().getBigDecimal("SATIPRAZ"));
    fFondSati.getRaQueryDataSet().setBigDecimal("SATIUK", sum);
  }

  void jraSatipraz_focusLost(FocusEvent e) {
    BigDecimal sum = new BigDecimal(0);
    sum = fFondSati.getRaQueryDataSet().getBigDecimal("SATIRAD").add(fFondSati.getRaQueryDataSet().getBigDecimal("SATIPRAZ"));
    fFondSati.getRaQueryDataSet().setBigDecimal("SATIUK", sum);
  }

  void jraMjesec_keyPressed(KeyEvent e) {
    if(jraMjesec.getBackground()!= Color.white)
      jraMjesec.setBackground(Color.white);
  }


}
