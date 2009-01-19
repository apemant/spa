/****license*****************************************************************
**   file: raKalkulBDMeskla.java
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

public class raKalkulBDMeskla {

  public raFakeBDStmeskla stavka;
  public raFakeBDStmeskla stavkaold;
  public raFakeBDStanje stanjeul;
  public raFakeBDStanje stanjeiz;
  public raKalkulBDStanje rKS = new raKalkulBDStanje();
  private BigDecimal Sto = new BigDecimal("100.00");
  private BigDecimal Nula = Aus.zero2;
  private BigDecimal tmpBD = Aus.zero2;
  private BigDecimal tmpBD2 = Aus.zero2;

  private void initTmp() {
    tmpBD = Aus.zero2;
    tmpBD2 = Aus.zero2;
  }

  public raKalkulBDMeskla() {

    stavka = new raFakeBDStmeskla();
    stavkaold = new raFakeBDStmeskla();
    stanjeul = new raFakeBDStanje();
    stanjeiz = new raFakeBDStanje();
  }

  public void ponistavanjeUlaza(){
    stavka.ponistavanjeUlaza();
  }

  public void ponistavanjeIzlaza(){
    stavka.ponistavanjeIzlaza();
  }

  public void setupOldPrice() {
    stavka.skol       = stanjeul.kol;
    stavka.snc        = stanjeul.nc;
    stavka.svc        = stanjeul.vc;
    stavka.smc        = stanjeul.mc;
  }

  public void returnOldPrice() {
    stanjeul.nc =    stavkaold.snc;
    stanjeul.vc =    stavkaold.svc;
    stanjeul.mc =    stavkaold.smc;
    if (stanjeul.sVrSklad.equalsIgnoreCase("N")){
      stanjeul.zc =    stavkaold.snc;
    } else if (stanjeul.sVrSklad.equalsIgnoreCase("V")){
      stanjeul.zc =    stavkaold.svc;
    } else if (stanjeul.sVrSklad.equalsIgnoreCase("M")){
      stanjeul.zc =    stavkaold.smc;
    }
  }

  public void setupPriceMEU(){

    initTmp();
    stavka.nc = stanjeul.nc;
    stavka.vc = stanjeul.vc;
    stavka.mc = stanjeul.mc;
//    stavka.zc = stanjeul.zc;
//    stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
    tmpBD  = stavka.vc.subtract(stavka.nc);
    tmpBD2 = stavka.nc.divide(Sto,4,BigDecimal.ROUND_HALF_UP);
    if (tmpBD2.doubleValue()!=0) {
      stavka.pmar = tmpBD.divide(tmpBD2,2,BigDecimal.ROUND_HALF_UP);
    }
    else {
      stavka.pmar = Nula;
    }
//
/*
    System.out.println("stanjeul.sVrSklad "+stanjeul.sVrSklad);
    if (stanjeul.sVrSklad.equals("N"))       stavka.zcul = stanjeul.nc;
    else if (stanjeul.sVrSklad.equals("V"))  stavka.zcul = stanjeul.vc;
    else if (stanjeul.sVrSklad.equals("M"))  stavka.zcul = stanjeul.mc;
*/
  }
  
  public void alterIzlaz() {
  	if (stanjeiz.sVrSklad.equals("N")) {
  		stavka.nc = stavka.zc;
  	} else if (stanjeiz.sVrSklad.equals("V")) {
  		stavka.vc = stavka.zc;
  		kalkPrice("VC");
  	} else if (stanjeiz.sVrSklad.equals("M")) {
  		stavka.mc = stavka.zc;
  		kalkPrice("MC");
  	}
  	stanjeiz.nc = stavka.nc;
  	stanjeiz.vc = stavka.vc;
  	stanjeiz.mc = stavka.mc;
  	stanjeiz.zc = stavka.zc;
  }
  
  public void alterUlaz() {
  	if (stanjeul.sVrSklad.equals("V")) {
  		stavka.vc = stavka.zcul;
  		kalkPrice("VC");
  	} else if (stanjeul.sVrSklad.equals("M")) {
  		stavka.mc = stavka.zcul;
  		kalkPrice("MC");
  	}
  }

  public void setupPrice(){
    initTmp();
    stavka.nc = stanjeiz.nc;
    stavka.vc = stanjeiz.vc;
    stavka.mc = stanjeiz.mc;
    stavka.zc = stanjeiz.zc;
    
    if ( stanjeul.sVrSklad.equals("N")) {
    	stavka.zcul = stavka.nc;
    } else if ( stanjeul.sVrSklad.equals("V")) {
    	stavka.zcul = stavka.vc;
    } else if ( stanjeul.sVrSklad.equals("M")) {
    	stavka.zcul = stavka.mc;
    }
    
//    stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
    tmpBD  = stavka.vc.subtract(stavka.nc);
    tmpBD2 = stavka.nc.divide(Sto,4,BigDecimal.ROUND_HALF_UP);
    if (tmpBD2.doubleValue()!=0) {
      stavka.pmar = tmpBD.divide(tmpBD2,2,BigDecimal.ROUND_HALF_UP);
    }
    else {
      stavka.pmar = Nula;
    }
//
//    if (stanjeul.sVrSklad.equals("N"))       stavka.zcul = stanjeul.nc;
//    else if (stanjeul.sVrSklad.equals("V"))  stavka.zcul = stanjeul.vc;
//    else if (stanjeul.sVrSklad.equals("M"))  stavka.zcul = stanjeul.mc;
  }

  public void Kalkulacija() {

/// izlazni dio
//    stavka.inabiz    = stavka.kol * stanjeiz.nc;
//    stavka.inabiz    = stavka.kol.multiply(stanjeiz.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
    stavka.inabiz    = stavka.kol.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);

//    stavka.zadraziz  = stavka.zc  * stavka.kol;
    stavka.zadraziz  = stavka.zc.multiply(stavka.kol).setScale(2,BigDecimal.ROUND_HALF_UP);

//    stavka.imariz    = (stavka.kol * stavka.vc ) - (stavka.kol  *  stavka.nc);
    initTmp();
    tmpBD = stavka.kol.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
    tmpBD2 = stavka.kol.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
    stavka.imariz = tmpBD.subtract(tmpBD2);

//      stavka.iporiz    = (stavka.kol * stavka.mc ) - (stavka.kol * stavka.vc );
    initTmp();
    tmpBD = stavka.kol.multiply(stavka.mc).setScale(2,BigDecimal.ROUND_HALF_UP);
    tmpBD2 = stavka.kol.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
    stavka.iporiz = tmpBD.subtract(tmpBD2);
//
    if ( stanjeiz.sVrSklad.equals("N")) {
      stavka.imariz    = Nula;
      stavka.iporiz    = Nula;
    }
    else if ( stanjeiz.sVrSklad.equals("V")){
      stavka.iporiz    = Nula;
    }
/// ulazni dio
    stavka.inabul    = stavka.kol.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
    initTmp();
    tmpBD = stavka.kol.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
    tmpBD2 = stavka.kol.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
    stavka.imarul = tmpBD.subtract(tmpBD2);
    initTmp();
    tmpBD = stavka.kol.multiply(stavka.mc).setScale(2,BigDecimal.ROUND_HALF_UP);
    tmpBD2 = stavka.kol.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
    stavka.iporul = tmpBD.subtract(tmpBD2);
//
    if ( stanjeul.sVrSklad.equals("N")) {
      stavka.imarul    = Nula ;
      stavka.iporul    = Nula ;
      stavka.porav     = Nula ;
      stavka.diopormar = Nula ;
      stavka.dioporpor = Nula ;
//      stavka.zadrazul  = stavka.kol * stavka.nc;
      stavka.zadrazul = stavka.kol.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD = stanjeul.kolul.add(stavka.kol).subtract(stavkaold.kol).subtract(stanjeul.koliz);
      tmpBD2 = stanjeul.vul.add(stavka.zadrazul).subtract(stavkaold.zadrazul).subtract(stanjeul.viz);
      /*if (tmpBD.doubleValue()!= 0) {
        stavka.zcul = tmpBD2.divide(tmpBD,2,BigDecimal.ROUND_HALF_UP);
      }*/
      stavka.zcul = stavka.nc;
    }
    else if ( stanjeul.sVrSklad.equals("V")){
      stavka.iporul    = Nula;
//        stavka.porav     = (stanjeul.kol * stavka.vc) - (stanjeul.kol * stanjeul.vc);
      initTmp();
/*
      tmpBD = stanjeul.kol.subtract(stavkaold.kol);
      tmpBD = tmpBD.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2 = stanjeul.kol.subtract(stavkaold.kol);
      tmpBD2 = tmpBD2.multiply(stavka.svc).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.porav = tmpBD.subtract(tmpBD2);
*/
      tmpBD2 = stavka.vc.subtract(stavka.svc);
      stavka.porav = stavka.skol.multiply(tmpBD2).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.diopormar = stavka.porav;
      stavka.dioporpor =  Nula;
      stavka.zadrazul = stavka.kol.multiply(stavka.vc).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.zcul = stavka.vc;
    }
    else if ( stanjeul.sVrSklad.equals("M")){
//      stavka.porav     = (stanjeul.kol * stavka.mc) - (stanjeul.kol * stanjeul.mc);
      initTmp();
/* staro i ne valja koji put radi ružne buge u kalkulaciji
      tmpBD    = stanjeul.kol.subtract(stavkaold.kol);
      tmpBD    = tmpBD.multiply(stavka.mc).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2   = stanjeul.kol.subtract(stavkaold.kol);
      tmpBD2   = tmpBD2.multiply(stavka.smc).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.porav = tmpBD.subtract(tmpBD2);
//      stavka.dioporpor = stavka.porav*stavka.reverzpostopor/100;
      stavka.dioporpor = stavka.porav.multiply(stavka.reverzpostopor).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.dioporpor = stavka.dioporpor.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
//      stavka.diopormar = stavka.porav - stavka.dioporpor;
      stavka.diopormar = stavka.porav.subtract(stavka.dioporpor);
*/
      tmpBD = stavka.vc.subtract(stavka.svc);
      tmpBD2 = stavka.mc.subtract(stavka.smc);
      stavka.diopormar = stavka.skol.multiply(tmpBD).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.porav = stavka.skol.multiply(tmpBD2).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.dioporpor =  stavka.porav.subtract(stavka.diopormar);
//      stavka.zadrazul  = stavka.kol * stavka.mc;
      stavka.zadrazul = stavka.kol.multiply(stavka.mc).setScale(2,BigDecimal.ROUND_HALF_UP);
      stavka.zcul = stavka.mc;
    }
  }

  public void kalkPrice(String how) {
    if (how.equals("PMARA")){
      // varijanta s promjenom nabavne vrijednosti a da se ne mijenjaju VC i MC
      // ide na teskocu marze
      if (stavka.vc.doubleValue()==0) {
        kalkPrice("PMAR");
      }
      else {
/*
        (stavka.vc - stavka.nc)/(stavka.nc/100)
*/
        initTmp();
        tmpBD    = stavka.vc.subtract(stavka.nc);
        tmpBD2   = stavka.nc.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
        stavka.pmar = tmpBD.divide(tmpBD2,2,BigDecimal.ROUND_HALF_UP);
      }
    }
    else if (how.equals("PMAR")){
//      stavka.vc=stavka.nc+(stavka.pmar*stavka.nc/100);
      initTmp();
      tmpBD    = stavka.pmar.multiply(stavka.nc).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2   = tmpBD.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.vc = stavka.nc.add(tmpBD2);
//      stavka.mc=stavka.vc+ (stavka.vc*stavka.postopor/100);
      initTmp();
      tmpBD     = stavka.vc.multiply(stavka.postopor).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2    = tmpBD.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.mc = stavka.vc.add(tmpBD2);
    }
    else if (how.equals("VC")){
//      stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
      initTmp();
      tmpBD    = stavka.vc.subtract(stavka.nc);
      tmpBD2   = stavka.nc.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      if (tmpBD.doubleValue()!=0) {
        stavka.pmar = tmpBD.divide(tmpBD2,2,BigDecimal.ROUND_HALF_UP);
      }
      else {
        stavka.pmar = Nula;
      }
//      stavka.mc=stavka.vc+ (stavka.vc*stavka.postopor/100);
      initTmp();
      tmpBD     = stavka.vc.multiply(stavka.postopor).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2    = tmpBD.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.mc = stavka.vc.add(tmpBD2);
    }
    else if (how.equals("MC")){
//System.out.println("kalkulacija MC");
//      stavka.vc=stavka.mc-(stavka.mc*stavka.reverzpostopor/100);
      initTmp();
      tmpBD     = stavka.mc.multiply(stavka.reverzpostopor).setScale(2,BigDecimal.ROUND_HALF_UP);
      tmpBD2    = tmpBD.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      stavka.vc = stavka.mc.subtract(tmpBD2);
//      stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
      initTmp();
      tmpBD    = stavka.vc.subtract(stavka.nc);
      tmpBD2   = stavka.nc.divide(Sto,2,BigDecimal.ROUND_HALF_UP);
      if (tmpBD.doubleValue()!=0) {
         stavka.pmar = tmpBD.divide(tmpBD2,2,BigDecimal.ROUND_HALF_UP);
       }
       else {
         stavka.pmar = Nula;
      }
    }
  }

  public void kalkStanja() {
    if(!raRobnoTrigger.isTriggerActive()) {
     rKS.kalkStanjefromStmesklaUlaz(stanjeul,stavka,stavkaold,true);
     rKS.kalkStanjefromStmesklaIzlaz(stanjeiz,stavka,stavkaold);
    }
   }
   public int TestStanja(){
     return rKS.TestStanje(stanjeiz,stavka,stavkaold);
/*
     if (stanjeiz.kol<0) return -1;
     else if (stanjeiz.kol + stavkaold.kol - stanjeiz.kolrez  < stavka.kol ) return -2;
     return 0;
*/
  }
  public BigDecimal getKolStanjeAfterMat(){
    return  stanjeiz.kol.add(stavkaold.kol).subtract(stanjeiz.kolrez).subtract(stavka.kol);
  }

  public boolean isKolStanjeManjeOd(BigDecimal bd){
    return (getKolStanjeAfterMat().compareTo(bd)<0);
  }



}