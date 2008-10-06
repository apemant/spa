/****license*****************************************************************
**   file: jpZemlje.java
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
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpZemlje extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmZemlje fZemlje;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCzemlje = new JLabel();
  JLabel jlDnevnica = new JLabel();
  JLabel jlLoco = new JLabel();
  JLabel jlNocenje = new JLabel();
  JLabel jlOznval = new JLabel();
  JLabel jlaLitbenz = new JLabel();
  JLabel jlaLoco = new JLabel();
  JraButton jbSelOznval = new JraButton();
  raComboBox rcbIndPuta = new raComboBox();
  JraTextField jraCzemlje = new JraTextField();
  JraTextField jraDnevnica = new JraTextField();
  JraTextField jraLitbenz = new JraTextField();
  JraTextField jraLoco = new JraTextField();
  JraTextField jraNazivzem = new JraTextField();
  JraTextField jraNocenje = new JraTextField();
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazval = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpZemlje(frmZemlje f) {
    try {
      fZemlje = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(591);
    lay.setHeight(155);

    jlCzemlje.setText("Zemlja");
    jlDnevnica.setText("Dnevnica");
    jlLoco.setText("Trošak prijevoza");
    jlNocenje.setText("No\u0107enje");
    jlOznval.setText("Valuta");
    jlaLitbenz.setHorizontalAlignment(SwingConstants.CENTER);
    jlaLitbenz.setText("Po l. benzina");
    jlaLoco.setHorizontalAlignment(SwingConstants.CENTER);
    jlaLoco.setText("Po km.");
    jraCzemlje.setColumnName("CZEMLJE");
    jraCzemlje.setDataSet(fZemlje.getRaQueryDataSet());
    jraDnevnica.setColumnName("DNEVNICA");
    jraDnevnica.setDataSet(fZemlje.getRaQueryDataSet());
    jraLitbenz.setColumnName("LITBENZ");
    jraLitbenz.setDataSet(fZemlje.getRaQueryDataSet());
    jraLoco.setColumnName("LOCO");
    jraLoco.setDataSet(fZemlje.getRaQueryDataSet());
    jraNazivzem.setColumnName("NAZIVZEM");
    jraNazivzem.setDataSet(fZemlje.getRaQueryDataSet());
    jraNocenje.setColumnName("NOCENJE");
    jraNocenje.setDataSet(fZemlje.getRaQueryDataSet());

    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setDataSet(fZemlje.getRaQueryDataSet());
    jlrOznval.setColNames(new String[] {"CVAL", "NAZVAL"});
    jlrOznval.setTextFields(new JTextComponent[] {jlrCval, jlrNazval});
    jlrOznval.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrOznval.setSearchMode(0);
    jlrOznval.setRaDataSet(dm.getValute());
    jlrOznval.setNavButton(jbSelOznval);

    jlrCval.setColumnName("CVAL");
    jlrCval.setNavProperties(jlrOznval);
    jlrCval.setSearchMode(1);

    jlrNazval.setColumnName("NAZVAL");
    jlrNazval.setNavProperties(jlrOznval);
    jlrNazval.setSearchMode(1);

    rcbIndPuta.setRaDataSet(fZemlje.getRaQueryDataSet());
    rcbIndPuta.setRaColumn("INDPUTA");

    rcbIndPuta.setRaItems(new String[][] {
      {"Inozemstvo","I"},
      {"Tuzemstvo","Z"},
    });


    jpDetail.add(jbSelOznval, new XYConstraints(555, 45, 21, 21));
    jpDetail.add(jlCzemlje, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlDnevnica, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlLoco, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlNocenje, new XYConstraints(360, 70, -1, -1));
    jpDetail.add(jlOznval, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlaLitbenz, new XYConstraints(256, 98, 98, -1));
    jpDetail.add(jlaLoco, new XYConstraints(151, 98, 98, -1));
    jpDetail.add(jlrCval, new XYConstraints(200, 45, 50, -1));
    jpDetail.add(jlrNazval, new XYConstraints(255, 45, 295, -1));
    jpDetail.add(jlrOznval, new XYConstraints(150, 45, 45, -1));
    jpDetail.add(jraCzemlje, new XYConstraints(150, 20, 45, -1));
    jpDetail.add(jraDnevnica, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraLitbenz, new XYConstraints(255, 115, 100, -1));
    jpDetail.add(jraLoco, new XYConstraints(150, 115, 100, -1));
    jpDetail.add(jraNazivzem, new XYConstraints(200, 20, 350, -1));
    jpDetail.add(jraNocenje, new XYConstraints(450, 70, 100, -1));
    jpDetail.add(rcbIndPuta,      new XYConstraints(450, 115, 100, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }

}
