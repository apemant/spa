/****license*****************************************************************
**   file: raArtiklUnos.java
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

import hr.restart.swing.JraTextField;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class raArtiklUnos extends raArtikl {
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private String godina;
  private String skl;
  private QueryDataSet tabela;
  private char ulazIzlaz='I';
  private boolean myAfterLookupOnNavigate=true;
  private QueryDataSet tds = new QueryDataSet();
  private QueryDataSet qds = new QueryDataSet();
  private Column KOL = new Column("KOL","Koli\u010Dina na zalihi", Variant.BIGDECIMAL);
  private Column REZKOL = new Column("REZKOL","Rezervirana kolièina", Variant.BIGDECIMAL);
  private Column TREKOL = new Column("TREKOL","Raspoloživa kolièina", Variant.BIGDECIMAL);
  String qStr;
  JPanel jpSouth = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlKolicina = new JLabel();
  JLabel jlStvarna = new JLabel();
  JLabel jlRezervirana = new JLabel();
  JLabel jlRaspoloziva = new JLabel();
  JraTextField jtfSTVARNA = new JraTextField();
  JraTextField jtfREZERVIRANA = new JraTextField();
  JraTextField jtfRASPOLOZIVA = new JraTextField();
  JraTextField jtfJM = new JraTextField();
  private String tempNAZART = ""; // member varijabla dodana radi hendlamnja nove i stare usluge
                                  // najgluplje na svijetu = najefikasnije na svijetu


  public raArtiklUnos() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    KOL.setDisplayMask("###,###,##0.000");
    KOL.setDefault("0");
    REZKOL.setDisplayMask("###,###,##0.000");
    REZKOL.setDefault("0");
    TREKOL.setDisplayMask("###,###,##0.000");
    TREKOL.setDefault("0");
    tds.setColumns(new Column[] {KOL, REZKOL, TREKOL});

    jlKolicina.setText("Kolièina");
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(55);
    jpSouth.setLayout(xYLayout1);
    jlStvarna.setHorizontalAlignment(SwingConstants.CENTER);
    jlStvarna.setText("Stvarna");
    jlRezervirana.setHorizontalAlignment(SwingConstants.CENTER);
    jlRezervirana.setText("Rezervirana");
    jlRaspoloziva.setHorizontalAlignment(SwingConstants.CENTER);
    jlRaspoloziva.setText("Raspoloživa");
    jtfSTVARNA.setFont(jtfSTVARNA.getFont().deriveFont(Font.BOLD));
    jtfSTVARNA.setColumnName("KOL");
    jtfSTVARNA.setDataSet(tds);
    jtfREZERVIRANA.setFont(jtfREZERVIRANA.getFont().deriveFont(Font.BOLD));
    jtfREZERVIRANA.setColumnName("REZKOL");
    jtfREZERVIRANA.setDataSet(tds);
    jtfRASPOLOZIVA.setFont(jtfRASPOLOZIVA.getFont().deriveFont(Font.BOLD));
    jtfRASPOLOZIVA.setColumnName("TREKOL");
    jtfRASPOLOZIVA.setDataSet(tds);
    jtfJM.setColumnName("JM");
    jpSouth.add(jlKolicina,  new XYConstraints(15, 20, -1, -1));
    jpSouth.add(jlStvarna,    new XYConstraints(150, 0, 130, -1));
    jpSouth.add(jlRezervirana,   new XYConstraints(285, 0, 130, -1));
    jpSouth.add(jlRaspoloziva,    new XYConstraints(420, 0, 130, -1));
    jpSouth.add(jtfSTVARNA,    new XYConstraints(150, 18, 130, -1));
    jpSouth.add(jtfREZERVIRANA,   new XYConstraints(285, 18, 130, -1));
    jpSouth.add(jtfRASPOLOZIVA,    new XYConstraints(420, 18, 130, -1));
    jpSouth.add(jtfJM,     new XYConstraints(555, 18, 49, -1));
    rcc.setLabelLaF(jtfSTVARNA, false);
//    super.add(jpSouth, BorderLayout.SOUTH);
  }
  public void setGodina(String str) {
    godina=str;
  }
  public void setCskl(String str) {
    skl=str;
  }
  public void setTabela(QueryDataSet qds) {
    tabela=qds;
    jrfCART.setDataSet(tabela);
    jrfCART1.setDataSet(tabela);
    jrfBC.setDataSet(tabela);
    jrfNAZART.setDataSet(tabela);
    jtfJM.setDataSet(tabela);
  }
  public QueryDataSet getTabela() {
    return tabela;
  }
  public String getGodina() {
    return godina;
  }
  public String getCskl() {
    return skl;
  }
  public void setUlazIzlaz(char ui) {
    ulazIzlaz=ui;
  }
  public void setMyAfterLookupOnNavigate(boolean b) {
    myAfterLookupOnNavigate=b;
  }
/**
 * deprecated
 * @param mode
 */
  public void setMode(String mode) {
    setMode();
  }
  public void setMode() {
    rcc.setLabelLaF(jtfSTVARNA, false);
    rcc.setLabelLaF(jtfREZERVIRANA, false);
    rcc.setLabelLaF(jtfRASPOLOZIVA, false);
    rcc.setLabelLaF(jtfJM, false);
    tempNAZART=getTabela().getString("NAZART");
  }
/**
 * @deprecated - InitRaPanCart nije potrebno koristiti !!!
 */
  public void InitRaPanCart(){
//    System.out.println("InitRaPanCart nije potrebno koristiti !!!");
  }
  public void afterLookup() {
    System.out.println("afterlukap");
    if (jrfCART.getText().trim().equals("")) {
      removeKOL();
      return;
    }
    if (!isMyAfterLookup()) return;
    findStanje();
    nextTofocus();
    if (isUsluga()) {    // Ako je usluga
      if(tempNAZART.trim().equals("")) {
        jrfNAZART.setDataSet(this.getTabela());
      }
      else {
        tabela.setString("NAZART", tempNAZART);
        jrfNAZART.setDataSet(this.getTabela());
      }
      jrfNAZART.setText(getTabela().getString("NAZART"));
        if (this.ulazIzlaz=='U') {
          JOptionPane.showConfirmDialog(null,"Stavka usluge ne može se upisati na ulaznom dokumentu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          this.setCART();
          return;
        }
      }
        jrfNAZART.setText(this.getTabela().getString("NAZART"));//-> tu je linija koda koju treba negdje smjestiti
      this.EnabDisab(false);
    metToDo_after_lookUp();
  }
  public void metToDo_after_lookUp() {
  }
  public boolean isMyAfterLookup() {
    return true;
//    return myAfterLookupOnNavigate||jrfCART.isEnabled();
  }
  Thread StanjeFinder = new Thread(new Runnable() {
    public void run() {
      System.out.println("stanje finder.run");
      tabela.setString("JM", jrfJM.getText());
      if (findStanje(godina,skl,tabela.getInt("CART"))) {
        tds.open();
        tds.insertRow(true);
//        ST.prn(tds);
        tds.setBigDecimal("KOL",    qds.getBigDecimal("KOL"));
        tds.setBigDecimal("REZKOL", qds.getBigDecimal("KOLREZ"));
        tds.setBigDecimal("TREKOL", util.negateValue(tds.getBigDecimal("KOL"), tds.getBigDecimal("REZKOL")));
//        jtfJM.setText(jrfJM.getText());
      }
      else {
        removeKOL();
      }
    }     // end run
});       // end thread

  void findStanje() {
    System.out.println("find stanje");
    StanjeFinder.run();
  }
  boolean findStanje(String godina, String skl, int art) {
    qStr="select stanje.kol as kol, stanje.kolrez as kolrez "+
      "from stanje "+
      "where god = '"+godina +"'"+" and cskl='"+skl+"' and cart="+new Integer(art).toString();
    vl.execSQL(qStr);
    qds=vl.RezSet;
    qds.open();
    System.out.println("qds: "+qStr+ qds.rowCount());
    if (qds.rowCount()>0) {
      return true;
    }
    return false;
  }
  public void removeKOL() {
    tds.open();
    tds.setBigDecimal("KOL",util.nul);
    tds.setBigDecimal("REZKOL",util.nul);
    tds.setBigDecimal("TREKOL",util.nul);
    jtfJM.setText("");
  }
  public void checkOther(boolean istina) {
    if (istina && jrfCART.getText().equals("")) {
      System.out.println("removeKOL");
      removeKOL();
    }
  }
}