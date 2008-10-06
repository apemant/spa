/****license*****************************************************************
**   file: raPopravakRezKol.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2004.12.10
 * 
 * Sad je koncipirano tako da poništava sve rezervacije iz ponuda èiji je datum dospjeæa manji od
 * današnjeg datuma. Omoguæeno poništavanje po skladištima kao i poništavanje svih rezervacija u
 * tekuæoj godini bez obzira vrijede li ili ne. 
 * 
 */

public class raPopravakRezKol extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  protected XYLayout xYLayout1 = new XYLayout();
  JPanel mainPanel = new JPanel();
  
  QueryDataSet fieldSet = new QueryDataSet();
  QueryDataSet dummyStanje;
  
  JLabel jlKnjig = new JLabel("Org. jedinica");
  JLabel jlaNazKnjig = new JLabel();
  
  JraButton jbSelKnjig = new JraButton();
  JlrNavField jlrKnjig = new JlrNavField(){

    public void after_lookUp() {
      if (isLastLookSuccessfull()){
        fieldSet.setString("CSKL","");
        setSkladSet();
        jlrSklad.emptyTextFields();
        jlrSklad.requestFocusLater();
      }
    }
  };
  JlrNavField jlrNazKnjig = new JlrNavField(){

    public void after_lookUp() {
      if (isLastLookSuccessfull()){
        fieldSet.setString("CSKL","");
        setSkladSet();
        jlrSklad.emptyTextFields();
        jlrSklad.requestFocusLater();
      }
    }
  };
  
  JraButton jbSelSklad = new JraButton();
  JlrNavField jlrSklad = new JlrNavField();
  JlrNavField jlrNazSklad = new JlrNavField();
  
  JCheckBox ponAll = new JCheckBox("Poništiti sve rezervacije u svim skladištim za zadano knjigovodstvo");
  
  public raPopravakRezKol() {
    try {
      jbInit();
    }
    catch (Exception ex) {
    }
  }
  
  private String deleteAllRezKol = "";
  private boolean sve5;

  public void okPress() {
    
    if (ponAll.isSelected()){
      deleteAllRezKol = "update stanje set stanje.kolrez = 0 where stanje.kolrez > 0 and stanje.cskl in (" + (fieldSet.getString("CSKL").equals("") ? skladsFromOrg(fieldSet.getString("CORG")) : "'"+fieldSet.getString("CSKL")+"'") + ") and stanje.god = '"+Aut.getAut().getKnjigodRobno()+"'";
      return;
    }
    
    String rezervacije = "SELECT "+
        "(stdoki.cart) as cart, "+
        "(stdoki.kol) as kol, "+
        "(stdoki.csklart) as cskl "+
        "FROM doki,stdoki " +
        "WHERE " + hr.restart.robno.Util.getUtil().getDoc("DOKI", "STDOKI") +
        " and doki.god = '"+Aut.getAut().getKnjigodRobno()+"'"+
        " and doki.datdosp < '"+vl.getToday()+"'"+
        " and stdoki.csklart in (" + (fieldSet.getString("CSKL").equals("") ? skladsFromOrg(fieldSet.getString("CORG")) : "'"+fieldSet.getString("CSKL")+"'") + ")"+ 
        " and stdoki.rezkol='D' and doki.statira='N' and doki.vrdok='PON' order by cskl,cart";
    
    QueryDataSet rezervacijeSet = ut.getNewQueryDataSet(rezervacije);
    
    if (rezervacijeSet.rowCount() == 0) {
      sve5 = true;
      return;
    } else {
      sve5 = false;
    }
    
    dummyStanje = hr.restart.baza.Stanje.getDataModule().getTempSet("god = '"+Aut.getAut().getKnjigodRobno()+
        "' and cskl in ("+ skladsFromOrg(fieldSet.getString("CORG")) +") and stanje.kolrez > 0");
    dummyStanje.open();
    dummyStanje.first();
    rezervacijeSet.first();
    
    int kaunter = 1;
    
    do {
      if (dummyStanje.getBigDecimal("KOLREZ").compareTo(_Main.nul) > 0) {
        if (lookupData.getlookupData().raLocate(dummyStanje, new String[]{"CSKL", "CART"}, new String[]{rezervacijeSet.getString("CSKL"), rezervacijeSet.getInt("CART") + ""})) {
          dummyStanje.setBigDecimal("KOLREZ", dummyStanje.getBigDecimal("KOLREZ").subtract(rezervacijeSet.getBigDecimal("KOL")));
          if (dummyStanje.getBigDecimal("KOLREZ").compareTo(_Main.nul) < 0)
            dummyStanje.setBigDecimal("KOLREZ", _Main.nul);
          System.out.println("novo stanje za "+dummyStanje.getInt("CART")+" je " + dummyStanje.getBigDecimal("KOLREZ")); //XDEBUG delete when no more needed
        }
      }
    } while (rezervacijeSet.next());
  }
  
  

  public void afterOKPress() {
    if (sve5) {
      JOptionPane.showMessageDialog(this.mainPanel, new String[]{"Nema rezervacija za poništavanje"}, "Obavjest", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    } else {
      if (ponAll.isSelected()) {
        if (executePonistenje()) {
          JOptionPane.showMessageDialog(this.mainPanel, new String[]{"Rezervirane kolièine su promptno poništene bez obzira da li postoje ili ne (èista sila)"}, "Obavjest", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(this.mainPanel, new String[]{"Poništenje rezerviranih kolièina nije uspio"}, "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
      } else {
        if (saveStanjeInTransaction()) {
          JOptionPane.showMessageDialog(this.mainPanel, new String[]{"Rezervirane kolièine su popravljene"}, "Obavjest", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(this.mainPanel, new String[]{"Popravak rezerviranih kolièina nije uspio"}, "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    firstESC();
  }
  
  protected boolean saveStanjeInTransaction(){
    raLocalTransaction saveStanje = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.saveChanges(dummyStanje);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return saveStanje.execTransaction(); /* false; // */
  }
  
  protected boolean executePonistenje() {
    raLocalTransaction saveStanje = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.runSQL(deleteAllRezKol);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return saveStanje.execTransaction();
  }
  
  

  public boolean Validacija() {
    return !vl.isEmpty(jlrKnjig);
  }
  
  public void componentShow() {
    fieldSet.setString("CORG",OrgStr.getKNJCORG());
    setSkladSet();
    jlrKnjig.forceFocLost();
    jlrSklad.requestFocusLater();
  }

  public boolean runFirstESC() {
    return true;
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return false;
  }


  private void jbInit() throws Exception {
    
    fieldSet.setColumns(new Column[] {
        dm.createStringColumn("CORG",0),
        dm.createStringColumn("CSKL",0)
      }
    );
    fieldSet.open();
    
    jlrKnjig.setColumnName("CORG");
    jlrKnjig.setColNames(new String[] {"NAZIV"});
    jlrKnjig.setDataSet(fieldSet);
    jlrKnjig.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKnjig});
    jlrKnjig.setVisCols(new int[] {0, 1});
    jlrKnjig.setSearchMode(0);
    jlrKnjig.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrKnjig.setNavButton(jbSelKnjig);

    jlrNazKnjig.setColumnName("NAZIV");
    jlrNazKnjig.setNavProperties(jlrKnjig);
    jlrNazKnjig.setSearchMode(1);
    
    jlrSklad.setColumnName("CSKL");
    jlrSklad.setColNames(new String[] {"NAZSKL"});
    jlrSklad.setDataSet(fieldSet);
    jlrSklad.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSklad});
    jlrSklad.setVisCols(new int[] {0, 1});
    jlrSklad.setSearchMode(0);
    jlrSklad.setRaDataSet(dm.getSklad());
    jlrSklad.setNavButton(jbSelSklad);
    
    jlrNazKnjig.setColumnName("NAZSKL");
    jlrNazKnjig.setNavProperties(jlrSklad);
    jlrNazKnjig.setSearchMode(1);

    mainPanel.setLayout(xYLayout1);

    this.setJPan(mainPanel);

    xYLayout1.setWidth(610);
    xYLayout1.setHeight(115);

    mainPanel.add(jlKnjig, new XYConstraints(15, 15, -1, -1));
    mainPanel.add(jlrKnjig, new XYConstraints(150, 15, 100, -1));
    mainPanel.add(jlrNazKnjig, new XYConstraints(255, 15, 295, -1));
    mainPanel.add(jbSelKnjig, new XYConstraints(555, 15, 21, 21));
    mainPanel.add(jlrSklad, new XYConstraints(15, 45, -1, -1));
    mainPanel.add(jlrSklad, new XYConstraints(150, 45, 100, -1));
    mainPanel.add(jlrNazSklad, new XYConstraints(255, 45, 295, -1));
    mainPanel.add(jbSelSklad, new XYConstraints(555, 45, 21, 21));
    mainPanel.add(ponAll, new XYConstraints(150, 75, -1, -1));
  }
  
  private String skladsFromOrg(String org){
    String sklads = "";
    QueryDataSet ss = sgQuerys.getSgQuerys().getObradeRadDvijeGodineDohvatSkladista(org);
    for (;;){
      sklads += "'"+ss.getString("CSKL")+"'";
      if (ss.next()) sklads += ",";
      else break;
    }
    System.err.println("sklads in ("+sklads+")");
    return sklads; 
  }
  
  private String olCorg = "";
  
  private void setSkladSet(){
    if (olCorg == fieldSet.getString("CORG")) return;
    System.out.println("CORG!¨!! " + fieldSet.getString("CORG")); //XDEBUG delete when no more needed
    olCorg = fieldSet.getString("CORG");
    jlrSklad.setRaDataSet(sgQuerys.getSgQuerys().getObradeRadDvijeGodineDohvatSkladista(olCorg));
  }

}