/****license*****************************************************************
**   file: jpExternalReports.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpExternalReports extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmExternalReports fExternalReports;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlApp = new JLabel();
  JLabel jlIme = new JLabel();
  JLabel jlUrl = new JLabel();
  JraButton jbSelApp = new JraButton();
  JraButton jbSelUrl = new JraButton();
  JraTextField jraIme = new JraTextField();
  JraTextField jraNaslov = new JraTextField();
  JraTextField jraUrl = new JraTextField();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpExternalReports(frmExternalReports f) {
    try {
      fExternalReports = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraIme.setDataSet(ds);
    jraNaslov.setDataSet(ds);
    jraUrl.setDataSet(ds);
    jlrApp.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(110);

    jbSelApp.setText("...");
    jlApp.setText("Aplikacija");
    jlIme.setText("Ime");
    jlUrl.setText("URL");
    jraIme.setColumnName("IME");
    jraNaslov.setColumnName("NASLOV");
    jraUrl.setColumnName("URL");

    jlrApp.setColumnName("APP");
    jlrApp.setColNames(new String[] {"OPIS"});
    jlrApp.setTextFields(new JTextComponent[] {jlrOpis});
    jlrApp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrApp.setSearchMode(0);
    jlrApp.setRaDataSet(dm.getAplikacija());
    jlrApp.setNavButton(jbSelApp);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrApp);
    jlrOpis.setSearchMode(1);

    jbSelUrl.setText("...");

    jpDetail.add(jbSelApp, new XYConstraints(555, 70, 21, 21));
    jpDetail.add(jlApp, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlIme, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlUrl, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrApp, new XYConstraints(150, 70, 75, -1));
    jpDetail.add(jlrOpis, new XYConstraints(230, 70, 320, -1));
    jpDetail.add(jraIme, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraNaslov, new XYConstraints(230, 20, 320, -1));
    jpDetail.add(jraUrl, new XYConstraints(150, 45, 400, -1));
    jpDetail.add(jbSelUrl, new XYConstraints(555, 45, 21, 21));

    jbSelUrl.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fExternalReports.selectUrl();
      }
    });
    jraUrl.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F9)
          fExternalReports.selectUrl();
      }
    });
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
