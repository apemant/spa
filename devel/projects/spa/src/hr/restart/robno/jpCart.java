/****license*****************************************************************
**   file: jpCart.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpCart extends JPanel
{
  private XYLayout xYLayout1 = new XYLayout();
  boolean disabling = true;
  public JlrNavField jrfCART = new JlrNavField(){
    public void after_lookUp()
    {
      if(tds.getInt("CART")>0)
      {
        if(disabling)
        {
          rcc.setLabelLaF(jrfCART, false);
          rcc.setLabelLaF(jrfCART1, false);
          rcc.setLabelLaF(jrfCGRART, false);
          rcc.setLabelLaF(jrfNAZART, false);
          rcc.setLabelLaF(jrfBC, false);
          rcc.setLabelLaF(jrfJM, false);
          rcc.setLabelLaF(jbCART, false);
        }
        nextToFocus();
      }
    }
  };
  public JlrNavField jrfCART1 = new JlrNavField(){
    public void after_lookUp()
    {
      jrfCART.after_lookUp();
    }
  };
  public JraButton jbCART = new JraButton();
  public JlrNavField jrfNAZART = new JlrNavField(){
    public void after_lookUp()
    {
      jrfCART.after_lookUp();
    }
  };
  public JlrNavField jrfBC = new JlrNavField(){
    public void after_lookUp()
    {
      jrfCART.after_lookUp();
    }
  };
  public JlrNavField jrfCGRART = new JlrNavField(){
    public void after_lookUp()
    {
      jrfCART.after_lookUp();
    }
  };
  public JlrNavField jrfJM = new JlrNavField(){
    public void after_lookUp()
    {
      jrfCART.after_lookUp();
    }
  };
  public JLabel jlArtikl = new JLabel();
  public JLabel jlNaziv = new JLabel();
  public JLabel jlSifra = new JLabel();
  public JLabel jlOznaka = new JLabel();
  public JLabel jlBarcode = new JLabel();
  public JLabel jlGrupa = new JLabel();
  TableDataSet tds = new TableDataSet();
  dM dm = dM.getDataModule();
  Column column1= new Column();
  Column column2= new Column();
  Column column3= new Column();
  Column column4= new Column();
  Column column5= new Column();
  Column column6= new Column();
  raCommonClass rcc = raCommonClass.getraCommonClass();


  public jpCart()
  {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jrfCGRART.setNextFocusableComponent(jrfNAZART);
    xYLayout1.setWidth(655);
    xYLayout1.setHeight(75);
    this.setLayout(xYLayout1);
    jbCART.setText("...");
    jlArtikl.setText("Artikl");
    jlNaziv.setText("Naziv / Jm");
    jlSifra.setText("Šifra");
    jlOznaka.setText("Oznaka");
    jlBarcode.setText("Barcode");
    jlGrupa.setText("Grupa");

    column1.setColumnName("CART");
    column1.setDataType(com.borland.dx.dataset.Variant.INT);
    column1.setDisplayMask("#####0");
    column1.setSqlType(0);
    column1.setDefault("");

    column2.setColumnName("CART1");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setDefault("");
    column2.setResolvable(false);
    column2.setSqlType(0);

    column3.setColumnName("BC");
    column3.setDataType(com.borland.dx.dataset.Variant.STRING);
    column3.setDefault("");
    column3.setResolvable(false);
    column3.setSqlType(0);

    column4.setColumnName("CGRART");
    column4.setDataType(com.borland.dx.dataset.Variant.STRING);
    column4.setDefault("");
    column4.setResolvable(false);
    column4.setSqlType(0);

    column5.setColumnName("NAZART");
    column5.setDataType(com.borland.dx.dataset.Variant.STRING);
    column5.setDefault("");
    column5.setResolvable(false);
    column5.setSqlType(0);

    column6.setColumnName("JM");
    column6.setDataType(com.borland.dx.dataset.Variant.STRING);
    column6.setDefault("");
    column6.setResolvable(false);
    column6.setSqlType(0);

    jrfCART.setColumnName("CART");
    jrfCART.setDataSet(tds);
    jrfCART.setColNames(new String[] {"CART1","BC","CGRART","NAZART","JM"});
    jrfCART.setTextFields(new JTextComponent[] {jrfCART1, jrfBC, jrfCGRART, jrfNAZART, jrfJM});
    jrfCART.setVisCols(new int[] {0, 1});
    jrfCART.setSearchMode(0);
    jrfCART.setRaDataSet(dm.getArtikli());
    jrfCART.setNavButton(jbCART);
    jrfCART1.setColumnName("CART1");
    jrfCART1.setNavProperties(jrfCART);
    jrfCART1.setSearchMode(1);
    jrfBC.setColumnName("BC");
    jrfBC.setNavProperties(jrfCART);
    jrfBC.setSearchMode(1);
    jrfCGRART.setColumnName("CGRART");
    jrfCGRART.setNavProperties(jrfCART);
    jrfCGRART.setSearchMode(1);
    jrfNAZART.setColumnName("NAZART");
    jrfNAZART.setNavProperties(jrfCART);
    jrfNAZART.setSearchMode(1);
    jrfJM.setColumnName("JM");
    jrfJM.setNavProperties(jrfCART);
    jrfJM.setSearchMode(1);

    tds.setColumns(new Column[] {column1, column2, column3, column4, column5, column6});

    this.add(jrfCART,       new XYConstraints(150, 20, 60, -1));
    this.add(jrfCART1,   new XYConstraints(215, 20, 135, -1));
    this.add(jrfCGRART,   new XYConstraints(495, 19, 105, -1));
    this.add(jrfBC, new XYConstraints(355, 20, 135, -1));
    this.add(jbCART,   new XYConstraints(605, 19, 21, 21));
    this.add(jrfNAZART,    new XYConstraints(150, 45, 405, -1));
    this.add(jrfJM,      new XYConstraints(560, 45, 40, -1));
    this.add(jlArtikl,  new XYConstraints(15, 20, -1, -1));
    this.add(jlNaziv,  new XYConstraints(15, 45, -1, -1));
    this.add(jlSifra,   new XYConstraints(150, 3, -1, -1));
    this.add(jlOznaka,   new XYConstraints(215, 3, -1, -1));
    this.add(jlBarcode,   new XYConstraints(355, 3, -1, -1));
    this.add(jlGrupa,  new XYConstraints(495, 3, -1, -1));
  }

  public void setCARTFocus()
  {
    String parSifra = hr.restart.sisfun.frmParam.getParam("robno","focusCart");
    String param = hr.restart.sisfun.frmParam.getParam("robno","indiCart");
    if(parSifra.equals("SIFRA"))
    {
      jrfNAZART.forceFocLost();
      if(param.equals("CART"))
        jrfCART.requestFocus();
      else if (param.equals("CART1"))
        jrfCART1.requestFocus();
      else if (param.equals("BC"))
        jrfBC.requestFocus();
    }
    else
      jrfNAZART.requestFocus();
  }
  public void disableAfterLookUp(boolean disable)
  {
    disabling = disable;
  }
  public void nextToFocus(){}
}