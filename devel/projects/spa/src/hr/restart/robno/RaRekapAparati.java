/****license*****************************************************************
**   file: RaRekapAparati.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Util;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.sysoutTEST;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;

public class RaRekapAparati extends JFrame {
  
  Util ut = Util.getUtil();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  
  private raJPTableView tableView = new raJPTableView(true) {
    public void mpTable_killFocus(java.util.EventObject e) {
      super.mpTable_killFocus(e);
    }
    public void mpTable_doubleClicked() {
    }
  };

  private raNavAction rnvIspis = new raNavAction("Ispis", raImages.IMGPRINT, java.awt.event.KeyEvent.VK_F5){
    public void actionPerformed(java.awt.event.ActionEvent e){
      ispis();
    }
  };
  
  int cpar;
  
  public RaRekapAparati(int cpar){
    this.setTitle("Aparati u posjedu");
    System.out.println("Ushlo " + cpar); //XDEBUG delete when no more needed
    this.cpar = cpar;
    initAndRest();
  }
  
  private void initAndRest(){
    GridBagLayout gb = new GridBagLayout();
    this.getContentPane().setLayout(gb);
    tableView.setBorder(BorderFactory.createEtchedBorder());
    tableView.setPreferredSize(new Dimension(400,200));
    GridBagConstraints gc = new GridBagConstraints();
    
    gc.fill = gc.BOTH;
    gc.weighty = 1.0;
    gc.weightx = 1.0;
    gb.setConstraints(tableView,gc);
    this.getContentPane().add(tableView);
    
    tableView.setDataSet(tableSet());
    this.addKeyListener(new KeyListener(){
      public void keyTyped(KeyEvent e) {
      }
      public void keyPressed(KeyEvent e) {
      }
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ESCAPE){
          ubij();
        }
      }
    });
    tableView.getNavBar().addOption(rnvIspis, 0);
  }
  
  private void ubij(){
    this.setVisible(false);
    this.dispose();
  }
  
  
  private QueryDataSet tableSet(){
    HashMap aparati = new HashMap();
    HashMap popisAparata = new HashMap();
    
    String grFilter = frmParam.getFrmParam().getParam("robno","grAparati","","Grupe artikala - aparati i srodno");
    
    StringTokenizer stAp = new StringTokenizer(grFilter,",");
    
    String apFilter = "";
    
    int tokenz1 = stAp.countTokens();
    
    System.out.println("stAp.countTokens() = "+tokenz1); //XDEBUG delete when no more needed
    for (int i=0;i<tokenz1;i++){
      System.out.println("i = " + i); //XDEBUG delete when no more needed
      apFilter += "'"+stAp.nextToken().trim()+"'";
      System.out.println("apFilter0 - "+ apFilter); //XDEBUG delete when no more needed
      if (i < (tokenz1-1)) apFilter += ",";
      System.out.println("apFilter1 - "+ apFilter); //XDEBUG delete when no more needed
    }
    
    System.out.println("apFilter - "+ apFilter); //XDEBUG delete when no more needed
    
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    QueryDataSet aparatiIzGrupa = ut.getNewQueryDataSet("select cart, nazart from artikli where cgrart in ("+apFilter+")");
    
    for (aparatiIzGrupa.first();aparatiIzGrupa.inBounds();aparatiIzGrupa.next()){
      popisAparata.put(new Integer(aparatiIzGrupa.getInt("CART")),aparatiIzGrupa.getString("NAZART"));
    }

    st.prn(aparatiIzGrupa);
    QueryDataSet povijest = ut.getNewQueryDataSet("SELECT cpar, ukuprom, apupos FROM VTCparHist WHERE CPAR = "+cpar);

    String apup = povijest.getString("APUPOS");

    st.prn(povijest);
    
    
    QueryDataSet prometi = ut.getNewQueryDataSet(getPrometQueryUpit());
    
    QueryDataSet zaPovrat = new QueryDataSet();
    zaPovrat.setColumns(new Column[] {
        dm.createBigDecimalColumn("KOL","Kolièina",0),
        dm.createStringColumn("JM","JM",10),
        dm.createStringColumn("NAZART","Aparati",500)
    });
    zaPovrat.open();
    
    if (apup != null || apup.trim().length() > 0) {
      StringTokenizer stoka = new StringTokenizer(apup,",");
      int tokenz2 = stoka.countTokens();
      for (int i=0;i<tokenz2;i++){
        String pld = stoka.nextToken().trim();
        if (popisAparata.containsKey(new Integer(pld.substring(pld.indexOf("x")+1)))) {
        zaPovrat.insertRow(false);
        zaPovrat.setBigDecimal("KOL",new BigDecimal(pld.substring(0,pld.indexOf("x"))));
        zaPovrat.setString("JM","kom");
        zaPovrat.setString("NAZART",popisAparata.get(new Integer(pld.substring(pld.indexOf("x")+1))).toString());
        
        System.out.println("kol  "+pld.substring(0,pld.indexOf("x"))); //XDEBUG delete when no more needed
        System.out.println("apa  "+(pld.substring(pld.indexOf("x")+1))); //XDEBUG delete when no more needed
      }
      }
    }
    
    for (prometi.first();prometi.inBounds();prometi.next()){
      if (popisAparata.containsKey(new Integer(prometi.getInt("CART")))){
        zaPovrat.insertRow(false);
        zaPovrat.setBigDecimal("KOL",prometi.getBigDecimal("KOL"));
        zaPovrat.setString("JM","kom");
        zaPovrat.setString("NAZART",popisAparata.get(new Integer(prometi.getInt("CART"))).toString());
      }
    }
    
    return zaPovrat;
  }

  protected String getPrometQueryUpit() {
    String upit = "SELECT doki.cpar, stdoki.cart, stdoki.kol " +
            "FROM doki, stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok " +
            "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND doki.vrdok in ('ROT','RAC','POD') " +
            "and doki.cskl = '1' and doki.cpar = "+cpar;
    
    System.out.println("\n"+upit+"\n"); //XDEBUG delete when no more needed
    return upit;
  }
  
  hr.restart.util.reports.JTablePrintRun jtpr = new hr.restart.util.reports.JTablePrintRun();

  public hr.restart.util.reports.raRunReport getRepRunner(){
    jtpr.setInterTitle(getClass().getName());
    jtpr.setColB(tableView.getColumnsBean());
    jtpr.setRTitle(this.getTitle());
    return jtpr.getReportRunner();
  }

  private String[] provider = new String[0];
  private String[] source = new String[0];
  private String[] design = new String[0];
  private String[] rTitle = new String[0];
  private int[] dataSrcIdx = new int[0];

  public void ispis(){
    tableView.enableEvents(false);
    this.getRepRunner().clearAllCustomReports();
    if (provider == null) return;
    for (int i=0;i<provider.length;i++) {
      if (source[i]==null) {
        getRepRunner().addReport(provider[i],rTitle[i],dataSrcIdx[i]);
      } else if (design[i]==null) {
        getRepRunner().addReport(provider[i], source[i], rTitle[i]);
      } else {
        getRepRunner().addReport(provider[i], source[i], design[i], rTitle[i]);
      }
    }
    jtpr.runIt();
    tableView.enableEvents(true);
  }

}
















