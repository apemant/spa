/****license*****************************************************************
**   file: jpMjesta.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMjesta extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmMjesta fMjesta;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCmjesta = new JLabel();
  JLabel jlCzem = new JLabel();
  JLabel jlCzup = new JLabel();
  JLabel jlaCmjesta = new JLabel();
  JLabel jlaNazmjesta = new JLabel();
  JLabel jlaPbr = new JLabel();
  JraButton jbSelCzem = new JraButton();
  JraButton jbSelCzup = new JraButton();
  JraTextField jraCmjesta = new JraTextField();
  JraTextField jraNazmjesta = new JraTextField();
  JraTextField jraPbr = new JraTextField();
  JlrNavField jlrCzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOznzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivzup = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCzup = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpMjesta(frmMjesta f) {
    try {
      fMjesta = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCmjesta.setDataSet(ds);
    jraNazmjesta.setDataSet(ds);
    jraPbr.setDataSet(ds);
    jlrCzem.setDataSet(ds);
    jlrCzup.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(601);
    lay.setHeight(120);

    jlCmjesta.setText("Mjesto");
    jlCzem.setText("Zemlja");
    jlCzup.setText("Županija");
    jlaCmjesta.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaCmjesta.setText("Šifra");
    jlaNazmjesta.setHorizontalAlignment(SwingConstants.LEADING);
    jlaNazmjesta.setText("Naziv");
    jlaPbr.setHorizontalAlignment(SwingConstants.LEADING);
    jlaPbr.setText("Poštanski broj");
    jraCmjesta.setColumnName("CMJESTA");
    jraNazmjesta.setColumnName("NAZMJESTA");
    jraPbr.setColumnName("PBR");

    jlrCzup.setColumnName("CZUP");
    jlrCzup.setColNames(new String[] {"NAZIVZUP"});
    jlrCzup.setTextFields(new JTextComponent[] {jlrNazivzup});
    jlrCzup.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCzup.setSearchMode(0);
    jlrCzup.setRaDataSet(dm.getZupanije());
    jlrCzup.setNavButton(jbSelCzup);

    jlrNazivzup.setColumnName("NAZIVZUP");
    jlrNazivzup.setNavProperties(jlrCzup);
    jlrNazivzup.setSearchMode(1);

    jlrCzem.setColumnName("CZEM");
    jlrCzem.setColNames(new String[] {"OZNZEM", "NAZIVZEM"});
    jlrCzem.setTextFields(new JTextComponent[] {jlrOznzem, jlrNazivzem});
    jlrCzem.setVisCols(new int[] {2, 0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCzem.setSearchMode(0);
    jlrCzem.setRaDataSet(dm.getZpZemlje());
    jlrCzem.setNavButton(jbSelCzem);

    jlrOznzem.setColumnName("OZNZEM");
    jlrOznzem.setNavProperties(jlrCzem);
    jlrOznzem.setSearchMode(1);

    jlrNazivzem.setColumnName("NAZIVZEM");
    jlrNazivzem.setNavProperties(jlrCzem);
    jlrNazivzem.setSearchMode(1);

    jpDetail.add(jbSelCzem, new XYConstraints(565, 55, 21, 21));
    jpDetail.add(jlCmjesta, new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlCzem, new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlaCmjesta, new XYConstraints(151, 13, 72, -1));
    jpDetail.add(jlaNazmjesta, new XYConstraints(231, 13, 223, -1));
    jpDetail.add(jlaPbr, new XYConstraints(461, 13, 98, -1));
    jpDetail.add(jlrCzem, new XYConstraints(230, 55, 75, -1));
    jpDetail.add(jlrNazivzem, new XYConstraints(310, 55, 250, -1));
    jpDetail.add(jlrOznzem, new XYConstraints(150, 55, 75, -1));
    jpDetail.add(jraCmjesta, new XYConstraints(150, 30, 75, -1));
    jpDetail.add(jraNazmjesta, new XYConstraints(230, 30, 225, -1));
    jpDetail.add(jraPbr, new XYConstraints(460, 30, 100, -1));
    jpDetail.add(jbSelCzup, new XYConstraints(565, 80, 21, 21));
    jpDetail.add(jlCzup, new XYConstraints(15, 80, -1, -1));
    jpDetail.add(jlrCzup, new XYConstraints(150, 80, 75, -1));
    jpDetail.add(jlrNazivzup, new XYConstraints(230, 80, 330, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
