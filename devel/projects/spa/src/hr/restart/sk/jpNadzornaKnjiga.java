/****license*****************************************************************
**   file: jpNadzornaKnjiga.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpNadzornaKnjiga extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmNadzornaKnjiga fNadzornaKnjiga;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrisprave = new JLabel();
  JLabel jlBrojjcd = new JLabel();
  JLabel jlBrojrjes = new JLabel();
  JLabel jlCnacpl = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlCparposr = new JLabel();
  JLabel jlDatumpl = new JLabel();
  JLabel jlProvizija = new JLabel();
  JLabel jlRbr = new JLabel();
  JLabel jlStarirbr = new JLabel();
  JLabel jlVrijednost = new JLabel();
  JLabel jlZempodrijetla = new JLabel();
  JraButton jbSelCnacpl = new JraButton();
  JraButton jbSelCpar = new JraButton();
  JraButton jbSelCparposr = new JraButton();
  JraButton jbSelZempodrijetla = new JraButton();
  JraTextField jraBrisprave = new JraTextField();
  JraTextField jraBrojjcd = new JraTextField();
  JraTextField jraBrojrjes = new JraTextField();
  JraTextField jraBrstr = new JraTextField();
  JraTextField jraDatdok = new JraTextField();
  JraTextField jraDatum = new JraTextField();
  JraTextField jraDatumjcd = new JraTextField();
  JraTextField jraDatumpl = new JraTextField();
  JraTextField jraDatumrjes = new JraTextField();
  JraTextField jraIznospl = new JraTextField();
  JraTextField jraProvizija = new JraTextField();
  JraTextField jraRbr = new JraTextField();
  JraTextField jraStaragod = new JraTextField();
  JraTextField jraStarirbr = new JraTextField();
  JraTextField jraVrijednost = new JraTextField();
  JlrNavField jlrCparposr = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaznacpl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCnacpl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivzem = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrZempodrijetla = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpNadzornaKnjiga(frmNadzornaKnjiga f) {
    try {
      fNadzornaKnjiga = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraBrisprave.setDataSet(ds);
    jraBrojjcd.setDataSet(ds);
    jraBrojrjes.setDataSet(ds);
    jraBrstr.setDataSet(ds);
    jraDatdok.setDataSet(ds);
    jraDatum.setDataSet(ds);
    jraDatumjcd.setDataSet(ds);
    jraDatumpl.setDataSet(ds);
    jraDatumrjes.setDataSet(ds);
    jraIznospl.setDataSet(ds);
    jraProvizija.setDataSet(ds);
    jraRbr.setDataSet(ds);
    jraStaragod.setDataSet(ds);
    jraStarirbr.setDataSet(ds);
    jraVrijednost.setDataSet(ds);
    jlrCparposr.setDataSet(ds);
    jlrCnacpl.setDataSet(ds);
    jlrCpar.setDataSet(ds);
    jlrZempodrijetla.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(606);
    lay.setHeight(335);

    jbSelCnacpl.setText("...");
    jbSelCpar.setText("...");
    jbSelCparposr.setText("...");
    jbSelZempodrijetla.setText("...");
    jlBrisprave.setText("Oznaka i datum isprave");
    jlBrojjcd.setText("Broj / datum JCD");
    jlBrojrjes.setText("Oznaka i datum rješenja");
    jlCnacpl.setText("Na\u010Din pla\u0107anja");
    jlCpar.setText("Tvrtka s kojom je sklopljen posao");
    jlCparposr.setText("U \u010Dije ime je sklopljen posao");
    jlDatumpl.setText("Datum / iznos pla\u0107anja");
    jlProvizija.setText("Postotak provizije");
    jlRbr.setText("RBR / datum / br.stranice unosa");
    jlStarirbr.setText("RBR / godina prijašnjeg unosa");
    jlVrijednost.setText("Vrijednost posla");
    jlZempodrijetla.setText("Zemlja podrijetla robe");
    jraBrisprave.setColumnName("BRISPRAVE");
    jraBrojjcd.setColumnName("BROJJCD");
    jraBrojrjes.setColumnName("BROJRJES");
    jraBrstr.setColumnName("BRSTR");
    jraDatdok.setColumnName("DATDOK");
    jraDatum.setColumnName("DATUM");
    jraDatumjcd.setColumnName("DATUMJCD");
    jraDatumpl.setColumnName("DATUMPL");
    jraDatumrjes.setColumnName("DATUMRJES");
    jraIznospl.setColumnName("IZNOSPL");
    jraProvizija.setColumnName("PROVIZIJA");
    jraRbr.setColumnName("RBR");
    jraStaragod.setColumnName("STARAGOD");
    jraStarirbr.setColumnName("STARIRBR");
    jraVrijednost.setColumnName("VRIJEDNOST");

    jlrCparposr.setColumnName("CPARPOSR");
    jlrCparposr.setNavColumnName("CPAR");
    jlrCparposr.setColNames(new String[] {"NAZPAR"});
    jlrCparposr.setTextFields(new JTextComponent[] {jlrNazpar1});
    jlrCparposr.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCparposr.setSearchMode(0);
    jlrCparposr.setRaDataSet(dm.getPartneri());
    jlrCparposr.setNavButton(jbSelCparposr);

    jlrNazpar1.setColumnName("NAZPAR");
    jlrNazpar1.setNavProperties(jlrCparposr);
    jlrNazpar1.setSearchMode(1);

    jlrCnacpl.setColumnName("CNACPL");
    jlrCnacpl.setColNames(new String[] {"NAZNACPL"});
    jlrCnacpl.setTextFields(new JTextComponent[] {jlrNaznacpl});
    jlrCnacpl.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCnacpl.setSearchMode(0);
    jlrCnacpl.setRaDataSet(dm.getNacpl());
    jlrCnacpl.setNavButton(jbSelCnacpl);

    jlrNaznacpl.setColumnName("NAZNACPL");
    jlrNaznacpl.setNavProperties(jlrCnacpl);
    jlrNaznacpl.setSearchMode(1);

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlrZempodrijetla.setColumnName("ZEMPODRIJETLA");
    jlrZempodrijetla.setNavColumnName("OZNZEM");
    jlrZempodrijetla.setColNames(new String[] {"NAZIVZEM"});
    jlrZempodrijetla.setTextFields(new JTextComponent[] {jlrNazivzem});
    jlrZempodrijetla.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrZempodrijetla.setSearchMode(0);
    jlrZempodrijetla.setRaDataSet(dm.getZpZemlje());
    jlrZempodrijetla.setNavButton(jbSelZempodrijetla);

    jlrNazivzem.setColumnName("NAZIVZEM");
    jlrNazivzem.setNavProperties(jlrZempodrijetla);
    jlrNazivzem.setSearchMode(1);

    jraBrisprave.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F9) {
          fNadzornaKnjiga.getURE(jraBrisprave.getText());
        }
      }
    });
    
    jpDetail.add(jbSelCnacpl, new XYConstraints(570, 195, 21, 21));
    jpDetail.add(jbSelCpar, new XYConstraints(570, 70, 21, 21));
    jpDetail.add(jbSelCparposr, new XYConstraints(570, 120, 21, 21));
    jpDetail.add(jbSelZempodrijetla, new XYConstraints(570, 295, 21, 21));
    jpDetail.add(jlBrisprave, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlBrojjcd, new XYConstraints(15, 270, -1, -1));
    jpDetail.add(jlBrojrjes, new XYConstraints(15, 220, -1, -1));
    jpDetail.add(jlCnacpl, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlCparposr, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlDatumpl, new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jlProvizija, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlRbr, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlStarirbr, new XYConstraints(15, 245, -1, -1));
    jpDetail.add(jlVrijednost, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlZempodrijetla, new XYConstraints(15, 295, -1, -1));
    jpDetail.add(jlrCnacpl, new XYConstraints(250, 195, 100, -1));
    jpDetail.add(jlrCpar, new XYConstraints(250, 70, 100, -1));
    jpDetail.add(jlrCparposr, new XYConstraints(250, 120, 100, -1));
    jpDetail.add(jlrNazivzem, new XYConstraints(355, 295, 210, -1));
    jpDetail.add(jlrNaznacpl, new XYConstraints(355, 195, 210, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(355, 70, 210, -1));
    jpDetail.add(jlrNazpar1, new XYConstraints(355, 120, 210, -1));
    jpDetail.add(jlrZempodrijetla, new XYConstraints(250, 295, 100, -1));
    jpDetail.add(jraBrisprave, new XYConstraints(250, 45, 210, -1));
    jpDetail.add(jraBrojjcd, new XYConstraints(250, 270, 210, -1));
    jpDetail.add(jraBrojrjes, new XYConstraints(250, 220, 210, -1));
    jpDetail.add(jraBrstr, new XYConstraints(465, 20, 100, -1));
    jpDetail.add(jraDatdok, new XYConstraints(465, 45, 100, -1));
    jpDetail.add(jraDatum, new XYConstraints(355, 20, 105, -1));
    jpDetail.add(jraDatumjcd, new XYConstraints(465, 270, 100, -1));
    jpDetail.add(jraDatumpl, new XYConstraints(250, 170, 100, -1));
    jpDetail.add(jraDatumrjes, new XYConstraints(465, 220, 100, -1));
    jpDetail.add(jraIznospl, new XYConstraints(355, 170, 105, -1));
    jpDetail.add(jraProvizija, new XYConstraints(250, 145, 100, -1));
    jpDetail.add(jraRbr, new XYConstraints(250, 20, 100, -1));
    jpDetail.add(jraStaragod, new XYConstraints(355, 245, 105, -1));
    jpDetail.add(jraStarirbr, new XYConstraints(250, 245, 100, -1));
    jpDetail.add(jraVrijednost, new XYConstraints(250, 95, 100, -1));

    /**@todo: Odkomentirati sljede?u liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
