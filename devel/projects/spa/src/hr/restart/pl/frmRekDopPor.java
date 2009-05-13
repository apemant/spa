/****license*****************************************************************
**   file: frmRekDopPor.java
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

import hr.restart.util.Util;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmRekDopPor extends frmRekObr{

  Util ut = Util.getUtil();
  raOdbici opl = raOdbici.getInstance();

  QueryDataSet repQDS01= new QueryDataSet();
  QueryDataSet repQDS02= new QueryDataSet();
  QueryDataSet repQDSsum= new QueryDataSet();
  QueryDataSet repQDSPorezi= new QueryDataSet();
  QueryDataSet repQDSPrirezi= new QueryDataSet();
  StorageDataSet repQDSporpri= new StorageDataSet();
  QueryDataSet repQDSPorezi1= new QueryDataSet();
  QueryDataSet repQDSPrirezi1= new QueryDataSet();
  StorageDataSet repQDSporpri1= new StorageDataSet();

  String qstrPor;
  public frmRekDopPor() {
    this('O');
  }

  public frmRekDopPor(char mode) {
    super(mode);
    isArh = mode == 'A';
    if (!isArh) {
      frdp = this;
      setReportProviders();
    }
  }

  static frmRekDopPor frdp;
  static boolean isArh = false;

  public static frmRekDopPor getFrmRekDopPor() {
    if (isArh) return frmRekDopPorA.getInstanceA();
    return frdp;
  }

  public void componentShow(){
    setDifolt();
  }

  private String getTableName() {
    if (getRepMode() == 'A') return "odbiciarh";
    else return "odbiciobr";
  }

  private String getTableNamePorez() {
    if (getRepMode() == 'A') return "kumulradarh";
    else return "kumulrad";
  }

  public DataSet getRepQDS(){
    return repQDS01;
  }

  public DataSet getRepQDS02(){
    return repQDS02;
  }

  public DataSet getRepQDSPorPri(){
    return repQDSporpri;
  }

  public DataSet getRepQDSPorPri1(){
    return repQDSporpri1;
  }

  public DataSet getSumSet(){
    return repQDSsum;
  }

  public String getNaNivou(){
    return "na organizacijskom nivou ".concat(fieldSet.getString("CORG")).concat(" ").concat(getNazivOrg(fieldSet.getString("CORG")));
  }

  public String getObracun(){
    String mjOd;
    String mjDo;

    if (fieldSet.getShort("MJESECOD") < 10) mjOd = "0"+fieldSet.getShort("MJESECOD");
    else mjOd = ""+fieldSet.getShort("MJESECOD");
    if (fieldSet.getShort("MJESECDO") < 10) mjDo = "0"+fieldSet.getShort("MJESECDO");
    else mjDo = ""+fieldSet.getShort("MJESECDO");

    if(getRepMode() == 'O'){
      return  "Obra\u010Dun za ".concat(mjOd).concat("-").concat(""+
              fieldSet.getShort("GODINAOD")).concat("/"+fieldSet.getShort("RBROD"));
    } else {
      return  "Obra\u010Dun za ".concat(mjOd).concat("-").concat(""+
              fieldSet.getShort("GODINAOD")).concat(" do ").concat(mjDo).concat("-").concat(""+
              fieldSet.getShort("GODINADO")).concat(", rbr ").concat(""+fieldSet.getShort("RBROD")).concat("-"+
              fieldSet.getShort("RBRDO"));
    }
  }

  public int getBrojDjelatnika(String flag){
    if(!fieldSet.getString("CVRO").equals("") && flag.equals("LD")) return getbrDjelatnikaVRO(fieldSet.getString("CVRO"),fieldSet.getString("CORG"));
    return getbrDjelatnika(fieldSet.getString("CORG"));
  }

  public String getNazivOrg(String corg){
    vl.execSQL("select naziv from orgstruktura where corg='" + corg + "'");
    vl.RezSet.open();
    return vl.RezSet.getString("NAZIV");
  }

  public void okPress(){
    isArh = getRepMode() == 'A';
    super.okPress();
    setStorigePorPri(repQDSPorezi, repQDSPrirezi, repQDSPorezi1, repQDSPrirezi1);
  }

  public boolean Validacija(){

    repQDS01 = getQDSDop("LD"); //Util.getNewQueryDataSet(getQstrDop("LD"));
    repQDS02 = getQDSDop("LDRO"); //Util.getNewQueryDataSet(getQstrDop("LDRO"));

    repQDSsum = getQDSDopSum(); //Util.getNewQueryDataSet(getQstrDopSum());

    repQDSPorezi = getQDSPorez("OPC"); //Util.getNewQueryDataSet(getQstrPorez("OPC"));
    repQDSPorezi1 = getQDSPorez(""); //Util.getNewQueryDataSet(getQstrPorez(""));

    repQDSPrirezi = getQDSPrirez("OPC"); //Util.getNewQueryDataSet(getQstrPrirez("OPC"));
    repQDSPrirezi1 = getQDSPrirez(""); //Util.getNewQueryDataSet(getQstrPrirez(""));

    repQDSPrirezi1.getColumn("OBRSTOPA").setDisplayMask("###,###,##0.00");
    repQDSPrirezi1.getColumn("OBROSN").setDisplayMask("###,###,##0.00");
    repQDSPrirezi1.getColumn("OBRIZNOS").setDisplayMask("###,###,##0.00");

    if (repQDS01.isEmpty()){
      JOptionPane.showConfirmDialog(this.getJPan(),
                                    "Nema podataka za prikaz!",
                                    "Upozorenje",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public String getFlag(){
    if(getRepMode() == 'O') return ("O");
    return ("A");
  }

  private String getQstrDop(String flag){
    String qstr = "SELECT max(radnicipl.cvro) as cvro, max(" + getTableName() +
                  ".cvrodb) as cvrodb, max(" + getTableName() +
                  ".obrstopa) as obrstopa, sum(" + getTableName() +
                  ".obrosn) as obrosn, sum(" + getTableName() +
                  ".obriznos) as obriznos "+
                  "FROM " + getTableName() + ",radnicipl,radnici where (("+
                  opl.getOdbiciWhereQuery(opl.DOPR_param1,getTableName())+
                  ") or ("+opl.getOdbiciWhereQuery(opl.DOPR_param2,getTableName())+
                  ") or ("+opl.getOdbiciWhereQuery(opl.DOPN_param,getTableName())+
                  ")) AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
                  " AND radnici.cradnik = " + getTableName() + ".cradnik"+
                  " AND "+getWhereQuery("radnici");

    String grupBy = " group by " + getTableName() + ".cvrodb";

    if (!fieldSet.getString("CVRO").equals("") && flag.equals("LD")) qstr = qstr.concat(" and radnicipl.cvro = "+fieldSet.getString("CVRO"));
    if (!flag.equals("LD")) grupBy = grupBy.concat(", radnicipl.cvro");
    qstr = qstr.concat(grupBy);

    return qstr;
  }

  private QueryDataSet getQDSDop(String flag){
    String qstr = "SELECT radnicipl.cvro as cvro, " + getTableName() +
                  ".cvrodb as cvrodb, " + getTableName() +
                  ".obrstopa as obrstopa, " + getTableName() + ".obrosn as obrosn, " +
                  getTableName() + ".obriznos as obriznos "+
                  "FROM " + getTableName() + ",radnicipl,radnici where (("+
                  opl.getOdbiciWhereQuery(opl.DOPR_param1,getTableName())+
                  ") or ("+opl.getOdbiciWhereQuery(opl.DOPR_param2,getTableName())+
                  ") or ("+opl.getOdbiciWhereQuery(opl.DOPN_param,getTableName())+
                  ")) AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
                  " AND radnici.cradnik = " + getTableName() + ".cradnik"+
                  " AND "+getWhereQuery("radnici");
    if (!fieldSet.getString("CVRO").equals("") && flag.equals("LD")) qstr = qstr.concat(" and radnicipl.cvro = "+fieldSet.getString("CVRO"));

    String[] gruping;
    if (!flag.equals("LD")) gruping = new String[]{"CVRODB","CVRO"};
    else gruping = new String[]{"CVRODB"};
System.out.println("getQDSDop("+flag+") :: " + qstr);
    QueryDataSet temporary = ut.getNewQueryDataSet(qstr);
    QueryDataSet temporary2 = new QueryDataSet();
    temporary2.setColumns(temporary.cloneColumns());
    temporary2.open();
    temporary.first();
    do {
      String[] filtring;
      if (!flag.equals("LD")) filtring = new String[]{""+temporary.getShort("CVRODB"),temporary.getString("CVRO")};
      else filtring = new String[]{""+temporary.getShort("CVRODB")};
      if (!lookupData.getlookupData().raLocate(temporary2, gruping, filtring)){
        temporary2.insertRow(false);
        temporary.copyTo(temporary2);
      } else {
        temporary2.setBigDecimal("OBROSN",temporary2.getBigDecimal("OBROSN").add(temporary.getBigDecimal("OBROSN")));
        temporary2.setBigDecimal("OBRIZNOS",temporary2.getBigDecimal("OBRIZNOS").add(temporary.getBigDecimal("OBRIZNOS")));
      }
    } while (temporary.next());

    return temporary2;
  }

  private String getQstrPrirez(String flag){
    String qstr = "SELECT max(radnicipl.cvro) as cvro, max(radnicipl.copcine) as copcine, max(" + getTableName() +
                  ".obrstopa) as obrstopa, sum(" + getTableName() + ".obrosn) as obrosn, sum(" +
                  getTableName() + ".obriznos) as obriznos "+
                  "FROM " + getTableName() + ",radnicipl,radnici where ("+
                  opl.getOdbiciWhereQuery(opl.PRIREZ_param,getTableName())+
                  ") AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
                  " AND radnici.cradnik = " + getTableName() + ".cradnik"+
                  " AND "+getWhereQuery("radnici");
    String grup1 = " group by radnicipl.copcine";
    String grup2 = " group by obrstopa";
    if (flag.equals("OPC")) qstr = qstr.concat(grup1);
    else qstr = qstr.concat(grup2);
    return qstr;
  }

  private QueryDataSet getQDSPrirez(String flag){
    String qstr = "SELECT radnicipl.cvro as cvro, radnicipl.copcine as copcine, " + getTableName() +
                  ".obrstopa as obrstopa, " + getTableName() + ".obrosn as obrosn, " +
                  getTableName() + ".obriznos as obriznos "+
                  "FROM " + getTableName() + ",radnicipl,radnici where ("+
                  opl.getOdbiciWhereQuery(opl.PRIREZ_param,getTableName())+
                  ") AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
                  " AND radnici.cradnik = " + getTableName() + ".cradnik"+
                  " AND "+getWhereQuery("radnici");

    QueryDataSet temporary = ut.getNewQueryDataSet(qstr);
    QueryDataSet temporary2 = new QueryDataSet();
    temporary2.setColumns(temporary.cloneColumns());
    temporary2.open();
    temporary.first();

    if (flag.equals("OPC")){
      do {
        if (!lookupData.getlookupData().raLocate(temporary2,"COPCINE",temporary.getString("COPCINE"))){
          temporary2.insertRow(false);
          temporary.copyTo(temporary2);
        } else {
          temporary2.setBigDecimal("OBROSN",temporary2.getBigDecimal("OBROSN").add(temporary.getBigDecimal("OBROSN")));
          temporary2.setBigDecimal("OBRIZNOS",temporary2.getBigDecimal("OBRIZNOS").add(temporary.getBigDecimal("OBRIZNOS")));
        }
      } while (temporary.next());
    } else {
      do {
        if (!lookupData.getlookupData().raLocate(temporary2,"OBRSTOPA",""+temporary.getBigDecimal("OBRSTOPA"))){
          temporary2.insertRow(false);
          temporary.copyTo(temporary2);
        } else {
          temporary2.setBigDecimal("OBROSN",temporary2.getBigDecimal("OBROSN").add(temporary.getBigDecimal("OBROSN")));
          temporary2.setBigDecimal("OBRIZNOS",temporary2.getBigDecimal("OBRIZNOS").add(temporary.getBigDecimal("OBRIZNOS")));
        }
      } while (temporary.next());
    }

    return temporary2;
  }

  private String getQstrPorez(String flag){
    String qstr = "SELECT max(radnicipl.copcine) as copcine, sum(" + getTableNamePorez() + ".por1) as por1, sum(" + getTableNamePorez() +
                  ".por2) as por2, sum(" + getTableNamePorez() + ".por3) as por3, sum(" + getTableNamePorez() + ".por4) as por4, sum(" +
                  getTableNamePorez() + ".por5) as por5, sum(" + getTableNamePorez() + ".poruk) as poruk "+
                  "FROM " + getTableNamePorez() + ",radnicipl,radnici where radnicipl.cradnik = " + getTableNamePorez() + ".cradnik "+
                  "AND radnici.cradnik = " + getTableNamePorez() + ".cradnik AND " + getWhereQuery("radnici");
    String grup1 = " group by radnicipl.copcine";
    if (flag.equals("OPC")) qstr = qstr.concat(grup1);
    return qstr;
  }

  private QueryDataSet getQDSPorez(String flag){
    String qstr = "SELECT radnicipl.copcine as copcine, " + getTableNamePorez() + ".por1 as por1, " + getTableNamePorez() +
                  ".por2 as por2, " + getTableNamePorez() + ".por3 as por3, " + getTableNamePorez() + ".por4 as por4, " +
                  getTableNamePorez() + ".por5 as por5, " + getTableNamePorez() + ".poruk as poruk "+
                  "FROM " + getTableNamePorez() + ",radnicipl,radnici where radnicipl.cradnik = " + getTableNamePorez() + ".cradnik "+
                  "AND radnici.cradnik = " + getTableNamePorez() + ".cradnik AND " + getWhereQuery("radnici");

    QueryDataSet temporary = ut.getNewQueryDataSet(qstr);
    QueryDataSet temporary2 = new QueryDataSet();
    temporary2.setColumns(temporary.cloneColumns());
    temporary2.open();
    temporary.first();

    if (!flag.equals("OPC")){
      temporary2.insertRow(false);
      temporary.copyTo(temporary2);
      temporary.next();
      do {
        temporary2.setBigDecimal("POR1",temporary2.getBigDecimal("POR1").add(temporary.getBigDecimal("POR1")));
        temporary2.setBigDecimal("POR2",temporary2.getBigDecimal("POR2").add(temporary.getBigDecimal("POR2")));
        temporary2.setBigDecimal("POR3",temporary2.getBigDecimal("POR3").add(temporary.getBigDecimal("POR3")));
        temporary2.setBigDecimal("POR4",temporary2.getBigDecimal("POR4").add(temporary.getBigDecimal("POR4")));
        temporary2.setBigDecimal("POR5",temporary2.getBigDecimal("POR5").add(temporary.getBigDecimal("POR5")));
        temporary2.setBigDecimal("PORUK",temporary2.getBigDecimal("PORUK").add(temporary.getBigDecimal("PORUK")));
      } while (temporary.next());
    } else {
      do {
        if (!lookupData.getlookupData().raLocate(temporary2,"COPCINE",temporary.getString("COPCINE"))){
          temporary2.insertRow(false);
          temporary.copyTo(temporary2);
        } else {
          temporary2.setBigDecimal("POR1",temporary2.getBigDecimal("POR1").add(temporary.getBigDecimal("POR1")));
          temporary2.setBigDecimal("POR2",temporary2.getBigDecimal("POR2").add(temporary.getBigDecimal("POR2")));
          temporary2.setBigDecimal("POR3",temporary2.getBigDecimal("POR3").add(temporary.getBigDecimal("POR3")));
          temporary2.setBigDecimal("POR4",temporary2.getBigDecimal("POR4").add(temporary.getBigDecimal("POR4")));
          temporary2.setBigDecimal("POR5",temporary2.getBigDecimal("POR5").add(temporary.getBigDecimal("POR5")));
          temporary2.setBigDecimal("PORUK",temporary2.getBigDecimal("PORUK").add(temporary.getBigDecimal("PORUK")));
        }

      } while (temporary.next());
    }

    return temporary2;
  }

  private String getQstrDopSum(){
    String qstr = "SELECT sum(" + getTableName() + ".obriznos) as sumiznos, max(radnicipl.cvro) as cvro "+
              "FROM " + getTableName() + ",radnicipl,radnici where (("+
              opl.getOdbiciWhereQuery(opl.DOPR_param1,getTableName())+            //\\   <- odbiciarh ??  getTableName() ??
              ") or ("+opl.getOdbiciWhereQuery(opl.DOPR_param2,getTableName())+  //~~\\  <- odbiciarh ??  getTableName() ??
              ") or ("+opl.getOdbiciWhereQuery(opl.DOPN_param,getTableName())+  //    \\ <- odbiciarh ??  getTableName() ??
              ")) AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
              " AND radnici.cradnik = " + getTableName() + ".cradnik"+
              " AND "+getWhereQuery("radnici"); // getWhereQuery() pocinje sa "corg in (..."

    String grupBy = " group by cvro";

    qstr = qstr.concat(grupBy);
    return qstr;
  }

  private QueryDataSet getQDSDopSum(){
    String qstr = "SELECT " + getTableName() + ".obriznos as sumiznos, radnicipl.cvro as cvro "+
              "FROM " + getTableName() + ",radnicipl,radnici where (("+
              opl.getOdbiciWhereQuery(opl.DOPR_param1,getTableName())+            //\\   <- odbiciarh ??  getTableName() ??
              ") or ("+opl.getOdbiciWhereQuery(opl.DOPR_param2,getTableName())+  //~~\\  <- odbiciarh ??  getTableName() ??
              ") or ("+opl.getOdbiciWhereQuery(opl.DOPN_param,getTableName())+  //    \\ <- odbiciarh ??  getTableName() ??
              ")) AND radnicipl.cradnik = " + getTableName() + ".cradnik"+
              " AND radnici.cradnik = " + getTableName() + ".cradnik"+
              " AND "+getWhereQuery("radnici");
    QueryDataSet temporary = ut.getNewQueryDataSet(qstr);
    QueryDataSet temporary2 = new QueryDataSet();
    temporary2.setColumns(temporary.cloneColumns());
    temporary2.open();
    temporary.first();

    do {
      if (!lookupData.getlookupData().raLocate(temporary2,"CVRO",temporary.getString("CVRO"))){
        temporary2.insertRow(false);
        temporary.copyTo(temporary2);
      } else {
        temporary2.setBigDecimal("SUMIZNOS",temporary2.getBigDecimal("SUMIZNOS").add(temporary.getBigDecimal("SUMIZNOS")));
      }
    } while (temporary.next());
    return temporary2;
  }

  Column copcine = new Column();
  Column por1 = new Column();
  Column por2 = new Column();
  Column por3 = new Column();
  Column por4 = new Column();
  Column por5 = new Column();
  Column poruk = new Column();
  Column obrstopa = new Column();
  Column obrosn = new Column();
  Column obriznos = new Column();
  Column ukupno = new Column();

  void setStorigePorPri(QueryDataSet qds, QueryDataSet qdsA, QueryDataSet qds1, QueryDataSet qds1A){
    try {
      copcine = dm.createStringColumn("COPCINE",0);
      por1 = dm.createBigDecimalColumn("POR1");
      por2 = dm.createBigDecimalColumn("POR2");
      por3 = dm.createBigDecimalColumn("POR3");
      por4 = dm.createBigDecimalColumn("POR4");
      por5 = dm.createBigDecimalColumn("POR5");
      poruk = dm.createBigDecimalColumn("PORUK");
      obrstopa = dm.createBigDecimalColumn("OBRSTOPA");
      obrosn = dm.createBigDecimalColumn("OBROSN");
      obriznos = dm.createBigDecimalColumn("OBRIZNOS");
      ukupno = dm.createBigDecimalColumn("UKUPNO");

      repQDSporpri.setColumns(new Column[] {copcine, por1, por2, por3, por4, por5, poruk,
                                            obrstopa, obrosn, obriznos, ukupno});
      repQDSporpri1.setColumns(repQDSporpri.cloneColumns());

      repQDSporpri.open();
      repQDSporpri1.open();

    }
    catch (Exception ex) {
      repQDSporpri.deleteAllRows();
      repQDSporpri1.deleteAllRows();
    }

    BigDecimal nullla = new BigDecimal(0);

    boolean notInserted;

    qds.first();

    do {
      repQDSporpri.insertRow(false);
      repQDSporpri.setString("COPCINE", qds.getString("COPCINE"));
      repQDSporpri.setBigDecimal("POR1", qds.getBigDecimal("POR1"));
      repQDSporpri.setBigDecimal("POR2", qds.getBigDecimal("POR2"));
      repQDSporpri.setBigDecimal("POR3", qds.getBigDecimal("POR3"));
      repQDSporpri.setBigDecimal("POR4", qds.getBigDecimal("POR4"));
      repQDSporpri.setBigDecimal("POR5", qds.getBigDecimal("POR5"));
      repQDSporpri.setBigDecimal("PORUK", qds.getBigDecimal("PORUK"));
      if(lookupData.getlookupData().raLocate(qdsA, new String[] {"COPCINE"}, new String[] {qds.getString("COPCINE")})){
        repQDSporpri.setBigDecimal("OBRSTOPA", qdsA.getBigDecimal("OBRSTOPA"));
        repQDSporpri.setBigDecimal("OBROSN", qdsA.getBigDecimal("OBROSN"));
        repQDSporpri.setBigDecimal("OBRIZNOS", qdsA.getBigDecimal("OBRIZNOS"));
        repQDSporpri.setBigDecimal("UKUPNO", qds.getBigDecimal("PORUK").add(qdsA.getBigDecimal("OBRIZNOS")));
      } else {
        repQDSporpri.setBigDecimal("OBRSTOPA", nullla);
        repQDSporpri.setBigDecimal("OBROSN", nullla);
        repQDSporpri.setBigDecimal("OBRIZNOS", nullla);
        repQDSporpri.setBigDecimal("UKUPNO", qds.getBigDecimal("PORUK"));
      }

    } while(qds.next());

    qds1A.first();

    repQDSporpri1.insertRow(false);
    repQDSporpri1.setString("COPCINE", qds1.getString("COPCINE"));
    repQDSporpri1.setBigDecimal("POR1", qds1.getBigDecimal("POR1"));
    repQDSporpri1.setBigDecimal("POR2", qds1.getBigDecimal("POR2"));
    repQDSporpri1.setBigDecimal("POR3", qds1.getBigDecimal("POR3"));
    repQDSporpri1.setBigDecimal("POR4", qds1.getBigDecimal("POR4"));
    repQDSporpri1.setBigDecimal("POR5", qds1.getBigDecimal("POR5"));
    repQDSporpri1.setBigDecimal("PORUK", qds1.getBigDecimal("PORUK"));
    do {
    if(lookupData.getlookupData().raLocate(qdsA, new String[] {"COPCINE"}, new String[] {qds.getString("COPCINE")})){
      //repQDSporpri1.setBigDecimal("UKUPNO", qds1.getBigDecimal("PORUK").add(qds1A.getBigDecimal("OBRIZNOS")));
      repQDSporpri1.setBigDecimal("UKUPNO", repQDSporpri1.getBigDecimal("UKUPNO").add(qds1A.getBigDecimal("OBRIZNOS")));
    } else {
//      repQDSporpri1.setBigDecimal("UKUPNO", qds1.getBigDecimal("UKUPNO").add(qds1A.getBigDecimal("OBRIZNOS")));
        repQDSporpri1.setBigDecimal("UKUPNO", qds1.getBigDecimal("PORUK"));
    }                         /** @todo sredit ovo da radi */
    } while (qds1A.next());
    setObrStrings();
  }

  private String getStringFormattedLikeVariant(DataSet set,String colName) {
    com.borland.dx.text.VariantFormatter formater = set.getColumn(colName).getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);
    return formater.format(v);
  }

  String OBRSTOPA, OBRIZNOS, OBROSN;

  public void setObrStrings(){
    OBRSTOPA = "";
    OBROSN = "";
    OBRIZNOS = "";
    for (repQDSPrirezi1.first(); repQDSPrirezi1.inBounds(); repQDSPrirezi1.next()) {
      OBRSTOPA = OBRSTOPA.concat(getStringFormattedLikeVariant(repQDSPrirezi1,"OBRSTOPA")).concat("\n");
      OBROSN = OBROSN.concat(getStringFormattedLikeVariant(repQDSPrirezi1,"OBROSN")).concat("\n");
      OBRIZNOS = OBRIZNOS.concat(getStringFormattedLikeVariant(repQDSPrirezi1,"OBRIZNOS")).concat("\n");
    }
  }

  public String getObrstopa(){
    return OBRSTOPA;
  }

  public String getObrosn(){
    return OBROSN;
  }

  public String getObriznos(){
    return OBRIZNOS;
  }


  public void setReportProviders() {
    killAllReports();
    this.addReport("hr.restart.pl.repRekDopPorDOP", "Lista doprinosa zbirni ispis", 2);
    this.addReport("hr.restart.pl.repRekDopPorDOPRO", "Lista doprinosa po vrstama radnog odnosa", 2);
    this.addReport("hr.restart.pl.repRekDopPorPORPRI01", "Lista poreza i prireza po op\u0107inama", 2);
    this.addReport("hr.restart.pl.repRekDopPorPORPRI02", "Lista poreza i prireza", 2);
  }
}