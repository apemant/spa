/****license*****************************************************************
**   file: raPanKonto.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raPanKonto extends JPanel {
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  private QueryDataSet qdsDohvatKnjigovodstva = new QueryDataSet();
  private QueryDataSet qdsDohvatKonta = new QueryDataSet();

  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrCorg = new JlrNavField(){
    public void after_lookUp() {
      jlrCorg_lookup();
    }
  };
  JlrNavField jlrNazorg = new JlrNavField(){
    public void after_lookUp() {
      jlrCorg_lookup();
    }
  };
  
  JlrNavField jlrKontoNaziv = new JlrNavField() {
    public void after_lookUp() {
      jlrKontoBroj_lookup();
    }
  };
  JraButton jbSelBrKon = new JraButton();

  JlrNavField jlrKontoBroj = new JlrNavField() {
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(3);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
    public void after_lookUp() {
      jlrKontoBroj_lookup();
    }
  };

  JraButton jbSelCorg = new JraButton();
  JLabel jlKonto = new JLabel();
  JLabel jlCorg = new JLabel();

  private boolean noLookup = false;

  public raPanKonto() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public raPanKonto(String b) {
    if (b.substring(2,4).equals("NO")) setNoLookup(true);
    try {
      if (b.substring(0,1).equals("C")) jbInit2();
      else jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setNoLookup(boolean dontDoLookup){
    this.noLookup = dontDoLookup;
  }

  public boolean isNoLookup(){
    return noLookup;
  }

  private boolean gotOrgStrukturu;

  private void jbInit() throws Exception {
    qdsDohvatKnjigovodstva.setColumns(new Column[] {dm.createStringColumn("CORG",12)});
    qdsDohvatKonta.setColumns(new Column[] {dm.createStringColumn("BROJKONTA",10)});
    qdsDohvatKnjigovodstva.open();
    qdsDohvatKonta.open();

    jlCorg.setText("Org. jedinica");

    jlrCorg.setDataSet(qdsDohvatKnjigovodstva);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.getDataSet().open();
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setColumnName("CORG");
    jlrCorg.setNavButton(jbSelCorg);

    gotOrgStrukturu = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig().rowCount() > 1;
//    System.out.println("gotOrgStrukturu - " + gotOrgStrukturu);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jlKonto.setText("Broj konta");

    jlrKontoBroj.setDataSet(qdsDohvatKonta);
    jlrKontoBroj.setRaDataSet(getDohvatKonta(""));
    jlrKontoBroj.setSearchMode(lookupData.EXACT, lookupData.TEXT);
    jlrKontoBroj.setVisCols(new int[] {0, 1});
    jlrKontoBroj.setTextFields(new javax.swing.text.JTextComponent[] {jlrKontoNaziv});
    jlrKontoBroj.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontoBroj.setColumnName("BROJKONTA");
    jlrKontoBroj.setNavButton(jbSelBrKon);

    jlrKontoNaziv.setNavProperties(jlrKontoBroj);
    jlrKontoNaziv.setColumnName("NAZIVKONTA");
    jlrKontoNaziv.setSearchMode(1);

    this.setLayout(xYLayout1);

    xYLayout1.setWidth(609);
    xYLayout1.setHeight(48);
    this.setMinimumSize(new Dimension(50, 50));
    this.setPreferredSize(new Dimension(570, 50));
    this.add(jlrNazorg,  new XYConstraints(240, 25, 285, -1));
    this.add(jlrKontoNaziv,  new XYConstraints(240, 0, 285, -1));
    this.add(jbSelBrKon,  new XYConstraints(530, 0, 21, 21));
    this.add(jlrKontoBroj, new XYConstraints(135, 0, 100, -1));
    this.add(jbSelCorg,  new XYConstraints(530, 25, 21, 21));
    this.add(jlKonto, new XYConstraints(0, 0, -1, -1));
    this.add(jlCorg, new XYConstraints(0, 25, -1, -1));
    this.add(jlrCorg, new XYConstraints(135, 25, 100, -1));
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        if (isVisible()) {
          setcORG(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
        }
        gotOrgStrukturu = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig().rowCount() > 1;
//        System.out.println("Knjig change listener: gotOrgStrukturu - " + gotOrgStrukturu);
      }
    });
  }

  private void jbInit2() throws Exception {

    qdsDohvatKnjigovodstva.setColumns(new Column[] {dm.createStringColumn("CORG",12)});
    qdsDohvatKonta.setColumns(new Column[] {dm.createStringColumn("BROJKONTA",10)});

    jlCorg.setText("Org. jedinica");

    jlrCorg.setDataSet(qdsDohvatKnjigovodstva);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setSearchMode(0);
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setColumnName("CORG");
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jlKonto.setText("Konto broj");

    jlrKontoBroj.setDataSet(qdsDohvatKonta);
    jlrKontoBroj.setRaDataSet(getDohvatKonta(""));
    jlrKontoBroj.setSearchMode(3);
    jlrKontoBroj.setVisCols(new int[] {0, 1});
    jlrKontoBroj.setTextFields(new javax.swing.text.JTextComponent[] {jlrKontoNaziv});
    jlrKontoBroj.setColNames(new String[] {"NAZIVKONTA"});
    jlrKontoBroj.setColumnName("BROJKONTA");
    jlrKontoBroj.setNavButton(jbSelBrKon);

    jlrKontoNaziv.setNavProperties(jlrKontoBroj);
    jlrKontoNaziv.setColumnName("NAZIVKONTA");
    jlrKontoNaziv.setSearchMode(1);

    this.setLayout(xYLayout1);

    xYLayout1.setWidth(609);
    xYLayout1.setHeight(48);
    this.setMinimumSize(new Dimension(50, 25));
    this.setPreferredSize(new Dimension(570, 25));
    this.add(jlrNazorg,  new XYConstraints(240, 0, 285, -1));
    this.add(jbSelCorg,  new XYConstraints(530, 0, 21, 21));
    this.add(jlCorg, new XYConstraints(0, 0, -1, -1));
    this.add(jlrCorg, new XYConstraints(135, 0, 100, -1));
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        if (isVisible()) {
          setcORG(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
        }
        gotOrgStrukturu = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig().rowCount() > 1;
//        System.out.println("Knjig change listener: gotOrgStrukturu - " + gotOrgStrukturu);

//        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  public QueryDataSet getDohvatKonta(String analitik) {
    if (analitik.equals("A")) return hr.restart.zapod.raKonta.getAnalitickaKonta();
    /*else {
      String queryString = "select max(brojkonta) as brojkonta, max(nazivkonta) as nazivkonta from konta group by brojkonta";
      qdsDohvatKonta = new QueryDataSet();
      qdsDohvatKonta.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
      qdsDohvatKonta.open();
      return qdsDohvatKonta;
    }*/
    return dM.getDataModule().getKonta();
  }

  public void jlrCorg_lookup() {}

  public void jlrKontoBroj_lookup() {
    if(!noLookup){
      String brKonta = jlrKontoBroj.getText().trim();
//      System.out.println(".");
//      System.out.println("!gotOrgStrukturu - " + (!gotOrgStrukturu));
      
      if(brKonta.equals("")) return;
      if(!gotOrgStrukturu) return;
      
//      System.out.println("hr.restart.zapod.raKonta.isAnalitik(brKonta) - " + hr.restart.zapod.raKonta.isAnalitik(brKonta));
//      System.out.println("hr.restart.zapod.raKonta.isOrgStr(brKonta)   - " + hr.restart.zapod.raKonta.isOrgStr(brKonta));
      
      try {
//        System.out.println("hr.restart.zapod.raKonta.isAnalitik(brKonta) " + hr.restart.zapod.raKonta.isAnalitik(brKonta));
//        System.out.println("hr.restart.zapod.raKonta.isOrgStr(brKonta)   " + hr.restart.zapod.raKonta.isOrgStr(brKonta));
        if(hr.restart.zapod.raKonta.isAnalitik(brKonta)){
          if(hr.restart.zapod.raKonta.isOrgStr(brKonta)) {
            rcc.setLabelLaF(jlrCorg, true);
            rcc.setLabelLaF(jlrNazorg, true);
            rcc.setLabelLaF(jbSelCorg, true);
            jlrCorg.requestFocus();
          } else {
            jlrCorg.setText(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
            jlrCorg.forceFocLost();
            rcc.setLabelLaF(jlrCorg, false);
            rcc.setLabelLaF(jlrNazorg, false);
            rcc.setLabelLaF(jbSelCorg, false);
          }
        } else {
          rcc.setLabelLaF(jlrCorg, true);
          rcc.setLabelLaF(jlrNazorg, true);
          rcc.setLabelLaF(jbSelCorg, true);
        }
      } catch (Exception ex) {
        System.err.println("Exception ocured: " + ex.toString());
      }
    }
  }
  public String getBrojKonta() {
    return jlrKontoBroj.getText(); //qdsDohvatKonta.getString("BROJKONTA");
  }
  public int getBrKonLength(){
    return jlrKontoBroj.getText().trim().length();
  }
  public void setBrojKonta(String brk) {
    qdsDohvatKonta.setString("BROJKONTA",brk);
  }
  public String getCorg() {
    return qdsDohvatKnjigovodstva.getString("CORG");
  }
  public void setcORG(String cor){
    qdsDohvatKnjigovodstva.setString("CORG",cor);
    jlrCorg.forceFocLost();
  }
  public boolean getStructure() {
    return (hr.restart.zapod.OrgStr.getOrgStr().getOrgstrFromKnjig(getBrojKonta()).rowCount() > 1);
  }
  public boolean getIsOrgstr() {
    StorageDataSet ojs;
    ojs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(jlrCorg.getText());
    return (ojs.getRowCount() > 1);
  }
}