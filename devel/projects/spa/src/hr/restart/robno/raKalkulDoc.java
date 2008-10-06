/****license*****************************************************************
**   file: raKalkulDoc.java
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


public class raKalkulDoc extends raTopCalcUtil{

  private TypeDoc TD = TypeDoc.getTypeDoc();
  private double tmpPosto = 0;
  public raFakeStavke stavka = new raFakeStavke();
  public raFakeStavke stavkaold = new raFakeStavke();
  public raFakeStanje stanje = new raFakeStanje();
  public raKalkulStanje rKS = new raKalkulStanje();
  char mode =' ';
  /**
   * Zna\u010Di treba zaobi\u0107i SimpleKalkConn i ovdje raditi sve tako da imamo stavke i ostalo i iz
   * gkoda kada prebacivati. Kalkulacija \u0107e biti u ovisnosti o tipu poziva kalkulacije i biti \u0107e
   * KalkulStavke i basta
   *
   */

  public raKalkulDoc() {
    stavka.Init();
    stavkaold.Init();
    stanje.Init();
  }


  public void KalkulacijaStavke(String what_kind_of_document,String name_field,char mode) {
System.out.println("KalkulacijaStavke pod normalno");
    this.mode = mode;
    if (TD.isDocUlaz(what_kind_of_document) && !TD.isDocMeskla(what_kind_of_document)) {
      if (TD.isDocSklad(what_kind_of_document) && TD.isDocFinanc(what_kind_of_document)){}
      else {}
    }
    else if (!TD.isDocUlaz(what_kind_of_document) && !TD.isDocMeskla(what_kind_of_document)){
      System.out.println("!TD.isDocUlaz(what_kind_of_document) && !TD.isDocMeskla(what_kind_of_document))");
        this.kalkRacOtprem(name_field);
    }
    else if (TD.isDocMeskla(what_kind_of_document)){}
  }

////////////////////////////////
// /RA\u010CUN OTPREMNICE
////////////////////////////////

  public void setupRacOtpremDummy4IB(){
        if (!(stavka.kol == 0)) {
          stavka.jirab = stavka.uirab / stavka.kol;
          stavka.jizt  = stavka.uizt  / stavka.kol;
          stavka.ppor1 = stavka.por1 / stavka.kol;
          stavka.ppor2 = stavka.por2 / stavka.kol;
          stavka.ppor3 = stavka.por3 / stavka.kol;
      }
  }
  public void setupRacOtpremDummy4N(){
        stavka.jirab = stavka.uprab * stavka.fc / 100.;
        stavka.jizt  = stavka.upzt * (stavka.fc - stavka.jirab) / 100.;
        stavka.fvc   = stavka.fc - stavka.jirab + stavka.jizt ;
        stavka.ppor1 = stavka.pospor1 * stavka.fvc / 100.;
        stavka.ppor2 = stavka.pospor2 * stavka.fvc / 100.;
        stavka.ppor3 = stavka.pospor3 * stavka.fvc / 100.;
        stavka.fmc   = stavka.fvc + stavka.ppor1 + stavka.ppor2 + stavka.ppor3;
  }

  /**
   * Kalkulira samo skladišni dio izlaznog dokumenta
   */

  public void kalkSkladPart() {
    stavka.inab = stavka.kol * stavka.nc;
    stavka.ibp  = stavka.vc * stavka.kol;
    stavka.imar = stavka.ibp - stavka.inab;
    stavka.isp  = stavka.mc * stavka.kol;
    stavka.ipor = stavka.isp - stavka.ibp;
    stavka.iraz = stavka.zc * stavka.kol;
  }
  /**
   * Priprema stavku izlaznog dokumenta tako da daje cijene iz stanja
   */
  public void kalkPrepareDoc(){
      stavka.nc   = stanje.nc;
      stavka.vc   = stanje.vc;
      stavka.mc   = stanje.mc;
      stavka.zc   = stanje.zc ;

  }
  /**
   * Resetira na nulu skladišni dio stavke izlaza
   */
  public void kalkResetSkladPart(){
      stavka.nc   = 0;
      stavka.inab = 0;
      stavka.vc   = 0;
      stavka.ibp  = 0;
      stavka.imar = 0;
      stavka.mc   = 0;
      stavka.isp  = 0;
      stavka.ipor = 0;
      stavka.zc   = 0;
      stavka.iraz = 0;
  }
  /**
   * Resetira na nulu financijski dio stavke izlaza
   */
  public void kalkResetFinancPart(){
       stavka.jirab = 0;
       stavka.jizt  = 0;
       stavka.ppor1 = 0;
       stavka.ppor2 = 0;
       stavka.ppor3 = 0;
  }

  private void kalkRacOtprem(String name_field){
    tmpPosto = 0;
    if (name_field.equals("_NETTO")){
      stavka.ineto= stavka.kol * stavka.fc ;
    }
    else if (name_field.equals("_RAZDUZ")){
      kalkPrepareDoc();
      kalkSkladPart();
    }
    else if (name_field.equals("_NULARAZDUZ")){
      kalkResetSkladPart();
    }
    else if (name_field.equals("_NETTO_BP")){
      tmpPosto = 1 - (stavka.uprab / 100.) + (stavka.upzt / 100.) - ((stavka.uprab * stavka.upzt) / 100.);
      stavka.ineto = stavka.iprodbp / tmpPosto ;
    }
    else if (name_field.equals("_FC")){
      if (!(stavka.kol == 0))
        stavka.fc = stavka.ineto / stavka.kol;
      else
        stavka.fc = 0;
    }
    else if (name_field.equals("_RABAT")){
      stavka.uirab = stavka.ineto * stavka.uprab / 100. ;
      if (!(stavka.kol == 0))
        stavka.jirab = stavka.uirab / stavka.kol ;
      else
        stavka.jirab = 0;
    }
    else if (name_field.equals("_JRABAT")){
      stavka.uirab = stavka.jirab * stavka.kol;
      if (!(stavka.ineto == 0))
        stavka.uprab = stavka.uirab * 100. / stavka.ineto;
      else
        stavka.uprab = 0;
    }
    else if (name_field.equals("_ZT")){
      stavka.uizt  = (stavka.ineto - stavka.uirab) * stavka.upzt / 100. ;
      if (!(stavka.kol == 0))
        stavka.jizt = stavka.uizt / stavka.kol ;
      else
        stavka.jizt = 0;
    }
    else if (name_field.equals("_JZT")){
      stavka.uizt = stavka.jizt * stavka.kol;
      if (!(stavka.ineto == 0))
        stavka.upzt = stavka.uizt * 100. / (stavka.ineto-stavka.uirab);
      else
        stavka.upzt = 0;
    }
    else if (name_field.equals("_FVC")){
      stavka.iprodbp = stavka.kol * stavka.fvc;
    }
    else if (name_field.equals("_BP")){
      stavka.iprodbp = stavka.ineto - stavka.uirab + stavka.uizt ;
      if (!(stavka.kol == 0)) stavka.fvc = stavka.iprodbp / stavka.kol ;
    }
    else if (name_field.equals("_BP_SP")){
      stavka.iprodbp = stavka.iprodsp - stavka.por1 - stavka.por2 - stavka.por3 ;
      if (!(stavka.kol == 0)) stavka.fvc = stavka.iprodbp / stavka.kol ;
    }
    else if (name_field.equals("_POREZ")){
      stavka.por1 = stavka.iprodbp * stavka.pospor1 / 100. ;
      if (!(stavka.kol == 0)) stavka.ppor1 = stavka.por1 / stavka.kol ;
      stavka.por2 = stavka.iprodbp * stavka.pospor2 / 100. ;
      if (!(stavka.kol == 0)) stavka.ppor2 = stavka.por2 / stavka.kol ;
      stavka.por3 = stavka.iprodbp * stavka.pospor3 / 100. ;
      if (!(stavka.kol == 0)) stavka.ppor3 = stavka.por3 / stavka.kol ;
    }
    else if (name_field.equals("_POREZ_SP")){
      stavka.por1do3 = stavka.pospor1 + stavka.pospor2 + stavka.pospor3;
      tmpPosto = stavka.iprodsp / (100.+stavka.por1do3);
      stavka.por1 = tmpPosto * stavka.pospor1 ;
      if (!(stavka.kol == 0)) stavka.ppor1 = stavka.por1 / stavka.kol ;
      stavka.por2 = tmpPosto * stavka.pospor2 ;
      if (!(stavka.kol == 0)) stavka.ppor2 = stavka.por2 / stavka.kol ;
      stavka.por3 = tmpPosto * stavka.pospor3;
      if (!(stavka.kol == 0)) stavka.ppor3 = stavka.por3 / stavka.kol ;
    }
    else if (name_field.equals("_FMC")){
      stavka.iprodsp = stavka.kol * stavka.fmc;
    }
    else if (name_field.equals("_SP")){
      stavka.iprodsp = stavka.iprodbp + stavka.por1 + stavka.por2 + stavka.por3;
      if (!(stavka.kol == 0)) stavka.fmc = stavka.iprodsp / stavka.kol ;
    }
    else if (name_field.equals("KOL")||name_field.equals("FC")){
      kalkRacOtprem("_NETTO");
      kalkRacOtprem("_RABAT");
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_BP");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
      if (name_field.equals("KOL")) {
        kalkRacOtprem("_RAZDUZ");
      }
    }
    else if (name_field.equals("UPRAB")){
      kalkRacOtprem("_RABAT");
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_BP");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
    }
    else if (name_field.equals("JIRAB")){
      kalkRacOtprem("_JRABAT");
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_BP");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
    }
    else if (name_field.equals("UPZT")){
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_BP");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
    }
    else if (name_field.equals("JIZT")){
      kalkRacOtprem("_JZT");
      kalkRacOtprem("_BP");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
    }
    else if (name_field.equals("FVC")){
      kalkRacOtprem("_FVC");
      kalkRacOtprem("_NETTO_BP");
      kalkRacOtprem("_FC");
      kalkRacOtprem("_RABAT");
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_POREZ");
      kalkRacOtprem("_SP");
    }
    else if (name_field.equals("FMC")){
      kalkRacOtprem("_FMC");
      kalkRacOtprem("_POREZ_SP");
      kalkRacOtprem("_BP_SP");
      kalkRacOtprem("_NETTO_BP");
      kalkRacOtprem("_RABAT");
      kalkRacOtprem("_ZT");
      kalkRacOtprem("_FC");
    }
  }

/////////// STANJE
  public void VratiRezervu(String what_kind_of_document) {
    rKS.VratiRezervu(stanje,stavka,what_kind_of_document);
  }

  public void KalkulacijaStanje(String what_kind_of_document) {
    rKS.kalkStanjefromStdoki(stanje,stavka,stavkaold,what_kind_of_document);
  }

  public int TestStanje(){
    return rKS.TestStanje(stanje,stavka ,stavkaold);
  }
}