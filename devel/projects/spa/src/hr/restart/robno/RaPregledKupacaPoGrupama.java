/****license*****************************************************************
**   file: RaPregledKupacaPoGrupama.java
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

import hr.restart.baza.Gruppart;
import hr.restart.baza.Partneri;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class RaPregledKupacaPoGrupama extends raUpitFat {
  
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private Valid vl = Valid.getValid();
  private TableDataSet tds = new TableDataSet();
  JPanel jp = new JPanel();
  XYLayout myXyLayout = new XYLayout();

  protected raComboBox rcmbGrupPart = new raComboBox();
  
  public RaPregledKupacaPoGrupama(){
    try {
      initialize();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private QueryDataSet stariPrometi;

  public void componentShow() {
    this.getJPTV().getNavBar().remove(rnvPJ);
    try{
    removeNav();
    } catch (Exception e) {
    }
  }

  raNavAction rnvPJ = new raNavAction("Aparati u posjedu",raImages.IMGHELP,KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent ev) {
      System.out.println("Kliknuto"); //XDEBUG delete when no more needed
      //TODO
      try {
      JFrame appan = new RaRekapAparati(getJPTV().getDataSet().getInt("CPAR"));
      appan.pack();
      appan.setBounds(200,200,550,200);
      appan.show();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  };
  
  private void initialize() throws Exception {
    QueryDataSet grupa = Gruppart.getDataModule().getQueryDataSet();
    grupa.open();
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(grupa);
    
    String [][] grArry = new String[grupa.rowCount()][2];
    
    tds.setColumns(new Column[] {dm.createTimestampColumn("pocDatum"), 
        dm.createTimestampColumn("zavDatum"), 
        dm.createStringColumn("GRUPPART",11)});
    tds.open();
    
    for (grupa.first();grupa.inBounds();grupa.next()){
      grArry[grupa.getRow()][0] = grupa.getString("NAZIV");
      grArry[grupa.getRow()][1] = grupa.getString("CGRPAR");
    }
    
    jp.setLayout(myXyLayout);
    
    myXyLayout.setWidth(640);
    myXyLayout.setHeight(45);
    
    rcmbGrupPart.setRaDataSet(tds);
    rcmbGrupPart.setRaColumn("GRUPPART");
    rcmbGrupPart.setRaItems(grArry);

    jp.add(new JLabel("Grupa partnera"),  new XYConstraints(15, 12, 100, -1));
    jp.add(rcmbGrupPart,  new XYConstraints(150, 10, 355, -1));
    
    rcmbGrupPart.setSelectedIndex(0);
    System.out.println("rcmbGrupPart.getDataValue() - " + rcmbGrupPart.getDataValue()); //XDEBUG delete when no more needed
    tds.setString("GRUPPART",rcmbGrupPart.getDataValue());
    
    this.setJPan(jp);
    
    this.getJPTV().getNavBar().addOption(rnvPJ,0);
  }
  
  HashMap partIzGrup = null;
  
  int[] viscols;
  
  public void okPress() {
    stariPrometi = ut.getNewQueryDataSet("SELECT * FROM VTCparHist");
    if (!dblclcked) {
      dblclckname = "Promet";
      viscols = new int[] {0,1,2};
      partIzGrup = new HashMap();
      QueryDataSet filtPart = Partneri.getDataModule().getFilteredDataSet("CGRPAR = '"+tds.getString("GRUPPART")+"'");
      filtPart.open();
      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
      st.prn(filtPart.getQuery().toString());
      for (filtPart.first();filtPart.inBounds();filtPart.next()){
        partIzGrup.put(new Integer(filtPart.getInt("CPAR")),"grupa");
      }
      filtPart = null;
      querySet1().first();
      setDataSet(querySet1());
    } else {
      dblclckname = "Dokument";
      viscols = new int[] {0,1,2,3,4};
      querySet2().first();
      setDataSet(querySet2());
    }
  }

  public void firstESC() {
    // TODO Auto-generated method stub
    if (dblclcked){
     dblclcked = false;
     ok_action();
    } else {
    setDataSet(null);
    this.getJPTV().getNavBar().remove(rnvPJ);
    removeNav();
    raCommonClass.getraCommonClass().EnabDisabAll(this.getJPan(),true);
    rcmbGrupPart.requestFocusLater();
    }
  }

  public boolean runFirstESC() {
    if (dblclcked) return true;
    // TODO nadopisati
    return this.getJPTV().getDataSet() != null;
  }

  public int[] navVisibleColumns() {
    // TODO Popravit
    return viscols;
  }
  
  String dblclckname;
  
  public String navDoubleClickActionName() {
    // TODO Auto-generated method stub
    return dblclckname;
  }

  boolean dblclcked = false;
  
  public void jptv_doubleClick() {
    if (!dblclcked){
     dblclcked = true; 
     ok_action();
    } else {
      if (this.getJPTV().getDataSet().getInt("BRDOK") == 0) return;
      
      String brdok = this.getJPTV().getDataSet().getInt("BRDOK")+"";
      String cskl = this.getJPTV().getDataSet().getString("CSKL");
      String vrdok = this.getJPTV().getDataSet().getString("VRDOK");
      String god = this.getJPTV().getDataSet().getString("GOD");
      
      System.out.println(cskl+", "+vrdok+", "+god+", "+brdok);
      
      raMasterDetail.showRecord("hr.restart.robno.ra"+vrdok, 
          new String[]{"CSKL", "VRDOK", "GOD", "BRDOK"}, 
          new String[]{cskl, vrdok, god, brdok}
      );
    }
  }
  
  private QueryDataSet querySet1(){
    QueryDataSet tmp = ut.getNewQueryDataSet("SELECT doki.cpar, sum (stdoki.iprodbp) as promet " +
        "FROM doki,stdoki " +
        "WHERE doki.cskl = stdoki.cskl "+
        "AND doki.vrdok = stdoki.vrdok "+
        "AND doki.god = stdoki.god "+
        "AND doki.brdok = stdoki.brdok and doki.vrdok in ('ROT','RAC','POD') " +
        "AND doki.cskl = '1' "+
//        "AND doki.datdok between '"+ ut.getFirstSecondOfDay(tds.getTimestamp("DATUMOD"))+"' and '"+ut.getLastSecondOfDay(tds.getTimestamp("DATUMDO"))+"' "+
        "group by doki.cpar order by doki.cpar"
    );
    
    if (tmp.rowCount() < 1) setNoDataAndReturnImmediately();

    QueryDataSet prikaz = new QueryDataSet();
    prikaz.setColumns(new Column[] {
        (Column) dm.getPartneri().getColumn("CPAR").clone(),
        (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
        dm.createBigDecimalColumn("PROMET", "Promet", 2)
    });
    prikaz.open();
    
    
    for (tmp.first();tmp.inBounds();tmp.next()){
      if (!partIzGrup.containsKey(new Integer(tmp.getInt("CPAR")))) continue;
      
      prikaz.insertRow(false);
      prikaz.setInt("CPAR",tmp.getInt("CPAR"));
      lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",tmp.getInt("CPAR")+"");
      prikaz.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
      prikaz.setBigDecimal("PROMET",tmp.getBigDecimal("PROMET"));
      
      if (lookupData.getlookupData().raLocate(stariPrometi,"CPAR",tmp.getInt("CPAR")+"")){
        prikaz.setBigDecimal("PROMET",prikaz.getBigDecimal("PROMET").add(stariPrometi.getBigDecimal("UKUPROM")));
        stariPrometi.deleteRow();
      }
    }
    
    if (prikaz.rowCount() < 1) setNoDataAndReturnImmediately();
    
    return prikaz;
  }
  
  private QueryDataSet querySet2(){
    QueryDataSet tmp = ut.getNewQueryDataSet("SELECT max(doki.cpar) as cpar, doki.cskl, doki.vrdok, doki.god, doki.brdok, sum (stdoki.iprodbp) as promet, max(doki.datdok) as datdok " +
        "FROM doki,stdoki " +
        "WHERE doki.cskl = stdoki.cskl "+
        "AND doki.vrdok = stdoki.vrdok "+
        "AND doki.god = stdoki.god "+
        "AND doki.brdok = stdoki.brdok and doki.vrdok in ('ROT','RAC','POD') " +
        "AND doki.cskl = '1' "+
        "AND doki.cpar = " + this.getJPTV().getDataSet().getInt("CPAR")+" "+
        "group by  doki.cskl, doki.god, doki.vrdok, doki.brdok order by doki.datdok"
    );
    
    System.out.println("Dclick cpar = "+this.getJPTV().getDataSet().getInt("CPAR")); //XDEBUG delete when no more needed
    
    QueryDataSet prikaz = new QueryDataSet();
    prikaz.setColumns(new Column[] {
//        (Column) dm.getPartneri().getColumn("NAZPAR").clone(),
        (Column) dm.getDoki().getColumn("CSKL").cloneColumn(),
        (Column) dm.getDoki().getColumn("GOD").cloneColumn(),
        (Column) dm.getDoki().getColumn("BRDOK").cloneColumn(),
        (Column) dm.getDoki().getColumn("VRDOK").cloneColumn(),
        dm.createBigDecimalColumn("PROMET", "Promet", 2)
    });
    prikaz.open();
    
    for (tmp.first();tmp.inBounds();tmp.next()){
      
      if (lookupData.getlookupData().raLocate(stariPrometi,"CPAR",tmp.getInt("CPAR")+"")){
        prikaz.insertRow(false);
        prikaz.setInt("BRDOK",0);
        prikaz.setString("GOD", "XXXX");
        prikaz.setString("CSKL", "1");
        prikaz.setString("VRDOK", "OLD");
        prikaz.setBigDecimal("PROMET",stariPrometi.getBigDecimal("UKUPROM"));
        stariPrometi.deleteRow();
      }
      
      prikaz.insertRow(false);
      prikaz.setString("CSKL",tmp.getString("CSKL"));
      prikaz.setString("GOD",tmp.getString("GOD"));
      prikaz.setString("VRDOK",tmp.getString("VRDOK"));
      prikaz.setInt("BRDOK",tmp.getInt("BRDOK"));
      prikaz.setBigDecimal("PROMET",tmp.getBigDecimal("PROMET"));
    }
    return prikaz;
  }
  
}
