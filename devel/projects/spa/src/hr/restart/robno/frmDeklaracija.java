/****license*****************************************************************
**   file: frmDeklaracija.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmDeklaracija extends raUpitLite{
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  JPanel jp = new JPanel();
  boolean OKPressed = false;
  jpCart jpC = new jpCart(){
    public void nextToFocus(){
      if(!jpC.jrfCART.getText().equals("") && !OKPressed) {
        setProizCijena(jpC.jrfCART.getText());
        if(jraProiz.getText().equals(""))
          jraProiz.requestFocus();
        else
          jraZemlja.requestFocus();
      } else
        jpC.jrfCART.requestFocus();
    }
  };

  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  static frmDeklaracija fD;
  boolean ispisCijene = false;
  Valid vl = Valid.getValid();
//  Column column1 = new Column();
//  Column column2 = new Column();
//  Column column3 = new Column();
//  Column column4 = new Column();
//  Column column5 = new Column();
//  Column column6 = new Column();
  hr.restart.robno.Util utRobno = hr.restart.robno.Util.getUtil();


  public static frmDeklaracija getInstance(){
    if(fD==null)
      fD = new frmDeklaracija();
    return fD;
  }

  private XYLayout xYLayout1 = new XYLayout();
  private JPanel jpCB = new JPanel();
  private XYLayout xYLayout2 = new XYLayout();
  private Border border1;
  private TitledBorder titledBorder1;
  private JraTextField jraProiz = new JraTextField();
  private JraTextField jraUvoz = new JraTextField();
  private JraTextField jraZemlja = new JraTextField();
  private JraTextField jraGod = new JraTextField();
  private JraTextField jraCijena = new JraTextField();
  private JraTextField jraBrojKomada = new JraTextField();
  static TableDataSet tds = new TableDataSet();
  TitledBorder titledBorder2;
  private JLabel jlProiz = new JLabel();
  private JLabel jlUvoz = new JLabel();
  private JLabel jlZemlja = new JLabel();
  private JLabel jlGod = new JLabel();
  private JLabel jlCijena = new JLabel();
  private JraCheckBox jcbCijena = new JraCheckBox();

  public frmDeklaracija(){
    fD = this;
    try {
      jbInit();
      } catch (Exception ex){}
  }

  private void jbInit() throws Exception {    
    
    tds.setColumns(new Column[] {dm.createStringColumn("PROIZ", 90),
        dm.createStringColumn("UVOZ",90),
        dm.createStringColumn("ZEMLJA", 15),
        dm.createStringColumn("GODINA",4),
        dm.createBigDecimalColumn("CIJENA"),
        dm.createStringColumn("CART",10),
        dm.createIntColumn("BRKOM")
    });

    xYLayout1.setWidth(655);
    xYLayout1.setHeight(195);
    jp.setLayout(xYLayout1);
    jraProiz.setDataSet(tds);
    jraProiz.setColumnName("PROIZ");
    jraUvoz.setDataSet(tds);
    jraUvoz.setColumnName("UVOZ");
    jraZemlja.setDataSet(tds);
    jraZemlja.setColumnName("ZEMLJA");
    jraGod.setDataSet(tds);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    jraGod.setColumnName("GODINA");
    
    jraCijena.setDataSet(tds);
    jraCijena.setColumnName("CIJENA");
    
    jraBrojKomada.setDataSet(tds);
    jraBrojKomada.setColumnName("BRKOM");

//    column1 = dm.createStringColumn("PROIZ", 20);
//    column2 = dm.createStringColumn("UVOZ",20);
//    column3 = dm.createStringColumn("ZEMLJA", 15);
//    column4 = dm.createStringColumn("GODINA",4);
//    column5 = dm.createBigDecimalColumn("CIJENA");
//    column6 = dm.createStringColumn("CART",10);

    jlProiz.setText("Proizvo\u0111a\u010D");
    jlUvoz.setText("Uvoznik");
    jlZemlja.setText("Zemlja porijekla");
    jlGod.setText("Godina proizvodnje");
    jlCijena.setText("Cijena");
    jcbCijena.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbCijena.setText("Ispisati cijenu");
    jp.add(jpC,     new XYConstraints(0, 5, 655, 75));
    jp.add(jraProiz,              new XYConstraints(150, 80, 155, -1));
    jp.add(jlProiz,    new XYConstraints(15, 80, -1, -1));
    jp.add(jcbCijena,    new XYConstraints(507, 130, -1, -1));
    jp.add(jlUvoz,  new XYConstraints(15, 105, -1, -1));
    jp.add(jraUvoz, new XYConstraints(150, 105, 155, -1));
//--------------------------------------------------------------------------------------------
    jp.add(jlGod,  new XYConstraints(15, 130, -1, -1));
    jp.add(jraGod,  new XYConstraints(149, 130, -1, -1));
    jp.add(new JLabel("Broj komada"),  new XYConstraints(15, 155, -1, -1));
    jp.add(jraBrojKomada,  new XYConstraints(149, 155, -1, -1));
//  --------------------------------------------------------------------------------------------
    jp.add(jlCijena,  new XYConstraints(355, 105, -1, -1));
    jp.add(jraCijena,   new XYConstraints(465, 105, 135, -1));
//  --------------------------------------------------------------------------------------------
    jp.add(jraZemlja,   new XYConstraints(465, 80, 135, -1));
    jp.add(jlZemlja,  new XYConstraints(355, 80, -1, -1));
    this.setJPan(jp);
//    this.addReport("hr.restart.robno.repDeklaracija","hr.restart.robno.repDeklaracija","DeklaracijaNew","Ispis deklaracija");
    this.addReport("hr.restart.robno.repDeklaracija","hr.restart.robno.repDeklaracija","Deklaracija3x8","Deklaracija 8x3 24 kom/str"); 
    this.addReport("hr.restart.robno.repDeklaracija","hr.restart.robno.repDeklaracija","DeklaracijaNewCols","Deklaracija 5x2 10 kom/str"); 
  }

  public void componentShow() {
    tds.setInt("BRKOM",24);
  }


  public boolean runFirstESC() {
    if(!jpC.jrfCART.getText().equals(""))
      return true;
    return false;
  }

  public void firstESC() {
    if(!jpC.jrfCART.getText().equals("")){
      rcc.EnabDisabAll(jp,true);
      if (!OKPressed) {
        reset();
        jpC.jrfCART.setText("");
        jpC.jrfCART.forceFocLost();
        jpC.setCARTFocus();
      } else 
      OKPressed = false;
    }
  }
  
  private QueryDataSet repSet; 

  public void okPress(){
    repSet = new QueryDataSet();
    repSet.setColumns(tds.cloneColumns());
    repSet.open();
    for (int i=0; i<tds.getInt("BRKOM"); i++){
      repSet.insertRow(false);
      tds.copyTo(repSet);
    }
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public boolean Validacija(){
    OKPressed = true;
    ispisCijene=false;
    tds.setString("CART", jpC.jrfCART.getText());
    if(jcbCijena.isSelected())
      ispisCijene=true;
    this.getRepRunner().clearAllReports();
    if(jpC.jrfCART.getText().equals("")){
      jpC.jrfCART.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if(jraProiz.getText().equals("")) {
      jraProiz.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos proizvoðaèa !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    if(jraZemlja.getText().equals("")){
      jraZemlja.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos zemlje porijekla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    if(jraUvoz.getText().equals("")){
      jraUvoz.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos uvoznika !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    if(!checkYear(jraGod.getText())) return false;
    return true;
  }

  private void reset() {
    jcbCijena.setSelected(true);
    if(!tds.open()) tds.open();
    tds.setString("PROIZ","");
    tds.setString("UVOZ","");
    tds.setString("ZEMLJA","");
    tds.setString("GODINA","");
    tds.setBigDecimal("Cijena",new BigDecimal(0));
  }

  private void setProizCijena(String cart){
    if(!tds.open()) tds.open();
    ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{cart});
    tds.setBigDecimal("CIJENA", dm.getArtikli().getBigDecimal("MC"));
    tds.setString("PROIZ", dm.getArtikli().getString("NAZPROIZ"));
  }

  public QueryDataSet getTds(){
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(repSet);
    return repSet;
  }

  public void show(){
    reset();
    super.show();
  }

  private boolean checkYear(String y){
    if(y.length()!=4){
      jraGod.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Neispravan unos godine !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jraGod.setBackground(Color.red);
      return false;
    }
    try {
      Integer i = new Integer(y);
    } catch (Exception ex){
      jraGod.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Neispravan unos godine !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jraGod.setBackground(Color.red);
      return false;
    }
    return true;
  }
}