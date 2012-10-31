/****license*****************************************************************
**   file: raKalkulBDDoc.java
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
import hr.restart.util.Aus;

import java.math.BigDecimal;

import com.borland.dx.dataset.ReadRow;


public class raKalkulBDDoc extends raTopCalcUtil {

  private TypeDoc TD = TypeDoc.getTypeDoc();
  private BigDecimal Sto = new BigDecimal("100.00");
  private BigDecimal Jedan = new BigDecimal("1.00");
  private BigDecimal Nula = Aus.zero2;
  private BigDecimal tmpBD = Aus.zero2;
  private BigDecimal tmpBD2 = Aus.zero2;

  private BigDecimal tmpBDMC = Aus.zero2;
  private boolean isMCFix = false;

  private BigDecimal tmpPosto = Aus.zero2;
  private String what_kind_of_document ;

  public raFakeBDStdoki stavka = new raFakeBDStdoki();
  public raFakeBDStdoki stavkaold = new raFakeBDStdoki();
  public raFakeBDStanje stanje = new raFakeBDStanje();
  public raKalkulBDStanje rKS = new raKalkulBDStanje();
  private boolean onPrice = false;
  private String vrzal = "";

  public void setVrzal(String cskl) {
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    if (hr.restart.util.lookupData.getlookupData().raLocate(
                        dm.getSklad(),new String[]{"CSKL"},new String[]{cskl})){
      vrzal = dm.getSklad().getString("VRZAL");
    }
    else {
      System.out.println("Nama skladišta");
      vrzal = "N";
    }
    stanje.sVrSklad = vrzal;
  }

  char mode =' ';
  /**
   * Zna\u010Di treba zaobi\u0107i SimpleKalkConn i ovdje raditi sve tako da imamo stavke i ostalo i iz
   * gkoda kada prebacivati. Kalkulacija \u0107e biti u ovisnosti o tipu poziva kalkulacije i biti \u0107e
   * KalkulStavke i basta
   *
   */

  public raKalkulBDDoc() {
    isDB = true;
    stavka.Init();
    stavkaold.Init();
    stanje.Init();
  }

  private void initTmp() {
    tmpBD  = BigDecimal.valueOf(0,2);
    tmpBD2 = BigDecimal.valueOf(0,2);
  }

  public void KalkulacijaStavke(String what_kind_of_document,String name_field,char mode,String cskl,
                                boolean maloprodajna) {
    this.mode = mode;
    this.what_kind_of_document = what_kind_of_document;
    setVrzal(cskl);
    isMCFix = false;
    kalkSkladPart();
    if (name_field.equalsIgnoreCase("IPRODSP") && !maloprodajna) {
      kalkFinancPartRikverc();
    }
    else if ((name_field.equalsIgnoreCase("FMC") || name_field.equalsIgnoreCase("FMCPRP")) && maloprodajna) {
      kalkFinancPartMC();
    }
    else if (!maloprodajna){
      kalkFinancPart();
    }
    else if (maloprodajna){
      MaloprodajnaKalkulacija();
    }
  }
/*
  public void setupRacOtpremDummy4N(){
  }
*/
  /**
   * Kalkulira samo skladišni dio izlaznog dokumenta
   */
  
  public void pureKalkSkladPart() {
    kalkPrepareDoc();
    pureKalkSkladPartCommon();
  }
  
  public void pureKalkSkladPartFrom(ReadRow ulaz) {
    stavka.nc   = ulaz.getBigDecimal("NC");
    stavka.vc   = ulaz.getBigDecimal("VC");
    stavka.mc   = ulaz.getBigDecimal("MC");
    stavka.zc   = ulaz.getBigDecimal("ZC");
    pureKalkSkladPartCommon();
  }
  
  public void pureCopySkladPartFrom(ReadRow ulaz) {
    stavka.nc   = ulaz.getBigDecimal("NC");
    stavka.vc   = ulaz.getBigDecimal("VC");
    stavka.mc   = ulaz.getBigDecimal("MC");
    stavka.zc   = ulaz.getBigDecimal("ZC");
    stavka.inab = ulaz.getBigDecimal("INAB");
    stavka.ibp  = ulaz.getBigDecimal("IBP");
    stavka.isp  = ulaz.getBigDecimal("ISP");
    stavka.iraz = ulaz.getBigDecimal("IZAD");
    stavka.imar = ulaz.getBigDecimal("IMAR");
    stavka.ipor = ulaz.getBigDecimal("IPOR");
  }

  private void pureKalkSkladPartCommon() {
    stavka.inab = stavka.kol.multiply(stavka.nc).setScale(2, BigDecimal.ROUND_HALF_UP); // fixed 2 (ab.f)
    stavka.ibp  = stavka.vc.multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP);
    stavka.isp  = stavka.mc.multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP);
    stavka.iraz = stavka.zc.multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP);
    stavka.imar = BigDecimal.valueOf(0,2);
    stavka.ipor = BigDecimal.valueOf(0,2);
// buga  ne uzima pravo sklad
    if (vrzal.equalsIgnoreCase("V"))      {
      stavka.imar = stavka.ibp.subtract(stavka.inab);
    }
    else if (vrzal.equalsIgnoreCase("M")) {
      stavka.imar = stavka.ibp.subtract(stavka.inab);
      stavka.ipor = stavka.isp.subtract(stavka.ibp);
      }
  }

  public void kalkSkladPart() {
//? ovo je zbog neke greske who know why
    hr.restart.baza.dM.getDataModule().getSklad().open();
//?
    if (TD.isDocSklad(what_kind_of_document)) {
      pureKalkSkladPart();

    }
    else {
      kalkResetSkladPart();
    }
  }

  public void SetupPriceForSkladSide(){
    stavka.nc   = stanje.nc;
    stavka.vc   = stanje.vc;
    stavka.mc   = stanje.mc;
    stavka.zc   = stanje.zc;
  }

  /**
   * Priprema stavku izlaznog dokumenta tako da daje cijene iz stanja
   */
  public void kalkPrepareDoc() {
    if (mode !='I') {
      SetupPriceForSkladSide();
    }
  }
  /**
   * Resetira na nulu skladišni dio stavke izlaza
   */
  public void kalkResetSkladPart(){
    stavka.resetSklad();
  }
  /**
   * Resetira na nulu financijski dio stavke izlaza
   */
  public void kalkResetFinancPart(){
    stavka.ppor1 = BigDecimal.valueOf(0,2);
    stavka.ppor2 = BigDecimal.valueOf(0,2);
    stavka.ppor3 = BigDecimal.valueOf(0,2);
  }

  private void kalkFinancPartMC(){
    try {
      isMCFix = true;
      tmpBD = stavka.fmcprp.subtract(stavka.fmc);
      tmpBD = tmpBD.divide(stavka.fmcprp,8,BigDecimal.ROUND_HALF_UP);
      stavka.uprab = tmpBD.multiply(new BigDecimal("100.00"));
      tmpBDMC = stavka.fmc;
//      kalkFinancPart();
      kalkFinancMalIznos();
    }
    catch (Exception ex) {
      tmpBD = Aus.zero2;
      stavka.uprab = Aus.zero2;
      System.out.println("BigInteger divide by zero zero u kalkFinancPartMC()");
      System.out.println("greška mogu\u0107a zbog loose focus a cijena je nula");
      System.out.println("ali se ne bi smijela pojavljivati");
    }
  }

  private void kalkFinancPartRikverc(){
    if (TD.isDocFinanc(what_kind_of_document)) {
      if (stavka.kol.compareTo(Nula)!=0) {
        stavka.ineto= stavka.kol.multiply(stavka.fc).setScale(2, BigDecimal.ROUND_HALF_UP);   // fixed 2 (ab.f)
        stavka.fmc = stavka.iprodsp.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);

      /**
       * ovdje nije riješeno izbijanje više poreza u igri nego samo jednog
       */
      initTmp();

      tmpBD = stavka.ppor1.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      tmpBD = new BigDecimal("1.00").add(tmpBD);
      stavka.iprodbp = stavka.iprodsp.divide(tmpBD,2,BigDecimal.ROUND_HALF_UP);
      stavka.por1 = stavka.iprodsp.subtract(stavka.iprodbp);
      stavka.por2= Aus.zero2; // ne tretiram ispravno ostale poreze
      stavka.por3= Aus.zero2; // ali ionako se ova metoda više
      stavka.uipor = stavka.por1;          // ne koristi
      if (raIzlazTemplate.allowIznosChange()) {
        stavka.ineto = stavka.iprodbp;
        if (stavka.kol.signum()!=0)
          stavka.fc = stavka.ineto.divide(stavka.kol, 2, BigDecimal.ROUND_HALF_UP);
      } else stavka.uirab = stavka.ineto.subtract(stavka.iprodbp);

      initTmp();
      tmpBD = stavka.ineto.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.uprab = stavka.uirab.divide(tmpBD,2,BigDecimal.ROUND_HALF_UP);
      }
    }
    else {
      stavka.resetFinanc();
    }
  }

  private BigDecimal porezizbruta(BigDecimal bruto,BigDecimal posto){  // fixed 2 (ab.f)
    if (posto.compareTo(Nula)!=0) {
      return bruto.subtract(bruto.multiply(Sto.divide(Sto.add(posto),6,BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
      return Aus.zero2;
  }

  public void MaloprodajnaKalkulacija() {
      kalkFinancMalCijene();
      kalkFinancMalIznos();

  }

  private void kalkFinancMalIznos() {

    stavka.iprodsp = stavka.kol.multiply(stavka.fmc).setScale(2, BigDecimal.ROUND_HALF_UP); // fixed 2 (ab.f)
    stavka.por1= porezizbruta(stavka.iprodsp,stavka.ppor1);
    stavka.por2= porezizbruta(stavka.iprodsp,stavka.ppor2);
    stavka.por3= porezizbruta(stavka.iprodsp,stavka.ppor3);
    stavka.uipor = stavka.por1.add(stavka.por2).add(stavka.por3);  // fixed 2 (ab.f)
    stavka.iprodbp = stavka.iprodsp.subtract(stavka.uipor); 
    if (stavka.uprab.compareTo(Nula)!=0) {
      tmpBD= Jedan.subtract(stavka.uprab.divide(Sto,4,BigDecimal.ROUND_HALF_UP));
      stavka.ineto=stavka.iprodbp.divide(tmpBD,2,BigDecimal.ROUND_HALF_UP);
      if (what_kind_of_document.equals("PON")) stavka.uirab = stavka.ineto.subtract(stavka.iprodbp);
      else stavka.uirab = stavka.fmcprp.subtract(stavka.fmc).multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      stavka.ineto=stavka.iprodbp;
    }

    if (stavka.kol.compareTo(Nula)!=0) {  // ? zasto 3 ?  (ab.f)
      stavka.fvc = stavka.iprodbp.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);
      stavka.fc = stavka.ineto.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);
    }
//    outfinancPart();
  }

  private void kalkFinancMalCijene() {
    stavka.fmc=stavka.fmcprp.multiply(Sto.subtract(stavka.uprab)).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
  }

  public void kalkFinancPart(){

    if (TD.isDocFinanc(what_kind_of_document)) {
//_NETTO");
      stavka.ineto= stavka.kol.multiply(stavka.fc).setScale(2, BigDecimal.ROUND_HALF_UP);  // fixed 2 (ab.f)
//_RABAT
      stavka.uirab = stavka.ineto.multiply(stavka.uprab).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
//_ZT
      initTmp();
      tmpBD = stavka.ineto.subtract(stavka.uirab);
              stavka.uizt = tmpBD.multiply(stavka.upzt).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
//_BP
      stavka.iprodbp = stavka.ineto.subtract(stavka.uirab).add(stavka.uizt) ;
      if (stavka.kol.compareTo(Nula)!=0)
        stavka.fvc = stavka.iprodbp.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);

//_POREZ
      stavka.por1 = stavka.iprodbp.multiply(stavka.ppor1).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.por2 = stavka.iprodbp.multiply(stavka.ppor2).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.por3 = stavka.iprodbp.multiply(stavka.ppor3).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.uipor = stavka.por1.add(stavka.por2).add(stavka.por3);

//_SP
      stavka.iprodsp = stavka.iprodbp.add(stavka.por1).add(stavka.por2).add(stavka.por3);
      if (stavka.kol.compareTo(Nula)!=0) {
        stavka.fmc = stavka.iprodsp.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);
        if (stavka.uirab.compareTo(Nula)==0 && stavka.fmcprp.compareTo(stavka.fmc)!=0) {
          stavka.fmcprp = stavka.fmc;
        }
        else if (stavka.uirab.compareTo(Nula)!=0){
          tmpBD = stavka.ineto.multiply(stavka.ppor1).divide(Sto,2,BigDecimal.ROUND_HALF_UP);
          tmpBD = tmpBD.add(stavka.ineto.multiply(stavka.ppor2).divide(Sto,2,BigDecimal.ROUND_HALF_UP));
          tmpBD = tmpBD.add(stavka.ineto.multiply(stavka.ppor3).divide(Sto,2,BigDecimal.ROUND_HALF_UP));
          tmpBD = tmpBD.add(stavka.ineto);
          stavka.fmcprp = tmpBD.divide(stavka.kol,2,BigDecimal.ROUND_HALF_UP);
        }
      }

      if (isMCFix) { // poravnanje ako je kalkulacija po MC i rabati
        if (stavka.fmc.compareTo(tmpBD) != 0) {
          stavka.fmc = tmpBDMC;
          tmpBD = tmpBDMC.multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP).subtract(stavka.iprodsp); ; // fixed 2 (ab.f)
          raspodjelaSitneRazlike(tmpBD);
          stavka.iprodsp = tmpBDMC.multiply(stavka.kol).setScale(2, BigDecimal.ROUND_HALF_UP); // fixed 2 (ab.f)
        }
      }
    } else {
      stavka.resetFinanc();
    }

  }
  private void raspodjelaSitneRazlike(java.math.BigDecimal tmpBD_raz) {
    if (stavka.uipor.floatValue() > 0) { // razliku stavljamo na porez ako
      if (stavka.por1.floatValue()>0) {  // ga ima i raspodjeljujemo po
        stavka.por1 = stavka.por1.add(tmpBD_raz); // strukturi
      } else if (stavka.por2.floatValue()>0) {
        stavka.por2 = stavka.por2.add(tmpBD_raz);
      } else if (stavka.por3.floatValue()>0) {
        stavka.por3 = stavka.por3.add(tmpBD_raz);
      }
      stavka.uipor = stavka.uipor.add(tmpBD_raz);
    }else { // ina\u010De stavljamo na rabat
      stavka.uirab = stavka.uirab.add(tmpBD_raz);
    }

  }
/////////// STANJE
  public void VratiRezervu(String what_kind_of_document) {
    //if (hr.restart.sisfun.frmParam.getParam("robno","rezkol").equals("D")) {
      rKS.VratiRezervu(stanje,stavka,what_kind_of_document);
    //}
  }

  public void KalkulacijaStanje(String what_kind_of_document) {
    if (!raRobnoTrigger.isTriggerActive()) {
      rKS.kalkStanjefromStdoki(stanje,stavka,stavkaold,what_kind_of_document);
    }
  }

  public int TestStanje(){
    return rKS.TestStanje(stanje,stavka ,stavkaold,what_kind_of_document);
  }

  public void setWhat_kind_of_document(String what_kind_of_document) {
    this.what_kind_of_document = what_kind_of_document;

  }

  public BigDecimal getKolStanjeAfterMat(){
    return  stanje.kol.add(stavkaold.kol).subtract(stanje.kolrez).subtract(stavka.kol);
  }

  public boolean isKolStanjeManjeOd(BigDecimal bd){
    return (getKolStanjeAfterMat().compareTo(bd)<0);
  }

  public void outfinancPart(){

    System.out.println("stavka.kol     = "+stavka.kol);
    System.out.println("stavka.fc      = "+ stavka.fc);
    System.out.println("stavka.ineto   = "+ stavka.ineto);
    System.out.println("stavka.uprab   = "+ stavka.uprab);
    System.out.println("stavka.uirab   = "+stavka.uirab);
    System.out.println("stavka.fvc     = "+stavka.fvc);
    System.out.println("stavka.iprodbp = "+stavka.iprodbp);
    System.out.println("stavka.fmc     = "+stavka.fmc);
    System.out.println("stavka.fmcprp  = "+stavka.fmcprp);
    System.out.println("stavka.iprodsp = "+stavka.iprodsp);
    System.out.println("stavka.por1    = "+stavka.por1);
    System.out.println("stavka.por2    = "+stavka.por2);
    System.out.println("stavka.por3    = "+stavka.por3);
    System.out.println("stavka.ppor1   = "+stavka.ppor1);
    System.out.println("stavka.ppor2   = "+stavka.ppor2);
    System.out.println("stavka.ppor3   = "+stavka.ppor3);
    System.out.println("stavka.uipor   = "+stavka.uipor);


  }


}
