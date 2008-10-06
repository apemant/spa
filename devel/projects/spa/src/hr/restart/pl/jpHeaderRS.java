/****license*****************************************************************
**   file: jpHeaderRS.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpHeaderRS extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  StorageDataSet headerRS;
  StorageDataSet vrsteUpl = hr.restart.zapod.Sifrarnici.getSifre("PLVU");
  StorageDataSet vrsteObv = hr.restart.zapod.Sifrarnici.getSifre("PLVO");
  frmRS fRS;
//  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCsif = new JLabel();
  JLabel jlCobv = new JLabel();
  JLabel jlGodobr = new JLabel();
  JLabel jlOdd = new JLabel();
  JLabel jlRsind = new JLabel();
  JraCheckBox jcbDisketa = new JraCheckBox();
  JCheckBox jcbMjesecIspl = new JCheckBox();
  JraButton jbSelCsif = new JraButton();
  JraButton jbSelCvrob = new JraButton();
  JraTextField jraGodobr = new JraTextField();
  JraTextField jraMjobr = new JraTextField();
  JraTextField jraOdd = new JraTextField();
  JraTextField jraDod = new JraTextField();
  JraTextField jraRsind = new JraTextField() {
    public void valueChanged() {
      fRS.focLostRSInd();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fRS.focLostRSInd();
    }*/
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCsif = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrVrstaObv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrob = new JlrNavField() {
    public void after_lookUp() {
      boolean otkrij = raParam.getParam(vrsteObv,"PARAMETRI",1).equals("D")&&fRS.rsmode.equals("O");
      raCommonClass.getraCommonClass().setLabelLaF(jraMjobr,otkrij);
      raCommonClass.getraCommonClass().setLabelLaF(jraGodobr,otkrij);
    }
  };

  public jpHeaderRS(frmRS f) {
    try {
      fRS = f;
      initHeaderRS();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(136);
    setBorder(BorderFactory.createEtchedBorder());
    jbSelCsif.setText("...");
    jlCsif.setText("Vrsta uplate");
    jlCobv.setText("Vrsta obveznika");
    jlGodobr.setText("Godina i mjesec");
    jlOdd.setText("Za razdoblje (od - do)");
    jlRsind.setText("Identifikator");
    jraDod.setColumnName("DODANA");
    jraDod.setDataSet(headerRS);
    jraGodobr.setColumnName("GODINA");
    jraGodobr.setDataSet(headerRS);
    jraMjobr.setColumnName("MJESEC");
    jraMjobr.setDataSet(headerRS);
    jraOdd.setColumnName("ODDANA");
    jraOdd.setDataSet(headerRS);
    jraRsind.setColumnName("IDENTIFIKATOR");
    jraRsind.setDataSet(headerRS);

    jcbDisketa.setText("Podaci za disketu");
    jcbDisketa.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbDisketa.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcbDisketa_itemStateChanged(e);
      }
    });
    jcbMjesecIspl.setText("Mjesec isplate");
    jcbMjesecIspl.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbMjesecIspl.setSelected(true);
    jlrCsif.setColumnName("CSIF");
    jlrCsif.setDataSet(headerRS);
    jlrCsif.setColNames(new String[] {"NAZIV"});
    jlrCsif.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCsif.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCsif.setSearchMode(0);
    jlrCsif.setRaDataSet(vrsteUpl);
    jlrCsif.setNavButton(jbSelCsif);

    jlrOpis.setColumnName("NAZIV");
    jlrOpis.setNavProperties(jlrCsif);
    jlrOpis.setSearchMode(1);

    jlrCvrob.setColumnName("CVROB");
    jlrCvrob.setNavColumnName("CSIF");
    jlrCvrob.setDataSet(headerRS);
    jlrCvrob.setColNames(new String[] {"NAZIV"});
    jlrCvrob.setTextFields(new JTextComponent[] {jlrVrstaObv});
    jlrCvrob.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrob.setSearchMode(0);
    jlrCvrob.setRaDataSet(vrsteObv);
    jlrCvrob.setNavButton(jbSelCvrob);

    jlrVrstaObv.setColumnName("NAZIV");
    jlrVrstaObv.setNavProperties(jlrCvrob);
    jlrVrstaObv.setSearchMode(1);

    add(jbSelCsif, new XYConstraints(555, 20, 21, 21));
    add(jlCsif, new XYConstraints(15, 20, -1, -1));
    add(jlrOpis, new XYConstraints(255, 20, 295, -1));
    add(jlrCsif, new XYConstraints(150, 20, 100, -1));
    add(jbSelCvrob, new XYConstraints(555, 45, 21, 21));
    add(jlCobv, new XYConstraints(15, 45, -1, -1));
    add(jlrVrstaObv, new XYConstraints(255, 45, 295, -1));
    add(jlrCvrob, new XYConstraints(150, 45, 100, -1));
    add(jlGodobr, new XYConstraints(15, 70, -1, -1));
    add(jraGodobr, new XYConstraints(150, 70, 60, -1));
    add(jraMjobr, new XYConstraints(215, 70, 35, -1));
    add(jlOdd,    new XYConstraints(290, 70, -1, -1));
    add(jraOdd,  new XYConstraints(450, 70, 47, -1));
    add(jraDod,  new XYConstraints(502, 70, 48, -1));
    add(jraRsind,  new XYConstraints(150, 95, 60, -1));
    add(jlRsind,  new XYConstraints(15, 95, -1, -1));
    add(jcbDisketa,  new XYConstraints(290, 90, 200, -1));//95->85
    add(jcbMjesecIspl, new XYConstraints(290, 110, 200, -1));
  }
  private void initHeaderRS() {
    headerRS = new StorageDataSet();
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    headerRS.addColumn((Column)dm.getRSPeriodobr().getColumn("IDENTIFIKATOR").clone());
    headerRS.addColumn((Column)dm.getRSPeriodobr().getColumn("MJESEC").clone());
    headerRS.addColumn((Column)dm.getRSPeriodobr().getColumn("GODINA").clone());
    Column csif = (Column)dm.getSifrarnici().getColumn("CSIF").clone();
    csif.setPrecision(2);
    headerRS.addColumn((Column)dm.getSifrarnici().getColumn("CSIF").clone());
    headerRS.addColumn(dm.createStringColumn("CVROB", 2));
    headerRS.addColumn((Column)dm.getRSPeriodobr().getColumn("ODDANA").clone());
    headerRS.addColumn((Column)dm.getRSPeriodobr().getColumn("DODANA").clone());
    headerRS.open();
  }

  boolean disketa;

  void jcbDisketa_itemStateChanged(ItemEvent e) {
    disketa = jcbDisketa.isSelected();
  }

  public boolean isPodaciZaDisketu(){
    return disketa;
  }
  public boolean isMjesecIsplate() {
    return jcbMjesecIspl.isSelected();
  }
}