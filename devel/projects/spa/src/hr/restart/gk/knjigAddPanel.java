/****license*****************************************************************
**   file: knjigAddPanel.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.raComboBox;
import hr.restart.util.sysoutTEST;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSetView;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class knjigAddPanel extends JPanel {

  private sysoutTEST ST = new sysoutTEST(false);
  private XYLayout xYLayout1 = new XYLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.raCommonClass cc = hr.restart.util.raCommonClass.getraCommonClass();
  private JPanel jPanel1 = new JPanel();
  private XYLayout xYLayout2 = new XYLayout();
  private Border border1;
  private JLabel jlCSKL = new JLabel("Skladište");
  private JraTextField jraBrnal = new JraTextField();
  private JlrNavField jlrCSKL = new JlrNavField();
  private JlrNavField jlrNAZIV = new JlrNavField();
  private JraButton jbGetCskl = new JraButton();
  private JLabel jlCORG = new JLabel("Org. jedinica");
  private JlrNavField jlrCORG = new JlrNavField();
  private JlrNavField jlrNAZORG = new JlrNavField();
  private JraButton jbGetCorg = new JraButton();
  private JraScrollPane jSP = new JraScrollPane();
  private hr.restart.robno.TypeDoc TD = hr.restart.robno.TypeDoc.getTypeDoc();
  private java.util.ArrayList AulazniVrdok = new java.util.ArrayList();
  private java.util.ArrayList AizlazniVrdok = new java.util.ArrayList();
  private java.util.ArrayList AfinancVrdok = new java.util.ArrayList();
  private java.util.ArrayList BfinancVrdok = new java.util.ArrayList();  
  private java.util.ArrayList AmesklaVrdokUl = new java.util.ArrayList();
  private java.util.ArrayList AmesklaVrdokIZ = new java.util.ArrayList();
  private java.util.ArrayList Askladista = new java.util.ArrayList();
  private java.util.ArrayList Acorgovi = new java.util.ArrayList();
  private int statusUI = -1;



  private JlrNavField jlrDok = new JlrNavField() {
      public void after_lookUp() {
        dokChanged();
      }
    };
  private JlrNavField jlrNazDok = new JlrNavField() {
      public void after_lookUp() {
        dokChanged();
      }
  };

  raComboBox rcbULIZ = new raComboBox() {
    public void this_itemStateChanged() {
      rcbULIZ_itemStateChanged();
    }
  };

  private QueryDataSet qdsvi = new QueryDataSet();

  private JraButton jbDok = new JraButton();
  private JraTextField jrtfOdstupanje = new  JraTextField();
  private Column codstupanje = new Column();
  private QueryDataSet dummy = new QueryDataSet();
  boolean isUlaz = true;
  boolean isIzlaz = true;
  boolean isMeskla = true;
  String dodajUlaz = "";
  String dodajUlazOJ = "";  
  String dodajIzlaz = "";
  String dodajMeskla = "";
  {
    codstupanje = dm.getStdoki().getColumn("VC").cloneColumn();
    codstupanje.setColumnName("ODSTUPANJE");
    codstupanje.setCaption("Dozvoljeno odstupanje duguje / potrazuje");
    dummy.setColumns(new Column[] {codstupanje});
  }

  public knjigAddPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void componentShow(){
    enableCSKL(true);
    emptyCSKL();
    enableCORG(true);
    emptyCORG();
    jlrDok.setText("");
    jlrDok.emptyTextFields();
    statusUI = -1;
    rcbULIZ.setSelectedIndex(rcbULIZ.getSelectedIndex());
    rcbULIZ_itemStateChanged();
    try {
    	dummy.setBigDecimal("ODSTUPANJE",new java.math.BigDecimal(
    			frmParam.getParam("robno","knjOdst","0.00","Dozvoljeno odstupanje po temeljnici")));
    } catch (Exception es){
    	dummy.setBigDecimal("ODSTUPANJE",new java.math.BigDecimal("0.00"));
    }
  }

  void jbInit() throws Exception {
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        componentShow();
      }
    });

    rcbULIZ.setRaColumn("ULIZ");
    rcbULIZ.setRaItems(new String[][] {
      {"Svi", "S"},
      {"Ulazni", "U"},
      {"Izlazni", "I"}
    });

    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setColNames(new String[] {"NAZDOK"});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok});
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setSearchMode(0);
    hr.restart.baza.Vrdokum.getDataModule().setFilter(qdsvi, "app in ('robno','rac','mp') and knjiz='D'");
    jlrDok.setRaDataSet(qdsvi);
    jlrDok.setNavButton(jbDok);

    jlrNazDok.setNavProperties(jlrDok);
    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setSearchMode(1);
    jrtfOdstupanje.setColumnName("ODSTUPANJE");
    jrtfOdstupanje.setDataSet(dummy);

    this.setLayout(xYLayout1);

    jPanel1.setLayout(xYLayout2);
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jlrCSKL.setColumnName("CSKL");
    jlrCSKL.setColNames(new String[] {"NAZSKL"});
    jlrCSKL.setVisCols(new int[]{0,1,2});
    jlrCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIV});
    jlrCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrNAZIV.setColumnName("NAZSKL");
    jlrNAZIV.setSearchMode(1);
    jlrNAZIV.setNavProperties(jlrCSKL);
    jbGetCskl.setText("...");
    jlrCSKL.setNavButton(jbGetCskl);

    jlrCORG.setColumnName("CORG");
    jlrCORG.setColNames(new String[] {"NAZIV"});
    jlrCORG.setVisCols(new int[]{0,1,2});
    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZORG});
    jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrNAZORG.setColumnName("NAZIV");
    jlrNAZORG.setSearchMode(1);
    jlrNAZORG.setNavProperties(jlrCORG);
    jbGetCorg.setText("...");
    jlrCORG.setNavButton(jbGetCorg);
    xYLayout1.setHeight(100);
    add(new JLabel("Dokumenti "), new XYConstraints(15, 0, 100, -1));
    add(rcbULIZ, new XYConstraints(150, 0, 100, -1));
    add(jlrDok, new XYConstraints(255, 0, 50, -1));
    add(jlrNazDok, new XYConstraints(310, 0, 210, -1));
    add(jbDok, new XYConstraints(525, 0, 21, 21));

    add(jlCORG, new XYConstraints(15, 25, -1, -1));
    add(jlrCORG, new XYConstraints(150,25,  100, -1));
    add(jlrNAZORG, new XYConstraints(255,25,  265, -1));
    add(jbGetCorg, new XYConstraints(525,25,  21, 21));

    add(jlCSKL, new XYConstraints(15, 50, -1, -1));
    add(jlrCSKL, new XYConstraints(150,50,  100, -1));
    add(jlrNAZIV, new XYConstraints(255,50,  265, -1));
    add(jbGetCskl, new XYConstraints(525,50,  21, 21));
    add(new JLabel("Odstupanje ") , new XYConstraints(15 ,75,  100, -1));
    add(jrtfOdstupanje ,  new XYConstraints(150, 75, 100, -1));
    if (raUser.getInstance().isRoot()) {
      add(new JLabel("Nalog ") , new XYConstraints(360 ,75,  100, -1));
      add(jraBrnal, new XYConstraints(420, 75, 100, -1));
    }
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldknjig, String newknjig) {
        jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        jlrNAZORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  private void clearAllArrayList(){
    AulazniVrdok.clear();
    AizlazniVrdok.clear();
    AfinancVrdok.clear();
    BfinancVrdok.clear();
    AmesklaVrdokUl.clear();
    AmesklaVrdokIZ.clear();
    Askladista.clear();
    Acorgovi.clear();
  }

  private void ALadd(String vrdok) {
  	
    if (TD.isDocMeskla(vrdok)) {
      if (vrdok.equalsIgnoreCase("MES") ||
          vrdok.equalsIgnoreCase("MEU")) {
        AmesklaVrdokUl.add(vrdok);
      }
      if (vrdok.equalsIgnoreCase("MES") ||
          vrdok.equalsIgnoreCase("MEI")) {
        AmesklaVrdokIZ.add(vrdok);
      }
    }
    else if (TD.isDocUlaz(vrdok) && !TD.isDocOJ(vrdok)) {
      AulazniVrdok.add(vrdok);
    }
    else if (TD.isDocSklad(vrdok)) {
      AizlazniVrdok.add(vrdok);
    }
    else if (TD.isDocOJ(vrdok) && !TD.isDocUlaz(vrdok)) {
      AfinancVrdok.add(vrdok);
    }
    else if (TD.isDocOJ(vrdok) && TD.isDocUlaz(vrdok)) {
      BfinancVrdok.add(vrdok);
    }    
  }

  public ArrayList getArrayListVrdok(){
  	ArrayList al = new ArrayList();
  	String dodatak="";
    if (statusUI==1) {
        dodatak = "and vrsdok in ('U','UI')";
    } else if (rcbULIZ.getSelectedIndex()==2) {
        dodatak = "and vrsdok in('I','UI')";
      }  	
  	if (jlrDok.getText().equalsIgnoreCase("")) {    // aako je za sve dokumente
  		String sqlupit = "select vrdok from vrdokum where app in "+
		"('robno','mp','rac','rn') and knjiz='D' "+ dodatak;
System.out.println(sqlupit);  		
  		QueryDataSet qds = Util.getNewQueryDataSet(sqlupit,true);
  		
        for (qds.first();qds.inBounds();qds.next()) {
          al.add(qds.getString("VRDOK"));
        }
      } else {
      	al.add(jlrDok.getText());
      }
  	return al;
  }
  
  public ArrayList getArrayListSklad(){
  	String knjig = hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG(true);
  	ArrayList al = new ArrayList();
  	if (jlrCSKL.getText().equalsIgnoreCase("")) {    // aako je za sve dokumente
  		String sqlupit = "select cskl from sklad where tipskl!='V' and knjig='"+knjig+"'";
  		QueryDataSet qds = Util.getNewQueryDataSet(sqlupit,true);
//System.out.println(sqlupit);  		
        for (qds.first();qds.inBounds();qds.next()) {
          al.add(qds.getString("CSKL"));
        }
  	} else {
  		al.add(jlrCSKL.getText());
  	}
  	return al;  	
  }
  
  public ArrayList getArrayListOrgstr(){
  	
  	ArrayList al = new ArrayList();
  	if (jlrCORG.getText().equalsIgnoreCase("")) {
  		StorageDataSet dswa  = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
  		jlrCORG.getRaDataSet().open();
        DataSetView  dsw = dswa.cloneDataSetView();
        for (dsw.first();dsw.inBounds();dsw.next()) {
          al.add(dsw.getString("CORG"));
        }
        dsw.removeRowFilterListener(null);
        dsw.close();
      } else {
      	al.add(jlrCORG.getText());
      }
  	return al;  	
  	
  
  
  
  }
  
  
  
  
  private void prepareArrayList() {
    clearAllArrayList();
// raspodjela VRDOK u ARRAYLISTU
    if (jlrDok.getText().equalsIgnoreCase("")) {    // aako je za sve dokumente
      jlrDok.getRaDataSet().open();
      DataSetView  dsw = jlrDok.getRaDataSet().cloneDataSetView();
      for (dsw.first();dsw.inBounds();dsw.next()) {
        ALadd(dsw.getString("VRDOK"));
      }
      dsw.removeRowFilterListener(null);
      dsw.close();
    } else {
      ALadd(jlrDok.getText());
    }
// raspodjela skladista
    if (jlrCSKL.getText().equalsIgnoreCase("")) {
      QueryDataSet dsw = hr.restart.robno.Util.getSkladFromCorg();
      for (dsw.first();dsw.inBounds();dsw.next()) {
        Askladista.add(dsw.getString("CSKL"));
      }
    }
    else {
      Askladista.add(jlrCSKL.getText());
    }
// raspodjela orgjedinica
    if (jlrCORG.getText().equalsIgnoreCase("")) {
      StorageDataSet dsw  = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
      for (dsw.first();dsw.inBounds();dsw.next()) {
        Acorgovi.add(dsw.getString("CORG"));
      }
    }
    else {
      Acorgovi.add(jlrCORG.getText());
    }
  }

  private hr.restart.util.VarStr UzagradiAL(java.util.ArrayList al) {
    if (al.isEmpty()) return new hr.restart.util.VarStr("");
    hr.restart.util.VarStr tmpVS = new hr.restart.util.VarStr("(");
    for (int i = 0;i<al.size();i++) {
      tmpVS.append("'").append((String) al.get(i)).append("',");
    }
    tmpVS.chop();
    tmpVS.append(")");
    return tmpVS;
  }

  private void printaj(String naziv,java.util.ArrayList al) {
    System.out.println(naziv+" ---- Po\u010Detak");
    for (int i = 0;i<al.size();i++) {
      System.out.println((String) al.get(i));
    }
    System.out.println(naziv+" ---- Kraj");
  }

  private void prepareSqls() {
    hr.restart.util.VarStr queryUL = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryULskl = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryULOJ = new hr.restart.util.VarStr();    
    hr.restart.util.VarStr queryIZ = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryIZps = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryIZpf = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryMES = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryMEST1 = new hr.restart.util.VarStr();
    hr.restart.util.VarStr queryMEST2 = new hr.restart.util.VarStr();

    isUlaz = !AulazniVrdok.isEmpty()  || !BfinancVrdok.isEmpty();;
    isIzlaz =  !AizlazniVrdok.isEmpty() || !AfinancVrdok.isEmpty();
    isMeskla = !AmesklaVrdokUl.isEmpty() || !AmesklaVrdokIZ.isEmpty();


    if (isUlaz) {
    	
      if (!AulazniVrdok.isEmpty()) {	
      	queryULskl.append(" and cskl in ").append(UzagradiAL(Askladista));
      	queryULskl.append(" and vrdok in ").append(UzagradiAL(AulazniVrdok));
      } else {
      	queryULskl.append(" and 1=0");
      }
      if (!BfinancVrdok.isEmpty()) {
      	queryULOJ.append(" and cskl in ").append(UzagradiAL(Acorgovi));
      	queryULOJ.append(" and vrdok in ").append(UzagradiAL(BfinancVrdok));
      } else {
      	queryULOJ.append(" and 1=0");
      }
System.out.println(queryULskl);      
System.out.println(queryULOJ);      
    }
    if (isMeskla) {
      if (!AmesklaVrdokIZ.isEmpty()) {
        queryMEST1.append("cskliz in ").append(UzagradiAL(Askladista));
        queryMEST1.append(" and vrdok in ").append(UzagradiAL(AmesklaVrdokIZ));
      }
      if (!AmesklaVrdokUl.isEmpty()) {
        queryMEST2.append("csklul in ").append(UzagradiAL(Askladista));
        queryMEST2.append(" and vrdok in ").append(UzagradiAL(AmesklaVrdokUl));
      }
      if (queryMEST1.length()!=0 && queryMEST2.length()==0) {
        queryMES = queryMES.append(" and ").append(queryMEST1);
      } else if (queryMEST1.length()==0 && queryMEST2.length()!=0) {
        queryMES = queryMES.append(" and ").append(queryMEST2);
      } else if (queryMEST1.length()!=0 && queryMEST2.length()!=0) {
        queryMES = queryMES.append(" and ((").append(queryMEST1).append(") or (").append(queryMEST2).append("))");
      }
    }


    if (isIzlaz) {
//WHERE rbsid=5 and ((cskl in ('1') and vrdok in ('OTP')) or (cskl in ('2') and vrdok in ('ROT')))
      if (!AizlazniVrdok.isEmpty()) {
        queryIZps.append("cskl in ").append(UzagradiAL(Askladista));
        queryIZps.append(" and vrdok in ").append(UzagradiAL(AizlazniVrdok));
      }

      if (!AfinancVrdok.isEmpty()) {
        queryIZpf.append(" cskl in ").append(UzagradiAL(Acorgovi));
        queryIZpf.append(" and vrdok in ").append(UzagradiAL(AfinancVrdok));
      }

      if (!AizlazniVrdok.isEmpty() && !AfinancVrdok.isEmpty()) {
        queryIZ.append(" and ((").append(queryIZps).append(") or (").append(queryIZpf).append("))");
      }
      else if (!AizlazniVrdok.isEmpty() && AfinancVrdok.isEmpty()) {
        queryIZ.append(" and ").append(queryIZps);
      }
      else if (AizlazniVrdok.isEmpty() && !AfinancVrdok.isEmpty()) {
        queryIZ.append(" and ").append(queryIZpf);
      }

    }
    dodajUlaz = queryULskl.toString();
    dodajUlazOJ = queryULOJ.toString();
    
    dodajIzlaz = queryIZ.toString();
    dodajMeskla = queryMES.toString();
    System.out.println(dodajUlaz);
    System.out.println(dodajUlazOJ);

  }

  public void setupString() {
    prepareArrayList();
    prepareSqls();
  
//    System.out.println("setupString() isUlaz "+isUlaz);
//    System.out.println("setupString() isIzlaz "+isIzlaz);
//    System.out.println("setupString() isMeskla "+isMeskla);

  }
  public String getUlazIzlazSvi(){
  	
  	System.out.println(rcbULIZ.getDataValue());
  	return rcbULIZ.getDataValue();
  
  }
  

  public java.math.BigDecimal getOdstupanje(){
    return dummy.getBigDecimal("ODSTUPANJE");
  }
  
  public String getBrnal() {
    if (!raUser.getInstance().isRoot()) return "";
    return jraBrnal.getText();
  }

  public void setBrnal(String brnal) {
    if (!raUser.getInstance().isRoot()) return;
    jraBrnal.setText(brnal);
  }
  
  private void enableCSKL(boolean isEnabled) {
    cc.setLabelLaF(jlrCSKL,isEnabled);
    cc.setLabelLaF(jlrNAZIV,isEnabled);
    cc.setLabelLaF(jbGetCskl,isEnabled);
  }

  private void emptyCSKL() {
    jlrCSKL.setText("");
    jlrCSKL.emptyTextFields();
  }

  private void enableCORG(boolean isEnabled) {
    cc.setLabelLaF(jlrCORG,isEnabled);
    cc.setLabelLaF(jlrNAZORG,isEnabled);
    cc.setLabelLaF(jbGetCorg,isEnabled);
  }

  private void emptyCORG() {
    jlrCORG.setText("");
    jlrCORG.emptyTextFields();
  }

  private void dokChanged() {
    if (jlrDok.getText().equalsIgnoreCase("")) return ;
    if (TD.isDocSklad(jlrDok.getText().trim())) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jlrCSKL.requestFocus();
        }
      });
      enableCORG(false);
      emptyCORG();
      enableCSKL(true);
    }else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jlrCORG.requestFocus();
        }
      });
      enableCORG(true);
      emptyCSKL();
      enableCSKL(false);
    }
  }

  private void rcbULIZ_itemStateChanged() {
    jlrDok.setText("");
    jlrDok.emptyTextFields();
    enableCORG(true);
    emptyCORG();
    emptyCSKL();
    enableCSKL(true);
    
    statusUI = rcbULIZ.getSelectedIndex(); 
    
    if (rcbULIZ.getSelectedIndex()==0) {
      hr.restart.baza.Vrdokum.getDataModule().setFilter(qdsvi, "app in ('robno','rac','mp') and knjiz='D'");
      jlrDok.setRaDataSet(qdsvi);
    } else if (rcbULIZ.getSelectedIndex()==1) {
      QueryDataSet qdsul = new QueryDataSet();
      hr.restart.baza.Vrdokum.getDataModule().setFilter(qdsul, "app in ('robno','rac','mp') and knjiz='D' and (vrsdok='U' or vrsdok='UI')");
      jlrDok.setRaDataSet(qdsul);
    } else if (rcbULIZ.getSelectedIndex()==2) {
      QueryDataSet qdsiz = new QueryDataSet();
      hr.restart.baza.Vrdokum.getDataModule().setFilter(qdsiz, "app in ('robno','rac','mp') and knjiz='D' and (vrsdok='I' or vrsdok='UI')");
      jlrDok.setRaDataSet(qdsiz);
    }
  }
}