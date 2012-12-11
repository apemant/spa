/****license*****************************************************************
**   file: frmIspList.java
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
package hr.restart.pl;

import hr.restart.baza.Radnici;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.MathEvaluator;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repNaljepnice;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmIspList extends frmIzvjestajiPL {
  lookupData ld = lookupData.getlookupData();
  JPanel jpr = new JPanel();
  XYLayout xyLayLady = new XYLayout();

  JTextField imerOd = new JTextField();
  JTextField imerDo = new JTextField();

  JLabel jlCradnikOd = new JLabel();
  JraTextField jlrPrezime = new JraTextField();
  JraTextField jlrIme = new JraTextField();
  JraTextField jlrPrezime2 = new JraTextField();
  JraTextField jlrIme2 = new JraTextField();

  JLabel jlCradnikDo = new JLabel();

  JraButton jbSelCradnikOd = new JraButton();
  JlrNavField jlrCradnikOd = new JlrNavField() {
    public void after_lookUp() {
      imerOd.setText(" "+jlrIme.getText()+" "+jlrPrezime.getText());
      if (jlrCradnikDo.getText().equals("")) {
        jlrCradnikDo.setText(jlrCradnikOd.getText());
        jlrCradnikDo.forceFocLost();
      }
    }
    public boolean isNavValuesChanged() {
      return super.isValueChanged(); /** @todo ovo do dalnjega */
    }
  };
  JraButton jbSelCradnikDo = new JraButton();
  JlrNavField jlrCradnikDo = new JlrNavField() {
    public void after_lookUp() {
      imerDo.setText(" "+jlrIme2.getText()+" "+jlrPrezime2.getText());
    }
    public boolean isNavValuesChanged() {
      return super.isValueChanged(); /** @todo ovo do dalnjega */
    }
  };
  JraCheckBox jrcbPrikaz = new JraCheckBox();

  QueryDataSet corgradnici = new QueryDataSet();
  String oldcorg = "";

  StorageDataSet range = new StorageDataSet();
  Column CRADOD = new Column();
  Column CRADDO = new Column();

  QueryDataSet SQLradnici = new QueryDataSet();
  QueryDataSet SQLradnicicorg = new QueryDataSet();

  QueryDataSet SQLprimanja = new QueryDataSet();
//  QueryDataSet SQLdoprinosi = new QueryDataSet();
  QueryDataSet SQLporezi = new QueryDataSet();
  QueryDataSet SQLkrediti = new QueryDataSet();

  QueryDataSet vrprim, vrodb, radpl, radmj;

  static frmIspList this_frm;

  public frmIspList() {
    this('O');
  }

  public frmIspList(char mode) {
    super(mode);
    isArh = mode == 'A'; // <- OVO!!
//    System.out.println("KONSTRUKTOR isArh : " + isArh);

   if (!isArh){
      this_frm = this;
      // addReports(); // <- OVO!!  // - > zakomentirao Rade: dodaju se reporti na OKPress jer za matri\u010Dni
                                    // ispis ds treba biti napunjen
    }
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  static boolean isArh = false;

  public static frmIspList getInstance() { // <- OVO OVAKO!!
//  System.out.println("getInstance() isArh : " + isArh);
    if (isArh){
//      System.out.println("frmIspListA.getInstanceA() = " + frmIspListA.getInstanceA());
      return frmIspListA.getInstanceA();
    }
    else {
//      System.out.println("this_frm " + this_frm);
      return this_frm;
    }
  }

  private String getTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = " kumulradarh";
    else tableName = " kumulrad";
    return tableName;
  }

  String radrange;
  public void findRadniciSorted() {
    String sql =
        "SELECT radnici.corg, radnici.cradnik, radnici.prezime, radnici.ime, radnici.imeoca, " + getTableName() + ".* "+
        "FROM radnici, " + getTableName() + " WHERE radnici.cradnik = " + getTableName() + ".cradnik AND " + getWhereQuery(isArh?getTableName():"radnici") + radrange +
        " ORDER BY radnici.prezime, radnici.ime ";//+vl.getCollateSQL(); /// PROXIMITY WARNING
    System.out.println("AaAa : " + sql);
    SQLradnici.close();
    SQLradnici.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    SQLradnici.open();
//    System.out.println("table name [1] : " + getTableName());
  }

  public void findRadniciSortedByCorg() {
    String sql =
        "SELECT radnici.corg, radnici.cradnik, radnici.prezime, radnici.ime, radnici.imeoca, " + getTableName() + ".* "+
        "FROM radnici, " + getTableName() + " WHERE radnici.cradnik = " + getTableName() + ".cradnik AND "+getWhereQuery(isArh?getTableName():"radnici")+radrange+
        " ORDER BY radnici.corg, radnici.prezime, radnici.ime"; /// PROXIMITY WARNING
    System.out.println("AaCc : " + sql);
    SQLradnicicorg.close();
    SQLradnicicorg.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    SQLradnicicorg.open();
//    System.out.println("table name [2] : " + getTableName());
  }

  public void okPress() {
    isArh = getRepMode() == 'A'; // <- OVO!!
    if (isArh) {
      raOdbici.getInstance().setObrRange(
          fieldSet.getShort("GODINAOD"),
          fieldSet.getShort("MJESECOD"),
          fieldSet.getShort("RBROD"),
          fieldSet.getShort("GODINADO"),
          fieldSet.getShort("MJESECDO"),
          fieldSet.getShort("RBRDO")
      );
    }
    if (!range.getString("CRADNIKOD").equals("") && !range.getString("CRADNIKDO").equals(""))
      radrange = " AND cast(radnici.cradnik as numeric(6,0)) BETWEEN "+range.getString("CRADNIKOD")+" AND "+range.getString("CRADNIKDO");
    else radrange = "";
    findRadniciSorted();
    findRadniciSortedByCorg();
    dm.getParametripl().open();
    dm.getParametripl().first();
    dm.getVrsteodb().open();
    makeObrListeRad();
    makeStDSradnici();
    fillNaljepnice();
// -> dodao rade
//    if(!isArh)
//    {
//      System.out.println("primanja: " + getPrimanja());
//      System.out.println("rm: " + this.getRadnoMjesto());
//      System.out.println("nrm: " + this.getNazivRadnogMjesta());



      this.killAllReports();
      this.addReports();
      

//      this.addReport("hr.restart.pl.repMxIsplListUk", "Matri\u010Dni ispis isplatnih listi\u0107a");
//    }

//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(fieldSet);
  }

  public void hide(){
    super.hide();

    jlrCradnikOd.setText("*");
    jlrCradnikOd.forceFocLost();
    jlrCradnikOd.setText("");
    jlrCradnikOd.forceFocLost();
//    jlrCradnikOd.emptyTextFields();
//    jlrCradnikOd.after_lookUp();

    jlrCradnikDo.setText("*");
    jlrCradnikDo.forceFocLost();
    jlrCradnikDo.setText("");
    jlrCradnikDo.forceFocLost();
//    jlrCradnikDo.emptyTextFields();
//    jlrCradnikDo.after_lookUp();
  }

  public void componentShow() {
    super.componentShow();
    rcc.setLabelLaF(imerOd, false);
    rcc.setLabelLaF(imerDo, false);
    rcc.setLabelLaF(jrcbPrikaz, true);
    jrcbPrikaz.setSelected(true);

    range.setString("CRADNIKOD","");
    range.setString("CRADNIKDO","");
  }

  public void firstESC(){
    try {
      if (getRepMode() == 'A'){
        rcc.EnabDisabAll(mainPanel, true);
      } else {
        rcc.EnabDisabAll(jPanel1, true);
      }
      rcc.setLabelLaF(imerOd, false);
      rcc.setLabelLaF(imerDo, false);
//      jrcbPrikaz.setSelected(true);
      jlrCorg.requestFocus();
      jlrCorg.selectAll();
      firstesc = false;
    }
    catch (Exception ex) {
    }
  }

  protected void addReports() {
    this.addReport("hr.restart.pl.repIspList", "hr.restart.pl.repIspList", "IspList", "Ispis isplatnih listi\u0107a po abecedi");
    this.addReport("hr.restart.pl.repIspListCorg", "hr.restart.pl.repIspListCorg", "IspList", "Ispis isplatnih listiæa po org. jedinicama");
    addJasper("hr.restart.pl.repIspListNa", "hr.restart.pl.repIspList", "ispListNa.jrxml", "Isplatni listiæ sa svim doprinosima");
    addJasper("hr.restart.pl.repIspListNaCorg", "hr.restart.pl.repIspListCorg", "ispListNa.jrxml", "Isplatni listiæ sa svim doprinosima po org.jed.");
//    this.addReport("hr.restart.pl.repIspListCorg", "Ispis isplatnih listi\u0107a po org. jedinicama",2);
    this.addReport("hr.restart.pl.repPlatjnaLista", "Ispis obra\u010Dunske liste radnika",2);
    this.addReport("hr.restart.pl.repMxIsplListUk", "Matri\u010Dni ispis isplatnih listi\u0107a");
    
//    this.addReport("hr.restart.pl.repIspList", "Ispis isplatnih listi\u0107a po abecedi",2);
    this.addReport("hr.restart.pl.repKoverte", "hr.restart.pl.repIspList", "KovertaL", "Ispis koverti po abecedi");
    this.addReport("nalj13x5", "hr.restart.zapod.repNaljepnice","Naljepnice13x5", "Ispis naljepnica 13 x 5");
  }
  
  
  private void fillNaljepnice() {
    repNaljepnice.clearDataSet();
    int i = 1;
    for (StDSradnici.first(); StDSradnici.inBounds(); StDSradnici.next()) {
      if (i==1) repNaljepnice.getDataSet().insertRow(false);
      repNaljepnice.getDataSet().setString("TXT"+i, StDSradnici.getString("PREZIME")+" "+StDSradnici.getString("IME"));
      repNaljepnice.getDataSet().post();
       if (i==5) {
         i=1;
       } else {
         i=i+1;
       }
    }
  }
  public void beforeReport() {
    super.beforeReport();
    this.getRepRunner().getReport("hr.restart.pl.repKoverte").disableSignature();
    this.getRepRunner().getReport("nalj13x5").disableSignature();
  }
  
  
  private void jbInit() throws Exception {
    jpr.setLayout(xyLayLady);
    xyLayLady.setHeight(50);
    xyLayLady.setWidth(600);

    CRADOD = (Column) dm.getAllRadnici().getColumn("CRADNIK").clone();
    CRADOD.setColumnName("CRADNIKOD");
    CRADDO = (Column) dm.getAllRadnici().getColumn("CRADNIK").clone();
    CRADDO.setColumnName("CRADNIKDO");
    range.setColumns(new Column[] {CRADOD, CRADDO});
    range.open();

    jlCradnikOd.setText("Od radnika");

    jlrCradnikOd.setColumnName("CRADNIKOD");
    jlrCradnikOd.setNavColumnName("CRADNIK");
    jlrCradnikOd.setDataSet(range);
    jlrCradnikOd.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnikOd.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnikOd.setVisCols(new int[] {0, 1, 2});
    jlrCradnikOd.setSearchMode(0);
    jlrCradnikOd.setRaDataSet(corgradnici);
    jlrCradnikOd.setNavButton(jbSelCradnikOd);

    jlrIme.setColumnName("IME");
    jlrIme.setVisible(false);
    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setVisible(false);

    jlCradnikDo.setText("Do radnika");

    rcc.setLabelLaF(imerOd, false);
    rcc.setLabelLaF(imerDo, false);

    jlrCradnikDo.setColumnName("CRADNIKDO");
    jlrCradnikDo.setNavColumnName("CRADNIK");
    jlrCradnikDo.setDataSet(range);
    jlrCradnikDo.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnikDo.setTextFields(new JTextComponent[] {jlrIme2, jlrPrezime2});
    jlrCradnikDo.setVisCols(new int[] {0, 1, 2});
    jlrCradnikDo.setSearchMode(0);
    jlrCradnikDo.setRaDataSet(corgradnici);
    jlrCradnikDo.setNavButton(jbSelCradnikDo);

    jlrIme2.setColumnName("IME");
    jlrPrezime2.setColumnName("PREZIME");

    jpr.add(jlCradnikOd, new XYConstraints(15, 0, -1, -1));
    jpr.add(jlCradnikDo, new XYConstraints(15, 25, -1, -1));
    jpr.add(jlrCradnikOd, new XYConstraints(150, 0, 100, -1));
    jpr.add(jlrCradnikDo, new XYConstraints(150, 25, 100, -1));
    jpr.add(imerOd, new XYConstraints(255, 0, 295, -1));
    jpr.add(imerDo, new XYConstraints(255, 25, 295, -1));
    jpr.add(jbSelCradnikOd, new XYConstraints(555, 0, 21, 21));
    jpr.add(jbSelCradnikDo, new XYConstraints(555, 25, 21, 21));

    jrcbPrikaz.setHorizontalAlignment(SwingConstants.RIGHT);
    jrcbPrikaz.setHorizontalTextPosition(SwingConstants.LEADING);
    jrcbPrikaz.setText("Prikaz isplate");

    vrprim = hr.restart.util.Util.getNewQueryDataSet("SELECT cvrp, naziv, parametri FROM vrsteprim");
    vrodb = hr.restart.util.Util.getNewQueryDataSet("SELECT cvrodb, opisvrodb FROM vrsteodb");
    radpl = hr.restart.util.Util.getNewQueryDataSet("SELECT cradnik, cradmj, brojtek, cisplmj, cvro, jmbg, adresa, copcine, oib, BRUTOSN FROM radnicipl");
    radmj = hr.restart.util.Util.getNewQueryDataSet("SELECT cradmj, nazivrm FROM radmj");
    mainPanel.add(jpr, BorderLayout.CENTER);
    jPanel2.add(jrcbPrikaz, new XYConstraints(475, 25, 100, -1));
  }

  public void aft_lookCorg() {
    super.aft_lookCorg();
    if (!jlrCorg.getText().equals(oldcorg)) {
      jlrCradnikDo.setText("");
      jlrCradnikDo.forceFocLost();
      jlrCradnikOd.setText("");
      jlrCradnikOd.forceFocLost();
//      jlrCradnikOd.requestFocus();
      oldcorg = jlrCorg.getText();
      corgradnici.close();
      Radnici.getDataModule().setFilter(corgradnici,
                                        "radnici.aktiv='D' AND corg in" + orgs.getInQuery(orgs.getOrgstrAndKnjig(oldcorg)));
      corgradnici.open();
    }
  }

  // GETTERI ZA PROVIDERA:: !! yeeeah! - Anteeeeeeeeeeeeeeeeee......


  // Dataset sa svim podacima o radnicima \u010Diji se listi\u0107i
  // ispisuju: section break za svakog
  // sortirani po prezime:ime radnika

  public DataSet getRadnici() {
//    return SQLradnici;
    return StDSradnici;
  }

  public QueryDataSet getVrprim() {
    return vrprim;
  }

  public QueryDataSet getVrodb() {
    return vrodb;
  }

  public QueryDataSet getRadnicipl() {
    return radpl;
  }

  public QueryDataSet getRadmj() {
    return radmj;
  }

  // Dataset kao i gore ali sortiran najprije po corg, pa
  // onda po prezime:ime radnika.

  public DataSet getRadniciCorg() {
//    return SQLradnicicorg;
    return StDSradniciCorg;
  }

  // Ostale metode kao parametar imaju šifru radnika.
  // Treba ih pozivati za svakog radnika s njegovom šifrom
  // da bi se dobio odgovaraju\u0107i dataset sa podacima za
  // doti\u010Dnog radnika. Npr.
  // radnici = frmisplist.getRadnici();
  // onda u petlji:
  // cradnik = radnici.getString("CRADNIK");
  // frmisplist.getPrimanja(cradnik), frmisplist.getDoprinosi(cradnik) itd.


  // U ovom datasetu se nalaze primanja i naknade
  // Slog pripada primanjima ako je (uz prim = frmisplist.getPrimanja(cradnik))
  // raParam.getParam(prim, 1).equals("D") a naknadama ako je
  // raParam.getParam(prim, 1).equals("N") ili
  // raParam.getParam(prim, 1).equals("N").
  public QueryDataSet getPrimanjaSet(String rad) {

//    System.out.println("getPrimanjaSet arh : " + isArh);

    String sql = "SELECT primanja"+(isArh?"arh":"obr")+".*, CAST ((primanja"+(isArh?"arh":"obr")+".BRUTO - primanja"+(isArh?"arh":"obr")+".DOPRINOSI) as numeric(15,2)) AS DOHODAK, vrsteprim.naziv FROM primanja"+(isArh?"arh":"obr")+", vrsteprim "+
                 "WHERE cradnik = '"+rad+"' AND primanja"+(isArh?"arh":"obr")+".cvrp = vrsteprim.cvrp "+
                 getBetweenAhrQuery("primanjaarh") +" "+
                 "ORDER BY "+(isArh?"primanjaarh.godobr, primanjaarh.mjobr, primanjaarh.rbrobr,":"")+" primanja"+(isArh?"arh":"obr")+".cvrp, primanja"+(isArh?"arh":"obr")+".rbr";

//    System.out.println("sql : " +sql);

    SQLprimanja.close();
    SQLprimanja.closeStatement();
    SQLprimanja.setQuery(new QueryDescriptor(dm.getDatabase1(),sql));
    SQLprimanja.open();
    return SQLprimanja;
  }

  public QueryDataSet getDoprinosiSet(String rad) {
    if (isArh) {
//      System.out.println("KVERI : " + raOdbici.getInstance().getDoprinosiRadnik(rad, raOdbici.ARH).getOriginalQueryString());
      return raOdbici.getInstance().getDoprinosiRadnik(rad, raOdbici.ARH);
    }
    return raOdbici.getInstance().getDoprinosiRadnik(rad, raOdbici.OBR);
  }
  public QueryDataSet getDoprinosiNaSet(String rad) {
    if (isArh) {
//      System.out.println("KVERI : " + raOdbici.getInstance().getDoprinosiRadnik(rad, raOdbici.ARH).getOriginalQueryString());
      return raOdbici.getInstance().getDoprinosiNa(rad, raOdbici.ARH);
    }
    return raOdbici.getInstance().getDoprinosiNa(rad, raOdbici.OBR);
  }

  public QueryDataSet getKreditiSet(String rad) {
    if (isArh) return raOdbici.getInstance().getKrediti(rad, raOdbici.ARH);
    return raOdbici.getInstance().getKrediti(rad, raOdbici.OBR);
  }
  public String justFormat(BigDecimal bd) {
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    com.borland.dx.text.VariantFormatter formater = dm.getVrsteodb().getColumn("IZNOS").getFormatter();
    v.setBigDecimal(bd);
    return formater.format(v);
  }
  protected String format(DataSet set, String colName) {
    BigDecimal bd = getEvaluatedBigDecimal("ilmz_",set,colName);
    if (bd.signum() == 0) return "";
    return justFormat(bd);
  }
  String getNetoColParam() {
    return frmParam.getParam("pl", "netildoh", "N", "Da li se na listicu po zaradama prikazuje (N)eto ili (D)ohodak?")
      .equalsIgnoreCase("D")?"DOHODAK":"NETO";
    //"NETO";
  }
  BigDecimal totalStopa, totalStopaNa, totalIznosNa;
  String nazivPrim, sati, koef, neto, bruto, nazivDop, osnovicaDop, stopa, iznos, nazivDopNa, osnovicaDopNa, stopaNa, iznosNa;
  String nazivNak, satiNaknada, iznosNak, nazivKred, iznosKred;
  String cradmj, nazradmj;
  String brojtek, nazbanke, tipIsplate, nazvro, copcine, oib, adresa, fondsati;
  short cisplmj;
  int cbanke;
  public void findStrings(String crad, short rbrObr, short mjObr, short godObr) {
    DataSet ds = getPrimanjaSet(crad);
    StringBuffer _naziv = new StringBuffer();
    StringBuffer _sati = new StringBuffer();
    StringBuffer _koef = new StringBuffer();
    StringBuffer _bruto = new StringBuffer();
    StringBuffer _neto = new StringBuffer();
    StringBuffer _nazivN = new StringBuffer();
    StringBuffer _satiN = new StringBuffer();
    StringBuffer _iznosN = new StringBuffer();

    for (ds.first(); ds.inBounds(); ds.next()) {
      ld.raLocate(getVrprim(), new String[] {"CVRP"}, new String[] {""+ds.getShort("CVRP")});
      if (isArh) {
        if (rbrObr == ds.getShort("RBROBR")
            && mjObr == ds.getShort("MJOBR")
            && godObr == ds.getShort("GODOBR")){
          if (raParam.getParam(getVrprim(), 1).equals("D")) {  // Primanja
            _naziv.append(getVrprim().getString("NAZIV")).append("\n");
            _sati.append(format(ds, "SATI")).append("\n");
            _koef.append(format(ds, "KOEF")).append("\n");
            _bruto.append(format(ds, "BRUTO")).append("\n");
            _neto.append(format(ds, getNetoColParam())).append("\n");
          }
          if (raParam.getParam(getVrprim(), 1).equals("N") &&
              raParam.getParam(getVrprim(), 2).equals("N")) {  // Naknade
            _nazivN.append(getVrprim().getString("NAZIV")).append("\n");
            _satiN.append(format(ds, "SATI")).append("\n");
            _iznosN.append(format(ds, getNetoColParam())).append("\n");
          }
        }
      } else{
        if (raParam.getParam(getVrprim(), 1).equals("D")) {  // Primanja
          _naziv.append(getVrprim().getString("NAZIV")).append("\n");
          _sati.append(format(ds, "SATI")).append("\n");
          _koef.append(format(ds, "KOEF")).append("\n");
          _bruto.append(format(ds, "BRUTO")).append("\n");
          _neto.append(format(ds, getNetoColParam())).append("\n");
        }
        if (raParam.getParam(getVrprim(), 1).equals("N") &&
            raParam.getParam(getVrprim(), 2).equals("N")) {  // Naknade
          _nazivN.append(getVrprim().getString("NAZIV")).append("\n");
          _satiN.append(format(ds, "SATI")).append("\n");
          _iznosN.append(format(ds, getNetoColParam())).append("\n");
        }
      }
    }
    _naziv.setLength(Math.max(0, _naziv.length() - 1));
    _sati.setLength(Math.max(0, _sati.length() - 1));
    _koef.setLength(Math.max(0, _koef.length() - 1));
    _bruto.setLength(Math.max(0, _bruto.length() - 1));
    _neto.setLength(Math.max(0, _neto.length() - 1));
    _nazivN.setLength(Math.max(0, _nazivN.length() - 1));
    _satiN.setLength(Math.max(0, _satiN.length() - 1));
    _iznosN.setLength(Math.max(0, _iznosN.length() - 1));
//krediti
    ds = getKreditiSet(crad);
    StringBuffer _nazivK = new StringBuffer();
    StringBuffer _iznosK = new StringBuffer();
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (isArh) {
        if (rbrObr == ds.getShort("RBROBR")
            && mjObr == ds.getShort("MJOBR")
            && godObr == ds.getShort("GODOBR")){
          ld.raLocate(getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
          _nazivK.append(getVrodb().getString("OPISVRODB")).append(getKreditInfo(ds)).append("\n");
          _iznosK.append(format(ds, "OBRIZNOS")).append("\n");
        }
      } else {
        ld.raLocate(getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
        _nazivK.append(getVrodb().getString("OPISVRODB")).append(getKreditInfo(ds)).append("\n");
        _iznosK.append(format(ds, "OBRIZNOS")).append("\n");
      }
    }
    _nazivK.setLength(Math.max(0, _nazivK.length() - 1));
    _iznosK.setLength(Math.max(0, _iznosK.length() - 1));

    nazivPrim = _naziv.toString();
    sati = _sati.toString();
    koef = _koef.toString();
    bruto = _bruto.toString();
    neto = _neto.toString();
    nazivNak = _nazivN.toString();
    satiNaknada= _satiN.toString();
    iznosNak = _iznosN.toString();

    //doprinosi
    ds = getDoprinosiSet(crad);
    Object[] doprinosi = makeDoprStringBuffers(ds, rbrObr, mjObr, godObr);

    nazivDop = ((StringBuffer)doprinosi[0]).toString();
    osnovicaDop = ((StringBuffer)doprinosi[1]).toString();
    stopa = ((StringBuffer)doprinosi[2]).toString();
    iznos = ((StringBuffer)doprinosi[3]).toString();
    totalStopa = (BigDecimal)doprinosi[4];
    //doprinosiNa
    ds = getDoprinosiNaSet(crad);
    Object[] doprinosiNa = makeDoprStringBuffers(ds, rbrObr, mjObr, godObr);

    nazivDopNa = doprinosiNa[0].toString();
    osnovicaDopNa = doprinosiNa[1].toString();
    stopaNa = doprinosiNa[2].toString();
    iznosNa = doprinosiNa[3].toString();
    totalStopaNa = (BigDecimal)doprinosiNa[4];
    totalIznosNa = (BigDecimal)doprinosiNa[5];
    
    nazivKred = _nazivK.toString();
    iznosKred = _iznosK.toString();

    ld.raLocate(radpl, "CRADNIK", crad);
    cradmj = radpl.getString("CRADMJ");
    brojtek = radpl.getString("BROJTEK");
    cisplmj = radpl.getShort("CISPLMJ");
    adresa = radpl.getString("ADRESA");
    copcine = radpl.getString("COPCINE");
    oib = radpl.getString("OIB");
    ld.raLocate(radmj, "CRADMJ", cradmj);
    nazradmj = radmj.getString("NAZIVRM");
    ld.raLocate(dm.getIsplMJ(), "CISPLMJ" ,String.valueOf(cisplmj));
    tipIsplate = dm.getIsplMJ().getString("TIPISPLMJ");
    ld.raLocate(dm.getBankepl(), "CBANKE" ,String.valueOf(dm.getIsplMJ().getInt("CBANKE")));
    nazbanke = dm.getBankepl().getString("NAZBANKE");
    dM.getDataModule().getVrodn().open();
    ld.raLocate(dM.getDataModule().getVrodn(), "CVRO", radpl.getString("CVRO"));
    nazvro = dM.getDataModule().getVrodn().getString("NAZIVRO");

    dM.getDataModule().getFondSati().open();
    ld.raLocate(dM.getDataModule().getFondSati(), new String[] {"KNJIG","GODINA","MJESEC"}, new String[] {OrgStr.getKNJCORG(), godObr+"", mjObr+""});
    fondsati = justFormat(dM.getDataModule().getFondSati().getBigDecimal("SATIUK"));
//    this.killAllReports();
//    this.addReports();

  }
  private Object[] makeDoprStringBuffers(DataSet ds, short rbrObr, short mjObr, short godObr) {
    StringBuffer _nazivD = new StringBuffer();
    StringBuffer _osnovD = new StringBuffer();
    StringBuffer _stopa = new StringBuffer();
    StringBuffer _iznos = new StringBuffer();
    BigDecimal _totalStopa = new BigDecimal(0.0);
    BigDecimal _totalIznos = new BigDecimal(0.0);
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (isArh) {
        if (rbrObr == ds.getShort("RBROBR")
            && mjObr == ds.getShort("MJOBR")
            && godObr == ds.getShort("GODOBR")){
      ld.raLocate(getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
      _nazivD.append(getVrodb().getString("OPISVRODB")).append("\n");
      _osnovD.append(format(ds, "OBROSN")).append("\n");
      _stopa.append(format(ds, "OBRSTOPA")).append("\n");
      _iznos.append(format(ds, "OBRIZNOS")).append("\n");
      _totalStopa = _totalStopa.add(ds.getBigDecimal("OBRSTOPA"));
      _totalIznos = _totalIznos.add(ds.getBigDecimal("OBRIZNOS"));
        }
      } else {
        ld.raLocate(getVrodb(), new String[] {"CVRODB"}, new String[] {""+ds.getShort("CVRODB")});
        _nazivD.append(getVrodb().getString("OPISVRODB")).append("\n");
        _osnovD.append(format(ds, "OBROSN")).append("\n");
        _stopa.append(format(ds, "OBRSTOPA")).append("\n");
        _iznos.append(format(ds, "OBRIZNOS")).append("\n");
        _totalStopa = _totalStopa.add(ds.getBigDecimal("OBRSTOPA"));
        _totalIznos = _totalIznos.add(ds.getBigDecimal("OBRIZNOS"));
      }
    }
    _nazivD.setLength(Math.max(0, _nazivD.length() - 1));
    _osnovD.setLength(Math.max(0, _osnovD.length() - 1));
    _stopa.setLength(Math.max(0, _stopa.length() - 1));
    _iznos.setLength(Math.max(0, _iznos.length() - 1));
    return new Object[] {_nazivD,_osnovD,_stopa,_iznos, _totalStopa, _totalIznos};
  }

  private String getKreditInfo(DataSet ds) {
    if (frmParam.getParam("pl", "ildetkred", "D", "Detaljna specifikacija kredita na ispl.listi").equalsIgnoreCase("N")) return "";
System.out.println("KreditInfo za "+ds);
    if (ds.getBigDecimal("OBRSTOPA").signum()!=0 && ds.getBigDecimal("OBROSN").signum()!=0) {
      //u postotku
      return " "+justFormat(ds.getBigDecimal("OBROSN"))+" x "+justFormat(ds.getBigDecimal("OBRSTOPA"))+"%";
    }
    if (ds.getBigDecimal("SALDO").signum()!= 0) {
      //rate i glavnica
      return " - ostatak duga "+justFormat(ds.getBigDecimal("SALDO"));
    }
    return "";
  }

  public String getNazivVRO() {
    return nazvro;
  }
  public String getInformLine() {
    String infolispl = frmParam.getParam("pl", "infolispl", "ABFX", "(AOMRJBUFX) Na infou ispod imena na ispl.list. treba biti M=RM, R=Rad.odn, J=JMBG, A,O,B,U");
    String il = "";
    if (infolispl.indexOf("A")!=-1) {
      il+= "\nAdresa: "+adresa;
    }
    if (infolispl.indexOf("O")!=-1) {
      il+= "\nOpæina: "+copcine+" "+getNazivOpcine(copcine);
    }
    if (infolispl.indexOf("M")!=-1) {
      il+= "\nRadno mjesto: "+getRadnoMjesto()+" "+getNazivRadnogMjesta();
    }
    if (infolispl.indexOf("R")!=-1) {
      il+= "\nRadni odnos: "+getRadnicipl().getString("CVRO")+" "+getNazivVRO();
    }
    if (infolispl.indexOf("J")!=-1) {
      il+= "\nJMBG: "+getRadnicipl().getString("JMBG");
    }
    if (infolispl.indexOf("B")!=-1) {
      il+= "\nOIB: "+oib;
    }
    if (infolispl.indexOf("U")!=-1) {
      il+= "\nUgovoreni bruto: "+format(getRadnicipl(), "BRUTOSN");
    }
    if (infolispl.indexOf("F")!=-1) {
      il+= "\nPlanirani redovni fond sati rada danju: "+fondsati;
    }
    if (infolispl.indexOf("X")!=-1) {
      String defValue = "Napomena: Podaci iz èl. 3. st. 1. Pravilnika o sadržaju obraèuna plaæe, nadoknade plaæe ili otpremnine " +
      		"\n(Nar.nov., br. 81/10.) nisu navedeni, s obzirom na to da nisu ostvareni";
      il+= "\n"+frmParam.getParam("pl", "infolisplX", defValue, "Dodatna napomena pod X u infolispl");
    }
    return il;
    
  }
  public String getRadnoMjesto() {
    return cradmj;
  }

  public String getNazivRadnogMjesta() {
    return nazradmj;
  }

  public String getPrimanja() {
    return nazivPrim;
  }

  public String getSati() {
    return sati;
  }

  public String getUcinak() {
    return koef;
  }

  public String getBruto() {
    return bruto;
  }

  public String getNeto() {
    return neto;
  }

  public String getDoprinosi() {
    return nazivDop;
  }
  
  public String getOsnovicaDoprinosa(){
    return osnovicaDop;
  }
  
  public String getStopa() {
    return stopa;
  }
  
  public String getIznosDoprinosa() {
    return iznos;
  }
  
  public String getDoprinosiNa() {
    return nazivDopNa;
  }

  public String getOsnovicaDoprinosaNa(){
    return osnovicaDopNa;
  }

  public String getStopaNa() {
    return stopaNa;
  }

  public String getIznosDoprinosaNa() {
    return iznosNa;
  }
  public BigDecimal getTotalStopaNa() {
    return totalStopaNa;
  }
  public BigDecimal getTotalIznosNa() {
    return totalIznosNa;
  }

  public String getNaknade() {
    return nazivNak;
  }
  
  public String getSatiNaknada() {
    return satiNaknada;
  }

  public String getIznosNaknada() {
    return iznosNak;
  }

  public String getKrediti() {
    return nazivKred;
  }

  public String getIznosKredita() {
    return iznosKred;
  }

  public BigDecimal getTotalStopa() {
    return totalStopa;
  }

  public String getIsplataString(){
//    System.out.println("tipIsplate : " + tipIsplate);
    String isplataString = "";
    if (tipIsplate.equals("T")){
      isplataString = "Isplata - ".concat(brojtek).concat(", ").concat(nazbanke);
    } else if (tipIsplate.equals("G")) {
      isplataString = "Isplata u gotovini";
    } else if (tipIsplate.equals("S")) {
      isplataString = "Isplata na štednu knjižicu";
    }
    return isplataString;
  }

  public String getObracun(){
//    String mjOd;
//    if (fieldSet.getShort("MJESECOD") < 10) mjOd = "0"+fieldSet.getShort("MJESECOD");
//    else mjOd = ""+fieldSet.getShort("MJESECOD");
//    return  "Obra\u010Dun pla\u0107e za ".concat(mjOd).concat(". mjesec ").concat(""+fieldSet.getShort("GODINAOD")+".").concat(" (rbr. "+fieldSet.getShort("RBROD")+")");
    return periodPlace(fieldSet.getShort("GODINAOD"), fieldSet.getShort("MJESECOD"));
  }

  public String getObracun(short g, short m, short r){
//    String mjOd;
//    if (m < (short)10) mjOd = "0"+m;
//    else mjOd = ""+m;
//    return  "Obra\u010Dun pla\u0107e za ".concat(mjOd).concat(". mjesec ").concat(""+g+".").concat(" (rbr. "+r+")");
    return periodPlace(g, m);
  }
  private String periodPlace(short g, short m) {
    String ret = "Obra\u010Dun pla\u0107e za ";
    Calendar c = Calendar.getInstance();
    c.set(g, m-1, 1);
    return ret + raDateUtil.getraDateUtil().dataFormatter(Util.getUtil().getFirstDayOfMonth(new Timestamp(c.getTimeInMillis())))+" - "+
    raDateUtil.getraDateUtil().dataFormatter(Util.getUtil().getLastDayOfMonth(new Timestamp(c.getTimeInMillis())));
  }

  QueryDataSet obracunskaLista;
  StorageDataSet sumListe;

  private void makeObrListeRad(){
    String sel =  "SELECT radnicipl.corg, " + getTableName()  + ".cradnik, radnici.prezime, radnici.ime, radnicipl.cradmj, " + getTableName()  + ".sati, " + getTableName()  + ".bruto, " +
                  getTableName()  + ".doprinosi, " + getTableName()  + ".neto, " + getTableName()  + ".iskneop, " + getTableName()  + ".por1, " + getTableName()  + ".por2, " + getTableName()  + ".por3, " +
                  getTableName()  + ".por4, " + getTableName()  + ".por5, " +
                  getTableName()  + ".poruk, " + getTableName()  + ".prir, " + getTableName()  + ".naknade, " + getTableName()  + ".krediti, " + getTableName()  + ".naruke";

    if (getRepMode() == 'A') sel = sel.concat(", " + getTableName()  + ".mjobr, " + getTableName()  + ".godobr, " + getTableName()  + ".rbrobr");

    String from  =  " FROM " + getTableName()  + ", radnici, radnicipl " +
                    "WHERE " + getTableName()  + ".cradnik = radnici.cradnik " +
                    "AND " + getTableName()  + ".cradnik = radnicipl.cradnik " +
                    "AND radnici.cradnik = radnicipl.cradnik ";

    String where =  " AND " + getWhereQuery(isArh?getTableName():"radnici");

    String order =  " ORDER BY radnici.prezime"; /// PROXIMITY WARNING

    String udjuturativ = (sel.concat(from).concat(where).concat(radrange).concat(order)).toUpperCase();

    obracunskaLista = Util.getNewQueryDataSet(udjuturativ);

    sumListe = new StorageDataSet();
    String[] strCols = obracunskaLista.getColumnNames(obracunskaLista.columnCount());
    Column[] cols = new Column[strCols.length-5];

    for (int i = 5 ; i < strCols.length ; i++ ) {
      cols[i-5] = (Column)obracunskaLista.getColumn(strCols[i]).clone();
    }

    sumListe.setColumns(cols);
    sumListe.open();
    if (getRepMode() == 'O') sumListe.insertRow(false);

    do {
      if (getRepMode() == 'A' && !ld.raLocate(sumListe, new String[] {"GODOBR", "MJOBR", "RBROBR"}, new String[] {String.valueOf(obracunskaLista.getShort("GODOBR")), String.valueOf(obracunskaLista.getShort("MJOBR")), String.valueOf(obracunskaLista.getShort("RBROBR"))})){
        sumListe.insertRow(false);
        sumListe.setShort("MJOBR", obracunskaLista.getShort("MJOBR"));
        sumListe.setShort("GODOBR", obracunskaLista.getShort("GODOBR"));
        sumListe.setShort("RBROBR", obracunskaLista.getShort("RBROBR"));
      }
      sumListe.setBigDecimal("SATI", sumListe.getBigDecimal("SATI").add(obracunskaLista.getBigDecimal("SATI")));
      sumListe.setBigDecimal("BRUTO", sumListe.getBigDecimal("BRUTO").add(obracunskaLista.getBigDecimal("BRUTO")));
      sumListe.setBigDecimal("DOPRINOSI", sumListe.getBigDecimal("DOPRINOSI").add(obracunskaLista.getBigDecimal("DOPRINOSI")));
      sumListe.setBigDecimal("NETO", sumListe.getBigDecimal("NETO").add(obracunskaLista.getBigDecimal("NETO")));
      sumListe.setBigDecimal("ISKNEOP", sumListe.getBigDecimal("ISKNEOP").add(obracunskaLista.getBigDecimal("ISKNEOP")));
      sumListe.setBigDecimal("POR1", sumListe.getBigDecimal("POR1").add(obracunskaLista.getBigDecimal("POR1")));
      sumListe.setBigDecimal("POR2", sumListe.getBigDecimal("POR2").add(obracunskaLista.getBigDecimal("POR2")));
      sumListe.setBigDecimal("POR3", sumListe.getBigDecimal("POR3").add(obracunskaLista.getBigDecimal("POR3")));
      sumListe.setBigDecimal("POR4", sumListe.getBigDecimal("POR4").add(obracunskaLista.getBigDecimal("POR4")));
      sumListe.setBigDecimal("POR5", sumListe.getBigDecimal("POR5").add(obracunskaLista.getBigDecimal("POR5")));
      sumListe.setBigDecimal("PORUK", sumListe.getBigDecimal("PORUK").add(obracunskaLista.getBigDecimal("PORUK")));
      sumListe.setBigDecimal("PRIR", sumListe.getBigDecimal("PRIR").add(obracunskaLista.getBigDecimal("PRIR")));
      sumListe.setBigDecimal("NAKNADE", sumListe.getBigDecimal("NAKNADE").add(obracunskaLista.getBigDecimal("NAKNADE")));
      sumListe.setBigDecimal("KREDITI", sumListe.getBigDecimal("KREDITI").add(obracunskaLista.getBigDecimal("KREDITI")));
      sumListe.setBigDecimal("NARUKE", sumListe.getBigDecimal("NARUKE").add(obracunskaLista.getBigDecimal("NARUKE")));
    } while (obracunskaLista.next());
  }

  public DataSet getObracunski() {
    return obracunskaLista;
  }

  public DataSet getSumObracunski() {
    return sumListe;
  }

  public boolean getPrikazIsplate(){
    return jrcbPrikaz.isSelected();
  }

  private String getOdbiciTableName() {
    String tableName = "";
    if (this.getRepMode() == 'A') tableName = "odbiciarh";
    else tableName = "odbiciobr";
    return tableName;
  }

  private String odbiciWh(short[] id){
    String vrstaOdbitka = raIzvjestaji.getOdbiciWhQueryIzv(id);
    if (!vrstaOdbitka.equals("")){
      if (this.getRepMode() == 'A')
        return " and ".concat(vrstaOdbitka);
      return " where ".concat(vrstaOdbitka);
    } else {
      return "";
    }
  }

  private String getWhereSQL(String table, short g, short m, short rbr) {
    if (this.getRepMode() == 'A'){
      String squereWhere = " where ".concat(table +".godobr*10000+" + table + ".mjobr*100+" + table + ".rbrobr in ("+
          (g*10000+m*100+rbr) + ") ");
      return squereWhere;
    } else {
      return "";
    }
  }

  public BigDecimal lifeInsurance(String cradnik, short g, short m, short rbr){
    if (!odbiciWh(raIzvjestaji.ID_3_1).equals("")){
      String III_3_1 = "select sum(" + getOdbiciTableName() + ".obriznos) as obriznos from " + getOdbiciTableName() +
                       getWhereSQL(getOdbiciTableName(), g, m, rbr) +
                       odbiciWh(raIzvjestaji.ID_3_1) +
                       " and " + getOdbiciTableName() +".cradnik = '" + cradnik + "' ";
      QueryDataSet qdsIII_3_1 = Util.getNewQueryDataSet(III_3_1);
      return qdsIII_3_1.getBigDecimal("OBRIZNOS");
    }
    return Aus.zero0;
  }

  public BigDecimal extraHealthInsurance(String cradnik, short g, short m, short rbr){
    if (!odbiciWh(raIzvjestaji.ID_3_2).equals("")){
      String III_3_2 = "select sum(" + getOdbiciTableName() + ".obriznos) as obriznos from " + getOdbiciTableName() +
                       getWhereSQL(getOdbiciTableName(), g, m, rbr) +
                       odbiciWh(raIzvjestaji.ID_3_2) +
                       " and " + getOdbiciTableName() +".cradnik = '" + cradnik + "' ";
      QueryDataSet qdsIII_3_2 = Util.getNewQueryDataSet(III_3_2);
      return qdsIII_3_2.getBigDecimal("OBRIZNOS");
    }
    return Aus.zero0;
  }

  public BigDecimal extraRetireInsurance(String cradnik, short g, short m, short rbr){
    if (!odbiciWh(raIzvjestaji.ID_3_3).equals("")){
      String III_3_3 = "select sum(" + getOdbiciTableName() + ".obriznos) as obriznos from " + getOdbiciTableName() +
                       getWhereSQL(getOdbiciTableName(), g, m, rbr) +
                       odbiciWh(raIzvjestaji.ID_3_3) +
                       " and " + getOdbiciTableName() +".cradnik = '" + cradnik + "' ";
      QueryDataSet qdsIII_3_3 = Util.getNewQueryDataSet(III_3_3);
      return qdsIII_3_3.getBigDecimal("OBRIZNOS");
    }
    return Aus.zero0;
  }

  public java.sql.Timestamp getDatumIspl(String corg, String cvro, short g, short m, short rbr) {
    QueryDataSet datis = Util.getNewQueryDataSet("SELECT datumispl FROM Kumulorgarh WHERE corg='" + corg + "' AND cvro ='" + cvro + "' AND godobr=" + g + " AND mjobr=" + m + " AND rbrobr=" + rbr);
    datumispl = datis.getTimestamp("DATUMISPL");
    return datumispl;
  }

  StorageDataSet StDSradnici, StDSradniciCorg;

  private void makeStDSradnici(){
    StDSradnici = new StorageDataSet();
    StDSradnici.setColumns(SQLradnici.cloneColumns());
    StDSradnici.addColumn(dM.createBigDecimalColumn("ZIVOTNOOSIG", 2));
    StDSradnici.addColumn(dM.createBigDecimalColumn("ZDRAVSTVENOOSIG", 2));
    StDSradnici.addColumn(dM.createBigDecimalColumn("MIROVINSKOOSIG", 2));
    StDSradnici.addColumn(dM.createTimestampColumn("DATISP"));
    if (getRepMode() == 'O'){
      StDSradnici.addColumn(dM.createShortColumn("GODOBR"));
      StDSradnici.addColumn(dM.createShortColumn("MJOBR"));
      StDSradnici.addColumn(dM.createShortColumn("RBROBR"));
    }
    StDSradnici.dropColumn("LOKK");
    StDSradnici.dropColumn("AKTIV");
    StDSradnici.dropColumn("CRADNIK1");
    StDSradnici.open();

    SQLradnici.first();

    do {
      StDSradnici.insertRow(false);
      if (getRepMode() != 'O'){
        StDSradnici.setShort("GODOBR", SQLradnici.getShort("GODOBR"));
        StDSradnici.setShort("MJOBR", SQLradnici.getShort("MJOBR"));
        StDSradnici.setShort("RBROBR", SQLradnici.getShort("RBROBR"));
        StDSradnici.setBigDecimal("ZIVOTNOOSIG", lifeInsurance(SQLradnici.getString("CRADNIK"), SQLradnici.getShort("GODOBR"), SQLradnici.getShort("MJOBR"), SQLradnici.getShort("RBROBR")));
        StDSradnici.setBigDecimal("ZDRAVSTVENOOSIG", extraHealthInsurance(SQLradnici.getString("CRADNIK"), SQLradnici.getShort("GODOBR"), SQLradnici.getShort("MJOBR"), SQLradnici.getShort("RBROBR")));
        StDSradnici.setBigDecimal("MIROVINSKOOSIG", extraRetireInsurance(SQLradnici.getString("CRADNIK"), SQLradnici.getShort("GODOBR"), SQLradnici.getShort("MJOBR"), SQLradnici.getShort("RBROBR")));
        StDSradnici.setTimestamp("DATISP" , getDatumIspl(SQLradnici.getString("CORG"), SQLradnici.getString("CVRO"), SQLradnici.getShort("GODOBR"), SQLradnici.getShort("MJOBR"), SQLradnici.getShort("RBROBR") ));
      } else {
        StDSradnici.setShort("GODOBR", fieldSet.getShort("GODINAOD"));
        StDSradnici.setShort("MJOBR", fieldSet.getShort("MJESECOD"));
        StDSradnici.setShort("RBROBR", fieldSet.getShort("RBROD"));
        StDSradnici.setBigDecimal("ZIVOTNOOSIG", lifeInsurance(SQLradnici.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradnici.setBigDecimal("ZDRAVSTVENOOSIG", extraHealthInsurance(SQLradnici.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradnici.setBigDecimal("MIROVINSKOOSIG", extraRetireInsurance(SQLradnici.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradnici.setTimestamp("DATISP" , getDatumIspl());
      }
      StDSradnici.setString("CORG", SQLradnici.getString("CORG"));
      StDSradnici.setString("CRADNIK", SQLradnici.getString("CRADNIK"));
      StDSradnici.setString("PREZIME", SQLradnici.getString("PREZIME"));
      StDSradnici.setString("IME", SQLradnici.getString("IME"));
      StDSradnici.setString("IMEOCA", SQLradnici.getString("IMEOCA"));
      StDSradnici.setBigDecimal("SATI", getModifiedBigDecimal(SQLradnici,"SATI"));
      StDSradnici.setBigDecimal("BRUTO", getModifiedBigDecimal(SQLradnici,"BRUTO"));
      StDSradnici.setBigDecimal("DOPRINOSI", getModifiedBigDecimal(SQLradnici,"DOPRINOSI"));
      StDSradnici.setBigDecimal("NETO", getModifiedBigDecimal(SQLradnici,"NETO"));
      StDSradnici.setBigDecimal("NEOP", getModifiedBigDecimal(SQLradnici,"NEOP"));
      StDSradnici.setBigDecimal("ISKNEOP", getModifiedBigDecimal(SQLradnici,"ISKNEOP"));
      StDSradnici.setBigDecimal("POROSN", getModifiedBigDecimal(SQLradnici,"POROSN"));
      StDSradnici.setBigDecimal("POR1", getModifiedBigDecimal(SQLradnici,"POR1"));
      StDSradnici.setBigDecimal("POR2", getModifiedBigDecimal(SQLradnici,"POR2"));
      StDSradnici.setBigDecimal("POR3", getModifiedBigDecimal(SQLradnici,"POR3"));
      StDSradnici.setBigDecimal("POR4", getModifiedBigDecimal(SQLradnici,"POR4"));
      StDSradnici.setBigDecimal("POR5", getModifiedBigDecimal(SQLradnici,"POR5"));
      StDSradnici.setBigDecimal("PORUK", getModifiedBigDecimal(SQLradnici,"PORUK"));
      StDSradnici.setBigDecimal("PRIR", getModifiedBigDecimal(SQLradnici,"PRIR"));
      StDSradnici.setBigDecimal("PORIPRIR", getModifiedBigDecimal(SQLradnici,"PORIPRIR"));
      StDSradnici.setBigDecimal("NETO2", getModifiedBigDecimal(SQLradnici,"NETO2"));
      StDSradnici.setBigDecimal("NETOPK", getModifiedBigDecimal(SQLradnici,"NETOPK"));
      StDSradnici.setBigDecimal("NAKNADE", getModifiedBigDecimal(SQLradnici,"NAKNADE"));
      StDSradnici.setBigDecimal("KREDITI", getModifiedBigDecimal(SQLradnici,"KREDITI"));
      StDSradnici.setBigDecimal("NARUKE", getModifiedBigDecimal(SQLradnici,"NARUKE"));
    } while (SQLradnici.next());

    StDSradniciCorg = new StorageDataSet();
    StDSradniciCorg.setColumns(StDSradnici.cloneColumns());
    StDSradniciCorg.open();

    SQLradnicicorg.first();

    do {
      StDSradniciCorg.insertRow(false);
      if (getRepMode() != 'O'){
        StDSradniciCorg.setShort("GODOBR", SQLradnicicorg.getShort("GODOBR"));
        StDSradniciCorg.setShort("MJOBR", SQLradnicicorg.getShort("MJOBR"));
        StDSradniciCorg.setShort("RBROBR", SQLradnicicorg.getShort("RBROBR"));
        StDSradniciCorg.setBigDecimal("ZIVOTNOOSIG", lifeInsurance(SQLradnicicorg.getString("CRADNIK"), SQLradnicicorg.getShort("GODOBR"), SQLradnicicorg.getShort("MJOBR"), SQLradnicicorg.getShort("RBROBR")));
        StDSradniciCorg.setBigDecimal("ZDRAVSTVENOOSIG", extraHealthInsurance(SQLradnicicorg.getString("CRADNIK"), SQLradnicicorg.getShort("GODOBR"), SQLradnicicorg.getShort("MJOBR"), SQLradnicicorg.getShort("RBROBR")));
        StDSradniciCorg.setBigDecimal("MIROVINSKOOSIG", extraRetireInsurance(SQLradnicicorg.getString("CRADNIK"), SQLradnicicorg.getShort("GODOBR"), SQLradnicicorg.getShort("MJOBR"), SQLradnicicorg.getShort("RBROBR")));
        StDSradniciCorg.setTimestamp("DATISP" , getDatumIspl(SQLradnicicorg.getString("CORG"), SQLradnicicorg.getString("CVRO"), SQLradnicicorg.getShort("GODOBR"), SQLradnicicorg.getShort("MJOBR"), SQLradnicicorg.getShort("RBROBR") ));
      } else {
        StDSradnici.setShort("GODOBR", fieldSet.getShort("GODINAOD"));
        StDSradnici.setShort("MJOBR", fieldSet.getShort("MJESECOD"));
        StDSradnici.setShort("RBROBR", fieldSet.getShort("RBROD"));
        StDSradniciCorg.setBigDecimal("ZIVOTNOOSIG", lifeInsurance(SQLradnicicorg.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradniciCorg.setBigDecimal("ZDRAVSTVENOOSIG", extraHealthInsurance(SQLradnicicorg.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradniciCorg.setBigDecimal("MIROVINSKOOSIG", extraRetireInsurance(SQLradnicicorg.getString("CRADNIK"), (short) 0, (short) 0, (short) 0));
        StDSradniciCorg.setTimestamp("DATISP" , getDatumIspl());
      }
      StDSradniciCorg.setString("CORG", SQLradnicicorg.getString("CORG"));
      StDSradniciCorg.setString("CRADNIK", SQLradnicicorg.getString("CRADNIK"));
      StDSradniciCorg.setString("PREZIME", SQLradnicicorg.getString("PREZIME"));
      StDSradniciCorg.setString("IME", SQLradnicicorg.getString("IME"));
      StDSradniciCorg.setString("IMEOCA", SQLradnicicorg.getString("IMEOCA"));
      StDSradniciCorg.setBigDecimal("SATI", getModifiedBigDecimal(SQLradnicicorg,"SATI"));
      StDSradniciCorg.setBigDecimal("BRUTO", getModifiedBigDecimal(SQLradnicicorg,"BRUTO"));
      StDSradniciCorg.setBigDecimal("DOPRINOSI", getModifiedBigDecimal(SQLradnicicorg,"DOPRINOSI"));
      StDSradniciCorg.setBigDecimal("NETO", getModifiedBigDecimal(SQLradnicicorg,"NETO"));
      StDSradniciCorg.setBigDecimal("NEOP", getModifiedBigDecimal(SQLradnicicorg,"NEOP"));
      StDSradniciCorg.setBigDecimal("ISKNEOP", getModifiedBigDecimal(SQLradnicicorg,"ISKNEOP"));
      StDSradniciCorg.setBigDecimal("POROSN", getModifiedBigDecimal(SQLradnicicorg,"POROSN"));
      StDSradniciCorg.setBigDecimal("POR1", getModifiedBigDecimal(SQLradnicicorg,"POR1"));
      StDSradniciCorg.setBigDecimal("POR2", getModifiedBigDecimal(SQLradnicicorg,"POR2"));
      StDSradniciCorg.setBigDecimal("POR3", getModifiedBigDecimal(SQLradnicicorg,"POR3"));
      StDSradniciCorg.setBigDecimal("POR4", getModifiedBigDecimal(SQLradnicicorg,"POR4"));
      StDSradniciCorg.setBigDecimal("POR5", getModifiedBigDecimal(SQLradnicicorg,"POR5"));
      StDSradniciCorg.setBigDecimal("PORUK", getModifiedBigDecimal(SQLradnicicorg,"PORUK"));
      StDSradniciCorg.setBigDecimal("PRIR", getModifiedBigDecimal(SQLradnicicorg,"PRIR"));
      StDSradniciCorg.setBigDecimal("PORIPRIR", getModifiedBigDecimal(SQLradnicicorg,"PORIPRIR"));
      StDSradniciCorg.setBigDecimal("NETO2", getModifiedBigDecimal(SQLradnicicorg,"NETO2"));
      StDSradniciCorg.setBigDecimal("NETOPK", getModifiedBigDecimal(SQLradnicicorg,"NETOPK"));
      StDSradniciCorg.setBigDecimal("NAKNADE", getModifiedBigDecimal(SQLradnicicorg,"NAKNADE"));
      StDSradniciCorg.setBigDecimal("KREDITI", getModifiedBigDecimal(SQLradnicicorg,"KREDITI"));
      StDSradniciCorg.setBigDecimal("NARUKE", getModifiedBigDecimal(SQLradnicicorg,"NARUKE"));
    } while (SQLradnicicorg.next());
    findStrings(StDSradnici.getString("CRADNIK"), StDSradnici.getShort("RBROBR"), StDSradnici.getShort("MJOBR"), StDSradnici.getShort("GODOBR"));
  }

  private BigDecimal getModifiedBigDecimal(QueryDataSet lradnici, String cn) {
    return getEvaluatedBigDecimal("ilmr_",lradnici, cn);
  }
  /**
   * Parametriziran prikaz bigdecimal vrijednosti kod ispisa isplatnih listica tako
   * da se moze bilo koja kolona na kumulrad, kumulradarh, primanjaobr, primanjaarh 
   * zamijeniti nekom drugom ili racunskom operacijom drugih kolona (MathEvaluator). 
   * Parametri su ilmr_IMEKOLONE za kumulrad, kumulradarh, 
   * odnosno ilmz_IMEKOLONE za primanjaobr, primanjaarh i ne dodaju se automatski
   * @param prefix ilmr_ ili ilmz_ ili nesto trece 4futureUse
   * @param set readrow koji sadrzi kolone
   * @param colName ime kolone koja se modificira
   * @return bigdecimal rezultat expressiona
   * @todo moglo bi se ovo i sire upotrijebiti
   */
  private BigDecimal getEvaluatedBigDecimal(String prefix, ReadRow set, String colName) {
    String ilm_param = frmParam.getParam("pl",prefix+colName);
    BigDecimal bd = new BigDecimal(0);
    if (ilm_param == null || ilm_param.trim().equals("")) {
      //set.getVariant(colName,v);
      bd = set.getBigDecimal(colName);
    } else {
      bd = MathEvaluator.getEvaluatedBigDecimal(ilm_param, set);
//      if (ilm_param.equalsIgnoreCase("null")) return bd;
//      MathEvaluator m = new MathEvaluator(ilm_param);
//      Column[] cn = set.getColumns();
//      for (int i = 0; i < cn.length; i++) {
//        if (cn[i].getDataType() == com.borland.dx.dataset.Variant.BIGDECIMAL) {
//          try {
//            m.addVariable(cn[i].getColumnName().toUpperCase(), set.getBigDecimal(cn[i].getColumnName()).doubleValue());  
//          } catch (Exception e) {
//            System.out.println("ilm_ex#addVariable ("+ilm_param+") :: "+e);
//          }
//        }
//      }
//      try {
//        bd = new BigDecimal(m.getValue().doubleValue());
//      } catch (Exception e) {
//        System.out.println("ilm_ex#getValue ("+ilm_param+") :: "+e);
//      }
    } 
    return bd;
  }

  public BigDecimal getMinimalac(DataSet r) {
    BigDecimal neoporezivo = r.getBigDecimal("NEOP");
    return hasPausal(r.getString("CRADNIK"))?neoporezivo:dm.getParametripl().getBigDecimal("MINPL");
  }

  private boolean hasPausal(String cradnik) {
    return raOdbici.getInstance().getPausalniOdbitak(cradnik,isArh?raOdbici.ARH:raOdbici.OBR).getRowCount()>0;
  }

  public BigDecimal getKoefOlaksice(DataSet r) {
    if (hasPausal(r.getString("CRADNIK"))) return Aus.zero2;
    BigDecimal neoporezivo = r.getBigDecimal("NEOP");
    return neoporezivo.divide(getMinimalac(r), 2, BigDecimal.ROUND_HALF_UP);
  }

  public boolean shouldPrintLogo() {
    return getRepRunner().getReport("hr.restart.pl.repIspList").shouldPrintLogo();
  }
  private boolean isDetPorezi() {
    return frmParam.getParam("pl", "ildetpor", "N", "Ispisuje li se stopa i osnovica poreza na listicu (D/N)").equals("D");
  }
  
  public String getPor1txt(DataSet radnici) {
    return isDetPorezi()?getPorText(radnici,1):frmParam.getParam("pl", "por1txt", "Porez 12%", "Text 1. poreza ako se ne izracunava (vidi ildetpor)");
  }
  public String getPor2txt(DataSet radnici) {
//    return isDetPorezi()?getPorText(radnici,2):"Porez 2";
    return isDetPorezi()?getPorText(radnici,2):frmParam.getParam("pl", "por2txt", "Porez 25%", "Text 2. poreza ako se ne izracunava (vidi ildetpor)");
  }
  public String getPor3txt(DataSet radnici) {
    return isDetPorezi()?getPorText(radnici,3):frmParam.getParam("pl", "por3txt", "Porez 40%", "Text 3. poreza ako se ne izracunava (vidi ildetpor)");
  }
  public String getPor4txt(DataSet radnici) {
    return isDetPorezi()?getPorText(radnici,4):frmParam.getParam("pl", "por4txt", "", "Text 4. poreza ako se ne izracunava (vidi ildetpor)");
  }
  public String getPrirtxt(DataSet radnici) {
    return isDetPorezi()?getPorText(radnici,-1):frmParam.getParam("pl", "prirtxt", "Prirez", "Text prireza ako se ne izracunava (vidi ildetpor)");
  }

  private String cachept_cradnik = "#$@!";
  private HashMap cachept;
  private String getPorText(DataSet radnici, int rbr) {
    if (!cachept_cradnik.equals(radnici.getString("CRADNIK"))) createCachept(radnici);
    String s = (String)cachept.get(rbr+"");
    if (s==null) return "";
    return s;
  }
  private void createCachept(DataSet radnici) {
    cachept_cradnik = radnici.getString("CRADNIK");
    cachept = new HashMap();
    raOdbici.clearCache();
    QueryDataSet porezi = raOdbici.getInstance().getPorez(cachept_cradnik, isArh?raOdbici.ARH:raOdbici.OBR);
    for (porezi.first(); porezi.inBounds(); porezi.next()) {
System.out.println(porezi);
      String s = "Porez "+
      //justFormat(porezi.getBigDecimal("OBRIZNOS").divide(porezi.getBigDecimal("OBRSTOPA"),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)))
      format(porezi,"OBROSN")
      +" x "+format(porezi,"OBRSTOPA")+"%";
      cachept.put(porezi.getShort("RBRODB")+"",s);
    }
    QueryDataSet prirez = raOdbici.getInstance().getPrirez(cachept_cradnik, isArh?raOdbici.ARH:raOdbici.OBR);
    prirez.first();
    if (prirez.getRowCount()>0) {
      String p = "Prirez "+format(prirez, "OBRSTOPA")+"% "+getNazivOpcine(prirez.getString("CKEY"));
      cachept.put("-1", p);
    }
  }
  private String getNazivOpcine(String c) {
    QueryDataSet opc = dM.getDataModule().getOpcine();
    opc.open();
    if (lookupData.getlookupData().raLocate(opc, "COPCINE", c)) return opc.getString("NAZIVOP");
    return "";
  }
}

