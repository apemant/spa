/****license*****************************************************************
**   file: jpParamBLPN.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpParamBLPN extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmParamBLPN fParamBLPN;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlCskl = new JLabel();
  JLabel jlPnf = new JLabel();
  JLabel jlStavkaf = new JLabel();
  JLabel jlStavkapni = new JLabel();
  JLabel jlStavkapnz = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCskl = new JraButton();
  JraButton jbSelStavkapni = new JraButton();
  JraButton jbSelStavkapnz = new JraButton();
  JraTextField jraPnf = new JraTextField();
  JraTextField jraStavkaf = new JraTextField();


  JlrNavField jlrOpispni = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrStavkapni = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
//      System.out.println("fParamBLPN.getMode()  " + fParamBLPN.getMode());
      fParamBLPN.lookThis(fParamBLPN.getMode());
    }
  };
  JlrNavField jlrStavkapnz = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpisvrsk = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpispnz = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpParamBLPN(frmParamBLPN f) {
    try {
      fParamBLPN = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(185);

    jlCorg.setText("Knjigovodstvo");
    jlCskl.setText("Vrsta stavke");
    jlPnf.setText("Footer putnog naloga");
    jlStavkaf.setText("Footer upl/ispl");
    jlStavkapni.setText("PN u inozemstvu");
    jlStavkapnz.setText("PN u zemlji");

    jraPnf.setColumnName("PNF");
    jraPnf.setDataSet(fParamBLPN.getRaQueryDataSet());
    jraStavkaf.setColumnName("STAVKAF");
    jraStavkaf.setDataSet(fParamBLPN.getRaQueryDataSet());

    jlrCorg.setColumnName("KNJIG");
    jlrCorg.setNavColumnName("CORG");
    jlrCorg.setDataSet(fParamBLPN.getRaQueryDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setNavColumnName("CVRSK");
    jlrCskl.setDataSet(fParamBLPN.getRaQueryDataSet());
    jlrCskl.setColNames(new String[] {"OPISVRSK"});
    jlrCskl.setTextFields(new JTextComponent[] {jlrOpisvrsk});
    jlrCskl.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(fParamBLPN.vrshemek);
    jlrCskl.setNavButton(jbSelCskl);

    jlrOpisvrsk.setColumnName("OPISVRSK");
    jlrOpisvrsk.setNavProperties(jlrCskl);
    jlrOpisvrsk.setSearchMode(1);

    jlrStavkapnz.setColumnName("STAVKAPNZ");
    jlrStavkapnz.setNavColumnName("STAVKA");
    jlrStavkapnz.setDataSet(fParamBLPN.getRaQueryDataSet());
    jlrStavkapnz.setColNames(new String[] {"OPIS"});
    jlrStavkapnz.setTextFields(new JTextComponent[] {jlrOpispnz});
    jlrStavkapnz.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrStavkapnz.setSearchMode(0);
//    jlrStavkapnz.setRaDataSet(dm.getShkonta());
    jlrStavkapnz.setNavButton(jbSelStavkapnz);

    jlrOpispnz.setColumnName("OPIS");
    jlrOpispnz.setNavProperties(jlrStavkapnz);
    jlrOpispnz.setSearchMode(1);

    jlrStavkapni.setColumnName("STAVKAPNI");
    jlrStavkapni.setNavColumnName("STAVKA");
    jlrStavkapni.setDataSet(fParamBLPN.getRaQueryDataSet());
    jlrStavkapni.setColNames(new String[] {"OPIS"});
    jlrStavkapni.setTextFields(new JTextComponent[] {jlrOpispni});
    jlrStavkapni.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrStavkapni.setSearchMode(0);
//    jlrStavkapni.setRaDataSet(dm.getShkonta());
    jlrStavkapni.setNavButton(jbSelStavkapni);

    jlrOpispni.setColumnName("OPIS");
    jlrOpispni.setNavProperties(jlrStavkapni);
    jlrOpispni.setSearchMode(1);

    jpDetail.add(jbSelCskl, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jbSelStavkapni, new XYConstraints(555, 95, 21, 21));
    jpDetail.add(jbSelStavkapnz, new XYConstraints(555, 70, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jbSelCorg, new XYConstraints(555, 20, 21, 21));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 295, -1));
    jpDetail.add(jlCskl, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlPnf, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlStavkaf, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlStavkapni, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlStavkapnz, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrCskl, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrOpispni, new XYConstraints(255, 95, 295, -1));
    jpDetail.add(jlrOpispnz, new XYConstraints(255, 70, 295, -1));
    jpDetail.add(jlrOpisvrsk, new XYConstraints(255, 45, 295, -1));
    jpDetail.add(jlrStavkapni, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jlrStavkapnz, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraPnf, new XYConstraints(150, 145, 400, -1));
    jpDetail.add(jraStavkaf, new XYConstraints(150, 120, 400, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
