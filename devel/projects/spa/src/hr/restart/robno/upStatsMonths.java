/****license*****************************************************************
**   file: upStatsMonths.java
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


import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class upStatsMonths extends raPanStats {
  private String[] moonshine = new String[] {"Sijeèanj","Veljaèa","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"};
  QueryDataSet monthSet, monthSetCache, monthSetGraph;
  static upStatsMonths instanceOfMe;
  BigDecimal medjuProstor;
  protected boolean doubleClicked, isIspis;

  //graph button
//  private JButton jbChart = null;

  public upStatsMonths() {
    init();
    instanceOfMe = this;
//    System.out.println("Initialized.....");
  }

  public static upStatsMonths getInstance(){
    if (instanceOfMe == null) instanceOfMe = new upStatsMonths();
    return instanceOfMe;
  }


  protected void showDefaultValues() {
    super.showDefaultValues();
    doubleClicked = false;
    fieldSet.setString("SLJED","CPAR");
    getJPTV().clearDataSet();
  }

  public void firstESC(){
//  	jbChart.setVisible(false);

    if (doubleClicked){
      doubleClicked = false;
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonths",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonths",
                     "bla bla bla");

      jpKup.setCpar("");

      changeIcon(1);

      monthSet = monthSetCache;

      if(fieldSet.getString("SLJED").equals("CPAR")) {
          monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}));
                    
          
      }
      else monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

      setDataSetAndSums(monthSet, new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"});
    } else super.firstESC();
  }



  /*private void init() {
//    this.addReport("hr.restart.robno.repStatsMonths",
//                   "hr.restart.robno.repStatsMonths",
//                   "StatsMonths",
//                   "bla bla bla");

    getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier(
        "CPAR", new String[] {"CPAR", "NAZPAR"}
        , dm.getPartneri()));
  }*/


  private void init(){
//  Test Grafikoni

    // FFU
    /*
    jbChart = new JButton("Grafikon");
    jbChart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        if (monthSet == null) return;
        getJPTV().enableEvents(false);

        try {
			ChartZ.showChartDialog(monthSet, "Grafikon po mjesecima", null);
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}

        getJPTV().enableEvents(true);
      }
    });
    getOKPanel().add(jbChart, BorderLayout.WEST);
    jbChart.setVisible(false);
*/

    this.addReport("hr.restart.robno.repStatsMonths",
                   "hr.restart.robno.repStatsMonths",
                   "StatsMonths",
                   "bla bla bla");
    

    getJPTV().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CPAR", new String[]{"CPAR", "NAZPAR"}
        , dm.getPartneri())
    );

  rcmbSljed.setRaItems(new String[][]{
                       {"Šifra kupca", "CPAR"},
                       {"Ukupni iznos", "UKUPNO"}
  });

//    jlSljed.setText("Kolona za ispis");
//    rcmbSljed.setRaItems(new String[][] {
//      /*{"Iznos nabave","INAB"},
//      {"Razlika u cijeni","RUC"},
//      {"Iznos Poreza","POR"},*/
//      {"Ukupan promet","IPRODSP"}
//    });

    jp.remove(jlDatum1);
    jp.remove(jtfPocDatum);
    jp.remove(jtfZavDatum);
    jp.add(jlSljed, new com.borland.jbcl.layout.XYConstraints(15, 167, -1, -1));
    jp.add(rcmbSljed,
           new com.borland.jbcl.layout.XYConstraints(150, 165, 205, 20));

    xYLayout3.setHeight(275);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.
        zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnj, String newKnj) {
        showDefaultValues();
      }
    });
  }

  public boolean Validacija(){
    if (jlrCorg.getText().equals("")) {
      jlrCorg.requestFocus();
      javax.swing.JOptionPane.showMessageDialog(this.getWindow(),"Obvezatan unos - ORG. JEDINICA !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (rpcskl.getCSKL().equals("")) {
      rpcskl.jrfCSKL.requestFocus();
      javax.swing.JOptionPane.showMessageDialog(this.getWindow(),"Obvezatan unos - SKLADIŠTE !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!hr.restart.util.Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals("")) && !doubleClicked){
      int grupe = javax.swing.JOptionPane.showConfirmDialog(this.getWindow(),"Ukljuèiti i podgrupe?","Grupe artikala",javax.swing.JOptionPane.YES_NO_OPTION);
      if (grupe == javax.swing.JOptionPane.CANCEL_OPTION) return false;
      if (grupe == javax.swing.JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }
  
  public String navDoubleClickActionName() {
    return "";
  }
  public int[] navVisibleColumns() {
    return new int[] {0,1,2,3,4,5};
  }


  public void okPress(){

  	// to be changed
//  	jbChart.setVisible(true);

    String selectString = getUpit();

    QueryDataSet tmpSet = ut.getNewQueryDataSet(selectString);

    if(tmpSet.isEmpty())setNoDataAndReturnImmediately();

    monthSet = new QueryDataSet();

    monthSet.setColumns(new Column[]{
                        dm.createIntColumn("CART", "Šifra"),
                        dm.createStringColumn("CART1", "Oznaka", 20),
                        dm.createStringColumn("BC", "Barcode", 20),
                        dm.createStringColumn("NAZART", "Naziv artikla", 50),
                        dm.createIntColumn("CPAR", "Kupac"),
                        dm.createBigDecimalColumn("01", moonshine[0], 2),
                        dm.createBigDecimalColumn("02", moonshine[1], 2),
                        dm.createBigDecimalColumn("03", moonshine[2], 2),
                        dm.createBigDecimalColumn("04", moonshine[3], 2),
                        dm.createBigDecimalColumn("05", moonshine[4], 2),
                        dm.createBigDecimalColumn("06", moonshine[5], 2),
                        dm.createBigDecimalColumn("07", moonshine[6], 2),
                        dm.createBigDecimalColumn("08", moonshine[7], 2),
                        dm.createBigDecimalColumn("09", moonshine[8], 2),
                        dm.createBigDecimalColumn("10", moonshine[9], 2),
                        dm.createBigDecimalColumn("11", moonshine[10], 2),
                        dm.createBigDecimalColumn("12", moonshine[11], 2),
                        dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2)
    });

    monthSetGraph = new QueryDataSet();

    monthSetGraph.setColumns(new Column[]{
                        dm.createIntColumn("CPAR", "Kupac"),
                        dm.createStringColumn("NAZPAR", "Naziv partnera", 50),
                        dm.createStringColumn("MJESEC", "Mjesec", 2),
                        dm.createBigDecimalColumn("IZNOS", "Iznos", 2),
                        dm.createBigDecimalColumn("UKUPNO", "Ukupno", 2)
    });

    monthSet.open();
    monthSetGraph.open();

    tmpSet.first();
    String misec;
    isIspis = true;

    try {
      racunica(tmpSet);
    }
    catch (Exception ex) {
//      ex.printStackTrace();
      setNoDataAndReturnImmediately();
    }

    monthSet.last();
    setDataSetAndSums(monthSet, new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"});
  }

  protected void racunica(QueryDataSet tmpSet) {
    String misec;

    tmpSet.first();


    if (getCkup().equalsIgnoreCase("")) {
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonths",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonths",
                     "bla bla bla");
      do{
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        if(!lookupData.getlookupData().raLocate(monthSet, "CPAR", tmpSet.getInt("CPAR")+"")){
          monthSet.insertRow(false);
          monthSet.setInt("CPAR", tmpSet.getInt("CPAR"));
          monthSet.setBigDecimal(misec, tmpSet.getBigDecimal("IPRODSP"));
          monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("IPRODSP"));
        } else{
          monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal("IPRODSP")));
          monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal("IPRODSP")));
        }



      } while(tmpSet.next());

      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("NAZART").setVisible(0);

      if(fieldSet.getString("SLJED").equals("CPAR")) {
          monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}));
          
          
      }
      else{ 
          monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));
          
          this.addReport("hr.restart.robno.RepStatParMonthsChart", "hr.restart.robno.RepStatParMonthsChart", "Top lista kupaca mjeseèno - grafikon");
      }

    } else {
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonthsArt",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonthsArt",
                     "bla bla bla");

      do {
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        if(!lookupData.getlookupData().raLocate(monthSet, "CART", tmpSet.getInt("CART")+"")){
          monthSet.insertRow(false);
          monthSet.setInt("CART", tmpSet.getInt("CART"));
          monthSet.setString("CART1",tmpSet.getString("CART1"));
          monthSet.setString("BC",tmpSet.getString("BC"));
          monthSet.setString("NAZART", tmpSet.getString("NAZART"));
          monthSet.setBigDecimal(misec, tmpSet.getBigDecimal("IPRODSP"));
          monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("IPRODSP"));
        } else{
          monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal("IPRODSP")));
          monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal("IPRODSP")));
        }
      } while (tmpSet.next());


      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("CPAR").setVisible(0);
      monthSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);

      monthSet.setSort(new SortDescriptor(new String[]{"UKUPNO"}, true, true));
    }

    monthSet.first();
    dm.getPartneri().open();
    String[] mpb = new String[] {
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
        "12"};

    do {
      for (int i = 0; i < mpb.length; i++) {
        monthSetGraph.insertRow(false);
        monthSetGraph.setInt("CPAR", monthSet.getInt("CPAR"));

        if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR",
                                                monthSet.getInt("CPAR") + "")) {
          monthSetGraph.setString("NAZPAR",
                                  dm.getPartneri().getString("NAZPAR"));
        }
        else {
          monthSetGraph.setString("NAZPAR", "");
        }
        monthSetGraph.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO"));

        monthSetGraph.setString("MJESEC", mpb[i]);
        monthSetGraph.setBigDecimal("IZNOS", monthSet.getBigDecimal(mpb[i]));
      }
    } while (monthSet.next());

    monthSetGraph.setSort(new SortDescriptor(new String[] {"UKUPNO","CPAR"}, true, true));


    monthSet.last();
    setDataSetAndSums(monthSet, new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"});
  }

  /** @todo for future use */

  /*private BigDecimal getPostoPromjene(BigDecimal vrijednost){
    System.out.print("mp: "+medjuProstor+" vri: "+vrijednost);
    if (medjuProstor.compareTo(Aus.zero2) == 0 ||
        vrijednost.compareTo(Aus.zero2)==0){
      medjuProstor = vrijednost;
      System.out.println("");
      return null;
    } else {
      BigDecimal pp;
      if (medjuProstor.compareTo(vrijednost) == 0) {
        pp = Aus.zero2;
      } else
      if (medjuProstor.compareTo(vrijednost) < 0) {
        pp = new BigDecimal("100.00").subtract((medjuProstor.multiply(new BigDecimal("100.00"))).divide(vrijednost,2,BigDecimal.ROUND_HALF_UP));
      } else {
        pp = ((vrijednost.multiply(new BigDecimal("100.00"))).divide(medjuProstor, 2,BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal("100.00"));
      }
      medjuProstor = vrijednost;
      System.out.println(" pp: " + pp);
      return pp;
    }
  }*/

  /*private void standardOutput(String selectString){
//    System.out.println("cskl         - " + rpcskl.getCSKL());
//    System.out.println("cart         - " + rpcart.getCART());
//    System.out.println("kupac        - " + getCkup());
//    System.out.println("dobavlajc    - " + getCpar());
//    System.out.println("upit         - \n" + selectString+"\n");
//    System.out.println("knjig godina - " + hr.restart.robno.Aut.getAut().getKnjigodRobno());
  }*/

  /*private void nullifySet(){
    for (int i= 1; i<=12 ;i++ ) {
      monthSet.insertRow(false);
      monthSet.setInt("MONTH",i);
      monthSet.setString("MONTHNAME",moonshine[i-1]);
      monthSet.setBigDecimal(fieldSet.getString("SLJED"),(Aus.zero2));
      monthSet.setBigDecimal("PP",null);
    }
    medjuProstor = Aus.zero2;
  }*/

  private String getUpit(){
    String selStr = "select artikli.cart, artikli.cart1, artikli.bc, artikli.nazart, doki.cpar, doki.datdok, stdoki.kol, stdoki.jm, stdoki.IPRODSP ";

//    if (fieldSet.getString("SLJED").equalsIgnoreCase("RUC")){
//      selStr += "(stdoki.iprodbp-stdoki.inab) as ruc ";
//    } else if (fieldSet.getString("SLJED").equalsIgnoreCase("POR")){
//      selStr += "(stdoki.iprodsp - stdoki.iprodbp) as por ";
//    } else {
//      selStr += "stdoki.IPRODSP ";
//    }


    String inq;
    StorageDataSet corgs = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(fieldSet.getString("CORG"));
    if (corgs.rowCount() == 0) inq = "1=1";
    else if (corgs.rowCount() == 1) inq = "DOKI.CSKL = '" + fieldSet.getString("CORG") + "'";
    else inq = "(DOKI.CSKL in " + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(corgs,"DOKI.CSKL")+") ";
    hr.restart.baza.Condition oj = hr.restart.baza.Condition.in("DOKI.VRDOK", TypeDoc.araj_docsOJ);
    String exInClude = "AND (("+oj+" AND "+inq+") OR ("+oj.not()+" AND DOKI.CSKL = '"+getCskl()+"')) ";

    String artikliFilter;

    if (fieldSet.getString("VRART").equals("") || fieldSet.getString("VRART").equals("X")) artikliFilter = "";
    else artikliFilter = " AND ARTIKLI.VRART='" + fieldSet.getString("VRART") + "' ";


    String carting = "";
    if (!rpcart.findCART(podgrupe).equals("")){
      carting = " AND "+rpcart.findCART(podgrupe);
    }

    String ckupca = "";

    if (!getCkup().equals("")) ckupca = "and doki.cpar='" + getCkup() + "' ";


    selStr += " from doki,stdoki,artikli,partneri WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god " +
        "AND doki.brdok = stdoki.brdok AND stdoki.cart = artikli.cart AND doki.cpar = partneri.cpar and doki.god='" + hr.restart.robno.Aut.getAut().getKnjigodRobno() +
        "' AND doki.vrdok not in ('PON','NDO','NKU','RNL','REV','PRV','OTR','INM','INV','IZD','TRE','ZAH') " +
        exInClude + ckupca + artikliFilter + carting + " order by datdok";

    // REMARK!!
    // nešto što sam primjetio, a moglo bi dobro doæi kad se bude implementirao nabavljaè je da postoji i CPAR kolona u tablici artikli
    // i predstavlja dobavljaèa za taj artikl.

//    System.out.println("\n\n"+selStr+"\n\n");

    return selStr;
  }

  public QueryDataSet getReportSet(){
    return monthSet;
  }

  public QueryDataSet getChartSet() {
    return monthSetGraph;
  }

//  public String getColName(){
//    return fieldSet.getString("SLJED");
//  }

//  public String getColCaption(){
//    return rcmbSljed.getSelectedItem().toString();
//  }


  public boolean isIspis() {
//    System.out.println("\njptv ima dataset - " + (getJPTV().getDataSet()!=null));
//    System.out.println("isIspis            - " + isIspis+"\n");
    return (getJPTV().getDataSet()!=null && isIspis);
  }


  public void jptv_doubleClick(){
//    System.out.print("TRALALA....  ");
    if (getCkup().equalsIgnoreCase("")){
//      System.out.println(this.getJPTV().getDataSet().getInt("CPAR"));
      jpKup.setCpar(this.getJPTV().getDataSet().getInt("CPAR"));
      monthSetCache = getJPTV().getDataSet();
      doubleClicked = true;
      isIspis = false;
      ok_action();
      isIspis = true;
    }
  }

}





/*


 WAS IN RACUNICA


 misec = monthSet.getTimestamp("DATDOK").toString().substring(5, 7);
       if(!lookupData.getlookupData().raLocate(monthSetGraph, new String[] {"CPAR","MJESEC"}, new String[] {monthSet.getInt("CPAR")+"",misec})){
  monthSetGraph.insertRow(false);
  monthSetGraph.setInt("CPAR",monthSet.getInt("CPAR"));
  if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",monthSet.getInt("CPAR")+""))
 monthSetGraph.setString("NAZPAR",dm.getPartneri().getString("NAZPAR"));
  else
    monthSetGraph.setString("NAZPAR","");
  monthSetGraph.setString("MJESEC",misec);
 monthSetGraph.setBigDecimal("IZNOS",monthSet.getBigDecimal("IPRODSP"));
       } else {
  monthSetGraph.setBigDecimal("IZNOS",monthSetGraph.getBigDecimal("IZNOS").add(monthSet.getBigDecimal("IPRODSP")));
       }
 */

