/****license*****************************************************************
**   file: frmNarRadovi.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmNarRadovi extends raMatPodaci {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpDetail = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlSif = new JLabel();
  JLabel jlTekst = new JLabel();
  JLabel jlNorm = new JLabel();
  JraTextField jraSif = new JraTextField();
  JTextArea jtaOpis = new JTextArea();
  Border border1;
  JlrNavField jlrNorma = new JlrNavField();
  JlrNavField jlrNazart = new JlrNavField();
  JraButton jbSelArt = new JraButton();
  JraScrollPane scroller = new JraScrollPane(jtaOpis);
  JScrollBar hs = new JScrollBar(JScrollBar.HORIZONTAL) {
    public void setEnabled(boolean dummy) {
      //super.setEnabled(true);
    }
  };
  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
    public void setEnabled(boolean dummy) {
      //super.setEnabled(true);
    }
  };

  public frmNarRadovi() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShow() {
    Aut.getAut().getNorme().refresh();
  }

  public void EntryPoint(char mode) {
    setArea();
    if (mode == 'I') {
      rcc.setLabelLaF(jraSif, false);
    }
  }

  public void SetFokus(char mode) {
    setArea();
    if (mode == 'N') {
      jlrNorma.forceFocLost();
      jraSif.requestFocus();
    } else if (mode == 'I') {
      jtaOpis.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraSif))
      return false;
    if (mode == 'N' && vl.notUnique(jraSif))
      return false;
    if (jtaOpis.getText().length() > 200)
      this.getRaQueryDataSet().setString("TEKST", jtaOpis.getText().substring(0,200));
    else this.getRaQueryDataSet().setString("TEKST", jtaOpis.getText());
    return true;
  }

  private void jbInit() throws Exception {

    this.setRaQueryDataSet(dm.getRN_tekstovi());
    this.setRaDetailPanel(jpDetail);
    this.setVisibleCols(new int[] {0,1,2});

    border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,Color.darkGray,Color.lightGray);

    jraSif.setDataSet(this.getRaQueryDataSet());
    jraSif.setColumnName("CTEKST");
    jlrNorma.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrNorma.setDataSet(this.getRaQueryDataSet());
    jlrNorma.setTextFields(new JTextComponent[] {jlrNazart});
    jlrNorma.setColNames(new String[] {"NAZSKUPART"});
    jlrNorma.setColumnName("CSKUPART");
    jlrNorma.setSearchMode(0);
    jlrNorma.setRaDataSet(Aut.getAut().getNorme());
    jlrNorma.setVisCols(new int[] {0, 1});
    jlrNorma.setNavButton(jbSelArt);

    jlrNazart.setColumnName("NAZSKUPART");
    jlrNazart.setNavProperties(jlrNorma);
    jlrNazart.setSearchMode(0);

    jlSif.setText("Šifra");
    xYLayout1.setWidth(540);
    xYLayout1.setHeight(205);
    jpDetail.setLayout(xYLayout1);
    jlTekst.setText("Tekst");
    jlNorm.setText("Skupina artikala");
    //jtaOpis.setFont(new java.awt.Font("SansSerif", 0, 12));
    jtaOpis.setBorder(border1);
    jbSelArt.setText("...");
    jtaOpis.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    scroller.setBorder(BorderFactory.createLoweredBevelBorder());
    scroller.setHorizontalScrollBar(hs);
    scroller.setVerticalScrollBar(vs);
    jpDetail.add(jlSif, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlTekst, new XYConstraints(15, 50, -1, -1));
    jpDetail.add(jraSif, new XYConstraints(120, 20, 75, -1));
    jpDetail.add(scroller, new XYConstraints(120, 50, 400, 105));
    jpDetail.add(jlNorm, new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jlrNorma, new XYConstraints(120, 165, 75, -1));
    jpDetail.add(jlrNazart, new XYConstraints(200, 165, 295, -1));
    jpDetail.add(jbSelArt, new XYConstraints(500, 165, 21, 21));
  }

  private void setArea() {
    jtaOpis.setText(this.getRaQueryDataSet().getString("TEKST"));
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    setArea();
  }
}