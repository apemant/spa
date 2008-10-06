/****license*****************************************************************
**   file: dlgPromArt.java
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
package hr.restart.robno;

import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Dialog za promjenu cijena grupe ili svih artikala. Klasa ne pr\u010Dka ništa
 * po DataSetovima ili sql tablicama nego služi samo kao su\u010Delje.
 * <p>Na\u010Din korištenja:<p>
 * - instancirati klasu, new dlgPromArt() ili new dlgPromArt(Frame owner, String title)
 * (drugi na\u010Din je poželjan da se dialog centrira u odnosu na owner frame)<p>
 * - pozvati metodu show(). Dialog je modalan i metoda vra\u0107a kontrolu natrag tek
 * nakon što se dialog zatvori. <p>
 * - nakon toga se pozivaju getteri za ispitivanje unosa i na\u010Dina izlaska iz prozora.
 * isOK() vra\u0107a true ako je pritisnuto dugme OK, ina\u010De false. isAll() vra\u0107a true ako
 * su odabrani svi artikli, ina\u010De getGrupart() vra\u0107a oznaku odabrane grupe. getPromjena()
 * vra\u0107a BigDecimal za odabranom postotnom promjenom cijena.
 */
public class dlgPromArt extends JraDialog {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  XYLayout xYLayout5 = new XYLayout();
  JraButton jbSelGrupart = new JraButton();
  JlrNavField jlrGrupart = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JraTextField jraPromjenaSvi = new JraTextField();
  JraCheckBox jcbGrupart = new JraCheckBox();
  JLabel jlPostoSvi = new JLabel();
  JLabel jlPromjenaSvi = new JLabel();
  JPanel jPanel3 = new JPanel();
  JLabel jlGrupart = new JLabel();
  JPanel jpMain = new JPanel();

  // standardni OK panel
  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
       pressOK();
    }
    public void jPrekid_actionPerformed(){
      pressCancel();
    }
  };

  /*
   * Dummy DataSet da bi se mogla postaviti maska ###,###,##0.00 na polje
   * promjena cijene.
   */
  StorageDataSet pr = new StorageDataSet();
  Column promSvi;
  BigDecimal promjena;
  String grupart;
  boolean accept;
  boolean all;

  /**
   * Default konstruktor. Postavlja naslov "Artikli".
   */
  public dlgPromArt() {
    this(null, "Artikli");
  }

  /**
   * Konstruktor za ekspilictno zadavanje naslova i owner frame-a
   * @param frame frame u odnosu na kojeg \u0107e dialog biti centiran.
   * @param title naslov dialoga.
   */
  public dlgPromArt(Frame frame, String title) {
    super(frame, title, true);
    try {
      jbInit();
      pack();
      this.setLocationRelativeTo(frame);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter za odre\u0111ivanje na\u010Dina na koji je dialog zatvoren.
   * @return true ako je pritisnuto dugme OK, ina\u010De false.
   */
  public boolean isOK() {
    return accept;
  }

  /**
   * @return true ako su odabrani svi artikli, ina\u010De false.
   */
  public boolean isAll() {
    return all;
  }

  /**
   * @return uneseni postotak promjene cijena odabranih artikala.
   */
  public BigDecimal getPromjena() {
    return promjena;
  }

  /**
   * @return šifru grupe artikala.
   */
  public String getGrupart() {
    return grupart;
  }

  private void jbInit() throws Exception {

    // inicijalizacija GUI komponenti
    jPanel3.setLayout(xYLayout5);
    jlPromjenaSvi.setText("Promjena cijene");
    jlPostoSvi.setText("%");
    jcbGrupart.setText("Svi artikli");
    jcbGrupart.setHorizontalTextPosition(JLabel.LEFT);
    jcbGrupart.setHorizontalAlignment(JLabel.RIGHT);

    promSvi = dm.createBigDecimalColumn("PROMSVI", "Promjena cijene");
    pr.setColumns(new Column[] {promSvi});
    jraPromjenaSvi.setColumnName("PROMSVI");
    jraPromjenaSvi.setDataSet(pr);
    jraPromjenaSvi.setPostOnRowPosted(false);

    jlrGrupart.setVisCols(new int[] {0,2,3});
    jlrGrupart.setSearchMode(0);
    jlrGrupart.setRaDataSet(dm.getGrupart());
    jlrGrupart.setColumnName("CGRART");
    jlrGrupart.setColNames(new String[] {"NAZGRART"});
    jlrGrupart.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrGrupart.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrGrupart.setNavButton(jbSelGrupart);

    jlrNaziv.setNavProperties(jlrGrupart);
    jlrNaziv.setColumnName("NAZGRART");
    jlrNaziv.setSearchMode(1);

    xYLayout5.setHeight(85);
    xYLayout5.setWidth(420);
    jlGrupart.setText("Grupa artikla");
    jPanel3.add(jlGrupart, new XYConstraints(15, 20, -1, -1));
    jPanel3.add(jlrGrupart, new XYConstraints(150, 20, 75, -1));
    jPanel3.add(jlrNaziv, new XYConstraints(230, 20, 150, -1));
    jPanel3.add(jbSelGrupart, new XYConstraints(385, 20, 21, 21));
    jPanel3.add(jcbGrupart, new XYConstraints(255, 45, 125, -1));
    jPanel3.add(jlPromjenaSvi, new XYConstraints(15, 45, -1, -1));
    jPanel3.add(jraPromjenaSvi, new XYConstraints(150, 45, 75, -1));
    jPanel3.add(jlPostoSvi, new XYConstraints(230, 45, -1, -1));
    jPanel3.setBorder(BorderFactory.createEtchedBorder());
    jpMain.setLayout(new BorderLayout());
    jpMain.add(jPanel3, BorderLayout.CENTER);
    jpMain.add(okp, BorderLayout.SOUTH);
    this.getContentPane().add(jpMain);

    // listeneri
    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    jcbGrupart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        checkSvi();
      }
    });
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        initFields();
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        daKeyPressed(e);
      }
    });
  }


  /*
   * Metoda koja inicijalizira dialog kad se isti otvori.
   */
  private void initFields() {
    pr.open();
    //jraPromjenaSvi.setText("");
    jcbGrupart.setSelected(false);
    pr.setBigDecimal("PROMSVI", hr.restart.robno._Main.nul);
    accept = false;
    all = false;
//    System.out.println("shown");
    jraPromjenaSvi.requestFocus();
    checkSvi();
  }

  /*
   * Metoda za prekap\u010Danje odabiranja svih artikala odn. odre\u0111ene grupe.
   */
  private void checkSvi() {
    if (jcbGrupart.isSelected()) {
      rcc.setLabelLaF(jlrGrupart, false);
      rcc.setLabelLaF(jlrNaziv, false);
      rcc.setLabelLaF(jbSelGrupart, false);
      jlrGrupart.setText("");
      jlrGrupart.forceFocLost();
      jraPromjenaSvi.requestFocus();
    } else {
      rcc.setLabelLaF(jlrGrupart, true);
      rcc.setLabelLaF(jlrNaziv, true);
      rcc.setLabelLaF(jbSelGrupart, true);
      jlrGrupart.requestFocus();
    }
  }

  private void pressCancel() {
    this.hide();
  }

  private void pressOK() {
    all = jcbGrupart.isSelected();
    grupart = jlrGrupart.getText();
    promjena = pr.getBigDecimal("PROMSVI");
    accept = true;
    this.hide();
  }

  private void daKeyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ESCAPE) {
      pressCancel();
    }
    else if (e.getKeyCode()==e.VK_F10) {
      pressOK();
    }
  }
}
