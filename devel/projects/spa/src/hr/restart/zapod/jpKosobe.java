/****license*****************************************************************
**   file: jpKosobe.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKosobe extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmKosobe fKosobe;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlIme = new JLabel();
  JLabel jlNapomena = new JLabel();
  JLabel jlTel = new JLabel();
  JLabel jlFax = new JLabel();
  JLabel jlEmail = new JLabel();
  JraTextField jraIme = new JraTextField();
  JraTextField jraNapomena = new JraTextField();
  JraTextField jraTel = new JraTextField();
  JraTextField jraFax = new JraTextField();
  JraTextField jraEmail = new JraTextField();
  
  

  public jpKosobe(frmKosobe f) {
    try {
      fKosobe = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(565);
    lay.setHeight(160);

    jlIme.setText("Ime");
    jlNapomena.setText("Napomena");
    jlTel.setText("Telefon");
    jlFax.setText("Fax");
    jlEmail.setText("E-mail");
    jraIme.setColumnName("IME");
    jraIme.setDataSet(fKosobe.getRaQueryDataSet());
    jraNapomena.setColumnName("NAPOMENA");
    jraNapomena.setDataSet(fKosobe.getRaQueryDataSet());
    jraTel.setColumnName("TEL");
    jraTel.setDataSet(fKosobe.getRaQueryDataSet());
    jraFax.setColumnName("FAX");
    jraFax.setDataSet(fKosobe.getRaQueryDataSet());
    jraEmail.setColumnName("EMAIL");
    jraEmail.setDataSet(fKosobe.getRaQueryDataSet());
    

    jpDetail.add(jlIme, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlNapomena, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlTel, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlFax, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlEmail, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraIme, new XYConstraints(150, 20, 400, -1));
    jpDetail.add(jraNapomena, new XYConstraints(150, 120, 400, -1));
    jpDetail.add(jraTel, new XYConstraints(150, 45, 400, -1));
    jpDetail.add(jraFax, new XYConstraints(150, 70, 400, -1));
    jpDetail.add(jraEmail, new XYConstraints(150, 95, 400, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
