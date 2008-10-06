/****license*****************************************************************
**   file: frmOsRevSkupine.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.robno._Main;
import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

public class frmOsRevSkupine extends raSifraNaziv {

  dM dm;
  _Main main;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jrfMjesec = new JraTextField() {
    public void valueChanged() {
      jrfMjesec_focusLost(null);
    }
  };
  JraTextField jrfKoef1 = new JraTextField();
  JraTextField jrfKoef2 = new JraTextField();
  JLabel jlMjesec = new JLabel();
  JLabel jlKoef1 = new JLabel();
  JLabel jLabel3 = new JLabel();
//  XYLayout xYLayout1 = new XYLayout

  public frmOsRevSkupine()
  {
    try
    {
      jbInit();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    dm = dM.getDataModule();
    jp.setLayout(xYLayout1);

    this.setRaDataSet(dm.getOS_Revskupine());
    this.setVisibleCols(new int[] {0,1,3});

    this.setRaColumnNaziv("NAZSKUPINE");
    this.setRaColumnSifra("CSKUPINE");
    this.setRaText("Skupina");

    jlMjesec.setToolTipText("");
    jlMjesec.setText("Mjesec");
    jlKoef1.setText("Koeficijent 1");
    jLabel3.setText("Koeficijent 2");

    jrfMjesec.setDataSet(dm.getOS_Revskupine()) ;
    jrfMjesec.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        jrfMjesec_keyTyped(e);
      }
    });


    /*jrfMjesec.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfMjesec_focusLost(e);
      }
    });*/
    jrfMjesec.setHorizontalAlignment(SwingConstants.RIGHT);
    jrfMjesec.setColumnName("MJESEC");

    jrfKoef1.setDataSet(dm.getOS_Revskupine());
    jrfKoef1.setColumnName("KOEFICIJENT");
    jrfKoef2.setDataSet(dm.getOS_Revskupine());
    jrfKoef2.setColumnName("KOEFICIJENT2");

//    jp.setMinimumSize(new Dimension(250, 85));
//    jp.setPreferredSize(new Dimension(250, 85));

    xYLayout1.setWidth(585);
    xYLayout1.setHeight(60);

    jp.add(jlKoef1,    new XYConstraints(15, 25, -1, -1));
    jp.add(jrfKoef1,   new XYConstraints(150, 25, 100, -1));
    jp.add(jrfKoef2,   new XYConstraints(440, 25, 100, -1));
    jp.add(jrfMjesec,    new XYConstraints(150, 0, 100, -1));
    jp.add(jlMjesec,    new XYConstraints(15, 0, -1, -1));
    jp.add(jLabel3,  new XYConstraints(354, 25, -1, -1));

    this.jpRoot.add(jp, java.awt.BorderLayout.SOUTH);
  }

  void jrfMjesec_focusLost(FocusEvent e)
  {
    try {
      provjeraMjeseca();
    }
    catch (Exception ex) {
      jrfMjesec.setBackground(Color.red);
      jrfMjesec.requestFocus();
    }
  }

  void jrfMjesec_keyTyped(KeyEvent e) {
      jrfMjesec.setBackground(Color.white);
  }

  public boolean Validacija(char mode)
  {
    try {
      provjeraMjeseca();
      return true;
    }
    catch (Exception ex) {
      jrfMjesec.setBackground(Color.red);
      jrfMjesec.requestFocus();
      return false;
    }
  }

  private void  provjeraMjeseca() throws Exception
  {
    Integer provjera = new Integer(this.jrfMjesec.getText());
    int intOK = provjera.intValue();

    if (intOK > 12 || intOK < 0)
    {
      throw new Exception();
    }
    else if (intOK < 10)
    {
      jrfMjesec.setText("0" + intOK);
    }
  }
}