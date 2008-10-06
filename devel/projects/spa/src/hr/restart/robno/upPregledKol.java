/****license*****************************************************************
**   file: upPregledKol.java
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
import hr.restart.sisfun.raUser;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
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

public class upPregledKol extends raUpitFat {

  JPanel comboPanel = new JPanel();
  JPanel mainPanel = new JPanel();
//  Column colST;
//  Column colAR;
//  Column colSK;
//  Column colNSK;

  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
//  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  TableDataSet tds = new TableDataSet();
  java.sql.Date dateP = null;
  static upPregledKol upk;
  XYLayout xYLayout1 = new XYLayout();
  Valid vl = Valid.getValid();
  dM dm = hr.restart.baza.dM.getDataModule();
  BorderLayout borderLayout1 = new BorderLayout();
  TableDataSet fieldSet = new TableDataSet();

  hr.restart.robno.rapancskl rpcskl = new hr.restart.robno.rapancskl() {
      public void findFocusAfter() {
        jcbStanje.requestFocus();
      }
  };
  
  raComboBox jcbStanje = new raComboBox();
  raComboBox jcbArtikli = new raComboBox();
  
  JLabel jlKolZal = new JLabel();
  JLabel jlVrsZal = new JLabel();

  //*** konstruktor
  public upPregledKol() {
    try {
      jbInit();
      upk=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static upPregledKol getInstance() {
    if (upk == null) {
      upk = new upPregledKol();
    }
    return upk;
  }
  
  int[] navVisCols;

  public void okPress() {
    String god = Aut.getAut().getKnjigodRobno();
    
    String cskling = ""; // "stanje.cskl = " + rpcskl.getCSKL() + " and "+
    
    if (rpcskl.getCSKL().equals("")){
      QueryDataSet csklovi = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("Knjig = '"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      csklovi.open();
      csklovi.first();
      cskling = "stanje.cskl in (";
      for (;;){
        cskling += "'"+csklovi.getString("CSKL")+"'";
        if (csklovi.next()) cskling += ",";
        else {
          cskling += ") and ";
          break;
        }
      }
    } else {
      cskling = "stanje.cskl = '" + rpcskl.getCSKL() + "' and ";
    }
    
    String qStr="select " +
    		"stanje.cskl, " +
    		"artikli.cart, " +
    		"artikli.cart1, " +
    		"artikli.bc, " +
//    		"artikli.nazart, " +
//    		"artikli.jm, " +
    		"("+fieldSet.getString("STANJE") + ") as A, "+
    		"("+fieldSet.getString("ARTIKLI") + ") as B "+

    		"from artikli, stanje " +

    		"where stanje.god='" + god + 
    		"' and artikli.cart = stanje.cart and " +
    		cskling +
    		"("+fieldSet.getString("STANJE") + ") <= (" + fieldSet.getString("ARTIKLI")+ ") and "+
    		fieldSet.getString("ARTIKLI") + " > 0 "+
    		" order by artikli.cart";
    		
    String poljeA= "", poljeB= "";
    
    if (jcbStanje.getSelectedIndex() == 0) poljeA = "Trenutna kol.";
    else poljeA = "Trenutna - rezervirana kol.";
    
    if (jcbArtikli.getSelectedIndex() == 0) poljeB = "Minimalna kol";
    else poljeB = "Signalna kol.";
    
    System.out.println("poljeA " + poljeA + " jcbArtikli sel index  - " + jcbArtikli.getSelectedIndex());
    System.out.println("poljeB " + poljeB + " jcbStanje sel index  - " + jcbStanje.getSelectedIndex());
    System.out.println("PREGLED:" + qStr);
    
    QueryDataSet tmpSet = ut.getNewQueryDataSet(qStr);
    
    if (tmpSet.rowCount() <= 0) setNoDataAndReturnImmediately();
    
    QueryDataSet workset = new QueryDataSet();
    workset.setColumns(new Column[] {
      (Column) dm.getStanje().getColumn("CSKL").clone(),
      dm.createIntColumn("CART","Artikl"),
//      (Column) dm.getArtikli().getColumn("CART").clone(),
      (Column) dm.getArtikli().getColumn("CART1").clone(),
      (Column) dm.getArtikli().getColumn("BC").clone(),
//      (Column) dm.getArtikli().getColumn("NAZART").clone(),
//      (Column) dm.getArtikli().getColumn("JM").clone(),
      dm.createBigDecimalColumn("A",poljeA,2),
      dm.createBigDecimalColumn("B",poljeB,2)
    });
    
    if (!rpcskl.getCSKL().equals("")){ //TODO
      workset.getColumn("CSKL").setVisible(0);
      navVisCols = new int[] {0,1,2};
      try {
        this.getJPTV().removeTableModifier(rtm);
      } catch (Exception e) {
        e.printStackTrace(); 
      }
    } else {
      navVisCols = new int[] {0,1,2,3};
      this.getJPTV().addTableModifier(rtm);
    }
    workset.getColumn("CART").setVisible(0);
    workset.getColumn("CART1").setVisible(0);
    workset.getColumn("BC").setVisible(0);
    workset.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);
    
    workset.open();
    
    tmpSet.first();
    
    do {
      workset.insertRow(false);
      workset.setString("CSKL",tmpSet.getString("CSKL"));
      workset.setInt("CART",tmpSet.getInt("CART"));
      workset.setString("CART1",tmpSet.getString("CART1"));
      workset.setString("BC",tmpSet.getString("BC"));
//      workset.setString("NAZART",tmpSet.getString("NAZART"));
//      workset.setString("JM",tmpSet.getString("JM"));
      try {
        workset.setBigDecimal("A",tmpSet.getBigDecimal("A"));
      } catch (Exception exc) {
        workset.setBigDecimal("A",new BigDecimal(tmpSet.getDouble("A")));
      }
      try {
        workset.setBigDecimal("B",tmpSet.getBigDecimal("B"));
      } catch (Exception exc) {
        workset.setBigDecimal("B",new BigDecimal(tmpSet.getDouble("B")));
      }
    } while (tmpSet.next());
    
    if (workset.rowCount() == 0) setNoDataAndReturnImmediately();

    workset.first();
    setDataSet(workset);
  };

  //*** handlanje pritiska na tipku F10 ili klika na OK button
  public boolean Validacija() {
//    if (rpcskl.getCSKL().equals("")) {
//      rpcskl.setCSKL("");
//      JOptionPane.showConfirmDialog(this.getWindow(), "Obavezan unos skladišta !",
//        "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      return false;
//    }
    return true;
  }

   // handlanje pritiska na tipku ESC
  public void firstESC() {
    if(this.getJPTV().getDataSet() != null || isInterrupted()) {
      this.getJPTV().clearDataSet();
      removeNav();
      //rcc.EnabDisabAll(jPanel3, true);
      jcbStanje.setEnabled(true);
      jcbArtikli.setEnabled(true);
//      rcc.EnabDisabAll(rpcskl, false);
      //showDefaultValues();
      jcbStanje.requestFocus();
      
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
    }
  }

  public boolean runFirstESC() {
//    return this.getJPTV().getDataSet() != null; // !rpcskl.getCSKL().equals("");
    if (this.getJPTV().getDataSet() != null) return true;
    if (!rpcskl.getCSKL().equals("")) return true;
    return false;
  }
  
  private raTableColumnModifier rtm = null;

  //*** init metoda
  private void jbInit() throws Exception {
    fieldSet.setColumns(new Column[] { 
      dm.createStringColumn("STANJE",30), 
      dm.createStringColumn("ARTIKLI",30)}
    );
    fieldSet.open();
  
    comboPanel.setLayout(xYLayout1);
//    this.addReport("hr.restart.robno.repPregledKol", "Ispis pregeda signalnih i minimalnih koli\u010Dina", 5);
//    this.addReport( "hr.restart.robno.repPregledKol", "hr.restart.robno.repPregledKol", "PregledKol", "Ispis pregeda signalnih i minimalnih koli\u010Dina");
    rpcskl.setRaMode('S');
    setJPan(mainPanel);
    mainPanel.setLayout(borderLayout1);
    mainPanel.setMinimumSize(new Dimension(555, 43));
    mainPanel.setPreferredSize(new Dimension(650, 90));

    jlKolZal.setText("Koli\u010Dina zalihe");
    jlVrsZal.setText("Vrsta koli\u010Dine");
    xYLayout1.setWidth(650);
    xYLayout1.setHeight(30);
    mainPanel.add(rpcskl, BorderLayout.NORTH);
    mainPanel.add(comboPanel, BorderLayout.CENTER);
    comboPanel.add(jlKolZal,      new XYConstraints(15, 7, -1, -1));
    comboPanel.add(jcbArtikli,             new XYConstraints(434, 7, 170, -1));
    comboPanel.add(jcbStanje,       new XYConstraints(150, 7, 170, -1));
    comboPanel.add(jlVrsZal,       new XYConstraints(334, 7, -1, -1));

    comboPanel.setMinimumSize(new Dimension(555, 30));
    comboPanel.setPreferredSize(new Dimension(650, 30));
    
    Column colST = dm.createStringColumn("ST",2);
    Column colAR = dm.createStringColumn("AR",2);
    Column colSK = dm.createStringColumn("SK",2);
    Column colNSK = dm.createStringColumn("NSK",2);

    jcbStanje.setRaDataSet(fieldSet);
    jcbStanje.setRaColumn("STANJE");
    jcbStanje.setRaItems(new String[][] {
        {"Trenutna","stanje.kol"},
        {"Trenutna - rezervirana","stanje.kol - stanje.kolrez"}}
    );

    jcbArtikli.setRaDataSet(fieldSet);
    jcbArtikli.setRaColumn("ARTIKLI");
    jcbArtikli.setRaItems(new String[][] {
        {"Minimalna","artikli.sigkol"},
        {"Signalna","artikli.minkol"}}
    );
    
    this.getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier("CART", new String[] {
		"CART", "NAZART" }, new String[] { "CART" }, dm.getArtikli()) {
		public int getMaxModifiedTextLength() {
			return 22;
		}
	});
    
    rtm = new hr.restart.swing.raTableColumnModifier(
        "CSKL", 
        new String[]{"CSKL", "NAZSKL"}, 
        new String[]{"CSKL"}, 
        dm.getSklad()) {
      public int getMaxModifiedTextLength() {
        return 20;
      }
    };
    
// this.getJPTV().addTableModifier(new
// hr.restart.swing.raTableColumnModifier("CSKL", new String[] {
//		"CSKL", "NAZSKL" }, new String[] { "CSKL" }, dm.getSklad()) {
//		public int getMaxModifiedTextLength() {
//			return 20;
//		}
//	});
  }

  //*** Defaultne vrijednosti dialoga
  void showDefaultValues() {
    setDataSet(null);
    this.jcbStanje.setSelectedIndex(0);
    this.jcbArtikli.setSelectedIndex(0);
    fieldSet.setString("STANJE","stanje.kol");
    fieldSet.setString("ARTIKLI","artikli.sigkol");
    rcc.EnabDisabAll(comboPanel, true);
    this.jcbStanje.requestFocus();
  }

  //***************************************************************************

  public DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  
  public int getStanje(){
    return jcbStanje.getSelectedIndex();
  }
  
  public int getArtikli(){
    return jcbArtikli.getSelectedIndex();
  }
  
  public String getCskl(){
    return rpcskl.getCSKL();
  }

  public void componentShow() {
    showDefaultValues();
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

   if(cskl_corg) {
     rpcskl.setCSKL(raUser.getInstance().getDefSklad());
     rpcskl.setDisab('S');
    }
    
    if(!cskl_corg) rpcskl.jrfCSKL.requestFocus();
  }
  
  
  
  
  
  public String navDoubleClickActionName() {
    return "";
  }
  public int[] navVisibleColumns() {
    return navVisCols; // new int[] {0,1,2,3};
  }
}