/****license*****************************************************************
**   file: jpDefPDVDetail.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpDefPDVDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmDefPDV fDefPDV;
  JPanel jpDetail = new JPanel();
  JPanel jpKnjiga = new JPanel();

  XYLayout lay = new XYLayout();
  XYLayout xyK = new XYLayout();
  JLabel jlCknjige = new JLabel();
  JLabel jlCkolone = new JLabel();
  JraButton jbSelCknjige = new JraButton();
  JraButton jbSelCkolone = new JraButton();
  JlrNavField jlrNazknjige = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkolone = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCkolone = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCknjige = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  raButtonGroup rbgURAIRA = new raButtonGroup();
  JraRadioButton jrbURA = new JraRadioButton();
  JraRadioButton jrbIRA = new JraRadioButton();

  public jpDefPDVDetail(frmDefPDV md) {
    try {
      fDefPDV = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(165);

    rbgURAIRA.setColumnName("URAIRA");
    rbgURAIRA.setDataSet(fDefPDV.getDetailSet());

    rbgURAIRA.add(jrbURA, "Ulaznih ra\u010Duna","U");
    rbgURAIRA.add(jrbIRA,"Izlaznih ra\u010Duna","I");
    rbgURAIRA.setHorizontalTextPosition(SwingConstants.TRAILING);

    jbSelCknjige.setText("...");
    jbSelCkolone.setText("...");
    jlCknjige.setText("Oznaka knjige");
    jlCkolone.setText("Broj kolone");

    jlrCknjige.setColumnName("CKNJIGE");
    jlrCknjige.setDataSet(fDefPDV.getDetailSet());
    jlrCknjige.setColNames(new String[] {"NAZKNJIGE"});
    jlrCknjige.setTextFields(new JTextComponent[] {jlrNazknjige});
    jlrCknjige.setVisCols(new int[] {0, 4});
    jlrCknjige.setSearchMode(0);
    jlrCknjige.setRaDataSet(dm.getKnjigeUI());
    jlrCknjige.setNavButton(jbSelCknjige);

    jlrNazknjige.setColumnName("NAZKNJIGE");
    jlrNazknjige.setNavProperties(jlrCknjige);
    jlrNazknjige.setSearchMode(1);

    jlrCkolone.setColumnName("CKOLONE");
    jlrCkolone.setDataSet(fDefPDV.getDetailSet());
    jlrCkolone.setColNames(new String[] {"NAZIVKOLONE"});
    jlrCkolone.setTextFields(new JTextComponent[] {jlrNazivkolone});
    jlrCkolone.setVisCols(new int[] {0, 1});
    jlrCkolone.setSearchMode(0);
    jlrCkolone.setRaDataSet(dm.getKoloneknjUI());
    jlrCkolone.setNavButton(jbSelCkolone);

    jlrNazivkolone.setColumnName("NAZIVKOLONE");
    jlrNazivkolone.setNavProperties(jlrCkolone);
    jlrNazivkolone.setSearchMode(1);

    jpKnjiga.setLayout(xyK);
    xyK.setWidth(395);
    xyK.setHeight(25);
    jpKnjiga.setBorder(BorderFactory.createEtchedBorder());
    jpKnjiga.add(jrbURA, new XYConstraints(25, 0, 150, -1));
    jpKnjiga.add(jrbIRA, new XYConstraints(200, 0, 150, -1));

    jpDetail.add(jpKnjiga, new XYConstraints(150, 10, -1, -1));

    jpDetail.add(jlCknjige, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCknjige, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrNazknjige, new XYConstraints(255, 45, 295, -1));
    jpDetail.add(jbSelCknjige, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jlCkolone, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrCkolone, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlrNazivkolone, new XYConstraints(255, 70, 295, -1));
    jpDetail.add(jbSelCkolone, new XYConstraints(555, 70, 21, 21));

    this.add(jpDetail, BorderLayout.CENTER);

    jrbURA.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbURA.isSelected())
          uraSelected();
      }
    });

    jrbIRA.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbIRA.isSelected())
          iraSelected();
      }
    });
  }

  int lastui = 0;
  boolean izmjena = false;

  private void uraSelected() {
    if (fDefPDV.raDetail.getMode() == 'B') lastui = 0;
    if (lastui != 1 && fDefPDV.raDetail.getMode() != 'B') {
      lastui = 1;
      jlrCknjige.setRaDataSet(dm.getKnjigeU());
      jlrCkolone.setRaDataSet(dm.getUlazneKolone());
      if ((fDefPDV.raDetail.getMode() == 'N' || izmjena)){
        jlrCknjige.setText("");
        jlrCknjige.forceFocLost();
        jlrCkolone.setText("");
        jlrCkolone.forceFocLost();
        rbgURAIRA.setSelected(jrbURA);
      }
    }
    izmjena = fDefPDV.raDetail.getMode() == 'I';
  }

  private void iraSelected() {
    if (fDefPDV.raDetail.getMode() == 'B') lastui = 0;
    if (lastui != 2 && fDefPDV.raDetail.getMode() != 'B') {
      lastui = 2;
      jlrCknjige.setRaDataSet(dm.getKnjigeI());
      jlrCkolone.setRaDataSet(dm.getIzlazneKolone());
      if ((fDefPDV.raDetail.getMode() == 'N') || izmjena){
        jlrCknjige.setText("");
        jlrCknjige.forceFocLost();
        jlrCkolone.setText("");
        jlrCkolone.forceFocLost();
        rbgURAIRA.setSelected(jrbIRA);
      }
    }
    izmjena = fDefPDV.raDetail.getMode() == 'I';
  }

  int lastUIrebinder = 0;

  public void rebinder(String UorI){
    if (UorI.equals("U") && lastUIrebinder != 1){
      lastUIrebinder = 1;
      jlrCknjige.setRaDataSet(dm.getKnjigeU());
      jlrCkolone.setRaDataSet(dm.getUlazneKolone());
    } else if(UorI.equals("I") && lastUIrebinder != 2){
      lastUIrebinder = 2;
      jlrCknjige.setRaDataSet(dm.getKnjigeI());
      jlrCkolone.setRaDataSet(dm.getIzlazneKolone());
    }
  }
}