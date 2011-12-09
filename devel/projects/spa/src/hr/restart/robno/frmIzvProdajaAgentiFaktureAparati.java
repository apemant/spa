/****license*****************************************************************
**   file: frmIzvProdajaAgentiFaktureAparati.java
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

import hr.restart.baza.Agenti;
import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.VTCparHist;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmIzvProdajaAgentiFaktureAparati extends raUpitFat {

  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  TableDataSet tds = new TableDataSet();
  QueryDataSet mainData = null;
  
  rapancskl1 rpc = new rapancskl1(350);
  
  HashMap partneriMap = null;
  HashMap agenti = null;
  HashMap generalije = null;
  HashMap dosadasnjiPrometi = null;
  HashMap datumiPrveFakture = null;
  HashMap ovogodisniPrometi = null;
  HashMap rekapApar = null;

  JPanel jp = new JPanel();
  JPanel jPanel3 = new JPanel();
  
  JlrNavField jlrCagent = new JlrNavField();
  JlrNavField jlrNazagent = new JlrNavField();
  JraButton jbAgenti = new JraButton();
  
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazSkl = new JlrNavField();
  JraButton jbSkl = new JraButton();
  
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  raComboBox rcmbFilterBox = new raComboBox();
  BorderLayout localBorderLayout = new BorderLayout();
  XYLayout localXYLayout = new XYLayout();

  JraTextField jtfGrupe = new JraTextField();
//  JraTextField jtfApPosjedu = new JraTextField();
  
  private static frmIzvProdajaAgentiFaktureAparati instanceOfMe = null;
  
  public static frmIzvProdajaAgentiFaktureAparati getInstance(){
    if (instanceOfMe == null) instanceOfMe = new frmIzvProdajaAgentiFaktureAparati();
    return instanceOfMe;
  }
  
  public QueryDataSet getReportSet(){
    return mainData;
  }
  
  public HashMap getGeneralije(){
    return generalije;
  }
  
  public HashMap getRekapAparatz(){
    return rekapApar;
  }
  
  public HashMap getAgente(){
    return agenti;
  }

  public frmIzvProdajaAgentiFaktureAparati() {
    try {
      initialize();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    this.getJPTV().clearDataSet();
    rpc.setCSKL("1");
    tds.setString("CAGENT","");
    jlrCagent.emptyTextFields();
    jlrCagent.requestFocusLater();
    partneriMap = new HashMap();
    QueryDataSet partneriSet = Partneri.getDataModule().getTempSet();
    partneriSet.open();
    
    tds.setString("GRUPE",(frmParam.getFrmParam().getParam("robno","grAparati","","Grupe artikala - aparati i srodno")));
    
    for(partneriSet.first();partneriSet.inBounds();partneriSet.next())
      partneriMap.put(Integer.valueOf(partneriSet.getInt("CPAR")+""),partneriSet.getString("NAZPAR"));
  }
  
  private Column[] columns;
  private String[] sums;
  
  private void makeAparatiSet(){
    String grupeAparata = tds.getString("GRUPE"); //frmParam.getFrmParam().getParam("robno","grAparati","","Grupe artikala - aparati i srodno");
    
    if (grupeAparata.equals("")) setNoDataAndReturnImmediately("Nije definirana grupa (ili grupe) aparata");
    
    VarStr grupe = new VarStr(grupeAparata);
    
    grupe.removeWhitespace();
    
    grupe.replaceAll(",","','");
    grupe.insert(0,"'");
    grupe.append("'");
    
    String gr = grupe.toString();
    
    System.out.println("grupe aparat = " + gr); //XDEBUG delete when no more needed
    
    QueryDataSet aparatiSet = Artikli.getDataModule().getTempSet("CART,NAZART","cgrart in ("+gr+")");
    aparatiSet.open();
    
    HashMap aparatiMap = new HashMap();
    
    for (aparatiSet.first();aparatiSet.inBounds();aparatiSet.next()){
      checkClosing();
      aparatiMap.put(aparatiSet.getInt("CART")+"",aparatiSet.getString("NAZART"));
    }
    makeMainSet(aparatiSet,aparatiMap);
  }
  
  private void makeMainSet(QueryDataSet aparatiSet, HashMap aparatiMap){
    QueryDataSet prometiSet = ut.getNewQueryDataSet(getPrometQueryUpit());
    
    if (prometiSet.rowCount() < 1) setNoDataAndReturnImmediately();
    
    datumiPrveFakture = new HashMap();
    dosadasnjiPrometi = new HashMap();
    
    QueryDataSet dsp = VTCparHist.getDataModule().getTempSet();
    dsp.open();
    for(dsp.first();dsp.inBounds();dsp.next()){
      dosadasnjiPrometi.put(dsp.getInt("CPAR")+"",dsp.getBigDecimal("UKUPROM"));
      datumiPrveFakture.put(dsp.getInt("CPAR")+"",dsp.getTimestamp("DATPRF").getTime()+"");
    }
    dsp.emptyAllRows();
    dsp.close();
    
    columns = new Column[] {
        dm.createIntColumn("CPAR", "Šifra"),
        dm.createStringColumn("CAGENT","Agent",50),
        dm.createStringColumn("NAZPAR","Kupac",150),
        dm.createStringColumn("APARATI","Prodani aparati u razdoblju",1500),
        dm.createBigDecimalColumn("NOVI","Novi",2),
        dm.createBigDecimalColumn("NOVIRUC","Novi RuC",2),
        dm.createBigDecimalColumn("POTROSNI","Potrošni",2),
        dm.createBigDecimalColumn("POTROSNIRUC","Potrošni RuC",2),
        dm.createBigDecimalColumn("UKUPNO","Ukupno",2),
        dm.createBigDecimalColumn("UKUPNORUC","Ukupno RuC",2),
        dm.createBigDecimalColumn("SVEUKUPNO","Dosadašnji promet",2),
        dm.createBigDecimalColumn("PRPOMJ","Prosjeèni mjeseèni promet",2),
        dm.createStringColumn("KUPOD","Kupac od",5)
    };
    
    
    mainData = new QueryDataSet();
    mainData.setColumns(columns);
    mainData.setRowId("CPAR",true);
    //mainData.getColumn("CPAR").setVisible(0);
    mainData.getColumn("CAGENT").setVisible(0);
    mainData.open();
    
    HashMap tempPartneri = new HashMap();
    HashMap tempAgenti = new HashMap();
    int ix = 0;
    for(prometiSet.first();prometiSet.inBounds();prometiSet.next()){
//      System.err.println(++ix + " VRDOK = " + prometiSet.getString("VRDOK") + " CPAR = " + prometiSet.getInt("CPAR")); //XDEBUG delete when no more needed
//      if (prometiSet.getInt("CPAR") == 2851) System.err.println(ix+" START ___ --- vrdok " + prometiSet.getString("VRDOK"));
      checkClosing();
      if (!tempPartneri.containsKey(prometiSet.getInt("CPAR")+"")){
        mainData.insertRow(false);
        mainData.setInt("CPAR",prometiSet.getInt("CPAR"));
        mainData.setString("CAGENT",tds.getString("CAGENT"));
        mainData.setString("NAZPAR",partneriMap.get(Integer.valueOf(prometiSet.getInt("CPAR")+"")).toString());
        mainData.setString("APARATI","Nema aparata");
        
        QueryDataSet aaaa = ut.getNewQueryDataSet("SELECT min (doki.datdok) as datdok, sum (stdoki.iprodbp) as iprodbp FROM doki, stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND DOKI.DATDOK <= '"+ut.getLastSecondOfDay(tds.getTimestamp("DO"))+"'  AND doki.vrdok in ('ROT','RAC','POD') AND  doki.cpar = "+prometiSet.getInt("CPAR") +"and doki.cskl in ('1', '01')"); //doki.god = '"+vl.findYear(tds.getTimestamp("OD"))+"'
        BigDecimal sveukupno = aaaa.getBigDecimal("IPRODBP");
//        System.out.println("cpar "+prometiSet.getInt("CPAR")+" sveukupno iz prometa   = " + sveukupno); //XDEBUG delete when no more needed
        String kupacod = ut.getMonth(aaaa.getTimestamp("DATDOK"))+"/"+vl.findYear(aaaa.getTimestamp("DATDOK")).substring(2,4);
        int diff = ut.getHourDifference(aaaa.getTimestamp("DATDOK"), tds.getTimestamp("DO"));
        
        if (dosadasnjiPrometi.containsKey(prometiSet.getInt("CPAR")+"")){
          sveukupno = sveukupno.add(new BigDecimal(dosadasnjiPrometi.get(prometiSet.getInt("CPAR")+"").toString()));
          kupacod = ut.getMonth(new Timestamp(new Long(datumiPrveFakture.get(prometiSet.getInt("CPAR")+"").toString()).longValue()))+"/"+vl.findYear(new Timestamp(new Long(datumiPrveFakture.get(prometiSet.getInt("CPAR")+"").toString()).longValue())).substring(2,4);
          diff = ut.getHourDifference(new Timestamp(new Long(datumiPrveFakture.get(prometiSet.getInt("CPAR")+"").toString()).longValue()), tds.getTimestamp("DO"));
//          System.out.println("sveukupno prom + dosad = " + sveukupno); //XDEBUG delete when no more needed
        }
        BigDecimal prosjekMj;
        try {
          prosjekMj = sveukupno.divide(new BigDecimal(diff/(24*30)),BigDecimal.ROUND_UP);
        } catch (ArithmeticException e) {
          prosjekMj = sveukupno;  
        }
        
//        System.out.println("diff = " + diff + " to je mjeseci " + (diff/(24*30))); //XDEBUG delete when no more needed
        
        mainData.setBigDecimal("SVEUKUPNO",sveukupno);
        mainData.setBigDecimal("PRPOMJ",prosjekMj);
        mainData.setString("KUPOD",kupacod);
        
        if (prometiSet.getString("CUG").equals("1")) {
          Aus.add(mainData, "NOVI", prometiSet, "IPRODBP");
          Aus.add(mainData, "NOVIRUC", prometiSet, "IPRODBP");
          Aus.sub(mainData, "NOVIRUC", prometiSet, "INAB");
        } else {
          Aus.add(mainData, "POTROSNI", prometiSet, "IPRODBP");
          Aus.add(mainData, "POTROSNIRUC", prometiSet, "IPRODBP");
          Aus.sub(mainData, "POTROSNIRUC", prometiSet, "INAB");
        }

        if (aparatiMap.containsKey(prometiSet.getInt("CART")+"")){
          mainData.setString("APARATI", prometiSet.getBigDecimal("KOL").intValue()+ "|" + aparatiMap.get(prometiSet.getInt("CART")+"").toString());
          System.out.println(prometiSet.getBigDecimal("KOL").intValue()); //XDEBUG delete when no more needed
        }
        Aus.add(mainData, "UKUPNO", prometiSet, "IPRODBP");
        Aus.add(mainData, "UKUPNORUC", prometiSet, "IPRODBP");
        Aus.sub(mainData, "UKUPNORUC", prometiSet, "INAB");
        tempPartneri.put(prometiSet.getInt("CPAR")+"",mainData.getRow()+"");
      } else {
        mainData.goToRow(Integer.parseInt(tempPartneri.get(prometiSet.getInt("CPAR")+"").toString()));
        
        if (prometiSet.getString("CUG").equals("1")) {
          Aus.add(mainData, "NOVI", prometiSet, "IPRODBP");
          Aus.add(mainData, "NOVIRUC", prometiSet, "IPRODBP");
          Aus.sub(mainData, "NOVIRUC", prometiSet, "INAB");
        } else {
          Aus.add(mainData, "POTROSNI", prometiSet, "IPRODBP");
          Aus.add(mainData, "POTROSNIRUC", prometiSet, "IPRODBP");
          Aus.sub(mainData, "POTROSNIRUC", prometiSet, "INAB");
        }
        
        if (aparatiMap.containsKey(prometiSet.getInt("CART")+"")){
          if (mainData.getString("APARATI").equals("Nema aparata"))
            mainData.setString("APARATI", prometiSet.getBigDecimal("KOL").intValue()+ "|" + aparatiMap.get(prometiSet.getInt("CART")+"").toString());
          else 
            mainData.setString("APARATI", mainData.getString("APARATI")+","+ prometiSet.getBigDecimal("KOL").intValue()+ "|" + aparatiMap.get(prometiSet.getInt("CART")+"").toString());
        }
        
        Aus.add(mainData, "UKUPNO", prometiSet, "IPRODBP");
        Aus.add(mainData, "UKUPNORUC", prometiSet, "IPRODBP");
        Aus.sub(mainData, "UKUPNORUC", prometiSet, "INAB");
        mainData.last();
      }
//      if (mainData.getInt("CPAR") == 2851) System.err.println("vrdok " + prometiSet.getString("VRDOK") + " -- " + mainData); //XDEBUG delete when no more needed
    }
    
    rekapApar = new HashMap();
    
    for (mainData.first();mainData.inBounds();mainData.next()){
      if (!mainData.getString("APARATI").equals("Nema aparata")){
        StringTokenizer stoka = new StringTokenizer(mainData.getString("APARATI"),",");
        int countTokenz = stoka.countTokens();
//        System.out.println("A : "+mainData.getString("APARATI")); //XDEBUG delete when no more needed
//        System.out.println("B : "+mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1)); //XDEBUG delete when no more needed
//        System.out.println("C : "+mainData.getString("APARATI").substring(0,mainData.getString("APARATI").indexOf("|"))); //XDEBUG delete when no more needed
        
        if (countTokenz == 1) {
          if (!rekapApar.containsKey(mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1))) {
            rekapApar.put(mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1),mainData.getString("APARATI").substring(0,mainData.getString("APARATI").indexOf("|")));
          } else {
            rekapApar.put(mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1), String.valueOf(Integer.parseInt(rekapApar.get(mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1)).toString())+Integer.parseInt(mainData.getString("APARATI").substring(0,mainData.getString("APARATI").indexOf("|"))))); //String.valueOf(Integer.parseInt(rekapApar.get(mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1)).toString())+mainData.getString("APARATI").substring(0,mainData.getString("APARATI").indexOf("|"))));
          }
          mainData.setString("APARATI",mainData.getString("APARATI").substring(0,mainData.getString("APARATI").indexOf("|"))+" x " + mainData.getString("APARATI").substring(mainData.getString("APARATI").indexOf("|")+1));          continue;
        }
//        System.out.println("START WITH - " + mainData.getString("APARATI")); //XDEBUG delete when no more needed
        String[] aparats = new String[countTokenz];
        for (int i=0;i<countTokenz;i++){
          String pld = stoka.nextToken(); 
          
          if (!rekapApar.containsKey(pld.substring(mainData.getString("APARATI").indexOf("|")+1))) {
            rekapApar.put(pld.substring(pld.indexOf("|")+1),pld.substring(0,pld.indexOf("|")));
          } else {
//            rekapApar.put(pld.substring(pld.indexOf("|")+1), String.valueOf(Integer.parseInt(rekapApar.get(pld).toString())+pld.substring(0,pld.indexOf("|"))));
              rekapApar.put(pld.substring(pld.indexOf("|")+1), String.valueOf(Integer.parseInt(rekapApar.get(pld.substring(pld.indexOf("|")+1)).toString())+Integer.parseInt(pld.substring(0,pld.indexOf("|")))));
              System.out.println("zbroj = " + String.valueOf(Integer.parseInt(rekapApar.get(pld.substring(pld.indexOf("|")+1)).toString())+Integer.parseInt(pld.substring(0,pld.indexOf("|"))))); //XDEBUG delete when no more needed
          }
          
          if (i == 0) {
            aparats[i] =pld.substring(0,pld.indexOf("|")) + " x " + pld.substring(pld.indexOf("|")+1);
          } else {
            aparats[i] =pld.substring(0,pld.indexOf("|")) + " x " + pld.substring(pld.indexOf("|")+1);
            for (int j=0;j<i;j++){
              System.out.println("i = " + i + " j = " + j); //XDEBUG delete when no more needed
              System.out.println("i - " + aparats[i]); //XDEBUG delete when no more needed
              System.out.println("j - " + aparats[j]); //XDEBUG delete when no more needed
              if (!aparats[i].equals("") && !aparats[j].equals("") &&
                  (aparats[i].substring(aparats[i].indexOf("x"),aparats[i].length())).equals(aparats[j].substring(aparats[j].indexOf("x"),aparats[j].length()))){
                //System.out.println("našao 2 ista... '" + aparats[i].substring(0,aparats[i].indexOf("x")-1).trim()+"'"); //XDEBUG delete when no more needed
                int broj = Integer.parseInt(aparats[j].substring(0,aparats[j].indexOf("x")-1).trim()) + Integer.parseInt(pld.substring(0,pld.indexOf("|")));
                aparats[j] = broj != 0 ? broj + " x " + pld.substring(pld.indexOf("|")+1) : "";
                aparats[i] = "";
              }
            }
          }
        }
        String finalStr = "";
        System.out.println("-----START-----"); //XDEBUG delete when no more needed
        for (int i=0;i<countTokenz;i++){
          System.out.println(aparats[i]); //XDEBUG delete when no more needed
          System.out.println("i="+i); //XDEBUG delete when no more needed
          if (aparats[i].equals("")) continue;
          if (finalStr.length() == 0)
            finalStr += aparats[i];
          else 
            finalStr += ", " + aparats[i];
        }
        System.out.println("-----STOP------"); //XDEBUG delete when no more needed
        mainData.setString("APARATI",finalStr);
      }
    }
    
    mainData.setSort(new SortDescriptor(new String[] {"NAZPAR"}));
    prometiSet = null;
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(mainData);
  }

  protected String getPrometQueryUpit() {
    String upit = "SELECT doki.vrdok, doki.cagent, doki.cpar, doki.cug, stdoki.cart, stdoki.kol, stdoki.ineto, stdoki.inab, " +
            "stdoki.iprodbp, stdoki.iprodsp " +
            "FROM doki, stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok " +
            "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND doki.vrdok in ('ROT','RAC','POD') AND " +
            Condition.between("doki.datdok", tds.getTimestamp("OD"),
                tds.getTimestamp("DO")).toString()+" and (doki.cskl = '"+rpc.getCSKL()+"' or doki.cskl='01')";
    
    /*if (tds.getString("FILTER").equals("1")) upit += "AND doki.cug = '1' ";*/
            
    /*if (!tds.getString("CAGENT").equals(""))*/ upit += "AND doki.cagent = '"+tds.getString("CAGENT")+"' ";
    /*else upit += "AND doki.cagent is not null ";*/
    
    System.out.println("\n"+upit+"\n"); //XDEBUG delete when no more needed
    return upit;
  }
  
  private void sastaviHashMape(){
    agenti = new HashMap();
    generalije = new HashMap();
    
    QueryDataSet ags = Agenti.getDataModule().getTempSet();
    ags.open();
    for(ags.first();ags.inBounds();ags.next())
      agenti.put(ags.getInt("CAGENT")+"",ags.getString("NAZAGENT"));
    
    generalije.put("PDAT",tds.getTimestamp("OD"));
    generalije.put("ZDAT",tds.getTimestamp("DO"));
  }

  public void okPress() {
    makeAparatiSet();
    sastaviHashMape();
    mainData.first();
    setDataSetAndSums(mainData,new String[] {"NOVI","NOVIRUC", "POTROSNI","POTROSNIRUC","UKUPNO","UKUPNORUC","SVEUKUPNO"} /*sums*/);
  }

  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return true;
    if (!tds.getString("CAGENT").equals(""))return true;
    if (!rpc.getCSKL().equals(""))return true;
    return false;
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    if (this.getJPTV().getDataSet() != null){
      this.getJPTV().clearDataSet();
      removeNav();
      jlrCagent.requestFocusLater();
      jlrCagent.selectAll();
    } else if (!tds.getString("CAGENT").equals("")) {
      tds.setString("CAGENT","");
      jlrCagent.emptyTextFields();
      jlrCagent.requestFocusLater();
      jlrCagent.selectAll();
    } else if (!rpc.getCSKL().equals("")){
      rpc.setCSKL("");
      rpc.jrfCSKL.emptyTextFields();
      rpc.jrfCSKL.requestFocusLater();
    }
  }

  public String navDoubleClickActionName() {
    return "Ne koristi se";
  }
  
  public int[] navVisibleColumns() {
//    return null;
  return new int[]{0,1,2,3,4,5,6,7};
//    return new int[]{0,1,2,3,4,5,6,7,8};
  }
  
  private void initialize() throws Exception {
    jp.setLayout(localBorderLayout);

    tds.setColumns(new Column[] {
        dm.createStringColumn("CSKL",20),
        dm.createTimestampColumn("OD"),
        dm.createTimestampColumn("DO"),
        dm.createStringColumn("CAGENT","Agent",20),
        dm.createStringColumn("FILTER",3),
        dm.createStringColumn("GRUPE",100)
        }
    );
    tds.open();

    jlrCagent.setColumnName("CAGENT");
    jlrCagent.setDataSet(tds);
    jlrCagent.setColNames(new String[] {"NAZAGENT"});
    jlrCagent.setVisCols(new int[] { 0, 1 });
    jlrCagent.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazagent});
    jlrCagent.setRaDataSet(dm.getAgenti());
    
    jlrNazagent.setColumnName("NAZAGENT");
    jlrNazagent.setSearchMode(1);
    jlrNazagent.setNavProperties(jlrCagent);
    jlrNazagent.setNavButton(jbAgenti);

    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("OD");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("DO");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    
    
    rcmbFilterBox.setRaDataSet(tds);
    rcmbFilterBox.setRaColumn("FILTER");
    rcmbFilterBox.setRaItems(new String[][] {
        {"Svi kupci","0"},
        {"Prvi kupci","1"}
      });
      
    
    jtfGrupe.setDataSet(tds);
    jtfGrupe.setColumnName("GRUPE");

    jPanel3.setLayout(localXYLayout);
    localXYLayout.setWidth(655);
    localXYLayout.setHeight(135);

    jp.add(jPanel3, BorderLayout.CENTER);

    this.setJPan(jp);
    
    jPanel3.add(rpc, new XYConstraints(0,0,-1,-1));

    jPanel3.add(new JLabel("Agent"), new XYConstraints(15, 52, -1, -1));
    jPanel3.add(jlrCagent, new XYConstraints(150, 50, 100, -1));
    jPanel3.add(jlrNazagent, new XYConstraints(255, 50, 350, -1));
    jPanel3.add(jbAgenti, new XYConstraints(610, 50, 21, 21));

    jPanel3.add(new JLabel("Period (od-do)"), new XYConstraints(15, 77, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 75, 100, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(255, 75, 100, -1));
    
    jPanel3.add(new JLabel("Grupe aparata"), new XYConstraints(15, 102, -1, -1));
    jPanel3.add(jtfGrupe, new XYConstraints(150, 100, 205, -1));
    

    tds.setTimestamp("OD", rut.findFirstDayOfYear(Integer.parseInt(hr.restart.util.Valid.getValid().findYear())));
    tds.setTimestamp("DO", hr.restart.util.Valid.getValid().findDate(false, 0));
    
    rcmbFilterBox.setSelectedIndex(0);
    tds.setString("FILTER","0");
    
    this.addReport("hr.restart.robno.repIzvProdajaAgentiFaktureAparati", "hr.restart.robno.repIzvProdajaAgentiFaktureAparatiProvider", "IzvProdajaAgentiFaktureAparati", "Formatirani ispis");
    this.addReport("hr.restart.robno.repIzvProdajaAgentiFaktureAparati", "hr.restart.robno.repIzvProdajaAgentiFaktureAparatiProvider", "IzvProdajaAgentiFaktureAparatiAgent", "Formatirani ispis za agenta");
  }

  public boolean Validacija() {
    if (vl.isEmpty(rpc.jrfCSKL)) return false;
    return !vl.isEmpty(jlrCagent);
  }
}