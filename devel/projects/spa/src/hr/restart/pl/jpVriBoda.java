/****license*****************************************************************
**   file: jpVriBoda.java
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
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVriBoda extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVriBoda fVriBoda;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBod = new JLabel();
  JLabel jlMjesec = new JLabel();
//  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraBod = new JraTextField();
  JraTextField jraMjesec = new JraTextField();

  public jpVriBoda(frmVriBoda f) {
    try {
      fVriBoda = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
//    lay.setWidth(520);
//    lay.setHeight(75);

    jlBod.setText("Vrijednost boda");
    jlMjesec.setText("Mjesec");
//    jcbAktiv.setColumnName("AKTIV");
//    jcbAktiv.setDataSet(fVriBoda.getRaQueryDataSet());
//    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
//    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbAktiv.setSelectedDataValue("D");
//    jcbAktiv.setText("Aktivan");
//    jcbAktiv.setUnselectedDataValue("N");
    jraBod.setColumnName("BOD");
    jraBod.setDataSet(fVriBoda.getRaQueryDataSet());
    jraMjesec.setColumnName("MJOBR");
    jraMjesec.setDataSet(fVriBoda.getRaQueryDataSet());
    jraMjesec.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jraMjesec_keyPressed(e);
      }
    });

    lay.setWidth(570);
    lay.setHeight(70);
    jpDetail.add(jlMjesec,    new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jraMjesec,    new XYConstraints(150, 25, 50, -1));
    jpDetail.add(jlBod,        new XYConstraints(315, 25, -1, -1));
    jpDetail.add(jraBod,              new XYConstraints(450, 25, 100, -1));
//    jpDetail.add(jcbAktiv,         new XYConstraints(480, 10, 70, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }

  void jraMjesec_keyPressed(KeyEvent e) {
    if(jraMjesec.getBackground()!= Color.white)
      jraMjesec.setBackground(Color.white);
  }
}
