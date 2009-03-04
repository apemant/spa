/****license*****************************************************************
**   file: raKalkulBDStanje.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import java.math.BigDecimal;



public class raKalkulBDStanje {
  
  public static final int OK_KOL = 0;
  public static final int NEG_KOL = -1;
  public static final int NEG_KOL_REZ = -2;

  private TypeDoc TD = TypeDoc.getTypeDoc();
  private BigDecimal Nula = Aus.zero2;
  private BigDecimal tmpBD = Aus.zero2;
  private BigDecimal tmpBD2 = Aus.zero2;

  public void kalkStanjefromStdoku(raFakeBDStanje stanje,raFakeBDStdoku stavka ,
                                   raFakeBDStdoku stavkaold,
                                   String what_kind_of_document)
  {
    if (TD.isDocUlaz(what_kind_of_document) && !TD.isDocMeskla(what_kind_of_document))
    {
      if (TD.isDocPST(what_kind_of_document))
      {
//          stanje.kolps = stanje.kolps + stavka.kol  - stavkaold.kol;
        stanje.kolps = (stanje.kolps.add(stavka.kol)).subtract(stavkaold.kol);
//          stanje.nabps = stanje.nabps + stavka.inab - stavkaold.inab;
        stanje.nabps = (stanje.nabps.add(stavka.inab)).subtract(stavkaold.inab);
//        stanje.marps = stanje.marps + stavka.imar - stavkaold.imar;
        stanje.marps = (stanje.marps.add(stavka.imar)).subtract(stavkaold.imar);
//        stanje.porps = stanje.porps + stavka.ipor - stavkaold.ipor;
        stanje.porps = (stanje.porps.add(stavka.ipor)).subtract(stavkaold.ipor);
//        stanje.vps   = stanje.vps   + stavka.izad - stavkaold.izad;
        stanje.vps   = (stanje.vps.add(stavka.izad)).subtract(stavkaold.izad);
      }
//      stanje.kolul = stanje.kolul + stavka.kol  - stavkaold.kol;
      stanje.kolul = (stanje.kolul.add(stavka.kol)).subtract(stavkaold.kol);
//      stanje.nabul = stanje.nabul + stavka.inab - stavkaold.inab;
      stanje.nabul = (stanje.nabul.add(stavka.inab)).subtract(stavkaold.inab);
//      stanje.marul = stanje.marul + stavka.imar - stavkaold.imar +
//                     stavka.diopormar - stavkaold.diopormar;
      stanje.marul = stanje.marul.add(stavka.imar).subtract(stavkaold.imar).
                     add(stavka.diopormar).subtract(stavkaold.diopormar);
//      stanje.porul = stanje.porul + stavka.ipor - stavkaold.ipor +
//                     stavka.dioporpor - stavkaold.dioporpor;
      stanje.porul = stanje.porul.add(stavka.ipor).subtract(stavkaold.ipor).
                     add(stavka.dioporpor).subtract(stavkaold.dioporpor);
//      stanje.vul   = stanje.vul   + stavka.izad - stavkaold.izad +
//                     stavka.porav - stavkaold.porav;
      stanje.vul   = stanje.vul.add(stavka.izad).subtract(stavkaold.izad).
                     add(stavka.porav).subtract(stavkaold.porav);
      stanje.kolskladul = stanje.kolskladul.add(stavka.kol).subtract(stavkaold.kol);
      ClasicCalc(stanje);
      //CalcNc(stanje);  ab.f nema kalkulacije nc-a kod viška
    }
  }

  public void SetupPrice(raFakeBDStanje stanje,raFakeBDStdoku stavka,String what_kind_of_document){
    if (TD.isDocUlaz(what_kind_of_document) && !TD.isDocMeskla(what_kind_of_document)) {
      stanje.vc= stavka.vc ;
      stanje.mc= stavka.mc ;
      setupZC(stanje);
    }
  }

  public void SetupPrice(raFakeBDStanje stanje,raFakeBDStmeskla stavka,String what_kind_of_document){
      stanje.vc= stavka.vc ;
      stanje.mc= stavka.mc ;
      setupZC(stanje);
  }

  public void kalkStanjefromStmesklaUlaz(raFakeBDStanje stanjeul,
                                         raFakeBDStmeskla stavka,
                                         raFakeBDStmeskla stavkaold,boolean touchPrice){

//      stanjeul.kolul   = stanjeul.kolul + stavka.kol    - stavkaold.kol;
      stanjeul.kolul   = stanjeul.kolul.add(stavka.kol).subtract(stavkaold.kol);
//      stanjeul.nabul   = stanjeul.nabul + stavka.inabul - stavkaold.inabul;
      stanjeul.nabul   = stanjeul.nabul.add(stavka.inabul).subtract(stavkaold.inabul);
//      stanjeul.marul   = stanjeul.marul + stavka.imarul - stavkaold.imarul+
//                         stavka.diopormar - stavkaold.diopormar ;
      stanjeul.marul   = stanjeul.marul.add(stavka.imarul).subtract(stavkaold.imarul).
                         add(stavka.diopormar).subtract(stavkaold.diopormar);
//      stanjeul.porul   = stanjeul.porul + stavka.iporul - stavkaold.iporul +
//                         stavka.dioporpor - stavkaold.dioporpor ;
      stanjeul.porul   = stanjeul.porul.add(stavka.iporul).subtract(stavkaold.iporul).
                         add(stavka.dioporpor).subtract(stavkaold.dioporpor);
//      stanjeul.porav   = stanjeul.porav + stavka.porav  - stavkaold.porav;
      stanjeul.porav   = stanjeul.porav.add(stavka.porav).subtract(stavkaold.porav);
//      stanjeul.vul     = stanjeul.vul + stavka.zadrazul + stavka.porav -
//                         stavkaold.zadrazul - stavkaold.porav;
      stanjeul.vul     = stanjeul.vul.add(stavka.zadrazul).add(stavka.porav).
                         subtract(stavkaold.zadrazul).subtract(stavkaold.porav);
      stanjeul.kolskladul = stanjeul.kolskladul.add(stavka.kol).subtract(stavkaold.kol);
      ClasicCalc(stanjeul);
      CalcNc(stanjeul);
      if (touchPrice) {
      stanjeul.vc      = stavka.vc;
      stanjeul.mc      = stavka.mc;
      setupZC(stanjeul);
      }
  }

  public void kalkStanjefromStmesklaIzlaz(raFakeBDStanje stanjeiz,
                                          raFakeBDStmeskla stavka,
                                          raFakeBDStmeskla stavkaold){

//      stanjeiz.koliz = stanjeiz.koliz + stavka.kol      - stavkaold.kol;
      stanjeiz.koliz = stanjeiz.koliz.add(stavka.kol).subtract(stavkaold.kol);
//      stanjeiz.nabiz = stanjeiz.nabiz + stavka.inabiz   - stavkaold.inabiz;
      stanjeiz.nabiz = stanjeiz.nabiz.add(stavka.inabiz).subtract(stavkaold.inabiz);
//      stanjeiz.mariz = stanjeiz.mariz + stavka.imariz   - stavkaold.imariz;
      stanjeiz.mariz = stanjeiz.mariz.add(stavka.imariz).subtract(stavkaold.imariz);
//      stanjeiz.poriz = stanjeiz.poriz + stavka.iporiz   - stavkaold.iporiz;
      stanjeiz.poriz = stanjeiz.poriz.add(stavka.iporiz).subtract(stavkaold.iporiz);
//      stanjeiz.viz   = stanjeiz.viz   + stavka.zadraziz - stavkaold.zadraziz;
      stanjeiz.viz   = stanjeiz.viz.add(stavka.zadraziz).subtract(stavkaold.zadraziz);
      stanjeiz.kolskladiz = stanjeiz.kolskladiz.add(stavka.kol).subtract(stavkaold.kol);
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
  private void testOTR(String what_kind_of_document,String stogledati){
    if (what_kind_of_document.equalsIgnoreCase(stogledati)) {
      System.out.println(stogledati+" je prosao");
    }
  }

  public void kalkStanjefromStdoki(raFakeBDStanje stanje,raFakeBDStdoki stavka ,
                                   raFakeBDStdoki stavkaold,
                                   String what_kind_of_document)  {

    if (TD.isDocStdoki(what_kind_of_document)) {

      if (TD.isDocDiraZalihu(what_kind_of_document)) {
//      stanje.koliz = stanje.koliz + stavka.kol  - stavkaold.kol;
        stanje.koliz = stanje.koliz.add(stavka.kol).subtract(stavkaold.kol);
//      stanje.viz   = stanje.viz   + stavka.iraz - stavkaold.iraz;
        stanje.viz   = stanje.viz.add(stavka.iraz).subtract(stavkaold.iraz);
//      stanje.nabiz = stanje.nabiz + stavka.inab - stavkaold.inab;
        stanje.nabiz = stanje.nabiz.add(stavka.inab).subtract(stavkaold.inab);
//      stanje.mariz = stanje.mariz + stavka.imar - stavkaold.imar;
        stanje.mariz = stanje.mariz.add(stavka.imar).subtract(stavkaold.imar);
//      stanje.poriz = stanje.poriz + stavka.ipor - stavkaold.ipor;
        stanje.poriz = stanje.poriz.add(stavka.ipor).subtract(stavkaold.ipor);
    	stanje.kolskladiz = stanje.kolskladiz.add(stavka.kol).subtract(stavkaold.kol);        
        ClasicCalc(stanje);
      } else if (stavka.rezkol.equalsIgnoreCase("D")) {
        System.out.println("stanje.kolrez "+stanje.kolrez);     
        stanje.kolrez = stanje.kolrez.add(stavka.kol).subtract(stavkaold.kol);
        System.out.println("stanje.kolrez posli"+stanje.kolrez);          
      }
    }
    if(what_kind_of_document.equalsIgnoreCase("DOS")) {
    	System.out.println("radim na DOSu");      	
    	stanje.kolskladiz = stanje.kolskladiz.add(stavka.kol).subtract(stavkaold.kol);
    	addClasicCalc(stanje);
    }
    
    
  }

  private void ClasicCalc(raFakeBDStanje stanje){

    stanje.kol = stanje.kolul.subtract(stanje.koliz);
    stanje.vri = stanje.vul.subtract(stanje.viz);
    addClasicCalc(stanje);

  }
  
  private void addClasicCalc(raFakeBDStanje stanje){
    stanje.kolsklad = stanje.kolskladul.subtract(stanje.kolskladiz);
System.out.println("stanje.kolsklad "+stanje.kolsklad);    
  	
  }
  
  
  private void CalcNc(raFakeBDStanje stanje){
    if (stanje.kol.doubleValue()!=0) {
        tmpBD = stanje.nabul.subtract(stanje.nabiz);
        stanje.nc =  tmpBD.divide(stanje.kol,2,BigDecimal.ROUND_HALF_UP);
    } else if (stanje.kolul.doubleValue()!=0) {
      stanje.nc =  stanje.nabul.divide(stanje.kolul,2,BigDecimal.ROUND_HALF_UP);
    } else {
      stanje.nc =  Nula;
    }
  }

  private void setupZC(raFakeBDStanje stanje){

    if ( stanje.sVrSklad.equals("N")){
      stanje.zc    = stanje.nc;
    } else if ( stanje.sVrSklad.equals("V")) {
      stanje.zc    = stanje.vc;
    } else if ( stanje.sVrSklad.equals("M")) {
      stanje.zc    = stanje.mc;
    }

  }
  /**
   * @return  0 = sve OK  -1 = koli\u010Dina manja od zalihe
   *  -2 = ušli smo u rezervirane koli\u010Dine
   */
  
  
  
  public int TestStanje(raFakeBDStanje stanje,raFakeBDStdoki stavka ,
                                   raFakeBDStdoki stavkaold,String vrdok){
  	
  	if ("DOS".equalsIgnoreCase(vrdok)){
  		if (frmParam.getParam("robno","checkDOS","D","Provjera kolièine na dostavnicama").equalsIgnoreCase("N")){
  			return OK_KOL;
  		}
  		
  	  	tmpBD = stanje.kolsklad.add(stavkaold.kol).subtract(stavka.kol);
  	    tmpBD2 = stanje.kolsklad.add(stavkaold.kol).subtract(stanje.kolrez).subtract(stavka.kol);  	  	
  	    if (tmpBD.compareTo(Nula)==-1) return NEG_KOL;
//  	    else if (tmpBD2.compareTo(Nula)  == -1  ) return -2;
  	    return OK_KOL;
  	}
  	
//    System.out.println("stanje.kol "+stanje.kol);
//    System.out.println("stavka.kol "+stavka.kol);
//    System.out.println("stavkaold.kol "+stavkaold.kol);
  	tmpBD = stanje.kol.add(stavkaold.kol).subtract(stavka.kol);
    tmpBD2 = stanje.kol.add(stavkaold.kol).subtract(stanje.kolrez).subtract(stavka.kol);
//    System.out.println("tmpBD "+tmpBD);    
//    System.out.println("tmpBD2 "+tmpBD2);
//    System.out.println("nula "+Nula);
    if (tmpBD.signum() < 0) return NEG_KOL;
    else if (tmpBD2.signum() < 0) return NEG_KOL_REZ;
    return OK_KOL;
  }

  public int TestStanje(raFakeBDStanje stanje,raFakeBDStmeskla stavka ,
                                   raFakeBDStmeskla stavkaold){
    tmpBD = stanje.kol.add(stavkaold.kol).subtract(stavka.kol);
    tmpBD2 = stanje.kol.add(stavkaold.kol).subtract(stanje.kolrez).subtract(stavka.kol);
    if (tmpBD.signum() < 0) return NEG_KOL;
    else if (tmpBD2.signum() < 0) return NEG_KOL_REZ;
    return OK_KOL;
  }

  public void VratiRezervu(raFakeBDStanje stanje,raFakeBDStdoki stavka ,
                                   String what_kind_of_document)  {
  	System.out.println("Vrati rezervu stavka.rezkol="+stavka.rezkol);
  	 if (stavka.rezkol.equalsIgnoreCase("D")) {
      stanje.kolrez = stanje.kolrez.subtract(stavka.kol);
    }
  }
}
