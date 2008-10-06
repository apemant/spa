/****license*****************************************************************
**   file: raKalkulMeskla.java
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

public class raKalkulMeskla {

  public raFakeStmeskla stavka;
  public raFakeStmeskla stavkaold;
  public raFakeStanje stanjeul;
  public raFakeStanje stanjeiz;
  public raKalkulStanje rKS = new raKalkulStanje();

  public raKalkulMeskla() {
    stavka = new raFakeStmeskla();
    stavkaold = new raFakeStmeskla();
    stanjeul = new raFakeStanje();
    stanjeiz = new raFakeStanje();
  }

  public void ponistavanjeUlaza(){

      stavka.skol      = 0;
      stavka.snc       = 0;
      stavka.svc       = 0;
      stavka.smc       = 0;
      stavka.zcul      = 0;
      stavka.inabul    = 0;
      stavka.imarul    = 0;
      stavka.iporul    = 0;
      stavka.porav     = 0 ;
      stavka.diopormar = 0 ;
      stavka.dioporpor = 0 ;
      stavka.zadrazul  = 0;
  }
  public void ponistavanjeIzlaza(){

    stavka.inabiz    = 0;
    stavka.zadraziz  = 0;
    stavka.imariz    = 0;
    stavka.iporiz    = 0;


  }
  public void kalkStanja() {
    rKS.kalkStanjefromStmesklaUlaz(stanjeul,stavka,stavkaold);
    rKS.kalkStanjefromStmesklaIzlaz(stanjeiz,stavka,stavkaold);
  }
  public int TestStanja(){
    if (stanjeiz.kol<0) return -1;
    else if (stanjeiz.kol + stavkaold.kol - stanjeiz.kolrez  < stavka.kol ) return -2;
    return 0;
  }

  public void setupOldPrice() {
    stavka.skol       = stanjeul.kol;
    stavka.snc        = stanjeul.nc;
    stavka.svc        = stanjeul.vc;
    stavka.smc        = stanjeul.mc;
  }
  public void setupPriceMEU(){
    stavka.nc = stanjeul.nc;
    stavka.vc = stanjeul.vc;
    stavka.mc = stanjeul.mc;
    stavka.zc = stanjeul.zc;
    stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
    if (stanjeul.sVrSklad.equals("N"))       stavka.zcul = stanjeul.nc;
    else if (stanjeul.sVrSklad.equals("V"))  stavka.zcul = stanjeul.vc;
    else if (stanjeul.sVrSklad.equals("M"))  stavka.zcul = stanjeul.mc;
  }

  public void setupPrice(){
    stavka.nc = stanjeiz.nc;
    stavka.vc = stanjeiz.vc;
    stavka.mc = stanjeiz.mc;
    stavka.zc = stanjeiz.zc;
    stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
    if (stanjeul.sVrSklad.equals("N"))       stavka.zcul = stanjeiz.nc;
    else if (stanjeul.sVrSklad.equals("V"))  stavka.zcul = stanjeiz.vc;
    else if (stanjeul.sVrSklad.equals("M"))  stavka.zcul = stanjeiz.mc;
  }
  public void Kalkulacija() {

/// izlazni dio
    stavka.inabiz    = stavka.kol * stanjeiz.nc;
    stavka.zadraziz  = stavka.zc  * stavka.kol;
    stavka.imariz    = (stavka.kol * stavka.vc ) - (stavka.kol  *  stavka.nc);
    stavka.iporiz    = (stavka.kol * stavka.mc ) - (stavka.kol * stavka.vc );
    if ( stanjeiz.sVrSklad.equals("N")) {
      stavka.imariz    = 0;
      stavka.iporiz    = 0;
    }
    else if ( stanjeiz.sVrSklad.equals("V")){
      stavka.iporiz    = 0;
    }
/// ulazni dio
      stavka.inabul    = stavka.kol * stavka.nc;
      stavka.imarul    = (stavka.kol * stavka.vc) - (stavka.kol * stavka.nc);
      stavka.iporul    = (stavka.kol * stavka.mc) - (stavka.kol * stavka.vc);

      if ( stanjeul.sVrSklad.equals("N")) {
        stavka.imarul    = 0 ;
        stavka.iporul    = 0 ;
        stavka.porav     = 0 ;
        stavka.diopormar = 0 ;
        stavka.dioporpor = 0 ;
        stavka.zadrazul  = stavka.kol * stavka.nc;
      }
      else if ( stanjeul.sVrSklad.equals("V")){
        stavka.iporul    = 0;
        stavka.porav     = (stanjeul.kol * stavka.vc) - (stanjeul.kol * stanjeul.vc);
        stavka.diopormar = stavka.porav;
        stavka.dioporpor =  0;
        stavka.zadrazul  = stavka.kol * stavka.vc;
      }
      else if ( stanjeul.sVrSklad.equals("M")){
        stavka.porav     = (stanjeul.kol * stavka.mc) - (stanjeul.kol * stanjeul.mc);
        stavka.dioporpor = stavka.porav*stavka.reverzpostopor/100;
        stavka.diopormar = stavka.porav - stavka.dioporpor;
        stavka.zadrazul  = stavka.kol * stavka.mc;
      }
  }
  public void kalkPrice(String how) {
    if (how.equals("PMAR")){
      stavka.vc=stavka.nc+(stavka.pmar*stavka.nc/100);
      stavka.mc=stavka.vc+ (stavka.vc*stavka.postopor/100);
    }
    else if (how.equals("VC")){
      stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
      stavka.mc=stavka.vc+ (stavka.vc*stavka.postopor/100);
    }
    else if (how.equals("MC")){
      stavka.vc=stavka.mc-(stavka.mc*stavka.reverzpostopor/100);
      stavka.pmar = (stavka.vc-stavka.nc)/(stavka.nc/100);
    }

  }
}