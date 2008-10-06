/****license*****************************************************************
**   file: raNalazachLosihKartica.java
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
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.JlrNavField;
import hr.restart.util.raProcess;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * @deprecated nadam se da se nigdje ne koristi jer se ne zeli kompajlirati 02.ozujka 2005. 
 * Zakomentirana linija 129 cKar.checkKartica(...
 */
public class raNalazachLosihKartica  extends hr.restart.util.raUpit {

  private TableDataSet tds = new TableDataSet();
  private JLabel jlSklad = new JLabel("Skladište");
  private JraButton jbsklad = new JraButton();
  private JlrNavField jrfCSKL = new JlrNavField();
  private JlrNavField jrfNAZSKL = new JlrNavField();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private JPanel panel_za_upit = new JPanel();
  private JLabel jlGodina = new JLabel("Skladište");
  private JraTextField jrfGOD = new JraTextField();

  private String upozorenje = "    Korisnièe ovog dijela aplikacije, "+
                              "molimo Vas da ove retke pažljivo proèitate "+
                              "jer kad jednom pokrenete analizator© \n možete se JAKO iznenaditi.\n"+
                              "    Naime zbog obima podataka ova akcija može (i hoæe) JAKO DUGO "+
                              "trajati, gotovo do vjeènosti.\n Budite svijesni da analizator© provjerava svaki, "+
                              "ama baš svaki promet po svakom artiklu iz zadanog skladišta, zato oboružajte "+
                              "se strpljenjem i molite se onome u koga ovisno o religijskim sklonostima " +
                              "vjerujete, da æe lista koja nastaje kao rezultat Vašeg cijenjenog pritiska na tipku OK (analogno F10) "+
                              "biti što kraæa, ili ako ste osobito omilili Svevišnjem možda se izrodi prazna. SRETNO i STRPLJIVO (autor)";

  private StorageDataSet losidjaci;

  private void initSDS(){

    tds.setColumns(new Column[] {
                   dm.createStringColumn("godina", "Godina" ,4)});
    tds.open();
    tds.setString("godina",hr.restart.util.Valid.getValid().findYear());


    losidjaci = new StorageDataSet();
    Column cskl = dm.getStanje().getColumn("CSKL").cloneColumn();
    Column god = dm.getStanje().getColumn("GOD").cloneColumn();
    Column cart = dm.getStanje().getColumn("CART").cloneColumn();
    losidjaci.setColumns(new Column[]{cskl,god,cart});
    losidjaci.open();

  }





  public raNalazachLosihKartica() {
    initSDS();
    this.setTitle("Analizator");
    jrfGOD.setColumnName("godina");
    jrfGOD.setDataSet(tds);
    jrfGOD.setHorizontalAlignment(SwingUtilities.CENTER);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1,2});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(dm.getSklad());
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jbsklad.setText("...");
    jrfCSKL.setNavButton(jbsklad);

    XYLayout xyl = new XYLayout();
    xyl.setWidth(700);
    xyl.setHeight(200);
    panel_za_upit.setLayout(xyl);


    JPanel jlupozorenje = new raMultiLineMessage(upozorenje,JLabel.LEFT,120);
    jlupozorenje.setForeground(Color.red);

    panel_za_upit.add(jlSklad,new XYConstraints(15,15,-1,-1));
    panel_za_upit.add(jrfCSKL,new XYConstraints(150,15,100,-1));
    panel_za_upit.add(jrfNAZSKL,new XYConstraints(255,15,350,-1));
    panel_za_upit.add(jbsklad,new XYConstraints(610,15,21,21));
    panel_za_upit.add(jlGodina,new XYConstraints(15,45,-1,-1));
    panel_za_upit.add(jrfGOD,new XYConstraints(150,45,80,-1));
    panel_za_upit.add(jlupozorenje,new XYConstraints(15,75,-1,-1));
    this.setJPan(panel_za_upit);

  }


  public void componentShow(){
    jrfCSKL.setText("");
    jrfCSKL.emptyTextFields();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jrfCSKL.requestFocus();
      }
    });
  }

  public void firstESC(){
  }

  public boolean runFirstESC(){
    return true;
  }

  public void okPress(){

    checkKartica cKar = new checkKartica();
    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet("select * from stanje where cskl='"+
        jrfCSKL.getText()+"' and god='"+jrfGOD.getText()+"'");
    int boj = qds.getRowCount();
    int i = 0;
    if (boj==0) return ;
    raProcess.setMessage("Obradjeno 0/"+boj,false);

    for (qds.first();qds.inBounds();qds.next()){
//      cKar.checkKartica(qds.getString("CSKL"),qds.getString("GOD"),qds.getInt("CART"));
      if (!cKar.isKarticaOK()) {
        losidjaci.insertRow(false);
        losidjaci.setString("CSKL",qds.getString("CSKL"));
        losidjaci.setString("GOD",qds.getString("GOD"));
        losidjaci.setInt("CART",qds.getInt("CART"));
      }
      raProcess.setMessage("Obradjeno "+ i++ +"/"+boj,false);
    }
    this.getJPTV().setDataSet(losidjaci);
  }
}