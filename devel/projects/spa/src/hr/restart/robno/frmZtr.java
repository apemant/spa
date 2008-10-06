/****license*****************************************************************
**   file: frmZtr.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

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

public class frmZtr extends raSifraNaziv {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = Valid.getValid();

  JLabel jlCPAR = new JLabel();
  JlrNavField jlrCPAR = new JlrNavField();
  JlrNavField jlrNAZPAR = new JlrNavField();
  JraButton jbPAR = new JraButton();
  JLabel jlPZT = new JLabel();
  JraTextField jraPZT = new JraTextField() {
    public void valueChanged() {
      if (getRaDataSet().getBigDecimal("PZT").signum() < 0) {
        getRaDataSet().setBigDecimal("PZT", getRaDataSet().getBigDecimal("PZT").abs());
      }
    }
  };
//  JraCheckBox jcbFIX = new JraCheckBox();
  JlrNavField jrfNAZDOK = new JlrNavField();
  JlrNavField jrfVRDOK = new JlrNavField();
  JraButton jbVRDOK = new JraButton();
  JLabel jlVRDOK = new JLabel();

  public frmZtr() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrCPAR.setText("");
      jrfVRDOK.setText("");
      jlrCPAR.forceFocLost();
    }
    super.SetFokus(mode);
  }

  public boolean Validacija2(char mode) {
    if (getRaDataSet().getBigDecimal("PZT").signum() < 0) {
      getRaDataSet().setBigDecimal("PZT", getRaDataSet().getBigDecimal("PZT").abs());
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaDataSet(dm.getZtr());
    this.setRaColumnSifra("CZT");
    this.setRaColumnNaziv("NZT");
    this.setRaText("Zavisni troškovi");

    jlCPAR.setText("Partner");
    jlVRDOK.setText("Vrsta dokumenta");
    jbVRDOK.setText("...");

    jlrCPAR.setColumnName("CPAR");
    jlrCPAR.setDataSet(this.getRaDataSet());
    jlrCPAR.setColNames(new String[] {"NAZPAR"});
    jlrCPAR.setTextFields(new JTextComponent[] {jlrNAZPAR});
    jlrCPAR.setSearchMode(0);
    jlrCPAR.setRaDataSet(dm.getPartneri());
    jlrCPAR.setVisCols(new int[] {0,1});
    jlrCPAR.setNavButton(jbPAR);

    jlrNAZPAR.setColumnName("NAZPAR");
    jlrNAZPAR.setNavProperties(jlrCPAR);
    jlrNAZPAR.setSearchMode(1);


    jlPZT.setText("Postotak");
    jraPZT.setColumnName("PZT");
    jraPZT.setDataSet(this.getRaDataSet());

    jrfVRDOK.setHorizontalAlignment(SwingConstants.TRAILING);
    jrfVRDOK.setColumnName("VRDOK");
    jrfVRDOK.setDataSet(this.getRaDataSet());
    jrfVRDOK.setColNames(new String[] {"NAZDOK"});
    jrfVRDOK.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZDOK});
    jrfVRDOK.setVisCols(new int[] {0,1});
    jrfVRDOK.setSearchMode(0);
    jrfVRDOK.setRaDataSet(dm.getVrdokum());
    jrfVRDOK.setNavButton(jbVRDOK);

    jrfNAZDOK.setNavProperties(jrfVRDOK);
    jrfNAZDOK.setColumnName("NAZDOK");
    jrfNAZDOK.setDataSet(this.getRaDataSet());
    jrfNAZDOK.setSearchMode(1);

    
//    jcbFIX.setDataSet(this.getRaDataSet());
//    jcbFIX.setColumnName("FIKS");
//    jcbFIX.setSelectedDataValue("D");
//    jcbFIX.setUnselectedDataValue("N");
//    jcbFIX.setText("Fiksni postotak");
//    jcbFIX.setHorizontalTextPosition(SwingConstants.LEADING);

    JPanel jp = (JPanel) this.getRaDetailPanel().getComponent(0);
    ((XYLayout) jp.getLayout()).setHeight(150);
    ((XYLayout) jp.getLayout()).setWidth(575);

    jp.add(jlCPAR, new XYConstraints(15, 65, -1, -1));
    jp.add(jlrCPAR, new XYConstraints(150, 65, 100, -1));
    jp.add(jlrNAZPAR, new XYConstraints(255, 65, 285, -1));
    jp.add(jbPAR, new XYConstraints(545, 65, 21, 21));
    jp.add(jlPZT, new XYConstraints(15, 90, -1, -1));
    jp.add(jraPZT, new XYConstraints(150, 90, 100, -1));

    jp.add(jrfNAZDOK, new XYConstraints(255, 115, 285, -1));
    jp.add(jrfVRDOK, new XYConstraints(150, 115, 100, -1));
    jp.add(jlVRDOK, new XYConstraints(15, 115, -1, -1));
    jp.add(jbVRDOK, new XYConstraints(545, 115, 21, 21));

    //    jp.add(jcbFIX, new XYConstraints(440, 90, 100, -1));

    /*jraPZT.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (getRaDataSet().getBigDecimal("PZT").signum() < 0) {
          getRaDataSet().setBigDecimal("PZT", getRaDataSet().getBigDecimal("PZT").abs());
        }
      }
    });*/

    hr.restart.sisfun.raDataIntegrity.installFor(this);
  }
}