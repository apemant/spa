/****license*****************************************************************
**   file: frmVirmani.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.sisfun.raDelayWindow;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raUpitLite;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmVirmani extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  QueryDataSet filteredQds= hr.restart.baza.Virmani.getDataModule().getTempSet("1=0");
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  int selectedRow=0;
  String knjigovodstvo = OrgStr.getKNJCORG();
  boolean idShowed = false;

  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect(){
    public boolean Validacija() {
      if(!jlrCKey.getText().equals("")) {
        ckey = jlrCKey.getText();
        return true;
      } else {
        jlrCKey.requestFocus();
        return false;
      }
    }
  };

  private XYLayout xYLayout1 = new XYLayout();
  private JlrNavField jlrCKey = new JlrNavField();
  private JraButton jButton1 = new JraButton();
  private JLabel jlPresCKey = new JLabel();
  jpVirmani jpDetail2; /** @todo this is old */
  jpVirmani2 jpDetail; /** @todo proba Virmani2 */
  JPanel jpSel = new JPanel();
  static frmVirmani fVirmani;
  static StorageDataSet diskDS;
  public static String[] PKs;
  LinkedList msg = new LinkedList();
  String[] keys = new String[] {};
  public String app, /*knjig,*/ckey;
  String brRacNT;
  String brRacUK="";
  Timestamp today;
  short rbr = 0;
  int addIdx = 0;
  boolean repAdded = false;
  boolean sv = false;
  boolean rac = false;
  boolean pb = false;
  boolean iz = false;
  boolean pnbo2 = false;
  boolean pnbz2 = false;

//*** konstruktori
  public frmVirmani() {
    this("zapod");
  }

  public frmVirmani(String app) {
//    System.out.println("APP: " + app);
//    knjig = hr.restart.zapod.OrgStr.getKNJCORG();
    fVirmani = this;
    this.app = app;
    pres.setSelDataSet(hr.restart.baza.Virmani.getDataModule().getTempSet("APP='"+app+"'"));
    try {
      jbInit();
      this.getRaQueryDataSet().open();
    } catch(Exception e) {}
    pres.setSelPanel(jpSel);
  }

  public frmVirmani(String app,boolean ckey) {
//    knjig = hr.restart.zapod.OrgStr.getKNJCORG();
    fVirmani = this;
    this.app = app;
    try {
      jbInit();
      this.getRaQueryDataSet().open();
    } catch(Exception e) {}
  }

//*** staticki getter
  public static frmVirmani getInstance() {
    if(fVirmani == null)
      fVirmani = new frmVirmani();
    return fVirmani;
  }

//*** raMatPodaci metode
  public void EntryPoint(char mode) {
    rcc.setLabelLaF(jpDetail2.jtfHorizontal, false);
/** @todo  ovo iznad kod starih virmana odkomentirati */
    rcc.setLabelLaF(jpDetail2.jraJedzav, false);
    if (mode == 'I') {
      if (isNewVir()) {
        rcc.setLabelLaF(jpDetail.jraNateret, false);
        rcc.setLabelLaF(jpDetail.jraBrracnt, false);
      } else {
        rcc.setLabelLaF(jpDetail2.jraNateret, false);
        rcc.setLabelLaF(jpDetail2.jraBrracnt, false);
      }
    } else if (mode == 'N') {

    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      this.getRaQueryDataSet().setString("APP",app);
      this.getRaQueryDataSet().setString("KNJIG",knjigovodstvo);
      this.getRaQueryDataSet().setString("CKEY",ckey);
      getRaQueryDataSet().setShort("RBR",setRBRVirmani(app, knjigovodstvo, ckey));
      getRaQueryDataSet().setTimestamp("DATUMIZV", today);
      getRaQueryDataSet().setTimestamp("DATUMPR", today);
      jpDetail.jcbPrijenos.setSelected(true);
      jpDetail.jcbHitnost1.setSelected(false);
      jpDetail.jcbHitnost2.setSelected(false);
//      jpDetail.cbAction("P");
      if (isNewVir()) getRaQueryDataSet().setString("JEDZAV", "NNDNN");
      jpDetail.jraNateret.requestFocus();
    } else if (mode == 'I') {
      if (isNewVir()) {
        jpDetail.resetCheckboxes();
        jpDetail.jraUkorist.requestFocus();
        //jpDetail.jraUkorist.selectAll();
      } else {
        jpDetail2.jraUkorist.requestFocus();
        //jpDetail2.jraUkorist.selectAll();
      }
    }
  }

  public static boolean isNewVir(){
    return hr.restart.sisfun.frmParam.getParam("zapod","newvir","D","Novi virmani (D/N)").equals("D");
  }

  public boolean Validacija(char mode) {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(getRaQueryDataSet());
    if (isNewVir()) {
      if (vl.isEmpty(jpDetail.jraSvrha) || 
          vl.isEmpty(jpDetail.jraBrracuk) || vl.isEmpty(jpDetail.jraPnbo2)) {
        return false;
      }
      if (chkIsEmptyIznos(jpDetail.jraIznos)) return false;
          
    } else {
      if (vl.isEmpty(jpDetail2.jraSvrha) ||  
      vl.isEmpty(jpDetail2.jraBrracuk) || vl.isEmpty(jpDetail2.jraPnbo2)) {
        return false;
      }
      if (chkIsEmptyIznos(jpDetail2.jraIznos)) return false;
    }

    if (mode == 'N' && notUnique(getRaQueryDataSet().getString("APP"), getRaQueryDataSet().getString("KNJIG"),
                                 getRaQueryDataSet().getString("CKEY"), getRaQueryDataSet().getShort("RBR"))) {
      JOptionPane.showConfirmDialog(getRaDetailPanel(),"Zapis postoji !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }

    if(getRaQueryDataSet().getString("PNBZ2").length()>22) {
      JOptionPane.showConfirmDialog(getRaDetailPanel(),"Poziv na broj (zaduž)2 ve\u0107i od 22 karaktera!", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      if (isNewVir()) jpDetail.jraPnbz2.requestFocus();
      else jpDetail2.jraPnbz2.requestFocus();
      return false;
    }

    if(getRaQueryDataSet().getString("PNBO2").length()>22) {
      JOptionPane.showConfirmDialog(getRaDetailPanel(),"Poziv na broj (odobr)2 ve\u0107i od 22 karaktera!", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      if (isNewVir()) jpDetail.jraPnbo2.requestFocus();
      else jpDetail2.jraPnbo2.requestFocus();
      return false;
    }

    if(getRaQueryDataSet().getBigDecimal("IZNOS").compareTo(new BigDecimal("99999999999.99"))>0) {
      JOptionPane.showConfirmDialog(getRaDetailPanel(),"Maksimalni iznos je 99.999.999.999,99!", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      if (isNewVir()) jpDetail.jraIznos.requestFocus();
      else jpDetail2.jraIznos.requestFocus();
      return false;
    }

//    String rezultat = replaceReturn(getRaQueryDataSet().getString("UKORIST"));
//    String rezultat2 = replaceReturn(getRaQueryDataSet().getString("NATERET"));

//    System.out.println("is wrong "+ isWrongLongness(getRaQueryDataSet().getString("UKORIST")));
//    System.out.println("is wrong "+ isWrongLongness(getRaQueryDataSet().getString("NATERET")));

    if (isWrongLongness(getRaQueryDataSet().getString("UKORIST"))) { //(rezultat2.substring(0,rezultat2.indexOf(" "))).length() >= 20 ){
      JOptionPane.showConfirmDialog(getRaDetailPanel(),
                                    new raMultiLineMessage(new String[] {"Polje PRIMATELJ sadrži rijeè sa 20 ili više","znakova, što je nedozvoljeno","","Uputa - dodati razmak prije i/ili poslje crtice, toèke..."}),
                                    "Greška",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
      jpDetail.jraNateret.requestFocus();
      return false;
    }

    if (isWrongLongness(getRaQueryDataSet().getString("NATERET"))) { //(rezultat.substring(0,rezultat.indexOf(" "))).length() >= 20 ){
      JOptionPane.showConfirmDialog(getRaDetailPanel(),
                                    new raMultiLineMessage(new String[] {"Polje PLATITELJ sadrži rijeè sa 20 ili više","znakova, što je nedozvoljeno","","Uputa - dodati razmak prije i/ili poslje crtice, toèke..."}),
                                    "Greška",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
      jpDetail.jraUkorist.requestFocus();
      return false;
    }
    return true;
  }

  /**
   * @param jraIznos
   * @return false da prodje dalje, true zaustavlja
   */
  private boolean chkIsEmptyIznos(JraTextField jraIznos) {
    if (!vl.chkIsEmpty(jraIznos)) {
     return false;
    }
    int answ = JOptionPane.showOptionDialog(this, "Iznos nije upisan !!! Snimiti bez iznosa?","Pozor!",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    if (answ == JOptionPane.YES_OPTION) {
      return false;
    }
    return true;
  }

  private boolean isWrongLongness(String initializer){
    String massmed = replaceReturn(initializer);
    StringTokenizer token = new java.util.StringTokenizer(massmed," ");
    String tmp = "";
    do {
      tmp = token.nextToken();
      if (tmp.length() >=20) return true;
    } while (token.hasMoreTokens());
    return false;
  }

  private String replaceReturn(String initStr) {
    initStr = initStr.trim();
    int i = initStr.indexOf("\n");
    if(i>0 && i!= (initStr.length()-1))
      return replaceReturn(initStr.substring(0, i)+" "+ initStr.substring(i+1, initStr.length()));
    else if(i>0)
      return replaceReturn(initStr.substring(0, i)+" "+ initStr.substring(i+1, initStr.length()));
    return initStr;
  }



//*** init metoda

  private void jbInit() throws Exception {
    // velicina, labela, pozicioniranje
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-655;  // -655
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-495;
    this.setLocation((int)x/2,(int)y/2);
//    this.setSize(555,495);  // proba
    this.setSize(655,495); // orginal
    this.setTitle("Virmani/Diskete");
    this.setRaQueryDataSet(filteredQds);
    this.setVisibleCols(new int[] {4, 6, 17});
    // vis cols, add, layout
    jpSel.setLayout(xYLayout1);
    jButton1.setText("jButton1");
    jlPresCKey.setText("Identifikator");
    xYLayout1.setWidth(280);
    xYLayout1.setHeight(50);
    jpDetail2 = new jpVirmani(this); /** @todo this is old */
    jpDetail = new jpVirmani2(this); /** @todo proba Virmnai2 */
    today = vl.getToday();
    jpSel.add(jlrCKey,   new XYConstraints(150, 15, 100, -1));
    jpSel.add(jButton1,   new XYConstraints(255, 16, 21, 21));
    jpSel.add(jlPresCKey,   new XYConstraints(15, 15, -1, -1));
// bind
//    this.setRaQueryDataSet(filteredQds);
//    this.setVisibleCols(new int[] {4, 6, 17});

    if (isNewVir()) this.setRaDetailPanel(jpDetail);
    else this.setRaDetailPanel(jpDetail2);

    jlrCKey.setText("Osnova osiguranja");
    jlrCKey.setColumnName("CKEY");
    jlrCKey.setNavColumnName("CKEY");
    jlrCKey.setDataSet(getRaQueryDataSet());
    jlrCKey.setVisCols(new int[] {0});
    jlrCKey.setSearchMode(0);
    jlrCKey.setRaDataSet(getIdentifikator());
    jlrCKey.setNavButton(jButton1);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        knjigovodstvo = OrgStr.getOrgStr().getKNJCORG();
      }
    });
  }

//*** metode za automatsko setiranje PK, dodavanje virmana i snimanje virmana
/**
 * @setKeys
 * @param p1 aplikacija
 * @param p2 knjigovodstvo
 * @param p3 ckey
 */
  public void setKeys(String p1, String p2, String p3) {
    app = p1;
    knjigovodstvo = p2;
    ckey= p3;
  }

  /**
   * @add
   * @param p1 Jedinica zavoda
   * @param p2 Na teret racuna
   * @param p3 Svrha doznake
   * @param p4 U korist racuna
   * @param p5 Broj racuna na teret
   * @param p6 Nacin izvrs
   * @param p7 Poziv na broj (zaduz.) 1
   * @param p8 Poziv na broj (zaduz.) 2
   * @param p9 Sifra 1
   * @param p10 Sifra 2
   * @param p11 Sifra 3
   * @param p12 Broj racuna u korist
   * @param p13 Poziv na broj (odobr.) 1
   * @param p14 Poziv na broj (odobr.) 2
   * @param p15 Iznos
   * @param p16 Mjesto
   * @param p17 Datum izvrsenja
   * @param p18 Datum predaje
   */
  public void add(String p1, String p2, String p3, String p4, String p5,
                  String p6, String p7, String p8, String p9, String p10,
                  String p11, String p12, String p13, String p14, BigDecimal p15,
                  String p16, Timestamp p17,  Timestamp p18)
  {
    if(p15==null) p15 = new BigDecimal(0);
    if(p3==null) p3 = "";
    if(p4==null) p4 = "";
    if(p14==null) p14 = "";
    if((p3.equals("")) && sv==false) {
      msg.add("Obavezan unos polja \"Svrha\"");
      sv =true;
    }
    if((p4.equals("")) && rac==false) {
      msg.add("Obavezan unos polja \"U korist ra\u010Duna\"");
      rac = true;
    }
    if((p14.equals("")) && pb==false) {
      msg.add("Obavezan unos polja \"Poziv na broj (odobr.) 2\"");
      pb = true;
    }
    if((p14.length()>22) && pnbo2==false) {
      msg.add("Dužina polja \"Poziv na broj (odobr.) 2\" ve\u0107a od 22 znaka");
      pnbo2 = true;
    }
    if((p8.length()>22) && pnbz2==false) {
      msg.add("Dužina polja \"Poziv na broj (zad.) 2\" ve\u0107a od 22 znaka");
      pnbz2 = true;
    }
    if(((p15.compareTo(new BigDecimal(0)))==0) && iz==false) {
      msg.add("Obavezan unos polja \"Iznos\"");
      iz=true;
    }
    if(brRacNT == null && (p5==null || p5.equals(""))) {
      brRacNT = getZiro(1);
    }
    else if(p5 != null) {
      if(!p5.equals(""))
        brRacNT = p5;
    }

    getRaQueryDataSet().insertRow(false);
    getRaQueryDataSet().setString("APP", app);
    getRaQueryDataSet().setString("KNJIG", knjigovodstvo);
    getRaQueryDataSet().setString("CKEY", ckey);
    if(addIdx == 0) {
      rbr = setRBRVirmani(app, knjigovodstvo, ckey);
      addIdx++;
    } else {
      rbr +=(short)1;
      addIdx++;
    }

    getRaQueryDataSet().setShort("RBR", rbr);
    getRaQueryDataSet().setString("JEDZAV", getAppr(p1,"JEDZAV"));
    getRaQueryDataSet().setString("NATERET", getAppr(p2,"NATERET"));
    getRaQueryDataSet().setString("SVRHA", getAppr(p3,"SVRHA"));
    getRaQueryDataSet().setString("UKORIST", getAppr(p4,"UKORIST"));
    getRaQueryDataSet().setString("BRRACNT", getAppr(brRacNT,"BRRACNT"));
    getRaQueryDataSet().setString("NACIZV", getAppr(p6,"NACIZV"));
    getRaQueryDataSet().setString("PNBZ1", getAppr(p7,"PNBZ1"));
    getRaQueryDataSet().setString("PNBZ2", getAppr(p8,"PNBZ2"));
    getRaQueryDataSet().setString("SIF1", getAppr(p9,"SIF1"));
    getRaQueryDataSet().setString("SIF2", getAppr(p10,"SIF2"));
    getRaQueryDataSet().setString("SIF3", getAppr(p11,"SIF3"));
    getRaQueryDataSet().setString("BRRACUK", getAppr(p12,"BRRACUK"));
    getRaQueryDataSet().setString("PNBO1", getAppr(p13,"PNBO1"));
    getRaQueryDataSet().setString("PNBO2", getAppr(p14,"PNBO2"));
    getRaQueryDataSet().setBigDecimal("IZNOS", p15);
    getRaQueryDataSet().setString("MJESTO", getAppr(p16,"MJESTO"));
    getRaQueryDataSet().setTimestamp("DATUMIZV", p17);
    getRaQueryDataSet().setTimestamp("DATUMPR", p18);
    getRaQueryDataSet().post();
  }

  /**
   * Ako je String p duzi od duljine STRING polja u getRaQueryDataSet()-u skrati ga za vugla
   * @param p12
   * @param string
   * @return
   */
  public String getAppr(String p, String coln) {
    try {
//      System.out.println("getAppr p="+p);
      int prec = getRaQueryDataSet().getColumn(coln).getPrecision();
      int len = p.length();
//      System.out.println("        p:len="+len);
//      System.out.println("        p:prec="+prec);
      if (len > prec) {
        String ap = p.substring(0,prec);
//        System.out.println("        returning "+ap);
//        System.out.println("        (len = "+ap.length());
        return ap;
      }
    } catch (Exception e) {
      e.printStackTrace();      
    }
    return p;
  }

  public void save() {
    if(!msg.isEmpty()) {
      String greske="";
      for(int i = 0;i<msg.size();i++)
        greske += msg.get(i).toString()+"\n";
      JOptionPane.showConfirmDialog(null, "Akcija nije uspjela !\n"+greske, "Greška !", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      getRaQueryDataSet().deleteRow();
      msg.clear();
      addIdx =0;
      return;
    }
    addIdx =0;
    getRaQueryDataSet().saveChanges(); /** @todo a transakcija ??? */
  }

//*** moje metode za manualno dodavanje virmana

  public String[] getSvrha(String filter) {
    String dodatak="";
    if(!filter.equals("")) {
        String filterLower = filter.toLowerCase();
        String filterUpper = filter.toUpperCase();
        String filterProp = filter.substring(0,1).toUpperCase()+filter.substring(1, filter.length()).toLowerCase();
      dodatak = " where svrha like '"+filterLower+"%' or svrha like '"+filterUpper+"%' or svrha like '"+filterProp+"%' AND knjig='"+knjigovodstvo+"'";
    }
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    java.awt.Frame dest = new java.awt.Frame();
    String qStr = "select * from Virmani"+dodatak; //select distinct svrha from virmani"+dodatak;
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(dm.getVirmani().cloneColumns());
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    String[] result = new String[]{};
    qds.open();
    qds.setTableName("GETSVRHA");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp(dest, qds, new int[] {14,6,20,17});
    return result;
  }

  public String getZiro(int param) {
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    java.awt.Frame dest = new java.awt.Frame();
    String[] result = new String[]{};
    vl.RezSet = hr.restart.zapod.OrgStr.getOrgStr().getKnjigziro(hr.restart.zapod.OrgStr.getKNJCORG());
    vl.RezSet.open();
    vl.RezSet.setTableName("GETZIRO");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp(dest, vl.RezSet, new int[] {1,3});
    if(param==1) {
      if(result == null) {
        JOptionPane.showConfirmDialog(dest, "Obavezan odabir broja ra\u010Duna na teret !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        return getZiro(param);
      }
    } else {
      if(result == null) {
        return "";
      }
      setNaTeretDep(result[3], "", 0);
    }
    return result[3];
  }

  public String getZiroPar() {
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    java.awt.Frame dest = new java.awt.Frame();
    String[] result = new String[]{};
    vl.RezSet = hr.restart.gk.gkQuerys.getParZiro();
    vl.RezSet.open();
    vl.RezSet.setTableName("GETZIROPAR");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp(dest, vl.RezSet, new int[] {0,1});
    if(result == null) {
      return "";
    }
    return result[0];
  }

  public String getNaTeret(String filter, String kPressed) {
    int filIdx = filter.indexOf("\n");
    if(filIdx > 0)
      filter = filter.substring(0,filIdx);
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    java.awt.Frame dest = new java.awt.Frame();
    String[] result = new String[]{};
    if(kPressed.equals("F8")) {
      if(filter.equals(""))
//        vl.RezSet = dm.getOrgstruktura();
        if (hr.restart.sisfun.frmParam.getParam("zapod","orgsvir","N","Dohvat svih OrgStr na F8").equals("D"))
          vl.RezSet = hr.restart.baza.Orgstruktura.getDataModule().getTempSet();//.getFilteredDataSet("CORG='"+knjigovodstvo+"'");
        else
          vl.RezSet = hr.restart.baza.Orgstruktura.getDataModule().getTempSet("CORG='"+knjigovodstvo+"'");
      else
        if (hr.restart.sisfun.frmParam.getParam("zapod","orgsvir","N","Dohvat svih OrgStr na F8").equals("D"))
          vl.RezSet = hr.restart.baza.Orgstruktura.getDataModule().getTempSet("NAZIV LIKE '"+filter+"%'");// AND CORG='"+knjigovodstvo+"'");
        else 
          vl.RezSet = hr.restart.baza.Orgstruktura.getDataModule().getTempSet("NAZIV LIKE '"+filter+"%' AND CORG='"+knjigovodstvo+"'");
    } else if(kPressed.equals("F9")) {
      if(filter.equals(""))
        vl.RezSet = getMyDSVirmani("","NT");
      else
        vl.RezSet = getMyDSVirmani(filter,"NT");
    }
    vl.RezSet.open();
    vl.RezSet.setTableName("GETNATERET");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp(dest, vl.RezSet, new int[] {1,2,3,4});
    if(result == null) {
      return "";
    }
    if(result[3].equals(""))
      return "";
    setNaTeretDep(result[7], result[4], 1);
    return (result[3]+"\n"+result[5]+", "+result[6]+" "+result[4]);
  }

  public String[] getUKorist(String filter, String kPressed) {
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    java.awt.Frame dest = new java.awt.Frame();
    String[] result = new String[]{};
    if(kPressed.equals("F8")) {
      if(filter.equals(""))
        vl.RezSet = dm.getPartneri();
      else{
        String filterLower = filter.toLowerCase();
        String filterUpper = filter.toUpperCase();
        String filterProp = filter.substring(0,1).toUpperCase()+filter.substring(1, filter.length()).toLowerCase();
        vl.RezSet = hr.restart.baza.Partneri.getDataModule().getTempSet("(NAZPAR LIKE '"+filterLower+"%' or nazpar like '"+filterUpper+"%' or nazpar like '"+filterProp+"%')");
      }
      vl.RezSet.open();
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(vl.RezSet);
      vl.RezSet.setTableName("GETUKORIST");
      lookupData LD = lookupData.getlookupData();
      result = LD.lookUp(dest, vl.RezSet, new int[] {1,2,3,4}); /** @todo ovo je simulacija lookupa */
      if(result == null) {
        return new String []{
          "","","","","","","","","","",
          "","","","","","","","","","",
          ""
        };
      }
      if(result[3].equals(""))
        return new String []{
          "","","","","","","","","","",
          "","","","","","","","","","",
          ""
        };
      setUKoristDep(result[9]);
      return result;//(result[3]+"\n"+result[5]+", "+result[6]+" "+result[4]);

    } else if (kPressed.equals("F9")) {
      if(filter.equals("")){
//        vl.RezSet = dm.getVirmani();
        String filterDodatak = "knjig = '" + OrgStr.getKNJCORG()+"'";
//        System.out.println("OrgStr.getKNJCORG() = " + OrgStr.getKNJCORG()+"");
        vl.RezSet = hr.restart.baza.Virmani.getDataModule().getTempSet(filterDodatak);
      } else {
        String filterLower = filter.toLowerCase();
        String filterUpper = filter.toUpperCase();
        String filterProp = filter.substring(0,1).toUpperCase()+filter.substring(1, filter.length()).toLowerCase();
        String filterDodatak = "(ukorist like '"+filterLower+"%' or ukorist like '"+filterUpper+"%' or ukorist like '"+filterProp+"%') AND knjig='"+knjigovodstvo+"'";
        vl.RezSet = hr.restart.baza.Virmani.getDataModule().getTempSet(filterDodatak);
      }
    }
    vl.RezSet.open();
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(vl.RezSet);
    vl.RezSet.setTableName("GETUKORISTVIR");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp(dest, vl.RezSet, new int[] {14,6,20,17}); /** @todo ovo je simulacija lookupa */
    if(result == null) {
      return new String []{
          "","","","","","","","","","",
          "","","","","","","","","","",
          ""
        };
    }
    if(result[3].equals(""))
      return new String []{
          "","","","","","","","","","",
          "","","","","","","","","","",
          ""
        };
//    setUKoristDep(result[9]);
    return result;//(result[3]+"\n"+result[5]+", "+result[6]+" "+result[4]);
  }
  //----> Dependencies

  private void setNaTeretDep(String ziro, String mjesto, int p) {
    StringTokenizer st = new StringTokenizer(ziro,"-");
    String rez= "";
    try {
      rez = st.nextToken();
    } catch (Exception ex) {}
    if (!isNewVir()) {
      this.getRaQueryDataSet().setString("JEDZAV", rez);
    }
    if(p!=0) {
      this.getRaQueryDataSet().setString("BRRACNT", ziro);
      this.getRaQueryDataSet().setString("MJESTO", mjesto);
    }
  }

  private void setUKoristDep(String ziro) {
    this.getRaQueryDataSet().setString("BRRACUK", ziro);
  }

//*** overridana show metoda

  public void show() {
//    if(idShowed) return;
    if((getIdentifikator().getRowCount()==0 && ckey==null) || (ckey==null && app.equals("zapod"))) {
      identifikator i = new identifikator(this);
      i.show();
    } else if(ckey==null || ckey.equals("")) {
      pres.showPreselect(this,"Klju\u010D virmana");
      pres.getSelDataSet().open();
      String appNew = pres.getSelDataSet().getString("app");
      String ckeyNew = pres.getSelDataSet().getString("ckey");
      String knjigNew = pres.getSelDataSet().getString("knjig");
      if(filteredQds.isOpen()) filteredQds.close();
      filteredQds.setQuery(new QueryDescriptor(dm.getDatabase1(),"Select * from virmani where app='"+
          appNew+"' and ckey='"+ckeyNew+"' and knjig='"+knjigNew+"'"));
      filteredQds.open();
    } else {
      if(filteredQds.isOpen()) filteredQds.close();
      filteredQds.setQuery(new QueryDescriptor(dm.getDatabase1(),"Select * from virmani where app='"+app+"' and ckey='"+ckey+"'"));
      filteredQds.open();
      super.show();
      idShowed=false;
    }
  }

//*** overridana this_hide metoda
  public void this_hide() {
    ckey = null;
    super.this_hide();
  }

//*** moje metode koje koriste SQL -> potrebno ubaciti u util
  public short setRBRVirmani(String app, String knjig, String ckey) {
    String qStr = "select max(rbr) as rbr from virmani where app='"+app+"' and knjig='"+knjig+"' and ckey='"+ckey+"'";
    QueryDataSet qds = Util.getNewQueryDataSet(qStr);
    if(qds.getRowCount()>0)
      return (short)(qds.getShort("rbr") + 1);
    else
      return 1;
  }

  public QueryDataSet getMyDSVirmani(String filter, String DS) {
    int filIdx = filter.indexOf("\n");
    if(filIdx>0)
      filter = filter.substring(0,filIdx);
    String tempQStr="";
    if(DS.equals("UK")) {
      if(filter.equals(""))
        tempQStr = "select ukorist as uvjet from virmani where knjig='"+knjigovodstvo+"'";
      else
        tempQStr = "select ukorist as uvjet from virmani where ukorist like '"+filter+"%' and knjig='"+knjigovodstvo+"'";
    } else if(DS.equals("NT")) {
      if(filter.equals(""))
        tempQStr = "select nateret as uvjet from virmani where knjig='"+knjigovodstvo+"'";
      else
        tempQStr = "select nateret as uvjet from virmani where nateret like '"+filter+"%' and knjig='"+knjigovodstvo+"'";
    }
//    System.out.println("tempStr : "+ tempQStr);
    QueryDataSet tempDS  = Util.getNewQueryDataSet(tempQStr);
    int i = 0;
    LinkedList naz = new LinkedList();
    QueryDataSet qds = new QueryDataSet();
//    System.out.println("QSTR: " + tempQStr);
    tempDS.open();
    while(tempDS.inBounds()) {
      i = tempDS.getString("UVJET").indexOf("\n");
      if(i>0)
        naz.add(tempDS.getString("UVJET").substring(0, i));
      else
        naz.add(tempDS.getString("UVJET"));
      tempDS.next();
    }
    String in = "('";
    for (int k = 0;k<naz.size();k++) {
      if(k<naz.size()-1)
        in += naz.get(k).toString()+"', '";
      else
        in += naz.get(k).toString()+"')";
//      System.out.println("naz["+k+"]: " + naz.get(k));
    }
    if(DS.equals("UK")) {
      if(naz.size()>0)
        qds = hr.restart.baza.Partneri.getDataModule().getTempSet("nazpar in" + in);
      else
        qds = hr.restart.baza.Partneri.getDataModule().getTempSet("1=0");
    } else if (DS.equals("NT")) {
      if(naz.size()>0)
        qds = hr.restart.baza.Orgstruktura.getDataModule().getTempSet("naziv in" + in);
      else
        qds = hr.restart.baza.Orgstruktura.getDataModule().getTempSet("1=0");
    }
    qds.open();
    return qds;
  }

  public QueryDataSet getIdentifikator() {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select distinct ckey as ckey from virmani where app='"+app+"'";
    qds.setColumns(new Column[]{
      (Column)dm.getVirmani().getColumn("CKEY").clone()
    });
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds;
  }

  public boolean notUnique(String app, String knjig, String ckey, short rbr) {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select * from virmani where app = '"+app+"' and knjig='"+knjig+"' and ckey='"+ckey+"' and rbr="+rbr;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
    qds.open();
    if(qds.getRowCount()>0)
      return true;
    return false;
  }

  // provjera ziro racuna
  public boolean provjeraZR(String zr) {
    boolean ok = true;
    StringTokenizer st = new StringTokenizer(zr,"-");
    while(st.hasMoreTokens()) {
      try {
        Integer digit = new Integer(st.nextToken());
      }
      catch (Exception ex) {
        ok = false;
      }
    }
    return ok;
  }

  // overridana prn metoda koa pokrece novi thread i u njemu radi sve setinge, zbog potrebe za
  // upozoravajucim ekranom
  public void rnvPrint_action() {
    selectedRow = getRaQueryDataSet().getRow();
    setPrnSets();
  }

  private void setPrnSets() {
    Thread sets = new Thread() {
      public void run() {
        raDelayWindow proc = raDelayWindow.show(frmVirmani.this.getWindow(), "Priprema podataka u tijeku !").setModal(true);
        setKeysArray();
        getJpTableView().enableEvents(false);

        try {
          getRepRunner().removeReport("hr.restart.zapod.repDiskZap");
          /*getRepRunner().removeReport("hr.restart.zapod.repIspVir");
          getRepRunner().removeReport("hr.restart.zapod.repIspVir2");*/
          getRepRunner().removeReport("hr.restart.zapod.repIspVirNewSmall");
          getRepRunner().removeReport("hr.restart.zapod.repIspVirNewSmall2");
          getRepRunner().removeReport("hr.restart.zapod.repIspVirNew");
          getRepRunner().removeReport("hr.restart.zapod.repIspVirNew2");
          getRepRunner().removeReport("hr.restart.zapod.repVirman");
          getRepRunner().removeReport("hr.restart.zapod.repVirmani");
          getRepRunner().removeReport("hr.restart.zapod.repVir3A");
          getRepRunner().removeReport("hr.restart.zapod.repVir3Ai");

          getRepRunner().addReport("hr.restart.zapod.repDiskZap", "Datoteka za e-plaæanje");
          getRepRunner().addJasper("hr.restart.zapod.repVir3A", "hr.restart.zapod.rep3Virman", "HUB3A.jrxml", "Ispis odabranog HUB 3A");
          getRepRunner().addJasper("hr.restart.zapod.repVir3Ai", "hr.restart.zapod.rep3Virmani", "HUB3A.jrxml", "Ispis svih HUB 3A");
          /*getRepRunner().addReport("hr.restart.zapod.repIspVir", "Ispis virmana (stari obrazac)");
          getRepRunner().addReport("hr.restart.zapod.repIspVir2", "Ispis svih virmana (stari obrazac)");*/
          getRepRunner().addReport("hr.restart.zapod.repIspVirNewSmall", "Ispis virmana (HUB 1)");
          getRepRunner().addReport("hr.restart.zapod.repIspVirNewSmall2", "Ispis svih virmana (HUB 1)");
          getRepRunner().addReport("hr.restart.zapod.repIspVirNew", "Ispis virmana (HUB 1-1)");
          getRepRunner().addReport("hr.restart.zapod.repIspVirNew2", "Ispis svih virmana (HUB 1-1)");
          getRepRunner().addJasper("hr.restart.zapod.repVirman", "hr.restart.zapod.repVirman", "virman.jrxml", "Laserski ispis virmana (HUB 1-1)");
          getRepRunner().addJasper("hr.restart.zapod.repVirmani", "hr.restart.zapod.repVirmani", "virman.jrxml", "Laserski ispis svih virmana (HUB 1-1)");
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }


        proc.close();
        getJpTableView().enableEvents(true);
        getRaQueryDataSet().goToRow(selectedRow); //-> enableEvents(false) me ne jebe 5%, pa se vracam na selektirani red ovak'
//        sysoutTEST syst = new sysoutTEST(false);
//        syst.prn(getRaQueryDataSet());
        frmVirmani.super.rnvPrint_action();
      }
    };
    sets.start();
  }

  private void setKeysArray() {
    PKs = new String[]{getRaQueryDataSet().getString("APP"), getRaQueryDataSet().getString("KNJIG"),
      getRaQueryDataSet().getString("CKEY"),getRaQueryDataSet().getShort("RBR")+""};
  }
  
  public String handleEmptyStr(String str, int i) {
    if(str.equals("-") || str.equals(".") || str.equals("0"))
      return "";
    if(i==1)
      return str+", ";
    return str;
  }

//*** inner klasa koja se po potrebi poziva iz show metode -> za kreiranje identifikatora
  public class identifikator extends raUpitLite {
    hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
    TableDataSet tds = new TableDataSet();
    JraTextField godina = new JraTextField();
    JraTextField rbr = new JraTextField();
    JPanel jp = new JPanel();
    JLabel jlDatum = new JLabel();
    JLabel jlRbr = new JLabel();
    XYLayout xYLayout1 = new XYLayout();
    frmVirmani f;
    boolean escPressed = false;
    public identifikator(frmVirmani fvir) {
      this.f = fvir;
      try {
        jbInit();
      }
      catch (Exception ex) {}
    }

    private void jbInit() throws Exception {
      this.setSize(new Dimension(280,130));
      int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-350;
      int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-130;
      this.setLocation((int)x/2,(int)y/2);
      this.setTitle("Identifikator");
      jp.setLayout(xYLayout1);
      jlDatum.setText("Datum pla\u0107anja");
      jlRbr.setText("Broj grupe");
      xYLayout1.setWidth(180);
      xYLayout1.setHeight(45);
      tds.setColumns(new Column[] {dM.createTimestampColumn("datum"), dM.createIntColumn("rbr")});
      godina.setDataSet(tds);
      godina.setColumnName("datum");
      godina.setHorizontalAlignment(SwingConstants.CENTER);
      rbr.setDataSet(tds);
      rbr.setColumnName("rbr");
      rbr.setHorizontalAlignment(SwingConstants.RIGHT);
      jp.add(jlDatum,    new XYConstraints(15, 15, -1, -1));
      jp.add(jlRbr,    new XYConstraints(15, 40, -1, -1));
      jp.add(godina,     new XYConstraints(150, 15, 100, 20));
      jp.add(rbr,     new XYConstraints(150, 40, 100/*50*/, 20));
      this.setJPan(jp);
//      hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
//          new hr.restart.zapod.raKnjigChangeListener() {
//        public void knjigChanged(String oldKnjig,String newKnjig) {
//        }
//      });
    }

    public void componentShow(){
      tds.setTimestamp("datum", val.getToday());
      tds.setInt("rbr", 1);
      hr.restart.sisfun.raUser.getInstance().getUser();
      escPressed = false;
      SwingUtilities.invokeLater(new Runnable(){
        public void run() {
          godina.selectAll();
        }
      });
    };

    public void firstESC(){
      tds.setInt("rbr",1);
      godina.requestFocus();
      escPressed = true;
    }

    public boolean runFirstESC() {
      if(!escPressed)
        return true;
      return false;
    }

    public boolean Validacija() {
      return true;
    }

    public void okPress() {
      ckey = /*hr.restart.zapod.*/OrgStr.getOrgStr().getKNJCORG()+"-"+
             hr.restart.sisfun.raUser.getInstance().getUser()+"-"+
             godina.getText()+"-"+
             tds.getInt("RBR");
      f.idShowed=true;
      this.hide();
      f.show();
      return;
    }
    public DataSet getData() {
    	return tds;
    }
    public boolean isIspis() {
      return false;
    }
  }

  public DataSet get3DataSet() {
    StorageDataSet vir3set = new StorageDataSet();
    Column[] rqdCols = getRaQueryDataSet().getColumns();
    for (int i = 0; i < rqdCols.length; i++) {
      for (int j = 1; j < 4; j++) {
        vir3set.addColumn(dM.createColumn(rqdCols[i].getColumnName()+"_"+j, rqdCols[i].getCaption(), rqdCols[i].getDefault(), rqdCols[i].getDataType(), rqdCols[i].getSqlType(), rqdCols[i].getPrecision(), rqdCols[i].getScale()));
      }
    }
    vir3set.open();
    System.out.println(VarStr.join(vir3set.getColumnNames(vir3set.getColumnCount())," # "));
    String[] cnms = getRaQueryDataSet().getColumnNames(getRaQueryDataSet().getColumnCount());
    int x = 4;
    for (getRaQueryDataSet().first(); getRaQueryDataSet().inBounds(); getRaQueryDataSet().next()) {
      if (x>3) {
        vir3set.post();
        vir3set.insertRow(false);
        x = 1;
      }
      for (int i = 0; i < cnms.length; i++) {
        Variant v = new Variant();
        getRaQueryDataSet().getVariant(cnms[i], v);
        vir3set.setVariant(cnms[i]+"_"+x, v);
      }
      x++;
    }
    return vir3set;
  }
  
}