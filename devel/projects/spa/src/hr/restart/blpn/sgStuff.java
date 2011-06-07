/****license*****************************************************************
**   file: sgStuff.java
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
package hr.restart.blpn;

import hr.restart.baza.Blagajna;
import hr.restart.baza.Condition;
import hr.restart.baza.Stavblag;
import hr.restart.baza.dM;
import hr.restart.baza.raDataSet;
import hr.restart.db.raVariant;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 *  Description of the Class
 *
 *@author     Administrator
 *@created    2002. rujan 25
 */
public class sgStuff {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();

  /**
   *  Staticki getter
   *
   *@return    sgStuff :)
   */
  public static sgStuff getStugg() {
    if (stuff == null) {
      stuff = new sgStuff();
    }
    return stuff;
  }
  static sgStuff stuff;

  /**
   *  Constructor for the sgStuff object
   */
  protected sgStuff() {
    stuff = this;
  }

  /**
   *  Vraca iznose dnevnica, nocenja, troska po kilometru i litru benzina (BigDecimal)<br>
   *  Te oznaku valute u datasetu.<br>
   *  Koristi se u <br><b>
   *  frmObracunPN<br>
   *  frmPrijavaPN</b>
   *
   *@param  czemlje  Oznaka zemlje
   *@return          Dataset sa kolonama DNEVNICA, NOCENJE, LOCO, LITBENZ i OZNVAL
   */
  public QueryDataSet DNKL(String czemlje) {
    String qstr = "select zemlje.dnevnica, zemlje.nocenje, zemlje.loco, zemlje.litbenz, zemlje.oznval from zemlje where zemlje.czemlje='" + czemlje + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Prebrojava stavke u tablici <b>STAVKEPN</b> kojima je ISPL='N'<br>
   *  Query : <pre> select count(*) from stavkepn where cpn='1-2002-00006-I' and ispl='N' </pre>
   *  Koristi se u <br><b>
   *  frmUplIspl<br>
   *  frmObracunPN</b>
   *
   *
   *@param  cpn  Broj putnog naloga
   *@return      broj neisplacenih stavki
   */
  public int brojNeisplacenihStavkiPN(String cpn) {
    String qstr = "select count(*) from stavkepn where cpn='" + cpn + "' and ispl='N'";
    QueryDataSet bnis = ut.getNewQueryDataSet(qstr);
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet.getInt("COUNT");
    return vl.getSetCount(bnis,0);
  }

//  public int brojPutnogNaloga(String knjig, short godina) {
//    String qstr = "select max(putninalog.broj) as broj from putninalog where knjig ='" + knjig + "' and godina ='" + godina + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet.getInt("BROJ") + 1;
//  }

  /**
   *  Vraca boolean u validaciju mastera u <b>frmUplIspl</b><br>
   *  Ako je uneseni datum veci od datuma u tablici blagajna.<br>
   *
   *@param  master   dataset koji sadrzi KNJIG, CBLAG, OZNVAL i DATOD
   *@return          !vl.RezSet.getTimestamp("DATIZV").before(master.getTimestamp("DATOD")) <b>true</b> or <b>false</b>
   */
  public boolean checkDates(StorageDataSet master) {
    String qstr = "select blagajna.datizv from blagajna where blagajna.knjig='" +
        master.getString("KNJIG") +
        "' and blagajna.cblag=" + master.getInt("CBLAG") +
        " and blagajna.oznval='" + master.getString("OZNVAL") + "' ";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return !vl.RezSet.getTimestamp("DATIZV").before(master.getTimestamp("DATOD"));
  }

//  public String cpnCPRIJSRED(String cpn) {
//    String qstr = "select cprijsred from putninalog where cpn='" + cpn + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet.getString("CPRIJSRED");
//  }

//  public String cpnCRADNIK(String cpn) {
//    String qstr = "select cradnik from putninalog where cpn='" + cpn + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet.getString("CRADNIK");
//  }

  /**
   *  Klasicno preracunavanje iznosa iz valute u valutu po tecaju na dan (ako je unesen) ili<br>
   *  zadnji uneseni do dana. Vraca big decimal protuvrjednosti u toCurrency iznosa u fromCurrency<br>
   *  Primjeri koristenja u klasama:
   *  <b> frmAkontacija<br>
   *  <b> frmObracunPN</b>
   *
   *@param  iznos         Iznos za preracunavanje
   *@param  fromCurrency  Valuta u kojoj je iznos
   *@param  toCurrency    Valuta u kojoj je protuvrijednost
   *@param  datum         Datum za tecaj
   *@return               Protuvrijednost zadanog iznosa u toCurrency valuti
   */
  public BigDecimal currencyConverter(BigDecimal iznos, String fromCurrency, String toCurrency, Timestamp datum) {
    BigDecimal protuVrijednost1;
    BigDecimal protuVrijednost2;
    try {
      if (toCurrency.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
        protuVrijednost1 = iznos.multiply(getTECAJ(fromCurrency, datum)).divide(getJEDVAL(fromCurrency), 2, BigDecimal.ROUND_HALF_UP);
        return protuVrijednost1;
      } else {
        if (getJEDVAL(fromCurrency).compareTo(new BigDecimal(100)) == 0) {
          protuVrijednost1 = iznos.multiply(getTECAJ(fromCurrency, datum)).divide(getJEDVAL(fromCurrency), 4, BigDecimal.ROUND_HALF_UP);
          //        protuVrijednost1 = protuVrijednost1.setScale(2,BigDecimal.ROUND_HALF_UP);
//          System.out.println("pv1 " + protuVrijednost1);
        } else {
          protuVrijednost1 = iznos.multiply(getTECAJ(fromCurrency, datum)).divide(getJEDVAL(fromCurrency), 2, BigDecimal.ROUND_HALF_UP);
        }
      }
      if (fromCurrency.equals(hr.restart.zapod.Tecajevi.getDomOZNVAL())) {
        if (getJEDVAL(toCurrency).compareTo(new BigDecimal(100)) == 0) {
          protuVrijednost2 = iznos.divide(getTECAJ(toCurrency, datum), 4, BigDecimal.ROUND_HALF_UP).multiply(getJEDVAL(toCurrency));
          protuVrijednost2 = protuVrijednost2.setScale(2);
        } else {
          protuVrijednost2 = iznos.divide(getTECAJ(toCurrency, datum), 2, BigDecimal.ROUND_HALF_UP).multiply(getJEDVAL(toCurrency));
        }
        return protuVrijednost2;
      }
      if (getJEDVAL(toCurrency).compareTo(new BigDecimal(100)) == 0) {
        protuVrijednost2 = protuVrijednost1.divide(getTECAJ(toCurrency, datum), 4, BigDecimal.ROUND_HALF_UP).multiply(getJEDVAL(toCurrency));
        protuVrijednost2 = protuVrijednost2.setScale(2);
//        System.out.println("pv2 " +protuVrijednost2);
      } else {
        protuVrijednost2 = protuVrijednost1.divide(getTECAJ(toCurrency, datum), 2, BigDecimal.ROUND_HALF_UP).multiply(getJEDVAL(toCurrency));
      }
      return protuVrijednost2;
    } catch (Exception ex) {
//      javax.swing.JOptionPane.showMessageDialog(null,
//          new hr.restart.swing.raMultiLineMessage(new String[]{"Valuta možda nije unesena pravilno ili", "nema te\u010Daja za doti\u010Dnu valutu ili", "nešto tre\u0107e.",
//          "", "Provjerite valutu i te\u010Daj u zajedni\u010Dkim podacima!", "", "Protuvrijednost je nula"}),
//          "Greška",
//          javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    return new BigDecimal(0);
  }

  /**
   *  Klasicno preracunavanje iznosa iz domace valute (KUNA) u valutu po tecaju na dan (ako je unesen) ili<br>
   *  zadnji uneseni do dana. Vraca big decimal protuvrjednosti u toCurrency iznosa u domacoj valuti (KN)<br>
   *  Primjeri koristenja u klasama:
   *
   *@param  iznos         Iznos za preracunavanje
   *@param  toCurrency    Valuta u kojoj je protuvrijednost
   *@param  datum         Datum za tecaj
   *@return               Protuvrijednost zadanog iznosa u toCurrency valuti
   */
  public BigDecimal currrencyConverterFromKN(BigDecimal iznos, String toCurrency, Timestamp datum) {
    return currencyConverter(iznos, hr.restart.zapod.Tecajevi.getDomOZNVAL(), toCurrency, datum);
  }

  /**
   *  Klasicno preracunavanje iznosa iz valute u domacu valutu (KUNA) po tecaju na dan (ako je unesen) ili<br>
   *  zadnji uneseni do dana. Vraca big decimal protuvrjednosti u domacoj valuti (KN) iznosa u fromCurrency <br>
   *  Primjeri koristenja u klasama:
   *  <b>frmUplIspl<br>
   *  frmAkontacijaPN<br>
   *  frmObracunPN<br>
   *  frmRazlikaPN</b>
   *
   *@param  iznos         Iznos za preracunavanje
   *@param  fromCurrency  Valuta iznosa
   *@param  datum         Datum za tecaj
   *@return               Protuvrijednost zadanog iznosa u domacoj valuti (KN)
   */
  public BigDecimal currrencyConverterToKN(BigDecimal iznos, String fromCurrency, Timestamp datum) {
    return currencyConverter(iznos, fromCurrency, hr.restart.zapod.Tecajevi.getDomOZNVAL(), datum);
  }

  /**
   *  rezSet iz qstringa <b>select putninalog.akontacija from putninalog where cpn ='1-2002-00251-Z'</b>
   *  primjer : <b> frmAkontacijaPN</b>
   *
   *@param  cpn  Broj putnog naloga
   *@return      vl.RezSet.getBigDecimal("AKONTACIJA") - znaci protuvrijednost akontacije u kunama
   */
  public BigDecimal getAkontacija(String cpn) {
    String qstr = "select czemlje, trajanje, akontacija from putninalog where cpn ='" + cpn + "'";
    QueryDataSet qds = ut.getNewQueryDataSet(qstr);
    if (qds.getBigDecimal("AKONTACIJA").compareTo(new BigDecimal(0)) != 0) {
      return qds.getBigDecimal("AKONTACIJA");
    } else {
      String zstr = "SELECT dnevnica, nocenje FROM Zemlje WHERE czemlje='" + qds.getString("CZEMLJE") + "'";
      QueryDataSet qdz = ut.getNewQueryDataSet(zstr);

      BigDecimal trajanje = new BigDecimal(String.valueOf(qds.getShort("TRAJANJE")));
      BigDecimal dnev = qdz.getBigDecimal("DNEVNICA");
      BigDecimal nocen = qdz.getBigDecimal("NOCENJE");

      BigDecimal akontir = dnev.multiply(trajanje).add(nocen.multiply(trajanje.subtract(new BigDecimal(1))));
      return akontir;
    }
  }

  /**
   *  Gets the akontacijaValuta attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The akontacijaValuta value
   */
  public String getAkontacijaValuta(String cpn) {
    String qstr = "select putninalog.czemlje from putninalog where cpn ='" + cpn + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return getOZNVAL(vl.RezSet.getString("CZEMLJE"));
  }

  /**
   *  Gets the akontacijaZaObracun attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The akontacijaZaObracun value
   */
  public DataSet getAkontacijaZaObracun(String cpn) {
    String qstr = "select sum(izdatak) as izdatak, sum(pvizdatak) as pvizdatak, oznval from stavblag where cpn='" + cpn +
        "' and cskl='6' and stavka='1' group by oznval union select sum(izdatak) as izdatak,  sum(pvizdatak) as pvizdatak, oznval from stavkeblarh where cpn='" + cpn +
        "' and cskl='6' and stavka='1' group by oznval";
    QueryDataSet qds = ut.getNewQueryDataSet(qstr);
    return qds;
  }

  /**
   *  Gets the blagajneZaValutu attribute of the sgStuff object
   *
   *@param  oznval  Description of the Parameter
   *@param  knjig   Description of the Parameter
   *@return         The blagajneZaValutu value
   */
  public QueryDataSet getBlagajneZaValutu(String oznval, String knjig) {
    String qstr = "select blagajna.cblag, blagajna.naziv from blagajna where blagajna.oznval='" + oznval + "' and blagajna.knjig='" + knjig + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Gets the brizv attribute of the sgStuff object<br>iliti drugin ricima vraca zadnji broj aktivnog (znaci, ne knjizenog) blagajnickog izvjestaja
   *  zbog radi dodavanje stavke <b>BAS U TAJ IZVJESTAJ</b><br>Parametri su junik kljuc tablice blagizv.
   *
   *@param  cblag   Broj blagajne
   *@param  knjig   Knjigovodstvo pod kojim je blagajna
   *@param  oznval  Valuta koju hendla blagajna
   *@param  god     Godina u kojoj se radi hendlanje
   *@return         Broj blagajnickog izvjestaja, koji je usput budi receno aktivan :))
   */
  public int getBrizv(int cblag, String knjig, String oznval, short god) {
    String qstr = "select max(brizv) as brizv from blagizv "+
                  "where cblag = " + cblag +
                  " and knjig = '" + knjig +
                  "' and oznval = '" + oznval +
                  "' and godina = " + god +
                  " and status = 'U'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getInt("BRIZV");
  }

  /**
   *  Gets the brojKonta attribute of the sgStuff object
   *
   *@param  stavka  Description of the Parameter
   *@param  cskl    Description of the Parameter
   *@return         The brojKonta value
   */
  public String getBrojKonta(String stavka, String cskl) {
    String qstr = "select brojkonta from shkonta where cskl='" + cskl + "' and vrdok='BL' and stavka='" + stavka + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("BROJKONTA");
  }

  /**
   *  Gets the datumAkontacije attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The datumAkontacije value
   */
  public java.sql.Timestamp getDatumAkontacije(String cpn) {
    String qstr = "select max(datum) as datum from stavblag where cskl='6' and stavka='1' and cpn='" + cpn +
        "' union select max(datum) as datum from stavkeblarh where cskl='6' and stavka='1' and cpn='" + cpn + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getTimestamp("DATUM");
  }

  /**
   *  Gets the datumObradePNa attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The datumObradePNa value
   */
  public java.sql.Timestamp getDatumObradePNa(String cpn) {
    String qstr = "select datobr from putninalog where cpn='" + cpn + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getTimestamp("DATOBR");
  }

  /**
   *  Gets the detailSetPN attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The detailSetPN value
   */
  public String getDetailSetPN(String cpn) {
    String qstr = "select * from stavkepn where  cpn='" + cpn + "' order by cskl, rbs";
    vl.execSQL(qstr);
    vl.RezSet.open();

    if (vl.RezSet.rowCount() == 0) {
      return "select * from stavpnarh where  cpn='" + cpn + "' order by cskl, rbs";
    }

    return qstr;
  }

  /**
   *  Gets the ime attribute of the sgStuff object
   *
   *@param  cradnik  Description of the Parameter
   *@return          The ime value
   */
  public String getIme(String cradnik) {
    String qstr = "select ime, prezime from radnici where cradnik='" + cradnik + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.isEmpty()) {
      return "";
    }
    String imPr = vl.RezSet.getString("IME").concat(" ").concat(vl.RezSet.getString("PREZIME"));
    return imPr;
  }

//  public DataSet getPutnikPutnogNaloga(String cpn){
//    String qstr = "select cradnik from putninalog where cpn='" + cpn + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet;
//  }

  /**
   *  Gets the isplacenaRazlika attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The isplacenaRazlika value
   */
  public DataSet getIsplacenaRazlika(String cpn) {
    String qstr = "select sum(primitak) as primitak, sum(izdatak) as izdatak, oznval from stavblag where cpn='" + cpn +
        "' and cskl='6' and stavka='2' group by oznval union select sum(primitak) as primitak, sum(izdatak) as izdatak, oznval from stavkeblarh where cpn='" + cpn +
        "' and cskl='6' and stavka='2' group by oznval";
//    System.out.println("QSTR za razliku \n" + qstr);
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Gets the jEDVAL attribute of the sgStuff object
   *
   *@param  OZNVAL  Description of the Parameter
   *@return         The jEDVAL value
   */
  public BigDecimal getJEDVAL(String OZNVAL) {
    String qstr = "select valute.jedval from valute where oznval='" + OZNVAL + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return new BigDecimal(vl.RezSet.getInt("JEDVAL"));
  }

  /**
   *  Gets the masterSetPN attribute of the sgStuff object
   *
   *@param  cpn  Description of the Parameter
   *@return      The masterSetPN value
   */
  public String getMasterSetPN(String cpn) {
    String qstr = "select * from putninalog where  cpn='" + cpn + "' ";
    /*
     *  "union " +
     *  "select * from putnalarh where  cpn='" + cpn + "'";
     */
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.rowCount() == 0) {
      return "select * from putnalarh where  cpn='" + cpn + "'";
    }
    return qstr;
  }

  /**
   *  Gets the nazivBlagajne attribute of the sgStuff object
   *
   *@param  presset  Description of the Parameter
   *@return          The nazivBlagajne value
   */
  public String getNazivBlagajne(StorageDataSet presset) {
    return "select blagajna.naziv from blagajna where knjig='"
         + presset.getString("KNJIG") +
        "' and cblag=" + presset.getInt("CBLAG") +
        " and oznval='" + presset.getString("OZNVAL") + "'";
  }

//  /**
//   *  Gets the nazivPrevoza attribute of the sgStuff object
//   *
//   *@param  csif  Description of the Parameter
//   *@return       The nazivPrevoza value
//   */
//  public String getNazivPrevoza(String csif) {
//    String qstr = "select  naziv from sifrarnici where vrstasif = 'BLPS' and csif='" + csif + "'";
//    return "";
//  }

  /**
   *  Gets the nextRBSstavblag attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  cblag   Description of the Parameter
   *@param  oznval  Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  brizv   Description of the Parameter
   *@return         The nextRBSstavblag value
   */
  public int getNextRBSstavblag(String knjig, int cblag, String oznval, short god, int brizv, String uplispl) {
    
    String separation = "";
    if (isSepNumUI()){
      separation = " and vrsta = '"+uplispl+"'"; 
    } 
    
    String qstr = "select max(stavblag.rbs) as rbs from stavblag where knjig='" + knjig + 
    "' and cblag=" + cblag + " and oznval='" + oznval + "' and godina=" + god + 
    " and brizv=" + brizv + ""+ separation;
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getInt("RBS") + 1;
  }

  /**
   *  Gets the nextRBSstavkepn attribute of the sgStuff object
   *
   *@param  knjig    Description of the Parameter
   *@param  god      Description of the Parameter
   *@param  brojnal  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return          The nextRBSstavkepn value
   */
  public short getNextRBSstavkepn(String knjig, short god, int brojnal, String indputa) {
    String qstr = "select max(stavkepn.rbs) as rbs from stavkepn where stavkepn.knjig ='" + knjig +
        "' and stavkepn.godina=" + god +
        " and stavkepn.broj=" + brojnal +
        " and stavkepn.indputa='" + indputa + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    int nextRBS = vl.RezSet.getShort("RBS") + 1;
    short nextShortRBS = (short) nextRBS;
//    System.out.println("nextrbs = " + nextShortRBS);
    return nextShortRBS;
  }

  /**
   *  Gets the noviBrIzv attribute of the sgStuff object
   *
   *@param  presset  Description of the Parameter
   *@return          The noviBrIzv value
   */
  public int getNoviBrIzv(StorageDataSet presset) {
    //TODO ovo ishendlat da na poèetku godine pita, ili nešto slièno :)
    /*String qstr = "select max(blagajna.brizv) as brizv from blagajna where blagajna.knjig='" +
        presset.getString("KNJIG") +
        "' and blagajna.cblag='" + presset.getInt("CBLAG") +
        "' and blagajna.oznval='" + presset.getString("OZNVAL") + "'";*/
    
    
    String qstr = "SELECT max(blagizv.brizv) as brizv FROM Blagizv WHERE " +
            "knjig ='"+presset.getString("KNJIG")+"' and " +
            "cblag = "+presset.getInt("CBLAG")+" and " +
            "oznval = '"+presset.getString("OZNVAL")+"' and " +
            "godina = "+vl.findYear(presset.getTimestamp("DATOD-from"))+"";
    
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getInt("BRIZV") < 1) {
      QueryDataSet blags = Blagajna.getDataModule().getFilteredDataSet(Condition.whereAllEqual(new String[] {"KNJIG","CBLAG","OZNVAL"}, presset));
      blags.open();
      if (blags.getRowCount() == 0) {
        System.out.println("!!!ERRROR: Nema blagajne!!!");
      } else {
        if (vl.findYear(blags.getTimestamp("DATIZV")).equals(vl.findYear(presset.getTimestamp("DATOD-from")))) {
          return blags.getInt("BRIZV")+1;
        } else {
          return 1;
        }
      }
    }
    return vl.RezSet.getInt("BRIZV") + 1;
  }
  public boolean isSepNumUI() {
    return hr.restart.sisfun.frmParam.getParam("blpn","SepNumUI","N",
        "Odvojeni brojevi uplatnica od isplatnica (D/N)").equals("D");
  }
  /**
   *  Gets the noviBrStav attribute of the sgStuff object
   *
   *@param  master   Description of the Parameter
   *@param  presset  Description of the Parameter
   *@return          The noviBrStav value
   */
  public int getNoviBrStav(QueryDataSet master, String vrsta) {
    
    String separation1 = "";
    String separation2 = "";
    if (isSepNumUI()){
      separation1 = "and stavblag.vrsta='" + vrsta + "'";
      separation2 = "and stavkeblarh.vrsta='" + vrsta + "'"; 
    } else {
      separation1 = "and stavblag.brizv=" + master.getInt("BRIZV") + " ";
    }
    
    String qstr = "select max(stavblag.rbs) as rbs from stavblag where stavblag.knjig='" +
    	master.getString("KNJIG") +
        "' and stavblag.cblag=" + master.getInt("CBLAG") +
        " and stavblag.oznval='" + master.getString("OZNVAL") + "' " +
        "and stavblag.godina=" + master.getShort("GODINA") + " " +
    	separation1;

    vl.execSQL(qstr);
    vl.RezSet.open();
    
    int zivi = vl.RezSet.getInt("RBS");
    
    if (!isSepNumUI()) {
      if (vl.RezSet.getRowCount() == 0) return 1;
      return zivi + 1;
    }
    
    String qstr2 = "select max(Stavkeblarh.rbs) as rbs from Stavkeblarh where Stavkeblarh.knjig='" +
    	master.getString("KNJIG") +
        "' and Stavkeblarh.cblag=" + master.getInt("CBLAG") +
        " and Stavkeblarh.oznval='" + master.getString("OZNVAL") + "' " +
        "and Stavkeblarh.godina=" + master.getShort("GODINA") + " " +
    	separation2;
    
    vl.execSQL(qstr2);
    vl.RezSet.open();
    
    int mrtvi = vl.RezSet.getInt("RBS");
    
    if (zivi >= mrtvi) return zivi + 1;
    
    return mrtvi + 1;
  }

  /**
   *  Gets the oZNVAL attribute of the sgStuff object
   *
   *@param  czemlje  Description of the Parameter
   *@return          The oZNVAL value
   */
  public String getOZNVAL(String czemlje) {
    String qstr = "select oznval from zemlje where czemlje='" + czemlje + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("OZNVAL");
  }

  /**
   *  Gets the opisStavke attribute of the sgStuff object
   *
   *@param  cskl    Description of the Parameter
   *@param  stavka  Description of the Parameter
   *@return         The opisStavke value
   */
  public String getOpisStavke(String cskl, short stavka) {
    String qstr = "select opis from shkonta where vrdok='PN' and cskl='" + cskl + "' and stavka='" + stavka + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("OPIS");
  }

  /**
   *  Gets the oznvalPrikoBlagajne attribute of the sgStuff object
   *
   *@param  knjig  Description of the Parameter
   *@param  cblag  Description of the Parameter
   *@return        The oznvalPrikoBlagajne value
   */
  public String getOznvalPrikoBlagajne(String knjig, String cblag) {
    String qstr = "select oznval from blagajna where knjig='" + knjig + "' and cblag=" + cblag + "";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("OZNVAL");
  }

  /**
   *  Gets the pRIJENOS attribute of the sgStuff object
   *
   *@param  presset  Description of the Parameter
   *@return          The pRIJENOS value
   */
  public BigDecimal getPRIJENOS(StorageDataSet presset) {
    String qstr = "select blagajna.saldo from blagajna where blagajna.knjig='" +
        presset.getString("KNJIG") +
        "' and blagajna.cblag=" + presset.getInt("CBLAG") +
        " and blagajna.oznval='" + presset.getString("OZNVAL") + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getBigDecimal("SALDO");
  }

  /**
   *  Gets the pVPRIJENOS attribute of the sgStuff object
   *
   *@param  presset  Description of the Parameter
   *@return          The pVPRIJENOS value
   */
  public BigDecimal getPVPRIJENOS(StorageDataSet presset) {
    String qstr = "select blagajna.pvsaldo from blagajna where blagajna.knjig='" +
        presset.getString("KNJIG") +
        "' and blagajna.cblag=" + presset.getInt("CBLAG") +
        " and blagajna.oznval='" + presset.getString("OZNVAL") + "'";

    QueryDataSet qds = ut.getNewQueryDataSet(qstr);
    return qds.getBigDecimal("PVSALDO");
  }

  /**
   *  Gets the prijevoznoSredstvo attribute of the sgStuff object
   *
   *@param  cprsr  Description of the Parameter
   *@return        The prijevoznoSredstvo value
   */
  public String getPrijevoznoSredstvo(String cprsr) {
    String qstr = "select naziv from sifrarnici where csif='" + cprsr + "' and vrstasif='BLPS'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.isEmpty()) {
      return "";
    }
    String prSr = vl.RezSet.getString("NAZIV");
    return prSr;
  }

  /**
   *  Gets the repIsplatnicaZaAkontacijuRazliku attribute of the sgStuff object
   *
   *@param  dejta  Description of the Parameter
   *@return        The repIsplatnicaZaAkontacijuRazliku value
   */
  public String getRepIsplatnicaZaAkontacijuRazliku(QueryDataSet dejta) {
    return "select blagizv.cblag, blagizv.godina, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, " +
        "blagizv.prijenos, blagizv.uksaldo, stavblag.rbs, stavblag.datum, stavblag.knjig, stavblag.opis, stavblag.primitak, " +
        "stavblag.izdatak, stavblag.stavka, stavblag.tko, stavblag.cskl, stavblag.oznval " +
        "from blagizv, stavblag " +
        "where stavblag.cpn='" + dejta.getString("CPN") + "' " +
        "and blagizv.cblag=" + dejta.getInt("CBLAG") + " " +
        "and blagizv.oznval='" + dejta.getString("OZNVAL") + "' " +
        "and blagizv.brizv=" + dejta.getInt("BRIZV") + " " +
        "and stavblag.rbs=" + dejta.getInt("RBS") + " " +
        "and stavblag.stavka='" + dejta.getString("STAVKA") + "' " +
        "and blagizv.cblag=stavblag.cblag " +
        "and blagizv.knjig=stavblag.knjig and blagizv.oznval=stavblag.oznval and blagizv.godina=stavblag.godina " +
        "and blagizv.brizv=stavblag.brizv ";
    // +
//        "union " +
//        "select blagizv.cblag, blagizv.godina, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, "+
//        "blagizv.prijenos, blagizv.uksaldo, stavkeblarh.rbs, stavkeblarh.datum, stavkeblarh.knjig, stavkeblarh.opis, stavkeblarh.primitak, "+
//        "stavkeblarh.izdatak, stavkeblarh.stavka, stavkeblarh.tko, stavkeblarh.cskl, stavkeblarh.oznval "+
//        "from blagizv, stavkeblarh " +
//        "where stavkeblarh.cpn='" + dejta.getString("CPN") + "' "+
//        "and blagizv.cblag='" + dejta.getInt("CBLAG") + "' " +
//        "and blagizv.oznval='" + dejta.getString("OZNVAL") + "' " +
//        "and blagizv.brizv='" + dejta.getInt("BRIZV") + "' " +
//        "and stavkeblarh.rbs='" + dejta.getInt("RBS") + "' " +
//        "and blagizv.cblag=stavkeblarh.cblag "+
//        "and blagizv.knjig=stavkeblarh.knjig and blagizv.oznval=stavkeblarh.oznval and blagizv.godina=stavkeblarh.godina "+
//        "and blagizv.brizv=stavkeblarh.brizv";
  }

  /**
   *  Gets the repQDSIzvjestaj attribute of the sgStuff object
   *
   *@param  master         Description of the Parameter
   *@param  detaljTablica  Description of the Parameter
   *@return                The repQDSIzvjestaj value
   */
  public String getRepQDSIzvjestaj(QueryDataSet master, String detaljTablica) {
    String q= "select blagizv.cblag, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, " +
        "blagizv.prijenos, blagizv.uksaldo, " + detaljTablica + ".rbs, " + detaljTablica + ".vrsta, " + detaljTablica + ".datum, " + detaljTablica + ".knjig, " + detaljTablica + ".opis, " + detaljTablica + ".primitak, " +
        "" + detaljTablica + ".izdatak, " + detaljTablica + ".stavka, " + detaljTablica + ".cskl, " +detaljTablica + ".brojkonta, " + detaljTablica + ".corg, "
        + detaljTablica + ".tko, " + detaljTablica + ".cpar, " + detaljTablica + ".brojdok, " + detaljTablica + ".datdok, " + detaljTablica + ".datdosp " + 
        "from blagizv, " + detaljTablica + " " +
        "where blagizv.knjig='" + master.getString("KNJIG") + "' and blagizv.cblag=" + master.getInt("CBLAG") +
        " and blagizv.oznval='" + master.getString("OZNVAL") + "' and blagizv.godina=" + master.getShort("GODINA") +
        " and blagizv.brizv=" + master.getInt("BRIZV") + " and blagizv.cblag=" + detaljTablica + ".cblag " +
        "and blagizv.knjig=" + detaljTablica + ".knjig and blagizv.oznval=" + detaljTablica + ".oznval and blagizv.godina=" + detaljTablica + ".godina " +
        "and blagizv.brizv=" + detaljTablica + ".brizv " + getOrderBlagIzv(detaljTablica);
    
    System.out.println("getRepQDSIzvjestaj :: "+q);
    return q;
  }

  private String getOrderBlagIzv(String tab) {
    String ostr = " ORDER BY "+tab+"."+getOrderBlagIzvField();
    return ostr;
  }
  public String getOrderBlagIzvField() {
    return frmParam.getParam("blpn", "orderblizv", isSepNumUI()?"DATUM":"RBS", "Po kojem polju sortirati stavke blag.izv RBS|DATUM|...");
  }

  /**
   *  Gets the repQDSJednaUplatnica attribute of the sgStuff object
   *
   *@param  master         Description of the Parameter
   *@param  detalj         Description of the Parameter
   *@param  detaljTablica  Description of the Parameter
   *@return                The repQDSJednaUplatnica value
   */
  public String getRepQDSJednaUplatnica(QueryDataSet master, QueryDataSet detalj, String detaljTablica) {
    return "select blagizv.cblag, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, " +
        "blagizv.prijenos, blagizv.uksaldo, " + detaljTablica + ".rbs, " + detaljTablica + ".datum, " + detaljTablica + ".knjig, " + detaljTablica + ".opis, " + detaljTablica + ".primitak, " +
        "" + detaljTablica + ".izdatak, " + detaljTablica + ".stavka, " + detaljTablica + ".cradnik, " + detaljTablica + ".tko, " + detaljTablica + ".cskl, " + detaljTablica + ".brojkonta " +
        "from blagizv, " + detaljTablica + " " +
        "where blagizv.knjig='" + master.getString("KNJIG") + "' and blagizv.cblag=" + master.getInt("CBLAG") +
        " and blagizv.oznval='" + master.getString("OZNVAL") + "' and blagizv.godina=" + master.getShort("GODINA") +
        " and blagizv.brizv=" + master.getInt("BRIZV") +
        " and " + detaljTablica + ".rbs=" + detalj.getInt("RBS") + " " +
        " and " + detaljTablica + ".vrsta='" + detalj.getString("VRSTA") + "' " +
        "and blagizv.cblag=" + detaljTablica + ".cblag " +
        "and blagizv.knjig=" + detaljTablica + ".knjig and blagizv.oznval=" + detaljTablica + ".oznval and blagizv.godina=" + detaljTablica + ".godina " +
        "and blagizv.brizv=" + detaljTablica + ".brizv";
  }

  /**
   *  Gets the repQDSJednaUplatnica attribute of the sgStuff object
   *
   *@param  master         Description of the Parameter
   *@param  detalj         Description of the Parameter
   *@param  detaljTablica  Description of the Parameter
   *@return                The repQDSJednaUplatnica value
   */
  public String getRepQDSSkupnaIsplatnica(QueryDataSet master, QueryDataSet detalj, String detaljTablica, String[] rbrs) {
    String rbrsin = "";
    for (int i = 0; i < rbrs.length; i++) {
      if (i < (rbrs.length - 1)) rbrsin += ""+rbrs[i]+", ";
      else rbrsin += rbrs[i];
    }
    
    return "select blagizv.cblag, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, " +
        "blagizv.prijenos, blagizv.uksaldo, " + detaljTablica + ".rbs, " + detaljTablica + ".datum, " + detaljTablica + ".knjig, " + detaljTablica + ".opis, " + detaljTablica + ".primitak, " +
        "" + detaljTablica + ".izdatak, " + detaljTablica + ".stavka, " + detaljTablica + ".cradnik, " + detaljTablica + ".tko, " + detaljTablica + ".cskl, " + detaljTablica + ".brojkonta "+
        "from blagizv, " + detaljTablica + " " +
        "where blagizv.knjig='" + master.getString("KNJIG") + "' and blagizv.cblag=" + master.getInt("CBLAG") +
        " and blagizv.oznval='" + master.getString("OZNVAL") + "' and blagizv.godina=" + master.getShort("GODINA") +
        " and blagizv.brizv=" + master.getInt("BRIZV") +
        " and " + detaljTablica + ".rbs in (" + rbrsin + ") " +
        " and " + detaljTablica + ".vrsta='I' " +
        "and blagizv.cblag=" + detaljTablica + ".cblag " +
        "and blagizv.knjig=" + detaljTablica + ".knjig and blagizv.oznval=" + detaljTablica + ".oznval and blagizv.godina=" + detaljTablica + ".godina " +
        "and blagizv.brizv=" + detaljTablica + ".brizv";
  }

  /**
   *  Gets the repQDSSveUplatnice attribute of the sgStuff object
   *
   *@param  master         Description of the Parameter
   *@param  detaljTablica  Description of the Parameter
   *@return                The repQDSSveUplatnice value
   */
  public String getRepQDSSveUplatnice(QueryDataSet master, String detaljTablica) {
    return "select blagizv.cblag, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, " +
        "blagizv.prijenos, blagizv.uksaldo, " + detaljTablica + ".rbs, " + detaljTablica + ".vrsta, " + detaljTablica + ".datum, " + detaljTablica + ".knjig, " + detaljTablica + ".opis, " + detaljTablica + ".primitak, " +
        "" + detaljTablica + ".izdatak, " + detaljTablica + ".stavka, " + detaljTablica + ".tko, " + detaljTablica + ".cskl, " +detaljTablica + ".brojkonta " +
        "from blagizv, " + detaljTablica + " " +
        "where blagizv.knjig='" + master.getString("KNJIG") + "' and blagizv.cblag=" + master.getInt("CBLAG") +
        " and blagizv.oznval='" + master.getString("OZNVAL") + "' and blagizv.godina=" + master.getShort("GODINA") +
        " and blagizv.brizv=" + master.getInt("BRIZV") +
        " " +
        "and blagizv.cblag=" + detaljTablica + ".cblag " +
        "and blagizv.knjig=" + detaljTablica + ".knjig and blagizv.oznval=" + detaljTablica + ".oznval and blagizv.godina=" + detaljTablica + ".godina " +
        "and blagizv.brizv=" + detaljTablica + ".brizv";
  }

  /**
   *  Gets the repSveIsplatniceZaAkontacijuRazliku attribute of the sgStuff
   *  object
   *
   *@param  dejta  Description of the Parameter
   *@return        The repSveIsplatniceZaAkontacijuRazliku value
   */
  public String getRepSveIsplatniceZaAkontacijuRazliku(QueryDataSet dejta) {
    return "select blagizv.cblag, blagizv.godina, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, blagizv.prijenos, blagizv.uksaldo, stavblag.rbs, stavblag.datum, stavblag.knjig, stavblag.opis, stavblag.primitak, stavblag.izdatak, stavblag.stavka, stavblag.tko, stavblag.cskl, stavblag.oznval " +
        "from blagizv, stavblag " +
        "where stavblag.cpn='" + dejta.getString("CPN") + "' and stavblag.stavka='" + dejta.getString("STAVKA") + "' and blagizv.cblag=stavblag.cblag and blagizv.knjig=stavblag.knjig and blagizv.oznval=stavblag.oznval and blagizv.godina=stavblag.godina and blagizv.brizv=stavblag.brizv ";
    //+
//        "union "+
//        "select blagizv.cblag, blagizv.godina, blagizv.brizv, blagizv.datod, blagizv.datdo, blagizv.ukprimitak, blagizv.ukizdatak, blagizv.saldo, blagizv.prijenos, blagizv.uksaldo, stavkeblarh.rbs, stavkeblarh.datum, stavkeblarh.knjig, stavkeblarh.opis, stavkeblarh.primitak, stavkeblarh.izdatak, stavkeblarh.stavka, stavkeblarh.tko, stavkeblarh.cskl, stavkeblarh.oznval "+
//        "from blagizv, stavkeblarh "+
//        "where stavkeblarh.cpn='" + dejta.getString("CPN") + "' and blagizv.cblag=stavkeblarh.cblag and blagizv.knjig=stavkeblarh.knjig and blagizv.oznval=stavkeblarh.oznval and blagizv.godina=stavkeblarh.godina and blagizv.brizv=stavkeblarh.brizv";
  }

  /**
   *  Gets the sTATUSPREDIZV attribute of the sgStuff object
   *
   *@param  presset  Description of the Parameter
   *@return          The sTATUSPREDIZV value
   */
  public String getSTATUSPREDIZV(StorageDataSet presset) {
    return getSTATUSPREDIZV(presset, (getNoviBrIzv(presset))); // - 1));
  }

  /**
   *  Gets the sTATUSPREDIZV attribute of the sgStuff object
   *
   *@param  presset    Description of the Parameter
   *@param  prethodni  Description of the Parameter
   *@return            The sTATUSPREDIZV value
   */
  public String getSTATUSPREDIZV(StorageDataSet presset, int prethodni) {
    String qstr = "select blagizv.status from blagizv where blagizv.knjig='" +
        presset.getString("KNJIG") +
        "' and blagizv.cblag=" + presset.getInt("CBLAG") +
        " and blagizv.oznval='" + presset.getString("OZNVAL") + "' " +
        "and blagizv.godina=" + presset.getShort("GODINA") + " " +
        "and blagizv.status = 'U'"
        +"and blagizv.brizv=" + prethodni + "";
//System.out.println("qstr getSTATUSPREDIZV " +qstr);
    vl.execSQL(qstr);
    vl.RezSet.open();
//System.out.println("q= "+qstr);
    if (vl.RezSet.isEmpty()) {
      return "K";
    }
    return vl.RezSet.getString("STATUS");
  }
  public boolean isExistUIzv(StorageDataSet presset) {
    String qstr = "select blagizv.status from blagizv where blagizv.knjig='" +
        presset.getString("KNJIG") +
        "' and blagizv.cblag=" + presset.getInt("CBLAG") +
        " and blagizv.oznval='" + presset.getString("OZNVAL") + "' " +
        "and blagizv.status = 'U'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return !vl.RezSet.isEmpty();
  }
  public boolean getJeLiBlagajnaBezgotovinska(String knjig, int cblag, String oznval){
    String qstr = "select brezgot from blagajna where knjig ='" + knjig + "' and cblag =" + cblag + " and oznval='" + oznval + "'";
    QueryDataSet qds = ut.getNewQueryDataSet(qstr);
    return qds.getString("BREZGOT").equals("D");
  }

//  public double getSumuSaldaBlagajne(String knjig, int cblag, String oznval, short godina, int brIzv){
//    String qstr = "select sum (primitak) - sum (izdatak) as suma from stavblag where godina ='" + godina + "' and brizv = '" + brIzv +
//                  "' and  knjig = '" + knjig + "' and cblag = '" + cblag + "' and oznval='" + oznval + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet.getDouble("SUMA");
//  }

  /**
   *  Gets the tECAJ attribute of the sgStuff object
   *
   *@param  OZNVAL   Description of the Parameter
   *@param  zaDatum  Description of the Parameter
   *@return          The tECAJ value
   */
  public BigDecimal getTECAJ(String OZNVAL, java.sql.Timestamp zaDatum) {
    BigDecimal tecaj = hr.restart.zapod.Tecajevi.getTecaj(zaDatum, OZNVAL);
    return tecaj;
  }

//  public DataSet getAkontacijaZaObracunPV(String cpn){
//
//    String qstr = "select sum(pvizdatak) as pvizdatak, max(oznval) as oznval from stavblag where cpn='" + cpn +
//                  "' and cskl='6' and stavka='1' group by oznval union select sum(izdatak) as izdatak, max(oznval) as oznval from stavkeblarh where cpn='" + cpn +
//                  "' and cskl='6' and stavka='1' group by oznval";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet;
//  }

  /**
   *  Gets the troskoveZaObracun attribute of the sgStuff object
   *1
   *@param  cpn  Description of the Parameter
   *@return      The troskoveZaObracun value
   */
  public DataSet getTroskoveZaObracun(String cpn) {
    String qstr = "select sum(iznos) as iznos, sum(pviznos) as pviznos, oznval from stavkepn where cpn='" + cpn + "'  group by oznval";
    // and ispl='N'
//    vl.execSQL(qstr);
//    vl.RezSet.open();
    QueryDataSet qds = ut.getNewQueryDataSet(qstr);
    return qds;
  }

  /**
   *  Gets the ukupnoDnevnice attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoDnevnice value
   */
  public BigDecimal getUkupnoDnevnice(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(iznos) as iznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='2' and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("IZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(iznos) as iznos from stavpnarh where knjig='" + knjig +
          "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='2' and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("IZNOS");
  }

  /**
   *  Gets the ukupnoDnevnicePV attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoDnevnicePV value
   */
  public BigDecimal getUkupnoDnevnicePV(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(pviznos) as pviznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='2'";
    //+ " and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("PVIZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(pviznos) as pviznos from stavpnarh where knjig='" + knjig +
          "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='2'";
      //+" and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("PVIZNOS");
  }

  /**
   *  Gets the ukupnoNocenja attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoNocenja value
   */
  public BigDecimal getUkupnoNocenja(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(iznos) as iznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='4' and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("IZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(iznos) as iznos from stavpnarh where knjig='" + knjig +
          "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='4' and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("IZNOS");
  }

  /**
   *  Gets the ukupnoNocenjaPV attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoNocenjaPV value
   */
  public BigDecimal getUkupnoNocenjaPV(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(pviznos) as pviznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj +  " and indputa='" + indputa +"' and cskl='4'";
    // +" and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("PVIZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(pviznos) as pviznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj +  " and indputa='" + indputa +"' and cskl='4'";
      // + " and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("PVIZNOS");
  }

  /**
   *  Gets the ukupnoOstalo attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoOstalo value
   */
  public BigDecimal getUkupnoOstalo(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(iznos) as iznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='5' and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("IZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(iznos) as iznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='5' and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("IZNOS");
  }

  /**
   *  Gets the ukupnoOstaloPV attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoOstaloPV value
   */
  public BigDecimal getUkupnoOstaloPV(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(pviznos) as pviznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='5'";
    // +" and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("PVIZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(pviznos) as pviznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='5'";
      //+ " and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("PVIZNOS");
  }

  /**
   *  Gets the ukupnoPutTros attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoPutTros value
   */
  public BigDecimal getUkupnoPutTros(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(iznos) as iznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='3' and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("IZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(iznos) as iznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and cskl='3' and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("IZNOS");
  }

  /**
   *  Gets the ukupnoPutTrosPV attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoPutTrosPV value
   */
  public BigDecimal getUkupnoPutTrosPV(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(pviznos) as pviznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj +  " and indputa='" + indputa +"' and cskl='3'";
    // +" and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("PVIZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(pviznos) as pviznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj +  " and indputa='" + indputa +"' and cskl='3'";
      //+ " and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("PVIZNOS");
  }

  /**
   *  Gets the ukupnoSveukupno attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoSveukupno value
   */
  public BigDecimal getUkupnoSveukupno(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(iznos) as iznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("IZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(iznos) as iznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "' and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("IZNOS");
  }

  /**
   *  Gets the ukupnoSveukupnoPV attribute of the sgStuff object
   *
   *@param  knjig   Description of the Parameter
   *@param  god     Description of the Parameter
   *@param  broj    Description of the Parameter
   *@param  valuta  Description of the Parameter
   *@param  indputa  Description of the Parameter
   *@return         The ukupnoSveukupnoPV value
   */
  public BigDecimal getUkupnoSveukupnoPV(String knjig, short god, int broj, String valuta, String indputa) {
    String qstr = "select sum(pviznos) as pviznos from stavkepn where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "'";
    //+ " and oznval='" + valuta + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    if (vl.RezSet.getBigDecimal("PVIZNOS").compareTo(new BigDecimal(0)) == 0) {
      qstr = "select sum(pviznos) as pviznos from Stavpnarh where knjig='" + knjig +
        "' and godina=" + god + " and broj=" + broj + " and indputa='" + indputa + "'";
      //+ " and oznval='" + valuta + "'";
      vl.execSQL(qstr);
      vl.RezSet.open();
    }
    return vl.RezSet.getBigDecimal("PVIZNOS");
  }

//  public QueryDataSet getIznosDNK(String czem){
//    String qstr ="select zemlje.* from zemlje where czemlje='" + czem + "'";
//    vl.execSQL(qstr);
//    vl.RezSet.open();
//    return vl.RezSet;
//  }

  /**
   *  Gets the vrshemekZaPN attribute of the sgStuff object
   *
   *@return    The vrshemekZaPN value
   */
  public QueryDataSet getVrshemekZaPN() {
    String qstr = "select * from vrshemek where app='blpn'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Description of the Method
   *
   *@param  czem  Description of the Parameter
   *@return       Description of the Return Value
   */
  public QueryDataSet inoZemlje(String czem) {
    String qstr = "select * from zemlje where indputa='I' and czemlje='" + czem + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public QueryDataSet prijevoznaSredstvaIzSifrarnika() {
    String qstr = "select * from sifrarnici where vrstasif = 'BLPS'";
    vl.execSQL(qstr);
    vl.RezSet.setColumns(dm.getSifrarnici().cloneColumns());

    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Description of the Method
   *
   *@param  knjig  Description of the Parameter
   *@param  god    Description of the Parameter
   *@param  broj   Description of the Parameter
   *@return        Description of the Return Value
   */
  public String repQueryStringPutniNalog(String knjig, short god, int broj) {
    return "select * from putninalog where knjig='" + knjig + "' and godina=" + god + " and broj=" + broj + "";
  }

  /**
   *  Sets the putninalogAkontiran attribute of the sgStuff object
   *
   *@param  cpn  The new putninalogAkontiran value
   */
  public void setPutninalogAkontiran(String cpn) {
    String qstr = "update putninalog set status='A' where cpn='" + cpn + "'";
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    }
    catch (Exception ex) {
    }
  }

  /**
   *  Sets the putninalogIsplacen attribute of the sgStuff object
   *
   *@param  cpn  The new putninalogIsplacen value
   */
  public void setPutninalogIsplacen(String cpn) {
    String qstr = "update putninalog set status='I' where cpn='" + cpn + "'";
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
      dM.getSynchronizer().markAsDirty("PutniNalog_Radnici");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   *  Description of the Method
   *
   *@param  cskl   Description of the Parameter
   *@param  vrdok  Description of the Parameter
   *@return        Description of the Return Value
   */
  public QueryDataSet shKontaPoCsklVrdok(String cskl, String vrdok) {
    String qstr = "select * from shkonta where vrdok='" + vrdok + "' and cskl='" + cskl + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public QueryDataSet shKontaZaBlagajnu() {
    String qstr = "select * from shkonta where vrdok='BL' and cskl='1'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

  /**
   *  Description of the Method
   *
   *@param  master   Description of the Parameter
   *@param  presset  Description of the Parameter
   *@return          Description of the Return Value
   */
  public String updateBlagajnaKodArhiviranja(QueryDataSet master, StorageDataSet presset) {
    String s = "update blagajna " +
        "set " +
        "saldo=" + master.getBigDecimal("UKSALDO") + ", " +
        "pvsaldo=" + master.getBigDecimal("PVUKSALDO") +
        ", datizv='" + master.getTimestamp("DATDO") +
        "', brizv=" + master.getInt("BRIZV") +
        " where " +
        "cblag=" + presset.getInt("CBLAG") +
        " and oznval='" + presset.getString("OZNVAL") +
        "' and knjig='" + presset.getString("KNJIG") + "'";
    System.out.println(s);
    return s;
  }

  /**
   *  Description of the Method
   *
   *@param  knjig       Description of the Parameter
   *@param  oznval      Description of the Parameter
   *@param  cblag       Description of the Parameter
   *@param  brizv       Description of the Parameter
   *@param  godina      Description of the Parameter
   *@param  izdatak     Description of the Parameter
   *@param  primitak    Description of the Parameter
   *@param  pvizdatak   Description of the Parameter
   *@param  pvprimitak  Description of the Parameter
   *@param  datumDo     Description of the Parameter
   */
  public void updateBlagizvPriAkontacijiRazlici(String knjig, String oznval, int cblag, int brizv, short godina, BigDecimal izdatak, BigDecimal primitak, BigDecimal pvizdatak, BigDecimal pvprimitak, Timestamp datumDo) {
    String qs = "select saldo, uksaldo, ukprimitak, ukizdatak, pvsaldo, pvuksaldo, pvukprimitak, pvukizdatak from blagizv where " +
        "cblag=" + cblag +
        " and oznval='" + oznval +
        "' and godina=" + godina +
        " and knjig='" + knjig +
        "' and brizv=" + brizv + "";

    QueryDataSet qds = ut.getNewQueryDataSet(qs);

    BigDecimal saldo = qds.getBigDecimal("SALDO").subtract(izdatak).add(primitak);
    BigDecimal uksaldo = qds.getBigDecimal("UKSALDO").subtract(izdatak).add(primitak);
    BigDecimal ukprimitak = qds.getBigDecimal("UKPRIMITAK").add(primitak);
    BigDecimal ukizdatak = qds.getBigDecimal("UKIZDATAK").add(izdatak);
    BigDecimal pvsaldo = qds.getBigDecimal("PVSALDO").subtract(pvizdatak).add(pvprimitak);
    BigDecimal pvuksaldo = qds.getBigDecimal("PVUKSALDO").subtract(pvizdatak).add(pvprimitak);
    BigDecimal pvukprimitak = qds.getBigDecimal("PVUKPRIMITAK").add(pvprimitak);
    BigDecimal pvukizdatak = qds.getBigDecimal("PVUKIZDATAK").add(pvizdatak);

    String qstr = "update blagizv set saldo='" + saldo +
                  "', uksaldo='" + uksaldo +
                  "', ukprimitak='" + ukprimitak +
                  "', ukizdatak='" + ukizdatak +
                  "', pvsaldo='" + pvsaldo +
                  "', pvuksaldo='" + pvuksaldo +
                  "', pvukprimitak='" + pvukprimitak +
                  "', pvukizdatak='" + pvukizdatak +
                  "', datdo='" + datumDo +
                  "' where cblag=" + cblag +
                  " and oznval='" + oznval +
                  "' and godina=" + godina +
                  " and knjig='" + knjig +
                  "' and brizv=" + brizv + "";
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("blagizv");
      System.out.println("BLAGIZV UPDATED");
      if (frmUplIspl.getUplIspl()!=null && frmUplIspl.getUplIspl().raMaster.isShowing()) {
System.out.println("UPDATING UI");
        int row = frmUplIspl.getUplIspl().getMasterSet().getRow();
        frmUplIspl.getUplIspl().getMasterSet().refresh();
        frmUplIspl.getUplIspl().getMasterSet().goToRow(row);
        frmUplIspl.getUplIspl().raMaster.getJpTableView().fireTableDataChanged();
        if (frmUplIspl.getUplIspl().raDetail.isShowing()) {
          frmUplIspl.getUplIspl().getDetailSet().refresh();
          frmUplIspl.getUplIspl().raDetail.getJpTableView().fireTableDataChanged();
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   *  Description of the Method
   *
   *@param  cpn        Description of the Parameter
   *@param  iznosAkon  Description of the Parameter
   */
  public void updatePutniNalogAkontacija(String cpn, BigDecimal iznosAkon) {
    vl.execSQL("select akontacija, status from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    String status = vl.RezSet.getString("STATUS");
    BigDecimal akont = iznosAkon;
    if (status.equals("A")) {
      akont = vl.RezSet.getBigDecimal("AKONTACIJA").add(iznosAkon);
    }

    String qstr = "update putninalog set akontacija = " + akont + ", razlika = " + akont + " where cpn='" + cpn + "'";
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    }
    catch (Exception ex) {
    }

  }

  /**
   *  Description of the Method
   *
   *@param  cpn       Description of the Parameter
   *@param  ExTrosak  Description of the Parameter
   */
  public void updatePutniNalogObracunBrisanjeStavke(String cpn, BigDecimal ExTrosak) {
    vl.execSQL("select akontacija, troskovi from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal trosak = vl.RezSet.getBigDecimal("TROSKOVI").subtract(ExTrosak);
    BigDecimal rzlika = vl.RezSet.getBigDecimal("AKONTACIJA").subtract(trosak);
    String qstr = "update putninalog set troskovi = " + trosak + ", razlika = " + rzlika + "  where cpn='" + cpn + "'";

    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {}
  }

  /**
   *  Description of the Method
   *
   *@param  cpn         Description of the Parameter
   *@param  exTrosak    Description of the Parameter
   *@param  NextTrosak  Description of the Parameter
   *@param  datum  Description of the Parameter
   */

  public void updatePutniNalogObracunIzmjena(String cpn, BigDecimal exTrosak, BigDecimal NextTrosak, Timestamp datum) {
    vl.execSQL("select akontacija, troskovi from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal trosak = vl.RezSet.getBigDecimal("TROSKOVI").add(NextTrosak.subtract(exTrosak));
    BigDecimal rzlika = vl.RezSet.getBigDecimal("AKONTACIJA").subtract(trosak);
    String qstr = "update putninalog set troskovi = " + trosak + ", razlika = " + rzlika + ", datobr = '" + datum + "' where cpn='" + cpn + "'";
System.out.println(qstr);
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {ex.printStackTrace();}
  }

  /**
   *  Description of the Method
   *
   *@param  cpn       Description of the Parameter
   */
  public void updatePutniNalogObracunObracun(String cpn){ // , BigDecimal ExTrosak) {
    QueryDataSet qds = ut.getNewQueryDataSet("select akontacija, troskovi from putninalog where cpn='" + cpn + "'");
//    vl.execSQL("select akontacija, troskovi from putninalog where cpn='" + cpn + "'");
//    vl.RezSet.open();
    BigDecimal trosak = qds.getBigDecimal("TROSKOVI"); // .subtract(ExTrosak);
    BigDecimal razlika = qds.getBigDecimal("AKONTACIJA").subtract(trosak);
    String qstr = "update putninalog set troskovi = " + trosak + ", razlika = " + razlika + "  where cpn='" + cpn + "'";

    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {
//      System.out.println("updatePutniNalogObracunObracun - EXEPSHN");
    }
  }

  /**
   *  Description of the Method
   *
   *@param  cpn         Description of the Parameter
   *@param  NextTrosak  Description of the Parameter
   *@param  datum  Description of the Parameter
   */
  public void updatePutniNalogObracunUnos(String cpn, BigDecimal NextTrosak, Timestamp datum) {
    vl.execSQL("select akontacija, troskovi from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal trosak = vl.RezSet.getBigDecimal("TROSKOVI").add(NextTrosak);
    BigDecimal rzlika = vl.RezSet.getBigDecimal("AKONTACIJA").subtract(trosak);
    String qstr = "update putninalog set troskovi = " + trosak + ", razlika = " + rzlika + ", datobr = '" + datum + "' where cpn='" + cpn + "'";
System.out.println(qstr);
    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {ex.printStackTrace();}
  }

  /**
   *  Description of the Method
   *
   *@param  cpn        Description of the Parameter
   *@param  newUplata  Description of the Parameter
   */
  public void updatePutniNalogObracunUplaceno(String cpn, BigDecimal newUplata) {
    vl.execSQL("select uplrazlika from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal razlika = vl.RezSet.getBigDecimal("UPLRAZLIKA").add(newUplata); //).setScale(2, BigDecimal.ROUND_HALF_UP);
    razlika = razlika.setScale(2, BigDecimal.ROUND_HALF_UP);
    String qstr = "update putninalog set uplrazlika = '" + razlika + "'  where cpn='" + cpn + "'";

    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {}
  }

  public void calculateTecRazlika(String cpn){
    vl.execSQL("select razlika, uplrazlika from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal uplRzlika = vl.RezSet.getBigDecimal("RAZLIKA");
    BigDecimal rzlika = vl.RezSet.getBigDecimal("UPLRAZLIKA");
    String qstr = "update putninalog set tecrazlika = " + (rzlika.subtract(uplRzlika)) + "  where cpn='" + cpn + "'";

    try {
      raTransaction.runSQL(qstr);
      dM.getSynchronizer().markAsDirty("putninalog");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public QueryDataSet zemZemlje() {
    String qstr = "select * from zemlje where indputa='Z'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet;
  }

// select cpn from putninalog where cradnik=8

  public boolean getNalogIsplacen(String cpn){
    String qstr = "select status from putninalog where cpn='" + cpn + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return (vl.RezSet.getString("STATUS").equals("I") || vl.RezSet.getString("STATUS").equals("K"));
  }


  public String getCPN(String cradnik){
    String qstr = "select cpn from putninalog where cradnik='" + cradnik + "' and status='P' or status ='A'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("CPN");
  }

  public String getCradnikFromCPN(String cpn){
    String qstr = "select cradnik from putninalog where cpn='" + cpn + "'";
    vl.execSQL(qstr);
    vl.RezSet.open();
    return vl.RezSet.getString("CRADNIK");
  }

//    public DataSet getPutniNaloziPrAk(String cradnik, char vrsta){
//      String qstr = "SELECT * FROM PutniNalog WHERE ";
//      if (!cradnik.equals("")) qstr = qstr.concat("cradnik='" + cradnik + "' AND ");
//      if (vrsta == 'A')qstr = qstr.concat("(status='A' OR status='P')");
//      else if (vrsta == 'R') qstr = qstr.concat("status='O'");
//      QueryDataSet pnpa = ut.getNewQueryDataSet(qstr);
//  //    sysoutTEST syst = new sysoutTEST(false);
//  //    syst.prn(pnpa);
//      return pnpa;
//    }

  public DataSet getPutniNaloziPrAk(char vrsta){
    String qstr = "SELECT putninalog.cradnik, radnici.ime, radnici.prezime, putninalog.cpn FROM PutniNalog, Radnici "+
                  "WHERE putninalog.cradnik = radnici.cradnik ";
    String A =    " AND (putninalog.status = 'P' OR putninalog.status = 'A')";
    String R =    " AND putninalog.status = 'O'";
    if (vrsta == 'A')qstr = qstr.concat(A);
    else qstr = qstr.concat(R);
    raDataSet pnpa = new raDataSet();//Util.getNewQueryDataSet(qstr, false);
    pnpa.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), qstr));
    pnpa.setColumns(new Column[] {
      dM.createStringColumn("CRADNIK", "Mati\u010Dni broj", 6),
      dM.createStringColumn("IME", "Ime", 50),
      dM.createStringColumn("PREZIME", "Prezime", 50),
      dM.createStringColumn("CPN", "Putni nalog", 24)
    });
    pnpa.open();
    pnpa.setTableName("PutniNalog_Radnici");

    for (int i = 0; i < pnpa.getColumnCount(); i++) {
      Column clc = pnpa.getColumn(i);
      if (clc.getColumnName().startsWith(clc.getCaption())) {
        clc.setVisible(0);
      }
    }
    return pnpa;
  }

  public boolean checkNalogJeIsplacen(String cpn){
    vl.execSQL("select razlika, uplrazlika, tecrazlika from putninalog where cpn='" + cpn + "'");
    vl.RezSet.open();
    BigDecimal uplRzlika = vl.RezSet.getBigDecimal("RAZLIKA");
    BigDecimal rzlika = vl.RezSet.getBigDecimal("UPLRAZLIKA");
    BigDecimal tecRzlika = vl.RezSet.getBigDecimal("TECRAZLIKA");

    return (rzlika.compareTo(uplRzlika.add(tecRzlika)) == 0);
  }

  QueryDataSet qdsForNeisplStav;
  String tmpCpn="";

  public void setNeisplaceneStavkePN(String cpn, boolean refresh){
    if(!tmpCpn.equals(cpn) || refresh){
      tmpCpn=cpn;
      qdsForNeisplStav = ut.getNewQueryDataSet("SELECT Stavkepn.rbs, Shkonta.opis, Stavkepn.iznos, Stavkepn.pviznos, Stavkepn.oznval "+
          "FROM Stavkepn, Shkonta "+
          "WHERE stavkepn.stavka = shkonta.stavka "+
          "AND stavkepn.cskl = shkonta.cskl "+
          "AND stavkepn.vrdok = shkonta.vrdok "+
          "AND cpn='" + cpn +
          "' AND ispl='N'", false);
      qdsForNeisplStav.setColumns(new Column[] {
        dm.createShortColumn("RBS", "Broj Stavke"),
        dm.createStringColumn("OPIS", "Opis", 30),
        dm.createBigDecimalColumn("IZNOS", "Trošak"),
        dm.createBigDecimalColumn("PVIZNOS", "Trošak u dom. val."),
        dm.createStringColumn("OZNVAL", "Oznaka valute", 3)
      });
      qdsForNeisplStav.open();
    }
  }

  QueryDataSet qdsForNeisplStavUVal;
  String tmpOznval="";

  public void setNeisplaceneStavkeUValPN(String cpn, String oznval, boolean refresh){
    if (!tmpOznval.equals(oznval) || refresh){
      tmpOznval=oznval;
      qdsForNeisplStavUVal = ut.getNewQueryDataSet("SELECT Stavkepn.rbs, Shkonta.opis, Stavkepn.iznos, Stavkepn.pviznos, Stavkepn.oznval "+
          "FROM Stavkepn, Shkonta "+
          "WHERE stavkepn.stavka = shkonta.stavka "+
          "AND stavkepn.cskl = shkonta.cskl "+
          "AND stavkepn.vrdok = shkonta.vrdok "+
          "AND cpn='" + cpn + "' "+
          "AND oznval='"+ oznval + "' "+
          "AND ispl='N'", false);
      qdsForNeisplStavUVal.setColumns(new Column[] {
        dm.createShortColumn("RBS", "Broj Stavke"),
        dm.createStringColumn("OPIS", "Opis", 30),
        dm.createBigDecimalColumn("IZNOS", "Trošak"),
        dm.createBigDecimalColumn("PVIZNOS", "Trošak u dom. val."),
        dm.createStringColumn("OZNVAL", "Oznaka valute", 3)
      });
      qdsForNeisplStavUVal.open();
    }
  }

  public DataSet getNeisplaceneStavkePN(){
    return qdsForNeisplStav;
  }

  public DataSet getNeisplaceneStavkePNUVal(){
    return qdsForNeisplStavUVal;
  }

  public QueryDataSet stavblagFilteredForRazlikaPN(String cradnik, String cpn){
    QueryDataSet rds = Stavblag.getDataModule().getFilteredDataSet("STAVBLAG.CRADNIK = '" + cradnik +
                  "' AND STAVBLAG.CPN = '" + cpn +
                  "' AND STAVBLAG.CSKL = '6' AND STAVBLAG.STAVKA = '2'");
    rds.open();
    return rds;
/*
    String qstr = "select * from stavblag WHERE STAVBLAG.CRADNIK = '" + cradnik +
                  "' AND STAVBLAG.CPN = '" + cpn +
                  "' AND STAVBLAG.CSKL = '6' AND STAVBLAG.STAVKA = '2'";

    QueryDataSet qds = ut.getNewQueryDataSet(qstr, false);
//    hr.restart.baza.Condition condisn =
//    hr.restart.baza.Condition.where("CRADNIK", hr.restart.baza.Condition.EQUAL, cradnik).and(hr.restart.baza.Condition.equal("CPN",cpn)).and(
//    hr.restart.baza.Condition.equal("CSKL","6")).and(hr.restart.baza.Condition.equal("STAVKA","2"));
//    QueryDataSet qds = hr.restart.baza.Stavblag.getDataModule().getTempSet(condisn);
    qds.setColumns(dm.getStavblag().cloneColumns());
    qds.open();
    sysoutTEST syst = new sysoutTEST(false);
    syst.prn(qds);
    return qds;
    */
  }

  public DataSet getRazlika(String cpn){
    StorageDataSet tmpRazlika = new StorageDataSet();
    Column column1 = new Column();
    Column column2 = new Column();
    Column column3 = new Column();

      try {
//        column1.setColumnName("RAZLIKA");
//        column1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//        column1.setDefault("0");
//        column1.setResolvable(false);
//        column1.setSqlType(0);

        column1 = dm.createBigDecimalColumn("RAZLIKA");

//        column2.setColumnName("PVRAZLIKA");
//        column2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//        column2.setDefault("0");
//        column2.setResolvable(false);
//        column2.setSqlType(0);

        column2 = dm.createBigDecimalColumn("PVRAZLIKA");

//        column3.setColumnName("VALUTA");
//        column3.setDataType(com.borland.dx.dataset.Variant.STRING);
//        column3.setResolvable(false);
//        column3.setSqlType(0);

        column3 = dm.createStringColumn("VALUTA",0);

        tmpRazlika.setColumns(new Column[]{column1, column2, column3});
        tmpRazlika.open();

      }
      catch (Exception ex) {
        tmpRazlika.deleteAllRows();
      }

      StorageDataSet troskovi = (StorageDataSet)getTroskoveZaObracun(cpn);
      StorageDataSet akontacija = (StorageDataSet)getAkontacijaZaObracun(cpn);
      StorageDataSet isplaceno = (StorageDataSet)getIsplacenaRazlika(cpn);

      troskovi.first();
      boolean insert;
      do{
        isplaceno.first();
        insert=true;
        do {
          if (troskovi.getString("OZNVAL").equals(isplaceno.getString("OZNVAL"))){
            insert=false;
            break;
          }
        } while (isplaceno.next());
        if (insert){
          tmpRazlika.insertRow(false);
          tmpRazlika.setString("VALUTA",troskovi.getString("OZNVAL"));
          tmpRazlika.setBigDecimal("RAZLIKA", troskovi.getBigDecimal("IZNOS"));
          tmpRazlika.setBigDecimal("PVRAZLIKA", troskovi.getBigDecimal("PVIZNOS"));
        }
      } while(troskovi.next());

      troskovi.first();

      do{
        akontacija.first();
        do{
          if(troskovi.getString("OZNVAL").equals(akontacija.getString("OZNVAL"))){
            tmpRazlika.first();
            do {
              if (troskovi.getString("OZNVAL").equals(tmpRazlika.getString("VALUTA"))){
                tmpRazlika.setBigDecimal("RAZLIKA", troskovi.getBigDecimal("IZNOS").subtract(akontacija.getBigDecimal("IZDATAK")));
                tmpRazlika.setBigDecimal("PVRAZLIKA", troskovi.getBigDecimal("PVIZNOS").subtract(akontacija.getBigDecimal("PVIZDATAK")));
                break;
              }
            } while (tmpRazlika.next());
            break;
          }
        } while (akontacija.next());
      } while (troskovi.next());

      return tmpRazlika;
  }

  private String getColCaption(javax.swing.text.JTextComponent incomingJTC) {
    com.borland.dx.dataset.Column col;
    com.borland.dx.dataset.ColumnAware caw;
    if (incomingJTC instanceof com.borland.dx.dataset.ColumnAware) {
      try {
        caw = (com.borland.dx.dataset.ColumnAware)incomingJTC;
        col = caw.getDataSet().getColumn(caw.getColumnName());
        return col.getCaption().toUpperCase();
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }

  public boolean isEmpty(javax.swing.text.JTextComponent jt1, javax.swing.text.JTextComponent jt2) {
    if (vl.chkIsEmpty(jt1) && vl.chkIsEmpty(jt2)) {
      java.awt.Component parent = jt1.getTopLevelAncestor();

    java.util.ResourceBundle dmres = java.util.ResourceBundle.getBundle("hr.restart.baza.dmRes");
    String msgText = dmres.getString("errReq_unos")+" "+getColCaption(jt1)+" ili "+getColCaption(jt2)+" !";

      jt1.requestFocus();
      javax.swing.JOptionPane.showMessageDialog(
          parent,
          msgText,
          dmres.getString("errMain"),
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return true;
    }
    return false;
  }

  public static String getOPISBLIzv(DataSet ds) {
    String opisblizv = frmParam.getParam("blpn", "opisblizv", "OPIS+TKO", "Sto treba pisati u opisu stavke kod blagajnickog izvj.");
    VarStr ret = new VarStr();
    StringTokenizer stobli = new StringTokenizer(opisblizv, "+");
    while (stobli.hasMoreTokens()) {
      String colnme = stobli.nextToken().trim().toUpperCase();
      Column col = ds.hasColumn(colnme);
      if (col != null) {
        String val = raVariant.getDataSetValue(ds, colnme).toString();
        if (col.getDataType() == Variant.TIMESTAMP) {
          val = raDateUtil.getraDateUtil().dataFormatter(ds.getTimestamp(colnme));
        }
        ret.append(val).append(" ");
      } else if (ds.hasColumn("CPAR") != null) {
        QueryDataSet ppar = dM.getDataModule().getAllPartneri();
        ppar.open();
        if (ppar.hasColumn(colnme) != null) {
          if (lookupData.getlookupData().raLocate(ppar, "CPAR", ""+ds.getInt("CPAR"))) {
            ret.append(raVariant.getDataSetValue(ppar, colnme).toString()).append(" ");
          }
        }
      }
      
    }
    if (ret.length() == 0) {
      return ds.getString("OPIS");
    }
    return ret.toString();
  }
}
