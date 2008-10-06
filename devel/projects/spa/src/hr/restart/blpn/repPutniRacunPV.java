/****license*****************************************************************
**   file: repPutniRacunPV.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class repPutniRacunPV implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  lookupData ld = lookupData.getlookupData();
  frmObracunPN_V1_1 fopn = frmObracunPN_V1_1.getInstance();
  DataSet master = fopn.getRepQDS_zaglavlje();
  DataSet detail = fopn.getRepQDS_stavke();
  DataSet ukupno = fopn.getRepQDS_ukupno();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  sgStuff ss = sgStuff.getStugg();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repPutniRacunPV() {
    ru.setDataSet(detail);
  }


  public repPutniRacunPV(int idx) {
    if (idx == 0){
    }
    setIznose();
    setUkupno();
    setAkontacija();
    setRazlika();
  }

  /**
   * kako dobiti formatirani string iz dataseta
   *
   * @param colName column name
   * @return formatirani string
   */


  private String getStringFormattedLikeVariant(DataSet set,String colName) {
    com.borland.dx.text.VariantFormatter formater = set.getColumn(colName).getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);
    return formater.format(v);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        master.open();
        detail.open();
        ukupno.open();
      }
      int indx=0;
      repPutniRacunPV rpr = null;
      public Object nextElement() {
        rpr = new repPutniRacunPV(0);
        return rpr;
      }
      public boolean hasMoreElements() {
        return (rpr == null);
      }
    };
  }

  public void close() {
  }

  public String getRADNIK(){
    return ss.getIme(master.getString("CRADNIK"));
  }

  public String getDOMJESTA(){
    return master.getString("MJESTA");
  }

  public String getAKONTACIJA(){
    return akontacijaaaa;
  }

  public String getAKONTACIJAPV(){
    return akontacijaaaaPV;
  }

  public String getAKONTVAL(){
    return akontacijaval;
  }

  public String getBROJPUTNOGNALOGA(){
    return "broj ".concat(master.getString("CPN"));
  }

  public String getTROSKOVI(){
    return ukupnoSVE;
  }

  public String getTROSKOVIPV(){
    return ukupnoSVE_PV;
  }

  public String getRAZLIKA(){
    return iznosRazlike;
  }

  public String getRAZLIKAPV(){
    return iznosRazlikePV;
  }

  public String getVALUTARAZLIKE(){
    return valutaRazlika;
  }

  public String getDOMACAVALUTA(){
    return domValRazlike;
  }

  public String getSTATUSRAZLIKE(){
    return statusRazlike;
  }

  public String getUPLISPL(){

    if (master.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) < 0) {
      return " - isplatiti";
    } else if (master.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) > 0){
      return " - uplatiti";
    }
      return "";
  }

  public String getOSTAJEZAUPLATUVRACANJE(){

    if (master.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) < 0) {
      return "\nOSTAJE ZA ISPLATU";
    } else if (master.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) > 0){
      return "\nOSTAJE ZA VRA\u0106ANJE";
    }
      return "\nOSTAJE ZA ISPLATU - VRA\u0106ANJE";
  }

  public String getDATUMPREDUJMA(){
    return rdu.dataFormatter(ss.getDatumAkontacije(master.getString("CPN")));
  }

  public String getCPN(){
    return master.getString("CPN");
  }

  public String getCSKL(){
    return detail.getString("CSKL");
  }

  public int getSTAVKA(){
    return detail.getShort("STAVKA");
  }

  static String iznosDN;
  static String iznosNC;
  static String iznosPT;
  static String iznosXX;

  static String iznosDN_PV;
  static String iznosNC_PV;
  static String iznosPT_PV;
  static String iznosXX_PV;

  static String odMjesta;
  static String doMjesta;
  static String prSred;
  static String razKm;

  static String datOdl;
  static String datDol;
  static String vriOdl;
  static String vriDol;
  static String brSati;
  static String brDnev;

  static String brNoc;

  static String opisNoc;
  static String opisOstalo;

  static String valDN;
  static String valNC;
  static String valPT;
  static String valXX;

  static java.util.HashMap hmDN = new java.util.HashMap();
  static java.util.HashMap hmNC = new java.util.HashMap();
  static java.util.HashMap hmPT = new java.util.HashMap();
  static java.util.Hashtable hmXX = new java.util.Hashtable();

  void setIznose(){

    hmDN.clear();
    hmPT.clear();
    hmNC.clear();
    hmXX.clear();

    iznosDN = "";
    iznosDN_PV = "";
    iznosNC = "";
    iznosNC_PV = "";
    iznosPT = "";
    iznosPT_PV = "";
    iznosXX = "";
    iznosXX_PV = "";
    odMjesta = "";
    doMjesta = "";
    prSred = "";
    razKm = "";
    datOdl = "";
    datDol = "";
    vriOdl = "";
    vriDol = "";
    brSati = "";
    brDnev = "";
    brNoc = "";
    opisNoc = "";
    opisOstalo = "";

    valDN = "";
    valNC = "";
    valPT = "";
    valXX = "";

    detail.first();
    do {
      if (detail.getString("CSKL").equals("2")){
        iznosDN = iznosDN.concat(getStringFormattedLikeVariant(detail,"IZNOS")).concat("\n");
        iznosDN_PV = iznosDN_PV.concat(getStringFormattedLikeVariant(detail,"PVIZNOS")).concat("\n");
        datOdl = datOdl.concat(rdu.dataFormatter(detail.getTimestamp("DATUMODL"))).concat("\n");
        datDol = datDol.concat(rdu.dataFormatter(detail.getTimestamp("DATUMDOL"))).concat("\n");
        vriOdl = vriOdl.concat(getVRIJODL(detail)).concat("\n");
        vriDol = vriDol.concat(getVRIJDOL(detail)).concat("\n");
        brSati = brSati.concat(getStringFormattedLikeVariant(detail,"BROJSATI")).concat("\n");
        brDnev = brDnev.concat(getStringFormattedLikeVariant(detail,"BROJDNK")).concat("\n");
        valDN = valDN.concat(detail.getString("OZNVAL")).concat("\n");

        if (!hmDN.containsKey(detail.getString("OZNVAL"))) hmDN.put(detail.getString("OZNVAL"), "1");
        else {
          int i = Integer.parseInt( (String) hmDN.get(detail.getString("OZNVAL")));
          ++i;
          hmDN.put(detail.getString("OZNVAL"), String.valueOf(i));
        }
      }
      else if (detail.getString("CSKL").equals("3")){
        iznosPT = iznosPT.concat(getStringFormattedLikeVariant(detail,"IZNOS")).concat("\n");
        iznosPT_PV = iznosPT_PV.concat(getStringFormattedLikeVariant(detail,"PVIZNOS")).concat("\n");
        odMjesta = odMjesta.concat(" ").concat(detail.getString("ODMJ")).concat("\n");
        doMjesta = doMjesta.concat(" ").concat(detail.getString("DOMJ")).concat("\n");
        prSred = prSred.concat(" ").concat(ss.getPrijevoznoSredstvo(detail.getString("CPRIJSRED"))).concat("\n");
        razKm = razKm.concat(getStringFormattedLikeVariant(detail,"BROJDNK")).concat("\n");
        valPT = valPT.concat(detail.getString("OZNVAL")).concat("\n");

        if (!hmPT.containsKey(detail.getString("OZNVAL"))) hmPT.put(detail.getString("OZNVAL"), "1");
        else {
          int i = Integer.parseInt( (String) hmPT.get(detail.getString("OZNVAL")));
          ++i;
          hmPT.put(detail.getString("OZNVAL"), String.valueOf(i));
        }
      }
      else if (detail.getString("CSKL").equals("4")){
        iznosNC = iznosNC.concat(getStringFormattedLikeVariant(detail,"IZNOS")).concat("\n");
        iznosNC_PV = iznosNC_PV.concat(getStringFormattedLikeVariant(detail,"PVIZNOS")).concat("\n");
        brNoc = brNoc.concat(getStringFormattedLikeVariant(detail,"BROJDNK")).concat("\n");
        opisNoc = opisNoc.concat(" ").concat(ss.getOpisStavke("4", detail.getShort("STAVKA"))).concat("\n");
        valNC = valNC.concat(detail.getString("OZNVAL")).concat("\n");

        if (!hmNC.containsKey(detail.getString("OZNVAL"))) hmNC.put(detail.getString("OZNVAL"), "1");
        else {
          int i = Integer.parseInt( (String) hmNC.get(detail.getString("OZNVAL")));
          ++i;
          hmNC.put(detail.getString("OZNVAL"), String.valueOf(i));
        }
      }
      else if (detail.getString("CSKL").equals("5")){
        iznosXX = iznosXX.concat(" ").concat(getStringFormattedLikeVariant(detail,"IZNOS")).concat("\n");
        iznosXX_PV = iznosXX_PV.concat(" ").concat(getStringFormattedLikeVariant(detail,"PVIZNOS")).concat("\n");
        opisOstalo = opisOstalo.concat(ss.getOpisStavke("5", detail.getShort("STAVKA"))).concat("\n");
        valXX = valXX.concat(detail.getString("OZNVAL")).concat("\n");

        if (!hmXX.containsKey(detail.getString("OZNVAL"))) hmXX.put(detail.getString("OZNVAL"), "1");
        else {
          int i = Integer.parseInt( (String) hmXX.get(detail.getString("OZNVAL")));
          ++i;
          hmXX.put(detail.getString("OZNVAL"), String.valueOf(i));
        }
      }
    } while (detail.next());

  }

  static String ukupnoDN;
  static String ukupnoPR;
  static String ukupnoNC;
  static String ukupnoOS;
  static String ukupnoSVE;
  static String ukupnoDN_PV;
  static String ukupnoPR_PV;
  static String ukupnoNC_PV;
  static String ukupnoOS_PV;
  static String ukupnoSVE_PV;
  static String uDNval;
  static String uPRval;
  static String uNCval;
  static String uOSval;
  static String ukupnoVALUTA;
  static String ukupnoVALUTA_PV;

  void setUkupno(){
    ukupnoDN = "";
    ukupnoPR = "";
    ukupnoNC = "";
    ukupnoOS = "";
    ukupnoSVE = "";
    ukupnoDN_PV = "";
    ukupnoPR_PV = "";
    ukupnoNC_PV = "";
    ukupnoOS_PV = "";
    ukupnoSVE_PV = "";
    uDNval = "";
    uPRval = "";
    uNCval = "";
    uOSval = "";
    ukupnoVALUTA = "";
    ukupnoVALUTA_PV = "";
    ukupno.first();
    do{
      if(ukupno.getBigDecimal("UKUDNEV").compareTo(new BigDecimal(0)) > 0){
        for (int i=0;i < Integer.parseInt((String)hmDN.get(ukupno.getString("VALUTA"))) ; i++) {
          if (i!= (Integer.parseInt((String)hmDN.get(ukupno.getString("VALUTA"))) -1)){
            ukupnoDN = ukupnoDN.concat(" \n");
            ukupnoDN_PV = ukupnoDN_PV.concat(" \n");
            uDNval = uDNval.concat(" \n");
          } else {
            ukupnoDN = ukupnoDN.concat(getStringFormattedLikeVariant(ukupno,"UKUDNEV")).concat("\n");
            ukupnoDN_PV = ukupnoDN_PV.concat(getStringFormattedLikeVariant(ukupno,"UKUDNEVPV")).concat("\n");
            uDNval = uDNval.concat(ukupno.getString("VALUTA")).concat("\n");
          }
        }
      }
      if(ukupno.getBigDecimal("UKUPUTR").compareTo(new BigDecimal(0)) > 0){
        for (int i=0;i < Integer.parseInt((String)hmPT.get(ukupno.getString("VALUTA"))) ; i++) {
          if (i!= (Integer.parseInt((String)hmPT.get(ukupno.getString("VALUTA"))) -1)){
            ukupnoPR = ukupnoPR.concat(" \n");
            ukupnoPR_PV = ukupnoPR_PV.concat(" \n");
            uPRval = uPRval.concat(" \n");
          } else {
            ukupnoPR = ukupnoPR.concat(getStringFormattedLikeVariant(ukupno,"UKUPUTR")).concat("\n");
            ukupnoPR_PV = ukupnoPR_PV.concat(getStringFormattedLikeVariant(ukupno,"UKUPUTRPV")).concat("\n");
            uPRval = uPRval.concat(ukupno.getString("VALUTA")).concat("\n");
          }
        }
      }
      if(ukupno.getBigDecimal("UKUNOCE").compareTo(new BigDecimal(0)) > 0){
        for (int i=0;i < Integer.parseInt((String)hmNC.get(ukupno.getString("VALUTA"))) ; i++) {
          if (i!= (Integer.parseInt((String)hmNC.get(ukupno.getString("VALUTA"))) -1)){
            ukupnoNC = ukupnoNC.concat(" \n");
            ukupnoNC_PV = ukupnoNC_PV.concat(" \n");
            uNCval = uNCval.concat(" \n");
          } else {
            ukupnoNC = ukupnoNC.concat(getStringFormattedLikeVariant(ukupno,"UKUNOCE")).concat("\n");
            ukupnoNC_PV = ukupnoNC_PV.concat(getStringFormattedLikeVariant(ukupno,"UKUNOCEPV")).concat("\n");
            uNCval = uNCval.concat(ukupno.getString("VALUTA")).concat("\n");
          }
        }
      }
      if(ukupno.getBigDecimal("UKUREST").compareTo(new BigDecimal(0)) > 0){
        for (int i=0;i < Integer.parseInt((String)hmXX.get(ukupno.getString("VALUTA"))) ; i++) {
          if (i!= (Integer.parseInt((String)hmXX.get(ukupno.getString("VALUTA"))) -1)){
            ukupnoOS = ukupnoOS.concat(" \n");
            ukupnoOS_PV = ukupnoOS_PV.concat(" \n");
            uOSval = uOSval.concat(" \n");
          } else {
            ukupnoOS = ukupnoOS.concat(getStringFormattedLikeVariant(ukupno,"UKUREST")).concat("\n");
            ukupnoOS_PV = ukupnoOS_PV.concat(getStringFormattedLikeVariant(ukupno,"UKURESTPV")).concat("\n");
            uOSval = uOSval.concat(ukupno.getString("VALUTA")).concat("\n");
          }
        }
      }

      ukupnoSVE = ukupnoSVE.concat(getStringFormattedLikeVariant(ukupno,"UKUSUKU")).concat("\n");
      ukupnoSVE_PV = ukupnoSVE_PV.concat(getStringFormattedLikeVariant(ukupno,"UKUSUKUPV")).concat("\n");
      ukupnoVALUTA = ukupnoVALUTA.concat(ukupno.getString("VALUTA")).concat("\n");
    } while (ukupno.next());
  }

  static String akontacijaaaa;
  static String akontacijaaaaPV;
  static String akontacijaval;
  static StorageDataSet akontacija = new StorageDataSet();

  void setAkontacija(){
    DataSet tempAkont = ss.getAkontacijaZaObracun(master.getString("CPN"));

    try {
      akontacija.setColumns(new Column[] {
        dm.createBigDecimalColumn("IZDATAK"),
        dm.createBigDecimalColumn("IZPV"),
        dm.createStringColumn("OZNVAL", 3),
      });
      akontacija.open();

    }
    catch (Exception ex) {
      akontacija.deleteAllRows();
    }

    do{
      akontacija.insertRow(false);
      akontacija.setBigDecimal("IZDATAK", tempAkont.getBigDecimal("IZDATAK"));
      akontacija.setBigDecimal("IZPV", tempAkont.getBigDecimal("PVIZDATAK"));
      akontacija.setString("OZNVAL", tempAkont.getString("OZNVAL"));
    } while (tempAkont.next());

    akontacijaaaa = "";
    akontacijaaaaPV = "";
    akontacijaval = "";
    akontacija.first();
    do{
      akontacijaaaa = akontacijaaaa.concat(getStringFormattedLikeVariant(akontacija, "IZDATAK")).concat("\n");
      akontacijaaaaPV = akontacijaaaaPV.concat(getStringFormattedLikeVariant(akontacija, "IZPV")).concat("\n");
      akontacijaval = akontacijaval.concat(akontacija.getString("OZNVAL"));
    } while(akontacija.next());
  }
  static StorageDataSet razlika = new StorageDataSet();
  static String iznosRazlike;
  static String iznosRazlikePV;
  static String valutaRazlika;
  static String domValRazlike;
  static String statusRazlike;

  void setRazlika(){

    DataSet akontacija = null;
    try {
      razlika.setColumns(new Column[] {
        dm.createBigDecimalColumn("RAZLIKA"),
        dm.createBigDecimalColumn("RAZLIKAPV"),
        dm.createStringColumn("VALUTA", 3),
      });
      razlika.open();

    }
    catch (Exception ex) {
      razlika.deleteAllRows();
    }
    akontacija = ss.getAkontacijaZaObracun(master.getString("CPN"));
    ukupno.first();

    do{
        razlika.insertRow(false);
        razlika.setString("VALUTA",ukupno.getString("VALUTA"));
        razlika.setBigDecimal("RAZLIKA", ukupno.getBigDecimal("UKUSUKU"));
        razlika.setBigDecimal("RAZLIKAPV", ukupno.getBigDecimal("UKUSUKUPV"));
    } while(ukupno.next());
    
    //ai: ako akontirana valuta nije trosena
    for (akontacija.first(); akontacija.inBounds(); akontacija.next()) {
      if (!lookupData.getlookupData().raLocate(ukupno,"VALUTA", akontacija.getString("OZNVAL"))) {
        ukupno.insertRow(false);
        ukupno.setString("VALUTA",akontacija.getString("OZNVAL"));
        ukupno.post();
      }
    }
    for (akontacija.first(); akontacija.inBounds(); akontacija.next()) {
      if (!lookupData.getlookupData().raLocate(razlika,"VALUTA", akontacija.getString("OZNVAL"))) {
        razlika.insertRow(false);
        razlika.setString("VALUTA",akontacija.getString("OZNVAL"));
        razlika.setBigDecimal("RAZLIKA", Aus.zero2);
        razlika.setBigDecimal("RAZLIKAPV", Aus.zero2);
        razlika.post();
      }
    }        
    ukupno.first();
    do{
      akontacija.first();
      do{
        if(ukupno.getString("VALUTA").equals(akontacija.getString("OZNVAL"))){
          razlika.first();
          do {
            if (ukupno.getString("VALUTA").equals(razlika.getString("VALUTA"))){
              razlika.setBigDecimal("RAZLIKA", ukupno.getBigDecimal("UKUSUKU").subtract(akontacija.getBigDecimal("IZDATAK")));
              BigDecimal roar;
              if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) <0 ) roar = razlika.getBigDecimal("RAZLIKA").negate();
              else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) >0 ) roar = razlika.getBigDecimal("RAZLIKA");
              else roar = Aus.zero0;
              razlika.setBigDecimal("RAZLIKAPV", ss.currrencyConverterToKN(roar, ukupno.getString("VALUTA"), master.getTimestamp("DATOBR")));
              break;
            }
          } while (razlika.next());
          break;
        }
      } while (akontacija.next());
    } while (ukupno.next());


    iznosRazlike = "";
    iznosRazlikePV = "";
    valutaRazlika = "";
    domValRazlike = "";
    statusRazlike = "";

    razlika.first();
    do{
      if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) <0 ){
        statusRazlike = statusRazlike.concat("uplatiti").concat("\n");
        razlika.setBigDecimal("RAZLIKA", razlika.getBigDecimal("RAZLIKA").negate());
      } else if (razlika.getBigDecimal("RAZLIKA").compareTo(new BigDecimal(0)) >0 ){
        statusRazlike = statusRazlike.concat("isplatiti").concat("\n");
      } else {
        statusRazlike = statusRazlike.concat("\n");
      }
      iznosRazlike = iznosRazlike.concat(getStringFormattedLikeVariant(razlika, "RAZLIKA")).concat("\n");
      iznosRazlikePV = iznosRazlikePV.concat(getStringFormattedLikeVariant(razlika, "RAZLIKAPV")).concat("\n");
      valutaRazlika = valutaRazlika.concat(razlika.getString("VALUTA")).concat("\n");
      domValRazlike = domValRazlike.concat(hr.restart.zapod.Tecajevi.getDomOZNVAL()).concat("\n");
    } while (razlika.next());
  }

  public String getIZNOSEDNEVNICA(){
      return iznosDN;
  }
  public String getIZNOSEDNEVNICA_PV(){
      return iznosDN_PV;
  }
  public String getDNEVNICEVALUTA(){
    return valDN;
  }

  public String getIZNOSEPRIJEVOZNITROSK(){
    return iznosPT;
  }
  public String getIZNOSEPRIJEVOZNITROSK_PV(){
    return iznosPT_PV;
  }
  public String getPRIJEVOZVALUTA(){
    return valPT;
  }

  public String getIZNOSENOCENJA(){
    return iznosNC;
  }
  public String getIZNOSENOCENJA_PV(){
    return iznosNC_PV;
  }
  public String getNOCENJEVALUTA(){
    return valNC;
  }

  public String getIZNOSEOSTALO(){
    return iznosXX;
  }
  public String getIZNOSEOSTALO_PV(){
    return iznosXX_PV;
  }
  public String getOSATLOVALUTA(){
    return valXX;
  }

  public String getRAZREDKM(){
    return razKm;
  }

  public String getODMJ(){
    return odMjesta;
  }

  public String getDOMJ(){
    return doMjesta;
  }

  public String getDATUMODLASKA(){
    return datOdl;
  }

  public String getDATUMDOLASKA(){
    return datDol;
  }

  public String getVRIJODL(){
    return vriOdl;
  }

  public String getVRIJDOL(){
    return vriDol;
  }

  private String getVRIJODL(DataSet set){
    int sat = fopn.getSat(set.getShort("VRIJODL"));
    int min = fopn.getMin(set.getShort("VRIJODL"));
    String saat = Integer.toString(sat);
    String miin = Integer.toString(min);
    if(sat <10) saat = "0".concat(saat);
    if(min <10) miin = "0".concat(miin);
    String vrijeme = saat.concat(":").concat(miin);

    return vrijeme;
  }

  private String getVRIJDOL(DataSet set){
    int sat = fopn.getSat(set.getShort("VRIJDOL"));
    int min = fopn.getMin(set.getShort("VRIJDOL"));
    String saat = Integer.toString(sat);
    String miin = Integer.toString(min);
    if(sat <10) saat = "0".concat(saat);
    if(min <10) miin = "0".concat(miin);
    String vrijeme = saat.concat(":").concat(miin);
    return vrijeme;
  }

  public String getBROJSATI(){
    return brSati;
  }

  public String getBROJDNEVNICA(){
    return brDnev;
  }

  public String getBROJNOCENJA(){
    return brNoc;
  }

  public String getOPISNOCENJA(){
    return opisNoc;
  }

  public String getOPISOSTALO(){
    return opisOstalo;
  }

  public String getUD(){
    return ukupnoDN;
  }
  public String getUD_PV(){
    return ukupnoDN_PV;
  }
  public String getUDVAL(){
    return uDNval;
  }
  public String getUP(){
    return ukupnoPR;
  }
  public String getUP_PV(){
    return ukupnoPR_PV;
  }
  public String getUPVAL(){
    return uPRval;
  }
  public String getUN(){
    return ukupnoNC;
  }
  public String getUN_PV(){
    return ukupnoNC_PV;
  }
  public String getUNVAL(){
    return uNCval;
  }
  public String getUO(){
    return ukupnoOS;
  }
  public String getUO_PV(){
    return ukupnoOS_PV;
  }
  public String getUOVAL(){
    return uOSval;
  }
  public String getSVE(){
    return ukupnoSVE;
  }
  public String getSVE_PV(){
    return ukupnoSVE_PV;
  }
  public String getVALUTAREKAP(){
    return ukupnoVALUTA;
  }

  public String getCORG(){
    return master.getString("CORG");
  }

  public String getNAZORG() {
     return ru.getSomething(new String[] {"CORG"},dm.getOrgstruktura(),"NAZIV").getString();
  }

  public String getPRIJEVOZNOSREDSTVO(){
    return prSred;
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }
  public String getSecondLine(){
    return rm.getSecondLine();
  }
  public String getThirdLine(){
    return rm.getThirdLine();
  }
  public String getLogoMjesto(){
    return rm.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
  public String getUMJESTUDANA(){
    String umd = getLogoMjesto().concat(", dana ").concat(rdu.dataFormatter(master.getTimestamp("DATOBR"))).concat(".");
    return umd;
  }

}