/****license*****************************************************************
**   file: jpVrart.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVrart extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVrart fVrart;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvrart = new JLabel();
  JLabel jlKontopdv = new JLabel();
  JLabel jlKontopnp = new JLabel();
  JLabel jlKontopot = new JLabel();
  JLabel jlKontopri = new JLabel();
  JLabel jlNazvrart = new JLabel();
  JLabel jlaKontopot = new JLabel();
  JLabel jlaNazivkonta = new JLabel();
  JraButton jbSelKontopdv = new JraButton();
  JraButton jbSelKontopnp = new JraButton();
  JraButton jbSelKontopot = new JraButton();
  JraButton jbSelKontopri = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCvrart = new JraTextField();
  JraTextField jraNazvrart = new JraTextField();
  JlrNavField jlrKontopot = new JlrNavField()/* {
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(1);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
  }*/;
  JlrNavField jlrNazivkontapot = new JlrNavField();
  JlrNavField jlrKontopnp = new JlrNavField() /*{
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(1);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
  }*/;
  JlrNavField jlrNazivkontapnp = new JlrNavField();
  JlrNavField jlrKontopdv = new JlrNavField() /*{
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(1);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
  }*/;
  JlrNavField jlrNazivkontapdv = new JlrNavField();
  JlrNavField jlrKontopri = new JlrNavField() /*{
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(1);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
  }*/;
  JlrNavField jlrNazivkontapri = new JlrNavField();
  
  JraCheckBox jcbStanje = new JraCheckBox();
  JraCheckBox jcbNorma = new JraCheckBox();
  JraCheckBox jcbNaziv = new JraCheckBox();
  JraCheckBox jcbUsluga = new JraCheckBox();

  public jpVrart(frmVrart f) {
    try {
      fVrart = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(646);
    lay.setHeight(260);

    jbSelKontopdv.setText("...");
    jbSelKontopnp.setText("...");
    jbSelKontopot.setText("...");
    jbSelKontopri.setText("...");
    jlCvrart.setText("Oznaka");
    jlKontopdv.setText("PDV");
    jlKontopnp.setText("PnP");
    jlKontopot.setText("Potraživanje");
    jlKontopri.setText("Prihod");
    jlNazvrart.setText("Naziv");
    jlaKontopot.setHorizontalAlignment(SwingConstants.LEADING);
    jlaKontopot.setText("Konto");
    jlaNazivkonta.setHorizontalAlignment(SwingConstants.LEADING);
    jlaNazivkonta.setText("Naziv");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fVrart.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");
    
    jcbStanje.setText("Vodi se na stanju");
    jcbStanje.setColumnName("STANJE");
    jcbStanje.setDataSet(fVrart.getRaQueryDataSet());
    jcbStanje.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbStanje.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbStanje.setSelectedDataValue("D");
    jcbStanje.setUnselectedDataValue("N");
    
    jcbNorma.setText("Dopušta normiranje");
    jcbNorma.setColumnName("NORMA");
    jcbNorma.setDataSet(fVrart.getRaQueryDataSet());
    jcbNorma.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbNorma.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbNorma.setSelectedDataValue("D");
    jcbNorma.setUnselectedDataValue("N");
    
    jcbNaziv.setText("Dopušta promjenu naziva");
    jcbNaziv.setColumnName("VARNAZIV");
    jcbNaziv.setDataSet(fVrart.getRaQueryDataSet());
    jcbNaziv.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbNaziv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbNaziv.setSelectedDataValue("D");
    jcbNaziv.setUnselectedDataValue("N");
    
    jcbUsluga.setText("Smatra se za uslugu");
    jcbUsluga.setColumnName("USLUGA");
    jcbUsluga.setDataSet(fVrart.getRaQueryDataSet());
    jcbUsluga.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbUsluga.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbUsluga.setSelectedDataValue("D");
    jcbUsluga.setUnselectedDataValue("N");
    
    jraCvrart.setColumnName("CVRART");
    jraCvrart.setDataSet(fVrart.getRaQueryDataSet());
    jraNazvrart.setColumnName("NAZVRART");
    jraNazvrart.setDataSet(fVrart.getRaQueryDataSet());

    jlrKontopot.setColumnName("KONTOPOT");
    jlrKontopot.setNavColumnName("BROJKONTA");
    jlrKontopot.setDataSet(fVrart.getRaQueryDataSet());
    jlrKontopot.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontopot.setTextFields(new JTextComponent[] {jlrNazivkontapot});
    jlrKontopot.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrKontopot.setSearchMode(lookupData.EXACT, lookupData.TEXT);
    jlrKontopot.setRaDataSet(dm.getKontaAnalitic());
    jlrKontopot.setNavButton(jbSelKontopot);

    jlrNazivkontapot.setColumnName("NAZIVKONTA");
    jlrNazivkontapot.setNavProperties(jlrKontopot);
    jlrNazivkontapot.setSearchMode(1);

    jlrKontopnp.setColumnName("KONTOPNP");
    jlrKontopnp.setNavColumnName("BROJKONTA");
    jlrKontopnp.setDataSet(fVrart.getRaQueryDataSet());
    jlrKontopnp.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontopnp.setTextFields(new JTextComponent[] {jlrNazivkontapnp});
    jlrKontopnp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrKontopnp.setSearchMode(lookupData.EXACT, lookupData.TEXT);
    jlrKontopnp.setRaDataSet(dm.getKontaAnalitic());
    jlrKontopnp.setNavButton(jbSelKontopnp);

    jlrNazivkontapnp.setColumnName("NAZIVKONTA");
    jlrNazivkontapnp.setNavProperties(jlrKontopnp);
    jlrNazivkontapnp.setSearchMode(1);

    jlrKontopdv.setColumnName("KONTOPDV");
    jlrKontopdv.setNavColumnName("BROJKONTA");
    jlrKontopdv.setDataSet(fVrart.getRaQueryDataSet());
    jlrKontopdv.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontopdv.setTextFields(new JTextComponent[] {jlrNazivkontapdv});
    jlrKontopdv.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrKontopdv.setSearchMode(lookupData.EXACT, lookupData.TEXT);
    jlrKontopdv.setRaDataSet(dm.getKontaAnalitic());
    jlrKontopdv.setNavButton(jbSelKontopdv);

    jlrNazivkontapdv.setColumnName("NAZIVKONTA");
    jlrNazivkontapdv.setNavProperties(jlrKontopdv);
    jlrNazivkontapdv.setSearchMode(1);

    jlrKontopri.setColumnName("KONTOPRI");
    jlrKontopri.setNavColumnName("BROJKONTA");
    jlrKontopri.setDataSet(fVrart.getRaQueryDataSet());
    jlrKontopri.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontopri.setTextFields(new JTextComponent[] {jlrNazivkontapri});
    jlrKontopri.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrKontopri.setSearchMode(lookupData.EXACT, lookupData.TEXT);
    jlrKontopri.setRaDataSet(dm.getKontaAnalitic());
    jlrKontopri.setNavButton(jbSelKontopri);

    jlrNazivkontapri.setColumnName("NAZIVKONTA");
    jlrNazivkontapri.setNavProperties(jlrKontopri);
    jlrNazivkontapri.setSearchMode(1);

    jpDetail.add(jbSelKontopdv, new XYConstraints(610, 140, 21, 21));
    jpDetail.add(jbSelKontopnp, new XYConstraints(610, 165, 21, 21));
    jpDetail.add(jbSelKontopot, new XYConstraints(610, 90, 21, 21));
    jpDetail.add(jbSelKontopri, new XYConstraints(610, 115, 21, 21));
    jpDetail.add(jcbAktiv, new XYConstraints(535, 20, 70, -1));
    jpDetail.add(jlCvrart, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlKontopdv, new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jlKontopnp, new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jlKontopot, new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlKontopri, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlNazvrart, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlaKontopot, new XYConstraints(151, 73, 98, -1));
    jpDetail.add(jlaNazivkonta, new XYConstraints(256, 73, 348, -1));
    jpDetail.add(jlrKontopdv, new XYConstraints(150, 140, 100, -1));
    jpDetail.add(jlrKontopnp, new XYConstraints(150, 165, 100, -1));
    jpDetail.add(jlrKontopot, new XYConstraints(150, 90, 100, -1));
    jpDetail.add(jlrKontopri, new XYConstraints(150, 115, 100, -1));
    jpDetail.add(jlrNazivkontapot, new XYConstraints(255, 90, 350, -1));
    jpDetail.add(jlrNazivkontapri, new XYConstraints(255, 115, 350, -1));
    jpDetail.add(jlrNazivkontapdv, new XYConstraints(255, 140, 350, -1));
    jpDetail.add(jlrNazivkontapnp, new XYConstraints(255, 165, 350, -1));
    jpDetail.add(jraCvrart, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraNazvrart, new XYConstraints(150, 45, 455, -1));
    jpDetail.add(jcbStanje, new XYConstraints(150, 190, 225, -1));
    jpDetail.add(jcbNaziv, new XYConstraints(380, 190, 225, -1));
    jpDetail.add(jcbNorma, new XYConstraints(150, 215, 225, -1));
    jpDetail.add(jcbUsluga, new XYConstraints(380, 215, 225, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
