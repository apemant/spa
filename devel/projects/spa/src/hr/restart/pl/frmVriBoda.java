/****license*****************************************************************
**   file: frmVriBoda.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmVriBoda extends raMatPodaci {
  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect(){
    public void SetFokus()
    {
      jrfGodina.requestFocus();
    }
    public boolean Validacija()
    {
//      if(jrfGodina.getText().length()!=4)
//      {
//        jrfGodina.requestFocus();
//        return false;
//      }
//
//      if(vl.isEmpty(jrfGodina))
//      {
//        jrfGodina.requestFocus();
//        return false;
//      }
//      predselekcija = true;
//      return true;
      try {
      provjeraGodine();
      predselekcija = true;
      return true;
    }
    catch (Exception ex) {
      jrfGodina.setBackground(Color.red);
      jrfGodina.setCaretPosition(0);
      jrfGodina.setText("");
      jrfGodina.requestFocus();
      return false;
      }
    }
  };

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpVriBoda jpDetail;
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpSel = new JPanel();
  boolean predselekcija = false;
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jraKnjig = new JraTextField();
  JraTextField jrfGodina = new JraTextField();
  JLabel jlGodina = new JLabel();
//  PlainDocument pd = new PlainDocument();


  public frmVriBoda() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraMjesec, false);
    }
  }

  public void SetFokus(char mode) {
    pres.copySelValues();
    pres.getSelDataSet().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    if (mode == 'N') {
      jpDetail.jraMjesec.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraBod.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraMjesec) || vl.isEmpty(jpDetail.jraBod))
      return false;
    if (mode == 'N' && notUnique())
    {
      jpDetail.jraMjesec.requestFocus();
      JOptionPane.showConfirmDialog(jpDetail,"Zapis postoji !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    try {
      provjeraMjeseca();
      return true;
    }
    catch (Exception ex) {
      jpDetail.jraMjesec.setText("");
      jpDetail.jraMjesec.this_ExceptionHandling(null);
      return false;
    }
  }

  private boolean notUnique()
  {
    return plUtil.getPlUtil().checkVriBodaUnique(getRaQueryDataSet().getShort("GODINA"),
                                                 getRaQueryDataSet().getShort("MJOBR"));
  }

  private void jbInit() throws Exception {
    jpSel.setLayout(xYLayout2);
    xYLayout1.setHeight(60);
    xYLayout1.setWidth(265);
    jraKnjig.setVisible(false);
    jlGodina.setText("Godina");

    this.setRaQueryDataSet(dm.getVriboda());
    this.setVisibleCols(new int[] {0, 1, 2, 3});

    jpDetail = new jpVriBoda(this);
    this.setRaDetailPanel(jpDetail);

    xYLayout2.setWidth(265);
    xYLayout2.setHeight(60);

    jpSel.add(jraKnjig,   new XYConstraints(100, 20, 100, -1));
    jpSel.add(jrfGodina,  new XYConstraints(150, 20, 100, -1));
    jpSel.add(jlGodina,    new XYConstraints(15, 20, -1, -1));

    jrfGodina.setColumnName("GODINA");
    jrfGodina.setDataSet(dm.getVriboda());
    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(dm.getVriboda());

    pres.setSelDataSet(dm.getVriboda());
    pres.setSelPanel(jpSel);
}

  public void show()
  {
    if(!predselekcija)
    {
      pres.showPreselect(this, "Vrijednost boda");
    }
    else
    {
      super.show();
    }
  }

  public void this_hide()
  {
    predselekcija = false;
    super.this_hide();
  }

  private void  provjeraMjeseca() throws Exception
  {
    int intOK =0;
    try {
     Integer provjera = new Integer(jpDetail.jraMjesec.getText());
     intOK = provjera.intValue();
    }
    catch (Exception ex) {
      throw new Exception();
    }

    if (intOK > 12 || intOK < 1)
    {
      throw new Exception();
    }
  }

  private void provjeraGodine() throws Exception
  {
    try {
      Integer i = new Integer(jrfGodina.getText());
    }
    catch (Exception ex) {
      throw new Exception();
    }

    if(jrfGodina.getText().length()!= 4 || jrfGodina.getText().equals(""))
    {
      throw new Exception();
    }
  }
}
