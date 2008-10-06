/****license*****************************************************************
**   file: raKalkulStanje.java
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


public class raKalkulStanje {
/**
 *
 * @param stanje
 * @param stavka
 * @param stavkaold
 * @param what_kind_of_document
 * PST,PRI,POR,POD,TER,ODB,INV
 *
 */


  private TypeDoc TD = TypeDoc.getTypeDoc();
  public void kalkStanjefromStdoku(raFakeStanje stanje,raFakeStdoku stavka ,
                                   raFakeStdoku stavkaold,
                                   String what_kind_of_document)
  {
    if (what_kind_of_document.equals("PST"))
    {
      stanje.kolps = stanje.kolps + stavka.kol  - stavkaold.kol;
      stanje.nabps = stanje.nabps + stavka.inab - stavkaold.inab;
      stanje.marps = stanje.marps + stavka.imar - stavkaold.imar;
      stanje.porps = stanje.porps + stavka.ipor - stavkaold.ipor;
      stanje.vps   = stanje.vps   + stavka.izad - stavkaold.izad;
    }
    if (what_kind_of_document.equals("PST") ||
        what_kind_of_document.equals("PRI") ||
        what_kind_of_document.equals("POR") ||
        what_kind_of_document.equals("PRK") ||
        what_kind_of_document.equals("KAL") ||
        what_kind_of_document.equals("PTE") ||
        what_kind_of_document.equals("INV"))
    {
      stanje.kolul = stanje.kolul + stavka.kol  - stavkaold.kol;
      stanje.nabul = stanje.nabul + stavka.inab - stavkaold.inab;
      stanje.marul = stanje.marul + stavka.imar - stavkaold.imar +
                     stavka.diopormar - stavkaold.diopormar;
      stanje.porul = stanje.porul + stavka.ipor - stavkaold.ipor +
                     stavka.dioporpor - stavkaold.dioporpor;
      stanje.vul   = stanje.vul   + stavka.izad - stavkaold.izad +
                     stavka.porav - stavkaold.porav;
      ClasicCalc(stanje);
      CalcNc(stanje);
    }
  }
  public void SetupPrice(raFakeStanje stanje,raFakeStdoku stavka,String what_kind_of_document){
    if (what_kind_of_document.equals("PST") ||
        what_kind_of_document.equals("POR") ||
        what_kind_of_document.equals("PRK") ||
        what_kind_of_document.equals("KAL"))
    {
      stanje.vc= stavka.vc ;
      stanje.mc= stavka.mc ;
      setupZC(stanje);
    }
  }

  public void kalkStanjefromStmesklaUlaz(raFakeStanje stanjeul,
                                         raFakeStmeskla stavka,
                                         raFakeStmeskla stavkaold){

      stanjeul.kolul   = stanjeul.kolul + stavka.kol    - stavkaold.kol;
      stanjeul.nabul   = stanjeul.nabul + stavka.inabul - stavkaold.inabul;
      stanjeul.marul   = stanjeul.marul + stavka.imarul - stavkaold.imarul+
                         stavka.diopormar - stavkaold.diopormar ;
      stanjeul.porul   = stanjeul.porul + stavka.iporul - stavkaold.iporul +
                         stavka.dioporpor - stavkaold.dioporpor ;
      stanjeul.porav   = stanjeul.porav + stavka.porav  - stavkaold.porav;
      stanjeul.vul     = stanjeul.vul + stavka.zadrazul + stavka.porav -
                         stavkaold.zadrazul - stavkaold.porav;
      stanjeul.vc      = stavka.vc;
      stanjeul.mc      = stavka.mc;

      ClasicCalc(stanjeul);
      CalcNc(stanjeul);
      setupZC(stanjeul);
  }
  public void kalkStanjefromStmesklaIzlaz(raFakeStanje stanjeiz,
                                          raFakeStmeskla stavka,
                                          raFakeStmeskla stavkaold){
      stanjeiz.koliz = stanjeiz.koliz + stavka.kol      - stavkaold.kol;
      stanjeiz.nabiz = stanjeiz.nabiz + stavka.inabiz   - stavkaold.inabiz;
      stanjeiz.mariz = stanjeiz.mariz + stavka.imariz   - stavkaold.imariz;
      stanjeiz.poriz = stanjeiz.poriz + stavka.iporiz   - stavkaold.iporiz;
      stanjeiz.viz   = stanjeiz.viz   + stavka.zadraziz - stavkaold.zadraziz;
      ClasicCalc(stanjeiz);
  }

  /**
   *
   * @param stanje
   * @param stavka
   * @param stavkaold
   * @param what_kind_of_document
   *
   * what_kind_of_document može biti GOT IZD OTP POS PRD RAC ROT, OTR,INM
   *
   * NKU, PON,PRD samo eventualno rezervoraju koli\u010Dinu
   * RAC, TER,ODB, cak ni to
   *
   */
  public void kalkStanjefromStdoki(raFakeStanje stanje,raFakeStavke stavka ,
                                   raFakeStavke stavkaold,
                                   String what_kind_of_document)
  {
    if (what_kind_of_document.equals("IZD") ||
        what_kind_of_document.equals("OTP") ||
        what_kind_of_document.equals("ROT") ||
        what_kind_of_document.equals("GOT") ||
        what_kind_of_document.equals("POS") ||
        what_kind_of_document.equals("OTR") ||
        what_kind_of_document.equals("INM"))
    {
      stanje.koliz = stanje.koliz + stavka.kol  - stavkaold.kol;
      stanje.viz   = stanje.viz   + stavka.iraz - stavkaold.iraz;
      stanje.nabiz = stanje.nabiz + stavka.inab - stavkaold.inab;
      stanje.mariz = stanje.mariz + stavka.imar - stavkaold.imar;
      stanje.poriz = stanje.poriz + stavka.ipor - stavkaold.ipor;
      ClasicCalc(stanje);
    }
    if ((what_kind_of_document.equals("NKU") ||
        what_kind_of_document.equals("PON") ||
        what_kind_of_document.equals("PRD")) &&
        stavka.rezkol.equals("D"))
    {
      stanje.kolrez = stanje.kolrez + stavka.kol - stavkaold.kol;
    }
  }

  private void ClasicCalc(raFakeStanje stanje){

    stanje.kol = stanje.kolul - stanje.koliz;
    stanje.vri = stanje.vul - stanje.viz;

  }
  private void CalcNc(raFakeStanje stanje){
    if (stanje.kol!=0)
        stanje.nc = (stanje.nabul-stanje.nabiz) / stanje.kol;
  }

  private void setupZC(raFakeStanje stanje){
      if ( stanje.sVrSklad.equals("N"))
        stanje.zc    = stanje.nc;
      else if ( stanje.sVrSklad.equals("V"))
        stanje.zc    = stanje.vc;
      else if ( stanje.sVrSklad.equals("M"))
        stanje.zc    = stanje.mc;
  }
  /**
   * @return  0 = sve OK  -1 = koli\u010Dina manja od zalihe
   *  -2 = ušli smo u signalne koli\u010Dine
   */
  public int TestStanje(raFakeStanje stanje,raFakeStavke stavka ,
                                   raFakeStavke stavkaold){
    if (stanje.kol<0) return -1;
    else if (stanje.kol + stavkaold.kol - stanje.kolrez  < stavka.kol ) return -2;
    return 0;
  }

  public void VratiRezervu(raFakeStanje stanje,raFakeStavke stavka ,
                                   String what_kind_of_document)  {
    if (TD.isDocRezerviraKol(what_kind_of_document)) {
      stanje.kolrez = stanje.kolrez - stavka.kol;
    }
  }

}