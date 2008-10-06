/****license*****************************************************************
**   file: frmIzvArt.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Vrart;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmIzvArt extends raUpitLite {

  public static String vrdoks = "";
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jp = new JPanel();
  rapancart rpcart = new rapancart(){
    public void nextTofocus(){
    }
//    public void metToDo_after_lookUp() {
//    }
//    public void findFocusAfter() {
//      jraDatumOd.requestFocus();
//    }
    public boolean isUsluga() {
      return false;
    }
  };

  static double suma;
  static Timestamp datOd, datDo;
  static QueryDataSet qds = new QueryDataSet();
//  static QueryDataSet qds2 = new QueryDataSet();
  static QueryDataSet qds3 = new QueryDataSet();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  static frmIzvArt fIA;
  Valid vl = Valid.getValid();
  hr.restart.robno.Util utRobno = hr.restart.robno.Util.getUtil();
  public static String vrstaDok="";
  public static String vrstaArt="";


  public static frmIzvArt getInstance() {
    if(fIA==null)
      fIA = new frmIzvArt();
    return fIA;
  }

  protected rapancskl1 rpcskl = new rapancskl1(348) {
    public void findFocusAfter() {
      rpcart.setDefParam();
      rpcart.setCART();
//      jraDatumOd.requestFocus();
    }
    public void MYpost_after_lookUp(){
//      jraDatumOd.requestFocus();
    }
  };


//  private raComboBox rcbSljed = new raComboBox();
//  private JLabel jlSljed = new JLabel("Sljed");
//  private JLabel jlIspis = new JLabel("Ispis");
//  private JCheckBox jcbIspisKol = new JCheckBox();
//  private JCheckBox jcbKolNull = new JCheckBox();


  protected XYLayout xYLayout1 = new XYLayout();
//  private TitledBorder ispisTitledBorder;
//  private JPanel jpCB = new JPanel();
//  private XYLayout xYLayout2 = new XYLayout();
  protected JRadioButton jrbDatNap = new JRadioButton();
  protected JraTextField jraDatumOd = new JraTextField();
  protected JraTextField jraDatumDo = new JraTextField();
  protected JLabel jlMinus = new JLabel();
  protected JLabel jlDatum = new JLabel();
  protected Border border1;
  protected TitledBorder titledBorder1;
  TableDataSet tds = new TableDataSet();
  TableDataSet tds2 = new TableDataSet();
  TitledBorder titledBorder2;
  protected boolean OKEsc = false;
  protected raComboBox rcbVrDok = new raComboBox();
  protected raComboBox rcbVrArt = new raComboBox();
  protected JLabel jlVrDok = new JLabel();
  protected JLabel jlVrArt = new JLabel();

  public frmIzvArt() {
    this('M');
  }
  
  public frmIzvArt(char pakidz) {
    if (pakidz == 'M')
      fIA = this;
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
//    rpcart.setBorder(null);
//    rpcart.setMode(new String("DOH"));
//    rpcskl.setDisabAfter(true);
//    rpcart.setAllowUsluga(false);
    qds.setLocale(Aus.hr);
    
    rpcart.setBorder(null);
    rpcart.setMode(new String("DOH"));
    rpcart.setSearchable(false);
    
    
    xYLayout1.setWidth(650);
    xYLayout1.setHeight(180);
//    xYLayout2.setWidth(400);
//    xYLayout2.setHeight(30);
    jp.setLayout(xYLayout1);
//    jpCB.setLayout(xYLayout2);
//    ispisTitledBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,new Color(224, 255, 255),new Color(76, 90, 98),new Color(109, 129, 140)),"");
//    jpCB.setBorder(ispisTitledBorder);

    tds.setColumns(new Column[] {dm.createTimestampColumn("pocDatum"), dm.createTimestampColumn("zavDatum"),
        dm.createStringColumn("CNACPL",3) });
    tds2.setColumns(new Column[]{dm.createStringColumn("VRDOK",3), dm.createStringColumn("VRART",1)});
    jraDatumOd.setColumnName("pocDatum");
    jraDatumOd.setDataSet(tds);
//    jraDatumOd.setText("jraTextField1");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDo.setColumnName("zavDatum");
    jraDatumDo.setDataSet(tds);
//    jraDatumDo.setText("jraTextField2");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);

    new raDateRange(jraDatumOd, jraDatumDo);

    rcbVrDok.setRaColumn("VRDOK");
    rcbVrDok.setRaDataSet(tds2);
    rcbVrDok.setRaItems(new String[][] {
      {"Svi dokumenti",""},
      {"GOT - Gotovinski raèuni","GOT"},
      {"POD - Povratnica odobrenje","POD"},
      {"POS - POS raèuni","POS"}
    });

    rcbVrArt.setRaColumn("VRART");
    rcbVrArt.setRaDataSet(tds2);
    StorageDataSet exvr = Vrart.getDataModule().getScopedSet("cvrart nazvrart");
    exvr.open();
    exvr.insertRow(false);
    exvr.setString("CVRART", "X");
    exvr.setString("NAZVRART", "Sve vrste");
    ut.fillReadonlyData(exvr, "select DISTINCT artikli.vrart as cvrart, " +
    		"vrart.nazvrart as nazvrart from artikli, vrart " +
    		"where artikli.vrart = vrart.cvrart");
    rcbVrArt.setRaItems(exvr, "CVRART", "NAZVRART");
    
    rcbVrArt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        vrArtChanged();
      }
    });

    jlDatum.setText("Datum (od - do)");
    jlVrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlVrDok.setHorizontalTextPosition(SwingConstants.LEADING);
    jlVrDok.setText("Dokumenti");

    jlVrArt.setHorizontalAlignment(SwingConstants.TRAILING);
    jlVrArt.setHorizontalTextPosition(SwingConstants.LEADING);
    jlVrArt.setText("Vrsta artikla");
    jp.add(rcbVrDok,               new XYConstraints(454, 50, 150, 20));
    jp.add(rcbVrArt,      new XYConstraints(454, 75, 150, 20));

    jp.add(rpcskl,    new XYConstraints(0, 0, 655, -1));
    jp.add(jraDatumOd,     new XYConstraints(150, 50, 100, -1));
    jp.add(jraDatumDo,     new XYConstraints(255, 50, 100, -1));
    jp.add(jlDatum,   new XYConstraints(15, 50, -1, -1));
    jp.add(rpcart,     new XYConstraints(0, 100, 655, 75));
    jp.add(jlVrDok,         new XYConstraints(365, 52, 70, -1));
    jp.add(jlVrArt,      new XYConstraints(365, 77, 70, -1));

//    jp.add(jpCB,          new XYConstraints(150, 180, 455, 45));
//    jp.add(jlIspis,          new XYConstraints(15, 195, -1, -1));
//
//    jpCB.add(jlSljed,    new XYConstraints(15, 9, -1, -1));
//    jpCB.add(rcbSljed,       new XYConstraints(100, 7, 150, -1));
//    jpCB.add(jcbIspisKol,   new XYConstraints(315, 5, -1, -1));
//    jpCB.add(jcbKolNull,    new XYConstraints(393, 5, -1, -1));

/*
    this.addReport("hr.restart.robno.repIzvArt","Pregled prodaje po artiklima",5);
    this.addReport("hr.restart.robno.repIzvArt3","Pregled prodaje po artiklima - detaljno",5);

    */


    /*this.addReport("hr.restart.robno.repIzvArt",
                   "hr.restart.robno.repIzvProdajaArtikli",
                   "IzvArt",
                   "Pregled prodaje po artiklima");*/

    /*this.addReport("hr.restart.robno.repIzvArt3",
                   "hr.restart.robno.repIzvProdajaArtikli",
                   "IzvArt3",
                   "Pregled prodaje po artiklima");

    this.addReport("hr.restart.robno.repIzvArt4",
                   "hr.restart.robno.repIzvProdajaArtikli",
                   "IzvArt4",
                   "Pregled prodaje po artiklima na skladištima");*/

    /** @todo razmisliti o uklanjanju klasa i templateova sa nazivom repIzvArt2 */
//    this.addReport("hr.restart.robno.repIzvArt2","Pregled prodaje po artiklima - još detaljnije",5);
    this.setJPan(jp);
  }

  public void componentShow() {
    if(!dm.getSklad().isOpen())
      dm.getSklad().open();
    rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());
    rpcart.setDefParam();
    rpcart.EnabDisab(true);
    tds.setTimestamp("pocDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    if(!tds2.isOpen())
      tds2.open();
    tds2.setString("VRDOK","X");
    rcbVrDok.setSelectedIndex(0);
    rcbVrArt.setSelectedIndex(0);
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

  if(cskl_corg) {
    rpcskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
    rpcskl.jrfCSKL.forceFocLost();
//     jraDatumOd.requestFocus();
   } else if(!cskl_corg){
      rpcskl.jrfCSKL.requestFocus();
//     jraDatumOd.requestFocus();
   }
  }


  public boolean runFirstESC() {
    if(!rpcskl.jrfCSKL.getText().equals("") || !rpcart.jrfCART.isEnabled())
      return true;
    return false;
  }

  public void firstESC() {
    reset();
    if(!rpcart.jrfCART.getText().equals("") || !rpcart.jrfCART.isEnabled()) {
      rcc.EnabDisabAll(rpcart, true);
      rpcart.setCART();
    }
//    } else if(!rpcskl.jrfCSKL.getText().equals("")) {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocus();
//      rpcskl.jrfCSKL.requestFocus();
//    }
  }
  
  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return true;
  }
  
  public void okPress(){
    rcc.EnabDisabAll(rpcart, false);
    try {
      if(preparePrint()==0) {
        setNoDataAndReturnImmediately();
        rpcart.setCART();
        rcc.EnabDisabAll(rpcart, true);
      }
      
      reporting();
      
    } catch (Exception ex) {
      setNoDataAndReturnImmediately();
      ex.printStackTrace();
    }
  }

  protected void reporting() {
    this.killAllReports();

    if (rpcart.jrfCART.getText().equals("") || (!rpcskl.jrfCSKL.getText().equals("") && !rpcart.jrfCART.getText().equals("")))
    this.addReport("hr.restart.robno.repIzvArt3",
                   "hr.restart.robno.repIzvProdajaArtikli",
                   "IzvArt3",
                   "Pregled prodaje po artiklima");

    if (rpcskl.jrfCSKL.getText().equals("") || (!rpcskl.jrfCSKL.getText().equals("") && !rpcart.jrfCART.getText().equals("")))
    this.addReport("hr.restart.robno.repIzvArt4",
                   "hr.restart.robno.repIzvProdajaArtikli",
                   "IzvArt4",
                   "Pregled prodaje po artiklima na skladištima");
  }

  boolean podgrupe = false;

  public boolean Validacija() {

    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) return false;
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }

    vrstaDok = tds2.getString("VRDOK");
    vrstaArt = tds2.getString("VRART");
    /*if(rpcskl.jrfCSKL.getText().equals("")) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }*/
    if (tds.getTimestamp("zavDatum").after(hr.restart.util.Valid.getValid().findDate(false, 0))) {
      jraDatumDo.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Završni datum ve\u0107i od današnjeg !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jraDatumOd.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Po\u010Detni datum mora biti manji od završnog !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
//    rcc.EnabDisabAll(rpcart, false);
//    try {
//      if(preparePrint()==0) {
//        rpcart.setCART();
//        rcc.EnabDisabAll(rpcart, true);
//        JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//        return false;
//      }
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
    return true;
  }

  protected int preparePrint() {
    System.out.println("MALOPRODAJA"); //XDEBUG delete when no more needed
    vrdoks = "maloprodajni";
    qds.close();
//    qds2.close();
    qds3.close();

    datOd = tds.getTimestamp("pocDatum");
    datDo = tds.getTimestamp("zavDatum");
    String uvjet=  "";

    if (!rpcart.findCART(podgrupe).equals("")){
      uvjet = "AND "+ rpcart.findCART(podgrupe);
    } else System.out.println("rpcart.findCart je prazan string"); //XDEBUG delete when no more needed
    

    /*"AND "+rpcart.findCART(podgrupe);*/

    String qStr = rdUtil.getUtil().getIzArt(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,"MALO","");
    //---------------------------------------------------------------------------------
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));

//    String qStr2 = rdUtil.getUtil().getIzArt2(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
//        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,"MALO");
    //---------------------------------------------------------------------------------
//    qds2.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr2));

    String qStr3 = rdUtil.getUtil().getIzArt3(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,"MALO","");
    //---------------------------------------------------------------------------------
    qds3.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr3));

    qds.open();
//    qds2.open();
    qds3.open();

    return qds.getRowCount();
  }

  public static QueryDataSet getQDS() {
    return qds;
  }

//  public static QueryDataSet getQDS2() {
//    return qds2;
//  }

  public static QueryDataSet getQDS3() {
    return qds3;
  }

  protected void reset() {
    rcc.EnabDisabAll(jp, true);
    rpcart.EnabDisab(true);
    if(!rpcart.jrfCART.getText().equals("")) {
       rpcskl.jrfCSKL.forceFocLost();
     } else {
       rcc.EnabDisabAll(rpcskl, false);
     }
     OKEsc=false;
     jp.repaint();
  }

  public void showDefaultValues(){}

  protected Timestamp setFirstDay(Timestamp today) {
    String strToday = today.toString();
    String date = strToday.substring(0,8)+"01"+strToday.substring(10, strToday.length());
    return Timestamp.valueOf(date);
  }
  
  public String getCSKLART(){
    return "";
  }
  
  public boolean notKnjig(){
    return true;
  }
  
  public String getVrsta(){
    return vrdoks;
  }
  
  public String getGrupaArtikala(){
    if (!rpcart.getCGRART().equals("") && rpcart.getCART().equals("")){
      lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",rpcart.getCGRART());
      return "Grupa artikala " + dm.getGrupart().getString("NAZGRART");
      
    }
    return "";
  }
  
  private void vrArtChanged(){
    System.out.println("Vrsta artikla promjenjena"); //XDEBUG delete when no more needed
    DataSet vrstaArtikalaDohvatFilterSet = null;
    String vra = tds2.getString("VRART");
    
    System.out.println("Vrsta artikala = "+ vra); //XDEBUG delete when no more needed
    
    rpcart.clearFields();
    if (vra.equals("X") || vra.equals(""))
      vrstaArtikalaDohvatFilterSet = dm.getArtikli();
    else {
      vrstaArtikalaDohvatFilterSet = Artikli.getDataModule().getFilteredDataSet("VRART='"+vra+"'");
    }
    
    rpcart.jrfCART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfCART1.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfBC.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfNAZART.setRaDataSet(vrstaArtikalaDohvatFilterSet);
    rpcart.jrfJM.setRaDataSet(vrstaArtikalaDohvatFilterSet);
  }
  
  
  
}
