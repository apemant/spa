/****license*****************************************************************
**   file: frmKoloneknjUI.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmKoloneknjUI extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  /*raButtonGroup bg = new raButtonGroup();
  JraRadioButton jrbURA = new JraRadioButton();
  JraRadioButton jrbIRA = new JraRadioButton();*/
  raComboBox rcbUI = new raComboBox();
  raComboBox rcbDugPot = new raComboBox();
  JraCheckBox jcbTotal = new JraCheckBox();
  JraCheckBox jcbProtu = new JraCheckBox();
  
  JLabel jlUI = new JLabel();
  JLabel jlDugPot = new JLabel();
  
  int lastUI, lastDugPot;
  
  public frmKoloneknjUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {      
      rcbUI.setSelectedIndex(lastUI);
      rcbDugPot.setSelectedIndex(lastDugPot);
    } else if (mode == 'I') {
      rcc.setLabelLaF(rcbUI, false);      
    }
    super.SetFokus(mode);
  }

  public boolean Validacija(char mode) {
    if (!(this.getRaDataSet().getShort("CKOLONE") > 0)) {
      super.SetFokus(mode);
      JOptionPane.showMessageDialog(this.getRaDetailPanel(), "Obavezan unos CKOLONE!", "Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N' && vl.chkExistsSQL(new Column[] {this.getRaDataSet().getColumn("CKOLONE"),
                                      this.getRaDataSet().getColumn("URAIRA")},
                        new String[] {""+this.getRaDataSet().getShort("CKOLONE"),
                                      this.getRaDataSet().getString("URAIRA")})) {
      super.SetFokus(mode);
      JOptionPane.showMessageDialog(this.getRaDetailPanel(), "Kolona veæ postoji!", "Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N') {
      lastUI = rcbUI.getSelectedIndex();
      lastDugPot = rcbDugPot.getSelectedIndex();
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDataSet(dm.getKoloneknjUI());
    this.setRaColumnSifra("CKOLONE");
    this.setRaColumnNaziv("NAZIVKOLONE");
    this.setRaText("Kolona");
    this.setVisibleCols(new int[] {0,1,2});

    jlUI.setText("Knjiga");
    jlDugPot.setText("Konto strana");
    rcbUI.setRaItems(new String[][] {
        {"URA", "U"},
        {"IRA", "I"}
    });
    rcbUI.setDataSet(getRaDataSet());
    rcbUI.setColumnName("URAIRA");
    rcbUI.setSelectedIndex(lastUI = 0);
    
    rcbDugPot.setRaItems(new String[][] {
        {"Oboje", "O"},
        {"Dugovna", "D"},
        {"Potražna", "P"}
    });
    rcbDugPot.setDataSet(getRaDataSet());
    rcbDugPot.setColumnName("DUGPOT");
    rcbDugPot.setSelectedIndex(lastDugPot = 0);
    
    jcbTotal.setText(" Kolona reprezentira ukupan iznos raèuna ");
    jcbTotal.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbTotal.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbTotal.setDataSet(getRaDataSet());
    jcbTotal.setColumnName("TOTAL");
    jcbTotal.setSelectedDataValue("D");
    jcbTotal.setUnselectedDataValue("N");
    
    jcbProtu.setText(" Kolona reprezentira protustavku raèuna ");
    jcbProtu.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbProtu.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbProtu.setDataSet(getRaDataSet());
    jcbProtu.setColumnName("PROTU");
    jcbProtu.setSelectedDataValue("D");
    jcbProtu.setUnselectedDataValue("N");
    
    /*bg.setColumnName("URAIRA");
    bg.setDataSet(this.getRaDataSet());
    bg.add(jrbIRA, "Izlazna", "I");
    bg.add(jrbURA, "Ulazna", "U");*/

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(145);
    ((XYLayout) jp.getLayout()).setWidth(570);

    jp.add(jlUI, new XYConstraints(15, 62, -1, -1));
    jp.add(rcbUI, new XYConstraints(150, 60, 100, -1));
    jp.add(jlDugPot, new XYConstraints(350, 62, -1, -1));
    jp.add(rcbDugPot, new XYConstraints(440, 60, 100, -1));
    jp.add(jcbTotal, new XYConstraints(240, 85, 300, -1));
    jp.add(jcbProtu, new XYConstraints(240, 108, 300, -1));
  }
}
