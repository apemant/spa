/****license*****************************************************************
**   file: upStanjeNaSkladistu.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraToggleButton;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * deprecated
 */

public class upStanjeNaSkladistu extends raUpitFat {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  rdUtil rde = rdUtil.getUtil();

  JPanel jp = new JPanel();
  JPanel jPanel3 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();

  JraTextField jtfStanjeNaDan = new JraTextField();
  JraTextField jtfStanjeZaGod = new JraTextField();

  JraRadioButton jrbNaDan = new JraRadioButton();
  protected JraRadioButton jrbSaStanja = new JraRadioButton();

  raButtonGroup rbgStanje = new raButtonGroup();

  JraToggleButton jtbKolicinaNula = new JraToggleButton();


  JLabel jlStanje = new JLabel();
  XYLayout xYLayout = new XYLayout();
//  XYLayout xYLayout2 = new XYLayout();
  rapancskl rpcskl = new rapancskl(){
    public void findFocusAfter() {
      rpcart.setDefParam();
      rpcart.setCART();
    }
  };
  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
//      jrbSaStanja.requestFocus();
    }
  };

  Column colNAB = new Column();
  Column colMAR = new Column();
  Column colPOR = new Column();

  TableDataSet tds = new TableDataSet();
  protected QueryDataSet mainDataSet = new QueryDataSet();


  //****
//  private JPanel jpCB = new JPanel();
//  private TitledBorder ispisTitledBorder;
//  JLabel jlSljed = new JLabel("Sljed");
//  private JraCheckBox jcbKolNull = new JraCheckBox();
//  private raComboBox rcbSljed = new raComboBox();
  //****

  static upStanjeNaSkladistu ust;

  public static upStanjeNaSkladistu getInstance(){
    if (ust == null) ust = new upStanjeNaSkladistu();
    return ust;
  }


  public upStanjeNaSkladistu() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    ust = this;
  }

  private void jbInit() throws Exception {
    naslovGenericReport = "ZALIHA ARTIKALA";
    jp.setLayout(borderLayout1);

    tds.setColumns(new Column[] {dm.createTimestampColumn("NADAN"),
    dm.createStringColumn("SLJED",10),dm.createStringColumn("GODINA",4)});

    jtfStanjeNaDan.setHorizontalAlignment(SwingConstants.CENTER);
    jtfStanjeNaDan.setColumnName("NADAN");
    jtfStanjeNaDan.setDataSet(tds);
    jlStanje.setText("Stanje");

    jtfStanjeZaGod.setHorizontalAlignment(SwingConstants.CENTER);
    jtfStanjeZaGod.setColumnName("GODINA");
    jtfStanjeZaGod.setDataSet(tds);

    rpcskl.setRaMode('S');
    rpcart.setBorder(null);
    rpcart.setMode(new String("DOH"));
    rpcart.setSearchable(false);
    jPanel3.setLayout(xYLayout);

    colNAB.setCaption("Nabavna vrijednost");
    colNAB.setColumnName("NAB");
    colNAB.setDataType(Variant.BIGDECIMAL);
    colNAB.setDisplayMask("###,###,##0.00");
    colNAB.setDefault("0");
    colNAB.setResolvable(false);
    colNAB.setSqlType(0);

    colMAR.setCaption("Popust");
    colMAR.setColumnName("MAR");
    colMAR.setDataType(Variant.BIGDECIMAL);
    colMAR.setDisplayMask("###,###,##0.00");
    colMAR.setDefault("0");
    colMAR.setResolvable(false);
    colMAR.setSqlType(0);

    colPOR.setCaption("Porez");
    colPOR.setColumnName("POR");
    colPOR.setDataType(Variant.BIGDECIMAL);
    colPOR.setDisplayMask("###,###,##0.00");
    colPOR.setDefault("0");
    colPOR.setResolvable(false);
    colPOR.setSqlType(0);

    xYLayout.setWidth(655);
    xYLayout.setHeight(135);

//    ispisTitledBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,new Color(224, 255, 255),new Color(76, 90, 98),new Color(109, 129, 140)),"");

//    jcbKolNull.setHorizontalTextPosition(SwingConstants.LEADING);
//    jcbKolNull.setText("Bez kolièina = 0");
    jtbKolicinaNula.setText("Kol. 0");
    jtbKolicinaNula.setIcon(raImages.getImageIcon(raImages.IMGX));
    jtbKolicinaNula.setSelectedIcon(raImages.getImageIcon(raImages.IMGOK));
//    jtbKolicinaNula.setDisabledSelectedIcon(raImages.getImageIcon(raImages.IMGX));
    jtbKolicinaNula.setToolTipText("Prikaz artikala s kolièinom 0");
    jtbKolicinaNula.setFocusPainted(false);

//    jpCB.setBorder(ispisTitledBorder);

//    jpCB.setLayout(xYLayout2);
//    xYLayout2.setWidth(400);
//    xYLayout2.setHeight(30);

    /*rcbSljed.setDataSet(tds);
    rcbSljed.setColumnName("SLJED");
    String nazOznaka = Aut.getAut().getCARTdependable("Šifra artikla","Oznaka artikla","Bar code artikla");
    String oznaka = Aut.getAut().getCARTdependable("CART","CART1","BC");
    rcbSljed.setRaItems(new String[][] {
      {nazOznaka,oznaka},
      {"Naziv artikla","NAZART"},
      {"Kolièina artikla","KOL"}
    });*/

//    jrbSaStanja.setHorizontalTextPosition(SwingConstants.LEADING);

//    jrbNaDan.setHorizontalTextPosition(SwingConstants.LEADING);
    rbgStanje.setHorizontalAlignment(SwingConstants.LEFT);
    rbgStanje.setHorizontalTextPosition(SwingConstants.LEADING);
    rbgStanje.add(jrbNaDan,"Na dan");
    rbgStanje.add(jrbSaStanja,"Kumulativi u godini");

    jrbNaDan.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e) {
        lightsCameraAction();
      }
    });
    jrbSaStanja.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e) {
        lightsCameraAction();
      }
    });

    jp.add(jPanel3, BorderLayout.CENTER);
    jp.add(rpcskl, BorderLayout.NORTH);

    jPanel3.add(rpcart,     new XYConstraints(0, 0, 655, 75));

    jPanel3.add(jrbSaStanja, new XYConstraints(150, 72, -1, -1));
    jPanel3.add(jrbNaDan,  new XYConstraints(400, 72, -1, -1));
//
    jPanel3.add(jtfStanjeZaGod,    new XYConstraints(305, 75, 50, -1));
    jPanel3.add(jtfStanjeNaDan,     new XYConstraints(500, 75, 104, -1));
//
    jPanel3.add(jlStanje, new XYConstraints(15, 75, -1, -1));



//    jPanel3.add(jpCB, new XYConstraints(150,100,455,45));

//    /*jpCB*/jPanel3.add(jlSljed,    new XYConstraints(15, 100, -1, -1));
//    /*jpCB*/jPanel3.add(rcbSljed,       new XYConstraints(150, 100, 150, -1));
    /*jpCB*/jPanel3.add(/*jcbKolNull*/ jtbKolicinaNula,    new XYConstraints(500,100,104,21));        //(502, 100, 104, -1));

    this.setJPan(jp);

//    rpcart.setNextFocusableComponent(jrbSaStanja);
    /*jcbKolNull*/jtbKolicinaNula.setNextFocusableComponent(rpcskl);
  }

  String defSKL = raUser.getInstance().getDefSklad();

  public void componentShow() {
    boolean cskl_corg = rde.getCSKL_CORG(defSKL, hr.restart.zapod.OrgStr.getKNJCORG());
    if(!dm.getSklad().isOpen())
      dm.getSklad().open();
    rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());
//    rcbSljed.setSelectedIndex(0);
    tds.open();
    tds.setString("SLJED",Aut.getAut().getCARTdependable("CART","CART1","BC"));
    /*jcbKolNull.setSelected(true);*/ jtbKolicinaNula.setSelected(true);
    rpcart.setDefParam();
    if(cskl_corg) {
      rpcskl.setCSKL(defSKL);
    }
    showDefaultValues();
    if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocusLater();
    else rpcart.setCARTLater();
  }

  String vrzal, qStr;
  private int[] viskol;
  lookupData ld = lookupData.getlookupData();
  

  public void okPress() {
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()});
    vrzal = dm.getSklad().getString("VRZAL");
    mainDataSet.setSort(null);
    try {
      killAllReports();
    } catch (Exception e) {
      System.err.println("ne postoje custom reporti");
    }
    
    QueryDataSet simSet = ut.getNewQueryDataSet("SELECT cart,sigkol,minkol FROM Artikli WHERE sigkol != 0.000 or minkol !=0.000");

    if (jrbSaStanja.isSelected()){ //isTrenutno()){
//      if (!rpcskl.getCSKL().equals(""))
//        this.addReport("hr.restart.robno.repStanje", "hr.restart.robno.repStanje", "Stanje", "Ispis stanja");
//      else {
//        this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repStanje", "StanjeSkl", "Ispis stanja po skladištima");
//        this.addReport("hr.restart.robno.repStanjeArt", "hr.restart.robno.repStanje", "StanjeArt", "Ispis usporednog stanja artikala");
//      }
      handleReports();
      qStr = rde.defaultStanje(rpcskl.getCSKL(), rpcart.findCART(podgrupe), tds.getString("GODINA"), tds.getString("SLJED"));
      mainDataSet.close();
      mainDataSet.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      mainDataSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      mainDataSet.setColumns(new Column[] {
        (Column) dm.getStanje().getColumn("CSKL").clone(),
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone(),
        (Column) dm.getStanje().getColumn("ZC").clone(),
        (Column) dm.getStanje().getColumn("NC").clone(),
        (Column) dm.getStanje().getColumn("VC").clone(),
        (Column) dm.getStanje().getColumn("MC").clone(),
        (Column) colNAB.clone(),
        (Column) colMAR.clone(),
        (Column) colPOR.clone(),
        (Column) dm.getStanje().getColumn("VRI").clone(),
        (Column) dm.getStanje().getColumn("KOLREZ").clone(),
        dm.createIntColumn("SIGMIN")
      });
      mainDataSet.getColumn("CART").setVisible(0);
      mainDataSet.getColumn("CART1").setVisible(0);
      mainDataSet.getColumn("BC").setVisible(0);
      mainDataSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);

      mainDataSet.getColumn("NAB").setVisible(0);
      mainDataSet.getColumn("MAR").setVisible(0);   // template ::: setHidden(true); //
      mainDataSet.getColumn("POR").setVisible(0);
      mainDataSet.getColumn("BRDOK").setVisible(0);
      mainDataSet.getColumn("VRDOK").setVisible(0);
      mainDataSet.getColumn("SIGMIN").setVisible(0);
      
      disableAdditionalColumns();

      openDataSet(mainDataSet);
      mainDataSet.first();

      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      if(/*jcbKolNull.isSelected()*/!jtbKolicinaNula.isSelected()){  
        mainDataSet = filterDataSet(mainDataSet);
        if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      }
      
      if (simSet.getRowCount() != 0) {
        mainDataSet.first();
        do {
          if (ld.raLocate(simSet,"CART",mainDataSet.getInt("CART")+"")){
            if (simSet.getBigDecimal("MINKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >=0){
              mainDataSet.setInt("SIGMIN",2);
            } else if (simSet.getBigDecimal("SIGKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >=0){
              mainDataSet.setInt("SIGMIN",1);
            } else {
              mainDataSet.setInt("SIGMIN",0);
            }
          } else {
            mainDataSet.setInt("SIGMIN", 0);
          }
        } while (mainDataSet.next());
      }
      
//    sysoutTEST sot = new sysoutTEST(false);
//    sot.showInFrame(mainDataSet,"");
      if (rpcskl.getCSKL().equals(""))
        viskol = new int[] {0,1,2,3,4,8};
      else
        viskol = new int[] {1,2,3,4,8};
    
      mainDataSet.last();

      this.getJPTV().addTableModifier(new SignalColorModifier());
      setDataSetAndSums(mainDataSet, new String[] {"NAB","MAR","POR","VRI"});
    } else {
      if (!rpcart.findCART(podgrupe).equals("")){
        qStr = rde.getSkladSqlPoj(vrzal, rpcskl.getCSKL(), rpcart.findCART(podgrupe) , util.getTimestampValue(tds.getTimestamp("NADAN"), 1), tds.getString("SLJED"));
      } else {
        qStr = rde.getSkladSqlPoj(vrzal, rpcskl.getCSKL(), "" , util.getTimestampValue(tds.getTimestamp("NADAN"), 1), tds.getString("SLJED"));
      }
//      this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repStanje", "StanjeSkl", "Ispis stanja");
      
      handleReports();
      
      System.out.println("UPIT "+qStr);

      mainDataSet.close();
      mainDataSet.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      mainDataSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      mainDataSet.setColumns(new Column[] {
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStanje().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone(),
        (Column) dm.getStanje().getColumn("ZC").clone(),
        (Column) dm.getStanje().getColumn("NC").clone(),
        (Column) dm.getStanje().getColumn("VC").clone(),
        (Column) dm.getStanje().getColumn("MC").clone(),
        (Column) colNAB.clone(),
        (Column) colMAR.clone(),
        (Column) colPOR.clone(),
        (Column) dm.getStanje().getColumn("VRI").clone(),
        dm.createIntColumn("SIGMIN")
      });

      String par = frmParam.getParam("robno","indiCart");
      if(par.equals("BC")) {
        mainDataSet.getColumn("CART").setVisible(0);
        mainDataSet.getColumn("CART1").setVisible(0);
      }
      else if (par.equals("CART1")) {
        mainDataSet.getColumn("CART").setVisible(0);
        mainDataSet.getColumn("BC").setVisible(0);
      } else {
        mainDataSet.getColumn("CART1").setVisible(0);
        mainDataSet.getColumn("BC").setVisible(0);
      }
      mainDataSet.getColumn("NAB").setVisible(0);
      mainDataSet.getColumn("MAR").setVisible(0);
      mainDataSet.getColumn("POR").setVisible(0);
      mainDataSet.getColumn("BRDOK").setVisible(0);
      mainDataSet.getColumn("VRDOK").setVisible(0);
      mainDataSet.getColumn("SIGMIN").setVisible(0);

      openDataSet(mainDataSet);

      mainDataSet.first();
      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      mainDataSet= sumDSPoj(mainDataSet,simSet);
      if(/*jcbKolNull.isSelected()*/!jtbKolicinaNula.isSelected()) mainDataSet = filterDataSet(mainDataSet);
      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      if (tds.getString("SLJED").equals("KOL")){
        mainDataSet.setSort(new SortDescriptor(new String[]{tds.getString("SLJED")},true,true));
      }
      
      System.out.println("--------------------------------------"); //XDEBUG delete when no more needed
      disableAdditionalColumns();
      System.out.println("--------------------------------------"); //XDEBUG delete when no more needed
      
//      if (simSet.getRowCount() != 0) {
//        lookupData ld = lookupData.getlookupData();
//        mainDataSet.first();
//        do {
//          if (ld.raLocate(simSet,"CART",mainDataSet.getInt("CART")+"")){
//            if (simSet.getBigDecimal("MINKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >=0){
//              mainDataSet.setInt("SIGMIN",2);
//            } else if (simSet.getBigDecimal("SIGKOL").compareTo(mainDataSet.getBigDecimal("KOL")) >=0){
//              mainDataSet.setInt("SIGMIN",1);
//            } else {
//              mainDataSet.setInt("SIGMIN",0);
//            }
//          } else {
//            mainDataSet.setInt("SIGMIN", 0);
//          }
//        } while (mainDataSet.next());
//      }

      viskol = new int[] {0,1,2,3,7};
      mainDataSet.last();
      this.getJPTV().addTableModifier(new SignalColorModifier());
      setDataSetAndSums(mainDataSet, new String[] {"NAB","MAR","POR","VRI"});
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        rcc.setLabelLaF(jtfStanjeNaDan,false);
        rcc.setLabelLaF(jtfStanjeZaGod,false);
      }
    });
  }

  public int[] navVisibleColumns(){
    return viskol;//new int[]{0,1,2,3,4,8};
  }

  public String navDoubleClickActionName(){
    return "Kartica artikla";
  }

  private QueryDataSet filterDataSet(QueryDataSet qds){
    qds.open();
    qds.first();
    do{
      if (qds.getBigDecimal("KOL").compareTo(new BigDecimal(0))==0
          || qds.getBigDecimal("KOL").compareTo(new BigDecimal(0))==-1)
        qds.deleteRow();
      else
        qds.next();
    }while(qds.inBounds());
    return qds;
  }

  boolean podgrupe = false;

  public boolean Validacija(){
    if(isNaDan() && rpcskl.getCSKL().equals("")){
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      rpcskl.setCSKL("");
      return false;
    }
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) {return false;}
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }

  public void firstESC(){
    System.out.print("FIRST ESC : ");
    if ((rpcart.findCART().equals("")) && this.getJPTV().getDataSet()==null && !isInterrupted()) {
      System.out.println("(rpcart.findCART().equals(\"\")) && this.getJPTV().getDataSet()==null");
      rpcart.EnabDisab(true);
      rcc.EnabDisabAll(jPanel3, true);
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
    } else {
      System.out.println("else");
      rpcart.EnabDisab(true);
      rcc.EnabDisabAll(jPanel3, true);
      rcc.EnabDisabAll(rpcskl,true);
      lightsCameraAction();
      tds.setTimestamp("NADAN", vl.findDate(false,0));
      this.getJPTV().clearDataSet();

      removeNav();

      rpcart.setCART();
      rpcskl.jrfCSKL.requestFocus();
      
    }
    nTransData=0;
  }

  public boolean runFirstESC() {
    if (rpcskl.getCSKL().equals("") && this.getJPTV().getDataSet() == null) {
      return false;
    } else {
      return true;
    }
  }

  void showDefaultValues() {
    rpcart.EnabDisab(true);
    tds.setTimestamp("NADAN", vl.findDate(false,0));
    tds.setString("GODINA",ut.getYear(vl.getToday()));
//    jrbSaStanja.setSelected();
    rbgStanje.setSelected(jrbSaStanja);
    lightsCameraAction();
    this.getJPTV().clearDataSet();
    /*jcbKolNull*/jtbKolicinaNula.setSelected(false);
  }

  public QueryDataSet sumDSPoj(QueryDataSet qds, QueryDataSet simSet){
    QueryDataSet tempQds = new QueryDataSet();
    tempQds.setColumns(new Column[] {
      (Column) dm.getStdoki().getColumn("BRDOK").clone(),
      (Column) dm.getStdoki().getColumn("VRDOK").clone(),
      (Column) dm.getStanje().getColumn("CART").clone(),
      (Column) dm.getArtikli().getColumn("CART1").clone(),
      (Column) dm.getArtikli().getColumn("BC").clone(),
      (Column) dm.getArtikli().getColumn("NAZART").clone(),
      (Column) dm.getArtikli().getColumn("JM").clone(),
      (Column) dm.getStanje().getColumn("KOL").clone(),
      (Column) dm.getStanje().getColumn("ZC").clone(),
      (Column) dm.getStanje().getColumn("NC").clone(),
      (Column) dm.getStanje().getColumn("VC").clone(),
      (Column) dm.getStanje().getColumn("MC").clone(),
      (Column) colNAB.clone(),
      (Column) colMAR.clone(),
      (Column) colPOR.clone(),
      (Column) dm.getStanje().getColumn("VRI").clone(),
      dm.createIntColumn("SIGMIN")
    });
    tempQds.open();

    qds.open();
    qds.first();
    
    QueryDataSet stanje = ut.getNewQueryDataSet("select cart, zc, nc, vc, mc from stanje where cskl = '" + rpcskl.getCSKL()+"'");

    do {
      if (lookupData.getlookupData().raLocate(tempQds,"CART",qds.getInt("CART")+"")){
        tempQds.setBigDecimal("KOL", tempQds.getBigDecimal("KOL").add(qds.getBigDecimal("KOL")));
        tempQds.setBigDecimal("VRI", tempQds.getBigDecimal("VRI").add(qds.getBigDecimal("VRI")));
      } else {
        tempQds.insertRow(false);
        tempQds.setInt("BRDOK", qds.getInt("BRDOK"));
        tempQds.setString("VRDOK", qds.getString("VRDOK"));
        tempQds.setInt("CART", qds.getInt("CART"));
        tempQds.setString("CART1", qds.getString("CART1"));
        tempQds.setString("BC", qds.getString("BC"));
        tempQds.setString("NAZART", qds.getString("NAZART"));
        tempQds.setString("JM", qds.getString("JM"));
        tempQds.setBigDecimal("KOL", qds.getBigDecimal("KOL"));

        lookupData.getlookupData().raLocate(stanje,"CART",qds.getInt("CART")+"");
        
        tempQds.setBigDecimal("ZC", stanje.getBigDecimal("ZC"));
        tempQds.setBigDecimal("NC", stanje.getBigDecimal("NC"));
        tempQds.setBigDecimal("VC", stanje.getBigDecimal("VC"));
        tempQds.setBigDecimal("MC", stanje.getBigDecimal("MC"));
        
        tempQds.setBigDecimal("VRI", qds.getBigDecimal("VRI"));
        tempQds.setBigDecimal("POR", qds.getBigDecimal("POR"));
        tempQds.setBigDecimal("MAR", qds.getBigDecimal("MAR"));
        tempQds.setBigDecimal("NAB", qds.getBigDecimal("NAB"));
        tempQds.post();
      }
     
      if (simSet != null && simSet.rowCount() != 0 && ld.raLocate(simSet,"CART",qds.getInt("CART")+"")){
        if (simSet.getBigDecimal("MINKOL").compareTo(qds.getBigDecimal("KOL")) >=0){
          tempQds.setInt("SIGMIN",2);
        } else if (simSet.getBigDecimal("SIGKOL").compareTo(qds.getBigDecimal("KOL")) >=0){
          tempQds.setInt("SIGMIN",1);
        } else {
          tempQds.setInt("SIGMIN",0);
        }
      } else {
        tempQds.setInt("SIGMIN", 0);
      }
    } while (qds.next());

    String par = frmParam.getParam("robno","indiCart");
    if(par.equals("BC")){
      tempQds.getColumn("CART").setVisible(0);
      tempQds.getColumn("CART1").setVisible(0);
    } else if (par.equals("CART1")){
      tempQds.getColumn("CART").setVisible(0);
      tempQds.getColumn("BC").setVisible(0);
    } else {
      tempQds.getColumn("CART1").setVisible(0);
      tempQds.getColumn("BC").setVisible(0);
    }

    tempQds.getColumn("NAB").setVisible(0);
    tempQds.getColumn("MAR").setVisible(0);
    tempQds.getColumn("POR").setVisible(0);
    tempQds.getColumn("BRDOK").setVisible(0);
    tempQds.getColumn("VRDOK").setVisible(0);
    tempQds.getColumn("SIGMIN").setVisible(0);
    return tempQds;
  }

  public DataSet getQds() {
//    sysoutTEST sot = new sysoutTEST(false);
//    sot.prn(getJPTV().getMpTable().getDataSet());
    return getJPTV().getMpTable().getDataSet();
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }

  public java.sql.Timestamp getDatum() {
    return tds.getTimestamp("NADAN");
  }

  public boolean isNaDan(){
    return jrbNaDan.isSelected();
  }

  public String getZaGodinu(){
    return tds.getString("GODINA");
  }

  int nTransData=-1;
  java.sql.Timestamp nTransDate, nTransDateOd;
  boolean nTransDonos = false;
  String nTransCskl="";

  public void jptv_doubleClick() {
    if (isNaDan()){
      nTransDate = tds.getTimestamp("NADAN");
      nTransDateOd = ut.getYearBegin(tds.getString("GODINA"));
//      System.out.println("ntrans date 1 " + nTransDate);
    } else {
      nTransDate = ut.getYearEnd(tds.getString("GODINA"));
      nTransDateOd = ut.getYearBegin(tds.getString("GODINA"));
//      System.out.println("ntrans date 2 " + nTransDate);
    }
    if (rpcskl.getCSKL().equals(""))
      nTransCskl = this.getJPTV().getMpTable().getDataSet().getString("CSKL");
    else
      nTransCskl = rpcskl.getCSKL();
    nTransData=this.getJPTV().getMpTable().getDataSet().getInt("CART");
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", res.getString("upFrmKartica_title"));
  }

  private void lightsCameraAction(){
    rcc.setLabelLaF(jtfStanjeZaGod,jrbSaStanja.isSelected());
    rcc.setLabelLaF(jtfStanjeNaDan,jrbNaDan.isSelected());
  }
  
  
  public void cancelPress() {
    super.cancelPress();
    nTransData=0;
  }
  
  ///// SKLADISNO POSLOVANJE METODE ZA OVERRIDE !!!
  
  
  protected void disableAdditionalColumns(){
    //override
  }
  
  protected void handleReports(){
   // override 
    if (jrbSaStanja.isSelected()){ //Trenutno
      if (!rpcskl.getCSKL().equals(""))
        this.addReport("hr.restart.robno.repStanje", "hr.restart.robno.repSklStanje", "Stanje", "Ispis stanja");
      else {
        this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repSklStanje", "StanjeSkl", "Ispis stanja po skladištima");
        this.addReport("hr.restart.robno.repStanjeArt", "hr.restart.robno.repSklStanje", "StanjeArt", "Ispis usporednog stanja artikala");
      }
    } else { //Promet
      this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repSklStanje", "StanjeSkl", "Ispis stanja");
    }
  }

  
  public boolean saStanja(){
    return jrbSaStanja.isSelected();
  }
  
}



class SignalColorModifier extends raTableModifier {
  Variant v = new Variant();
  boolean signal = false;

  public boolean doModify() {

    if (getTable() instanceof JraTable2) {
      JraTable2 tab = (JraTable2) getTable();
      if (tab.getDataSet().getRowCount() > 0 && tab.getDataSet().hasColumn("SIGMIN") != null) {
        tab.getDataSet().getVariant("SIGMIN", this.getRow(), v);
        if (v.getInt() == 1)signal = true;
        else signal = false;
        return (v.getInt() == 1 || v.getInt() == 2);
      }
    }
    return false;
  }
  
  public void modify(){
    if (hr.restart.sisfun.frmParam.getFrmParam().getParam("robno","stanjeAlert","D","Bojanje stanja u ovisnosti o sig. i min. kol D - šareni prikaz, N - monotoni prikaz").equals("D")){
    Color colorS = hr.restart.swing.raColors.yellow;//Color.green.darker().darker();
      Color colorN = hr.restart.swing.raColors.red;//Color.red;
      JComponent jRenderComp = (JComponent) renderComponent;
      if (isSelected()) {
        if (v.getInt() == 1) {
          jRenderComp.setBackground(colorS);
          jRenderComp.setForeground(Color.black);
        } else if (v.getInt() == 2) {
          jRenderComp.setBackground(colorN);
          jRenderComp.setForeground(Color.black);
        } else {
          jRenderComp.setBackground(getTable().getSelectionBackground());
          jRenderComp.setForeground(getTable().getSelectionForeground());
        }
      } else {
        if (v.getInt() == 1) {
          jRenderComp.setForeground(Color.yellow.darker().darker());
        } else if (v.getInt() == 2) {
          jRenderComp.setForeground(Color.red);
        } else {
          jRenderComp.setForeground(getTable().getForeground());
        }
      }
    }
  }
}