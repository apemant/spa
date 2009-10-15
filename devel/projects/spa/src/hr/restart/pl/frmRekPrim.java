/****license*****************************************************************
**   file: frmRekPrim.java
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
import hr.restart.util.Valid;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmRekPrim extends frmIzvjestajiPL {

  Valid vl = Valid.getValid();

  QueryDataSet repQDS01= new QueryDataSet();
  QueryDataSet repQDS02= new QueryDataSet();
  QueryDataSet repQDS02A= new QueryDataSet();
  StorageDataSet repQDS02Konkat = new StorageDataSet();
  QueryDataSet repQDS03= new QueryDataSet();
  QueryDataSet repQDS03A= new QueryDataSet();
  QueryDataSet repQDSsumaSuma= new QueryDataSet();
  StorageDataSet repQDS03Konkat = new StorageDataSet();


  String qstr01;
  String qstr02;
  String qstr02A;
  String qstr03;
  String qstr03A;
  String qstr03SS;

  public frmRekPrim() {
    this('O');
  }
  public frmRekPrim(char mode) {
    super(mode);
    isArh = mode == 'A';
    if (!isArh) {
      frkpr = this;
      setReportProviders();
    }
    setDifolt();
  }

  static frmRekPrim frkpr;

  static boolean isArh = false;

  public static frmRekPrim getFrmRekPrim() {
    if (isArh) return frmRekPrimA.getInstanceA();
    return frkpr;
  }

  public void okPress() {
    isArh = getRepMode() == 'A';
    setStorige02(repQDS02, repQDS02A);
    setStorige03(repQDS03, repQDS03A, repQDS02A, repQDSsumaSuma);

    super.okPress();
  }

  private String getTableName() {
    if (getRepMode() == 'A') return "primanjaarh";
    else return "primanjaobr";
  }
  public boolean Validacija(){

    if (!super.Validacija()) return false;

    qstr01 = "SELECT max(cradnik) as cradnik, cvrp, '" + fieldSet.getString("CORG") + "' as corg, sum(sati) as sati,  " +
             "sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto) as neto FROM " + getTableName() + " WHERE " +
             getWhereQuery() + " GROUP BY cvrp";

    qstr02 = "SELECT max(cradnik) as cradnik, cvrp, corg, sum(sati) as sati,  " +
             "sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto) as neto FROM " + getTableName() + " WHERE " +
             getWhereQuery() + " GROUP BY corg, cvrp";

    qstr02A= "SELECT corg, sum(sati) as sumsati,  sum(bruto) as sumbruto, sum(doprinosi) as sumdoprinosi, "+
             "sum(neto) as sumneto FROM " + getTableName() + " WHERE " +
             getWhereQuery() + " GROUP BY corg";

    qstr03 = "SELECT cradnik, cvrp, corg, sati,  " +
             "bruto, doprinosi, neto FROM " + getTableName() + " WHERE " +
             getWhereQuery() + " ORDER BY cradnik";

    qstr03A= "SELECT cradnik, sum(sati) as sumsati,  sum(bruto) as sumbruto, sum(doprinosi) as sumdoprinosi, "+
             "sum(neto) as sumneto FROM " + getTableName() + " WHERE " +
             getWhereQuery() + " GROUP BY cradnik";

    qstr03SS= "SELECT sum(sati) as sumsati,  sum(bruto) as sumbruto, sum(doprinosi) as sumdoprinosi, "+
              "sum(neto) as sumneto FROM " + getTableName() + " WHERE " +
              getWhereQuery();

    repQDS01 = Util.getNewQueryDataSet(qstr01);
    repQDS02 = Util.getNewQueryDataSet(qstr02);
    repQDS02A = Util.getNewQueryDataSet(qstr02A);
    repQDS03 = Util.getNewQueryDataSet(qstr03);
    repQDS03A = Util.getNewQueryDataSet(qstr03A);
    repQDSsumaSuma = Util.getNewQueryDataSet(qstr03SS);

    if (repQDS01.isEmpty() || repQDS02.isEmpty() || repQDS03.isEmpty()){
      JOptionPane.showConfirmDialog(this.getJPan(),
                                    "Nema podataka za prikaz!",
                                    "Upozorenje",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  Column cradnik = new Column();
  Column cvrp = new Column();
  Column corg = new Column();
  Column sati = new Column();
  Column bruto = new Column();
  Column doprinosi = new Column();
  Column neto = new Column();
  Column sumsati = new Column();
  Column sumbruto = new Column();
  Column sumdoprinosi = new Column();
  Column sumneto = new Column();

  void setStorige02(QueryDataSet qds, QueryDataSet qdsA){

    try {
//      cradnik.setColumnName("CRADNIK");
//      cradnik.setDataType(com.borland.dx.dataset.Variant.STRING);

      cradnik = dm.createStringColumn("CRADNIK",0);

//      cvrp.setColumnName("CVRP");
//      cvrp.setDataType(com.borland.dx.dataset.Variant.SHORT);

      cvrp = dm.createShortColumn("CVRP");

//      corg.setColumnName("CORG");
//      corg.setDataType(com.borland.dx.dataset.Variant.STRING);

      corg = dm.createStringColumn("CORG",0);

//      sati.setColumnName("SATI");
//      sati.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      sati = dm.createBigDecimalColumn("SATI");

//      bruto.setColumnName("BRUTO");
//      bruto.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      bruto = dm.createBigDecimalColumn("BRUTO");

//      doprinosi.setColumnName("DOPRINOSI");
//      doprinosi.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      doprinosi = dm.createBigDecimalColumn("DOPRINOSI");

//      neto.setColumnName("NETO");
//      neto.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      neto = dm.createBigDecimalColumn("NETO");

//      sumsati.setColumnName("SUMSATI");
//      sumsati.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      sumsati = dm.createBigDecimalColumn("SUMSATI");

//      sumbruto.setColumnName("SUMBRUTO");
//      sumbruto.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      sumbruto = dm.createBigDecimalColumn("SUMBRUTO");

//      sumdoprinosi.setColumnName("SUMDOPRINOSI");
//      sumdoprinosi.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      sumdoprinosi = dm.createBigDecimalColumn("SUMDOPRINOSI");

//      sumneto.setColumnName("SUMNETO");
//      sumneto.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      sumneto = dm.createBigDecimalColumn("SUMNETO");

      repQDS02Konkat.setColumns(new Column[] {cradnik, cvrp, corg, sati, bruto,
                                              doprinosi, neto, sumsati, sumbruto, sumdoprinosi, sumneto});

      repQDS02Konkat.open();

    }
    catch (Exception ex) {
      repQDS02Konkat.deleteAllRows();
    }

    qdsA.first();
    do{
      qds.first();
      do{
        if(qds.getString("CORG").equals(qdsA.getString("CORG"))){
          repQDS02Konkat.insertRow(false);
          repQDS02Konkat.setString("CRADNIK", qds.getString("CRADNIK"));
          repQDS02Konkat.setShort("CVRP", qds.getShort("CVRP"));
          repQDS02Konkat.setString("CORG", qds.getString("CORG"));
          repQDS02Konkat.setBigDecimal("SATI", qds.getBigDecimal("SATI"));
          repQDS02Konkat.setBigDecimal("BRUTO", qds.getBigDecimal("BRUTO"));
          repQDS02Konkat.setBigDecimal("DOPRINOSI", qds.getBigDecimal("DOPRINOSI"));
          repQDS02Konkat.setBigDecimal("NETO", qds.getBigDecimal("NETO"));
          repQDS02Konkat.setBigDecimal("SUMSATI", qdsA.getBigDecimal("SUMSATI"));
          repQDS02Konkat.setBigDecimal("SUMBRUTO", qdsA.getBigDecimal("SUMBRUTO"));
          repQDS02Konkat.setBigDecimal("SUMDOPRINOSI", qdsA.getBigDecimal("SUMDOPRINOSI"));
          repQDS02Konkat.setBigDecimal("SUMNETO", qdsA.getBigDecimal("SUMNETO"));
        }
      } while (qds.next());
    } while (qdsA.next());
  }

  Column cradnik2 = new Column();
  Column cvrp2 = new Column();
  Column corg2 = new Column();
  Column sati2 = new Column();
  Column bruto2 = new Column();
  Column doprinosi2 = new Column();
  Column neto2 = new Column();
  Column sumsati2 = new Column();
  Column sumbruto2 = new Column();
  Column sumdoprinosi2 = new Column();
  Column sumneto2 = new Column();
  Column sumsati2Corg = new Column();
  Column sumbruto2Corg = new Column();
  Column sumdoprinosi2Corg = new Column();
  Column sumneto2Corg = new Column();

  Column sumsumSati = new Column();
  Column sumsumDopr = new Column();
  Column sumsumbruto = new Column();
  Column sumsumNeto = new Column();

  void setStorige03(QueryDataSet qds, QueryDataSet qdsA, QueryDataSet qdsB, QueryDataSet suma){

    try {
      cradnik2 = dm.createStringColumn("CRADNIK",0);
      cvrp2 = dm.createShortColumn("CVRP");
      corg2 = dm.createStringColumn("CORG",0);
      sati2 = dm.createBigDecimalColumn("SATI");
      bruto2 = dm.createBigDecimalColumn("BRUTO");
      doprinosi2 = dm.createBigDecimalColumn("DOPRINOSI");
      neto2 = dm.createBigDecimalColumn("NETO");
      sumsati2 = dm.createBigDecimalColumn("SUMSATI");
      sumbruto2 = dm.createBigDecimalColumn("SUMBRUTO");
      sumdoprinosi2 = dm.createBigDecimalColumn("SUMDOPRINOSI");
      sumneto2 = dm.createBigDecimalColumn("SUMNETO");
      sumsati2Corg = dm.createBigDecimalColumn("SUMSATICORG");
      sumbruto2Corg = dm.createBigDecimalColumn("SUMBRUTOCORG");
      sumdoprinosi2Corg = dm.createBigDecimalColumn("SUMDOPRINOSICORG");
      sumneto2Corg = dm.createBigDecimalColumn("SUMNETOCORG");
      sumsumSati = dm.createBigDecimalColumn("SUMSUMSATI");
      sumsumDopr = dm.createBigDecimalColumn("SUMSUMDOPR");
      sumsumbruto = dm.createBigDecimalColumn("SUMSUMBRUTO");
      sumsumNeto = dm.createBigDecimalColumn("SUMSUMNETO");

      repQDS03Konkat.setColumns(new Column[] {cradnik2, cvrp2, corg2, sati2, bruto2,
                                              doprinosi2, neto2, sumsati2, sumbruto2, sumdoprinosi2, sumneto2,
                                              sumsati2Corg, sumbruto2Corg, sumdoprinosi2Corg, sumneto2Corg,
                                              sumsumSati, sumsumDopr, sumsumbruto, sumsumNeto});

      repQDS03Konkat.open();

    }
    catch (Exception ex) {
      repQDS03Konkat.deleteAllRows();
    }

    qds.first();
    do{
      qdsA.first();
      do{
        if(qds.getString("CRADNIK").equals(qdsA.getString("CRADNIK"))){
          qdsB.first();
          do{
            if(qds.getString("CORG").equals(qdsB.getString("CORG"))){
                repQDS03Konkat.insertRow(false);
                repQDS03Konkat.setString("CRADNIK", qds.getString("CRADNIK"));
                repQDS03Konkat.setShort("CVRP", qds.getShort("CVRP"));
                repQDS03Konkat.setString("CORG", qds.getString("CORG"));
                repQDS03Konkat.setBigDecimal("SATI", qds.getBigDecimal("SATI"));
                repQDS03Konkat.setBigDecimal("BRUTO", qds.getBigDecimal("BRUTO"));
                repQDS03Konkat.setBigDecimal("DOPRINOSI", qds.getBigDecimal("DOPRINOSI"));
                repQDS03Konkat.setBigDecimal("NETO", qds.getBigDecimal("NETO"));
                repQDS03Konkat.setBigDecimal("SUMSATI", qdsA.getBigDecimal("SUMSATI"));
                repQDS03Konkat.setBigDecimal("SUMBRUTO", qdsA.getBigDecimal("SUMBRUTO"));
                repQDS03Konkat.setBigDecimal("SUMDOPRINOSI", qdsA.getBigDecimal("SUMDOPRINOSI"));
                repQDS03Konkat.setBigDecimal("SUMNETO", qdsA.getBigDecimal("SUMNETO"));
                repQDS03Konkat.setBigDecimal("SUMSATICORG", qdsB.getBigDecimal("SUMSATI"));
                repQDS03Konkat.setBigDecimal("SUMBRUTOCORG", qdsB.getBigDecimal("SUMBRUTO"));
                repQDS03Konkat.setBigDecimal("SUMDOPRINOSICORG", qdsB.getBigDecimal("SUMDOPRINOSI"));
                repQDS03Konkat.setBigDecimal("SUMNETOCORG", qdsB.getBigDecimal("SUMNETO"));

                repQDS03Konkat.setBigDecimal("SUMSUMSATI", suma.getBigDecimal("SUMSATI"));
                repQDS03Konkat.setBigDecimal("SUMSUMBRUTO", suma.getBigDecimal("SUMBRUTO"));
                repQDS03Konkat.setBigDecimal("SUMSUMDOPR", suma.getBigDecimal("SUMDOPRINOSI"));
                repQDS03Konkat.setBigDecimal("SUMSUMNETO", suma.getBigDecimal("SUMNETO"));
                break;
            }
          }while (qdsB.next());
        }
      } while (qdsA.next());
    } while (qds.next());
  }

  public DataSet getRepQDS01(){
    return repQDS01;
  }

  public DataSet getRepQDS02(){
    return repQDS02Konkat;
  }

  public DataSet getRepQDS03(){
    return repQDS03Konkat;
  }

  public String getIme(String cradnik){
    vl.execSQL("select ime, prezime from radnici where cradnik='" + cradnik + "'");
    vl.RezSet.open();
    String ime = vl.RezSet.getString("IME").concat(" ").concat(vl.RezSet.getString("PREZIME"));
    return ime;
  }

  public String getBezimeZaSort(String cradnik){
    vl.execSQL("select prezime from radnici where cradnik='" + cradnik + "'");
    vl.RezSet.open();
    return vl.RezSet.getString("PREZIME");
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
      return  "Obra\u010Dun za ".concat(mjOd).concat("-").concat(""+fieldSet.getShort("GODINAOD")).concat("/"+fieldSet.getShort("RBROD"));
    } else {
      return  "Obra\u010Dun za ".concat(mjOd).concat("-").concat(""+
          fieldSet.getShort("GODINAOD")).concat(" do ").concat(mjDo).concat("-").concat(""+
              fieldSet.getShort("GODINADO")).concat(", rbr ").concat(""+fieldSet.getShort("RBROD")).concat("-"+
              fieldSet.getShort("RBRDO"));
    }
  }

  public String getFlag(){
    if(getRepMode() == 'O') return ("O");
    return ("A");
  }

  public String getNazivOrg(String corg){
    vl.execSQL("select naziv from orgstruktura where corg='" + corg + "'");
    vl.RezSet.open();
    return vl.RezSet.getString("NAZIV");
  }

  public String getNazivVrsteRada(short cvrp){
    vl.execSQL("select naziv from vrsteprim where cvrp=" + cvrp);
    vl.RezSet.open();
    return vl.RezSet.getString("NAZIV");
  }

  public void setReportProviders() {
    killAllReports();
    this.addReport("hr.restart.pl.repRekPrimCVRP", "Rekapitulacija po vrstama primanja", 2);
    this.addReport("hr.restart.pl.repRekPrimCORG", "Rekapitulacija po organizacijskim jedinicama", 2);
    this.addReport("hr.restart.pl.repRekPrimCRADNIK", "Rekapitulacija po radnicima", 2);
  }
}