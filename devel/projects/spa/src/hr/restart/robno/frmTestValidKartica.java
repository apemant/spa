/****license*****************************************************************
**   file: frmTestValidKartica.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.Stdoku;
import hr.restart.baza.Stmeskla;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.raProcess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmTestValidKartica extends hr.restart.util.raUpit {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private JraTextField godina = new JraTextField();
  private checkKartica ck = new checkKartica();
  private JraButton popravi = new JraButton();
  private JraCheckBox sysdat = new JraCheckBox();
  private JraCheckBox fixmes = new JraCheckBox(); 

  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      godina.requestFocusLater();
    }
  };

  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setCskl(rpcskl.getCSKL());
      rpcart.setDefParam();
      rpcart.setCART();
    }
  };
  private JPanel jp = new JPanel();

  public frmTestValidKartica() {
    try {
//      tVK.initKartica();
//      tVK.clear();

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
//    this.addReport("hr.restart.robno.repKartica", "Ispis kartice artikla", 5);
//    this.addReport("hr.restart.robno.repKartica2Reda", "Ispis kartice artikla - 2 reda", 5);

    popravi.setText("Popravak");
    popravi.setPreferredSize(new Dimension(69, 27));
    popravi.setAutomaticFocusLost(false);
    popravi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        popraviMe();
      }
    });
    sysdat.setText(" Poredak po sistemskom datumu ");
    fixmes.setText(" Popravi ulaze na meðuskladišnicama ");
    
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(popravi,false);
    this.setJPan(jp);
    this.getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    godina.setHorizontalAlignment(SwingConstants.CENTER);
    rpcskl.setRaMode('S');

    rpcart.setMode("DOH");
    rpcart.setBorder(null);
    
    jp.setLayout(new XYLayout());
    jp.setPreferredSize(new Dimension(650, 165));
    jp.add(rpcskl, new XYConstraints(0,0,-1,-1));
    jp.add(rpcart, new XYConstraints(0,40,-1,75));
    jp.add(new JLabel("Godina "),new XYConstraints(15,115,-1,-1));
    jp.add(godina,new XYConstraints(150,115,100,-1));
    jp.add(sysdat,new XYConstraints(260,115,-1,-1));
    jp.add(fixmes,new XYConstraints(260,140,-1,-1));
    jp.add(popravi,new XYConstraints(515,115,90,22));
  }

  public void popraviMe(){
  	
  	
//    new Thread() {
//        public void run() {
//            raProcess.setMessage("Popravak u tijeku",true);
            ck.saveAllChanges();
            hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(popravi,false);
            fel();
            
//        }
//      }.start();
  	
//
      if (fixmes.isSelected() && ck.mcskl.size() > 0) {
         ArrayList map = new ArrayList(ck.mcskl.entrySet());
         Collections.sort(map, new Comparator() {
           public int compare(Object o1, Object o2) {
             return ((Timestamp) ((Map.Entry) o1).getValue()).compareTo((Timestamp) ((Map.Entry) o2).getValue());
           }
         });
         ArrayList skl = new ArrayList();
         for (int i = 0; i < map.size(); i++) skl.add(((Map.Entry) map.get(i)).getKey());
           
         VarStr mes = new VarStr();
         mes.append("\nPromijenjeni ulazi na skladištima: " + VarStr.join(new ArrayList(skl), ", "));
         JOptionPane.showMessageDialog(this.getWindow(), "Promijenjeni ulazi na skladištima: " + VarStr.join(new ArrayList(skl), ", "), 
             "Popravak kartica", JOptionPane.INFORMATION_MESSAGE);
      }

  }

  public void componentShow(){
    this.getJPTV().setDataSet(null);
//    rpcskl.Clear();
    /*rcc.EnabDisabAll(rpcskl, true);
    rpcart.EnabDisab(true);
    rcc.setLabelLaF(godina, true);*/
    rcc.setLabelLaF(popravi, false);
    godina.setText(hr.restart.util.Valid.getValid().findYear());
    rpcskl.jrfCSKL.requestFocus();
  }
  public void firstESC(){
//    this.rcc.setLabelLaF(rpcskl,true);
//    this.rcc.setLabelLaF(rpcart,true);
    rcc.setLabelLaF(popravi, false);
    if (rpcart.getCART().trim().length() == 0 && !svi) {
      rcc.EnabDisabAll(rpcskl,true);
      rcc.setLabelLaF(sysdat, true);
      rcc.setLabelLaF(fixmes, true);
      rpcskl.setCSKL("");
      rpcskl.defFocus();
    } else {      
      this.getJPTV().clearDataSet();
      rcc.setLabelLaF(sysdat, true);
      rcc.setLabelLaF(fixmes, true);
      rpcart.EnabDisab(true);
      rpcart.setCART();
      this.rcc.setLabelLaF(godina,true);
    }
    svi = false;
  }
  public boolean runFirstESC(){

    return rpcskl.getCSKL().trim().length() != 0; 
  }

  int artCount, obrCount;
  Condition[] partition;
  int fixCards, fixRows, fixCells;
  void preparePartition() {
    String cskl = rpcskl.jrfCSKL.getText();
    String god = godina.getText();
    
    raProcess.setMessage("Provjera opsega podataka u bazi ...", true);
    ck.setMainParams(cskl, god);
    
    int stav = stdoki.getDataModule().getRowCount(ck.cCskl.and(ck.cGod)) +
                Stdoku.getDataModule().getRowCount(ck.cCskl.and(ck.cGod)) +
                Stmeskla.getDataModule().getRowCount(ck.cGod.and(ck.cMei.or(ck.cMeu)));
    
    System.err.println("TOTAL ROWS: "+stav);
    raProcess.setMessage("Dohvat i particioniranje artikala...", false);
    DataSet arts = Stanje.getDataModule().getTempSet("CART", ck.cCskl.and(ck.cGod) + "ORDER BY cart");
    raProcess.openScratchDataSet(arts);
    artCount = arts.rowCount();
    if (artCount == 0 || stav == 0) setNoDataAndReturnImmediately();    
    
    String srows = frmParam.getParam("robno", "maxKartRows", "10000", 
        "Broj stavaka koji se dohvaca odjednom kod popravka kartica");
    int maxRows = Aus.getNumber(srows);
    if (maxRows <= 0 || stav / maxRows > 100) {
      maxRows = stav / 10;
      System.err.println("Pogresan parametar maxKarRows! auto-reset na "+maxRows);
    }
    int numSegments = stav / maxRows + 1, lastRow = 0;    
    if (numSegments > artCount) numSegments = artCount;
    partition = new Condition[numSegments];
    if (numSegments == 1) partition[0] = Condition.none;
    else for (int i = 1; i <= numSegments; i++) {
      arts.goToRow(lastRow);
      int cart = arts.getInt("CART");
      int endRow = i * artCount / numSegments - 1;
      if (endRow == lastRow) partition[i-1] = Condition.equal("CART", cart);
      else {
        arts.goToRow(endRow);
        partition[i-1] = Condition.between("CART", cart, arts.getInt("CART"));
      }
      lastRow = endRow + 1;
    }    
  }
  
  void displayErrors() {
    ck.getErrors().show(this.getWindow());
  }

  void reportFatalErrors() {
    String[] opts = {"OK", "Detalji"};
    int reply = JOptionPane.showOptionDialog(this.getWindow(), 
        "Došlo je do fatalne greške. Nemoguæ popravak.", "Provjera kartica", 0, 
        JOptionPane.ERROR_MESSAGE, null, opts, opts[0]);
    if (reply == 1) ck.getFatalErrors().show(this.getWindow());
  }
  
  boolean promptForSaveChanges(int seg) {
    anyError = true;
    obrCount += ck.totalCards;
    String[] opts = {"Popravi", "Preskoèi", "Prikaži", "Prekini"};
    if (seg == partition.length)
      opts = new String[] {"Popravi", "Prikaži", "Prekini"};
    int reply;
    while (true) {
      VarStr mes = new VarStr();
      mes.append(Aus.getNumDep(obrCount, "Provjerena ", "Provjerene ", "Provjereno "));
      mes.append(ck.totalCards);
      if (partition.length > 1) mes.append('(').append(obrCount).append(')');
      mes.append(" od ");
      mes.append(Aus.getNum(artCount, "kartica", "kartice", "kartica"));
      if (partition.length > 1) {
        mes.append(" (").append(seg).append(" od ");
        mes.append(Aus.getNum(partition.length, "dijela)", "dijela)", "dijelova)"));
      }
      if (ck.erroneousCells == 1) mes.append(".\nPronaðena nepravilnost");
      else mes.append(".\nPronaðene nepravilnosti");
      mes.append(partition.length > 1 ? " u ovom dijelu" : "").append(":\n-  ");
      mes.append(Aus.getNum(ck.erroneousCards, "neispravna kartica", 
          "neispravne kartice", "neispravnih kartica")).append("\n-  ");
      mes.append(Aus.getNum(ck.erroneousRows, "neispravna stavka", 
          "neispravne stavke", "neispravnih stavki"));
      mes.append(ck.erroneousCards == 1 ? " na kartici\n-  " : " na karticama\n-  ");
      mes.append(Aus.getNum(ck.erroneousCells, "neispravan iznos", 
          "neispravna iznosa", "neispravnih iznosa"));
      mes.append(ck.erroneousRows == 1 ? " na stavci" : " na stavkama");
      mes.append("\n\nŽelite li popraviti sve pronaðene greške?\n");
      if (seg < partition.length) mes.chop(2).append(" ili preskoèiti ovaj dio?\n");
      raMultiLineMessage ml = new raMultiLineMessage(mes.toString(), SwingConstants.LEADING);
      reply = JOptionPane.showOptionDialog(raProcess.getDialog(), ml, "Provjera kartica", 0, 
          JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
      if (reply == opts.length - 2) displayErrors();
      else break;
    }
    if (reply == 0) {
      setMessage("Snimanje popravaka ...");
      ck.saveAllChanges();
      fixCards += ck.erroneousCards;
      fixRows += ck.erroneousRows;
      fixCells += ck.erroneousCells;
    }
    return (reply != opts.length - 1 && reply >= 0);
  }
  
  boolean anyError;
  public void sviArtikli(){
    anyError = false;
    obrCount = fixCards = fixRows = fixCells = 0;
    preparePartition();
    for (int i = 0; i < partition.length; i++)
      System.out.println(partition[i]);
    ck.prepareDataCache();
    for (int i = 0; i < partition.length; i++) {      
      ck.checkMultiple(partition[i], true);
      if (ck.getFatalErrors().countErrors() > 0) break;
      if (ck.erroneousCards > 0)
        if (!promptForSaveChanges(i + 1))
          break;
    }
    
  	/*ck.prepareDataCache();
  	ck.checkMultiple(Condition.none);*/
//  	if (!raProcess.isRunning()) stanja.open();
//  	else raProcess.openDataSet(stanja);
  }
  
  public void afterOKPress() {
    boolean fatal = ck.getFatalErrors().countErrors() > 0;
    if (raProcess.isFailed()) {
      System.err.println("error");
    } 
    if (raProcess.isCompleted() && svi && !fatal) {
      System.err.println("erroneous cards "+ck.erroneousCards);
      System.err.println("erroneous rows "+ck.erroneousRows);
      System.err.println("erroneous cells "+ck.erroneousCells);
      if (!anyError) {
        JOptionPane.showMessageDialog(this.getWindow(), "Nije pronaðena nijedna greška.",
            "Popravak kartica", JOptionPane.INFORMATION_MESSAGE);
        fel();
        return;
      }
      if (fixCards == 0) {
        JOptionPane.showMessageDialog(this.getWindow(), "Popravak prekinut bez promjena.",
            "Popravak kartica", JOptionPane.WARNING_MESSAGE);
        fel();
        return;
      }
      VarStr mes = new VarStr();
      mes.append("Ukupno popravljeno:\n-  ");      
      mes.append(Aus.getNum(fixCards, "neispravna kartica", 
          "neispravne kartice", "neispravnih kartica")).append("\n-  ");
      mes.append(Aus.getNum(fixRows, "neispravna stavka", 
          "neispravne stavke", "neispravnih stavki"));
      mes.append(fixCards == 1 ? " na kartici\n-  " : " na karticama\n-  ");
      mes.append(Aus.getNum(fixCells, "neispravan iznos", 
          "neispravna iznosa", "neispravnih iznosa"));
      mes.append(fixRows == 1 ? " na stavci\n" : " na stavkama\n");
      if (fixmes.isSelected() && ck.mcskl.size() > 0) {
        ArrayList map = new ArrayList(ck.mcskl.entrySet());
        Collections.sort(map, new Comparator() {
          public int compare(Object o1, Object o2) {
            return ((Timestamp) ((Map.Entry) o1).getValue()).compareTo((Timestamp) ((Map.Entry) o2).getValue());
          }
        });
        ArrayList skl = new ArrayList();
        for (int i = 0; i < map.size(); i++) skl.add(((Map.Entry) map.get(i)).getKey());
        
        mes.append("\nPromijenjeni ulazi na skladištima: " + VarStr.join(new ArrayList(skl), ", "));
      }
      raMultiLineMessage ml = new raMultiLineMessage(mes.toString(), SwingConstants.LEADING);
      JOptionPane.showMessageDialog(this.getWindow(), ml, "Popravak kartica", 
          JOptionPane.INFORMATION_MESSAGE);
    }
    if (fatal) reportFatalErrors();
    if (!raProcess.isCompleted() || svi || fatal) fel();
  }
  
  void fel() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        firstESC();
      }
    });
  }
  
  public void okPress() {
    ck.setSysdat(sysdat.isSelected());
    ck.setFixMes(fixmes.isSelected());

    if (svi) {
      sviArtikli();
      return;
    }
    ck.setMainParams(rpcskl.jrfCSKL.getText(), godina.getText());
    ck.checkMultiple(Condition.equal("CART", Integer.parseInt(rpcart.jrfCART.getText())), true);
  	
/*    ck.checkKartica(rpcskl.jrfCSKL.getText(),godina.getText(),Integer.parseInt(rpcart.jrfCART.getText()));*/

    if (ck.getFatalErrors().countErrors() > 0) return;

    if (ck.erroneousCells > 0){
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(popravi,true);
    }

    this.getJPTV().addTableColorModifier();
    this.getJPTV().addTableModifier(new MyColorModifier());

    this.getJPTV().setDataSet(ck.getKartica());

//    this.getJPTV().

  }

  boolean svi;  
  public boolean Validacija() {
    svi = false;    
  	if (rpcskl.jrfCSKL.getText().equalsIgnoreCase("")){
  		JOptionPane
		.showMessageDialog(
				this.getWindow(),
				"Niste unijeli skladište !",
				"Upozorenje", JOptionPane.WARNING_MESSAGE);
  		return false;
  	}
  	
    if (!ValGod()) {
      return false;
    }
    if (rpcart.jrfCART.getText().equalsIgnoreCase("")){      
      if(JOptionPane.showConfirmDialog(
        this.getWindow(),
        "Niste unijeli artikl. Pokrecem kontrolu kartice za cijelo skladište. U redu?", "Potvrda",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) svi = true;
      else return false;
    }
    return true;
  }

  public boolean ValGod() {
   int pero = 0;
   try {
      pero = Integer.parseInt(godina.getText());
   }
   catch (Exception e) {
    return false ;
   }
   if (pero < 1900 || pero >3900) return false;
   return true ;
  }

  class MyColorModifier extends raTableModifier {
    // A.K.A. BoredTotalModifier :)
    Column vcol = new Column();
    Variant v = new Variant();
    Font bf = null, of = null;
    public boolean doModify() {

      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 &&
            tab.getDataSet().hasColumn("DOBAR") != null) {
          tab.getDataSet().getVariant("DOBAR",this.getRow(),v);
          return (v.getString().equalsIgnoreCase("N"));
        }
      }
      return false;
    }

    public void modify() {
          renderComponent.setBackground(Color.red);
    }
  }
}