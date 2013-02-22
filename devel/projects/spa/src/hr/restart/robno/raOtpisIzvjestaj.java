/****license*****************************************************************
**   file: raOtpisIzvjestaj.java
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
import hr.restart.baza.Sklad;
import hr.restart.baza.Vrdokum;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;
import hr.restart.zapod.OrgStr;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raOtpisIzvjestaj extends raUpitFat {

  lookupData lD = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  raCommonClass rCC = raCommonClass.getraCommonClass();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  TableDataSet tds = new TableDataSet();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  
  private final BigDecimal NULA = Aus.zero2;
  private final BigDecimal STO = new BigDecimal("100.00");
//  private final BigDecimal MILIJUN = new BigDecimal("1000000.00");
  
  private JlrNavField jnfCORG = new JlrNavField();
  private JlrNavField jnfNAZORG = new JlrNavField();
  
  private HashMap generalije, zcs;
  
  private TableDataSet fieldSet = new TableDataSet();
  
  private raComboBox comboIzbor = new raComboBox();
  private JraTextField jnfGODINA = new JraTextField();
  private JraCheckBox jcbNula = new JraCheckBox();
  
  private static raOtpisIzvjestaj instanceOfMe = null;
  
  public static raOtpisIzvjestaj getInstance(){
    if (instanceOfMe == null) new raOtpisIzvjestaj();
    return instanceOfMe;
  }
  
  public QueryDataSet getReportSet(){
    return this.getJPTV().getDataSet();
  }
  
  public HashMap getGeneralije(){
    return generalije;
  }
  

  public raOtpisIzvjestaj() {
    try {
      initializer();
      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  JButton jbcorg = new JButton();

  private void initializer() {
    fieldSet.setColumns(new Column[] {
        dM.createStringColumn("IZBOR",2),
        dM.createStringColumn("CSKLCORG",15),
        dM.createStringColumn("GODINA",4)
    });
    fieldSet.open();
    
    
    
    
    JPanel jp = new JPanel();
    XYLayout xyl = new XYLayout();

    comboIzbor.setRaDataSet(fieldSet);
    comboIzbor.setRaColumn("IZBOR");
    
    comboIzbor.setRaItems(new String[][]{{"Org. jedinica", "O"}, {"Skladište", "S"}});

    comboIzbor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changeNavs();
      }
    });
    
    jp.setPreferredSize(new Dimension(640, 85));
    xyl.setHeight(85);
    xyl.setWidth(640);
    jp.setLayout(xyl);
    
    tds.setColumns(new Column[] {
            dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
            dM.createTimestampColumn("zavDatum", "Završni datum"),
            dM.createStringColumn("cskl", "Skladište", 12)
    });
    
    
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    new raDateRange(jtfPocDatum, jtfZavDatum);
    
    jcbNula.setText(" Prikaz svih artikala ");
    jcbNula.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbNula.setHorizontalTextPosition(SwingConstants.LEADING);
    
    jnfGODINA.setDataSet(fieldSet);
    jnfGODINA.setColumnName("GODINA");

    jp.add(comboIzbor, new XYConstraints(15, 20, 130, -1));
    jp.add(jnfCORG, new XYConstraints(150, 20, 100, -1));
    jp.add(jnfNAZORG, new XYConstraints(255, 20, 349, -1));
    jp.add(jbcorg, new XYConstraints(609, 20, 21, 21));
    
    
    jp.add(new JLabel("Datum (od-do)"), new XYConstraints(15, 55, 130, -1));
    jp.add(jtfPocDatum, new XYConstraints(150, 55, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(255, 55, 100, -1));
    // AI: za poslije kad Ante bude imao vremena i to... 
    //jp.add(jcbNula, new XYConstraints(400, 55, 204, -1));
    // eoAI
    jcbNula.setSelected(true);
//    jp.add(new JLabel("Godina"), new XYConstraints(15, 55, 130, -1));
//    jp.add(jnfGODINA, new XYConstraints(150, 55, 100, -1));

    
    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
    this.setJPan(jp);
    //IzvjestajOtpis
    addReport("hr.restart.robno.repOtpisIzvjestaj", "hr.restart.robno.repOtpisIzvjestaj", "IzvjestajOtpis", "Lista raspodjele otpisa");
    addReport("hr.restart.robno.repOtpisIzvjestaj", "hr.restart.robno.repOtpisIzvjestaj", "IzvjestajOtpisDozNedoz", "Lista raspodjele otpisa - dozvoljeni, nedozvoljeni otpis");
  }

  public String navDoubleClickActionName() {
    return "";
  }

  public int[] navVisibleColumns() {
    return new int[] {Aut.getAut().getCARTdependable(0,1,2),3,4,5,6,7,8,9,10,11,12,13,14}; /// ovo su sad sve kolone, izbaciti one koje se ne žele difoltno prikazivati 
  }

  public void okPress() {
    QueryDataSet qds = findDataSetForIspis(fieldSet.getString("CSKLCORG"), (comboIzbor.getSelectedIndex() == 0)); //rpcskl.jrfCSKL.getText(), jnfCORG.getText());
    if (qds == null || qds.rowCount() < 1) setNoDataAndReturnImmediately();
    
    generalije = new HashMap();
    generalije.put("CCLABEL",comboIzbor.getSelectedItem());
    generalije.put("CORGCSKL",jnfCORG.getText());
    generalije.put("NAZORGNAZSKL",jnfNAZORG.getText());
    generalije.put("PDAT",tds.getTimestamp("pocDatum"));
    generalije.put("ZDAT",tds.getTimestamp("zavDatum"));
    
    qds.first();
    setDataSetAndSums(qds,new String[] {"VRI_IZLAZ","VRI_OTPIS","VRI_OTPIS_DOZ","VRI_RAZ"});
  }

  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null || !jnfCORG.getText().equals("")) {
      return true;
    } else {
      return false;
    }
  }

  public void firstESC() {
    if (this.getJPTV().getDataSet() != null){
    this.getJPTV().clearDataSet();
      removeNav();
      raCommonClass.getraCommonClass().EnabDisabAll(this.getJPan(), true);
      jnfCORG.requestFocus();
      jnfCORG.selectAll();
    } else if (!jnfCORG.getText().equals("")) {
      fieldSet.setString("CSKLCORG", "");
      jnfCORG.setText("");
      jnfCORG.emptyTextFields();
      comboIzbor.requestFocus();
    }
  }

  public void componentShow() {
    fieldSet.setString("GODINA",
            hr.restart.util.Util.getNewQueryDataSet("select god from Knjigod where app='robno'").getString("GOD"));
    tds.setTimestamp("pocDatum",
            hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));    
        
    
    fieldSet.setString("IZBOR","O");
    comboIzbor.setSelectedIndex(0);
    this.getJPTV().clearDataSet();
  }

  public void initOtpisSds(StorageDataSet sds) {
    Column cart = dM.getDataModule().getArtikli().getColumn("CART").cloneColumn();
    Column cart1 = dM.getDataModule().getArtikli().getColumn("CART1").cloneColumn();
    Column bc = dM.getDataModule().getArtikli().getColumn("BC").cloneColumn();
    Column nazart = dM.getDataModule().getArtikli().getColumn("NAZART").cloneColumn();
    Column jm = dM.getDataModule().getArtikli().getColumn("JM").cloneColumn();
    Column zc = dM.createBigDecimalColumn("ZC", "Cijena zalihe", 2);
    Column kol_izlaz = dM.createBigDecimalColumn("KOL_IZLAZ", "Kolièina izlaza", 3);
    Column vri_izlaz = dM.createBigDecimalColumn("VRI_IZLAZ", "Vrijednost izlaza", 2);
    Column kol_otpisa = dM.createBigDecimalColumn("KOL_OTPIS", "Kolièina otpisanog", 3);
    Column vri_otpisa = dM.createBigDecimalColumn("VRI_OTPIS", "Vrijednost otpisanog", 2);
    Column kol_doz_otpisa = dM.createBigDecimalColumn("KOL_OTPIS_DOZ", "Kolièina moguæeg otpisa", 3);
    Column vri_doz_otpisa = dM.createBigDecimalColumn("VRI_OTPIS_DOZ", "Vrijednost moguæeg otpisa", 2);
    Column kol_razlika = dM.createBigDecimalColumn("KOL_RAZ", "Kolièina razlike", 3);
    Column vri_razlika = dM.createBigDecimalColumn("VRI_RAZ", "Vrijednost razlike", 2);
    Column postotak = dM.createBigDecimalColumn("POSTO", "Posto dozvoljenog otpisa", 2);
    Column kol_otpisad = dM.createBigDecimalColumn("KOL_OTPISD", "Kolièina otpisanog (doz)", 3);
    Column vri_otpisad = dM.createBigDecimalColumn("VRI_OTPISD", "Vrijednost otpisanog (doz)", 2);
    Column kol_otpisan = dM.createBigDecimalColumn("KOL_OTPISN", "Kolièina otpisanog (nedoz)", 3);
    Column vri_otpisan = dM.createBigDecimalColumn("VRI_OTPISN", "Vrijednost otpisanog (nedoz)", 2);

    

    sds.setColumns(new Column[]{cart, cart1, bc, nazart, jm, zc, kol_izlaz, vri_izlaz, kol_otpisa, vri_otpisa, kol_doz_otpisa, vri_doz_otpisa, kol_razlika, vri_razlika, postotak,kol_otpisad,vri_otpisad,kol_otpisan,vri_otpisan});
    sds.open();
  }

  public QueryDataSet findDataSetForIspis(String oznaka, boolean corg) { //String cskl, String corg) {
    String dodatak = "";
    if (corg) {
      dodatak = Condition.in("CSKL", Sklad.getDataModule().getTempSet(Condition.in("CORG", 
          OrgStr.getOrgStr().getOrgstrAndKnjig(oznaka)))).qualified("doki").toString();
      /*dodatak = "doki.cskl in (";
      QueryDataSet qds = Util.getUtil().getSkladFromCorg(); 
      
      for (;;){
       dodatak += "'"+qds.getString("CSKL")+"'";
       if (qds.next()){
         dodatak += ",";
       } else {
         dodatak += ")";
        break; 
       }
      }
*/      

    } else {
      dodatak = "doki.cskl='" + oznaka + "'";
    }
    String dodm = "meskla.cskliz" + dodatak.substring(9); 
    System.out.println("dodatak = " + dodatak);
    Condition otr = Condition.in("VRDOK", new String[] {"INM", "OTR"});
    Condition otr2 = otr; 
    if (!frmParam.getParam("robno", "otpisINM", "D",
        "Zbraja li se INM u otpis na izvještaju (D,N)").equalsIgnoreCase("D"))
      otr2 = Condition.equal("VRDOK", "OTR");
    
    QueryDataSet sds = new QueryDataSet();
    initOtpisSds(sds);
    hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
    DataSet sdokiz = Vrdokum.getDataModule().getTempSet(
        Condition.in("APP", new String[] {"robno","mp"}).
        and(Condition.in("TIPDOK", new String[] {"S","SF","FS"})).
        and(Condition.equal("VRSDOK", "I")).and(otr.not()));
    sdokiz.open();
    
    DataSet stanj = Aus.q("SELECT cart,zc FROM stanje WHERE "+
        "god='" + vl.findYear(tds.getTimestamp("zavDatum")) + 
        "' and " + dodatak.substring(5));
    zcs = new HashMap();
    for (stanj.first(); stanj.inBounds(); stanj.next())
      zcs.put(new Integer(stanj.getInt("CART")), stanj.getBigDecimal("ZC"));
    
    String sql = 
      "select stdoki.cart, sum(stdoki.KOL) as KOL_IZLAZ, " +
      "sum(stdoki.IRAZ) as VRI_IZLAZ " +
      "from doki,stdoki WHERE " +  
      rut.getDoc("doki", "stdoki") + " and " + dodatak + " and " + 
      Condition.between("DATDOK", tds, "pocDatum", "zavDatum").
      and(Condition.in("VRDOK", sdokiz)).qualified("doki") + 
      " group by stdoki.cart order by 1";

    System.out.println(sql);
    

    QueryDataSet ukupanizlaz = hr.restart.util.Util.getNewQueryDataSet(sql);
    
    sql = 
      "select stmeskla.cart, sum(stmeskla.KOL) as KOL_IZLAZ, " +
      "sum(stmeskla.ZADRAZIZ) as VRI_IZLAZ " +
      "from meskla,stmeskla WHERE " + 
      rut.getDocMes("meskla", "stmeskla") + " and " + dodm + " and " + 
      Condition.between("DATDOK", tds, "pocDatum", "zavDatum").
      and(Condition.in("VRDOK", "MES MEI")).qualified("meskla") + 
      " group by stmeskla.cart order by 1";

    System.out.println(sql);
    

    QueryDataSet ukupanmiz = hr.restart.util.Util.getNewQueryDataSet(sql);
    
    sql = "select stdoki.cart, sum(stdoki.KOL) as KOL_OTPIS, " +
          "sum(stdoki.IRAZ) as VRI_OTPIS " +
          "from doki,stdoki WHERE " + 
          rut.getDoc("doki", "stdoki") + " and " + dodatak + " and " + 
          Condition.between("DATDOK", tds, "pocDatum", "zavDatum").
          and(otr2).qualified("doki") + " group by stdoki.cart order by 1";
    
    System.out.println(sql);

    QueryDataSet ukupanotpis = hr.restart.util.Util.getNewQueryDataSet(sql);
    
    System.out.println(sql);
    System.out.println(ukupanotpis);
    
    ukupanizlaz.first();
    ukupanotpis.first();
    while (ukupanizlaz.inBounds() && ukupanotpis.inBounds()) {
      if (ukupanizlaz.getInt("CART") < ukupanotpis.getInt("CART"))
        updateList(sds, ukupanizlaz, ukupanmiz, null);
      else if (ukupanizlaz.getInt("CART") > ukupanotpis.getInt("CART"))
        updateList(sds, null, null, ukupanotpis);
      else updateList(sds, ukupanizlaz, ukupanmiz, ukupanotpis);
    }
    while (ukupanizlaz.inBounds())
      updateList(sds, ukupanizlaz, ukupanmiz, null);
    while (ukupanotpis.inBounds())
      updateList(sds, null, null, ukupanotpis);
    
    /*
    for (ukupanizlaz.first(); ukupanizlaz.inBounds(); ukupanizlaz.next()) {
      sds.insertRow(false);
      sds.setInt("CART", ukupanizlaz.getInt("CART"));
      sds.setString("CART1", ukupanizlaz.getString("CART1"));
      sds.setString("BC", ukupanizlaz.getString("BC"));
      sds.setString("NAZART", ukupanizlaz.getString("NAZART"));
      sds.setString("JM", ukupanizlaz.getString("JM"));
      sds.setBigDecimal("ZC", ukupanizlaz.getBigDecimal("ZC"));
      sds.setBigDecimal("KOL_IZLAZ", ukupanizlaz.getBigDecimal("KOL_IZLAZ"));
      sds.setBigDecimal("VRI_IZLAZ", ukupanizlaz.getBigDecimal("VRI_IZLAZ"));
      sds.setBigDecimal("POSTO", ukupanizlaz.getBigDecimal("POSTO"));
      sds.setBigDecimal("KOL_OTPIS_DOZ", ukupanizlaz.getBigDecimal("POSTO").multiply(ukupanizlaz.getBigDecimal("KOL_IZLAZ")));
      sds.setBigDecimal("KOL_OTPIS_DOZ",sds.getBigDecimal("KOL_OTPIS_DOZ").divide(STO,ukupanizlaz.getInt("ZNACDEC"),BigDecimal.ROUND_DOWN));
      sds.setBigDecimal("VRI_OTPIS_DOZ", ukupanizlaz.getBigDecimal("ZC").multiply(sds.getBigDecimal("KOL_OTPIS_DOZ")).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    for (ukupanotpis.first(); ukupanotpis.inBounds(); ukupanotpis.next()) {

      if (!lD.raLocate(sds, "CART", String.valueOf(ukupanotpis.getInt("CART")))) {
        sds.insertRow(false);
        sds.setInt("CART", ukupanotpis.getInt("CART"));
        sds.setString("CART1", ukupanotpis.getString("CART1"));
        sds.setString("BC", ukupanotpis.getString("BC"));
        sds.setString("NAZART", ukupanotpis.getString("NAZART"));
        sds.setString("JM", ukupanotpis.getString("JM"));
        sds.setBigDecimal("ZC", ukupanotpis.getBigDecimal("ZC"));
        sds.setBigDecimal("KOL_IZLAZ", NULA);
        sds.setBigDecimal("VRI_IZLAZ", NULA);
        sds.setBigDecimal("POSTO", ukupanotpis.getBigDecimal("POSTO"));
        sds.setBigDecimal("KOL_OTPIS_DOZ", NULA);
        sds.setBigDecimal("VRI_OTPIS_DOZ", NULA);
      }
      sds.setBigDecimal("KOL_OTPIS", ukupanotpis.getBigDecimal("KOL_OTPIS"));
      sds.setBigDecimal("VRI_OTPIS", ukupanotpis.getBigDecimal("VRI_OTPIS"));
      sds.setBigDecimal("KOL_RAZ", ukupanotpis.getBigDecimal("KOL_OTPIS").subtract(sds.getBigDecimal("KOL_OTPIS_DOZ")));
      sds.setBigDecimal("VRI_RAZ", ukupanotpis.getBigDecimal("VRI_OTPIS").subtract(sds.getBigDecimal("VRI_OTPIS_DOZ")));
    }

    for (sds.first();sds.inBounds();sds.next()){
    	
    
    	if (sds.getBigDecimal("KOL_OTPIS").compareTo(
    			sds.getBigDecimal("KOL_OTPIS_DOZ"))>0){
    		sds.setBigDecimal("KOL_OTPISD", sds.getBigDecimal("KOL_OTPIS_DOZ"));
            sds.setBigDecimal("VRI_OTPISD", sds.getBigDecimal("VRI_OTPIS_DOZ"));
        	sds.setBigDecimal("KOL_OTPISN",
        		sds.getBigDecimal("KOL_OTPIS").subtract(sds.getBigDecimal("KOL_OTPIS_DOZ")));
            sds.setBigDecimal("VRI_OTPISN",
            		sds.getBigDecimal("VRI_OTPIS").subtract(sds.getBigDecimal("VRI_OTPIS_DOZ")));
    	} else {
    		sds.setBigDecimal("KOL_OTPISD", sds.getBigDecimal("KOL_OTPIS"));
            sds.setBigDecimal("VRI_OTPISD", sds.getBigDecimal("VRI_OTPIS"));
        	sds.setBigDecimal("KOL_OTPISN", Aus.zero3);
            sds.setBigDecimal("VRI_OTPISN", Aus.zero2);
    	}
    }*/

    return sds;
  }
  
  String[] ccols = {"CART"};
  String[] acols = {"CART1", "BC", "NAZART", "JM"};
  void updateList(DataSet dest, DataSet izlaz, DataSet mes, DataSet otpis) {
    if (!jcbNula.isSelected() && (otpis == null || otpis.getBigDecimal("KOL_OTPIS").signum() == 0)) {
      return;
    }      
    dest.insertRow(false);
    if (izlaz != null)
      dM.copyColumns(izlaz, dest, ccols);
    else dM.copyColumns(otpis, dest, ccols);
    BigDecimal zc = (BigDecimal) zcs.get(new Integer(dest.getInt("CART")));
    if (zc == null) zc = Aus.zero0;
    dest.setBigDecimal("ZC", zc);
    int znacdec = 3;
    if (lD.raLocate(dm.getArtikli(), "CART", 
        Integer.toString(dest.getInt("CART")))) {
      dM.copyColumns(dm.getArtikli(), dest, acols);
      dest.setBigDecimal("POSTO", dm.getArtikli().getBigDecimal("POSTOINV"));
      String cg = dm.getArtikli().getString("CGRART");
      while (dest.getBigDecimal("POSTO").signum() <= 0 && cg != null &&
          cg.length() > 0 && lD.raLocate(dm.getGrupart(), "CGRART", cg)) {
        dest.setBigDecimal("POSTO", dm.getGrupart().getBigDecimal("POSTOINV"));
        cg = dm.getGrupart().getString("CGRARTPRIP");
        if (dm.getGrupart().getString("CGRART").equals(cg)) cg = null;
      }
      if (lD.raLocate(dm.getJedmj(), "JM", dest.getString("JM")))
        znacdec = dm.getJedmj().getInt("ZNACDEC");
    }
    if (izlaz != null) {
      BigDecimal kol = izlaz.getBigDecimal("KOL_IZLAZ");
      BigDecimal vri = Aus.zero2;
      if (mes != null && lD.raLocate(mes, "CART", 
          Integer.toString(izlaz.getInt("CART")))) {
        kol = kol.add(mes.getBigDecimal("KOL_IZLAZ"));
        vri = mes.getBigDecimal("VRI_IZLAZ");
      }
      
      BigDecimal doz = dest.getBigDecimal("POSTO").multiply(kol).
              divide(STO, znacdec, BigDecimal.ROUND_DOWN);
      if (kol.signum() < 0) doz = Aus.zero3;
      dest.setBigDecimal("KOL_IZLAZ", kol);
      dest.setBigDecimal("VRI_IZLAZ", vri.add(izlaz.getBigDecimal("VRI_IZLAZ")));
      dest.setBigDecimal("KOL_OTPIS_DOZ", doz);
      dest.setBigDecimal("VRI_OTPIS_DOZ", zc.
          multiply(doz).setScale(2, BigDecimal.ROUND_HALF_UP));
      izlaz.next();
    }
    if (otpis != null && otpis.getBigDecimal("KOL_OTPIS").signum() != 0) {
      dest.setBigDecimal("KOL_OTPIS", otpis.getBigDecimal("KOL_OTPIS"));
      dest.setBigDecimal("VRI_OTPIS", otpis.getBigDecimal("VRI_OTPIS"));
      Aus.div(dest, "ZC", "VRI_OTPIS", "KOL_OTPIS");
      Aus.mul(dest, "VRI_OTPIS_DOZ", "ZC", "KOL_OTPIS_DOZ");
      dest.setBigDecimal("KOL_RAZ", otpis.getBigDecimal("KOL_OTPIS").
          subtract(dest.getBigDecimal("KOL_OTPIS_DOZ")));
      dest.setBigDecimal("VRI_RAZ", otpis.getBigDecimal("VRI_OTPIS").
          subtract(dest.getBigDecimal("VRI_OTPIS_DOZ")));
      otpis.next();
    }
    if (dest.getBigDecimal("KOL_OTPIS").compareTo(
        dest.getBigDecimal("KOL_OTPIS_DOZ")) > 0) {
      dest.setBigDecimal("KOL_OTPISD", dest.getBigDecimal("KOL_OTPIS_DOZ"));
      dest.setBigDecimal("VRI_OTPISD", dest.getBigDecimal("VRI_OTPIS_DOZ"));
      dest.setBigDecimal("KOL_OTPISN", dest.getBigDecimal("KOL_OTPIS").
          subtract(dest.getBigDecimal("KOL_OTPIS_DOZ")));
      dest.setBigDecimal("VRI_OTPISN", dest.getBigDecimal("VRI_OTPIS").
          subtract(dest.getBigDecimal("VRI_OTPIS_DOZ")));
    } else {
      dest.setBigDecimal("KOL_OTPISD", dest.getBigDecimal("KOL_OTPIS"));
      dest.setBigDecimal("VRI_OTPISD", dest.getBigDecimal("VRI_OTPIS"));
      dest.setBigDecimal("KOL_OTPISN", Aus.zero3);
      dest.setBigDecimal("VRI_OTPISN", Aus.zero2);
    }
    dest.post();
  }
  
  private void changeNavs() {
    fieldSet.setString("CSKLCORG","");
    jnfCORG.setText("");
    jnfCORG.emptyTextFields();
    try {
      if (comboIzbor.getSelectedIndex() == 0) {
        jnfCORG.setDataSet(fieldSet);
        jnfCORG.setColumnName("CSKLCORG");
        jnfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        jnfCORG.setNavColumnName("CORG");
        jnfCORG.setColNames(new String[]{"NAZIV"});
        jnfCORG.setVisCols(new int[]{0, 1, 2});
        jnfCORG.setTextFields(new javax.swing.text.JTextComponent[]{jnfNAZORG});
        jnfCORG.setNavButton(jbcorg);

        jnfNAZORG.setColumnName("NAZIV");
        jnfNAZORG.setNavProperties(jnfCORG);
        jnfNAZORG.setSearchMode(1);
      } else {
        jnfCORG.setDataSet(fieldSet);
        jnfCORG.setColumnName("CSKLCORG");
        jnfCORG.setRaDataSet(hr.restart.robno.Util.getUtil().getSkladFromCorg());
        jnfCORG.setNavColumnName("CSKL");
        jnfCORG.setColNames(new String[]{"NAZSKL"});
        jnfCORG.setVisCols(new int[]{0, 1});
        jnfCORG.setTextFields(new javax.swing.text.JTextComponent[]{jnfNAZORG});
        jnfCORG.setNavButton(jbcorg);

        jnfNAZORG.setColumnName("NAZSKL");
        jnfNAZORG.setNavProperties(jnfCORG);
        jnfNAZORG.setSearchMode(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean Validacija() {
    if (jnfCORG.getText().equals("")) {
      jnfCORG.requestFocus();
      JOptionPane.showConfirmDialog(null, (comboIzbor.getSelectedIndex() == 0) ? "Unesite organizacijsku jedinicu !":"Unesite skladište !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
        jtfPocDatum.requestFocus();
        JOptionPane.showMessageDialog(jtfPocDatum,
          "Po\u010Detni datum ve\u0107i od završnog !","Greška",JOptionPane.ERROR_MESSAGE);
        return false;
    }
    return true;
  }
}
