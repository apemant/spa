/****license*****************************************************************
**   file: frmPorezi.java
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

import hr.restart.swing.JrCheckBox;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.raMatPodaci;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 *
 * 20.09.2001 - Version 1.0
 */

public class frmPorezi extends raMatPodaci {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlPOR3 = new JLabel();
  JLabel jlPOR2 = new JLabel();
  JLabel jlPOR1 = new JLabel();
  JraTextField jtfUKUNPOR = new JraTextField();
  JraTextField jtfNAZPOR = new JraTextField();
  JraTextField jtfUKUPOR = new JraTextField();
  JLabel jlUKUPOR = new JLabel();
  JraTextField jtfUNPOR3 = new JraTextField();
  JraTextField jtfUNPOR2 = new JraTextField();
  JraTextField jtfCPOR = new JraTextField();
  JraTextField jtfUNPOR1 = new JraTextField();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraTextField jtfPOR3 = new JraTextField() {
    public void valueChanged() {
      jtfPOR1_focusLost(null);
    }
  };
  JraTextField jtfPOR2 = new JraTextField() {
    public void valueChanged() {
      jtfPOR1_focusLost(null);
    }
  };
  JraTextField jtfPOR1 = new JraTextField() {
    public void valueChanged() {
      jtfPOR1_focusLost(null);
    }
  };
  JLabel jlCPOR = new JLabel();
  JLabel jpStopa = new JLabel();
  JLabel jlInvertno = new JLabel();
  JLabel jlPorNaPor = new JLabel();
  JLabel jLabel1 = new JLabel();
  JraTextField jtfNAZPOR3 = new JraTextField();
  JraTextField jtfNAZPOR2 = new JraTextField();
  JraTextField jtfNAZPOR1 = new JraTextField();
  JrCheckBox jcbPnp1 = new JrCheckBox();
  JrCheckBox jcbPnp2 = new JrCheckBox();
  JrCheckBox jcbPnp3 = new JrCheckBox();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();

  public frmPorezi() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jp.setLayout(xYLayout1);
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllPorezi());
    this.setVisibleCols(new int[] {0,1,7});

    jlCPOR.setText(res.getString("jlCPOR_text"));
    jcbAKTIV.setText(res.getString("jcbAKTIV_text"));
    jlUKUPOR.setText(res.getString("jlUKUPOR_text"));
    jlPOR1.setText(res.getString("jlPOR1_text"));
    jlPOR2.setText(res.getString("jlPOR2_text"));
    jlPOR3.setText(res.getString("jlPOR3_text"));
    jpStopa.setHorizontalAlignment(SwingConstants.CENTER);
    jpStopa.setText(res.getString("jpStopa_text"));
    jlInvertno.setHorizontalAlignment(SwingConstants.CENTER);
    jlInvertno.setText(res.getString("jlInvertno_text"));
    /*jtfPOR1.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfPOR1_focusLost(e);
      }
    });*/
    jtfPOR1.setDataSet(getRaQueryDataSet());
    jtfPOR1.setColumnName("POR1");
    /*jtfPOR2.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfPOR2_focusLost(e);
      }
    });*/
    jtfPOR2.setDataSet(getRaQueryDataSet());
    jtfPOR2.setColumnName("POR2");
    /*jtfPOR3.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfPOR3_focusLost(e);
      }
    });*/
    jtfPOR3.setDataSet(getRaQueryDataSet());
    jtfPOR3.setColumnName("POR3");
    jtfCPOR.setDataSet(getRaQueryDataSet());
    jtfCPOR.setColumnName("CPOR");
    jtfUNPOR1.setDataSet(getRaQueryDataSet());
    jtfUNPOR1.setColumnName("UNPOR1");
    jtfUNPOR2.setDataSet(getRaQueryDataSet());
    jtfUNPOR2.setColumnName("UNPOR2");
    jtfUNPOR3.setDataSet(getRaQueryDataSet());
    jtfUNPOR3.setColumnName("UNPOR3");
    jtfUKUPOR.setDataSet(getRaQueryDataSet());
    jtfUKUPOR.setColumnName("UKUPOR");
    jtfNAZPOR.setDataSet(getRaQueryDataSet());
    jtfNAZPOR.setColumnName("NAZPOR");
    jtfUKUNPOR.setDataSet(getRaQueryDataSet());
    jtfUKUNPOR.setColumnName("UKUNPOR");
    jcbAKTIV.setUnselectedDataValue("N");
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(219);
    jlPorNaPor.setHorizontalAlignment(SwingConstants.CENTER);
    jlPorNaPor.setText("PnP");
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("Naziv");
    jtfNAZPOR3.setColumnName("NAZPOR3");
    jtfNAZPOR3.setDataSet(getRaQueryDataSet());
    jtfNAZPOR2.setColumnName("NAZPOR2");
    jtfNAZPOR2.setDataSet(getRaQueryDataSet());
    jtfNAZPOR1.setColumnName("NAZPOR1");
    jtfNAZPOR1.setDataSet(getRaQueryDataSet());
    jcbPnp1.setText(" ");
    jcbPnp1.setHorizontalTextPosition(SwingConstants.CENTER);
    jcbPnp1.setHorizontalAlignment(SwingConstants.CENTER);
    jcbPnp1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPnp1_actionPerformed(e);
      }
    });
    jcbPnp3.setText(" ");
    jcbPnp3.setHorizontalTextPosition(SwingConstants.CENTER);
    jcbPnp3.setHorizontalAlignment(SwingConstants.CENTER);
    jcbPnp3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPnp3_actionPerformed(e);
      }
    });
    jcbPnp2.setText(" ");
    jcbPnp2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPnp2_actionPerformed(e);
      }
    });
    jcbPnp2.setToolTipText("");
    jcbPnp2.setHorizontalTextPosition(SwingConstants.CENTER);
    jcbPnp2.setHorizontalAlignment(SwingConstants.CENTER);
    jcbPnp2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPnp2_actionPerformed(e);
      }
    });
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");
    jp.add(jlCPOR, new XYConstraints(15, 38, -1, -1));
    jp.add(jtfNAZPOR,    new XYConstraints(260, 38, 370, -1));
    jp.add(jtfCPOR, new XYConstraints(150, 38, 100, -1));
    jp.add(jcbAKTIV,  new XYConstraints(530, 15, 100, -1));
    jp.add(jlSifra, new XYConstraints(150, 16, -1, -1));
    jp.add(jlNaziv, new XYConstraints(260, 16, -1, -1));
    jp.add(jpStopa, new XYConstraints(150, 78, 100, -1));
    jp.add(jlPorNaPor, new XYConstraints(370, 78, 30, -1));
    jp.add(jtfNAZPOR3,  new XYConstraints(410, 153, 220, -1));
    jp.add(jtfUKUPOR, new XYConstraints(150, 178, 100, -1));
    jp.add(jcbPnp1, new XYConstraints(370, 103, 30, -1));
    jp.add(jtfUNPOR1, new XYConstraints(260, 103, 100, -1));
    jp.add(jtfPOR2, new XYConstraints(150, 128, 100, -1));
    jp.add(jtfPOR3, new XYConstraints(150, 153, 100, -1));
    jp.add(jtfNAZPOR1,   new XYConstraints(410, 103, 220, -1));
    jp.add(jLabel1,  new XYConstraints(410, 78, 220, -1));
    jp.add(jlUKUPOR, new XYConstraints(15, 178, -1, -1));
    jp.add(jlPOR1, new XYConstraints(15, 103, -1, -1));
    jp.add(jtfUKUNPOR, new XYConstraints(260, 178, 100, -1));
    jp.add(jtfNAZPOR2,  new XYConstraints(410, 128, 220, -1));
    jp.add(jtfPOR1, new XYConstraints(150, 103, 100, -1));
    jp.add(jcbPnp2, new XYConstraints(370, 128, 30, -1));
    jp.add(jlInvertno, new XYConstraints(260, 78, 100, -1));
    jp.add(jtfUNPOR3, new XYConstraints(260, 153, 100, -1));
    jp.add(jcbPnp3, new XYConstraints(370, 153, 30, -1));
    jp.add(jlPOR2, new XYConstraints(15, 128, -1, -1));
    jp.add(jtfUNPOR2, new XYConstraints(260, 128, 100, -1));
    jp.add(jlPOR3, new XYConstraints(15, 153, -1, -1));
  }
  public boolean Validacija(char mode) {
    if (mode=='N') {
      if (hr.restart.util.Valid.getValid().isEmpty(jtfCPOR))
        return false;
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZPOR))
      return false;
    return true;
  }
  public void SetFokus(char mode) {
    if (mode=='N') {
      rcc.setLabelLaF(jtfCPOR,true);
      jtfCPOR.requestFocus();
      jcbPnp1.setSelected(true);
      jcbPnp2.setSelected(false);
      jcbPnp3.setSelected(false);
    }
    else if (mode=='I') {
      rcc.setLabelLaF(jtfCPOR,false);
      if (getRaQueryDataSet().getString("PORNAPOR1").trim().equals("D")) {
        jcbPnp1.setSelected(true);
      }
      if (getRaQueryDataSet().getString("PORNAPOR2").trim().equals("D")) {
        jcbPnp2.setSelected(true);
      }
      if (getRaQueryDataSet().getString("PORNAPOR3").trim().equals("D")) {
        jcbPnp3.setSelected(true);
      }
      jtfNAZPOR.requestFocus();
    }
  }
  public boolean DeleteCheck() {
    return util.isDeleteable("ARTIKLI", "CPOR", getRaQueryDataSet().getString("CPOR"), util.MOD_STR);
  }
  public void EntryPoint(char mode) {
    rcc.setLabelLaF(jcbPnp1,false);
    rcc.setLabelLaF(jtfUNPOR1,false);
    rcc.setLabelLaF(jtfUNPOR2,false);
    rcc.setLabelLaF(jtfUNPOR3,false);
    rcc.setLabelLaF(jtfUKUPOR,false);
    rcc.setLabelLaF(jtfUKUNPOR,false);
  }
  void jtfPOR1_focusLost(FocusEvent e) {
  	getRaQueryDataSet().setBigDecimal("UNPOR1", new java.math.BigDecimal(findUnPor(getRaQueryDataSet().getBigDecimal("POR1").doubleValue())));
    calcUkPorez();
  }
  void jtfPOR2_focusLost(FocusEvent e) {
  	getRaQueryDataSet().setBigDecimal("UNPOR2", new java.math.BigDecimal(findUnPor(getRaQueryDataSet().getBigDecimal("POR2").doubleValue())));
    calcUkPorez();
  }
  void jtfPOR3_focusLost(FocusEvent e) {
  	getRaQueryDataSet().setBigDecimal("UNPOR3", new java.math.BigDecimal(findUnPor(getRaQueryDataSet().getBigDecimal("POR3").doubleValue())));
    calcUkPorez();
  }
/**
 * Izrachunavanje poreza i poreza unatrag prema checkBoxevima
 */
  void calcUkPorez() {
    double p1=getRaQueryDataSet().getBigDecimal("POR1").doubleValue();
    double p2=getRaQueryDataSet().getBigDecimal("POR2").doubleValue();
    double p3=getRaQueryDataSet().getBigDecimal("POR3").doubleValue();
    double ukp;
    if (this.jcbPnp2.isSelected() && jcbPnp3.isSelected()) {
      ukp = p1 + ((100+p1)*(p2/100)) + (100+(p1+((100+p1)*(p2/100)))) * (p3/100);
    }
    else if (jcbPnp2.isSelected()) {
      ukp = p1 + ((100+p1)*(p2/100)) + p3;
    }
    else if (jcbPnp3.isSelected()) {
      ukp = p1 + ((100+p1)*(p3/100)) + p2;
    }
    else {
      ukp = p1 + p2 + p3;
    }
    getRaQueryDataSet().setBigDecimal("UKUPOR", new java.math.BigDecimal(ukp));
    getRaQueryDataSet().setBigDecimal("UKUNPOR", new java.math.BigDecimal(findUnPor(getRaQueryDataSet().getBigDecimal("UKUPOR").doubleValue())));
  }
  double findUnPor (double dobvar) {
    if (dobvar==0)
      return 0;
    else
      return 100-((100/(100+dobvar))*100);
  }
  void jcbPORNAPOR1_actionPerformed(ActionEvent e) {
    calcUkPorez();
  }
  void jcbPnp1_actionPerformed(ActionEvent e) {
    if (jcbPnp1.isSelected()) {
    	getRaQueryDataSet().setString("PORNAPOR1","D");
    }
    else {
    	getRaQueryDataSet().setString("PORNAPOR1","N");
    }
    calcUkPorez();
  }
  void jcbPnp2_actionPerformed(ActionEvent e) {
    if (jcbPnp2.isSelected()) {
    	getRaQueryDataSet().setString("PORNAPOR2","D");
    }
    else {
    	getRaQueryDataSet().setString("PORNAPOR2","N");
    }
    calcUkPorez();
  }
  void jcbPnp3_actionPerformed(ActionEvent e) {
    if (jcbPnp3.isSelected()) {
    	getRaQueryDataSet().setString("PORNAPOR3","D");
    }
    else {
    	getRaQueryDataSet().setString("PORNAPOR3","N");
    }
    calcUkPorez();
  }
}